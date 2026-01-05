package com.b3.service;

import com.b3.exception.ResourceNotFoundException;
import com.b3.model.Milestone;
import com.b3.model.Milestone.MilestoneType;
import com.b3.model.UserProfile;
import com.b3.repository.MilestoneRepository;
import com.b3.repository.UserProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service layer for Milestone operations
 * 
 * Handles achievement tracking, milestone creation, and progress updates.
 * Automatically checks for milestone completion after workouts.
 */
@Service
@Transactional
public class MilestoneService {

    private static final Logger logger = LoggerFactory.getLogger(MilestoneService.class);

    private final MilestoneRepository milestoneRepository;
    private final UserProfileRepository userProfileRepository;

    // ========================================================================
    // CONSTRUCTOR
    // ========================================================================

    public MilestoneService(MilestoneRepository milestoneRepository,
                           UserProfileRepository userProfileRepository) {
        this.milestoneRepository = milestoneRepository;
        this.userProfileRepository = userProfileRepository;
    }

    // ========================================================================
    // PUBLIC METHODS - MILESTONE RETRIEVAL
    // ========================================================================

    /**
     * Get all milestones for a user
     */
    @Transactional(readOnly = true)
    public List<Milestone> getAllMilestones(Long profileId) {
        logger.info("Fetching all milestones for user: {}", profileId);

        UserProfile userProfile = getUserProfile(profileId);
        List<Milestone> milestones = milestoneRepository.findByUserProfileOrderByCreatedAtDesc(userProfile);

        logger.info("Found {} milestones for user {}", milestones.size(), profileId);
        return milestones;
    }

    /**
     * Get achieved milestones for a user
     */
    @Transactional(readOnly = true)
    public List<Milestone> getAchievedMilestones(Long profileId) {
        logger.info("Fetching achieved milestones for user: {}", profileId);

        UserProfile userProfile = getUserProfile(profileId);
        return milestoneRepository.findByUserProfileAndIsAchieved(userProfile, true);
    }

    /**
     * Get in-progress milestones for a user
     */
    @Transactional(readOnly = true)
    public List<Milestone> getInProgressMilestones(Long profileId) {
        logger.info("Fetching in-progress milestones for user: {}", profileId);

        UserProfile userProfile = getUserProfile(profileId);
        return milestoneRepository.findInProgressMilestones(userProfile);
    }

    /**
     * Get milestones almost complete (>= 80% progress)
     */
    @Transactional(readOnly = true)
    public List<Milestone> getAlmostCompleteMilestones(Long profileId) {
        logger.info("Fetching almost complete milestones for user: {}", profileId);

        UserProfile userProfile = getUserProfile(profileId);
        return milestoneRepository.findAlmostCompleteMilestones(userProfile);
    }

    /**
     * Get milestones by type
     */
    @Transactional(readOnly = true)
    public List<Milestone> getMilestonesByType(Long profileId, MilestoneType type) {
        logger.info("Fetching {} milestones for user: {}", type, profileId);

        UserProfile userProfile = getUserProfile(profileId);
        return milestoneRepository.findByUserProfileAndMilestoneType(userProfile, type);
    }

    /**
     * Get count of achieved milestones
     */
    @Transactional(readOnly = true)
    public long getAchievedCount(Long profileId) {
        UserProfile userProfile = getUserProfile(profileId);
        return milestoneRepository.countByUserProfileAndIsAchieved(userProfile, true);
    }

    // ========================================================================
    // PUBLIC METHODS - MILESTONE CREATION
    // ========================================================================

    /**
     * Initialize default milestones for a new user
     * Called when user profile is created
     */
    public List<Milestone> initializeDefaultMilestones(Long profileId) {
        logger.info("Initializing default milestones for user: {}", profileId);

        UserProfile userProfile = getUserProfile(profileId);
        List<Milestone> milestones = new ArrayList<>();

        // TIER 1: BEGINNER WINS (Week 1)
        milestones.add(createMilestone(userProfile, "First Brick Laid! 🧱",
                "Your first workout is complete!", MilestoneType.WORKOUT_COUNT, 1));
        milestones.add(createMilestone(userProfile, "3-Day Streak! 🔥",
                "Three days in a row! You're building momentum!", MilestoneType.STREAK, 3));
        milestones.add(createMilestone(userProfile, "5 Workouts! 💪",
                "You've completed 5 workouts!", MilestoneType.WORKOUT_COUNT, 5));
        milestones.add(createMilestone(userProfile, "7-Day Warrior! ⚡",
                "One full week! This is a habit now!", MilestoneType.STREAK, 7));

        // TIER 2: BUILDING MOMENTUM (Month 1)
        milestones.add(createMilestone(userProfile, "First 10! 🎯",
                "10 workouts complete! You're serious about this!", MilestoneType.WORKOUT_COUNT, 10));
        milestones.add(createMilestone(userProfile, "14-Day Dedication! 🌟",
                "Two weeks strong!", MilestoneType.STREAK, 14));
        milestones.add(createMilestone(userProfile, "25 Workouts! 🏋️",
                "Quarter century of workouts!", MilestoneType.WORKOUT_COUNT, 25));
        milestones.add(createMilestone(userProfile, "30-Day Champion! 🏆",
                "One full month of consistency!", MilestoneType.STREAK, 30));

        // TIER 3: ESTABLISHED HABIT (2-6 Months)
        milestones.add(createMilestone(userProfile, "50 Workouts! 🎖️",
                "Half a hundred! Impressive!", MilestoneType.WORKOUT_COUNT, 50));
        milestones.add(createMilestone(userProfile, "60-Day Diamond! 💎",
                "Two months of daily dedication!", MilestoneType.STREAK, 60));
        milestones.add(createMilestone(userProfile, "Century Club! 💯",
                "100 workouts completed!", MilestoneType.WORKOUT_COUNT, 100));
        milestones.add(createMilestone(userProfile, "100-Day Club! 👑",
                "Triple digits! You're elite!", MilestoneType.STREAK, 100));

        // TIER 4: ADVANCED (6+ Months)
        milestones.add(createMilestone(userProfile, "200 Workouts! 🎖️",
                "Two hundred strong!", MilestoneType.WORKOUT_COUNT, 200));
        milestones.add(createMilestone(userProfile, "6-Month Streak! 🏅",
                "180 days of consistency!", MilestoneType.STREAK, 180));
        milestones.add(createMilestone(userProfile, "365-Day Legend! 🎆",
                "ONE FULL YEAR! Incredible!", MilestoneType.STREAK, 365));
        milestones.add(createMilestone(userProfile, "500-Workout Hero! 🦸",
                "You're a fitness superhero!", MilestoneType.WORKOUT_COUNT, 500));

        // Save all milestones
        List<Milestone> savedMilestones = milestoneRepository.saveAll(milestones);
        logger.info("Created {} default milestones for user {}", savedMilestones.size(), profileId);

        return savedMilestones;
    }

    /**
     * Create a custom milestone for a user
     */
    public Milestone createCustomMilestone(Long profileId, String name, String description,
                                           MilestoneType type, Integer targetValue) {
        logger.info("Creating custom milestone '{}' for user: {}", name, profileId);

        UserProfile userProfile = getUserProfile(profileId);
        Milestone milestone = createMilestone(userProfile, name, description, type, targetValue);

        Milestone saved = milestoneRepository.save(milestone);
        logger.info("Created custom milestone with ID: {}", saved.getMilestoneId());

        return saved;
    }

    // ========================================================================
    // PUBLIC METHODS - MILESTONE PROGRESS
    // ========================================================================

    /**
     * Check and update all milestones after a workout
     * Returns list of newly achieved milestones
     */
    public List<Milestone> checkMilestones(Long profileId) {
        logger.info("Checking milestones for user: {}", profileId);

        UserProfile userProfile = getUserProfile(profileId);
        List<Milestone> newlyAchieved = new ArrayList<>();

        // Get all unachieved milestones
        List<Milestone> activeMilestones = milestoneRepository
                .findByUserProfileAndIsAchieved(userProfile, false);

        for (Milestone milestone : activeMilestones) {
            int newValue = getCurrentValueForType(userProfile, milestone.getMilestoneType());
            
            // Update progress
            boolean wasAchieved = milestone.getIsAchieved();
            milestone.updateProgress(newValue);

            // Check if just achieved
            if (!wasAchieved && milestone.getIsAchieved()) {
                newlyAchieved.add(milestone);
                logger.info("🎉 Milestone achieved: {} for user {}", 
                        milestone.getMilestoneName(), profileId);
            }

            milestoneRepository.save(milestone);
        }

        if (!newlyAchieved.isEmpty()) {
            logger.info("User {} achieved {} new milestone(s)!", profileId, newlyAchieved.size());
        }

        return newlyAchieved;
    }

    /**
     * Update progress for a specific milestone type
     */
    public void updateMilestoneProgress(Long profileId, MilestoneType type, int currentValue) {
        logger.info("Updating {} milestones for user {} with value {}", type, profileId, currentValue);

        UserProfile userProfile = getUserProfile(profileId);
        List<Milestone> milestones = milestoneRepository
                .findByUserProfileAndMilestoneType(userProfile, type);

        for (Milestone milestone : milestones) {
            if (!milestone.getIsAchieved()) {
                milestone.updateProgress(currentValue);
                milestoneRepository.save(milestone);
            }
        }
    }

    // ========================================================================
    // PRIVATE HELPER METHODS
    // ========================================================================

    /**
     * Get UserProfile by ID or throw exception
     */
    private UserProfile getUserProfile(Long profileId) {
        return userProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User profile not found with ID: " + profileId));
    }

    /**
     * Create a new milestone (not saved)
     */
    private Milestone createMilestone(UserProfile user, String name, String description,
                                      MilestoneType type, Integer target) {
        return new Milestone(user, name, description, type, target);
    }

    /**
     * Get current value based on milestone type
     */
    private int getCurrentValueForType(UserProfile user, MilestoneType type) {
        switch (type) {
            case STREAK:
                return user.getCurrentStreak() != null ? user.getCurrentStreak() : 0;
            case WORKOUT_COUNT:
                return user.getTotalWorkouts() != null ? user.getTotalWorkouts() : 0;
            case CONSISTENCY:
                // TODO: Calculate from workout history
                return 0;
            case GOAL_ACHIEVED:
            case PERSONAL_RECORD:
                // These are manually triggered
                return 0;
            default:
                return 0;
        }
    }
}