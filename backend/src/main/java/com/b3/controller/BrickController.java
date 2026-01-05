package com.b3.controller;

import com.b3.dto.response.BrickResponse;
import com.b3.dto.response.BrickStatsResponse;
import com.b3.service.BrickService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Brick operations
 * 
 * Handles the brick wall visualization - the core visual metaphor of B3.
 * Each completed workout = one brick laid.
 */
@RestController
@RequestMapping("/api/v1/bricks")
public class BrickController {

    private static final Logger logger = LoggerFactory.getLogger(BrickController.class);

    private final BrickService brickService;

    public BrickController(BrickService brickService) {
        this.brickService = brickService;
    }

    // ========================================================================
    // GET ENDPOINTS
    // ========================================================================

    /**
     * Get brick wall calendar for a month
     * GET /api/v1/bricks/calendar/{profileId}?month=1&year=2025
     */
    @GetMapping("/calendar/{profileId}")
    public ResponseEntity<List<BrickResponse>> getBrickCalendar(
            @PathVariable Long profileId,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        
        // Default to current month/year if not provided
        LocalDate now = LocalDate.now();
        int m = (month != null) ? month : now.getMonthValue();
        int y = (year != null) ? year : now.getYear();
        
        logger.info("GET /api/v1/bricks/calendar/{}?month={}&year={}", profileId, m, y);
        List<BrickResponse> calendar = brickService.getBrickCalendar(profileId, y, m);
        return ResponseEntity.ok(calendar);
    }

    /**
     * Get brick stats for user
     * GET /api/v1/bricks/stats/{profileId}
     */
    @GetMapping("/stats/{profileId}")
    public ResponseEntity<BrickStatsResponse> getBrickStats(@PathVariable Long profileId) {
        logger.info("GET /api/v1/bricks/stats/{}", profileId);
        BrickStatsResponse stats = brickService.getBrickStats(profileId);
        return ResponseEntity.ok(stats);
    }

    /**
     * Check if user has a brick for today
     * GET /api/v1/bricks/today/{profileId}
     */
    @GetMapping("/today/{profileId}")
    public ResponseEntity<Map<String, Object>> hasBrickToday(@PathVariable Long profileId) {
        logger.info("GET /api/v1/bricks/today/{}", profileId);
        boolean hasBrick = brickService.hasBrickForToday(profileId);
        return ResponseEntity.ok(Map.of(
                "hasBrickToday", hasBrick,
                "date", LocalDate.now().toString()
        ));
    }

    /**
     * Get brick history for user (all bricks, newest first)
     * GET /api/v1/bricks/history/{profileId}
     */
    @GetMapping("/history/{profileId}")
    public ResponseEntity<List<BrickResponse>> getBrickHistory(@PathVariable Long profileId) {
        logger.info("GET /api/v1/bricks/history/{}", profileId);
        List<BrickResponse> bricks = brickService.getBrickHistory(profileId);
        return ResponseEntity.ok(bricks);
    }
}