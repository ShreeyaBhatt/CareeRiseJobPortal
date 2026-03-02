package jobportal.resume;

import jobportal.auth.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class ResumeUploader {
    private final Connection conn;

    public ResumeUploader(Connection conn) {
        this.conn = conn;
    }

    public void uploadResume(Scanner sc, User user) {
        System.out.print("Enter Resume File Path: ");
        String filePath = sc.nextLine();
        File file = new File(filePath);

        if (!file.exists() || !file.isFile()) {
            System.out.println("File does not exist or is not a valid file.");
            return;
        }

        // Read resume file safely
        String resumeContent = "";
        try (Scanner fileScanner = new Scanner(file)) {
            StringBuilder sb = new StringBuilder();
            while (fileScanner.hasNextLine()) {
                sb.append(fileScanner.nextLine()).append("\n");
            }
            resumeContent = sb.toString();
        } catch (FileNotFoundException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }

        // Update resume in database safely
        String sql = "UPDATE unapplied SET resume_link=? WHERE user_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, resumeContent);
            ps.setInt(2, user.getId());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Resume uploaded successfully!");
            } else {
                System.out.println("Failed to update resume. User not found.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}