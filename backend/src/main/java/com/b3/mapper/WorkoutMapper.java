package com.b3.mapper;

import com.b3.dto.WorkoutDTO;
import com.b3.dto.WorkoutListDTO;
import com.b3.model.Workout;
import com.b3.model.WorkoutExercise;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper utility for Workout entity <-> DTO conversions
 */
public class WorkoutMapper {

    /**
     * Convert Workout entity to WorkoutDTO (full details)
     */
    public static WorkoutDTO toDTO(Workout entity) {
        if (entity == null) {
            return null;
        }

        List<WorkoutDTO.WorkoutExerciseDTO> exerciseDTOs = new ArrayList<>();
        if (entity.getExercises() != null) {
            exerciseDTOs = entity.getExercises().stream()
                .map(WorkoutMapper::toWorkoutExerciseDTO)
                .collect(Collectors.toList());
        }

        return new WorkoutDTO(
            entity.getWorkoutId(),
            entity.getName(),
            entity.getDescription(),
            entity.getEstimatedDuration(),
            entity.getDifficultyLevel() != null ? entity.getDifficultyLevel().name() : null,
            null,  // targetMuscleGroups - optional, can add later
            entity.getRequiredEquipment(),
            exerciseDTOs
        );
    }

    /**
     * Convert Workout entity to WorkoutListDTO (minimal info)
     */
    public static WorkoutListDTO toListDTO(Workout entity) {
        if (entity == null) {
            return null;
        }

        int exerciseCount = entity.getExercises() != null 
            ? entity.getExercises().size() 
            : 0;

        return new WorkoutListDTO(
            entity.getWorkoutId(),
            entity.getName(),
            entity.getEstimatedDuration(),
            entity.getDifficultyLevel() != null ? entity.getDifficultyLevel().name() : null,
            exerciseCount,
            null  // targetMuscleGroups - optional, can add later
        );
    }

    /**
     * Convert WorkoutExercise to nested DTO
     */
    private static WorkoutDTO.WorkoutExerciseDTO toWorkoutExerciseDTO(WorkoutExercise we) {
        if (we == null || we.getExercise() == null) {
            return null;
        }

        return new WorkoutDTO.WorkoutExerciseDTO(
            we.getExercise().getExerciseId(),
            we.getExercise().getName(),
            we.getSets(),
            we.getReps(),
            we.getDurationSeconds(),
            we.getRestSeconds(),  
            we.getNotes()          
        );
    }
}