package com.b3.service;

import com.b3.dto.DailyLogCreateDTO;
import com.b3.dto.DailyLogDTO;
import com.b3.exception.ResourceNotFoundException;
import com.b3.exception.DuplicateResourceException;
import com.b3.model.DailyLog;
import com.b3.model.UserProfile;
import com.b3.repository.DailyLogRepository;
import com.b3.repository.UserProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for DailyLog operations
 * 
 * Handles daily check-ins for energy, stress, sleep, and mood tracking.
 * Powers BRIX's adaptive recommendations by tracking daily wellness indicators.
 */
@Service
@Transactional
public class DailyLogService {

    private static final Logger logger = LoggerFactory.getLogger(DailyLogService.class);

    private final DailyLogRepository dailyLogRepository;
    private final UserProfileRepository userProfileRepository;

    // ========================================================================
    // CONSTRUCTOR
    // ========================================================================

    public DailyLogService(DailyLogRepository dailyLogRepository,
                          UserProfileRepository userProfileRepository) {
        this.dailyLogRepository = dailyLogRepository;
        this.userProfileRepository = userProfileRepository;
    }

    // ========================================================================
    // PUBLIC METHODS
    // ========================================================================

    /**
     * Submit a daily log entry
     * Only one log per user per day is allowed
     */
    public DailyLogDTO submitDailyLog(DailyLogCreateDTO createDTO) {
        logger.info("Submitting daily log for user: {}", createDTO.getProfileId());

        // Get user profile
        UserProfile userProfile = userProfileRepository.findById(createDTO.getProfileId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User profile not found with ID: " + createDTO.getProfileId()));

        // Check if log already exists for today
        LocalDate today = LocalDate.now();
        if (dailyLogRepository.existsByUserProfileAndLogDate(userProfile, today)) {
            throw new DuplicateResourceException(
                    "Daily log already exists for user " + createDTO.getProfileId() + " on " + today);
        }

        // Create new daily log
        DailyLog dailyLog = new DailyLog();
        dailyLog.setUserProfile(userProfile);
        dailyLog.setLogDate(today);
        dailyLog.setEnergyLevel(createDTO.getEnergyLevel());
        dailyLog.setStressLevel(createDTO.getStressLevel());
        dailyLog.setSleepQuality(createDTO.getSleepQuality());
        dailyLog.setMood(DailyLog.Mood.valueOf(createDTO.getMood()));
        dailyLog.setNotes(createDTO.getNotes());

        DailyLog savedLog = dailyLogRepository.save(dailyLog);
        logger.info("Successfully created daily log with ID: {}", savedLog.getLogId());

        return mapToDTO(savedLog);
    }

    /**
     * Get today's log for a user
     */
    @Transactional(readOnly = true)
    public DailyLogDTO getTodaysLog(Long profileId) {
        logger.info("Fetching today's log for user: {}", profileId);

        UserProfile userProfile = userProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User profile not found with ID: " + profileId));

        LocalDate today = LocalDate.now();
        DailyLog dailyLog = dailyLogRepository.findByUserProfileAndLogDate(userProfile, today)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No daily log found for user " + profileId + " on " + today));

        return mapToDTO(dailyLog);
    }

    /**
     * Get recent logs for a user (last 7 days)
     */
    @Transactional(readOnly = true)
    public List<DailyLogDTO> getRecentLogs(Long profileId) {
        logger.info("Fetching recent logs for user: {}", profileId);

        UserProfile userProfile = userProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User profile not found with ID: " + profileId));

        LocalDate cutoffDate = LocalDate.now().minusDays(7);
        List<DailyLog> logs = dailyLogRepository.findRecentLogs(userProfile, cutoffDate);

        logger.info("Found {} recent logs for user {}", logs.size(), profileId);
        return logs.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get logs within a date range
     */
    @Transactional(readOnly = true)
    public List<DailyLogDTO> getLogsByDateRange(Long profileId, LocalDate startDate, LocalDate endDate) {
        logger.info("Fetching logs for user {} from {} to {}", profileId, startDate, endDate);

        UserProfile userProfile = userProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User profile not found with ID: " + profileId));

        List<DailyLog> logs = dailyLogRepository.findByUserProfileAndLogDateBetweenOrderByLogDateDesc(
                userProfile, startDate, endDate);

        logger.info("Found {} logs in date range for user {}", logs.size(), profileId);
        return logs.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Check if user has logged today
     */
    @Transactional(readOnly = true)
    public boolean hasLoggedToday(Long profileId) {
        logger.info("Checking if user {} has logged today", profileId);

        UserProfile userProfile = userProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User profile not found with ID: " + profileId));

        return dailyLogRepository.existsByUserProfileAndLogDate(userProfile, LocalDate.now());
    }

    /**
     * Get days where user needs recovery (low energy + high stress)
     */
    @Transactional(readOnly = true)
    public List<DailyLogDTO> getRecoveryNeededDays(Long profileId) {
        logger.info("Fetching recovery needed days for user: {}", profileId);

        UserProfile userProfile = userProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User profile not found with ID: " + profileId));

        List<DailyLog> logs = dailyLogRepository.findRecoveryNeededDays(userProfile);

        logger.info("Found {} recovery days for user {}", logs.size(), profileId);
        return logs.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update an existing daily log (same day only)
     */
    public DailyLogDTO updateDailyLog(Long logId, DailyLogCreateDTO updateDTO) {
        logger.info("Updating daily log: {}", logId);

        DailyLog dailyLog = dailyLogRepository.findById(logId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Daily log not found with ID: " + logId));

        // Only allow updating today's log
        if (!dailyLog.getLogDate().equals(LocalDate.now())) {
            throw new IllegalStateException("Can only update today's log");
        }

        dailyLog.setEnergyLevel(updateDTO.getEnergyLevel());
        dailyLog.setStressLevel(updateDTO.getStressLevel());
        dailyLog.setSleepQuality(updateDTO.getSleepQuality());
        dailyLog.setMood(DailyLog.Mood.valueOf(updateDTO.getMood()));
        dailyLog.setNotes(updateDTO.getNotes());

        DailyLog savedLog = dailyLogRepository.save(dailyLog);
        logger.info("Successfully updated daily log: {}", logId);

        return mapToDTO(savedLog);
    }

    // ========================================================================
    // PRIVATE HELPER METHODS
    // ========================================================================

    /**
     * Map DailyLog entity to DailyLogDTO
     */
    private DailyLogDTO mapToDTO(DailyLog entity) {
        DailyLogDTO dto = new DailyLogDTO();
        dto.setLogId(entity.getLogId());
        dto.setProfileId(entity.getUserProfile().getProfileId());
        dto.setDate(entity.getLogDate().toString());
        dto.setEnergyLevel(entity.getEnergyLevel());
        dto.setStressLevel(entity.getStressLevel());
        dto.setSleepQuality(entity.getSleepQuality());
        dto.setMood(entity.getMood().name());
        dto.setNotes(entity.getNotes());
        dto.setWellnessScore(entity.getWellnessScore());
        dto.setNeedsRecovery(entity.needsRecovery());
        dto.setOptimalWorkoutDay(entity.isOptimalWorkoutDay());
        return dto;
    }
}