/**
 * KEY DESIGN: Typed API client with domain-specific modules.
 *
 * Architecture:
 * 1. Single axios instance with interceptors for logging/error handling
 * 2. Organized into domain modules: profileApi, workoutApi, sessionApi, etc.
 * 3. All functions return typed responses (TypeScript interfaces)
 * 4. Combined export (b3Api) for convenient imports
 *
 * Benefits:
 * - Consistent error handling across all API calls
 * - Request/response logging in one place
 * - Type safety catches API misuse at compile time
 * - Easy to find endpoints: b3Api.workout.getWorkoutById(1)
 */
import axios, { AxiosInstance, AxiosError } from 'axios';
const API_BASE_URL = 'http://localhost:8080/api/v1';
import {
  UserProfileResponse,
  UserProfileUpdateRequest,
  WorkoutResponse,
  WorkoutExerciseDTO,
  WorkoutSessionResponse,
  WorkoutSessionCreateRequest,
  WorkoutSessionCompleteRequest,
  BrickResponse,
  BrickStatsResponse,
  BehaviorProfileResponse,
  BehaviorProfileUpdateRequest,
  DailyLogDTO,
  DailyLogCreateDTO,
  MilestoneDTO,
  ErrorResponse,
  DifficultyLevel,
  WorkoutType,
} from '../types/api';

// ============================================================
// API CLIENT SETUP
// ============================================================

const api: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor for logging
api.interceptors.request.use(
  (config) => {
    console.log(`📤 ${config.method?.toUpperCase()} ${config.url}`);
    return config;
  },
  (error) => {
    console.error('Request error:', error);
    return Promise.reject(error);
  }
);

// Response interceptor for error handling
api.interceptors.response.use(
  (response) => {
    console.log(`📥 ${response.status} ${response.config.url}`);
    return response;
  },
  (error: AxiosError<ErrorResponse>) => {
    if (error.response) {
      console.error(`❌ ${error.response.status}: ${error.response.data?.message || error.message}`);
    } else {
      console.error('Network error:', error.message);
    }
    return Promise.reject(error);
  }
);

// Default profile ID for single-user demo
const DEFAULT_PROFILE_ID = 1;

// ============================================================
// PROFILE API
// ============================================================

export const profileApi = {
  getProfile: async (): Promise<UserProfileResponse> => {
    const response = await api.get<UserProfileResponse>('/profile');
    return response.data;
  },

  getProfileById: async (profileId: number): Promise<UserProfileResponse> => {
    const response = await api.get<UserProfileResponse>(`/profile/${profileId}`);
    return response.data;
  },

  updateProfile: async (data: UserProfileUpdateRequest): Promise<UserProfileResponse> => {
    const response = await api.put<UserProfileResponse>('/profile', data);
    return response.data;
  },

  updateProfileById: async (
    profileId: number, 
    data: UserProfileUpdateRequest
  ): Promise<UserProfileResponse> => {
    const response = await api.put<UserProfileResponse>(`/profile/${profileId}`, data);
    return response.data;
  },
};

// ============================================================
// WORKOUT API
// ============================================================

export const workoutApi = {
  getAllWorkouts: async (): Promise<WorkoutResponse[]> => {
    const response = await api.get<WorkoutResponse[]>('/workouts');
    return response.data;
  },

  getWorkoutById: async (workoutId: number): Promise<WorkoutResponse> => {
    const response = await api.get<WorkoutResponse>(`/workouts/${workoutId}`);
    return response.data;
  },

  getWorkoutExercises: async (workoutId: number): Promise<WorkoutExerciseDTO[]> => {
    const response = await api.get<WorkoutExerciseDTO[]>(`/workouts/${workoutId}/exercises`);
    return response.data;
  },

  getWorkoutsByType: async (type: WorkoutType): Promise<WorkoutResponse[]> => {
    const response = await api.get<WorkoutResponse[]>(`/workouts/type/${type}`);
    return response.data;
  },

  getWorkoutsByDifficulty: async (difficulty: DifficultyLevel): Promise<WorkoutResponse[]> => {
    const response = await api.get<WorkoutResponse[]>(`/workouts/difficulty/${difficulty}`);
    return response.data;
  },

  searchWorkouts: async (query: string): Promise<WorkoutResponse[]> => {
    const response = await api.get<WorkoutResponse[]>('/workouts/search', {
      params: { q: query },
    });
    return response.data;
  },

  getRecommendedWorkouts: async (fitnessLevel: DifficultyLevel): Promise<WorkoutResponse[]> => {
    const response = await api.get<WorkoutResponse[]>(`/workouts/recommended/${fitnessLevel}`);
    return response.data;
  },
};

// ============================================================
// WORKOUT SESSION API
// ============================================================

export const sessionApi = {
  startSession: async (data: WorkoutSessionCreateRequest): Promise<WorkoutSessionResponse> => {
    const response = await api.post<WorkoutSessionResponse>('/sessions', data);
    return response.data;
  },

  completeSession: async (
    sessionId: number, 
    data: WorkoutSessionCompleteRequest
  ): Promise<WorkoutSessionResponse> => {
    const response = await api.post<WorkoutSessionResponse>(
      `/sessions/${sessionId}/complete`, 
      data
    );
    return response.data;
  },

  getActiveSession: async (
    profileId: number = DEFAULT_PROFILE_ID
  ): Promise<WorkoutSessionResponse | null> => {
    try {
      const response = await api.get<WorkoutSessionResponse>(`/sessions/active/${profileId}`);
      return response.data;
    } catch (error) {
      if (axios.isAxiosError(error) && error.response?.status === 204) {
        return null;
      }
      throw error;
    }
  },

  getSessionHistory: async (
    profileId: number = DEFAULT_PROFILE_ID
  ): Promise<WorkoutSessionResponse[]> => {
    const response = await api.get<WorkoutSessionResponse[]>(`/sessions/history/${profileId}`);
    return response.data;
  },
};

// ============================================================
// BRICK API
// ============================================================

export const brickApi = {
  getBrickCalendar: async (
    profileId: number = DEFAULT_PROFILE_ID,
    year?: number,
    month?: number
  ): Promise<BrickResponse[]> => {
    const params: Record<string, number> = {};
    if (year) params.year = year;
    if (month) params.month = month;
    
    const response = await api.get<BrickResponse[]>(`/bricks/calendar/${profileId}`, { params });
    return response.data;
  },

  getBrickStats: async (profileId: number = DEFAULT_PROFILE_ID): Promise<BrickStatsResponse> => {
    const response = await api.get<BrickStatsResponse>(`/bricks/stats/${profileId}`);
    return response.data;
  },

  hasBrickToday: async (
    profileId: number = DEFAULT_PROFILE_ID
  ): Promise<{ hasBrickToday: boolean; date: string }> => {
    const response = await api.get<{ hasBrickToday: boolean; date: string }>(
      `/bricks/today/${profileId}`
    );
    return response.data;
  },

  getBrickHistory: async (profileId: number = DEFAULT_PROFILE_ID): Promise<BrickResponse[]> => {
    const response = await api.get<BrickResponse[]>(`/bricks/history/${profileId}`);
    return response.data;
  },
};

// ============================================================
// DAILY LOG API
// ============================================================

export const dailyLogApi = {
  submitDailyLog: async (data: DailyLogCreateDTO): Promise<DailyLogDTO> => {
    const response = await api.post<DailyLogDTO>('/daily-logs', data);
    return response.data;
  },

  getTodaysLog: async (profileId: number = DEFAULT_PROFILE_ID): Promise<DailyLogDTO> => {
    const response = await api.get<DailyLogDTO>(`/daily-logs/today/${profileId}`);
    return response.data;
  },

  getRecentLogs: async (profileId: number = DEFAULT_PROFILE_ID): Promise<DailyLogDTO[]> => {
    const response = await api.get<DailyLogDTO[]>(`/daily-logs/recent/${profileId}`);
    return response.data;
  },

  hasLoggedToday: async (
    profileId: number = DEFAULT_PROFILE_ID
  ): Promise<{ hasLoggedToday: boolean; date: string }> => {
    const response = await api.get<{ hasLoggedToday: boolean; date: string }>(
      `/daily-logs/check/${profileId}`
    );
    return response.data;
  },

  updateDailyLog: async (logId: number, data: DailyLogCreateDTO): Promise<DailyLogDTO> => {
    const response = await api.put<DailyLogDTO>(`/daily-logs/${logId}`, data);
    return response.data;
  },
};

// ============================================================
// MILESTONE API
// ============================================================

export const milestoneApi = {
  getAllMilestones: async (profileId: number = DEFAULT_PROFILE_ID): Promise<MilestoneDTO[]> => {
    const response = await api.get<MilestoneDTO[]>(`/milestones/${profileId}`);
    return response.data;
  },

  getAchievedMilestones: async (
    profileId: number = DEFAULT_PROFILE_ID
  ): Promise<MilestoneDTO[]> => {
    const response = await api.get<MilestoneDTO[]>(`/milestones/${profileId}/achieved`);
    return response.data;
  },

  getInProgressMilestones: async (
    profileId: number = DEFAULT_PROFILE_ID
  ): Promise<MilestoneDTO[]> => {
    const response = await api.get<MilestoneDTO[]>(`/milestones/${profileId}/in-progress`);
    return response.data;
  },

  getMilestoneStats: async (
    profileId: number = DEFAULT_PROFILE_ID
  ): Promise<{ totalMilestones: number; achievedCount: number; inProgressCount: number }> => {
    const response = await api.get(`/milestones/${profileId}/stats`);
    return response.data;
  },

  checkMilestones: async (profileId: number = DEFAULT_PROFILE_ID): Promise<MilestoneDTO[]> => {
    const response = await api.post<MilestoneDTO[]>(`/milestones/${profileId}/check`);
    return response.data;
  },
};

// ============================================================
// BRIX AI COACH API
// ============================================================

export interface BrixChatResponse {
  message: string;
  tone: string;
  timestamp: string;
}

export interface BrixRecommendation {
  workoutId: number;
  workoutName: string;
  workoutType: string;
  difficulty: string;
  duration: number;
  reason: string;
}

export const brixApi = {
  chat: async (profileId: number, message: string): Promise<BrixChatResponse> => {
    const response = await api.post<BrixChatResponse>('/brix/chat', {
      profileId,
      message,
    });
    return response.data;
  },

  getRecommendation: async (profileId: number = DEFAULT_PROFILE_ID): Promise<BrixRecommendation | null> => {
    try {
      const response = await api.get<BrixRecommendation>(`/brix/recommendation/${profileId}`);
      return response.data;
    } catch (error) {
      if (axios.isAxiosError(error) && error.response?.status === 204) {
        return null;
      }
      throw error;
    }
  },

  getMessages: async (profileId: number = DEFAULT_PROFILE_ID, limit: number = 20): Promise<any[]> => {
    const response = await api.get(`/brix/messages/${profileId}`, { params: { limit } });
    return response.data;
  },

  trigger: async (profileId: number, trigger: string): Promise<any> => {
    const response = await api.post('/brix/trigger', { profileId, trigger });
    return response.data;
  },
};

// ============================================================
// BEHAVIOR PROFILE API
// ============================================================

export const behaviorApi = {
  getBehaviorProfile: async (): Promise<BehaviorProfileResponse> => {
    const response = await api.get<BehaviorProfileResponse>('/behavior');
    return response.data;
  },

  getBehaviorProfileById: async (profileId: number): Promise<BehaviorProfileResponse> => {
    const response = await api.get<BehaviorProfileResponse>(`/behavior/${profileId}`);
    return response.data;
  },

  updateBehaviorProfile: async (
    profileId: number,
    data: BehaviorProfileUpdateRequest
  ): Promise<BehaviorProfileResponse> => {
    const response = await api.put<BehaviorProfileResponse>(`/behavior/${profileId}`, data);
    return response.data;
  },
};

// ============================================================
// COMBINED API OBJECT
// ============================================================

export const b3Api = {
  profile: profileApi,
  workout: workoutApi,
  session: sessionApi,
  brick: brickApi,
  dailyLog: dailyLogApi,
  milestone: milestoneApi,
  behavior: behaviorApi,
  brix: brixApi,
};

export default b3Api;
