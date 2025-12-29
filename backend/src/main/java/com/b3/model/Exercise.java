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

    @Size(max = 50)
    @Column(name = "muscle_group", length = 50)
    private String muscleGroup;

    @Size(max = 255)
    @Column(name = "video_url", length = 255)
    private String videoUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public Exercise() {}
    
    public Exercise(String name, String description, String muscleGroup, String videoUrl) {
        this.name = name;
        this.description = description;
        this.muscleGroup = muscleGroup;
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
     * Check if this exercise targets a specific muscle group (case-insensitive)
     */
    public boolean targetsMuscleGroup(String targetGroup) {
        if (muscleGroup == null || targetGroup == null) {
            return false;
        }
        return muscleGroup.equalsIgnoreCase(targetGroup);
    }

    /**
     * Check if this is a bodyweight exercise (no equipment required)
     * Detects common equipment keywords in description
     */
    public boolean isBodyweight() {
        if (description == null || description.isBlank()) {
            return true; // Assume bodyweight if no description
        }

        String descLower = description.toLowerCase();
        String[] equipmentKeywords = {
            "dumbbell", "barbell", "kettlebell", "weight", "band", "resistance",
            "bench", "machine", "cable", "plate", "medicine ball", "trx"
        };

        for (String keyword : equipmentKeywords) {
            if (descLower.contains(keyword)) {
                return false;
            }
        }

        return true;
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

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
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
                ", muscleGroup='" + muscleGroup + '\'' +
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