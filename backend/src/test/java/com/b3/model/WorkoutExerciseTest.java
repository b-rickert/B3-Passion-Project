package com.b3.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import com.b3.model.Workout.WorkoutType;
import com.b3.model.Workout.DifficultyLevel;

/**
 * Unit tests for WorkoutExercise entity (junction table)
 */
@DisplayName("WorkoutExercise Entity Tests")
class WorkoutExerciseTest {

    private WorkoutExercise workoutExercise;
    private Workout workout;
    private Exercise exercise;

    @BeforeEach
    void setUp() {
        // Create parent Workout
        workout = new Workout(
            "Full Body Workout",
            "Complete body workout",
            WorkoutType.STRENGTH,
            DifficultyLevel.BEGINNER,
            30,
            "Dumbbells"
        );
        workout.setWorkoutId(1L);

        // Create parent Exercise
        exercise = new Exercise(
            "Push-ups",
            "Chest and triceps exercise",
            Exercise.MuscleGroup.CHEST,
            Exercise.EquipmentType.BODYWEIGHT,
            "https://example.com/pushups.mp4"
        );
        exercise.setExerciseId(1L);

        // Create junction entity
        workoutExercise = new WorkoutExercise(workout, exercise, 1, 3, 12, null);
    }

    // =====================================================
    // Constructor Tests
    // =====================================================

    @Test
    @DisplayName("Should create WorkoutExercise with all fields")
    void testConstructorWithAllFields() {
        assertEquals(workout, workoutExercise.getWorkout());
        assertEquals(exercise, workoutExercise.getExercise());
        assertEquals(1, workoutExercise.getOrderIndex());
        assertEquals(3, workoutExercise.getSets());
        assertEquals(12, workoutExercise.getReps());
        assertNull(workoutExercise.getDurationSeconds());
    }

    @Test
    @DisplayName("Should create WorkoutExercise with duration instead of reps")
    void testConstructorWithDuration() {
        WorkoutExercise timedExercise = new WorkoutExercise(
            workout, 
            exercise, 
            2, 
            3, 
            null, 
            60  // 60 seconds plank
        );

        assertEquals(3, timedExercise.getSets());
        assertNull(timedExercise.getReps());
        assertEquals(60, timedExercise.getDurationSeconds());
    }

    @Test
    @DisplayName("Should create empty WorkoutExercise with default constructor")
    void testDefaultConstructor() {
        WorkoutExercise empty = new WorkoutExercise();
        
        assertNotNull(empty);
        assertNull(empty.getWorkout());
        assertNull(empty.getExercise());
    }

    // =====================================================
    // Relationship Tests
    // =====================================================

    @Test
    @DisplayName("Should maintain relationship with Workout")
    void testWorkoutRelationship() {
        assertNotNull(workoutExercise.getWorkout());
        assertEquals("Full Body Workout", workoutExercise.getWorkout().getName());
        assertEquals(1L, workoutExercise.getWorkout().getWorkoutId());
    }

    @Test
    @DisplayName("Should maintain relationship with Exercise")
    void testExerciseRelationship() {
        assertNotNull(workoutExercise.getExercise());
        assertEquals("Push-ups", workoutExercise.getExercise().getName());
        assertEquals(1L, workoutExercise.getExercise().getExerciseId());
    }

    // =====================================================
    // Business Logic Tests
    // =====================================================

    @Test
    @DisplayName("isRepBased() returns true when reps are specified")
    void testIsRepBasedTrue() {
        assertTrue(workoutExercise.isRepBased());
    }

    @Test
    @DisplayName("isRepBased() returns false when duration is specified")
    void testIsRepBasedFalse() {
        WorkoutExercise timedEx = new WorkoutExercise(workout, exercise, 1, 3, null, 60);
        assertFalse(timedEx.isRepBased());
    }

    @Test
    @DisplayName("isTimeBased() returns true when duration is specified")
    void testIsTimeBasedTrue() {
        WorkoutExercise timedEx = new WorkoutExercise(workout, exercise, 1, 3, null, 45);
        assertTrue(timedEx.isTimeBased());
    }

    @Test
    @DisplayName("isTimeBased() returns false when reps are specified")
    void testIsTimeBasedFalse() {
        assertFalse(workoutExercise.isTimeBased());
    }

    @Test
    @DisplayName("getTotalVolume() calculates correctly for rep-based")
    void testGetTotalVolumeRepBased() {
        // 3 sets x 12 reps = 36 total reps
        assertEquals(36, workoutExercise.getTotalVolume());
    }

    @Test
    @DisplayName("getTotalVolume() calculates correctly for time-based")
    void testGetTotalVolumeTimeBased() {
        WorkoutExercise timedEx = new WorkoutExercise(workout, exercise, 1, 3, null, 60);
        // 3 sets x 60 seconds = 180 total seconds
        assertEquals(180, timedEx.getTotalVolume());
    }

    @Test
    @DisplayName("getTotalVolume() returns 0 when neither reps nor duration")
    void testGetTotalVolumeZero() {
        WorkoutExercise emptyEx = new WorkoutExercise(workout, exercise, 1, 3, null, null);
        assertEquals(0, emptyEx.getTotalVolume());
    }

    @Test
    @DisplayName("getEstimatedSeconds() calculates for rep-based (4 sec per rep)")
    void testGetEstimatedSecondsRepBased() {
        // 3 sets x 12 reps x 4 seconds = 144 seconds
        assertEquals(144, workoutExercise.getEstimatedSeconds());
    }

    @Test
    @DisplayName("getEstimatedSeconds() returns actual duration for time-based")
    void testGetEstimatedSecondsTimeBased() {
        WorkoutExercise timedEx = new WorkoutExercise(workout, exercise, 1, 3, null, 45);
        // 3 sets x 45 seconds = 135 seconds
        assertEquals(135, timedEx.getEstimatedSeconds());
    }

    @Test
    @DisplayName("getFormattedDuration() returns rep format")
    void testGetFormattedDurationReps() {
        assertEquals("3 sets × 12 reps", workoutExercise.getFormattedDuration());
    }

    @Test
    @DisplayName("getFormattedDuration() returns time format")
    void testGetFormattedDurationTime() {
        WorkoutExercise timedEx = new WorkoutExercise(workout, exercise, 1, 3, null, 60);
        assertEquals("3 sets × 60 sec", timedEx.getFormattedDuration());
    }

    // =====================================================
    // Validation Tests
    // =====================================================

    @Test
    @DisplayName("Should handle minimum order index (0)")
    void testMinimumOrderIndex() {
        workoutExercise.setOrderIndex(0);
        assertEquals(0, workoutExercise.getOrderIndex());
    }

    @Test
    @DisplayName("Should handle large order index")
    void testLargeOrderIndex() {
        workoutExercise.setOrderIndex(50);
        assertEquals(50, workoutExercise.getOrderIndex());
    }

    @Test
    @DisplayName("Should handle single set")
    void testSingleSet() {
        workoutExercise.setSets(1);
        assertEquals(1, workoutExercise.getSets());
        assertEquals(12, workoutExercise.getTotalVolume()); // 1 set x 12 reps
    }

    @Test
    @DisplayName("Should handle high rep count")
    void testHighRepCount() {
        workoutExercise.setReps(100);
        assertEquals(100, workoutExercise.getReps());
        assertEquals(300, workoutExercise.getTotalVolume()); // 3 sets x 100 reps
    }

    @Test
    @DisplayName("Should handle long duration")
    void testLongDuration() {
        WorkoutExercise longEx = new WorkoutExercise(workout, exercise, 1, 1, null, 300);
        assertEquals(300, longEx.getDurationSeconds());
        assertEquals(300, longEx.getTotalVolume());
    }

    // =====================================================
    // Edge Case Tests
    // =====================================================

    @Test
    @DisplayName("Handles both reps AND duration (prioritizes reps)")
    void testBothRepsAndDuration() {
        WorkoutExercise bothEx = new WorkoutExercise(workout, exercise, 1, 3, 12, 60);
        
        // Should prioritize reps over duration
        assertTrue(bothEx.isRepBased());
        assertFalse(bothEx.isTimeBased());
        assertEquals(36, bothEx.getTotalVolume()); // Uses reps
    }

    @Test
    @DisplayName("Handles zero sets")
    void testZeroSets() {
        workoutExercise.setSets(0);
        assertEquals(0, workoutExercise.getTotalVolume());
    }

    @Test
    @DisplayName("Handles zero reps")
    void testZeroReps() {
        workoutExercise.setReps(0);
        assertEquals(0, workoutExercise.getTotalVolume());
    }

    // =====================================================
    // equals() and hashCode() Tests
    // =====================================================

    @Test
    @DisplayName("equals() returns true for same workoutExerciseId")
    void testEqualsReturnsTrueForSameId() {
        WorkoutExercise we1 = new WorkoutExercise();
        we1.setWorkoutExerciseId(1L);
        
        WorkoutExercise we2 = new WorkoutExercise();
        we2.setWorkoutExerciseId(1L);
        
        assertEquals(we1, we2);
    }

    @Test
    @DisplayName("equals() returns false for different workoutExerciseIds")
    void testEqualsReturnsFalseForDifferentIds() {
        WorkoutExercise we1 = new WorkoutExercise();
        we1.setWorkoutExerciseId(1L);
        
        WorkoutExercise we2 = new WorkoutExercise();
        we2.setWorkoutExerciseId(2L);
        
        assertNotEquals(we1, we2);
    }

    @Test
    @DisplayName("equals() returns true when comparing to itself")
    void testEqualsReturnsTrueForSameObject() {
        assertEquals(workoutExercise, workoutExercise);
    }

    @Test
    @DisplayName("equals() returns false when comparing to null")
    void testEqualsReturnsFalseForNull() {
        assertNotEquals(null, workoutExercise);
    }

    @Test
    @DisplayName("hashCode() is same for same workoutExerciseId")
    void testHashCodeSameForSameId() {
        WorkoutExercise we1 = new WorkoutExercise();
        we1.setWorkoutExerciseId(1L);
        
        WorkoutExercise we2 = new WorkoutExercise();
        we2.setWorkoutExerciseId(1L);
        
        assertEquals(we1.hashCode(), we2.hashCode());
    }

    // =====================================================
    // toString() Tests
    // =====================================================

    @Test
    @DisplayName("toString() contains key fields")
    void testToStringContainsKeyFields() {
        workoutExercise.setWorkoutExerciseId(1L);
        
        String result = workoutExercise.toString();
        
        assertTrue(result.contains("workoutExerciseId=1"));
        assertTrue(result.contains("orderIndex=1"));
        assertTrue(result.contains("sets=3"));
        assertTrue(result.contains("reps=12"));
    }
}