package com.b3.dto;

/**
 * DTO for displaying milestone details
 * Used when viewing a specific milestone (GET /milestones/{id})
 */
public class MilestoneDTO {

    private Long milestoneId;
    private String milestoneName;
    private String description;
    private String milestoneType; // "STREAK", "WORKOUT_COUNT", "GOAL_ACHIEVED", "CONSISTENCY", "PERSONAL_RECORD"
    private Integer targetValue;
    private Integer currentValue;
    private Boolean isAchieved;
    private String achievedAt; // ISO format timestamp (null if not achieved)
    
    // Computed values (from entity business logic)
    private Double progressPercentage; // 0.0 to 1.0
    private String progressPercentageString; // "75%"
    private Integer remainingValue; // How much left to achieve
    private Boolean isAlmostComplete; // >= 80% progress
    private Boolean isInProgress; // Started but not completed
    
    private String createdAt; // ISO format timestamp

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public MilestoneDTO() {}

    public MilestoneDTO(Long milestoneId, String milestoneName, String description,
                       String milestoneType, Integer targetValue, Integer currentValue,
                       Boolean isAchieved, String achievedAt, Double progressPercentage,
                       String progressPercentageString, Integer remainingValue,
                       Boolean isAlmostComplete, Boolean isInProgress, String createdAt) {
        this.milestoneId = milestoneId;
        this.milestoneName = milestoneName;
        this.description = description;
        this.milestoneType = milestoneType;
        this.targetValue = targetValue;
        this.currentValue = currentValue;
        this.isAchieved = isAchieved;
        this.achievedAt = achievedAt;
        this.progressPercentage = progressPercentage;
        this.progressPercentageString = progressPercentageString;
        this.remainingValue = remainingValue;
        this.isAlmostComplete = isAlmostComplete;
        this.isInProgress = isInProgress;
        this.createdAt = createdAt;
    }

    // ========================================================================
    // GETTERS AND SETTERS
    // ========================================================================

    public Long getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(Long milestoneId) {
        this.milestoneId = milestoneId;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMilestoneType() {
        return milestoneType;
    }

    public void setMilestoneType(String milestoneType) {
        this.milestoneType = milestoneType;
    }

    public Integer getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Integer targetValue) {
        this.targetValue = targetValue;
    }

    public Integer getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Integer currentValue) {
        this.currentValue = currentValue;
    }

    public Boolean getIsAchieved() {
        return isAchieved;
    }

    public void setIsAchieved(Boolean isAchieved) {
        this.isAchieved = isAchieved;
    }

    public String getAchievedAt() {
        return achievedAt;
    }

    public void setAchievedAt(String achievedAt) {
        this.achievedAt = achievedAt;
    }

    public Double getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(Double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public String getProgressPercentageString() {
        return progressPercentageString;
    }

    public void setProgressPercentageString(String progressPercentageString) {
        this.progressPercentageString = progressPercentageString;
    }

    public Integer getRemainingValue() {
        return remainingValue;
    }

    public void setRemainingValue(Integer remainingValue) {
        this.remainingValue = remainingValue;
    }

    public Boolean getIsAlmostComplete() {
        return isAlmostComplete;
    }

    public void setIsAlmostComplete(Boolean isAlmostComplete) {
        this.isAlmostComplete = isAlmostComplete;
    }

    public Boolean getIsInProgress() {
        return isInProgress;
    }

    public void setIsInProgress(Boolean isInProgress) {
        this.isInProgress = isInProgress;
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
        return "MilestoneDTO{" +
                "milestoneId=" + milestoneId +
                ", milestoneName='" + milestoneName + '\'' +
                ", milestoneType='" + milestoneType + '\'' +
                ", progress=" + currentValue + "/" + targetValue +
                ", isAchieved=" + isAchieved +
                '}';
    }
}