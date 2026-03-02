package jobportal;

import jobportal.auth.JobPortalService;

import java.sql.*;
import java.util.Scanner;

public class JobPortalMain {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/jobportal", "root", "")) {
            System.out.println("Connected to Database!");
            JobPortalService service = new JobPortalService(conn, sc);
            service.start();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            sc.close();
        }
    }
}
