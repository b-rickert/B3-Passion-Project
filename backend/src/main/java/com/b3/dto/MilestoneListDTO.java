package com.b3.dto;

import java.util.List;

/**
 * DTO for displaying list of milestones
 * Used when viewing all milestones (GET /milestones)
 * Organized by status for easier frontend rendering
 */
public class MilestoneListDTO {

    private Long profileId;
    private String userName;
    
    // Statistics
    private Integer totalMilestones;
    private Integer achievedCount;
    private Integer inProgressCount;
    private Integer notStartedCount;
    
    // Milestones organized by status
    private List<MilestoneDTO> achieved;
    private List<MilestoneDTO> inProgress;
    private List<MilestoneDTO> notStarted;
    
    // Recent achievement for celebration
    private MilestoneDTO mostRecentAchievement;

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public MilestoneListDTO() {}

    public MilestoneListDTO(Long profileId, String userName, Integer totalMilestones,
                           Integer achievedCount, Integer inProgressCount, Integer notStartedCount,
                           List<MilestoneDTO> achieved, List<MilestoneDTO> inProgress,
                           List<MilestoneDTO> notStarted, MilestoneDTO mostRecentAchievement) {
        this.profileId = profileId;
        this.userName = userName;
        this.totalMilestones = totalMilestones;
        this.achievedCount = achievedCount;
        this.inProgressCount = inProgressCount;
        this.notStartedCount = notStartedCount;
        this.achieved = achieved;
        this.inProgress = inProgress;
        this.notStarted = notStarted;
        this.mostRecentAchievement = mostRecentAchievement;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getTotalMilestones() {
        return totalMilestones;
    }

    public void setTotalMilestones(Integer totalMilestones) {
        this.totalMilestones = totalMilestones;
    }

    public Integer getAchievedCount() {
        return achievedCount;
    }

    public void setAchievedCount(Integer achievedCount) {
        this.achievedCount = achievedCount;
    }

    public Integer getInProgressCount() {
        return inProgressCount;
    }

    public void setInProgressCount(Integer inProgressCount) {
        this.inProgressCount = inProgressCount;
    }

    public Integer getNotStartedCount() {
        return notStartedCount;
    }

    public void setNotStartedCount(Integer notStartedCount) {
        this.notStartedCount = notStartedCount;
    }

    public List<MilestoneDTO> getAchieved() {
        return achieved;
    }

    public void setAchieved(List<MilestoneDTO> achieved) {
        this.achieved = achieved;
    }

    public List<MilestoneDTO> getInProgress() {
        return inProgress;
    }

    public void setInProgress(List<MilestoneDTO> inProgress) {
        this.inProgress = inProgress;
    }

    public List<MilestoneDTO> getNotStarted() {
        return notStarted;
    }

    public void setNotStarted(List<MilestoneDTO> notStarted) {
        this.notStarted = notStarted;
    }

    public MilestoneDTO getMostRecentAchievement() {
        return mostRecentAchievement;
    }

    public void setMostRecentAchievement(MilestoneDTO mostRecentAchievement) {
        this.mostRecentAchievement = mostRecentAchievement;
    }

    // ========================================================================
    // TOSTRING
    // ========================================================================

    @Override
    public String toString() {
        return "MilestoneListDTO{" +
                "profileId=" + profileId +
                ", userName='" + userName + '\'' +
                ", totalMilestones=" + totalMilestones +
                ", achievedCount=" + achievedCount +
                ", inProgressCount=" + inProgressCount +
                ", notStartedCount=" + notStartedCount +
                '}';
    }
}
