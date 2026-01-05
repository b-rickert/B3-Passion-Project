package com.b3.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO for creating a new workout session
 */
public class WorkoutSessionCreateDTO {

    @NotNull(message = "Profile ID is required")
    private Long profileId;

    @NotNull(message = "Workout ID is required")
    private Long workoutId;

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public WorkoutSessionCreateDTO() {}

    public WorkoutSessionCreateDTO(Long profileId, Long workoutId) {
        this.profileId = profileId;
        this.workoutId = workoutId;
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
}