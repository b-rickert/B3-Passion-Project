package com.b3.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import com.b3.model.Brick.BrickType;
import com.b3.model.UserProfile.FitnessLevel;
import com.b3.model.UserProfile.PrimaryGoal;
import com.b3.model.Workout.WorkoutType;
import com.b3.model.Workout.DifficultyLevel;
import com.b3.model.WorkoutSession.CompletionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Unit tests for Brick entity
 */
@DisplayName("Brick Entity Tests")
class BrickTest {

    private Brick brick;
    private UserProfile userProfile;
    private WorkoutSession workoutSession;

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
        Workout workout = new Workout(
            "Morning Strength",
            "Full body workout",
            WorkoutType.STRENGTH,
            DifficultyLevel.BEGINNER,
            30,
            "Dumbbells"
        );
        workout.setWorkoutId(1L);

        // Create WorkoutSession
        workoutSession = new WorkoutSession(userProfile, workout, LocalDateTime.now().minusMinutes(30));
        workoutSession.setSessionId(1L);
        workoutSession.completeSession(3);

        // Create Brick
        brick = new Brick(userProfile, workoutSession, LocalDate.now(), BrickType.WORKOUT);
    }

    // =====================================================
    // CONSTRUCTOR TESTS
    // =====================================================

    @Test
    @DisplayName("Should create Brick with all required fields")
    void testConstructorWithAllFields() {
        assertEquals(userProfile, brick.getUserProfile());
        assertEquals(workoutSession, brick.getWorkoutSession());
        assertEquals(LocalDate.now(), brick.getBrickDate());
        assertEquals(BrickType.WORKOUT, brick.getBrickType());
    }

    @Test
    @DisplayName("Should create empty Brick with default constructor")
    void testDefaultConstructor() {
        Brick empty = new Brick();
        
        assertNotNull(empty);
        assertNull(empty.getUserProfile());
        assertNull(empty.getWorkoutSession());
    }

    // =====================================================
    // RELATIONSHIP TESTS
    // =====================================================

    @Test
    @DisplayName("Should maintain relationship with UserProfile")
    void testUserProfileRelationship() {
        assertEquals(userProfile, brick.getUserProfile());
        assertEquals("TestUser", brick.getUserProfile().getDisplayName());
    }

    @Test
    @DisplayName("Should maintain relationship with WorkoutSession")
    void testWorkoutSessionRelationship() {
        assertEquals(workoutSession, brick.getWorkoutSession());
        assertEquals(CompletionStatus.COMPLETED, brick.getWorkoutSession().getCompletionStatus());
    }

    // =====================================================
    // ENUM TESTS
    // =====================================================

    @Test
    @DisplayName("Should handle all BrickType enum values")
    void testBrickTypeEnum() {
        brick.setBrickType(BrickType.WORKOUT);
        assertEquals(BrickType.WORKOUT, brick.getBrickType());

        brick.setBrickType(BrickType.MILESTONE);
        assertEquals(BrickType.MILESTONE, brick.getBrickType());
    }

    // =====================================================
    // BUSINESS LOGIC TESTS
    // =====================================================

    @Test
    @DisplayName("isWorkoutBrick() returns true for WORKOUT type")
    void testIsWorkoutBrickTrue() {
        assertTrue(brick.isWorkoutBrick());
    }

    @Test
    @DisplayName("isWorkoutBrick() returns false for MILESTONE type")
    void testIsWorkoutBrickFalse() {
        brick.setBrickType(BrickType.MILESTONE);
        assertFalse(brick.isWorkoutBrick());
    }

    @Test
    @DisplayName("isMilestoneBrick() returns true for MILESTONE type")
    void testIsMilestoneBrickTrue() {
        brick.setBrickType(BrickType.MILESTONE);
        assertTrue(brick.isMilestoneBrick());
    }

    @Test
    @DisplayName("isMilestoneBrick() returns false for WORKOUT type")
    void testIsMilestoneBrickFalse() {
        assertFalse(brick.isMilestoneBrick());
    }

    @Test
    @DisplayName("isFromToday() returns true for today's date")
    void testIsFromTodayTrue() {
        brick.setBrickDate(LocalDate.now());
        assertTrue(brick.isFromToday());
    }

    @Test
    @DisplayName("isFromToday() returns false for past date")
    void testIsFromTodayFalse() {
        brick.setBrickDate(LocalDate.now().minusDays(1));
        assertFalse(brick.isFromToday());
    }

    @Test
    @DisplayName("isFromThisWeek() returns true for date within current week")
    void testIsFromThisWeekTrue() {
        brick.setBrickDate(LocalDate.now());
        assertTrue(brick.isFromThisWeek());

        brick.setBrickDate(LocalDate.now().minusDays(3));
        assertTrue(brick.isFromThisWeek());
    }

    @Test
    @DisplayName("isFromThisWeek() returns false for date outside current week")
    void testIsFromThisWeekFalse() {
        brick.setBrickDate(LocalDate.now().minusDays(10));
        assertFalse(brick.isFromThisWeek());
    }

    @Test
    @DisplayName("isFromThisMonth() returns true for date within current month")
    void testIsFromThisMonthTrue() {
        brick.setBrickDate(LocalDate.now());
        assertTrue(brick.isFromThisMonth());

        brick.setBrickDate(LocalDate.now().minusDays(15));
        assertTrue(brick.isFromThisMonth());
    }

    @Test
    @DisplayName("isFromThisMonth() returns false for date outside current month")
    void testIsFromThisMonthFalse() {
        brick.setBrickDate(LocalDate.now().minusMonths(1));
        assertFalse(brick.isFromThisMonth());
    }

    @Test
    @DisplayName("getDaysAgo() returns correct number of days")
    void testGetDaysAgo() {
        brick.setBrickDate(LocalDate.now().minusDays(5));
        assertEquals(5, brick.getDaysAgo());

        brick.setBrickDate(LocalDate.now());
        assertEquals(0, brick.getDaysAgo());
    }

    @Test
    @DisplayName("getWorkoutName() returns workout name from session")
    void testGetWorkoutName() {
        assertEquals("Morning Strength", brick.getWorkoutName());
    }

    @Test
    @DisplayName("getWorkoutName() returns 'Unknown' when session is null")
    void testGetWorkoutNameNull() {
        brick.setWorkoutSession(null);
        assertEquals("Unknown", brick.getWorkoutName());
    }

    // =====================================================
    // DATE VALIDATION TESTS
    // =====================================================

    @Test
    @DisplayName("Should handle past dates")
    void testPastDate() {
        LocalDate pastDate = LocalDate.of(2023, 1, 15);
        brick.setBrickDate(pastDate);
        assertEquals(pastDate, brick.getBrickDate());
    }

    @Test
    @DisplayName("Should handle future dates")
    void testFutureDate() {
        LocalDate futureDate = LocalDate.now().plusDays(5);
        brick.setBrickDate(futureDate);
        assertEquals(futureDate, brick.getBrickDate());
    }

    // =====================================================
    // EDGE CASE TESTS
    // =====================================================

    @Test
    @DisplayName("Handles brick from exactly 7 days ago")
    void testSevenDaysAgo() {
        brick.setBrickDate(LocalDate.now().minusDays(7));
        assertEquals(7, brick.getDaysAgo());
    }

    @Test
    @DisplayName("Handles brick from exactly 30 days ago")
    void testThirtyDaysAgo() {
        brick.setBrickDate(LocalDate.now().minusDays(30));
        assertEquals(30, brick.getDaysAgo());
    }

    @Test
    @DisplayName("isFromThisWeek() handles week boundaries correctly")
    void testWeekBoundaries() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        
        brick.setBrickDate(startOfWeek);
        assertTrue(brick.isFromThisWeek());
    }

    @Test
    @DisplayName("isFromThisMonth() handles month boundaries correctly")
    void testMonthBoundaries() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        
        brick.setBrickDate(startOfMonth);
        assertTrue(brick.isFromThisMonth());
    }

    // =====================================================
    // EQUALS AND HASHCODE TESTS
    // =====================================================

    @Test
    @DisplayName("equals() returns true for same brickId")
    void testEqualsReturnsTrueForSameId() {
        Brick brick1 = new Brick();
        brick1.setBrickId(1L);
        
        Brick brick2 = new Brick();
        brick2.setBrickId(1L);
        
        assertEquals(brick1, brick2);
    }

    @Test
    @DisplayName("equals() returns false for different brickIds")
    void testEqualsReturnsFalseForDifferentIds() {
        Brick brick1 = new Brick();
        brick1.setBrickId(1L);
        
        Brick brick2 = new Brick();
        brick2.setBrickId(2L);
        
        assertNotEquals(brick1, brick2);
    }

    @Test
    @DisplayName("equals() returns true when comparing to itself")
    void testEqualsReturnsTrueForSameObject() {
        assertEquals(brick, brick);
    }

    @Test
    @DisplayName("equals() returns false when comparing to null")
    void testEqualsReturnsFalseForNull() {
        assertNotEquals(null, brick);
    }

    @Test
    @DisplayName("hashCode() is same for bricks with same brickId")
    void testHashCodeSameForSameId() {
        Brick brick1 = new Brick();
        brick1.setBrickId(1L);
        
        Brick brick2 = new Brick();
        brick2.setBrickId(1L);
        
        assertEquals(brick1.hashCode(), brick2.hashCode());
    }

    // =====================================================
    // TOSTRING TESTS
    // =====================================================

    @Test
    @DisplayName("toString() contains key fields")
    void testToStringContainsKeyFields() {
        brick.setBrickId(1L);
        
        String result = brick.toString();
        
        assertTrue(result.contains("brickId=1"));
        assertTrue(result.contains("WORKOUT"));
        assertTrue(result.contains(LocalDate.now().toString()));
    }
}