package com.b3.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import com.b3.model.WorkoutSession.CompletionStatus;
import com.b3.model.UserProfile.FitnessLevel;
import com.b3.model.UserProfile.PrimaryGoal;
import com.b3.model.Workout.WorkoutType;
import com.b3.model.Workout.DifficultyLevel;

import java.time.LocalDateTime;
import java.time.Duration;

/**
 * Unit tests for WorkoutSession entity
 */
@DisplayName("WorkoutSession Entity Tests")
class WorkoutSessionTest {

    private WorkoutSession session;
    private UserProfile userProfile;
    private Workout workout;

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

        // Create Workout
        workout = new Workout(
            "Morning Strength",
            "Full body workout",
            WorkoutType.STRENGTH,
            DifficultyLevel.BEGINNER,
            30,
            "Dumbbells"
        );
        workout.setWorkoutId(1L);

        // Create WorkoutSession
        LocalDateTime startTime = LocalDateTime.now().minusMinutes(30);
        session = new WorkoutSession(userProfile, workout, startTime);
    }

    // =====================================================
    // CONSTRUCTOR TESTS
    // =====================================================

    @Test
    @DisplayName("Should create WorkoutSession with required fields")
    void testConstructorWithRequiredFields() {
        assertNotNull(session.getUserProfile());
        assertNotNull(session.getWorkout());
        assertNotNull(session.getStartTime());
        assertEquals(CompletionStatus.IN_PROGRESS, session.getCompletionStatus());
    }

    @Test
    @DisplayName("Should create empty WorkoutSession with default constructor")
    void testDefaultConstructor() {
        WorkoutSession empty = new WorkoutSession();
        
        assertNotNull(empty);
        assertNull(empty.getUserProfile());
        assertNull(empty.getWorkout());
    }

    // =====================================================
    // RELATIONSHIP TESTS
    // =====================================================

    @Test
    @DisplayName("Should maintain relationship with UserProfile")
    void testUserProfileRelationship() {
        assertEquals(userProfile, session.getUserProfile());
        assertEquals("TestUser", session.getUserProfile().getDisplayName());
    }

    @Test
    @DisplayName("Should maintain relationship with Workout")
    void testWorkoutRelationship() {
        assertEquals(workout, session.getWorkout());
        assertEquals("Morning Strength", session.getWorkout().getName());
    }

    // =====================================================
    // ENUM TESTS
    // =====================================================

    @Test
    @DisplayName("Should handle all CompletionStatus enum values")
    void testCompletionStatusEnum() {
        session.setCompletionStatus(CompletionStatus.COMPLETED);
        assertEquals(CompletionStatus.COMPLETED, session.getCompletionStatus());

        session.setCompletionStatus(CompletionStatus.PARTIAL);
        assertEquals(CompletionStatus.PARTIAL, session.getCompletionStatus());

        session.setCompletionStatus(CompletionStatus.SKIPPED);
        assertEquals(CompletionStatus.SKIPPED, session.getCompletionStatus());

        session.setCompletionStatus(CompletionStatus.IN_PROGRESS);
        assertEquals(CompletionStatus.IN_PROGRESS, session.getCompletionStatus());
    }

    // =====================================================
    // BUSINESS LOGIC TESTS
    // =====================================================

    @Test
    @DisplayName("completeSession() sets end time and status to COMPLETED")
    void testCompleteSession() {
        LocalDateTime beforeComplete = LocalDateTime.now();
        
        session.completeSession(4);
        
        assertEquals(CompletionStatus.COMPLETED, session.getCompletionStatus());
        assertNotNull(session.getEndTime());
        assertTrue(session.getEndTime().isAfter(beforeComplete) || session.getEndTime().isEqual(beforeComplete));
        assertEquals(4, session.getPerceivedDifficulty());
    }

    @Test
    @DisplayName("skipSession() sets status to SKIPPED without end time")
    void testSkipSession() {
        session.skipSession();
        
        assertEquals(CompletionStatus.SKIPPED, session.getCompletionStatus());
        assertNull(session.getEndTime());
    }

    @Test
    @DisplayName("isCompleted() returns true when status is COMPLETED")
    void testIsCompletedTrue() {
        session.completeSession(3);
        assertTrue(session.isCompleted());
    }

    @Test
    @DisplayName("isCompleted() returns false when not completed")
    void testIsCompletedFalse() {
        assertFalse(session.isCompleted());
        
        session.skipSession();
        assertFalse(session.isCompleted());
    }

    @Test
    @DisplayName("isInProgress() returns true when status is IN_PROGRESS")
    void testIsInProgressTrue() {
        assertTrue(session.isInProgress());
    }

    @Test
    @DisplayName("isInProgress() returns false when completed or skipped")
    void testIsInProgressFalse() {
        session.completeSession(3);
        assertFalse(session.isInProgress());
        
        session.setCompletionStatus(CompletionStatus.SKIPPED);
        assertFalse(session.isInProgress());
    }

    @Test
    @DisplayName("getDurationMinutes() calculates correct duration")
    void testGetDurationMinutes() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 1, 10, 30);
        
        session.setStartTime(start);
        session.setEndTime(end);
        
        assertEquals(30, session.getDurationMinutes());
    }

    @Test
    @DisplayName("getDurationMinutes() returns 0 when no end time")
    void testGetDurationMinutesNoEndTime() {
        session.setEndTime(null);
        assertEquals(0, session.getDurationMinutes());
    }

    @Test
    @DisplayName("getDurationMinutes() returns 0 when no start time")
    void testGetDurationMinutesNoStartTime() {
        session.setStartTime(null);
        assertEquals(0, session.getDurationMinutes());
    }

    @Test
    @DisplayName("wasHarderThanExpected() returns true when difficulty > 3")
    void testWasHarderThanExpectedTrue() {
        session.setPerceivedDifficulty(4);
        assertTrue(session.wasHarderThanExpected());
        
        session.setPerceivedDifficulty(5);
        assertTrue(session.wasHarderThanExpected());
    }

    @Test
    @DisplayName("wasHarderThanExpected() returns false when difficulty <= 3")
    void testWasHarderThanExpectedFalse() {
        session.setPerceivedDifficulty(3);
        assertFalse(session.wasHarderThanExpected());
        
        session.setPerceivedDifficulty(1);
        assertFalse(session.wasHarderThanExpected());
    }

    @Test
    @DisplayName("wasHarderThanExpected() returns false when difficulty is null")
    void testWasHarderThanExpectedNull() {
        session.setPerceivedDifficulty(null);
        assertFalse(session.wasHarderThanExpected());
    }

    @Test
    @DisplayName("wasEasierThanExpected() returns true when difficulty < 3")
    void testWasEasierThanExpectedTrue() {
        session.setPerceivedDifficulty(1);
        assertTrue(session.wasEasierThanExpected());
        
        session.setPerceivedDifficulty(2);
        assertTrue(session.wasEasierThanExpected());
    }

    @Test
    @DisplayName("wasEasierThanExpected() returns false when difficulty >= 3")
    void testWasEasierThanExpectedFalse() {
        session.setPerceivedDifficulty(3);
        assertFalse(session.wasEasierThanExpected());
        
        session.setPerceivedDifficulty(5);
        assertFalse(session.wasEasierThanExpected());
    }

    // =====================================================
    // VALIDATION TESTS
    // =====================================================

    @Test
    @DisplayName("Should handle perceived difficulty range 1-5")
    void testPerceivedDifficultyRange() {
        session.setPerceivedDifficulty(1);
        assertEquals(1, session.getPerceivedDifficulty());
        
        session.setPerceivedDifficulty(5);
        assertEquals(5, session.getPerceivedDifficulty());
    }

    @Test
    @DisplayName("Should handle null perceived difficulty")
    void testNullPerceivedDifficulty() {
        session.setPerceivedDifficulty(null);
        assertNull(session.getPerceivedDifficulty());
    }

    // =====================================================
    // EDGE CASE TESTS
    // =====================================================

    @Test
    @DisplayName("Handles same start and end time (instant completion)")
    void testInstantCompletion() {
        LocalDateTime now = LocalDateTime.now();
        session.setStartTime(now);
        session.setEndTime(now);
        
        assertEquals(0, session.getDurationMinutes());
    }

    @Test
    @DisplayName("Handles very long workout duration")
    void testLongDuration() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 1, 13, 0); // 3 hours
        
        session.setStartTime(start);
        session.setEndTime(end);
        
        assertEquals(180, session.getDurationMinutes());
    }

    @Test
    @DisplayName("completeSession() can be called multiple times")
    void testMultipleCompletions() {
        session.completeSession(3);
        LocalDateTime firstEndTime = session.getEndTime();
        
        // Simulate updating the difficulty rating
        session.completeSession(50);
        
        assertEquals(CompletionStatus.COMPLETED, session.getCompletionStatus());
        assertEquals(4, session.getPerceivedDifficulty());
        // End time should be updated
        assertNotEquals(firstEndTime, session.getEndTime());
    }

    // =====================================================
    // EQUALS AND HASHCODE TESTS
    // =====================================================

    @Test
    @DisplayName("equals() returns true for same sessionId")
    void testEqualsReturnsTrueForSameId() {
        WorkoutSession session1 = new WorkoutSession();
        session1.setSessionId(1L);
        
        WorkoutSession session2 = new WorkoutSession();
        session2.setSessionId(1L);
        
        assertEquals(session1, session2);
    }

    @Test
    @DisplayName("equals() returns false for different sessionIds")
    void testEqualsReturnsFalseForDifferentIds() {
        WorkoutSession session1 = new WorkoutSession();
        session1.setSessionId(1L);
        
        WorkoutSession session2 = new WorkoutSession();
        session2.setSessionId(2L);
        
        assertNotEquals(session1, session2);
    }

    @Test
    @DisplayName("equals() returns true when comparing to itself")
    void testEqualsReturnsTrueForSameObject() {
        assertEquals(session, session);
    }

    @Test
    @DisplayName("equals() returns false when comparing to null")
    void testEqualsReturnsFalseForNull() {
        assertNotEquals(null, session);
    }

    @Test
    @DisplayName("hashCode() is same for sessions with same sessionId")
    void testHashCodeSameForSameId() {
        WorkoutSession session1 = new WorkoutSession();
        session1.setSessionId(1L);
        
        WorkoutSession session2 = new WorkoutSession();
        session2.setSessionId(1L);
        
        assertEquals(session1.hashCode(), session2.hashCode());
    }

    // =====================================================
    // TOSTRING TESTS
    // =====================================================

    @Test
    @DisplayName("toString() contains key fields")
    void testToStringContainsKeyFields() {
        session.setSessionId(1L);
        session.completeSession(45);
        
        String result = session.toString();
        
        assertTrue(result.contains("sessionId=1"));
        assertTrue(result.contains("COMPLETED"));
        assertTrue(result.contains("perceivedDifficulty=4"));
    }
}