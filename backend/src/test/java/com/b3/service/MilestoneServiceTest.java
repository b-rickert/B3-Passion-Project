package com.b3.service;

import com.b3.exception.ResourceNotFoundException;
import com.b3.model.Milestone;
import com.b3.model.Milestone.MilestoneType;
import com.b3.model.UserProfile;
import com.b3.repository.MilestoneRepository;
import com.b3.repository.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

/**
 * Unit tests for MilestoneService
 * Tests achievement tracking and milestone management
 */
@DisplayName("MilestoneService Tests")
class MilestoneServiceTest {

    @Mock
    private MilestoneRepository milestoneRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private MilestoneService milestoneService;

    private UserProfile testUser;
    private Milestone streakMilestone;
    private Milestone workoutMilestone;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create test user
        testUser = new UserProfile();
        testUser.setProfileId(1L);
        testUser.setDisplayName("TestUser");
        testUser.setCurrentStreak(5);
        testUser.setTotalWorkouts(8);

        // Create test milestones
        streakMilestone = new Milestone(testUser, "7-Day Streak! ⚡",
                "One full week!", MilestoneType.STREAK, 7);
        streakMilestone.setMilestoneId(1L);
        streakMilestone.updateProgress(5);

        workoutMilestone = new Milestone(testUser, "First 10! 🎯",
                "10 workouts complete!", MilestoneType.WORKOUT_COUNT, 10);
        workoutMilestone.setMilestoneId(2L);
        workoutMilestone.updateProgress(8);
    }

    // ========================================================================
    // GET ALL MILESTONES TESTS
    // ========================================================================

    @Test
    @DisplayName("Should get all milestones for user")
    void testGetAllMilestones() {
        // Given
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(milestoneRepository.findByUserProfileOrderByCreatedAtDesc(testUser))
                .thenReturn(Arrays.asList(streakMilestone, workoutMilestone));

        // When
        List<Milestone> result = milestoneService.getAllMilestones(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void testGetAllMilestonesUserNotFound() {
        // Given
        when(userProfileRepository.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class,
                () -> milestoneService.getAllMilestones(999L));
    }

    // ========================================================================
    // GET ACHIEVED MILESTONES TESTS
    // ========================================================================

    @Test
    @DisplayName("Should get achieved milestones")
    void testGetAchievedMilestones() {
        // Given
        Milestone achievedMilestone = new Milestone(testUser, "First Brick! 🧱",
                "Your first workout!", MilestoneType.WORKOUT_COUNT, 1);
        achievedMilestone.updateProgress(1); // This will mark it as achieved

        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(milestoneRepository.findByUserProfileAndIsAchieved(testUser, true))
                .thenReturn(Arrays.asList(achievedMilestone));

        // When
        List<Milestone> result = milestoneService.getAchievedMilestones(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getIsAchieved());
    }

    // ========================================================================
    // GET IN-PROGRESS MILESTONES TESTS
    // ========================================================================

    @Test
    @DisplayName("Should get in-progress milestones")
    void testGetInProgressMilestones() {
        // Given
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(milestoneRepository.findInProgressMilestones(testUser))
                .thenReturn(Arrays.asList(streakMilestone, workoutMilestone));

        // When
        List<Milestone> result = milestoneService.getInProgressMilestones(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // ========================================================================
    // GET ALMOST COMPLETE MILESTONES TESTS
    // ========================================================================

    @Test
    @DisplayName("Should get almost complete milestones")
    void testGetAlmostCompleteMilestones() {
        // Given - workoutMilestone is at 80% (8/10)
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(milestoneRepository.findAlmostCompleteMilestones(testUser))
                .thenReturn(Arrays.asList(workoutMilestone));

        // When
        List<Milestone> result = milestoneService.getAlmostCompleteMilestones(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    // ========================================================================
    // GET MILESTONES BY TYPE TESTS
    // ========================================================================

    @Test
    @DisplayName("Should get milestones by type")
    void testGetMilestonesByType() {
        // Given
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(milestoneRepository.findByUserProfileAndMilestoneType(testUser, MilestoneType.STREAK))
                .thenReturn(Arrays.asList(streakMilestone));

        // When
        List<Milestone> result = milestoneService.getMilestonesByType(1L, MilestoneType.STREAK);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(MilestoneType.STREAK, result.get(0).getMilestoneType());
    }

    // ========================================================================
    // GET ACHIEVED COUNT TESTS
    // ========================================================================

    @Test
    @DisplayName("Should get achieved milestone count")
    void testGetAchievedCount() {
        // Given
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(milestoneRepository.countByUserProfileAndIsAchieved(testUser, true))
                .thenReturn(5L);

        // When
        long result = milestoneService.getAchievedCount(1L);

        // Then
        assertEquals(5L, result);
    }

    // ========================================================================
    // INITIALIZE DEFAULT MILESTONES TESTS
    // ========================================================================

    @Test
    @DisplayName("Should initialize default milestones for new user")
    void testInitializeDefaultMilestones() {
        // Given
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(milestoneRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        List<Milestone> result = milestoneService.initializeDefaultMilestones(1L);

        // Then
        assertNotNull(result);
        assertEquals(16, result.size()); // We create 16 default milestones
        verify(milestoneRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("Should include streak milestones in defaults")
    void testDefaultMilestonesIncludeStreaks() {
        // Given
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(milestoneRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        List<Milestone> result = milestoneService.initializeDefaultMilestones(1L);

        // Then
        long streakCount = result.stream()
                .filter(m -> m.getMilestoneType() == MilestoneType.STREAK)
                .count();
        assertTrue(streakCount > 0, "Should include streak milestones");
    }

    @Test
    @DisplayName("Should include workout count milestones in defaults")
    void testDefaultMilestonesIncludeWorkoutCounts() {
        // Given
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(milestoneRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        List<Milestone> result = milestoneService.initializeDefaultMilestones(1L);

        // Then
        long workoutCount = result.stream()
                .filter(m -> m.getMilestoneType() == MilestoneType.WORKOUT_COUNT)
                .count();
        assertTrue(workoutCount > 0, "Should include workout count milestones");
    }

    // ========================================================================
    // CREATE CUSTOM MILESTONE TESTS
    // ========================================================================

    @Test
    @DisplayName("Should create custom milestone")
    void testCreateCustomMilestone() {
        // Given
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(milestoneRepository.save(any(Milestone.class))).thenAnswer(invocation -> {
            Milestone m = invocation.getArgument(0);
            m.setMilestoneId(100L);
            return m;
        });

        // When
        Milestone result = milestoneService.createCustomMilestone(
                1L, "Custom Goal!", "My personal goal", MilestoneType.GOAL_ACHIEVED, 50);

        // Then
        assertNotNull(result);
        assertEquals("Custom Goal!", result.getMilestoneName());
        assertEquals(MilestoneType.GOAL_ACHIEVED, result.getMilestoneType());
        assertEquals(50, result.getTargetValue());
        verify(milestoneRepository, times(1)).save(any(Milestone.class));
    }

    // ========================================================================
    // CHECK MILESTONES TESTS
    // ========================================================================

    @Test
    @DisplayName("Should check and update milestones after workout")
    void testCheckMilestones() {
        // Given
        testUser.setCurrentStreak(7); // Now at 7, should achieve 7-day streak
        testUser.setTotalWorkouts(10); // Now at 10, should achieve first 10

        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(milestoneRepository.findByUserProfileAndIsAchieved(testUser, false))
                .thenReturn(Arrays.asList(streakMilestone, workoutMilestone));
        when(milestoneRepository.save(any(Milestone.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        List<Milestone> newlyAchieved = milestoneService.checkMilestones(1L);

        // Then
        assertNotNull(newlyAchieved);
        // Both milestones should now be achieved
        verify(milestoneRepository, times(2)).save(any(Milestone.class));
    }

    @Test
    @DisplayName("Should return empty list when no new achievements")
    void testCheckMilestonesNoNewAchievements() {
        // Given - user still at 5 streak, needs 7
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(milestoneRepository.findByUserProfileAndIsAchieved(testUser, false))
                .thenReturn(Arrays.asList(streakMilestone));
        when(milestoneRepository.save(any(Milestone.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        List<Milestone> newlyAchieved = milestoneService.checkMilestones(1L);

        // Then
        assertNotNull(newlyAchieved);
        // Streak milestone is still not achieved (5/7)
    }

    // ========================================================================
    // UPDATE MILESTONE PROGRESS TESTS
    // ========================================================================

    @Test
    @DisplayName("Should update milestone progress by type")
    void testUpdateMilestoneProgress() {
        // Given
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(milestoneRepository.findByUserProfileAndMilestoneType(testUser, MilestoneType.STREAK))
                .thenReturn(Arrays.asList(streakMilestone));
        when(milestoneRepository.save(any(Milestone.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        milestoneService.updateMilestoneProgress(1L, MilestoneType.STREAK, 6);

        // Then
        verify(milestoneRepository, times(1)).save(any(Milestone.class));
    }

    @Test
    @DisplayName("Should not update already achieved milestones")
    void testUpdateMilestoneProgressSkipsAchieved() {
        // Given
        Milestone achievedMilestone = new Milestone(testUser, "First Brick! 🧱",
                "Your first workout!", MilestoneType.WORKOUT_COUNT, 1);
        achievedMilestone.updateProgress(1); // Mark as achieved

        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(milestoneRepository.findByUserProfileAndMilestoneType(testUser, MilestoneType.WORKOUT_COUNT))
                .thenReturn(Arrays.asList(achievedMilestone));

        // When
        milestoneService.updateMilestoneProgress(1L, MilestoneType.WORKOUT_COUNT, 10);

        // Then - should not save because milestone is already achieved
        verify(milestoneRepository, never()).save(any(Milestone.class));
    }
}