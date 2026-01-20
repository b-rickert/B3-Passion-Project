package com.b3.service;

import com.b3.dto.response.WorkoutResponse;
import com.b3.exception.ResourceNotFoundException;
import com.b3.model.Workout;
import com.b3.model.WorkoutExercise;
import com.b3.model.Exercise;
import com.b3.repository.WorkoutRepository;
import com.b3.repository.WorkoutExerciseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
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
    private final WorkoutExerciseRepository workoutExerciseRepository;
    
    public WorkoutService(WorkoutRepository workoutRepository, WorkoutExerciseRepository workoutExerciseRepository) {
        this.workoutRepository = workoutRepository;
        this.workoutExerciseRepository = workoutExerciseRepository;
    }
    
    /**
     * Get all workouts (without exercises for list view)
     */
    public List<WorkoutResponse> getAllWorkouts() {
        log.info("Fetching all workouts");
        List<Workout> workouts = workoutRepository.findAll();
        log.info("Found {} workouts", workouts.size());
        return workouts.stream()
            .map(this::mapToResponseBasic)
            .collect(Collectors.toList());
    }
    
    /**
     * Get workout by ID (with exercises for detail view)
     */
    @Transactional(readOnly = true)
    public WorkoutResponse getWorkoutById(Long workoutId) {
        log.info("Fetching workout with ID: {}", workoutId);
        Workout workout = workoutRepository.findById(workoutId)
            .orElseThrow(() -> new ResourceNotFoundException("Workout", workoutId));
        
        // Fetch exercises separately to avoid lazy loading issues
        List<WorkoutExercise> exercises = workoutExerciseRepository.findByWorkout_WorkoutIdOrderByOrderIndexAsc(workoutId);
        log.info("Found {} exercises for workout {}", exercises.size(), workoutId);
        
        return mapToResponseWithExercises(workout, exercises);
    }
    
    /**
     * Filter workouts by type
     */
    public List<WorkoutResponse> getWorkoutsByType(Workout.WorkoutType type) {
        log.info("Fetching workouts by type: {}", type);
        List<Workout> workouts = workoutRepository.findByWorkoutType(type);
        log.info("Found {} {} workouts", workouts.size(), type);
        return workouts.stream()
            .map(this::mapToResponseBasic)
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
            .map(this::mapToResponseBasic)
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
            .map(this::mapToResponseBasic)
            .collect(Collectors.toList());
    }
    
    /**
     * Get recommended workouts based on fitness level
     */
    public List<WorkoutResponse> getRecommendedWorkouts(Workout.DifficultyLevel fitnessLevel) {
        log.info("Getting recommended workouts for fitness level: {}", fitnessLevel);
        List<Workout> workouts = workoutRepository.findByDifficultyLevel(fitnessLevel);
        log.info("Recommended {} workouts", workouts.size());
        return workouts.stream()
            .map(this::mapToResponseBasic)
            .collect(Collectors.toList());
    }
    
    /**
     * Map Workout entity to basic WorkoutResponse (no exercises)
     */
    private WorkoutResponse mapToResponseBasic(Workout workout) {
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
    
    /**
     * Map Workout entity to full WorkoutResponse (with exercises)
     */
    private WorkoutResponse mapToResponseWithExercises(Workout workout, List<WorkoutExercise> workoutExercises) {
        WorkoutResponse response = mapToResponseBasic(workout);
        
        // Map exercises (already sorted by repository query)
        List<WorkoutResponse.ExerciseDetail> exerciseDetails = workoutExercises.stream()
            .map(this::mapExerciseDetail)
            .collect(Collectors.toList());
        
        response.setExercises(exerciseDetails);
        log.info("Mapped {} exercises for workout {}", exerciseDetails.size(), workout.getName());
        
        return response;
    }
    
    /**
     * Map WorkoutExercise to ExerciseDetail
     */
    private WorkoutResponse.ExerciseDetail mapExerciseDetail(WorkoutExercise we) {
        WorkoutResponse.ExerciseDetail detail = new WorkoutResponse.ExerciseDetail();
        Exercise exercise = we.getExercise();
        
        detail.setExerciseId(exercise.getExerciseId());
        detail.setName(exercise.getName());
        detail.setDescription(exercise.getDescription());
        detail.setMuscleGroup(exercise.getMuscleGroup().name());
        detail.setEquipmentType(exercise.getEquipmentType().name());
        detail.setVideoUrl(exercise.getVideoUrl());
        detail.setSets(we.getSets());
        detail.setReps(we.getReps());
        detail.setDurationSeconds(we.getDurationSeconds());
        detail.setRestSeconds(we.getRestSeconds());
        detail.setOrderIndex(we.getOrderIndex());
        
        return detail;
    }
}
