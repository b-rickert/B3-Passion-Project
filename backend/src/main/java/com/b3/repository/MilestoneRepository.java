package com.b3.repository;

import com.b3.model.Milestone;
import com.b3.model.UserProfile;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Milestone entity
 * Tracks achievement progress and milestone completion
 */
@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, Long> {

    /**
     * Find all milestones for a user
     */
    List<Milestone> findByUserProfile(UserProfile userProfile);
    
    /**
     * Find milestones by achievement status
     */
    List<Milestone> findByIsAchieved(Boolean isAchieved);
    
    /**
     * Find user's achieved milestones
     */
    List<Milestone> findByUserProfileAndIsAchieved(
        UserProfile userProfile,
        Boolean isAchieved
    );
    
    /**
     * Find milestones by type
     */
    List<Milestone> findByMilestoneType(Milestone.MilestoneType milestoneType);
    
    /**
     * Find user's milestones by type
     */
    List<Milestone> findByUserProfileAndMilestoneType(
        UserProfile userProfile,
        Milestone.MilestoneType milestoneType
    );
    
    /**
     * Find user's milestones ordered by creation date
     */
    List<Milestone> findByUserProfileOrderByCreatedAtDesc(UserProfile userProfile);
    
    /**
     * Find user's in-progress milestones (not achieved, progress > 0)
     */
    @Query("SELECT m FROM Milestone m WHERE m.userProfile = :user " +
           "AND m.isAchieved = false AND m.currentValue > 0 " +
           "ORDER BY m.currentValue DESC")
    List<Milestone> findInProgressMilestones(@Param("user") UserProfile user);
    
    /**
     * Find milestones almost complete (>= 80% progress)
     */
    @Query("SELECT m FROM Milestone m WHERE m.userProfile = :user " +
           "AND m.isAchieved = false " +
           "AND (m.currentValue * 1.0 / m.targetValue) >= 0.8 " +
           "ORDER BY (m.currentValue * 1.0 / m.targetValue) DESC")
    List<Milestone> findAlmostCompleteMilestones(@Param("user") UserProfile user);
    
    /**
     * Count achieved milestones for user
     */
    long countByUserProfileAndIsAchieved(UserProfile userProfile, Boolean isAchieved);
    
    /**
     * Find most recently achieved milestone
     */
    List<Milestone> findTop1ByUserProfileAndIsAchievedOrderByAchievedAtDesc(
        UserProfile userProfile,
        Boolean isAchieved
    );
}
