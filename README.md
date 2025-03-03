# Book Store Management System

This project is a **Book Store Management System** built using **Spring Boot, Thymeleaf, Spring Security, Mail,Razor pay, and MySQL**. The system allows an **admin** to manage store managers and staff, while **managers** handle book categories, books, and staff. Orders placed by users are assigned to available delivery staff by the store manager, and completion is verified via OTP.

## Features

### **Admin Features**
- Create and manage store managers.
- Oversee store operations and reports.

### **Manager Features**
- Manage book categories.
- Add, update, and remove books.
- Assign staff roles and responsibilities.
- Handle orders by assigning delivery staff.

### **Staff Features**
- Process book orders.
- Deliver orders to customers.
- Verify OTP for order completion.

### **User Features**
- Browse and search for books.
- Place orders.
- Track order status.
- Provide OTP upon order receipt.

## Technologies Used
- **Spring Boot** - Backend Framework
- **Thymeleaf** - Template Engine for UI
- **Spring Security** - Authentication & Authorization
- **Spring Mail** - Email notifications
- **MySQL** - Database Management

## Installation & Setup

### **Prerequisites**
- Java 17+
- Maven
- MySQL Database

### **Clone the Repository**
```sh
git clone https://github.com/your-repo/book-store-management.git
cd book-store-management
```

### **Configure Database**
Modify `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bookstoredb
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

### **Build & Run the Application**
```sh
mvn clean install
mvn spring-boot:run
```

### **Access the Application**
- **Admin Panel**: `http://localhost:8080/admin`
- **Manager Panel**: `http://localhost:8080/manager`
- **Staff Dashboard**: `http://localhost:8080/staff`
- **User Store**: `http://localhost:8080/store`

## Order Flow
1. User places an order.
2. The store manager receives the order.
3. The manager assigns an available delivery staff.
4. Staff delivers the order and verifies OTP.
5. Order is marked **completed**.

## Future Enhancements
- Implement real-time notifications.
- AI-based book recommendations.

---
This project provides an efficient way to **manage book stores, staff, and orders**, ensuring a smooth ordering and delivery process. 🚀

