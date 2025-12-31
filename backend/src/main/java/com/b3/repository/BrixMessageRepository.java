package com.b3.repository;

import com.b3.model.BrixMessage;
import com.b3.model.UserProfile;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for BrixMessage entity
 * Manages BRIX AI coach message history and retrieval
 */
@Repository
public interface BrixMessageRepository extends JpaRepository<BrixMessage, Long> {

    /**
     * Find all messages for a user, ordered by most recent
     */
    List<BrixMessage> findByUserProfileOrderBySentAtDesc(UserProfile userProfile);
    
    /**
     * Find messages by user profile ID
     */
    List<BrixMessage> findByUserProfile_ProfileIdOrderBySentAtDesc(Long profileId);
    
    /**
     * Find messages by type
     */
    List<BrixMessage> findByMessageType(BrixMessage.MessageType messageType);
    
    /**
     * Find user's messages by type
     */
    List<BrixMessage> findByUserProfileAndMessageType(
        UserProfile userProfile,
        BrixMessage.MessageType messageType
    );
    
    /**
     * Find messages by tone
     */
    List<BrixMessage> findByTone(BrixMessage.Tone tone);
    
    /**
     * Find recent messages (last N)
     */
    List<BrixMessage> findTop20ByUserProfileOrderBySentAtDesc(UserProfile userProfile);
    
    /**
     * Find messages sent after a specific time
     */
    List<BrixMessage> findByUserProfileAndSentAtAfter(
        UserProfile userProfile,
        LocalDateTime cutoffTime
    );
    
    /**
     * Find messages triggered by specific context
     */
    List<BrixMessage> findByContextTrigger(String contextTrigger);
    
    /**
     * Count messages for user
     */
    long countByUserProfile(UserProfile userProfile);
    
    /**
     * Custom query: Find today's messages
     */
    @Query("SELECT bm FROM BrixMessage bm WHERE bm.userProfile = :user " +
           "AND DATE(bm.sentAt) = CURRENT_DATE " +
           "ORDER BY bm.sentAt DESC")
    List<BrixMessage> findTodaysMessages(@Param("user") UserProfile user);
}