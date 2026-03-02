package jobportal.jobfinder;

import jobportal.jobs.Job;
import jobportal.util.CustomQueue;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.*;
import java.util.*;

//JobFinder class searches and displays jobs matching user's resume criteria.

public class JobFinder {

    private final Connection conn;
    private final CustomQueue jobQueue;

    public JobFinder(Connection conn, CustomQueue jobQueue) {
        this.conn = conn;
        this.jobQueue = jobQueue;
    }

    public void findMatchingJobs(int userId) throws SQLException {
        // Clear existing jobs from queue first
        while (!jobQueue.isEmpty()) {
            jobQueue.dequeue();
        }

        // Read user's resume from database
        String resumeText = readResumeFromDB(userId);
        if (resumeText == null || resumeText.isEmpty()) {
            System.out.println("No resume found for user ID: " + userId);
            return;
        }

        // Convert resume text to lowercase for case-insensitive matching
        String resumeLower = resumeText.toLowerCase();

        // Tokenize resume into words for accurate whole-word matching
        Set<String> resumeWords = new HashSet<>(
                Arrays.asList(resumeLower.split("\\W+"))
        );

        String sql = "SELECT company_id, company_name, criteria, location, salary_offered FROM jobs";
        Set<Integer> matchedCompanyIds = new HashSet<>();

        // Execute query and process each job
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String criteria = rs.getString("criteria");
                if (criteria == null || criteria.isEmpty()) continue;

                // Split criteria on commas, check each part against resume words
                String[] parts = criteria.toLowerCase().split(",");
                boolean allFound = true;
                for (String p : parts) {
                    String part = p.trim();
                    if (!resumeWords.contains(part)) {
                        allFound = false;
                        break;
                    }
                }

                // If all criteria matched, enqueue the job
                if (allFound) {
                    int companyId = rs.getInt("company_id");
                    if (!matchedCompanyIds.contains(companyId)) {
                        Job job = new Job(
                                companyId,
                                rs.getString("company_name"),
                                criteria,
                                rs.getString("location"),
                                rs.getDouble("salary_offered")
                        );
                        jobQueue.enqueue(job);
                        matchedCompanyIds.add(companyId);
                    }
                }
            }
        }

        // Display matching jobs or "no matches found"
        if (jobQueue.isEmpty()) {
            System.out.println("No matching jobs found for your resume.");
        } else {
            System.out.println("\n--- Matching Jobs Based on Resume ---");
            CustomQueue tempQueue = new CustomQueue(jobQueue.getCapacity());

            // Print and temporarily hold jobs
            while (!jobQueue.isEmpty()) {
                Job job = jobQueue.dequeue();
                System.out.println(job);
                tempQueue.enqueue(job);
            }

            // Refill original queue
            while (!tempQueue.isEmpty()) {
                jobQueue.enqueue(tempQueue.dequeue());
            }
        }
    }

    // Helper method to read resume text (CLOB) from database for user
    // Without using StringBuilder (using String concatenation)
    private String readResumeFromDB(int userId) throws SQLException {
        String sql = "SELECT resume_link FROM unapplied WHERE user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Clob clob = rs.getClob("resume_link");
                    if (clob == null) {
                        return "";
                    }

                    try (Reader reader = clob.getCharacterStream();
                         BufferedReader br = new BufferedReader(reader)) {
                        String resumeText = "";
                        String line;
                        while ((line = br.readLine()) != null) {
                            // Accumulate lines without StringBuilder
                            resumeText = resumeText + line + "\n";
                        }
                        return resumeText;
                    } catch (Exception e) {
                        System.out.println("Error reading resume content: " + e.getMessage());
                        return "";
                    }
                }
            }
        }
        return "";
    }
}
