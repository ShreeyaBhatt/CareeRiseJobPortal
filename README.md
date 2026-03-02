# CareeRisePortal

A **Java-based Job Portal System** for managing user registrations, resume uploads, and job applications. This console-based application allows users to register, log in, upload resumes, view jobs, filter companies by salary, and apply for jobs while tracking competition.

---

## **Features**

- **User Authentication**  
  - Register new users  
  - Login/logout with multiple session prevention  

- **Profile Management**  
  - View user profile details including resume preview  

- **Job Management**  
  - Display all jobs or only eligible jobs  
  - Apply for jobs  
  - Filter companies by minimum salary  
  - Display jobs with applicant competition ranking  

- **Resume Handling**  
  - Upload resumes to the portal  
  - Resume content stored in database for skill matching  

- **Queue Management**  
  - Internal job queue to handle available jobs efficiently  

---

## **Project Structure**
CareeRisePortal/
│
├─ src/
│ ├─ jobportal/
│ │ ├─ auth/ # User and login management
│ │ ├─ jobs/ # Job management classes
│ │ ├─ resume/ # Resume upload handling
│ │ └─ util/ # Utility classes (queues, etc.)
│
├─ README.md
└─ .gitignore

---

## **Prerequisites**

- Java 8 or higher  
- JDBC-enabled database (e.g., MySQL, PostgreSQL)  
- Optional: IDE like IntelliJ IDEA or Eclipse  

---
