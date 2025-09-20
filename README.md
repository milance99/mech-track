# MechTrack - Workshop Management System

Simple, secure workshop management for tracking jobs and parts.

## 🚀 Quick Start

### Environment Variables (Required)
```bash
# Security Configuration
MECHTRACK_SECURITY_JWT_SECRET=your-64-character-random-string
MECHTRACK_SECURITY_OWNER_NAME=your_name
MECHTRACK_SECURITY_OWNER_PASSWORD=YourSecurePassword123!
MECHTRACK_SECURITY_CORS_ORIGINS=http://localhost:3000,https://yourdomain.com

# Token Expiration (Optional - defaults shown)
MECHTRACK_SECURITY_ACCESS_TOKEN_EXPIRATION_MS=900000    # 15 minutes
MECHTRACK_SECURITY_REFRESH_TOKEN_EXPIRATION_MS=604800000 # 7 days

# Timezone Configuration (Optional - defaults to Europe/Belgrade)
TZ=Europe/Belgrade                                       # Your timezone with automatic DST handling

# Database
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

### Refresh Token System
MechTrack uses a secure refresh token system with:
- **Access tokens**: 15-minute expiration for API calls
- **Refresh tokens**: 7-day expiration for obtaining new access tokens
- **Token rotation**: New tokens issued on each refresh
- **Secure logout**: Refresh token revocation

### Login
POST `/api/auth/login`
```json
{
  "username": "your_name",
  "password": "YourSecurePassword123!"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "your_name",
  "accessTokenExpiresAt": "2024-01-01T12:15:00",
  "refreshTokenExpiresAt": "2024-01-08T12:00:00"
}
```

### Refresh Token
POST `/api/auth/refresh`
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Logout
POST `/api/auth/logout`
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Using Tokens
Use access token in Authorization header: `Bearer <accessToken>`

## 📡 API Endpoints

- `GET /api/jobs` - List all jobs
- `GET /api/jobs/search?customer=John&from=2024-01-01` - Search jobs
- `POST /api/jobs` - Create job
- `GET /api/parts` - List all parts
- `POST /api/jobs/{id}/parts` - Add part to job

Full API docs: `/swagger-ui.html`

## 🧪 Testing

### Manual API Testing
Two testing tools are provided:

#### Bash Script
```bash
# Update credentials in the script
nano test-refresh-tokens.sh
# Set USERNAME and PASSWORD variables

# Run comprehensive tests
./test-refresh-tokens.sh
```

#### Postman Collection
1. Import `MechTrack-RefreshToken-Tests.postman_collection.json`
2. Set collection variables: `username` and `password`
3. Run requests in sequence (1→2→3→4→5→6→7)

Both tools test:
- ✅ Login with token generation
- ✅ Token validation
- ✅ Token refresh and rotation
- ✅ Logout and token revocation
- ✅ Security (invalid/expired token handling)
