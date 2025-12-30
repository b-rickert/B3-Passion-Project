package com.b3.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import com.b3.model.DailyLog.Mood;
import com.b3.model.UserProfile.FitnessLevel;
import com.b3.model.UserProfile.PrimaryGoal;

import java.time.LocalDate;

/**
 * Unit tests for DailyLog entity
 */
@DisplayName("DailyLog Entity Tests")
class DailyLogTest {

    private DailyLog dailyLog;
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

        // Create DailyLog
        dailyLog = new DailyLog(
            userProfile,
            LocalDate.now(),
            4,  // energyLevel
            2,  // stressLevel
            4,  // sleepQuality
            Mood.GOOD
        );
    }

    // =====================================================
    // CONSTRUCTOR TESTS
    // =====================================================

    @Test
    @DisplayName("Should create DailyLog with all required fields")
    void testConstructorWithAllFields() {
        assertEquals(userProfile, dailyLog.getUserProfile());
        assertEquals(LocalDate.now(), dailyLog.getLogDate());
        assertEquals(4, dailyLog.getEnergyLevel());
        assertEquals(2, dailyLog.getStressLevel());
        assertEquals(4, dailyLog.getSleepQuality());
        assertEquals(Mood.GOOD, dailyLog.getMood());
    }

    @Test
    @DisplayName("Should create empty DailyLog with default constructor")
    void testDefaultConstructor() {
        DailyLog empty = new DailyLog();
        
        assertNotNull(empty);
        assertNull(empty.getUserProfile());
        assertNull(empty.getLogDate());
    }

    // =====================================================
    // RELATIONSHIP TESTS
    // =====================================================

    @Test
    @DisplayName("Should maintain relationship with UserProfile")
    void testUserProfileRelationship() {
        assertEquals(userProfile, dailyLog.getUserProfile());
        assertEquals("TestUser", dailyLog.getUserProfile().getDisplayName());
    }

    // =====================================================
    // ENUM TESTS
    // =====================================================

    @Test
    @DisplayName("Should handle all Mood enum values")
    void testMoodEnum() {
        dailyLog.setMood(Mood.GREAT);
        assertEquals(Mood.GREAT, dailyLog.getMood());

        dailyLog.setMood(Mood.GOOD);
        assertEquals(Mood.GOOD, dailyLog.getMood());

        dailyLog.setMood(Mood.OKAY);
        assertEquals(Mood.OKAY, dailyLog.getMood());

        dailyLog.setMood(Mood.LOW);
        assertEquals(Mood.LOW, dailyLog.getMood());

        dailyLog.setMood(Mood.STRESSED);
        assertEquals(Mood.STRESSED, dailyLog.getMood());
    }

    // =====================================================
    // BUSINESS LOGIC TESTS
    // =====================================================

    @Test
    @DisplayName("isHighEnergy() returns true when energy >= 4")
    void testIsHighEnergyTrue() {
        dailyLog.setEnergyLevel(4);
        assertTrue(dailyLog.isHighEnergy());

        dailyLog.setEnergyLevel(5);
        assertTrue(dailyLog.isHighEnergy());
    }

    @Test
    @DisplayName("isHighEnergy() returns false when energy < 4")
    void testIsHighEnergyFalse() {
        dailyLog.setEnergyLevel(3);
        assertFalse(dailyLog.isHighEnergy());

        dailyLog.setEnergyLevel(1);
        assertFalse(dailyLog.isHighEnergy());
    }

    @Test
    @DisplayName("isLowEnergy() returns true when energy <= 2")
    void testIsLowEnergyTrue() {
        dailyLog.setEnergyLevel(2);
        assertTrue(dailyLog.isLowEnergy());

        dailyLog.setEnergyLevel(1);
        assertTrue(dailyLog.isLowEnergy());
    }

    @Test
    @DisplayName("isLowEnergy() returns false when energy > 2")
    void testIsLowEnergyFalse() {
        dailyLog.setEnergyLevel(3);
        assertFalse(dailyLog.isLowEnergy());
    }

    @Test
    @DisplayName("isHighStress() returns true when stress >= 4")
    void testIsHighStressTrue() {
        dailyLog.setStressLevel(4);
        assertTrue(dailyLog.isHighStress());

        dailyLog.setStressLevel(5);
        assertTrue(dailyLog.isHighStress());
    }

    @Test
    @DisplayName("isHighStress() returns false when stress < 4")
    void testIsHighStressFalse() {
        dailyLog.setStressLevel(3);
        assertFalse(dailyLog.isHighStress());
    }

    @Test
    @DisplayName("isPoorSleep() returns true when sleep <= 2")
    void testIsPoorSleepTrue() {
        dailyLog.setSleepQuality(2);
        assertTrue(dailyLog.isPoorSleep());

        dailyLog.setSleepQuality(1);
        assertTrue(dailyLog.isPoorSleep());
    }

    @Test
    @DisplayName("isPoorSleep() returns false when sleep > 2")
    void testIsPoorSleepFalse() {
        dailyLog.setSleepQuality(3);
        assertFalse(dailyLog.isPoorSleep());
    }

    @Test
    @DisplayName("isGoodSleep() returns true when sleep >= 4")
    void testIsGoodSleepTrue() {
        dailyLog.setSleepQuality(4);
        assertTrue(dailyLog.isGoodSleep());

        dailyLog.setSleepQuality(5);
        assertTrue(dailyLog.isGoodSleep());
    }

    @Test
    @DisplayName("isGoodSleep() returns false when sleep < 4")
    void testIsGoodSleepFalse() {
        dailyLog.setSleepQuality(3);
        assertFalse(dailyLog.isGoodSleep());
    }

    @Test
    @DisplayName("isPositiveMood() returns true for GREAT or GOOD")
    void testIsPositiveMoodTrue() {
        dailyLog.setMood(Mood.GREAT);
        assertTrue(dailyLog.isPositiveMood());

        dailyLog.setMood(Mood.GOOD);
        assertTrue(dailyLog.isPositiveMood());
    }

    @Test
    @DisplayName("isPositiveMood() returns false for OKAY, LOW, STRESSED")
    void testIsPositiveMoodFalse() {
        dailyLog.setMood(Mood.OKAY);
        assertFalse(dailyLog.isPositiveMood());

        dailyLog.setMood(Mood.LOW);
        assertFalse(dailyLog.isPositiveMood());

        dailyLog.setMood(Mood.STRESSED);
        assertFalse(dailyLog.isPositiveMood());
    }

    @Test
    @DisplayName("isNegativeMood() returns true for LOW or STRESSED")
    void testIsNegativeMoodTrue() {
        dailyLog.setMood(Mood.LOW);
        assertTrue(dailyLog.isNegativeMood());

        dailyLog.setMood(Mood.STRESSED);
        assertTrue(dailyLog.isNegativeMood());
    }

    @Test
    @DisplayName("isNegativeMood() returns false for GREAT, GOOD, OKAY")
    void testIsNegativeMoodFalse() {
        dailyLog.setMood(Mood.GREAT);
        assertFalse(dailyLog.isNegativeMood());

        dailyLog.setMood(Mood.GOOD);
        assertFalse(dailyLog.isNegativeMood());

        dailyLog.setMood(Mood.OKAY);
        assertFalse(dailyLog.isNegativeMood());
    }

    @Test
    @DisplayName("needsRecovery() returns true when multiple stress indicators")
    void testNeedsRecoveryTrue() {
        // Low energy + high stress
        dailyLog.setEnergyLevel(2);
        dailyLog.setStressLevel(4);
        assertTrue(dailyLog.needsRecovery());

        // Low energy + poor sleep
        dailyLog.setEnergyLevel(2);
        dailyLog.setStressLevel(2);
        dailyLog.setSleepQuality(2);
        assertTrue(dailyLog.needsRecovery());

        // High stress + poor sleep
        dailyLog.setEnergyLevel(4);
        dailyLog.setStressLevel(4);
        dailyLog.setSleepQuality(2);
        assertTrue(dailyLog.needsRecovery());
    }

    @Test
    @DisplayName("needsRecovery() returns false when feeling good")
    void testNeedsRecoveryFalse() {
        dailyLog.setEnergyLevel(4);
        dailyLog.setStressLevel(2);
        dailyLog.setSleepQuality(4);
        assertFalse(dailyLog.needsRecovery());
    }

    @Test
    @DisplayName("isOptimalWorkoutDay() returns true when all indicators good")
    void testIsOptimalWorkoutDayTrue() {
        dailyLog.setEnergyLevel(4);
        dailyLog.setStressLevel(2);
        dailyLog.setMood(Mood.GREAT);
        assertTrue(dailyLog.isOptimalWorkoutDay());
    }

    @Test
    @DisplayName("isOptimalWorkoutDay() returns false with any poor indicator")
    void testIsOptimalWorkoutDayFalse() {
        // Low energy
        dailyLog.setEnergyLevel(2);
        dailyLog.setStressLevel(2);
        dailyLog.setMood(Mood.GOOD);
        assertFalse(dailyLog.isOptimalWorkoutDay());

        // High stress
        dailyLog.setEnergyLevel(4);
        dailyLog.setStressLevel(4);
        dailyLog.setMood(Mood.GOOD);
        assertFalse(dailyLog.isOptimalWorkoutDay());

        // Negative mood
        dailyLog.setEnergyLevel(4);
        dailyLog.setStressLevel(2);
        dailyLog.setMood(Mood.LOW);
        assertFalse(dailyLog.isOptimalWorkoutDay());
    }

    @Test
    @DisplayName("getWellnessScore() calculates correctly")
    void testGetWellnessScore() {
        // Perfect day: 5+5+5 = 15, normalized to 1.0
        dailyLog.setEnergyLevel(5);
        dailyLog.setStressLevel(1); // Inverted: 1 stress = 5 points
        dailyLog.setSleepQuality(5);
        assertEquals(1.0, dailyLog.getWellnessScore(), 0.01);

        // Average day: 3+3+3 = 9, normalized to 0.6
        dailyLog.setEnergyLevel(3);
        dailyLog.setStressLevel(3); // Inverted: 3 stress = 3 points
        dailyLog.setSleepQuality(3);
        assertEquals(0.6, dailyLog.getWellnessScore(), 0.01);

        // Bad day: 1+1+1 = 3, normalized to 0.2
        dailyLog.setEnergyLevel(1);
        dailyLog.setStressLevel(5); // Inverted: 5 stress = 1 point
        dailyLog.setSleepQuality(1);
        assertEquals(0.2, dailyLog.getWellnessScore(), 0.01);
    }

    // =====================================================
    // VALIDATION TESTS
    // =====================================================

    @Test
    @DisplayName("Should handle energy level range 1-5")
    void testEnergyLevelRange() {
        dailyLog.setEnergyLevel(1);
        assertEquals(1, dailyLog.getEnergyLevel());

        dailyLog.setEnergyLevel(5);
        assertEquals(5, dailyLog.getEnergyLevel());
    }

    @Test
    @DisplayName("Should handle stress level range 1-5")
    void testStressLevelRange() {
        dailyLog.setStressLevel(1);
        assertEquals(1, dailyLog.getStressLevel());

        dailyLog.setStressLevel(5);
        assertEquals(5, dailyLog.getStressLevel());
    }

    @Test
    @DisplayName("Should handle sleep quality range 1-5")
    void testSleepQualityRange() {
        dailyLog.setSleepQuality(1);
        assertEquals(1, dailyLog.getSleepQuality());

        dailyLog.setSleepQuality(5);
        assertEquals(5, dailyLog.getSleepQuality());
    }

    @Test
    @DisplayName("Should handle optional notes")
    void testNotes() {
        dailyLog.setNotes("Feeling great today!");
        assertEquals("Feeling great today!", dailyLog.getNotes());

        dailyLog.setNotes(null);
        assertNull(dailyLog.getNotes());
    }

    // =====================================================
    // EDGE CASE TESTS
    // =====================================================

    @Test
    @DisplayName("Handles past dates")
    void testPastDate() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        dailyLog.setLogDate(yesterday);
        assertEquals(yesterday, dailyLog.getLogDate());
    }

    @Test
    @DisplayName("Handles future dates")
    void testFutureDate() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        dailyLog.setLogDate(tomorrow);
        assertEquals(tomorrow, dailyLog.getLogDate());
    }

    @Test
    @DisplayName("Handles long notes text")
    void testLongNotes() {
        String longNote = "This is a very long note. ".repeat(50);
        dailyLog.setNotes(longNote);
        assertEquals(longNote, dailyLog.getNotes());
    }

    // =====================================================
    // EQUALS AND HASHCODE TESTS
    // =====================================================

    @Test
    @DisplayName("equals() returns true for same logId")
    void testEqualsReturnsTrueForSameId() {
        DailyLog log1 = new DailyLog();
        log1.setLogId(1L);
        
        DailyLog log2 = new DailyLog();
        log2.setLogId(1L);
        
        assertEquals(log1, log2);
    }

    @Test
    @DisplayName("equals() returns false for different logIds")
    void testEqualsReturnsFalseForDifferentIds() {
        DailyLog log1 = new DailyLog();
        log1.setLogId(1L);
        
        DailyLog log2 = new DailyLog();
        log2.setLogId(2L);
        
        assertNotEquals(log1, log2);
    }

    @Test
    @DisplayName("equals() returns true when comparing to itself")
    void testEqualsReturnsTrueForSameObject() {
        assertEquals(dailyLog, dailyLog);
    }

    @Test
    @DisplayName("equals() returns false when comparing to null")
    void testEqualsReturnsFalseForNull() {
        assertNotEquals(null, dailyLog);
    }

    @Test
    @DisplayName("hashCode() is same for logs with same logId")
    void testHashCodeSameForSameId() {
        DailyLog log1 = new DailyLog();
        log1.setLogId(1L);
        
        DailyLog log2 = new DailyLog();
        log2.setLogId(1L);
        
        assertEquals(log1.hashCode(), log2.hashCode());
    }

    // =====================================================
    // TOSTRING TESTS
    // =====================================================

    @Test
    @DisplayName("toString() contains key fields")
    void testToStringContainsKeyFields() {
        dailyLog.setLogId(1L);
        
        String result = dailyLog.toString();
        
        assertTrue(result.contains("logId=1"));
        assertTrue(result.contains("energyLevel=4"));
        assertTrue(result.contains("stressLevel=2"));
        assertTrue(result.contains("GOOD"));
    }
}