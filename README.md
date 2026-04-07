# Finance Data Processing and Access Control Backend

A Spring Boot REST API for a finance dashboard system with role-based access control (RBAC).

## 🎯 Project Overview

This backend system manages:
- **User Management** with roles (VIEWER, ANALYST, ADMIN)
- **Financial Records** (income/expense tracking)
- **Dashboard Analytics** (summaries, trends, category totals)
- **Role-Based Access Control** (different permissions per role)

## 🛠️ Tech Stack

| Technology | Purpose |
|------------|---------|
| Java 17 | Programming Language |
| Spring Boot 3.2 | Backend Framework |
| Spring Security | Authentication & Authorization |
| JWT | Token-based Authentication |
| Spring Data JPA | Database ORM |
| H2 Database | In-memory Database |
| Swagger/OpenAPI | API Documentation |
| Maven | Build Tool |

## 📁 Project Structure

```
src/main/java/com/finance/
├── FinanceApplication.java          # Main entry point
├── config/
│   └── OpenApiConfig.java           # Swagger configuration
├── controller/
│   ├── AuthController.java          # Login/Register endpoints
│   ├── UserController.java          # User management endpoints
│   ├── FinancialRecordController.java  # CRUD for records
│   └── DashboardController.java     # Summary/Analytics endpoints
├── dto/
│   ├── request/                     # Request DTOs
│   │   ├── LoginRequest.java
│   │   ├── RegisterRequest.java
│   │   ├── FinancialRecordRequest.java
│   │   └── UserUpdateRequest.java
│   └── response/                    # Response DTOs
│       ├── AuthResponse.java
│       ├── UserResponse.java
│       ├── FinancialRecordResponse.java
│       ├── DashboardSummaryResponse.java
│       └── ApiResponse.java
├── entity/
│   ├── User.java                    # User entity
│   └── FinancialRecord.java         # Financial record entity
├── enums/
│   ├── Role.java                    # VIEWER, ANALYST, ADMIN
│   ├── UserStatus.java              # ACTIVE, INACTIVE
│   └── RecordType.java              # INCOME, EXPENSE
├── exception/
│   ├── GlobalExceptionHandler.java  # Centralized error handling
│   ├── ResourceNotFoundException.java
│   ├── UnauthorizedException.java
│   └── BadRequestException.java
├── repository/
│   ├── UserRepository.java
│   └── FinancialRecordRepository.java
├── security/
│   ├── JwtTokenProvider.java        # JWT utilities
│   ├── JwtAuthenticationFilter.java # Filter for JWT validation
│   ├── SecurityConfig.java          # Security configuration
│   └── CustomUserDetailsService.java
└── service/
    ├── AuthService.java
    ├── UserService.java
    ├── FinancialRecordService.java
    └── DashboardService.java
```

## 🔐 Role-Based Access Control

| Action | VIEWER | ANALYST | ADMIN |
|--------|--------|---------|-------|
| View Dashboard | ✅ | ✅ | ✅ |
| View Records | ✅ | ✅ | ✅ |
| Access Analytics | ❌ | ✅ | ✅ |
| Create Records | ❌ | ❌ | ✅ |
| Update Records | ❌ | ❌ | ✅ |
| Delete Records | ❌ | ❌ | ✅ |
| Manage Users | ❌ | ❌ | ✅ |

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Installation

1. **Clone the repository**
```bash
git clone <repository-url>
cd finance-backend
```

2. **Build the project**
```bash
mvn clean install
```

3. **Run the application**
```bash
mvn spring-boot:run
```

4. **Access the application**
- API Base URL: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- H2 Console: `http://localhost:8080/h2-console`

### H2 Database Console
- URL: `jdbc:h2:mem:financedb`
- Username: `sa`
- Password: (empty)

## 📡 API Endpoints

### Authentication
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/auth/register` | Register new user | Public |
| POST | `/api/auth/login` | Login and get JWT token | Public |

### User Management (Admin Only)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| PUT | `/api/users/{id}` | Update user |
| PUT | `/api/users/{id}/status` | Change user status |
| PUT | `/api/users/{id}/role` | Change user role |
| DELETE | `/api/users/{id}` | Delete user |

### Financial Records
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/records` | Get all records (with filters) | All authenticated |
| GET | `/api/records/{id}` | Get record by ID | All authenticated |
| POST | `/api/records` | Create new record | Admin only |
| PUT | `/api/records/{id}` | Update record | Admin only |
| DELETE | `/api/records/{id}` | Delete record | Admin only |

**Query Parameters for filtering:**
- `type`: INCOME or EXPENSE
- `category`: Category name
- `startDate`: Start date (YYYY-MM-DD)
- `endDate`: End date (YYYY-MM-DD)
- `page`: Page number (default: 0)
- `size`: Page size (default: 10)

### Dashboard
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/dashboard/summary` | Get overall summary | All authenticated |
| GET | `/api/dashboard/category-totals` | Category-wise totals | Analyst, Admin |
| GET | `/api/dashboard/monthly-trends` | Monthly trends | Analyst, Admin |
| GET | `/api/dashboard/recent-activity` | Recent transactions | All authenticated |

## 📝 Sample API Requests

### Register a User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "password123"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123"
  }'
```

### Create Financial Record (Admin)
```bash
curl -X POST http://localhost:8080/api/records \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-jwt-token>" \
  -d '{
    "amount": 5000.00,
    "type": "INCOME",
    "category": "Salary",
    "description": "Monthly salary",
    "date": "2024-01-15"
  }'
```

### Get Dashboard Summary
```bash
curl -X GET http://localhost:8080/api/dashboard/summary \
  -H "Authorization: Bearer <your-jwt-token>"
```

## 🧪 Default Test Users

The application creates these test users on startup:

| Username | Password | Role | Status |
|----------|----------|------|--------|
| admin | admin123 | ADMIN | ACTIVE |
| analyst | analyst123 | ANALYST | ACTIVE |
| viewer | viewer123 | VIEWER | ACTIVE |

## 🏗️ Design Decisions & Assumptions

### Architecture
1. **Layered Architecture**: Controller → Service → Repository pattern for separation of concerns
2. **DTOs**: Separate request/response objects to decouple API from entities
3. **Global Exception Handling**: Centralized error handling for consistent responses

### Security
1. **JWT Authentication**: Stateless authentication suitable for REST APIs
2. **Role-Based Access**: Method-level security using `@PreAuthorize`
3. **Password Encoding**: BCrypt for secure password storage

### Database
1. **H2 In-Memory**: Chosen for simplicity and easy setup (configurable for production DBs)
2. **Auto Schema Generation**: Hibernate creates tables automatically

### Assumptions
1. Users can self-register with VIEWER role by default
2. Only ADMIN can change user roles and status
3. Financial records are shared across all users (not per-user isolation)
4. Soft delete not implemented (could be added as enhancement)

## 🔧 Configuration

### Switching to PostgreSQL
Update `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/financedb
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

### Changing JWT Settings
```properties
jwt.secret=your-super-secret-key-here
jwt.expiration=86400000  # 24 hours in milliseconds
```

## 📊 Future Enhancements

- [ ] Pagination for all list endpoints
- [ ] Soft delete for records
- [ ] User-specific record isolation
- [ ] Rate limiting
- [ ] Refresh tokens
- [ ] Unit and integration tests
- [ ] Docker containerization

## Author

Devyani

## License

This project is for assessment purposes.
