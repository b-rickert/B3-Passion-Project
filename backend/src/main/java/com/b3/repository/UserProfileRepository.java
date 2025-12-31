package com.b3.repository;

import com.b3.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

/**
 * Repository interface for UserProfile entity
 * Provides CRUD operations and custom queries for user profiles
 */
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    // Basic CRUD inherited from JpaRepository:
    // - save(UserProfile)
    // - findById(Long)
    // - findAll()
    // - deleteById(Long)
    // - count()

    /**
     * Find user by display name
     */
    Optional<UserProfile> findByDisplayName(String displayName);
    
    /**
     * Find users by fitness level
     */
    List<UserProfile> findByFitnessLevel(UserProfile.FitnessLevel fitnessLevel);
    
    /**
     * Find users by primary goal
     */
    List<UserProfile> findByPrimaryGoal(UserProfile.PrimaryGoal primaryGoal);
    
    /**
     * Find users with streak greater than or equal to value
     */
    List<UserProfile> findByCurrentStreakGreaterThanEqual(Integer streak);
    
    /**
     * Check if display name exists
     */
    boolean existsByDisplayName(String displayName);
}