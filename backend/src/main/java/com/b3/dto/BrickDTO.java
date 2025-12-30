package com.b3.dto;

/** 
 * DTO for displaying a single brick
 * Represents one day's workout status in the brick wall calendar
 */

public class BrickDTO {

    private Long brickId;
    private String date; // ISO format: "2025-01-15"
    private String status; // "LAID", "MISSED", "RECOVERY"
    private String color; // Hex color code for UI
    private String notes; // Optional user notes for that day

    // Workout info for tooltip/detail view
    private String workoutName; // Name of workout completed (if LAID)
    private Integer workoutDuration; // Duration in minutes (if LAID)

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public BrickDTO() {}

    public BrickDTO(Long brickId, String date, String status, String color, 
                   String notes, String workoutName, Integer workoutDuration) {
        this.brickId = brickId;
        this.date = date;
        this.status = status;
        this.color = color;
        this.notes = notes;
        this.workoutName = workoutName;
        this.workoutDuration = workoutDuration;
    }

    // ========================================================================
    // GETTERS AND SETTERS
    // ========================================================================

    public Long getBrickId() {
        return brickId;
    }

    public void setBrickId(Long brickId) {
        this.brickId = brickId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public Integer getWorkoutDuration() {
        return workoutDuration;
    }

    public void setWorkoutDuration(Integer workoutDuration) {
        this.workoutDuration = workoutDuration;
    }

    // ========================================================================
    // TOSTRING
    // ========================================================================

    @Override
    public String toString() {
        return "BrickDTO{" +
                "brickId=" + brickId +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                ", workoutName='" + workoutName + '\'' +
                '}';
    }
}
    
