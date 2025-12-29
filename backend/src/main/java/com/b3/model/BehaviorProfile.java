package com.b3.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * BehaviorProfile
 *
 * Stores psychological / behavioral signals used by BRIX to personalize 
 * workout recommendations. Implements weighted priority to determine 
 * the user's dominant behavioral state.
 */
@Entity
@Table(name = "behavior_profile")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BehaviorProfile {

    // ========================================================================
    // ENUMS
    // ========================================================================

    /**
     * Tone categories tracked by BRIX.
     * These map cleanly to motivation modifiers in the recommendation engine.
     */
    public enum BehaviorTone {
        MOODY,
        TIRED,
        MOTIVATED
    }

    // ========================================================================
    // FIELDS
    // ========================================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "behavior_id")
    private Long behaviorId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "current_tone", nullable = false, length = 20)
    private BehaviorTone currentTone;

    @NotNull
    @Min(0)
    @Column(name = "moody_weight", nullable = false)
    private Integer moodyWeight = 0;

    @NotNull
    @Min(0)
    @Column(name = "tired_weight", nullable = false)
    private Integer tiredWeight = 0;

    @NotNull
    @Min(0)
    @Column(name = "motivated_weight", nullable = false)
    private Integer motivatedWeight = 0;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    // ------------------------------------------------------------------------
    // ONE-TO-ONE: UserProfile (owner side)
    // ------------------------------------------------------------------------
    @OneToOne
    @JoinColumn(name = "profile_id", unique = true, nullable = false)
    private UserProfile userProfile;

    // ========================================================================
    // CONSTRUCTORS
    // ========================================================================

    public BehaviorProfile() {}

    public BehaviorProfile(BehaviorTone currentTone,
                           Integer moodyWeight,
                           Integer tiredWeight,
                           Integer motivatedWeight) {
        this.currentTone = currentTone;
        this.moodyWeight = moodyWeight != null ? moodyWeight : 0;
        this.tiredWeight = tiredWeight != null ? tiredWeight : 0;
        this.motivatedWeight = motivatedWeight != null ? motivatedWeight : 0;
    }

    // ========================================================================
    // JPA CALLBACKS
    // ========================================================================

    @PrePersist
    @PreUpdate
    protected void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }

    // ========================================================================
    // BUSINESS LOGIC
    // ========================================================================

    /**
     * Returns the dominant behavioral tone using a deterministic weighted
     * priority algorithm. Higher weight always wins. Ties fall back to 
     * currentTone as the tiebreaker for stable behavior.
     */
    public BehaviorTone getDominantTone() {
        int moody = this.moodyWeight;
        int tired = this.tiredWeight;
        int motivated = this.motivatedWeight;

        // Determine maximum weight
        int max = Math.max(moody, Math.max(tired, motivated));

        if (max == moody) return BehaviorTone.MOODY;
        if (max == tired) return BehaviorTone.TIRED;
        if (max == motivated) return BehaviorTone.MOTIVATED;

        // Should never happen, but fallback to currentTone
        return currentTone;
    }

    /**
     * Increment weight for a tone. Used by BRIX events.
     */
    public void reinforce(BehaviorTone tone) {
        switch (tone) {
            case MOODY -> this.moodyWeight++;
            case TIRED -> this.tiredWeight++;
            case MOTIVATED -> this.motivatedWeight++;
        }
    }

    /**
     * Reset weights back to zero (important for cycle resets).
     */
    public void resetWeights() {
        this.moodyWeight = 0;
        this.tiredWeight = 0;
        this.motivatedWeight = 0;
    }

    // ========================================================================
    // GETTERS & SETTERS
    // ========================================================================

    public Long getBehaviorId() { return behaviorId; }

    public BehaviorTone getCurrentTone() { return currentTone; }
    public void setCurrentTone(BehaviorTone currentTone) { this.currentTone = currentTone; }

    public Integer getMoodyWeight() { return moodyWeight; }
    public void setMoodyWeight(Integer moodyWeight) { this.moodyWeight = moodyWeight; }

    public Integer getTiredWeight() { return tiredWeight; }
    public void setTiredWeight(Integer tiredWeight) { this.tiredWeight = tiredWeight; }

    public Integer getMotivatedWeight() { return motivatedWeight; }
    public void setMotivatedWeight(Integer motivatedWeight) { this.motivatedWeight = motivatedWeight; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }

    public UserProfile getUserProfile() { return userProfile; }

    /**
     * Ensures bidirectional sync when assigning a UserProfile.
     */
    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
        if (userProfile != null && userProfile.getBehaviorProfile() != this) {
            userProfile.setBehaviorProfile(this);
        }
    }

    // ========================================================================
    // OVERRIDES
    // ========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BehaviorProfile)) return false;
        BehaviorProfile that = (BehaviorProfile) o;
        return Objects.equals(behaviorId, that.behaviorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(behaviorId);
    }

    @Override
    public String toString() {
        return "BehaviorProfile{" +
                "behaviorId=" + behaviorId +
                ", currentTone=" + currentTone +
                ", moodyWeight=" + moodyWeight +
                ", tiredWeight=" + tiredWeight +
                ", motivatedWeight=" + motivatedWeight +
                '}';
    }
}
