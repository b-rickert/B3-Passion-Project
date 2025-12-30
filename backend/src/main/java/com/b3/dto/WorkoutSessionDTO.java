package com.b3.dto;

import java.util.List;

/**
 * DTO for displaying workout session details
 * Used when viewing active or completed session (GET /sessions/{id})
 */
public class WorkoutSessionDTO {

    private Long sessionId;
    private Long profileId;
    private String userName; // For display
    private Long workoutId;
    private String workoutName;
    
    private String status; // "PLANNED", "IN_PROGRESS", "COMPLETED", "SKIPPED"
    private String startTime; // ISO format timestamp
    private String endTime; // ISO format timestamp (null if in progress)
    private Integer durationMinutes; // Actual duration (null if in progress)
    
    private String notes; // User notes
    private Integer caloriesBurned; // Estimated/tracked calories
    
    // List of exercises with logged sets
    private List<SessionExerciseDTO> exercises;

    // ========================================================================
    // NESTED DTO - Exercise progress in session
    // ========================================================================

    public static class SessionExerciseDTO {
        private Long exerciseId;
        private String exerciseName;
        private Integer targetSets;
        private Integer targetReps;
        private Integer completedSets;
        private List<LoggedSetDTO> loggedSets;

        // Constructors
        public SessionExerciseDTO() {}

        public SessionExerciseDTO(Long exerciseId, String exerciseName, Integer targetSets,
                                 Integer targetReps, Integer completedSets, List<LoggedSetDTO> loggedSets) {
            this.exerciseId = exerciseId;
            this.exerciseName = exerciseName;
            this.targetSets = targetSets;
            this.targetReps = targetReps;
            this.completedSets = completedSets;
            this.loggedSets = loggedSets;
        }

        // Getters and Setters
        public Long getExerciseId() { return exerciseId; }
        public void setExerciseId(Long exerciseId) { this.exerciseId = exerciseId; }

        public String getExerciseName() { return exerciseName; }
        public void setExerciseName(String exerciseName) { this.exerciseName = exerciseName; }

        public Integer getTargetSets() { return targetSets; }
        public void setTargetSets(Integer targetSets) { this.targetSets = targetSets; }

        public Integer getTargetReps() { return targetReps; }
        public void setTargetReps(Integer targetReps) { this.targetReps = targetReps; }

        public Integer getCompletedSets() { return completedSets; }
        public void setCompletedSets(Integer completedSets) { this.completedSets = completedSets; }

        public List<LoggedSetDTO> getLoggedSets() { return loggedSets; }
        public void setLoggedSets(List<LoggedSetDTO> loggedSets) { this.loggedSets = loggedSets; }
    }

    public static class LoggedSetDTO {
        private Integer setNumber;
        private Integer reps;
        private Double weight;
        private Integer durationSeconds;
        private String notes;

        // Constructors
        public LoggedSetDTO() {}

        public LoggedSetDTO(Integer setNumber, Integer reps, Double weight, 
                           Integer durationSeconds, String notes) {
            this.setNumber = setNumber;
            this.reps = reps;
            this.weight = weight;
            this.durationSeconds = durationSeconds;
            this.notes = notes;
        }

        // Getters and Setters
        public Integer getSetNumber() { return setNumber; }
        public void setSetNumber(Integer setNumber) { this.setNumber = setNumber; }

        public Integer getReps() { return reps; }
        public void setReps(Integer reps) { this.reps = reps; }

        public Double getWeight() { return weight; }
        public void setWeight(Double weight) { this.weight = weight; }

        public Integer getDurationSeconds() { return durationSeconds; }
        public void setDurationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; }

        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public WorkoutSessionDTO() {}

    public WorkoutSessionDTO(Long sessionId, Long profileId, String userName, Long workoutId,
                            String workoutName, String status, String startTime, String endTime,
                            Integer durationMinutes, String notes, Integer caloriesBurned,
                            List<SessionExerciseDTO> exercises) {
        this.sessionId = sessionId;
        this.profileId = profileId;
        this.userName = userName;
        this.workoutId = workoutId;
        this.workoutName = workoutName;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationMinutes = durationMinutes;
        this.notes = notes;
        this.caloriesBurned = caloriesBurned;
        this.exercises = exercises;
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

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(Integer caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public List<SessionExerciseDTO> getExercises() {
        return exercises;
    }

    public void setExercises(List<SessionExerciseDTO> exercises) {
        this.exercises = exercises;
    }

    // ========================================================================
    // TOSTRING
    // ========================================================================

    @Override
    public String toString() {
        return "WorkoutSessionDTO{" +
                "sessionId=" + sessionId +
                ", workoutName='" + workoutName + '\'' +
                ", status='" + status + '\'' +
                ", durationMinutes=" + durationMinutes +
                '}';
    }
}