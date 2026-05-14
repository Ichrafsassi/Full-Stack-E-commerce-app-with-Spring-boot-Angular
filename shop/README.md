# Full-Stack E-Commerce App

Monorepo built with Nx containing:

- `apps/ecom-frontend`: Angular 21 SSR storefront styled with Tailwind CSS, DaisyUI, and Font Awesome
- `apps/ecom-backend`: Spring Boot 3.5 REST API with JWT authentication, seeded admin user, and role-based admin endpoints

## Finished Features

### Frontend

- Responsive storefront landing page
- Product catalog with search, category filters, and sorting
- Persistent cart stored in browser local storage
- Checkout flow with order summary and confirmation
- Registration and login flows connected to the backend JWT API
- Account page with authenticated profile loading and local order history
- Admin users page for promoting and demoting roles

### Backend

- JWT login and registration
- Role-protected `/api/admin/users` endpoints
- `/api/users/me` authenticated profile endpoint
- Seeded admin account on startup
- Validation and JSON error responses for auth, validation, and access control failures
- H2 dev profile and PostgreSQL-ready production profile

## Tech Stack

- Angular 21 SSR
- Nx
- Tailwind CSS
- DaisyUI
- Font Awesome
- Spring Boot 3.5
- Spring Security
- Spring Data JPA
- H2
- PostgreSQL
- JWT

## Local Run

### 1. Install frontend dependencies

```bash
npm install
```

### 2. Start the backend

From `apps/ecom-backend`:

```bash
./mvnw spring-boot:run
```

The API runs on `http://localhost:8081`.

### 3. Start the frontend

From the workspace root:

```bash
npx nx serve ecom-frontend
```

The storefront runs on `http://localhost:4200`.

## Demo Credentials

- Admin email: `admin@local`
- Admin password: `admin12345`

You can change these with environment variables:

```bash
APP_ADMIN_EMAIL=admin@local
APP_ADMIN_PASSWORD=admin12345
APP_JWT_SECRET=your-very-long-secret-key-here
APP_CORS_ALLOWED_ORIGINS=http://localhost:4200
```

## Build And Verify

### Frontend build

```bash
npx nx build ecom-frontend --configuration development
```

### Backend tests

From `apps/ecom-backend`:

```bash
./mvnw test
```

## API Overview

### Auth

- `POST /api/auth/register`
- `POST /api/auth/login`

### User

- `GET /api/users/me`

### Admin

- `GET /api/admin/users`
- `PUT /api/admin/users/{id}/role`

## Notes

- The storefront catalog, cart, checkout, and order history are demo-oriented and stored client-side.
- The backend already handles authentication and admin user management, so the app is ready for portfolio demos or further expansion into a full production commerce system.
