# Resource Booking System

A RESTful Resource Booking System built using **Java, Spring Boot, Spring Security, JWT, JPA/Hibernate, MySQL, Maven, and Postman**.

---

## 🛠️ Tech Stack

- Java 17+
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA / Hibernate
- MySQL
- Maven
- Postman
- Git & GitHub

---

# 🚀 Setup & Installation

## Step 1: Clone the GitHub Repository

Check Git installation:

```powershell
git --version
```

Clone the repository:

```powershell
git clone <YOUR_GITHUB_REPOSITORY_URL>
```

Example:

```powershell
git clone https://github.com/username/resource-booking-system.git
```

Go inside the project:

```powershell
cd resource-booking-system
```

Check project files:

```powershell
dir
```

You should see:

```text
src
pom.xml
.gitignore
README.md
```

---

# Step 2: Open Project

### VS Code

```powershell
code .
```

### IntelliJ IDEA

```powershell
idea .
```

Or open the project manually using VS Code/IntelliJ.

---

# Step 3: Create MySQL Database

Open MySQL Workbench or MySQL Command Line.

Run:

```sql
CREATE DATABASE resource_booking_db;
```

Check database:

```sql
SHOW DATABASES;
```

---

# Step 4: Configure Environment Variables

Open **PowerShell** inside the project directory.

Run:

```powershell
$env:DB_USERNAME="root"
$env:DB_PASSWORD="1204"
$env:DB_URL="jdbc:mysql://localhost:3306/resource_booking_db"
$env:JWT_SECRET="resource-booking-system-jwt-secret-2026-secure-key"
$env:JWT_EXPIRATION="3600000"
```

Verify:

```powershell
echo $env:DB_USERNAME
echo $env:DB_URL
echo $env:JWT_EXPIRATION
```

Expected:

```text
root
jdbc:mysql://localhost:3306/resource_booking_db
3600000
```

> These environment variables are available only in the current PowerShell session.

---

# Step 5: Start Spring Boot Application

Run:

```powershell
mvn spring-boot:run
```

The application should start at:

```text
http://localhost:8080
```

You should see something similar to:

```text
Started ResourceBookingSystemApplication
```

---

# 🧪 API Testing Using Postman

## Base URL

```text
http://localhost:8080
```

---

# 1. Register User

### Method

```text
POST
```

### URL

```text
http://localhost:8080/api/auth/register
```

### Body → raw → JSON

```json
{
    "username": "john",
    "email": "john@gmail.com",
    "password": "john123"
}
```

Newly registered users should have the `USER` role.

---

# 2. Login User

### Method

```text
POST
```

### URL

```text
http://localhost:8080/api/auth/login
```

### Body

```json
{
    "username": "john",
    "password": "john123"
}
```

Copy the JWT token returned by the API.

Example:

```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

# 3. Login Admin

Use the seeded ADMIN credentials configured in your application.

### Method

```text
POST
```

### URL

```text
http://localhost:8080/api/auth/login
```

### Body

```json
{
    "username": "admin",
    "password": "admin123"
}
```

Copy the ADMIN JWT token.

> If your seeded ADMIN password is different, use the credentials configured in your project.

---

# 🔑 JWT Authentication

For protected APIs in Postman:

```text
Authorization
    ↓
Type: Bearer Token
    ↓
Token: <YOUR_JWT_TOKEN>
```

Use the appropriate token:

```text
USER Token  → USER APIs
ADMIN Token → ADMIN APIs
```

---

# 4. Get All Resources

### Method

```text
GET
```

### URL

```text
http://localhost:8080/api/resources
```

### Authorization

```text
Bearer Token
```

Use the USER or ADMIN token according to your security configuration.

---

# 5. Create Resource

Only `ADMIN` should perform this operation.

### Method

```text
POST
```

### URL

```text
http://localhost:8080/api/resources
```

### Authorization

```text
Bearer Token
```

Use the ADMIN token.

### Body

```json
{
    "name": "Meeting Room A",
    "description": "Room for team meetings",
    "location": "First Floor",
    "pricePerUnit": 500,
    "available": true
}
```

---

# 6. Update Resource

Only `ADMIN` should perform this operation.

### Method

```text
PUT
```

### URL

```text
http://localhost:8080/api/resources/1
```

### Authorization

```text
Bearer Token
```

Use the ADMIN token.

### Body

```json
{
    "name": "Meeting Room A Updated",
    "description": "Updated meeting room",
    "location": "Second Floor",
    "pricePerUnit": 600,
    "available": true
}
```

---

# 7. Create Reservation

This operation should be performed by a `USER`.

### Method

```text
POST
```

### URL

```text
http://localhost:8080/api/reservations
```

### Authorization

```text
Bearer Token
```

Use the USER token.

### Body

```json
{
    "resourceId": 1,
    "startTime": "2026-12-01T10:00:00",
    "endTime": "2026-12-01T12:00:00"
}
```

> The backend should identify the logged-in user from the JWT. Do not send `userId` in the request body.

---

# 8. Get My Reservations

### Method

```text
GET
```

### URL

```text
http://localhost:8080/api/reservations/my
```

### Authorization

```text
Bearer Token
```

Use the USER token.

The USER should only see their own reservations.

---

# 9. Update Reservation

### Method

```text
PUT
```

### URL

```text
http://localhost:8080/api/reservations/1
```

### Authorization

```text
Bearer Token
```

Use the USER token.

### Body

```json
{
    "resourceId": 1,
    "startTime": "2026-12-01T11:00:00",
    "endTime": "2026-12-01T13:00:00",
    "status": "PENDING"
}
```

The backend should verify that the reservation belongs to the authenticated user.

---

# 10. Delete Resource

Only `ADMIN` should perform this operation.

### Method

```text
DELETE
```

### URL

```text
http://localhost:8080/api/resources/1
```

### Authorization

```text
Bearer Token
```

Use the ADMIN token.

---

# 📋 API Testing Order

```text
1. Start MySQL
       ↓
2. Clone GitHub Repository
       ↓
3. Open Project
       ↓
4. Create MySQL Database
       ↓
5. Set Environment Variables
       ↓
6. Start Spring Boot
       ↓
7. Register USER
       ↓
8. Login USER
       ↓
9. Login ADMIN
       ↓
10. Create Resource → ADMIN
       ↓
11. Get Resources
       ↓
12. Create Reservation → USER
       ↓
13. Get My Reservations → USER
       ↓
14. Update Reservation → USER
       ↓
15. Update Resource → ADMIN
       ↓
16. Delete Resource → ADMIN
```

---

# 🌿 Git Commands

## Check Git Status

```powershell
git status
```

## Check Current Branch

```powershell
git branch
```

## Create New Branch

```powershell
git checkout -b feature/resource-booking
```

## Add Changes

```powershell
git add .
```

## Commit Changes

```powershell
git commit -m "Update resource booking system"
```

## Push Branch

```powershell
git push -u origin feature/resource-booking
```

## Pull Latest Changes

```powershell
git pull origin main
```

If you are working on another branch:

```powershell
git pull origin <branch-name>
```

---

# 🔄 If Repository Is Already Cloned

You do not need to clone it again.

```powershell
cd resource-booking-system
```

Pull latest changes:

```powershell
git pull
```

Set environment variables:

```powershell
$env:DB_USERNAME="root"
$env:DB_PASSWORD="1204"
$env:DB_URL="jdbc:mysql://localhost:3306/resource_booking_db"
$env:JWT_SECRET="resource-booking-system-jwt-secret-2026-secure-key"
$env:JWT_EXPIRATION="3600000"
```

Start the application:

```powershell
mvn spring-boot:run
```

---

# ⚠️ Security

Do **not** commit passwords, JWT secrets, API keys, or database credentials to GitHub.

Do not commit:

```text
DB_PASSWORD=1204
JWT_SECRET=resource-booking-system-jwt-secret-2026-secure-key
```

Use environment variables instead.

Add sensitive files to `.gitignore`:

```gitignore
.env
*.env
application-local.properties
```

---

# 📁 Project Structure

```text
resource-booking-system/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/resource_booking_system/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       ├── entity/
│   │   │       ├── exception/
│   │   │       ├── repository/
│   │   │       ├── security/
│   │   │       ├── service/
│   │   │       └── ResourceBookingSystemApplication.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│
├── pom.xml
├── .gitignore
└── README.md
```

---

# ✅ Final Checklist

```text
[ ] Git installed
[ ] GitHub repository cloned
[ ] Project opened
[ ] MySQL running
[ ] Database created
[ ] Environment variables configured
[ ] Spring Boot application started
[ ] USER registered
[ ] USER login tested
[ ] ADMIN login tested
[ ] USER JWT copied
[ ] ADMIN JWT copied
[ ] Resource created
[ ] Resource updated
[ ] Reservation created
[ ] My reservations tested
[ ] Reservation updated
[ ] Resource deleted
[ ] Changes committed
[ ] Changes pushed to GitHub
```

---

# 🎯 Quick Start

If the repository is already cloned and MySQL is ready:

```powershell
cd resource-booking-system

$env:DB_USERNAME="root"
$env:DB_PASSWORD="1204"
$env:DB_URL="jdbc:mysql://localhost:3306/resource_booking_db"
$env:JWT_SECRET="resource-booking-system-jwt-secret-2026-secure-key"
$env:JWT_EXPIRATION="3600000"

mvn spring-boot:run
```

Then test the APIs in Postman:

```text
POST   /api/auth/register
POST   /api/auth/login
GET    /api/resources
POST   /api/resources
PUT    /api/resources/{id}
POST   /api/reservations
GET    /api/reservations/my
PUT    /api/reservations/{id}
DELETE /api/resources/{id}
```

---

# 👨‍💻 Author

**Darshan Pawar**

Java | Spring Boot | REST API | MySQL | JWT | Backend Development