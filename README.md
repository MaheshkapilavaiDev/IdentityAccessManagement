# Enterprise Identity & Access Management (IAM) Platform

## Project Overview

The Enterprise Identity & Access Management (IAM) Platform is a secure authentication and authorization system developed using Spring Boot and Java 21. It provides user registration, JWT-based authentication, role-based access control (RBAC), permission management, session management, audit logging, account locking, email verification, Redis integration, Docker support, and REST APIs.

---

# Technologies Used

- Java 21
- Spring Boot 3.x
- Spring Security
- Spring Data JPA
- MySQL
- Redis
- JWT (JSON Web Token)
- Maven
- Docker
- Docker Compose
- Lombok
- Swagger (OpenAPI)
- Jakarta Validation

---

# Features

### Authentication

- User Registration
- User Login
- JWT Authentication
- Refresh Token Support
- Email Verification
- Password Encryption using BCrypt

### User Management

- Get All Users
- Get User By Id
- Update User
- Delete User

### Role Management

- Create Role
- Update Role
- Delete Role
- Get All Roles
- Get Role By Id

### Permission Management

- Create Permission
- Update Permission
- Delete Permission
- Get All Permissions
- Get Permission By Id

### User Role Management

- Assign Role to User
- Remove Role from User
- Get User Roles

### Session Management

- Store User Sessions
- Login Time
- Logout Time
- Active Session Tracking

### Audit Logging

- User Activity Logging
- Login History
- API Activity

### Security

- Spring Security
- JWT Authorization
- Role Based Access Control (RBAC)
- Password Encryption

### API Documentation

- Swagger UI
- OpenAPI Documentation

---

# Project Structure

```
src
 ├── controller
 ├── service
 ├── repository
 ├── entity
 ├── dto
 ├── security
 ├── exception
 ├── config
 └── util
```

---

# Database

Database Name

```
iamdb
```

Tables

- users
- roles
- permissions
- user_roles
- role_permissions
- refresh_tokens
- user_sessions
- audit_logs

---

# Prerequisites

- Java 21
- Maven 3.9+
- MySQL 8
- Redis
- Docker Desktop
- Git
- Postman

---

# Clone Project

```bash
git clone <repository-url>

cd IdentityAccessManagement
```

---

# Build Project

```bash
mvn clean package
```

---

# Run Project

```bash
mvn spring-boot:run
```

or

Run

```
IdentityAccessManagementApplication.java
```

---

# Docker

Build Docker Image

```bash
docker build -t iam-app .
```

Run Docker Container

```bash
docker run -d -p 8080:8080 iam-app
```

Using Docker Compose

```bash
docker compose up --build
```

Stop Containers

```bash
docker compose down
```

---

# Swagger

```
http://localhost:8080/swagger-ui/index.html
```

API Docs

```
http://localhost:8080/v3/api-docs
```

---

# Authentication APIs

## Register

POST

```
/api/auth/register
```

## Login

POST

```
/api/auth/login
```

## Refresh Token

POST

```
/api/auth/refresh
```

---

# User APIs

GET

```
/api/users
```

GET

```
/api/users/{id}
```

PUT

```
/api/users/{id}
```

DELETE

```
/api/users/{id}
```

Assign Role

POST

```
/api/users/{userId}/roles/{roleId}
```

Remove Role

DELETE

```
/api/users/{userId}/roles/{roleId}
```

Get User Roles

GET

```
/api/users/{userId}/roles
```

---

# Role APIs

Create Role

POST

```
/api/roles
```

Get All Roles

GET

```
/api/roles
```

Get Role By Id

GET

```
/api/roles/{id}
```

Update Role

PUT

```
/api/roles/{id}
```

Delete Role

DELETE

```
/api/roles/{id}
```

---

# Permission APIs

Create Permission

POST

```
/api/permissions
```

Get All Permissions

GET

```
/api/permissions
```

Get Permission By Id

GET

```
/api/permissions/{id}
```

Update Permission

PUT

```
/api/permissions/{id}
```

Delete Permission

DELETE

```
/api/permissions/{id}
```

---

# Security

The application uses JWT Authentication.

All secured APIs require the Authorization header.

Example

```
Authorization: Bearer <JWT_TOKEN>
```

---

# Docker Services

- Spring Boot Application
- MySQL Database
- Redis Cache

---

# Logging

Spring Boot Logging

Hibernate SQL Logging

Security Debug Logging
---

# Author

Mahesh Kapilavai

---
# Project Conclusion
The Enterprise Identity & Access Management (IAM) Platform provides secure authentication, authorization, user management, role management, permission management, JWT security, Docker deployment, Redis integration, and RESTful APIs using Spring Boot and Java 21. The project follows a layered architecture with secure coding practices and is suitable as a production-ready IAM backend application.