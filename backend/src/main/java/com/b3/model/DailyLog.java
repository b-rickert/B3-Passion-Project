package com.b3.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * DailyLog entity - user's daily check-in for energy, stress, sleep, and mood
 * 
 * Powers BRIX's adaptive recommendations by tracking daily wellness indicators.
 * One log per day maximum (enforced by unique constraint).
 */
@Entity
@Table(name = "daily_log",
       uniqueConstraints = @UniqueConstraint(columnNames = {"profile_id", "log_date"}),
       indexes = {
           @Index(name = "idx_dailylog_date", columnList = "log_date"),
           @Index(name = "idx_dailylog_profile", columnList = "profile_id")
       })
@JsonIgnoreProperties(ignoreUnknown = true)
public class DailyLog {

    // ========================================================================
    // ENUMS
    // ========================================================================

    public enum Mood {
        GREAT,
        GOOD,
        OKAY,
        LOW,
        STRESSED
    }

    // ========================================================================
    // FIELDS
    // ========================================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    // ========================================================================
    // RELATIONSHIPS
    // ========================================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private UserProfile userProfile;

    // ========================================================================
    // DAILY WELLNESS DATA
    // ========================================================================

    @NotNull
    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @NotNull
    @Min(1)
    @Max(5)
    @Column(name = "energy_level", nullable = false)
    private Integer energyLevel;

    @NotNull
    @Min(1)
    @Max(5)
    @Column(name = "stress_level", nullable = false)
    private Integer stressLevel;

    @NotNull
    @Min(1)
    @Max(5)
    @Column(name = "sleep_quality", nullable = false)
    private Integer sleepQuality;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "mood", nullable = false, length = 20)
    private Mood mood;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public DailyLog() {}

    public DailyLog(UserProfile userProfile, 
                    LocalDate logDate, 
                    Integer energyLevel, 
                    Integer stressLevel, 
                    Integer sleepQuality, 
                    Mood mood) {
        this.userProfile = userProfile;
        this.logDate = logDate;
        this.energyLevel = energyLevel;
        this.stressLevel = stressLevel;
        this.sleepQuality = sleepQuality;
        this.mood = mood;
    }

    // ========================================================================
    // JPA CALLBACKS
    // ========================================================================

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // ========================================================================
    // BUSINESS LOGIC
    // ========================================================================

    /**
     * Check if user has high energy (4-5)
     */
    public boolean isHighEnergy() {
        return energyLevel != null && energyLevel >= 4;
    }

    /**
     * Check if user has low energy (1-2)
     */
    public boolean isLowEnergy() {
        return energyLevel != null && energyLevel <= 2;
    }

    /**
     * Check if user is highly stressed (4-5)
     */
    public boolean isHighStress() {
        return stressLevel != null && stressLevel >= 4;
    }

    /**
     * Check if user had poor sleep (1-2)
     */
    public boolean isPoorSleep() {
        return sleepQuality != null && sleepQuality <= 2;
    }

    /**
     * Check if user had good sleep (4-5)
     */
    public boolean isGoodSleep() {
        return sleepQuality != null && sleepQuality >= 4;
    }

    /**
     * Check if user has positive mood (GREAT or GOOD)
     */
    public boolean isPositiveMood() {
        return mood == Mood.GREAT || mood == Mood.GOOD;
    }

    /**
     * Check if user has negative mood (LOW or STRESSED)
     */
    public boolean isNegativeMood() {
        return mood == Mood.LOW || mood == Mood.STRESSED;
    }

    /**
     * Check if user needs recovery day (multiple stress indicators)
     */
    public boolean needsRecovery() {
        int stressIndicators = 0;
        
        if (isLowEnergy()) stressIndicators++;
        if (isHighStress()) stressIndicators++;
        if (isPoorSleep()) stressIndicators++;
        
        return stressIndicators >= 2;
    }

    /**
     * Check if this is an optimal day for challenging workout
     */
    public boolean isOptimalWorkoutDay() {
        return isHighEnergy() && !isHighStress() && !isNegativeMood();
    }

    /**
     * Calculate overall wellness score (0.0 to 1.0)
     * Formula: (energy + (6-stress) + sleep) / 15
     * Note: Stress is inverted (1 stress = 5 points, 5 stress = 1 point)
     */
    public double getWellnessScore() {
        if (energyLevel == null || stressLevel == null || sleepQuality == null) {
            return 0.0;
        }
        
        int invertedStress = 6 - stressLevel; // Invert stress (lower is better)
        int totalScore = energyLevel + invertedStress + sleepQuality;
        
        return totalScore / 15.0; // Max possible: 15 (5+5+5)
    }

    // ========================================================================
    // GETTERS AND SETTERS
    // ========================================================================

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public LocalDate getLogDate() {
        return logDate;
    }

    public void setLogDate(LocalDate logDate) {
        this.logDate = logDate;
    }

    public Integer getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(Integer energyLevel) {
        this.energyLevel = energyLevel;
    }

    public Integer getStressLevel() {
        return stressLevel;
    }

    public void setStressLevel(Integer stressLevel) {
        this.stressLevel = stressLevel;
    }

    public Integer getSleepQuality() {
        return sleepQuality;
    }

    public void setSleepQuality(Integer sleepQuality) {
        this.sleepQuality = sleepQuality;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // ========================================================================
    // OBJECT OVERRIDES
    // ========================================================================

    @Override
    public String toString() {
        return "DailyLog{" +
                "logId=" + logId +
                ", logDate=" + logDate +
                ", energyLevel=" + energyLevel +
                ", stressLevel=" + stressLevel +
                ", sleepQuality=" + sleepQuality +
                ", mood=" + mood +
                ", wellnessScore=" + String.format("%.2f", getWellnessScore()) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DailyLog)) return false;
        DailyLog dailyLog = (DailyLog) o;
        return Objects.equals(logId, dailyLog.logId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logId);
    }
}