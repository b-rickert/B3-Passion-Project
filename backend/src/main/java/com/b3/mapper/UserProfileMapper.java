package com.b3.mapper;

import com.b3.dto.UserProfileDTO;
import com.b3.dto.UserProfileUpdateDTO;
import com.b3.model.UserProfile;
import java.time.format.DateTimeFormatter;

/**
 * Mapper utility for UserProfile entity <-> DTO conversions
 */
public class UserProfileMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM yyyy");

    /**
     * Convert UserProfile entity to UserProfileDTO
     */
    public static UserProfileDTO toDTO(UserProfile entity) {
        if (entity == null) {
            return null;
        }

        String memberSince = entity.getCreatedAt() != null 
            ? entity.getCreatedAt().format(DATE_FORMATTER) 
            : null;

        return new UserProfileDTO(
            entity.getProfileId(),
            entity.getDisplayName(),
            entity.getAge(),
            entity.getFitnessLevel() != null ? entity.getFitnessLevel().name() : null,
            entity.getPrimaryGoal() != null ? entity.getPrimaryGoal().name() : null,
            entity.getEquipment(),
            entity.getWeeklyGoalDays(),
            entity.getCurrentStreak(),
            entity.getLongestStreak(),
            entity.getTotalWorkouts(),
            memberSince
        );
    }

    /**
     * Update UserProfile entity from UserProfileUpdateDTO
     * Does NOT create new entity, only updates existing one
     */
    public static void updateEntityFromDTO(UserProfile entity, UserProfileUpdateDTO dto) {
        if (entity == null || dto == null) {
            return;
        }

        if (dto.getDisplayName() != null) {
            entity.setDisplayName(dto.getDisplayName());
        }
        if (dto.getAge() != null) {
            entity.setAge(dto.getAge());
        }
        if (dto.getFitnessLevel() != null) {
            entity.setFitnessLevel(UserProfile.FitnessLevel.valueOf(dto.getFitnessLevel()));
        }
        if (dto.getPrimaryGoal() != null) {
            entity.setPrimaryGoal(UserProfile.PrimaryGoal.valueOf(dto.getPrimaryGoal()));
        }
        if (dto.getAvailableEquipment() != null) {
            entity.setEquipment(dto.getAvailableEquipment());
        }
        if (dto.getWorkoutsPerWeek() != null) {
            entity.setWeeklyGoalDays(dto.getWorkoutsPerWeek());
        }
    }
}