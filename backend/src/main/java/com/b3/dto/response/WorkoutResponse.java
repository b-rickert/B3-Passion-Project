package com.b3.dto.response;

/**
 * Response DTO for workout
 */
public class WorkoutResponse {
    
    private Long workoutId;
    private String name;
    private String description;
    private String workoutType;
    private String difficultyLevel;
    private Integer estimatedDuration;
    private String requiredEquipment;
    
    // Getters and Setters
    public Long getWorkoutId() {
        return workoutId;
    }
    
    public void setWorkoutId(Long workoutId) {
        this.workoutId = workoutId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getWorkoutType() {
        return workoutType;
    }
    
    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }
    
    public String getDifficultyLevel() {
        return difficultyLevel;
    }
    
    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
    
    public Integer getEstimatedDuration() {
        return estimatedDuration;
    }
    
    public void setEstimatedDuration(Integer estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }
    
    public String getRequiredEquipment() {
        return requiredEquipment;
    }
    
    public void setRequiredEquipment(String requiredEquipment) {
        this.requiredEquipment = requiredEquipment;
    }
}