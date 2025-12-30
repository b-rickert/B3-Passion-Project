package com.b3.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * BrixMessage entity - Messages sent from BRIX AI coach to user
 * 
 * Represents all communication from BRIX, including motivational messages,
 * check-ins, celebrations, and tips. Tracks message type, tone, and context
 * to help BRIX learn what messaging works best for each user.
 */
@Entity
@Table(name = "brix_message")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BrixMessage {

    // ========================================================================
    // ENUMS
    // ========================================================================

    public enum MessageType {
        MOTIVATION,      // "You got this! Let's crush today!"
        CHECK_IN,        // "How are you feeling today?"
        CELEBRATION,     // "7-day streak! You're on fire!"
        TIP              // "Pro tip: warm up prevents injury"
    }

    public enum Tone {
        ENCOURAGING,     // Supportive, gentle
        CHALLENGING,     // Push harder, level up
        EMPATHETIC,      // Understanding, compassionate
        CELEBRATORY      // Excited, congratulatory
    }

    // ========================================================================
    // FIELDS
    // ========================================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    // ========================================================================
    // RELATIONSHIPS
    // ========================================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private UserProfile userProfile;

    // ========================================================================
    // MESSAGE DATA
    // ========================================================================

    @NotBlank
    @Size(max = 500)
    @Column(name = "message_text", nullable = false, length = 500)
    private String messageText;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false, length = 20)
    private MessageType messageType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tone", nullable = false, length = 20)
    private Tone tone;

    @Size(max = 100)
    @Column(name = "context_trigger", length = 100)
    private String contextTrigger; // What triggered this message (e.g., "7_day_streak")

    @Column(name = "sent_at", nullable = false, updatable = false)
    private LocalDateTime sentAt;

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public BrixMessage() {}

    public BrixMessage(UserProfile userProfile, 
                       String messageText, 
                       MessageType messageType, 
                       Tone tone) {
        this.userProfile = userProfile;
        this.messageText = messageText;
        this.messageType = messageType;
        this.tone = tone;
    }

    public BrixMessage(UserProfile userProfile, 
                       String messageText, 
                       MessageType messageType, 
                       Tone tone, 
                       String contextTrigger) {
        this(userProfile, messageText, messageType, tone);
        this.contextTrigger = contextTrigger;
    }

    // ========================================================================
    // JPA CALLBACKS
    // ========================================================================

    @PrePersist
    protected void onCreate() {
        this.sentAt = LocalDateTime.now();
    }

    // ========================================================================
    // BUSINESS LOGIC
    // ========================================================================

    /**
     * Check if this is a motivational message
     */
    public boolean isMotivational() {
        return messageType == MessageType.MOTIVATION;
    }

    /**
     * Check if this is a celebration message
     */
    public boolean isCelebration() {
        return messageType == MessageType.CELEBRATION;
    }

    /**
     * Check if this is a check-in message
     */
    public boolean isCheckIn() {
        return messageType == MessageType.CHECK_IN;
    }

    /**
     * Check if this message was triggered by a specific event
     */
    public boolean wasTriggeredBy(String trigger) {
        return contextTrigger != null && 
               contextTrigger.equalsIgnoreCase(trigger);
    }

    /**
     * Check if message was sent today
     */
    public boolean isSentToday() {
        return sentAt != null && 
               sentAt.toLocalDate().equals(LocalDateTime.now().toLocalDate());
    }

    /**
     * Check if message was sent recently (within last 24 hours)
     */
    public boolean isSentRecently() {
        if (sentAt == null) return false;
        
        long hoursAgo = ChronoUnit.HOURS.between(sentAt, LocalDateTime.now());
        return hoursAgo <= 24;
    }

    /**
     * Get how many hours ago message was sent
     */
    public long getHoursAgo() {
        if (sentAt == null) return -1;
        return ChronoUnit.HOURS.between(sentAt, LocalDateTime.now());
    }

    /**
     * Get message preview (first 50 chars)
     */
    public String getMessagePreview() {
        if (messageText == null || messageText.length() <= 50) {
            return messageText;
        }
        return messageText.substring(0, 47) + "...";
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

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Tone getTone() {
        return tone;
    }

    public void setTone(Tone tone) {
        this.tone = tone;
    }

    public String getContextTrigger() {
        return contextTrigger;
    }

    public void setContextTrigger(String contextTrigger) {
        this.contextTrigger = contextTrigger;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
    this.sentAt = sentAt;
    }

    // ========================================================================
    // OBJECT OVERRIDES
    // ========================================================================

    @Override
    public String toString() {
        return "BrixMessage{" +
                "messageId=" + messageId +
                ", messageType=" + messageType +
                ", tone=" + tone +
                ", preview='" + getMessagePreview() + '\'' +
                ", sentAt=" + sentAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BrixMessage)) return false;
        BrixMessage that = (BrixMessage) o;
        return Objects.equals(messageId, that.messageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId);
    }
}