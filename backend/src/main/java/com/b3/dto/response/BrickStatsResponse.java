package com.b3.dto.response;

/** 
 * Response DTO for brick statistics
 */

public class BrickStatsResponse {

    private Long totalBricks;
    private Integer currentStreak;
    private Integer longestStreak;
    private Integer bricksThisMonth;
    private Integer bricksThisWeek;

    // =====================================================
    // GETTERS AND SETTERS
    // =====================================================

    public Long getTotalBricks() {
        return totalBricks;
    }

    public void setTotalBricks(Long totalBricks) {
        this.totalBricks = totalBricks;
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

    public Integer getBricksThisMonth() {
        return bricksThisMonth;
    }
    public void setBricksThisMonth(Integer bricksThisMonth) {
        this.bricksThisMonth = bricksThisMonth;
    }

    public Integer getBricksThisWeek() {
        return bricksThisWeek;
    }

    public void setBricksThisWeek(Integer bricksThisWeek) {
        this.bricksThisWeek = bricksThisWeek;
    }
    
}
