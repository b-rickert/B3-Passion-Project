package com.b3.repository;

import com.b3.model.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for UserProfileRepository
 * Using @SpringBootTest instead of @DataJpaTest to avoid H2/SQLite issues
 */
@SpringBootTest
@Transactional  // Rollback after each test
@DisplayName("UserProfileRepository Tests")
class UserProfileRepositoryTest {

    @Autowired
    private UserProfileRepository repository;

    private UserProfile testUser;

    @BeforeEach
    void setUp() {
        // Clean slate
        repository.deleteAll();
        
        // Create and save test user
        testUser = new UserProfile(
            "TestUser",
            25,
            UserProfile.FitnessLevel.INTERMEDIATE,
            UserProfile.PrimaryGoal.STRENGTH,
            "Dumbbells",
            4
        );
        testUser = repository.save(testUser);
    }

    @Test
    @DisplayName("Should save and find user by ID")
    void testSaveAndFindById() {
        Optional<UserProfile> found = repository.findById(testUser.getProfileId());
        
        assertTrue(found.isPresent());
        assertEquals("TestUser", found.get().getDisplayName());
    }

    @Test
    @DisplayName("Should find all users")
    void testFindAll() {
        List<UserProfile> users = repository.findAll();
        
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
    }

    @Test
    @DisplayName("Should find user by display name")
    void testFindByDisplayName() {
        Optional<UserProfile> found = repository.findByDisplayName("TestUser");
        
        assertTrue(found.isPresent());
        assertEquals(testUser.getProfileId(), found.get().getProfileId());
    }

    @Test
    @DisplayName("Should return empty when display name not found")
    void testFindByDisplayNameNotFound() {
        Optional<UserProfile> found = repository.findByDisplayName("NonExistent");
        
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Should find users by fitness level")
    void testFindByFitnessLevel() {
        List<UserProfile> users = repository.findByFitnessLevel(UserProfile.FitnessLevel.INTERMEDIATE);
        
        assertFalse(users.isEmpty());
        assertEquals(UserProfile.FitnessLevel.INTERMEDIATE, users.get(0).getFitnessLevel());
    }

    @Test
    @DisplayName("Should find users by primary goal")
    void testFindByPrimaryGoal() {
        List<UserProfile> users = repository.findByPrimaryGoal(UserProfile.PrimaryGoal.STRENGTH);
        
        assertFalse(users.isEmpty());
        assertEquals(UserProfile.PrimaryGoal.STRENGTH, users.get(0).getPrimaryGoal());
    }

    @Test
    @DisplayName("Should check if display name exists")
    void testExistsByDisplayName() {
        assertTrue(repository.existsByDisplayName("TestUser"));
        assertFalse(repository.existsByDisplayName("NonExistent"));
    }

    @Test
    @DisplayName("Should find users with high streak")
    void testFindByCurrentStreakGreaterThanEqual() {
        testUser.setCurrentStreak(7);
        repository.save(testUser);
        
        List<UserProfile> users = repository.findByCurrentStreakGreaterThanEqual(5);
        
        assertFalse(users.isEmpty());
        assertTrue(users.get(0).getCurrentStreak() >= 5);
    }

    @Test
    @DisplayName("Should delete user by ID")
    void testDeleteById() {
        Long userId = testUser.getProfileId();
        
        repository.deleteById(userId);
        
        Optional<UserProfile> found = repository.findById(userId);
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Should count all users")
    void testCount() {
        long count = repository.count();
        
        assertEquals(1, count);
    }
}