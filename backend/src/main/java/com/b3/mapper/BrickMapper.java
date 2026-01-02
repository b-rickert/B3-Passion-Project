package com.b3.mapper;

import com.b3.dto.BrickDTO;
import com.b3.model.Brick;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Brick entity and BrickDTO
 */
@Component
public class BrickMapper {

    // ========================================================================
    // ENTITY TO DTO
    // ========================================================================

    /**
     * Convert Brick entity to BrickDTO
     */
    public BrickDTO toDTO(Brick entity) {
        if (entity == null) {
            return null;
        }

        BrickDTO dto = new BrickDTO();
        dto.setBrickId(entity.getBrickId());
        dto.setProfileId(entity.getUserProfile().getProfileId());
        
        if (entity.getWorkoutSession() != null) {
            dto.setSessionId(entity.getWorkoutSession().getSessionId());
        }
        
        dto.setBrickDate(entity.getBrickDate());
        dto.setBrickType(entity.getBrickType().name());
        dto.setBrickStatus(entity.getBrickStatus().name());
        dto.setBrickColor(entity.getBrickColor());

        return dto;
    }

    // ========================================================================
    // DTO TO ENTITY
    // ========================================================================

    /**
     * Convert BrickDTO to Brick entity (for updates)
     * Note: For creation, use the Brick constructor
     */
    public Brick toEntity(BrickDTO dto) {
        if (dto == null) {
            return null;
        }

        Brick entity = new Brick();
        entity.setBrickId(dto.getBrickId());
        entity.setBrickDate(dto.getBrickDate());
        
        if (dto.getBrickType() != null) {
            entity.setBrickType(Brick.BrickType.valueOf(dto.getBrickType()));
        }
        
        if (dto.getBrickStatus() != null) {
            entity.setBrickStatus(Brick.BrickStatus.valueOf(dto.getBrickStatus()));
        }
        
        entity.setBrickColor(dto.getBrickColor());

        return entity;
    }

    // ========================================================================
    // UPDATE ENTITY FROM DTO
    // ========================================================================

    /**
     * Update existing Brick entity from DTO
     */
    public void updateEntityFromDTO(Brick entity, BrickDTO dto) {
        if (entity == null || dto == null) {
            return;
        }

        if (dto.getBrickDate() != null) {
            entity.setBrickDate(dto.getBrickDate());
        }

        if (dto.getBrickType() != null) {
            entity.setBrickType(Brick.BrickType.valueOf(dto.getBrickType()));
        }

        if (dto.getBrickStatus() != null) {
            entity.setBrickStatus(Brick.BrickStatus.valueOf(dto.getBrickStatus()));
        }

        if (dto.getBrickColor() != null) {
            entity.setBrickColor(dto.getBrickColor());
        }
    }
}