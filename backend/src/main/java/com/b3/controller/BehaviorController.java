package com.b3.controller;

import com.b3.dto.request.BehaviorProfileUpdateRequest;
import com.b3.dto.response.BehaviorProfileResponse;
import com.b3.service.BehaviorProfileService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Behavior Profile operations
 * 
 * Handles BRIX learning data - tracks user patterns for adaptive coaching.
 */
@RestController
@RequestMapping("/api/v1/behavior")
public class BehaviorController {

    private static final Logger logger = LoggerFactory.getLogger(BehaviorController.class);

    private final BehaviorProfileService behaviorProfileService;

    public BehaviorController(BehaviorProfileService behaviorProfileService) {
        this.behaviorProfileService = behaviorProfileService;
    }

    // ========================================================================
    // GET ENDPOINTS
    // ========================================================================

    /**
     * Get behavior profile for a user
     * GET /api/v1/behavior/{profileId}
     */
    @GetMapping("/{profileId}")
    public ResponseEntity<BehaviorProfileResponse> getBehaviorProfile(@PathVariable Long profileId) {
        logger.info("GET /api/v1/behavior/{}", profileId);
        BehaviorProfileResponse profile = behaviorProfileService.getBehaviorProfile(profileId);
        return ResponseEntity.ok(profile);
    }

    /**
     * Get default user's behavior profile
     * GET /api/v1/behavior
     */
    @GetMapping
    public ResponseEntity<BehaviorProfileResponse> getDefaultBehaviorProfile() {
        logger.info("GET /api/v1/behavior (default user)");
        BehaviorProfileResponse profile = behaviorProfileService.getBehaviorProfile(1L);
        return ResponseEntity.ok(profile);
    }

    // ========================================================================
    // POST ENDPOINTS
    // ========================================================================

    /**
     * Create behavior profile for a user
     * POST /api/v1/behavior/{profileId}
     */
    @PostMapping("/{profileId}")
    public ResponseEntity<BehaviorProfileResponse> createBehaviorProfile(@PathVariable Long profileId) {
        logger.info("POST /api/v1/behavior/{}", profileId);
        BehaviorProfileResponse profile = behaviorProfileService.createBehaviorProfile(profileId);
        return ResponseEntity.ok(profile);
    }

    // ========================================================================
    // PUT ENDPOINTS
    // ========================================================================

    /**
     * Update behavior profile
     * PUT /api/v1/behavior/{profileId}
     */
    @PutMapping("/{profileId}")
    public ResponseEntity<BehaviorProfileResponse> updateBehaviorProfile(
            @PathVariable Long profileId,
            @Valid @RequestBody BehaviorProfileUpdateRequest updateRequest) {
        logger.info("PUT /api/v1/behavior/{}", profileId);
        BehaviorProfileResponse profile = behaviorProfileService.updateBehaviorProfile(profileId, updateRequest);
        return ResponseEntity.ok(profile);
    }
}