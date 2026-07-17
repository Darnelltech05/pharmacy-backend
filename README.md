# 🏥 SA MedConnect - Backend API

## 📋 Project Overview

SA MedConnect is a digital pharmacy ordering system designed to address the medicine accessibility crisis in South Africa's rural and underserved communities. This backend REST API serves as the foundation for the entire platform, providing secure authentication, user management, medicine inventory control, order processing, and payment tracking.

The system connects patients directly to clinic pharmacy inventories, allowing them to check medicine availability in real-time and place orders for pickup at their nearest public health facility. By digitizing the medicine procurement process, SA MedConnect reduces the burden on overworked clinic staff, minimizes medication waste, and empowers patients—especially those in remote areas—to access essential medicines without making unnecessary trips to clinics.

---

## 🛠️ Technology Stack

The backend is built using a robust, modern Java ecosystem:

- **Spring Boot 3.2.0** - Core framework providing auto-configuration and production-ready features
- **Spring Security 3.2.0** - Handles authentication, authorization, and JWT token validation
- **Spring Data JPA 3.2.0** - Object-relational mapping for database operations
- **JJWT 0.11.5** - JSON Web Token generation and validation for stateless authentication
- **MySQL 8.x** - Relational database for persistent data storage
- **Maven 3.9.x** - Build automation and dependency management
- **Lombok 1.18.30** - Reduces boilerplate code with annotations
- **Java 21** - Latest LTS version with improved performance and features

---

## 📁 Project Structure Explained

The codebase follows a clean, layered architecture that separates concerns:

```
pharmacy-backend/
├── src/main/java/com/samedconnect/pharmacy_backend/
│   ├── PharmacyBackendApplication.java      # Spring Boot entry point
│   │
│   ├── config/                              # Configuration classes
│   │   ├── SecurityConfig.java              # Spring Security & CORS setup
│   │   └── JwtAuthenticationFilter.java     # Intercepts requests for JWT validation
│   │
│   ├── controller/                          # REST API endpoints (Controller layer)
│   │   ├── AuthController.java              # Registration & Login endpoints
│   │   ├── UserController.java              # Profile management
│   │   ├── MedicineController.java          # Medicine CRUD operations
│   │   ├── OrderController.java             # Order placement & history
│   │   └── PaymentController.java           # Payment processing & tracking
│   │
│   ├── dto/                                 # Data Transfer Objects
│   │   ├── request/                         # Incoming request DTOs with validation
│   │   └── response/                        # Outgoing response DTOs
│   │
│   ├── entity/                              # JPA Entities (Database tables)
│   │   ├── User.java                        # User accounts with roles
│   │   ├── UserProfile.java                 # SA-specific profile info
│   │   ├── Medicine.java                    # Medicine catalog
│   │   ├── Order.java                       # Customer orders
│   │   ├── OrderItem.java                   # Individual order items
│   │   └── Payment.java                     # Payment records
│   │
│   ├── exception/                           # Global exception handling
│   │   ├── GlobalExceptionHandler.java      # Catches all exceptions, returns consistent errors
│   │   ├── ResourceNotFoundException.java   # 404 Not Found
│   │   └── BadRequestException.java         # 400 Bad Request
│   │
│   ├── repository/                          # Data access layer (Spring Data JPA)
│   │   ├── UserRepository.java
│   │   ├── MedicineRepository.java
│   │   ├── OrderRepository.java
│   │   └── PaymentRepository.java
│   │
│   ├── service/                             # Business logic layer
│   │   ├── AuthService.java                 # Registration & login logic
│   │   ├── UserService.java                 # Profile management logic
│   │   ├── MedicineService.java             # Medicine business logic
│   │   ├── OrderService.java                # Order processing logic
│   │   └── PaymentService.java              # Payment logic
│   │
│   ├── security/                            # Security components
│   │   ├── jwt/JwtService.java              # JWT generation & validation
│   │   └── filter/JwtAuthenticationFilter.java # Security filter
│   │
│   └── utils/                               # Utility classes
│       └── Response.java                    # Standard API response wrapper
│
└── src/main/resources/
    └── application.properties               # Application configuration
```

This architecture ensures separation of concerns, making the codebase maintainable, testable, and scalable.

---

## 🗄️ Database Schema

The database consists of six core tables that support all platform functionality:

| Table | Purpose | Key Relationships |
|-------|---------|-------------------|
| `users` | Stores user accounts with roles (CUSTOMER, PHARMACIST, ADMIN) | Base table for authentication |
| `user_profiles` | Stores South African-specific information (ID number, medical aid, clinic affiliation) | One-to-one with users |
| `medicines` | Stores medicine catalog with stock quantities and expiry dates | Referenced by order_items |
| `orders` | Stores customer orders with UUID and status tracking | Many-to-one with users |
| `order_items` | Stores individual items within each order | Many-to-one with orders and medicines |
| `payments` | Stores payment records and transaction references | One-to-one with orders |

---

## 📡 API Endpoints

All endpoints follow RESTful conventions and return standardized responses wrapped in a `Response<T>` object.

### 🔐 Authentication Module (Person 1 - Darnell)

This module handles user identity and access control:

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/auth/register` | Creates a new user account with BCrypt password encryption | ❌ |
| POST | `/api/auth/login` | Authenticates user and returns JWT token | ❌ |
| GET | `/api/users/profile` | Retrieves the authenticated user's complete profile | ✅ |
| PUT | `/api/users/profile` | Updates user profile information including SA-specific fields | ✅ |

**Key Features:**
- Passwords are encrypted using BCrypt (strong, salted hashing)
- JWT tokens are stateless and expire after 24 hours
- Users have roles: CUSTOMER, PHARMACIST, ADMIN
- Profile includes South African fields: ID number, medical aid, clinic affiliation

---

### 💊 Medicine Management Module (Person 2 - Teboho)

This module manages the medicine catalog and inventory:

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/api/medicines` | Returns paginated list of all medicines | ✅ |
| GET | `/api/medicines/search` | Searches medicines by name or category | ✅ |
| GET | `/api/medicines/low-stock` | Returns medicines below threshold quantity | ✅ |
| GET | `/api/medicines/{id}` | Returns details of a specific medicine | ✅ |
| POST | `/api/medicines` | Creates a new medicine entry | ✅ |
| PUT | `/api/medicines/{id}` | Updates an existing medicine | ✅ |
| DELETE | `/api/medicines/{id}` | Deletes a medicine (admin only) | ✅ |

**Key Features:**
- Pagination for efficient data retrieval
- Search by name or category
- Low stock alerts for inventory management
- Stock quantity tracking with automatic updates on orders
- Expiry date tracking

---

### 📦 Order Processing Module (Person 3 - Japhta)

This module handles the entire order lifecycle:

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/orders` | Creates a new order with stock validation | ✅ |
| GET | `/api/orders` | Returns all orders | ✅ |
| GET | `/api/orders/{id}` | Returns details of a specific order | ✅ |
| GET | `/api/orders/my-orders` | Returns orders for the authenticated user | ✅ |
| GET | `/api/orders/user/{userId}` | Returns orders for a specific user | ✅ |

**Key Features:**
- Stock validation before order placement
- Automatic stock reduction when orders are placed
- Prevents ordering out-of-stock medicines
- Calculates total order price from item subtotals
- Generates unique UUID for each order for tracking
- Supports order status tracking (PENDING → PAID → PROCESSING → SHIPPED → DELIVERED)

---

### 💳 Payment Tracking Module (Person 4 - Muofhe)

This module handles payments and order tracking:

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/payments` | Simulates payment processing | ✅ |
| GET | `/api/payments/{orderId}` | Retrieves payment status | ✅ |
| PUT | `/api/payments/{orderId}/status` | Updates order status (pharmacist/admin) | ✅ |
| GET | `/api/payments/track/{uuid}` | Tracks order by UUID | ✅ |

**Key Features:**
- Mock payment simulation (success/failure)
- Order status workflow management
- Unique UUID for every order
- Payment history and transaction tracking
- Role-based status updates (pharmacists can update, patients can view)

---

## 📝 Standard API Response Format

Every endpoint in the system returns data in a consistent, predictable format:

### Success Response
```json
{
    "success": true,
    "message": "Operation successful",
    "data": {},           // The requested data (object or array)
    "errors": null,
    "timestamp": 1783709269609
}
```

### Error Response
```json
{
    "success": false,
    "message": "Error description",
    "data": null,
    "errors": {},          // Validation errors (field-specific)
    "timestamp": 1783709269609
}
```

**Why this matters:**
- Consistent format makes frontend integration seamless
- All errors are handled globally (no raw stack traces)
- Every response includes a timestamp for debugging
- Validation errors are returned with field-specific messages

---

## 🛡️ Security Implementation

The system uses a multi-layered security approach:

1. **Stateless Authentication** - JWT tokens enable stateless, scalable authentication
2. **BCrypt Password Encoding** - Industry-standard password hashing
3. **Role-Based Access Control** - Different roles have different permissions:
   - **CUSTOMER**: Can view medicines, place orders, view own orders
   - **PHARMACIST**: Can manage medicines, update order status, view all orders
   - **ADMIN**: Full system access, user management
4. **CORS Configuration** - Allows frontend (localhost:3000) to communicate securely
5. **JWT Filter** - Intercepts every request to validate tokens
6. **Global Exception Handler** - No sensitive information leaks to clients

---

## 👥 Team Contributions

### Darnell - Backend Lead (Authentication & Security)
- Designed and implemented the entire authentication module
- Created User and UserProfile entities with South African-specific fields
- Implemented JWT token generation and validation
- Configured Spring Security with role-based access control
- Created Response wrapper and global exception handler
- Integrated frontend authentication screens (Login, Register, Profile)

### Teboho - Medicine & Inventory Module
- Designed and implemented the Medicine entity
- Created complete CRUD operations for medicines
- Added search and filter functionality
- Implemented pagination for efficient data retrieval
- Created low stock alert system
- Built medicine management frontend screens

### Muofhe - Order Processing Module
- Designed and implemented Order and OrderItem entities
- Created order placement logic with stock validation
- Implemented total price calculation
- Added order history tracking
- Created order status management system
- Built order management frontend screens

### Japhta - Payment & Tracking Module
- Designed and implemented Payment entity
- Created mock payment processing API
- Implemented order status tracking (PENDING → PAID → PROCESSING → SHIPPED → DELIVERED)
- Added UUID generation for order tracking
- Implemented QA testing for all modules
- Built payment and tracking frontend screens

---

## 📅 Development Progress

### Week 1 (Completed) - ✅
**Focus:** Foundation and Authentication
- ✓ User and UserProfile entities created
- ✓ JWT security implemented
- ✓ Registration and Login APIs completed
- ✓ Profile management APIs completed
- ✓ Frontend Login/Register screens built
- ✓ Response wrapper and global exception handling
- ✓ Tag: `v1-week1`

### Week 2 (Completed) - ✅
**Focus:** Core Business Logic
- ✓ Medicine entity and CRUD APIs
- ✓ Search, filter, and pagination
- ✓ Order and OrderItem entities
- ✓ Order placement with stock validation
- ✓ Payment entity and mock payment API
- ✓ Order status tracking
- ✓ Frontend integration for all modules
- ✓ Tag: `v1-week2`

### Week 3 (In Progress) - 🔄
**Focus:** Polish and Testing
- Bug fixes and edge cases
- UI polish and user experience improvements
- Comprehensive testing (Postman and manual)
- Demo preparation
- Tag: `v1-final`

---

## 🎯 What Makes This Different

| Feature | Why It Matters |
|---------|----------------|
| **South Africa-Specific Design** | Integrates with public healthcare system, not just private pharmacies |
| **Patient Empowerment** | Gives patients control and transparency over their medicine access |
| **Real-Time Stock Visibility** | Patients check availability before travelling, saving time and money |
| **Proactive Inventory Management** | Pharmacists receive alerts before stock runs out |
| **Reduces Healthcare Burden** | Less strain on overworked clinic staff |
| **No Existing Product** | Built specifically for South Africa's public healthcare needs |

---

## 🐛 Known Issues & Resolutions

| Issue | Status | Resolution |
|-------|--------|------------|
| `/medicines/low-stock` endpoint path conflict | ✅ Fixed | Reordered endpoint mapping in controller |
| Order response wrapper missing | ✅ Fixed | Added Response<T> wrapper to all endpoints |
| BigDecimal to Double conversion errors | ✅ Fixed | Properly handled monetary values |
| JWT secret key validation | ✅ Fixed | Updated to proper Base64 encoding |

---

## 📚 Repository Information

- **Backend Repository:** https://github.com/Darnelltech05/pharmacy-backend
- **Frontend Repository:** https://github.com/Darnelltech05/pharmacy-frontend
- **Default Branch:** `main` (stable production code)
- **Development Branch:** `develop` (integration branch)
- **Feature Branches:** `feature/{name}/{module}`

