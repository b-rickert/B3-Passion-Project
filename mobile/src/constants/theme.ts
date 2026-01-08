export const colors = {
  background: {
    primary: '#09090b',
    secondary: '#18181b',
    tertiary: '#27272a',
    elevated: '#3f3f46',
  },
  
  text: {
    primary: '#ffffff',
    secondary: '#a1a1aa',
    muted: '#71717a',
    inverse: '#09090b',
  },
  
  orange: {
    DEFAULT: '#f97316',
    light: '#fb923c',
    dark: '#ea580c',
  },
  
  blue: {
    DEFAULT: '#3b82f6',
    light: '#60a5fa',
    dark: '#2563eb',
  },
  
  amber: {
    DEFAULT: '#f59e0b',
    light: '#fbbf24',
  },
  
  green: {
    DEFAULT: '#22c55e',
  },
  
  red: {
    DEFAULT: '#ef4444',
  },
  
  brick: {
    workout: '#E67E22',
    streakBonus: '#3b82f6',
    milestone: '#22c55e',
    inactive: '#3f3f46',
  },
};

export const typography = {
  size: {
    xs: 12,
    sm: 14,
    base: 16,
    lg: 18,
    xl: 20,
    '2xl': 24,
    '3xl': 30,
    '4xl': 36,
  },
  
  weight: {
    normal: '400',
    medium: '500',
    semibold: '600',
    bold: '700',
    extrabold: '800',
  },
};

export const spacing = {
  0: 0,
  1: 4,
  2: 8,
  3: 12,
  4: 16,
  5: 20,
  6: 24,
  8: 32,
  10: 40,
  12: 48,
  16: 64,
  20: 80,
};

export const borderRadius = {
  none: 0,
  sm: 4,
  DEFAULT: 8,
  md: 12,
  lg: 16,
  xl: 20,
  '2xl': 24,
  full: 9999,
};

export const SCREENS = {
  WELCOME: 'Welcome',
  GOALS: 'Goals',
  PROFILE_SETUP: 'ProfileSetup',
  HOME: 'Home',
  WORKOUTS: 'Workouts',
  PROGRESS: 'Progress',
  BRIX: 'Brix',
  PROFILE: 'Profile',
  WORKOUT_DETAIL: 'WorkoutDetail',
  ACTIVE_WORKOUT: 'ActiveWorkout',
  WORKOUT_COMPLETE: 'WorkoutComplete',
  DAILY_LOG: 'DailyLog',
  ANALYTICS: 'Analytics',
  NOTIFICATIONS: 'Notifications',
  SETTINGS: 'Settings',
  COMMUNITY: 'Community',
} as const;

export const TAB_BAR = {
  height: 80,
  iconSize: 24,
  labelSize: 12,
};

export const API = {
  DEFAULT_PROFILE_ID: 1,
  TIMEOUT: 10000,
};

export const MOOD_OPTIONS = [
  { value: 'GREAT', label: 'Great', emoji: '😄' },
  { value: 'GOOD', label: 'Good', emoji: '🙂' },
  { value: 'OKAY', label: 'Okay', emoji: '😐' },
  { value: 'LOW', label: 'Low', emoji: '😔' },
  { value: 'STRESSED', label: 'Stressed', emoji: '😰' },
] as const;

export const ENERGY_OPTIONS = [
  { value: 1, label: 'Low', emoji: '😴' },
  { value: 2, label: 'Okay', emoji: '😐' },
  { value: 3, label: 'Good', emoji: '🙂' },
  { value: 4, label: 'Great', emoji: '😀' },
  { value: 5, label: 'Pumped', emoji: '⚡' },
] as const;

export const FITNESS_LEVEL_OPTIONS = [
  { value: 'BEGINNER', label: 'Complete Beginner' },
  { value: 'INTERMEDIATE', label: 'Intermediate' },
  { value: 'ADVANCED', label: 'Advanced' },
] as const;

export const GOAL_OPTIONS = [
  { value: 'STRENGTH', label: 'Build Strength', description: 'Get stronger and more muscular', emoji: '💪' },
  { value: 'CARDIO', label: 'Improve Health', description: 'Feel better and more energized', emoji: '❤️' },
  { value: 'WEIGHT_LOSS', label: 'Lose Weight', description: 'Shed pounds sustainably', emoji: '🏃' },
  { value: 'FLEXIBILITY', label: 'Stay Consistent', description: 'Build a lasting habit', emoji: '🧘' },
] as const;

export const EQUIPMENT_OPTIONS = [
  { value: 'NONE', label: 'None' },
  { value: 'DUMBBELLS', label: 'Dumbbells' },
  { value: 'RESISTANCE_BANDS', label: 'Resistance Bands' },
  { value: 'FULL_GYM', label: 'Full Gym' },
] as const;

export const TIME_OPTIONS = [
  { value: 'MORNING', label: 'Morning' },
  { value: 'AFTERNOON', label: 'Afternoon' },
  { value: 'EVENING', label: 'Evening' },
  { value: 'FLEXIBLE', label: 'Flexible' },
] as const;

export default {
  colors,
  typography,
  spacing,
  borderRadius,
  SCREENS,
  TAB_BAR,
  API,
};