package com.b3.controller;

import com.b3.dto.DailyLogCreateDTO;
import com.b3.dto.request.UserProfileUpdateRequest;
import com.b3.dto.request.WorkoutSessionCreateRequest;
import com.b3.dto.request.WorkoutSessionCompleteRequest;
import com.b3.model.UserProfile;
import com.b3.model.Workout;
import com.b3.repository.UserProfileRepository;
import com.b3.repository.WorkoutRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    @Autowired
    private WorkoutRepository workoutRepository;

    private UserProfile testUser;
    private Workout testWorkout;

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

        // Create test workout if not exists
        testWorkout = workoutRepository.findAll().stream().findFirst().orElseGet(() -> {
            Workout workout = new Workout();
            workout.setName("Test Workout");
            workout.setDescription("Test workout description");
            workout.setWorkoutType(Workout.WorkoutType.STRENGTH);
            workout.setDifficultyLevel(Workout.DifficultyLevel.INTERMEDIATE);
            workout.setEstimatedDuration(30);
            workout.setRequiredEquipment("None");
            return workoutRepository.save(workout);
        });
    }

    // ========================================================================
    // PROFILE CONTROLLER TESTS
    // ========================================================================

    @Nested
    @DisplayName("Profile Controller")
    class ProfileControllerTests {

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

        @Test
        @DisplayName("PUT /api/v1/profile/{id} - Should update user profile")
        void testUpdateProfile() throws Exception {
            UserProfileUpdateRequest request = new UserProfileUpdateRequest();
            request.setDisplayName("Updated Name");
            request.setAge(30);

            mockMvc.perform(put("/api/v1/profile/" + testUser.getProfileId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.displayName").value("Updated Name"));
        }

        @Test
        @DisplayName("PUT /api/v1/profile - Should update default profile")
        void testUpdateDefaultProfile() throws Exception {
            UserProfileUpdateRequest request = new UserProfileUpdateRequest();
            request.setDisplayName("Default Updated");

            mockMvc.perform(put("/api/v1/profile")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
    }

    // ========================================================================
    // WORKOUT CONTROLLER TESTS
    // ========================================================================

    @Nested
    @DisplayName("Workout Controller")
    class WorkoutControllerTests {

        @Test
        @DisplayName("GET /api/v1/workouts - Should return all workouts")
        void testGetAllWorkouts() throws Exception {
            mockMvc.perform(get("/api/v1/workouts"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        }

        @Test
        @DisplayName("GET /api/v1/workouts/{id} - Should return workout by ID")
        void testGetWorkoutById() throws Exception {
            mockMvc.perform(get("/api/v1/workouts/" + testWorkout.getWorkoutId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.workoutId").value(testWorkout.getWorkoutId()));
        }

        @Test
        @DisplayName("GET /api/v1/workouts/999 - Should return 404 for non-existent workout")
        void testGetWorkoutNotFound() throws Exception {
            mockMvc.perform(get("/api/v1/workouts/999"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("GET /api/v1/workouts/search?q=test - Should search workouts")
        void testSearchWorkouts() throws Exception {
            mockMvc.perform(get("/api/v1/workouts/search").param("q", "test"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        }

        @Test
        @DisplayName("GET /api/v1/workouts/type/STRENGTH - Should filter by type")
        void testGetWorkoutsByType() throws Exception {
            mockMvc.perform(get("/api/v1/workouts/type/STRENGTH"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        }

        @Test
        @DisplayName("GET /api/v1/workouts/difficulty/INTERMEDIATE - Should filter by difficulty")
        void testGetWorkoutsByDifficulty() throws Exception {
            mockMvc.perform(get("/api/v1/workouts/difficulty/INTERMEDIATE"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        }

        @Test
        @DisplayName("GET /api/v1/workouts/recommended/INTERMEDIATE - Should get recommended workouts")
        void testGetRecommendedWorkouts() throws Exception {
            mockMvc.perform(get("/api/v1/workouts/recommended/INTERMEDIATE"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        }
    }

    // ========================================================================
    // WORKOUT SESSION CONTROLLER TESTS
    // ========================================================================

    @Nested
    @DisplayName("Workout Session Controller")
    class WorkoutSessionControllerTests {

        @Test
        @DisplayName("POST /api/v1/sessions - Should create new session")
        void testCreateSession() throws Exception {
            WorkoutSessionCreateRequest request = new WorkoutSessionCreateRequest();
            request.setProfileId(testUser.getProfileId());
            request.setWorkoutId(testWorkout.getWorkoutId());

            mockMvc.perform(post("/api/v1/sessions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.sessionId").exists());
        }

        @Test
        @DisplayName("POST /api/v1/sessions - Should return 400 when profileId is null")
        void testCreateSessionMissingProfileId() throws Exception {
            WorkoutSessionCreateRequest request = new WorkoutSessionCreateRequest();
            request.setWorkoutId(testWorkout.getWorkoutId());
            // profileId is null

            mockMvc.perform(post("/api/v1/sessions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("POST /api/v1/sessions - Should return 400 when workoutId is null")
        void testCreateSessionMissingWorkoutId() throws Exception {
            WorkoutSessionCreateRequest request = new WorkoutSessionCreateRequest();
            request.setProfileId(testUser.getProfileId());
            // workoutId is null

            mockMvc.perform(post("/api/v1/sessions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("GET /api/v1/sessions/history/{profileId} - Should return session history")
        void testGetSessionHistory() throws Exception {
            mockMvc.perform(get("/api/v1/sessions/history/" + testUser.getProfileId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        }

        @Test
        @DisplayName("GET /api/v1/sessions/active/{profileId} - Should check for active session")
        void testGetActiveSession() throws Exception {
            mockMvc.perform(get("/api/v1/sessions/active/" + testUser.getProfileId()))
                    .andExpect(status().isNoContent());
}
    }

    // ========================================================================
    // BRICK CONTROLLER TESTS
    // ========================================================================

    @Nested
    @DisplayName("Brick Controller")
    class BrickControllerTests {

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
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalBricks").exists());
        }

        @Test
        @DisplayName("GET /api/v1/bricks/today/{profileId} - Should check today's brick")
        void testHasBrickToday() throws Exception {
            mockMvc.perform(get("/api/v1/bricks/today/" + testUser.getProfileId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.hasBrickToday").isBoolean())
                    .andExpect(jsonPath("$.date").exists());
        }

        @Test
        @DisplayName("GET /api/v1/bricks/history/{profileId} - Should return brick history")
        void testGetBrickHistory() throws Exception {
            mockMvc.perform(get("/api/v1/bricks/history/" + testUser.getProfileId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        }
    }

    // ========================================================================
    // DAILY LOG CONTROLLER TESTS
    // ========================================================================

    @Nested
    @DisplayName("Daily Log Controller")
    class DailyLogControllerTests {

        @Test
        @DisplayName("POST /api/v1/daily-logs - Should create daily log")
        void testCreateDailyLog() throws Exception {
            DailyLogCreateDTO request = new DailyLogCreateDTO();
            request.setProfileId(testUser.getProfileId());
            request.setEnergyLevel(4);
            request.setStressLevel(2);
            request.setSleepQuality(4);
            request.setMood("GREAT");
            request.setNotes("Feeling good!");

            mockMvc.perform(post("/api/v1/daily-logs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.logId").exists());
        }

        @Test
        @DisplayName("POST /api/v1/daily-logs - Should return 400 when profileId is null")
        void testCreateDailyLogMissingProfileId() throws Exception {
            DailyLogCreateDTO request = new DailyLogCreateDTO();
            request.setEnergyLevel(4);
            request.setStressLevel(2);
            request.setSleepQuality(4);
            request.setMood("GREAT");

            mockMvc.perform(post("/api/v1/daily-logs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

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

        @Test
        @DisplayName("GET /api/v1/daily-logs/today/{profileId} - Should get today's log")
        void testGetTodaysLog() throws Exception {
            // First create a log
            DailyLogCreateDTO request = new DailyLogCreateDTO();
            request.setProfileId(testUser.getProfileId());
            request.setEnergyLevel(4);
            request.setStressLevel(2);
            request.setSleepQuality(4);
            request.setMood("GREAT");

            mockMvc.perform(post("/api/v1/daily-logs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // Then retrieve it
            mockMvc.perform(get("/api/v1/daily-logs/today/" + testUser.getProfileId()))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("GET /api/v1/daily-logs/recovery/{profileId} - Should get recovery days")
        void testGetRecoveryDays() throws Exception {
            mockMvc.perform(get("/api/v1/daily-logs/recovery/" + testUser.getProfileId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        }
    }

    // ========================================================================
    // MILESTONE CONTROLLER TESTS
    // ========================================================================

    @Nested
    @DisplayName("Milestone Controller")
    class MilestoneControllerTests {

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

        @Test
        @DisplayName("GET /api/v1/milestones/{profileId}/achieved - Should return achieved milestones")
        void testGetAchievedMilestones() throws Exception {
            mockMvc.perform(get("/api/v1/milestones/" + testUser.getProfileId() + "/achieved"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        }

        @Test
        @DisplayName("GET /api/v1/milestones/{profileId}/in-progress - Should return in-progress milestones")
        void testGetInProgressMilestones() throws Exception {
            mockMvc.perform(get("/api/v1/milestones/" + testUser.getProfileId() + "/in-progress"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        }

        @Test
        @DisplayName("GET /api/v1/milestones/{profileId}/almost-complete - Should return almost complete milestones")
        void testGetAlmostCompleteMilestones() throws Exception {
            mockMvc.perform(get("/api/v1/milestones/" + testUser.getProfileId() + "/almost-complete"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        }

        @Test
        @DisplayName("POST /api/v1/milestones/{profileId}/check - Should check and update milestones")
        void testCheckMilestones() throws Exception {
            mockMvc.perform(post("/api/v1/milestones/" + testUser.getProfileId() + "/check"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        }
    }

    // ========================================================================
    // BEHAVIOR CONTROLLER TESTS
    // ========================================================================

    @Nested
    @DisplayName("Behavior Controller")
    class BehaviorControllerTests {

        @Test
        @DisplayName("POST /api/v1/behavior/{profileId} - Should create behavior profile")
        void testCreateBehaviorProfile() throws Exception {
            mockMvc.perform(post("/api/v1/behavior/" + testUser.getProfileId()))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("GET /api/v1/behavior/{profileId} - Should return 404 when no behavior profile")
        void testGetBehaviorProfileNotFound() throws Exception {
            // Create a new user without behavior profile
            UserProfile newUser = new UserProfile();
            newUser.setDisplayName("No Behavior User");
            newUser.setAge(25);
            newUser.setFitnessLevel(UserProfile.FitnessLevel.BEGINNER);
            newUser.setPrimaryGoal(UserProfile.PrimaryGoal.WEIGHT_LOSS);
            newUser.setWeeklyGoalDays(3);
            newUser = userProfileRepository.save(newUser);

            mockMvc.perform(get("/api/v1/behavior/" + newUser.getProfileId()))
                    .andExpect(status().isNotFound());
        }
    }
}