package com.b3.controller;

import com.b3.dto.request.WorkoutSessionCreateRequest;
import com.b3.dto.request.WorkoutSessionCompleteRequest;
import com.b3.dto.response.WorkoutSessionResponse;
import com.b3.service.WorkoutSessionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Workout Session operations
 * 
 * Handles starting, completing, and tracking workout sessions.
 * This is the core functionality for the brick wall visualization.
 */
@RestController
@RequestMapping("/api/v1/sessions")
public class WorkoutSessionController {

    private static final Logger logger = LoggerFactory.getLogger(WorkoutSessionController.class);

    private final WorkoutSessionService workoutSessionService;

    public WorkoutSessionController(WorkoutSessionService workoutSessionService) {
        this.workoutSessionService = workoutSessionService;
    }

    // ========================================================================
    // POST ENDPOINTS
    // ========================================================================

    /**
     * Start a new workout session
     * POST /api/v1/sessions
     */
    @PostMapping
    public ResponseEntity<WorkoutSessionResponse> startSession(
            @Valid @RequestBody WorkoutSessionCreateRequest createRequest) {
        logger.info("POST /api/v1/sessions - Starting session for user {} with workout {}",
                createRequest.getProfileId(), createRequest.getWorkoutId());
        WorkoutSessionResponse session = workoutSessionService.createSession(createRequest);
        return ResponseEntity.ok(session);
    }

    /**
     * Complete a workout session
     * POST /api/v1/sessions/{sessionId}/complete
     */
    @PostMapping("/{sessionId}/complete")
    public ResponseEntity<WorkoutSessionResponse> completeSession(
            @PathVariable Long sessionId,
            @Valid @RequestBody WorkoutSessionCompleteRequest completeRequest) {
        logger.info("POST /api/v1/sessions/{}/complete", sessionId);
        WorkoutSessionResponse session = workoutSessionService.completeSession(sessionId, completeRequest);
        return ResponseEntity.ok(session);
    }

    // ========================================================================
    // GET ENDPOINTS
    // ========================================================================

    /**
     * Get active session for user
     * GET /api/v1/sessions/active/{profileId}
     */
    @GetMapping("/active/{profileId}")
    public ResponseEntity<WorkoutSessionResponse> getActiveSession(@PathVariable Long profileId) {
        logger.info("GET /api/v1/sessions/active/{}", profileId);
        Optional<WorkoutSessionResponse> session = workoutSessionService.getActiveSession(profileId);
        return session.map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    /**
     * Get workout history for user
     * GET /api/v1/sessions/history/{profileId}
     */
    @GetMapping("/history/{profileId}")
    public ResponseEntity<List<WorkoutSessionResponse>> getSessionHistory(@PathVariable Long profileId) {
        logger.info("GET /api/v1/sessions/history/{}", profileId);
        List<WorkoutSessionResponse> sessions = workoutSessionService.getSessionHistory(profileId);
        return ResponseEntity.ok(sessions);
    }
}