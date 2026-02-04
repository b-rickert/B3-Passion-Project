package com.b3.service;

import com.b3.dto.response.BrickResponse;
import com.b3.dto.response.BrickStatsResponse;
import com.b3.exception.ResourceNotFoundException;
import com.b3.model.Brick;
import com.b3.model.UserProfile;
import com.b3.model.WorkoutSession;
import com.b3.repository.BrickRepository;
import com.b3.repository.UserProfileRepository;
import com.b3.repository.WorkoutSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for Brick business logic
 * Handles the brick wall visualization and tracking
 */
@Service
@Transactional
public class BrickService {
    
    // ========================================================================
    // FIELDS
    // ========================================================================
    
    private static final Logger log = LoggerFactory.getLogger(BrickService.class);
    
    private final BrickRepository brickRepository;
    private final UserProfileRepository userProfileRepository;
    private final WorkoutSessionRepository workoutSessionRepository;
    
    // ========================================================================
    // CONSTRUCTOR
    // ========================================================================
    
    public BrickService(
            BrickRepository brickRepository,
            UserProfileRepository userProfileRepository,
            WorkoutSessionRepository workoutSessionRepository) {
        this.brickRepository = brickRepository;
        this.userProfileRepository = userProfileRepository;
        this.workoutSessionRepository = workoutSessionRepository;
    }
    
    // ========================================================================
    // PUBLIC METHODS
    // ========================================================================
    
    /**
     * Create a brick after workout completion.
     *
     * KEY DESIGN: One-brick-per-day rule enforcement.
     * This is a core philosophy decision: we encourage CONSISTENCY over VOLUME.
     * Even if a user does 3 workouts in one day, they only get 1 brick.
     * This prevents "binge and burn" patterns and rewards showing up daily.
     *
     * The check happens at the service layer, not the database constraint,
     * so we can provide a meaningful error message.
     */
    public BrickResponse createBrick(Long sessionId) {
        log.info("Creating brick for session: {}", sessionId);

        // Get workout session
        WorkoutSession session = workoutSessionRepository.findById(sessionId)
            .orElseThrow(() -> new ResourceNotFoundException("WorkoutSession", sessionId));

        // Check if session is completed
        if (!session.isCompleted()) {
            throw new IllegalStateException("Cannot create brick for incomplete session");
        }

        // Get the date from session
        LocalDate brickDate = session.getEndTime().toLocalDate();

        // Check if brick already exists for this date (one brick per day rule)
        boolean exists = brickRepository.existsByUserProfile_ProfileIdAndBrickDate(
            session.getUserProfile().getProfileId(), brickDate);

        if (exists) {
            log.warn("Brick already exists for user {} on {}",
                session.getUserProfile().getProfileId(), brickDate);
            throw new IllegalStateException("Brick already exists for this date");
        }
        
        // Create brick
        Brick brick = new Brick(
            session.getUserProfile(), 
            session, 
            brickDate, 
            Brick.BrickType.WORKOUT
        );
        Brick saved = brickRepository.save(brick);
        
        log.info("Created brick with ID: {}", saved.getBrickId());
        
        return mapToResponse(saved);
    }
    
    /**
     * Get brick calendar for a specific month
     * Returns all bricks for the given month (for brick wall visualization)
     */
    public List<BrickResponse> getBrickCalendar(Long profileId, int year, int month) {
        log.info("Fetching brick calendar for user {} - {}/{}", profileId, year, month);
        
        // Calculate start and end dates for the month
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        
        List<Brick> bricks = brickRepository.findByUserProfile_ProfileIdAndBrickDateBetween(
            profileId, startDate, endDate);
        
        log.info("Found {} bricks for {}/{}", bricks.size(), year, month);
        
        return bricks.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Get brick statistics for user
     */
    public BrickStatsResponse getBrickStats(Long profileId) {
        log.info("Fetching brick stats for user: {}", profileId);
        
        // Verify user exists
        UserProfile user = userProfileRepository.findById(profileId)
            .orElseThrow(() -> new ResourceNotFoundException("UserProfile", profileId));
        
        BrickStatsResponse stats = new BrickStatsResponse();
        
        // Total bricks
        Long totalBricks = brickRepository.countByUserProfile_ProfileId(profileId);
        stats.setTotalBricks(totalBricks);
        
        // Streaks from UserProfile
        stats.setCurrentStreak(user.getCurrentStreak());
        stats.setLongestStreak(user.getLongestStreak());
        
        // Bricks this month
        LocalDate now = LocalDate.now();
        YearMonth thisMonth = YearMonth.now();
        LocalDate monthStart = thisMonth.atDay(1);
        LocalDate monthEnd = thisMonth.atEndOfMonth();
        
        List<Brick> bricksThisMonth = brickRepository.findByUserProfile_ProfileIdAndBrickDateBetween(
            profileId, monthStart, monthEnd);
        stats.setBricksThisMonth(bricksThisMonth.size());
        
        // Bricks this week
        LocalDate weekStart = now.minusDays(now.getDayOfWeek().getValue() - 1);
        LocalDate weekEnd = weekStart.plusDays(6);
        
        List<Brick> bricksThisWeek = brickRepository.findByUserProfile_ProfileIdAndBrickDateBetween(
            profileId, weekStart, weekEnd);
        stats.setBricksThisWeek(bricksThisWeek.size());
        
        log.info("Brick stats for user {}: {} total, {} this month, {} this week", 
            profileId, totalBricks, bricksThisMonth.size(), bricksThisWeek.size());
        
        return stats;
    }
    
    /**
     * Check if user has brick for today
     */
    public boolean hasBrickForToday(Long profileId) {
        LocalDate today = LocalDate.now();
        return brickRepository.existsByUserProfile_ProfileIdAndBrickDate(profileId, today);
    }
    
    /**
     * Get brick history for user (all bricks, newest first)
     */
    public List<BrickResponse> getBrickHistory(Long profileId) {
        log.info("Fetching brick history for user: {}", profileId);
        
        List<Brick> bricks = brickRepository.findByUserProfile_ProfileIdOrderByBrickDateDesc(profileId);
        
        log.info("Found {} total bricks for user {}", bricks.size(), profileId);
        
        return bricks.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    // ========================================================================
    // PRIVATE HELPER METHODS
    // ========================================================================
    
    /**
     * Map Brick entity to BrickResponse DTO
     */
    private BrickResponse mapToResponse(Brick brick) {
        BrickResponse response = new BrickResponse();
        response.setBrickId(brick.getBrickId());
        response.setProfileId(brick.getUserProfile().getProfileId());
        response.setSessionId(brick.getWorkoutSession().getSessionId());
        response.setBrickDate(brick.getBrickDate());
        response.setBrickColor(brick.getBrickColor());
        response.setIsFirstOfMonth(brick.isFirstOfMonth());
        return response;
    }
}