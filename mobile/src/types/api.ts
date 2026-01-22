// ============================================================
// B3 API TYPES - Mirrors Java DTOs exactly
// ============================================================

// ============================================================
// ENUMS
// ============================================================

// UserProfile.java
export type FitnessLevel = 'BEGINNER' | 'INTERMEDIATE' | 'ADVANCED';
export type PrimaryGoal = 'STRENGTH' | 'CARDIO' | 'FLEXIBILITY' | 'WEIGHT_LOSS';

// Workout.java
export type WorkoutType = 'STRENGTH' | 'CARDIO' | 'FLEXIBILITY' | 'MIXED';
export type DifficultyLevel = 'BEGINNER' | 'INTERMEDIATE' | 'ADVANCED';

// WorkoutSession.java
export type CompletionStatus = 'IN_PROGRESS' | 'COMPLETED' | 'PARTIAL' | 'SKIPPED';
export type PerformanceRating = 'EXCELLENT' | 'GOOD' | 'AVERAGE' | 'POOR';

// BehaviorProfile.java
export type MotivationState = 'MOTIVATED' | 'NEUTRAL' | 'STRUGGLING';
export type MomentumTrend = 'RISING' | 'FALLING' | 'STABLE';
export type CoachingTone = 'ENCOURAGING' | 'CHALLENGING' | 'EMPATHETIC' | 'CELEBRATORY';
export type PreferredTime = 'MORNING' | 'AFTERNOON' | 'EVENING' | 'NIGHT' | 'FLEXIBLE';
export type StressTrend = 'DECREASING' | 'STABLE' | 'INCREASING';

// Brick.java
export type BrickType = 'WORKOUT' | 'STREAK_BONUS' | 'MILESTONE';
export type BrickStatus = 'ACTIVE' | 'ARCHIVED';

// BrixMessage.java
export type MessageType = 'MOTIVATION' | 'CHECK_IN' | 'CELEBRATION' | 'TIP';
export type Tone = 'ENCOURAGING' | 'CHALLENGING' | 'EMPATHETIC' | 'CELEBRATORY';

// DailyLog.java
export type Mood = 'GREAT' | 'GOOD' | 'OKAY' | 'LOW' | 'STRESSED';

// Milestone.java
export type MilestoneType = 'STREAK' | 'WORKOUT_COUNT' | 'GOAL_ACHIEVED' | 'CONSISTENCY' | 'PERSONAL_RECORD';

// Exercise.java
export type MuscleGroup = 
  | 'CHEST' | 'BACK' | 'SHOULDERS' | 'BICEPS' | 'TRICEPS' 
  | 'LEGS' | 'QUADS' | 'HAMSTRINGS' | 'GLUTES' | 'CORE' | 'FULL_BODY';

export type EquipmentType =
  | 'BODYWEIGHT' | 'DUMBBELLS' | 'BARBELL' | 'KETTLEBELL'
  | 'RESISTANCE_BANDS' | 'CABLE' | 'MACHINE' | 'BENCH' | 'PULL_UP_BAR'
  | 'YOGA_MAT' | 'FOAM_ROLLER' | 'OTHER';

// ============================================================
// RESPONSE DTOS (Backend → Frontend)
// ============================================================

// UserProfileResponse.java
export interface UserProfileResponse {
  profileId: number;
  displayName: string;
  age: number;
  fitnessLevel: FitnessLevel;
  primaryGoal: PrimaryGoal;
  equipment: string | null;
  weeklyGoalDays: number;
  currentStreak: number;
  longestStreak: number;
  totalWorkouts: number;
  createdAt: string;
  updatedAt: string;
}

// WorkoutResponse.java
export interface WorkoutResponse {
  workoutId: number;
  name: string;
  description: string | null;
  workoutType: WorkoutType;
  difficultyLevel: DifficultyLevel;
  estimatedDuration: number;
  requiredEquipment: string | null;
}

// WorkoutExerciseDTO.java
export interface WorkoutExerciseDTO {
  exerciseId: number;
  name: string;
  description: string | null;
  muscleGroup: string;
  equipmentType: string;
  videoUrl: string | null;
  orderIndex: number;
  sets: number;
  reps: number | null;
  durationSeconds: number | null;
  restSeconds: number;
  notes: string | null;
}

// WorkoutSessionResponse.java
export interface WorkoutSessionResponse {
  sessionId: number;
  profileId: number;
  workoutId: number;
  workoutName: string;
  startTime: string;
  endTime: string | null;
  actualDuration: number | null;
  completionStatus: CompletionStatus;
  performanceRating: PerformanceRating | null;
  notes: string | null;
}

// BrickResponse.java
export interface BrickResponse {
  brickId: number;
  profileId: number;
  sessionId: number;
  brickDate: string;
  brickColor: string;
  isFirstOfMonth: boolean;
}

// BrickStatsResponse.java
export interface BrickStatsResponse {
  totalBricks: number;
  currentStreak: number;
  longestStreak: number;
  bricksThisMonth: number;
  bricksThisWeek: number;
}

// BehaviorProfileResponse.java
export interface BehaviorProfileResponse {
  behaviorId: number;
  profileId: number;
  preferredWorkoutTime: PreferredTime | null;
  currentTone: CoachingTone;
  consecutiveDays: number;
  longestStreak: number;
  totalBricksLaid: number;
  consistencyScore: number;
  lastWorkoutDate: string | null;
  motivationState: MotivationState;
  momentumTrend: MomentumTrend;
  fatigueScore: number;
  recentEnergyScore: number;
  lastToneChange: string | null;
  avgWorkoutTimeOfDay: PreferredTime | null;
  preferredWorkoutTypes: string | null;
  energyPattern: string | null;
  stressTrend: StressTrend | null;
  avgSessionDuration: number | null;
  skipFrequency: number | null;
  updatedAt: string | null;
}

// DailyLogDTO.java
export interface DailyLogDTO {
  logId: number;
  profileId: number;
  date: string;
  energyLevel: number;
  stressLevel: number;
  sleepQuality: number;
  mood: Mood;
  notes: string | null;
  wellnessScore: number;
  needsRecovery: boolean;
  optimalWorkoutDay: boolean;
  createdAt?: string;
}

// MilestoneDTO.java
export interface MilestoneDTO {
  milestoneId: number;
  milestoneName: string;
  description: string | null;
  milestoneType: MilestoneType;
  targetValue: number;
  currentValue: number;
  isAchieved: boolean;
  achievedAt: string | null;
  progressPercentage: number;
  progressPercentageString: string;
  remainingValue: number;
  isAlmostComplete: boolean;
  isInProgress: boolean;
  createdAt: string;
}

// BrixMessageDTO.java
export interface BrixMessageDTO {
  messageId: number;
  messageText: string;
  messageType: MessageType;
  tone: Tone;
  contextTrigger: string | null;
  sentAt: string;
  timeAgo: string;
  isSentToday: boolean;
}

// ErrorResponse.java
export interface ErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
}

// ============================================================
// REQUEST DTOS (Frontend → Backend)
// ============================================================

// UserProfileUpdateRequest.java
export interface UserProfileUpdateRequest {
  displayName?: string;
  age?: number;
  fitnessLevel?: FitnessLevel;
  primaryGoal?: PrimaryGoal;
  equipment?: string;
  weeklyGoalDays?: number;
}

// WorkoutSessionCreateRequest.java
export interface WorkoutSessionCreateRequest {
  profileId: number;
  workoutId: number;
}

// WorkoutSessionCompleteRequest.java
export interface WorkoutSessionCompleteRequest {
  actualDuration: number;
  notes?: string;
}

// DailyLogCreateDTO.java
export interface DailyLogCreateDTO {
  profileId: number;
  energyLevel: number;
  stressLevel: number;
  sleepQuality: number;
  mood: Mood;
  notes?: string;
}

// BehaviorProfileUpdateRequest.java
export interface BehaviorProfileUpdateRequest {
  preferredWorkoutTime?: PreferredTime;
  avgWorkoutTimeOfDay?: PreferredTime;
  avgSessionDuration?: number;
  preferredWorkoutTypes?: string;
  energyPattern?: string;
  stressTrend?: StressTrend;
  skipFrequency?: number;
}

// ============================================================
// COMPOSITE/VIEW TYPES (for UI convenience)
// ============================================================

export interface HomeScreenData {
  profile: UserProfileResponse;
  brickStats: BrickStatsResponse;
  todaysLog: DailyLogDTO | null;
  hasLoggedToday: boolean;
  hasBrickToday: boolean;
  recommendedWorkout: WorkoutResponse | null;
}

export interface ProgressScreenData {
  brickStats: BrickStatsResponse;
  brickCalendar: BrickResponse[];
  recentMilestones: MilestoneDTO[];
}

export interface WorkoutDetailData extends WorkoutResponse {
  exercises: WorkoutExerciseDTO[];
}

export interface WorkoutExerciseDTO {
  exerciseId: number;
  exerciseName: string;
  sets: number;
  reps: number | null;
  durationSeconds: number | null;
  restSeconds: number;
  notes: string | null;
}
