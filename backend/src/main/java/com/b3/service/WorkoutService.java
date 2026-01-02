package com.b3.service;

import com.b3.dto.response.WorkoutResponse;
import com.b3.exception.ResourceNotFoundException;
import com.b3.model.Workout;
import com.b3.repository.WorkoutRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for Workout business logic
 */
@Service
@Transactional
public class WorkoutService {
    
    private static final Logger log = LoggerFactory.getLogger(WorkoutService.class);
    private final WorkoutRepository workoutRepository;
    
    public WorkoutService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }
    
    /**
     * Get all workouts
     */
    public List<WorkoutResponse> getAllWorkouts() {
        log.info("Fetching all workouts");
        
        List<Workout> workouts = workoutRepository.findAll();
        
        log.info("Found {} workouts", workouts.size());
        
        return workouts.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Get workout by ID
     */
    public WorkoutResponse getWorkoutById(Long workoutId) {
        log.info("Fetching workout with ID: {}", workoutId);
        
        Workout workout = workoutRepository.findById(workoutId)
            .orElseThrow(() -> new ResourceNotFoundException("Workout", workoutId));
        
        return mapToResponse(workout);
    }
    
    /**
     * Filter workouts by type
     */
    public List<WorkoutResponse> getWorkoutsByType(Workout.WorkoutType type) {
        log.info("Fetching workouts by type: {}", type);
        
        List<Workout> workouts = workoutRepository.findByWorkoutType(type);
        
        log.info("Found {} {} workouts", workouts.size(), type);
        
        return workouts.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Filter workouts by difficulty
     */
    public List<WorkoutResponse> getWorkoutsByDifficulty(Workout.DifficultyLevel difficulty) {
        log.info("Fetching workouts by difficulty: {}", difficulty);
        
        List<Workout> workouts = workoutRepository.findByDifficultyLevel(difficulty);
        
        log.info("Found {} {} workouts", workouts.size(), difficulty);
        
        return workouts.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Search workouts by name
     */
    public List<WorkoutResponse> searchWorkouts(String keyword) {
        log.info("Searching workouts with keyword: {}", keyword);
        
        List<Workout> workouts = workoutRepository.findByNameContainingIgnoreCase(keyword);
        
        log.info("Found {} workouts matching '{}'", workouts.size(), keyword);
        
        return workouts.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Get recommended workouts based on fitness level
     */
    public List<WorkoutResponse> getRecommendedWorkouts(Workout.DifficultyLevel fitnessLevel) {
        log.info("Getting recommended workouts for fitness level: {}", fitnessLevel);
        
        // For now, recommend workouts matching user's fitness level
        // Can be enhanced with more sophisticated logic later
        List<Workout> workouts = workoutRepository.findByDifficultyLevel(fitnessLevel);
        
        log.info("Recommended {} workouts", workouts.size());
        
        return workouts.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Map Workout entity to WorkoutResponse DTO
     */
    private WorkoutResponse mapToResponse(Workout workout) {
        WorkoutResponse response = new WorkoutResponse();
        response.setWorkoutId(workout.getWorkoutId());
        response.setName(workout.getName());
        response.setDescription(workout.getDescription());
        response.setWorkoutType(workout.getWorkoutType().name());
        response.setDifficultyLevel(workout.getDifficultyLevel().name());
        response.setEstimatedDuration(workout.getEstimatedDuration());
        response.setRequiredEquipment(workout.getRequiredEquipment());
        return response;
    }
}