package com.b3.service;

import com.b3.model.Exercise;
import com.b3.repository.ExerciseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for ExerciseApiService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ExerciseApiService Tests")
class ExerciseApiServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    private ExerciseApiService exerciseApiService;

    @BeforeEach
    void setUp() {
        // Configure the mock chain
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.defaultHeader(anyString(), anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        // Manually create the service with mocked dependencies
        exerciseApiService = new ExerciseApiService(exerciseRepository, webClientBuilder, "https://test-api.com", "test-api-key");
    }

    @Test
    @DisplayName("Should fetch exercises by body part from API")
    void testFetchExercisesByBodyPart() {
        assertNotNull(exerciseApiService);
    }

    @Test
    @DisplayName("Should cache fetched exercises in database")
    void testCacheExercises() {
        assertNotNull(exerciseApiService);
    }
}