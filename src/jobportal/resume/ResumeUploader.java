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
            System.out.println("Invalid file.");
            return;
        }

        String resumeContent = "";

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                resumeContent = resumeContent + fileScanner.nextLine() + "\n";
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading file.");
            return;
        }

        String sql = "UPDATE unapplied SET resume_link=? WHERE user_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, resumeContent);
            ps.setInt(2, user.getId());
            ps.executeUpdate();
            System.out.println("Resume uploaded successfully!");
        } catch (SQLException e) {
            System.out.println("Database error.");
        }
    }
}