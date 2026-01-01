package com.b3.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Exercise entity
 */
@DisplayName("Exercise Entity Tests")
class ExerciseTest {

    private Exercise exercise;

    @BeforeEach
    void setUp() {
        exercise = new Exercise(
            "Push-ups",
            "Place hands shoulder-width apart, lower body until chest nearly touches floor, push back up",
            Exercise.MuscleGroup.CHEST,
            Exercise.EquipmentType.BODYWEIGHT,
            "https://example.com/pushups.mp4"
        );
    }

    // =====================================================
    // Constructor Tests
    // =====================================================

    @Test
    @DisplayName("Should create Exercise with all fields")
    void testConstructorWithAllFields() {
        assertEquals("Push-ups", exercise.getName());
        assertEquals("Place hands shoulder-width apart, lower body until chest nearly touches floor, push back up", 
                     exercise.getDescription());
        assertEquals(Exercise.MuscleGroup.CHEST, exercise.getMuscleGroup());
        assertEquals("https://example.com/pushups.mp4", exercise.getVideoUrl());
    }

    @Test
    @DisplayName("Should create empty Exercise with default constructor")
    void testDefaultConstructor() {
        Exercise emptyExercise = new Exercise();
        
        assertNotNull(emptyExercise);
        assertNull(emptyExercise.getName());
        assertNull(emptyExercise.getMuscleGroup());
    }

    // =====================================================
    // Field Tests
    // =====================================================

    @Test
    @DisplayName("Should set and get exercise name")
    void testSetAndGetName() {
        exercise.setName("Squats");
        assertEquals("Squats", exercise.getName());
    }

    @Test
    @DisplayName("Should set and get description")
    void testSetAndGetDescription() {
        String newDescription = "Stand with feet shoulder-width apart, lower hips back and down";
        exercise.setDescription(newDescription);
        assertEquals(newDescription, exercise.getDescription());
    }

    @Test
    @DisplayName("Should set and get muscle group")
    void testSetAndGetMuscleGroup() {
        exercise.setMuscleGroup(Exercise.MuscleGroup.LEGS);
        assertEquals(Exercise.MuscleGroup.LEGS, exercise.getMuscleGroup());
    }

    @Test
    @DisplayName("Should set and get video URL")
    void testSetAndGetVideoUrl() {
        exercise.setVideoUrl("https://example.com/newvideo.mp4");
        assertEquals("https://example.com/newvideo.mp4", exercise.getVideoUrl());
    }

    // =====================================================
    // Business Logic Tests
    // =====================================================

    @Test
    @DisplayName("hasVideo() returns true when video URL exists")
    void testHasVideoReturnsTrueWhenPresent() {
        assertTrue(exercise.hasVideo());
    }

    @Test
    @DisplayName("hasVideo() returns false when video URL is null")
    void testHasVideoReturnsFalseWhenNull() {
        exercise.setVideoUrl(null);
        assertFalse(exercise.hasVideo());
    }

    @Test
    @DisplayName("hasVideo() returns false when video URL is empty")
    void testHasVideoReturnsFalseWhenEmpty() {
        exercise.setVideoUrl("");
        assertFalse(exercise.hasVideo());
    }

    @Test
    @DisplayName("hasVideo() returns false when video URL is blank")
    void testHasVideoReturnsFalseWhenBlank() {
        exercise.setVideoUrl("   ");
        assertFalse(exercise.hasVideo());
    }

    @Test
    @DisplayName("targetsMuscleGroup() returns true for matching muscle group")
    void testTargetsMuscleGroupMatching() {
        assertTrue(exercise.targetsMuscleGroup(Exercise.MuscleGroup.CHEST));
    }

    @Test
    @DisplayName("targetsMuscleGroup() returns false for non-matching muscle group")
    void testTargetsMuscleGroupNonMatching() {
        assertFalse(exercise.targetsMuscleGroup(Exercise.MuscleGroup.LEGS));
        assertFalse(exercise.targetsMuscleGroup(Exercise.MuscleGroup.BACK));
    }

    @Test
    @DisplayName("targetsMuscleGroup() returns false when muscle group is null")
    void testTargetsMuscleGroupWhenNull() {
        exercise.setMuscleGroup(null);
        assertFalse(exercise.targetsMuscleGroup(Exercise.MuscleGroup.CHEST));
    }

    @Test
    @DisplayName("isBodyweight() returns true for bodyweight exercises")
    void testIsBodyweightTrue() {
        Exercise bodyweightEx = new Exercise(
            "Burpees",
            "Full body exercise",
            Exercise.MuscleGroup.FULL_BODY,
            Exercise.EquipmentType.BODYWEIGHT,
            null
        );
        assertTrue(bodyweightEx.isBodyweight());
    }

    @Test
    @DisplayName("isBodyweight() returns false for equipment-based exercises")
    void testIsBodyweightFalseWithEquipment() {
        Exercise weightedEx = new Exercise(
            "Dumbbell Press",
            "Press dumbbells overhead",
            Exercise.MuscleGroup.SHOULDERS,
            Exercise.EquipmentType.DUMBBELLS,
            null
        );
        assertFalse(weightedEx.isBodyweight());
    }

    @Test
    @DisplayName("requiresEquipment() returns true for specific equipment")
    void testRequiresEquipment() {
        Exercise dumbbellEx = new Exercise(
            "Bicep Curl",
            "Curl dumbbells",
            Exercise.MuscleGroup.BICEPS,
            Exercise.EquipmentType.DUMBBELLS,
            null
        );
        assertTrue(dumbbellEx.requiresEquipment(Exercise.EquipmentType.DUMBBELLS));
        assertFalse(dumbbellEx.requiresEquipment(Exercise.EquipmentType.BARBELL));
    }

    @Test
    @DisplayName("isUpperBody() returns true for upper body muscle groups")
    void testIsUpperBody() {
        Exercise chestEx = new Exercise("Bench Press", "Press barbell", 
            Exercise.MuscleGroup.CHEST, Exercise.EquipmentType.BARBELL, null);
        assertTrue(chestEx.isUpperBody());

        Exercise backEx = new Exercise("Pull-ups", "Pull up", 
            Exercise.MuscleGroup.BACK, Exercise.EquipmentType.PULL_UP_BAR, null);
        assertTrue(backEx.isUpperBody());

        Exercise shoulderEx = new Exercise("Shoulder Press", "Press overhead", 
            Exercise.MuscleGroup.SHOULDERS, Exercise.EquipmentType.DUMBBELLS, null);
        assertTrue(shoulderEx.isUpperBody());
    }

    @Test
    @DisplayName("isUpperBody() returns false for lower body exercises")
    void testIsUpperBodyFalseForLowerBody() {
        Exercise legEx = new Exercise("Squats", "Squat down", 
            Exercise.MuscleGroup.LEGS, Exercise.EquipmentType.BODYWEIGHT, null);
        assertFalse(legEx.isUpperBody());
    }

    @Test
    @DisplayName("isLowerBody() returns true for lower body muscle groups")
    void testIsLowerBody() {
        Exercise legEx = new Exercise("Squats", "Squat down", 
            Exercise.MuscleGroup.LEGS, Exercise.EquipmentType.BODYWEIGHT, null);
        assertTrue(legEx.isLowerBody());

        Exercise gluteEx = new Exercise("Hip Thrust", "Thrust hips", 
            Exercise.MuscleGroup.GLUTES, Exercise.EquipmentType.BARBELL, null);
        assertTrue(gluteEx.isLowerBody());

        Exercise quadEx = new Exercise("Leg Extension", "Extend legs", 
            Exercise.MuscleGroup.QUADS, Exercise.EquipmentType.MACHINE, null);
        assertTrue(quadEx.isLowerBody());
    }

    @Test
    @DisplayName("isLowerBody() returns false for upper body exercises")
    void testIsLowerBodyFalseForUpperBody() {
        assertFalse(exercise.isLowerBody());
    }

    @Test
    @DisplayName("isCore() returns true for core exercises")
    void testIsCore() {
        Exercise coreEx = new Exercise("Plank", "Hold plank position", 
            Exercise.MuscleGroup.CORE, Exercise.EquipmentType.BODYWEIGHT, null);
        assertTrue(coreEx.isCore());
    }

    @Test
    @DisplayName("isCore() returns false for non-core exercises")
    void testIsCoreForNonCore() {
        assertFalse(exercise.isCore());
    }

    // =====================================================
    // Edge Cast Tests
    // =====================================================

    @Test
    @DisplayName("Handles long description text")
    void testLongDescription() {
        String longDesc = "This is a very long description that contains many words and explains in great detail how to perform this exercise with proper form and technique. ".repeat(5);
        exercise.setDescription(longDesc);
        assertEquals(longDesc, exercise.getDescription());
    }

    @Test
    @DisplayName("Handles special characters in name")
    void testSpecialCharactersInName() {
        exercise.setName("90° Push-ups (Modified)");
        assertEquals("90° Push-ups (Modified)", exercise.getName());
    }

    @Test
    @DisplayName("Handles YouTube URL format")
    void testYouTubeUrl() {
        exercise.setVideoUrl("https://www.youtube.com/watch?v=abc123");
        assertEquals("https://www.youtube.com/watch?v=abc123", exercise.getVideoUrl());
        assertTrue(exercise.hasVideo());
    }

    // =====================================================
    // equals() and hashCode() Tests
    // =====================================================

    @Test
    @DisplayName("equals() returns true for same exerciseId")
    void testEqualsReturnsTrueForSameId() {
        Exercise ex1 = new Exercise();
        ex1.setExerciseId(1L);
        
        Exercise ex2 = new Exercise();
        ex2.setExerciseId(1L);
        
        assertEquals(ex1, ex2);
    }

    @Test
    @DisplayName("equals() returns false for different exerciseIds")
    void testEqualsReturnsFalseForDifferentIds() {
        Exercise ex1 = new Exercise();
        ex1.setExerciseId(1L);
        
        Exercise ex2 = new Exercise();
        ex2.setExerciseId(2L);
        
        assertNotEquals(ex1, ex2);
    }

    @Test
    @DisplayName("equals() returns true when comparing to itself")
    void testEqualsReturnsTrueForSameObject() {
        assertEquals(exercise, exercise);
    }

    @Test
    @DisplayName("equals() returns false when comparing to null")
    void testEqualsReturnsFalseForNull() {
        assertNotEquals(null, exercise);
    }

    @Test
    @DisplayName("hashCode() is same for exercises with same exerciseId")
    void testHashCodeSameForSameId() {
        Exercise ex1 = new Exercise();
        ex1.setExerciseId(1L);
        
        Exercise ex2 = new Exercise();
        ex2.setExerciseId(1L);
        
        assertEquals(ex1.hashCode(), ex2.hashCode());
    }

    // =====================================================
    // toString() Tests
    // =====================================================

    @Test
    @DisplayName("toString() contains key fields")
    void testToStringContainsKeyFields() {
        exercise.setExerciseId(1L);
        
        String result = exercise.toString();
        
        assertTrue(result.contains("exerciseId=1"));
        assertTrue(result.contains("Push-ups"));
        assertTrue(result.contains("muscleGroup=CHEST"));
    }
}