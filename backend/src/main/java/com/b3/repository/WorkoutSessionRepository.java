package com.b3.repository;

import com.b3.model.UserProfile;
import com.b3.model.WorkoutSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for WorkoutSession entity
 * Tracks completed and in-progress workout sessions
 */
@Repository
public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Long> {
    
    /**
     * Find all sessions for a user
     */
    List<WorkoutSession> findByUserProfile(UserProfile userProfile);
    
    /**
     * Find sessions by user profile ID
     */
    List<WorkoutSession> findByUserProfile_ProfileId(Long profileId);
    
    /**
     * Find sessions by completion status
     */
    List<WorkoutSession> findByCompletionStatus(WorkoutSession.CompletionStatus status);
    
    /**
     * Find completed sessions for a user (returns list)
     */
    List<WorkoutSession> findByUserProfileAndCompletionStatus(
        UserProfile userProfile,
        WorkoutSession.CompletionStatus status
    );
    
    /**
     * Find sessions within date range
     */
    List<WorkoutSession> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
    
    /**
     * Find user's sessions within date range
     */
    List<WorkoutSession> findByUserProfileAndStartTimeBetween(
        UserProfile userProfile,
        LocalDateTime start,
        LocalDateTime end
    );
    
    /**
     * Find user's recent sessions (last 10)
     */
    List<WorkoutSession> findTop10ByUserProfileOrderByStartTimeDesc(UserProfile userProfile);
    
    /**
     * Find user's current in-progress session (single result)
     */
    Optional<WorkoutSession> findFirstByUserProfileAndCompletionStatusOrderByStartTimeDesc(
        UserProfile userProfile,
        WorkoutSession.CompletionStatus status
    );
    
    /**
     * Count completed workouts for user
     */
    long countByUserProfileAndCompletionStatus(
        UserProfile userProfile,
        WorkoutSession.CompletionStatus status
    );
    
    /**
     * Custom query: Get user's workout history with workout details
     */
    @Query("SELECT ws FROM WorkoutSession ws " +
           "JOIN FETCH ws.workout " +
           "WHERE ws.userProfile = :user " +
           "ORDER BY ws.startTime DESC")
    List<WorkoutSession> findUserWorkoutHistory(@Param("user") UserProfile user);
}