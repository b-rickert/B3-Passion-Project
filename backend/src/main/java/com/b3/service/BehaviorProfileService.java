package com.b3.service;

import com.b3.dto.request.BehaviorProfileUpdateRequest;
import com.b3.dto.response.BehaviorProfileResponse;
import com.b3.exception.ResourceNotFoundException;
import com.b3.model.BehaviorProfile;
import com.b3.model.UserProfile;
import com.b3.repository.BehaviorProfileRepository;
import com.b3.repository.UserProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for BehaviorProfile business logic
 */
@Service
@Transactional
public class BehaviorProfileService {
    
    private static final Logger log = LoggerFactory.getLogger(BehaviorProfileService.class);
    
    private final BehaviorProfileRepository behaviorProfileRepository;
    private final UserProfileRepository userProfileRepository;
    
    public BehaviorProfileService(
            BehaviorProfileRepository behaviorProfileRepository,
            UserProfileRepository userProfileRepository) {
        this.behaviorProfileRepository = behaviorProfileRepository;
        this.userProfileRepository = userProfileRepository;
    }
    
    /**
     * Get behavior profile by user ID
     */
    public BehaviorProfileResponse getBehaviorProfile(Long profileId) {
        log.info("Fetching behavior profile for user: {}", profileId);
        
        BehaviorProfile behavior = behaviorProfileRepository
            .findByUserProfile_ProfileId(profileId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "BehaviorProfile not found for user: " + profileId));
        
        return mapToResponse(behavior);
    }
    
    /**
     * Update behavior profile
     */
    public BehaviorProfileResponse updateBehaviorProfile(
            Long profileId, 
            BehaviorProfileUpdateRequest request) {
        
        log.info("Updating behavior profile for user: {}", profileId);
        
        BehaviorProfile behavior = behaviorProfileRepository
            .findByUserProfile_ProfileId(profileId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "BehaviorProfile not found for user: " + profileId));
        
        // Update fields if provided
        if (request.getPreferredWorkoutTime() != null) {
            behavior.setPreferredWorkoutTime(request.getPreferredWorkoutTime());
        }
        if (request.getAvgWorkoutTimeOfDay() != null) {
            behavior.setAvgWorkoutTimeOfDay(request.getAvgWorkoutTimeOfDay());
        }
        if (request.getAvgSessionDuration() != null) {
            behavior.setAvgSessionDuration(request.getAvgSessionDuration());
        }
        if (request.getPreferredWorkoutTypes() != null) {
            behavior.setPreferredWorkoutTypes(request.getPreferredWorkoutTypes());
        }
        if (request.getEnergyPattern() != null) {
            behavior.setEnergyPattern(request.getEnergyPattern());
        }
        if (request.getStressTrend() != null) {
            behavior.setStressTrend(request.getStressTrend());
        }
        if (request.getSkipFrequency() != null) {
            behavior.setSkipFrequency(request.getSkipFrequency());
        }
        
        BehaviorProfile updated = behaviorProfileRepository.save(behavior);
        log.info("Successfully updated behavior profile for user: {}", profileId);
        
        return mapToResponse(updated);
    }
    
    /**
     * Calculate and update consistency score
     */
    public double calculateAndUpdateConsistency(
            Long profileId,
            int workoutsCompleted,
            int weeklyGoal) {
        
        log.info("Calculating consistency for user {}: {}/{} workouts", 
            profileId, workoutsCompleted, weeklyGoal);
        
        BehaviorProfile behavior = behaviorProfileRepository
            .findByUserProfile_ProfileId(profileId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "BehaviorProfile not found for user: " + profileId));
        
        // Calculate consistency
        double newScore = weeklyGoal > 0 
            ? (double) workoutsCompleted / weeklyGoal 
            : 0.0;
        
        newScore = Math.min(newScore, 1.0);
        
        // Weighted average
        double currentScore = behavior.getConsistencyScore() != null 
            ? behavior.getConsistencyScore() 
            : 0.0;
        
        double updatedScore = (currentScore * 0.7) + (newScore * 0.3);
        
        behavior.setConsistencyScore(updatedScore);
        behaviorProfileRepository.save(behavior);
        
        log.info("Updated consistency score for user {}: {}", profileId, updatedScore);
        
        return updatedScore;
    }
    
    /**
     * Create new behavior profile for user
     */
    public BehaviorProfileResponse createBehaviorProfile(Long profileId) {
        log.info("Creating behavior profile for user: {}", profileId);
        
        UserProfile user = userProfileRepository.findById(profileId)
            .orElseThrow(() -> new ResourceNotFoundException("UserProfile", profileId));
        
        BehaviorProfile behavior = new BehaviorProfile(user);
        BehaviorProfile saved = behaviorProfileRepository.save(behavior);
        
        log.info("Successfully created behavior profile for user: {}", profileId);
        
        return mapToResponse(saved);
    }
    
    /**
     * Map BehaviorProfile entity to response DTO
     */
    private BehaviorProfileResponse mapToResponse(BehaviorProfile behavior) {
        BehaviorProfileResponse response = new BehaviorProfileResponse();
        response.setBehaviorId(behavior.getBehaviorId());
        response.setProfileId(behavior.getUserProfile().getProfileId());
        response.setConsistencyScore(behavior.getConsistencyScore());
        
        response.setPreferredWorkoutTime(
            behavior.getPreferredWorkoutTime() != null 
                ? behavior.getPreferredWorkoutTime().name() 
                : null
        );
        response.setAvgWorkoutTimeOfDay(
            behavior.getAvgWorkoutTimeOfDay() != null 
                ? behavior.getAvgWorkoutTimeOfDay().name() 
                : null
        );
        response.setStressTrend(
            behavior.getStressTrend() != null 
                ? behavior.getStressTrend().name() 
                : null
        );
        
        response.setCurrentTone(behavior.getCurrentTone().name());
        response.setConsecutiveDays(behavior.getConsecutiveDays());
        response.setLongestStreak(behavior.getLongestStreak());
        response.setTotalBricksLaid(behavior.getTotalBricksLaid());
        response.setLastWorkoutDate(behavior.getLastWorkoutDate());
        response.setMotivationState(behavior.getMotivationState().name());
        response.setMomentumTrend(behavior.getMomentumTrend().name());
        response.setFatigueScore(behavior.getFatigueScore());
        response.setRecentEnergyScore(behavior.getRecentEnergyScore());
        response.setLastToneChange(behavior.getLastToneChange());
        response.setAvgSessionDuration(behavior.getAvgSessionDuration());
        response.setPreferredWorkoutTypes(behavior.getPreferredWorkoutTypes());
        response.setEnergyPattern(behavior.getEnergyPattern());
        response.setSkipFrequency(behavior.getSkipFrequency());
        response.setUpdatedAt(behavior.getUpdatedAt());
        
        return response;
    }
}