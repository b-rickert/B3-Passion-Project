package com.b3.dto;

import jakarta.validation.constraints.*;

/**
 * DTO for creating a daily log entry
 * Used when user completes daily check-in (POST /daily-logs)
 */
public class DailyLogCreateDTO {

    @NotNull(message = "User profile ID is required")
    private Long profileId;

    @NotNull(message = "Energy level is required")
    @Min(value = 1, message = "Energy level must be between 1 and 5")
    @Max(value = 5, message = "Energy level must be between 1 and 5")
    private Integer energyLevel;

    @NotNull(message = "Stress level is required")
    @Min(value = 1, message = "Stress level must be between 1 and 5")
    @Max(value = 5, message = "Stress level must be between 1 and 5")
    private Integer stressLevel;

    @NotNull(message = "Sleep quality is required")
    @Min(value = 1, message = "Sleep quality must be between 1 and 5")
    @Max(value = 5, message = "Sleep quality must be between 1 and 5")
    private Integer sleepQuality;

    @NotBlank(message = "Mood is required")
    private String mood; // "GREAT", "GOOD", "OKAY", "LOW", "STRESSED"

    private String notes; // Optional user notes

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public DailyLogCreateDTO() {}

    public DailyLogCreateDTO(Long profileId, Integer energyLevel, Integer stressLevel,
                            Integer sleepQuality, String mood, String notes) {
        this.profileId = profileId;
        this.energyLevel = energyLevel;
        this.stressLevel = stressLevel;
        this.sleepQuality = sleepQuality;
        this.mood = mood;
        this.notes = notes;
    }

    // ========================================================================
    // GETTERS AND SETTERS
    // ========================================================================

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public Integer getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(Integer energyLevel) {
        this.energyLevel = energyLevel;
    }

    public Integer getStressLevel() {
        return stressLevel;
    }

    public void setStressLevel(Integer stressLevel) {
        this.stressLevel = stressLevel;
    }

    public Integer getSleepQuality() {
        return sleepQuality;
    }

    public void setSleepQuality(Integer sleepQuality) {
        this.sleepQuality = sleepQuality;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // ========================================================================
    // TOSTRING
    // ========================================================================

    @Override
    public String toString() {
        return "DailyLogCreateDTO{" +
                "profileId=" + profileId +
                ", energyLevel=" + energyLevel +
                ", stressLevel=" + stressLevel +
                ", sleepQuality=" + sleepQuality +
                ", mood='" + mood + '\'' +
                '}';
    }
}