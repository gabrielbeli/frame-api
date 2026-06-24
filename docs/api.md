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

## Workspaces

### POST `/api/workspaces`

Creates a new workspace.

#### Request Body

```json
{
  "name": "Frame Studio",
  "description": "Creative workspace for managing visual projects, scenes and references.",
  "ownerId": "user-uuid"
}
```

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

Lists all workspaces.

---

### GET `/api/workspaces/owner/{ownerId}`

Lists all workspaces owned by a specific user.

---

## Projects

### POST `/api/projects`

Creates a new project inside a workspace.

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

Lists all projects.

---

### GET `/api/projects/workspace/{workspaceId}`

Lists all projects inside a specific workspace.

---

## Scenes

### POST `/api/scenes`

Creates a new scene inside a project.

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

Lists all scenes.

---

### GET `/api/scenes/project/{projectId}`

Lists all scenes from a specific project ordered by position.

---

### PATCH `/api/scenes/{sceneId}`

Updates scene details.

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

Updates the scene status.

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
