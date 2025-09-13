🛒 Order Management System
A professional Spring Boot REST API for managing e-commerce orders with modern Java features and production-ready architecture.
🚀 Features

Complete Order CRUD Operations - Create, read, update, and delete orders
Advanced Search & Filtering - Search orders by customer, status, amount, and date ranges
Pagination Support - Handle large datasets efficiently
Input Validation - Comprehensive request validation with detailed error messages
Global Exception Handling - Professional error responses with proper HTTP status codes
Multiple Database Support - H2 for development, PostgreSQL for production
Environment Profiles - Separate configurations for development, testing, and production
Comprehensive Testing - Unit tests for repositories and controllers

🛠️ Technology Stack

Java 17 - Modern LTS version with latest features
Spring Boot 3.2.x - Latest Spring Boot with enhanced performance
Spring Data JPA - Simplified data access with custom queries
H2 Database - In-memory database for development
PostgreSQL - Production-ready database
Maven - Dependency management and build tool
JUnit 5 - Comprehensive testing framework

📚 API Endpoints
Order Management

POST /api/v1/orders/create-order - Create new order
GET /api/v1/orders/getOrders - List orders with pagination
GET /api/v1/orders/{id} - Get specific order with pagination
PUT /api/v1/orders/{id} - Update existing order
DELETE /api/v1/orders/{id} - Delete order
PATCH /api/v1/orders/{id}?status=... - Update order status

Search & Analytics

GET /api/v1/orders/search - Advanced search with multiple criteria
GET /api/v1/orders/stats - Order statistics and reporting

System Health

GET /actuator/health - Detailed system health

GET /api/v1/health - Application health check
GET /actuator/health - Detailed system health
