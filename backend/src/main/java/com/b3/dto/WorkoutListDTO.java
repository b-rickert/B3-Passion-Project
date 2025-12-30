package com.b3.dto;

/**
 * DTO for displaying workout in list view
 * Used when showing list of workouts (GET /workouts)
 * Contains minimal info for performance
 */
public class WorkoutListDTO {

    private Long workoutId;
    private String workoutName;
    private Integer estimatedDuration;
    private String difficultyLevel;
    private Integer exerciseCount;
    private String targetMuscleGroups; // Brief summary

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public WorkoutListDTO() {}

    public WorkoutListDTO(Long workoutId, String workoutName, Integer estimatedDuration,
                         String difficultyLevel, Integer exerciseCount, String targetMuscleGroups) {
        this.workoutId = workoutId;
        this.workoutName = workoutName;
        this.estimatedDuration = estimatedDuration;
        this.difficultyLevel = difficultyLevel;
        this.exerciseCount = exerciseCount;
        this.targetMuscleGroups = targetMuscleGroups;
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

    public Integer getExerciseCount() {
        return exerciseCount;
    }

    public void setExerciseCount(Integer exerciseCount) {
        this.exerciseCount = exerciseCount;
    }

    public String getTargetMuscleGroups() {
        return targetMuscleGroups;
    }

    public void setTargetMuscleGroups(String targetMuscleGroups) {
        this.targetMuscleGroups = targetMuscleGroups;
    }

    // ========================================================================
    // TOSTRING
    // ========================================================================

    @Override
    public String toString() {
        return "WorkoutListDTO{" +
                "workoutId=" + workoutId +
                ", workoutName='" + workoutName + '\'' +
                ", estimatedDuration=" + estimatedDuration +
                ", difficultyLevel='" + difficultyLevel + '\'' +
                ", exerciseCount=" + exerciseCount +
                '}';
    }
}
