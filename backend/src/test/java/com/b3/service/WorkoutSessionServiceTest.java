package com.b3.service;

import com.b3.dto.request.WorkoutSessionCreateRequest;
import com.b3.dto.request.WorkoutSessionCompleteRequest;
import com.b3.dto.response.WorkoutSessionResponse;
import com.b3.exception.BadRequestException;
import com.b3.exception.ResourceNotFoundException;
import com.b3.model.UserProfile;
import com.b3.model.Workout;
import com.b3.model.WorkoutSession;
import com.b3.repository.UserProfileRepository;
import com.b3.repository.WorkoutRepository;
import com.b3.repository.WorkoutSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * TDD Tests for WorkoutSessionService
 */
@DisplayName("WorkoutSessionService Tests")
class WorkoutSessionServiceTest {

    @Mock
    private WorkoutSessionRepository workoutSessionRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private WorkoutRepository workoutRepository;

    @InjectMocks
    private WorkoutSessionService workoutSessionService;

    private UserProfile testUser;
    private Workout testWorkout;
    private WorkoutSession testSession;

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
    }

    // =====================================================
    // CREATE SESSION Tests
    // =====================================================

    @Test
    @DisplayName("Should create workout session")
    void testCreateSession() {
        // Given
        WorkoutSessionCreateRequest request = new WorkoutSessionCreateRequest();
        request.setProfileId(1L);
        request.setWorkoutId(1L);
        
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(workoutRepository.findById(1L)).thenReturn(Optional.of(testWorkout));
        when(workoutSessionRepository.save(any(WorkoutSession.class))).thenReturn(testSession);
        
        // When
        WorkoutSessionResponse response = workoutSessionService.createSession(request);
        
        // Then
        assertNotNull(response);
        verify(workoutSessionRepository).save(any(WorkoutSession.class));
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void testCreateSessionUserNotFound() {
        // Given
        WorkoutSessionCreateRequest request = new WorkoutSessionCreateRequest();
        request.setProfileId(999L);
        request.setWorkoutId(1L);
        
        when(userProfileRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            workoutSessionService.createSession(request);
        });
    }

    @Test
    @DisplayName("Should throw exception when workout not found")
    void testCreateSessionWorkoutNotFound() {
        // Given
        WorkoutSessionCreateRequest request = new WorkoutSessionCreateRequest();
        request.setProfileId(1L);
        request.setWorkoutId(999L);
        
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(workoutRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            workoutSessionService.createSession(request);
        });
    }

    // =====================================================
    // COMPLETE SESSION Tests
    // =====================================================

    @Test
    @DisplayName("Should complete workout session")
    void testCompleteSession() {
        // Given
        WorkoutSessionCompleteRequest request = new WorkoutSessionCompleteRequest();
        request.setActualDuration(40);
        
        when(workoutSessionRepository.findById(1L)).thenReturn(Optional.of(testSession));
        when(workoutSessionRepository.save(any(WorkoutSession.class))).thenReturn(testSession);
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(testUser);
        
        // When
        WorkoutSessionResponse response = workoutSessionService.completeSession(1L, request);
        
        // Then
        assertNotNull(response);
        verify(workoutSessionRepository).save(any(WorkoutSession.class));
        verify(userProfileRepository).save(any(UserProfile.class)); // User stats updated
    }

    @Test
    @DisplayName("Should throw exception when completing non-existent session")
    void testCompleteSessionNotFound() {
        // Given
        WorkoutSessionCompleteRequest request = new WorkoutSessionCompleteRequest();
        request.setActualDuration(40);
        
        when(workoutSessionRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            workoutSessionService.completeSession(999L, request);
        });
    }

    @Test
    @DisplayName("Should throw exception when completing already completed session")
    void testCompleteAlreadyCompletedSession() {
        // Given
        testSession.completeSession(40);
        WorkoutSessionCompleteRequest request = new WorkoutSessionCompleteRequest();
        request.setActualDuration(40);
        
        when(workoutSessionRepository.findById(1L)).thenReturn(Optional.of(testSession));
        
        // When & Then
        assertThrows(BadRequestException.class, () -> {
            workoutSessionService.completeSession(1L, request);
        });
    }

    // =====================================================
    // GET ACTIVE SESSION Tests
    // =====================================================

    @Test
    @DisplayName("Should get active session for user")
    void testGetActiveSession() {
        // Given
        when(workoutSessionRepository.findActiveSessionByProfileId(1L))
            .thenReturn(Optional.of(testSession));
        
        // When
        Optional<WorkoutSessionResponse> response = workoutSessionService.getActiveSession(1L);
        
        // Then
        assertTrue(response.isPresent());
        verify(workoutSessionRepository).findActiveSessionByProfileId(1L);
    }

    @Test
    @DisplayName("Should return empty when no active session")
    void testGetActiveSessionEmpty() {
        // Given
        when(workoutSessionRepository.findActiveSessionByProfileId(1L))
            .thenReturn(Optional.empty());
        
        // When
        Optional<WorkoutSessionResponse> response = workoutSessionService.getActiveSession(1L);
        
        // Then
        assertFalse(response.isPresent());
    }

    // =====================================================
    // GET SESSION HISTORY Tests
    // =====================================================

    @Test
    @DisplayName("Should get session history for user")
    void testGetSessionHistory() {
        // Given
        WorkoutSession session2 = new WorkoutSession(testUser, testWorkout, LocalDateTime.now().minusDays(1));
        session2.completeSession(35);
        
        when(workoutSessionRepository.findByUserProfile_ProfileIdOrderByStartTimeDesc(1L))
            .thenReturn(Arrays.asList(testSession, session2));
        
        // When
        List<WorkoutSessionResponse> history = workoutSessionService.getSessionHistory(1L);
        
        // Then
        assertNotNull(history);
        assertEquals(2, history.size());
        verify(workoutSessionRepository).findByUserProfile_ProfileIdOrderByStartTimeDesc(1L);
    }

    // =====================================================
    // STREAK CALCULATION Tests
    // =====================================================

    @Test
    @DisplayName("Should update streak when completing workout")
    void testStreakUpdate() {
        // Given
        testUser.setCurrentStreak(5);
        WorkoutSessionCompleteRequest request = new WorkoutSessionCompleteRequest();
        request.setActualDuration(40);
        
        when(workoutSessionRepository.findById(1L)).thenReturn(Optional.of(testSession));
        when(workoutSessionRepository.save(any(WorkoutSession.class))).thenReturn(testSession);
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(testUser);
        
        // When
        workoutSessionService.completeSession(1L, request);
        
        // Then
        verify(userProfileRepository).save(any(UserProfile.class));
        // Streak logic is tested in UserProfile entity tests
    }
}