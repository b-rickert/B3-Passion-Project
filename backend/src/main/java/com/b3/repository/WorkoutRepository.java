package com.b3.repository;

import com.b3.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository interface for Workout entity
 * Manages workout templates and library queries
 */
@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    /**
     * Find workouts by difficulty level
     */
    List<Workout> findByDifficultyLevel(Workout.DifficultyLevel difficultyLevel);
    
    /**
     * Find workouts by workout type
     */
    List<Workout> findByWorkoutType(Workout.WorkoutType workoutType);
    
    /**
     * Find workouts by difficulty and type
     */
    List<Workout> findByDifficultyLevelAndWorkoutType(
        Workout.DifficultyLevel difficultyLevel,
        Workout.WorkoutType workoutType
    );
    
    /**
     * Find workouts with duration less than or equal to specified minutes
     */
    List<Workout> findByEstimatedDurationLessThanEqual(Integer minutes);
    
    /**
     * Find workouts by name containing search term (case insensitive)
     */
    List<Workout> findByNameContainingIgnoreCase(String searchTerm);
    
    /**
     * Find workouts ordered by estimated duration
     */
    List<Workout> findAllByOrderByEstimatedDurationAsc();
    
    /**
     * Custom query: Find workouts suitable for user's fitness level
     * (workout difficulty <= user level)
     */
    @Query("SELECT w FROM Workout w WHERE w.difficultyLevel <= :userLevel ORDER BY w.estimatedDuration")
    List<Workout> findSuitableWorkouts(@Param("userLevel") Workout.DifficultyLevel userLevel);
}