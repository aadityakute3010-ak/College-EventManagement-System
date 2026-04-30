# 🎯 Event Management System API

A **full-stack backend REST API system** built using Spring Boot for managing college/university events, student registrations, payments, and automated notifications.

This project simulates a **real-world event automation system** with features like OTP authentication, email notifications, payment integration, and intelligent event recommendations.

---

# 🚀 Key Features

## 👨‍🎓 Student Module
- Student registration & authentication
- OTP-based password reset via email
- Secure login system

## 🎟️ Event Module
- Create, update, delete events (Admin side)
- Event categorization
- Event scheduling system

## 📝 Registration Module
- Students can register for events
- Registration status: PENDING / APPROVED / REJECTED
- Auto-block registration for past events

## 📊 Smart Features
- 🎯 Event recommendation based on past registrations
- ⏰ Automated reminder emails before event (scheduled jobs)
- 🔒 Duplicate registration prevention

## 💳 Payment System
- Razorpay payment gateway integration
- Payment confirmation triggers:
  - Email receipt
  - Registration confirmation

## 📧 Email System
- Registration confirmation emails
- Approval / rejection notifications
- OTP email for password reset
- Event reminder emails

## 📄 PDF Generation
- Auto-generated PDF receipt after successful payment
- Sent via email to student

---

# 🛠️ Tech Stack

- Java 17+
- Spring Boot
- Spring MVC
- Spring Data JPA (Hibernate)
- MySQL
- Maven
- Razorpay API
- JavaMailSender
- Postman (API testing)

---

# 🧱 Project Architecture
## 📁 Project Structure

## 🏗️ Project Architecture
```text
college-event-management/
├── src/
│   ├── main/
│   │   ├── java/com/eventapp/
│   │   │   ├── controller/      # REST API Endpoints
│   │   │   ├── service/         # Business Logic Layer
│   │   │   ├── repository/      # JPA Data Access
│   │   │   ├── model/           # Database Entities (Student, Event, etc.)
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── security/        # JWT & Spring Security Config
│   │   │   ├── config/          # Mail & General Configs
│   │   │   ├── exception/       # Global Exception Handling
│   │   │   └── EventManagementApplication.java
│   │   └── resources/
│   │       └── application.properties
├── postman/
│   └── event-management.postman_collection.json
├── pom.xml
└── README.md
```


### Layers Explained:
- **Controller** → Handles HTTP requests
- **Service** → Business logic implementation
- **Repository** → Database interaction
- **DTO Layer** → Clean request/response handling
- **Model Layer** → Entity mapping with database

---

# 📮 API Testing (Postman)

A ready-to-use Postman collection is included:


### How to use:
1. Open Postman
2. Click Import
3. Select the JSON file
4. Start testing APIs immediately

---

# 🔐 Security & Validation

- Email-based authentication flow
- OTP verification for password reset
- Input validation using DTOs
- Exception handling with custom responses
- Role-based APIs access

---

# 📊 Future Enhancements

- Role-based access (Admin / Student / Organizer)
- Frontend dashboard (React/Angular)
- Event analytics dashboard
- QR-based event check-in system
- Feedback & rating system 

---

# 👨‍💻 Author

**Aditya Kute**  
Java Backend Developer (Spring Boot)

---

# ⭐ Project Highlights (What makes it stand out)

✔ Real-world system design  
✔ Payment gateway integration  
✔ Email automation system  
✔ Scheduled tasks (reminders)  
✔ Clean layered architecture  
✔ Production-style API structure  