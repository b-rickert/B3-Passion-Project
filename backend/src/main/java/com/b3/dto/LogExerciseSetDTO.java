package com.b3.dto;

import jakarta.validation.constraints.*;

/**
 * DTO for logging an individual exercise set during workout
 * Used when user completes a set (POST /sessions/{id}/log-set)
 */
public class LogExerciseSetDTO {

    @NotNull(message = "Session ID is required")
    private Long sessionId;

    @NotNull(message = "Exercise ID is required")
    private Long exerciseId;

    @NotNull(message = "Set number is required")
    @Min(value = 1, message = "Set number must be at least 1")
    private Integer setNumber;

    @Min(value = 0, message = "Reps cannot be negative")
    private Integer reps; // Number of reps completed

    @Min(value = 0, message = "Weight cannot be negative")
    private Double weight; // Weight used (lbs or kg)

    @Min(value = 0, message = "Duration cannot be negative")
    private Integer durationSeconds; // For time-based exercises (plank, etc.)

    private String notes; // Optional notes for this set

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public LogExerciseSetDTO() {}

    public LogExerciseSetDTO(Long sessionId, Long exerciseId, Integer setNumber,
                            Integer reps, Double weight, Integer durationSeconds, String notes) {
        this.sessionId = sessionId;
        this.exerciseId = exerciseId;
        this.setNumber = setNumber;
        this.reps = reps;
        this.weight = weight;
        this.durationSeconds = durationSeconds;
        this.notes = notes;
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

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public Integer getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(Integer setNumber) {
        this.setNumber = setNumber;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // ========================================================================
    // TOSTRING
    // ========================================================================

    @Override
    public String toString() {
        return "LogExerciseSetDTO{" +
                "sessionId=" + sessionId +
                ", exerciseId=" + exerciseId +
                ", setNumber=" + setNumber +
                ", reps=" + reps +
                ", weight=" + weight +
                '}';
    }
}