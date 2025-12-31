package com.b3.repository;

import com.b3.model.BehaviorProfile;
import com.b3.model.UserProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository interface for BehaviorProfile entity
 * Handles queries for user behavior tracking and patterns
 */
@Repository
public interface BehaviorProfileRepository extends JpaRepository<BehaviorProfile, Long> {

    /**
     * Find behavior profile by user profile
     */
    Optional<BehaviorProfile> findByUserProfile(UserProfile userProfile);
    
    /**
     * Find behavior profile by user profile ID
     */
    Optional<BehaviorProfile> findByUserProfile_ProfileId(Long profileId);
    
    /**
     * Check if behavior profile exists for user
     */
    boolean existsByUserProfile(UserProfile userProfile);
}