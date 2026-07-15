# NEXORA: Software Knowledge Continuity Framework

A unified enterprise web application designed to automatically analyze software repositories, preserve software knowledge, visualize software evolution, and assist developers in understanding large-scale software projects.

## 🎯 Project Overview

NEXORA is a Software Knowledge Continuity Framework (SKCF) that combines:
- Repository mining
- Static code analysis
- Knowledge graphs
- Software evolution tracking
- Architecture recovery
- Developer expertise analysis
- Project health monitoring

All in a single integrated platform to reduce software knowledge loss and improve developer onboarding.

## 🏗️ Technology Stack

### Frontend
- React.js with TypeScript
- Tailwind CSS for styling
- React Router for navigation
- Axios for HTTP requests
- Recharts for analytics
- Cytoscape.js for graph visualization
- Mermaid.js for diagrams

### Backend
- Java Spring Boot
- Spring Security with JWT
- REST APIs
- JPA/Hibernate ORM
- JGit for repository analysis
- JavaParser for code parsing

### Databases
- PostgreSQL (transactional data, users, projects, reports)
- Neo4j (knowledge graph, software relationships)

### Deployment
- Docker & Docker Compose

## 📦 Project Structure

```
NEXORA/
├── frontend/                          # React TypeScript SPA
│   ├── src/
│   │   ├── components/                # Reusable React components
│   │   ├── pages/                     # Page components
│   │   ├── layouts/                   # Layout components
│   │   ├── services/                  # API services
│   │   ├── hooks/                     # Custom React hooks
│   │   ├── utils/                     # Utility functions
│   │   ├── types/                     # TypeScript types
│   │   └── assets/                    # Images, icons, etc.
│   ├── public/
│   ├── package.json
│   ├── tsconfig.json
│   ├── tailwind.config.js
│   ├── Dockerfile
│   └── .env.example
│
├── backend/                           # Spring Boot REST API
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/nexora/
│   │   │   │   ├── controller/        # REST endpoints
│   │   │   │   ├── service/           # Business logic
│   │   │   │   ├── repository/        # Data access layer
│   │   │   │   ├── entity/            # JPA entities
│   │   │   │   ├── dto/               # Data transfer objects
│   │   │   │   ├── security/          # JWT & auth config
│   │   │   │   ├── config/            # Application config
│   │   │   │   ├── parser/            # Code parsing (JavaParser)
│   │   │   │   ├── graph/             # Neo4j graph operations
│   │   │   │   ├── analyzer/          # Repository analyzer
│   │   │   │   ├── onboarding/        # Onboarding logic
│   │   │   │   └── reports/           # Report generation
│   │   │   └── resources/
│   │   │       └── application.yml
│   │   └── test/
│   ├── pom.xml
│   ├── Dockerfile
│   └── .env.example
│
├── database/
│   ├── postgres/
│   │   └── init.sql                   # PostgreSQL schema
│   └── neo4j/
│       └── init.cypher                # Neo4j queries
│
├── docker-compose.yml
├── .gitignore
└── README.md
```

## 🚀 Core Features

### 1. Authentication Module
- User registration & login
- JWT authentication
- Role-based authorization (Admin, Project Manager, Developer)
- Secure password hashing

### 2. Dashboard
- Total projects, repositories, developers overview
- Knowledge coverage percentage
- Architecture stability metrics
- Documentation score
- Knowledge risk assessment
- Project health trend charts
- Contributor activity visualization

### 3. Repository Management
- Add Git repository via URL
- Clone repositories
- Upload local repositories
- Auto-scan & analyze
- Delete & refresh analysis
- Display repository metadata

### 4. Repository Analyzer
- Extract folder structure & packages
- Detect classes, interfaces, methods
- Identify APIs & configuration
- Detect programming language & framework
- Recognize architecture patterns (MVC, Microservices, Monolith, etc.)
- Generate component & class diagrams

### 5. Dependency Intelligence
- Class dependencies
- Package dependencies
- API dependencies
- Service dependencies
- Database dependencies
- Circular dependency detection

### 6. Knowledge Graph
- Nodes: Project, Module, Package, Class, Interface, Method, API, Database, Developer
- Relationships: CALLS, IMPLEMENTS, EXTENDS, USES, MODIFIES, DEPENDS_ON, DOCUMENTS, INTRODUCES, OWNS
- Stored in Neo4j
- Visualized with Cytoscape.js

### 7. Software Evolution Module
- Timeline view of code evolution
- Module & architecture evolution tracking
- API evolution monitoring
- Project replay (time machine feature)
- Historical dependency analysis

### 8. Developer Expertise Module
- Automatic module owner identification
- Top contributor tracking
- Personalized onboarding plans
- Learning path generation

### 9. Knowledge Verification
- Documentation vs source code comparison
- Missing documentation detection
- Outdated documentation alerts
- Broken reference tracking
- Software provenance tracking

### 10. Project Health Dashboard
- Documentation coverage metrics
- Dependency health status
- Technical debt assessment
- Complexity metrics
- Contributor diversity
- Build status tracking

## 📋 Development Roadmap

- **Phase 1:** Project Setup & Infrastructure
- **Phase 2:** Authentication Module
- **Phase 3:** Dashboard Development
- **Phase 4:** Repository Management
- **Phase 5:** Repository Analyzer
- **Phase 6:** Dependency Intelligence
- **Phase 7:** Knowledge Graph Implementation
- **Phase 8:** Software Evolution Module
- **Phase 9:** Developer Expertise Module
- **Phase 10:** Reports & Notifications
- **Phase 11:** Advanced Search
- **Phase 12:** Deployment & Optimization

## 🛠️ Getting Started

### Prerequisites
- Node.js 18+ or higher
- Java 17+ or higher
- PostgreSQL 13+
- Neo4j 4.4+
- Docker & Docker Compose
- Git

### Quick Start with Docker

```bash
# Clone the repository
git clone https://github.com/Nandiniburra/NEXORA-A-Unified-Software-Knowledge-Continuity-Framework-for-Software-Evolution-.git
cd NEXORA

# Start all services
docker-compose up -d

# Access the application
Frontend:  http://localhost:3000
Backend:   http://localhost:8080
Neo4j:     http://localhost:7474
```

### Manual Setup

#### Backend Setup
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

#### Frontend Setup
```bash
cd frontend
npm install
npm start
```

## 📚 API Documentation

API endpoints will be documented in `/backend/API.md`

## 🎨 UI/UX Design

- Modern enterprise dashboard design
- Minimalist interface
- Professional blue/indigo color palette
- Responsive layout
- Dark and light theme support
- Sidebar navigation
- Interactive charts and graphs

## 📝 License

MIT License - See LICENSE file for details

## 🤝 Contributing

Contributions are welcome! Please follow the established code style and guidelines.

---

**Built with ❤️ for software developers and teams**
