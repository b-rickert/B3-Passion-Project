package com.b3.repository;

import com.b3.model.DailyLog;
import com.b3.model.UserProfile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for DailyLog entity
 * Handles daily check-in data and wellness tracking
 */
@Repository
public interface DailyLogRepository extends JpaRepository<DailyLog, Long> {

    /**
     * Find all logs for a user
     */
    List<DailyLog> findByUserProfile(UserProfile userProfile);
    
    /**
     * Find log by user and date
     */
    Optional<DailyLog> findByUserProfileAndLogDate(UserProfile userProfile, LocalDate date);
    
    /**
     * Find logs within date range
     */
    List<DailyLog> findByLogDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find user's logs within date range, ordered by date
     */
    List<DailyLog> findByUserProfileAndLogDateBetweenOrderByLogDateDesc(
        UserProfile userProfile,
        LocalDate startDate,
        LocalDate endDate
    );
    
    /**
     * Find user's recent logs (last 7 days)
     */
    @Query("SELECT dl FROM DailyLog dl WHERE dl.userProfile = :user " +
           "AND dl.logDate >= :cutoffDate " +
           "ORDER BY dl.logDate DESC")
    List<DailyLog> findRecentLogs(
        @Param("user") UserProfile user,
        @Param("cutoffDate") LocalDate cutoffDate
    );
    
    /**
     * Find logs by mood
     */
    List<DailyLog> findByMood(DailyLog.Mood mood);
    
    /**
     * Find user's logs by mood
     */
    List<DailyLog> findByUserProfileAndMood(UserProfile userProfile, DailyLog.Mood mood);
    
    /**
     * Check if log exists for user on date
     */
    boolean existsByUserProfileAndLogDate(UserProfile userProfile, LocalDate date);
    
    /**
     * Custom query: Find days needing recovery (low wellness score)
     */
    @Query("SELECT dl FROM DailyLog dl WHERE dl.userProfile = :user " +
           "AND dl.energyLevel <= 2 AND dl.stressLevel >= 4 " +
           "ORDER BY dl.logDate DESC")
    List<DailyLog> findRecoveryNeededDays(@Param("user") UserProfile user);
}