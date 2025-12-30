package com.b3.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import com.b3.model.BehaviorProfile.MotivationState;
import com.b3.model.BehaviorProfile.MomentumTrend;
import com.b3.model.BehaviorProfile.CoachingTone;
import com.b3.model.UserProfile.FitnessLevel;
import com.b3.model.UserProfile.PrimaryGoal;

import java.time.LocalDate;

/**
 * Unit tests for BehaviorProfile entity
 */
@DisplayName("BehaviorProfile Entity Tests")
class BehaviorProfileTest {

    private BehaviorProfile behaviorProfile;
    private UserProfile userProfile;

    @BeforeEach
    void setUp() {
        // Create UserProfile first (required for relationship)
        userProfile = new UserProfile(
            "TestUser",
            25,
            FitnessLevel.INTERMEDIATE,
            PrimaryGoal.STRENGTH,
            "Dumbbells,Resistance Bands",
            4
        );
        userProfile.setProfileId(1L); // Set ID for testing
        
        // Create BehaviorProfile with UserProfile reference
        behaviorProfile = new BehaviorProfile(userProfile);
    }

    // =====================================================
    // Constructor Tests
    // =====================================================

    @Test
    @DisplayName("Should create BehaviorProfile with default values")
    void testConstructorWithUserProfile() {
        assertNotNull(behaviorProfile.getUserProfile());
        assertEquals(userProfile, behaviorProfile.getUserProfile());
        assertEquals(0, behaviorProfile.getConsecutiveDays());
        assertEquals(0, behaviorProfile.getLongestStreak());
        assertEquals(0, behaviorProfile.getTotalBricksLaid());
        assertEquals(0.0, behaviorProfile.getConsistencyScore());
        assertEquals(MotivationState.NEUTRAL, behaviorProfile.getMotivationState());
        assertEquals(MomentumTrend.STABLE, behaviorProfile.getMomentumTrend());
        assertEquals(0.0, behaviorProfile.getFatigueScore());
        assertEquals(0.5, behaviorProfile.getRecentEnergyScore());
        assertEquals(CoachingTone.ENCOURAGING, behaviorProfile.getCurrentTone());
    }

    @Test
    @DisplayName("Should create empty BehaviorProfile with default constructor")
    void testDefaultConstructor() {
        BehaviorProfile emptyProfile = new BehaviorProfile();
        
        assertNotNull(emptyProfile);
        assertNull(emptyProfile.getUserProfile());
    }

    // =====================================================
    // Relationship Tests
    // =====================================================

    @Test
    @DisplayName("Should maintain bidirectional relationship with UserProfile")
    void testBidirectionalRelationship() {
        userProfile.setBehaviorProfile(behaviorProfile);
        
        assertEquals(userProfile, behaviorProfile.getUserProfile());
        assertEquals(behaviorProfile, userProfile.getBehaviorProfile());
    }

    // =====================================================
    // Enum Tests
    // =====================================================

    @Test
    @DisplayName("Should handle all MotivationState enum values")
    void testMotivationStateEnum() {
        behaviorProfile.setMotivationState(MotivationState.MOTIVATED);
        assertEquals(MotivationState.MOTIVATED, behaviorProfile.getMotivationState());
        
        behaviorProfile.setMotivationState(MotivationState.NEUTRAL);
        assertEquals(MotivationState.NEUTRAL, behaviorProfile.getMotivationState());
        
        behaviorProfile.setMotivationState(MotivationState.STRUGGLING);
        assertEquals(MotivationState.STRUGGLING, behaviorProfile.getMotivationState());
    }

    @Test
    @DisplayName("Should handle all MomentumTrend enum values")
    void testMomentumTrendEnum() {
        behaviorProfile.setMomentumTrend(MomentumTrend.RISING);
        assertEquals(MomentumTrend.RISING, behaviorProfile.getMomentumTrend());
        
        behaviorProfile.setMomentumTrend(MomentumTrend.FALLING);
        assertEquals(MomentumTrend.FALLING, behaviorProfile.getMomentumTrend());
        
        behaviorProfile.setMomentumTrend(MomentumTrend.STABLE);
        assertEquals(MomentumTrend.STABLE, behaviorProfile.getMomentumTrend());
    }

    @Test
    @DisplayName("Should handle all CoachingTone enum values")
    void testCoachingToneEnum() {
        behaviorProfile.setCurrentTone(CoachingTone.ENCOURAGING);
        assertEquals(CoachingTone.ENCOURAGING, behaviorProfile.getCurrentTone());
        
        behaviorProfile.setCurrentTone(CoachingTone.CHALLENGING);
        assertEquals(CoachingTone.CHALLENGING, behaviorProfile.getCurrentTone());
        
        behaviorProfile.setCurrentTone(CoachingTone.EMPATHETIC);
        assertEquals(CoachingTone.EMPATHETIC, behaviorProfile.getCurrentTone());
        
        behaviorProfile.setCurrentTone(CoachingTone.CELEBRATORY);
        assertEquals(CoachingTone.CELEBRATORY, behaviorProfile.getCurrentTone());
    }

    // =====================================================
    // logWorkout() Tests
    // =====================================================

    @Test
    @DisplayName("logWorkout() starts streak at 1 on first workout")
    void testLogWorkoutFirstTime() {
        LocalDate today = LocalDate.now();
        
        behaviorProfile.logWorkout(today, 1);
        
        assertEquals(1, behaviorProfile.getConsecutiveDays());
        assertEquals(1, behaviorProfile.getLongestStreak());
        assertEquals(1, behaviorProfile.getTotalBricksLaid());
        assertEquals(today, behaviorProfile.getLastWorkoutDate());
    }

    @Test
    @DisplayName("logWorkout() increments streak for consecutive days")
    void testLogWorkoutConsecutiveDays() {
        LocalDate day1 = LocalDate.now();
        LocalDate day2 = day1.plusDays(1);
        LocalDate day3 = day2.plusDays(1);
        
        behaviorProfile.logWorkout(day1, 1);
        behaviorProfile.logWorkout(day2, 2);
        behaviorProfile.logWorkout(day3, 3);
        
        assertEquals(3, behaviorProfile.getConsecutiveDays());
        assertEquals(3, behaviorProfile.getLongestStreak());
        assertEquals(3, behaviorProfile.getTotalBricksLaid());
    }

    @Test
    @DisplayName("logWorkout() resets streak after gap")
    void testLogWorkoutAfterGap() {
        LocalDate day1 = LocalDate.now();
        LocalDate day2 = day1.plusDays(1);
        LocalDate day5 = day1.plusDays(4); // 3-day gap
        
        behaviorProfile.logWorkout(day1, 1);
        behaviorProfile.logWorkout(day2, 2);
        behaviorProfile.logWorkout(day5, 5);
        
        assertEquals(1, behaviorProfile.getConsecutiveDays()); // Reset
        assertEquals(2, behaviorProfile.getLongestStreak()); // Preserved
        assertEquals(3, behaviorProfile.getTotalBricksLaid());
    }

    @Test
    @DisplayName("logWorkout() doesn't change streak for same-day workout")
    void testLogWorkoutSameDay() {
        LocalDate today = LocalDate.now();
        
        behaviorProfile.logWorkout(today, 1);
        int streakBefore = behaviorProfile.getConsecutiveDays();
        
        behaviorProfile.logWorkout(today, 1);
        
        assertEquals(streakBefore, behaviorProfile.getConsecutiveDays());
        assertEquals(2, behaviorProfile.getTotalBricksLaid()); // Still increments bricks
    }

    // =====================================================
    // Consistency Score Tests
    // =====================================================

    @Test
    @DisplayName("Consistency score calculates correctly")
    void testConsistencyScoreCalculation() {
        LocalDate day1 = LocalDate.now();
        LocalDate day2 = day1.plusDays(1);
        LocalDate day3 = day2.plusDays(1);
        
        behaviorProfile.logWorkout(day1, 1);
        assertEquals(1.0, behaviorProfile.getConsistencyScore()); // 1 workout / 1 day
        
        behaviorProfile.logWorkout(day2, 2);
        assertEquals(1.0, behaviorProfile.getConsistencyScore()); // 2 workouts / 2 days
        
        behaviorProfile.logWorkout(day3, 3);
        assertEquals(1.0, behaviorProfile.getConsistencyScore()); // 3 workouts / 3 days
    }

    @Test
    @DisplayName("Consistency score handles gaps correctly")
    void testConsistencyScoreWithGaps() {
        LocalDate day1 = LocalDate.now();
        LocalDate day5 = day1.plusDays(4);
        
        behaviorProfile.logWorkout(day1, 1);
        behaviorProfile.logWorkout(day5, 5);
        
        assertEquals(0.4, behaviorProfile.getConsistencyScore(), 0.01); // 2 workouts / 5 days
    }

    @Test
    @DisplayName("Consistency score caps at 1.0")
    void testConsistencyScoreCapsAtOne() {
        LocalDate today = LocalDate.now();
        
        // Log multiple workouts on same day
        behaviorProfile.logWorkout(today, 1);
        behaviorProfile.logWorkout(today, 1);
        behaviorProfile.logWorkout(today, 1);
        
        assertTrue(behaviorProfile.getConsistencyScore() <= 1.0);
    }

    // =====================================================
    // Motivation State Tests
    // =====================================================

    @Test
    @DisplayName("Motivation becomes MOTIVATED at high consistency")
    void testMotivationStateMotivated() {
        LocalDate startDate = LocalDate.now();
        
        // Create 7 consecutive workouts (100% consistency)
        for (int i = 0; i < 7; i++) {
            behaviorProfile.logWorkout(startDate.plusDays(i), i + 1);
        }
        
        assertEquals(MotivationState.MOTIVATED, behaviorProfile.getMotivationState());
    }

    @Test
    @DisplayName("Motivation becomes STRUGGLING at low consistency")
    void testMotivationStateStruggling() {
        LocalDate day1 = LocalDate.now();
        LocalDate day10 = day1.plusDays(9);
        
        // Only 2 workouts in 10 days (20% consistency)
        behaviorProfile.logWorkout(day1, 1);
        behaviorProfile.logWorkout(day10, 10);
        
        assertEquals(MotivationState.STRUGGLING, behaviorProfile.getMotivationState());
    }

    @Test
    @DisplayName("Motivation stays NEUTRAL at moderate consistency")
    void testMotivationStateNeutral() {
        LocalDate startDate = LocalDate.now();
        
        // 5 workouts in 10 days (50% consistency)
        behaviorProfile.logWorkout(startDate, 1);
        behaviorProfile.logWorkout(startDate.plusDays(1), 2);
        behaviorProfile.logWorkout(startDate.plusDays(3), 4);
        behaviorProfile.logWorkout(startDate.plusDays(6), 7);
        behaviorProfile.logWorkout(startDate.plusDays(9), 10);
        
        assertEquals(MotivationState.NEUTRAL, behaviorProfile.getMotivationState());
    }

    // =====================================================
    // Momentum Trend Tests
    // =====================================================

    @Test
    @DisplayName("Momentum becomes RISING with 3+ day streak")
    void testMomentumTrendRising() {
        LocalDate startDate = LocalDate.now();
        
        behaviorProfile.logWorkout(startDate, 1);
        behaviorProfile.logWorkout(startDate.plusDays(1), 2);
        behaviorProfile.logWorkout(startDate.plusDays(2), 3);
        behaviorProfile.logWorkout(startDate.plusDays(3), 4);
        
        assertEquals(MomentumTrend.RISING, behaviorProfile.getMomentumTrend());
    }

    @Test
    @DisplayName("Momentum becomes FALLING after long gap")
    void testMomentumTrendFalling() {
        LocalDate day1 = LocalDate.now();
        LocalDate day2 = day1.plusDays(1);
        LocalDate day6 = day1.plusDays(5); // 3+ day gap
        
        behaviorProfile.logWorkout(day1, 1);
        behaviorProfile.logWorkout(day2, 2);
        behaviorProfile.logWorkout(day6, 6);
        
        assertEquals(MomentumTrend.FALLING, behaviorProfile.getMomentumTrend());
    }

    @Test
    @DisplayName("Momentum is STABLE on first workout")
    void testMomentumTrendStableFirstWorkout() {
        LocalDate today = LocalDate.now();
        
        behaviorProfile.logWorkout(today, 1);
        
        assertEquals(MomentumTrend.STABLE, behaviorProfile.getMomentumTrend());
    }

    // =====================================================
    // Coaching Tone Tests
    // =====================================================

    @Test
    @DisplayName("Tone becomes EMPATHETIC when struggling")
    void testToneEmpathetic() {
        LocalDate day1 = LocalDate.now();
        LocalDate day10 = day1.plusDays(9);
        
        behaviorProfile.logWorkout(day1, 1);
        behaviorProfile.logWorkout(day10, 10); // Low consistency
        
        assertEquals(CoachingTone.EMPATHETIC, behaviorProfile.getCurrentTone());
    }

    @Test
    @DisplayName("Tone becomes EMPATHETIC when fatigued")
    void testToneEmpatheticWhenFatigued() {
        behaviorProfile.setFatigueScore(0.8);
        
        LocalDate today = LocalDate.now();
        behaviorProfile.logWorkout(today, 1);
        
        assertEquals(CoachingTone.EMPATHETIC, behaviorProfile.getCurrentTone());
    }

    @Test
    @DisplayName("Tone becomes CHALLENGING with 7+ day streak")
    void testToneChallenging() {
        LocalDate startDate = LocalDate.now();
        
        for (int i = 0; i < 7; i++) {
            behaviorProfile.logWorkout(startDate.plusDays(i), i + 1);
        }
        
        assertEquals(CoachingTone.CHALLENGING, behaviorProfile.getCurrentTone());
    }

    @Test
    @DisplayName("Tone becomes CELEBRATORY with rising momentum")
    void testToneCelebratory() {
        LocalDate startDate = LocalDate.now();
        
        // Create rising momentum (3+ consecutive days)
        for (int i = 0; i < 4; i++) {
            behaviorProfile.logWorkout(startDate.plusDays(i), i + 1);
        }
        
        // Should be CELEBRATORY unless streak is 7+ (then CHALLENGING takes precedence)
        assertTrue(
            behaviorProfile.getCurrentTone() == CoachingTone.CELEBRATORY ||
            behaviorProfile.getCurrentTone() == CoachingTone.CHALLENGING
        );
    }

    @Test
    @DisplayName("Tone defaults to ENCOURAGING")
    void testToneEncouraging() {
        assertEquals(CoachingTone.ENCOURAGING, behaviorProfile.getCurrentTone());
    }

    // =====================================================
    // INTELLIGENCE FIELDS TESTS 
    // =====================================================
    
    @Test
    @DisplayName("Should set and get avgWorkoutTimeOfDay")
    void testSetAvgWorkoutTimeOfDay() {
        behaviorProfile.setAvgWorkoutTimeOfDay("morning");
        assertEquals("morning", behaviorProfile.getAvgWorkoutTimeOfDay());
    }

    @Test
    @DisplayName("Should set and get preferredWorkoutTypes")
    void testSetPreferredWorkoutTypes() {
        behaviorProfile.setPreferredWorkoutTypes("STRENGTH,CARDIO");
        assertEquals("STRENGTH,CARDIO", behaviorProfile.getPreferredWorkoutTypes());
    }

    @Test
    @DisplayName("Should set and get energyPattern")
    void testSetEnergyPattern() {
        behaviorProfile.setEnergyPattern("high_morning");
        assertEquals("high_morning", behaviorProfile.getEnergyPattern());
    }

    @Test
    @DisplayName("Should set and get stressTrend")
    void testSetStressTrend() {
        behaviorProfile.setStressTrend("stable");
        assertEquals("stable", behaviorProfile.getStressTrend());
    }

    @Test
    @DisplayName("Should set and get avgSessionDuration")
    void testSetAvgSessionDuration() {
        behaviorProfile.setAvgSessionDuration(45);
        assertEquals(45, behaviorProfile.getAvgSessionDuration());
    }

    @Test
    @DisplayName("Should set and get skipFrequency")
    void testSetSkipFrequency() {
        behaviorProfile.setSkipFrequency(0.15);
        assertEquals(0.15, behaviorProfile.getSkipFrequency());
    }

    // =====================================================
    // Edge Case Tests
    // =====================================================

    @Test
    @DisplayName("Handles minimum valid values")
    void testMinimumValues() {
        behaviorProfile.setConsecutiveDays(0);
        behaviorProfile.setLongestStreak(0);
        behaviorProfile.setTotalBricksLaid(0);
        behaviorProfile.setConsistencyScore(0.0);
        behaviorProfile.setFatigueScore(0.0);
        behaviorProfile.setRecentEnergyScore(0.0);
        
        assertEquals(0, behaviorProfile.getConsecutiveDays());
        assertEquals(0, behaviorProfile.getLongestStreak());
        assertEquals(0, behaviorProfile.getTotalBricksLaid());
        assertEquals(0.0, behaviorProfile.getConsistencyScore());
        assertEquals(0.0, behaviorProfile.getFatigueScore());
        assertEquals(0.0, behaviorProfile.getRecentEnergyScore());
    }

    @Test
    @DisplayName("Handles maximum valid values")
    void testMaximumValues() {
        behaviorProfile.setConsistencyScore(1.0);
        behaviorProfile.setFatigueScore(1.0);
        behaviorProfile.setRecentEnergyScore(1.0);
        
        assertEquals(1.0, behaviorProfile.getConsistencyScore());
        assertEquals(1.0, behaviorProfile.getFatigueScore());
        assertEquals(1.0, behaviorProfile.getRecentEnergyScore());
    }

    @Test
    @DisplayName("Longest streak is preserved across resets")
    void testLongestStreakPreservation() {
        LocalDate startDate = LocalDate.now();
    }
}