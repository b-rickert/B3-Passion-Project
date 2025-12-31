package com.b3.repository;

import com.b3.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository interface for Exercise entity
 * Handles exercise library and filtering queries
 */
@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    
    /**
     * Find exercises by muscle group
     */
    List<Exercise> findByMuscleGroup(Exercise.MuscleGroup muscleGroup);
    
    /**
     * Find exercises by equipment type
     */
    List<Exercise> findByEquipmentType(Exercise.EquipmentType equipmentType);
    
    /**
     * Find exercises by muscle group and equipment
     */
    List<Exercise> findByMuscleGroupAndEquipmentType(
        Exercise.MuscleGroup muscleGroup,
        Exercise.EquipmentType equipmentType
    );
    
    /**
     * Find exercises by name containing search term
     */
    List<Exercise> findByNameContainingIgnoreCase(String searchTerm);
    
    /**
     * Find exercises with video demonstrations
     */
    @Query("SELECT e FROM Exercise e WHERE e.videoUrl IS NOT NULL AND e.videoUrl <> ''")
    List<Exercise> findExercisesWithVideo();
    
    /**
     * Find upper body exercises
     */
    @Query("SELECT e FROM Exercise e WHERE e.muscleGroup IN " +
           "(com.b3.model.Exercise$MuscleGroup.CHEST, " +
           "com.b3.model.Exercise$MuscleGroup.BACK, " +
           "com.b3.model.Exercise$MuscleGroup.SHOULDERS, " +
           "com.b3.model.Exercise$MuscleGroup.BICEPS, " +
           "com.b3.model.Exercise$MuscleGroup.TRICEPS)")
    List<Exercise> findUpperBodyExercises();
    
    /**
     * Find lower body exercises
     */
    @Query("SELECT e FROM Exercise e WHERE e.muscleGroup IN " +
           "(com.b3.model.Exercise$MuscleGroup.LEGS, " +
           "com.b3.model.Exercise$MuscleGroup.QUADS, " +
           "com.b3.model.Exercise$MuscleGroup.HAMSTRINGS, " +
           "com.b3.model.Exercise$MuscleGroup.GLUTES)")
    List<Exercise> findLowerBodyExercises();
    
    /**
     * Search exercises by name or description
     */
    @Query("SELECT e FROM Exercise e WHERE " +
           "LOWER(e.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Exercise> searchExercises(@Param("searchTerm") String searchTerm);
    
    /**
     * Find all exercises ordered by name
     */
    List<Exercise> findAllByOrderByNameAsc();
}