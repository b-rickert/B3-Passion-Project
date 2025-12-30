package com.b3.dto;

/**
 * DTO for displaying daily log details
 * Used when viewing daily check-in (GET /daily-logs/{date})
 */
public class DailyLogDTO {

    private Long logId;
    private Long profileId;
    private String date; // ISO format: "2025-01-15"
    private Integer energyLevel; // 1-5
    private Integer stressLevel; // 1-5
    private Integer sleepQuality; // 1-5
    private String mood; // "GREAT", "GOOD", "OKAY", "LOW", "STRESSED"
    private String notes;
    
    // Computed values (from entity business logic)
    private Double wellnessScore; // 0.0 to 1.0
    private Boolean needsRecovery; // Does user need rest day?
    private Boolean optimalWorkoutDay; // Is today good for intense workout?
    
    private String createdAt; // ISO format timestamp

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public DailyLogDTO() {}

    public DailyLogDTO(Long logId, Long profileId, String date, Integer energyLevel,
                      Integer stressLevel, Integer sleepQuality, String mood, String notes,
                      Double wellnessScore, Boolean needsRecovery, Boolean optimalWorkoutDay,
                      String createdAt) {
        this.logId = logId;
        this.profileId = profileId;
        this.date = date;
        this.energyLevel = energyLevel;
        this.stressLevel = stressLevel;
        this.sleepQuality = sleepQuality;
        this.mood = mood;
        this.notes = notes;
        this.wellnessScore = wellnessScore;
        this.needsRecovery = needsRecovery;
        this.optimalWorkoutDay = optimalWorkoutDay;
        this.createdAt = createdAt;
    }

    // ========================================================================
    // GETTERS AND SETTERS
    // ========================================================================

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public Double getWellnessScore() {
        return wellnessScore;
    }

    public void setWellnessScore(Double wellnessScore) {
        this.wellnessScore = wellnessScore;
    }

    public Boolean getNeedsRecovery() {
        return needsRecovery;
    }

    public void setNeedsRecovery(Boolean needsRecovery) {
        this.needsRecovery = needsRecovery;
    }

    public Boolean getOptimalWorkoutDay() {
        return optimalWorkoutDay;
    }

    public void setOptimalWorkoutDay(Boolean optimalWorkoutDay) {
        this.optimalWorkoutDay = optimalWorkoutDay;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    // ========================================================================
    // TOSTRING
    // ========================================================================

    @Override
    public String toString() {
        return "DailyLogDTO{" +
                "logId=" + logId +
                ", date='" + date + '\'' +
                ", energyLevel=" + energyLevel +
                ", stressLevel=" + stressLevel +
                ", sleepQuality=" + sleepQuality +
                ", mood='" + mood + '\'' +
                ", wellnessScore=" + wellnessScore +
                ", needsRecovery=" + needsRecovery +
                '}';
    }
}
