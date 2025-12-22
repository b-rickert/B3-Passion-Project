# B3 Data Model

## Overview
The B3 database is designed to support a single-user fitness tracking application with an adaptive AI coach (BRIX). The schema focuses on workout tracking, progress visualization (bricks), behavioral learning, and personalized coaching.

---

## Database Tables

### 1. UserProfile
**Purpose:** Stores the demo user's basic information and fitness preferences.

| Column | Type | Description |
|--------|------|-------------|
| profile_id | BIGINT (PK) | Unique identifier (always 1 for demo user) |
| display_name | VARCHAR | User's display name |
| age | INT | User's age |
| fitness_level | VARCHAR | Beginner/Intermediate/Advanced |
| primary_goal | VARCHAR | Strength/Cardio/Flexibility/Weight Loss |
| equipment | VARCHAR | Available equipment (comma-separated) |
| weekly_goal_days | INT | Target workout days per week |
| created_at | TIMESTAMP | Account creation timestamp |
| updated_at | TIMESTAMP | Last profile update |

**Notes:**
- Single user system (profile_id = 1)
- No authentication required
- Allows BRIX personalization without login complexity

---

### 2. BehaviorProfile
**Purpose:** Tracks user behavior patterns for BRIX adaptive coaching.

| Column | Type | Description |
|--------|------|-------------|
| behavior_id | BIGINT (PK) | Unique identifier |
| profile_id | BIGINT (FK) | References UserProfile |
| preferred_workout_time | VARCHAR | Morning/Afternoon/Evening |
| current_tone | VARCHAR | BRIX's current coaching tone |
| consecutive_days | INT | Current streak count |
| longest_streak | INT | Best streak achieved |
| total_bricks_laid | INT | Lifetime workout count |
| consistency_score | DOUBLE | 0.0-1.0 consistency rating |
| last_workout_date | DATE | Most recent workout |
| motivation_state | VARCHAR | Motivated/Neutral/Struggling |
| updated_at | TIMESTAMP | Last behavior update |

**Notes:**
- One-to-one with UserProfile
- Updated after each workout and daily check-in
- Powers BRIX's adaptive recommendations

---

### 3. Workout
**Purpose:** Library of available workout templates.

| Column | Type | Description |
|--------|------|-------------|
| workout_id | BIGINT (PK) | Unique identifier |
| workout_name | VARCHAR | Display name |
| description | TEXT | Workout description |
| category | VARCHAR | Strength/Cardio/Mobility/HIIT |
| difficulty_level | VARCHAR | Easy/Medium/Hard |
| estimated_duration | INT | Minutes |
| equipment_required | VARCHAR | Comma-separated equipment list |
| created_at | TIMESTAMP | Creation timestamp |

**Notes:**
- Pre-populated with workout templates
- Referenced by WorkoutSession when user completes a workout

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

**Notes:**
- Building blocks for workouts
- Can be reused across multiple workouts

---

### 5. WorkoutExercise (Junction Table)
**Purpose:** Maps exercises to workouts with specific parameters.

| Column | Type | Description |
|--------|------|-------------|
| workout_exercise_id | BIGINT (PK) | Unique identifier |
| workout_id | BIGINT (FK) | References Workout |
| exercise_id | BIGINT (FK) | References Exercise |
| order_index | INT | Exercise sequence in workout |
| sets | INT | Number of sets |
| reps | INT | Repetitions per set |
| duration_seconds | INT | Duration (for timed exercises) |

**Notes:**
- Many-to-many relationship between Workout and Exercise
- Allows same exercise in multiple workouts with different parameters

---

### 6. WorkoutSession
**Purpose:** Records when user completes a workout.

| Column | Type | Description |
|--------|------|-------------|
| session_id | BIGINT (PK) | Unique identifier |
| profile_id | BIGINT (FK) | References UserProfile |
| workout_id | BIGINT (FK) | References Workout |
| start_time | TIMESTAMP | When workout started |
| end_time | TIMESTAMP | When workout ended |
| completion_status | VARCHAR | COMPLETED/PARTIAL/SKIPPED |
| perceived_difficulty | INT | User rating 1-5 |
| created_at | TIMESTAMP | Record creation |

**Notes:**
- Actual workout history
- Generates a Brick when completion_status = COMPLETED
- Used to calculate streaks and consistency

---

### 7. Brick
**Purpose:** Visual progress tracking - one brick per completed workout.

| Column | Type | Description |
|--------|------|-------------|
| brick_id | BIGINT (PK) | Unique identifier |
| profile_id | BIGINT (FK) | References UserProfile |
| session_id | BIGINT (FK) | References WorkoutSession |
| brick_date | DATE | Date brick was earned |
| brick_type | VARCHAR | workout/consistency/milestone |
| created_at | TIMESTAMP | Creation timestamp |

**Notes:**
- Displayed in brick wall calendar UI
- One workout = one brick (brick_type = 'workout')
- Special bricks for milestones (brick_type = 'milestone')
- Core feature: "Build your foundation brick by brick"

---

### 8. DailyLog
**Purpose:** User's daily energy, stress, sleep, and mood check-ins.

| Column | Type | Description |
|--------|------|-------------|
| log_id | BIGINT (PK) | Unique identifier |
| profile_id | BIGINT (FK) | References UserProfile |
| log_date | DATE | Date of check-in |
| energy_level | INT | 1-5 scale |
| stress_level | INT | 1-5 scale |
| sleep_quality | INT | 1-5 scale |
| mood | VARCHAR | Great/Good/Okay/Low/Stressed |
| notes | TEXT | Optional user notes |
| created_at | TIMESTAMP | Creation timestamp |

**Notes:**
- Powers BRIX's adaptive recommendations
- One entry per day maximum
- Helps BRIX understand context (e.g., low energy → easier workout)

---

### 9. BrixMessage
**Purpose:** Stores BRIX's coaching messages to the user.

| Column | Type | Description |
|--------|------|-------------|
| message_id | BIGINT (PK) | Unique identifier |
| profile_id | BIGINT (FK) | References UserProfile |
| message_text | TEXT | The actual message |
| message_type | VARCHAR | motivation/check-in/celebration/tip |
| tone | VARCHAR | encouraging/neutral/challenging |
| context_trigger | VARCHAR | What triggered this message |
| sent_at | TIMESTAMP | When message was sent |

**Notes:**
- Chat history with BRIX
- Context-aware messaging (tied to behavior patterns)
- Displayed in BRIX Chat screen

---

### 10. Milestone
**Purpose:** Tracks user achievements and goals.

| Column | Type | Description |
|--------|------|-------------|
| milestone_id | BIGINT (PK) | Unique identifier |
| profile_id | BIGINT (FK) | References UserProfile |
| milestone_name | VARCHAR | Display name |
| milestone_type | VARCHAR | streak/total_workouts/consistency |
| target_value | INT | Goal threshold |
| current_value | INT | Current progress |
| is_achieved | BOOLEAN | Achieved status |
| achieved_at | TIMESTAMP | When milestone was achieved |
| created_at | TIMESTAMP | Creation timestamp |

**Notes:**
- Examples: "7-Day Streak", "50 Bricks Laid", "80% Monthly Consistency"
- Unlocks special bricks and BRIX celebrations
- Displayed in Progress screen

---

## Key Relationships

### One-to-One
- **UserProfile ↔ BehaviorProfile**: Each user has one behavior profile

### One-to-Many
- **UserProfile → WorkoutSession**: User completes many workouts
- **UserProfile → Brick**: User earns many bricks
- **UserProfile → DailyLog**: User logs daily check-ins
- **UserProfile → BrixMessage**: User receives many messages
- **UserProfile → Milestone**: User has many milestones
- **Workout → WorkoutSession**: Workout template used in many sessions
- **WorkoutSession → Brick**: Each completed session generates one brick

### Many-to-Many
- **Workout ↔ Exercise** (through WorkoutExercise): Workouts contain multiple exercises, exercises appear in multiple workouts

---

## Design Decisions

### Why No Authentication?
- Bootcamp requirement: Single-user demo application
- Simplifies MVP while still allowing full feature demonstration
- Profile ID hardcoded to 1 throughout application

### Why Separate Workout and WorkoutSession?
- **Workout** = Template/recipe (unchanging library)
- **WorkoutSession** = Actual instance when user did it (history)
- Allows tracking: "User did Full Body Beginner 15 times this month"

### Why Brick Table?
- Core branding feature: "Build your foundation brick by brick"
- Visual progress representation in UI
- One record per completed workout = one brick displayed
- Enables "repair gaps" feature (missed days shown as empty bricks)

### Why BehaviorProfile Separate from UserProfile?
- UserProfile = Static preferences (equipment, goals)
- BehaviorProfile = Dynamic patterns (streaks, consistency, motivation state)
- Frequently updated behavioral data without touching user profile
- Enables BRIX adaptive intelligence

---

## Sample Data Flow

**User completes a workout:**
1. WorkoutSession record created (start_time, end_time, workout_id)
2. Brick record created (brick_date = today, session_id)
3. BehaviorProfile updated (consecutive_days++, total_bricks_laid++, consistency_score recalculated)
4. Check for Milestone achievements (e.g., 7-day streak)
5. BRIX generates celebratory BrixMessage

**User does daily check-in:**
1. DailyLog record created (energy=2, stress=4, mood="Low")
2. BehaviorProfile.motivation_state updated to "Struggling"
3. BRIX recommends easier workout for today
4. BrixMessage sent with encouraging tone

---

## Technology Notes

- **Database:** SQLite (file-based, perfect for single-user demo)
- **ORM:** Hibernate/JPA (Spring Data JPA)
- **Constraints:** All foreign keys enforced, CASCADE on delete where appropriate
- **Indexes:** Primary keys auto-indexed, consider indexing:
  - `brick_date` in Brick (for calendar queries)
  - `log_date` in DailyLog (for recent check-ins)
  - `sent_at` in BrixMessage (for chat history)

---

## Future Enhancements (Post-MVP)

- **Social features:** Add User table with authentication
- **Custom workouts:** Allow users to create their own workout templates
- **Progress photos:** Add ProgressPhoto table linked to Brick/Milestone
- **Body metrics:** Track weight, measurements over time
- **Workout plans:** Multi-week programs with scheduled workouts
