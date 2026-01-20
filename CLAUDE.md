# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

B3 (Brick by Brick) is a mobile fitness application with an adaptive AI behavior coach called BRIX. The app uses a "brick" metaphor where each completed workout is a brick in the user's fitness foundation.

## Tech Stack

- **Backend**: Java 21, Spring Boot 3.4, Spring Data JPA, SQLite
- **Frontend**: React Native with Expo, NativeWind (Tailwind), TypeScript
- **Database**: SQLite (embedded, file-based at `backend/b3.db`)

## Development Commands

### Backend (Spring Boot)
```bash
cd backend
mvn clean install          # Build the project
mvn spring-boot:run        # Run the server (port 8080)
mvn test                   # Run all tests
mvn test -Dtest=ClassName  # Run specific test class
```

### Frontend (React Native/Expo)
```bash
cd mobile
npm install                # Install dependencies
npx expo start             # Start Expo dev server
npx expo start --ios       # Start with iOS simulator
npx expo start --android   # Start with Android emulator
npm test                   # Run tests
```

## Architecture

### Three-Tier Architecture
```
Mobile App (React Native) → REST API → Spring Boot Services → SQLite
```

### Backend Structure (`backend/src/main/java/com/b3/`)
- **controller/**: REST endpoints (`/api/v1/*`) - WorkoutController, ProfileController, BrickController, etc.
- **service/**: Business logic - WorkoutService, BrickService, BehaviorProfileService, etc.
- **repository/**: Spring Data JPA interfaces
- **model/**: JPA entities with validation annotations
- **dto/**: Request/response DTOs (separate `request/` and `response/` packages)
- **mapper/**: Entity-to-DTO mapping utilities
- **config/**: CorsConfig, DataInitializer (seeds demo data on startup)
- **exception/**: Custom exceptions and RestExceptionHandler

### Frontend Structure (`mobile/src/`)
- **screens/**: Main screens (HomeScreen, WorkoutsScreen, ProgressScreen, BrixScreen, ProfileScreen)
- **components/**: Reusable UI components (Card, Button, Input, Header, B3Logo)
- **services/api.ts**: Centralized API client with typed endpoints for all backend calls
- **types/**: TypeScript interfaces matching backend DTOs
- **navigation/**: React Navigation setup with bottom tab navigator
- **constants/theme.ts**: Color palette and design tokens

### Key Domain Concepts

**Brick System**: Completed workouts create "bricks" that build a visual wall showing progress. Bricks have types (WORKOUT, STREAK_BONUS, MILESTONE) and are tracked in the `brick` table.

**BehaviorProfile**: BRIX's adaptive intelligence engine. Tracks:
- Streak data (consecutiveDays, longestStreak)
- Motivation state (MOTIVATED, NEUTRAL, STRUGGLING)
- Momentum trend (RISING, FALLING, STABLE)
- Coaching tone (ENCOURAGING, CHALLENGING, EMPATHETIC, CELEBRATORY)
- Energy and fatigue scores

The `logWorkout()` method in BehaviorProfile automatically updates streaks, consistency, motivation, momentum, and adjusts the coaching tone.

**Single User Design**: This MVP uses a single demo user (profileId=1) created by DataInitializer. No authentication is implemented.

## API Base URL

Backend serves at `http://localhost:8080/api/v1`. Key endpoints:
- `/profile` - User profile (GET, PUT)
- `/workouts` - Workout library with filtering by type/difficulty
- `/sessions` - Start/complete workout sessions
- `/bricks` - Progress tracking calendar and stats
- `/daily-logs` - Energy/mood/stress logging
- `/milestones` - Achievement tracking
- `/behavior` - BRIX behavior profile

## Database

SQLite database file: `backend/b3.db`
- Auto-created on first run via `spring.jpa.hibernate.ddl-auto=update`
- Demo data seeded by DataInitializer (exercises, workouts, default user)
- Delete `b3.db` to reset database

## Testing Patterns

Backend tests are organized by layer:
- `model/*Test.java` - Entity validation tests
- `service/*ServiceTest.java` - Business logic tests
- `repository/*RepositoryTest.java` - JPA query tests
- `controller/ApiIntegrationTest.java` - Full API integration tests

## External APIs

- **ExerciseDB API**: For exercise data (configured in application.properties)
- **OpenAI API**: Placeholder for BRIX AI responses (not yet implemented)
