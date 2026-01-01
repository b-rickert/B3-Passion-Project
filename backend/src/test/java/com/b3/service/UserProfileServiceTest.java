package com.b3.service;

import com.b3.dto.request.UserProfileUpdateRequest;
import com.b3.dto.response.UserProfileResponse;
import com.b3.exception.ResourceNotFoundException;
import com.b3.model.UserProfile;
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
 * TDD Tests for UserProfileService
 */
@DisplayName("UserProfileService Tests")
class UserProfileServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserProfileService userProfileService;

    private UserProfile testUser;

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
    }

    // =====================================================
    // GET Profile Tests
    // =====================================================

    @Test
    @DisplayName("Should get user profile by ID")
    void testGetUserProfile() {
        // Given
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        
        // When
        UserProfileResponse response = userProfileService.getUserProfile(1L);
        
        // Then
        assertNotNull(response);
        assertEquals("TestUser", response.getDisplayName());
        assertEquals(25, response.getAge());
        verify(userProfileRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user not found")
    void testGetUserProfileNotFound() {
        // Given
        when(userProfileRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            userProfileService.getUserProfile(999L);
        });
        
        verify(userProfileRepository).findById(999L);
    }

    // =====================================================
    // UPDATE Profile Tests
    // =====================================================

    @Test
    @DisplayName("Should update user profile")
    void testUpdateUserProfile() {
        // Given
        UserProfileUpdateRequest updateRequest = new UserProfileUpdateRequest();
        updateRequest.setDisplayName("UpdatedName");
        updateRequest.setAge(26);
        updateRequest.setWeeklyGoalDays(5);
        
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(testUser);
        
        // When
        UserProfileResponse response = userProfileService.updateUserProfile(1L, updateRequest);
        
        // Then
        assertNotNull(response);
        verify(userProfileRepository).findById(1L);
        verify(userProfileRepository).save(any(UserProfile.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent user")
    void testUpdateUserProfileNotFound() {
        // Given
        UserProfileUpdateRequest updateRequest = new UserProfileUpdateRequest();
        when(userProfileRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            userProfileService.updateUserProfile(999L, updateRequest);
        });
    }

    // =====================================================
    // Business Logic Tests
    // =====================================================

    @Test
    @DisplayName("Should check if user exists")
    void testUserExists() {
        // Given
        when(userProfileRepository.existsById(1L)).thenReturn(true);
        
        // When
        boolean exists = userProfileService.userExists(1L);
        
        // Then
        assertTrue(exists);
        verify(userProfileRepository).existsById(1L);
    }
}