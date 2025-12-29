package com.b3.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import com.b3.model.Workout.WorkoutType;
import com.b3.model.Workout.DifficultyLevel;

/**
 * Unit tests for Workout entity
 */
@DisplayName("Workout Entity Tests")
class WorkoutTest {

    private Workout workout;

    @BeforeEach
    void setUp() {
        workout = new Workout(
            "Morning Strength",
            "Full body strength training for beginners",
            WorkoutType.STRENGTH,
            DifficultyLevel.BEGINNER,
            20,
            "Dumbbells,Resistance Bands"
        );
    }

    // ==================== Constructor Tests ====================

    @Test
    @DisplayName("Should create Workout with all fields")
    void testConstructorWithAllFields() {
        assertEquals("Morning Strength", workout.getName());
        assertEquals("Full body strength training for beginners", workout.getDescription());
        assertEquals(WorkoutType.STRENGTH, workout.getWorkoutType());
        assertEquals(DifficultyLevel.BEGINNER, workout.getDifficultyLevel());
        assertEquals(20, workout.getEstimatedDuration());
        assertEquals("Dumbbells,Resistance Bands", workout.getRequiredEquipment());
    }

    @Test
    @DisplayName("Should create empty Workout with default constructor")
    void testDefaultConstructor() {
        Workout emptyWorkout = new Workout();
        
        assertNotNull(emptyWorkout);
        assertNull(emptyWorkout.getName());
        assertNull(emptyWorkout.getWorkoutType());
    }

    // ==================== Enum Tests ====================

    @Test
    @DisplayName("Should handle all WorkoutType enum values")
    void testWorkoutTypeEnum() {
        workout.setWorkoutType(WorkoutType.STRENGTH);
        assertEquals(WorkoutType.STRENGTH, workout.getWorkoutType());
        
        workout.setWorkoutType(WorkoutType.CARDIO);
        assertEquals(WorkoutType.CARDIO, workout.getWorkoutType());
        
        workout.setWorkoutType(WorkoutType.FLEXIBILITY);
        assertEquals(WorkoutType.FLEXIBILITY, workout.getWorkoutType());
        
        workout.setWorkoutType(WorkoutType.MIXED);
        assertEquals(WorkoutType.MIXED, workout.getWorkoutType());
    }

    @Test
    @DisplayName("Should handle all DifficultyLevel enum values")
    void testDifficultyLevelEnum() {
        workout.setDifficultyLevel(DifficultyLevel.BEGINNER);
        assertEquals(DifficultyLevel.BEGINNER, workout.getDifficultyLevel());
        
        workout.setDifficultyLevel(DifficultyLevel.INTERMEDIATE);
        assertEquals(DifficultyLevel.INTERMEDIATE, workout.getDifficultyLevel());
        
        workout.setDifficultyLevel(DifficultyLevel.ADVANCED);
        assertEquals(DifficultyLevel.ADVANCED, workout.getDifficultyLevel());
    }

    // ==================== Equipment Logic Tests ====================

    @Test
    @DisplayName("requiresEquipment() returns true when equipment is specified")
    void testRequiresEquipmentReturnsTrueWhenPresent() {
        assertTrue(workout.requiresEquipment());
    }

    @Test
    @DisplayName("requiresEquipment() returns false when no equipment")
    void testRequiresEquipmentReturnsFalseWhenNull() {
        workout.setRequiredEquipment(null);
        assertFalse(workout.requiresEquipment());
    }

    @Test
    @DisplayName("requiresEquipment() returns false when equipment is empty")
    void testRequiresEquipmentReturnsFalseWhenEmpty() {
        workout.setRequiredEquipment("");
        assertFalse(workout.requiresEquipment());
    }

    @Test
    @DisplayName("requiresEquipment() returns false when equipment is blank")
    void testRequiresEquipmentReturnsFalseWhenBlank() {
        workout.setRequiredEquipment("   ");
        assertFalse(workout.requiresEquipment());
    }

    @Test
    @DisplayName("requiresEquipment() returns false for 'None' equipment")
    void testRequiresEquipmentReturnsFalseForNone() {
        workout.setRequiredEquipment("None");
        assertFalse(workout.requiresEquipment());
    }

    @Test
    @DisplayName("hasEquipment() returns true when specific equipment exists")
    void testHasEquipmentReturnsTrueWhenExists() {
        assertTrue(workout.hasEquipment("Dumbbells"));
        assertTrue(workout.hasEquipment("Resistance Bands"));
    }

    @Test
    @DisplayName("hasEquipment() is case-insensitive")
    void testHasEquipmentIsCaseInsensitive() {
        assertTrue(workout.hasEquipment("dumbbells"));
        assertTrue(workout.hasEquipment("DUMBBELLS"));
        assertTrue(workout.hasEquipment("DuMbBeLLs"));
    }

    @Test
    @DisplayName("hasEquipment() returns false when equipment doesn't exist")
    void testHasEquipmentReturnsFalseWhenNotExists() {
        assertFalse(workout.hasEquipment("Pull-up Bar"));
        assertFalse(workout.hasEquipment("Barbell"));
    }

    @Test
    @DisplayName("hasEquipment() returns false when required equipment is null")
    void testHasEquipmentReturnsFalseWhenNull() {
        workout.setRequiredEquipment(null);
        assertFalse(workout.hasEquipment("Dumbbells"));
    }

    // ==================== Duration Logic Tests ====================

    @Test
    @DisplayName("isShortWorkout() returns true for workouts under 15 minutes")
    void testIsShortWorkoutReturnsTrueUnder15() {
        workout.setEstimatedDuration(10);
        assertTrue(workout.isShortWorkout());
    }

    @Test
    @DisplayName("isShortWorkout() returns false for workouts 15 minutes or longer")
    void testIsShortWorkoutReturnsFalseAt15OrMore() {
        workout.setEstimatedDuration(15);
        assertFalse(workout.isShortWorkout());
        
        workout.setEstimatedDuration(20);
        assertFalse(workout.isShortWorkout());
    }

    // ==================== Difficulty Matching Tests ====================

    @Test
    @DisplayName("isSuitableFor() returns true for matching difficulty")
    void testIsSuitableForMatchingDifficulty() {
        assertTrue(workout.isSuitableFor(DifficultyLevel.BEGINNER));
    }

    @Test
    @DisplayName("isSuitableFor() returns true for easier levels")
    void testIsSuitableForEasierLevels() {
        // BEGINNER workout is suitable for INTERMEDIATE/ADVANCED users
        assertTrue(workout.isSuitableFor(DifficultyLevel.INTERMEDIATE));
        assertTrue(workout.isSuitableFor(DifficultyLevel.ADVANCED));
    }

    @Test
    @DisplayName("isSuitableFor() returns false for harder levels")
    void testIsSuitableForHarderLevels() {
        workout.setDifficultyLevel(DifficultyLevel.ADVANCED);
        
        // ADVANCED workout is NOT suitable for BEGINNER/INTERMEDIATE
        assertFalse(workout.isSuitableFor(DifficultyLevel.BEGINNER));
        assertFalse(workout.isSuitableFor(DifficultyLevel.INTERMEDIATE));
        assertTrue(workout.isSuitableFor(DifficultyLevel.ADVANCED));
    }

    // ==================== Equipment Matching Tests ====================

    @Test
    @DisplayName("matchesEquipment() returns true when user has required equipment")
    void testMatchesEquipmentReturnsTrueWhenUserHasAll() {
        String userEquipment = "Dumbbells,Resistance Bands,Yoga Mat";
        assertTrue(workout.matchesEquipment(userEquipment));
    }

    @Test
    @DisplayName("matchesEquipment() returns false when user missing required equipment")
    void testMatchesEquipmentReturnsFalseWhenUserMissingEquipment() {
        String userEquipment = "Dumbbells,Yoga Mat"; // Missing Resistance Bands
        assertFalse(workout.matchesEquipment(userEquipment));
    }

    @Test
    @DisplayName("matchesEquipment() returns true when workout requires no equipment")
    void testMatchesEquipmentReturnsTrueWhenNoEquipmentRequired() {
        workout.setRequiredEquipment("None");
        assertTrue(workout.matchesEquipment("Dumbbells"));
        assertTrue(workout.matchesEquipment(""));
        assertTrue(workout.matchesEquipment(null));
    }

    @Test
    @DisplayName("matchesEquipment() is case-insensitive")
    void testMatchesEquipmentIsCaseInsensitive() {
        String userEquipment = "dumbbells,resistance bands";
        assertTrue(workout.matchesEquipment(userEquipment));
    }

    // ==================== Edge Case Tests ====================

    @Test
    @DisplayName("Handles minimum valid duration (1 minute)")
    void testMinimumDuration() {
        workout.setEstimatedDuration(1);
        assertEquals(1, workout.getEstimatedDuration());
    }

    @Test
    @DisplayName("Handles maximum reasonable duration (120 minutes)")
    void testMaximumDuration() {
        workout.setEstimatedDuration(120);
        assertEquals(120, workout.getEstimatedDuration());
    }

    @Test
    @DisplayName("Handles multiple equipment items")
    void testMultipleEquipmentItems() {
        workout.setRequiredEquipment("Dumbbells,Resistance Bands,Yoga Mat,Pull-up Bar");
        
        assertTrue(workout.hasEquipment("Dumbbells"));
        assertTrue(workout.hasEquipment("Resistance Bands"));
        assertTrue(workout.hasEquipment("Yoga Mat"));
        assertTrue(workout.hasEquipment("Pull-up Bar"));
    }

    // ==================== equals() and hashCode() Tests ====================

    @Test
    @DisplayName("equals() returns true for same workoutId")
    void testEqualsReturnsTrueForSameId() {
        Workout workout1 = new Workout();
        workout1.setWorkoutId(1L);
        
        Workout workout2 = new Workout();
        workout2.setWorkoutId(1L);
        
        assertEquals(workout1, workout2);
    }

    @Test
    @DisplayName("equals() returns false for different workoutIds")
    void testEqualsReturnsFalseForDifferentIds() {
        Workout workout1 = new Workout();
        workout1.setWorkoutId(1L);
        
        Workout workout2 = new Workout();
        workout2.setWorkoutId(2L);
        
        assertNotEquals(workout1, workout2);
    }

    @Test
    @DisplayName("equals() returns true when comparing to itself")
    void testEqualsReturnsTrueForSameObject() {
        assertEquals(workout, workout);
    }

    @Test
    @DisplayName("equals() returns false when comparing to null")
    void testEqualsReturnsFalseForNull() {
        assertNotEquals(null, workout);
    }

    @Test
    @DisplayName("hashCode() is same for workouts with same workoutId")
    void testHashCodeSameForSameId() {
        Workout workout1 = new Workout();
        workout1.setWorkoutId(1L);
        
        Workout workout2 = new Workout();
        workout2.setWorkoutId(1L);
        
        assertEquals(workout1.hashCode(), workout2.hashCode());
    }

    // ==================== toString() Test ====================

    @Test
    @DisplayName("toString() contains key fields")
    void testToStringContainsKeyFields() {
        workout.setWorkoutId(1L);
        
        String result = workout.toString();
        
        assertTrue(result.contains("workoutId=1"));
        assertTrue(result.contains("Morning Strength"));
        assertTrue(result.contains("STRENGTH"));
        assertTrue(result.contains("BEGINNER"));
        assertTrue(result.contains("20"));
    }
}