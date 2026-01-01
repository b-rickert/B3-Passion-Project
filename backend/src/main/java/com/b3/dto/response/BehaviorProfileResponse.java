package com.b3.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO for behavior profile
 */
public class BehaviorProfileResponse {
    
    private Long behaviorId;
    private Long profileId;
    private String preferredWorkoutTime;
    private String currentTone;
    private Integer consecutiveDays;
    private Integer longestStreak;
    private Integer totalBricksLaid;
    private Double consistencyScore;
    private LocalDate lastWorkoutDate;
    private String motivationState;
    private String momentumTrend;
    private Double fatigueScore;
    private Double recentEnergyScore;
    private LocalDateTime lastToneChange;
    private String avgWorkoutTimeOfDay;
    private String preferredWorkoutTypes;
    private String energyPattern;
    private String stressTrend;
    private Integer avgSessionDuration;
    private Double skipFrequency;
    private LocalDateTime updatedAt;
    
    // Getters and Setters
    public Long getBehaviorId() {
        return behaviorId;
    }
    
    public void setBehaviorId(Long behaviorId) {
        this.behaviorId = behaviorId;
    }
    
    public Long getProfileId() {
        return profileId;
    }
    
    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }
    
    public String getPreferredWorkoutTime() {
        return preferredWorkoutTime;
    }
    
    public void setPreferredWorkoutTime(String preferredWorkoutTime) {
        this.preferredWorkoutTime = preferredWorkoutTime;
    }
    
    public String getCurrentTone() {
        return currentTone;
    }
    
    public void setCurrentTone(String currentTone) {
        this.currentTone = currentTone;
    }
    
    public Integer getConsecutiveDays() {
        return consecutiveDays;
    }
    
    public void setConsecutiveDays(Integer consecutiveDays) {
        this.consecutiveDays = consecutiveDays;
    }
    
    public Integer getLongestStreak() {
        return longestStreak;
    }
    
    public void setLongestStreak(Integer longestStreak) {
        this.longestStreak = longestStreak;
    }
    
    public Integer getTotalBricksLaid() {
        return totalBricksLaid;
    }
    
    public void setTotalBricksLaid(Integer totalBricksLaid) {
        this.totalBricksLaid = totalBricksLaid;
    }
    
    public Double getConsistencyScore() {
        return consistencyScore;
    }
    
    public void setConsistencyScore(Double consistencyScore) {
        this.consistencyScore = consistencyScore;
    }
    
    public LocalDate getLastWorkoutDate() {
        return lastWorkoutDate;
    }
    
    public void setLastWorkoutDate(LocalDate lastWorkoutDate) {
        this.lastWorkoutDate = lastWorkoutDate;
    }
    
    public String getMotivationState() {
        return motivationState;
    }
    
    public void setMotivationState(String motivationState) {
        this.motivationState = motivationState;
    }
    
    public String getMomentumTrend() {
        return momentumTrend;
    }
    
    public void setMomentumTrend(String momentumTrend) {
        this.momentumTrend = momentumTrend;
    }
    
    public Double getFatigueScore() {
        return fatigueScore;
    }
    
    public void setFatigueScore(Double fatigueScore) {
        this.fatigueScore = fatigueScore;
    }
    
    public Double getRecentEnergyScore() {
        return recentEnergyScore;
    }
    
    public void setRecentEnergyScore(Double recentEnergyScore) {
        this.recentEnergyScore = recentEnergyScore;
    }
    
    public LocalDateTime getLastToneChange() {
        return lastToneChange;
    }
    
    public void setLastToneChange(LocalDateTime lastToneChange) {
        this.lastToneChange = lastToneChange;
    }
    
    public String getAvgWorkoutTimeOfDay() {
        return avgWorkoutTimeOfDay;
    }
    
    public void setAvgWorkoutTimeOfDay(String avgWorkoutTimeOfDay) {
        this.avgWorkoutTimeOfDay = avgWorkoutTimeOfDay;
    }
    
    public String getPreferredWorkoutTypes() {
        return preferredWorkoutTypes;
    }
    
    public void setPreferredWorkoutTypes(String preferredWorkoutTypes) {
        this.preferredWorkoutTypes = preferredWorkoutTypes;
    }
    
    public String getEnergyPattern() {
        return energyPattern;
    }
    
    public void setEnergyPattern(String energyPattern) {
        this.energyPattern = energyPattern;
    }
    
    public String getStressTrend() {
        return stressTrend;
    }
    
    public void setStressTrend(String stressTrend) {
        this.stressTrend = stressTrend;
    }
    
    public Integer getAvgSessionDuration() {
        return avgSessionDuration;
    }
    
    public void setAvgSessionDuration(Integer avgSessionDuration) {
        this.avgSessionDuration = avgSessionDuration;
    }
    
    public Double getSkipFrequency() {
        return skipFrequency;
    }
    
    public void setSkipFrequency(Double skipFrequency) {
        this.skipFrequency = skipFrequency;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}