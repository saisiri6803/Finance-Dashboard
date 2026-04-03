
# Finance Dashboard System
Backend for a finance Dashboard system built with Java, SpringBoot, Spring Security, JWT, JPA.


## Core Functionalities

- User and Role Management
- Financial Record Management
- Dashboards Summary APIs
- Access Control

## Features Implemented
##### User and Role Management
- Create and manage users
- Assign roles to users
- Manage user active/inactive status
- Authenticate users with email and password
- Generate JWT token after login
- Restrict access based on roles
- Return validation and error responses in a consistent format
##### Financial Record Management
- Create and manage financial records
- View all financial records
- View Single record by id
- Update and delete records
- Filter records by:
  - type
  - category
  - stateDate
  - endDate


## Role Model

This application supports three role:

- **VIEWER** - can access read-only dashboard data
- **ANALYST** - can manage dashboard data and insights
- **ADMIN** - can manage users and perform full system actions

### Tech Stack Used
- Java 17
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- H2 Database
- Lombok
- Maven

### User Entity

Each user contains:
- `id`
- `name`
- `email`
- `password`
- `role`
- `active`
- `createdAt`
- `updatedAt`

### Access Control Rules

Configured in `SecurityConfig.java`.

#### Public EndPoints

- `/api/auth/**`
- `h2-console/**`

#### Restricted EndPoints

- `/api/users/**` -> `ADMIN` only
- `/api/dashboard/summary` -> 'VIEWER`, `ANALYST`, `ADMIN`
- `/api/dashboard/insights/**` -> `ANALYST`, `ADMIN`
- Record write endpoints -> `ADMIN` only


## EndPoints
### Authentication

All protected endpoints require a Bearer token.
`Authorization: Bearer <token>` Required for access
### User Authentication, Authorization and Registration

| Method | EndPoint | Description |
|--------|----------|-------------|
| POST | `/api/login` | Login with User credentials |
| POST | `/api/register` | Register as User |
| GET | `/api/users` | Get all Users |
| GET | `/api/users/{id}` | Get User by id |


### Financial records

| Method | EndPoint | Description |
|--------|----------|-------------|
| POST | `/api/records` | Create a financial record |
| GET | `/api/records` | Get all records with optional filters |
| GET | `/api/records/{id}` | Get record by ID |
| PUT | `/api/records/{id}` | Update record |
| DELETE | `/api/records/{id}` | Delete record |

#### Example Filter Usage
```http
GET /api/records?type=INCOME&category=Salary&startDate=2026-01-01&endDate=2026-03-31
```

#### Default Seed Users

The application creates these users on startup:

| Role | Email | Password |
|------|-------|----------|
| ADMIN | admin@finance.com | admin123 |
| ANALYST | analyst@finance.com | analyst123 |
| VIEWER | viewer@finance.com | viewer123 |

### How Authentication Works

1. User sends email and password to login endpoint
2. Spring Security authentication credentials
3. JWT token is generated
4. Client sends token in `Authorization: Bearer<token>` header
5. JWT filter validates token and loads user details
6. Spring Security checks role permission before allowing access






## Run the project

```bash
mvn spring-boot:run
```

Application runs on:

```text
http://localhost:8080
```


Use:
- JDBC URL: `jdbc:h2:mem:financedb`
- Username: root
- Password: [*replace with you Password*]

### How to and What to test in Postman

- `AuthController.java`
  - `POST  /api/auth/login`
  - `POST  /api/auth/request`

- `UserController.java`
  - `GET /api/users`
  - `GET /api/users/{id}`
  - `PUT /api/users/{id}`
  - `DELETE /api/users/{id}`