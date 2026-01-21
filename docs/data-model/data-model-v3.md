# B3 Data Model v3

**Updated:** January 2025

## Overview

The B3 database supports a single-user fitness tracking application with an adaptive AI coach (BRIX). The schema uses proper JPA entity relationships for object-oriented navigation and data integrity. All entities are implemented in `backend/src/main/java/com/b3/model/`.

---

## Entity Relationships Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│                          UserProfile                                 │
│                        (profile_id PK)                               │
├─────────────────────────────────────────────────────────────────────┤
│  1:1   │  1:N              │  1:N         │  1:N        │  1:N      │
│   ↓    │   ↓               │   ↓          │   ↓         │   ↓       │
│ Behavior│ WorkoutSession   │ DailyLog    │ BrixMessage │ Milestone │
│ Profile │    ↓ 1:1         │             │             │           │
│         │  Brick           │             │             │           │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│  Workout (1) ←→ (N) WorkoutExercise (N) ←→ (1) Exercise             │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Database Tables

### 1. UserProfile (user_profile)

Stores user information and fitness preferences.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| profile_id | BIGINT | PK, AUTO | Unique identifier |
| display_name | VARCHAR(100) | NOT NULL | User's display name |
| age | INT | | User's age |
| fitness_level | VARCHAR(20) | | BEGINNER/INTERMEDIATE/ADVANCED |
| primary_goal | VARCHAR(30) | | STRENGTH/CARDIO/FLEXIBILITY/WEIGHT_LOSS |
| equipment | VARCHAR(255) | | Available equipment (comma-separated) |
| weekly_goal_days | INT | DEFAULT 3 | Target workout days per week |
| created_at | TIMESTAMP | NOT NULL | Account creation |
| updated_at | TIMESTAMP | | Last update |

**Relationships:**
- `@OneToOne` → BehaviorProfile (cascade ALL)
- `@OneToMany` → WorkoutSession, Brick, DailyLog, BrixMessage, Milestone

---

### 2. BehaviorProfile (behavior_profile)

Tracks user behavior patterns for BRIX adaptive coaching.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| behavior_id | BIGINT | PK, AUTO | Unique identifier |
| profile_id | BIGINT | FK, UNIQUE | References user_profile |
| preferred_workout_time | VARCHAR(20) | | MORNING/AFTERNOON/EVENING |
| current_tone | VARCHAR(20) | | ENCOURAGING/CHALLENGING/EMPATHETIC/CELEBRATORY |
| consecutive_days | INT | DEFAULT 0 | Current streak |
| longest_streak | INT | DEFAULT 0 | Best streak achieved |
| total_bricks_laid | INT | DEFAULT 0 | Lifetime workout count |
| consistency_score | DOUBLE | DEFAULT 0.0 | 0.0-1.0 rating |
| last_workout_date | DATE | | Most recent workout |
| motivation_state | VARCHAR(20) | | MOTIVATED/NEUTRAL/STRUGGLING |
| momentum_trend | VARCHAR(20) | | RISING/STABLE/FALLING |
| energy_score | INT | | Average energy level |
| fatigue_score | INT | | Accumulated fatigue |
| updated_at | TIMESTAMP | | Last behavior update |

**Key Methods:**
```java
logWorkout() // Updates streaks, consistency, motivation, momentum
adjustTone() // Selects coaching tone based on current state
```

---

### 3. Workout (workout)

Library of pre-built workout templates.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| workout_id | BIGINT | PK, AUTO | Unique identifier |
| workout_name | VARCHAR(100) | NOT NULL | Display name |
| description | TEXT | | Workout description |
| workout_type | VARCHAR(30) | | STRENGTH/CARDIO/MOBILITY/HIIT/FULL_BODY |
| difficulty_level | VARCHAR(20) | | EASY/MEDIUM/HARD |
| estimated_duration | INT | | Minutes |
| equipment_required | VARCHAR(255) | | Comma-separated list |
| created_at | TIMESTAMP | | Creation time |

**Relationships:**
- `@OneToMany` → WorkoutExercise (cascade ALL)
- `@OneToMany` → WorkoutSession

---

### 4. Exercise (exercise)

Library of individual exercises.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| exercise_id | BIGINT | PK, AUTO | Unique identifier |
| exercise_name | VARCHAR(100) | NOT NULL | Exercise name |
| description | TEXT | | How to perform |
| muscle_group | VARCHAR(50) | | Primary muscle group |
| video_url | VARCHAR(500) | | Demo image/video URL |
| created_at | TIMESTAMP | | Creation time |

**Note:** Video URLs currently use Unsplash images for demo purposes.

---

### 5. WorkoutExercise (workout_exercise)

Junction table linking workouts to exercises with parameters.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| workout_exercise_id | BIGINT | PK, AUTO | Unique identifier |
| workout_id | BIGINT | FK, NOT NULL | References workout |
| exercise_id | BIGINT | FK, NOT NULL | References exercise |
| order_index | INT | DEFAULT 0 | Sequence in workout |
| sets | INT | | Number of sets |
| reps | INT | | Reps per set |
| duration_seconds | INT | | Duration for timed exercises |

---

### 6. WorkoutSession (workout_session)

Records actual workout instances.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| session_id | BIGINT | PK, AUTO | Unique identifier |
| profile_id | BIGINT | FK, NOT NULL | References user_profile |
| workout_id | BIGINT | FK, NOT NULL | References workout |
| start_time | TIMESTAMP | | When workout started |
| end_time | TIMESTAMP | | When workout ended |
| completion_status | VARCHAR(20) | | IN_PROGRESS/COMPLETED/PARTIAL/SKIPPED |
| perceived_difficulty | INT | | User rating 1-5 |
| notes | TEXT | | Optional notes |
| created_at | TIMESTAMP | | Record creation |

**Relationships:**
- `@ManyToOne` → UserProfile, Workout
- `@OneToOne` → Brick (cascade ALL)

---

### 7. Brick (brick)

Visual progress tracking - one brick per completed workout.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| brick_id | BIGINT | PK, AUTO | Unique identifier |
| profile_id | BIGINT | FK, NOT NULL | References user_profile |
| session_id | BIGINT | FK, UNIQUE | References workout_session |
| brick_date | DATE | NOT NULL | Date earned |
| brick_type | VARCHAR(20) | | WORKOUT/STREAK_BONUS/MILESTONE |
| created_at | TIMESTAMP | | Creation time |

**Types:**
- `WORKOUT`: Standard completion brick
- `STREAK_BONUS`: Bonus for streak milestones
- `MILESTONE`: Achievement unlocked

---

### 8. DailyLog (daily_log)

User's daily check-ins for context-aware recommendations.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| log_id | BIGINT | PK, AUTO | Unique identifier |
| profile_id | BIGINT | FK, NOT NULL | References user_profile |
| log_date | DATE | NOT NULL | Date of check-in |
| energy_level | INT | | 1-5 scale |
| stress_level | INT | | 1-5 scale |
| sleep_quality | INT | | 1-5 scale |
| mood | VARCHAR(20) | | GREAT/GOOD/OKAY/LOW/STRESSED |
| notes | TEXT | | Optional notes |
| created_at | TIMESTAMP | | Creation time |

**Constraint:** UNIQUE(profile_id, log_date) - one log per day

---

### 9. BrixMessage (brix_message)

Chat history with BRIX AI coach.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| message_id | BIGINT | PK, AUTO | Unique identifier |
| profile_id | BIGINT | FK, NOT NULL | References user_profile |
| message_text | TEXT | NOT NULL | Message content |
| is_from_user | BOOLEAN | | true=user, false=BRIX |
| message_type | VARCHAR(30) | | MOTIVATION/CHECK_IN/CELEBRATION/TIP/RECOMMENDATION |
| tone | VARCHAR(20) | | ENCOURAGING/CHALLENGING/EMPATHETIC/CELEBRATORY |
| context_trigger | VARCHAR(100) | | What triggered the message |
| sent_at | TIMESTAMP | NOT NULL | When sent |

---

### 10. Milestone (milestone)

Achievement tracking.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| milestone_id | BIGINT | PK, AUTO | Unique identifier |
| profile_id | BIGINT | FK, NOT NULL | References user_profile |
| milestone_name | VARCHAR(100) | NOT NULL | Display name |
| milestone_type | VARCHAR(30) | | STREAK/TOTAL_WORKOUTS/CONSISTENCY |
| target_value | INT | | Goal threshold |
| current_value | INT | DEFAULT 0 | Current progress |
| is_achieved | BOOLEAN | DEFAULT false | Achievement status |
| achieved_at | TIMESTAMP | | When achieved |
| created_at | TIMESTAMP | | Creation time |

**Default Milestones:**
- 7-Day Streak
- 30-Day Streak
- 50 Bricks Laid
- 100 Bricks Laid
- 80% Monthly Consistency

---

## Backend Implementation

### Package Structure
```
backend/src/main/java/com/b3/
├── model/
│   ├── UserProfile.java
│   ├── BehaviorProfile.java
│   ├── Workout.java
│   ├── Exercise.java
│   ├── WorkoutExercise.java
│   ├── WorkoutSession.java
│   ├── Brick.java
│   ├── DailyLog.java
│   ├── BrixMessage.java
│   └── Milestone.java
├── repository/
│   └── *Repository.java (Spring Data JPA interfaces)
├── service/
│   ├── BrixService.java
│   ├── OllamaService.java
│   ├── BrickService.java
│   ├── WorkoutSessionService.java
│   └── *Service.java
├── controller/
│   └── *Controller.java
├── dto/
│   ├── request/
│   └── response/
├── mapper/
│   └── *Mapper.java
└── config/
    └── DataInitializer.java (seeds demo data)
```

### Data Seeding

`DataInitializer.java` runs on startup and creates:
- Default UserProfile (id=1) with BehaviorProfile
- Sample workouts with exercises
- Default milestones

---

## Database File

- **Location:** `backend/b3.db`
- **Type:** SQLite (file-based)
- **Created:** Automatically on first run
- **Reset:** Delete file to regenerate

---

## Query Examples

### Get brick calendar for month
```java
List<Brick> findByUserProfileProfileIdAndBrickDateBetween(
    Long profileId, LocalDate start, LocalDate end);
```

### Get today's log
```java
Optional<DailyLog> findByUserProfileProfileIdAndLogDate(
    Long profileId, LocalDate date);
```

### Get active workout session
```java
Optional<WorkoutSession> findByUserProfileProfileIdAndCompletionStatus(
    Long profileId, CompletionStatus status);
```

---

## Future Enhancements

- **Multi-user:** Add authentication, separate user tables
- **Custom workouts:** User-created workout templates
- **Progress photos:** Link photos to milestones
- **Body metrics:** Weight/measurement tracking
- **Workout plans:** Multi-week programs

---

**End of Data Model v3**
