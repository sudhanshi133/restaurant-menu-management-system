# Restaurant & Menu Management API Documentation

## Base URL
```
/api/v1
```

## 1️⃣ Restaurant APIs

### Create Restaurant
**Endpoint:** `POST /api/v1/restaurants`

**Request Body:**
```json
{
  "name": "Pizza Palace",
  "location": "Downtown"
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "name": "Pizza Palace",
  "location": "Downtown",
  "isOpen": true
}
```

**Business Rules:**
- ✅ Name is mandatory
- ✅ Restaurant name must be unique
- ✅ Newly created restaurant is OPEN by default

**Error Responses:**
- `400 Bad Request` - Validation error (missing name)
- `409 Conflict` - Duplicate restaurant name

---

### Get Restaurant by ID
**Endpoint:** `GET /api/v1/restaurants/{id}`

**Response:** `200 OK`
```json
{
  "id": 1,
  "name": "Pizza Palace",
  "location": "Downtown",
  "isOpen": true
}
```

**Error Responses:**
- `404 Not Found` - Restaurant not found

---

### Update Restaurant Status (Open/Close)
**Endpoint:** `PATCH /api/v1/restaurants/{id}/status`

**Request Body:**
```json
{
  "isOpen": false
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "name": "Pizza Palace",
  "location": "Downtown",
  "isOpen": false
}
```

**Business Rules:**
- ✅ If restaurant is CLOSED → menu items cannot be added
- ✅ Response clearly indicates current status

**Error Responses:**
- `404 Not Found` - Restaurant not found
- `400 Bad Request` - Validation error

---

## 2️⃣ Menu Item APIs

### Add Menu Item to Restaurant
**Endpoint:** `POST /api/v1/restaurants/{restaurantId}/menu-items`

**Request Body:**
```json
{
  "name": "Margherita Pizza",
  "price": 12.99,
  "category": "VEG"
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "name": "Margherita Pizza",
  "price": 12.99,
  "category": "VEG",
  "status": "AVAILABLE",
  "restaurantId": 1,
  "restaurantName": "Pizza Palace"
}
```

**Business Rules:**
- ✅ Restaurant must exist
- ✅ Restaurant must be OPEN
- ✅ Menu item name must be unique per restaurant
- ✅ Default status = AVAILABLE
- ✅ Price must be > 0

**Error Responses:**
- `404 Not Found` - Restaurant not found
- `400 Bad Request` - Restaurant closed or validation error
- `409 Conflict` - Duplicate menu item name for this restaurant

---

### Get Menu Items of a Restaurant
**Endpoint:** `GET /api/v1/restaurants/{restaurantId}/menu-items`

**Query Parameters:**
- `category` (optional): Filter by category (VEG, NON_VEG, DESSERT, DRINK)
- `status` (optional): Filter by status (AVAILABLE, OUT_OF_STOCK)
- `sort` (optional): Sort by field,direction (e.g., `price,asc` or `price,desc`)
- `page` (optional, default: 0): Page number
- `size` (optional, default: 10): Page size

**Example Request:**
```
GET /api/v1/restaurants/1/menu-items?category=VEG&status=AVAILABLE&sort=price,asc&page=0&size=10
```

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": 1,
      "name": "Margherita Pizza",
      "price": 12.99,
      "category": "VEG",
      "status": "AVAILABLE",
      "restaurantId": 1,
      "restaurantName": "Pizza Palace"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    }
  },
  "totalElements": 1,
  "totalPages": 1,
  "last": true,
  "first": true,
  "size": 10,
  "number": 0,
  "numberOfElements": 1,
  "empty": false
}
```

**Business Rules:**
- ✅ Supports filtering by category
- ✅ Supports filtering by status
- ✅ Supports sorting by price (asc/desc)
- ✅ Supports pagination
- ✅ Soft-deleted items are excluded

**Error Responses:**
- `404 Not Found` - Restaurant not found

---

### Update Menu Item Status
**Endpoint:** `PATCH /api/v1/menu-items/{menuItemId}/status`

**Request Body:**
```json
{
  "status": "OUT_OF_STOCK"
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "name": "Margherita Pizza",
  "price": 12.99,
  "category": "VEG",
  "status": "OUT_OF_STOCK",
  "restaurantId": 1,
  "restaurantName": "Pizza Palace"
}
```

**Business Rules:**
- ✅ Only AVAILABLE ↔ OUT_OF_STOCK transitions allowed
- ✅ Returns proper HTTP status

**Error Responses:**
- `404 Not Found` - Menu item not found
- `400 Bad Request` - Validation error

---

### Delete Menu Item
**Endpoint:** `DELETE /api/v1/menu-items/{menuItemId}`

**Response:** `204 No Content`

**Business Rules:**
- ✅ Soft delete (flag-based)
- ✅ Deleted items do not appear in list APIs

**Error Responses:**
- `404 Not Found` - Menu item not found

---

## 🚨 Error Handling

All errors follow a consistent structure:

```json
{
  "error": "ERROR_CODE",
  "message": "Human-readable error message"
}
```

### Error Codes and HTTP Status

| Scenario | HTTP Code | Error Code | Example Message |
|----------|-----------|------------|-----------------|
| Restaurant not found | 404 | RESOURCE_NOT_FOUND | "Restaurant not found with id: 1" |
| Menu item not found | 404 | RESOURCE_NOT_FOUND | "Menu item not found with id: 1" |
| Duplicate restaurant name | 409 | DUPLICATE_RESOURCE | "Restaurant with name 'Pizza Palace' already exists" |
| Duplicate menu item name | 409 | DUPLICATE_RESOURCE | "Menu item with name 'Margherita Pizza' already exists for this restaurant" |
| Restaurant closed | 400 | RESTAURANT_CLOSED | "Cannot add menu items to a closed restaurant" |
| Validation error | 400 | VALIDATION_ERROR | "Restaurant name is mandatory" |
| Internal server error | 500 | INTERNAL_SERVER_ERROR | "An unexpected error occurred" |

---

## 📊 Data Models

### Enums

**Category:**
- `VEG`
- `NON_VEG`
- `DESSERT`
- `DRINK`

**Status:**
- `AVAILABLE`
- `OUT_OF_STOCK`

---

## 🚀 Running the Application

### Prerequisites
- Java 17+
- Maven 3.6+

### Build and Run
```bash
# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

### H2 Database Console
Access the H2 console at: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:restaurantdb`
- Username: `sa`
- Password: (leave blank)

---

## ✅ Implementation Checklist

### Architecture
- ✅ Clean separation: Controllers → Services → Repositories
- ✅ No business logic in controllers
- ✅ All validations in service layer
- ✅ No business logic in repositories
- ✅ DTOs for request/response
- ✅ Entities not exposed directly

### Business Rules
- ✅ Restaurant name uniqueness
- ✅ Menu item name uniqueness per restaurant
- ✅ Restaurant open/close validation
- ✅ Default values (isOpen=true, status=AVAILABLE)
- ✅ Soft delete for menu items
- ✅ Price validation (must be > 0)

### Error Handling
- ✅ Global exception handler
- ✅ Structured error responses
- ✅ Proper HTTP status codes
- ✅ Meaningful error messages

### Features
- ✅ Pagination support
- ✅ Sorting support
- ✅ Filtering by category and status
- ✅ RESTful API design
- ✅ Proper HTTP methods and status codes

