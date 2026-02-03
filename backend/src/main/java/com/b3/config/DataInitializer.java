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
                user.setDisplayName("Marcus Johnson");
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
                // Exercise images from Unsplash (free for commercial use)
                // Using specific fitness-related photos for each exercise type

                // ================================================================
                // CHEST EXERCISES
                // ================================================================
                Exercise pushUp = createExercise(exerciseRepo, "Push-Up",
                    "Classic bodyweight chest exercise. Keep your core tight and lower your chest to the ground, then push back up.",
                    Exercise.MuscleGroup.CHEST, Exercise.EquipmentType.BODYWEIGHT, null,
                    null);

                Exercise dumbellPress = createExercise(exerciseRepo, "Dumbbell Bench Press",
                    "Chest press with dumbbells. Lie on a bench, press dumbbells up from chest level.",
                    Exercise.MuscleGroup.CHEST, Exercise.EquipmentType.DUMBBELLS, null,
                    null);

                Exercise chestFly = createExercise(exerciseRepo, "Dumbbell Chest Fly",
                    "Isolation exercise for chest. With arms slightly bent, lower dumbbells out to sides.",
                    Exercise.MuscleGroup.CHEST, Exercise.EquipmentType.DUMBBELLS, null,
                    null);

                // ================================================================
                // BACK EXERCISES
                // ================================================================
                Exercise pullUp = createExercise(exerciseRepo, "Pull-Up",
                    "Classic back and bicep exercise. Hang from bar and pull yourself up until chin is over bar.",
                    Exercise.MuscleGroup.BACK, Exercise.EquipmentType.PULL_UP_BAR, null,
                    null);

                Exercise dumbbellRow = createExercise(exerciseRepo, "Dumbbell Row",
                    "Single arm rowing movement. Brace on bench, pull dumbbell to hip.",
                    Exercise.MuscleGroup.BACK, Exercise.EquipmentType.DUMBBELLS, null,
                    null);

                Exercise superman = createExercise(exerciseRepo, "Superman",
                    "Lie face down, lift arms and legs simultaneously off the ground. Hold briefly, lower with control.",
                    Exercise.MuscleGroup.BACK, Exercise.EquipmentType.BODYWEIGHT, null,
                    null);

                // ================================================================
                // SHOULDER EXERCISES
                // ================================================================
                Exercise shoulderPress = createExercise(exerciseRepo, "Shoulder Press",
                    "Overhead pressing movement. Press dumbbells from shoulders to overhead.",
                    Exercise.MuscleGroup.SHOULDERS, Exercise.EquipmentType.DUMBBELLS, null,
                    null);

                Exercise lateralRaise = createExercise(exerciseRepo, "Lateral Raise",
                    "Side delt isolation. Raise dumbbells out to sides with slight bend in elbows.",
                    Exercise.MuscleGroup.SHOULDERS, Exercise.EquipmentType.DUMBBELLS, null,
                    null);

                // ================================================================
                // ARM EXERCISES
                // ================================================================
                Exercise bicepCurl = createExercise(exerciseRepo, "Bicep Curl",
                    "Classic arm curl. Keep elbows at sides, curl dumbbells to shoulders.",
                    Exercise.MuscleGroup.BICEPS, Exercise.EquipmentType.DUMBBELLS, null,
                    null);

                Exercise tricepDip = createExercise(exerciseRepo, "Tricep Dip",
                    "Bodyweight tricep exercise. Lower body by bending elbows, push back up.",
                    Exercise.MuscleGroup.TRICEPS, Exercise.EquipmentType.BODYWEIGHT, null,
                    null);

                // ================================================================
                // LEG EXERCISES
                // ================================================================
                Exercise squat = createExercise(exerciseRepo, "Bodyweight Squat",
                    "Fundamental leg exercise. Lower hips back and down, keeping chest up.",
                    Exercise.MuscleGroup.LEGS, Exercise.EquipmentType.BODYWEIGHT, null,
                    null);

                Exercise lunge = createExercise(exerciseRepo, "Walking Lunge",
                    "Unilateral leg exercise. Step forward into lunge, alternate legs.",
                    Exercise.MuscleGroup.LEGS, Exercise.EquipmentType.BODYWEIGHT, null,
                    null);

                Exercise calfRaise = createExercise(exerciseRepo, "Calf Raise",
                    "Lower leg exercise. Rise up on toes, lower with control.",
                    Exercise.MuscleGroup.LEGS, Exercise.EquipmentType.BODYWEIGHT, null,
                    null);

                Exercise gluteBridge = createExercise(exerciseRepo, "Glute Bridge",
                    "Lie on back, knees bent, feet flat. Drive hips up by squeezing glutes, lower with control.",
                    Exercise.MuscleGroup.GLUTES, Exercise.EquipmentType.BODYWEIGHT, null,
                    null);

                Exercise wallSit = createExercise(exerciseRepo, "Wall Sit",
                    "Isometric leg exercise. Lean against wall with thighs parallel to ground, hold position.",
                    Exercise.MuscleGroup.QUADS, Exercise.EquipmentType.BODYWEIGHT, null,
                    null);

                Exercise boxJump = createExercise(exerciseRepo, "Box Jump",
                    "Explosive plyometric exercise. Jump onto elevated surface, step down, repeat.",
                    Exercise.MuscleGroup.LEGS, Exercise.EquipmentType.OTHER, null,
                    null);

                // ================================================================
                // CORE EXERCISES
                // ================================================================
                Exercise plank = createExercise(exerciseRepo, "Plank",
                    "Isometric core hold. Hold body in straight line from head to heels.",
                    Exercise.MuscleGroup.CORE, Exercise.EquipmentType.BODYWEIGHT, null,
                    null);

                Exercise crunch = createExercise(exerciseRepo, "Crunch",
                    "Abdominal exercise. Curl shoulders off ground, squeezing abs.",
                    Exercise.MuscleGroup.CORE, Exercise.EquipmentType.BODYWEIGHT, null,
                    null);

                Exercise mountainClimber = createExercise(exerciseRepo, "Mountain Climber",
                    "Dynamic core and cardio. In plank position, drive knees to chest alternately.",
                    Exercise.MuscleGroup.CORE, Exercise.EquipmentType.BODYWEIGHT, null,
                    null);

                Exercise deadBug = createExercise(exerciseRepo, "Dead Bug",
                    "Core stability exercise. Lie on back, extend opposite arm and leg while keeping core engaged.",
                    Exercise.MuscleGroup.CORE, Exercise.EquipmentType.BODYWEIGHT, null,
                    null);

                Exercise birdDog = createExercise(exerciseRepo, "Bird Dog",
                    "Core and balance exercise. On all fours, extend opposite arm and leg, hold, then switch.",
                    Exercise.MuscleGroup.CORE, Exercise.EquipmentType.BODYWEIGHT, null,
                    null);

                Exercise russianTwist = createExercise(exerciseRepo, "Russian Twist",
                    "Rotational core exercise. Seated with feet elevated, rotate torso side to side.",
                    Exercise.MuscleGroup.CORE, Exercise.EquipmentType.BODYWEIGHT, null,
                    null);

                // ================================================================
                // FULL BODY / CARDIO EXERCISES
                // ================================================================
                Exercise burpee = createExercise(exerciseRepo, "Burpee",
                    "Full body cardio exercise. Drop to pushup, jump feet in, jump up with arms overhead.",
                    Exercise.MuscleGroup.FULL_BODY, Exercise.EquipmentType.BODYWEIGHT, null,
                    null);

                Exercise jumpingJack = createExercise(exerciseRepo, "Jumping Jack",
                    "Cardio warmup exercise. Jump feet out while raising arms overhead.",
                    Exercise.MuscleGroup.FULL_BODY, Exercise.EquipmentType.BODYWEIGHT, null,
                    null);

                Exercise highKnees = createExercise(exerciseRepo, "High Knees",
                    "Cardio exercise. Run in place bringing knees up to hip height with quick foot turnover.",
                    Exercise.MuscleGroup.FULL_BODY, Exercise.EquipmentType.BODYWEIGHT, null,
                    null);

                // ================================================================
                // RUNNING / WALKING EXERCISES
                // ================================================================
                Exercise easyRun = createExercise(exerciseRepo, "Easy Run",
                    "Low intensity steady-state running. Maintain conversational pace for aerobic base building.",
                    Exercise.MuscleGroup.FULL_BODY, Exercise.EquipmentType.BODYWEIGHT, null,
                    null);

                Exercise intervalSprints = createExercise(exerciseRepo, "Interval Sprints",
                    "High intensity running intervals. Sprint for 30 seconds, recover with walking or light jog.",
                    Exercise.MuscleGroup.FULL_BODY, Exercise.EquipmentType.BODYWEIGHT, null,
                    null);

                Exercise briskWalk = createExercise(exerciseRepo, "Brisk Walk",
                    "Moderate intensity walking. Walk at a pace that elevates heart rate while still allowing conversation.",
                    Exercise.MuscleGroup.FULL_BODY, Exercise.EquipmentType.BODYWEIGHT, null,
                    null);

                // ================================================================
                // YOGA EXERCISES
                // ================================================================
                Exercise downwardDog = createExercise(exerciseRepo, "Downward Dog",
                    "Foundational yoga pose. Form inverted V-shape, pressing hands and heels toward floor.",
                    Exercise.MuscleGroup.FULL_BODY, Exercise.EquipmentType.YOGA_MAT, null,
                    null);

                Exercise warriorOne = createExercise(exerciseRepo, "Warrior I",
                    "Standing yoga pose. Front knee bent, back leg straight, arms reaching overhead.",
                    Exercise.MuscleGroup.LEGS, Exercise.EquipmentType.YOGA_MAT, null,
                    null);

                Exercise warriorTwo = createExercise(exerciseRepo, "Warrior II",
                    "Standing yoga pose. Legs wide, front knee bent, arms extended parallel to floor.",
                    Exercise.MuscleGroup.LEGS, Exercise.EquipmentType.YOGA_MAT, null,
                    null);

                Exercise childsPose = createExercise(exerciseRepo, "Child's Pose",
                    "Restorative yoga pose. Kneel and fold forward, arms extended or by sides, forehead to mat.",
                    Exercise.MuscleGroup.FULL_BODY, Exercise.EquipmentType.YOGA_MAT, null,
                    null);

                Exercise catCowStretch = createExercise(exerciseRepo, "Cat-Cow Stretch",
                    "Spinal mobility flow. On all fours, alternate between arching and rounding the spine.",
                    Exercise.MuscleGroup.BACK, Exercise.EquipmentType.YOGA_MAT, null,
                    null);

                Exercise cobraPose = createExercise(exerciseRepo, "Cobra Pose",
                    "Back extension yoga pose. Lie face down, press hands to lift chest while keeping hips grounded.",
                    Exercise.MuscleGroup.BACK, Exercise.EquipmentType.YOGA_MAT, null,
                    null);

                Exercise treePose = createExercise(exerciseRepo, "Tree Pose",
                    "Balance yoga pose. Stand on one leg, place other foot on inner thigh, hands at heart or overhead.",
                    Exercise.MuscleGroup.LEGS, Exercise.EquipmentType.YOGA_MAT, null,
                    null);

                Exercise seatedForwardFold = createExercise(exerciseRepo, "Seated Forward Fold",
                    "Hamstring and back stretch. Sit with legs extended, fold forward reaching toward feet.",
                    Exercise.MuscleGroup.HAMSTRINGS, Exercise.EquipmentType.YOGA_MAT, null,
                    null);

                // ================================================================
                // STRETCHING EXERCISES
                // ================================================================
                Exercise standingQuadStretch = createExercise(exerciseRepo, "Standing Quad Stretch",
                    "Quadriceps stretch. Stand on one leg, pull other heel toward glutes, keep knees together.",
                    Exercise.MuscleGroup.QUADS, Exercise.EquipmentType.BODYWEIGHT, null,
                    null);

                Exercise standingHamstringStretch = createExercise(exerciseRepo, "Standing Hamstring Stretch",
                    "Hamstring stretch. Place heel on elevated surface, hinge at hips reaching toward toes.",
                    Exercise.MuscleGroup.HAMSTRINGS, Exercise.EquipmentType.BODYWEIGHT, null,
                    null);

                Exercise hipFlexorStretch = createExercise(exerciseRepo, "Hip Flexor Stretch",
                    "Deep stretch for hip flexors. Lunge position with back knee down, push hips forward gently.",
                    Exercise.MuscleGroup.LEGS, Exercise.EquipmentType.YOGA_MAT, null,
                    null);

                Exercise chestOpenerStretch = createExercise(exerciseRepo, "Chest Opener Stretch",
                    "Pectoralis stretch. Clasp hands behind back, squeeze shoulder blades, lift chest.",
                    Exercise.MuscleGroup.CHEST, Exercise.EquipmentType.BODYWEIGHT, null,
                    null);

                Exercise shoulderCrossBodyStretch = createExercise(exerciseRepo, "Shoulder Cross-Body Stretch",
                    "Shoulder stretch. Pull one arm across body at shoulder height, hold with other hand.",
                    Exercise.MuscleGroup.SHOULDERS, Exercise.EquipmentType.BODYWEIGHT, null,
                    null);

                Exercise tricepOverheadStretch = createExercise(exerciseRepo, "Tricep Overhead Stretch",
                    "Triceps stretch. Reach one arm overhead, bend elbow, use other hand to gently push elbow.",
                    Exercise.MuscleGroup.TRICEPS, Exercise.EquipmentType.BODYWEIGHT, null,
                    null);

                Exercise neckRolls = createExercise(exerciseRepo, "Neck Rolls",
                    "Neck mobility exercise. Slowly roll head in circles, releasing tension in neck muscles.",
                    Exercise.MuscleGroup.FULL_BODY, Exercise.EquipmentType.BODYWEIGHT, null,
                    null);

                Exercise pigeonPose = createExercise(exerciseRepo, "Pigeon Pose",
                    "Deep hip opener. One leg bent in front, other extended behind. Fold forward for deeper stretch.",
                    Exercise.MuscleGroup.GLUTES, Exercise.EquipmentType.YOGA_MAT, null,
                    null);

                // ================================================================
                // FOAM ROLLER EXERCISES
                // ================================================================
                Exercise foamRollerITBand = createExercise(exerciseRepo, "Foam Roller IT Band",
                    "Self-myofascial release for IT band. Lie on side, roll from hip to knee on outer thigh.",
                    Exercise.MuscleGroup.LEGS, Exercise.EquipmentType.FOAM_ROLLER, null,
                    null);

                Exercise foamRollerBack = createExercise(exerciseRepo, "Foam Roller Back",
                    "Upper back release. Lie on roller at mid-back, roll from shoulders to mid-back with control.",
                    Exercise.MuscleGroup.BACK, Exercise.EquipmentType.FOAM_ROLLER, null,
                    null);

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
                    workout4.setDescription("Improve flexibility and reduce stress with gentle stretches");
                    workout4.setWorkoutType(Workout.WorkoutType.FLEXIBILITY);
                    workout4.setDifficultyLevel(Workout.DifficultyLevel.BEGINNER);
                    workout4.setEstimatedDuration(40);
                    workout4.setRequiredEquipment("Yoga Mat");
                    workoutRepo.save(workout4);

                    addExerciseToWorkout(workoutExerciseRepo, workout4, catCowStretch, 1, 2, 10, null, 15, "Sync with breath");
                    addExerciseToWorkout(workoutExerciseRepo, workout4, downwardDog, 2, 2, null, 30, 15, "Pedal feet");
                    addExerciseToWorkout(workoutExerciseRepo, workout4, childsPose, 3, 2, null, 45, 15, "Rest and breathe");
                    addExerciseToWorkout(workoutExerciseRepo, workout4, standingHamstringStretch, 4, 2, null, 30, 15, "Each leg");
                    addExerciseToWorkout(workoutExerciseRepo, workout4, plank, 5, 2, null, 30, 30, "Engage core");

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
                    addExerciseToWorkout(workoutExerciseRepo, workout5, deadBug, 4, 3, 10, null, 30, "Each side");
                    addExerciseToWorkout(workoutExerciseRepo, workout5, russianTwist, 5, 3, 20, null, 30, null);

                    // ================================================================
                    // NEW WORKOUTS
                    // ================================================================

                    // Workout 6: Morning Yoga Flow
                    Workout workout6 = new Workout();
                    workout6.setName("Morning Yoga Flow");
                    workout6.setDescription("Gentle yoga sequence to energize your morning and improve flexibility");
                    workout6.setWorkoutType(Workout.WorkoutType.FLEXIBILITY);
                    workout6.setDifficultyLevel(Workout.DifficultyLevel.BEGINNER);
                    workout6.setEstimatedDuration(20);
                    workout6.setRequiredEquipment("Yoga Mat");
                    workoutRepo.save(workout6);

                    addExerciseToWorkout(workoutExerciseRepo, workout6, catCowStretch, 1, 2, 8, null, 10, "Gentle warmup");
                    addExerciseToWorkout(workoutExerciseRepo, workout6, downwardDog, 2, 2, null, 30, 15, null);
                    addExerciseToWorkout(workoutExerciseRepo, workout6, warriorOne, 3, 2, null, 30, 10, "Each side");
                    addExerciseToWorkout(workoutExerciseRepo, workout6, warriorTwo, 4, 2, null, 30, 10, "Each side");
                    addExerciseToWorkout(workoutExerciseRepo, workout6, treePose, 5, 2, null, 20, 10, "Each leg");
                    addExerciseToWorkout(workoutExerciseRepo, workout6, childsPose, 6, 1, null, 60, 0, "Rest and recover");

                    // Workout 7: Deep Stretch Yoga
                    Workout workout7 = new Workout();
                    workout7.setName("Deep Stretch Yoga");
                    workout7.setDescription("Intermediate yoga session with deeper stretches and longer holds");
                    workout7.setWorkoutType(Workout.WorkoutType.FLEXIBILITY);
                    workout7.setDifficultyLevel(Workout.DifficultyLevel.INTERMEDIATE);
                    workout7.setEstimatedDuration(35);
                    workout7.setRequiredEquipment("Yoga Mat");
                    workoutRepo.save(workout7);

                    addExerciseToWorkout(workoutExerciseRepo, workout7, catCowStretch, 1, 3, 10, null, 15, null);
                    addExerciseToWorkout(workoutExerciseRepo, workout7, downwardDog, 2, 3, null, 45, 20, null);
                    addExerciseToWorkout(workoutExerciseRepo, workout7, warriorOne, 3, 3, null, 45, 15, "Each side");
                    addExerciseToWorkout(workoutExerciseRepo, workout7, warriorTwo, 4, 3, null, 45, 15, "Each side");
                    addExerciseToWorkout(workoutExerciseRepo, workout7, pigeonPose, 5, 2, null, 60, 20, "Each side");
                    addExerciseToWorkout(workoutExerciseRepo, workout7, seatedForwardFold, 6, 2, null, 45, 15, null);
                    addExerciseToWorkout(workoutExerciseRepo, workout7, cobraPose, 7, 2, null, 30, 15, null);
                    addExerciseToWorkout(workoutExerciseRepo, workout7, childsPose, 8, 1, null, 90, 0, "Final rest");

                    // Workout 8: Couch to 5K Starter
                    Workout workout8 = new Workout();
                    workout8.setName("Couch to 5K Starter");
                    workout8.setDescription("Walk/run intervals for beginners building cardio endurance");
                    workout8.setWorkoutType(Workout.WorkoutType.CARDIO);
                    workout8.setDifficultyLevel(Workout.DifficultyLevel.BEGINNER);
                    workout8.setEstimatedDuration(25);
                    workout8.setRequiredEquipment("None");
                    workoutRepo.save(workout8);

                    addExerciseToWorkout(workoutExerciseRepo, workout8, briskWalk, 1, 1, null, 300, 0, "5 min warmup walk");
                    addExerciseToWorkout(workoutExerciseRepo, workout8, easyRun, 2, 8, null, 60, 90, "Run 1 min, walk 90 sec");
                    addExerciseToWorkout(workoutExerciseRepo, workout8, briskWalk, 3, 1, null, 300, 0, "5 min cooldown walk");

                    // Workout 9: HIIT Running Intervals
                    Workout workout9 = new Workout();
                    workout9.setName("HIIT Running Intervals");
                    workout9.setDescription("High intensity sprint intervals for advanced cardiovascular conditioning");
                    workout9.setWorkoutType(Workout.WorkoutType.CARDIO);
                    workout9.setDifficultyLevel(Workout.DifficultyLevel.ADVANCED);
                    workout9.setEstimatedDuration(30);
                    workout9.setRequiredEquipment("None");
                    workoutRepo.save(workout9);

                    addExerciseToWorkout(workoutExerciseRepo, workout9, easyRun, 1, 1, null, 300, 0, "5 min warmup jog");
                    addExerciseToWorkout(workoutExerciseRepo, workout9, intervalSprints, 2, 8, null, 30, 60, "30 sec sprint, 60 sec recover");
                    addExerciseToWorkout(workoutExerciseRepo, workout9, easyRun, 3, 1, null, 300, 0, "5 min cooldown jog");
                    addExerciseToWorkout(workoutExerciseRepo, workout9, standingQuadStretch, 4, 2, null, 30, 10, "Each leg");

                    // Workout 10: Active Recovery Walk
                    Workout workout10 = new Workout();
                    workout10.setName("Active Recovery Walk");
                    workout10.setDescription("Low intensity walking session with light stretching for recovery days");
                    workout10.setWorkoutType(Workout.WorkoutType.CARDIO);
                    workout10.setDifficultyLevel(Workout.DifficultyLevel.BEGINNER);
                    workout10.setEstimatedDuration(30);
                    workout10.setRequiredEquipment("None");
                    workoutRepo.save(workout10);

                    addExerciseToWorkout(workoutExerciseRepo, workout10, briskWalk, 1, 1, null, 1200, 60, "20 min steady walk");
                    addExerciseToWorkout(workoutExerciseRepo, workout10, standingQuadStretch, 2, 2, null, 30, 10, "Each leg");
                    addExerciseToWorkout(workoutExerciseRepo, workout10, standingHamstringStretch, 3, 2, null, 30, 10, "Each leg");
                    addExerciseToWorkout(workoutExerciseRepo, workout10, shoulderCrossBodyStretch, 4, 2, null, 20, 10, "Each arm");

                    // Workout 11: Full Body Stretch
                    Workout workout11 = new Workout();
                    workout11.setName("Full Body Stretch");
                    workout11.setDescription("Quick stretching routine targeting all major muscle groups");
                    workout11.setWorkoutType(Workout.WorkoutType.FLEXIBILITY);
                    workout11.setDifficultyLevel(Workout.DifficultyLevel.BEGINNER);
                    workout11.setEstimatedDuration(15);
                    workout11.setRequiredEquipment("Yoga Mat");
                    workoutRepo.save(workout11);

                    addExerciseToWorkout(workoutExerciseRepo, workout11, neckRolls, 1, 2, 10, null, 10, "Each direction");
                    addExerciseToWorkout(workoutExerciseRepo, workout11, shoulderCrossBodyStretch, 2, 2, null, 20, 10, "Each arm");
                    addExerciseToWorkout(workoutExerciseRepo, workout11, tricepOverheadStretch, 3, 2, null, 20, 10, "Each arm");
                    addExerciseToWorkout(workoutExerciseRepo, workout11, chestOpenerStretch, 4, 2, null, 20, 15, null);
                    addExerciseToWorkout(workoutExerciseRepo, workout11, standingQuadStretch, 5, 2, null, 30, 10, "Each leg");
                    addExerciseToWorkout(workoutExerciseRepo, workout11, standingHamstringStretch, 6, 2, null, 30, 10, "Each leg");
                    addExerciseToWorkout(workoutExerciseRepo, workout11, hipFlexorStretch, 7, 2, null, 30, 10, "Each side");

                    // Workout 12: Foam Roller Recovery
                    Workout workout12 = new Workout();
                    workout12.setName("Foam Roller Recovery");
                    workout12.setDescription("Self-myofascial release session for muscle recovery and mobility");
                    workout12.setWorkoutType(Workout.WorkoutType.FLEXIBILITY);
                    workout12.setDifficultyLevel(Workout.DifficultyLevel.INTERMEDIATE);
                    workout12.setEstimatedDuration(20);
                    workout12.setRequiredEquipment("Foam Roller, Yoga Mat");
                    workoutRepo.save(workout12);

                    addExerciseToWorkout(workoutExerciseRepo, workout12, foamRollerBack, 1, 2, null, 60, 30, "Slow rolls");
                    addExerciseToWorkout(workoutExerciseRepo, workout12, foamRollerITBand, 2, 2, null, 45, 30, "Each leg");
                    addExerciseToWorkout(workoutExerciseRepo, workout12, pigeonPose, 3, 2, null, 60, 20, "Each side");
                    addExerciseToWorkout(workoutExerciseRepo, workout12, catCowStretch, 4, 2, 10, null, 15, null);
                    addExerciseToWorkout(workoutExerciseRepo, workout12, childsPose, 5, 1, null, 90, 0, "Final rest");

                    // Workout 13: Morning Energizer
                    Workout workout13 = new Workout();
                    workout13.setName("Morning Energizer");
                    workout13.setDescription("Mixed cardio and strength to kickstart your day with energy");
                    workout13.setWorkoutType(Workout.WorkoutType.MIXED);
                    workout13.setDifficultyLevel(Workout.DifficultyLevel.INTERMEDIATE);
                    workout13.setEstimatedDuration(25);
                    workout13.setRequiredEquipment("None");
                    workoutRepo.save(workout13);

                    addExerciseToWorkout(workoutExerciseRepo, workout13, jumpingJack, 1, 2, null, 45, 15, "Warmup");
                    addExerciseToWorkout(workoutExerciseRepo, workout13, highKnees, 2, 3, null, 30, 30, null);
                    addExerciseToWorkout(workoutExerciseRepo, workout13, squat, 3, 3, 15, null, 30, null);
                    addExerciseToWorkout(workoutExerciseRepo, workout13, pushUp, 4, 3, 10, null, 30, null);
                    addExerciseToWorkout(workoutExerciseRepo, workout13, gluteBridge, 5, 3, 15, null, 30, null);
                    addExerciseToWorkout(workoutExerciseRepo, workout13, birdDog, 6, 3, 10, null, 30, "Each side");
                    addExerciseToWorkout(workoutExerciseRepo, workout13, plank, 7, 2, null, 30, 0, "Finish strong");

                    logger.info(" Created {} workouts with exercises", workoutRepo.count());
                }
            }

            logger.info(" B3 Backend ready for demo!");
        };
    }

    private Exercise createExercise(ExerciseRepository repo, String name, String description,
                                     Exercise.MuscleGroup muscleGroup, Exercise.EquipmentType equipmentType,
                                     String videoUrl, String imageUrl) {
        Exercise exercise = new Exercise();
        exercise.setName(name);
        exercise.setDescription(description);
        exercise.setMuscleGroup(muscleGroup);
        exercise.setEquipmentType(equipmentType);
        exercise.setVideoUrl(videoUrl);
        exercise.setImageUrl(imageUrl);
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
