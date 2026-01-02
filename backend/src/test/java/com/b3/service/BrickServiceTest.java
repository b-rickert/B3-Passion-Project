package com.b3.service;

import com.b3.dto.response.BrickResponse;
import com.b3.dto.response.BrickStatsResponse;
import com.b3.exception.ResourceNotFoundException;
import com.b3.model.Brick;
import com.b3.model.UserProfile;
import com.b3.model.WorkoutSession;
import com.b3.model.Workout;
import com.b3.repository.BrickRepository;
import com.b3.repository.UserProfileRepository;
import com.b3.repository.WorkoutSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * TDD Tests for BrickService
 */
@DisplayName("BrickService Tests")
class BrickServiceTest {

    @Mock
    private BrickRepository brickRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private WorkoutSessionRepository workoutSessionRepository;

    @InjectMocks
    private BrickService brickService;

    private UserProfile testUser;
    private Workout testWorkout;
    private WorkoutSession testSession;
    private Brick testBrick;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testUser = new UserProfile(
            "TestUser",
            25,
            UserProfile.FitnessLevel.INTERMEDIATE,
            UserProfile.PrimaryGoal.STRENGTH,
            "Dumbbells",
            4
        );
        testUser.setProfileId(1L);
        
        testWorkout = new Workout(
            "Upper Body Blast",
            "Intense upper body workout",
            Workout.WorkoutType.STRENGTH,
            Workout.DifficultyLevel.INTERMEDIATE,
            45,
            "Dumbbells"
        );
        testWorkout.setWorkoutId(1L);
        
        testSession = new WorkoutSession(testUser, testWorkout, LocalDateTime.now());
        testSession.setSessionId(1L);
        testSession.completeSession(40);
        
        testBrick = new Brick(testUser, testSession, LocalDate.now(), Brick.BrickType.WORKOUT);
        testBrick.setBrickId(1L);
    }

    // =====================================================
    // CREATE BRICK Tests
    // =====================================================

    @Test
    @DisplayName("Should create brick after workout completion")
    void testCreateBrick() {
        // Given
        when(workoutSessionRepository.findById(1L)).thenReturn(Optional.of(testSession));
        when(brickRepository.save(any(Brick.class))).thenReturn(testBrick);
        
        // When
        BrickResponse response = brickService.createBrick(1L);
        
        // Then
        assertNotNull(response);
        verify(brickRepository).save(any(Brick.class));
    }

    @Test
    @DisplayName("Should throw exception when session not found")
    void testCreateBrickSessionNotFound() {
        // Given
        when(workoutSessionRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            brickService.createBrick(999L);
        });
    }

    // =====================================================
    // GET BRICK CALENDAR Tests
    // =====================================================

    @Test
    @DisplayName("Should get brick calendar for month")
    void testGetBrickCalendar() {
        // Given
        LocalDate today = LocalDate.now();
        Brick brick2 = new Brick(testUser, testSession, today.minusDays(5), Brick.BrickType.WORKOUT);
        
        when(brickRepository.findByUserProfile_ProfileIdAndBrickDateBetween(
            eq(1L), any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(Arrays.asList(testBrick, brick2));
        
        // When
        List<BrickResponse> calendar = brickService.getBrickCalendar(1L, today.getYear(), today.getMonthValue());
        
        // Then
        assertNotNull(calendar);
        assertEquals(2, calendar.size());
        verify(brickRepository).findByUserProfile_ProfileIdAndBrickDateBetween(
            eq(1L), any(LocalDate.class), any(LocalDate.class));
    }

    // =====================================================
    // GET BRICK STATS Tests
    // =====================================================

    @Test
    @DisplayName("Should get brick stats for user")
    void testGetBrickStats() {
        // Given
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(brickRepository.countByUserProfile_ProfileId(1L)).thenReturn(15L);
        when(brickRepository.findByUserProfile_ProfileIdOrderByBrickDateDesc(1L))
            .thenReturn(Arrays.asList(testBrick));
        
        // When
        BrickStatsResponse stats = brickService.getBrickStats(1L);
        
        // Then
        assertNotNull(stats);
        assertEquals(15L, stats.getTotalBricks());
        verify(brickRepository).countByUserProfile_ProfileId(1L);
    }

    @Test
    @DisplayName("Should throw exception when user not found for stats")
    void testGetBrickStatsUserNotFound() {
        // Given
        when(userProfileRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            brickService.getBrickStats(999L);
        });
    }

    // =====================================================
    // CHECK BRICK EXISTS Tests
    // =====================================================

    @Test
    @DisplayName("Should check if brick exists for today")
    void testHasBrickForToday() {
        // Given
        LocalDate today = LocalDate.now();
        when(brickRepository.existsByUserProfile_ProfileIdAndBrickDate(1L, today))
            .thenReturn(true);
        
        // When
        boolean exists = brickService.hasBrickForToday(1L);
        
        // Then
        assertTrue(exists);
        verify(brickRepository).existsByUserProfile_ProfileIdAndBrickDate(1L, today);
    }

    @Test
    @DisplayName("Should return false when no brick for today")
    void testHasNoBrickForToday() {
        // Given
        LocalDate today = LocalDate.now();
        when(brickRepository.existsByUserProfile_ProfileIdAndBrickDate(1L, today))
            .thenReturn(false);
        
        // When
        boolean exists = brickService.hasBrickForToday(1L);
        
        // Then
        assertFalse(exists);
    }

    // =====================================================
    // GET BRICK HISTORY Tests
    // =====================================================

    @Test
    @DisplayName("Should get brick history for user")
    void testGetBrickHistory() {
        // Given
        Brick brick2 = new Brick(testUser, testSession, LocalDate.now().minusDays(1), Brick.BrickType.WORKOUT);
        
        when(brickRepository.findByUserProfile_ProfileIdOrderByBrickDateDesc(1L))
            .thenReturn(Arrays.asList(testBrick, brick2));
        
        // When
        List<BrickResponse> history = brickService.getBrickHistory(1L);
        
        // Then
        assertNotNull(history);
        assertEquals(2, history.size());
        verify(brickRepository).findByUserProfile_ProfileIdOrderByBrickDateDesc(1L);
    }
}