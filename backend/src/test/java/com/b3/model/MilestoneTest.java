package com.b3.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import com.b3.model.Milestone.MilestoneType;
import com.b3.model.UserProfile.FitnessLevel;
import com.b3.model.UserProfile.PrimaryGoal;

import java.time.LocalDateTime;

/**
 * Unit tests for Milestone entity
 */
@DisplayName("Milestone Entity Tests")
class MilestoneTest {

    private Milestone milestone;
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

        // Create Milestone (7-day streak)
        milestone = new Milestone(
            userProfile,
            "7-Day Streak!",
            "You worked out 7 days in a row!",
            MilestoneType.STREAK,
            7
        );
    }

    // =====================================================
    // CONSTRUCTOR TESTS
    // =====================================================

    @Test
    @DisplayName("Should create Milestone with all required fields")
    void testConstructorWithAllFields() {
        assertEquals(userProfile, milestone.getUserProfile());
        assertEquals("7-Day Streak!", milestone.getMilestoneName());
        assertEquals("You worked out 7 days in a row!", milestone.getDescription());
        assertEquals(MilestoneType.STREAK, milestone.getMilestoneType());
        assertEquals(7, milestone.getTargetValue());
        assertEquals(0, milestone.getCurrentValue());
        assertFalse(milestone.getIsAchieved());
    }

    @Test
    @DisplayName("Should create empty Milestone with default constructor")
    void testDefaultConstructor() {
        Milestone empty = new Milestone();
        
        assertNotNull(empty);
        assertNull(empty.getUserProfile());
        assertNull(empty.getMilestoneName());
    }

    // =====================================================
    // RELATIONSHIP TESTS
    // =====================================================

    @Test
    @DisplayName("Should maintain relationship with UserProfile")
    void testUserProfileRelationship() {
        assertEquals(userProfile, milestone.getUserProfile());
        assertEquals("TestUser", milestone.getUserProfile().getDisplayName());
    }

    // =====================================================
    // ENUM TESTS
    // =====================================================

    @Test
    @DisplayName("Should handle all MilestoneType enum values")
    void testMilestoneTypeEnum() {
        milestone.setMilestoneType(MilestoneType.STREAK);
        assertEquals(MilestoneType.STREAK, milestone.getMilestoneType());

        milestone.setMilestoneType(MilestoneType.WORKOUT_COUNT);
        assertEquals(MilestoneType.WORKOUT_COUNT, milestone.getMilestoneType());

        milestone.setMilestoneType(MilestoneType.GOAL_ACHIEVED);
        assertEquals(MilestoneType.GOAL_ACHIEVED, milestone.getMilestoneType());

        milestone.setMilestoneType(MilestoneType.CONSISTENCY);
        assertEquals(MilestoneType.CONSISTENCY, milestone.getMilestoneType());

        milestone.setMilestoneType(MilestoneType.PERSONAL_RECORD);
        assertEquals(MilestoneType.PERSONAL_RECORD, milestone.getMilestoneType());
    }

    // =====================================================
    // BUSINESS LOGIC TESTS - COMPLETION
    // =====================================================

    @Test
    @DisplayName("isCompleted() returns false for new milestone")
    void testIsCompletedFalseForNew() {
        assertFalse(milestone.isCompleted());
    }

    @Test
    @DisplayName("isCompleted() returns true when marked as achieved")
    void testIsCompletedTrueWhenAchieved() {
        milestone.markAsAchieved();
        assertTrue(milestone.isCompleted());
    }

    @Test
    @DisplayName("isInProgress() returns false for new milestone")
    void testIsInProgressFalseForNew() {
        assertFalse(milestone.isInProgress());
    }

    @Test
    @DisplayName("isInProgress() returns true when partially complete")
    void testIsInProgressTrueWhenPartial() {
        milestone.setCurrentValue(3);
        assertTrue(milestone.isInProgress());
    }

    @Test
    @DisplayName("isInProgress() returns false when completed")
    void testIsInProgressFalseWhenCompleted() {
        milestone.markAsAchieved();
        assertFalse(milestone.isInProgress());
    }

    // =====================================================
    // BUSINESS LOGIC TESTS - PROGRESS
    // =====================================================

    @Test
    @DisplayName("getProgressPercentage() returns 0.0 for new milestone")
    void testProgressPercentageZero() {
        assertEquals(0.0, milestone.getProgressPercentage(), 0.001);
    }

    @Test
    @DisplayName("getProgressPercentage() calculates correctly")
    void testProgressPercentageCalculation() {
        milestone.setCurrentValue(3);
        assertEquals(3.0/7.0, milestone.getProgressPercentage(), 0.001);
    }

    @Test
    @DisplayName("getProgressPercentage() returns 1.0 when completed")
    void testProgressPercentageComplete() {
        milestone.setCurrentValue(7);
        assertEquals(1.0, milestone.getProgressPercentage(), 0.001);
    }

    @Test
    @DisplayName("getProgressPercentage() caps at 1.0 even if exceeded")
    void testProgressPercentageCapped() {
        milestone.setCurrentValue(10);
        assertEquals(1.0, milestone.getProgressPercentage(), 0.001);
    }

    @Test
    @DisplayName("getProgressPercentageString() formats correctly")
    void testProgressPercentageString() {
        milestone.setCurrentValue(5);
        assertEquals("71%", milestone.getProgressPercentageString());
    }

    @Test
    @DisplayName("getProgressPercentageString() returns 0% for new milestone")
    void testProgressPercentageStringZero() {
        assertEquals("0%", milestone.getProgressPercentageString());
    }

    @Test
    @DisplayName("getProgressPercentageString() returns 100% when complete")
    void testProgressPercentageStringComplete() {
        milestone.setCurrentValue(7);
        assertEquals("100%", milestone.getProgressPercentageString());
    }

    // =====================================================
    // BUSINESS LOGIC TESTS - ACHIEVEMENT
    // =====================================================

    @Test
    @DisplayName("markAsAchieved() sets isAchieved to true")
    void testMarkAsAchievedSetsFlag() {
        milestone.markAsAchieved();
        assertTrue(milestone.getIsAchieved());
    }

    @Test
    @DisplayName("markAsAchieved() sets achievedAt timestamp")
    void testMarkAsAchievedSetsTimestamp() {
        milestone.markAsAchieved();
        assertNotNull(milestone.getAchievedAt());
    }

    @Test
    @DisplayName("markAsAchieved() sets currentValue to targetValue")
    void testMarkAsAchievedSetsProgress() {
        milestone.setCurrentValue(5);
        milestone.markAsAchieved();
        assertEquals(7, milestone.getCurrentValue());
    }

    // =====================================================
    // BUSINESS LOGIC TESTS - UPDATE PROGRESS
    // =====================================================

    @Test
    @DisplayName("updateProgress() updates currentValue")
    void testUpdateProgressUpdatesValue() {
        milestone.updateProgress(3);
        assertEquals(3, milestone.getCurrentValue());
    }

    @Test
    @DisplayName("updateProgress() auto-achieves when target reached")
    void testUpdateProgressAutoAchieves() {
        milestone.updateProgress(7);
        assertTrue(milestone.isCompleted());
        assertNotNull(milestone.getAchievedAt());
    }

    @Test
    @DisplayName("updateProgress() ignores null values")
    void testUpdateProgressIgnoresNull() {
        milestone.setCurrentValue(3);
        milestone.updateProgress(null);
        assertEquals(3, milestone.getCurrentValue());
    }

    @Test
    @DisplayName("updateProgress() ignores negative values")
    void testUpdateProgressIgnoresNegative() {
        milestone.setCurrentValue(3);
        milestone.updateProgress(-5);
        assertEquals(3, milestone.getCurrentValue());
    }

    @Test
    @DisplayName("incrementProgress() increases by 1")
    void testIncrementProgress() {
        milestone.setCurrentValue(3);
        milestone.incrementProgress();
        assertEquals(4, milestone.getCurrentValue());
    }

    @Test
    @DisplayName("incrementProgress() auto-achieves when reaching target")
    void testIncrementProgressAutoAchieves() {
        milestone.setCurrentValue(6);
        milestone.incrementProgress();
        assertTrue(milestone.isCompleted());
    }

    // =====================================================
    // BUSINESS LOGIC TESTS - ALMOST COMPLETE
    // =====================================================

    @Test
    @DisplayName("isAlmostComplete() returns false at 0% progress")
    void testIsAlmostCompleteFalseAtZero() {
        assertFalse(milestone.isAlmostComplete());
    }

    @Test
    @DisplayName("isAlmostComplete() returns false at 50% progress")
    void testIsAlmostCompleteFalseAtHalf() {
        milestone.setCurrentValue(3);
        assertFalse(milestone.isAlmostComplete());
    }

    @Test
    @DisplayName("isAlmostComplete() returns true at 80% progress")
    void testIsAlmostCompleteTrueAt80() {
        milestone.setCurrentValue(6); // 6/7 = 85.7%
        assertTrue(milestone.isAlmostComplete());
    }

    @Test
    @DisplayName("isAlmostComplete() returns false when completed")
    void testIsAlmostCompleteFalseWhenDone() {
        milestone.markAsAchieved();
        assertFalse(milestone.isAlmostComplete());
    }

    // =====================================================
    // BUSINESS LOGIC TESTS - REMAINING VALUE
    // =====================================================

    @Test
    @DisplayName("getRemainingValue() returns target for new milestone")
    void testRemainingValueAtStart() {
        assertEquals(7, milestone.getRemainingValue());
    }

    @Test
    @DisplayName("getRemainingValue() calculates correctly")
    void testRemainingValueCalculation() {
        milestone.setCurrentValue(3);
        assertEquals(4, milestone.getRemainingValue());
    }

    @Test
    @DisplayName("getRemainingValue() returns 0 when complete")
    void testRemainingValueZeroWhenComplete() {
        milestone.setCurrentValue(7);
        assertEquals(0, milestone.getRemainingValue());
    }

    @Test
    @DisplayName("getRemainingValue() returns 0 when exceeded")
    void testRemainingValueZeroWhenExceeded() {
        milestone.setCurrentValue(10);
        assertEquals(0, milestone.getRemainingValue());
    }

    // =====================================================
    // EDGE CASE TESTS
    // =====================================================

    @Test
    @DisplayName("Handles milestone with target value of 1")
    void testTargetValueOfOne() {
        Milestone single = new Milestone(
            userProfile,
            "First Workout",
            "Complete your first workout",
            MilestoneType.WORKOUT_COUNT,
            1
        );
        
        single.incrementProgress();
        assertTrue(single.isCompleted());
        assertEquals("100%", single.getProgressPercentageString());
    }

    @Test
    @DisplayName("Handles milestone with large target value")
    void testLargeTargetValue() {
        Milestone large = new Milestone(
            userProfile,
            "500 Workouts",
            "Complete 500 total workouts",
            MilestoneType.WORKOUT_COUNT,
            500
        );
        
        large.setCurrentValue(250);
        assertEquals(0.5, large.getProgressPercentage(), 0.001);
        assertEquals("50%", large.getProgressPercentageString());
    }

    @Test
    @DisplayName("Handles null currentValue in getProgressPercentage")
    void testNullCurrentValue() {
        milestone.setCurrentValue(null);
        assertEquals(0.0, milestone.getProgressPercentage(), 0.001);
    }

    @Test
    @DisplayName("Handles null targetValue in getProgressPercentage")
    void testNullTargetValue() {
        milestone.setTargetValue(null);
        assertEquals(0.0, milestone.getProgressPercentage(), 0.001);
    }

    @Test
    @DisplayName("Handles zero targetValue in getProgressPercentage")
    void testZeroTargetValue() {
        milestone.setTargetValue(0);
        assertEquals(0.0, milestone.getProgressPercentage(), 0.001);
    }

    // =====================================================
    // EQUALS AND HASHCODE TESTS
    // =====================================================

    @Test
    @DisplayName("equals() returns true for same milestoneId")
    void testEqualsReturnsTrueForSameId() {
        Milestone m1 = new Milestone();
        m1.setMilestoneId(1L);
        
        Milestone m2 = new Milestone();
        m2.setMilestoneId(1L);
        
        assertEquals(m1, m2);
    }

    @Test
    @DisplayName("equals() returns false for different milestoneIds")
    void testEqualsReturnsFalseForDifferentIds() {
        Milestone m1 = new Milestone();
        m1.setMilestoneId(1L);
        
        Milestone m2 = new Milestone();
        m2.setMilestoneId(2L);
        
        assertNotEquals(m1, m2);
    }

    @Test
    @DisplayName("equals() returns true when comparing to itself")
    void testEqualsReturnsTrueForSameObject() {
        assertEquals(milestone, milestone);
    }

    @Test
    @DisplayName("equals() returns false when comparing to null")
    void testEqualsReturnsFalseForNull() {
        assertNotEquals(null, milestone);
    }

    @Test
    @DisplayName("hashCode() is same for milestones with same milestoneId")
    void testHashCodeSameForSameId() {
        Milestone m1 = new Milestone();
        m1.setMilestoneId(1L);
        
        Milestone m2 = new Milestone();
        m2.setMilestoneId(1L);
        
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    // =====================================================
    // TOSTRING TESTS
    // =====================================================

    @Test
    @DisplayName("toString() contains key fields")
    void testToStringContainsKeyFields() {
        milestone.setMilestoneId(1L);
        milestone.setCurrentValue(3);
        
        String result = milestone.toString();
        
        assertTrue(result.contains("milestoneId=1"));
        assertTrue(result.contains("7-Day Streak!"));
        assertTrue(result.contains("STREAK"));
        assertTrue(result.contains("3/7"));
    }

    @Test
    @DisplayName("toString() shows achievement status")
    void testToStringShowsAchievement() {
        milestone.setMilestoneId(1L);
        milestone.markAsAchieved();
        
        String result = milestone.toString();
        
        assertTrue(result.contains("isAchieved=true"));
    }
}