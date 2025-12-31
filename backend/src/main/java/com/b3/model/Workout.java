package com.b3.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

/**
 * Workout entity - represents workout templates in the library
 * 
 * Stores pre-defined workout routines that users can perform.
 * Contains metadata like type, difficulty, duration, and equipment needs.
 */

@Entity
@Table(name = "workout")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Workout {

    // ========================================================================
    // RELATIONSHIPS  
    // ========================================================================

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutExercise> exercises = new ArrayList<>();

    // ========================================================================
    // ENUMS
    // ========================================================================

    public enum WorkoutType {
        STRENGTH,
        CARDIO,
        FLEXIBILITY,
        MIXED
    }

    public enum DifficultyLevel {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED
    }

    // ========================================================================
    // FIELDS
    // ========================================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workout_id")
    private Long workoutId;

    @NotBlank
    @Size(min = 3, max = 100)
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "workout_type", nullable = false, length = 20)
    private WorkoutType workoutType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level", nullable = false, length = 20)
    private DifficultyLevel difficultyLevel;

    @NotNull
    @Min(1)
    @Max(100)
    @Column(name = "estimated_duration", nullable = false)
    private Integer estimatedDuration;

    @Column(name = "required_equipment", length = 200)
    private String requiredEquipment;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public Workout() {}

        public Workout(String name,
                   String description,
                   WorkoutType workoutType,
                   DifficultyLevel difficultyLevel,
                   Integer estimatedDuration,
                   String requiredEquipment) {
            this.name = name;
            this.description = description;
            this.workoutType = workoutType;
            this.difficultyLevel = difficultyLevel;
            this.estimatedDuration = estimatedDuration;
            this.requiredEquipment = requiredEquipment;
    }

    // ========================================================================
    // JPA CALLBACKS
    // ========================================================================

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // ========================================================================
    // BUSINESS LOGIC
    // ========================================================================

    /**
     * Check if this workout requires any equipment
     */

    public boolean requiresEquipment() {
        if (requiredEquipment == null || requiredEquipment.isBlank()) {
            return false;
        }
        return !requiredEquipment.trim().equalsIgnoreCase("None");
    }

    /**
     * Check if this workout requires a specific piece of equipment
     */

    public boolean hasEquipment(String equipmentName) {
        if (requiredEquipment == null || requiredEquipment.isBlank()) {
            return false;
        }
        return requiredEquipment.toLowerCase().contains(equipmentName.toLowerCase());
    }

    /**
     * Check if this is a short workout (under 15 minutes)
     */

    public boolean isShortWorkout() {
        return estimatedDuration != null && estimatedDuration < 15;
    }

    /**
     * Check if this workout is suitable for a given difficulty level
     * Logic: A workout is suitable if the user's level is >= the workout's level
     * (e.g., BEGINNER workout is suitable for INTERMEDIATE/ADVANCED users)
     */

    public boolean isSuitableFor(DifficultyLevel userLevel) {
        if (difficultyLevel == null || userLevel == null) {
            return false;
        }

        // Compare ordinal values (BEGINNER=0, INTERMEDIATE=1, ADVANCED=2)
        // Workout is suitable if user's level >= workout's level
        return userLevel.ordinal() >= difficultyLevel.ordinal();
    }

    /**
     * Check if user has all required equipment for this workout
     */

    public boolean matchesEquipment(String userEquipment) {
        // If workout requires no equipment, it always matches
        if (!requiresEquipment()) {
            return true;
        }

        // If user has no equipment, they can't do this workout
        if (userEquipment == null || userEquipment.isBlank()) {
            return false;
        }

        // Split required equipment and check if user has each piece
        String[] requiredItems = requiredEquipment.split(",");
        String userEquipLower = userEquipment.toLowerCase();

        for (String item : requiredItems) {
            String trimmedItem = item.trim().toLowerCase();
            if (!trimmedItem.equals("none") && !userEquipLower.contains(trimmedItem)) {
                return false;
            }
        }

        return true;
    }

    // ========================================================================
    // GETTERS AND SETTERS
    // ========================================================================

    public Long getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(Long workoutId) {
        this.workoutId = workoutId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WorkoutType getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(WorkoutType workoutType) {
        this.workoutType = workoutType;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public Integer getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(Integer estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public String getRequiredEquipment() {
        return requiredEquipment;
    }

    public void setRequiredEquipment(String requiredEquipment) {
        this.requiredEquipment = requiredEquipment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<WorkoutExercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<WorkoutExercise> exercises) {
        this.exercises = exercises;
}

    // ========================================================================
    // OBJECT OVERRIDES
    // ========================================================================

    @Override
    public String toString() {
        return "Workout{" +
                "workoutId=" + workoutId +
                ", name='" + name + '\'' +
                ", workoutType=" + workoutType +
                ", difficultyLevel=" + difficultyLevel +
                ", estimatedDuration=" + estimatedDuration +
                ", requiredEquipment='" + requiredEquipment + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Workout)) return false;
        Workout workout = (Workout) o;
        return Objects.equals(workoutId, workout.workoutId);
    }

    @Override 
    public int hashCode() {
        return Objects.hash(workoutId);
    }
}