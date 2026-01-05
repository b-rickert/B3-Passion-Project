package com.b3.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Brick entity - represents a completed workout day
 * 
 * The brick metaphor: each workout completion is a brick
 * in building your fitness wall.
 */
@Entity
@Table(name = "brick", indexes = {
    @Index(name = "idx_brick_date", columnList = "brick_date"),
    @Index(name = "idx_brick_profile", columnList = "profile_id")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Brick {

    // ========================================================================
    // ENUMS
    // ========================================================================

    public enum BrickType {
        WORKOUT,
        STREAK_BONUS,
        MILESTONE
    }

    public enum BrickStatus {
        ACTIVE,
        ARCHIVED
    }

    // ========================================================================
    // FIELDS
    // ========================================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brick_id")
    private Long brickId;

    // ========================================================================
    // RELATIONSHIPS
    // ========================================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private UserProfile userProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private WorkoutSession workoutSession;

    // ========================================================================
    // BRICK DATA
    // ========================================================================

    @NotNull
    @Column(name = "brick_date", nullable = false)
    private LocalDate brickDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "brick_type", nullable = false, length = 20)
    private BrickType brickType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "brick_status", nullable = false, length = 20)
    private BrickStatus brickStatus;

    @Column(name = "brick_color", length = 20)
    private String brickColor;

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public Brick() {}

    public Brick(UserProfile userProfile, WorkoutSession workoutSession, 
                 LocalDate brickDate, BrickType brickType) {
        this.userProfile = userProfile;
        this.workoutSession = workoutSession;
        this.brickDate = brickDate;
        this.brickType = brickType;
        this.brickStatus = BrickStatus.ACTIVE;
        this.brickColor = generateBrickColor();
    }

    // ========================================================================
    // BUSINESS LOGIC
    // ========================================================================

    /**
     * Archive this brick (soft delete)
     */
    public void archive() {
        this.brickStatus = BrickStatus.ARCHIVED;
    }

    /**
     * Check if brick is archived
     */
    public boolean isArchived() {
        return brickStatus == BrickStatus.ARCHIVED;
    }

    /**
     * Check if brick is from current month
     */
    public boolean isFromThisMonth() {
        if (brickDate == null) return false;
        LocalDate today = LocalDate.now();
        return brickDate.getYear() == today.getYear() && 
               brickDate.getMonth() == today.getMonth();
    }

    /**
     * Check if this brick is the first of the month
     */
    public boolean isFirstOfMonth() {
        return brickDate != null && brickDate.getDayOfMonth() == 1;
    }

    /**
     * Generate brick color based on type
     */
    private String generateBrickColor() {
        switch (brickType) {
            case WORKOUT:
                return "#E67E22"; // Orange
            case STREAK_BONUS:
                return "#F39C12"; // Gold
            case MILESTONE:
                return "#9B59B6"; // Purple
            default:
                return "#95A5A6"; // Gray
        }
    }

    // ========================================================================
    // GETTERS AND SETTERS
    // ========================================================================

    public Long getBrickId() {
        return brickId;
    }

    public void setBrickId(Long brickId) {
        this.brickId = brickId;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public WorkoutSession getWorkoutSession() {
        return workoutSession;
    }

    public void setWorkoutSession(WorkoutSession workoutSession) {
        this.workoutSession = workoutSession;
    }

    public LocalDate getBrickDate() {
        return brickDate;
    }

    public void setBrickDate(LocalDate brickDate) {
        this.brickDate = brickDate;
    }

    public BrickType getBrickType() {
        return brickType;
    }

    public void setBrickType(BrickType brickType) {
        this.brickType = brickType;
    }

    public BrickStatus getBrickStatus() {
        return brickStatus;
    }

    public void setBrickStatus(BrickStatus brickStatus) {
        this.brickStatus = brickStatus;
    }

    public String getBrickColor() {
        return brickColor;
    }

    public void setBrickColor(String brickColor) {
        this.brickColor = brickColor;
    }

    /**
     * Check if brick is a workout brick
     */
    public boolean isWorkoutBrick() {
        return brickType == BrickType.WORKOUT;
    }

    /**
     * Check if brick is a milestone brick
     */
    public boolean isMilestoneBrick() {
        return brickType == BrickType.MILESTONE;
    }

    /**
     * Check if brick is from today
     */
    public boolean isFromToday() {
        return brickDate != null && brickDate.equals(LocalDate.now());
    }

    /**
     * Check if brick is from this week
     */
    public boolean isFromThisWeek() {
        if (brickDate == null) return false;
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate weekEnd = weekStart.plusDays(6);
        return !brickDate.isBefore(weekStart) && !brickDate.isAfter(weekEnd);
    }

    /**
     * Get how many days ago this brick was created
     */
    public long getDaysAgo() {
        if (brickDate == null) return 0;
        return java.time.temporal.ChronoUnit.DAYS.between(brickDate, LocalDate.now());
    }

    /**
     * Get the workout name from the session
     */
    public String getWorkoutName() {
        if (workoutSession != null && workoutSession.getWorkout() != null) {
            String name = workoutSession.getWorkout().getName();
            return name != null ? name : "Unknown";
        }
        return "Unknown";
    }


    // ========================================================================
    // OBJECT OVERRIDES
    // ========================================================================

    @Override
    public String toString() {
        return "Brick{" +
                "brickId=" + brickId +
                ", brickDate=" + brickDate +
                ", brickType=" + brickType +
                ", brickStatus=" + brickStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Brick)) return false;
        Brick brick = (Brick) o;
        return Objects.equals(brickId, brick.brickId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brickId);
    }
}