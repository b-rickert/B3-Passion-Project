package com.b3.service;

import com.b3.dto.DailyLogCreateDTO;
import com.b3.dto.DailyLogDTO;
import com.b3.exception.ResourceNotFoundException;
import com.b3.exception.DuplicateResourceException;
import com.b3.model.DailyLog;
import com.b3.model.UserProfile;
import com.b3.repository.DailyLogRepository;
import com.b3.repository.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for DailyLogService
 * Tests daily check-in functionality and wellness tracking
 */
@DisplayName("DailyLogService Tests")
class DailyLogServiceTest {

    @Mock
    private DailyLogRepository dailyLogRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private DailyLogService dailyLogService;

    private UserProfile testUser;
    private DailyLog testLog;
    private DailyLogCreateDTO testCreateDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create test user
        testUser = new UserProfile();
        testUser.setProfileId(1L);
        testUser.setDisplayName("TestUser");

        // Create test daily log
        testLog = new DailyLog();
        testLog.setLogId(1L);
        testLog.setUserProfile(testUser);
        testLog.setLogDate(LocalDate.now());
        testLog.setEnergyLevel(4);
        testLog.setStressLevel(2);
        testLog.setSleepQuality(4);
        testLog.setMood(DailyLog.Mood.GOOD);
        testLog.setNotes("Feeling good today");

        // Create test DTO
        testCreateDTO = new DailyLogCreateDTO();
        testCreateDTO.setProfileId(1L);
        testCreateDTO.setEnergyLevel(4);
        testCreateDTO.setStressLevel(2);
        testCreateDTO.setSleepQuality(4);
        testCreateDTO.setMood("GOOD");
        testCreateDTO.setNotes("Feeling good today");
    }

    // ========================================================================
    // SUBMIT DAILY LOG TESTS
    // ========================================================================

    @Test
    @DisplayName("Should submit daily log successfully")
    void testSubmitDailyLog() {
        // Given
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(dailyLogRepository.existsByUserProfileAndLogDate(any(), any())).thenReturn(false);
        when(dailyLogRepository.save(any(DailyLog.class))).thenReturn(testLog);

        // When
        DailyLogDTO result = dailyLogService.submitDailyLog(testCreateDTO);

        // Then
        assertNotNull(result);
        assertEquals(4, result.getEnergyLevel());
        assertEquals(2, result.getStressLevel());
        assertEquals("GOOD", result.getMood());
        verify(dailyLogRepository, times(1)).save(any(DailyLog.class));
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void testSubmitDailyLogUserNotFound() {
        // Given
        when(userProfileRepository.findById(999L)).thenReturn(Optional.empty());
        testCreateDTO.setProfileId(999L);

        // When/Then
        assertThrows(ResourceNotFoundException.class,
                () -> dailyLogService.submitDailyLog(testCreateDTO));
    }

    @Test
    @DisplayName("Should throw exception when log already exists for today")
    void testSubmitDailyLogDuplicate() {
        // Given
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(dailyLogRepository.existsByUserProfileAndLogDate(any(), any())).thenReturn(true);

        // When/Then
        assertThrows(DuplicateResourceException.class,
                () -> dailyLogService.submitDailyLog(testCreateDTO));
    }

    // ========================================================================
    // GET TODAY'S LOG TESTS
    // ========================================================================

    @Test
    @DisplayName("Should get today's log successfully")
    void testGetTodaysLog() {
        // Given
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(dailyLogRepository.findByUserProfileAndLogDate(any(), any()))
                .thenReturn(Optional.of(testLog));

        // When
        DailyLogDTO result = dailyLogService.getTodaysLog(1L);

        // Then
        assertNotNull(result);
        assertEquals(4, result.getEnergyLevel());
    }

    @Test
    @DisplayName("Should throw exception when today's log not found")
    void testGetTodaysLogNotFound() {
        // Given
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(dailyLogRepository.findByUserProfileAndLogDate(any(), any()))
                .thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class,
                () -> dailyLogService.getTodaysLog(1L));
    }

    // ========================================================================
    // GET RECENT LOGS TESTS
    // ========================================================================

    @Test
    @DisplayName("Should get recent logs successfully")
    void testGetRecentLogs() {
        // Given
        DailyLog log2 = new DailyLog();
        log2.setLogId(2L);
        log2.setUserProfile(testUser);
        log2.setLogDate(LocalDate.now().minusDays(1));
        log2.setEnergyLevel(3);
        log2.setStressLevel(3);
        log2.setSleepQuality(3);
        log2.setMood(DailyLog.Mood.OKAY);

        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(dailyLogRepository.findRecentLogs(any(), any()))
                .thenReturn(Arrays.asList(testLog, log2));

        // When
        List<DailyLogDTO> result = dailyLogService.getRecentLogs(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Should return empty list when no recent logs")
    void testGetRecentLogsEmpty() {
        // Given
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(dailyLogRepository.findRecentLogs(any(), any())).thenReturn(Arrays.asList());

        // When
        List<DailyLogDTO> result = dailyLogService.getRecentLogs(1L);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ========================================================================
    // GET LOGS BY DATE RANGE TESTS
    // ========================================================================

    @Test
    @DisplayName("Should get logs by date range")
    void testGetLogsByDateRange() {
        // Given
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();

        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(dailyLogRepository.findByUserProfileAndLogDateBetweenOrderByLogDateDesc(
                any(), any(), any())).thenReturn(Arrays.asList(testLog));

        // When
        List<DailyLogDTO> result = dailyLogService.getLogsByDateRange(1L, startDate, endDate);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    // ========================================================================
    // HAS LOGGED TODAY TESTS
    // ========================================================================

    @Test
    @DisplayName("Should return true when user has logged today")
    void testHasLoggedTodayTrue() {
        // Given
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(dailyLogRepository.existsByUserProfileAndLogDate(any(), any())).thenReturn(true);

        // When
        boolean result = dailyLogService.hasLoggedToday(1L);

        // Then
        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false when user has not logged today")
    void testHasLoggedTodayFalse() {
        // Given
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(dailyLogRepository.existsByUserProfileAndLogDate(any(), any())).thenReturn(false);

        // When
        boolean result = dailyLogService.hasLoggedToday(1L);

        // Then
        assertFalse(result);
    }

    // ========================================================================
    // GET RECOVERY NEEDED DAYS TESTS
    // ========================================================================

    @Test
    @DisplayName("Should get recovery needed days")
    void testGetRecoveryNeededDays() {
        // Given
        DailyLog lowEnergyLog = new DailyLog();
        lowEnergyLog.setLogId(3L);
        lowEnergyLog.setUserProfile(testUser);
        lowEnergyLog.setLogDate(LocalDate.now().minusDays(2));
        lowEnergyLog.setEnergyLevel(2);
        lowEnergyLog.setStressLevel(4);
        lowEnergyLog.setSleepQuality(2);
        lowEnergyLog.setMood(DailyLog.Mood.STRESSED);

        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(dailyLogRepository.findRecoveryNeededDays(any()))
                .thenReturn(Arrays.asList(lowEnergyLog));

        // When
        List<DailyLogDTO> result = dailyLogService.getRecoveryNeededDays(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getEnergyLevel());
        assertEquals(4, result.get(0).getStressLevel());
    }

    // ========================================================================
    // UPDATE DAILY LOG TESTS
    // ========================================================================

    @Test
    @DisplayName("Should update daily log successfully")
    void testUpdateDailyLog() {
        // Given
        testCreateDTO.setEnergyLevel(5);
        testCreateDTO.setMood("GREAT");

        DailyLog updatedLog = new DailyLog();
        updatedLog.setLogId(1L);
        updatedLog.setUserProfile(testUser);
        updatedLog.setLogDate(LocalDate.now());
        updatedLog.setEnergyLevel(5);
        updatedLog.setStressLevel(2);
        updatedLog.setSleepQuality(4);
        updatedLog.setMood(DailyLog.Mood.GREAT);

        when(dailyLogRepository.findById(1L)).thenReturn(Optional.of(testLog));
        when(dailyLogRepository.save(any(DailyLog.class))).thenReturn(updatedLog);

        // When
        DailyLogDTO result = dailyLogService.updateDailyLog(1L, testCreateDTO);

        // Then
        assertNotNull(result);
        assertEquals(5, result.getEnergyLevel());
        assertEquals("GREAT", result.getMood());
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent log")
    void testUpdateDailyLogNotFound() {
        // Given
        when(dailyLogRepository.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class,
                () -> dailyLogService.updateDailyLog(999L, testCreateDTO));
    }

    @Test
    @DisplayName("Should throw exception when updating past log")
    void testUpdateDailyLogPastDate() {
        // Given
        testLog.setLogDate(LocalDate.now().minusDays(1)); // Yesterday's log
        when(dailyLogRepository.findById(1L)).thenReturn(Optional.of(testLog));

        // When/Then
        assertThrows(IllegalStateException.class,
                () -> dailyLogService.updateDailyLog(1L, testCreateDTO));
    }
}