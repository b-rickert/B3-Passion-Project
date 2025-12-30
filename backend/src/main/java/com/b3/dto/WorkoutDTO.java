package com.b3.dto;

import java.util.List;

/**
 * DTO for displaying full workout details
 * Used when viewing a specific workout (GET /workouts/{id})
 */
public class WorkoutDTO {

    private Long workoutId;
    private String workoutName;
    private String description;
    private Integer estimatedDuration; // minutes
    private String difficultyLevel; // "BEGINNER", "INTERMEDIATE", "ADVANCED"
    private String targetMuscleGroups; // Comma-separated
    private String requiredEquipment; // Comma-separated
    
    // List of exercises in this workout
    private List<WorkoutExerciseDTO> exercises;

    // ========================================================================
    // NESTED DTO - Exercise info within workout
    // ========================================================================
    
    public static class WorkoutExerciseDTO {
        private Long exerciseId;
        private String exerciseName;
        private Integer sets;
        private Integer reps;
        private Integer durationSeconds;
        private Integer restSeconds;
        private String notes;

        // Constructors
        public WorkoutExerciseDTO() {}

        public WorkoutExerciseDTO(Long exerciseId, String exerciseName, Integer sets,
                                  Integer reps, Integer durationSeconds, Integer restSeconds,
                                  String notes) {
            this.exerciseId = exerciseId;
            this.exerciseName = exerciseName;
            this.sets = sets;
            this.reps = reps;
            this.durationSeconds = durationSeconds;
            this.restSeconds = restSeconds;
            this.notes = notes;
        }

        // Getters and Setters
        public Long getExerciseId() { return exerciseId; }
        public void setExerciseId(Long exerciseId) { this.exerciseId = exerciseId; }

        public String getExerciseName() { return exerciseName; }
        public void setExerciseName(String exerciseName) { this.exerciseName = exerciseName; }

        public Integer getSets() { return sets; }
        public void setSets(Integer sets) { this.sets = sets; }

        public Integer getReps() { return reps; }
        public void setReps(Integer reps) { this.reps = reps; }

        public Integer getDurationSeconds() { return durationSeconds; }
        public void setDurationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; }

        public Integer getRestSeconds() { return restSeconds; }
        public void setRestSeconds(Integer restSeconds) { this.restSeconds = restSeconds; }

        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public WorkoutDTO() {}

    public WorkoutDTO(Long workoutId, String workoutName, String description,
                      Integer estimatedDuration, String difficultyLevel,
                      String targetMuscleGroups, String requiredEquipment,
                      List<WorkoutExerciseDTO> exercises) {
        this.workoutId = workoutId;
        this.workoutName = workoutName;
        this.description = description;
        this.estimatedDuration = estimatedDuration;
        this.difficultyLevel = difficultyLevel;
        this.targetMuscleGroups = targetMuscleGroups;
        this.requiredEquipment = requiredEquipment;
        this.exercises = exercises;
    }

    // ========================================================================
    // GETTERS AND SETTERS
    // ========================================================================

    public Long getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(Long workoutId) {
        this.workoutId = workoutId;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(Integer estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getTargetMuscleGroups() {
        return targetMuscleGroups;
    }

    public void setTargetMuscleGroups(String targetMuscleGroups) {
        this.targetMuscleGroups = targetMuscleGroups;
    }

    public String getRequiredEquipment() {
        return requiredEquipment;
    }

    public void setRequiredEquipment(String requiredEquipment) {
        this.requiredEquipment = requiredEquipment;
    }

    public List<WorkoutExerciseDTO> getExercises() {
        return exercises;
    }

    public void setExercises(List<WorkoutExerciseDTO> exercises) {
        this.exercises = exercises;
    }

    // ========================================================================
    // TOSTRING
    // ========================================================================

    @Override
    public String toString() {
        return "WorkoutDTO{" +
                "workoutId=" + workoutId +
                ", workoutName='" + workoutName + '\'' +
                ", difficultyLevel='" + difficultyLevel + '\'' +
                ", estimatedDuration=" + estimatedDuration +
                ", exerciseCount=" + (exercises != null ? exercises.size() : 0) +
                '}';
    }
}