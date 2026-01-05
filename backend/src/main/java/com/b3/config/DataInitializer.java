package com.b3.config;

import com.b3.model.UserProfile;
import com.b3.model.Workout;
import com.b3.repository.UserProfileRepository;
import com.b3.repository.WorkoutRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Seeds the database with demo data on application startup.
 * Creates a default user and sample workouts for demo purposes.
 */
@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner initDatabase(UserProfileRepository userRepo, WorkoutRepository workoutRepo) {
        return args -> {
            // Create default user if not exists
            if (userRepo.count() == 0) {
                logger.info(" Initializing demo data...");
                
                UserProfile user = new UserProfile();
                user.setDisplayName("Demo User");
                user.setAge(28);
                user.setFitnessLevel(UserProfile.FitnessLevel.INTERMEDIATE);
                user.setPrimaryGoal(UserProfile.PrimaryGoal.STRENGTH);
                user.setWeeklyGoalDays(4);
                user.setEquipment("Dumbbells, Pull-up Bar, Resistance Bands");
                userRepo.save(user);
                
                logger.info(" Created demo user with ID: {}", user.getProfileId());
            }

            // Create sample workouts if none exist
            if (workoutRepo.count() == 0) {
                // Workout 1: Upper Body Strength
                Workout workout1 = new Workout();
                workout1.setName("Upper Body Blast");
                workout1.setDescription("Build strength in your chest, back, shoulders and arms");
                workout1.setWorkoutType(Workout.WorkoutType.STRENGTH);
                workout1.setDifficultyLevel(Workout.DifficultyLevel.INTERMEDIATE);
                workout1.setEstimatedDuration(45);
                workout1.setRequiredEquipment("Dumbbells, Pull-up Bar");
                workoutRepo.save(workout1);

                // Workout 2: HIIT Cardio
                Workout workout2 = new Workout();
                workout2.setName("HIIT Cardio Burn");
                workout2.setDescription("High intensity intervals to torch calories");
                workout2.setWorkoutType(Workout.WorkoutType.CARDIO);
                workout2.setDifficultyLevel(Workout.DifficultyLevel.ADVANCED);
                workout2.setEstimatedDuration(30);
                workout2.setRequiredEquipment("None");
                workoutRepo.save(workout2);

                // Workout 3: Beginner Full Body
                Workout workout3 = new Workout();
                workout3.setName("Beginner Full Body");
                workout3.setDescription("Perfect starting point for fitness beginners");
                workout3.setWorkoutType(Workout.WorkoutType.STRENGTH);
                workout3.setDifficultyLevel(Workout.DifficultyLevel.BEGINNER);
                workout3.setEstimatedDuration(30);
                workout3.setRequiredEquipment("None");
                workoutRepo.save(workout3);

                // Workout 4: Yoga Flow
                Workout workout4 = new Workout();
                workout4.setName("Flexibility Flow");
                workout4.setDescription("Improve flexibility and reduce stress");
                workout4.setWorkoutType(Workout.WorkoutType.FLEXIBILITY);
                workout4.setDifficultyLevel(Workout.DifficultyLevel.BEGINNER);
                workout4.setEstimatedDuration(40);
                workout4.setRequiredEquipment("Yoga Mat");
                workoutRepo.save(workout4);

                // Workout 5: Core Crusher
                Workout workout5 = new Workout();
                workout5.setName("Core Crusher");
                workout5.setDescription("Strengthen your abs, obliques, and lower back");
                workout5.setWorkoutType(Workout.WorkoutType.STRENGTH);
                workout5.setDifficultyLevel(Workout.DifficultyLevel.INTERMEDIATE);
                workout5.setEstimatedDuration(25);
                workout5.setRequiredEquipment("None");
                workoutRepo.save(workout5);

                logger.info(" Created {} sample workouts", workoutRepo.count());
            }

            logger.info(" B3 Backend ready for demo!");
        };
    }
}