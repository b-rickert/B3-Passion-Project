package com.b3.service;

import com.b3.dto.request.BehaviorProfileUpdateRequest;
import com.b3.dto.response.BehaviorProfileResponse;
import com.b3.exception.ResourceNotFoundException;
import com.b3.model.BehaviorProfile;
import com.b3.model.UserProfile;
import com.b3.repository.BehaviorProfileRepository;
import com.b3.repository.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * TDD Tests for BehaviorProfileService
 */
@DisplayName("BehaviorProfileService Tests")
class BehaviorProfileServiceTest {

    @Mock
    private BehaviorProfileRepository behaviorProfileRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private BehaviorProfileService behaviorProfileService;

    private UserProfile testUser;
    private BehaviorProfile testBehavior;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Create test user
        testUser = new UserProfile(
            "TestUser",
            25,
            UserProfile.FitnessLevel.INTERMEDIATE,
            UserProfile.PrimaryGoal.STRENGTH,
            "Dumbbells",
            4
        );
        testUser.setProfileId(1L);
        
        // Create test behavior profile
        testBehavior = new BehaviorProfile(testUser);
        testBehavior.setBehaviorId(1L);
        testBehavior.setConsistencyScore(0.75);
        testBehavior.setPreferredWorkoutTime(BehaviorProfile.PreferredTime.MORNING);
        testBehavior.setAverageSessionDuration(35);
    }

    // =====================================================
    // GET BehaviorProfile Tests
    // =====================================================

    @Test
    @DisplayName("Should get behavior profile by user ID")
    void testGetBehaviorProfile() {
        // Given
        when(behaviorProfileRepository.findByUserProfile_ProfileId(1L))
            .thenReturn(Optional.of(testBehavior));
        
        // When
        BehaviorProfileResponse response = behaviorProfileService.getBehaviorProfile(1L);
        
        // Then
        assertNotNull(response);
        assertEquals(0.75, response.getConsistencyScore());
        assertEquals("MORNING", response.getPreferredWorkoutTime());
        verify(behaviorProfileRepository).findByUserProfile_ProfileId(1L);
    }

    @Test
    @DisplayName("Should throw exception when behavior profile not found")
    void testGetBehaviorProfileNotFound() {
        // Given
        when(behaviorProfileRepository.findByUserProfile_ProfileId(999L))
            .thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            behaviorProfileService.getBehaviorProfile(999L);
        });
    }

    // =====================================================
    // UPDATE BehaviorProfile Tests
    // =====================================================

    @Test
    @DisplayName("Should update behavior profile")
    void testUpdateBehaviorProfile() {
        // Given
        BehaviorProfileUpdateRequest updateRequest = new BehaviorProfileUpdateRequest();
        updateRequest.setPreferredWorkoutTime(BehaviorProfile.PreferredTime.EVENING);
        updateRequest.setAverageSessionDuration(45);
        
        when(behaviorProfileRepository.findByUserProfile_ProfileId(1L))
            .thenReturn(Optional.of(testBehavior));
        when(behaviorProfileRepository.save(any(BehaviorProfile.class)))
            .thenReturn(testBehavior);
        
        // When
        BehaviorProfileResponse response = behaviorProfileService
            .updateBehaviorProfile(1L, updateRequest);
        
        // Then
        assertNotNull(response);
        verify(behaviorProfileRepository).save(any(BehaviorProfile.class));
    }

    // =====================================================
    // CONSISTENCY CALCULATION Tests
    // =====================================================

    @Test
    @DisplayName("Should calculate consistency score")
    void testCalculateConsistency() {
        // Given
        when(behaviorProfileRepository.findByUserProfile_ProfileId(1L))
            .thenReturn(Optional.of(testBehavior));
        when(behaviorProfileRepository.save(any(BehaviorProfile.class)))
            .thenReturn(testBehavior);
        
        // When
        double consistency = behaviorProfileService.calculateAndUpdateConsistency(
            1L, 
            3,  // workouts completed
            4   // weekly goal
        );
        
        // Then
        assertTrue(consistency >= 0.0 && consistency <= 1.0);
        verify(behaviorProfileRepository).save(any(BehaviorProfile.class));
    }

    @Test
    @DisplayName("Should handle perfect consistency (100%)")
    void testPerfectConsistency() {
        // Given
        when(behaviorProfileRepository.findByUserProfile_ProfileId(1L))
            .thenReturn(Optional.of(testBehavior));
        when(behaviorProfileRepository.save(any(BehaviorProfile.class)))
            .thenReturn(testBehavior);
        
        // When
        double consistency = behaviorProfileService.calculateAndUpdateConsistency(
            1L, 
            4,  // workouts completed
            4   // weekly goal
        );
        
        // Then
        assertEquals(1.0, consistency);
    }

    @Test
    @DisplayName("Should handle zero workouts")
    void testZeroWorkouts() {
        // Given
        when(behaviorProfileRepository.findByUserProfile_ProfileId(1L))
            .thenReturn(Optional.of(testBehavior));
        when(behaviorProfileRepository.save(any(BehaviorProfile.class)))
            .thenReturn(testBehavior);
        
        // When
        double consistency = behaviorProfileService.calculateAndUpdateConsistency(
            1L, 
            0,  // workouts completed
            4   // weekly goal
        );
        
        // Then
        assertEquals(0.0, consistency);
    }

    // =====================================================
    // CREATE BehaviorProfile Tests
    // =====================================================

    @Test
    @DisplayName("Should create behavior profile for new user")
    void testCreateBehaviorProfile() {
        // Given
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(behaviorProfileRepository.save(any(BehaviorProfile.class)))
            .thenReturn(testBehavior);
        
        // When
        BehaviorProfileResponse response = behaviorProfileService.createBehaviorProfile(1L);
        
        // Then
        assertNotNull(response);
        verify(behaviorProfileRepository).save(any(BehaviorProfile.class));
    }

    @Test
    @DisplayName("Should throw exception when creating for non-existent user")
    void testCreateBehaviorProfileUserNotFound() {
        // Given
        when(userProfileRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            behaviorProfileService.createBehaviorProfile(999L);
        });
    }
}