package jobportal.jobs;

import jobportal.auth.User;
import jobportal.exceptions.InvalidDataException;
import jobportal.exceptions.UserNotFoundException;
import jobportal.util.CustomQueue;

import java.sql.*;
import java.util.Scanner;

public class JobManager {
    private final Connection conn;
    private final CustomQueue jobQueue;

    // ===== Constructor =====
    public JobManager(Connection conn) {
        this.conn = conn;
        int defaultCapacity = 100; // default queue capacity
        this.jobQueue = new CustomQueue(defaultCapacity);
    }

    public CustomQueue getJobQueue() {
        return jobQueue;
    }

    // ===== Display all jobs (eligible or all) =====
    public void displayApplicableJobs(User user) {
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            rs = st.executeQuery("SELECT * FROM jobs");

            // manually clear queue
            while (!jobQueue.isEmpty()) jobQueue.dequeue();

            System.out.println("\n--- Available Jobs ---");
            boolean foundJobs = false;
            int totalJobs = 0;

            while (rs.next()) {
                totalJobs++;
                Job job = new Job(
                        rs.getInt("company_id"),
                        rs.getString("company_name"),
                        rs.getString("criteria"),
                        rs.getString("location"),
                        rs.getDouble("salary_offered")
                );

                foundJobs = true;
                jobQueue.enqueue(job);
                System.out.println("✓ " + job);
            }

            if (!foundJobs) {
                System.out.println("No jobs found.");
            } else {
                System.out.println("Showing " + totalJobs + " jobs in total.");
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving jobs: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    // ===== Matching skills from resume and criteria =====
  public String[] getMatchingSkills(String resumeText, String criteria) {
    if (resumeText == null || criteria == null) return new String[0];

    String resumeLower = resumeText.toLowerCase();
    String[] keywords = criteria.toLowerCase().split(",");
    
    // Count matching keywords first
    int matchCount = 0;
    for (String keyword : keywords) {
        keyword = keyword.trim();
        if (keyword.length() > 2 && resumeLower.contains(keyword)) {
            matchCount++;
        }
    }

    if (matchCount == 0) return new String[0];

    // Store matches in array
    String[] matches = new String[matchCount];
    int index = 0;
    for (String keyword : keywords) {
        keyword = keyword.trim();
        if (keyword.length() > 2 && resumeLower.contains(keyword)) {
            matches[index++] = keyword;
        }
    }

    return matches;
}

    // ===== Display all jobs (backward compatibility) =====
    public void displayJobs(User user) {
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            rs = st.executeQuery("SELECT * FROM jobs");

            // manually clear queue
            while (!jobQueue.isEmpty()) jobQueue.dequeue();

            boolean found = false;
            while (rs.next()) {
                found = true;
                Job job = new Job(
                        rs.getInt("company_id"),
                        rs.getString("company_name"),
                        rs.getString("criteria"),
                        rs.getString("location"),
                        rs.getDouble("salary_offered")
                );
                jobQueue.enqueue(job);
            }

            if (!found) throw new UserNotFoundException("No jobs found.");

        } catch (SQLException | UserNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    // ===== Apply to a job =====
    public void applyJob(Scanner sc, User user) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement checkPs = null;
        ResultSet checkRs = null;

        try {
            System.out.print("Enter Company ID to apply: ");
            if (!sc.hasNextInt()) {
                sc.nextLine();
                throw new InvalidDataException("Company ID must be a number.");
            }
            int companyId = sc.nextInt();
            sc.nextLine();

            // Check if job exists
            String sql = "SELECT * FROM jobs WHERE company_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, companyId);
            rs = ps.executeQuery();

            if (!rs.next()) {
                throw new InvalidDataException("Invalid Company ID.");
            }

            // Check if already applied
            String checkApplied = "SELECT COUNT(*) FROM applied WHERE company_id = ? AND user_id = ?";
            checkPs = conn.prepareStatement(checkApplied);
            checkPs.setInt(1, companyId);
            checkPs.setInt(2, user.getId());
            checkRs = checkPs.executeQuery();

            if (checkRs.next() && checkRs.getInt(1) > 0) {
                System.out.println("You have already applied for this job.");
                return;
            }

            // Submit application
            String insertSQL = "INSERT INTO applied(company_id, user_id, jobs_selected) VALUES(?,?,?)";
            try (PreparedStatement insertPs = conn.prepareStatement(insertSQL)) {
                insertPs.setInt(1, companyId);
                insertPs.setInt(2, user.getId());
                insertPs.setString(3, "Applied");
                insertPs.executeUpdate();
            }

            System.out.println("✅ Job application submitted successfully!");

        } catch (InvalidDataException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (checkRs != null) checkRs.close();
                if (checkPs != null) checkPs.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    // ===== Filter companies by salary =====
    public void filterCompaniesBySalary(double minSalary) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT company_name, salary_offered FROM jobs WHERE salary_offered > ?";
            ps = conn.prepareStatement(sql);
            ps.setDouble(1, minSalary);
            rs = ps.executeQuery();

            System.out.println("\n--- Companies Offering Salary > ₹" + minSalary + " ---");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(rs.getString("company_name") + " | ₹" + rs.getDouble("salary_offered"));
            }
            if (!found) System.out.println("No companies found with salary above ₹" + minSalary);

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    // ===== Display jobs with competition =====
    public void displayJobsWithCompetition() {
        Statement st = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT j.company_id, j.company_name, j.criteria, j.location, j.salary_offered, " +
                    "COUNT(e.user_id) AS applicant_count " +
                    "FROM jobs j " +
                    "LEFT JOIN applied e ON j.company_id = e.company_id " +
                    "GROUP BY j.company_id, j.company_name, j.criteria, j.location, j.salary_offered " +
                    "ORDER BY applicant_count DESC";

            st = conn.createStatement();
            rs = st.executeQuery(sql);

            System.out.println("\n--- Jobs with Applicant Count (Highest Competition First) ---");
            while (rs.next()) {
                System.out.println(
                        rs.getInt("company_id") + " | " +
                        rs.getString("company_name") + " | " +
                        rs.getString("location") + " | ₹" +
                        rs.getDouble("salary_offered") + " | " +
                        rs.getString("criteria") + " | Applicants: " +
                        rs.getInt("applicant_count")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}