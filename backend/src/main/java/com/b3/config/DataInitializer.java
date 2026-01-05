package com.b3.config;

import com.b3.model.Exercise;
import com.b3.model.UserProfile;
import com.b3.model.Workout;
import com.b3.model.WorkoutExercise;
import com.b3.repository.ExerciseRepository;
import com.b3.repository.UserProfileRepository;
import com.b3.repository.WorkoutExerciseRepository;
import com.b3.repository.WorkoutRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Seeds the database with demo data on application startup.
 * Creates a default user, sample exercises, and workouts for demo purposes.
 */
@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner initDatabase(
            UserProfileRepository userRepo,
            WorkoutRepository workoutRepo,
            ExerciseRepository exerciseRepo,
            WorkoutExerciseRepository workoutExerciseRepo) {
        
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

            // Create exercises if none exist
            if (exerciseRepo.count() == 0) {
                // Chest exercises
                Exercise pushUp = createExercise(exerciseRepo, "Push-Up", 
                    "Classic bodyweight chest exercise", 
                    Exercise.MuscleGroup.CHEST, Exercise.EquipmentType.BODYWEIGHT);
                
                Exercise dumbellPress = createExercise(exerciseRepo, "Dumbbell Bench Press",
                    "Chest press with dumbbells",
                    Exercise.MuscleGroup.CHEST, Exercise.EquipmentType.DUMBBELLS);
                
                Exercise chestFly = createExercise(exerciseRepo, "Dumbbell Chest Fly",
                    "Isolation exercise for chest",
                    Exercise.MuscleGroup.CHEST, Exercise.EquipmentType.DUMBBELLS);

                // Back exercises
                Exercise pullUp = createExercise(exerciseRepo, "Pull-Up",
                    "Classic back and bicep exercise",
                    Exercise.MuscleGroup.BACK, Exercise.EquipmentType.BODYWEIGHT);
                
                Exercise dumbbellRow = createExercise(exerciseRepo, "Dumbbell Row",
                    "Single arm rowing movement",
                    Exercise.MuscleGroup.BACK, Exercise.EquipmentType.DUMBBELLS);

                // Shoulder exercises
                Exercise shoulderPress = createExercise(exerciseRepo, "Shoulder Press",
                    "Overhead pressing movement",
                    Exercise.MuscleGroup.SHOULDERS, Exercise.EquipmentType.DUMBBELLS);
                
                Exercise lateralRaise = createExercise(exerciseRepo, "Lateral Raise",
                    "Side delt isolation",
                    Exercise.MuscleGroup.SHOULDERS, Exercise.EquipmentType.DUMBBELLS);

                // Arm exercises
                Exercise bicepCurl = createExercise(exerciseRepo, "Bicep Curl",
                    "Classic arm curl",
                    Exercise.MuscleGroup.BICEPS, Exercise.EquipmentType.DUMBBELLS);
                
                Exercise tricepDip = createExercise(exerciseRepo, "Tricep Dip",
                    "Bodyweight tricep exercise",
                    Exercise.MuscleGroup.TRICEPS, Exercise.EquipmentType.BODYWEIGHT);

                // Leg exercises
                Exercise squat = createExercise(exerciseRepo, "Bodyweight Squat",
                    "Fundamental leg exercise",
                    Exercise.MuscleGroup.LEGS, Exercise.EquipmentType.BODYWEIGHT);
                
                Exercise lunge = createExercise(exerciseRepo, "Walking Lunge",
                    "Unilateral leg exercise",
                    Exercise.MuscleGroup.LEGS, Exercise.EquipmentType.BODYWEIGHT);
                
                Exercise calfRaise = createExercise(exerciseRepo, "Calf Raise",
                    "Lower leg exercise",
                    Exercise.MuscleGroup.LEGS, Exercise.EquipmentType.BODYWEIGHT);

                // Core exercises
                Exercise plank = createExercise(exerciseRepo, "Plank",
                    "Isometric core hold",
                    Exercise.MuscleGroup.CORE, Exercise.EquipmentType.BODYWEIGHT);
                
                Exercise crunch = createExercise(exerciseRepo, "Crunch",
                    "Abdominal exercise",
                    Exercise.MuscleGroup.CORE, Exercise.EquipmentType.BODYWEIGHT);
                
                Exercise mountainClimber = createExercise(exerciseRepo, "Mountain Climber",
                    "Dynamic core and cardio",
                    Exercise.MuscleGroup.CORE, Exercise.EquipmentType.BODYWEIGHT);

                // Full body
                Exercise burpee = createExercise(exerciseRepo, "Burpee",
                    "Full body cardio exercise",
                    Exercise.MuscleGroup.FULL_BODY, Exercise.EquipmentType.BODYWEIGHT);
                
                Exercise jumpingJack = createExercise(exerciseRepo, "Jumping Jack",
                    "Cardio warmup exercise",
                    Exercise.MuscleGroup.FULL_BODY, Exercise.EquipmentType.BODYWEIGHT);

                logger.info(" Created {} exercises", exerciseRepo.count());

                // Create workouts with exercises
                if (workoutRepo.count() == 0) {
                    // Workout 1: Upper Body Blast
                    Workout workout1 = new Workout();
                    workout1.setName("Upper Body Blast");
                    workout1.setDescription("Build strength in your chest, back, shoulders and arms");
                    workout1.setWorkoutType(Workout.WorkoutType.STRENGTH);
                    workout1.setDifficultyLevel(Workout.DifficultyLevel.INTERMEDIATE);
                    workout1.setEstimatedDuration(45);
                    workout1.setRequiredEquipment("Dumbbells, Pull-up Bar");
                    workoutRepo.save(workout1);
                    
                    addExerciseToWorkout(workoutExerciseRepo, workout1, pushUp, 1, 3, 15, null, 60, null);
                    addExerciseToWorkout(workoutExerciseRepo, workout1, dumbellPress, 2, 4, 10, null, 90, null);
                    addExerciseToWorkout(workoutExerciseRepo, workout1, pullUp, 3, 3, 8, null, 90, "Use assisted if needed");
                    addExerciseToWorkout(workoutExerciseRepo, workout1, dumbbellRow, 4, 3, 12, null, 60, null);
                    addExerciseToWorkout(workoutExerciseRepo, workout1, shoulderPress, 5, 3, 10, null, 60, null);
                    addExerciseToWorkout(workoutExerciseRepo, workout1, bicepCurl, 6, 3, 12, null, 45, null);
                    addExerciseToWorkout(workoutExerciseRepo, workout1, tricepDip, 7, 3, 12, null, 45, null);

                    // Workout 2: HIIT Cardio Burn
                    Workout workout2 = new Workout();
                    workout2.setName("HIIT Cardio Burn");
                    workout2.setDescription("High intensity intervals to torch calories");
                    workout2.setWorkoutType(Workout.WorkoutType.CARDIO);
                    workout2.setDifficultyLevel(Workout.DifficultyLevel.ADVANCED);
                    workout2.setEstimatedDuration(30);
                    workout2.setRequiredEquipment("None");
                    workoutRepo.save(workout2);
                    
                    addExerciseToWorkout(workoutExerciseRepo, workout2, jumpingJack, 1, 1, null, 60, 15, "Warmup");
                    addExerciseToWorkout(workoutExerciseRepo, workout2, burpee, 2, 4, 10, null, 30, "All out effort");
                    addExerciseToWorkout(workoutExerciseRepo, workout2, mountainClimber, 3, 4, null, 30, 30, null);
                    addExerciseToWorkout(workoutExerciseRepo, workout2, squat, 4, 4, 20, null, 30, "Fast pace");
                    addExerciseToWorkout(workoutExerciseRepo, workout2, pushUp, 5, 4, 15, null, 30, null);
                    addExerciseToWorkout(workoutExerciseRepo, workout2, plank, 6, 3, null, 45, 15, "Hold strong");

                    // Workout 3: Beginner Full Body
                    Workout workout3 = new Workout();
                    workout3.setName("Beginner Full Body");
                    workout3.setDescription("Perfect starting point for fitness beginners");
                    workout3.setWorkoutType(Workout.WorkoutType.STRENGTH);
                    workout3.setDifficultyLevel(Workout.DifficultyLevel.BEGINNER);
                    workout3.setEstimatedDuration(30);
                    workout3.setRequiredEquipment("None");
                    workoutRepo.save(workout3);
                    
                    addExerciseToWorkout(workoutExerciseRepo, workout3, jumpingJack, 1, 1, null, 60, 30, "Warmup");
                    addExerciseToWorkout(workoutExerciseRepo, workout3, squat, 2, 3, 10, null, 60, "Focus on form");
                    addExerciseToWorkout(workoutExerciseRepo, workout3, pushUp, 3, 3, 8, null, 60, "Knee pushups OK");
                    addExerciseToWorkout(workoutExerciseRepo, workout3, lunge, 4, 2, 8, null, 60, "Each leg");
                    addExerciseToWorkout(workoutExerciseRepo, workout3, plank, 5, 3, null, 20, 45, null);

                    // Workout 4: Flexibility Flow
                    Workout workout4 = new Workout();
                    workout4.setName("Flexibility Flow");
                    workout4.setDescription("Improve flexibility and reduce stress");
                    workout4.setWorkoutType(Workout.WorkoutType.FLEXIBILITY);
                    workout4.setDifficultyLevel(Workout.DifficultyLevel.BEGINNER);
                    workout4.setEstimatedDuration(40);
                    workout4.setRequiredEquipment("Yoga Mat");
                    workoutRepo.save(workout4);
                    
                    addExerciseToWorkout(workoutExerciseRepo, workout4, plank, 1, 2, null, 30, 30, "Engage core");

                    // Workout 5: Core Crusher
                    Workout workout5 = new Workout();
                    workout5.setName("Core Crusher");
                    workout5.setDescription("Strengthen your abs, obliques, and lower back");
                    workout5.setWorkoutType(Workout.WorkoutType.STRENGTH);
                    workout5.setDifficultyLevel(Workout.DifficultyLevel.INTERMEDIATE);
                    workout5.setEstimatedDuration(25);
                    workout5.setRequiredEquipment("None");
                    workoutRepo.save(workout5);
                    
                    addExerciseToWorkout(workoutExerciseRepo, workout5, plank, 1, 3, null, 45, 30, null);
                    addExerciseToWorkout(workoutExerciseRepo, workout5, crunch, 2, 3, 20, null, 30, null);
                    addExerciseToWorkout(workoutExerciseRepo, workout5, mountainClimber, 3, 3, null, 30, 30, null);

                    logger.info(" Created {} workouts with exercises", workoutRepo.count());
                }
            }

            logger.info(" B3 Backend ready for demo!");
        };
    }

    private Exercise createExercise(ExerciseRepository repo, String name, String description,
                                     Exercise.MuscleGroup muscleGroup, Exercise.EquipmentType equipmentType) {
        Exercise exercise = new Exercise();
        exercise.setName(name);
        exercise.setDescription(description);
        exercise.setMuscleGroup(muscleGroup);
        exercise.setEquipmentType(equipmentType);
        return repo.save(exercise);
    }

    private void addExerciseToWorkout(WorkoutExerciseRepository repo, Workout workout, Exercise exercise,
                                       int orderIndex, Integer sets, Integer reps, Integer durationSeconds,
                                       Integer restSeconds, String notes) {
        WorkoutExercise we = new WorkoutExercise();
        we.setWorkout(workout);
        we.setExercise(exercise);
        we.setOrderIndex(orderIndex);
        we.setSets(sets);
        we.setReps(reps);
        we.setDurationSeconds(durationSeconds);
        we.setRestSeconds(restSeconds);
        we.setNotes(notes);
        repo.save(we);
    }
}