package com.b3.service;

import com.b3.dto.request.UserProfileUpdateRequest;
import com.b3.dto.response.UserProfileResponse;
import com.b3.exception.ResourceNotFoundException;
import com.b3.model.UserProfile;
import com.b3.repository.UserProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for UserProfile business logic
 * Following TDD - implementing methods to make tests pass
 */
@Service
@Transactional
public class UserProfileService {
    
    private static final Logger log = LoggerFactory.getLogger(UserProfileService.class);
    private final UserProfileRepository userProfileRepository;
    
    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }
    
    /**
     * Get user profile by ID
     * @throws ResourceNotFoundException if user not found
     */
    public UserProfileResponse getUserProfile(Long profileId) {
        log.info("Fetching user profile with ID: {}", profileId);
        
        UserProfile user = userProfileRepository.findById(profileId)
            .orElseThrow(() -> new ResourceNotFoundException("UserProfile", profileId));
        
        return mapToResponse(user);
    }
    
    /**
     * Update user profile
     * @throws ResourceNotFoundException if user not found
     */
    public UserProfileResponse updateUserProfile(Long profileId, UserProfileUpdateRequest request) {
        log.info("Updating user profile with ID: {}", profileId);
        
        UserProfile user = userProfileRepository.findById(profileId)
            .orElseThrow(() -> new ResourceNotFoundException("UserProfile", profileId));
        
        // Update fields if provided
        user.updateProfile(
            request.getDisplayName(),
            request.getAge(),
            request.getFitnessLevel(),
            request.getPrimaryGoal(),
            request.getEquipment(),
            request.getWeeklyGoalDays()
        );
        
        UserProfile updated = userProfileRepository.save(user);
        log.info("Successfully updated user profile: {}", profileId);
        
        return mapToResponse(updated);
    }
    
    /**
     * Check if user exists
     */
    public boolean userExists(Long profileId) {
        return userProfileRepository.existsById(profileId);
    }
    
    /**
     * Map UserProfile entity to UserProfileResponse DTO
     */
    private UserProfileResponse mapToResponse(UserProfile user) {
        UserProfileResponse response = new UserProfileResponse();
        response.setProfileId(user.getProfileId());
        response.setDisplayName(user.getDisplayName());
        response.setAge(user.getAge());
        response.setFitnessLevel(user.getFitnessLevel().name());
        response.setPrimaryGoal(user.getPrimaryGoal().name());
        response.setEquipment(user.getEquipment());
        response.setWeeklyGoalDays(user.getWeeklyGoalDays());
        response.setCurrentStreak(user.getCurrentStreak());
        response.setLongestStreak(user.getLongestStreak());
        response.setTotalWorkouts(user.getTotalWorkouts());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}