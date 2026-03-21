# 🚗 Vehicle Rental Management System - Backend

[![My Skills](https://skillicons.dev/icons?i=py,docker,spring,elasticsearch,git,github,idea,postgres,postman)](https://skillicons.dev)


---

## 🌟 Overview

Welcome to **Vehicle Rental Management System Backend**! This comprehensive Spring Boot application provides a robust backend solution for managing vehicle rentals, repairs, subscriptions, and user roles in a multi-tenant environment.

### What is it?
A **fully-featured backend** for managing vehicle rentals with:
- **Multi-role access control** (Admin, Supplier, Repairer, Client)
- **Complete vehicle lifecycle management** (rentals, repairs, subscriptions)
- **Secure authentication** with OAuth2 and Keycloak integration
- **Elasticsearch integration** for session tracking and analytics
- **Comprehensive API documentation** with Swagger/OpenAPI

### Who is it for?
- **Vehicle rental businesses** looking to streamline operations
- **Developers** who want to build scalable rental management systems
- **Startups** needing a complete backend solution for their mobility platform

---

## ✨ Key Features

### 🔑 Authentication & Security
- **OAuth2 with Keycloak** integration for secure user management
- **Role-based access control** with fine-grained permissions
- **JWT token conversion** for enhanced security
- **CORS configuration** for flexible frontend integration

### 🚗 Vehicle Management
- **Comprehensive vehicle inventory** with categories and statuses
- **Rental system** with booking, tracking, and flexible periods
- **Subscription plans** for clients with customizable tiers
- **Multi-location support** for global operations

### 🔧 Repair & Maintenance
- **Ticketing system** for repair requests
- **Repair tracking** with status updates
- **Multi-repairer support** for distributed maintenance
- **Demand management** for repair requests

### 🏢 Multi-Tenant Architecture
- **Admin dashboard** for managing users, locations, and system configurations
- **Supplier management** for vehicle inventory and categories
- **Repairer management** for maintenance operations
- **Client portal** for booking, subscriptions, and repair tracking

### 📊 Analytics & Monitoring
- **User login session tracking** with Elasticsearch integration
- **GeoIP integration** for location-based analytics
- **Comprehensive API documentation** with Swagger/OpenAPI

---

## 🛠️ Tech Stack

### Core Technologies
- **Language**: Java 21
- **Framework**: Spring Boot 3.3.4
- **Database**: PostgreSQL 17 (with H2 for testing)
- **Build Tool**: Maven 3.9.9
- **Authentication**: Keycloak 25.0.0
- **Search**: Elasticsearch 8.19.8
- **API Documentation**: SpringDoc OpenAPI

### Key Dependencies
- **Spring Security**: OAuth2 client and resource server
- **Spring Data JPA**: For database operations
- **Lombok**: For reducing boilerplate code
- **Jackson**: For JSON processing
- **GeoIP2**: For location-based services
- **Test Frameworks**: JUnit, Spring Boot Test

### Additional Tools
- **Docker**: For containerization
- **Swagger UI**: For API documentation
- **Keycloak**: For identity and access management

---

## 📦 Installation

### Prerequisites

Before you begin, ensure you have the following installed on your system:

- **[Java JDK 21](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)**
- **[PostgreSQL 17](https://www.postgresql.org/download/)**
- **[Maven 3.9.9](https://maven.apache.org/download.cgi)**
- **[Docker](https://www.docker.com/get-started)** (for containerized deployment)
- **[Keycloak](https://www.keycloak.org/download)** (for authentication)

### Quick Start

#### 1. Clone the Repository
```bash
git clone https://github.com/kerfaiyass54/vehicule-rental-backend.git
cd vehicule-rental-backend
```

#### 2. Set Up Environment Variables
Create a `.env` file in the root directory with the following variables:

```env
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/vehicule_rental
DB_USER=your_username
DB_PASSWORD=your_password

# Keycloak Configuration
KEYCLOAK_SERVER_URL=http://localhost:8080
KEYCLOAK_REALM=vehicule-app
KEYCLOAK_CLIENT_ID=vehicule-rental-client
KEYCLOAK_CLIENT_SECRET=your_client_secret

# Elasticsearch Configuration
ELASTICSEARCH_HOST=http://localhost:9200

# Application Configuration
ALLOWED_ORIGIN=http://localhost:3000
```

#### 3. Build the Project
```bash
mvn clean install
```

#### 4. Run the Application
```bash
mvn spring-boot:run
```

#### 5. Run with Docker (Alternative)
```bash
docker-compose up --build
```



## 🔧 Configuration

### Environment Variables
The application uses environment variables for configuration. Here are the key ones:

| Variable                     | Description                                  | Example Value                     |
|------------------------------|----------------------------------------------|-----------------------------------|
| `DB_URL`                     | Database connection URL                      | `jdbc:postgresql://localhost:5432/vehicule_rental` |
| `DB_USER`                    | Database username                            | `postgres`                        |
| `DB_PASSWORD`                | Database password                            | `securePassword`                  |
| `KEYCLOAK_SERVER_URL`        | Keycloak server URL                          | `http://localhost:8080`           |
| `KEYCLOAK_REALM`             | Keycloak realm name                          | `vehicule-app`                    |
| `KEYCLOAK_CLIENT_ID`         | Keycloak client ID                           | `vehicule-rental-client`          |
| `KEYCLOAK_CLIENT_SECRET`     | Keycloak client secret                       | `your_client_secret`              |
| `ELASTICSEARCH_HOST`         | Elasticsearch host URL                       | `http://localhost:9200`           |
| `ALLOWED_ORIGIN`             | Allowed CORS origins                         | `http://localhost:3000`           |

### Keycloak Setup
1. **Create a Realm**: Create a realm named `vehicule-app` in Keycloak.
2. **Create a Client**: Create a client named `vehicule-rental-client` with the following settings:
   - **Client Protocol**: `openid-connect`
   - **Standard Flow Enabled**: `true`
   - **Direct Access Grants Enabled**: `true`
   - **Valid Redirect URIs**: `http://localhost:8090/*`
   - **Web Origins**: `http://localhost:3000`

3. **Create Roles**: Create roles for each user type (ADMIN, SUPPLIER, REPAIR, CLIENT).

### Elasticsearch Setup
1. **Create Index**: Use the provided script in `elastisearch/create_index.txt` to create the `user_login_sessions` index.
   ```bash
   curl -X PUT "http://localhost:9200/user_login_sessions" -H 'Content-Type: application/json' -d @elastisearch/create_index.txt
   ```

---

## 🤝 Contributing

We welcome contributions from the community! Here's how you can contribute to the project:

### How to Contribute
1. **Fork the Repository**: Create your own copy of the project.
2. **Create a Feature Branch**: `git checkout -b feature/your-feature-name`
3. **Commit Your Changes**: `git commit -m 'Add some feature'`
4. **Push to the Branch**: `git push origin feature/your-feature-name`
5. **Open a Pull Request**: Describe your changes and submit the PR.

### Development Setup
1. **Clone the Repository**: Follow the quick start instructions above.
2. **Set Up Your IDE**: Use IntelliJ IDEA, Eclipse, or VS Code with Maven support.
3. **Run Tests**: `mvn test`
4. **Build the Project**: `mvn clean package`

### Code Style Guidelines
- Follow the **Java Code Conventions** (Google Style Guide).
- Use **Lombok** annotations for reducing boilerplate code.
- Write **unit tests** for all new features.
- Ensure **consistent logging** using SLF4J.

### Pull Request Process
1. **Ensure your code follows the style guidelines**.
2. **Write clear commit messages**.
3. **Include tests** for your changes.
4. **Document any new features or changes** in the README or relevant documentation.
5. **Submit your PR** with a clear description of the changes.

---


## 🚀 Getting Started

Ready to get started? Follow these steps to contribute or use the Vehicle Rental Management System:

1. **Star the Repository**: Show your support by starring this project.
2. **Fork the Repository**: Create your own copy to start contributing.
3. **Explore the Code**: Dive into the codebase and start making changes.
4. **Submit a Pull Request**: Share your improvements with the community.

Thank you for your interest in the Vehicle Rental Management System! Together, we can build a robust and scalable solution for vehicle rental businesses worldwide. 🚀
```

This README.md file is designed to be comprehensive, engaging, and easy to follow. It includes all the necessary sections to guide developers through the project, from installation to contributing. The use of emojis, clear code snippets, and practical examples makes it visually appealing and easy to navigate.
