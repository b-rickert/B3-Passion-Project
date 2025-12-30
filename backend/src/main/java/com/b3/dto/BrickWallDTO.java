package com.b3.dto;

import java.util.List;
import java.util.Map;

/**
 * DTO for displaying brick wall calendar view
 * Contains a month's worth of bricks organized by week
 */
public class BrickWallDTO {

    private String month; // "January"
    private String year; // "2025"
    private Integer currentStreak; // User's current streak
    private Integer totalBricksLaid; // Total LAID bricks in this month
    private Integer consistencyPercentage; // % of days worked out this month
    
    // Bricks organized by week for calendar display
    // Key: week number (1-5), Value: list of 7 bricks (Sun-Sat)
    private Map<Integer, List<BrickDTO>> weeks;
    
    // Alternative: Flat list of all bricks in the month
    private List<BrickDTO> bricks;

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public BrickWallDTO() {}

    public BrickWallDTO(String month, String year, Integer currentStreak, 
                       Integer totalBricksLaid, Integer consistencyPercentage,
                       List<BrickDTO> bricks) {
        this.month = month;
        this.year = year;
        this.currentStreak = currentStreak;
        this.totalBricksLaid = totalBricksLaid;
        this.consistencyPercentage = consistencyPercentage;
        this.bricks = bricks;
    }

    public BrickWallDTO(String month, String year, Integer currentStreak,
                       Integer totalBricksLaid, Integer consistencyPercentage,
                       Map<Integer, List<BrickDTO>> weeks) {
        this.month = month;
        this.year = year;
        this.currentStreak = currentStreak;
        this.totalBricksLaid = totalBricksLaid;
        this.consistencyPercentage = consistencyPercentage;
        this.weeks = weeks;
    }

    // ========================================================================
    // GETTERS AND SETTERS
    // ========================================================================

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(Integer currentStreak) {
        this.currentStreak = currentStreak;
    }

    public Integer getTotalBricksLaid() {
        return totalBricksLaid;
    }

    public void setTotalBricksLaid(Integer totalBricksLaid) {
        this.totalBricksLaid = totalBricksLaid;
    }

    public Integer getConsistencyPercentage() {
        return consistencyPercentage;
    }

    public void setConsistencyPercentage(Integer consistencyPercentage) {
        this.consistencyPercentage = consistencyPercentage;
    }

    public Map<Integer, List<BrickDTO>> getWeeks() {
        return weeks;
    }

    public void setWeeks(Map<Integer, List<BrickDTO>> weeks) {
        this.weeks = weeks;
    }

    public List<BrickDTO> getBricks() {
        return bricks;
    }

    public void setBricks(List<BrickDTO> bricks) {
        this.bricks = bricks;
    }

    // ========================================================================
    // TOSTRING
    // ========================================================================

    @Override
    public String toString() {
        return "BrickWallDTO{" +
                "month='" + month + '\'' +
                ", year='" + year + '\'' +
                ", currentStreak=" + currentStreak +
                ", totalBricksLaid=" + totalBricksLaid +
                ", consistencyPercentage=" + consistencyPercentage +
                ", brickCount=" + (bricks != null ? bricks.size() : 0) +
                '}';
    }
}
