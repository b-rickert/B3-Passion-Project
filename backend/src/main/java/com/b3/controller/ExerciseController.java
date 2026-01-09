package com.b3.controller;

import com.b3.model.Exercise;
import com.b3.repository.ExerciseRepository;
import com.b3.service.ExerciseApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for Exercise endpoints
 * Handles fetching from ExerciseDB API and local database
 */
@RestController
@RequestMapping("/api/v1/exercises")
public class ExerciseController {

    private static final Logger logger = LoggerFactory.getLogger(ExerciseController.class);

    private final ExerciseRepository exerciseRepository;
    private final ExerciseApiService exerciseApiService;

    public ExerciseController(ExerciseRepository exerciseRepository, ExerciseApiService exerciseApiService) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseApiService = exerciseApiService;
    }

    /**
     * Get all exercises from local database
     * GET /api/v1/exercises
     */
    @GetMapping
    public ResponseEntity<List<Exercise>> getAllExercises() {
        logger.info("GET /api/v1/exercises");
        List<Exercise> exercises = exerciseRepository.findAllByOrderByNameAsc();
        return ResponseEntity.ok(exercises);
    }

    /**
     * Get exercise by ID
     * GET /api/v1/exercises/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Exercise> getExerciseById(@PathVariable Long id) {
        logger.info("GET /api/v1/exercises/{}", id);
        return exerciseRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get exercises by muscle group
     * GET /api/v1/exercises/muscle/{muscleGroup}
     */
    @GetMapping("/muscle/{muscleGroup}")
    public ResponseEntity<List<Exercise>> getByMuscleGroup(@PathVariable Exercise.MuscleGroup muscleGroup) {
        logger.info("GET /api/v1/exercises/muscle/{}", muscleGroup);
        List<Exercise> exercises = exerciseRepository.findByMuscleGroup(muscleGroup);
        return ResponseEntity.ok(exercises);
    }

    /**
     * Get exercises by equipment type
     * GET /api/v1/exercises/equipment/{equipmentType}
     */
    @GetMapping("/equipment/{equipmentType}")
    public ResponseEntity<List<Exercise>> getByEquipment(@PathVariable Exercise.EquipmentType equipmentType) {
        logger.info("GET /api/v1/exercises/equipment/{}", equipmentType);
        List<Exercise> exercises = exerciseRepository.findByEquipmentType(equipmentType);
        return ResponseEntity.ok(exercises);
    }

    /**
     * Search exercises by name
     * GET /api/v1/exercises/search?q={query}
     */
    @GetMapping("/search")
    public ResponseEntity<List<Exercise>> searchExercises(@RequestParam String q) {
        logger.info("GET /api/v1/exercises/search?q={}", q);
        List<Exercise> exercises = exerciseRepository.searchExercises(q);
        return ResponseEntity.ok(exercises);
    }

    /**
     * Fetch and cache exercises from ExerciseDB API
     * POST /api/v1/exercises/fetch/{bodyPart}?limit=10
     */
    @PostMapping("/fetch/{bodyPart}")
    public ResponseEntity<Map<String, Object>> fetchFromApi(
            @PathVariable String bodyPart,
            @RequestParam(defaultValue = "10") int limit) {
        logger.info("POST /api/v1/exercises/fetch/{} (limit={})", bodyPart, limit);
        
        long beforeCount = exerciseRepository.count();
        exerciseApiService.fetchAndCacheExercises(bodyPart, limit);
        long afterCount = exerciseRepository.count();
        
        Map<String, Object> response = new HashMap<>();
        response.put("bodyPart", bodyPart);
        response.put("exercisesBefore", beforeCount);
        response.put("exercisesAfter", afterCount);
        response.put("newExercises", afterCount - beforeCount);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Fetch exercises for all body parts (populate database)
     * POST /api/v1/exercises/fetch-all?limit=10
     */
    @PostMapping("/fetch-all")
    public ResponseEntity<Map<String, Object>> fetchAllFromApi(
            @RequestParam(defaultValue = "10") int limit) {
        logger.info("POST /api/v1/exercises/fetch-all (limit={})", limit);
        
        long beforeCount = exerciseRepository.count();
        
        // Fetch for all body parts
        String[] bodyParts = {"back", "cardio", "chest", "lower arms", "lower legs", 
                              "neck", "shoulders", "upper arms", "upper legs", "waist"};
        
        for (String bodyPart : bodyParts) {
            logger.info("Fetching exercises for: {}", bodyPart);
            exerciseApiService.fetchAndCacheExercises(bodyPart, limit);
        }
        
        long afterCount = exerciseRepository.count();
        
        Map<String, Object> response = new HashMap<>();
        response.put("bodyPartsFetched", bodyParts.length);
        response.put("limitPerBodyPart", limit);
        response.put("exercisesBefore", beforeCount);
        response.put("exercisesAfter", afterCount);
        response.put("newExercises", afterCount - beforeCount);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get exercise count
     * GET /api/v1/exercises/count
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getCount() {
        logger.info("GET /api/v1/exercises/count");
        Map<String, Long> response = new HashMap<>();
        response.put("totalExercises", exerciseRepository.count());
        return ResponseEntity.ok(response);
    }
}