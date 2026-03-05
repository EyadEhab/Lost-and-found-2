# 🕵️ Lost and Found Management System

Welcome to the **Lost and Found Management System**! This is a robust Java-based application designed to help organizations manage lost and found items efficiently. Featuring a modern Swing UI with dynamic theme support (Light/Dark), it provides distinct roles for Admins, Officers, and Students.

---

## 🚀 Getting Started

### 📋 Prerequisites

Before you begin, ensure you have the following installed on your machine:

1.  **Java Development Kit (JDK):** Version 11 or higher.
2.  **Microsoft SQL Server:** Ensure it's running locally on port.
3.  **IDE:** [NetBeans](https://netbeans.apache.org/front/main/index.html) is highly recommended for building and running if u dont have it u dont have to its not a must.
4.  **SQL Server JDBC Driver:** Included in the project (`mssql-jdbc-13.2.1.jre11.jar`).

---

## 🛠️ Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/EyadEhab/Lost-and-found-2.git
cd Lost-and-found-2
```

### 2. Database Initialization
This project uses **Microsoft SQL Server**. Follow these steps to set up the database:

1.  Open **SQL Server Management Studio (SSMS)**.
2.  Restore the database from the backup file:
    *   Navigate to the `DATABASE/` directory.
    *   Locate `LostAndFoundDB.bak`.
    *   Right-click on "Databases" in SSMS -> **Restore Database...** In the "Source" section, select Device choose **Device** -> Select `LostAndFoundDB.bak`.
    *   In the restore window, go to the Files page (left menu) -> Check "Relocate all files to folder" and make sure the paths are valid
    *   Go to Options page -> Check "Overwrite the existing database (WITH REPLACE)"
3.  Ensure your SQL Server instance allows **SQL Server Authentication** (Mixed Mode).


### 3. Configuration
The application requires a `db.properties` file to connect to your database.

1.  Navigate to `Master dcd java/`.
2.  Duplicate `db.properties.example` and rename it to `db.properties`.
3.  Update the credentials with your SQL Server details:
    *   **Option 1: SQL Server Authentication** (Recommended)
        ```properties
        db.url=jdbc:sqlserver://localhost:3306;databaseName=LostAndFoundDB;encrypt=false;
        db.user=your_username
        db.pass=your_password
        ```
    *   **Option 2: Windows Authentication**
        If you prefer to use your Windows credentials, follow these extra steps:
        1.  Locate `Master dcd java/sqljdbc_13.2.1.0_enu/sqljdbc_13.2/enu/auth/x64/mssql-jdbc_auth-13.2.1.x64.dll`.
        2.  Copy this `.dll` file directly into your **IDE's project root** or Java's `bin` folder (alternatively, ensure it's in your system `PATH`).
        3.  Set your `db.properties` as follows (User and Pass are ignored):
            ```properties
            db.url=jdbc:sqlserver://localhost:3306;databaseName=LostAndFoundDB;integratedSecurity=true;encrypt=false;
            ```

---

## 💻 Running the Application

### Option A: Using Any IDE
1.  Open NetBeans.
2.  Select **File > Open Project**.
3.  Navigate to and select the `Master dcd java` folder.
4.  Right-click on the project and select **Run**.
5.  Wait for the application to build; the **Login Window** should appear.

### Option B: Using Command Line (Ant)
If you have [Apache Ant](https://ant.apache.org/) installed:
```bash
cd "Master dcd java"
ant run
```

---

## ✨ Features
*   **Role-Based Access:** Specialized dashboards for Admins, Officers, and Students.
*   **Dynamic Theme System:** Seamlessly switch between **Light Mode** and **Dark Mode** via the `ThemeManager`.
*   **Search & Filter:** Advanced searching for lost/found items with status indicators.
*   **Reporting:** Visual statistics for items claimed vs. found.
*   **User Management:** Admin panel for adding, deleting, and updating user roles.

---

## 📂 Project Structure
*   `src/Boundary`: Contains the GUI code (Swing JFrames/Panels).
*   `src/dataaccess`: Contains `DBConnection.java` and data retrieval logic.
*   `src/master/dcd`: The main entry point (`MasterDcd.java`).
*   `src/factory/ui`: UI Factory classes for theme management.
*   `DATABASE/`: Contains DB backup files and scripts.
*   `lib/`: External JAR dependencies.

---

## 🤝 Contributing
*   Create a new branch: `feature/your-feature-name`.  /git add .
*   Commit your changes with descriptive messages.     /git commit -m "commit message"
*   Push to your branch and open a Pull Request.       /git push  , /git pull

---

## 📜 License
This project is developed for educational purposes as part of the **Software Engineering** course.

---
*Maintained by the Software Development Team at CIS.*
