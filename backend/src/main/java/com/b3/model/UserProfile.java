package com.b3.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * UserProfile entity
 * 
 * Represents the single-user profile for the B3 MVP (no authentication).
 * Stores demographic data, goals, equipment, and timestamps.
 */
@Entity
@Table(name = "user_profile")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfile {

    // ========================================================================
    // ENUMS
    // ========================================================================

    public enum FitnessLevel {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED
    }

    public enum PrimaryGoal {
        STRENGTH,
        CARDIO,
        FLEXIBILITY,
        WEIGHT_LOSS
    }

    // ========================================================================
    // FIELDS
    // ========================================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(name = "display_name", nullable = false, length = 50)
    private String displayName;

    @NotNull
    @Min(13)
    @Max(120)
    @Column(nullable = false)
    private Integer age;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "fitness_level", nullable = false, length = 20)
    private FitnessLevel fitnessLevel;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "primary_goal", nullable = false, length = 30)
    private PrimaryGoal primaryGoal;

    @Column(name = "equipment", length = 200)
    private String equipment;

    @NotNull
    @Min(1)
    @Max(7)
    @Column(name = "weekly_goal_days", nullable = false)
    private Integer weeklyGoalDays;

    @Column(name = "current_streak")
    private Integer currentStreak = 0;

    @Column(name = "longest_streak")
    private Integer longestStreak = 0;

    @Column(name = "total_workouts")
    private Integer totalWorkouts = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ========================================================================
    // RELATIONSHIPS
    // ========================================================================

    /**
     * One-to-one bidirectional relationship with BehaviorProfile
     * Cascade ALL: When UserProfile is deleted, BehaviorProfile is also deleted
     * orphanRemoval: If relationship is broken, BehaviorProfile is deleted
     */
    @OneToOne(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private BehaviorProfile behaviorProfile;

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public UserProfile() {}

    public UserProfile(String displayName,
                       Integer age,
                       FitnessLevel fitnessLevel,
                       PrimaryGoal primaryGoal,
                       String equipment,
                       Integer weeklyGoalDays) {
        this.displayName = displayName;
        this.age = age;
        this.fitnessLevel = fitnessLevel;
        this.primaryGoal = primaryGoal;
        this.equipment = equipment;
        this.weeklyGoalDays = weeklyGoalDays;
    }

    // ========================================================================
    // JPA CALLBACKS
    // ========================================================================

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ========================================================================
    // BUSINESS LOGIC
    // ========================================================================

    public boolean isBeginner() {
        return this.fitnessLevel == FitnessLevel.BEGINNER;
    }

    public boolean hasEquipment(String equipmentName) {
        if (equipment == null || equipment.isBlank()) return false;
        return equipment.toLowerCase().contains(equipmentName.toLowerCase());
    }

    public void updateProfile(String displayName,
                              Integer age,
                              FitnessLevel fitnessLevel,
                              PrimaryGoal primaryGoal,
                              String equipment,
                              Integer weeklyGoalDays) {
        if (displayName != null) this.displayName = displayName;
        if (age != null) this.age = age;
        if (fitnessLevel != null) this.fitnessLevel = fitnessLevel;
        if (primaryGoal != null) this.primaryGoal = primaryGoal;
        if (equipment != null) this.equipment = equipment;
        if (weeklyGoalDays != null) this.weeklyGoalDays = weeklyGoalDays;
    }

    /**
     * Increment total workouts count
     */
    public void incrementTotalWorkouts() {
        if (this.totalWorkouts == null) {
            this.totalWorkouts = 0;
        }
        this.totalWorkouts++;
    }

    /**
     * Update current streak and check if new longest streak
     */
    public void updateStreak(Integer newStreak) {
        this.currentStreak = newStreak;
        if (this.longestStreak == null || newStreak > this.longestStreak) {
            this.longestStreak = newStreak;
        }
    }

    /**
     * Reset current streak to 0
     */
    public void resetStreak() {
        this.currentStreak = 0;
    }

    // ========================================================================
    // GETTERS AND SETTERS
    // ========================================================================

    public Long getProfileId() { 
        return profileId; 
    }
    
    public void setProfileId(Long profileId) { 
        this.profileId = profileId; 
    }

    public String getDisplayName() { 
        return displayName; 
    }
    
    public void setDisplayName(String displayName) { 
        this.displayName = displayName; 
    }

    public Integer getAge() { 
        return age; 
    }
    
    public void setAge(Integer age) { 
        this.age = age; 
    }

    public FitnessLevel getFitnessLevel() { 
        return fitnessLevel; 
    }
    
    public void setFitnessLevel(FitnessLevel fitnessLevel) { 
        this.fitnessLevel = fitnessLevel; 
    }

    public PrimaryGoal getPrimaryGoal() { 
        return primaryGoal; 
    }
    
    public void setPrimaryGoal(PrimaryGoal primaryGoal) { 
        this.primaryGoal = primaryGoal; 
    }

    public String getEquipment() { 
        return equipment; 
    }
    
    public void setEquipment(String equipment) { 
        this.equipment = equipment; 
    }

    public Integer getWeeklyGoalDays() { 
        return weeklyGoalDays; 
    }
    
    public void setWeeklyGoalDays(Integer weeklyGoalDays) { 
        this.weeklyGoalDays = weeklyGoalDays; 
    }

    public Integer getCurrentStreak() {
    return currentStreak;
    }

    public void setCurrentStreak(Integer currentStreak) {
        this.currentStreak = currentStreak;
    }

    public Integer getLongestStreak() {
        return longestStreak;
    }

    public void setLongestStreak(Integer longestStreak) {
        this.longestStreak = longestStreak;
    }

    public Integer getTotalWorkouts() {
        return totalWorkouts;
    }

    public void setTotalWorkouts(Integer totalWorkouts) {
        this.totalWorkouts = totalWorkouts;
    }

    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }

    public LocalDateTime getUpdatedAt() { 
        return updatedAt; 
    }

    public BehaviorProfile getBehaviorProfile() { 
        return behaviorProfile; 
    }
    
    public void setBehaviorProfile(BehaviorProfile behaviorProfile) { 
        this.behaviorProfile = behaviorProfile; 
    }

    // ========================================================================
    // OBJECT OVERRIDES
    // ========================================================================

    @Override
    public String toString() {
        return "UserProfile{" +
                "profileId=" + profileId +
                ", displayName='" + displayName + '\'' +
                ", age=" + age +
                ", fitnessLevel=" + fitnessLevel +
                ", primaryGoal=" + primaryGoal +
                ", weeklyGoalDays=" + weeklyGoalDays +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserProfile)) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(profileId, that.profileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileId);
    }
}