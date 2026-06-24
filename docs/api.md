# Frame API Documentation

Base URL:

```txt
http://localhost:8080
```

---

## Health

### GET `/api/health`

Checks if the API is running.

#### Response

```json
{
  "message": "Frame API is running",
  "status": "UP",
  "timestamp": "2026-06-24T12:00:00Z"
}
```

---

## Users

### POST `/api/users`

Creates a new user.

#### Request Body

```json
{
  "fullName": "Gabriel Belo",
  "email": "gabriel@example.com",
  "password": "password123"
}
```
Passwords are stored as hashes and are never returned by the API.

#### Response

```json
{
  "id": "uuid",
  "fullName": "Gabriel Belo",
  "email": "gabriel@example.com",
  "role": "MEMBER",
  "createdAt": "2026-06-24T12:00:00Z",
  "updatedAt": "2026-06-24T12:00:00Z"
}
```
---

### GET `/api/users`

Lists all users.

---

## Auth

### POST `/api/auth/login`

Authenticates a user and returns a JWT access token.

#### Request Body

```json
{
  "email": "gabriel@example.com",
  "password": "password123"
}
```
#### Response
```json
{
"accessToken": "jwt-token",
"tokenType": "Bearer",
"expiresAt": "2026-06-24T12:00:00Z",
"userId": "uuid",
"fullName": "Gabriel Belo",
"email": "gabriel@example.com",
"role": "MEMBER"
}
```

Protected endpoints require a JWT Bearer token.

```txt
Authorization: Bearer jwt-token
```

### GET `/api/auth/me`

Returns the authenticated user.

Requires authentication.

```txt
Authorization: Bearer jwt-token
```

#### Response
```json
{
"id": "uuid",
"fullName": "Gabriel Belo",
"email": "gabriel@example.com",
"role": "MEMBER"
}
```
### Public endpoints:

- GET /api/health
- POST /api/users
- POST /api/auth/login

---

## Workspaces

### POST `/api/workspaces`

Creates a new workspace.

#### Request Body

```json
{
  "name": "Frame Studio",
  "description": "Creative workspace for managing visual projects, scenes and references."
}
```
The workspace owner is automatically resolved from the authenticated JWT token.

#### Response

```json
{
  "id": "workspace-uuid",
  "name": "Frame Studio",
  "description": "Creative workspace for managing visual projects, scenes and references.",
  "ownerId": "user-uuid",
  "ownerName": "Gabriel Belo",
  "createdAt": "2026-06-24T12:00:00Z",
  "updatedAt": "2026-06-24T12:00:00Z"
}
```

---

### GET `/api/workspaces`

Lists workspaces owned by the authenticated user.

### GET `/api/workspaces/{workspaceId}`

Returns a workspace owned by the authenticated user.

---

### PATCH `/api/workspaces/{workspaceId}`

Updates a workspace owned by the authenticated user.

#### Request Body

All fields are optional.

```json
{
  "name": "Frame Creative Studio",
  "description": "Updated workspace for organizing creative production workflows."
}
```
---

## Projects

### POST `/api/projects`

Creates a project inside a workspace owned by the authenticated user.

#### Request Body

```json
{
  "name": "Short Film Campaign",
  "description": "A visual production project for organizing scenes, references and creative decisions.",
  "workspaceId": "workspace-uuid"
}
```

#### Response

```json
{
  "id": "project-uuid",
  "name": "Short Film Campaign",
  "description": "A visual production project for organizing scenes, references and creative decisions.",
  "status": "DRAFT",
  "workspaceId": "workspace-uuid",
  "workspaceName": "Frame Studio",
  "createdAt": "2026-06-24T12:00:00Z",
  "updatedAt": "2026-06-24T12:00:00Z"
}
```

---

### GET `/api/projects`

Lists projects from workspaces owned by the authenticated user.

### GET `/api/projects/{projectId}`

Returns a project from a workspace owned by the authenticated user.

---

### PATCH `/api/projects/{projectId}`

Updates a project from a workspace owned by the authenticated user.

#### Request Body

All fields are optional.

```json
{
  "name": "Frame Visual Campaign",
  "description": "Updated project for organizing scenes, references and creative decisions."
}
```
### PATCH `/api/projects/{projectId}/status`

Updates the status of a project from a workspace owned by the authenticated user.

#### Avaliable Statuses

- DRAFT
- ACTIVE
- ARCHIVED

#### Request Body

```json
{
  "status": "ACTIVE"
}
```
---

## Scenes

### POST `/api/scenes`

Creates a scene inside a project owned by the authenticated user.

#### Request Body

```json
{
  "title": "Opening Sequence",
  "summary": "Initial scene establishing the tone, visual mood and main creative direction of the project.",
  "position": 1,
  "layer": "Script",
  "projectId": "project-uuid"
}
```

#### Response

```json
{
  "id": "scene-uuid",
  "title": "Opening Sequence",
  "summary": "Initial scene establishing the tone, visual mood and main creative direction of the project.",
  "position": 1,
  "layer": "Script",
  "status": "IDEA",
  "projectId": "project-uuid",
  "projectName": "Short Film Campaign",
  "createdAt": "2026-06-24T12:00:00Z",
  "updatedAt": "2026-06-24T12:00:00Z"
}
```

---

### GET `/api/scenes`

Lists scenes from projects owned by the authenticated user.

### GET `/api/scenes/{sceneId}`

Returns a scene from a project owned by the authenticated user.

---

### PATCH `/api/scenes/{sceneId}`

Updates a scene owned by the authenticated user.

#### Request Body

All fields are optional.

```json
{
  "title": "Opening Mood",
  "summary": "Updated scene focused on visual atmosphere and creative direction.",
  "position": 2,
  "layer": "Visual Reference"
}
```

---

### PATCH `/api/scenes/{sceneId}/status`

Updates the status of a scene owned by the authenticated user.

#### Request Body

```json
{
  "status": "IN_PROGRESS"
}
```

Available statuses:

```txt
IDEA
DRAFT
IN_PROGRESS
REVIEW
APPROVED
```

---

## Dashboard

### GET `/api/dashboard/summary`

Returns a summary of the authenticated user's workspaces, projects and scenes.

Requires authentication.

```txt
Authorization: Bearer jwt-token
```

#### Response

```json
{
  "totalWorkspaces": 1,
  "totalProjects": 2,
  "activeProjects": 1,
  "archivedProjects": 0,
  "totalScenes": 4,
  "scenesInProgress": 1,
  "scenesInReview": 0,
  "approvedScenes": 1,
  "recentScenes": [
    {
      "id": "scene-uuid",
      "title": "Opening Mood",
      "projectName": "Frame Visual Campaign",
      "status": "IN_PROGRESS",
      "position": 2,
      "layer": "Visual Reference",
      "updatedAt": "2026-06-24T12:00:00Z"
    }
  ]
}
```

---

## Error Response Format

Frame API uses a standardized error response.

### Example

```json
{
  "timestamp": "2026-06-24T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/users",
  "fieldErrors": [
    {
      "field": "email",
      "message": "Email must be valid"
    }
  ]
}
```
