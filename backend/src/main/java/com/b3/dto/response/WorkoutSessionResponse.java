package com.b3.dto.response;

import java.time.LocalDateTime;

public class WorkoutSessionResponse {
    
    private Long sessionId;
    private Long profileId;
    private Long workoutId;
    private String workoutName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer actualDuration;
    private String completionStatus;
    private String performanceRating;
    private String notes;
    
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
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public Integer getActualDuration() {
        return actualDuration;
    }
    
    public void setActualDuration(Integer actualDuration) {
        this.actualDuration = actualDuration;
    }
    
    public String getCompletionStatus() {
        return completionStatus;
    }
    
    public void setCompletionStatus(String completionStatus) {
        this.completionStatus = completionStatus;
    }
    
    public String getPerformanceRating() {
        return performanceRating;
    }
    
    public void setPerformanceRating(String performanceRating) {
        this.performanceRating = performanceRating;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
}