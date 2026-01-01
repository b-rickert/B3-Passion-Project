package com.b3.dto.request;

import com.b3.model.UserProfile;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for updating user profile
 */
public class UserProfileUpdateRequest {
    
    @Size(min = 2, max = 50, message = "Display name must be between 2 and 50 characters")
    private String displayName;
    
    @Min(value = 13, message = "Age must be at least 13")
    @Max(value = 120, message = "Age must be less than 120")
    private Integer age;
    
    private UserProfile.FitnessLevel fitnessLevel;
    private UserProfile.PrimaryGoal primaryGoal;
    private String equipment;
    
    @Min(value = 1, message = "Weekly goal must be at least 1 day")
    @Max(value = 7, message = "Weekly goal cannot exceed 7 days")
    private Integer weeklyGoalDays;
    
    // Getters and Setters
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    
    public UserProfile.FitnessLevel getFitnessLevel() { return fitnessLevel; }
    public void setFitnessLevel(UserProfile.FitnessLevel fitnessLevel) { this.fitnessLevel = fitnessLevel; }
    
    public UserProfile.PrimaryGoal getPrimaryGoal() { return primaryGoal; }
    public void setPrimaryGoal(UserProfile.PrimaryGoal primaryGoal) { this.primaryGoal = primaryGoal; }
    
    public String getEquipment() { return equipment; }
    public void setEquipment(String equipment) { this.equipment = equipment; }
    
    public Integer getWeeklyGoalDays() { return weeklyGoalDays; }
    public void setWeeklyGoalDays(Integer weeklyGoalDays) { this.weeklyGoalDays = weeklyGoalDays; }
}