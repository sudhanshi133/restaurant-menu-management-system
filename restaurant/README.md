# Restaurant & Menu Management API

A RESTful backend service for managing restaurants and their menu items with comprehensive business rules, clean API design, and proper error handling.

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Build and Run
```bash
# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

### Access H2 Database Console
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:restaurantdb`
- Username: `sa`
- Password: (leave blank)

---

## 📚 API Documentation

### Base URL
All APIs are served under: `/api/v1`

### Restaurant APIs

#### 1. Create Restaurant
```http
POST /api/v1/restaurants
Content-Type: application/json

{
  "name": "Pizza Palace",
  "location": "Downtown"
}
```

#### 2. Get Restaurant by ID
```http
GET /api/v1/restaurants/{id}
```

#### 3. Update Restaurant Status
```http
PATCH /api/v1/restaurants/{id}/status
Content-Type: application/json

{
  "isOpen": false
}
```

### Menu Item APIs

#### 1. Add Menu Item
```http
POST /api/v1/restaurants/{restaurantId}/menu-items
Content-Type: application/json

{
  "name": "Margherita Pizza",
  "price": 12.99,
  "category": "VEG"
}
```

#### 2. Get Menu Items (with filters)
```http
GET /api/v1/restaurants/{restaurantId}/menu-items?category=VEG&status=AVAILABLE&sort=price,asc&page=0&size=10
```

**Query Parameters:**
- `category` (optional): VEG, NON_VEG, DESSERT, DRINK
- `status` (optional): AVAILABLE, OUT_OF_STOCK
- `sort` (optional): field,direction (e.g., price,asc)
- `page` (optional, default: 0)
- `size` (optional, default: 10)

#### 3. Update Menu Item Status
```http
PATCH /api/v1/menu-items/{menuItemId}/status
Content-Type: application/json

{
  "status": "OUT_OF_STOCK"
}
```

#### 4. Delete Menu Item
```http
DELETE /api/v1/menu-items/{menuItemId}
```

---

## 🚨 Error Responses

All errors follow a consistent structure:

```json
{
  "error": "ERROR_CODE",
  "message": "Human-readable error message"
}
```

### HTTP Status Codes
- `200 OK` - Successful GET/PATCH
- `201 Created` - Successful POST
- `204 No Content` - Successful DELETE
- `400 Bad Request` - Validation error or business rule violation
- `404 Not Found` - Resource not found
- `409 Conflict` - Duplicate resource
- `500 Internal Server Error` - Unexpected error

---

## 🏗️ Architecture

### Clean Layered Architecture
```
Controller Layer (REST API)
    ↓
Service Layer (Business Logic)
    ↓
Repository Layer (Data Access)
    ↓
Database (H2)
```

### Package Structure
```
com.example.restaurant/
├── controller/          # REST endpoints
├── service/            # Business logic & validations
├── repository/         # Data access (Spring Data JPA)
├── model/
│   ├── entity/        # JPA entities
│   ├── dto/           # Request/Response DTOs
│   └── enums/         # Category, Status enums
└── exception/         # Custom exceptions & global handler
```

---


## 📝 Assumptions

1. **Database**: H2 in-memory database for development. For production, configure PostgreSQL/MySQL in `application.properties`
2. **Authentication**: Not implemented (can be added with Spring Security)
3. **Soft Delete**: Menu items use flag-based deletion for audit trails
4. **Pagination**: Default page size is 10, can be customized via query parameters

---

