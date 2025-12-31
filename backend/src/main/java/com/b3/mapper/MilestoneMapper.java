package com.b3.mapper;

import com.b3.dto.MilestoneDTO;
import com.b3.model.Milestone;
import java.time.format.DateTimeFormatter;

/**
 * Mapper utility for Milestone entity <-> DTO conversions
 */
public class MilestoneMapper {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    /**
     * Convert Milestone entity to MilestoneDTO
     */
    public static MilestoneDTO toDTO(Milestone entity) {
        if (entity == null) {
            return null;
        }

        String achievedAt = entity.getAchievedAt() != null 
            ? entity.getAchievedAt().format(TIMESTAMP_FORMATTER) 
            : null;

        String createdAt = entity.getCreatedAt() != null 
            ? entity.getCreatedAt().format(TIMESTAMP_FORMATTER) 
            : null;

        return new MilestoneDTO(
            entity.getMilestoneId(),
            entity.getMilestoneName(),
            entity.getDescription(),
            entity.getMilestoneType() != null ? entity.getMilestoneType().name() : null,
            entity.getTargetValue(),
            entity.getCurrentValue(),
            entity.getIsAchieved(),
            achievedAt,
            entity.getProgressPercentage(),
            entity.getProgressPercentageString(),
            entity.getRemainingValue(),
            entity.isAlmostComplete(),
            entity.isInProgress(),
            createdAt
        );
    }
}
