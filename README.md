# B3 (Brick by Brick)

![B3 Logo](https://img.shields.io/badge/B3-Brick%20by%20Brick-orange)
![Status](https://img.shields.io/badge/Status-In%20Development-yellow)
![Java](https://img.shields.io/badge/Java-Spring%20Boot-green)
![React Native](https://img.shields.io/badge/React%20Native-Expo-blue)
![SQLite](https://img.shields.io/badge/Database-SQLite-lightgrey)

> **Build Yourself. Brick by Brick.**

B3 is a mobile fitness application that helps users rebuild their health through consistent, achievable actions. At its core is **BRIX**, an adaptive AI behavior coach that learns patterns, adjusts coaching tone, and provides personalized guidance—one brick at a time.

---

## Table of Contents

- [Project Overview](#project-overview)
- [The Problem](#the-problem)
- [The Solution](#the-solution)
- [User Experience](#user-experience)
- [Target Users](#target-users)
- [Documentation](#documentation)
- [Architecture](#architecture)
- [Features](#features)
- [Screenshots](#screenshots)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Design System](#design-system)
- [Development Notes](#development-notes)
- [Success Metrics](#success-metrics)
- [Competitive Differentiation](#competitive-differentiation)
- [Development Timeline](#development-timeline)
- [Future Roadmap](#future-roadmap)
- [License](#license)
- [Author](#author)
- [Acknowledgments](#acknowledgments)

---

## Project Overview

Most fitness apps fail at **adherence**, not features. Users don't abandon fitness because they lack workout options or tracking tools—they abandon because motivation fluctuates, life intervenes, and apps treat everyone identically. Generic workout plans ignore individual psychology, energy states, and real-world constraints. The result: **90% of users fall off within 60 days**, restarting endlessly in cycles of guilt and frustration.

B3 transforms consistency through:

- **Adaptive Behavioral Coaching** — BRIX learns your patterns and adjusts its tone (supportive, challenging, gentle, energizing)
- **Momentum-Based Progression** — Every workout is a "brick" building your visual foundation
- **Context-Aware Recommendations** — Workouts adapt to your energy, stress, and schedule

### Key Differentiator

Unlike generic fitness apps that compete on workout libraries or macro tracking, B3 competes on **behavioral transformation**. BRIX forms relationships, not just recommendations. The brick metaphor and visual foundation create tangible progress that abstract numbers and charts cannot match. Users stay because they're building something visible, not because they're hitting arbitrary targets.

---

## The Problem

Most fitness apps fail because they focus on **features rather than adherence**. Users don't abandon fitness because they lack workout options or tracking tools—they abandon because:

- **Motivation fluctuates** naturally over time
- **Life intervenes** with travel, illness, stress, and schedule changes
- **Apps treat everyone identically** with generic one-size-fits-all plans
- **Generic workout plans ignore** individual psychology, energy states, and real-world constraints

The result: 90% of users fall off within 60 days, restarting endlessly in cycles of guilt and frustration.

---

## The Solution

B3 transforms adherence through three core innovations that address the psychological and behavioral barriers to consistency:

### 1. Adaptive Behavioral Coaching

BRIX continuously observes user behavior—workout patterns, timing preferences, consistency metrics, energy logs, and engagement with different coaching styles. Over time, BRIX learns what resonates most with each user and automatically adjusts its tone:

- **Supportive** during low-motivation periods
- **Challenging** during strong streaks
- **Gentle** after long breaks
- **Energizing** when users need a push

There are no personality quizzes or static profiles—BRIX simply learns by watching, responding, and refining. This creates the feeling of working with a personal coach who genuinely knows you.

### 2. Momentum-Based Progression ("The Brick System")

Every action taken in B3—whether a 3-minute stretch or a full 30-minute workout—counts as a "brick." Users build their fitness journey brick by brick, creating a visual structure of their progress that resembles an actual foundation being constructed.

Missing a day isn't framed as failure or a broken streak—it's simply a gap in the wall that BRIX helps repair through encouragement, micro-wins, and accessible restart pathways. This reframes fitness from perfectionism to construction work: **some days you lay more bricks, some days fewer, but the foundation keeps growing.**

This psychological shift:
- Reduces guilt and shame
- Combats all-or-nothing thinking
- Reinforces consistency as the primary metric of success

### 3. Context-Aware Recommendations

BRIX interprets real-world signals beyond workout history. When a user logs low energy, limited available time, or high stress, BRIX adapts immediately:

> "Today's schedule is packed—want a 5-minute win instead of your usual 20-minute session?"

When users return after illness, travel, or extended breaks, BRIX automatically shifts into maintenance or rebuild mode with gentler workouts and modified expectations. The app understands that life happens and adjusts programming accordingly, ensuring progress continues without pressure or perfectionism. **This context awareness eliminates the friction that causes most users to quit.**

---

## User Experience

B3 feels like having a personal coach who genuinely understands you. BRIX:

- **Remembers past milestones:** "This is the 5th time you've completed this routine—that consistency matters"
- **Recognizes patterns:** "You tend to work out best in the mornings—want me to queue up a 7 AM reminder?"
- **Celebrates wins in personalized ways:** "Seven days straight! Your foundation is getting stronger"

The visual brick wall provides **tangible evidence of progress**, transforming abstract fitness goals into concrete construction work. Users see their foundation growing day by day, creating intrinsic motivation that outlasts external accountability.

### BRIX Conversational Examples

**Returning user who hasn't worked out in a week:**
> "Welcome back. Let's start with one brick today—a simple 3-minute movement to rebuild momentum. No judgment, just progress."

**Consistent user on a 10-day streak:**
> "You've stacked ten strong days. Your foundation is getting solid. Ready to push a little harder today?"

---

## Target Users

- **Frequent restarters:** Individuals who repeatedly "start over" every few months and struggle with long-term consistency
- **Beginners:** People seeking structured guidance without overwhelming intensity or complexity
- **Accountability seekers:** Users who benefit from coaching-driven motivation rather than data-driven dashboards
- **Life-juggling adults:** Anyone balancing work, family, and stress who needs fitness to adapt to their real-world constraints

---

## Documentation

- [Project Specification](docs/specification-sheet.md) - Complete requirements document
- [Data Model](docs/data-model/data-model.md) - Database schema and relationships
- [UML Class Diagram](docs/B3-UML.png) - System architecture
- [UI/UX Wireframes](docs/ui-ux-mockup/) - Mobile screen designs

---

## Architecture

### Tech Stack

**Backend**
- Java 17+
- Spring Boot 4.x
- Spring Data JPA
- Spring Web (RESTful API)
- SQLite (embedded database)

**Frontend**
- React Native
- Expo
- NativeWind (Tailwind utilities)
- React Navigation

**AI Integration**
- OpenAI API
- Custom BRIX behavioral rule engine

**Architecture Pattern**

Three-tier architecture with clear separation of concerns:

- **Presentation Layer:** React Native mobile app (iOS/Android via Expo)
- **Business Logic Layer:** Spring Boot backend with RESTful API
  - Controllers: Handle HTTP requests/responses
  - Services: BRIX intelligence, business rules, recommendations
  - Repositories: Data access and persistence
- **Data Layer:** SQLite embedded database

Communication flow: Mobile → REST API → Spring Boot → JPA → SQLite

### Why This Stack?

**Backend: Java Spring Boot**
- Industry-standard framework for enterprise applications
- Robust ecosystem for REST APIs
- Excellent ORM support with JPA/Hibernate
- Strong typing and compile-time safety
- Perfect for bootcamp demonstration

**Frontend: React Native + Expo**
- Single codebase for iOS and Android
- Native performance with JavaScript flexibility
- Expo simplifies development workflow (no Xcode/Android Studio required)
- Hot reloading for rapid iteration
- Large community and extensive libraries

**Database: SQLite**
- Lightweight and embedded (no server setup)
- Perfect for single-user MVP
- File-based database (easy to demo and debug)
- Simple migration path to PostgreSQL for production
- Zero configuration required

**AI: OpenAI API**
- State-of-the-art natural language generation
- Consistent, context-aware coaching messages
- Easy integration via REST API
- Flexible prompt engineering for BRIX personality

---

## Features

### MVP Features (v1.0)

- **BRIX-guided onboarding:** Personalized welcome experience that establishes baseline patterns and preferences
- **Visual brick-building progress system:** Interactive 30-day foundation wall showing completed and missed workouts
- **Habit and consistency tracking:** Automatic streak counting, consistency scoring, and pattern recognition
- **Behavioral pattern recognition:** Silent observation of workout timing, duration preferences, and engagement patterns
- **Adaptive coaching tone:** Dynamic adjustment between supportive, challenging, gentle, and energizing messaging (4 modes)
- **Starter workout library:** Curated collection of 15-20 workouts across difficulty levels and durations (5-30 minutes)
- **Energy/mood logging:** Daily check-in system capturing energy, stress, sleep quality, and mood for context-aware recommendations
- **Personalized daily recommendations:** BRIX-powered workout suggestions based on current state and historical patterns
- **Milestone tracking:** Achievement system recognizing first workouts, streaks, comeback efforts, and cumulative progress

### Future Roadmap (Post-MVP)

- **Social Pods:** Anonymous 5-7 person accountability groups matched by motivation style
- **Nutrition Integration:** Meal logging and energy correlation with workout performance
- **Custom Workout Builder:** User-created routines sharable within community
- **Voice-Enabled BRIX:** Hands-free coaching during workouts via speech synthesis
- **Wearable Integration:** Apple Watch/Fitbit data for automatic workout detection
- **Premium Content:** Structured multi-week programs with progressive difficulty
- **Web Dashboard:** Desktop interface for detailed analytics and program planning

---

## Screenshots

| Home Screen   | Progress Screen | BRIX Chat     |
|---------------|-----------------|---------------|
| *Coming soon* | *Coming soon*   | *Coming soon* |

*Screenshots will be added as development progresses*

---

---

## Getting Started

### Prerequisites

**Backend**
- Java 17 or higher
- Maven 3.8+
- IDE: IntelliJ IDEA (recommended) or VS Code

**Frontend**
- Node.js 18+ and npm
- Expo CLI
- iOS Simulator (Mac) or Android Studio (for emulator)

**API Keys**
- OpenAI API key (for BRIX intelligence)

---

### Backend Setup

1. **Clone the repository**
```bash
   git clone https://github.com/b-rickert/B3-Passion-Project.git
   cd B3-Passion-Project
```

2. **Navigate to backend directory**
```bash
   cd backend
```

3. **Configure application properties**

   Edit `src/main/resources/application.properties`:
```properties
   # Server Configuration
   server.port=8080

   # SQLite Database
   spring.datasource.url=jdbc:sqlite:b3.db
   spring.datasource.driver-class-name=org.sqlite.JDBC
   spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
   spring.jpa.hibernate.ddl-auto=update

   # OpenAI API
   openai.api.key=your-api-key-here
   openai.api.url=https://api.anthropic.com/v1/messages

   # Logging
   logging.level.com.b3=DEBUG
```

4. **Build and run**
```bash
   mvn clean install
   mvn spring-boot:run
```

5. **Verify backend is running**

   Server should start on `http://localhost:8080`

---

### Frontend Setup

1. **Navigate to frontend directory**
```bash
   cd ../mobile
```

2. **Install dependencies**
```bash
   npm install
```

3. **Configure environment**

   Create `.env` file:
```env
   API_BASE_URL=http://localhost:8080/api
```

4. **Start Expo development server**
```bash
   npx expo start
```

5. **Run on device/simulator**
   - Press `i` for iOS simulator
   - Press `a` for Android emulator
   - Scan QR code with Expo Go app on physical device

---

## Project Structure
```
B3-Passion-Project/
├── backend/                    # Java Spring Boot backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/b3/
│   │   │   │   ├── controller/     # REST API endpoints
│   │   │   │   ├── service/        # Business logic & BRIX engine
│   │   │   │   ├── repository/     # Data access layer
│   │   │   │   ├── model/          # Entity classes
│   │   │   │   └── dto/            # Data transfer objects
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   │       └── java/com/b3/
│   │           ├── model/          # Entity tests
│   │           ├── repository/     # Repository tests
│   │           ├── service/        # Service tests
│   │           └── controller/     # API tests
│   └── pom.xml
│
├── mobile/                     # React Native frontend
│   ├── src/
│   │   ├── screens/            # Main app screens
│   │   ├── components/         # Reusable components
│   │   ├── services/           # API calls
│   │   ├── utils/              # Helper functions
│   │   └── navigation/         # Navigation setup
│   ├── App.jsx
│   ├── tailwind.config.js
│   └── package.json
│
├── docs/                       # Documentation
│   ├── specification-sheet.md
│   ├── data-model/
│   │   ├── data-model.md
│   │   └── B3-DataModel-Visual.png
│   ├── ui-ux-mockup/
│   └── B3-UML.png
│
└── README.md
```

---

## API Documentation

### Base URL
```
http://localhost:8080/api/v1
```

### Key Endpoints

#### User Profile (Single Demo User)
```
GET    /api/v1/profile           # Get user profile
PUT    /api/v1/profile           # Update user preferences (equipment, goals, etc.)
```

**Note**: No authentication required. The app uses a single demo user profile (profileId = 1) to store preferences like available equipment, fitness level, and goals. This allows BRIX to personalize coaching without implementing login functionality.

#### Workouts
```
GET    /api/v1/workouts          # List all workouts
GET    /api/v1/workouts/{id}     # Get workout details
POST   /api/v1/sessions          # Start workout session
PUT    /api/v1/sessions/{id}     # Complete workout session
GET    /api/v1/sessions/history  # Get workout history
```

#### Daily Logs
```
POST   /api/v1/logs              # Create daily log (energy, mood, stress)
GET    /api/v1/logs/recent       # Get recent logs
```

#### Progress
```
GET    /api/v1/bricks?startDate={date}&endDate={date}  # Get brick wall data
GET    /api/v1/milestones        # Get milestones
```

#### BRIX
```
POST   /api/v1/brix/chat         # Send message to BRIX
GET    /api/v1/brix/recommendation  # Get workout recommendation
```

---

## Testing

### Backend Tests
```bash
cd backend
mvn test
```

### Frontend Tests
```bash
cd mobile
npm test
```

---

## Design System

### Color Palette
```css
/* Foundation */
--zinc-950: #09090b  /* Primary background */
--zinc-900: #18181b  /* Card backgrounds */
--zinc-800: #27272a  /* Interactive elements */
--zinc-700: #3f3f46  /* Borders */

/* Accents */
--orange-500: #f97316  /* Primary actions */
--amber-500: #f59e0b   /* Achievements */
--blue-500: #3b82f6    /* Today's focus */
```

### Typography
- Display: System UI (bold weights 700-900)
- Body: System UI (regular 400-600)
- Labels: Uppercase, letter-spacing

### Design Philosophy

B3's interface embodies the "brick by brick" metaphor through an **industrial warmth aesthetic**:

- **Dark zinc foundation colors** (zinc-950, zinc-900, zinc-800) representing structural stability
- **Orange/amber gradients** symbolizing the motivational fire that drives progress

The design balances **strength and approachability**:
- Bold typography and confident layouts convey discipline
- Warm accent colors and generous spacing create psychological safety for beginners

**Every visual element reinforces the construction metaphor:**
- Completed workouts appear as solid, glowing bricks
- Missed days show as outlined gaps
- Milestones resemble construction site achievements

---

## Development Notes

### BRIX Behavioral Rules

BRIX uses a rule-based system with four coaching tones:

- **Supportive**: Low energy, returning users, gentle encouragement
- **Challenging**: High consistency streaks, performance improvement
- **Gentle**: After long breaks, low motivation periods
- **Energizing**: New users, milestone achievements

**Rules are evaluated based on:**
- Days since last workout
- Current streak length
- Energy/mood logs
- Historical patterns

### Database Schema

See [docs/data-model/data-model.md](docs/data-model/data-model.md) for complete entity relationship diagram and schema details.

**Key entities:**
- UserProfile (single demo user profile)
- BehaviorProfile (BRIX learning data)
- Workout / Exercise
- WorkoutSession
- Brick (progress tracking)
- DailyLog
- BrixMessage
- Milestone

### Single User Design

This MVP uses a single demo user (no authentication) to simplify development:
- UserProfile with profileId = 1
- Stores equipment, fitness level, goals
- BRIX personalizes based on this profile
- Production version would add multi-user support

### SQLite vs PostgreSQL

**Why SQLite for MVP:**
- Lightweight embedded database ideal for rapid iteration
- Local-first storage (database is just a file)
- Simple schema management during development
- Perfect for bootcamp timeline
- Zero server configuration

**Migration to PostgreSQL (Future):**
- Easy migration path when scaling to multi-user
- Change `pom.xml` dependency
- Update `application.properties`
- Entity classes remain unchanged
- Document migration plan in presentation: "Chose SQLite for single-user demo, would migrate to PostgreSQL for production multi-user support"

---

## Success Metrics

Target metrics vs industry average:

| Metric | B3 Target | Industry Average |
|--------|-----------|------------------|
| **Adherence Rate** (3+ workouts/week after 90 days) | 60% | 10-15% |
| **Retention** (active after 30 days) | 50% | 15-20% |
| **Comeback Rate** (returning after 7+ day breaks) | 70% | 20-30% |
| **Engagement Depth** (avg bricks/user/month) | 15+ | N/A |

---

## Competitive Differentiation

Unlike generic fitness apps that compete on workout libraries or macro tracking, B3 competes on **behavioral transformation**.

**Competitors** (MyFitnessPal, Fitbod, Nike Training Club):
- ✅ Excel at content and data
- ❌ Fail at psychology and adherence

**B3's Unique Advantage:**
- ✅ BRIX forms relationships, not just recommendations
- ✅ Brick metaphor creates tangible progress that abstract numbers cannot match
- ✅ Users stay because they're building something visible
- ✅ Context-aware adaptation eliminates friction
- ✅ Momentum-based progression reduces guilt and shame

---

## Development Timeline

**6 Weeks to Final Presentation**

| Week | Focus |
|------|-------|
| **Week 1-2** | Core infrastructure (Spring Boot API, database schema, basic CRUD operations) |
| **Week 3-4** | BRIX intelligence layer (behavior tracking, rule engine, OpenAI integration) |
| **Week 5** | React Native UI implementation (home, workouts, progress, daily log) |
| **Week 6** | Polish, testing, demo preparation, presentation materials |

---

## Future Roadmap

### Post-MVP Features

- **Social Pods:** Anonymous 5-7 person accountability groups matched by motivation style
- **Nutrition Integration:** Meal logging and energy correlation with workout performance
- **Custom Workout Builder:** User-created routines sharable within community
- **Voice-Enabled BRIX:** Hands-free coaching during workouts via speech synthesis
- **Wearable Integration:** Apple Watch/Fitbit data for automatic workout detection
- **Premium Content:** Structured multi-week programs with progressive difficulty
- **Web Dashboard:** Desktop interface for detailed analytics and program planning

---

## License

This project is developed as a bootcamp capstone project.

---

## Author

**Ben Rickert**
- GitHub: [@b-rickert](https://github.com/b-rickert)
- LinkedIn: [Ben Rickert](https://www.linkedin.com/in/ben-rickert)
- Bootcamp: Zip Code Wilmington

---

## Acknowledgments

- Zip Code Wilmington bootcamp instructors and mentors
- OpenAI for BRIX intelligence capabilities
- React Native and Expo communities
- Spring Boot ecosystem

---

**Tagline:** "Build Yourself. Brick by Brick."

**Core Message:** Fitness isn't about perfection. It's about showing up, laying one brick at a time, and building a foundation strong enough to support the life you want.