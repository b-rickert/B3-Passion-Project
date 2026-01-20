package com.b3.service;

import com.b3.dto.WorkoutExerciseDTO;
import com.b3.dto.response.WorkoutResponse;
import com.b3.exception.ResourceNotFoundException;
import com.b3.model.Workout;
import com.b3.repository.WorkoutRepository;
import com.b3.repository.WorkoutExerciseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkoutService {
    
    private static final Logger log = LoggerFactory.getLogger(WorkoutService.class);
    private final WorkoutRepository workoutRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;
    
    public WorkoutService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
        this.workoutExerciseRepository = workoutExerciseRepository;
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
    
    public List<WorkoutExerciseDTO> getWorkoutExercises(Long workoutId) {
        log.info("Fetching exercises for workout ID: {}", workoutId);
        if (!workoutRepository.existsById(workoutId)) {
            throw new ResourceNotFoundException("Workout", workoutId);
        }
        List<WorkoutExercise> exercises = workoutExerciseRepository
            .findByWorkout_WorkoutIdOrderByOrderIndexAsc(workoutId);
        log.info("Found {} exercises for workout {}", exercises.size(), workoutId);
        return exercises.stream().map(this::mapToExerciseDTO).collect(Collectors.toList());
    }
    
    public List<WorkoutResponse> getWorkoutsByType(Workout.WorkoutType type) {
        log.info("Fetching workouts by type: {}", type);
        List<Workout> workouts = workoutRepository.findByWorkoutType(type);
        log.info("Found {} {} workouts", workouts.size(), type);
        
        return workouts.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    public List<WorkoutResponse> getWorkoutsByDifficulty(Workout.DifficultyLevel difficulty) {
        log.info("Fetching workouts by difficulty: {}", difficulty);
        List<Workout> workouts = workoutRepository.findByDifficultyLevel(difficulty);
        log.info("Found {} {} workouts", workouts.size(), difficulty);
        
        return workouts.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    public List<WorkoutResponse> searchWorkouts(String keyword) {
        log.info("Searching workouts with keyword: {}", keyword);
        List<Workout> workouts = workoutRepository.findByNameContainingIgnoreCase(keyword);
        log.info("Found {} workouts matching '{}'", workouts.size(), keyword);
        
        return workouts.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    public List<WorkoutResponse> getRecommendedWorkouts(Workout.DifficultyLevel fitnessLevel) {
        log.info("Getting recommended workouts for fitness level: {}", fitnessLevel);
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