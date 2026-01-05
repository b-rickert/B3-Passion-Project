package com.b3.controller;

import com.b3.dto.DailyLogCreateDTO;
import com.b3.dto.DailyLogDTO;
import com.b3.service.DailyLogService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Daily Log operations
 * 
 * Handles daily check-ins for energy, stress, sleep, and mood.
 * Powers BRIX's adaptive recommendations.
 */
@RestController
@RequestMapping("/api/v1/daily-logs")
public class DailyLogController {

    private static final Logger logger = LoggerFactory.getLogger(DailyLogController.class);

    private final DailyLogService dailyLogService;

    public DailyLogController(DailyLogService dailyLogService) {
        this.dailyLogService = dailyLogService;
    }

    // ========================================================================
    // POST ENDPOINTS
    // ========================================================================

    /**
     * Submit daily check-in
     * POST /api/v1/daily-logs
     */
    @PostMapping
    public ResponseEntity<DailyLogDTO> submitDailyLog(@Valid @RequestBody DailyLogCreateDTO createDTO) {
        logger.info("POST /api/v1/daily-logs for user {}", createDTO.getProfileId());
        DailyLogDTO log = dailyLogService.submitDailyLog(createDTO);
        return ResponseEntity.ok(log);
    }

    // ========================================================================
    // GET ENDPOINTS
    // ========================================================================

    /**
     * Get today's log for user
     * GET /api/v1/daily-logs/today/{profileId}
     */
    @GetMapping("/today/{profileId}")
    public ResponseEntity<DailyLogDTO> getTodaysLog(@PathVariable Long profileId) {
        logger.info("GET /api/v1/daily-logs/today/{}", profileId);
        DailyLogDTO log = dailyLogService.getTodaysLog(profileId);
        return ResponseEntity.ok(log);
    }

    /**
     * Get recent logs (last 7 days)
     * GET /api/v1/daily-logs/recent/{profileId}
     */
    @GetMapping("/recent/{profileId}")
    public ResponseEntity<List<DailyLogDTO>> getRecentLogs(@PathVariable Long profileId) {
        logger.info("GET /api/v1/daily-logs/recent/{}", profileId);
        List<DailyLogDTO> logs = dailyLogService.getRecentLogs(profileId);
        return ResponseEntity.ok(logs);
    }

    /**
     * Get logs by date range
     * GET /api/v1/daily-logs/{profileId}?start=2025-01-01&end=2025-01-31
     */
    @GetMapping("/{profileId}")
    public ResponseEntity<List<DailyLogDTO>> getLogsByDateRange(
            @PathVariable Long profileId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        logger.info("GET /api/v1/daily-logs/{}?start={}&end={}", profileId, start, end);
        List<DailyLogDTO> logs = dailyLogService.getLogsByDateRange(profileId, start, end);
        return ResponseEntity.ok(logs);
    }

    /**
     * Check if user has logged today
     * GET /api/v1/daily-logs/check/{profileId}
     */
    @GetMapping("/check/{profileId}")
    public ResponseEntity<Map<String, Object>> hasLoggedToday(@PathVariable Long profileId) {
        logger.info("GET /api/v1/daily-logs/check/{}", profileId);
        boolean hasLogged = dailyLogService.hasLoggedToday(profileId);
        return ResponseEntity.ok(Map.of(
                "hasLoggedToday", hasLogged,
                "date", LocalDate.now().toString()
        ));
    }

    /**
     * Get recovery needed days (low energy + high stress)
     * GET /api/v1/daily-logs/recovery/{profileId}
     */
    @GetMapping("/recovery/{profileId}")
    public ResponseEntity<List<DailyLogDTO>> getRecoveryNeededDays(@PathVariable Long profileId) {
        logger.info("GET /api/v1/daily-logs/recovery/{}", profileId);
        List<DailyLogDTO> logs = dailyLogService.getRecoveryNeededDays(profileId);
        return ResponseEntity.ok(logs);
    }

    // ========================================================================
    // PUT ENDPOINTS
    // ========================================================================

    /**
     * Update today's log
     * PUT /api/v1/daily-logs/{logId}
     */
    @PutMapping("/{logId}")
    public ResponseEntity<DailyLogDTO> updateDailyLog(
            @PathVariable Long logId,
            @Valid @RequestBody DailyLogCreateDTO updateDTO) {
        logger.info("PUT /api/v1/daily-logs/{}", logId);
        DailyLogDTO log = dailyLogService.updateDailyLog(logId, updateDTO);
        return ResponseEntity.ok(log);
    }
}