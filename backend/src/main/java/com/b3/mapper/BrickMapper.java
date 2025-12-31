package com.b3.mapper;

import com.b3.dto.BrickDTO;
import com.b3.dto.BrickWallDTO;
import com.b3.model.Brick;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper utility for Brick entity <-> DTO conversions
 */
public class BrickMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Convert Brick entity to BrickDTO
     */
    public static BrickDTO toDTO(Brick entity) {
        if (entity == null) {
            return null;
        }

        String workoutName = null;
        Integer workoutDuration = null;
        
        // Get workout details from session if available
        if (entity.getWorkoutSession() != null && entity.getWorkoutSession().getWorkout() != null) {
            workoutName = entity.getWorkoutSession().getWorkout().getName();
            workoutDuration = (int) entity.getWorkoutSession().getDurationMinutes();
        }

        return new BrickDTO(
            entity.getBrickId(),
            entity.getBrickDate() != null ? entity.getBrickDate().format(DATE_FORMATTER) : null,
            entity.getStatus() != null ? entity.getStatus().name() : null,
            getColorForStatus(entity.getStatus()),
            entity.getNotes(),
            workoutName,
            workoutDuration
        );
    }

    /**
     * Convert list of Bricks to BrickWallDTO
     */
    public static BrickWallDTO toBrickWallDTO(List<Brick> bricks, String month, String year, 
                                              Integer currentStreak, Integer totalBricksLaid) {
        if (bricks == null) {
            return null;
        }

        List<BrickDTO> brickDTOs = bricks.stream()
            .map(BrickMapper::toDTO)
            .collect(Collectors.toList());

        int totalDays = bricks.size();
        int consistencyPercentage = totalDays > 0 
            ? (int) ((totalBricksLaid * 100.0) / totalDays) 
            : 0;

        return new BrickWallDTO(
            month,
            year,
            currentStreak,
            totalBricksLaid,
            consistencyPercentage,
            brickDTOs
        );
    }

    /**
     * Get hex color code based on brick status
     */
    private static String getColorForStatus(Brick.BrickStatus status) {
        if (status == null) {
            return "#E5E7EB"; // Gray for null/empty
        }

        switch (status) {
            case LAID:
                return "#FF6B35"; // Orange - workout completed
            case MISSED:
                return "#94A3B8"; // Light gray - missed workout
            case RECOVERY:
                return "#3B82F6"; // Blue - recovery day
            default:
                return "#E5E7EB"; // Default gray
        }
    }
}