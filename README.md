# Perspectia Backend

Backend API for Perspectia - A platform for sharing daily perspectives and thought-provoking discussions.

## 📋 Overview

Perspectia Backend is a Spring Boot-based REST API that powers a daily discussion platform. The application generates AI-powered topics every day, allows users to share their perspectives, and automatically generates AI summaries of the day's discussions.

## ✨ Features

- **AI-Powered Topic Generation**: Automatically generates thought-provoking discussion topics daily at 9:00 AM IST using Spring AI
- **User Authentication**: 
  - Email/Password authentication with OTP verification
  - Google OAuth integration
  - GitHub OAuth integration
  - JWT-based session management with refresh tokens
- **Perspective Management**: Users can share their thoughts on daily topics
- **AI Summary Generation**: Automatically generates summaries of daily discussions at 9:00 PM IST
- **GraphQL API**: Support for GraphQL queries alongside REST endpoints
- **Scheduled Tasks**: Automated topic generation and summary creation using Spring Scheduler

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.5.9
- **Language**: Java 21
- **Database**: PostgreSQL
- **Security**: Spring Security with JWT
- **AI Integration**: Spring AI 1.1.2
- **GraphQL**: Spring GraphQL
- **Build Tool**: Maven
- **Containerization**: Docker

## 📦 Key Dependencies

- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- Spring Boot Starter GraphQL
- Spring AI
- PostgreSQL Driver
- JWT (jsonwebtoken)
- Lombok
- Spring Boot DevTools

## 🚀 Getting Started

### Prerequisites

- Java 21 or higher
- PostgreSQL database
- Maven 3.x
- Docker (optional)

### Environment Variables

Configure the following environment variables:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/perspectia
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password

# AI Configuration
spring.ai.openai.api-key=your_openai_api_key

# OAuth Configuration
google.api.client.id=your_google_client_id
github.client.id=your_github_client_id
github.client.secret=your_github_client_secret

# JWT Configuration
jwt.secret=your_jwt_secret
```

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/rishabhrawat05/Perspectia-Backend.git
   cd Perspectia-Backend
   ```

2. **Build the project**
   ```bash
   ./mvnw clean install
   ```

3. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

### Docker Deployment

Build and run using Docker:

```bash
docker build -t perspectia-backend .
docker run -p 8080:8080 perspectia-backend
```

## 📡 API Endpoints

### Authentication

- `POST /api/perspectia/auth/signup` - User registration
- `POST /api/perspectia/auth/login` - User login
- `POST /api/perspectia/auth/verify/email` - Verify email with OTP
- `POST /api/perspectia/auth/resend/otp` - Resend OTP
- `POST /api/perspectia/auth/google-login` - Google OAuth login
- `POST /api/perspectia/auth/refreshtoken/generate` - Generate refresh token

### Topics

- `GET /api/perspectia/topic/latest` - Get today's discussion topic
- `GET /api/perspectia/topic/random` - Generate a random topic (admin/testing)

### Perspectives

- `POST /api/perspectia/perspective` - Share a perspective
- `GET /api/perspectia/perspective/{id}` - Get a specific perspective
- `GET /api/perspectia/perspectives` - Get all perspectives (paginated)

### AI Summary

- `GET /api/perspectia/summary/latest` - Get the latest AI-generated summary

## ⏰ Scheduled Tasks

The application runs two scheduled tasks in IST timezone:

1. **Daily Topic Generation** - Every day at 9:00 AM IST
   - Generates a new thought-provoking topic using AI
   - Creates a new topic entry in the database

2. **Daily Summary Generation** - Every day at 9:00 PM IST
   - Collects all perspectives from the day
   - Generates an AI-powered summary of the discussion
   - Requires minimum 3 valid perspectives (>50 characters)

## 🏗️ Project Structure

```
src/main/java/com/perspectia/perspectiabackend/
├── controllers/         # REST API controllers
├── services/           # Business logic layer
├── repositories/       # Data access layer
├── models/            # Entity classes
├── requests/          # Request DTOs
├── responses/         # Response DTOs
├── exceptions/        # Custom exceptions
├── enums/            # Enum definitions
└── config/           # Configuration classes
```

## 🔐 Security

- JWT-based authentication
- Password encryption using BCrypt
- CORS configuration for cross-origin requests
- Role-based access control
- Secure OAuth2 integration

## 🧪 Testing

Run tests using:

```bash
./mvnw test
```

## 👨‍💻 Developer

**Rishabh Rawat** ([@rishabhrawat05](https://github.com/rishabhrawat05))

## 🤝 Contributing

Contributions, issues, and feature requests are welcome! Feel free to check the [issues page](https://github.com/rishabhrawat05/Perspectia-Backend/issues).

## 📧 Contact

For any questions or feedback, please open an issue on GitHub.

---

Built with ❤️ using Spring Boot and Spring AI
