package com.b3.dto.request;

import jakarta.validation.constraints.NotNull;

public class WorkoutSessionCreateRequest {
    
    @NotNull(message = "Profile ID is required")
    private Long profileId;
    
    @NotNull(message = "Workout ID is required")
    private Long workoutId;
    
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