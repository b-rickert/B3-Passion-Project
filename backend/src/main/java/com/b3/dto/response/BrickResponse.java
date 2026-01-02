package com.b3.dto.response;

import java.time.LocalDate;

/**
 * Response DTO for brick
 */
public class BrickResponse {

    private Long brickId;
    private Long profileId;
    private Long sessionId;
    private LocalDate brickDate;
    private String brickColor;
    private Boolean isFirstOfMonth;

    // =====================================================
    // GETTERS AND SETTERS
    // =====================================================

    public Long getBrickId() {
        return brickId;
    }

    public void setBrickId(Long brickId) {
        this.brickId = brickId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public LocalDate getBrickDate() {
        return brickDate;
    }

    public void setBrickDate(LocalDate brickDate) {
        this.brickDate = brickDate;
    }

    public String getBrickColor() {
        return brickColor;
    }

    public void setBrickColor(String brickColor) {
        this.brickColor = brickColor;
    }

    public Boolean getIsFirstOfMonth() {
        return isFirstOfMonth;
    }

    public void setIsFirstOfMonth(Boolean isFirstOfMonth) {
        this.isFirstOfMonth = isFirstOfMonth;
    }
}