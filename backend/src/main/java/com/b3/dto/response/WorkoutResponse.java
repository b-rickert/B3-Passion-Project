package com.b3.dto.response;

import java.util.List;
import java.util.ArrayList;

/**
 * Response DTO for workout with exercises
 */
public class WorkoutResponse {
    
    private Long workoutId;
    private String name;
    private String description;
    private String workoutType;
    private String difficultyLevel;
    private Integer estimatedDuration;
    private String requiredEquipment;
    private List<ExerciseDetail> exercises = new ArrayList<>();
    
    // Nested class for exercise details
    public static class ExerciseDetail {
        private Long exerciseId;
        private String name;
        private String description;
        private String muscleGroup;
        private String equipmentType;
        private String videoUrl;
        private Integer sets;
        private Integer reps;
        private Integer durationSeconds;
        private Integer restSeconds;
        private Integer orderIndex;
        
        // Getters and Setters
        public Long getExerciseId() { return exerciseId; }
        public void setExerciseId(Long exerciseId) { this.exerciseId = exerciseId; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getMuscleGroup() { return muscleGroup; }
        public void setMuscleGroup(String muscleGroup) { this.muscleGroup = muscleGroup; }
        
        public String getEquipmentType() { return equipmentType; }
        public void setEquipmentType(String equipmentType) { this.equipmentType = equipmentType; }
        
        public String getVideoUrl() { return videoUrl; }
        public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
        
        public Integer getSets() { return sets; }
        public void setSets(Integer sets) { this.sets = sets; }
        
        public Integer getReps() { return reps; }
        public void setReps(Integer reps) { this.reps = reps; }
        
        public Integer getDurationSeconds() { return durationSeconds; }
        public void setDurationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; }
        
        public Integer getRestSeconds() { return restSeconds; }
        public void setRestSeconds(Integer restSeconds) { this.restSeconds = restSeconds; }
        
        public Integer getOrderIndex() { return orderIndex; }
        public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }
    }
    
    // Getters and Setters
    public Long getWorkoutId() { return workoutId; }
    public void setWorkoutId(Long workoutId) { this.workoutId = workoutId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getWorkoutType() { return workoutType; }
    public void setWorkoutType(String workoutType) { this.workoutType = workoutType; }
    
    public String getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(String difficultyLevel) { this.difficultyLevel = difficultyLevel; }
    
    public Integer getEstimatedDuration() { return estimatedDuration; }
    public void setEstimatedDuration(Integer estimatedDuration) { this.estimatedDuration = estimatedDuration; }
    
    public String getRequiredEquipment() { return requiredEquipment; }
    public void setRequiredEquipment(String requiredEquipment) { this.requiredEquipment = requiredEquipment; }
    
    public List<ExerciseDetail> getExercises() { return exercises; }
    public void setExercises(List<ExerciseDetail> exercises) { this.exercises = exercises; }
}
