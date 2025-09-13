# MechTrack - Workshop Management System

Simple, secure workshop management for tracking jobs and parts.

## 🚀 Quick Start

### Environment Variables (Required)
```bash
MECHTRACK_JWT_SECRET=your-64-character-random-string
MECHTRACK_OWNER_NAME=your_name
MECHTRACK_OWNER_PASSWORD=YourSecurePassword123!
MECHTRACK_CORS_ORIGINS=http://localhost:3000,https://yourdomain.com
DATABASE_URL=jdbc:postgresql://host:5432/dbname
```

### Run Locally
```bash
mvn spring-boot:run
```

### Deploy to Heroku
```bash
git add .
git commit -m "Deploy to Heroku"
git push heroku main
```

## 🔐 Authentication

POST `/api/auth/login`
```json
{
  "username": "your_name",
  "password": "YourSecurePassword123!"
}
```

Use returned JWT token in Authorization header: `Bearer <token>`

## 📡 API Endpoints

- `GET /api/jobs` - List all jobs
- `GET /api/jobs/search?customer=John&from=2024-01-01` - Search jobs
- `POST /api/jobs` - Create job
- `GET /api/parts` - List all parts
- `POST /api/jobs/{id}/parts` - Add part to job

Full API docs: `/swagger-ui.html`
