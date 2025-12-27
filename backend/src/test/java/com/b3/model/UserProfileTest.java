package com.b3.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Nested;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import com.b3.model.UserProfile.FitnessLevel;
import com.b3.model.UserProfile.PrimaryGoal;

/**
 * Comprehensive unit tests for UserProfile entity.
 * Uses nested test classes for structured organization.
 */
@TestMethodOrder(MethodOrderer.DisplayName.class)
class UserProfileTest {

    private UserProfile userProfile;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
        factory.afterPropertiesSet();
        validator = factory.getValidator();
    }

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

    // =====================================================
    // CONSTRUCTOR TESTS
    // =====================================================

    @Nested
    @DisplayName("1. Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("1.1 Should build profile with all fields via constructor")
        void testFullConstructor() {
            assertEquals("TestUser", userProfile.getDisplayName());
            assertEquals(25, userProfile.getAge());
            assertEquals(FitnessLevel.INTERMEDIATE, userProfile.getFitnessLevel());
            assertEquals(PrimaryGoal.STRENGTH, userProfile.getPrimaryGoal());
            assertEquals("Dumbbells,Resistance Bands", userProfile.getEquipment());
            assertEquals(4, userProfile.getWeeklyGoalDays());
        }

        @Test
        @DisplayName("1.2 Default constructor creates empty profile")
        void testDefaultConstructor() {
            UserProfile empty = new UserProfile();
            assertNotNull(empty);
            assertNull(empty.getDisplayName());
            assertNull(empty.getAge());
        }
    }

    // =====================================================
    // ENUM TESTS
    // =====================================================

    @Nested
    @DisplayName("2. Enum Tests")
    class EnumTests {

        @Test
        @DisplayName("2.1 FitnessLevel enum values work correctly")
        void testFitnessLevelEnum() {
            userProfile.setFitnessLevel(FitnessLevel.BEGINNER);
            assertEquals(FitnessLevel.BEGINNER, userProfile.getFitnessLevel());

            userProfile.setFitnessLevel(FitnessLevel.INTERMEDIATE);
            assertEquals(FitnessLevel.INTERMEDIATE, userProfile.getFitnessLevel());

            userProfile.setFitnessLevel(FitnessLevel.ADVANCED);
            assertEquals(FitnessLevel.ADVANCED, userProfile.getFitnessLevel());
        }

        @Test
        @DisplayName("2.2 PrimaryGoal enum values work correctly")
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
    }

    // =====================================================
    // BUSINESS LOGIC TESTS
    // =====================================================

    @Nested
    @DisplayName("3. Business Logic Tests")
    class BusinessLogicTests {

        @Test
        @DisplayName("3.1 isBeginner() returns true only for BEGINNER")
        void testIsBeginner() {
            userProfile.setFitnessLevel(FitnessLevel.BEGINNER);
            assertTrue(userProfile.isBeginner());

            userProfile.setFitnessLevel(FitnessLevel.ADVANCED);
            assertFalse(userProfile.isBeginner());
        }

        @Test
        @DisplayName("3.2 hasEquipment() matches case-insensitively")
        void testHasEquipmentCaseInsensitive() {
            assertTrue(userProfile.hasEquipment("dumbbells"));
            assertTrue(userProfile.hasEquipment("DUMBBELLS"));
        }

        @Test
        @DisplayName("3.3 hasEquipment() returns false when missing or null")
        void testHasEquipmentMissing() {
            assertFalse(userProfile.hasEquipment("Pull-up Bar"));

            userProfile.setEquipment(null);
            assertFalse(userProfile.hasEquipment("Dumbbells"));
        }

        @Test
        @DisplayName("3.4 updateProfile() updates only non-null values")
        void testPartialUpdate() {
            String originalName = userProfile.getDisplayName();

            userProfile.updateProfile(null, null, FitnessLevel.ADVANCED, null, null, 6);

            assertEquals(originalName, userProfile.getDisplayName());
            assertEquals(FitnessLevel.ADVANCED, userProfile.getFitnessLevel());
            assertEquals(6, userProfile.getWeeklyGoalDays());
        }
    }

    // =====================================================
    // VALIDATION TESTS (BEAN VALIDATION)
    // =====================================================

    @Nested
    @DisplayName("4. Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("4.1 Display name cannot be blank")
        void testBlankDisplayName() {
            userProfile.setDisplayName("");

            Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);
            assertFalse(violations.isEmpty());
        }

        @Test
        @DisplayName("4.2 Age must be within valid range 13–120")
        void testAgeRange() {
            userProfile.setAge(12);
            assertFalse(validator.validate(userProfile).isEmpty());

            userProfile.setAge(121);
            assertFalse(validator.validate(userProfile).isEmpty());
        }

        @Test
        @DisplayName("4.3 Weekly goal must be 1–7")
        void testWeeklyGoalRange() {
            userProfile.setWeeklyGoalDays(0);
            assertFalse(validator.validate(userProfile).isEmpty());

            userProfile.setWeeklyGoalDays(8);
            assertFalse(validator.validate(userProfile).isEmpty());
        }
    }

    // =====================================================
    // LIFECYCLE CALLBACK TESTS (PrePersist / PreUpdate)
    // =====================================================

    @Nested
    @DisplayName("5. JPA Lifecycle Callback Tests")
    class LifecycleTests {

        @Test
        @DisplayName("5.1 PrePersist populates createdAt and updatedAt")
        void testPrePersist() {
            UserProfile profile = new UserProfile();

            profile.onCreate(); // manually invoke lifecycle event

            assertNotNull(profile.getCreatedAt());
            assertNotNull(profile.getUpdatedAt());
            assertEquals(profile.getCreatedAt(), profile.getUpdatedAt());
        }

        @Test
        @DisplayName("5.2 PreUpdate updates updatedAt but not createdAt")
        void testPreUpdate() throws InterruptedException {
            UserProfile profile = new UserProfile();
            profile.onCreate();

            LocalDateTime createdBefore = profile.getCreatedAt();
            LocalDateTime updatedBefore = profile.getUpdatedAt();

            Thread.sleep(2); // ensure time has advanced
            profile.onUpdate();

            assertEquals(createdBefore, profile.getCreatedAt());
            assertTrue(profile.getUpdatedAt().isAfter(updatedBefore));
        }
    }

    // =====================================================
    // EQUALS + HASHCODE
    // =====================================================

    @Nested
    @DisplayName("6. equals() and hashCode() Tests")
    class EqualityTests {

        @Test
        @DisplayName("6.1 equals() true for same IDs")
        void testEqualsSameId() {
            UserProfile p1 = new UserProfile();
            UserProfile p2 = new UserProfile();

            p1.setProfileId(1L);
            p2.setProfileId(1L);

            assertEquals(p1, p2);
        }

        @Test
        @DisplayName("6.2 equals() false for different IDs")
        void testEqualsDifferentId() {
            UserProfile p1 = new UserProfile();
            p1.setProfileId(1L);

            UserProfile p2 = new UserProfile();
            p2.setProfileId(2L);

            assertNotEquals(p1, p2);
        }

        @Test
        @DisplayName("6.3 hashCode matches for identical IDs")
        void testHashCodeConsistency() {
            UserProfile p1 = new UserProfile();
            UserProfile p2 = new UserProfile();

            p1.setProfileId(10L);
            p2.setProfileId(10L);

            assertEquals(p1.hashCode(), p2.hashCode());
        }
    }

    // =====================================================
    // TOSTRING
    // =====================================================

    @Nested
    @DisplayName("7. toString() Tests")
    class ToStringTests {

        @Test
        @DisplayName("7.1 toString() should include key fields")
        void testToString() {
            String str = userProfile.toString();

            assertTrue(str.contains("TestUser"));
            assertTrue(str.contains("INTERMEDIATE"));
            assertTrue(str.contains("STRENGTH"));
            assertTrue(str.contains("4"));
        }
    }
}
