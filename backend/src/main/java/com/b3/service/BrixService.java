package com.b3.service;

import com.b3.model.*;
import com.b3.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * BrixService - The AI Coach Brain
 * 
 * Uses Claude AI for intelligent responses when configured,
 * falls back to keyword-based responses otherwise.
 */
@Service
@Transactional
public class BrixService {

    private static final Logger logger = LoggerFactory.getLogger(BrixService.class);

    private final UserProfileRepository userProfileRepository;
    private final BehaviorProfileRepository behaviorProfileRepository;
    private final WorkoutRepository workoutRepository;
    private final DailyLogRepository dailyLogRepository;
    private final BrixMessageRepository brixMessageRepository;
    private final BrickRepository brickRepository;
    private final ClaudeService claudeService;

    public BrixService(UserProfileRepository userProfileRepository,
                       BehaviorProfileRepository behaviorProfileRepository,
                       WorkoutRepository workoutRepository,
                       DailyLogRepository dailyLogRepository,
                       BrixMessageRepository brixMessageRepository,
                       BrickRepository brickRepository,
                       ClaudeService claudeService) {
        this.userProfileRepository = userProfileRepository;
        this.behaviorProfileRepository = behaviorProfileRepository;
        this.workoutRepository = workoutRepository;
        this.dailyLogRepository = dailyLogRepository;
        this.brixMessageRepository = brixMessageRepository;
        this.brickRepository = brickRepository;
        this.claudeService = claudeService;
    }

    // ========================================================================
    // CHAT RESPONSE
    // ========================================================================

    /**
     * Generate a chat response to user message
     */
    public BrixChatResponse chat(Long profileId, String userMessage) {
        logger.info("BRIX chat for user {}: {}", profileId, userMessage);

        UserProfile user = userProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("User not found: " + profileId));

        BehaviorProfile behavior = behaviorProfileRepository.findByUserProfile(user)
                .orElse(null);

        DailyLog todaysLog = dailyLogRepository.findByUserProfileAndLogDate(user, LocalDate.now())
                .orElse(null);

        // Determine tone based on behavior
        BrixMessage.Tone tone = determineTone(behavior, todaysLog);

        // Try Claude AI first, fall back to keyword-based
        String response = generateResponse(userMessage, user, behavior, todaysLog, tone);

        // Save the message
        BrixMessage message = new BrixMessage(user, response, BrixMessage.MessageType.MOTIVATION, tone);
        message.setContextTrigger("user_chat");
        brixMessageRepository.save(message);

        return new BrixChatResponse(response, tone.name(), getWorkoutRecommendation(profileId));
    }

    /**
     * Generate response - tries Claude AI first, then falls back to keywords
     */
    private String generateResponse(String userMessage, UserProfile user,
                                     BehaviorProfile behavior, DailyLog todaysLog,
                                     BrixMessage.Tone tone) {
        
        // Try Claude AI if configured
        if (claudeService.isConfigured()) {
            logger.info("Using Claude AI for response");
            try {
                String aiResponse = claudeService.generateBrixResponse(
                    userMessage,
                    user.getDisplayName().split(" ")[0],
                    behavior != null ? behavior.getConsecutiveDays() : 0,
                    behavior != null ? behavior.getTotalBricksLaid() : 0,
                    todaysLog != null ? todaysLog.getEnergyLevel() : null,
                    todaysLog != null ? todaysLog.getStressLevel() : null,
                    todaysLog != null ? todaysLog.getMood().name() : null,
                    user.getFitnessLevel() != null ? user.getFitnessLevel().name() : null,
                    user.getPrimaryGoal() != null ? user.getPrimaryGoal().name() : null
                );
                
                if (aiResponse != null && !aiResponse.isEmpty()) {
                    return aiResponse;
                }
            } catch (Exception e) {
                logger.error("Claude AI failed, falling back to keywords: {}", e.getMessage());
            }
        }

        // Fall back to keyword-based response
        logger.info("Using keyword-based response");
        return generateKeywordResponse(userMessage, user, behavior, todaysLog, tone);
    }

    /**
     * Keyword-based response generation (fallback)
     */
    private String generateKeywordResponse(String userMessage, UserProfile user,
                                            BehaviorProfile behavior, DailyLog todaysLog,
                                            BrixMessage.Tone tone) {
        String lowerMessage = userMessage.toLowerCase();
        String name = user.getDisplayName().split(" ")[0];
        int streak = behavior != null ? behavior.getConsecutiveDays() : 0;

        // Greeting
        if (containsAny(lowerMessage, "hello", "hi", "hey", "sup", "what's up")) {
            return greetingResponse(name, streak, tone);
        }

        // Tired/Low energy
        if (containsAny(lowerMessage, "tired", "exhausted", "no energy", "drained", "sleepy", "low energy", "fatigued")) {
            return tiredResponse(name, todaysLog, tone);
        }

        // Motivated/Ready
        if (containsAny(lowerMessage, "ready", "crush", "let's go", "pumped", "motivated", "fired up", "excited")) {
            return motivatedResponse(name, streak, tone);
        }

        // Need motivation
        if (containsAny(lowerMessage, "motivation", "motivate", "inspire", "struggling", "can't", "help", "stuck")) {
            return motivationBoostResponse(name, behavior, tone);
        }

        // Workout recommendation
        if (containsAny(lowerMessage, "workout", "what should", "recommend", "suggestion", "exercise", "train")) {
            return workoutRecommendationResponse(name, todaysLog, behavior, tone);
        }

        // Streak/Progress questions
        if (containsAny(lowerMessage, "streak", "progress", "how am i", "stats", "doing", "bricks")) {
            return progressResponse(name, behavior, tone);
        }

        // Stress
        if (containsAny(lowerMessage, "stressed", "stress", "anxious", "overwhelmed", "pressure")) {
            return stressResponse(name, todaysLog, tone);
        }

        // Feeling bad/down
        if (containsAny(lowerMessage, "bad", "down", "sad", "not great", "rough", "hard day", "difficult")) {
            return empathyResponse(name, behavior, tone);
        }

        // Default response
        return defaultResponse(name, tone);
    }

    // ========================================================================
    // RESPONSE GENERATORS
    // ========================================================================

    private String greetingResponse(String name, int streak, BrixMessage.Tone tone) {
        if (streak >= 7) {
            return String.format("Hey %s! 🔥 Look at you with that %d-day streak! You're absolutely crushing it. What can I help you with today?", name, streak);
        } else if (streak >= 3) {
            return String.format("Hey %s! 👋 Great to see you! You've got %d days going strong. Ready to add another brick to that wall?", name, streak);
        } else {
            return String.format("Hey %s! 👋 I'm BRIX, your AI fitness coach. I'm here to help you build your foundation, one brick at a time. How are you feeling today?", name);
        }
    }

    private String tiredResponse(String name, DailyLog todaysLog, BrixMessage.Tone tone) {
        if (todaysLog != null && todaysLog.getEnergyLevel() <= 2) {
            return String.format("I hear you, %s. Your check-in showed low energy today, and that's totally valid. 🧱 Rest IS part of the process. Maybe a light 15-minute stretch or a walk? Your body will thank you, and tomorrow you'll come back stronger!", name);
        }
        return String.format("I hear you, %s! Rest is just as important as the workout itself. 🧱 Remember, even the strongest walls need time to set. Maybe try some light stretching or a short walk today? Tomorrow you'll come back stronger!", name);
    }

    private String motivatedResponse(String name, int streak, BrixMessage.Tone tone) {
        if (streak >= 5) {
            return String.format("THAT'S WHAT I LOVE TO HEAR, %s! 🔥 You're on a
cat > ~/ZCWProjects/B3-Passion-Project/backend/src/main/java/com/b3/service/BrixService.java << 'ENDOFFILE'
package com.b3.service;

import com.b3.model.*;
import com.b3.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * BrixService - The AI Coach Brain
 * 
 * Uses Claude AI for intelligent responses when configured,
 * falls back to keyword-based responses otherwise.
 */
@Service
@Transactional
public class BrixService {

    private static final Logger logger = LoggerFactory.getLogger(BrixService.class);

    private final UserProfileRepository userProfileRepository;
    private final BehaviorProfileRepository behaviorProfileRepository;
    private final WorkoutRepository workoutRepository;
    private final DailyLogRepository dailyLogRepository;
    private final BrixMessageRepository brixMessageRepository;
    private final BrickRepository brickRepository;
    private final ClaudeService claudeService;

    public BrixService(UserProfileRepository userProfileRepository,
                       BehaviorProfileRepository behaviorProfileRepository,
                       WorkoutRepository workoutRepository,
                       DailyLogRepository dailyLogRepository,
                       BrixMessageRepository brixMessageRepository,
                       BrickRepository brickRepository,
                       ClaudeService claudeService) {
        this.userProfileRepository = userProfileRepository;
        this.behaviorProfileRepository = behaviorProfileRepository;
        this.workoutRepository = workoutRepository;
        this.dailyLogRepository = dailyLogRepository;
        this.brixMessageRepository = brixMessageRepository;
        this.brickRepository = brickRepository;
        this.claudeService = claudeService;
    }

    // ========================================================================
    // CHAT RESPONSE
    // ========================================================================

    /**
     * Generate a chat response to user message
     */
    public BrixChatResponse chat(Long profileId, String userMessage) {
        logger.info("BRIX chat for user {}: {}", profileId, userMessage);

        UserProfile user = userProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("User not found: " + profileId));

        BehaviorProfile behavior = behaviorProfileRepository.findByUserProfile(user)
                .orElse(null);

        DailyLog todaysLog = dailyLogRepository.findByUserProfileAndLogDate(user, LocalDate.now())
                .orElse(null);

        // Determine tone based on behavior
        BrixMessage.Tone tone = determineTone(behavior, todaysLog);

        // Try Claude AI first, fall back to keyword-based
        String response = generateResponse(userMessage, user, behavior, todaysLog, tone);

        // Save the message
        BrixMessage message = new BrixMessage(user, response, BrixMessage.MessageType.MOTIVATION, tone);
        message.setContextTrigger("user_chat");
        brixMessageRepository.save(message);

        return new BrixChatResponse(response, tone.name(), getWorkoutRecommendation(profileId));
    }

    /**
     * Generate response - tries Claude AI first, then falls back to keywords
     */
    private String generateResponse(String userMessage, UserProfile user,
                                     BehaviorProfile behavior, DailyLog todaysLog,
                                     BrixMessage.Tone tone) {
        
        // Try Claude AI if configured
        if (claudeService.isConfigured()) {
            logger.info("Using Claude AI for response");
            try {
                String aiResponse = claudeService.generateBrixResponse(
                    userMessage,
                    user.getDisplayName().split(" ")[0],
                    behavior != null ? behavior.getConsecutiveDays() : 0,
                    behavior != null ? behavior.getTotalBricksLaid() : 0,
                    todaysLog != null ? todaysLog.getEnergyLevel() : null,
                    todaysLog != null ? todaysLog.getStressLevel() : null,
                    todaysLog != null ? todaysLog.getMood().name() : null,
                    user.getFitnessLevel() != null ? user.getFitnessLevel().name() : null,
                    user.getPrimaryGoal() != null ? user.getPrimaryGoal().name() : null
                );
                
                if (aiResponse != null && !aiResponse.isEmpty()) {
                    return aiResponse;
                }
            } catch (Exception e) {
                logger.error("Claude AI failed, falling back to keywords: {}", e.getMessage());
            }
        }

        // Fall back to keyword-based response
        logger.info("Using keyword-based response");
        return generateKeywordResponse(userMessage, user, behavior, todaysLog, tone);
    }

    /**
     * Keyword-based response generation (fallback)
     */
    private String generateKeywordResponse(String userMessage, UserProfile user,
                                            BehaviorProfile behavior, DailyLog todaysLog,
                                            BrixMessage.Tone tone) {
        String lowerMessage = userMessage.toLowerCase();
        String name = user.getDisplayName().split(" ")[0];
        int streak = behavior != null ? behavior.getConsecutiveDays() : 0;

        // Greeting
        if (containsAny(lowerMessage, "hello", "hi", "hey", "sup", "what's up")) {
            return greetingResponse(name, streak, tone);
        }

        // Tired/Low energy
        if (containsAny(lowerMessage, "tired", "exhausted", "no energy", "drained", "sleepy", "low energy", "fatigued")) {
            return tiredResponse(name, todaysLog, tone);
        }

        // Motivated/Ready
        if (containsAny(lowerMessage, "ready", "crush", "let's go", "pumped", "motivated", "fired up", "excited")) {
            return motivatedResponse(name, streak, tone);
        }

        // Need motivation
        if (containsAny(lowerMessage, "motivation", "motivate", "inspire", "struggling", "can't", "help", "stuck")) {
            return motivationBoostResponse(name, behavior, tone);
        }

        // Workout recommendation
        if (containsAny(lowerMessage, "workout", "what should", "recommend", "suggestion", "exercise", "train")) {
            return workoutRecommendationResponse(name, todaysLog, behavior, tone);
        }

        // Streak/Progress questions
        if (containsAny(lowerMessage, "streak", "progress", "how am i", "stats", "doing", "bricks")) {
            return progressResponse(name, behavior, tone);
        }

        // Stress
        if (containsAny(lowerMessage, "stressed", "stress", "anxious", "overwhelmed", "pressure")) {
            return stressResponse(name, todaysLog, tone);
        }

        // Feeling bad/down
        if (containsAny(lowerMessage, "bad", "down", "sad", "not great", "rough", "hard day", "difficult")) {
            return empathyResponse(name, behavior, tone);
        }

        // Default response
        return defaultResponse(name, tone);
    }

    // ========================================================================
    // RESPONSE GENERATORS
    // ========================================================================

    private String greetingResponse(String name, int streak, BrixMessage.Tone tone) {
        if (streak >= 7) {
            return String.format("Hey %s! 🔥 Look at you with that %d-day streak! You're absolutely crushing it. What can I help you with today?", name, streak);
        } else if (streak >= 3) {
            return String.format("Hey %s! 👋 Great to see you! You've got %d days going strong. Ready to add another brick to that wall?", name, streak);
        } else {
            return String.format("Hey %s! 👋 I'm BRIX, your AI fitness coach. I'm here to help you build your foundation, one brick at a time. How are you feeling today?", name);
        }
    }

    private String tiredResponse(String name, DailyLog todaysLog, BrixMessage.Tone tone) {
        if (todaysLog != null && todaysLog.getEnergyLevel() <= 2) {
            return String.format("I hear you, %s. Your check-in showed low energy today, and that's totally valid. 🧱 Rest IS part of the process. Maybe a light 15-minute stretch or a walk? Your body will thank you, and tomorrow you'll come back stronger!", name);
        }
        return String.format("I hear you, %s! Rest is just as important as the workout itself. 🧱 Remember, even the strongest walls need time to set. Maybe try some light stretching or a short walk today? Tomorrow you'll come back stronger!", name);
    }

    private String motivatedResponse(String name, int streak, BrixMessage.Tone tone) {
        if (streak >= 5) {
            return String.format("THAT'S WHAT I LOVE TO HEAR, %s! 🔥 You're on a %d-day streak and your energy is UNMATCHED! Let's channel this fire into something legendary. Check out the Workouts tab - I've got the perfect challenge waiting for you!", name, streak);
        }
        return String.format("That's the spirit, %s! 🔥 Your energy is contagious! Let's channel that motivation into building something great. Head to the Workouts tab - I've got some perfect options waiting for you!", name);
    }

    private String motivationBoostResponse(String name, BehaviorProfile behavior, BrixMessage.Tone tone) {
        int totalBricks = behavior != null ? behavior.getTotalBricksLaid() : 0;

        if (totalBricks > 0) {
            return String.format("Listen %s, here's some truth: You've already laid %d bricks. 🧱 That's %d times you showed up when you could've stayed on the couch. You've proven you can do this. One more brick today - that's all. Not perfect, just present.", name, totalBricks, totalBricks);
        }
        return String.format("Here's the thing, %s: Every single brick matters. 🧱 You're not just working out - you're building the person you want to become. The wall doesn't care if you're slow or fast. It only cares that you show up. And YOU showed up today by being here!", name);
    }

    private String workoutRecommendationResponse(String name, DailyLog todaysLog, BehaviorProfile behavior, BrixMessage.Tone tone) {
        if (todaysLog != null) {
            int energy = todaysLog.getEnergyLevel();
            int stress = todaysLog.getStressLevel();

            if (energy <= 2 || stress >= 4) {
                return String.format("Based on your check-in, %s, I'd recommend something lighter today. 💚 Try 'Gentle Flow Yoga' or a quick 20-minute walk. Taking care of yourself IS building your foundation. Head to Workouts when you're ready!", name);
            } else if (energy >= 4 && stress <= 2) {
                return String.format("Your energy is HIGH and stress is low - perfect conditions, %s! 💪 I'm recommending 'Full Body Strength' or 'HIIT Cardio Burn'. You've got the power today - let's use it! Check the Workouts tab!", name);
            }
        }
        return String.format("Based on your profile, %s, I'd recommend checking out 'Upper Body Blast' today! It's a solid 30-minute workout that'll help you build a strong foundation. 💪 Head to the Workouts tab when you're ready!", name);
    }

    private String progressResponse(String name, BehaviorProfile behavior, BrixMessage.Tone tone) {
        if (behavior == null) {
            return String.format("You're just getting started, %s! 🧱 Every master builder started with their first brick. Complete a workout today and watch your wall begin to rise!", name);
        }

        int streak = behavior.getConsecutiveDays();
        int total = behavior.getTotalBricksLaid();
        int longest = behavior.getLongestStreak();

        if (streak >= longest && streak > 0) {
            return String.format("🏆 %s, you're making HISTORY! Your current %d-day streak is your LONGEST EVER! You've laid %d total bricks. Keep this momentum going - you're unstoppable!", name, streak, total);
        } else if (streak >= 7) {
            return String.format("🔥 %s, you're on FIRE! %d-day streak, %d total bricks laid. Your longest streak is %d days - you're getting close! One brick at a time.", name, streak, total, longest);
        } else {
            return String.format("Hey %s! Here's where you stand: %d-day current streak, %d total bricks laid, and your record is %d days. Every brick counts - let's keep building! 🧱", name, streak, total, longest);
        }
    }

    private String stressResponse(String name, DailyLog todaysLog, BrixMessage.Tone tone) {
        return String.format("I see you, %s. Stress is real, and I'm glad you're here. 💙 Exercise is actually one of the best stress relievers - but only if it feels right. How about a 15-minute 'Stress Relief Stretch' or a simple walk? No pressure, just presence. You've got this.", name);
    }

    private String empathyResponse(String name, BehaviorProfile behavior, BrixMessage.Tone tone) {
        int totalBricks = behavior != null ? behavior.getTotalBricksLaid() : 0;
        if (totalBricks > 0) {
            return String.format("I'm sorry you're not feeling great, %s. 💙 But here's what I know - you've already built %d bricks in your wall. One tough day doesn't tear that down. Rest if you need to. The wall will be here tomorrow, and so will I.", name, totalBricks);
        }
        return String.format("I hear you, %s. Some days are just hard, and that's okay. 💙 You don't have to crush a workout today. Just showing up here and checking in? That counts. How about we start small - even a 5-minute stretch or walk outside?", name);
    }

    private String defaultResponse(String name, BrixMessage.Tone tone) {
        List<String> responses = Arrays.asList(
            String.format("I love that you're here, %s! 🧱 Every interaction is another brick in your foundation. What else can I help you with? Ask about workouts, motivation, or your progress!", name),
            String.format("You showing up matters, %s! 💪 Whether it's a workout or just checking in, you're building momentum. What's on your mind?", name),
            String.format("Remember %s, consistency beats intensity. 🧱 You're doing great just by being here. Need a workout recommendation or some motivation?", name)
        );
        return responses.get(new Random().nextInt(responses.size()));
    }

    // ========================================================================
    // WORKOUT RECOMMENDATION
    // ========================================================================

    public WorkoutRecommendation getWorkoutRecommendation(Long profileId) {
        logger.info("Getting workout recommendation for user {}", profileId);

        UserProfile user = userProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("User not found: " + profileId));

        DailyLog todaysLog = dailyLogRepository.findByUserProfileAndLogDate(user, LocalDate.now())
                .orElse(null);

        BehaviorProfile behavior = behaviorProfileRepository.findByUserProfile(user)
                .orElse(null);

        List<Workout> allWorkouts = workoutRepository.findAll();
        if (allWorkouts.isEmpty()) {
            return null;
        }

        Workout recommended = scoreAndSelectWorkout(allWorkouts, user, todaysLog, behavior);
        String reason = generateRecommendationReason(recommended, todaysLog, behavior);

        return new WorkoutRecommendation(
                recommended.getWorkoutId(),
                recommended.getName(),
                recommended.getWorkoutType().name(),
                recommended.getDifficultyLevel().name(),
                recommended.getEstimatedDuration(),
                reason
        );
    }

    private Workout scoreAndSelectWorkout(List<Workout> workouts, UserProfile user,
                                           DailyLog todaysLog, BehaviorProfile behavior) {
        Map<Workout, Integer> scores = new HashMap<>();

        for (Workout workout : workouts) {
            int score = 50;

            if (workout.getDifficultyLevel().name().equals(user.getFitnessLevel().name())) {
                score += 20;
            }

            if (todaysLog != null) {
                int energy = todaysLog.getEnergyLevel();
                int stress = todaysLog.getStressLevel();

                if (energy <= 2) {
                    if (workout.getDifficultyLevel() == Workout.DifficultyLevel.BEGINNER) score += 30;
                    if (workout.getEstimatedDuration() <= 20) score += 20;
                    if (workout.getWorkoutType() == Workout.WorkoutType.FLEXIBILITY) score += 25;
                }

                if (energy >= 4) {
                    if (workout.getDifficultyLevel() == Workout.DifficultyLevel.ADVANCED) score += 15;
                    if (workout.getWorkoutType() == Workout.WorkoutType.STRENGTH) score += 10;
                }

                if (stress >= 4) {
                    if (workout.getWorkoutType() == Workout.WorkoutType.FLEXIBILITY) score += 30;
                    if (workout.getEstimatedDuration() <= 25) score += 15;
                }
            }

            if (user.getPrimaryGoal() != null) {
                String goal = user.getPrimaryGoal().name();
                if (goal.equals("STRENGTH") && workout.getWorkoutType() == Workout.WorkoutType.STRENGTH) {
                    score += 15;
                } else if (goal.equals("CARDIO") && workout.getWorkoutType() == Workout.WorkoutType.CARDIO) {
                    score += 15;
                } else if (goal.equals("FLEXIBILITY") && workout.getWorkoutType() == Workout.WorkoutType.FLEXIBILITY) {
                    score += 15;
                }
            }

            scores.put(workout, score);
        }

        return scores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(workouts.get(0));
    }

    private String generateRecommendationReason(Workout workout, DailyLog todaysLog, BehaviorProfile behavior) {
        if (todaysLog != null) {
            if (todaysLog.getEnergyLevel() <= 2) {
                return "Perfect for your energy level today - gentle but effective!";
            }
            if (todaysLog.getStressLevel() >= 4) {
                return "Great for stress relief - you'll feel amazing after!";
            }
            if (todaysLog.getEnergyLevel() >= 4) {
                return "You've got great energy today - let's maximize it!";
            }
        }
        return "Matches your fitness level and goals perfectly!";
    }

    // ========================================================================
    // TONE ADAPTATION
    // ========================================================================

    private BrixMessage.Tone determineTone(BehaviorProfile behavior, DailyLog todaysLog) {
        if (todaysLog != null) {
            if (todaysLog.getMood() == DailyLog.Mood.STRESSED || 
                todaysLog.getMood() == DailyLog.Mood.LOW) {
                return BrixMessage.Tone.EMPATHETIC;
            }
            if (todaysLog.getEnergyLevel() >= 4 && todaysLog.getMood() == DailyLog.Mood.GREAT) {
                return BrixMessage.Tone.CHALLENGING;
            }
        }

        if (behavior != null) {
            if (behavior.getConsecutiveDays() == 7 || 
                behavior.getConsecutiveDays() == 30 ||
                behavior.getConsecutiveDays() == behavior.getLongestStreak()) {
                return BrixMessage.Tone.CELEBRATORY;
            }

            if (behavior.getMotivationState() == BehaviorProfile.MotivationState.STRUGGLING) {
                return BrixMessage.Tone.EMPATHETIC;
            }

            if (behavior.getMotivationState() == BehaviorProfile.MotivationState.MOTIVATED &&
                behavior.getMomentumTrend() == BehaviorProfile.MomentumTrend.RISING) {
                return BrixMessage.Tone.CHALLENGING;
            }
        }

        return BrixMessage.Tone.ENCOURAGING;
    }

    // ========================================================================
    // CONTEXT TRIGGERS
    // ========================================================================

    public BrixMessage generateContextMessage(Long profileId, String trigger) {
        logger.info("Generating context message for user {} with trigger: {}", profileId, trigger);

        UserProfile user = userProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("User not found: " + profileId));

        BehaviorProfile behavior = behaviorProfileRepository.findByUserProfile(user)
                .orElse(null);

        DailyLog todaysLog = dailyLogRepository.findByUserProfileAndLogDate(user, LocalDate.now())
                .orElse(null);

        String name = user.getDisplayName().split(" ")[0];
        String messageText;
        BrixMessage.MessageType messageType;
        BrixMessage.Tone tone = determineTone(behavior, todaysLog);

        switch (trigger.toLowerCase()) {
            case "app_open":
                messageText = generateAppOpenMessage(name, behavior, todaysLog);
                messageType = BrixMessage.MessageType.CHECK_IN;
                break;
            case "workout_complete":
                messageText = generateWorkoutCompleteMessage(name, behavior);
                messageType = BrixMessage.MessageType.CELEBRATION;
                tone = BrixMessage.Tone.CELEBRATORY;
                break;
            case "streak_milestone":
                messageText = generateStreakMilestoneMessage(name, behavior);
                messageType = BrixMessage.MessageType.CELEBRATION;
                tone = BrixMessage.Tone.CELEBRATORY;
                break;
            case "missed_day":
                messageText = generateMissedDayMessage(name, behavior);
                messageType = BrixMessage.MessageType.MOTIVATION;
                tone = BrixMessage.Tone.EMPATHETIC;
                break;
            case "low_energy":
                messageText = generateLowEnergyMessage(name, todaysLog);
                messageType = BrixMessage.MessageType.TIP;
                tone = BrixMessage.Tone.EMPATHETIC;
                break;
            default:
                messageText = String.format("Hey %s! Ready to build something great today? 🧱", name);
                messageType = BrixMessage.MessageType.MOTIVATION;
        }

        BrixMessage message = new BrixMessage(user, messageText, messageType, tone, trigger);
        return brixMessageRepository.save(message);
    }

    private String generateAppOpenMessage(String name, BehaviorProfile behavior, DailyLog todaysLog) {
        if (behavior != null && behavior.getConsecutiveDays() >= 7) {
            return String.format("Welcome back, %s! 🔥 Day %d of your streak - you're building something incredible!", name, behavior.getConsecutiveDays());
        }
        if (todaysLog == null) {
            return String.format("Hey %s! 👋 Start your day right with a quick check-in. How are you feeling?", name);
        }
        return String.format("Good to see you, %s! Ready to add another brick to your wall today? 🧱", name);
    }

    private String generateWorkoutCompleteMessage(String name, BehaviorProfile behavior) {
        int streak = behavior != null ? behavior.getConsecutiveDays() + 1 : 1;
        int total = behavior != null ? behavior.getTotalBricksLaid() + 1 : 1;

        if (streak == 7) {
            return String.format("🏆 %s, you just hit a 7-DAY STREAK! That's a full week of showing up! Brick #%d is LEGENDARY!", name, total);
        } else if (streak == 30) {
            return String.format("🎉 THIRTY DAYS, %s! You've built an UNSHAKEABLE foundation! Brick #%d - you're officially unstoppable!", name, total);
        }
        return String.format("BRICK LAID! 🧱 Nice work, %s! That's brick #%d in your wall. Day %d streak and counting!", name, total, streak);
    }

    private String generateStreakMilestoneMessage(String name, BehaviorProfile behavior) {
        int streak = behavior != null ? behavior.getConsecutiveDays() : 0;
        return String.format("🏆 MILESTONE ALERT! %s, you've hit %d consecutive days! Your consistency is building something amazing!", name, streak);
    }

    private String generateMissedDayMessage(String name, BehaviorProfile behavior) {
        int total = behavior != null ? behavior.getTotalBricksLaid() : 0;
        return String.format("Hey %s, I noticed you took a rest day. That's okay! 💙 You've still got %d bricks in your wall - they're not going anywhere. Ready to add another when you are.", name, total);
    }

    private String generateLowEnergyMessage(String name, DailyLog todaysLog) {
        return String.format("I see your energy is low today, %s. 💚 That's totally valid. Maybe a gentle stretch or short walk? Taking care of yourself IS building your foundation.", name);
    }

    // ========================================================================
    // GET RECENT MESSAGES
    // ========================================================================

    public List<BrixMessage> getRecentMessages(Long profileId, int limit) {
        UserProfile user = userProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("User not found: " + profileId));

        return brixMessageRepository.findTop20ByUserProfileOrderBySentAtDesc(user)
                .stream()
                .limit(limit)
                .toList();
    }

    // ========================================================================
    // HELPER METHODS
    // ========================================================================

    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) return true;
        }
        return false;
    }

    // ========================================================================
    // RESPONSE CLASSES
    // ========================================================================

    public record BrixChatResponse(
            String message,
            String tone,
            WorkoutRecommendation recommendation
    ) {}

    public record WorkoutRecommendation(
            Long workoutId,
            String name,
            String type,
            String difficulty,
            Integer duration,
            String reason
    ) {}
}
