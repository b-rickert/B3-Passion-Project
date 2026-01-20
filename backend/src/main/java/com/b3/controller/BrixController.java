package com.b3.controller;

import com.b3.model.BrixMessage;
import com.b3.service.BrixService;
import com.b3.service.BrixService.BrixChatResponse;
import com.b3.service.BrixService.WorkoutRecommendation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for BRIX AI Coach
 * 
 * Handles chat interactions, workout recommendations, and context-triggered messages.
 */
@RestController
@RequestMapping("/api/v1/brix")
public class BrixController {

    private static final Logger logger = LoggerFactory.getLogger(BrixController.class);

    private final BrixService brixService;

    public BrixController(BrixService brixService) {
        this.brixService = brixService;
    }

    // ========================================================================
    // CHAT ENDPOINTS
    // ========================================================================

    /**
     * Send a message to BRIX and get a response
     * POST /api/v1/brix/chat
     */
    @PostMapping("/chat")
    public ResponseEntity<BrixChatResponse> chat(@RequestBody Map<String, Object> request) {
        Long profileId = Long.valueOf(request.get("profileId").toString());
        String message = request.get("message").toString();

        logger.info("POST /api/v1/brix/chat - User {}: {}", profileId, message);

        BrixChatResponse response = brixService.chat(profileId, message);
        return ResponseEntity.ok(response);
    }

    // ========================================================================
    // RECOMMENDATION ENDPOINTS
    // ========================================================================

    /**
     * Get personalized workout recommendation
     * GET /api/v1/brix/recommendation/{profileId}
     */
    @GetMapping("/recommendation/{profileId}")
    public ResponseEntity<WorkoutRecommendation> getRecommendation(@PathVariable Long profileId) {
        logger.info("GET /api/v1/brix/recommendation/{}", profileId);

        WorkoutRecommendation recommendation = brixService.getWorkoutRecommendation(profileId);
        if (recommendation == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(recommendation);
    }

    // ========================================================================
    // MESSAGE HISTORY ENDPOINTS
    // ========================================================================

    /**
     * Get recent BRIX messages
     * GET /api/v1/brix/messages/{profileId}
     */
    @GetMapping("/messages/{profileId}")
    public ResponseEntity<List<BrixMessage>> getMessages(
            @PathVariable Long profileId,
            @RequestParam(defaultValue = "20") int limit) {
        logger.info("GET /api/v1/brix/messages/{} (limit={})", profileId, limit);

        List<BrixMessage> messages = brixService.getRecentMessages(profileId, limit);
        return ResponseEntity.ok(messages);
    }

    // ========================================================================
    // CONTEXT TRIGGER ENDPOINTS
    // ========================================================================

    /**
     * Trigger a context-based message
     * POST /api/v1/brix/trigger
     * 
     * Triggers: app_open, workout_complete, streak_milestone, missed_day, low_energy
     */
    @PostMapping("/trigger")
    public ResponseEntity<BrixMessage> triggerMessage(@RequestBody Map<String, Object> request) {
        Long profileId = Long.valueOf(request.get("profileId").toString());
        String trigger = request.get("trigger").toString();

        logger.info("POST /api/v1/brix/trigger - User {}: {}", profileId, trigger);

        BrixMessage message = brixService.generateContextMessage(profileId, trigger);
        return ResponseEntity.ok(message);
    }
}
