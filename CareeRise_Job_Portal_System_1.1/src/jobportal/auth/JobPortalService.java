package jobportal.auth;   // Fixed package declaration

import jobportal.jobfinder.JobFinder;
import jobportal.jobs.JobManager;
import jobportal.resume.ResumeUploader;
import jobportal.util.CustomQueue; // your queue

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class JobPortalService {
    private final Connection conn;
    private final Scanner sc;
    private final LoginManager loginManager;
    private final JobManager jobManager;
    private final ResumeUploader resumeUploader;

    // maintain a jobQueue here
    private CustomQueue jobQueue;

    public JobPortalService(Connection conn, Scanner sc) {
        this.conn = conn;
        this.sc   = sc;
        this.loginManager   = new LoginManager(conn);
        this.jobManager     = new JobManager(conn);
        this.resumeUploader = new ResumeUploader(conn);

        // FIX: Initialize queue with a proper capacity (you can customize this value)
        int capacity = 100; // example capacity
        this.jobQueue = new CustomQueue(capacity); // pass the integer, not the queue itself
    }

    public void start() throws SQLException {
        System.out.println("==== Welcome to CareeRise Job Portal ====");
        while (true) {
            System.out.println("\n1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    loginManager.register(sc);
                    break;

                case 2: {
                    User user = loginManager.login(sc);
                    if (user != null) {
                        userMenu(user);
                    }
                    break;
                }

                case 3:
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }

    private void userMenu(User user) throws SQLException {
        while (true) {
            System.out.println("\n--- CareeRise User Menu ---");
            System.out.println("1. View Profile");
            System.out.println("2. View Applicable Jobs");
            System.out.println("3. Apply Job");
            System.out.println("4. Upload Resume");
            System.out.println("5. Filter Companies by Salary");
            System.out.println("6. View Jobs by Competition");
            System.out.println("7. Logout");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    loginManager.viewProfile(user.getId());
                    break;

                case 2: {
                    // FIX: Use JobFinder with proper userId
                    JobFinder finder = new JobFinder(conn, jobQueue);
                    finder.findMatchingJobs(user.getId());
                    break;
                }

                case 3:
                    jobManager.applyJob(sc, user);
                    break;

                case 4:
                    resumeUploader.uploadResume(sc, user);
                    break;

                case 5: {
                    System.out.print("Enter minimum salary: ");
                    double minSalary = sc.nextDouble();
                    sc.nextLine(); // consume newline
                    jobManager.filterCompaniesBySalary(minSalary);
                    break;
                }

                case 6:
                    jobManager.displayJobsWithCompetition();
                    break;

                case 7:
                    System.out.println("Logging out...");
                    return;

                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }
}
