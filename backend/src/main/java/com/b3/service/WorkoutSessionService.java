package com.b3.service;

import com.b3.dto.request.WorkoutSessionCreateRequest;
import com.b3.dto.request.WorkoutSessionCompleteRequest;
import com.b3.dto.response.WorkoutSessionResponse;
import com.b3.exception.BadRequestException;
import com.b3.exception.ResourceNotFoundException;
import com.b3.model.UserProfile;
import com.b3.model.Workout;
import com.b3.model.WorkoutSession;
import com.b3.repository.UserProfileRepository;
import com.b3.repository.WorkoutRepository;
import com.b3.repository.WorkoutSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for WorkoutSession business logic
 * Handles workout tracking, completion, and streak calculations
 */
@Service
@Transactional
public class WorkoutSessionService {
    
    private static final Logger log = LoggerFactory.getLogger(WorkoutSessionService.class);
    
    private final WorkoutSessionRepository workoutSessionRepository;
    private final UserProfileRepository userProfileRepository;
    private final WorkoutRepository workoutRepository;
    
    public WorkoutSessionService(
            WorkoutSessionRepository workoutSessionRepository,
            UserProfileRepository userProfileRepository,
            WorkoutRepository workoutRepository) {
        this.workoutSessionRepository = workoutSessionRepository;
        this.userProfileRepository = userProfileRepository;
        this.workoutRepository = workoutRepository;
    }
    
    /**
     * Create new workout session (start workout)
     */
    public WorkoutSessionResponse createSession(WorkoutSessionCreateRequest request) {
        log.info("Creating workout session for user {} with workout {}", 
            request.getProfileId(), request.getWorkoutId());
        
        // Validate user exists
        UserProfile user = userProfileRepository.findById(request.getProfileId())
            .orElseThrow(() -> new ResourceNotFoundException("UserProfile", request.getProfileId()));
        
        // Validate workout exists
        Workout workout = workoutRepository.findById(request.getWorkoutId())
            .orElseThrow(() -> new ResourceNotFoundException("Workout", request.getWorkoutId()));
        
        // Create session
        WorkoutSession session = new WorkoutSession(user, workout, LocalDateTime.now());
        WorkoutSession saved = workoutSessionRepository.save(session);
        
        log.info("Created workout session with ID: {}", saved.getSessionId());
        
        return mapToResponse(saved);
    }
    
    /**
     * Complete workout session
     * Updates user stats and calculates streaks
     */
    public WorkoutSessionResponse completeSession(Long sessionId, WorkoutSessionCompleteRequest request) {
        log.info("Completing workout session: {}", sessionId);
        
        // Get session
        WorkoutSession session = workoutSessionRepository.findById(sessionId)
            .orElseThrow(() -> new ResourceNotFoundException("WorkoutSession", sessionId));
        
        // Check if already completed
        if (session.getCompletionStatus() == WorkoutSession.CompletionStatus.COMPLETED) {
            throw new BadRequestException("Workout session is already completed");
        }
        
        // Complete session
        session.completeSession(request.getActualDuration());
        
        if (request.getNotes() != null && !request.getNotes().isEmpty()) {
            session.setNotes(request.getNotes());
        }
        
        WorkoutSession updated = workoutSessionRepository.save(session);
        
        // Update user stats (streak, total workouts)
        updateUserStats(session.getUserProfile());
        
        log.info("Completed workout session: {}", sessionId);
        
        return mapToResponse(updated);
    }
    
    /**
     * Get active (in-progress) session for user
     */
    public Optional<WorkoutSessionResponse> getActiveSession(Long profileId) {
        log.info("Fetching active session for user: {}", profileId);
        
        Optional<WorkoutSession> activeSession = workoutSessionRepository
            .findActiveSessionByProfileId(profileId);
        
        return activeSession.map(this::mapToResponse);
    }
    
    /**
     * Get workout session history for user
     */
    public List<WorkoutSessionResponse> getSessionHistory(Long profileId) {
        log.info("Fetching session history for user: {}", profileId);
        
        List<WorkoutSession> sessions = workoutSessionRepository
            .findByUserProfile_ProfileIdOrderByStartTimeDesc(profileId);
        
        log.info("Found {} sessions for user {}", sessions.size(), profileId);
        
        return sessions.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Update user statistics after completing workout
     * Increments total workouts and updates streak
     */
    private void updateUserStats(UserProfile user) {
        log.info("Updating stats for user: {}", user.getProfileId());
        
        // Increment total workouts
        user.incrementTotalWorkouts();
        
        // Update streak (handled by UserProfile entity logic)
        user.updateStreak();
        
        userProfileRepository.save(user);
        
        log.info("Updated user stats - Total workouts: {}, Current streak: {}", 
            user.getTotalWorkouts(), user.getCurrentStreak());
    }
    
    /**
     * Map WorkoutSession entity to WorkoutSessionResponse DTO
     */
    private WorkoutSessionResponse mapToResponse(WorkoutSession session) {
        WorkoutSessionResponse response = new WorkoutSessionResponse();
        response.setSessionId(session.getSessionId());
        response.setProfileId(session.getUserProfile().getProfileId());
        response.setWorkoutId(session.getWorkout().getWorkoutId());
        response.setWorkoutName(session.getWorkout().getName());
        response.setStartTime(session.getStartTime());
        response.setEndTime(session.getEndTime());
        response.setActualDuration(session.getActualDuration());
        response.setCompletionStatus(session.getCompletionStatus().name());
        
        if (session.getPerformanceRating() != null) {
            response.setPerformanceRating(session.getPerformanceRating().name());
        }
        
        response.setNotes(session.getNotes());
        return response;
    }
}