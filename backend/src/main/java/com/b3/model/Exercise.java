package com.b3.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Exercise entity - represents individual exercises in the library
 * 
 * Stores reusable exercises that can be added to multiple workouts.
 * Contains exercise details, target muscle groups, and demo videos.
 */
@Entity
@Table(name = "exercise")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Exercise {

    // ========================================================================
    // ENUMS
    // ========================================================================

    public enum MuscleGroup {
        CHEST,
        BACK,
        SHOULDERS,
        BICEPS,
        TRICEPS,
        LEGS,
        QUADS,
        HAMSTRINGS,
        GLUTES,
        CORE,
        FULL_BODY
    }

    public enum EquipmentType {
        BODYWEIGHT,
        DUMBBELLS,
        BARBELL,
        KETTLEBELL,
        RESISTANCE_BANDS,
        CABLE,
        MACHINE,
        BENCH,
        PULL_UP_BAR,
        OTHER
    }

    // ========================================================================
    // FIELDS
    // ========================================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_id")
    private Long exerciseId;

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "muscle_group", nullable = false, length = 30)
    private MuscleGroup muscleGroup;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "equipment_type", nullable = false, length = 30)
    private EquipmentType equipmentType;

    @Size(max = 255)
    @Column(name = "video_url", length = 255)
    private String videoUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public Exercise() {}
    
    public Exercise(String name, String description, MuscleGroup muscleGroup, 
                   EquipmentType equipmentType, String videoUrl) {
        this.name = name;
        this.description = description;
        this.muscleGroup = muscleGroup;
        this.equipmentType = equipmentType;
        this.videoUrl = videoUrl;
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
     * Check if this exercise has a demo video
     */
    public boolean hasVideo() {
        return videoUrl != null && !videoUrl.isBlank();
    }

    /**
     * Check if this exercise targets a specific muscle group
     */
    public boolean targetsMuscleGroup(MuscleGroup targetGroup) {
        return muscleGroup == targetGroup;
    }

    /**
     * Check if this is a bodyweight exercise (no equipment required)
     */
    public boolean isBodyweight() {
        return equipmentType == EquipmentType.BODYWEIGHT;
    }

    /**
     * Check if this exercise requires a specific equipment type
     */
    public boolean requiresEquipment(EquipmentType equipment) {
        return equipmentType == equipment;
    }

    /**
     * Check if this is an upper body exercise
     */
    public boolean isUpperBody() {
        return muscleGroup == MuscleGroup.CHEST ||
               muscleGroup == MuscleGroup.BACK ||
               muscleGroup == MuscleGroup.SHOULDERS ||
               muscleGroup == MuscleGroup.BICEPS ||
               muscleGroup == MuscleGroup.TRICEPS;
    }

    /**
     * Check if this is a lower body exercise
     */
    public boolean isLowerBody() {
        return muscleGroup == MuscleGroup.LEGS ||
               muscleGroup == MuscleGroup.QUADS ||
               muscleGroup == MuscleGroup.HAMSTRINGS ||
               muscleGroup == MuscleGroup.GLUTES;
    }

    /**
     * Check if this targets core
     */
    public boolean isCore() {
        return muscleGroup == MuscleGroup.CORE;
    }

    // ========================================================================
    // GETTERS AND SETTERS
    // ========================================================================

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
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

    public MuscleGroup getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(MuscleGroup muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public EquipmentType getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(EquipmentType equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // ========================================================================
    // OBJECT OVERRIDES
    // ========================================================================

    @Override
    public String toString() {
        return "Exercise{" +
                "exerciseId=" + exerciseId +
                ", name='" + name + '\'' +
                ", muscleGroup=" + muscleGroup +
                ", equipmentType=" + equipmentType +
                ", hasVideo=" + hasVideo() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exercise)) return false;
        Exercise exercise = (Exercise) o;
        return Objects.equals(exerciseId, exercise.exerciseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exerciseId);
    }
}