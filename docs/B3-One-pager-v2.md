# B3 (Brick by Brick) - One Pager v2

**Build Yourself. Brick by Brick.**

*Updated: January 2025*

## What It Is

B3 is a mobile fitness app that helps users build lasting habits through small, consistent actions. Every workout—whether 3 minutes or 30—becomes a "brick" in your visual foundation. At the heart of B3 is **BRIX**, an AI coach powered by Llama 3.2 that learns your patterns and adapts its tone to keep you moving forward.

## The Problem

90% of fitness app users quit within 60 days. Apps focus on features—workout libraries, calorie counters, streak trackers—but ignore the real issue: **motivation fluctuates, life happens, and one-size-fits-all plans don't work.**

## The Solution

B3 transforms adherence through three core innovations:

**1. Adaptive Behavioral Coaching**
BRIX observes your patterns (workout times, energy levels, consistency) and automatically adjusts its tone—supportive during low motivation, challenging during streaks, gentle after breaks.

**2. Momentum-Based Progression**
Every workout is a "brick." Missing a day isn't failure—it's just a gap in your wall that BRIX helps you fill. This reframes fitness from perfectionism to construction work: some days you lay more bricks, some days fewer, but the foundation keeps growing.

**3. Context-Aware Recommendations**
Low energy? BRIX suggests a 5-minute win instead of your usual 20-minute session. Returning after travel? It shifts to rebuild mode with gentler workouts. The app understands that life happens.

## User Experience

BRIX feels like a personal coach who genuinely knows you. It remembers milestones ("5th time completing this routine—consistency matters"), recognizes patterns ("You work out best in the mornings—want a 7 AM reminder?"), and celebrates wins ("Seven days straight! Your foundation is getting stronger").

The visual brick wall—featuring a **3D depth effect with industrial aesthetic**—provides tangible evidence of progress, transforming abstract fitness goals into concrete construction work.

## Target Users

- Frequent restarters who struggle with long-term consistency
- Beginners seeking guidance without overwhelming intensity
- Anyone juggling work, family, and stress who needs fitness to adapt to real life

## Current Features (MVP)

- **Landing page** with animated onboarding experience
- **BRIX AI coaching** powered by Ollama/Llama 3.2 (free, runs locally)
- **3D brick wall background** throughout the app
- **Visual 30-day brick calendar** showing workout progress
- **Adaptive coaching tone** (encouraging, challenging, empathetic, celebratory)
- **Workout library** with exercise images and guided execution
- **Daily energy/mood/stress logging** for context-aware recommendations
- **Streak tracking** and milestone achievements
- **Profile settings** and help/support screens
- **Personal records tracking**

## Tech Stack

**Backend:** Java 21, Spring Boot 3.4.1, SQLite, RESTful API
**Frontend:** React Native 0.81.5 + Expo 54, NativeWind (Tailwind utilities)
**AI:** Ollama with Llama 3.2 (free local AI) — Optional Claude API fallback
**Architecture:** Three-tier (Controller → Service → Repository)

## Design Philosophy

Industrial warmth aesthetic—dark zinc foundation colors representing structural stability, contrasted with orange/amber gradients symbolizing motivational fire. The app features a **custom 3D brick wall background** with depth effects that reinforces the construction metaphor on every screen.

## Success Metrics

- **60% adherence** (3+ workouts/week after 90 days) vs. 10-15% industry average
- **50% retention** (active after 30 days) vs. 15-20% industry average
- **70% comeback rate** (returning after breaks) vs. 20-30% industry average

## Why It Matters

Unlike generic fitness apps that compete on content, B3 competes on **behavioral transformation**. BRIX forms relationships, not just recommendations. The brick metaphor creates tangible progress that abstract numbers cannot match. Users stay because they're building something visible.

---

**Tagline:** "Build Yourself. Brick by Brick."

**Core Message:** Fitness isn't about perfection. It's about showing up, laying one brick at a time, and building a foundation strong enough to support the life you want.
