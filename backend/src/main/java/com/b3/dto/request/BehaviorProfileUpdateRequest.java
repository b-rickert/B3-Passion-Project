package com.b3.dto.request;

import com.b3.model.BehaviorProfile;

/**
 * Request DTO for updating behavior profile
 */
public class BehaviorProfileUpdateRequest {
    
    private BehaviorProfile.PreferredTime preferredWorkoutTime;
    private BehaviorProfile.PreferredTime avgWorkoutTimeOfDay;
    private Integer avgSessionDuration;
    private String preferredWorkoutTypes;
    private String energyPattern;
    private BehaviorProfile.StressTrend stressTrend;
    private Double skipFrequency;
    
    // Getters and Setters
    public BehaviorProfile.PreferredTime getPreferredWorkoutTime() {
        return preferredWorkoutTime;
    }
    
    public void setPreferredWorkoutTime(BehaviorProfile.PreferredTime preferredWorkoutTime) {
        this.preferredWorkoutTime = preferredWorkoutTime;
    }
    
    public BehaviorProfile.PreferredTime getAvgWorkoutTimeOfDay() {
        return avgWorkoutTimeOfDay;
    }
    
    public void setAvgWorkoutTimeOfDay(BehaviorProfile.PreferredTime avgWorkoutTimeOfDay) {
        this.avgWorkoutTimeOfDay = avgWorkoutTimeOfDay;
    }
    
    public Integer getAvgSessionDuration() {
        return avgSessionDuration;
    }
    
    public void setAvgSessionDuration(Integer avgSessionDuration) {
        this.avgSessionDuration = avgSessionDuration;
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
    
    public BehaviorProfile.StressTrend getStressTrend() {
        return stressTrend;
    }
    
    public void setStressTrend(BehaviorProfile.StressTrend stressTrend) {
        this.stressTrend = stressTrend;
    }
    
    public Double getSkipFrequency() {
        return skipFrequency;
    }
    
    public void setSkipFrequency(Double skipFrequency) {
        this.skipFrequency = skipFrequency;
    }
}