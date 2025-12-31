package com.b3.repository;

import com.b3.model.Workout;
import com.b3.model.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository interface for WorkoutExercise entity
 * Manages junction table between workouts and exercises
 */
@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, Long> {

    /**
     * Find all exercises in a workout, ordered by order index
     */
    List<WorkoutExercise> findByWorkoutOrderByOrderIndexAsc(Workout workout);
    
    /**
     * Find all exercises in a workout by workout ID
     */
    List<WorkoutExercise> findByWorkout_WorkoutIdOrderByOrderIndexAsc(Long workoutId);
    
    /**
     * Count exercises in a workout
     */
    long countByWorkout(Workout workout);
    
    /**
     * Count exercises in a workout by workout ID
     */
    long countByWorkout_WorkoutId(Long workoutId);
    
    /**
     * Delete all exercises for a workout
     */
    void deleteByWorkout(Workout workout);
}