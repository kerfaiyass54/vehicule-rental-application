
# 🚗 Vehicule Rent Frontend
[![My Skills](https://skillicons.dev/icons?i=docker,angular,bootstrap,css,git,github,html,idea,npm)](https://skillicons.dev)

**A comprehensive vehicle rental management system frontend** built with Angular and TypeScript, designed for admins, repairers, suppliers, and clients.

---

## 🚀 Overview

The **Vehicule Rent Frontend** is a sophisticated Angular application that provides a complete interface for managing a vehicle rental business. This system empowers different user roles with specialized dashboards and functionality:

- **Admin**: Manage users, locations, and system-wide operations
- **Repairer**: Track and update vehicle repair statuses
- **Supplier**: Manage inventory and subscription details
- **Client**: Book and track vehicle rentals

With modern UI components, responsive design, and secure authentication via Keycloak, this application delivers a seamless experience across all devices.

---

## ✨ Key Features

✅ **Multi-role Access Control** - Tailored dashboards for each user type
✅ **Comprehensive Vehicle Management** - Track inventory, repairs, and locations
✅ **Client Booking System** - Intuitive interface for rentals and reservations
✅ **Material Design** - Professional, modern UI components
✅ **Keycloak Integration** - Secure authentication and authorization
✅ **Responsive Design** - Works perfectly on all devices
✅ **Data Visualization** - Charts and analytics for business insights
✅ **Step-by-Step Forms** - Intuitive client and location creation workflows

---

## 🛠️ Tech Stack

**Core Technologies:**
- **Framework**: Angular 21
- **Language**: TypeScript
- **UI Components**: Angular Material, Bootstrap 5
- **State Management**: RxJS
- **Authentication**: Keycloak.js

**Development Tools:**
- **Build System**: Angular CLI
- **Testing**: Jasmine, Karma
- **Containerization**: Docker
- **Styling**: CSS, SCSS
- **Icons**: Eva Icons, Lucide

**System Requirements:**
- Node.js v18.16.0+
- Angular CLI v20.2.0+
- Docker (for containerized deployment)

---

## 📦 Installation

### Prerequisites

Ensure you have the following installed:
- [Node.js](https://nodejs.org/) (v18.16.0 or higher)
- [Angular CLI](https://angular.io/cli) (v20.2.0 or higher)
- [Docker](https://www.docker.com/) (optional, for containerized deployment)

### Quick Start

```bash
# Clone the repository
git clone https://github.com/yourusername/vehicule-rent-frontend.git
cd vehicule-rent-frontend

# Install dependencies
npm install

# Start the development server
npm start

# The application will be available at http://localhost:4200
```

### Alternative Installation Methods

#### Docker Setup

```bash
# Build the Docker image
docker build -t vehicule-rent-frontend .

# Run the container
docker run -p 4200:4200 vehicule-rent-frontend
```

#### Development Setup

```bash
# Install all dependencies (including dev dependencies)
npm ci

# Run tests
npm test

# Build for production
npm run build
```


## 🔧 Configuration

### Environment Variables

Configure environment-specific settings in `src/environments/`:

```typescript
// environment.ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api',
  keycloakConfig: {
    url: 'http://localhost:8080/auth',
    realm: 'vehicule-rent',
    clientId: 'frontend'
  }
};
```

### Keycloak Configuration

Configure Keycloak in your `app.module.ts`:

```typescript
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';

@NgModule({
  imports: [
    KeycloakAngularModule
  ],
  providers: [
    {
      provide: 'keycloakConfig',
      useValue: {
        config: {
          url: 'http://localhost:8080/auth',
          realm: 'vehicule-rent',
          clientId: 'frontend'
        }
      }
    }
  ]
})
export class AppModule {}
```

### Customization Options

1. **Theming**: Customize colors in `src/styles.css` or Angular Material theme files
2. **Routing**: Modify `app-routing.module.ts` to change application flow
3. **API Endpoints**: Update environment variables for different API configurations

---

## 🤝 Contributing

We welcome contributions from the community! Here's how you can help:

### How to Contribute

1. **Fork** the repository
2. **Clone** your fork locally
3. **Create** a new branch for your feature/bugfix
4. **Commit** your changes with descriptive messages
5. **Push** to your fork
6. **Open** a Pull Request to the main repository

### Development Setup

```bash
# Clone the repository
git clone https://github.com/yourusername/vehicule-rent-frontend.git
cd vehicule-rent-frontend

# Install dependencies
npm install

# Start the development server
npm start
```

### Code Style Guidelines

1. Follow **Angular Style Guide** for component structure
2. Use **TypeScript best practices** for type safety
3. Maintain **consistent formatting** (prettier is configured)
4. Write **comprehensive tests** for new features
5. Keep **commit messages** clear and descriptive

### Pull Request Process

1. Ensure your code passes all tests
2. Update documentation if adding new features
3. Follow the existing code style
4. Create a clear PR description explaining your changes
5. Reference any related issues

---


### FAQ

**Q: How do I deploy this application?**
A: You can deploy using Docker (see installation) or build the production files with `npm run build` and deploy to any static hosting service.

**Q: How do I add a new role?**
A: Create a new module under `src/app/` with role-specific components, then add routes in `app-routing.module.ts`.

**Q: How do I customize the UI?**
A: Modify the Angular Material theme in `angular.json` or create custom styles in `src/styles.css`.

---


## 🎉 Get Started Today!

Ready to transform your vehicle rental business? Fork this repository, customize it to fit your needs, and start building your perfect rental management system.

👉 **Star this repository** to show your support and stay updated with the latest developments!

💬 **Join our community** to discuss features, share ideas, and get help with implementation.

🚀 **Contribute** by submitting pull requests or opening issues - every contribution helps make this project better!
```
