# 💰 Expense Tracker API

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-Database-blue)
![Maven](https://img.shields.io/badge/Maven-Build-red)
![JWT](https://img.shields.io/badge/JWT-Authentication-yellow)
![License](https://img.shields.io/badge/License-MIT-green)

## 📌 Overview

Expense Tracker API is a backend application developed using **Spring Boot** that enables users to securely manage their personal finances through RESTful APIs.

The application includes secure authentication, income and expense management, budgeting, category management, reporting, and API documentation. It is designed using a clean layered architecture and follows modern backend development practices.

> **Note:** This repository currently contains the backend implementation. A React frontend is planned for a future release.


## ⭐ Project Highlights

- 🔐 Secure JWT Authentication & Authorization
- ⚡ RESTful API built with Spring Boot 3.5
- 🗄️ MySQL database with Flyway migrations
- 📖 Interactive API documentation using Swagger/OpenAPI
- 🛡️ Spring Security for protected endpoints
- 📊 Budget, Expense, Income & Report modules
- 📦 Clean layered architecture (Controller → Service → Repository)
- ☕ Built with Java 21 and Maven

---

# 🚀 Features
## 📋 Features Overview

| Module | Status |
|----------|--------|
| 🔐 JWT Authentication | ✅ Complete |
| 👤 User Management | ✅ Complete |
| 💸 Expense Management | ✅ Complete |
| 💰 Income Management | ✅ Complete |
| 🏷 Category Management | ✅ Complete |
| 📊 Budget Management | ✅ Complete |
| 📈 Reports | ✅ Complete |
| 📄 CSV Export | ✅ Complete |
| 📑 PDF Export | ✅ Complete |
| 📚 Swagger Documentation | ✅ Complete |
| 🛡 Spring Security | ✅ Complete |
| 🗄 MySQL Integration | ✅ Complete |
| 🚧 React Frontend | Planned |
| 🚀 Cloud Deployment | Planned |

## 🔐 Authentication

- User Registration
- Secure Login
- JWT Authentication
- Refresh Token
- Logout
- Password Encryption

---

## 💸 Expense Management

- Add Expense
- Update Expense
- Delete Expense
- View Expenses
- Pagination
- Search
- Filters
- Category Support

---

## 💰 Income Management

- Add Income
- Update Income
- Delete Income
- View Income

---

## 📊 Dashboard

- Current Balance
- Total Income
- Total Expense
- Monthly Summary
- Budget Overview

---

## 📈 Reports

- Financial Summary
- CSV Export
- PDF Export

---

## 🗂 Category Management

- Create Categories
- Update Categories
- Delete Categories
- Safe Delete Validation

---

## 🛡 Security

- Spring Security
- JWT Authentication
- Protected APIs
- Password Encryption
- User-specific Data Isolation

---

# 🛠 Tech Stack

# 🛠 Tech Stack

| Category | Technologies |
|----------|--------------|
| **Language** | Java 21 |
| **Framework** | Spring Boot 3.5 |
| **Security** | Spring Security, JWT |
| **Database** | MySQL |
| **ORM** | Hibernate, Spring Data JPA |
| **Database Migration** | Flyway |
| **API Documentation** | Swagger / OpenAPI |
| **Build Tool** | Maven |
| **Version Control** | Git & GitHub |
---

# 📷 Screenshots

### Swagger API

![Swagger API](screenshots/swagger-home.png)

---

# 🏗 Project Architecture

```
Client (React / Mobile / Swagger)

↓

Spring Boot REST API

↓

Controllers

↓

Services

↓

Repositories

↓

MySQL Database
```

---

# 🏗️ System Architecture

```text
                Client
      (Swagger / React Frontend)

                    │
                    ▼

         Spring Boot REST API

                    │
                    ▼

             Controller Layer

                    │
                    ▼

              Service Layer

                    │
                    ▼

           Repository Layer

                    │
                    ▼

             MySQL Database
```

### Request Flow

```
Client
   ↓
REST API
   ↓
Controller
   ↓
Service
   ↓
Repository
   ↓
MySQL
```

# 📡 API Documentation

Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

---

# ⚙️ Installation

Clone the repository

```bash
git clone https://github.com/namandeeptripathi/Expense-Tracker.git
```

Go to the project

```bash
cd Expense-Tracker
```

Run the application

```bash
mvn spring-boot:run
```

---

# 📂 Project Structure

```
# 📂 Project Structure

```text
Expense-Tracker
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.namandeep.expensetracker
│   │   │       ├── config
│   │   │       ├── controller
│   │   │       ├── dto
│   │   │       ├── entity
│   │   │       ├── exception
│   │   │       ├── repository
│   │   │       ├── security
│   │   │       ├── service
│   │   │       ├── util
│   │   │       └── ExpenseTrackerApplication.java
│   │   └── resources
│   │
│   └── test
│
├── screenshots
├── README.md
├── LICENSE
├── pom.xml
└── .gitignore
```
```

---

# 📚 Key Learning Outcomes

Building this project helped me gain practical experience in:

- REST API Development using Spring Boot
- Layered Architecture (Controller → Service → Repository)
- JWT Authentication & Spring Security
- Database Design with MySQL
- Database Versioning using Flyway
- Hibernate & Spring Data JPA
- API Documentation using Swagger/OpenAPI
- Exception Handling & Validation
- Git & GitHub Workflow
- Maven Project Management


# 🔮 Future Improvements

- React Frontend
- Dashboard Charts
- Budget Analytics
- Notifications
- Docker
- Cloud Deployment
- Mobile App

---

# 🚧 Project Status

Current Status: **Backend Completed ✅**

This project currently provides a production-style REST API built with Spring Boot.

### Completed

- ✅ JWT Authentication
- ✅ User Management
- ✅ Expense Management
- ✅ Income Management
- ✅ Category Management
- ✅ Budget Management
- ✅ Reports
- ✅ Swagger Documentation
- ✅ Flyway Database Migration
- ✅ MySQL Integration

### Planned

- 🔜 React Frontend
- 🔜 Interactive Dashboard
- 🔜 Charts & Analytics
- 🔜 Docker Support
- 🔜 Cloud Deployment

# 👨‍💻 Author

**Naman Deep Tripathi**

Java Backend Developer

GitHub:
https://github.com/namandeeptripathi

---

⭐ If you like this project, consider giving it a star.
