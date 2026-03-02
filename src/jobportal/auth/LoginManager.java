package jobportal.auth;

import jobportal.exceptions.*;

import java.sql.*;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;

public class LoginManager {

    private final Connection conn;
    private static final HashSet<String> activeSessions = new HashSet<>();

    public LoginManager(Connection conn) {
        this.conn = conn;
    }

    // ================= REGISTER =================
    public void register(Scanner sc) {
        try {
            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            System.out.print("Enter Phone: ");
            String phone = sc.nextLine();

            System.out.print("Enter Location: ");
            String location = sc.nextLine();

            System.out.print("Enter Resume File Path: ");
            String resumePath = sc.nextLine();

            String resumeContent = "";

            try (Scanner fileScanner = new Scanner(new File(resumePath))) {
                while (fileScanner.hasNextLine()) {
                    resumeContent = resumeContent + fileScanner.nextLine() + "\n";
                }
            } catch (Exception e) {
                System.out.println("Error reading resume file.");
                return;
            }

            String sql = "INSERT INTO unapplied(name, email_id, phone_number, location, resume_link) VALUES(?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, location);
            ps.setString(5, resumeContent);

            ps.executeUpdate();
            ps.close();

            System.out.println("User registered successfully!");

        } catch (SQLException e) {
            System.out.println("Database error.");
        }
    }
    public void viewProfile(int userId) {

    CallableStatement cs = null;
    ResultSet rs = null;

    try {
        String sql = "{CALL ViewProfile(?)}";
        cs = conn.prepareCall(sql);
        cs.setInt(1, userId);

        rs = cs.executeQuery();

        if (rs.next()) {

            System.out.println("\n=== USER PROFILE ===");
            System.out.println("User ID: " + rs.getInt("user_id"));
            System.out.println("Name: " + rs.getString("name"));
            System.out.println("Email: " + rs.getString("email_id"));
            System.out.println("Phone: " + rs.getString("phone_number"));
            System.out.println("Location: " + rs.getString("location"));

            Clob resumeClob = rs.getClob("resume_link");

            if (resumeClob != null) {
                long length = resumeClob.length();
                if (length > 0) {
                    String resumeContent = resumeClob.getSubString(1, (int) length);

                    System.out.println("Resume Content:");
                    System.out.println("------------------");

                    if (resumeContent.length() > 300) {
                        System.out.println(resumeContent.substring(0, 300) + "...");
                    } else {
                        System.out.println(resumeContent);
                    }

                    System.out.println("------------------");
                } else {
                    System.out.println("Resume is empty.");
                }
            } else {
                System.out.println("No resume uploaded.");
            }

            System.out.println("=====================");

        } else {
            System.out.println("Profile not found.");
        }

    } catch (SQLException e) {
        System.out.println("Error viewing profile: " + e.getMessage());
    } finally {
        try {
            if (rs != null) rs.close();
            if (cs != null) cs.close();
        } catch (SQLException e) {
            System.out.println("Error closing resources.");
        }
    }
}

    // ================= LOGIN =================
    public User login(Scanner sc) {

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Phone: ");
        String phone = sc.nextLine();

        try {

            if (!email.contains("@") || !email.contains(".com")) {
                throw new InputMismatchException("Invalid email format!");
            }

            if (phone.length() != 10) {
                throw new InputMismatchException("Phone must be 10 digits.");
            }

            // Manual digit check
            for (int i = 0; i < phone.length(); i++) {
                char ch = phone.charAt(i);
                if (ch < '0' || ch > '9') {
                    throw new InputMismatchException("Phone must contain only digits.");
                }
            }

            User user = authenticate(email, phone);

            if (activeSessions.contains(email)) {
                throw new MultipleLoginException("User already logged in.");
            }

            activeSessions.add(email);
            System.out.println("Login successful! Welcome " + user.getName());
            return user;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    private User authenticate(String email, String phone)
            throws SQLException, UserNotFoundException {

        String sql = "SELECT * FROM unapplied WHERE email_id = ? AND phone_number = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);
        ps.setString(2, phone);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            String resumeContent = "";
            Clob resumeClob = rs.getClob("resume_link");

            if (resumeClob != null) {
                long length = resumeClob.length();
                if (length > 0) {
                    resumeContent = resumeClob.getSubString(1, (int) length);
                }
            }

            User user = new User(
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    email,
                    phone,
                    resumeContent
            );

            rs.close();
            ps.close();
            return user;

        } else {
            rs.close();
            ps.close();
            throw new UserNotFoundException("Invalid credentials.");
        }
    }
    public void logout(User user) {
    if (user != null) {
        activeSessions.remove(user.getEmail());
        System.out.println("User " + user.getName() + " logged out successfully.");
    }
}
}