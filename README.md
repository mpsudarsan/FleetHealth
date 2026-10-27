# FleetHealth — Vehicle Health & Maintenance Tracker

A full-stack vehicle health and maintenance monitoring system for fleet managers.

## Tech Stack

| Layer    | Technology                          |
|----------|-------------------------------------|
| Backend  | Java 17, Spring Boot 3, Spring Data JPA |
| Database | MySQL 8                             |
| Frontend | React, TypeScript, Vite             |
| Testing  | JUnit 5, Mockito                    |

## Project Structure

```
FleetHealth/
├── backend/      → Spring Boot REST API
├── frontend/     → React + TypeScript dashboard
├── database/     → Database documentation
├── docs/         → Project documentation
├── .gitignore
└── README.md
```

## Getting Started

### Prerequisites

- Java 17
- Node.js 18+
- MySQL 8

### 1. Database Setup

```sql
CREATE DATABASE IF NOT EXISTS fleethealth;
```

### 2. Backend

```bash
cd backend
./mvnw spring-boot:run
```

The API runs at **http://localhost:8080**

### 3. Frontend

```bash
cd frontend
npm install
npm run dev
```

The dashboard runs at **http://localhost:5173**

## API Endpoints

| Method | Endpoint                            | Description            |
|--------|-------------------------------------|------------------------|
| POST   | `/api/vehicles`                     | Add a vehicle          |
| GET    | `/api/vehicles`                     | List all vehicles      |
| GET    | `/api/vehicles/{id}`                | Get vehicle by ID      |
| POST   | `/api/vehicles/{id}/health`         | Record health data     |
| GET    | `/api/vehicles/{id}/health`         | Get health records     |
| GET    | `/api/maintenance/alerts`           | List alerts            |
| PUT    | `/api/maintenance/alerts/{id}/resolve` | Resolve an alert    |
