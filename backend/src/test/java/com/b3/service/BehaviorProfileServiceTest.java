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
        
        testUser = new UserProfile(
            "TestUser",
            25,
            UserProfile.FitnessLevel.INTERMEDIATE,
            UserProfile.PrimaryGoal.STRENGTH,
            "Dumbbells",
            4
        );
        testUser.setProfileId(1L);
        
        testBehavior = new BehaviorProfile(testUser);
        testBehavior.setBehaviorId(1L);
        testBehavior.setConsistencyScore(0.75);
        testBehavior.setPreferredWorkoutTime(BehaviorProfile.PreferredTime.MORNING);
        testBehavior.setAvgSessionDuration(35);
    }

    @Test
    @DisplayName("Should get behavior profile by user ID")
    void testGetBehaviorProfile() {
        when(behaviorProfileRepository.findByUserProfile_ProfileId(1L))
            .thenReturn(Optional.of(testBehavior));
        
        BehaviorProfileResponse response = behaviorProfileService.getBehaviorProfile(1L);
        
        assertNotNull(response);
        assertEquals(0.75, response.getConsistencyScore());
        assertEquals("MORNING", response.getPreferredWorkoutTime());
        verify(behaviorProfileRepository).findByUserProfile_ProfileId(1L);
    }

    @Test
    @DisplayName("Should throw exception when behavior profile not found")
    void testGetBehaviorProfileNotFound() {
        when(behaviorProfileRepository.findByUserProfile_ProfileId(999L))
            .thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            behaviorProfileService.getBehaviorProfile(999L);
        });
    }

    @Test
    @DisplayName("Should update behavior profile")
    void testUpdateBehaviorProfile() {
        BehaviorProfileUpdateRequest updateRequest = new BehaviorProfileUpdateRequest();
        updateRequest.setPreferredWorkoutTime(BehaviorProfile.PreferredTime.EVENING);
        updateRequest.setAvgSessionDuration(45);
        
        when(behaviorProfileRepository.findByUserProfile_ProfileId(1L))
            .thenReturn(Optional.of(testBehavior));
        when(behaviorProfileRepository.save(any(BehaviorProfile.class)))
            .thenReturn(testBehavior);
        
        BehaviorProfileResponse response = behaviorProfileService
            .updateBehaviorProfile(1L, updateRequest);
        
        assertNotNull(response);
        verify(behaviorProfileRepository).save(any(BehaviorProfile.class));
    }

    @Test
    @DisplayName("Should calculate consistency score")
    void testCalculateConsistency() {
        when(behaviorProfileRepository.findByUserProfile_ProfileId(1L))
            .thenReturn(Optional.of(testBehavior));
        when(behaviorProfileRepository.save(any(BehaviorProfile.class)))
            .thenReturn(testBehavior);
        
        double consistency = behaviorProfileService.calculateAndUpdateConsistency(1L, 3, 4);
        
        assertTrue(consistency >= 0.0 && consistency <= 1.0);
        verify(behaviorProfileRepository).save(any(BehaviorProfile.class));
    }

    @Test
    @DisplayName("Should handle perfect consistency (moves toward 100%)")
    void testPerfectConsistency() {
        when(behaviorProfileRepository.findByUserProfile_ProfileId(1L))
            .thenReturn(Optional.of(testBehavior));
        when(behaviorProfileRepository.save(any(BehaviorProfile.class)))
            .thenReturn(testBehavior);
        
        double consistency = behaviorProfileService.calculateAndUpdateConsistency(1L, 4, 4);
        
        // Weighted average: (0.75 * 0.7) + (1.0 * 0.3) = 0.825
        assertEquals(0.825, consistency, 0.001);
        assertTrue(consistency > 0.75); // Improved from previous
    }

    @Test
    @DisplayName("Should handle zero workouts (moves toward 0%)")
    void testZeroWorkouts() {
        when(behaviorProfileRepository.findByUserProfile_ProfileId(1L))
            .thenReturn(Optional.of(testBehavior));
        when(behaviorProfileRepository.save(any(BehaviorProfile.class)))
            .thenReturn(testBehavior);
        
        double consistency = behaviorProfileService.calculateAndUpdateConsistency(1L, 0, 4);
        
        // Weighted average: (0.75 * 0.7) + (0.0 * 0.3) = 0.525
        assertEquals(0.525, consistency, 0.001);
        assertTrue(consistency < 0.75); // Declined from previous
    }
git
    @Test
    @DisplayName("Should create behavior profile for new user")
    void testCreateBehaviorProfile() {
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(behaviorProfileRepository.save(any(BehaviorProfile.class)))
            .thenReturn(testBehavior);
        
        BehaviorProfileResponse response = behaviorProfileService.createBehaviorProfile(1L);
        
        assertNotNull(response);
        verify(behaviorProfileRepository).save(any(BehaviorProfile.class));
    }

    @Test
    @DisplayName("Should throw exception when creating for non-existent user")
    void testCreateBehaviorProfileUserNotFound() {
        when(userProfileRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            behaviorProfileService.createBehaviorProfile(999L);
        });
    }
}