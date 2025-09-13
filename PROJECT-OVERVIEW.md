# MechTrack - Automotive Workshop Management System

## 🎯 Project Vision
MechTrack is a comprehensive web application designed to help automotive workshop owners manage their daily operations, track repair jobs, manage parts inventory, and analyze business performance.

## 🏗️ System Architecture Overview

```mermaid
graph TB
    subgraph "Frontend (React/Vue/Angular)"
        UI[User Interface]
        AUTH[Authentication Pages]
        JOBS[Job Management Pages]
        PARTS[Parts Management Pages]
        ANALYTICS[Analytics Dashboard]
    end
    
    subgraph "Backend (Spring Boot)"
        API[REST API Layer]
        SERVICE[Business Logic Services]
        SECURITY[JWT Security]
        STORAGE[File Storage Service]
    end
    
    subgraph "Data Layer"
        DB[(H2/PostgreSQL Database)]
        FILES[Local File System]
    end
    
    UI --> API
    AUTH --> API
    JOBS --> API
    PARTS --> API
    ANALYTICS --> API
    
    API --> SERVICE
    API --> SECURITY
    SERVICE --> DB
    STORAGE --> FILES
    SERVICE --> STORAGE
```

## 🔑 Core Business Entities

```mermaid
erDiagram
    JOB ||--o{ PART : contains
    JOB {
        uuid id PK
        string customerName
        string carModel
        string description
        date jobDate
        decimal income
        timestamp createdAt
    }
    
    PART {
        uuid id PK
        uuid jobId FK
        string name
        decimal cost
        string invoiceImageUrl
        date purchaseDate
        timestamp createdAt
    }
    
    USER {
        string username PK
        string password
        string role
    }
```

## 🎭 User Personas & Use Cases

### Primary User: Workshop Owner (Mike)
- **Goal**: Manage daily workshop operations efficiently
- **Needs**: 
  - Track repair jobs and customers
  - Manage parts inventory and costs
  - Store invoice images for parts
  - Analyze monthly profits
  - Quick access on mobile/desktop

### Key User Journeys:

#### 1. **New Repair Job Workflow**
```mermaid
journey
    title Workshop Owner Creates New Job
    section Daily Operations
      Login to system: 5: Owner
      Navigate to jobs: 4: Owner
      Create new job: 5: Owner
      Fill job details: 4: Owner
      Save job: 5: Owner
    section Add Parts
      Search for job: 4: Owner
      Add parts to job: 5: Owner
      Upload part invoice: 3: Owner
      Calculate total cost: 5: Owner
```

#### 2. **Monthly Business Analysis**
```mermaid
journey
    title Monthly Profit Analysis
    section Review Performance
      Open analytics: 5: Owner
      View monthly summary: 5: Owner
      Check income vs expenses: 5: Owner
      Identify profitable months: 4: Owner
      Plan for next month: 5: Owner
```

## 🔄 API Workflow Diagrams

### Authentication Flow
```mermaid
sequenceDiagram
    participant F as Frontend
    participant A as Auth API
    participant J as JWT Service
    participant D as Database
    
    F->>A: POST /api/auth/login
    Note over F,A: { username: "test_owner", password: "password123" }
    A->>D: Validate credentials
    D-->>A: User valid
    A->>J: Generate JWT token
    J-->>A: JWT token
    A-->>F: { token: "eyJ...", username: "test_owner" }
    Note over F: Store JWT in localStorage
    
    loop Every API Call
        F->>A: Any API Request + Authorization: Bearer JWT
        A->>J: Validate JWT
        J-->>A: Valid/Invalid
        alt JWT Valid
            A-->>F: API Response
        else JWT Invalid
            A-->>F: 401 Unauthorized
        end
    end
```

### Job Management Flow
```mermaid
sequenceDiagram
    participant F as Frontend
    participant J as Job API
    participant P as Part API
    participant FS as File Storage
    participant DB as Database
    
    F->>J: POST /api/jobs (Create Job)
    J->>DB: Save job
    DB-->>J: Job created with UUID
    J-->>F: JobDto { id, customerName, carModel, etc. }
    
    F->>P: POST /api/jobs/{id}/parts (Add Part + Invoice)
    Note over F,P: FormData: { name, cost, date, file }
    P->>FS: Store invoice file
    FS-->>P: File URL
    P->>DB: Save part with file URL
    DB-->>P: Part saved
    P-->>F: PartDto { id, name, cost, invoiceImageUrl }
    
    F->>P: GET /api/parts/{id}/invoice (View Invoice)
    P->>FS: Load file
    FS-->>P: File stream
    P-->>F: Invoice file (PDF/Image)
```

## 🎨 Frontend Implementation Guide

### 1. **Application Structure**
```
src/
├── components/
│   ├── auth/
│   │   ├── LoginForm.jsx
│   │   └── ProtectedRoute.jsx
│   ├── jobs/
│   │   ├── JobList.jsx
│   │   ├── JobForm.jsx
│   │   ├── JobCard.jsx
│   │   └── JobSearch.jsx
│   ├── parts/
│   │   ├── PartList.jsx
│   │   ├── PartForm.jsx
│   │   ├── FileUpload.jsx
│   │   └── InvoiceViewer.jsx
│   ├── analytics/
│   │   ├── Dashboard.jsx
│   │   ├── MonthlyChart.jsx
│   │   └── SummaryCards.jsx
│   └── shared/
│       ├── Layout.jsx
│       ├── Navigation.jsx
│       └── Loading.jsx
├── services/
│   ├── authService.js
│   ├── jobService.js
│   ├── partService.js
│   └── analyticsService.js
├── hooks/
│   ├── useAuth.js
│   └── useApi.js
└── utils/
    ├── apiClient.js
    └── formatters.js
```

### 2. **Key API Integration Points**

#### Authentication Service Example
```javascript
// services/authService.js
class AuthService {
  async login(username, password) {
    const response = await apiClient.post('/api/auth/login', {
      username, password
    });
    
    localStorage.setItem('jwt_token', response.token);
    return response;
  }
  
  isAuthenticated() {
    return localStorage.getItem('jwt_token') !== null;
  }
  
  logout() {
    localStorage.removeItem('jwt_token');
  }
}
```

#### Parts Management with File Upload
```javascript
// services/partService.js
class PartService {
  async createPartWithInvoice(jobId, partData, file) {
    const formData = new FormData();
    formData.append('name', partData.name);
    formData.append('cost', partData.cost);
    formData.append('purchaseDate', partData.purchaseDate);
    
    if (file) {
      formData.append('file', file);
    }
    
    return apiClient.post(`/api/jobs/${jobId}/parts`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
  }
  
  getInvoiceUrl(partId) {
    return `/api/parts/${partId}/invoice`;
  }
}
```

### 3. **State Management Strategy**

```mermaid
graph LR
    subgraph "Frontend State"
        AUTH[Auth State]
        JOBS[Jobs State]
        PARTS[Parts State]
        UI[UI State]
    end
    
    subgraph "Backend APIs"
        AAPI[Auth API]
        JAPI[Jobs API]
        PAPI[Parts API]
        ANAPI[Analytics API]
    end
    
    AUTH --> AAPI
    JOBS --> JAPI
    PARTS --> PAPI
    UI --> ANAPI
```

## 🚀 Implementation Phases

### Phase 1: Core Authentication & Jobs (Week 1-2)
- [ ] Login/logout functionality
- [ ] Protected routes
- [ ] Job CRUD operations
- [ ] Job listing and search
- [ ] Basic responsive layout

### Phase 2: Parts Management & File Upload (Week 3-4)
- [ ] Parts CRUD operations
- [ ] File upload component
- [ ] Invoice viewing/downloading
- [ ] Parts-to-jobs relationship
- [ ] Form validation

### Phase 3: Analytics & Polish (Week 5-6)
- [ ] Analytics dashboard
- [ ] Monthly charts
- [ ] Summary statistics
- [ ] Error handling
- [ ] Loading states
- [ ] Mobile optimization

## 💡 Technical Considerations

### File Upload Handling
- **Max file size**: 10MB
- **Allowed types**: PDF, PNG, JPG, JPEG
- **Storage**: Local filesystem (easily migrated to cloud later)
- **Frontend**: Use `FormData` for multipart uploads
- **Preview**: Direct links to `/api/parts/{id}/invoice`

### Authentication
- **Type**: JWT Bearer tokens
- **Storage**: localStorage (consider httpOnly cookies for production)
- **Expiration**: 1 hour (configurable)
- **Refresh**: Manual re-login (can add refresh tokens later)

### API Response Format
All APIs return consistent JSON structure:
```json
// Success Response
{
  "id": "uuid",
  "field1": "value1",
  "field2": "value2"
}

// Error Response  
{
  "timestamp": "2025-09-13T14:22:13",
  "status": 400,
  "error": "Bad Request", 
  "message": "Validation failed"
}
```

## 🎯 Success Metrics
- [ ] Workshop owner can create and manage jobs in under 2 minutes
- [ ] File upload works reliably for invoices up to 10MB
- [ ] Analytics provide clear monthly profit insights
- [ ] Mobile-friendly interface for on-the-go access
- [ ] Zero data loss with proper error handling

---

**Ready to build?** This overview should give your frontend developer everything they need to understand the project scope, technical requirements, and implementation approach. The Postman collection provides all the API details, while this document explains the "why" and "how" from a user perspective.
