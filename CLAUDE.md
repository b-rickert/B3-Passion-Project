# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

B3 (Brick by Brick) is a mobile fitness application with an adaptive AI behavior coach called BRIX. The app uses a "brick" metaphor where each completed workout is a brick in the user's fitness foundation. The core differentiator is **behavioral transformation** through adaptive coaching, not just workout content.

## Tech Stack

- **Backend**: Java 21, Spring Boot 3.4.1, Spring Data JPA, SQLite
- **Frontend**: React Native 0.81 with Expo 54, NativeWind 4.x, TypeScript
- **Database**: SQLite (embedded, file-based at `backend/b3.db`)
- **AI**: Ollama (Llama) via `OllamaService.java` for free local BRIX coaching responses

## AI Setup (Ollama/Llama - Free)

BRIX uses Ollama to run Llama locally for free AI responses.

1. **Install Ollama**: https://ollama.ai (macOS/Linux/Windows)
2. **Pull the model**:
   ```bash
   ollama pull llama3.2
   ```
3. **Run Ollama** (runs in background):
   ```bash
   ollama serve
   ```

The backend auto-detects Ollama at `localhost:11434`. If unavailable, BRIX falls back to keyword-based responses.

## Development Commands

### Backend (Spring Boot)
```bash
cd backend
mvn clean install                              # Build the project
mvn spring-boot:run                            # Run the server (port 8080)
mvn test                                       # Run all tests
mvn test -Dtest=ClassName                      # Run specific test class
mvn test -Dtest=ClassName#methodName           # Run specific test method
```

### Frontend (React Native/Expo)
```bash
cd mobile
npm install                # Install dependencies
npx expo start             # Start Expo dev server
npx expo start --ios       # Start with iOS simulator
npx expo start --android   # Start with Android emulator
```

Note: No test runner is currently configured for the mobile app.

## Architecture

### Three-Tier Architecture
```
Mobile App (React Native) → REST API (localhost:8080) → Spring Boot Services → SQLite
```

### Backend Structure (`backend/src/main/java/com/b3/`)
- **controller/**: REST endpoints (`/api/v1/*`) - WorkoutController, ProfileController, BrickController, BrixController, etc.
- **service/**: Business logic including ClaudeService (AI), BrixService, WorkoutSessionService, BrickService
- **repository/**: Spring Data JPA interfaces
- **model/**: JPA entities - BehaviorProfile, Brick, Workout, WorkoutSession, DailyLog, Milestone, etc.
- **dto/**: Request/response DTOs with `request/` and `response/` subpackages
- **mapper/**: Entity-to-DTO mapping utilities (manual mappers, not MapStruct)
- **config/**: CorsConfig, DataInitializer (seeds demo data on startup)
- **exception/**: ResourceNotFoundException, BadRequestException, DuplicateResourceException + RestExceptionHandler

### Frontend Structure (`mobile/src/`)
- **screens/**: HomeScreen, WorkoutsScreen, WorkoutDetailScreen, ProgressScreen, BrixScreen, ProfileScreen, DailyLogScreen
- **components/**: Card, Button, Input, Header, B3Logo, Divider
- **services/api.ts**: Centralized typed API client with interceptors - exports `b3Api` object with `profile`, `workout`, `session`, `brick`, `dailyLog`, `milestone`, `behavior` namespaces
- **types/**: TypeScript interfaces matching backend DTOs (`api.ts`, `index.ts`)
- **navigation/**: Bottom tab navigator via React Navigation
- **constants/theme.ts**: Color palette (zinc-based dark theme with orange accents)

### Key Domain Concepts

**Brick System**: Completed workouts create "bricks" that build a visual wall. Bricks have types (WORKOUT, STREAK_BONUS, MILESTONE) tracked in the `brick` table. The `BrickService` creates bricks when sessions complete. Missing days are framed as gaps to repair, not failures.

**BehaviorProfile**: BRIX's adaptive intelligence engine. Tracks:
- Streak data (consecutiveDays, longestStreak)
- Motivation state (MOTIVATED, NEUTRAL, STRUGGLING)
- Momentum trend (RISING, FALLING, STABLE)
- Coaching tone (ENCOURAGING, CHALLENGING, EMPATHETIC, CELEBRATORY)
- Energy and fatigue scores

The `logWorkout()` method in BehaviorProfile automatically updates streaks, consistency, motivation, momentum, and adjusts the coaching tone.

**BRIX Coaching Tones** (determined by behavioral rules):
- **Supportive/Encouraging**: Low energy, returning users, gentle encouragement
- **Challenging**: High consistency streaks, performance improvement opportunities
- **Empathetic**: After long breaks, low motivation periods
- **Celebratory**: Milestone achievements, streak completions

**Single User Design**: This MVP uses a single demo user (profileId=1) created by DataInitializer. No authentication is implemented. The frontend API client defaults to `DEFAULT_PROFILE_ID = 1`.

## API Endpoints

Backend serves at `http://localhost:8080/api/v1`:
- `/profile`, `/profile/{id}` - User profile CRUD
- `/workouts`, `/workouts/{id}`, `/workouts/type/{type}`, `/workouts/difficulty/{level}` - Workout library
- `/sessions`, `/sessions/{id}/complete`, `/sessions/active/{profileId}` - Workout sessions
- `/bricks/calendar/{profileId}`, `/bricks/stats/{profileId}` - Progress tracking
- `/daily-logs`, `/daily-logs/today/{profileId}` - Energy/mood/stress logging
- `/milestones/{profileId}`, `/milestones/{profileId}/check` - Achievement tracking
- `/behavior`, `/behavior/{profileId}` - BRIX behavior profile
- `/brix/chat`, `/brix/recommendation` - BRIX AI interactions

## Database

SQLite database file: `backend/b3.db`
- Auto-created on first run via `spring.jpa.hibernate.ddl-auto=update`
- Demo data seeded by DataInitializer (exercises, workouts, default user profile with BehaviorProfile)
- Delete `b3.db` to reset database completely

## Testing

Backend tests are in `backend/src/test/java/com/b3/`:
- `model/*Test.java` - Entity validation tests
- `service/*ServiceTest.java` - Business logic tests with mocked repositories
- `repository/*RepositoryTest.java` - JPA query tests
- `controller/ApiIntegrationTest.java` - Full API integration tests

## External APIs

- **ExerciseDB API**: For fetching exercise data (see `ExerciseApiService.java`, configured in application.properties)
- **Claude API**: BRIX AI responses via `ClaudeService.java` (requires `ANTHROPIC_API_KEY` environment variable)
