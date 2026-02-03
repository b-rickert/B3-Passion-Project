package com.b3.service;

import com.b3.model.Exercise;
import com.b3.repository.ExerciseRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service for fetching exercises from ExerciseDB API
 * and caching them in local database
 */
@Service
public class ExerciseApiService {

    private static final Logger log = LoggerFactory.getLogger(ExerciseApiService.class);

    // Mapping of our exercise names to ExerciseDB search terms
    private static final Map<String, String> EXERCISE_NAME_MAP = new HashMap<>();

    // Exercises that don't exist in ExerciseDB (yoga, walking, etc.)
    private static final Set<String> SKIP_EXERCISES = Set.of(
        "Downward Dog", "Warrior I", "Warrior II", "Child's Pose", "Cat-Cow Stretch",
        "Cobra Pose", "Tree Pose", "Seated Forward Fold", "Pigeon Pose",
        "Easy Run", "Interval Sprints", "Brisk Walk", "Walking Lunges Outdoors",
        "Standing Quad Stretch", "Standing Hamstring Stretch", "Hip Flexor Stretch",
        "Chest Opener Stretch", "Shoulder Cross-Body Stretch", "Tricep Overhead Stretch",
        "Neck Rolls", "Foam Roller IT Band", "Foam Roller Back"
    );

    static {
        // Map our names to ExerciseDB search terms for better matches
        EXERCISE_NAME_MAP.put("Push-Up", "push-up");
        EXERCISE_NAME_MAP.put("Dumbbell Bench Press", "dumbbell bench press");
        EXERCISE_NAME_MAP.put("Dumbbell Chest Fly", "dumbbell fly");
        EXERCISE_NAME_MAP.put("Pull-Up", "pull-up");
        EXERCISE_NAME_MAP.put("Dumbbell Row", "dumbbell row");
        EXERCISE_NAME_MAP.put("Superman", "superman");
        EXERCISE_NAME_MAP.put("Shoulder Press", "dumbbell shoulder press");
        EXERCISE_NAME_MAP.put("Lateral Raise", "dumbbell lateral raise");
        EXERCISE_NAME_MAP.put("Bicep Curl", "dumbbell curl");
        EXERCISE_NAME_MAP.put("Tricep Dip", "triceps dip");
        EXERCISE_NAME_MAP.put("Bodyweight Squat", "bodyweight squat");
        EXERCISE_NAME_MAP.put("Walking Lunge", "lunge");
        EXERCISE_NAME_MAP.put("Calf Raise", "calf raise");
        EXERCISE_NAME_MAP.put("Glute Bridge", "glute bridge");
        EXERCISE_NAME_MAP.put("Wall Sit", "wall sit");
        EXERCISE_NAME_MAP.put("Box Jump", "box jump");
        EXERCISE_NAME_MAP.put("Plank", "plank");
        EXERCISE_NAME_MAP.put("Crunch", "crunch");
        EXERCISE_NAME_MAP.put("Mountain Climber", "mountain climber");
        EXERCISE_NAME_MAP.put("Dead Bug", "dead bug");
        EXERCISE_NAME_MAP.put("Bird Dog", "bird dog");
        EXERCISE_NAME_MAP.put("Russian Twist", "russian twist");
        EXERCISE_NAME_MAP.put("Burpee", "burpee");
        EXERCISE_NAME_MAP.put("Jumping Jack", "jumping jack");
        EXERCISE_NAME_MAP.put("High Knees", "high knee");
    }

    private final ExerciseRepository exerciseRepository;
    private final WebClient webClient;

    public ExerciseApiService(
        ExerciseRepository exerciseRepository,
        WebClient.Builder webClientBuilder,
        @Value("${exercisedb.api.key}") String apiKey,
        @Value("${exercisedb.api.url}") String apiUrl
    ) {
        this.exerciseRepository = exerciseRepository;
        this.webClient = webClientBuilder
            .baseUrl(apiUrl)
            .defaultHeader("X-RapidAPI-Key", apiKey)
            .defaultHeader("X-RapidAPI-Host", "exercisedb.p.rapidapi.com")
            .build();
    }

    /**
     * Fetch exercises by body part from API
     */
    public List<Exercise> fetchExercisesByBodyPart(String bodyPart) {
        log.info("Fetching exercises for body part: {}", bodyPart);
        
        try {
            // Call API endpoint: /exercises/bodyPart/{bodyPart}
            JsonNode[] response = webClient.get()
                .uri("/exercises/bodyPart/{bodyPart}?limit=10", bodyPart)
                .retrieve()
                .bodyToMono(JsonNode[].class)
                .block();
            
            if (response == null || response.length == 0) {
                log.warn("No exercises found for body part: {}", bodyPart);
                return new ArrayList<>();
            }
            
            // Convert API response to Exercise entities
            List<Exercise> exercises = new ArrayList<>();
            for (JsonNode node : response) {
                Exercise exercise = convertToExercise(node);
                if (exercise != null) {
                    exercises.add(exercise);
                }
            }
            
            log.info("Fetched {} exercises for {}", exercises.size(), bodyPart);
            return exercises;
            
        } catch (Exception e) {
            log.error("Error fetching exercises from API: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Fetch exercises and cache them in database
     */
    public void fetchAndCacheExercises(String bodyPart, int limit) {
        log.info("Fetching and caching {} exercises for: {}", limit, bodyPart);

        try {
            // Fetch from API
            JsonNode[] response = webClient.get()
                .uri("/exercises/bodyPart/{bodyPart}?limit={limit}", bodyPart, limit)
                .retrieve()
                .bodyToMono(JsonNode[].class)
                .block();

            if (response == null) {
                log.warn("No response from API");
                return;
            }

            // Convert and save to database
            int saved = 0;
            for (JsonNode node : response) {
                Exercise exercise = convertToExercise(node);
                if (exercise != null) {
                    // Check if already exists
                    List<Exercise> existing = exerciseRepository.findByNameContainingIgnoreCase(exercise.getName());
                    if (existing.isEmpty()) {
                        exerciseRepository.save(exercise);
                        saved++;
                        log.debug("Cached exercise: {}", exercise.getName());
                    }
                }
            }

            log.info("Successfully cached {} new exercises", saved);

        } catch (Exception e) {
            log.error("Error caching exercises: {}", e.getMessage(), e);
        }
    }

    /**
     * Search ExerciseDB API by exercise name and return the gifUrl if found
     */
    public String searchExerciseGifUrl(String exerciseName) {
        // Skip exercises not in ExerciseDB (yoga, stretching, etc.)
        if (SKIP_EXERCISES.contains(exerciseName)) {
            log.info("Skipping {} - not in ExerciseDB", exerciseName);
            return null;
        }

        // Use mapped name if available, otherwise use original
        String searchTerm = EXERCISE_NAME_MAP.getOrDefault(exerciseName, exerciseName.toLowerCase());
        log.info("Searching API for exercise: {} (search term: {})", exerciseName, searchTerm);

        try {
            // Search by name
            JsonNode[] response = webClient.get()
                .uri("/exercises/name/{name}?limit=5", searchTerm)
                .retrieve()
                .bodyToMono(JsonNode[].class)
                .block();

            if (response == null || response.length == 0) {
                log.warn("No results found for: {} (searched: {})", exerciseName, searchTerm);
                return null;
            }

            // Return the gifUrl from the first match
            JsonNode firstResult = response[0];
            if (firstResult.has("gifUrl")) {
                String gifUrl = firstResult.get("gifUrl").asText();
                log.info("Found gifUrl for {}: {}", exerciseName, gifUrl);
                return gifUrl;
            }

            return null;

        } catch (Exception e) {
            log.error("Error searching for exercise {}: {}", exerciseName, e.getMessage());
            return null;
        }
    }

    /**
     * Update all local exercises with images from ExerciseDB API
     */
    public int refreshAllExerciseImages() {
        log.info("Refreshing all exercise images from API");

        List<Exercise> allExercises = exerciseRepository.findAll();
        int updated = 0;

        for (Exercise exercise : allExercises) {
            String gifUrl = searchExerciseGifUrl(exercise.getName());
            if (gifUrl != null && !gifUrl.equals(exercise.getImageUrl())) {
                exercise.setImageUrl(gifUrl);
                exerciseRepository.save(exercise);
                updated++;
                log.info("Updated image for: {}", exercise.getName());
            }

            // Small delay to avoid rate limiting
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        log.info("Updated images for {} exercises", updated);
        return updated;
    }

    /**
     * Convert API JSON response to Exercise entity
     */
    private Exercise convertToExercise(JsonNode node) {
        try {
            // Extract fields from API response
            String name = node.has("name") ? node.get("name").asText() : null;
            String bodyPart = node.has("bodyPart") ? node.get("bodyPart").asText() : null;
            String equipment = node.has("equipment") ? node.get("equipment").asText() : null;
            String target = node.has("target") ? node.get("target").asText() : "";

            // Get description from API
            String apiDescription = node.has("description") ? node.get("description").asText() : "";

            // Get instructions
            StringBuilder instructions = new StringBuilder();
            if (node.has("instructions")) {
                JsonNode instructionsNode = node.get("instructions");
                if (instructionsNode.isArray()) {
                    for (JsonNode instruction : instructionsNode) {
                        instructions.append(instruction.asText()).append(" ");
                    }
                }
            }

            // Build full description
            String description = apiDescription.isEmpty()
                ? String.format("Target: %s. Equipment: %s", target, equipment)
                : apiDescription;

            // Get the actual gifUrl from API response
            String gifUrl = node.has("gifUrl") ? node.get("gifUrl").asText() : null;

            if (name == null || bodyPart == null || equipment == null) {
                log.warn("Missing required fields in API response");
                return null;
            }

            // Map body part to muscle group enum
            Exercise.MuscleGroup muscleGroup = mapBodyPartToMuscleGroup(bodyPart);

            // Map equipment to equipment type enum
            Exercise.EquipmentType equipmentType = mapEquipmentToType(equipment);

            return new Exercise(name, description, muscleGroup, equipmentType, gifUrl);

        } catch (Exception e) {
            log.error("Error converting exercise: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Map API body part to Exercise.MuscleGroup enum
     */
    private Exercise.MuscleGroup mapBodyPartToMuscleGroup(String bodyPart) {
        return switch (bodyPart.toLowerCase()) {
            case "chest" -> Exercise.MuscleGroup.CHEST;
            case "back" -> Exercise.MuscleGroup.BACK;
            case "shoulders" -> Exercise.MuscleGroup.SHOULDERS;
            case "upper arms" -> Exercise.MuscleGroup.BICEPS; // Default to biceps
            case "lower arms" -> Exercise.MuscleGroup.BICEPS;
            case "upper legs" -> Exercise.MuscleGroup.QUADS;
            case "lower legs" -> Exercise.MuscleGroup.LEGS;
            case "waist", "cardio" -> Exercise.MuscleGroup.CORE;
            case "neck" -> Exercise.MuscleGroup.CORE;
            default -> Exercise.MuscleGroup.FULL_BODY;
        };
    }

    /**
     * Map API equipment to Exercise.EquipmentType enum
     */
    private Exercise.EquipmentType mapEquipmentToType(String equipment) {
        return switch (equipment.toLowerCase()) {
            case "body weight", "bodyweight", "assisted" -> Exercise.EquipmentType.BODYWEIGHT;
            case "dumbbell" -> Exercise.EquipmentType.DUMBBELLS;
            case "barbell", "ez barbell", "trap bar" -> Exercise.EquipmentType.BARBELL;
            case "kettlebell" -> Exercise.EquipmentType.KETTLEBELL;
            case "cable", "rope" -> Exercise.EquipmentType.CABLE;
            case "band", "resistance band" -> Exercise.EquipmentType.RESISTANCE_BANDS;
            // Map all machines to MACHINE
            case "machine", "leverage machine", "sled machine", "smith machine",
                "upper body ergometer", "skierg machine", "stationary bike", 
                "elliptical machine" -> Exercise.EquipmentType.MACHINE;
            // Map balls and other equipment to OTHER
            default -> Exercise.EquipmentType.OTHER;
        };
    }
}