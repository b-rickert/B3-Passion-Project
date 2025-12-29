package com.b3.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Objects;

/**
 * WorkoutSession entity - records user's completed workout instances
 * 
 * Tracks when users perform workouts, including start/end times,
 * completion status, and perceived difficulty.
 */
@Entity
@Table(name = "workout_session")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkoutSession {

    // ========================================================================
    // ENUMS
    // ========================================================================

    public enum CompletionStatus {
        IN_PROGRESS,
        COMPLETED,
        PARTIAL,
        SKIPPED
    }

    // ========================================================================
    // FIELDS
    // ========================================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long sessionId;

    // ========================================================================
    // RELATIONSHIPS
    // ========================================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private UserProfile userProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    // ========================================================================
    // SESSION DATA
    // ========================================================================

    @NotNull
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "completion_status", nullable = false, length = 20)
    private CompletionStatus completionStatus;

    @Min(1)
    @Max(5)
    @Column(name = "perceived_difficulty")
    private Integer perceivedDifficulty;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public WorkoutSession() {}

    public WorkoutSession(UserProfile userProfile, Workout workout, LocalDateTime startTime) {
        this.userProfile = userProfile;
        this.workout = workout;
        this.startTime = startTime;
        this.completionStatus = CompletionStatus.IN_PROGRESS;
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
     * Mark session as completed with difficulty rating
     */
    public void completeSession(Integer perceivedDifficulty) {
        this.endTime = LocalDateTime.now();
        this.completionStatus = CompletionStatus.COMPLETED;
        this.perceivedDifficulty = perceivedDifficulty;
    }

    /**
     * Mark session as skipped
     */
    public void skipSession() {
        this.completionStatus = CompletionStatus.SKIPPED;
    }

    /**
     * Check if session was completed
     */
    public boolean isCompleted() {
        return completionStatus == CompletionStatus.COMPLETED;
    }

    /**
     * Check if session is currently in progress
     */
    public boolean isInProgress() {
        return completionStatus == CompletionStatus.IN_PROGRESS;
    }

    /**
     * Calculate workout duration in minutes
     */
    public long getDurationMinutes() {
        if (startTime == null || endTime == null) {
            return 0;
        }
        return Duration.between(startTime, endTime).toMinutes();
    }

    /**
     * Check if workout was harder than expected (difficulty > 3)
     */
    public boolean wasHarderThanExpected() {
        return perceivedDifficulty != null && perceivedDifficulty > 3;
    }

    /**
     * Check if workout was easier than expected (difficulty < 3)
     */
    public boolean wasEasierThanExpected() {
        return perceivedDifficulty != null && perceivedDifficulty < 3;
    }

    // ========================================================================
    // GETTERS AND SETTERS
    // ========================================================================

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public CompletionStatus getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(CompletionStatus completionStatus) {
        this.completionStatus = completionStatus;
    }

    public Integer getPerceivedDifficulty() {
        return perceivedDifficulty;
    }

    public void setPerceivedDifficulty(Integer perceivedDifficulty) {
        this.perceivedDifficulty = perceivedDifficulty;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // ========================================================================
    // OBJECT OVERRIDES
    // ========================================================================

    @Override
    public String toString() {
        return "WorkoutSession{" +
                "sessionId=" + sessionId +
                ", completionStatus=" + completionStatus +
                ", perceivedDifficulty=" + perceivedDifficulty +
                ", durationMinutes=" + getDurationMinutes() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkoutSession)) return false;
        WorkoutSession that = (WorkoutSession) o;
        return Objects.equals(sessionId, that.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId);
    }
}