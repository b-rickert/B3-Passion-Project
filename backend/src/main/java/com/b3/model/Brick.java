package com.b3.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;

/**
 * Brick entity - visual progress tracking element
 * 
 * Represents a single brick in the brick wall calendar.
 * One brick is created for each completed workout.
 * Milestone bricks can also be created for achievements.
 */
@Entity
@Table(name = "brick")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Brick {

    // ========================================================================
    // ENUMS
    // ========================================================================

    public enum BrickType {
        WORKOUT,
        MILESTONE
    }

    public enum BrickStatus {
        LAID,       // Workout completed
        MISSED,     // No workout (unplanned)
        RECOVERY    // Planned rest day
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

    @OneToOne(fetch = FetchType.LAZY)
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private BrickStatus status;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public Brick() {}

    public Brick(UserProfile userProfile, WorkoutSession workoutSession, LocalDate brickDate, BrickType brickType) {
        this.userProfile = userProfile;
        this.workoutSession = workoutSession;
        this.brickDate = brickDate;
        this.brickType = brickType;
    }

    public Brick(UserProfile userProfile, LocalDate brickDate, BrickStatus status) {
        this.userProfile = userProfile;
        this.brickDate = brickDate;
        this.status = status;
        this.brickType = BrickType.WORKOUT;
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
     * Check if this is a workout brick
     */
    public boolean isWorkoutBrick() {
        return brickType == BrickType.WORKOUT;
    }

    /**
     * Check if this is a milestone brick
     */
    public boolean isMilestoneBrick() {
        return brickType == BrickType.MILESTONE;
    }

    /**
     * Check if brick represents a completed workout
     */
    public boolean isLaid() {
        return status == BrickStatus.LAID;
    }

    /**
     * Check if brick represents a missed workout
     */
    public boolean isMissed() {
        return status == BrickStatus.MISSED;
    }

    /**
     * Check if brick represents a recovery day
     */
    public boolean isRecovery() {
        return status == BrickStatus.RECOVERY;
    }

    /**
     * Check if brick is from today
     */
    public boolean isFromToday() {
        return brickDate != null && brickDate.equals(LocalDate.now());
    }

    /**
     * Check if brick is from current week
     */
    public boolean isFromThisWeek() {
        if (brickDate == null) return false;
        
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
        
        return !brickDate.isBefore(startOfWeek) && !brickDate.isAfter(endOfWeek);
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
     * Get number of days ago this brick was created
     */
    public long getDaysAgo() {
        if (brickDate == null) return 0;
        return ChronoUnit.DAYS.between(brickDate, LocalDate.now());
    }

    /**
     * Get workout name from associated session
     */
    public String getWorkoutName() {
        if (workoutSession != null && workoutSession.getWorkout() != null) {
            return workoutSession.getWorkout().getName();
        }
        return "Unknown";
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

    public BrickStatus getStatus() {
        return status;
    }

    public void setStatus(BrickStatus status) {
        this.status = status;
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
        return "Brick{" +
                "brickId=" + brickId +
                ", brickType=" + brickType +
                ", status=" + status +
                ", brickDate=" + brickDate +
                ", daysAgo=" + getDaysAgo() +
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