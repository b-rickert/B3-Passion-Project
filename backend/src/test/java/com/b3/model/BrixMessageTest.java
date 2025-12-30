package com.b3.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import com.b3.model.BrixMessage.MessageType;
import com.b3.model.BrixMessage.Tone;
import com.b3.model.UserProfile.FitnessLevel;
import com.b3.model.UserProfile.PrimaryGoal;

import java.time.LocalDateTime;

/**
 * Unit tests for BrixMessage entity
 */
@DisplayName("BrixMessage Entity Tests")
class BrixMessageTest {

    private BrixMessage message;
    private UserProfile userProfile;

    @BeforeEach
    void setUp() {
        // Create UserProfile
        userProfile = new UserProfile(
            "TestUser",
            25,
            FitnessLevel.INTERMEDIATE,
            PrimaryGoal.STRENGTH,
            "Dumbbells",
            4
        );
        userProfile.setProfileId(1L);

        // Create BrixMessage
        message = new BrixMessage(
            userProfile,
            "Great job on your workout today! You're building momentum.",
            MessageType.MOTIVATION,
            Tone.ENCOURAGING,
            "workout_completed"
        );
    }

    // =====================================================
    // CONSTRUCTOR TESTS
    // =====================================================

    @Test
    @DisplayName("Should create BrixMessage with all required fields")
    void testConstructorWithAllFields() {
        assertEquals(userProfile, message.getUserProfile());
        assertEquals("Great job on your workout today! You're building momentum.", message.getMessageText());
        assertEquals(MessageType.MOTIVATION, message.getMessageType());
        assertEquals(Tone.ENCOURAGING, message.getTone());
        assertEquals("workout_completed", message.getContextTrigger());
        assertNotNull(message.getSentAt());
    }

    @Test
    @DisplayName("Should create empty BrixMessage with default constructor")
    void testDefaultConstructor() {
        BrixMessage empty = new BrixMessage();
        
        assertNotNull(empty);
        assertNull(empty.getUserProfile());
        assertNull(empty.getMessageText());
    }

    // =====================================================
    // RELATIONSHIP TESTS
    // =====================================================

    @Test
    @DisplayName("Should maintain relationship with UserProfile")
    void testUserProfileRelationship() {
        assertEquals(userProfile, message.getUserProfile());
        assertEquals("TestUser", message.getUserProfile().getDisplayName());
    }

    // =====================================================
    // ENUM TESTS
    // =====================================================

    @Test
    @DisplayName("Should handle all MessageType enum values")
    void testMessageTypeEnum() {
        message.setMessageType(MessageType.MOTIVATION);
        assertEquals(MessageType.MOTIVATION, message.getMessageType());

        message.setMessageType(MessageType.CHECK_IN);
        assertEquals(MessageType.CHECK_IN, message.getMessageType());

        message.setMessageType(MessageType.CELEBRATION);
        assertEquals(MessageType.CELEBRATION, message.getMessageType());

        message.setMessageType(MessageType.TIP);
        assertEquals(MessageType.TIP, message.getMessageType());
    }

    @Test
    @DisplayName("Should handle all Tone enum values")
    void testToneEnum() {
        message.setTone(Tone.ENCOURAGING);
        assertEquals(Tone.ENCOURAGING, message.getTone());

        message.setTone(Tone.CHALLENGING);
        assertEquals(Tone.CHALLENGING, message.getTone());

        message.setTone(Tone.EMPATHETIC);
        assertEquals(Tone.EMPATHETIC, message.getTone());

        message.setTone(Tone.CELEBRATORY);
        assertEquals(Tone.CELEBRATORY, message.getTone());
    }

    // =====================================================
    // BUSINESS LOGIC TESTS
    // =====================================================

    @Test
    @DisplayName("isMotivational() returns true for MOTIVATION type")
    void testIsMotivationalTrue() {
        message.setMessageType(MessageType.MOTIVATION);
        assertTrue(message.isMotivational());
    }

    @Test
    @DisplayName("isMotivational() returns false for non-MOTIVATION types")
    void testIsMotivationalFalse() {
        message.setMessageType(MessageType.TIP);
        assertFalse(message.isMotivational());
    }

    @Test
    @DisplayName("isCelebration() returns true for CELEBRATION type")
    void testIsCelebrationTrue() {
        message.setMessageType(MessageType.CELEBRATION);
        assertTrue(message.isCelebration());
    }

    @Test
    @DisplayName("isCelebration() returns false for non-CELEBRATION types")
    void testIsCelebrationFalse() {
        message.setMessageType(MessageType.MOTIVATION);
        assertFalse(message.isCelebration());
    }

    @Test
    @DisplayName("isCheckIn() returns true for CHECK_IN type")
    void testIsCheckInTrue() {
        message.setMessageType(MessageType.CHECK_IN);
        assertTrue(message.isCheckIn());
    }

    @Test
    @DisplayName("isCheckIn() returns false for non-CHECK_IN types")
    void testIsCheckInFalse() {
        message.setMessageType(MessageType.CELEBRATION);
        assertFalse(message.isCheckIn());
    }

    @Test
    @DisplayName("wasTriggeredBy() returns true for matching trigger")
    void testWasTriggeredByTrue() {
        assertTrue(message.wasTriggeredBy("workout_completed"));
    }

    @Test
    @DisplayName("wasTriggeredBy() returns false for non-matching trigger")
    void testWasTriggeredByFalse() {
        assertFalse(message.wasTriggeredBy("streak_milestone"));
    }

    @Test
    @DisplayName("wasTriggeredBy() is case-insensitive")
    void testWasTriggeredByCaseInsensitive() {
        assertTrue(message.wasTriggeredBy("WORKOUT_COMPLETED"));
        assertTrue(message.wasTriggeredBy("Workout_Completed"));
    }

    @Test
    @DisplayName("wasTriggeredBy() returns false when trigger is null")
    void testWasTriggeredByNull() {
        message.setContextTrigger(null);
        assertFalse(message.wasTriggeredBy("workout_completed"));
    }

    @Test
    @DisplayName("isSentToday() returns true for today's message")
    void testIsSentTodayTrue() {
        message.setSentAt(LocalDateTime.now());
        assertTrue(message.isSentToday());
    }

    @Test
    @DisplayName("isSentToday() returns false for yesterday's message")
    void testIsSentTodayFalse() {
        message.setSentAt(LocalDateTime.now().minusDays(1));
        assertFalse(message.isSentToday());
    }

    @Test
    @DisplayName("isSentRecently() returns true for message within last hour")
    void testIsSentRecentlyTrue() {
        message.setSentAt(LocalDateTime.now().minusMinutes(30));
        assertTrue(message.isSentRecently());
    }

    @Test
    @DisplayName("isSentRecently() returns false for message over an hour ago")
    void testIsSentRecentlyFalse() {
        message.setSentAt(LocalDateTime.now().minusHours(2));
        assertFalse(message.isSentRecently());
    }

    @Test
    @DisplayName("getHoursAgo() calculates correctly")
    void testGetHoursAgo() {
        message.setSentAt(LocalDateTime.now().minusHours(3));
        assertEquals(3, message.getHoursAgo());

        message.setSentAt(LocalDateTime.now().minusMinutes(30));
        assertEquals(0, message.getHoursAgo());
    }

    @Test
    @DisplayName("getMessagePreview() returns first 50 chars")
    void testGetMessagePreview() {
        String longMessage = "This is a very long message that should be truncated to only show the first 50 characters for preview purposes.";
        message.setMessageText(longMessage);
        
        String preview = message.getMessagePreview();
        assertEquals(50, preview.length());
        assertTrue(preview.endsWith("..."));
    }

    @Test
    @DisplayName("getMessagePreview() returns full message if under 50 chars")
    void testGetMessagePreviewShort() {
        message.setMessageText("Short message");
        assertEquals("Short message", message.getMessagePreview());
    }

    // =====================================================
    // VALIDATION TESTS
    // =====================================================

    @Test
    @DisplayName("Should handle long message text")
    void testLongMessageText() {
        String longText = "This is a message. ".repeat(100);
        message.setMessageText(longText);
        assertEquals(longText, message.getMessageText());
    }

    @Test
    @DisplayName("Should handle empty context trigger")
    void testEmptyContextTrigger() {
        message.setContextTrigger("");
        assertEquals("", message.getContextTrigger());
    }

    @Test
    @DisplayName("Should handle null context trigger")
    void testNullContextTrigger() {
        message.setContextTrigger(null);
        assertNull(message.getContextTrigger());
    }

    // =====================================================
    // EDGE CASE TESTS
    // =====================================================

    @Test
    @DisplayName("Handles message with special characters")
    void testSpecialCharacters() {
        String specialMsg = "Great job! 🎉 You're 💪 crushing it!";
        message.setMessageText(specialMsg);
        assertEquals(specialMsg, message.getMessageText());
    }

    @Test
    @DisplayName("Handles message with newlines")
    void testMessageWithNewlines() {
        String multiLine = "Line 1\nLine 2\nLine 3";
        message.setMessageText(multiLine);
        assertEquals(multiLine, message.getMessageText());
    }

    @Test
    @DisplayName("Handles exactly 50 character message")
    void testExactly50CharMessage() {
        String exact50 = "12345678901234567890123456789012345678901234567890"; // 50 chars
        message.setMessageText(exact50);
        assertEquals(exact50, message.getMessagePreview());
    }

    @Test
    @DisplayName("Handles 51 character message")
    void testExactly51CharMessage() {
        String char51 = "123456789012345678901234567890123456789012345678901"; // 51 chars
        message.setMessageText(char51);
        
        String preview = message.getMessagePreview();
        assertEquals(50, preview.length());
        assertTrue(preview.endsWith("..."));
    }

    // =====================================================
    // EQUALS AND HASHCODE TESTS
    // =====================================================

    @Test
    @DisplayName("equals() returns true for same messageId")
    void testEqualsReturnsTrueForSameId() {
        BrixMessage msg1 = new BrixMessage();
        msg1.setMessageId(1L);
        
        BrixMessage msg2 = new BrixMessage();
        msg2.setMessageId(1L);
        
        assertEquals(msg1, msg2);
    }

    @Test
    @DisplayName("equals() returns false for different messageIds")
    void testEqualsReturnsFalseForDifferentIds() {
        BrixMessage msg1 = new BrixMessage();
        msg1.setMessageId(1L);
        
        BrixMessage msg2 = new BrixMessage();
        msg2.setMessageId(2L);
        
        assertNotEquals(msg1, msg2);
    }

    @Test
    @DisplayName("equals() returns true when comparing to itself")
    void testEqualsReturnsTrueForSameObject() {
        assertEquals(message, message);
    }

    @Test
    @DisplayName("equals() returns false when comparing to null")
    void testEqualsReturnsFalseForNull() {
        assertNotEquals(null, message);
    }

    @Test
    @DisplayName("hashCode() is same for messages with same messageId")
    void testHashCodeSameForSameId() {
        BrixMessage msg1 = new BrixMessage();
        msg1.setMessageId(1L);
        
        BrixMessage msg2 = new BrixMessage();
        msg2.setMessageId(1L);
        
        assertEquals(msg1.hashCode(), msg2.hashCode());
    }

    // =====================================================
    // TOSTRING TESTS
    // =====================================================

    @Test
    @DisplayName("toString() contains key fields")
    void testToStringContainsKeyFields() {
        message.setMessageId(1L);
        
        String result = message.toString();
        
        assertTrue(result.contains("messageId=1"));
        assertTrue(result.contains("MOTIVATION"));
        assertTrue(result.contains("ENCOURAGING"));
        assertTrue(result.contains("workout_completed"));
    }
}