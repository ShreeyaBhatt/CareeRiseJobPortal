<div align="center">

# 🚀 CareeRisePortal

<img src="https://readme-typing-svg.demolab.com?font=Poppins&weight=600&size=26&pause=1000&color=3B82F6&center=true&vCenter=true&width=650&lines=Java-Based+Job+Portal+System;Register+%7C+Upload+%7C+Apply+%7C+Track;Console-Based+%26+Lightweight;Built+with+Core+Java+%2B+JDBC" alt="Typing SVG" />

<br><br>

<img src="https://img.shields.io/badge/Java-8%2B-orange?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 8+" />
<img src="https://img.shields.io/badge/Type-Console%20App-blue?style=for-the-badge" alt="Console App" />
<img src="https://img.shields.io/badge/Database-JDBC-336791?style=for-the-badge&logo=postgresql&logoColor=white" alt="JDBC Database" />
<img src="https://img.shields.io/badge/PRs-Welcome-brightgreen?style=for-the-badge" alt="PRs Welcome" />

</div>

---

## 📑 Table of Contents

- [About](#-about)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Prerequisites](#-prerequisites)
- [Getting Started](#-getting-started)
- [Contributing](#-contributing)
- [Author](#-author)

---

## 📖 About

**CareeRisePortal** is a **Java-based Job Portal System** for managing user registrations, resume uploads, and job applications. This console-based application allows users to register, log in, upload resumes, view jobs, filter companies by salary, and apply for jobs while tracking competition.

---

## ✨ Features

<details open>
<summary><b>🔐 User Authentication</b></summary>
<br>

- Register new users
- Login/logout with multiple session prevention

</details>

<details open>
<summary><b>👤 Profile Management</b></summary>
<br>

- View user profile details including resume preview

</details>

<details open>
<summary><b>💼 Job Management</b></summary>
<br>

- Display all jobs or only eligible jobs
- Apply for jobs
- Filter companies by minimum salary
- Display jobs with applicant competition ranking

</details>

<details open>
<summary><b>📄 Resume Handling</b></summary>
<br>

- Upload resumes to the portal
- Resume content stored in database for skill matching

</details>

<details open>
<summary><b>🗂️ Queue Management</b></summary>
<br>

- Internal job queue to handle available jobs efficiently

</details>

---

## 🛠️ Tech Stack

<p align="center">
<img src="https://skillicons.dev/icons?i=java,mysql,idea,eclipse,git,github" alt="Tech stack icons" />
</p>

---

## 📂 Project Structure

<details>
<summary>Click to expand folder tree</summary>

```text
CareeRisePortal/
│
├─ src/
│  ├─ jobportal/
│  │  ├─ auth/      # User and login management
│  │  ├─ jobs/      # Job management classes
│  │  ├─ resume/    # Resume upload handling
│  │  └─ util/      # Utility classes (queues, etc.)
│
├─ README.md
└─ .gitignore
```

</details>

---

## ⚙️ Prerequisites

- Java 8 or higher
- JDBC-enabled database (e.g., MySQL, PostgreSQL)
- Optional: IDE like IntelliJ IDEA or Eclipse

---

## 🚦 Getting Started

### Clone the Repository
```bash
git clone https://github.com/<your-username>/CareeRisePortal.git
```

### Navigate to the Project
```bash
cd CareeRisePortal
```

### Configure the Database
Update your JDBC connection details (URL, username, password) in the relevant config/util class before running.

### Compile
```bash
javac -d bin -sourcepath src src/jobportal/**/*.java
```

### Run
```bash
java -cp bin jobportal.Main
```
*(Adjust the main class path above to match your actual entry-point class.)*

---

## 🤝 Contributing

Contributions are always welcome!

```text
Fork 🍴
   ↓
Clone 📥
   ↓
Create Branch 🌱
   ↓
Commit Changes 💻
   ↓
Push 🚀
   ↓
Pull Request 🎉
```

---

## 👩‍💻 Author

<div align="center">

## Shreeya Bhatt
Computer Science & Technology Student

<a href="https://github.com/ShreeyaBhatt">
<img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github" alt="GitHub profile" />
</a>

</div>

---

<div align="center">

### ⭐ If you find this project useful, consider giving it a star!

</div>
