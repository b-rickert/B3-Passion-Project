package com.b3.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * BehaviorProfile entity - BRIX's adaptive intelligence engine
 * 
 * Tracks user behavior patterns for adaptive coaching.
 * One-to-one relationship with UserProfile.
 */
@Entity
@Table(name = "behavior_profile")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BehaviorProfile {

    // ========================================================================
    // ENUMS
    // ========================================================================

    public enum MotivationState {
        MOTIVATED,
        NEUTRAL,
        STRUGGLING
    }

    public enum MomentumTrend {
        RISING,
        FALLING,
        STABLE
    }

    public enum CoachingTone {
        ENCOURAGING,
        CHALLENGING,
        EMPATHETIC,
        CELEBRATORY
    }

    // ========================================================================
    // FIELDS
    // ========================================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "behavior_id")
    private Long behaviorId;

    // ========================================================================
    // RELATIONSHIPS
    // ========================================================================

    /**
     * One-to-one relationship with UserProfile (owns the foreign key)
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false, unique = true)
    private UserProfile userProfile;

    // ========================================================================
    // BEHAVIOR METRICS
    // ========================================================================

    @Column(name = "preferred_workout_time", length = 20)
    private String preferredWorkoutTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "current_tone", nullable = false)
    private CoachingTone currentTone;

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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "motivation_state", nullable = false)
    private MotivationState motivationState;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "momentum_trend", nullable = false)
    private MomentumTrend momentumTrend;

    @NotNull
    @Min(0)
    @Max(1)
    @Column(name = "fatigue_score", nullable = false)
    private Double fatigueScore;

    @NotNull
    @Min(0)
    @Max(1)
    @Column(name = "recent_energy_score", nullable = false)
    private Double recentEnergyScore;

    @Column(name = "last_tone_change")
    private LocalDateTime lastToneChange;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "avg_workout_time_of_day", length = 20)
    private String avgWorkoutTimeOfDay;

    @Column(name = "preferred_workout_types", length = 100)
    private String preferredWorkoutTypes;

    @Column(name = "energy_pattern", length = 50)
    private String energyPattern;

    @Column(name = "stress_trend", length = 20)
    private String stressTrend;

    @Column(name = "avg_session_duration")
    private Integer avgSessionDuration;

    @Column(name = "skip_frequency")
    private Double skipFrequency;

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public BehaviorProfile() {}

    public BehaviorProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
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
        if (this.lastToneChange == null) {
            this.lastToneChange = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ========================================================================
    // BUSINESS LOGIC
    // ========================================================================

    public void logWorkout(LocalDate today, int daysSinceCreation) {
        updateStreak(today);
        incrementBricks();
        calculateConsistency(daysSinceCreation);
        updateMotivation();
        updateMomentum(today);
        adjustTone();
        this.lastWorkoutDate = today;
    }

    private void updateStreak(LocalDate today) {
        if (lastWorkoutDate == null) {
            consecutiveDays = 1;
        } else {
            long diff = ChronoUnit.DAYS.between(lastWorkoutDate, today);
            if (diff == 0) {
                return;
            }
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

    private void updateMotivation() {
        if (consistencyScore >= 0.7) {
            motivationState = MotivationState.MOTIVATED;
        } else if (consistencyScore < 0.4) {
            motivationState = MotivationState.STRUGGLING;
        } else {
            motivationState = MotivationState.NEUTRAL;
        }
    }

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
    // GETTERS AND SETTERS
    // ========================================================================

    public Long getBehaviorId() {
        return behaviorId;
    }

    public void setBehaviorId(Long behaviorId) {
        this.behaviorId = behaviorId;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public String getPreferredWorkoutTime() {
        return preferredWorkoutTime;
    }

    public void setPreferredWorkoutTime(String preferredWorkoutTime) {
        this.preferredWorkoutTime = preferredWorkoutTime;
    }

    public Integer getConsecutiveDays() {
        return consecutiveDays;
    }

    public void setConsecutiveDays(Integer consecutiveDays) {
        this.consecutiveDays = consecutiveDays;
    }

    public Integer getLongestStreak() {
        return longestStreak;
    }

    public void setLongestStreak(Integer longestStreak) {
        this.longestStreak = longestStreak;
    }

    public Integer getTotalBricksLaid() {
        return totalBricksLaid;
    }

    public void setTotalBricksLaid(Integer totalBricksLaid) {
        this.totalBricksLaid = totalBricksLaid;
    }

    public Double getConsistencyScore() {
        return consistencyScore;
    }

    public void setConsistencyScore(Double consistencyScore) {
        this.consistencyScore = consistencyScore;
    }

    public LocalDate getLastWorkoutDate() {
        return lastWorkoutDate;
    }

    public void setLastWorkoutDate(LocalDate lastWorkoutDate) {
        this.lastWorkoutDate = lastWorkoutDate;
    }

    public MotivationState getMotivationState() {
        return motivationState;
    }

    public void setMotivationState(MotivationState motivationState) {
        this.motivationState = motivationState;
    }

    public MomentumTrend getMomentumTrend() {
        return momentumTrend;
    }

    public void setMomentumTrend(MomentumTrend momentumTrend) {
        this.momentumTrend = momentumTrend;
    }

    public Double getFatigueScore() {
        return fatigueScore;
    }

    public void setFatigueScore(Double fatigueScore) {
        this.fatigueScore = fatigueScore;
    }

    public Double getRecentEnergyScore() {
        return recentEnergyScore;
    }

    public void setRecentEnergyScore(Double recentEnergyScore) {
        this.recentEnergyScore = recentEnergyScore;
    }

    public CoachingTone getCurrentTone() {
        return currentTone;
    }

    public void setCurrentTone(CoachingTone currentTone) {
        this.currentTone = currentTone;
    }

    public LocalDateTime getLastToneChange() {
        return lastToneChange;
    }

    public void setLastToneChange(LocalDateTime lastToneChange) {
        this.lastToneChange = lastToneChange;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAvgWorkoutTimeOfDay() {
        return avgWorkoutTimeOfDay;
    }

    public void setAvgWorkoutTimeOfDay(String avgWorkoutTimeOfDay) {
        this.avgWorkoutTimeOfDay = avgWorkoutTimeOfDay;
    }

    public String getPreferredWorkoutTypes() {
        return preferredWorkoutTypes;
    }

    public void setPreferredWorkoutTypes(String preferredWorkoutTypes) {
        this.preferredWorkoutTypes = preferredWorkoutTypes;
    }

    public String getEnergyPattern() {
        return energyPattern;
    }

    public void setEnergyPattern(String energyPattern) {
        this.energyPattern = energyPattern;
    }

    public String getStressTrend() {
        return stressTrend;
    }

    public void setStressTrend(String stressTrend) {
        this.stressTrend = stressTrend;
    }

    public Integer getAvgSessionDuration() {
        return avgSessionDuration;
    }

    public void setAvgSessionDuration(Integer avgSessionDuration) {
        this.avgSessionDuration = avgSessionDuration;
    }

    public Double getSkipFrequency() {
        return skipFrequency;
    }

    public void setSkipFrequency(Double skipFrequency) {
        this.skipFrequency = skipFrequency;
    }

    // ========================================================================
    // OBJECT OVERRIDES
    // ========================================================================

    @Override
    public String toString() {
        return "BehaviorProfile{" +
                "behaviorId=" + behaviorId +
                ", consecutiveDays=" + consecutiveDays +
                ", longestStreak=" + longestStreak +
                ", totalBricksLaid=" + totalBricksLaid +
                ", consistencyScore=" + consistencyScore +
                ", motivationState=" + motivationState +
                ", momentumTrend=" + momentumTrend +
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