package com.b3.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * OllamaService - Integration with locally running Ollama (Llama)
 *
 * Provides free, local AI responses for BRIX coach using Llama models.
 * Requires Ollama to be installed and running locally.
 *
 * Install: https://ollama.ai
 * Run: ollama run llama3.2
 */
@Service
public class OllamaService {

    private static final Logger logger = LoggerFactory.getLogger(OllamaService.class);

    @Value("${ollama.api.url:http://localhost:11434}")
    private String ollamaUrl;

    @Value("${ollama.model:llama3.2}")
    private String model;

    @Value("${ollama.enabled:true}")
    private boolean enabled;

    private final RestTemplate restTemplate;

    public OllamaService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Check if Ollama is configured and reachable
     */
    public boolean isConfigured() {
        if (!enabled) {
            return false;
        }

        try {
            // Quick health check
            ResponseEntity<String> response = restTemplate.getForEntity(
                ollamaUrl + "/api/tags",
                String.class
            );
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            logger.debug("Ollama not reachable: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Send a message to Ollama and get a response
     */
    public String chat(String systemPrompt, String userMessage) {
        if (!enabled) {
            logger.debug("Ollama is disabled");
            return null;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("stream", false);

            // Build messages array for chat endpoint
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content", systemPrompt));
            messages.add(Map.of("role", "user", "content", userMessage));
            requestBody.put("messages", messages);

            // Set options for faster responses
            Map<String, Object> options = new HashMap<>();
            options.put("num_predict", 300); // Limit response length
            options.put("temperature", 0.7);
            requestBody.put("options", options);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            logger.debug("Sending request to Ollama: {}", ollamaUrl + "/api/chat");

            ResponseEntity<Map> response = restTemplate.exchange(
                ollamaUrl + "/api/chat",
                HttpMethod.POST,
                request,
                Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> message = (Map<String, Object>) response.getBody().get("message");
                if (message != null) {
                    return (String) message.get("content");
                }
            }
        } catch (Exception e) {
            logger.error("Ollama API error: {}", e.getMessage());
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

    /**
     * KEY DESIGN: Dynamic system prompt construction.
     * We inject user context (streak, bricks, energy, stress, mood) directly
     * into the AI's system prompt. This means the AI "knows" the user's current
     * state without us having to re-explain it in every message.
     *
     * The prompt defines BRIX's personality and gives explicit guidelines:
     * - Use brick/wall metaphors naturally
     * - Be adaptive (empathetic when struggling, challenging when ready)
     * - Keep responses concise (2-4 sentences)
     * - Always address user by first name
     *
     * This is prompt engineering in action—shaping AI behavior through context.
     */
    private String buildBrixSystemPrompt(String userName, int streak, int totalBricks,
                                          Integer energyLevel, Integer stressLevel,
                                          String mood, String fitnessLevel, String primaryGoal) {

        StringBuilder prompt = new StringBuilder();
        prompt.append("You are BRIX, an enthusiastic and supportive AI fitness coach in the B3 (Brick by Brick) fitness app. ");
        prompt.append("Your personality is warm, motivating, and uses a 'brick by brick' metaphor - each workout is a brick in building the user's fitness wall.\n\n");

        prompt.append("KEY TRAITS:\n");
        prompt.append("- Use brick/wall building metaphors naturally (not every message, but often)\n");
        prompt.append("- Be encouraging but not fake - acknowledge struggles genuinely\n");
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
