# 💊 SA MedConnect – Pharmacy Backend API

A secure RESTful backend for the **SA MedConnect** digital pharmacy platform, built with **Spring Boot**. The system aims to improve access to healthcare by enabling patients, pharmacies, and administrators to manage medicine orders and user accounts securely, with a focus on South Africa's rural and underserved communities.

---

# 📖 Project Overview

SA MedConnect is a pharmacy management and medicine ordering platform that provides secure authentication, user management, and the foundation for future features such as prescription management, medicine ordering, inventory tracking, and pharmacy administration.

The backend follows REST API best practices, uses JWT-based authentication, and implements role-based access control to ensure secure communication between clients and the server.

---

# 🚀 Week 1 Features (v1-week1)

### Authentication & Security
- ✅ User registration
- ✅ Secure password encryption using BCrypt
- ✅ User login with JWT authentication
- ✅ Stateless authentication using Spring Security
- ✅ Role-based authorization

### User Management
- ✅ View user profile
- ✅ Update user profile
- ✅ South African user profile support
  - ID Number
  - Medical Aid Information
  - Clinic Affiliation
  - Emergency Contact Details

### Backend Features
- ✅ Global exception handling
- ✅ Standardized API responses
- ✅ Generic `Response<T>` wrapper
- ✅ MySQL database integration
- ✅ Cross-Origin Resource Sharing (CORS) configuration
- ✅ Layered architecture (Controller → Service → Repository)

---

# 🛠️ Technology Stack

| Technology | Version |
|------------|---------|
| Java | 21 |
| Spring Boot | 3.2.0 |
| Spring Security | 3.2.0 |
| Spring Data JPA | 3.2.0 |
| MySQL | 8.x |
| JWT (JJWT) | 0.11.5 |
| Maven | 3.9.x |
| Jakarta Validation | Included |
| Lombok | Latest |

---

# 🏗️ Architecture

The project follows a clean layered architecture:

```
Controller
     │
     ▼
Service
     │
     ▼
Repository
     │
     ▼
MySQL Database
```

This structure improves maintainability, scalability, and separation of concerns.

---

# 📁 Project Structure

```
src
├── controller
├── dto
│   ├── request
│   └── response
├── entity
├── exception
├── repository
├── security
│   ├── config
│   ├── jwt
│   └── service
├── service
├── utils
└── PharmacyBackendApplication.java
```

---

# 🔐 User Roles

The system currently supports three user roles:

| Role | Description |
|------|-------------|
| CUSTOMER | Registers, logs in, and manages their profile |
| PHARMACIST | Manages pharmacy operations *(future implementation)* |
| ADMIN | Administrative access *(future implementation)* |

---

# 🗄️ Database

The application uses **MySQL** with **Spring Data JPA** for persistence.

Core entities include:

- User
- UserProfile

Additional entities such as Medicine, Order, Inventory, Prescription, and Pharmacy will be introduced in future milestones.

---

# 📡 API Features

### Authentication
- Register
- Login

### User
- Get Profile
- Update Profile

All endpoints return a standardized response format:

```json
{
  "success": true,
  "message": "Operation successful",
  "data": {},
  "errors": null,
  "timestamp": 1712345678901
}
```

---

# 🔒 Security

The backend implements:

- JWT Authentication
- BCrypt password hashing
- Spring Security
- Stateless authentication
- Role-based authorization
- Protected API endpoints

---

# 🚧 Roadmap

### Week 2
- Medicine Management
- Pharmacy Management
- Search Medicines

### Week 3
- Shopping Cart
- Orders
- Checkout Process

### Week 4
- Prescription Upload
- Inventory Tracking
- Reporting Dashboard

---

# 👥 Development Team

**Project:** SA MedConnect

Developed as a collaborative Spring Boot backend project following modern Java development practices.

