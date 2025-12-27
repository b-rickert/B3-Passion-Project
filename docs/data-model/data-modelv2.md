# B3 Data Model (Entity Relationship Diagram)

## Overview
The B3 database is designed to support a single-user fitness tracking application with an adaptive AI coach (BRIX). The schema uses proper JPA entity relationships (not just foreign key IDs) to enable object-oriented navigation and ensure data integrity.

---

## Entity Relationships Summary

```
UserProfile (1) ←──────────→ (1) BehaviorProfile
     │
     ├──────────→ (many) WorkoutSession
     ├──────────→ (many) Brick
     ├──────────→ (many) DailyLog
     ├──────────→ (many) BrixMessage
     └──────────→ (many) Milestone

Workout (1) ←──────────→ (many) WorkoutExercise ←──────────→ (many) Exercise
   │
   └──────────→ (many) WorkoutSession

WorkoutSession (1) ──────────→ (1) Brick
```

---

## Database Tables & Relationships

### 1. UserProfile
**Purpose:** Stores the demo user's basic information and fitness preferences.

| Column | Type | Description |
|--------|------|-------------|
| profile_id | BIGINT (PK) | Unique identifier (always 1 for demo user) |
| display_name | VARCHAR | User's display name |
| age | INT | User's age |
| fitness_level | ENUM | BEGINNER/INTERMEDIATE/ADVANCED |
| primary_goal | ENUM | STRENGTH/CARDIO/FLEXIBILITY/WEIGHT_LOSS |
| equipment | VARCHAR | Available equipment (comma-separated) |
| weekly_goal_days | INT | Target workout days per week |
| created_at | TIMESTAMP | Account creation timestamp |
| updated_at | TIMESTAMP | Last profile update |

**Relationships:**
- **@OneToOne** with BehaviorProfile (bidirectional, cascade delete)
- **@OneToMany** with WorkoutSession (user's workout history)
- **@OneToMany** with Brick (user's visual progress)
- **@OneToMany** with DailyLog (user's daily check-ins)
- **@OneToMany** with BrixMessage (BRIX coaching messages)
- **@OneToMany** with Milestone (user's achievements)

**Notes:**
- Single user system (profile_id = 1)
- Uses type-safe enums instead of VARCHAR for fitness_level and primary_goal
- Bidirectional relationship with BehaviorProfile (can navigate both ways)

---

### 2. BehaviorProfile
**Purpose:** Tracks user behavior patterns for BRIX adaptive coaching.

| Column | Type | Description |
|--------|------|-------------|
| behavior_id | BIGINT (PK) | Unique identifier |
| profile_id | BIGINT (FK) | Foreign key to UserProfile |
| preferred_workout_time | VARCHAR | Morning/Afternoon/Evening |
| current_tone | ENUM | ENCOURAGING/CHALLENGING/EMPATHETIC/CELEBRATORY |
| consecutive_days | INT | Current streak count |
| longest_streak | INT | Best streak achieved |
| total_bricks_laid | INT | Lifetime workout count |
| consistency_score | DOUBLE | 0.0-1.0 consistency rating |
| last_workout_date | DATE | Most recent workout |
| motivation_state | ENUM | MOTIVATED/NEUTRAL/STRUGGLING |
| updated_at | TIMESTAMP | Last behavior update |

**Relationships:**
- **@OneToOne** with UserProfile (owns the foreign key)
  - `@JoinColumn(name = "profile_id")` creates the FK
  - Lazy loading: doesn't load UserProfile unless accessed

**JPA Mapping:**
```java
@OneToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "profile_id", nullable = false, unique = true)
private UserProfile userProfile;
```

**Notes:**
- Uses enums for current_tone and motivation_state (type-safe)
- One-to-one relationship ensures each user has exactly one behavior profile
- Can navigate: `behaviorProfile.getUserProfile().getDisplayName()`

---

### 3. Workout
**Purpose:** Library of available workout templates.

| Column | Type | Description |
|--------|------|-------------|
| workout_id | BIGINT (PK) | Unique identifier |
| workout_name | VARCHAR | Display name |
| description | TEXT | Workout description |
| category | ENUM | STRENGTH/CARDIO/MOBILITY/HIIT |
| difficulty_level | ENUM | EASY/MEDIUM/HARD |
| estimated_duration | INT | Minutes |
| equipment_required | VARCHAR | Comma-separated equipment list |
| created_at | TIMESTAMP | Creation timestamp |

**Relationships:**
- **@OneToMany** with WorkoutExercise (workout contains exercises)
- **@OneToMany** with WorkoutSession (workout can be performed many times)

**JPA Mapping:**
```java
@OneToMany(mappedBy = "workout", cascade = CascadeType.ALL)
private List<WorkoutExercise> workoutExercises;

@OneToMany(mappedBy = "workout")
private List<WorkoutSession> sessions;
```

**Notes:**
- Pre-populated with workout templates
- Uses enums for category and difficulty_level
- Can navigate: `workout.getWorkoutExercises()` to get exercise list

---

### 4. Exercise
**Purpose:** Library of individual exercises.

| Column | Type | Description |
|--------|------|-------------|
| exercise_id | BIGINT (PK) | Unique identifier |
| exercise_name | VARCHAR | Exercise name |
| description | TEXT | How to perform |
| muscle_group | VARCHAR | Primary muscle group |
| video_url | VARCHAR | Demo video link |
| created_at | TIMESTAMP | Creation timestamp |

**Relationships:**
- **@OneToMany** with WorkoutExercise (exercise appears in many workouts)

**JPA Mapping:**
```java
@OneToMany(mappedBy = "exercise")
private List<WorkoutExercise> workoutExercises;
```

**Notes:**
- Reusable across multiple workouts
- Can navigate: `exercise.getWorkoutExercises()` to see which workouts use it

---

### 5. WorkoutExercise (Junction Table)
**Purpose:** Maps exercises to workouts with specific parameters (Many-to-Many).

| Column | Type | Description |
|--------|------|-------------|
| workout_exercise_id | BIGINT (PK) | Unique identifier |
| workout_id | BIGINT (FK) | Foreign key to Workout |
| exercise_id | BIGINT (FK) | Foreign key to Exercise |
| order_index | INT | Exercise sequence in workout |
| sets | INT | Number of sets |
| reps | INT | Repetitions per set |
| duration_seconds | INT | Duration (for timed exercises) |

**Relationships:**
- **@ManyToOne** with Workout (many WorkoutExercises belong to one Workout)
- **@ManyToOne** with Exercise (many WorkoutExercises reference one Exercise)

**JPA Mapping:**
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "workout_id", nullable = false)
private Workout workout;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "exercise_id", nullable = false)
private Exercise exercise;
```

**Notes:**
- Junction table for Many-to-Many relationship (Workout ↔ Exercise)
- Stores workout-specific parameters (sets, reps, order)
- Can navigate both directions: `workoutExercise.getWorkout()` or `workoutExercise.getExercise()`

---

### 6. WorkoutSession
**Purpose:** Records when user completes a workout (actual history).

| Column | Type | Description |
|--------|------|-------------|
| session_id | BIGINT (PK) | Unique identifier |
| profile_id | BIGINT (FK) | Foreign key to UserProfile |
| workout_id | BIGINT (FK) | Foreign key to Workout |
| start_time | TIMESTAMP | When workout started |
| end_time | TIMESTAMP | When workout ended |
| completion_status | ENUM | COMPLETED/PARTIAL/SKIPPED |
| perceived_difficulty | INT | User rating 1-5 |
| created_at | TIMESTAMP | Record creation |

**Relationships:**
- **@ManyToOne** with UserProfile (many sessions belong to one user)
- **@ManyToOne** with Workout (many sessions reference one workout template)
- **@OneToOne** with Brick (one session generates one brick on completion)

**JPA Mapping:**
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "profile_id", nullable = false)
private UserProfile userProfile;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "workout_id", nullable = false)
private Workout workout;

@OneToOne(mappedBy = "workoutSession", cascade = CascadeType.ALL)
private Brick brick;
```

**Notes:**
- Uses enum for completion_status
- Generates a Brick when completion_status = COMPLETED
- Can navigate: `session.getUserProfile()`, `session.getWorkout()`, `session.getBrick()`

---

### 7. Brick
**Purpose:** Visual progress tracking - one brick per completed workout.

| Column | Type | Description |
|--------|------|-------------|
| brick_id | BIGINT (PK) | Unique identifier |
| profile_id | BIGINT (FK) | Foreign key to UserProfile |
| session_id | BIGINT (FK) | Foreign key to WorkoutSession |
| brick_date | DATE | Date brick was earned |
| brick_type | ENUM | WORKOUT/MILESTONE |
| created_at | TIMESTAMP | Creation timestamp |

**Relationships:**
- **@ManyToOne** with UserProfile (many bricks belong to one user)
- **@OneToOne** with WorkoutSession (one brick per completed session)

**JPA Mapping:**
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "profile_id", nullable = false)
private UserProfile userProfile;

@OneToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "session_id", nullable = false)
private WorkoutSession workoutSession;
```

**Notes:**
- Uses enum for brick_type (WORKOUT or MILESTONE)
- Displayed in brick wall calendar UI
- Can navigate: `brick.getWorkoutSession().getWorkout().getWorkoutName()`

---

### 8. DailyLog
**Purpose:** User's daily energy, stress, sleep, and mood check-ins.

| Column | Type | Description |
|--------|------|-------------|
| log_id | BIGINT (PK) | Unique identifier |
| profile_id | BIGINT (FK) | Foreign key to UserProfile |
| log_date | DATE | Date of check-in |
| energy_level | INT | 1-5 scale |
| stress_level | INT | 1-5 scale |
| sleep_quality | INT | 1-5 scale |
| mood | ENUM | GREAT/GOOD/OKAY/LOW/STRESSED |
| notes | TEXT | Optional user notes |
| created_at | TIMESTAMP | Creation timestamp |

**Relationships:**
- **@ManyToOne** with UserProfile (many logs belong to one user)

**JPA Mapping:**
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "profile_id", nullable = false)
private UserProfile userProfile;
```

**Notes:**
- Uses enum for mood
- One entry per day maximum (enforced by unique constraint on profile_id + log_date)
- Powers BRIX's adaptive recommendations

---

### 9. BrixMessage
**Purpose:** Stores BRIX's coaching messages to the user.

| Column | Type | Description |
|--------|------|-------------|
| message_id | BIGINT (PK) | Unique identifier |
| profile_id | BIGINT (FK) | Foreign key to UserProfile |
| message_text | TEXT | The actual message |
| message_type | ENUM | MOTIVATION/CHECK_IN/CELEBRATION/TIP |
| tone | ENUM | ENCOURAGING/CHALLENGING/EMPATHETIC/CELEBRATORY |
| context_trigger | VARCHAR | What triggered this message |
| sent_at | TIMESTAMP | When message was sent |

**Relationships:**
- **@ManyToOne** with UserProfile (many messages belong to one user)

**JPA Mapping:**
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "profile_id", nullable = false)
private UserProfile userProfile;
```

**Notes:**
- Uses enums for message_type and tone
- Chat history with BRIX
- Displayed in BRIX Chat screen

---

### 10. Milestone
**Purpose:** Tracks user achievements and goals.

| Column | Type | Description |
|--------|------|-------------|
| milestone_id | BIGINT (PK) | Unique identifier |
| profile_id | BIGINT (FK) | Foreign key to UserProfile |
| milestone_name | VARCHAR | Display name |
| milestone_type | ENUM | STREAK/TOTAL_WORKOUTS/CONSISTENCY |
| target_value | INT | Goal threshold |
| current_value | INT | Current progress |
| is_achieved | BOOLEAN | Achieved status |
| achieved_at | TIMESTAMP | When milestone was achieved |
| created_at | TIMESTAMP | Creation timestamp |

**Relationships:**
- **@ManyToOne** with UserProfile (many milestones belong to one user)

**JPA Mapping:**
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "profile_id", nullable = false)
private UserProfile userProfile;
```

**Notes:**
- Uses enum for milestone_type
- Examples: "7-Day Streak", "50 Bricks Laid", "80% Monthly Consistency"
- Displayed in Progress screen

---

## JPA Relationship Types Explained

### @OneToOne
**Definition:** One entity relates to exactly one other entity.

**Example:** UserProfile ↔ BehaviorProfile
- One UserProfile has exactly one BehaviorProfile
- One BehaviorProfile belongs to exactly one UserProfile

**Code:**
```java
// In UserProfile:
@OneToOne(mappedBy = "userProfile", cascade = CascadeType.ALL)
private BehaviorProfile behaviorProfile;

// In BehaviorProfile:
@OneToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "profile_id")
private UserProfile userProfile;
```

---

### @OneToMany / @ManyToOne
**Definition:** One entity has many related entities (inverse of @ManyToOne).

**Example:** UserProfile → WorkoutSession
- One UserProfile has many WorkoutSessions
- Many WorkoutSessions belong to one UserProfile

**Code:**
```java
// In UserProfile:
@OneToMany(mappedBy = "userProfile")
private List<WorkoutSession> workoutSessions;

// In WorkoutSession:
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "profile_id")
private UserProfile userProfile;
```

---

### @ManyToMany (via Junction Table)
**Definition:** Many entities relate to many other entities.

**Example:** Workout ↔ Exercise (via WorkoutExercise)
- One Workout contains many Exercises
- One Exercise appears in many Workouts
- WorkoutExercise is the junction table

**Code:**
```java
// In Workout:
@OneToMany(mappedBy = "workout")
private List<WorkoutExercise> workoutExercises;

// In WorkoutExercise:
@ManyToOne
@JoinColumn(name = "workout_id")
private Workout workout;

@ManyToOne
@JoinColumn(name = "exercise_id")
private Exercise exercise;
```

---

## Key Design Decisions

### Why Use JPA Relationships Instead of Just IDs?

**❌ Old Way (Just IDs):**
```java
@Column(name = "profile_id")
private Long profileId;  // Just a number
```

**✅ New Way (JPA Relationships):**
```java
@ManyToOne
@JoinColumn(name = "profile_id")
private UserProfile userProfile;  // Actual object reference
```

**Benefits:**
1. **Object Navigation:** `session.getUserProfile().getDisplayName()` instead of manual joins
2. **Type Safety:** Can't accidentally put wrong ID
3. **Cascade Operations:** Delete user → auto-delete related data
4. **Lazy Loading:** Don't load related data until needed (performance)
5. **IDE Autocomplete:** See available methods on related objects
6. **Cleaner Code:** Hibernate handles SQL joins automatically

---

### Why Separate Workout and WorkoutSession?
- **Workout** = Template/recipe (unchanging library)
- **WorkoutSession** = Actual instance when user performed it (history)
- Allows tracking: "User did Full Body Beginner 15 times this month"

---

### Why Brick Table?
- Core branding feature: "Build your foundation brick by brick"
- Visual progress representation in UI
- One record per completed workout = one brick displayed

---

### Why BehaviorProfile Separate from UserProfile?
- **UserProfile** = Static preferences (equipment, goals)
- **BehaviorProfile** = Dynamic patterns (streaks, consistency, motivation state)
- Frequently updated behavioral data without touching user profile
- Enables BRIX adaptive intelligence

---

## Sample Data Flow

### User Completes a Workout:
1. **WorkoutSession** created with reference to UserProfile and Workout
   ```java
   WorkoutSession session = new WorkoutSession(userProfile, workout);
   ```
2. **Brick** created with reference to WorkoutSession
   ```java
   Brick brick = new Brick(userProfile, session, LocalDate.now());
   ```
3. **BehaviorProfile** updated (navigate from UserProfile)
   ```java
   userProfile.getBehaviorProfile().incrementStreak();
   userProfile.getBehaviorProfile().incrementBricks();
   ```
4. Check for **Milestone** achievements
5. BRIX generates **BrixMessage**

### User Does Daily Check-In:
1. **DailyLog** created with reference to UserProfile
   ```java
   DailyLog log = new DailyLog(userProfile, energy, stress, sleep, mood);
   ```
2. **BehaviorProfile** motivation_state updated based on energy
3. BRIX recommends easier workout if energy < 3
4. **BrixMessage** sent with empathetic tone

---

## Technology Notes

- **Database:** SQLite (file-based, perfect for single-user demo)
- **ORM:** Hibernate/JPA (Spring Data JPA)
- **Constraints:** All foreign keys enforced via @JoinColumn
- **Cascading:** @OneToOne and @OneToMany use cascade options
- **Lazy Loading:** Most relationships use FetchType.LAZY for performance
- **Enums:** Stored as STRING in database (not ordinal numbers)

---

## Entity Relationship Diagram (Text Format)

```
┌─────────────────┐
│   UserProfile   │──────────────────────────────────┐
│ (profile_id PK) │                                  │
└────────┬────────┘                                  │
         │ 1:1                                       │ 1:N
         ▼                                           ▼
┌─────────────────┐                         ┌─────────────────┐
│BehaviorProfile  │                         │ WorkoutSession  │
│(behavior_id PK) │                         │ (session_id PK) │
│profile_id FK    │                         │ profile_id FK   │
└─────────────────┘                         │ workout_id FK   │
                                            └────────┬────────┘
                                                     │ 1:1
                                                     ▼
                                            ┌─────────────────┐
                                            │     Brick       │
                                            │ (brick_id PK)   │
                                            │ profile_id FK   │
                                            │ session_id FK   │
                                            └─────────────────┘

┌─────────────────┐         ┌──────────────────┐         ┌─────────────────┐
│    Workout      │ 1:N     │ WorkoutExercise  │ N:1     │    Exercise     │
│ (workout_id PK) │────────▶│(workout_ex_id PK)│◀───────│(exercise_id PK) │
└─────────────────┘         │ workout_id FK    │         └─────────────────┘
                            │ exercise_id FK   │
                            └──────────────────┘

UserProfile 1:N relationships also include:
- DailyLog (profile_id FK)
- BrixMessage (profile_id FK)
- Milestone (profile_id FK)
```

---

## Future Enhancements (Post-MVP)

- **Social features:** Add User table with authentication, friend relationships
- **Custom workouts:** User-created workout templates
- **Progress photos:** ProgressPhoto entity linked to Milestone
- **Body metrics:** BodyMetric entity for weight/measurements tracking
- **Workout plans:** WorkoutPlan entity with multi-week programs

---

**End of Data Model Documentation**