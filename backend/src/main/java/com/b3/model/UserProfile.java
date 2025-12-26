package com.b3.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.processing.Pattern;

/**
* UserProfile entity represents the demo user in B3.
* Single-user system (no authentication) with profileId = 1.
*/

@Entity
@Table(name = "user_profile")

public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;

    @NotBlank(message = "Display name is required")
    @Size(min = 2, max = 50, message = "Display name must be between 2 and 50 characters")
    @Column(name = "display_name", nullable = false, length = 50)
    private String displayName;

    @NotNull(message = "Age is required")
    @Min(value = 13, message = "User must be at least 13 years old")
    @Max(value = 120, message = "Age must be realistic")
    @Column(name = "age", nullable = false)
    private Integer age;

    @NotBlank(message = "Fitness level is required")
    @Pattern(regexp = "Beginner|Intermediate|Advanced", message = "Fitness level must be Beginner, Intermediate, or Advanced")
    @Column(name = "fitness_level", nullable = false, length = 20)
    private String fitnessLevel;

    @NotBlank(message = "Primary goal is required")
    @Pattern(regexp = "Strength|Cardio|Flexibility|Weight Loss", message = "Invalid primary goal")
    @Column(name = "primary_goal", nullable = false, length = 30)
    private String primaryGoal;

    @Column(name = "equipment", length = 200)
    private String equipment; // Comma-seperated list (e.g., "Dumbells, Resistance Bands,")

    @NotNull(message = "Weekly goal days is required")
    @Min(value = 1, message = "Weekly goal must be at least 1 day")
    @Max(value = 7, message = "Weekly goal cannot exceed 7 days")
    @Column(name = "weekly_goal_days", nullable = false)
    private Integer weeklyGoalDays;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ==================== Constructors ====================

    public UserProfile() {
        // Default constructor for JPA
    }

    public UserProfile(String displayName, Integer age, String fitnessLevel, 
                       String primaryGoal, String equipment, Integer weeklyGoalDays) {
            this.displayName = displayName;
            this.age = age;
            this.fitnessLevel = fitnessLevel;
            this.primaryGoal = primaryGoal;
            this.equipment = equipment;
            this.weeklyGoalDays = weeklyGoalDays;
    }

    // ==================== Lifecycle Callbacks ====================

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
 
    // ==================== Getters and Setters ====================

    

    
    
}
