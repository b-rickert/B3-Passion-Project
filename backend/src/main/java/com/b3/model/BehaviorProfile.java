package com.b3.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * BehaviorProfile
 *
 * This is BRIX’s adaptive intelligence engine.
 * It models:
 * - Motivation
 * - Consistency
 * - Behavioral trends
 * - Momentum (upward, downward, neutral)
 * - Fatigue & energy patterns
 * - Coaching tone logic
 * - Workout streaks
 *
 * Each user has exactly one BehaviorProfile (1:1 with UserProfile).
 * BRIX reads this profile to personalize coaching messages and workout guidance.
 */
@Entity
@Table(name = "behavior_profile")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BehaviorProfile {

    // ========================================================================
    // ENUMS (Psychology + Coaching)
    // ========================================================================

    /**
     * Motivation State (high-level interpretation of user consistency).
     * BRIX uses this to determine coaching direction.
     */
    public enum MotivationState {
        MOTIVATED,      // Consistent, strong patterns
        NEUTRAL,        // Moderate performance
        STRUGGLING      // Low consistency or discouragement signals
    }

    /**
     * Momentum shows the direction the user is trending.
     * Unlike motivation (snapshot), momentum measures trajectory.
     */
    public enum MomentumTrend {
        RISING,         // Improving consistency and streak behavior
        FALLING,        // Losing rhythm, missing workouts
        STABLE          // Minimal movement either direction
    }

    /**
     * Coaching tone used by BRIX.
     * Adapted automatically based on motivation + momentum + fatigue.
     */
    public enum CoachingTone {
        ENCOURAGING,
        CHALLENGING,
        EMPATHETIC,
        CELEBRATORY
    }


    // ========================================================================
    // FIELDS (Database Columns)
    // ========================================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "behavior_id")
    private Long behaviorId;

    /**
     * Foreign-key to UserProfile (1:1 relationship).
     */
    @NotNull
    @Column(name = "profile_id", nullable = false, unique = true)
    private Long profileId;

    // ---------------- Core workout metrics ----------------

    @NotNull
    @Min(0)
    @Column(name = "consecutive_days", nullable = false)
    private Integer consecutiveDays;

    @NotNull
    @Min(0)
    @Column(name = "longest_streak", nullable = false)
    private Integer longestStreak;

    @NotNull
    @Min(0)
    @Column(name = "total_bricks_laid", nullable = false)
    private Integer totalBricksLaid;

    @NotNull
    @Min(0)
    @Max(1)
    @Column(name = "consistency_score", nullable = false)
    private Double consistencyScore;

    @Column(name = "last_workout_date")
    private LocalDate lastWorkoutDate;

    // ---------------- Psychological layer ----------------

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "motivation_state", nullable = false)
    private MotivationState motivationState;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "momentum_trend", nullable = false)
    private MomentumTrend momentumTrend;

    /**
     * Fatigue score (0.0 - 1.0)
     * 0.0 -> fully recovered
     * 1.0 -> highly fatigued
     */
    @NotNull
    @Min(0)
    @Max(1)
    @Column(name = "fatigue_score", nullable = false)
    private Double fatigueScore;

    /**
     * Energy trend (daily logs average):
     * Example scale: 1–10, normalized to 0.0–1.0
     */
    @NotNull
    @Min(0)
    @Max(1)
    @Column(name = "recent_energy_score", nullable = false)
    private Double recentEnergyScore;

    // ---------------- Coaching layer ----------------

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "current_tone", nullable = false)
    private CoachingTone currentTone;

    @Column(name = "last_tone_change")
    private LocalDateTime lastToneChange;

    // ---------------- Housekeeping ----------------

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public BehaviorProfile() {}

    public BehaviorProfile(Long profileId) {
        this.profileId = profileId;
        this.consecutiveDays = 0;
        this.longestStreak = 0;
        this.totalBricksLaid = 0;
        this.consistencyScore = 0.0;

        this.motivationState = MotivationState.NEUTRAL;
        this.momentumTrend = MomentumTrend.STABLE;

        this.fatigueScore = 0.0;
        this.recentEnergyScore = 0.5;

        this.currentTone = CoachingTone.ENCOURAGING;
        this.lastToneChange = LocalDateTime.now();
    }


    // ========================================================================
    // JPA CALLBACKS
    // ========================================================================

    @PrePersist
    protected void onCreate() {
        this.updatedAt = LocalDateTime.now();
        this.lastToneChange = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


    // ========================================================================
    // ADAPTIVE BUSINESS LOGIC
    // ========================================================================

    /**
     * Called when the user completes a workout.
     */
    public void logWorkout(LocalDate today, int daysSinceCreation) {
        updateStreak(today);
        incrementBricks();
        calculateConsistency(daysSinceCreation);
        updateMotivation();
        updateMomentum(today);
        adjustTone();
        this.lastWorkoutDate = today;
    }

    // ------------------- Streak Logic -------------------

    private void updateStreak(LocalDate today) {
        if (lastWorkoutDate == null) {
            consecutiveDays = 1;
        } else {
            long diff = ChronoUnit.DAYS.between(lastWorkoutDate, today);
            if (diff == 0) return;
            if (diff == 1) {
                consecutiveDays++;
            } else {
                consecutiveDays = 1;
            }
        }

        if (consecutiveDays > longestStreak) {
            longestStreak = consecutiveDays;
        }
    }

    private void incrementBricks() {
        totalBricksLaid++;
    }

    private void calculateConsistency(int daysSinceCreation) {
        if (daysSinceCreation <= 0) {
            consistencyScore = 0.0;
        } else {
            consistencyScore = (double) totalBricksLaid / daysSinceCreation;
            consistencyScore = Math.min(consistencyScore, 1.0);
        }
    }

    // ------------------- Motivation Logic -------------------

    private void updateMotivation() {
        if (consistencyScore >= 0.7) {
            motivationState = MotivationState.MOTIVATED;
        } else if (consistencyScore < 0.4) {
            motivationState = MotivationState.STRUGGLING;
        } else {
            motivationState = MotivationState.NEUTRAL;
        }
    }

    // ------------------- Momentum Logic -------------------

    private void updateMomentum(LocalDate today) {
        if (lastWorkoutDate == null) {
            momentumTrend = MomentumTrend.STABLE;
            return;
        }

        long gap = ChronoUnit.DAYS.between(lastWorkoutDate, today);

        if (gap == 1 && consecutiveDays >= 3) {
            momentumTrend = MomentumTrend.RISING;
        } else if (gap > 2) {
            momentumTrend = MomentumTrend.FALLING;
        } else {
            momentumTrend = MomentumTrend.STABLE;
        }
    }

    // ------------------- Tone Adaptation -------------------

    private void adjustTone() {
        CoachingTone oldTone = currentTone;

        if (motivationState == MotivationState.STRUGGLING || fatigueScore >= 0.7) {
            currentTone = CoachingTone.EMPATHETIC;
        } else if (consecutiveDays >= 7) {
            currentTone = CoachingTone.CHALLENGING;
        } else if (momentumTrend == MomentumTrend.RISING) {
            currentTone = CoachingTone.CELEBRATORY;
        } else {
            currentTone = CoachingTone.ENCOURAGING;
        }

        if (oldTone != currentTone) {
            lastToneChange = LocalDateTime.now();
        }
    }


    // ========================================================================
    // GETTERS + SETTERS (Omitted explanation for brevity)
    // ========================================================================

    // ... getters and setters identical to your existing style ...


    // ========================================================================
    // OBJECT OVERRIDES
    // ========================================================================

    @Override
    public String toString() {
        return "BehaviorProfile{" +
                "behaviorId=" + behaviorId +
                ", profileId=" + profileId +
                ", consecutiveDays=" + consecutiveDays +
                ", longestStreak=" + longestStreak +
                ", totalBricksLaid=" + totalBricksLaid +
                ", consistencyScore=" + consistencyScore +
                ", motivationState=" + motivationState +
                ", momentumTrend=" + momentumTrend +
                ", fatigueScore=" + fatigueScore +
                ", currentTone=" + currentTone +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BehaviorProfile)) return false;
        BehaviorProfile that = (BehaviorProfile) o;
        return Objects.equals(behaviorId, that.behaviorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(behaviorId);
    }
}
