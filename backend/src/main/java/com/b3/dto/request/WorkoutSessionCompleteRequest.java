package com.b3.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class WorkoutSessionCompleteRequest {
    
    @NotNull(message = "Actual duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer actualDuration;
    
    private String notes;
    
    public Integer getActualDuration() {
        return actualDuration;
    }
    
    public void setActualDuration(Integer actualDuration) {
        this.actualDuration = actualDuration;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
}