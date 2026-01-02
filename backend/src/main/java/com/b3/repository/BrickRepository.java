package com.b3.repository;

import com.b3.model.Brick;
import com.b3.model.UserProfile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Brick entity
 * Manages brick wall calendar queries and streak calculations
 */
@Repository
public interface BrickRepository extends JpaRepository<Brick, Long> {

    /**
     * Find all bricks for a user
     */
    List<Brick> findByUserProfile(UserProfile userProfile);
    
    /**
     * Find brick by user and date
     */
    Optional<Brick> findByUserProfileAndBrickDate(UserProfile userProfile, LocalDate date);
    
    /**
     * Find bricks within date range
     */
    List<Brick> findByBrickDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find user's bricks within date range, ordered by date
     */
    List<Brick> findByUserProfileAndBrickDateBetweenOrderByBrickDateAsc(
        UserProfile userProfile,
        LocalDate startDate,
        LocalDate endDate
    );
    
    /**
     * Find bricks by status
     */
    List<Brick> findByStatus(Brick.BrickStatus status);
    
    /**
     * Find user's bricks by status
     */
    List<Brick> findByUserProfileAndStatus(UserProfile userProfile, Brick.BrickStatus status);
    
    /**
     * Find user's recent bricks (last 30 days)
     */
    @Query("SELECT b FROM Brick b WHERE b.userProfile = :user " +
           "AND b.brickDate >= :cutoffDate " +
           "ORDER BY b.brickDate DESC")
    List<Brick> findRecentBricks(
        @Param("user") UserProfile user,
        @Param("cutoffDate") LocalDate cutoffDate
    );
    
    /**
     * Count LAID bricks for user in date range
     */
    long countByUserProfileAndStatusAndBrickDateBetween(
        UserProfile userProfile,
        Brick.BrickStatus status,
        LocalDate startDate,
        LocalDate endDate
    );
    
    /**
     * Find bricks for calculating streak (LAID status, ordered by date desc)
     */
    List<Brick> findByUserProfileAndStatusOrderByBrickDateDesc(
        UserProfile userProfile,
        Brick.BrickStatus status
    );

    /**
     * Find bricks for a user within date range
     */
    List<Brick> findByUserProfile_ProfileIdAndBrickDateBetween(
        Long profileId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Find all bricks for a user, ordered by date (newest first)
     */
    List<Brick> findByUserProfile_ProfileIdOrderByBrickDateDesc(Long profileId);
    
    /**
     * Count total bricks for a user
     */
    Long countByUserProfile_ProfileId(Long profileId);
    
    /**
     * Check if brick exists for user on specific date
     */
    boolean existsByUserProfile_ProfileIdAndBrickDate(Long profileId, LocalDate brickDate);
}