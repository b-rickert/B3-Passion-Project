package com.b3.dto;

import jakarta.validation.constraints.*;

/**
 * DTO for completing a workout session
 * Used when user finishes a workout (POST /sessions/{id}/complete)
 */

public class CompleteWorkoutDTO {

@NotNull(message = "Session ID is required")
private Long sessionId;

private String notes; // Post-workout notes

@Min(value = 0, message = "Calories burned cannot be negative")
private Integer caloriesBurned; // Optional manual entry

@Min(value = 1, message = "Rating must be at least 1")
@Max(value = 5, message = "Rating cannot exceed 5")
private Integer difficultyRating; // How hard was it? (1-5)

@Min(value = 1, message = "Rating must be at least 1")
@Max(value = 5, message = "Rating cannot exceed 5")
private Integer enjoymentRating; // How much did you enjoy it? (1-5)

// ========================================================================
// CONSTRUCTORS
// ========================================================================

public CompleteWorkoutDTO() {}

    public CompleteWorkoutDTO(Long sessionId, String notes, Integer caloriesBurned,
                             Integer difficultyRating, Integer enjoymentRating) {
        this.sessionId = sessionId;
        this.notes = notes;
        this.caloriesBurned = caloriesBurned;
        this.difficultyRating = difficultyRating;
        this.enjoymentRating = enjoymentRating;
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

public Integer getDifficultyRating() {
    return difficultyRating;
}

public void setDifficultyRating(Integer difficultyRating) {
    this.difficultyRating = difficultyRating;
}

public Integer getEnjoymentRating() {
    return enjoymentRating;
}

public void setEnjoymentRating(Integer enjoymentRating) {
    this.enjoymentRating = enjoymentRating;
}

// ========================================================================
// TOSTRING
// ========================================================================

    @Override
    public String toString() {
        return "CompleteWorkoutDTO{" +
                "sessionId=" + sessionId +
                ", caloriesBurned=" + caloriesBurned +
                ", difficultyRating=" + difficultyRating +
                ", enjoymentRating=" + enjoymentRating +
                '}';
    }
}


