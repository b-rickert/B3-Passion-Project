package com.b3.controller;

import com.b3.model.Milestone;
import com.b3.model.Milestone.MilestoneType;
import com.b3.service.MilestoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for Milestone operations
 * 
 * Handles achievement tracking and milestone management.
 */
@RestController
@RequestMapping("/api/v1/milestones")
public class MilestoneController {

    private static final Logger logger = LoggerFactory.getLogger(MilestoneController.class);

    private final MilestoneService milestoneService;

    public MilestoneController(MilestoneService milestoneService) {
        this.milestoneService = milestoneService;
    }

    // ========================================================================
    // GET ENDPOINTS
    // ========================================================================

    /**
     * Get all milestones for user
     * GET /api/v1/milestones/{profileId}
     */
    @GetMapping("/{profileId}")
    public ResponseEntity<List<Milestone>> getAllMilestones(@PathVariable Long profileId) {
        logger.info("GET /api/v1/milestones/{}", profileId);
        List<Milestone> milestones = milestoneService.getAllMilestones(profileId);
        return ResponseEntity.ok(milestones);
    }

    /**
     * Get achieved milestones
     * GET /api/v1/milestones/{profileId}/achieved
     */
    @GetMapping("/{profileId}/achieved")
    public ResponseEntity<List<Milestone>> getAchievedMilestones(@PathVariable Long profileId) {
        logger.info("GET /api/v1/milestones/{}/achieved", profileId);
        List<Milestone> milestones = milestoneService.getAchievedMilestones(profileId);
        return ResponseEntity.ok(milestones);
    }

    /**
     * Get in-progress milestones
     * GET /api/v1/milestones/{profileId}/in-progress
     */
    @GetMapping("/{profileId}/in-progress")
    public ResponseEntity<List<Milestone>> getInProgressMilestones(@PathVariable Long profileId) {
        logger.info("GET /api/v1/milestones/{}/in-progress", profileId);
        List<Milestone> milestones = milestoneService.getInProgressMilestones(profileId);
        return ResponseEntity.ok(milestones);
    }

    /**
     * Get almost complete milestones (>= 80%)
     * GET /api/v1/milestones/{profileId}/almost-complete
     */
    @GetMapping("/{profileId}/almost-complete")
    public ResponseEntity<List<Milestone>> getAlmostCompleteMilestones(@PathVariable Long profileId) {
        logger.info("GET /api/v1/milestones/{}/almost-complete", profileId);
        List<Milestone> milestones = milestoneService.getAlmostCompleteMilestones(profileId);
        return ResponseEntity.ok(milestones);
    }

    /**
     * Get milestones by type
     * GET /api/v1/milestones/{profileId}/type/{type}
     */
    @GetMapping("/{profileId}/type/{type}")
    public ResponseEntity<List<Milestone>> getMilestonesByType(
            @PathVariable Long profileId,
            @PathVariable MilestoneType type) {
        logger.info("GET /api/v1/milestones/{}/type/{}", profileId, type);
        List<Milestone> milestones = milestoneService.getMilestonesByType(profileId, type);
        return ResponseEntity.ok(milestones);
    }

    /**
     * Get milestone stats
     * GET /api/v1/milestones/{profileId}/stats
     */
    @GetMapping("/{profileId}/stats")
    public ResponseEntity<Map<String, Object>> getMilestoneStats(@PathVariable Long profileId) {
        logger.info("GET /api/v1/milestones/{}/stats", profileId);
        long achievedCount = milestoneService.getAchievedCount(profileId);
        List<Milestone> all = milestoneService.getAllMilestones(profileId);
        
        return ResponseEntity.ok(Map.of(
                "totalMilestones", all.size(),
                "achievedCount", achievedCount,
                "inProgressCount", all.size() - achievedCount
        ));
    }

    // ========================================================================
    // POST ENDPOINTS
    // ========================================================================

    /**
     * Initialize default milestones for new user
     * POST /api/v1/milestones/{profileId}/initialize
     */
    @PostMapping("/{profileId}/initialize")
    public ResponseEntity<List<Milestone>> initializeMilestones(@PathVariable Long profileId) {
        logger.info("POST /api/v1/milestones/{}/initialize", profileId);
        List<Milestone> milestones = milestoneService.initializeDefaultMilestones(profileId);
        return ResponseEntity.ok(milestones);
    }

    /**
     * Create custom milestone
     * POST /api/v1/milestones/{profileId}/custom
     */
    @PostMapping("/{profileId}/custom")
    public ResponseEntity<Milestone> createCustomMilestone(
            @PathVariable Long profileId,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam MilestoneType type,
            @RequestParam Integer target) {
        logger.info("POST /api/v1/milestones/{}/custom - {}", profileId, name);
        Milestone milestone = milestoneService.createCustomMilestone(
                profileId, name, description, type, target);
        return ResponseEntity.ok(milestone);
    }

    /**
     * Check and update milestones after workout
     * POST /api/v1/milestones/{profileId}/check
     */
    @PostMapping("/{profileId}/check")
    public ResponseEntity<List<Milestone>> checkMilestones(@PathVariable Long profileId) {
        logger.info("POST /api/v1/milestones/{}/check", profileId);
        List<Milestone> newlyAchieved = milestoneService.checkMilestones(profileId);
        return ResponseEntity.ok(newlyAchieved);
    }

    // ========================================================================
    // PUT ENDPOINTS
    // ========================================================================

    /**
     * Update milestone progress for a type
     * PUT /api/v1/milestones/{profileId}/progress
     */
    @PutMapping("/{profileId}/progress")
    public ResponseEntity<Void> updateMilestoneProgress(
            @PathVariable Long profileId,
            @RequestParam MilestoneType type,
            @RequestParam int currentValue) {
        logger.info("PUT /api/v1/milestones/{}/progress?type={}&currentValue={}", profileId, type, currentValue);
        milestoneService.updateMilestoneProgress(profileId, type, currentValue);
        return ResponseEntity.ok().build();
    }
}