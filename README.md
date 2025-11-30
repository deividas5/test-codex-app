# Warehouse App

A simple warehouse dashboard with a Java 21 Spring Boot backend and a React.js frontend. The backend uses an in-memory H2 database for quick setup and includes JWT-based authentication for login and registration.

## Prerequisites
- Java 21+
- Maven 3.9+
- Node.js 18+ and npm

## Running the backend
1. Install dependencies and start the API:
   ```bash
   cd backend
   ./mvnw spring-boot:run  # or mvn spring-boot:run
   ```
2. The API listens on `http://localhost:8080` and exposes:
   - `POST /api/auth/register` and `POST /api/auth/login` for auth
   - CRUD under `GET/POST/PUT/DELETE /api/items` (authenticated)
   - `GET /api/items/public` for a public item listing
3. An admin user is seeded with `username=admin`, `password=admin123`.

## Running the frontend
1. Install packages and start the dev server:
   ```bash
   cd frontend
   npm install
   npm run dev
   ```
2. Vite serves the app on `http://localhost:5173` and proxies `/api` requests to the backend.

## Using the app
- Register or log in from the landing page to receive a JWT token stored in memory for the session.
- View public inventory, then create, update, or delete items after logging in.
- H2 console is available at `http://localhost:8080/h2-console` while the backend is running.
