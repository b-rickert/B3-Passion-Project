package com.b3.mapper;

import com.b3.dto.WorkoutSessionDTO;
import com.b3.model.WorkoutSession;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Mapper utility for WorkoutSession entity <-> DTO conversions
 */
public class WorkoutSessionMapper {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    /**
     * Convert WorkoutSession entity to WorkoutSessionDTO
     */
    public static WorkoutSessionDTO toDTO(WorkoutSession entity) {
        if (entity == null) {
            return null;
        }

        String userName = entity.getUserProfile() != null 
            ? entity.getUserProfile().getDisplayName() 
            : null;

        String workoutName = entity.getWorkout() != null 
            ? entity.getWorkout().getName()  
            : null;

        String startTime = entity.getStartTime() != null 
            ? entity.getStartTime().format(TIMESTAMP_FORMATTER) 
            : null;

        String endTime = entity.getEndTime() != null 
            ? entity.getEndTime().format(TIMESTAMP_FORMATTER) 
            : null;

        // Convert CompletionStatus to String for DTO
        String status = entity.getCompletionStatus() != null 
            ? entity.getCompletionStatus().name() 
            : null;

        return new WorkoutSessionDTO(
            entity.getSessionId(),
            entity.getUserProfile() != null ? entity.getUserProfile().getProfileId() : null,
            userName,
            entity.getWorkout() != null ? entity.getWorkout().getWorkoutId() : null,
            workoutName,
            status,  
            startTime,
            endTime,
            (int) entity.getDurationMinutes(),  
            null,  // notes - WorkoutSession doesn't have this field
            null,  // caloriesBurned - WorkoutSession doesn't have this field
            new ArrayList<>() // TODO: Map exercises when implementing set logging
        );
    }
}