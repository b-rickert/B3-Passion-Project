# B3 Interview Study Guide

## Quick Start Commands

```bash
# === BACKEND (Docker + PostgreSQL) ===
cd ~/Projects/B3-Passion-Project
docker compose up --build          # Start backend + PostgreSQL
# Wait for: "B3 Backend ready for demo!"

# === FRONTEND ===
cd ~/Projects/B3-Passion-Project/mobile
npm install                        # First time only
npx expo start --web               # Opens in browser automatically

# === AI (Optional - run in separate terminal) ===
ollama serve                       # Start Ollama for BRIX AI chat
```

**Verify backend is running:** `curl http://localhost:8080/api/v1/workouts`

---

## What We Built Tonight: Docker + PostgreSQL Migration

### Files Created/Modified

| File | Purpose |
|------|---------|
| `backend/Dockerfile` | Multi-stage build: JDK 21 (build) → JRE 21 (runtime) |
| `docker-compose.yml` | Orchestrates PostgreSQL + Spring Boot backend |
| `backend/src/main/resources/application-docker.properties` | PostgreSQL configuration |
| `backend/pom.xml` | Added PostgreSQL driver dependency |

### Key Talking Points

1. **"Why migrate from SQLite to PostgreSQL?"**
   - SQLite is great for local development and single-user scenarios
   - PostgreSQL is production-ready: concurrent connections, ACID compliance, better scaling
   - JPA abstraction made it trivial—just configuration changes, zero code changes

2. **"How does Spring Profiles work?"**
   - `application.properties` = default (SQLite for local dev)
   - `application-docker.properties` = docker profile (PostgreSQL)
   - Activated via: `-Dspring.profiles.active=docker` in Dockerfile ENTRYPOINT

3. **"Explain the Docker setup"**
   - Multi-stage Dockerfile reduces image size (build artifacts not in final image)
   - docker-compose orchestrates two services with proper networking
   - Health check ensures backend waits for PostgreSQL to be ready
   - Volume persistence keeps data between restarts

4. **"What's next for production?"**
   - Kubernetes for orchestration, scaling, and self-healing
   - Helm charts for configuration management
   - CI/CD pipeline for automated builds and deployments

---

## Full Stack Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                        │
│     React Native (Expo 54) + TypeScript + NativeWind        │
│     Screens → Components → Services (API Client)            │
└─────────────────────────────┬───────────────────────────────┘
                              │ HTTP/REST (JSON)
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     APPLICATION LAYER                        │
│              Spring Boot 3.4.1 + Java 21                    │
│     Controllers → Services → Repositories → DTOs/Mappers    │
└─────────────────────────────┬───────────────────────────────┘
                              │ JPA/Hibernate
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                       DATA LAYER                             │
│     PostgreSQL (Docker) or SQLite (Local Dev)               │
│     + Ollama API (Llama 3.2 for BRIX AI)                    │
└─────────────────────────────────────────────────────────────┘
```

### Request Flow Example: Starting a Workout

1. **Mobile**: User taps "Start Workout" → `api.createSession(profileId, workoutId)`
2. **API Client**: POST to `http://localhost:8080/api/v1/sessions`
3. **Controller**: `WorkoutSessionController.createSession()` validates request
4. **Service**: `WorkoutSessionService.createSession()` contains business logic
5. **Repository**: `WorkoutSessionRepository.save()` persists via JPA
6. **Database**: INSERT into `workout_session` table
7. **Response**: DTO mapped from entity, returned as JSON

---

## Backend Deep Dive

### Package Structure (`com.b3.*`)

| Package | Purpose | Example Classes |
|---------|---------|-----------------|
| `controller/` | REST endpoints, request handling | `WorkoutController`, `BrixController` |
| `service/` | Business logic, orchestration | `BrickService`, `OllamaService` |
| `repository/` | Data access (Spring Data JPA) | `WorkoutRepository`, `BrickRepository` |
| `model/` | JPA entities, domain objects | `UserProfile`, `Workout`, `Brick` |
| `dto/` | Request/Response objects | `WorkoutDTO`, `BrickStatsResponse` |
| `mapper/` | Entity ↔ DTO conversion | `WorkoutMapper`, `BrickMapper` |
| `exception/` | Custom exceptions + handlers | `ResourceNotFoundException` |
| `config/` | Spring configuration | `DataInitializer`, `WebConfig` |

### Key Entities and Relationships

```
UserProfile (1) ──────── (1) BehaviorProfile
     │
     ├── (1:N) WorkoutSession ──── (N:1) Workout
     │            │
     │            └── (1:1) Brick
     │
     ├── (1:N) DailyLog
     ├── (1:N) BrixMessage
     └── (1:N) Milestone

Workout (1) ──── (N) WorkoutExercise ──── (N) Exercise
```

### Spring Data JPA - Repository Pattern

```java
// No SQL required - Spring generates queries from method names
public interface BrickRepository extends JpaRepository<Brick, Long> {
    List<Brick> findByUserProfile_ProfileIdAndBrickDateBetween(
        Long profileId, LocalDate start, LocalDate end);

    boolean existsByUserProfile_ProfileIdAndBrickDate(Long profileId, LocalDate date);

    long countByUserProfile_ProfileId(Long profileId);
}
```

**Interview point**: "Spring Data JPA derives queries from method names. I can also use `@Query` for complex queries, but the naming convention handles 90% of cases."

---

## Frontend Deep Dive

### Screen Architecture (`mobile/src/screens/`)

| Screen | Purpose | Key Features |
|--------|---------|--------------|
| `LandingScreen` | Onboarding | Animated entry, feature highlights |
| `HomeScreen` | Dashboard | Streak card, daily recommendation, quick actions |
| `WorkoutsScreen` | Library | Filter by type/difficulty, search |
| `WorkoutDetailScreen` | Single workout | Exercise list with images |
| `WorkoutSessionScreen` | Active workout | Timer, exercise tracking, completion |
| `ProgressScreen` | Brick wall | 30-day calendar visualization |
| `BrixScreen` | AI chat | Conversational coaching interface |
| `DailyLogScreen` | Check-in | Energy, stress, mood logging |

### State Management

- **Local state**: `useState` for component-specific state
- **API state**: Fetch on mount with `useEffect`, store in local state
- **Navigation state**: React Navigation handles screen stack

**Interview point**: "For this MVP, I kept state management simple—no Redux. Each screen fetches what it needs. For a larger app, I'd consider React Query for caching or Zustand for global state."

### API Client Pattern (`mobile/src/services/api.ts`)

```typescript
const API_BASE_URL = 'http://localhost:8080/api/v1';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: { 'Content-Type': 'application/json' }
});

export const getWorkouts = () => api.get('/workouts');
export const createSession = (data) => api.post('/sessions', data);
export const completeSession = (id, data) => api.put(`/sessions/${id}/complete`, data);
```

---

## BRIX AI System

### How It Works

1. **BehaviorProfile** tracks user patterns:
   - `motivationState`: MOTIVATED / NEUTRAL / STRUGGLING
   - `momentumTrend`: RISING / STABLE / FALLING
   - `preferredWorkoutTime`, `averageSessionDuration`
   - `consistencyScore`, `currentStreak`

2. **Coaching Tones** adapt based on state:
   - `ENCOURAGING`: For neutral users, gentle motivation
   - `CHALLENGING`: For motivated users, push harder
   - `EMPATHETIC`: For struggling users, understanding
   - `CELEBRATORY`: After achievements

3. **OllamaService** sends context to Llama 3.2:
```java
String systemPrompt = buildSystemPrompt(behaviorProfile, recentLogs);
String response = ollamaService.chat(userMessage, systemPrompt);
```

**Interview point**: "BRIX isn't just a chatbot—it's context-aware. It knows your streak, recent workouts, energy levels, and adapts its personality accordingly."

---

## Testing Strategy

### Test Pyramid

```
         /\
        /  \     E2E Tests (Manual for MVP)
       /----\
      /      \   Integration Tests (MockMvc)
     /--------\
    /          \ Unit Tests (JUnit + Mockito)
   /__________\
```

### Test Types Implemented

**1. Unit Tests - Model Layer** (10 test classes)
- Test entity validation (age range, required fields)
- Test business logic methods (`userProfile.isBeginner()`)
- Test JPA lifecycle callbacks (`@PrePersist`, `@PreUpdate`)

```java
@Test
@DisplayName("Age must be within valid range 13-120")
void testAgeRange() {
    userProfile.setAge(12);
    assertFalse(validator.validate(userProfile).isEmpty());
}
```

**2. Unit Tests - Service Layer** (8 test classes)
- Mock dependencies with Mockito
- Test business logic in isolation
- Verify correct repository calls

```java
@Test
@DisplayName("Should create brick after workout completion")
void testCreateBrick() {
    when(workoutSessionRepository.findById(1L)).thenReturn(Optional.of(testSession));
    when(brickRepository.save(any(Brick.class))).thenReturn(testBrick);

    BrickResponse response = brickService.createBrick(1L);

    assertNotNull(response);
    verify(brickRepository).save(any(Brick.class));
}
```

**3. Integration Tests - API Layer** (1 comprehensive test class)
- `@SpringBootTest` loads full application context
- `MockMvc` simulates HTTP requests
- Tests full request/response cycle
- Verifies JSON response structure

```java
@Test
@DisplayName("GET /api/v1/workouts - Should return all workouts")
void testGetAllWorkouts() throws Exception {
    mockMvc.perform(get("/api/v1/workouts"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray());
}
```

### Running Tests

```bash
cd backend
mvn test                    # Run all tests
mvn test -Dtest=BrickServiceTest   # Run specific test class
```

**Interview point**: "I follow the testing pyramid—lots of fast unit tests, fewer integration tests. Unit tests use Mockito to isolate the class under test. Integration tests verify the full stack works together."

---

## Key Technical Decisions

| Decision | Choice | Why |
|----------|--------|-----|
| Backend Language | Java 21 | Industry standard, strong typing, excellent Spring ecosystem |
| Backend Framework | Spring Boot 3.4 | Convention over configuration, built-in DI, JPA integration |
| Database (Dev) | SQLite | Zero config, file-based, perfect for single-user MVP |
| Database (Prod) | PostgreSQL | Production-ready, concurrent connections, Docker-friendly |
| Mobile Framework | React Native + Expo | Single codebase for iOS/Android, rapid iteration |
| Styling | NativeWind (Tailwind) | Utility-first, consistent design system, fast development |
| AI | Ollama + Llama 3.2 | Free, local, no API costs, privacy-friendly |
| Containerization | Docker Compose | Reproducible environments, one-command startup |

---

## API Endpoints Reference

### Core Endpoints

| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/api/v1/profile/{id}` | Get user profile |
| PUT | `/api/v1/profile/{id}` | Update user profile |
| GET | `/api/v1/workouts` | List all workouts |
| GET | `/api/v1/workouts/{id}/exercises` | Get workout with exercises |
| POST | `/api/v1/sessions` | Start workout session |
| PUT | `/api/v1/sessions/{id}/complete` | Complete workout session |
| GET | `/api/v1/bricks/calendar/{profileId}` | Get brick calendar |
| GET | `/api/v1/bricks/stats/{profileId}` | Get brick statistics |
| POST | `/api/v1/brix/chat` | Chat with BRIX AI |
| POST | `/api/v1/daily-logs` | Log daily check-in |

---

## Potential Interview Questions

### Architecture
- **Q: Why separate DTOs from entities?**
  - A: Decouples API contract from database schema. I can change the database without breaking clients, and I control exactly what data is exposed.

- **Q: Why not use a NoSQL database?**
  - A: Relational data model fits well—users have workouts, workouts have exercises. PostgreSQL handles this naturally with foreign keys and joins.

### Docker/DevOps
- **Q: What happens if PostgreSQL isn't ready when the backend starts?**
  - A: The health check in docker-compose ensures PostgreSQL is accepting connections before the backend container starts.

- **Q: How would you deploy this to production?**
  - A: Kubernetes with separate deployments for backend and database. Use a managed PostgreSQL service (RDS/Cloud SQL). Add ingress controller for routing, secrets for credentials.

### Testing
- **Q: How do you decide what to test?**
  - A: Business logic gets unit tests. API contracts get integration tests. I focus on behavior, not implementation details.

- **Q: Why mock in unit tests?**
  - A: Isolation. When testing BrickService, I don't want a database failure to break my test. Mocks let me control dependencies and test edge cases.

### Code Quality
- **Q: How do you handle errors?**
  - A: Custom exceptions (`ResourceNotFoundException`) with a global exception handler that returns consistent error responses.

- **Q: How do you prevent N+1 queries?**
  - A: JPA fetch strategies and `@EntityGraph` when needed. I also use DTOs to control exactly what's loaded.

---

## Demo Flow Suggestion

1. **Show docker-compose.yml** - "One command starts the entire backend"
2. **Run `docker compose up`** - Show PostgreSQL and backend starting
3. **Open mobile app** - Walk through main screens
4. **Start a workout** - Show the full flow from UI to database
5. **Show brick calendar** - Explain the visual progress metaphor
6. **Open BRIX chat** - Demonstrate AI coaching
7. **Show code** - Pick one vertical slice (e.g., brick creation)
8. **Run tests** - `mvn test` to show test coverage

---

## Files to Have Open

- `docker-compose.yml` - Show orchestration
- `backend/Dockerfile` - Explain multi-stage build
- `BrickService.java` - Core business logic example
- `BrickServiceTest.java` - Show testing approach
- `ApiIntegrationTest.java` - Show API testing
- `mobile/src/screens/ProgressScreen.tsx` - Frontend example
- `mobile/src/services/api.ts` - API client pattern

Good luck tomorrow!
