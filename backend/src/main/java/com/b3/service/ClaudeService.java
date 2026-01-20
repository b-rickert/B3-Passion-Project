package com.b3.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * ClaudeService - Integration with Anthropic's Claude API
 * 
 * Provides intelligent AI responses for BRIX coach
 */
@Service
public class ClaudeService {

    private static final Logger logger = LoggerFactory.getLogger(ClaudeService.class);
    private static final String CLAUDE_API_URL = "https://api.anthropic.com/v1/messages";

    @Value("${anthropic.api.key:}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public ClaudeService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Check if Claude API is configured
     */
    public boolean isConfigured() {
        return apiKey != null && !apiKey.isEmpty() && !apiKey.equals("your-api-key-here");
    }

    /**
     * Send a message to Claude and get a response
     */
    public String chat(String systemPrompt, String userMessage) {
        if (!isConfigured()) {
            logger.warn("Claude API key not configured");
            return null;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-api-key", apiKey);
            headers.set("anthropic-version", "2023-06-01");

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "claude-3-haiku-20240307"); // Fast & cheap for chat
            requestBody.put("max_tokens", 300);
            requestBody.put("system", systemPrompt);
            
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "user", "content", userMessage));
            requestBody.put("messages", messages);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                CLAUDE_API_URL,
                HttpMethod.POST,
                request,
                Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<Map<String, Object>> content = (List<Map<String, Object>>) response.getBody().get("content");
                if (content != null && !content.isEmpty()) {
                    return (String) content.get(0).get("text");
                }
            }
        } catch (Exception e) {
            logger.error("Claude API error: {}", e.getMessage());
        }

        return null;
    }

    /**
     * Generate BRIX coach response with context
     */
    public String generateBrixResponse(String userMessage, String userName, int streak, 
                                        int totalBricks, Integer energyLevel, Integer stressLevel,
                                        String mood, String fitnessLevel, String primaryGoal) {
        
        String systemPrompt = buildBrixSystemPrompt(userName, streak, totalBricks, 
                                                     energyLevel, stressLevel, mood,
                                                     fitnessLevel, primaryGoal);
        
        return chat(systemPrompt, userMessage);
    }

    private String buildBrixSystemPrompt(String userName, int streak, int totalBricks,
                                          Integer energyLevel, Integer stressLevel, 
                                          String mood, String fitnessLevel, String primaryGoal) {
        
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are BRIX, an enthusiastic and supportive AI fitness coach in the B3 (Brick by Brick) fitness app. ");
        prompt.append("Your personality is warm, motivating, and uses a 'brick by brick' metaphor - each workout is a brick in building the user's fitness wall.\n\n");
        
        prompt.append("KEY TRAITS:\n");
        prompt.append("- Use brick/wall building metaphors naturally (not every message, but often)\n");
        prompt.append("- Be encouraging but not fake - acknowledge struggles genuinely\n");
        prompt.append("- Use emojis sparingly but effectively (🧱💪🔥)\n");
        prompt.append("- Keep responses concise (2-4 sentences max)\n");
        prompt.append("- Be adaptive: empathetic when user is struggling, celebratory for wins, challenging when they're ready\n\n");
        
        prompt.append("USER CONTEXT:\n");
        prompt.append(String.format("- Name: %s\n", userName));
        prompt.append(String.format("- Current streak: %d days\n", streak));
        prompt.append(String.format("- Total bricks (workouts): %d\n", totalBricks));
        prompt.append(String.format("- Fitness level: %s\n", fitnessLevel != null ? fitnessLevel : "Unknown"));
        prompt.append(String.format("- Primary goal: %s\n", primaryGoal != null ? primaryGoal : "General fitness"));
        
        if (energyLevel != null) {
            prompt.append(String.format("- Today's energy level: %d/5\n", energyLevel));
        }
        if (stressLevel != null) {
            prompt.append(String.format("- Today's stress level: %d/5\n", stressLevel));
        }
        if (mood != null) {
            prompt.append(String.format("- Today's mood: %s\n", mood));
        }
        
        prompt.append("\nRESPONSE GUIDELINES:\n");
        prompt.append("- If user is tired/low energy: Be empathetic, suggest rest or light activity\n");
        prompt.append("- If user is stressed: Acknowledge it, suggest exercise as stress relief\n");
        prompt.append("- If user is motivated: Match their energy, encourage them\n");
        prompt.append("- If user asks about workouts: Recommend based on their energy/goals\n");
        prompt.append("- If user asks about progress: Celebrate their bricks and streak\n");
        prompt.append("- Always address them by first name\n");
        prompt.append("- Never break character - you ARE BRIX the fitness coach\n");
        
        return prompt.toString();
    }
}
