# B3 PROJECT OVERVIEW

A detailed walkthrough of the B3 codebase with specific code references for portfolio presentation.

---

## Elevator Pitch (60 seconds)

> "Hey, thanks for stopping by! So this is B3—Brick by Brick. It's a fitness app, but not like the ones you've probably tried and quit.
>
> Here's the thing: **90% of people who download fitness apps quit within two months.** Not because the apps are bad, but because life happens. You miss a day, feel guilty, and before you know it you're starting over from scratch for the fifth time this year.
>
> B3 flips that script. Instead of counting calories or tracking PRs, we focus on one thing: **showing up**. Every workout you complete—whether it's 5 minutes or 50—becomes a brick in your foundation. You're literally building something you can see grow.
>
> And the AI coach, **BRIX**, actually learns how you work. Stressed out? It suggests a quick stretch instead of pushing HIIT. On a roll with a seven-day streak? It'll challenge you to step it up. It adapts to *you*, not the other way around.
>
> The whole philosophy is that **missing a day isn't failure—it's just a gap in your wall** that you can repair tomorrow. Some days you lay more bricks, some days fewer, but your foundation keeps growing.
>
> I built this because I'm one of those chronic restarters. And I figured if I could solve this problem for myself, I could probably help a lot of other people too."

---

## Quick Start Commands

```bash
# 1. Start AI (keep running in separate terminal)
ollama pull llama3.2
ollama serve

# 2. Start backend (new terminal)
cd backend
mvn spring-boot:run    # Runs on http://localhost:8080

# 3. Start mobile app (new terminal)
cd mobile
npm install
npx expo start         # Press 'i' for iOS, 'a' for Android
```

**Reset database:** Delete `backend/b3.db` to regenerate with fresh demo data.

---

## Why We Migrated: SQLite → PostgreSQL + Docker

### The Problem with SQLite for Production

SQLite is excellent for development:
- Zero configuration (just a file)
- No separate server process
- Perfect for single-user demos

But it has **production limitations**:
- **No concurrent writes** — Only one write operation at a time
- **No network access** — Database is a local file, can't share across servers
- **Limited scalability** — Not designed for high-traffic applications
- **No user management** — No authentication/authorization at DB level

### The Solution: PostgreSQL + Docker

We added PostgreSQL as the **production database** while keeping SQLite for local development:

```
Local Development          Production (Docker)
─────────────────          ──────────────────
SQLite (b3.db)      →      PostgreSQL container
- Zero setup               - Industry standard
- Fast iteration           - Concurrent connections
- Easy reset               - Scales horizontally
```

### How It Works: Spring Profiles

**File:** `backend/src/main/resources/application.properties` (default - SQLite)
```properties
spring.datasource.url=jdbc:sqlite:b3.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
```

**File:** `backend/src/main/resources/application-docker.properties` (PostgreSQL)
```properties
spring.datasource.url=jdbc:postgresql://postgres:5432/b3
spring.datasource.username=b3user
spring.datasource.password=b3password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

The `docker` profile is activated in the Dockerfile:
```dockerfile
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]
```

### Docker Compose Architecture

**File:** `docker-compose.yml`
```yaml
services:
  postgres:
    image: postgres:16-alpine
    environment:
      POSTGRES_DB: b3
      POSTGRES_USER: b3user
      POSTGRES_PASSWORD: b3password
    volumes:
      - postgres_data:/var/lib/postgresql/data  # Persistent storage
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U b3user -d b3"]

  backend:
    build: ./backend
    ports:
      - "8080:8080"
    depends_on:
      postgres:
        condition: service_healthy  # Wait for DB to be ready
    environment:
      - SPRING_PROFILES_ACTIVE=docker
```

### Key Design Decisions

1. **Health checks** — Backend waits for PostgreSQL to be fully ready before starting
2. **Named volumes** — Data persists across container restarts (`postgres_data`)
3. **Alpine images** — Smaller container sizes for faster deployment
4. **Environment variables** — Credentials passed via compose, not hardcoded

### Running with Docker

```bash
# Start everything (PostgreSQL + Backend)
docker-compose up --build

# Backend available at http://localhost:8080
# Note: Ollama must still run on host machine
```

### Talking Point for Interviews

> "We started with SQLite for rapid prototyping—zero config, just works. But SQLite doesn't support concurrent writes, which would be a problem with multiple users. So we added PostgreSQL for production, using Spring profiles to switch between them. Docker Compose orchestrates the services, with health checks ensuring the backend doesn't start until the database is ready. This shows a realistic development-to-production path."

---

## Key Design Comments Location Guide

I've added `KEY DESIGN:` comments throughout the codebase at important architectural decision points. Here's where to find them:

### Backend (Java/Spring Boot)

| File | Location | What It Explains |
|------|----------|------------------|
| `BrixService.java` | Line ~57 | Main AI chat entry point—context-aware response flow |
| `BrixService.java` | Line ~86 | Graceful degradation pattern (AI → keyword fallback) |
| `BrixService.java` | Line ~297 | Additive scoring algorithm for workout recommendations |
| `BrixService.java` | Line ~368 | Two-layer tone selection algorithm (4 tones) |
| `BehaviorProfile.java` | Line ~205 | Rich domain model—6 cascading calculations on workout log |
| `BehaviorProfile.java` | Line ~275 | Automatic coaching tone adjustment priority |
| `BrickService.java` | Line ~60 | One-brick-per-day rule (consistency > volume) |
| `OllamaService.java` | Line ~129 | Dynamic system prompt construction for AI |
| `RestExceptionHandler.java` | Line ~17 | Centralized exception handling with @RestControllerAdvice |
| `DataInitializer.java` | Line ~17 | CommandLineRunner seeding pattern |

### Mobile (React Native/TypeScript)

| File | Location | What It Explains |
|------|----------|------------------|
| `api.ts` | Line ~1 | Typed API client with domain modules |
| `haptics.ts` | Line ~1 | Semantic haptics abstraction layer |
| `haptics.ts` | Line ~43 | Brick placed pattern (heavy + light tap sequence) |
| `BrickBackground.tsx` | Line ~1 | Procedural 3D brick wall generation |
| `BrickBackground.tsx` | Line ~92 | Deterministic pseudo-random formula |
| `WorkoutSessionScreen.tsx` | Line ~223 | Multi-sensory celebration (haptic + confetti + alert) |
| `BrixScreen.tsx` | Line ~33 | Optimistic UI pattern for chat |

---

## Table of Contents

1. [Architecture Overview](#architecture-overview)
2. [AI Coaching System (BRIX)](#ai-coaching-system-brix)
3. [Adaptive Tone Selection](#adaptive-tone-selection)
4. [Workout Recommendation Scoring](#workout-recommendation-scoring)
5. [Workout Session Flow](#workout-session-flow)
6. [Brick Progress Tracking](#brick-progress-tracking)
7. [Global Exception Handling](#global-exception-handling)
8. [JPA Entity Design](#jpa-entity-design)
9. [Haptic Feedback System](#haptic-feedback-system)
10. [3D Brick Wall Background](#3d-brick-wall-background)
11. [API Client Design](#api-client-design)
12. [Database Seeding](#database-seeding)
13. [Interview Talking Points](#interview-talking-points)

*(SQLite → PostgreSQL migration covered above in "Why We Migrated" section)*

---

## Architecture Overview

### Layered Backend Architecture

The backend follows a classic **Controller → Service → Repository → Entity** pattern:

```
HTTP Request
    ↓
Controller (validates, routes)
    ↓
Service (business logic)
    ↓
Repository (JPA data access)
    ↓
Database (PostgreSQL/SQLite)
```

**Key Files:**
- Controllers: `backend/src/main/java/com/b3/controller/`
- Services: `backend/src/main/java/com/b3/service/`
- Repositories: `backend/src/main/java/com/b3/repository/`
- Entities: `backend/src/main/java/com/b3/model/`

---

## AI Coaching System (BRIX)

### The Core AI Flow

BRIX uses a **fallback chain**: Ollama (Llama 3.2) → Keyword-based responses

**File:** `backend/src/main/java/com/b3/service/BrixService.java`

#### 1. Chat Entry Point (Lines 57-81)

```java
public BrixChatResponse chat(Long profileId, String userMessage) {
    // Load user context
    UserProfile user = userProfileRepository.findById(profileId)...
    BehaviorProfile behavior = behaviorProfileRepository.findByUserProfile(user)...
    DailyLog todaysLog = dailyLogRepository.findByUserProfileAndLogDate(user, LocalDate.now())...

    // Determine coaching tone based on user state
    BrixMessage.Tone tone = determineTone(behavior, todaysLog);  // Line 70

    // Generate response with AI or fallback
    String response = generateResponse(userMessage, user, behavior, todaysLog, tone);  // Line 73

    // Return with workout recommendation attached
    return new BrixChatResponse(response, tone.name(), getWorkoutRecommendation(profileId));
}
```

**Talking Point:** "BRIX gathers user context (behavior profile, today's daily log) before generating a response, allowing personalized coaching."

#### 2. AI with Graceful Fallback (Lines 86-117)

```java
private String generateResponse(...) {
    // Try Ollama (Llama) if configured and running
    if (ollamaService.isConfigured()) {  // Line 91
        try {
            String aiResponse = ollamaService.generateBrixResponse(
                userMessage,
                user.getDisplayName().split(" ")[0],  // First name only
                behavior != null ? behavior.getConsecutiveDays() : 0,
                behavior != null ? behavior.getTotalBricksLaid() : 0,
                // ... energy, stress, mood, fitness level, goal
            );
            if (aiResponse != null && !aiResponse.isEmpty()) {
                return aiResponse;
            }
        } catch (Exception e) {
            logger.error("Ollama failed, falling back to keywords: {}", e.getMessage());
        }
    }
    // Fall back to keyword-based response
    return generateKeywordResponse(userMessage, user, behavior, todaysLog, tone);  // Line 116
}
```

**Talking Point:** "The system gracefully degrades—if the AI is unavailable, it falls back to keyword matching, ensuring the app always responds."

### Ollama Integration

**File:** `backend/src/main/java/com/b3/service/OllamaService.java`

#### Health Check (Lines 44-60)

```java
public boolean isConfigured() {
    if (!enabled) return false;
    try {
        ResponseEntity<String> response = restTemplate.getForEntity(
            ollamaUrl + "/api/tags", String.class
        );
        return response.getStatusCode() == HttpStatus.OK;
    } catch (Exception e) {
        logger.debug("Ollama not reachable: {}", e.getMessage());
        return false;
    }
}
```

**Talking Point:** "Before each request, we check if Ollama is running—this prevents slow timeouts when the AI service is down."

#### Dynamic System Prompt (Lines 129-170)

```java
private String buildBrixSystemPrompt(String userName, int streak, int totalBricks, ...) {
    StringBuilder prompt = new StringBuilder();
    prompt.append("You are BRIX, an enthusiastic AI fitness coach...\n\n");

    prompt.append("USER CONTEXT:\n");
    prompt.append(String.format("- Name: %s\n", userName));
    prompt.append(String.format("- Current streak: %d days\n", streak));
    prompt.append(String.format("- Total bricks (workouts): %d\n", totalBricks));
    if (energyLevel != null) {
        prompt.append(String.format("- Today's energy level: %d/5\n", energyLevel));
    }
    // ... builds context-aware prompt
}
```

**Talking Point:** "The system prompt is dynamically constructed with the user's current state—streak, energy, mood—so the AI can give contextually appropriate responses."

---

## Adaptive Tone Selection

### How BRIX Chooses Its Coaching Style

**File:** `backend/src/main/java/com/b3/service/BrixService.java` (Lines 368-397)

```java
private BrixMessage.Tone determineTone(BehaviorProfile behavior, DailyLog todaysLog) {
    // Priority 1: Today's emotional state
    if (todaysLog != null) {
        if (todaysLog.getMood() == DailyLog.Mood.STRESSED ||
            todaysLog.getMood() == DailyLog.Mood.LOW) {
            return BrixMessage.Tone.EMPATHETIC;  // Line 372
        }
        if (todaysLog.getEnergyLevel() >= 4 && todaysLog.getMood() == DailyLog.Mood.GREAT) {
            return BrixMessage.Tone.CHALLENGING;  // Line 376 - Push them!
        }
    }

    // Priority 2: Behavioral patterns
    if (behavior != null) {
        // Celebrate milestones
        if (behavior.getConsecutiveDays() == 7 ||
            behavior.getConsecutiveDays() == 30 ||
            behavior.getConsecutiveDays() == behavior.getLongestStreak()) {
            return BrixMessage.Tone.CELEBRATORY;  // Line 384
        }
        // Support struggling users
        if (behavior.getMotivationState() == MotivationState.STRUGGLING) {
            return BrixMessage.Tone.EMPATHETIC;  // Line 388
        }
        // Challenge motivated users on a roll
        if (behavior.getMotivationState() == MotivationState.MOTIVATED &&
            behavior.getMomentumTrend() == MomentumTrend.RISING) {
            return BrixMessage.Tone.CHALLENGING;  // Line 393
        }
    }

    return BrixMessage.Tone.ENCOURAGING;  // Default: positive reinforcement
}
```

**Talking Point:** "The tone selection algorithm prioritizes immediate emotional state (from daily check-in), then falls back to behavioral patterns. A stressed user always gets empathy; a user on a record streak gets challenged."

#### All 4 Tones Summarized

| Tone | Trigger Conditions | Line |
|------|-------------------|------|
| **EMPATHETIC** | Mood is STRESSED or LOW | 372 |
| **EMPATHETIC** | Motivation state is STRUGGLING | 387 |
| **CHALLENGING** | Energy ≥ 4 AND mood is GREAT | 375 |
| **CHALLENGING** | MOTIVATED state + RISING momentum | 392 |
| **CELEBRATORY** | Hit milestone: 7 days, 30 days, or personal best streak | 383 |
| **ENCOURAGING** | Default fallback (none of above) | 396 |

**Key Design Decision:** EMPATHETIC and CHALLENGING each have two paths—one from the daily log (immediate state) and one from behavior profile (patterns over time). This creates a layered system where today's check-in can override historical trends.

---

## Workout Recommendation Scoring

### Points-Based Workout Selection Algorithm

**File:** `backend/src/main/java/com/b3/service/BrixService.java` (Lines 297-347)

BRIX doesn't just pick random workouts—it uses a **scoring algorithm** that weighs multiple factors:

```java
private Workout scoreAndSelectWorkout(List<Workout> workouts, UserProfile user,
                                       DailyLog todaysLog, BehaviorProfile behavior) {
    Map<Workout, Integer> scores = new HashMap<>();

    for (Workout workout : workouts) {
        int score = 50;  // Base score

        // Factor 1: Match user's fitness level (+20)
        if (workout.getDifficultyLevel().name().equals(user.getFitnessLevel().name())) {
            score += 20;
        }

        // Factor 2: Adjust for today's energy level
        if (todaysLog != null) {
            int energy = todaysLog.getEnergyLevel();
            int stress = todaysLog.getStressLevel();

            if (energy <= 2) {  // Low energy day
                if (workout.getDifficultyLevel() == DifficultyLevel.BEGINNER) score += 30;
                if (workout.getEstimatedDuration() <= 20) score += 20;  // Short workouts
                if (workout.getWorkoutType() == WorkoutType.FLEXIBILITY) score += 25;  // Gentle
            }

            if (energy >= 4) {  // High energy day
                if (workout.getDifficultyLevel() == DifficultyLevel.ADVANCED) score += 15;
                if (workout.getWorkoutType() == WorkoutType.STRENGTH) score += 10;
            }

            if (stress >= 4) {  // High stress - recommend calming workouts
                if (workout.getWorkoutType() == WorkoutType.FLEXIBILITY) score += 30;
                if (workout.getEstimatedDuration() <= 25) score += 15;
            }
        }

        // Factor 3: Match user's primary goal (+15)
        if (user.getPrimaryGoal() != null) {
            if (goal.equals("STRENGTH") && workout.getWorkoutType() == WorkoutType.STRENGTH) {
                score += 15;
            }
            // ... cardio, flexibility
        }

        scores.put(workout, score);
    }

    // Select highest scoring workout
    return scores.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(workouts.get(0));
}
```

#### Scoring Breakdown

| Factor | Points | Condition |
|--------|--------|-----------|
| Base score | 50 | Every workout starts here |
| Fitness level match | +20 | Workout difficulty = user level |
| Low energy + Beginner workout | +30 | Energy ≤ 2 |
| Low energy + Short duration | +20 | Duration ≤ 20 min |
| Low energy + Flexibility | +25 | Gentle workout type |
| High energy + Advanced | +15 | Energy ≥ 4 |
| High stress + Flexibility | +30 | Stress ≥ 4 |
| Goal alignment | +15 | Workout type matches goal |

**Talking Point:** "The recommendation system uses additive scoring—each workout accumulates points based on how well it matches the user's current state and goals. A stressed, low-energy user might see a 15-minute yoga workout score 50+30+25+30 = 135 points, while an intense HIIT workout only scores 50."

#### Human-Readable Recommendation Reasons (Lines 349-362)

```java
private String generateRecommendationReason(Workout workout, DailyLog todaysLog, ...) {
    if (todaysLog != null) {
        if (todaysLog.getEnergyLevel() <= 2) {
            return "Perfect for your energy level today - gentle but effective!";
        }
        if (todaysLog.getStressLevel() >= 4) {
            return "Great for stress relief - you'll feel amazing after!";
        }
        if (todaysLog.getEnergyLevel() >= 4) {
            return "You've got great energy today - let's maximize it!";
        }
    }
    return "Matches your fitness level and goals perfectly!";
}
```

**Talking Point:** "We don't just recommend—we explain WHY. The reason text is dynamically generated based on the same factors that influenced the score."

---

## The BehaviorProfile Entity

**File:** `backend/src/main/java/com/b3/model/BehaviorProfile.java`

#### State Enums (Lines 26-57)

```java
public enum MotivationState {
    MOTIVATED,    // Consistency score >= 0.7
    NEUTRAL,      // Normal engagement
    STRUGGLING    // Consistency score < 0.4
}

public enum MomentumTrend {
    RISING,       // 3+ day streak, worked out yesterday
    FALLING,      // 2+ day gap
    STABLE        // Consistent but not accelerating
}

public enum CoachingTone {
    ENCOURAGING,  // Default supportive tone
    CHALLENGING,  // Push harder when ready
    EMPATHETIC,   // Gentle when struggling
    CELEBRATORY   // Celebrate milestones
}
```

#### Business Logic: Logging a Workout (Lines 205-213)

```java
public void logWorkout(LocalDate today, int daysSinceCreation) {
    updateStreak(today);           // Update consecutive days
    incrementBricks();             // +1 total bricks
    calculateConsistency(daysSinceCreation);  // Consistency = bricks / days
    updateMotivation();            // Determine motivation state
    updateMomentum(today);         // Determine momentum trend
    adjustTone();                  // Select appropriate coaching tone
    this.lastWorkoutDate = today;
}
```

**Talking Point:** "When a workout is logged, the BehaviorProfile cascades through six calculations—this keeps all behavioral metrics in sync without the controller needing to know the details."

#### Auto-Adjusting Coaching Tone (Lines 275-291)

```java
private void adjustTone() {
    CoachingTone oldTone = currentTone;

    if (motivationState == MotivationState.STRUGGLING || fatigueScore >= 0.7) {
        currentTone = CoachingTone.EMPATHETIC;
    } else if (consecutiveDays >= 7) {
        currentTone = CoachingTone.CHALLENGING;
    } else if (momentumTrend == MomentumTrend.RISING) {
        currentTone = CoachingTone.CELEBRATORY;
    } else {
        currentTone = CoachingTone.ENCOURAGING;
    }

    if (oldTone != currentTone) {
        lastToneChange = LocalDateTime.now();  // Track when tone changed
    }
}
```

---

## Workout Session Flow

### Starting → Executing → Completing a Workout

**Backend File:** `backend/src/main/java/com/b3/service/WorkoutSessionService.java`

#### Creating a Session (Lines 50-69)

```java
public WorkoutSessionResponse createSession(WorkoutSessionCreateRequest request) {
    // Validate user and workout exist
    UserProfile user = userProfileRepository.findById(request.getProfileId())
        .orElseThrow(() -> new ResourceNotFoundException("UserProfile", request.getProfileId()));
    Workout workout = workoutRepository.findById(request.getWorkoutId())
        .orElseThrow(() -> new ResourceNotFoundException("Workout", request.getWorkoutId()));

    // Create session with start time
    WorkoutSession session = new WorkoutSession(user, workout, LocalDateTime.now());
    WorkoutSession saved = workoutSessionRepository.save(session);

    return mapToResponse(saved);
}
```

#### Completing a Session (Lines 75-102)

```java
public WorkoutSessionResponse completeSession(Long sessionId, WorkoutSessionCompleteRequest request) {
    WorkoutSession session = workoutSessionRepository.findById(sessionId)
        .orElseThrow(() -> new ResourceNotFoundException("WorkoutSession", sessionId));

    // Prevent double-completion
    if (session.getCompletionStatus() == CompletionStatus.COMPLETED) {
        throw new BadRequestException("Workout session is already completed");
    }

    session.completeSession(request.getActualDuration());  // Line 88

    // Update user stats (streak, total workouts)
    updateUserStats(session.getUserProfile());  // Line 97 - Critical!

    return mapToResponse(updated);
}
```

#### Updating User Stats (Lines 136-149)

```java
private void updateUserStats(UserProfile user) {
    user.incrementTotalWorkouts();  // +1 total
    user.updateStreak();            // Streak logic in entity
    userProfileRepository.save(user);

    log.info("Updated user stats - Total workouts: {}, Current streak: {}",
        user.getTotalWorkouts(), user.getCurrentStreak());
}
```

**Talking Point:** "The session service orchestrates the completion flow—it validates the session isn't already complete, marks it done, then delegates streak/stat updates to the UserProfile entity."

### Mobile Workout Execution

**File:** `mobile/src/screens/WorkoutSessionScreen.tsx`

#### Session Initialization (Lines 81-125)

```typescript
useEffect(() => {
  const initSession = async () => {
    // Load workout and exercises in parallel
    const [workoutData, exercisesData] = await Promise.all([
      workoutApi.getWorkoutById(workoutId),
      workoutApi.getWorkoutExercises(workoutId),
    ]);

    // Initialize progress tracking for each exercise
    const progress = exercisesData.map(ex => ({
      exerciseId: ex.exerciseId,
      completedSets: 0,
      totalSets: ex.sets,
      isComplete: false,
    }));
    setExerciseProgress(progress);

    // Start session via API
    const sessionData = await sessionApi.startSession({
      profileId: 1,
      workoutId: workoutId,
    });
    setSession(sessionData);
  };
  initSession();
}, [workoutId]);
```

**Talking Point:** "We use `Promise.all` to load workout data and exercises in parallel, reducing initial load time."

#### Completing a Set with Haptic Feedback (Lines 166-192)

```typescript
const completeSet = useCallback(() => {
  Haptics.setComplete();  // Tactile feedback!

  const newCompletedSets = currentProgress.completedSets + 1;
  const isExerciseComplete = newCompletedSets >= currentProgress.totalSets;

  setExerciseProgress(prev =>
    prev.map((p, i) =>
      i === currentExerciseIndex
        ? { ...p, completedSets: newCompletedSets, isComplete: isExerciseComplete }
        : p
    )
  );

  // Auto-start rest timer
  if (!isExerciseComplete) {
    setRestTimeRemaining(currentExercise.restSeconds);
    setIsResting(true);
  }
}, [currentProgress, currentExercise, currentExerciseIndex]);
```

#### Workout Completion with Confetti (Lines 223-262)

```typescript
const finishWorkout = async () => {
  try {
    await sessionApi.completeSession(session.sessionId, {
      actualDuration: Math.floor(elapsedTime / 60),
      notes: `Completed ${completedExercises}/${totalExercises} exercises`,
    });

    // Celebration sequence
    await Haptics.brickPlaced();      // Strong haptic
    setShowConfetti(true);            // Visual celebration
    confettiRef.current?.start();

    setTimeout(() => {
      Alert.alert('Workout Complete!', 'Great job! You just laid another brick.');
    }, 500);
  } catch (err) {
    Alert.alert('Error', 'Failed to save workout.');
  }
};
```

**Talking Point:** "Workout completion triggers a multi-sensory celebration—haptic feedback + confetti animation—creating positive reinforcement."

---

## Brick Progress Tracking

### Creating a Brick After Workout

**File:** `backend/src/main/java/com/b3/service/BrickService.java`

#### Brick Creation Logic (Lines 60-97)

```java
public BrickResponse createBrick(Long sessionId) {
    WorkoutSession session = workoutSessionRepository.findById(sessionId)
        .orElseThrow(() -> new ResourceNotFoundException("WorkoutSession", sessionId));

    // Only completed sessions can create bricks
    if (!session.isCompleted()) {
        throw new IllegalStateException("Cannot create brick for incomplete session");
    }

    LocalDate brickDate = session.getEndTime().toLocalDate();

    // One brick per day rule
    boolean exists = brickRepository.existsByUserProfile_ProfileIdAndBrickDate(
        session.getUserProfile().getProfileId(), brickDate);
    if (exists) {
        throw new IllegalStateException("Brick already exists for this date");
    }

    Brick brick = new Brick(session.getUserProfile(), session, brickDate, Brick.BrickType.WORKOUT);
    return mapToResponse(brickRepository.save(brick));
}
```

**Talking Point:** "The one-brick-per-day rule is enforced at the service layer. Multiple workouts in a day still only create one brick—this encourages consistency over volume."

---

## Global Exception Handling

### Centralized Error Responses with @RestControllerAdvice

**File:** `backend/src/main/java/com/b3/exception/RestExceptionHandler.java`

Instead of try-catch blocks scattered throughout controllers, we use a **global exception handler**:

```java
@RestControllerAdvice  // Applies to all controllers
public class RestExceptionHandler {

    // 404 - Resource Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex, WebRequest request) {

        log.warn("Resource not found: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Not Found",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // 400 - Validation Errors (with field-level details)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException ex, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);  // e.g., {"age": "must be >= 13"}
        });

        // Return structured error with all field violations
        Map<String, Object> response = new HashMap<>();
        response.put("status", 400);
        response.put("error", "Validation Failed");
        response.put("errors", errors);  // All field errors
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 500 - Catch-all for unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, ...) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);
        // Return generic message (don't leak internal details)
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

**Custom Exception Classes:**
- `ResourceNotFoundException.java` - Thrown when entity not found (404)
- `BadRequestException.java` - Thrown for invalid operations (400)
- `DuplicateResourceException.java` - Thrown for uniqueness violations

**Talking Point:** "Using @RestControllerAdvice, all exceptions flow through one handler. This guarantees consistent error response format across the entire API—every error has status, message, and path. The validation handler even returns field-level errors so the mobile app knows exactly which input was wrong."

---

## JPA Entity Design

### Rich Domain Model with Business Logic

**File:** `backend/src/main/java/com/b3/model/UserProfile.java`

Entities aren't just data holders—they contain **business logic**:

#### JPA Lifecycle Callbacks (Lines 127-137)

```java
@PrePersist
protected void onCreate() {
    LocalDateTime now = LocalDateTime.now();
    this.createdAt = now;
    this.updatedAt = now;
}

@PreUpdate
protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
}
```

**Talking Point:** "JPA callbacks automatically set timestamps—we never forget to update `updatedAt` because it happens at the persistence layer."

#### Bidirectional One-to-One Relationship (Lines 95-101)

```java
/**
 * Cascade ALL: When UserProfile is deleted, BehaviorProfile is also deleted
 * orphanRemoval: If relationship is broken, BehaviorProfile is deleted
 */
@OneToOne(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true)
private BehaviorProfile behaviorProfile;
```

**Talking Point:** "The UserProfile owns the BehaviorProfile relationship with cascade delete. If a user is removed, their behavioral data goes with them—no orphaned records."

#### Entity Business Methods (Lines 196-201)

```java
public void updateStreak() {
    this.currentStreak++;
    if (this.currentStreak > this.longestStreak) {
        this.longestStreak = this.currentStreak;
    }
}
```

**Talking Point:** "Streak logic lives in the entity, not the service. The service just calls `user.updateStreak()` and the entity handles the comparison. This is the 'rich domain model' pattern—entities know their own rules."

---

## Haptic Feedback System

### Semantic Haptics Utility

**File:** `mobile/src/utils/haptics.ts`

Instead of calling `Haptics.impactAsync()` directly, we created **semantic haptic functions**:

```typescript
// Light tap - for selections, toggles, small actions
export const lightTap = () => {
  Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light);
};

// Medium tap - for completing sets, navigation
export const mediumTap = () => {
  Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium);
};

// Set complete pattern - satisfying tap for completing a set
export const setComplete = () => {
  Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium);
};

// Rest timer end - alert that rest is over
export const restTimerEnd = () => {
  Haptics.notificationAsync(Haptics.NotificationFeedbackType.Warning);
};
```

#### Custom Haptic Sequences (Lines 44-60)

```typescript
// Brick placed pattern - custom sequence for placing a brick
export const brickPlaced = async () => {
  await Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Heavy);
  setTimeout(() => {
    Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light);
  }, 100);
};

// Streak milestone pattern - celebratory sequence
export const streakMilestone = async () => {
  await Haptics.notificationAsync(Haptics.NotificationFeedbackType.Success);
  setTimeout(() => Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium), 150);
  setTimeout(() => Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light), 300);
};
```

**Talking Point:** "Haptic feedback is a first-class feature. `brickPlaced()` gives a heavy thud followed by a light tap—like a brick landing and settling. `streakMilestone()` creates a cascade of vibrations for celebration. These patterns are reusable across the app."

#### Usage in Components

```typescript
// WorkoutSessionScreen.tsx:170
Haptics.setComplete();  // When completing a set

// WorkoutSessionScreen.tsx:243
await Haptics.brickPlaced();  // When finishing workout

// BrixScreen.tsx:37
Haptics.lightTap();  // When sending a message
```

---

## 3D Brick Wall Background

### Procedural Visual Generation

**File:** `mobile/src/components/BrickBackground.tsx`

The app's signature visual—a 3D brick wall—is **procedurally generated**:

#### Brick Generation with useMemo (Lines 78-101)

```typescript
const bricks = useMemo(() => {
  const result: BrickProps[] = [];
  const rows = Math.ceil(SCREEN_HEIGHT / (BRICK_HEIGHT + MORTAR_GAP)) + 2;
  const cols = Math.ceil(SCREEN_WIDTH / (BRICK_WIDTH + MORTAR_GAP)) + 2;

  for (let row = 0; row < rows; row++) {
    // Offset every other row (standard brick pattern)
    const isOffset = row % 2 === 1;
    const offsetX = isOffset ? -(BRICK_WIDTH / 2) : 0;

    for (let col = 0; col < cols + 1; col++) {
      const x = col * (BRICK_WIDTH + MORTAR_GAP) + offsetX;
      const y = row * (BRICK_HEIGHT + MORTAR_GAP);

      // Deterministic "random" for organic look (same seed = same result)
      const randomSeed = (row * 17 + col * 31) % 100;
      const opacity = 0.75 + (randomSeed / 400);
      const shade = randomSeed % 3;  // 3 different depth levels

      result.push({ x, y, opacity, shade });
    }
  }
  return result;
}, [opacityMultiplier]);
```

**Talking Point:** "The brick pattern is calculated once with `useMemo` and cached. We use a deterministic pseudo-random formula `(row * 17 + col * 31) % 100` so the wall looks organic but renders the same every time—no flickering on re-render."

#### 3D Effect with Highlights and Shadows (Lines 21-57)

```typescript
const Brick: React.FC<BrickProps> = ({ x, y, opacity, shade }) => {
  const shadeColors = [
    { base: '#2a2a30', highlight: '#4a4a55', shadow: '#0a0a0c' },
    { base: '#323238', highlight: '#55555f', shadow: '#0c0c0e' },
    { base: '#252528', highlight: '#45454f', shadow: '#08080a' },
  ];

  return (
    <View style={[styles.brick, { left: x, top: y, opacity }]}>
      <View style={[styles.brickBody, { backgroundColor: brickColor.base }]}>
        {/* Top highlight edge for 3D effect */}
        <View style={[styles.brickHighlight, { backgroundColor: brickColor.highlight }]} />
        {/* Left highlight edge */}
        <View style={[styles.brickHighlightLeft, { backgroundColor: brickColor.highlight }]} />
        {/* Bottom shadow edge */}
        <View style={[styles.brickShadow, { backgroundColor: brickColor.shadow }]} />
        {/* Right shadow edge */}
        <View style={[styles.brickShadowRight, { backgroundColor: brickColor.shadow }]} />
      </View>
    </View>
  );
};
```

**Talking Point:** "Each brick has 4 edge overlays—highlight on top/left, shadow on bottom/right—creating a 3D beveled effect. Combined with 3 shade variations, the wall looks like it has depth even though it's flat 2D views."

#### Gradient Accent Glows (Lines 118-129)

```typescript
{/* Orange accent glow (top right) */}
<LinearGradient
  colors={['rgba(249, 115, 22, 0.15)', 'transparent']}
  style={styles.orangeGlow}  // Positioned off-screen top-right
/>

{/* Blue accent glow (bottom left) */}
<LinearGradient
  colors={['rgba(59, 130, 246, 0.08)', 'transparent']}
  style={styles.blueGlow}  // Positioned off-screen bottom-left
/>
```

**Talking Point:** "Subtle gradient overlays add warmth—an orange glow from the top-right and blue from the bottom-left. It's barely noticeable consciously, but it prevents the wall from feeling cold and sterile."

---

#### Calendar View (Lines 103-119)

```java
public List<BrickResponse> getBrickCalendar(Long profileId, int year, int month) {
    YearMonth yearMonth = YearMonth.of(year, month);
    LocalDate startDate = yearMonth.atDay(1);
    LocalDate endDate = yearMonth.atEndOfMonth();

    List<Brick> bricks = brickRepository.findByUserProfile_ProfileIdAndBrickDateBetween(
        profileId, startDate, endDate);

    return bricks.stream().map(this::mapToResponse).collect(Collectors.toList());
}
```

---

## API Client Design

### Typed API Client with Interceptors

**File:** `mobile/src/services/api.ts`

#### Axios Setup with Logging (Lines 27-61)

```typescript
const api: AxiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api/v1',
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' },
});

// Request logging
api.interceptors.request.use((config) => {
  console.log(`📤 ${config.method?.toUpperCase()} ${config.url}`);
  return config;
});

// Response/error handling
api.interceptors.response.use(
  (response) => {
    console.log(`📥 ${response.status} ${response.config.url}`);
    return response;
  },
  (error: AxiosError<ErrorResponse>) => {
    if (error.response) {
      console.error(`❌ ${error.response.status}: ${error.response.data?.message}`);
    }
    return Promise.reject(error);
  }
);
```

**Talking Point:** "Axios interceptors provide consistent logging and error handling across all API calls, making debugging easier."

#### Domain-Specific API Modules (Lines 70-93, 99-136, etc.)

```typescript
export const profileApi = {
  getProfile: async (): Promise<UserProfileResponse> => {
    const response = await api.get<UserProfileResponse>('/profile');
    return response.data;
  },
  updateProfile: async (data: UserProfileUpdateRequest): Promise<UserProfileResponse> => {
    const response = await api.put<UserProfileResponse>('/profile', data);
    return response.data;
  },
};

export const workoutApi = {
  getAllWorkouts: async (): Promise<WorkoutResponse[]> => {...},
  getWorkoutById: async (workoutId: number): Promise<WorkoutResponse> => {...},
  getWorkoutExercises: async (workoutId: number): Promise<WorkoutExerciseDTO[]> => {...},
};
```

**Talking Point:** "The API is organized into domain-specific modules—profileApi, workoutApi, sessionApi—making it easy to find and use the right endpoints."

#### Combined Export (Lines 370-381)

```typescript
export const b3Api = {
  profile: profileApi,
  workout: workoutApi,
  session: sessionApi,
  brick: brickApi,
  dailyLog: dailyLogApi,
  milestone: milestoneApi,
  behavior: behaviorApi,
  brix: brixApi,
};
```

---

## Database Seeding

### CommandLineRunner Pattern

**File:** `backend/src/main/java/com/b3/config/DataInitializer.java`

#### Seed on Startup (Lines 27-48)

```java
@Bean
CommandLineRunner initDatabase(
        UserProfileRepository userRepo,
        WorkoutRepository workoutRepo,
        ExerciseRepository exerciseRepo,
        WorkoutExerciseRepository workoutExerciseRepo) {

    return args -> {
        if (userRepo.count() == 0) {
            logger.info("Initializing demo data...");

            UserProfile user = new UserProfile();
            user.setDisplayName("Marcus Johnson");
            user.setFitnessLevel(UserProfile.FitnessLevel.INTERMEDIATE);
            user.setPrimaryGoal(UserProfile.PrimaryGoal.STRENGTH);
            userRepo.save(user);
        }
        // ... exercises and workouts
    };
}
```

**Talking Point:** "The CommandLineRunner only seeds if the database is empty—this makes the app immediately usable for demos while not overwriting real data."

#### Helper Methods for Clean Seeding (Lines 549-575)

```java
private Exercise createExercise(ExerciseRepository repo, String name, String description,
                                 Exercise.MuscleGroup muscleGroup, Exercise.EquipmentType equipmentType,
                                 String videoUrl, String imageUrl) {
    Exercise exercise = new Exercise();
    exercise.setName(name);
    exercise.setDescription(description);
    exercise.setMuscleGroup(muscleGroup);
    exercise.setEquipmentType(equipmentType);
    return repo.save(exercise);
}

private void addExerciseToWorkout(WorkoutExerciseRepository repo, Workout workout, Exercise exercise,
                                   int orderIndex, Integer sets, Integer reps, Integer durationSeconds,
                                   Integer restSeconds, String notes) {
    WorkoutExercise we = new WorkoutExercise();
    we.setWorkout(workout);
    we.setExercise(exercise);
    we.setOrderIndex(orderIndex);
    // ... set remaining fields
    repo.save(we);
}
```

---

## Mobile UI Patterns

### BRIX Chat Screen

**File:** `mobile/src/screens/BrixScreen.tsx`

#### Chat with API Integration (Lines 33-76)

```typescript
const sendMessage = async (text: string) => {
  Haptics.lightTap();  // Feedback on send

  // Add user message immediately (optimistic UI)
  const userMessage: Message = {
    id: Date.now().toString(),
    text: text.trim(),
    isUser: true,
    timestamp: new Date(),
  };
  setMessages(prev => [...prev, userMessage]);
  setInputText('');
  setIsLoading(true);

  try {
    const response = await brixApi.chat(1, text.trim());
    const brixResponse: Message = {
      id: (Date.now() + 1).toString(),
      text: response.message,
      isUser: false,
      timestamp: new Date(response.timestamp),
    };
    Haptics.selection();  // Subtle feedback on response
    setMessages(prev => [...prev, brixResponse]);
  } catch (error) {
    // Graceful error handling with user-friendly message
    const errorResponse: Message = {
      text: "I'm having trouble connecting. Make sure the backend is running...",
      isUser: false,
      timestamp: new Date(),
    };
    setMessages(prev => [...prev, errorResponse]);
  }
};
```

**Talking Point:** "The chat uses optimistic UI—the user's message appears instantly while we wait for the AI response, making the app feel more responsive."

#### Quick Response Buttons (Lines 26-31, 171-198)

```typescript
const quickResponses = [
  { text: "Ready to crush it!", icon: Zap },
  { text: "Feeling tired", icon: Battery },
  { text: "Need motivation", icon: Smile },
  { text: "What workout today?", icon: Dumbbell },
];

// Rendered as horizontal scrolling chips
{quickResponses.map((response, index) => (
  <TouchableOpacity onPress={() => sendMessage(response.text)} disabled={isLoading}>
    <View style={chipStyles}>
      <response.icon size={14} color={colors.orange.DEFAULT} />
      <Text>{response.text}</Text>
    </View>
  </TouchableOpacity>
))}
```

**Talking Point:** "Quick response buttons reduce friction—users can tap common messages instead of typing, making the chat feel more natural."

---

## Quick Reference: Key Files by Feature

| Feature | Backend | Mobile |
|---------|---------|--------|
| AI Chat | `BrixService.java:57-117` | `BrixScreen.tsx:33-76` |
| Tone Selection | `BrixService.java:368-397` | - |
| Behavior Tracking | `BehaviorProfile.java:205-291` | - |
| Workout Sessions | `WorkoutSessionService.java:50-102` | `WorkoutSessionScreen.tsx:81-262` |
| Brick Creation | `BrickService.java:60-97` | - |
| API Client | - | `api.ts:27-61, 370-381` |
| Data Seeding | `DataInitializer.java:27-48` | - |

---

## Interview Talking Points

### AI & Personalization
1. **AI Fallback Pattern:** "BRIX uses graceful degradation—Ollama (Llama 3.2) first, keyword matching as backup. The app always responds, even if the AI is down."

2. **Adaptive Tone Selection:** "Four coaching tones (Empathetic, Challenging, Celebratory, Encouraging) are selected based on two layers—today's daily log (immediate state) and behavior profile (patterns over time). A stressed user always gets empathy; a user on a record streak gets challenged."

3. **Workout Recommendation Scoring:** "Workouts are scored with additive points—fitness level match (+20), low energy + gentle workout (+55), goal alignment (+15). The highest-scoring workout wins, and we generate a human-readable explanation for WHY it was recommended."

### Backend Architecture
4. **Rich Domain Model:** "Entities contain business logic, not just data. `BehaviorProfile.logWorkout()` cascades through streak, consistency, motivation, momentum, and tone calculations—the service just calls one method."

5. **Global Exception Handling:** "@RestControllerAdvice catches all exceptions centrally. Every error returns consistent JSON with status, message, and path. Validation errors include field-level details so the mobile app knows exactly what's wrong."

6. **JPA Callbacks:** "@PrePersist and @PreUpdate automatically manage timestamps. We never forget to set `updatedAt` because it happens at the persistence layer."

### Mobile UX
7. **Semantic Haptics:** "We created a haptics utility with semantic functions—`brickPlaced()` gives a heavy thud + light tap like a brick settling, `streakMilestone()` cascades three vibrations for celebration. Haptic patterns are reusable across the app."

8. **3D Brick Background:** "The brick wall is procedurally generated with `useMemo`. Each brick has 4 edge overlays (highlight top/left, shadow bottom/right) creating a 3D beveled effect. Deterministic pseudo-random shading ensures consistent rendering."

9. **Optimistic UI:** "In the chat, the user's message appears instantly while we wait for the AI response. This makes the app feel more responsive."

10. **Multi-Sensory Celebration:** "Workout completion triggers three channels—haptic feedback, confetti animation, and a success alert. Users feel rewarded through touch, sight, and interaction."

### Data & API
11. **One Brick Per Day Rule:** "Multiple workouts in a day still create only one brick—enforced at the service layer. This encourages consistency over volume."

12. **API Client Organization:** "The mobile API client uses Axios interceptors for logging and is organized into domain-specific modules (profileApi, workoutApi, brixApi). Each function is typed with TypeScript interfaces."

### Infrastructure & DevOps
13. **SQLite → PostgreSQL Migration:** "We started with SQLite for zero-config development—just a file, no setup. But SQLite doesn't support concurrent writes, which breaks with multiple users. So we added PostgreSQL for production using Spring profiles. The same codebase runs against SQLite locally or PostgreSQL in Docker—just change the active profile."

14. **Docker Compose Orchestration:** "Docker Compose runs PostgreSQL and the backend together. We use health checks so the backend waits for the database to be ready before starting. Named volumes persist data across container restarts."

---

## Quick Reference: File Locations

| Feature | Backend | Mobile |
|---------|---------|--------|
| AI Chat | `BrixService.java:57-117` | `BrixScreen.tsx:33-76` |
| Tone Selection | `BrixService.java:368-397` | - |
| Workout Scoring | `BrixService.java:297-347` | - |
| Behavior Tracking | `BehaviorProfile.java:205-291` | - |
| Workout Sessions | `WorkoutSessionService.java:50-102` | `WorkoutSessionScreen.tsx:81-262` |
| Brick Creation | `BrickService.java:60-97` | - |
| Exception Handling | `RestExceptionHandler.java:21-112` | - |
| Entity Relationships | `UserProfile.java:95-101` | - |
| Haptic Feedback | - | `haptics.ts:1-85` |
| 3D Brick Wall | - | `BrickBackground.tsx:21-136` |
| API Client | - | `api.ts:27-381` |
| Data Seeding | `DataInitializer.java:27-48` | - |
| DB Config (SQLite) | `application.properties` | - |
| DB Config (PostgreSQL) | `application-docker.properties` | - |
| Docker Setup | `docker-compose.yml`, `Dockerfile` | - |
