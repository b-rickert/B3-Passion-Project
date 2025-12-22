# B3 (Brick by Brick)

![B3 Logo](https://img.shields.io/badge/B3-Brick%20by%20Brick-orange)
![Java](https://img.shields.io/badge/Java-Spring%20Boot-green)
![React Native](https://img.shields.io/badge/React%20Native-Expo-blue)
![SQLite](https://img.shields.io/badge/Database-SQLite-lightgrey)

> **Build Yourself. Brick by Brick.**

B3 is a mobile fitness application that helps users rebuild their health through consistent, achievable actions. At its core is **BRIX**, an adaptive AI behavior coach that learns patterns, adjusts coaching tone, and provides personalized guidance—one brick at a time.

---

## Project Overview

Most fitness apps fail at **adherence**, not features. B3 transforms consistency through:

- **Adaptive Behavioral Coaching** — BRIX learns your patterns and adjusts its tone (supportive, challenging, gentle, energizing)
- **Momentum-Based Progression** — Every workout is a "brick" building your visual foundation
- **Context-Aware Recommendations** — Workouts adapt to your energy, stress, and schedule

### Key Differentiator
Unlike generic fitness apps, B3 competes on **behavioral transformation**. BRIX forms relationships, not just recommendations. The brick metaphor creates tangible progress that abstract numbers cannot match.

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
```
┌─────────────────┐
│  Mobile Client  │  (React Native + Expo)
│   (iOS/Android) │
└────────┬────────┘
         │ REST API
         ▼
┌─────────────────┐
│  Spring Boot    │  (Java Backend)
│  ├─ Controllers │  (REST endpoints)
│  ├─ Services    │  (Business logic, BRIX rules)
│  └─ Repositories│  (Data access)
└────────┬────────┘
         │ JPA
         ▼
┌─────────────────┐
│     SQLite      │  (Embedded database)
└─────────────────┘
```

---

## Features

### MVP Features (v1.0)
- BRIX-guided onboarding
- Visual brick-building progress wall (30-day calendar)
- Habit and consistency tracking
- Behavioral pattern recognition
- Adaptive coaching tone (4 modes)
- Starter workout library (15-20 workouts)
- Daily energy/mood/stress logging
- Personalized workout recommendations
- Milestone tracking and achievements

### Future Roadmap
- Social accountability pods (anonymous groups)
- Nutrition tracking integration
- Custom workout builder
- Voice-enabled BRIX coaching
- Wearable device integration
- Web dashboard for analytics

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
   git clone https://github.com/yourusername/B3-Passion-Project.git
   cd B3-Passion-Project/backend
   ```

2. **Configure application properties**
   
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

3. **Build and run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Verify backend is running**
   
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
│   ├── one-pager.md
│   ├── detailed-overview.md
│   ├── data-model.md
│   ├── ui-design.md
│   └── file-structure.md
│
└── README.md
```

---

## API Documentation

### Base URL
```
http://localhost:8080/api
```

### Key Endpoints

#### User Profile (Single Demo User)
```
GET    /api/profile              # Get user profile
PUT    /api/profile              # Update user preferences (equipment, goals, etc.)
```

**Note**: No authentication required. The app uses a single demo user profile (profileId = 1) to store preferences like available equipment, fitness level, and goals. This allows BRIX to personalize coaching without implementing login functionality.

#### Workouts
```
GET    /api/workouts             # List all workouts
GET    /api/workouts/{id}        # Get workout details
POST   /api/workouts/sessions    # Start workout session
PUT    /api/workouts/sessions/{id} # Complete workout session
```

#### Daily Logs
```
POST   /api/logs/daily           # Create daily log (energy, mood, stress)
GET    /api/logs/daily/latest    # Get today's log
```

#### Progress
```
GET    /api/progress/bricks      # Get brick wall (30 days)
GET    /api/progress/stats       # Get user statistics
GET    /api/progress/milestones  # Get milestones
```

#### BRIX
```
POST   /api/brix/message         # Get BRIX coaching message
GET    /api/brix/recommendation  # Get workout recommendation
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
--orange-600: #ea580c  /* Primary actions */
--orange-500: #f97316  /* Hover states */
--amber-500: #f59e0b   /* Achievements */
```

### Typography
- Display: System UI (bold weights 700-900)
- Body: System UI (regular 400-600)
- Labels: Uppercase, letter-spacing

---

## Development Notes

### BRIX Behavioral Rules
BRIX uses a rule-based system with four coaching tones:
- **Supportive**: Low energy, returning users, gentle encouragement
- **Challenging**: High consistency streaks, performance improvement
- **Gentle**: After long breaks, low motivation periods
- **Energizing**: New users, milestone achievements

Rules are evaluated based on:
- Days since last workout
- Current streak length
- Energy/mood logs
- Historical patterns

### Database Schema
See `docs/data-model.md` for complete entity relationship diagram and schema details.

Key entities:
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

---

## Success Metrics

Target metrics vs industry average:
- **60% adherence** (3+ workouts/week after 90 days) vs. 10-15% industry
- **50% retention** (active after 30 days) vs. 15-20% industry
- **70% comeback rate** (returning after breaks) vs. 20-30% industry

---

## Development Timeline

**Week 1-2**: Core infrastructure (Spring Boot API, database schema)  
**Week 3-4**: BRIX intelligence layer (behavior tracking, OpenAI integration)  
**Week 5**: React Native UI (home, workouts, progress, daily log)  
**Week 6**: Polish, testing, demo preparation

---

## License

This project is developed as a bootcamp capstone project.

---

## Author

**Your Name**
- GitHub: [@yourusername](https://github.com/b-rickert)
- LinkedIn: [Your LinkedIn](www.linkedin.com/in/ben-rickert)

---

## Acknowledgments

- Bootcamp instructors and mentors
- OpenAI for BRIX intelligence capabilities
- React Native and Expo communities
- Spring Boot ecosystem

---

**Built one brick at a time.**