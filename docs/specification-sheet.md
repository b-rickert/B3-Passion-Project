# B3 (Brick by Brick) - Specification Sheet

**Version:** 1.0  
**Project Type:** Mobile Fitness Application with Adaptive AI Coach  
**Target Platform:** iOS & Android (React Native)  
**Backend:** Java Spring Boot + SQLite  
**Timeline:** 6 weeks (MVP)

---

## Product Name

**B3** (Brick by Brick)

---

## Product Description

A mobile fitness application that helps users build consistent workout habits through **visual progress tracking** and **adaptive AI coaching**.

B3 reframes fitness as "building a foundation brick by brick" - each completed workout becomes a visual brick in a masonry-style calendar. An AI coach named BRIX learns user patterns, adapts its tone based on consistency, and recommends workouts based on daily energy, stress, and sleep data.

The application prioritizes **habit formation over metrics**, **visual motivation over guilt**, and **adaptive support over one-size-fits-all plans**.

---

## Core Goals (MVP)

- Build consistent workout habits through visual reinforcement
- Provide adaptive workout recommendations based on daily state
- Track progress without punishment (gaps to repair, not failures)
- Support users at all fitness levels (beginner to intermediate)
- Create a motivational, non-judgmental coaching experience
- Demonstrate full-stack development skills (backend + mobile + AI)

---

## Core Concepts

### Brick

A visual representation of a completed workout.

- One workout = one brick
- Displayed in brick wall calendar (masonry pattern)
- Color-coded:
  - **Orange** = Completed workout
  - **Blue** = Today (current focus)
  - **Gray (dashed)** = Missed day (gap to repair)
- Core metaphor: "Build your foundation brick by brick"

Bricks serve as:
- Tangible progress visualization
- Motivational reinforcement
- Non-judgmental tracking (gaps are visible but not punishing)

---

### BRIX (AI Coach)

An adaptive coaching system that learns user patterns and adjusts recommendations.

BRIX is:
- Context-aware (considers energy, stress, workout history)
- Tone-adaptive (encouraging, challenging, or empathetic)
- Non-generic (recommendations based on individual patterns)

BRIX provides:
- Daily workout recommendations
- Motivational messages
- Milestone celebrations
- Check-in responses

BRIX does NOT:
- Shame users for missed workouts
- Provide one-size-fits-all plans
- Ignore user's current state

---

### Behavior Profile

A backend data structure tracking user patterns for BRIX intelligence.

Tracks:
- Current streak (consecutive days)
- Longest streak achieved
- Total bricks laid (lifetime workouts)
- Consistency score (percentage of days with workouts)
- Last workout date
- Motivation state (Motivated/Neutral/Struggling)
- Preferred workout time
- Current coaching tone

Updated:
- After each workout completion
- After daily check-in
- When calculating streaks

Used for:
- BRIX recommendation algorithm
- Tone adaptation
- Milestone detection

---

### Workout Session

A single instance of completing a workout.

Workflow:
1. User selects workout from library
2. Session starts (records start time)
3. User completes exercises
4. Session ends (records end time, perceived difficulty)
5. System generates brick
6. System updates behavior profile
7. System checks for milestone achievements

Sessions track:
- Which workout was performed (reference to Workout template)
- When it occurred (start/end timestamps)
- How hard it felt (perceived difficulty 1-5)
- Completion status (COMPLETED/PARTIAL/SKIPPED)

---

### Daily Log

User's daily check-in before workouts.

Captures:
- **Energy level** (1-5 slider)
- **Stress level** (1-5 slider)
- **Sleep quality** (1-5 slider)
- **Mood** (emoji selector: Great/Good/Okay/Low/Stressed)
- **Optional notes** (free text)

Purpose:
- Informs BRIX workout recommendations
- Prevents over-exertion on low-energy days
- Enables adaptive coaching tone
- Creates data for future analytics

Example flow:
- User logs: energy=2, stress=4, mood="Low"
- BRIX updates motivation_state to "Struggling"
- BRIX recommends 10-min gentle mobility workout instead of 25-min HIIT
- BRIX message: "I see today's tough. A short flow will keep your streak alive without burning you out."

---

### Milestone

Achievement unlocked when reaching specific goals.

Built-in milestones:
- **7-Day Streak** (first week of consistency)
- **30-Day Streak** (one month solid)
- **50 Bricks Laid** (50 workouts completed)
- **100 Bricks Laid** (100 workouts completed)
- **80% Monthly Consistency** (maintain 80%+ for a month)

Milestones trigger:
- Special BRIX celebration messages
- Optional special brick visual (different color/icon)
- Sense of progression and achievement

Milestones are:
- Auto-checked after each workout
- Tracked per user in database
- Displayed on Progress screen

---

## Key Features (MVP)

### 1. Brick Wall Calendar

**Description:** Visual progress tracking via masonry-style calendar.

**Features:**
- 30-day view (7 columns × 5 rows, offset every other row)
- Color-coded bricks (orange=done, blue=today, gray=missed)
- Tappable bricks (show workout details for that day)
- Statistics display (bricks laid, consistency %, current streak)
- Recent milestones section

**Design:**
- Industrial warmth aesthetic (dark backgrounds, orange/blue accents)
- Bricks look like actual bricks (solid blocks, offset rows)
- Clean, readable legend
- No clutter or overwhelming data

**User value:**
- Visual motivation (see progress at a glance)
- Non-punishing (gaps are visible but not shaming)
- Satisfying (filling in the wall feels rewarding)
- Clear tracking (no ambiguity about workout history)

---

### 2. Adaptive Workout Recommendations

**Description:** BRIX suggests workouts based on user's current state.

**Algorithm inputs:**
- Latest daily log (energy, stress, sleep)
- Workout history (recent difficulty ratings)
- Current streak status
- Consistency score
- Motivation state

**Algorithm logic:**
```
IF energy < 3 THEN
  recommend Easy difficulty, shorter duration
ELSE IF energy >= 4 AND stress < 3 AND streak >= 7 THEN
  recommend Medium/Hard difficulty, challenge user
ELSE IF missed yesterday THEN
  recommend shorter workout (ease back in)
ELSE
  recommend based on fitness level and goals
END IF
```

**Tone adaptation:**
- **Encouraging** (70-90% consistency): "You're building something real. Keep showing up!"
- **Challenging** (7+ day streak, high energy): "Seven days strong. Ready to push harder?"
- **Empathetic** (<50% consistency, low energy): "Today's tough. A 5-min flow will keep you moving."

**User value:**
- No guesswork (BRIX picks the right workout)
- Prevents burnout (won't push on low-energy days)
- Maintains streaks (suggests achievable workouts)
- Feels personalized (not generic advice)

---

### 3. Workout Library & Execution

**Description:** Pre-built workout templates with guided execution.

**Library features:**
- 10+ workouts spanning categories:
  - Strength (bodyweight and dumbbell variations)
  - Cardio (HIIT, steady-state)
  - Mobility (stretching, yoga-inspired)
  - Core
- Filter by:
  - Category
  - Difficulty (Easy/Medium/Hard)
  - Equipment (None, Dumbbells, Resistance Bands)
- Each workout shows:
  - Name, description
  - Estimated duration
  - Difficulty level
  - Required equipment
  - Exercise list with sets/reps/duration

**Execution features:**
- Workout timer (tracks elapsed time)
- Exercise list (current exercise highlighted)
- Mark exercises complete
- Perceived difficulty rating (1-5) on completion
- Generate brick automatically

**User value:**
- No planning required (workouts pre-designed)
- Clear instructions (sets, reps, duration)
- Progress tracking (mark exercises done)
- Quick completion (workouts are 10-25 minutes)

---

### 4. Daily Check-In

**Description:** Quick pre-workout state logging for BRIX intelligence.

**Input fields:**
- Energy level (1-5 slider: Low → Medium → High)
- Stress level (1-5 slider: Low → Medium → High)
- Sleep quality (1-5 slider: Poor → Good → Great)
- Mood (5 emoji buttons: 😊 🙂 😐 😔 😰)
- Optional notes (text area for context)

**Workflow:**
1. User taps "Daily Log" button (appears on Home screen)
2. User adjusts sliders and selects mood
3. User optionally adds notes (e.g., "Didn't sleep well, big work deadline")
4. User taps "Complete Check-in"
5. BRIX updates behavior profile
6. BRIX generates acknowledgment message
7. BRIX updates workout recommendation

**User value:**
- Takes 30 seconds
- Makes BRIX recommendations relevant
- Prevents over-exertion
- Creates awareness of patterns (e.g., "I'm stressed a lot on Mondays")

---

### 5. BRIX Chat

**Description:** Conversational interface with AI coach.

**Features:**
- Message history (last 20 messages)
- BRIX messages in orange brick-shaped bubbles
- User messages in gray rounded bubbles
- Message input field
- Context-aware responses

**BRIX message types:**
- **Celebration** (workout completed, milestone achieved)
- **Encouragement** (missed day, low energy)
- **Check-in acknowledgment** ("I see today's tough...")
- **Tips** (form cues, recovery advice)
- **Progress updates** ("23 bricks this month! 77% consistency.")

**Tone examples:**

**Encouraging:**
> "Seven days strong! Your foundation is getting solid. Ready to push harder today?"

**Empathetic:**
> "Low energy today? That's real life. A 5-min flow will keep your streak alive without draining you."

**Celebratory:**
> "🔥 SEVEN-DAY STREAK! That's not luck—that's discipline. You're building something real."

**User value:**
- Feels like a supportive coach (not a robot)
- Non-judgmental (acknowledges struggles)
- Personalized (references actual data)
- Motivating (celebrates wins authentically)

---

### 6. Streak Tracking

**Description:** Automatic calculation of consecutive workout days.

**Streak rules:**
- **Counts:** Any day with a COMPLETED workout session
- **Resets:** If user misses a full day (no workout logged)
- **Grace period:** None in MVP (strict consecutive days)
- **Longest streak:** Tracked separately (never resets)

**Displayed:**
- Home screen (current streak prominently)
- Progress screen (current + longest)
- BRIX messages (reference streak in context)

**Milestone integration:**
- 7-day streak → Milestone achievement
- 30-day streak → Major milestone
- Breaking streak doesn't lose progress (bricks remain, longest streak preserved)

**User value:**
- Clear goal (keep the streak alive)
- Immediate feedback (see number go up)
- Not punishing (longest streak preserved)
- Motivating (gamification element)

---

## Data Model Summary

```
User (1)
├── BehaviorProfile (1)
├── Calendar (implicit, single-user)
│   ├── Brick (0..*)
│   └── WorkoutSession (0..*)
│       └── Brick (1, generated on completion)
├── DailyLog (0..*)
├── BrixMessage (0..*)
└── Milestone (0..*)

Workout (library, pre-seeded)
├── WorkoutExercise (1..*)
│   └── Exercise (reference)

Exercise (library, pre-seeded)
```

### Detailed Entity Descriptions

**User**
- Single demo user (id=1, no authentication)
- Stores: name, age, fitness_level, primary_goal, equipment, weekly_goal_days

**BehaviorProfile**
- One per user (1:1)
- Stores: consecutive_days, longest_streak, total_bricks_laid, consistency_score, last_workout_date, motivation_state, current_tone
- Updated: After workouts, after daily logs

**Brick**
- One per completed workout
- Stores: brick_date, brick_type (workout/milestone), session_id
- Displayed: In brick wall calendar

**WorkoutSession**
- Records actual workout instances
- Stores: start_time, end_time, completion_status, perceived_difficulty, workout_id, profile_id
- Generates: One Brick on completion

**DailyLog**
- One per day maximum
- Stores: log_date, energy_level, stress_level, sleep_quality, mood, notes
- Used by: BRIX recommendation algorithm

**BrixMessage**
- Chat history with BRIX
- Stores: message_text, message_type, tone, context_trigger, sent_at
- Displayed: In BRIX Chat screen

**Milestone**
- Achievement tracking
- Stores: milestone_name, milestone_type, target_value, current_value, is_achieved, achieved_at
- Examples: "7-Day Streak", "50 Bricks Laid", "80% Consistency"

**Workout (Library)**
- Pre-built templates
- Stores: workout_name, description, category, difficulty_level, estimated_duration, equipment_required
- Contains: Multiple exercises via WorkoutExercise junction

**Exercise (Library)**
- Reusable exercise definitions
- Stores: exercise_name, description, muscle_group, video_url
- Used in: Multiple workouts

**WorkoutExercise (Junction)**
- Links Workout → Exercise with parameters
- Stores: workout_id, exercise_id, order_index, sets, reps, duration_seconds

---

## Design Constraints

### Technical Constraints
- **Single-user only** (no authentication, profile_id=1 hardcoded)
- **Mobile-first** (React Native + Expo, iOS/Android)
- **SQLite database** (file-based, embedded)
- **REST API** (Spring Boot, JSON responses)
- **No cloud sync** (local data only in MVP)

### Architectural Constraints
- **3-tier architecture** (Mobile → Backend API → Database)
- **Stateless backend** (no session management)
- **RESTful design** (resource-based URLs, proper HTTP methods)
- **DTO pattern** (API layer separate from database layer)

### Data Constraints
- **One workout per session** (no multi-workout days in MVP)
- **One daily log per day** (cannot log multiple times)
- **One note per event** (not applicable to B3, no notes in MVP)
- **Strict streak rules** (no grace periods, consecutive days only)

### UX Constraints
- **No onboarding tutorial** (app should be self-explanatory)
- **Offline support limited** (can complete workout offline, syncs when online)
- **No social features** (no sharing, friends, leaderboards in MVP)

---

## Non-Goals (MVP)

### Features Explicitly Excluded
- **Multi-user support** (authentication, user accounts)
- **Social features** (sharing progress, comparing with friends)
- **Custom workout builder** (users can't create own workouts)
- **Nutrition tracking** (no calorie counting, meal logging)
- **Wearable integration** (no Apple Watch, Fitbit, etc.)
- **Progress photos** (no before/after image uploads)
- **Body measurements** (no weight, body fat %, measurements)
- **Workout plans** (no multi-week programs)
- **Form instruction** (no video tutorials for exercises)
- **Community challenges** (no group events)

### Technical Non-Goals
- **Real-time sync** (no WebSockets, push notifications)
- **Cloud storage** (no AWS, Firebase integration)
- **Advanced analytics** (no ML predictions, trend analysis)
- **Voice commands** (no speech-to-text workout logging)
- **Internationalization** (English only)

### Design Non-Goals
- **Gamification** (no points, badges, levels beyond streaks/milestones)
- **Competitive elements** (no rankings, challenges against others)
- **Extensive customization** (no theme switching, layout changes)

---

## UX Principles

### 1. Visual Motivation Over Guilt
- Completed workouts are celebrated (orange bricks)
- Missed days are visible but not punishing (gray outlines, not red X's)
- Language is constructive ("gaps to repair" not "failures")
- BRIX never shames or criticizes

### 2. Adaptive Support Over Rigidity
- BRIX adjusts recommendations based on daily state
- Low-energy days get gentler workouts, not impossible challenges
- Tone adapts to user's consistency patterns
- No one-size-fits-all mandates

### 3. Simplicity and Clarity
- App should be self-explanatory (no tutorial required)
- UI is clean, uncluttered, focused
- Actions are immediate and predictable
- Data is transparent (user knows what's tracked and why)

### 4. Habit Formation Over Metrics
- Focus on consistency (streaks, bricks) not calories burned
- Progress measured by showing up, not performance
- Workouts are achievable (10-25 minutes)
- Success = completing workout, regardless of intensity

### 5. Industrial Warmth Aesthetic
- Dark backgrounds with bright accents (not harsh)
- Orange = achievement, energy, completion
- Blue = focus, calm, today's action
- Gray = neutral, structure, foundation
- Typography is bold but friendly

---

## Future Considerations (Post-MVP)

### Phase 2 Features (Months 1-3)
- **Custom workout builder** (user-created workouts)
- **Body silhouette visualization** (alternative to brick wall, muscles fill in)
- **OpenAI-powered BRIX** (GPT-4 for natural conversations)
- **Progress photos** (upload and compare over time)
- **Export functionality** (workout history as CSV/PDF)

### Phase 3 Features (Months 3-6)
- **Multi-user authentication** (sign up, log in)
- **Cloud sync** (data backed up, accessible across devices)
- **Social sharing** (share brick wall achievements)
- **Community challenges** (group streaks, friendly competition)
- **Push notifications** (workout reminders)

### Phase 4 Features (Months 6-12)
- **Advanced analytics** (graphs, trends, predictions)
- **Workout plans** (multi-week progressive programs)
- **Wearable integration** (Apple Watch, Fitbit)
- **Nutrition tracking** (optional meal logging)
- **Voice workout logging** ("Hey BRIX, log today's workout")

### Technical Improvements
- **Backend migration** (SQLite → PostgreSQL for scalability)
- **Caching layer** (Redis for performance)
- **CDN for media** (exercise videos, images)
- **Advanced BRIX** (sentiment analysis, pattern detection)

---

## Success Criteria (MVP)

### User Success Metrics
- ✅ Users can complete first workout within 5 minutes of app launch
- ✅ Brick wall visualization is immediately understandable
- ✅ BRIX recommendations feel relevant (not generic)
- ✅ Users maintain 7+ day streak (shows habit formation)
- ✅ Daily check-in completion rate >60% (shows engagement)

### Technical Success Metrics
- ✅ API response times <500ms (90th percentile)
- ✅ Mobile app runs smoothly on iOS 13+ and Android 8+
- ✅ Zero data loss (workout sessions reliably saved)
- ✅ Offline mode works (workouts can be logged without internet)
- ✅ Backend tests cover >70% of service layer

### Presentation Success Metrics
- ✅ Live demo runs without crashing
- ✅ BRIX adaptive logic is clearly demonstrated
- ✅ Brick wall visualization impresses visually
- ✅ Documentation is comprehensive (README, spec, UML, data model)
- ✅ Code is clean, commented, follows best practices

---

## Summary

B3 (Brick by Brick) is a mobile fitness application that reframes workout tracking as **building a foundation brick by brick**.

Rather than focusing on calories, weight, or performance metrics, B3 emphasizes **consistent habit formation** through:
- Visual progress (brick wall calendar)
- Adaptive coaching (BRIX learns and adjusts)
- Non-judgmental support (gaps to repair, not failures)
- Achievable workouts (10-25 minutes, no gym required)

The application is designed for **beginners and intermediate users** who struggle with consistency, not advanced athletes seeking performance optimization.

B3 is built as a **single-user demo application** showcasing full-stack development (React Native mobile + Spring Boot backend + SQLite database) with a unique UX approach that avoids common fitness app pitfalls (guilt, rigidity, overwhelming metrics).

The MVP focuses on **core functionality** (workout tracking, brick visualization, adaptive recommendations) while laying groundwork for future enhancements (social features, advanced analytics, multi-user support).

Success is measured by **user engagement** (streak maintenance, daily check-in completion) and **technical quality** (performance, reliability, code cleanliness), with the ultimate goal of demonstrating both **software engineering skills** and **thoughtful product design**.

---

**End of Specification Sheet**