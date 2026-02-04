# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

B3 (Brick by Brick) is a mobile fitness app with an adaptive AI coach called BRIX. Built with React Native/Expo frontend and Spring Boot backend. The app helps users build workout habits through visual progress tracking (bricks).

## Build & Run Commands

### Backend (Java/Spring Boot)
```bash
cd backend
mvn spring-boot:run              # Run locally (port 8080, SQLite)
mvn test                         # Run all tests
mvn test -Dtest=BrickServiceTest # Run single test class
mvn test -Dtest=BrickServiceTest#testCreateBrick  # Run single test method
mvn clean package -DskipTests    # Build JAR for Docker
```

### Mobile (React Native/Expo)
```bash
cd mobile
npm install                      # Install dependencies
npm start                        # Start Expo dev server
npm run ios                      # Run iOS simulator
npm run android                  # Run Android emulator
```

### Docker (PostgreSQL + Backend)
```bash
docker-compose up --build        # From root directory
```

### AI Setup (Ollama - required for BRIX)
```bash
ollama pull llama3.2             # Download model
ollama serve                     # Start server (port 11434)
```

## Architecture

### Backend (`/backend`)
Layered Spring Boot architecture:
- **Controllers** (`controller/`) - REST endpoints at `/api/v1`
- **Services** (`service/`) - Business logic; `BrixService` handles AI coaching, `OllamaService` integrates with Llama 3.2
- **Repositories** (`repository/`) - JPA data access
- **Models** (`model/`) - 10 JPA entities (UserProfile, BehaviorProfile, Workout, WorkoutSession, Brick, etc.)
- **DTOs** (`dto/`) - Request/Response objects

### Mobile (`/mobile`)
React Native with Expo:
- **Screens** (`src/screens/`) - 14+ app screens
- **Components** (`src/components/`) - Reusable UI
- **Services** (`src/services/`) - API client via axios
- **Navigation** (`src/navigation/`) - React Navigation bottom tabs

### Key Data Relationships
```
UserProfile (1) ←→ (1) BehaviorProfile
    ├─→ (N) WorkoutSession → (1) Brick
    ├─→ (N) DailyLog
    └─→ (N) BrixMessage

Workout (1) ←→ (N) WorkoutExercise ←→ (1) Exercise
```

## Database Configuration

**Local Development**: SQLite (`backend/b3.db`) - delete file to reset with seeded data

**Docker/Production**: PostgreSQL via docker-compose
- Database: `b3`, User: `b3user`, Password: `b3password`
- Uses `application-docker.properties` profile

## Key API Endpoints

Base URL: `http://localhost:8080/api/v1`

| Endpoint | Purpose |
|----------|---------|
| `GET /profile` | Get user profile (default ID=1) |
| `GET /workouts` | List workout library |
| `POST /sessions` | Start workout session |
| `PUT /sessions/{id}/complete` | Complete workout, creates brick |
| `GET /bricks/calendar/{profileId}` | 30-day brick calendar |
| `POST /brix/chat` | Send message to AI coach |
| `POST /daily-logs` | Create daily check-in |

## AI Coaching System

BRIX uses Ollama with Llama 3.2 locally (free, no API costs). Coaching tones adapt based on `BehaviorProfile`:
- **Encouraging** - Low energy + high stress
- **Challenging** - On streak (7+ days)
- **Empathetic** - After long breaks
- **Celebratory** - Hitting milestones

## Environment Variables

Copy `backend/.env.example` to `backend/.env`:
```
OLLAMA_API_URL=http://localhost:11434
OLLAMA_MODEL=llama3.2
OLLAMA_ENABLED=true
EXERCISEDB_API_KEY=your-key  # Optional, for exercise GIFs
```

## Development Notes

- Single user MVP: Profile ID hardcoded to 1
- `DataInitializer.java` seeds default user, 40+ exercises, and workouts on startup
- Mobile uses NativeWind (Tailwind for React Native)
- Haptic feedback and confetti animations implemented
- Mobile API base URL is hardcoded in `mobile/src/services/api.ts` (localhost:8080)

## Testing

Backend tests are organized by layer:
- **Model tests** (`model/*Test.java`) - Entity validation and business logic
- **Service tests** (`service/*Test.java`) - Business logic with mocked repositories
- **Repository tests** (`repository/*Test.java`) - JPA queries
- **Integration tests** (`ApiIntegrationTest.java`) - Full API endpoint testing
