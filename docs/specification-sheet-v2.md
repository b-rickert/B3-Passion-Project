# B3 (Brick by Brick) - Specification Sheet v2

**Version:** 2.0
**Updated:** January 2025
**Project Type:** Mobile Fitness Application with Adaptive AI Coach
**Target Platform:** iOS & Android (React Native + Expo)
**Backend:** Java 21, Spring Boot 3.4.1 + SQLite
**AI:** Ollama/Llama 3.2 (local, free)

---

## Product Name

**B3** (Brick by Brick)

---

## Product Description

A mobile fitness application that helps users build consistent workout habits through **visual progress tracking** and **adaptive AI coaching**.

B3 reframes fitness as "building a foundation brick by brick" - each completed workout becomes a visual brick in a masonry-style calendar. An AI coach named BRIX learns user patterns, adapts its tone based on consistency, and recommends workouts based on daily energy, stress, and sleep data.

The application prioritizes **habit formation over metrics**, **visual motivation over guilt**, and **adaptive support over one-size-fits-all plans**.

---

## Tech Stack (Current Implementation)

### Backend
- **Java 21** with Spring Boot 3.4.1
- **Spring Data JPA** for database access
- **SQLite** embedded database (file: `backend/b3.db`)
- **RESTful API** at `localhost:8080/api/v1`

### Frontend
- **React Native 0.81.5** with Expo 54
- **NativeWind 4.x** (Tailwind utilities for React Native)
- **React Navigation** for screen navigation
- **Lucide React Native** icons
- **Expo Linear Gradient** for visual effects

### AI Integration
- **Primary:** Ollama with Llama 3.2 (free, runs locally)
- **Optional Fallback:** Claude API via Anthropic SDK
- **Fallback:** Keyword-based responses if AI unavailable

---

## Application Screens

### Navigation Structure
```
LandingScreen (entry)
    └── MainTabs (bottom tab navigator)
            ├── HomeScreen
            ├── WorkoutsScreen
            │       └── WorkoutDetailScreen
            ├── ProgressScreen
            ├── BrixScreen (BRIX chat)
            └── ProfileScreen
                    ├── SettingsScreen
                    ├── HelpSupportScreen
                    ├── PersonalRecordsScreen
                    └── DailyLogScreen
```

### Screen Details

**LandingScreen**
- Animated entrance with B3 logo
- Floating decorative bricks animation
- Feature highlights (Build foundation, Track streaks, AI coaching)
- "Get Started" button transitions to main app

**HomeScreen**
- Welcome message with BRIX
- Current streak display
- Quick workout recommendation
- Today's daily log status
- Start workout action

**WorkoutsScreen**
- Workout library grouped by category
- Filter by difficulty and equipment
- Expandable workout cards showing exercises
- Exercise images displayed

**WorkoutDetailScreen**
- Full exercise list with images
- Sets, reps, duration for each exercise
- Start workout button
- Timer functionality

**ProgressScreen**
- 3D brick wall calendar (30-day view)
- Color-coded bricks (orange=done, blue=today, gray=missed)
- Stats: total bricks, streak, consistency
- Milestone achievements

**BrixScreen**
- Chat interface with BRIX AI
- Context-aware responses
- Message history
- Powered by Ollama/Llama 3.2

**ProfileScreen**
- User profile display
- Navigation to settings, help, records
- Daily log quick access

---

## Visual Design System

### BrickBackground Component
All screens use a custom `BrickBackground` component featuring:
- **3D brick pattern** with staggered rows
- **Depth effect** using highlights and shadows
- **Zinc color palette** for industrial aesthetic
- **Orange/blue gradient overlays** for warmth

### Color Palette
```javascript
colors = {
  background: {
    start: '#09090b',    // Primary dark
    end: '#18181b',      // Secondary dark
    glass: 'rgba(255, 255, 255, 0.05)',
    glassBorder: 'rgba(255, 255, 255, 0.1)',
  },
  orange: {
    DEFAULT: '#f97316',  // Primary accent
    light: '#fdba74',
    dark: '#c2410c',
  },
  amber: '#f59e0b',      // Achievements
  blue: '#3b82f6',       // Today's focus
  text: {
    primary: '#ffffff',
    secondary: 'rgba(255, 255, 255, 0.7)',
    muted: 'rgba(255, 255, 255, 0.5)',
  }
}
```

### Brick Styling
```javascript
// 3D brick effect colors
shadeColors = [
  { base: '#2a2a30', highlight: '#4a4a55', shadow: '#0a0a0c' },
  { base: '#323238', highlight: '#55555f', shadow: '#0c0c0e' },
  { base: '#252528', highlight: '#45454f', shadow: '#08080a' },
]
```

---

## API Endpoints

### Base URL
```
http://localhost:8080/api/v1
```

### Profile
```
GET    /profile              # Get all profiles
GET    /profile/{id}         # Get profile by ID
POST   /profile              # Create profile
PUT    /profile/{id}         # Update profile
```

### Workouts
```
GET    /workouts                      # List all workouts
GET    /workouts/{id}                 # Get workout details
GET    /workouts/type/{type}          # Filter by category
GET    /workouts/difficulty/{level}   # Filter by difficulty
GET    /workouts/{id}/exercises       # Get workout exercises
```

### Sessions
```
POST   /sessions                       # Start workout session
PUT    /sessions/{id}/complete         # Complete session
GET    /sessions/active/{profileId}    # Get active session
```

### Bricks (Progress)
```
GET    /bricks/calendar/{profileId}    # Get brick calendar data
GET    /bricks/stats/{profileId}       # Get brick statistics
```

### Daily Logs
```
POST   /daily-logs                     # Create daily log
GET    /daily-logs/today/{profileId}   # Get today's log
```

### Milestones
```
GET    /milestones/{profileId}         # Get user milestones
POST   /milestones/{profileId}/check   # Check for new achievements
```

### Behavior Profile
```
GET    /behavior/{profileId}           # Get behavior data
```

### BRIX AI
```
POST   /brix/chat                      # Chat with BRIX
GET    /brix/recommendation            # Get workout recommendation
```

---

## BRIX Coaching System

### Coaching Tones
- **ENCOURAGING**: Low energy, returning users, gentle encouragement
- **CHALLENGING**: High consistency streaks, ready for more
- **EMPATHETIC**: After long breaks, low motivation periods
- **CELEBRATORY**: Milestone achievements, streak completions

### Motivation States
- **MOTIVATED**: High energy, consistent workouts
- **NEUTRAL**: Stable, moderate engagement
- **STRUGGLING**: Low energy, missed workouts, returning from break

### Momentum Trends
- **RISING**: Improving consistency
- **STABLE**: Maintaining consistency
- **FALLING**: Declining engagement

### AI Integration
```java
// OllamaService.java
- Connects to Ollama at localhost:11434
- Uses Llama 3.2 model
- Formats BRIX personality in system prompt
- Falls back to keyword responses if unavailable
```

---

## Data Model Summary

### Core Entities
- **UserProfile**: User preferences and settings
- **BehaviorProfile**: BRIX learning data (1:1 with UserProfile)
- **Workout**: Workout templates
- **Exercise**: Individual exercises
- **WorkoutExercise**: Junction table (Workout ↔ Exercise)
- **WorkoutSession**: Actual workout instances
- **Brick**: Progress visualization (1:1 with completed session)
- **DailyLog**: Energy/mood/stress check-ins
- **BrixMessage**: Chat history with BRIX
- **Milestone**: Achievement tracking

### Key Relationships
```
UserProfile (1) ←→ (1) BehaviorProfile
UserProfile (1) → (N) WorkoutSession → (1) Brick
Workout (1) ←→ (N) WorkoutExercise ←→ (N) Exercise
```

---

## Development Setup

### Prerequisites
- Java 21+
- Maven 3.8+
- Node.js 18+
- Expo CLI
- Ollama (for AI features)

### Quick Start
```bash
# Install Ollama and pull model
ollama pull llama3.2
ollama serve

# Start backend
cd backend
mvn spring-boot:run

# Start frontend
cd mobile
npm install
npx expo start
```

### Environment Variables
Create `backend/.env`:
```
ANTHROPIC_API_KEY=your-key-here  # Optional, for Claude fallback
```

---

## Success Criteria

### User Success
- First workout completion within 5 minutes of app launch
- Brick wall visualization immediately understandable
- BRIX recommendations feel personalized
- Users maintain 7+ day streaks

### Technical Success
- API response times <500ms
- Smooth mobile performance on iOS 13+ / Android 8+
- Zero data loss
- Backend test coverage >70%

---

## Future Roadmap

### Near Term
- Push notifications for workout reminders
- Offline workout support
- More workout templates
- Progress photos

### Medium Term
- Multi-user authentication
- Cloud sync
- Social sharing
- Custom workout builder

### Long Term
- Wearable integration
- Voice-enabled BRIX
- Advanced analytics
- Premium content

---

**End of Specification Sheet v2**
