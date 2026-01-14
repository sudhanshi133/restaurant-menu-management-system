# Repository Layer Documentation

## Overview
This document describes the repository layer implementation for the Restaurant & Menu Management API. The repository layer uses **Spring Data JPA** to provide database access with minimal boilerplate code.

## Technology Stack
- **Spring Data JPA**: For repository abstraction
- **Hibernate**: As the JPA implementation
- **H2 Database**: In-memory database for development and testing
- **Jakarta Persistence API**: For entity annotations

## Database Configuration

### H2 In-Memory Database
The application uses H2 in-memory database configured in `application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:restaurantdb
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

**Access H2 Console**: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:restaurantdb`
- Username: `sa`
- Password: (leave blank)

## Entity Models

### Restaurant Entity
**Table**: `restaurants`

| Column   | Type    | Constraints           | Description                    |
|----------|---------|----------------------|--------------------------------|
| id       | BIGINT  | PRIMARY KEY, AUTO    | Unique identifier              |
| name     | VARCHAR | NOT NULL, UNIQUE     | Restaurant name                |
| location | VARCHAR | -                    | Restaurant location            |
| is_open  | BOOLEAN | NOT NULL, DEFAULT=true| Restaurant operational status |

**Key Features**:
- Unique constraint on `name` to prevent duplicate restaurant names
- Default value `true` for `isOpen` (newly created restaurants are open)

### MenuItem Entity
**Table**: `menu_items`

| Column        | Type    | Constraints                      | Description                    |
|---------------|---------|----------------------------------|--------------------------------|
| id            | BIGINT  | PRIMARY KEY, AUTO                | Unique identifier              |
| name          | VARCHAR | NOT NULL                         | Menu item name                 |
| price         | DOUBLE  | NOT NULL                         | Menu item price                |
| category      | VARCHAR | NOT NULL (ENUM)                  | VEG, NON_VEG, DESSERT, DRINK  |
| status        | VARCHAR | NOT NULL (ENUM), DEFAULT=AVAILABLE| AVAILABLE, OUT_OF_STOCK       |
| restaurant_id | BIGINT  | NOT NULL, FOREIGN KEY            | Reference to restaurant        |
| deleted       | BOOLEAN | NOT NULL, DEFAULT=false          | Soft delete flag               |

**Key Features**:
- Composite unique constraint on `(name, restaurant_id)` - menu item names must be unique per restaurant
- Many-to-One relationship with Restaurant (lazy loading)
- Soft delete implementation using `deleted` flag
- Default status is `AVAILABLE`

## Repository Interfaces

### RestaurantRepository

```java
public interface RestaurantRepository extends JpaRepository<Restaurant, Long>
```

**Methods**:

| Method                      | Return Type           | Description                                    |
|-----------------------------|-----------------------|------------------------------------------------|
| `findByName(String name)`   | `Optional<Restaurant>`| Find restaurant by exact name                  |
| `existsByName(String name)` | `boolean`             | Check if restaurant name already exists        |

**Inherited from JpaRepository**:
- `save(Restaurant)` - Create or update restaurant
- `findById(Long)` - Find restaurant by ID
- `findAll()` - Get all restaurants
- `deleteById(Long)` - Delete restaurant
- `count()` - Count total restaurants

### MenuItemRepository

```java
public interface MenuItemRepository extends JpaRepository<MenuItem, Long>
```

**Custom Query Methods**:

| Method                                          | Parameters                                    | Description                                           |
|-------------------------------------------------|-----------------------------------------------|-------------------------------------------------------|
| `findByRestaurantId`                            | restaurantId, Pageable                        | Get all menu items for a restaurant (paginated)       |
| `findByRestaurantIdAndCategory`                 | restaurantId, category, Pageable              | Filter by restaurant and category                     |
| `findByRestaurantIdAndStatus`                   | restaurantId, status, Pageable                | Filter by restaurant and status                       |
| `findByRestaurantIdAndCategoryAndStatus`        | restaurantId, category, status, Pageable      | Filter by restaurant, category, and status            |
| `findByIdAndNotDeleted`                         | id                                            | Find menu item by ID (excluding soft-deleted)         |
| `existsByNameAndRestaurantId`                   | name, restaurantId                            | Check if menu item name exists for restaurant         |

**Key Features**:
- All queries automatically exclude soft-deleted items (`deleted = false`)
- Support for pagination and sorting via `Pageable`
- Custom JPQL queries for complex filtering

**Inherited from JpaRepository**:
- `save(MenuItem)` - Create or update menu item
- `findById(Long)` - Find menu item by ID (includes deleted)
- `findAll()` - Get all menu items
- `deleteById(Long)` - Hard delete (not recommended, use soft delete)

## Soft Delete Implementation

Menu items use **soft delete** pattern:
- Items are never physically deleted from the database
- The `deleted` flag is set to `true` instead
- All repository queries filter out deleted items automatically
- Allows for data recovery and audit trails

**To soft delete a menu item**:
```java
menuItem.setDeleted(true);
menuItemRepository.save(menuItem);
```

## Pagination and Sorting

The repository supports Spring Data's `Pageable` interface for pagination and sorting.

**Example Usage**:
```java
// Page 0, size 10, sorted by price ascending
Pageable pageable = PageRequest.of(0, 10, Sort.by("price").ascending());
Page<MenuItem> items = menuItemRepository.findByRestaurantId(restaurantId, pageable);
```

## Business Rules Enforced at Repository Level

1. **Restaurant Name Uniqueness**: Database constraint + `existsByName()` method
2. **Menu Item Name Uniqueness per Restaurant**: Composite unique constraint + `existsByNameAndRestaurantId()` method
3. **Soft Delete**: All queries exclude deleted items
4. **Default Values**: 
   - Restaurant `isOpen = true`
   - MenuItem `status = AVAILABLE`
   - MenuItem `deleted = false`

## Assumptions

1. **Database**: H2 in-memory database is used for development. For production, switch to PostgreSQL/MySQL by updating `application.properties`
2. **ID Generation**: Auto-increment strategy for primary keys
3. **Lazy Loading**: Restaurant relationship in MenuItem uses lazy loading for performance
4. **Cascade Operations**: No cascade delete configured - must be handled at service layer
5. **Transaction Management**: Handled by Spring's `@Transactional` at service layer
6. **Data Validation**: Basic constraints at database level; detailed validation in service layer

## Next Steps

The repository layer is complete. The next implementation steps are:
1. **Service Layer**: Business logic, validations, and transaction management
2. **Exception Handling**: Custom exceptions and global exception handler
3. **DTOs**: Request/Response objects for API layer
4. **Controller Layer**: REST endpoints following the API specification

