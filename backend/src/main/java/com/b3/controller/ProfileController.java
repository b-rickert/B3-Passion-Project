package com.b3.controller;

import com.b3.dto.request.UserProfileUpdateRequest;
import com.b3.dto.response.UserProfileResponse;
import com.b3.service.UserProfileService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for User Profile operations
 * 
 * Handles user profile retrieval and updates.
 * Single-user system (profileId = 1 for demo).
 */
@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    private final UserProfileService userProfileService;

    public ProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    // ========================================================================
    // GET ENDPOINTS
    // ========================================================================

    /**
     * Get user profile by ID
     * GET /api/v1/profile/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getProfile(@PathVariable Long id) {
        logger.info("GET /api/v1/profile/{}", id);
        UserProfileResponse profile = userProfileService.getUserProfile(id);
        return ResponseEntity.ok(profile);
    }

    /**
     * Get default user profile (ID = 1 for single-user demo)
     * GET /api/v1/profile
     */
    @GetMapping
    public ResponseEntity<UserProfileResponse> getDefaultProfile() {
        logger.info("GET /api/v1/profile (default user)");
        UserProfileResponse profile = userProfileService.getUserProfile(1L);
        return ResponseEntity.ok(profile);
    }

    // ========================================================================
    // PUT ENDPOINTS
    // ========================================================================

    /**
     * Update user profile
     * PUT /api/v1/profile/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody UserProfileUpdateRequest updateRequest) {
        logger.info("PUT /api/v1/profile/{}", id);
        UserProfileResponse updated = userProfileService.updateUserProfile(id, updateRequest);
        return ResponseEntity.ok(updated);
    }

    /**
     * Update default user profile
     * PUT /api/v1/profile
     */
    @PutMapping
    public ResponseEntity<UserProfileResponse> updateDefaultProfile(
            @Valid @RequestBody UserProfileUpdateRequest updateRequest) {
        logger.info("PUT /api/v1/profile (default user)");
        UserProfileResponse updated = userProfileService.updateUserProfile(1L, updateRequest);
        return ResponseEntity.ok(updated);
    }
}