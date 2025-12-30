package com.b3.dto;

import jakarta.validation.constraints.*;

/**
 * DTO for updating user profile
 * Used when user edits their profile settings (PUT).
 */
public class UserProfileUpdateDTO {

    @Size(min = 2, max = 50, message = "Display name must be between 2 and 50 characters")
    private String displayName;

    @Min(value = 13, message = "Age must be at least 13")
    @Max(value = 120, message = "Age must be realistic")
    private Integer age;

    private String fitnessLevel; // "BEGINNER", "INTERMEDIATE", "ADVANCED"

    private String primaryGoal; // "WEIGHT_LOSS", "STRENGTH", etc.

    private String availableEquipment;

    @Min(value = 1, message = "Workouts per week must be at least 1")
    @Max(value = 7, message = "Workouts per week cannot exceed 7")
    private Integer workoutsPerWeek;

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public UserProfileUpdateDTO() {}

    public UserProfileUpdateDTO(String displayName, Integer age, String fitnessLevel,
                                String primaryGoal, String availableEquipment,
                                Integer workoutsPerWeek) {
        this.displayName = displayName;
        this.age = age;
        this.fitnessLevel = fitnessLevel;
        this.primaryGoal = primaryGoal;
        this.availableEquipment = availableEquipment;
        this.workoutsPerWeek = workoutsPerWeek;
    }

    // ========================================================================
    // GETTERS AND SETTERS
    // ========================================================================

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getFitnessLevel() {
        return fitnessLevel;
    }

    public void setFitnessLevel(String fitnessLevel) {
        this.fitnessLevel = fitnessLevel;
    }

    public String getPrimaryGoal() {
        return primaryGoal;
    }

    public void setPrimaryGoal(String primaryGoal) {
        this.primaryGoal = primaryGoal;
    }

    public String getAvailableEquipment() {
        return availableEquipment;
    }

    public void setAvailableEquipment(String availableEquipment) {
        this.availableEquipment = availableEquipment;
    }

    public Integer getWorkoutsPerWeek() {
        return workoutsPerWeek;
    }

    public void setWorkoutsPerWeek(Integer workoutsPerWeek) {
        this.workoutsPerWeek = workoutsPerWeek;
    }

    // ========================================================================
    // TOSTRING
    // ========================================================================

    @Override
    public String toString() {
        return "UserProfileUpdateDTO{" +
                "displayName='" + displayName + '\'' +
                ", age=" + age +
                ", fitnessLevel='" + fitnessLevel + '\'' +
                ", primaryGoal='" + primaryGoal + '\'' +
                ", workoutsPerWeek=" + workoutsPerWeek +
                '}';
    }
}