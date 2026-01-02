package com.b3.service;

import com.b3.dto.response.WorkoutResponse;
import com.b3.exception.ResourceNotFoundException;
import com.b3.model.Workout;
import com.b3.repository.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * TDD Tests for WorkoutService
 */
@DisplayName("WorkoutService Tests")
class WorkoutServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @InjectMocks
    private WorkoutService workoutService;

    private Workout testWorkout1;
    private Workout testWorkout2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testWorkout1 = new Workout(
            "Upper Body Blast",
            "Intense upper body workout",
            Workout.WorkoutType.STRENGTH,
            Workout.DifficultyLevel.INTERMEDIATE,
            45,
            "Dumbbells, Pull-up Bar"
        );
        testWorkout1.setWorkoutId(1L);
        
        testWorkout2 = new Workout(
            "Cardio Burner",
            "High intensity cardio",
            Workout.WorkoutType.CARDIO,
            Workout.DifficultyLevel.BEGINNER,
            30,
            "None"
        );
        testWorkout2.setWorkoutId(2L);
    }

    // =====================================================
    // GET ALL WORKOUTS Tests
    // =====================================================

    @Test
    @DisplayName("Should get all workouts")
    void testGetAllWorkouts() {
        // Given
        when(workoutRepository.findAll()).thenReturn(Arrays.asList(testWorkout1, testWorkout2));
        
        // When
        List<WorkoutResponse> workouts = workoutService.getAllWorkouts();
        
        // Then
        assertNotNull(workouts);
        assertEquals(2, workouts.size());
        verify(workoutRepository).findAll();
    }

    // =====================================================
    // GET BY ID Tests
    // =====================================================

    @Test
    @DisplayName("Should get workout by ID")
    void testGetWorkoutById() {
        // Given
        when(workoutRepository.findById(1L)).thenReturn(Optional.of(testWorkout1));
        
        // When
        WorkoutResponse response = workoutService.getWorkoutById(1L);
        
        // Then
        assertNotNull(response);
        assertEquals("Upper Body Blast", response.getName());
        assertEquals("STRENGTH", response.getWorkoutType());
        verify(workoutRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when workout not found")
    void testGetWorkoutByIdNotFound() {
        // Given
        when(workoutRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            workoutService.getWorkoutById(999L);
        });
    }

    // =====================================================
    // FILTER BY CATEGORY Tests
    // =====================================================

    @Test
    @DisplayName("Should filter workouts by type")
    void testFilterByWorkoutType() {
        // Given
        when(workoutRepository.findByWorkoutType(Workout.WorkoutType.STRENGTH))
            .thenReturn(Arrays.asList(testWorkout1));
        
        // When
        List<WorkoutResponse> workouts = workoutService.getWorkoutsByType(Workout.WorkoutType.STRENGTH);
        
        // Then
        assertNotNull(workouts);
        assertEquals(1, workouts.size());
        assertEquals("STRENGTH", workouts.get(0).getWorkoutType());
        verify(workoutRepository).findByWorkoutType(Workout.WorkoutType.STRENGTH);
    }

    // =====================================================
    // FILTER BY DIFFICULTY Tests
    // =====================================================

    @Test
    @DisplayName("Should filter workouts by difficulty")
    void testFilterByDifficulty() {
        // Given
        when(workoutRepository.findByDifficultyLevel(Workout.DifficultyLevel.BEGINNER))
            .thenReturn(Arrays.asList(testWorkout2));
        
        // When
        List<WorkoutResponse> workouts = workoutService.getWorkoutsByDifficulty(Workout.DifficultyLevel.BEGINNER);
        
        // Then
        assertNotNull(workouts);
        assertEquals(1, workouts.size());
        assertEquals("BEGINNER", workouts.get(0).getDifficultyLevel());
        verify(workoutRepository).findByDifficultyLevel(Workout.DifficultyLevel.BEGINNER);
    }

    // =====================================================
    // SEARCH Tests
    // =====================================================

    @Test
    @DisplayName("Should search workouts by name")
    void testSearchWorkouts() {
        // Given
        when(workoutRepository.findByNameContainingIgnoreCase("upper"))
            .thenReturn(Arrays.asList(testWorkout1));
        
        // When
        List<WorkoutResponse> workouts = workoutService.searchWorkouts("upper");
        
        // Then
        assertNotNull(workouts);
        assertEquals(1, workouts.size());
        assertTrue(workouts.get(0).getName().toLowerCase().contains("upper"));
        verify(workoutRepository).findByNameContainingIgnoreCase("upper");
    }

    @Test
    @DisplayName("Should return empty list when no matches found")
    void testSearchWorkoutsNoResults() {
        // Given
        when(workoutRepository.findByNameContainingIgnoreCase("yoga"))
            .thenReturn(Arrays.asList());
        
        // When
        List<WorkoutResponse> workouts = workoutService.searchWorkouts("yoga");
        
        // Then
        assertNotNull(workouts);
        assertEquals(0, workouts.size());
    }

    // =====================================================
    // RECOMMENDATIONS Tests
    // =====================================================

    @Test
    @DisplayName("Should recommend workouts based on user fitness level")
    void testGetRecommendedWorkouts() {
        // Given
        when(workoutRepository.findByDifficultyLevel(Workout.DifficultyLevel.INTERMEDIATE))
            .thenReturn(Arrays.asList(testWorkout1));
        
        // When
        List<WorkoutResponse> workouts = workoutService.getRecommendedWorkouts(
            Workout.DifficultyLevel.INTERMEDIATE
        );
        
        // Then
        assertNotNull(workouts);
        assertEquals(1, workouts.size());
        assertEquals("INTERMEDIATE", workouts.get(0).getDifficultyLevel());
    }
}