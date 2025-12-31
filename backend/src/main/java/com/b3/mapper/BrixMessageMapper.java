package com.b3.mapper;

import com.b3.dto.BrixMessageDTO;
import com.b3.model.BrixMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Mapper utility for BrixMessage entity <-> DTO conversions
 */
public class BrixMessageMapper {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    /**
     * Convert BrixMessage entity to BrixMessageDTO
     */
    public static BrixMessageDTO toDTO(BrixMessage entity) {
        if (entity == null) {
            return null;
        }

        return new BrixMessageDTO(
            entity.getMessageId(),
            entity.getMessageText(),
            entity.getMessageType() != null ? entity.getMessageType().name() : null,
            entity.getTone() != null ? entity.getTone().name() : null,
            entity.getContextTrigger(),
            entity.getSentAt() != null ? entity.getSentAt().format(TIMESTAMP_FORMATTER) : null,
            formatTimeAgo(entity.getSentAt()),
            entity.isSentToday()
        );
    }

    /**
     * Format timestamp as "X minutes/hours/days ago"
     */
    private static String formatTimeAgo(LocalDateTime sentAt) {
        if (sentAt == null) {
            return null;
        }

        long minutesAgo = ChronoUnit.MINUTES.between(sentAt, LocalDateTime.now());
        
        if (minutesAgo < 1) {
            return "Just now";
        } else if (minutesAgo < 60) {
            return minutesAgo + (minutesAgo == 1 ? " minute ago" : " minutes ago");
        }
        
        long hoursAgo = ChronoUnit.HOURS.between(sentAt, LocalDateTime.now());
        if (hoursAgo < 24) {
            return hoursAgo + (hoursAgo == 1 ? " hour ago" : " hours ago");
        }
        
        long daysAgo = ChronoUnit.DAYS.between(sentAt, LocalDateTime.now());
        return daysAgo + (daysAgo == 1 ? " day ago" : " days ago");
    }
}