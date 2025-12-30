package com.b3.dto;

/**
 * DTO for displaying a BRIX message
 * Used when viewing BRIX chat messages (GET /brix/messages)
 */
public class BrixMessageDTO {

    private Long messageId;
    private String messageText;
    private String messageType; // "MOTIVATION", "CHECK_IN", "CELEBRATION", "TIP"
    private String tone; // "ENCOURAGING", "CHALLENGING", "EMPATHETIC", "CELEBRATORY"
    private String contextTrigger; // What triggered this message
    private String sentAt; // ISO format timestamp
    private String timeAgo; // Human-readable: "5 minutes ago", "2 hours ago"
    private Boolean isSentToday; // Was sent today?

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public BrixMessageDTO() {}

    public BrixMessageDTO(Long messageId, String messageText, String messageType,
                         String tone, String contextTrigger, String sentAt,
                         String timeAgo, Boolean isSentToday) {
        this.messageId = messageId;
        this.messageText = messageText;
        this.messageType = messageType;
        this.tone = tone;
        this.contextTrigger = contextTrigger;
        this.sentAt = sentAt;
        this.timeAgo = timeAgo;
        this.isSentToday = isSentToday;
    }

    // ========================================================================
    // GETTERS AND SETTERS
    // ========================================================================

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }

    public String getContextTrigger() {
        return contextTrigger;
    }

    public void setContextTrigger(String contextTrigger) {
        this.contextTrigger = contextTrigger;
    }

    public String getSentAt() {
        return sentAt;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public Boolean getIsSentToday() {
        return isSentToday;
    }

    public void setIsSentToday(Boolean isSentToday) {
        this.isSentToday = isSentToday;
    }

    // ========================================================================
    // TOSTRING
    // ========================================================================

    @Override
    public String toString() {
        return "BrixMessageDTO{" +
                "messageId=" + messageId +
                ", messageType='" + messageType + '\'' +
                ", tone='" + tone + '\'' +
                ", timeAgo='" + timeAgo + '\'' +
                '}';
    }
}