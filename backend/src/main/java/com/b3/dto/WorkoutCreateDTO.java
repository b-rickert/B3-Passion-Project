package com.b3.dto;

import jakarta.validation.constraints.*;
import java.util.List;

/**
 * DTO for creating a new workout
 * Used when admin/user creates a workout (POST /workouts)
 */
public class WorkoutCreateDTO {

    @NotBlank(message = "Workout name is required")
    @Size(max = 100, message = "Workout name must be less than 100 characters")
    private String workoutName;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    @NotNull(message = "Estimated duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer estimatedDuration;

    @NotBlank(message = "Difficulty level is required")
    private String difficultyLevel; // "BEGINNER", "INTERMEDIATE", "ADVANCED"

    private String targetMuscleGroups; // Comma-separated

    private String requiredEquipment; // Comma-separated

    // List of exercise IDs to include in this workout
    private List<ExerciseInWorkoutDTO> exercises;

    // ========================================================================
    // NESTED DTO - Exercise configuration for workout
    // ========================================================================

    public static class ExerciseInWorkoutDTO {
        @NotNull(message = "Exercise ID is required")
        private Long exerciseId;

        @NotNull(message = "Sets is required")
        @Min(value = 1, message = "Sets must be at least 1")
        private Integer sets;

        @Min(value = 1, message = "Reps must be at least 1")
        private Integer reps;

        @Min(value = 1, message = "Duration must be at least 1 second")
        private Integer durationSeconds;

        @NotNull(message = "Rest time is required")
        @Min(value = 0, message = "Rest must be at least 0 seconds")
        private Integer restSeconds;

        private String notes;

        // Constructors
        public ExerciseInWorkoutDTO() {}

        public ExerciseInWorkoutDTO(Long exerciseId, Integer sets, Integer reps,
                                    Integer durationSeconds, Integer restSeconds, String notes) {
            this.exerciseId = exerciseId;
            this.sets = sets;
            this.reps = reps;
            this.durationSeconds = durationSeconds;
            this.restSeconds = restSeconds;
            this.notes = notes;
        }

        // Getters and Setters
        public Long getExerciseId() { return exerciseId; }
        public void setExerciseId(Long exerciseId) { this.exerciseId = exerciseId; }

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

    public WorkoutCreateDTO() {}

    public WorkoutCreateDTO(String workoutName, String description, Integer estimatedDuration,
                           String difficultyLevel, String targetMuscleGroups,
                           String requiredEquipment, List<ExerciseInWorkoutDTO> exercises) {
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

    public List<ExerciseInWorkoutDTO> getExercises() {
        return exercises;
    }

    public void setExercises(List<ExerciseInWorkoutDTO> exercises) {
        this.exercises = exercises;
    }

    // ========================================================================
    // TOSTRING
    // ========================================================================

    @Override
    public String toString() {
        return "WorkoutCreateDTO{" +
                "workoutName='" + workoutName + '\'' +
                ", difficultyLevel='" + difficultyLevel + '\'' +
                ", estimatedDuration=" + estimatedDuration +
                ", exerciseCount=" + (exercises != null ? exercises.size() : 0) +
                '}';
    }
}