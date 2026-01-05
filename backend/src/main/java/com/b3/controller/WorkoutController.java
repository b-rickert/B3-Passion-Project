package com.b3.controller;

import com.b3.dto.response.WorkoutResponse;
import com.b3.model.Workout;
import com.b3.service.WorkoutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Workout operations
 * 
 * Handles workout library retrieval and filtering.
 */
@RestController
@RequestMapping("/api/v1/workouts")
public class WorkoutController {

    private static final Logger logger = LoggerFactory.getLogger(WorkoutController.class);

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    // ========================================================================
    // GET ENDPOINTS
    // ========================================================================

    /**
     * Get all workouts
     * GET /api/v1/workouts
     */
    @GetMapping
    public ResponseEntity<List<WorkoutResponse>> getAllWorkouts() {
        logger.info("GET /api/v1/workouts");
        List<WorkoutResponse> workouts = workoutService.getAllWorkouts();
        return ResponseEntity.ok(workouts);
    }

    /**
     * Get workout by ID
     * GET /api/v1/workouts/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<WorkoutResponse> getWorkoutById(@PathVariable Long id) {
        logger.info("GET /api/v1/workouts/{}", id);
        WorkoutResponse workout = workoutService.getWorkoutById(id);
        return ResponseEntity.ok(workout);
    }

    /**
     * Get workouts by type
     * GET /api/v1/workouts/type/{type}
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<WorkoutResponse>> getWorkoutsByType(@PathVariable Workout.WorkoutType type) {
        logger.info("GET /api/v1/workouts/type/{}", type);
        List<WorkoutResponse> workouts = workoutService.getWorkoutsByType(type);
        return ResponseEntity.ok(workouts);
    }

    /**
     * Get workouts by difficulty
     * GET /api/v1/workouts/difficulty/{difficulty}
     */
    @GetMapping("/difficulty/{difficulty}")
    public ResponseEntity<List<WorkoutResponse>> getWorkoutsByDifficulty(
            @PathVariable Workout.DifficultyLevel difficulty) {
        logger.info("GET /api/v1/workouts/difficulty/{}", difficulty);
        List<WorkoutResponse> workouts = workoutService.getWorkoutsByDifficulty(difficulty);
        return ResponseEntity.ok(workouts);
    }

    /**
     * Search workouts by name
     * GET /api/v1/workouts/search?q={query}
     */
    @GetMapping("/search")
    public ResponseEntity<List<WorkoutResponse>> searchWorkouts(@RequestParam String q) {
        logger.info("GET /api/v1/workouts/search?q={}", q);
        List<WorkoutResponse> workouts = workoutService.searchWorkouts(q);
        return ResponseEntity.ok(workouts);
    }

    /**
     * Get recommended workouts for fitness level
     * GET /api/v1/workouts/recommended/{fitnessLevel}
     */
    @GetMapping("/recommended/{fitnessLevel}")
    public ResponseEntity<List<WorkoutResponse>> getRecommendedWorkouts(
            @PathVariable Workout.DifficultyLevel fitnessLevel) {
        logger.info("GET /api/v1/workouts/recommended/{}", fitnessLevel);
        List<WorkoutResponse> workouts = workoutService.getRecommendedWorkouts(fitnessLevel);
        return ResponseEntity.ok(workouts);
    }
}