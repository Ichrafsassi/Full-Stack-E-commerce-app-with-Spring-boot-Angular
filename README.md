# NERD'S TECH — Full-Stack E-commerce

Full-stack e-commerce: **Angular 21**, **Spring Boot 4.0.6**, **Java 21**, REST APIs, JWT auth, neo deals with real discount math, admin CMS, and cyber-neo UI.

## Stack

| Layer | Technology |
|-------|------------|
| Frontend | Angular 21.2, standalone components |
| Backend | Spring Boot 4.0.6, Spring Security, JPA |
| Database | PostgreSQL (localhost:5432) |

## Quick start

### Backend

```powershell
cd backend
.\run.cmd
```

`run.cmd` frees port **8081** if needed, then starts the API.

API: `http://localhost:8081/api`

If startup fails with **Hibernate dialect** or **port in use**, stop other Java processes on 8081 and run again.

### Frontend

```powershell
cd frontend
npm start
```

Open `http://localhost:4200`

## Demo accounts

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@nerdstech.com | admin123 |
| User | user@nerdstech.com | user123 |

## Pages

- `/` — Landing (deals preview, featured gear)
- `/products` — Tech catalog
- `/deals` — Discounted items (backend-calculated prices)
- `/builds` — Custom rig ideas
- `/about`, `/support`

## Docs

- [Code documentation](docs/CODE_DOCUMENTATION.md)
- [Project report](docs/PROJECT_REPORT.md)
