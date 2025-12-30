package com.b3.dto;

import jakarta.validation.constraints.*;

/**
 * DTO for starting a workout session
 * Used when user begins a workout (POST /sessions/start)
 */
public class StartWorkoutDTO {

    @NotNull(message = "User profile ID is required")
    private Long profileId;

    @NotNull(message = "Workout ID is required")
    private Long workoutId;

    private String notes; // Optional pre-workout notes

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public StartWorkoutDTO() {}

    public StartWorkoutDTO(Long profileId, Long workoutId, String notes) {
        this.profileId = profileId;
        this.workoutId = workoutId;
        this.notes = notes;
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

    public Long getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(Long workoutId) {
        this.workoutId = workoutId;
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
        return "StartWorkoutDTO{" +
                "profileId=" + profileId +
                ", workoutId=" + workoutId +
                '}';
    }
}