# 🏡 Real Estate Listings Backend API

This is the backend RESTful API for a real estate listing platform. It enables users to register, authenticate, post rental ads (annonces), manage listings, and allows administrators to validate and moderate them.

Built using Spring Boot, Spring Security, JPA (Hibernate), and MySQL.

---

## 📦 Tech Stack

- Java 17+
- Spring Boot 3+
- Spring Data JPA
- Spring Security (JWT Authentication)
- Hibernate
- MySQL / H2 (for development)
- Lombok
- Maven
- Cloudinary or File System (for image uploads)

---

## 📁 Project Structure

src/
├── main/
│ ├── java/com/daark/backend/
│ │ ├── controller # REST API controllers
│ │ ├── service # Business logic layer
│ │ ├── entity # JPA entity classes (Annonce, User)
│ │ ├── dto # Data Transfer Objects
│ │ ├── repository # Spring Data repositories
│ │ ├── config # Security and app configurations
│ └── resources/
│ └── application.properties

---

## 🔐 Authentication & Authorization

- Secure JWT-based login system
- Role-based access control:
  - CLIENT: can create, view, and delete their own listings
  - ADMIN: can view all listings, approve/reject/delete any listing, manage users

---

## 🧠 Features

✅ User registration & login  
✅ JWT token authentication  
✅ Role-based access control (ADMIN / CLIENT)  
✅ Create, view, and delete listings  
✅ Upload 3–15 images per listing  
✅ View public approved listings  
✅ Admin moderation (accept/reject)  
✅ Admin deletes users (cascade delete their listings)  
✅ Search and filter listings (city, type, price, etc.)

---

## 🧪 API Endpoints Overview

🔐 Auth (no authentication required):

- `POST /api/auth/register` → Register a new user
- `POST /api/auth/login` → Login and receive JWT

👤 User Profile:

- `GET /api/user/me` → Get current user profile
- `PUT /api/user/me/telephone` → Update telephone
- `DELETE /api/user/me` → Delete own account
- `GET /api/user/all` → List all users (ADMIN only)
- `DELETE /api/user/admin/delete-user/{id}` → Delete a user and their listings (ADMIN only)

📄 Annonces (Listings):

- `GET /api/annonces/public` → View all public (ACCEPTEE) listings (no authentication required)
- `GET /api/annonces` → View own listings (ADMIN can see all)
- `POST /api/annonces/create` → Create a listing (requires 3–15 images)
- `DELETE /api/annonces/{id}` → Delete own listing (or any listing if ADMIN)
- `PUT /api/annonces/statut/{id}` → Update listing status (ADMIN only)
- `GET /api/annonces/user/{userId}` → View all listings of a specific user (ADMIN only)
- `GET /api/annonces/public?ville=Fès&typeLocation=mensuel` → Filtered search

---

## 🏗 Sample JSON (AnnonceStatutRequest)

```json
{
  "statut": "ACCEPTEE"
}
Available values:

EN_ATTENTE

ACCEPTEE

REFUSEE

🛠 Setup & Run
1. Clone the repository:
git clone https://github.com/Hibanj8/daark-backend.git
cd daark-backend

2. Create a MySQL database:
CREATE DATABASE real_estate;

3. Update your application.properties:
spring.datasource.url=jdbc:mysql://localhost:3306/real_estate
spring.datasource.username=root
spring.datasource.password=your_password
app.upload.dir=uploads/

4. Run the project:
mvn spring-boot:run
The API will be available at: http://localhost:8080

🔐 Testing Authentication (JWT)
1. Register a user → POST /api/auth/register
2. Login to get a token → POST /api/auth/login
3. Use the token in protected requests:
   Authorization: Bearer <your_token>

🧑‍💻 Developer Notes
Image uploads are saved under /uploads directory

You can switch to Cloudinary integration if preferred

Role management (ADMIN/CLIENT) can be updated via the DB manually

Access control enforced with @PreAuthorize annotations

📄 License
This project is for educational and demonstration purposes. Feel free to fork and adapt it for your own use.




