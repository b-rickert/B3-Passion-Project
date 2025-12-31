package com.b3.mapper;

import com.b3.dto.DailyLogDTO;
import com.b3.model.DailyLog;
import java.time.format.DateTimeFormatter;

/**
 * Mapper utility for DailyLog entity <-> DTO conversions
 */
public class DailyLogMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    /**
     * Convert DailyLog entity to DailyLogDTO
     */
    public static DailyLogDTO toDTO(DailyLog entity) {
        if (entity == null) {
            return null;
        }

        return new DailyLogDTO(
            entity.getLogId(),
            entity.getUserProfile() != null ? entity.getUserProfile().getProfileId() : null,
            entity.getLogDate() != null ? entity.getLogDate().format(DATE_FORMATTER) : null,
            entity.getEnergyLevel(),
            entity.getStressLevel(),
            entity.getSleepQuality(),
            entity.getMood() != null ? entity.getMood().name() : null,
            entity.getNotes(),
            entity.getWellnessScore(),
            entity.needsRecovery(),
            entity.isOptimalWorkoutDay(),
            entity.getCreatedAt() != null ? entity.getCreatedAt().format(TIMESTAMP_FORMATTER) : null
        );
    }
}