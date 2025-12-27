package com.b3.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import com.b3.model.UserProfile.FitnessLevel;
import com.b3.model.UserProfile.PrimaryGoal;

/**
 * Unit tests for UserProfile entity
 */
@DisplayName("UserProfile Entity Tests")
class UserProfileTest {

    private UserProfile userProfile;

    @BeforeEach
    void setUp() {
        userProfile = new UserProfile(
            "TestUser",
            25,
            FitnessLevel.INTERMEDIATE,
            PrimaryGoal.STRENGTH,
            "Dumbbells,Resistance Bands",
            4
        );
    }

    // ==================== Constructor Tests ====================

    @Test
    @DisplayName("Should create UserProfile with all fields")
    void testConstructorWithAllFields() {
        assertEquals("TestUser", userProfile.getDisplayName());
        assertEquals(25, userProfile.getAge());
        assertEquals(FitnessLevel.INTERMEDIATE, userProfile.getFitnessLevel());
        assertEquals(PrimaryGoal.STRENGTH, userProfile.getPrimaryGoal());
        assertEquals("Dumbbells,Resistance Bands", userProfile.getEquipment());
        assertEquals(4, userProfile.getWeeklyGoalDays());
    }

    @Test
    @DisplayName("Should create empty UserProfile with default constructor")
    void testDefaultConstructor() {
        UserProfile emptyProfile = new UserProfile();
        
        assertNotNull(emptyProfile);
        assertNull(emptyProfile.getDisplayName());
        assertNull(emptyProfile.getAge());
    }

    // ==================== Enum Tests ====================

    @Test
    @DisplayName("Should handle all FitnessLevel enum values")
    void testFitnessLevelEnum() {
        userProfile.setFitnessLevel(FitnessLevel.BEGINNER);
        assertEquals(FitnessLevel.BEGINNER, userProfile.getFitnessLevel());
        
        userProfile.setFitnessLevel(FitnessLevel.INTERMEDIATE);
        assertEquals(FitnessLevel.INTERMEDIATE, userProfile.getFitnessLevel());
        
        userProfile.setFitnessLevel(FitnessLevel.ADVANCED);
        assertEquals(FitnessLevel.ADVANCED, userProfile.getFitnessLevel());
    }

    @Test
    @DisplayName("Should handle all PrimaryGoal enum values")
    void testPrimaryGoalEnum() {
        userProfile.setPrimaryGoal(PrimaryGoal.STRENGTH);
        assertEquals(PrimaryGoal.STRENGTH, userProfile.getPrimaryGoal());
        
        userProfile.setPrimaryGoal(PrimaryGoal.CARDIO);
        assertEquals(PrimaryGoal.CARDIO, userProfile.getPrimaryGoal());
        
        userProfile.setPrimaryGoal(PrimaryGoal.FLEXIBILITY);
        assertEquals(PrimaryGoal.FLEXIBILITY, userProfile.getPrimaryGoal());
        
        userProfile.setPrimaryGoal(PrimaryGoal.WEIGHT_LOSS);
        assertEquals(PrimaryGoal.WEIGHT_LOSS, userProfile.getPrimaryGoal());
    }

    // ==================== Business Logic Tests ====================

    @Test
    @DisplayName("isBeginner() returns true for BEGINNER fitness level")
    void testIsBeginnerReturnsTrueForBeginner() {
        userProfile.setFitnessLevel(FitnessLevel.BEGINNER);
        assertTrue(userProfile.isBeginner());
    }

    @Test
    @DisplayName("isBeginner() returns false for non-BEGINNER fitness levels")
    void testIsBeginnerReturnsFalseForNonBeginner() {
        userProfile.setFitnessLevel(FitnessLevel.INTERMEDIATE);
        assertFalse(userProfile.isBeginner());
        
        userProfile.setFitnessLevel(FitnessLevel.ADVANCED);
        assertFalse(userProfile.isBeginner());
    }

    @Test
    @DisplayName("hasEquipment() returns true when equipment exists")
    void testHasEquipmentReturnsTrueWhenExists() {
        assertTrue(userProfile.hasEquipment("Dumbbells"));
        assertTrue(userProfile.hasEquipment("Resistance Bands"));
    }

    @Test
    @DisplayName("hasEquipment() is case-insensitive")
    void testHasEquipmentIsCaseInsensitive() {
        assertTrue(userProfile.hasEquipment("dumbbells"));
        assertTrue(userProfile.hasEquipment("DUMBBELLS"));
        assertTrue(userProfile.hasEquipment("DuMbBeLLs"));
    }

    @Test
    @DisplayName("hasEquipment() returns false when equipment doesn't exist")
    void testHasEquipmentReturnsFalseWhenNotExists() {
        assertFalse(userProfile.hasEquipment("Pull-up Bar"));
        assertFalse(userProfile.hasEquipment("Barbell"));
    }

    @Test
    @DisplayName("hasEquipment() returns false when equipment is null")
    void testHasEquipmentReturnsFalseWhenNull() {
        userProfile.setEquipment(null);
        assertFalse(userProfile.hasEquipment("Dumbbells"));
    }

    @Test
    @DisplayName("hasEquipment() returns false when equipment is empty")
    void testHasEquipmentReturnsFalseWhenEmpty() {
        userProfile.setEquipment("");
        assertFalse(userProfile.hasEquipment("Dumbbells"));
    }

    @Test
    @DisplayName("hasEquipment() returns false when equipment is blank")
    void testHasEquipmentReturnsFalseWhenBlank() {
        userProfile.setEquipment("   ");
        assertFalse(userProfile.hasEquipment("Dumbbells"));
    }

    // ==================== updateProfile() Tests ====================

    @Test
    @DisplayName("updateProfile() updates all non-null fields")
    void testUpdateProfileUpdatesAllFields() {
        userProfile.updateProfile(
            "UpdatedName",
            30,
            FitnessLevel.ADVANCED,
            PrimaryGoal.CARDIO,
            "Pull-up Bar",
            5
        );
        
        assertEquals("UpdatedName", userProfile.getDisplayName());
        assertEquals(30, userProfile.getAge());
        assertEquals(FitnessLevel.ADVANCED, userProfile.getFitnessLevel());
        assertEquals(PrimaryGoal.CARDIO, userProfile.getPrimaryGoal());
        assertEquals("Pull-up Bar", userProfile.getEquipment());
        assertEquals(5, userProfile.getWeeklyGoalDays());
    }

    @Test
    @DisplayName("updateProfile() supports partial updates (null values ignored)")
    void testUpdateProfilePartialUpdate() {
        String originalName = userProfile.getDisplayName();
        Integer originalAge = userProfile.getAge();
        
        userProfile.updateProfile(
            null, null, FitnessLevel.ADVANCED, null, null, 6
        );
        
        assertEquals(originalName, userProfile.getDisplayName());  // Unchanged
        assertEquals(originalAge, userProfile.getAge());  // Unchanged
        assertEquals(FitnessLevel.ADVANCED, userProfile.getFitnessLevel());  // Changed
        assertEquals(6, userProfile.getWeeklyGoalDays());  // Changed
    }

    @Test
    @DisplayName("updateProfile() does nothing when all params are null")
    void testUpdateProfileWithAllNullParams() {
        String originalName = userProfile.getDisplayName();
        Integer originalAge = userProfile.getAge();
        FitnessLevel originalLevel = userProfile.getFitnessLevel();
        
        userProfile.updateProfile(null, null, null, null, null, null);
        
        assertEquals(originalName, userProfile.getDisplayName());
        assertEquals(originalAge, userProfile.getAge());
        assertEquals(originalLevel, userProfile.getFitnessLevel());
    }

    // ==================== Edge Case Tests ====================

    @Test
    @DisplayName("Handles minimum valid age (13)")
    void testMinimumAge() {
        userProfile.setAge(13);
        assertEquals(13, userProfile.getAge());
    }

    @Test
    @DisplayName("Handles maximum valid age (120)")
    void testMaximumAge() {
        userProfile.setAge(120);
        assertEquals(120, userProfile.getAge());
    }

    @Test
    @DisplayName("Handles minimum weekly goal (1)")
    void testMinimumWeeklyGoal() {
        userProfile.setWeeklyGoalDays(1);
        assertEquals(1, userProfile.getWeeklyGoalDays());
    }

    @Test
    @DisplayName("Handles maximum weekly goal (7)")
    void testMaximumWeeklyGoal() {
        userProfile.setWeeklyGoalDays(7);
        assertEquals(7, userProfile.getWeeklyGoalDays());
    }

    // ==================== equals() and hashCode() Tests ====================

    @Test
    @DisplayName("equals() returns true for same profileId")
    void testEqualsReturnsTrueForSameId() {
        UserProfile profile1 = new UserProfile();
        profile1.setProfileId(1L);
        
        UserProfile profile2 = new UserProfile();
        profile2.setProfileId(1L);
        
        assertEquals(profile1, profile2);
    }

    @Test
    @DisplayName("equals() returns false for different profileIds")
    void testEqualsReturnsFalseForDifferentIds() {
        UserProfile profile1 = new UserProfile();
        profile1.setProfileId(1L);
        
        UserProfile profile2 = new UserProfile();
        profile2.setProfileId(2L);
        
        assertNotEquals(profile1, profile2);
    }

    @Test
    @DisplayName("equals() returns true when comparing to itself")
    void testEqualsReturnsTrueForSameObject() {
        assertEquals(userProfile, userProfile);
    }

    @Test
    @DisplayName("equals() returns false when comparing to null")
    void testEqualsReturnsFalseForNull() {
        assertNotEquals(null, userProfile);
    }

    @Test
    @DisplayName("hashCode() is same for profiles with same profileId")
    void testHashCodeSameForSameId() {
        UserProfile profile1 = new UserProfile();
        profile1.setProfileId(1L);
        
        UserProfile profile2 = new UserProfile();
        profile2.setProfileId(1L);
        
        assertEquals(profile1.hashCode(), profile2.hashCode());
    }

    // ==================== toString() Test ====================

    @Test
    @DisplayName("toString() contains key fields")
    void testToStringContainsKeyFields() {
        String result = userProfile.toString();
        
        assertTrue(result.contains("TestUser"));
        assertTrue(result.contains("25"));
        assertTrue(result.contains("INTERMEDIATE"));
        assertTrue(result.contains("STRENGTH"));
        assertTrue(result.contains("4"));
    }
}