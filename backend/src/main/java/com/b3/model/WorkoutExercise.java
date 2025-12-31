package com.b3.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;

/**
 * WorkoutExercise entity - junction table for Workout and Exercise
 * 
 * Maps exercises to workouts with specific parameters like sets, reps, and order.
 * Enables Many-to-Many relationship between Workout and Exercise.
 */

@Entity
@Table(name = "workout_exercise")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkoutExercise {

    // ========================================================================
    // FIELDS
    // ========================================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workout_exercise_id")
    private Long workoutExerciseId;

    // ========================================================================
    // RELATIONSHIPS
    // ========================================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    // ========================================================================
    // EXERCISE PARAMETERS
    // ========================================================================

    @NotNull
    @Min(0)
    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @NotNull
    @Min(1)
    @Column(name = "sets", nullable = false)
    private Integer sets;

    @Min(0)
    @Column(name = "reps")
    private Integer reps;

    @Min(0)
    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Min(0)
    @Column(name = "rest_seconds")
    private Integer restSeconds;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public WorkoutExercise() {}

    public WorkoutExercise(Workout workout, 
                          Exercise exercise, 
                          Integer orderIndex, 
                          Integer sets, 
                          Integer reps, 
                          Integer durationSeconds) {
            this.workout = workout;
            this.exercise = exercise;
            this.orderIndex = orderIndex;
            this.sets = sets;
            this.reps = reps;
            this.durationSeconds = durationSeconds;
        }

    // ========================================================================
    // BUSINESS LOGIC
    // ========================================================================

    /**
     * Check if this is a rep-based exercise
     */

    public boolean isRepBased() {
        return reps != null && reps > 0;
    }

    /**
     * Check if this is a time-based exercise
     */

    public boolean isTimeBased() {
        return !isRepBased() && durationSeconds != null && durationSeconds > 0;
    }

    /**
     * Calculate total volume (total reps OR total seconds)
     */

    public int getTotalVolume() {
        if (sets == null || sets == 0) {
            return 0;
        }

        if (isRepBased()) {
            return sets * reps;
        } else if (isTimeBased()) {
            return sets * durationSeconds;
        }

        return 0;
    }

    /**
     * Estimate total time for this exercise in seconds
     * Assumes 4 seconds per rep for rep-based exercises
     */
    
    public int getEstimatedSeconds() {
        if (sets == null || sets == 0) {
            return 0;
        }

        if (isRepBased()) {
            return sets * reps * 4;  // 4 seconds per rep
        } else if (isTimeBased()) {
            return sets * durationSeconds;
        }

        return 0;
    }

    /**
     * Get formatted string representation of exercise parameters
     */

    public String getFormattedDuration() {
        if (isRepBased()) {
            return sets + " sets × " + reps + " reps";
        } else if (isTimeBased()) {
            return sets + " sets × " + durationSeconds + " sec";
        }

        return sets + " sets";
    }

    // ========================================================================
    // GETTERS AND SETTERS
    // ========================================================================

    public Long getWorkoutExerciseId() {
        return workoutExerciseId;
    }

    public void setWorkoutExerciseId(Long workoutExerciseId) {
        this.workoutExerciseId = workoutExerciseId;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Integer getSets() {
        return sets;
    }

    public void setSets(Integer sets) {
        this.sets = sets;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public Integer getRestSeconds() {
        return restSeconds;
    }

    public void setRestSeconds(Integer restSeconds) {
        this.restSeconds = restSeconds;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // ========================================================================
    // OBJECT OVERRIDES
    // ========================================================================

    @Override
    public String toString() {
        return "WorkoutExercise{" +
                "workoutExerciseId=" + workoutExerciseId +
                ", orderIndex=" + orderIndex +
                ", sets=" + sets +
                ", reps=" + reps +
                ", durationSeconds=" + durationSeconds +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkoutExercise)) return false;
        WorkoutExercise that = (WorkoutExercise) o;
        return Objects.equals(workoutExerciseId, that.workoutExerciseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workoutExerciseId);
    }
}