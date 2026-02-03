# B3 Project Overview

**For Board Presentation | January 2026**

---

## Elevator Pitch (60 seconds)

*Use this to open conversations when people visit your table:*

---

"Hey, thanks for stopping by! So this is B3—Brick by Brick. It's a fitness app, but not like the ones you've probably tried and quit.

Here's the thing: 90% of people who download fitness apps quit within two months. Not because the apps are bad, but because life happens. You miss a day, feel guilty, and before you know it you're starting over from scratch for the fifth time this year.

B3 flips that script. Instead of counting calories or tracking PRs, we focus on one thing: showing up. Every workout you complete—whether it's 5 minutes or 50—becomes a brick in your foundation. You're literally building something you can see grow.

And the AI coach, BRIX, actually learns how you work. Stressed out? It suggests a quick stretch instead of pushing HIIT. On a roll with a seven-day streak? It'll challenge you to step it up. It adapts to *you*, not the other way around.

The whole philosophy is that missing a day isn't failure—it's just a gap in your wall that you can repair tomorrow. Some days you lay more bricks, some days fewer, but your foundation keeps growing.

I built this because I'm one of those chronic restarters. And I figured if I could solve this problem for myself, I could probably help a lot of other people too."

---

## Project Summary

| | |
|---|---|
| **Name** | B3 (Brick by Brick) |
| **Type** | Mobile Fitness Application |
| **Tagline** | Build Yourself. Brick by Brick. |
| **Core Feature** | Adaptive AI Coach (BRIX) |
| **Tech Stack** | Java/Spring Boot + React Native + Ollama AI |
| **Status** | MVP Complete |

---

## The Problem I'm Solving

Most fitness apps fail at **adherence**, not features.

- **90% of users quit within 60 days**
- Motivation naturally fluctuates
- Life interrupts (work, family, illness, travel)
- One missed day leads to guilt, which leads to quitting
- Generic plans don't account for how you're *actually* feeling

The industry focuses on content (more workouts! more features!) when the real problem is psychological: **people need adaptive support, not more options.**

---

## My Solution: Three Core Innovations

### 1. Adaptive Behavioral Coaching (BRIX)

BRIX isn't a chatbot with canned responses. It:

- **Learns your patterns** — When you work out, how often, what you prefer
- **Tracks your state** — Energy, stress, mood from daily check-ins
- **Adjusts its tone automatically:**
  - *Encouraging* when you're struggling
  - *Challenging* when you're on a streak
  - *Empathetic* after long breaks
  - *Celebratory* when you hit milestones

**Example:** If you log low energy and high stress, BRIX won't suggest a 30-minute HIIT session. It'll recommend a 5-minute stretch to keep your streak alive without burning you out.

### 2. The Brick System (Visual Progress)

Every completed workout becomes a **brick** in your visual foundation:

- 30-day brick wall calendar shows your progress
- Orange bricks = completed workouts
- Blue brick = today's focus
- Gray outlines = gaps to repair (not failures!)

**Why it works:** Abstract numbers and percentages don't motivate. Watching a wall physically build up does. You're constructing something tangible.

### 3. Momentum Over Perfection

The biggest innovation is psychological:

- **Missing a day ≠ failure** — It's a gap to fill tomorrow
- **Any workout counts** — 5 minutes is still a brick
- **Streaks preserve progress** — Your longest streak is always tracked
- **Comeback system** — BRIX welcomes you back, never shames you

**The philosophy:** Some days you lay more bricks, some days fewer, but the foundation keeps growing.

---

## Technical Implementation

### Architecture

```
┌─────────────────────────────────────────┐
│         React Native Mobile App          │
│     (Expo 54 + NativeWind + TypeScript)  │
└───────────────────┬─────────────────────┘
                    │ REST API
                    ▼
┌─────────────────────────────────────────┐
│          Spring Boot Backend             │
│   (Java 21 + Spring Data JPA + SQLite)   │
│                   │                      │
│        ┌─────────┴─────────┐            │
│        ▼                   ▼            │
│    SQLite DB          Ollama API        │
│   (Local File)       (Llama 3.2 AI)     │
└─────────────────────────────────────────┘
```

### Tech Stack Choices

| Choice | Why |
|--------|-----|
| **Java/Spring Boot** | Industry standard, robust APIs, demonstrates enterprise skills |
| **React Native/Expo** | Single codebase for iOS + Android, rapid development |
| **SQLite** | Zero configuration, embedded, perfect for single-user MVP |
| **Ollama/Llama 3.2** | Free local AI, no API costs, privacy-friendly |

### Key Features Implemented

- Landing page with animated onboarding
- Full workout library with guided execution
- 3D brick wall background (custom visual)
- BRIX AI chat interface
- Streak and milestone tracking
- Daily energy/mood/stress logging
- Profile settings and preferences

### Recent Improvements (January 2026)

| Feature | Description |
|---------|-------------|
| **Haptic Feedback** | Tactile feedback throughout the app - workout completion, set tracking, daily check-ins, navigation |
| **Confetti Celebration** | Celebratory confetti animation when completing a workout |
| **Exercise Display** | Color-coded muscle group icons for all 46 exercises |
| **ExerciseDB Integration** | API integration ready for animated exercise GIFs (optional enhancement) |

---

## App Walkthrough

### Screen Flow

```
Landing Page
    └─→ Home (Main dashboard)
            ├─→ Workouts (Browse library)
            │       └─→ Workout Detail (Exercise list)
            ├─→ Progress (Brick calendar)
            ├─→ BRIX (AI chat)
            └─→ Profile (Settings)
```

### What You'll See in the Demo

1. **Landing Page** — Animated entrance with floating bricks
2. **Home Screen** — Current streak, recommended workout, BRIX greeting
3. **Workout Selection** — Browse by category, see difficulty and duration
4. **Workout Execution** — Exercise list with images, timer, completion tracking
5. **Progress Calendar** — 30-day brick wall showing workout history
6. **BRIX Chat** — Real AI conversation that adapts to your state

---

## The BRIX Intelligence System

### How BRIX Learns

BRIX maintains a **Behavior Profile** for each user:

```
┌─────────────────────────────────────────┐
│           Behavior Profile               │
├─────────────────────────────────────────┤
│ Current Streak:        7 days           │
│ Longest Streak:        14 days          │
│ Total Bricks:          47               │
│ Consistency Score:     78%              │
│ Motivation State:      MOTIVATED        │
│ Momentum Trend:        RISING           │
│ Current Tone:          CHALLENGING      │
│ Last Workout:          Today            │
└─────────────────────────────────────────┘
```

### Coaching Tone Logic

| Situation | Tone | Example Message |
|-----------|------|-----------------|
| Low energy, high stress | EMPATHETIC | "Rough day? A 5-min stretch keeps the streak alive." |
| 7+ day streak, high energy | CHALLENGING | "Seven days strong. Ready to push harder?" |
| Returning after break | ENCOURAGING | "Welcome back. One brick at a time." |
| Milestone achieved | CELEBRATORY | "30-DAY STREAK! You're building something real." |

### AI Implementation

- **Primary:** Ollama running Llama 3.2 locally (free, private)
- **System Prompt:** BRIX personality and context injected
- **Fallback:** Keyword-based responses if AI unavailable
- **Optional:** Claude API as premium alternative

---

## Design Philosophy

### Visual Identity: "Industrial Warmth"

- **Dark zinc backgrounds** — Structural stability, foundation
- **Orange/amber accents** — Motivational fire, achievement
- **Blue highlights** — Focus, today's action
- **3D brick textures** — Reinforces the construction metaphor

### UX Principles

1. **Visual motivation over guilt** — No red X's for missed days
2. **Adaptive support over rigidity** — Workouts match your state
3. **Simplicity over complexity** — Self-explanatory interface
4. **Habit formation over metrics** — Consistency > performance

---

## Target Users

1. **Chronic restarters** — People who've started and stopped fitness many times
2. **Beginners** — Need guidance without overwhelming intensity
3. **Busy adults** — Work, family, stress—need fitness to adapt to life
4. **Anyone who's felt fitness app guilt** — The "I missed a day so why bother" crowd

---

## Success Metrics (Target vs Industry)

| Metric | B3 Target | Industry Average |
|--------|-----------|------------------|
| 90-day adherence (3+ workouts/week) | 60% | 10-15% |
| 30-day retention (still active) | 50% | 15-20% |
| Comeback rate (return after 7+ day break) | 70% | 20-30% |

---

## Why This Project Matters to Me

I built B3 because I've been that person who downloads a new fitness app every few months, gets excited, sticks with it for a few weeks, misses a couple days, and then abandons it entirely. The all-or-nothing mentality that most apps reinforce just doesn't work for real life.

I wanted to create something that understands that motivation isn't constant, that life gets in the way, and that the goal isn't perfection—it's persistence. If I can help even one person break the restart cycle and actually build lasting habits, this project is worth it.

Plus, it gave me a chance to build a full-stack application with real AI integration, which is exactly the kind of experience I wanted from this bootcamp.

---

## What I Learned Building This

### Technical Skills
- Full-stack development (backend API → mobile frontend)
- Spring Boot with JPA relationships
- React Native with Expo
- Local AI integration with Ollama
- REST API design
- Mobile UI/UX with NativeWind

### Soft Skills
- Product thinking (solving real problems, not just building features)
- Iterative development (started simple, added complexity)
- Documentation (you're reading it!)
- Demo presentation

---

## Future Roadmap

### Near Term
- Push notifications for reminders
- Offline workout support
- More workout variety

### Medium Term
- User authentication (multi-user)
- Cloud sync across devices
- Custom workout builder
- Social features (accountability groups)

### Long Term
- Wearable integration (Apple Watch, Fitbit)
- Voice-enabled BRIX
- Advanced analytics
- Premium content tiers

---

## Questions I'm Ready For

**"Why not just use MyFitnessPal/Nike Training Club/etc?"**
> Those apps compete on content—more workouts, more features. B3 competes on psychology. BRIX adapts to you, the brick system reframes failure as temporary gaps, and the whole philosophy is about momentum, not perfection.

**"How does the AI work?"**
> BRIX runs on Llama 3.2 through Ollama—it's completely local and free. The AI receives context about your workout history, energy levels, and current streak, then generates personalized responses. It's not canned messages; it actually understands your situation.

**"What was the hardest part to build?"**
> Honestly, getting the behavioral adaptation right. Making BRIX feel responsive and personal without being annoying or generic took a lot of iteration. The technical implementation was straightforward, but the *feel* of the coaching required tuning.

**"Could this scale to real users?"**
> Absolutely. The single-user SQLite setup is for demo purposes. Swapping to PostgreSQL with authentication is a known migration path, and the architecture already separates concerns properly. The AI can run in the cloud instead of locally for scale.

---

## Demo Tips

When showing the app:

1. **Start with the landing page** — The animation sets the tone
2. **Show the brick calendar** — Visual impact is immediate
3. **Chat with BRIX** — Let them ask a question and see the AI respond
4. **Explain the gap philosophy** — This is the key differentiator
5. **Let them play with it** — Hand over the device

---

**Created by Ben Rickert**
Zip Code Wilmington | January 2025

*"Build Yourself. Brick by Brick."*
