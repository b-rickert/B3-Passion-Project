package com.b3.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Milestone entity - Tracks user achievements and goals in B3
 * 
 * Represents significant achievements like streaks, workout counts, goals achieved,
 * and personal records. Milestones trigger BRIX celebrations and provide motivation
 * through the "brick by brick" progress visualization.
 */
@Entity
@Table(name = "milestone")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Milestone {

    // ========================================================================
    // ENUMS
    // ========================================================================

    public enum MilestoneType {
        STREAK,          // 7-day, 30-day, 100-day streaks
        WORKOUT_COUNT,   // 10, 50, 100, 500 total workouts
        GOAL_ACHIEVED,   // User's personal goal met
        CONSISTENCY,     // 80% monthly consistency
        PERSONAL_RECORD  // New PR achieved
    }

    // ========================================================================
    // FIELDS
    // ========================================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "milestone_id")
    private Long milestoneId;

    // ========================================================================
    // RELATIONSHIPS
    // ========================================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private UserProfile userProfile;

    // ========================================================================
    // MILESTONE DATA
    // ========================================================================

    @NotBlank
    @Size(max = 100)
    @Column(name = "milestone_name", nullable = false, length = 100)
    private String milestoneName; // "7-Day Streak!" or "First 10 Workouts!"

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description; // "You worked out 7 days in a row!"

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "milestone_type", nullable = false, length = 20)
    private MilestoneType milestoneType;

    @NotNull
    @Min(1)
    @Column(name = "target_value", nullable = false)
    private Integer targetValue; // 7 for "7-day streak", 100 for "100 workouts"

    @NotNull
    @Min(0)
    @Column(name = "current_value", nullable = false)
    private Integer currentValue; // User's current progress (0 to targetValue)

    @NotNull
    @Column(name = "is_achieved", nullable = false)
    private Boolean isAchieved; // Has this milestone been completed?

    @Column(name = "achieved_at")
    private LocalDateTime achievedAt; // When milestone was completed

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public Milestone() {}

    public Milestone(UserProfile userProfile,
                     String milestoneName,
                     String description,
                     MilestoneType milestoneType,
                     Integer targetValue) {
        this.userProfile = userProfile;
        this.milestoneName = milestoneName;
        this.description = description;
        this.milestoneType = milestoneType;
        this.targetValue = targetValue;
        this.currentValue = 0;
        this.isAchieved = false;
    }

    // ========================================================================
    // JPA CALLBACKS
    // ========================================================================

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.currentValue == null) {
            this.currentValue = 0;
        }
        if (this.isAchieved == null) {
            this.isAchieved = false;
        }
    }

    // ========================================================================
    // BUSINESS LOGIC
    // ========================================================================

    /**
     * Check if milestone is completed
     */
    public boolean isCompleted() {
        return isAchieved != null && isAchieved;
    }

    /**
     * Check if milestone is in progress (started but not completed)
     */
    public boolean isInProgress() {
        return currentValue != null && currentValue > 0 && !isCompleted();
    }

    /**
     * Get progress percentage (0.0 to 1.0)
     */
    public double getProgressPercentage() {
        if (targetValue == null || targetValue == 0) {
            return 0.0;
        }
        if (currentValue == null) {
            return 0.0;
        }
        
        double progress = (double) currentValue / targetValue;
        return Math.min(progress, 1.0); // Cap at 100%
    }

    /**
     * Get progress as a percentage string (e.g., "75%")
     */
    public String getProgressPercentageString() {
        return String.format("%d%%", (int)(getProgressPercentage() * 100));
    }

    /**
     * Mark milestone as achieved
     */
    public void markAsAchieved() {
        this.isAchieved = true;
        this.achievedAt = LocalDateTime.now();
        this.currentValue = this.targetValue; // Set to 100%
    }

    /**
     * Update progress toward milestone
     */
    public void updateProgress(Integer newValue) {
        if (newValue == null || newValue < 0) {
            return;
        }
        
        this.currentValue = newValue;
        
        // Auto-achieve if target reached
        if (this.currentValue >= this.targetValue && !this.isAchieved) {
            markAsAchieved();
        }
    }

    /**
     * Increment progress by 1
     */
    public void incrementProgress() {
        updateProgress(this.currentValue + 1);
    }

    /**
     * Check if milestone is almost complete (>= 80% progress)
     */
    public boolean isAlmostComplete() {
        return getProgressPercentage() >= 0.8 && !isCompleted();
    }

    /**
     * Get remaining value to complete milestone
     */
    public Integer getRemainingValue() {
        if (currentValue == null || targetValue == null) {
            return targetValue;
        }
        return Math.max(0, targetValue - currentValue);
    }

    // ========================================================================
    // GETTERS AND SETTERS
    // ========================================================================

    public Long getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(Long milestoneId) {
        this.milestoneId = milestoneId;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MilestoneType getMilestoneType() {
        return milestoneType;
    }

    public void setMilestoneType(MilestoneType milestoneType) {
        this.milestoneType = milestoneType;
    }

    public Integer getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Integer targetValue) {
        this.targetValue = targetValue;
    }

    public Integer getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Integer currentValue) {
        this.currentValue = currentValue;
    }

    public Boolean getIsAchieved() {
        return isAchieved;
    }

    public void setIsAchieved(Boolean isAchieved) {
        this.isAchieved = isAchieved;
    }

    public LocalDateTime getAchievedAt() {
        return achievedAt;
    }

    public void setAchievedAt(LocalDateTime achievedAt) {
        this.achievedAt = achievedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // ========================================================================
    // OBJECT OVERRIDES
    // ========================================================================

    @Override
    public String toString() {
        return "Milestone{" +
                "milestoneId=" + milestoneId +
                ", milestoneName='" + milestoneName + '\'' +
                ", milestoneType=" + milestoneType +
                ", progress=" + currentValue + "/" + targetValue +
                ", isAchieved=" + isAchieved +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Milestone)) return false;
        Milestone milestone = (Milestone) o;
        return Objects.equals(milestoneId, milestone.milestoneId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(milestoneId);
    }
}