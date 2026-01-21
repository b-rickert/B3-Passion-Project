# B3 (Brick by Brick)

![B3 Logo](https://img.shields.io/badge/B3-Brick%20by%20Brick-orange)
![Status](https://img.shields.io/badge/Status-MVP%20Complete-green)
![Java](https://img.shields.io/badge/Java%2021-Spring%20Boot%203.4-green)
![React Native](https://img.shields.io/badge/React%20Native-Expo%2054-blue)
![AI](https://img.shields.io/badge/AI-Ollama%20Llama%203.2-purple)

> **Build Yourself. Brick by Brick.**

B3 is a mobile fitness application that helps users rebuild their health through consistent, achievable actions. At its core is **BRIX**, an adaptive AI behavior coach powered by Llama 3.2 that learns patterns, adjusts coaching tone, and provides personalized guidance—one brick at a time.

---

## Table of Contents

- [Quick Start](#quick-start)
- [Project Overview](#project-overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Screenshots](#screenshots)
- [Documentation](#documentation)
- [API Reference](#api-reference)
- [Development](#development)
- [Author](#author)

---

## Quick Start

### Prerequisites
- Java 21+
- Node.js 18+
- [Ollama](https://ollama.ai) (for AI features)

### Setup

```bash
# 1. Clone the repository
git clone https://github.com/b-rickert/B3-Passion-Project.git
cd B3-Passion-Project

# 2. Setup AI (Ollama)
ollama pull llama3.2
ollama serve  # Keep running in background

# 3. Start backend
cd backend
mvn spring-boot:run

# 4. Start mobile app (new terminal)
cd mobile
npm install
npx expo start
```

Press `i` for iOS simulator, `a` for Android, or scan QR with Expo Go app.

---

## Project Overview

Most fitness apps fail at **adherence**, not features. Users don't abandon fitness because they lack workout options—they abandon because motivation fluctuates, life intervenes, and apps treat everyone identically. The result: **90% of users quit within 60 days**.

B3 transforms consistency through:

- **Adaptive Behavioral Coaching** — BRIX learns your patterns and adjusts its tone (supportive, challenging, gentle, celebratory)
- **Momentum-Based Progression** — Every workout is a "brick" building your visual foundation
- **Context-Aware Recommendations** — Workouts adapt to your energy, stress, and schedule

### The Brick Philosophy

Missing a day isn't failure—it's a gap to repair. Some days you lay more bricks, some days fewer, but the foundation keeps growing.

---

## Features

### Current (MVP)

- **Landing Page** — Animated onboarding with B3 branding
- **BRIX AI Coach** — Conversational AI powered by Ollama/Llama 3.2
- **3D Brick Wall Background** — Custom visual throughout app
- **Brick Calendar** — 30-day progress visualization
- **Workout Library** — Guided workouts with exercise images
- **Daily Check-ins** — Energy, stress, mood logging
- **Streak Tracking** — Consecutive day counters
- **Milestone Achievements** — 7-day, 30-day streaks and more
- **Adaptive Coaching Tones** — Encouraging, Challenging, Empathetic, Celebratory
- **Profile & Settings** — User preferences and support

### Coming Soon

- Push notifications
- Offline workout support
- Custom workout builder
- Progress photos
- Social features

---

## Tech Stack

| Layer | Technology |
|-------|------------|
| **Backend** | Java 21, Spring Boot 3.4.1, Spring Data JPA |
| **Database** | SQLite (embedded, file-based) |
| **Frontend** | React Native 0.81.5, Expo 54, NativeWind 4.x |
| **AI** | Ollama + Llama 3.2 (local, free) |
| **Optional AI** | Claude API (Anthropic) as fallback |

### Why This Stack?

- **Spring Boot**: Industry-standard, robust REST APIs, excellent JPA support
- **React Native + Expo**: Single codebase for iOS/Android, rapid development
- **SQLite**: Zero-config embedded database, perfect for single-user MVP
- **Ollama**: Free local AI, no API costs, privacy-friendly

---

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    React Native App                      │
│  (Expo 54 + NativeWind + React Navigation)              │
└─────────────────────────┬───────────────────────────────┘
                          │ REST API
                          ▼
┌─────────────────────────────────────────────────────────┐
│                  Spring Boot Backend                     │
│  Controller → Service → Repository                       │
│                    │                                     │
│         ┌─────────┴─────────┐                           │
│         ▼                   ▼                           │
│     SQLite DB          Ollama API                       │
│    (b3.db file)       (Llama 3.2)                       │
└─────────────────────────────────────────────────────────┘
```

### Project Structure

```
B3-Passion-Project/
├── backend/                    # Spring Boot API
│   ├── src/main/java/com/b3/
│   │   ├── controller/         # REST endpoints
│   │   ├── service/            # Business logic + BRIX
│   │   ├── repository/         # Data access
│   │   ├── model/              # JPA entities
│   │   └── dto/                # Request/Response DTOs
│   └── b3.db                   # SQLite database
│
├── mobile/                     # React Native app
│   ├── src/
│   │   ├── screens/            # App screens
│   │   ├── components/         # Reusable components
│   │   ├── services/           # API client
│   │   ├── navigation/         # React Navigation
│   │   └── constants/          # Theme, colors
│   └── App.js                  # Entry point
│
├── docs/                       # Documentation
│   ├── B3-One-pager-v2.md
│   ├── specification-sheet-v2.md
│   ├── data-model/
│   └── ui-ux-mockup/
│
└── CLAUDE.md                   # AI assistant instructions
```

---

## Screenshots

| Landing | Home | Workouts |
|---------|------|----------|
| Animated entry with B3 logo and features | Streak card, daily recommendation | Workout library with exercises |

| Progress | BRIX Chat | Profile |
|----------|-----------|---------|
| 3D brick calendar visualization | AI-powered coaching conversation | Settings and personal records |

*The app features a custom 3D brick wall background throughout all screens.*

---

## Documentation

| Document | Description |
|----------|-------------|
| [One-Pager v2](docs/B3-One-pager-v2.md) | Quick project overview |
| [Specification v2](docs/specification-sheet-v2.md) | Detailed requirements |
| [Data Model v3](docs/data-model/data-model-v3.md) | Database schema |
| [Project Overview](docs/PROJECT_OVERVIEW.md) | Comprehensive guide |
| [CLAUDE.md](CLAUDE.md) | AI assistant context |

---

## API Reference

**Base URL:** `http://localhost:8080/api/v1`

### Key Endpoints

```http
# Profile
GET    /profile/{id}

# Workouts
GET    /workouts
GET    /workouts/{id}/exercises

# Sessions
POST   /sessions
PUT    /sessions/{id}/complete

# Progress
GET    /bricks/calendar/{profileId}
GET    /bricks/stats/{profileId}

# BRIX AI
POST   /brix/chat
GET    /brix/recommendation

# Daily Logs
POST   /daily-logs
GET    /daily-logs/today/{profileId}
```

See full API documentation in the [specification sheet](docs/specification-sheet-v2.md).

---

## Development

### Backend Commands

```bash
cd backend
mvn clean install          # Build
mvn spring-boot:run        # Run server (port 8080)
mvn test                   # Run tests
```

### Frontend Commands

```bash
cd mobile
npm install                # Install dependencies
npx expo start             # Start dev server
npx expo start --ios       # iOS simulator
npx expo start --android   # Android emulator
```

### AI Setup (Ollama)

```bash
# Install from https://ollama.ai
ollama pull llama3.2       # Download model
ollama serve               # Start server (port 11434)
```

### Environment Variables

Create `backend/.env` for optional Claude API fallback:
```
ANTHROPIC_API_KEY=your-key-here
```

### Database Reset

Delete `backend/b3.db` to regenerate with fresh demo data.

---

## Design System

### Colors

```
Background:  #09090b (zinc-950)
Cards:       #18181b (zinc-900)
Primary:     #f97316 (orange-500)
Accent:      #f59e0b (amber-500)
Focus:       #3b82f6 (blue-500)
```

### Philosophy

**Industrial Warmth**: Dark zinc foundation representing stability, contrasted with orange/amber gradients symbolizing motivational fire. Every visual element reinforces the construction metaphor—completed workouts as solid bricks, missed days as gaps to repair.

---

## Success Metrics

| Metric | B3 Target | Industry Average |
|--------|-----------|------------------|
| 90-day Adherence | 60% | 10-15% |
| 30-day Retention | 50% | 15-20% |
| Comeback Rate | 70% | 20-30% |

---

## Author

**Ben Rickert**
- GitHub: [@b-rickert](https://github.com/b-rickert)
- LinkedIn: [Ben Rickert](https://www.linkedin.com/in/ben-rickert)
- Bootcamp: Zip Code Wilmington

---

## License

This project was developed as a bootcamp capstone project.

---

**Tagline:** "Build Yourself. Brick by Brick."

**Core Message:** Fitness isn't about perfection. It's about showing up, laying one brick at a time, and building a foundation strong enough to support the life you want.
