package com.b3.dto;

/**
 * DTO for displaying user profile information
 * Used as response when fetching user profile (GET).
 * Contains user settings and progress statistics.
 */
public class UserProfileDTO {

    private Long profileId; // Added for demo user selection
    private String displayName;
    private Integer age;
    private String fitnessLevel;
    private String primaryGoal;
    private String availableEquipment;
    private Integer workoutsPerWeek;
    
    // Progress/Stats
    private Integer currentStreak;
    private Integer longestStreak;
    private Integer totalWorkouts;
    private String memberSince; // Formatted date string

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public UserProfileDTO() {}

    public UserProfileDTO(Long profileId, String displayName, Integer age, 
                         String fitnessLevel, String primaryGoal, 
                         String availableEquipment, Integer workoutsPerWeek, 
                         Integer currentStreak, Integer longestStreak, 
                         Integer totalWorkouts, String memberSince) {
        this.profileId = profileId;
        this.displayName = displayName;
        this.age = age;
        this.fitnessLevel = fitnessLevel;
        this.primaryGoal = primaryGoal;
        this.availableEquipment = availableEquipment;
        this.workoutsPerWeek = workoutsPerWeek;
        this.currentStreak = currentStreak;
        this.longestStreak = longestStreak;
        this.totalWorkouts = totalWorkouts;
        this.memberSince = memberSince;
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

    public Integer getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(Integer currentStreak) {
        this.currentStreak = currentStreak;
    }

    public Integer getLongestStreak() {
        return longestStreak;
    }

    public void setLongestStreak(Integer longestStreak) {
        this.longestStreak = longestStreak;
    }

    public Integer getTotalWorkouts() {
        return totalWorkouts;
    }

    public void setTotalWorkouts(Integer totalWorkouts) {
        this.totalWorkouts = totalWorkouts;
    }

    public String getMemberSince() {
        return memberSince;
    }

    public void setMemberSince(String memberSince) {
        this.memberSince = memberSince;
    }

    // ========================================================================
    // TOSTRING
    // ========================================================================

    @Override
    public String toString() {
        return "UserProfileDTO{" +
                "profileId=" + profileId +
                ", displayName='" + displayName + '\'' +
                ", age=" + age +
                ", fitnessLevel='" + fitnessLevel + '\'' +
                ", primaryGoal='" + primaryGoal + '\'' +
                ", currentStreak=" + currentStreak +
                ", totalWorkouts=" + totalWorkouts +
                '}';
    }
}