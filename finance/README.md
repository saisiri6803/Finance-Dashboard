
# Finance Dashboard System
A **Production-ready Spring Boot + Java** REST API implemented Finance Dashboard Systems.


## Security Note !!

This project uses **demo-only fallback credentials** in `application.properties`:
- JWT: `404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970`
- H2 DB: `root` / empty password

**Never commit real secrets to Git.** Override with environment variables:
```bash
export JWT_SECRET="your-real-production-secret"
mvn spring-boot:run
```

## Core Functionalities

- User and Role Management
- Financial Record Management
- Dashboards Summary APIs
- Access Control

## Setup Process
### Prerequistes
- Java 17+
- Maven 3.6+

### Development
```bash
git clone <repo>
cd finance
mvn clean install
mvc spring-boot:run 

# Backend ready: http://localhost:8080
# H2 Console: http://localhost:8080/h2-console
```

## Features Implemented
#### User and Role Management
- Create and manage users
- Assign roles to users
- Manage user active/inactive status
- Authenticate users with email and password
- Generate JWT token after login
- Restrict access based on roles
- Return validation and error responses in a consistent format
#### Financial Record Management
- Create and manage financial records
- View all financial records
- View Single record by id
- Update and delete records
- Filter records by:
  - type
  - category
  - startDate
  - endDate
#### Dashboard Summary APIs
- Dashboard summary APIs for totals, category-wise breakdowns, recent activity, and monthly trends.
- Input validation with useful error responses.


### Role Model

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

## Role Permissions

| Action | Viewer | Analyst | Admin |
|---|---|---|---|
| View dashboard summary | Yes | Yes | Yes |
| View financial records | Yes | Yes | Yes |
| Access detailed insights | No | Yes | Yes |
| Create records | No | No | Yes |
| Update records | No | No | Yes |
| Delete records | No | No | Yes |
| Manage users | No | No | Yes |

**Security layers**:
1. **URL patterns** in `SecurityConfig` (blocks before controller)
2. **`@PreAuthorize`** on methods
3. **JWT validation** middleware

## Default Seed Users

The application creates these users on startup:

| Role | Email | Password |
|------|-------|----------|
| ADMIN | admin@finance.com | admin123 |
| ANALYST | analyst@finance.com | analyst123 |
| VIEWER | viewer@finance.com | viewer123 |

## API Documentation
### Authentication Flow
#### 1. Login (return JWT token)
```bash
curl -X POST http://localhost:8080/api/auth/login \
   -H "Content-Type: application/json" \
   -d '{"email":"admin@finance.com","password":"admin123"}'
```
#### 2. Use token for protected endpoints
```bash
curl -H "Authorization: Bearer <YOUR_JET_TOKEN>"
http://localhost:8080/api/dashboard/summary

```


### User Authentication, Authorization and Registration

| Method | EndPoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/login` | Login with User credentials |
| POST | `/api/auth/register` | Register as User |
| GET | `/api/users` | Get all Users |
| GET | `/api/users/{id}` | Get User by id |


### Financial records

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/records` | Viewer, Analyst, Admin | List records with optional filters |
| GET | `/api/records/{id}` | Viewer, Analyst, Admin | Get a single record |
| POST | `/api/records` | Admin | Create a record |
| PUT | `/api/records/{id}` | Admin | Update a record |
| DELETE | `/api/records/{id}` | Admin | Delete a record |

#### For Record Filters
Use query parameters with `GET /api/records`:

| Parameter | Type | Example | Description |
|---|---|---|---|
| `type` | Enum | `INCOME` | Filter by record type |
| `category` | String | `Salary` | Filter by category |
| `from` | Date | `2026-01-01` | Start date |
| `to` | Date | `2026-12-31` | End date |

#### Example Filter Usage
```http
GET /api/records?type=INCOME&category=Salary&startDate=2026-01-01&endDate=2026-03-31
```

#### Dashboard APIs

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/dashboard/summary` | Viewer, Analyst, Admin | Get totals, balance, breakdowns, recent activity, and trends |
| GET | `/api/dashboard/insights/category-breakdown` | Analyst, Admin | Get deeper analytics access |

Returns a full summary for the finance dashboard.

#### Includes
- `totalIncome`: sum of all income records
- `totalExpenses`: sum of all expense records
- `netBalance`: income minus expenses
- `incomeByCategory`: total income grouped by category
- `expenseByCategory`: total expense grouped by category
- `recentActivity`: latest financial records
- `monthlyTrends`: monthly grouped totals by type

### Sample Dashboard Response

```json
{
  "success": true,
  "message": "Dashboard summary fetched",
  "data": {
    "totalIncome": 5800.00,
    "totalExpenses": 1750.00,
    "netBalance": 4050.00,
    "incomeByCategory": {
      "Salary": 5000.00,
      "Freelance": 800.00
    },
    "expenseByCategory": {
      "Rent": 1200.00,
      "Utilities": 350.00,
      "Groceries": 200.00
    }
  }
}
```
## Validation and Error Handling

The API includes:

- Bean validation for request payloads.
- Proper HTTP status codes.
- Consistent JSON responses for success and failure.
- Protection against invalid operations such as missing entities or unauthorized actions.

### How Authentication Works

1. User sends email and password to login endpoint
2. Spring Security authentication credentials
3. JWT token is generated
4. Client sends token in `Authorization: Bearer<token>` header
5. JWT filter validates token and loads user details
6. Spring Security checks role permission before allowing access


## Trade-offs

- H2 in-memory database is easy to run, but data is not durable across restarts.
- Fixed RBAC roles are simple and clear, but less flexible than fine-grained permissions.
- JWT keeps authentication stateless, but token revocation and logout are more complex.
- Monolithic structure is easier to maintain for this scope, but less scalable than splitting analytics, auth, and reco
  rds into separate services later.

## Assumptions Made

1. **Single tenant** - No Multi-tenant isolation
2. **Moderate data** - No pagination
3. **Admin registration** - Public `/register` for demo
4. **English only** - No i18n
5. **Basic categories** - No predefined category list


## Testing the Implementation

1. Login as different roles
```bash
curl -X POST http://localhost:8080/api/auth/login -d 
    '{"email":"viewer@finance.com","password":"viewer123"}'
```

2. Test VIEWER permissions (works)
```bash
curl -H "Authorization: Bearer <YOUR_JWT_TOKEN>" - X POST
https://localhost:8080/api/dashboard/summary 
```

3. Test ADMIN permissions (works)
```bash
curl -H "Authorization: Bearer <ADMIN_TOKEN>" -X POST http://localhost:8080/api/records -d '{"amount":1000,"type":"INCOME","category":"Bonus","date":"2026-04-04"}'
```

4. Test unauthorized access (403)
```bash
curl -H "Authorization: Bearer $VIEWER_TOKEN" -X POST http://localhost:8080/api/records
```
## Future Improvements

- [ ] Refresh tokens and logout handling
- [ ] Pagination and Sorting for records
- [ ] Replace H2-in memory with PostgreSQL
- [ ] Category Management
- [ ] File uploads for receipts
- [ ] OpenAPI/Swagger docs
- [ ] Real-time updates
- [ ] Integration tests

## License
MIT - Free to use and modify