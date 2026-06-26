# Frame

> Creative production, without the chaos.

Frame is a collaborative platform designed to help creative teams organize projects, scenes, references, feedback, and production workflows in a more human and visually structured way.

The project aims to combine modern software engineering practices with thoughtful product design, creating a workspace tailored for creative processes without sacrificing scalability, performance, and maintainability.

---

## 🚧 Status

Frame is currently in active development.

The backend foundation is being built with Java and Spring Boot, focusing on clean architecture, domain modeling, REST APIs, validation, error handling, and secure configuration practices.


## Tech Stack

### Backend
- Java 21
- Spring Boot
- Spring Web
- Spring Security
- Spring data JPA
- PostgreSQL
- Jakarta Validation
- Maven

### Frontend *(planned)*
- React
- TypeScript
- Vite
- TailwindCSS

---

## Planned Features

- [ ] Authentication & Authorization
- [ ] Workspaces
- [ ] Project Management
- [ ] Scene Management
- [ ] Activity Feed
- [ ] File Uploads
- [ ] Comments & Collaboration
- [ ] Dashboard & Analytics

---

## Current Backend Features

- Health check endpoint
- User creation and listing
- Workspace creation and listing
- Workspace update endpoint
- Project creation and listing
- Project update endpoints
- Project status update
- Scene creation and listing
- Scene update endpoints
- Scene status update
- Scene references
- Reference creation and listing
- Reference update endpoint
- Ownership checks for references
- Modular package structure
- PostgreSQL integration
- Global exception handling
- Request validation
- Local configuration ignored from Git
- Authenticated workspace creation
- Ownership-based access control
- Dashboard summary endpoint

---

## Domain Structure
```
Authenticated User    
└── Workspace   
    └── Project     
        └── Scene
            └── Reference
```
Frame is structured around creative workspaces.

A user can own workspaces, each workspace can contain projects, and each project can contain scenes organized by position and layer.

Scenes are the first core product concept of Frame, designed to represent creative units inside a timeline-like workflow.

---

## API Documentation

The current API endpoints are documented here:

docs/api.md

---

## Local Configuration

Sensitive local configuration is not committed to the repository.

The project uses a local Spring profile:

spring.profiles.active=local

Local database settings should be placed in:

src/main/resources/application-local.properties

Example:

server.port=8080

spring.datasource.url=jdbc:postgresql://localhost:5432/frame_db
spring.datasource.username=your_database_username
spring.datasource.password=your_database_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false

This file is ignored by Git to avoid exposing local credentials.

---
## Vision

Frame is not intended to be just another task manager.

The platform is being designed as a collaborative production environment focused on clarity, creative workflows, and structured communication for modern creative teams.

The goal is to explore the intersection between software engineering, product thinking, UX, and creative production.

---