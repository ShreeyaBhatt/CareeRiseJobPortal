package jobportal.auth;

import jobportal.jobfinder.JobFinder;
import jobportal.jobs.JobManager;
import jobportal.resume.ResumeUploader;
import jobportal.util.CustomQueue;

import java.sql.Connection;
import java.util.Scanner;

public class JobPortalService {

    private final Connection conn;
    private final Scanner sc;
    private final LoginManager loginManager;
    private final JobManager jobManager;
    private final ResumeUploader resumeUploader;
    private final CustomQueue jobQueue;

    public JobPortalService(Connection conn, Scanner sc) {
        this.conn = conn;
        this.sc = sc;
        this.loginManager = new LoginManager(conn);
        this.jobManager = new JobManager(conn);
        this.resumeUploader = new ResumeUploader(conn);

        // Initialize queue
        this.jobQueue = new CustomQueue(100);
    }

    public void start() {

        System.out.println("==== Welcome to CareeRise Job Portal ====");

        while (true) {

            System.out.println("\n1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
                continue;
            }

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {

                case 1:
                    loginManager.register(sc);
                    break;

                case 2:
                    User user = loginManager.login(sc);
                    if (user != null) {
                        userMenu(user);
                    }
                    break;

                case 3:
                    System.out.println("Exiting... Thank you!");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void userMenu(User user) {

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

            if (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
                continue;
            }

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {

                case 1:
                    loginManager.viewProfile(user.getId());
                    break;

                case 2:
                    JobFinder finder = new JobFinder(conn, jobQueue);
                    try {
                        finder.findMatchingJobs(user.getId());
                    } catch (Exception e) {
                        System.out.println("Error fetching jobs.");
                    }
                    break;

                case 3:
                    jobManager.applyJob(sc, user);
                    break;

                case 4:
                    resumeUploader.uploadResume(sc, user);
                    break;

                case 5:
                    System.out.print("Enter minimum salary: ");

                    if (!sc.hasNextDouble()) {
                        System.out.println("Invalid salary amount.");
                        sc.nextLine();
                        break;
                    }

                    double minSalary = sc.nextDouble();
                    sc.nextLine();
                    jobManager.filterCompaniesBySalary(minSalary);
                    break;

                case 6:
                    jobManager.displayJobsWithCompetition();
                    break;

                case 7:
                    loginManager.logout(user);  // ✅ Proper logout
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}