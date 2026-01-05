package com.b3.controller;

import com.b3.dto.DailyLogCreateDTO;
import com.b3.dto.request.WorkoutSessionCreateRequest;
import com.b3.model.UserProfile;
import com.b3.repository.UserProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for REST API endpoints
 * Tests the full request/response cycle through controllers
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("API Integration Tests")
class ApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserProfileRepository userProfileRepository;

    private UserProfile testUser;

    @BeforeEach
    void setUp() {
        // Create test user if not exists
        testUser = userProfileRepository.findById(1L).orElseGet(() -> {
            UserProfile user = new UserProfile();
            user.setDisplayName("Test User");
            user.setAge(25);
            user.setFitnessLevel(UserProfile.FitnessLevel.INTERMEDIATE);
            user.setPrimaryGoal(UserProfile.PrimaryGoal.STRENGTH);
            user.setWeeklyGoalDays(4);
            return userProfileRepository.save(user);
        });
    }

    // ========================================================================
    // PROFILE CONTROLLER TESTS
    // ========================================================================

    @Test
    @DisplayName("GET /api/v1/profile/{id} - Should return user profile")
    void testGetProfile() throws Exception {
        mockMvc.perform(get("/api/v1/profile/" + testUser.getProfileId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profileId").value(testUser.getProfileId()))
                .andExpect(jsonPath("$.displayName").exists());
    }

    @Test
    @DisplayName("GET /api/v1/profile - Should return default profile")
    void testGetDefaultProfile() throws Exception {
        mockMvc.perform(get("/api/v1/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profileId").exists());
    }

    @Test
    @DisplayName("GET /api/v1/profile/999 - Should return 404 for non-existent user")
    void testGetProfileNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/profile/999"))
                .andExpect(status().isNotFound());
    }

    // ========================================================================
    // WORKOUT CONTROLLER TESTS
    // ========================================================================

    @Test
    @DisplayName("GET /api/v1/workouts - Should return all workouts")
    void testGetAllWorkouts() throws Exception {
        mockMvc.perform(get("/api/v1/workouts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("GET /api/v1/workouts/search?q=test - Should search workouts")
    void testSearchWorkouts() throws Exception {
        mockMvc.perform(get("/api/v1/workouts/search").param("q", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // ========================================================================
    // BRICK CONTROLLER TESTS
    // ========================================================================

    @Test
    @DisplayName("GET /api/v1/bricks/calendar/{profileId} - Should return brick calendar")
    void testGetBrickCalendar() throws Exception {
        mockMvc.perform(get("/api/v1/bricks/calendar/" + testUser.getProfileId())
                        .param("month", "1")
                        .param("year", "2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("GET /api/v1/bricks/stats/{profileId} - Should return brick stats")
    void testGetBrickStats() throws Exception {
        mockMvc.perform(get("/api/v1/bricks/stats/" + testUser.getProfileId()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/bricks/today/{profileId} - Should check today's brick")
    void testHasBrickToday() throws Exception {
        mockMvc.perform(get("/api/v1/bricks/today/" + testUser.getProfileId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hasBrickToday").isBoolean())
                .andExpect(jsonPath("$.date").exists());
    }

    // ========================================================================
    // DAILY LOG CONTROLLER TESTS
    // ========================================================================

    @Test
    @DisplayName("GET /api/v1/daily-logs/check/{profileId} - Should check if logged today")
    void testHasLoggedToday() throws Exception {
        mockMvc.perform(get("/api/v1/daily-logs/check/" + testUser.getProfileId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hasLoggedToday").isBoolean())
                .andExpect(jsonPath("$.date").exists());
    }

    @Test
    @DisplayName("GET /api/v1/daily-logs/recent/{profileId} - Should return recent logs")
    void testGetRecentLogs() throws Exception {
        mockMvc.perform(get("/api/v1/daily-logs/recent/" + testUser.getProfileId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // ========================================================================
    // MILESTONE CONTROLLER TESTS
    // ========================================================================

    @Test
    @DisplayName("GET /api/v1/milestones/{profileId} - Should return all milestones")
    void testGetAllMilestones() throws Exception {
        mockMvc.perform(get("/api/v1/milestones/" + testUser.getProfileId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("GET /api/v1/milestones/{profileId}/stats - Should return milestone stats")
    void testGetMilestoneStats() throws Exception {
        mockMvc.perform(get("/api/v1/milestones/" + testUser.getProfileId() + "/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalMilestones").exists())
                .andExpect(jsonPath("$.achievedCount").exists());
    }

    @Test
    @DisplayName("POST /api/v1/milestones/{profileId}/initialize - Should initialize milestones")
    void testInitializeMilestones() throws Exception {
        mockMvc.perform(post("/api/v1/milestones/" + testUser.getProfileId() + "/initialize"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}