# 🚗 Vehicle Rental Management System

[![My Skills](https://skillicons.dev/icons?i=py,docker,spring,elasticsearch,git,github,postgres,postman,angular,bootstrap,css,html,idea,npm)](https://skillicons.dev)

> A full-stack, multi-tenant vehicle rental platform — secure, scalable, and production-ready.


## 🌟 Overview

The **Vehicle Rental Management System** is a comprehensive full-stack solution for managing vehicle rentals, repairs, subscriptions, and user roles in a multi-tenant environment.

| Layer | Tech | Purpose |
|---|---|---|
| **Backend** | Spring Boot 3.3.4 + Java 21 | REST API, business logic, auth |
| **Frontend** | Angular 21 + TypeScript | User interfaces for all roles |
| **Auth** | Keycloak 25.0.0 | OAuth2 / JWT / SSO |
| **Database** | PostgreSQL 17 | Persistent data storage |
| **Search** | Elasticsearch 8.19.8 | Session tracking & analytics |
| **Container** | Docker + Docker Compose | Deployment orchestration |

### Who is it for?
- 🏢 **Vehicle rental businesses** looking to streamline operations
- 👨‍💻 **Developers** building scalable rental management systems
- 🚀 **Startups** needing a complete full-stack mobility platform

---

## ✨ Key Features

### 🔑 Authentication & Security
- **OAuth2 with Keycloak** for secure user management and SSO
- **Role-based access control** — Admin, Supplier, Repairer, Client
- **JWT token conversion** for enhanced API security
- **CORS configuration** for flexible frontend integration

### 🚗 Vehicle Management
- **Comprehensive vehicle inventory** with categories and status tracking
- **Booking & rental system** with flexible periods and tracking
- **Subscription plans** for clients with customizable tiers
- **Multi-location support** for global operations

### 🔧 Repair & Maintenance
- **Ticketing system** for repair requests and tracking
- **Status updates** with real-time repair progression
- **Multi-repairer support** for distributed maintenance teams
- **Demand management** for incoming repair requests

### 🏢 Multi-Tenant Architecture
- **Admin dashboard** — users, locations, system configurations
- **Supplier portal** — vehicle inventory and category management
- **Repairer portal** — maintenance operations and ticket handling
- **Client portal** — booking, subscriptions, and repair tracking

### 📊 Analytics & Monitoring
- **User session tracking** via Elasticsearch integration
- **GeoIP integration** for location-based analytics
- **Data visualization** with charts and business insights
- **Full API documentation** via Swagger / OpenAPI

---

## 🛠️ Full Tech Stack

### 📦 Backend — `vehicule-rental-backend`

[![My Skills](https://skillicons.dev/icons?i=java,spring,postgres,docker,elasticsearch,github)](https://skillicons.dev)

| Category | Technology | Version |
|---|---|---|
| Language | Java | 21 |
| Framework | Spring Boot | 3.3.4 |
| Database | PostgreSQL | 17 |
| Auth | Keycloak | 25.0.0 |
| Search | Elasticsearch | 8.19.8 |
| Build | Maven | 3.9.9 |
| Docs | SpringDoc OpenAPI | — |
| Testing | JUnit, H2 (in-memory) | — |
| Utilities | Lombok, Jackson, GeoIP2 | — |

### 🎨 Frontend — `vehicule-rent-frontend`

[![My Skills](https://skillicons.dev/icons?i=angular,ts,bootstrap,css,html,docker,npm,github)](https://skillicons.dev)

| Category | Technology | Version |
|---|---|---|
| Framework | Angular | 21 |
| Language | TypeScript | — |
| UI | Angular Material + Bootstrap | 5 |
| Auth | Keycloak.js | — |
| State | RxJS | — |
| Styling | CSS / SCSS | — |
| Icons | Eva Icons, Lucide | — |
| Testing | Jasmine, Karma | — |

---

## 📦 Installation & Quick Start

### Prerequisites

| Tool | Version | Required For |
|---|---|---|
| Java JDK | 21 | Backend |
| Maven | 3.9.9 | Backend |
| Node.js | v18.16.0+ | Frontend |
| Angular CLI | v20.2.0+ | Frontend |
| PostgreSQL | 17 | Backend |
| Docker | Latest | Both (optional) |
| Keycloak | 25.0.0 | Both |

---

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/your-org/vehicle-rental.git
cd vehicle-rental
```

---

### 2️⃣ Backend Setup — `vehicule-rental-backend`

#### Environment Variables

Create a `.env` file in `vehicule-rental-backend/`:

```env
# Database
DB_URL=jdbc:postgresql://localhost:5432/vehicule_rental
DB_USER=your_username
DB_PASSWORD=your_password

# Keycloak
KEYCLOAK_SERVER_URL=http://localhost:8080
KEYCLOAK_REALM=vehicule-app
KEYCLOAK_CLIENT_ID=vehicule-rental-client
KEYCLOAK_CLIENT_SECRET=your_client_secret

# Elasticsearch
ELASTICSEARCH_HOST=http://localhost:9200

# CORS
ALLOWED_ORIGIN=http://localhost:4200
```

#### Run Locally

```bash
cd vehicule-rental-backend
mvn clean install
mvn spring-boot:run
# API available at http://localhost:8090
# Swagger UI at http://localhost:8090/swagger-ui.html
```

#### Run with Docker

```bash
cd vehicule-rental-backend
docker-compose up --build
```

---

### 3️⃣ Frontend Setup — `vehicule-rent-frontend`

#### Environment Configuration

Edit `src/environments/environment.ts`:

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8090/api',
  keycloakConfig: {
    url: 'http://localhost:8080/auth',
    realm: 'vehicule-app',
    clientId: 'frontend'
  }
};
```

#### Run Locally

```bash
cd vehicule-rent-frontend
npm install
npm start
# App available at http://localhost:4200
```

#### Run with Docker

```bash
cd vehicule-rent-frontend
docker build -t vehicule-rent-frontend .
docker run -p 4200:4200 vehicule-rent-frontend
```

---

### 4️⃣ Run Full Stack with Docker Compose

To spin up the entire system (backend + frontend + database + Keycloak + Elasticsearch):

```bash
docker-compose up --build
```

| Service | URL |
|---|---|
| Frontend | http://localhost:4200 |
| Backend API | http://localhost:8090 |
| Swagger UI | http://localhost:8090/swagger-ui.html |
| Keycloak | http://localhost:8080 |
| Elasticsearch | http://localhost:9200 |

---

## 🔧 Configuration

### Keycloak Setup

1. **Create a Realm** named `vehicule-app`
2. **Create a Client** named `vehicule-rental-client`:
   - Protocol: `openid-connect`
   - Standard Flow Enabled: `true`
   - Direct Access Grants Enabled: `true`
   - Valid Redirect URIs: `http://localhost:8090/*`
   - Web Origins: `http://localhost:4200`
3. **Create Roles**: `ADMIN`, `SUPPLIER`, `REPAIR`, `CLIENT`

### Elasticsearch Setup

```bash
curl -X PUT "http://localhost:9200/user_login_sessions" \
  -H 'Content-Type: application/json' \
  -d @vehicule-rental-backend/elastisearch/create_index.txt
```

### Environment Variables Reference

| Variable | Description | Example |
|---|---|---|
| `DB_URL` | Database connection URL | `jdbc:postgresql://localhost:5432/vehicule_rental` |
| `DB_USER` | Database username | `postgres` |
| `DB_PASSWORD` | Database password | `securePassword` |
| `KEYCLOAK_SERVER_URL` | Keycloak server URL | `http://localhost:8080` |
| `KEYCLOAK_REALM` | Keycloak realm name | `vehicule-app` |
| `KEYCLOAK_CLIENT_ID` | Keycloak client ID | `vehicule-rental-client` |
| `KEYCLOAK_CLIENT_SECRET` | Keycloak client secret | `your_secret` |
| `ELASTICSEARCH_HOST` | Elasticsearch host URL | `http://localhost:9200` |
| `ALLOWED_ORIGIN` | Allowed CORS origins | `http://localhost:4200` |

---

## 🤝 Contributing

We welcome contributions from the community!

### Workflow

```bash
# 1. Fork the repository
# 2. Create your feature branch
git checkout -b feature/your-feature-name

# 3. Commit your changes
git commit -m 'feat: add some feature'

# 4. Push to your branch
git push origin feature/your-feature-name

# 5. Open a Pull Request
```

### Code Style Guidelines

**Backend:**
- Follow the **Google Java Style Guide**
- Use **Lombok** annotations to reduce boilerplate
- Write **unit tests** with JUnit for all new features
- Use **SLF4J** for consistent logging

**Frontend:**
- Follow the **Angular Style Guide**
- Use **TypeScript best practices** for type safety
- Keep components focused and single-responsibility
- Write tests with **Jasmine / Karma**

### Pull Request Process

1. ✅ All existing tests pass (`mvn test` / `npm test`)
2. ✅ New tests written for new features
3. ✅ Code follows the project style guidelines
4. ✅ Clear and descriptive PR description
5. ✅ Documentation updated if needed


## 🚀 Get Started

1. ⭐ **Star this repository** to show your support
2. 🍴 **Fork the repository** to start contributing
3. 🐛 **Open an issue** for bugs or feature requests
4. 📥 **Submit a Pull Request** to share your improvements

Thank you for your interest in the Vehicle Rental Management System. Together, we can build a robust and scalable solution for vehicle rental businesses worldwide. 🚗💨
