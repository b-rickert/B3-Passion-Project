// B3 Design System - Premium Dark Theme with Orange/Blue Accent System

export const colors = {
  // Background layers
  background: {
    start: '#1a1216',      // Dark purple-brown
    mid: '#130f12',        // Deeper
    end: '#0a0809',        // Near black
    card: 'rgba(255, 255, 255, 0.05)',
    glass: 'rgba(255, 255, 255, 0.08)',
    glassBorder: 'rgba(255, 255, 255, 0.12)',
  },

  // Text hierarchy
  text: {
    primary: '#ffffff',
    secondary: 'rgba(255, 255, 255, 0.7)',
    muted: 'rgba(255, 255, 255, 0.5)',
    inverse: '#0a0809',
  },

  // Orange system (warm actions)
  orange: {
    DEFAULT: '#f97316',
    light: '#fb923c',
    dark: '#ea580c',
    muted: 'rgba(249, 115, 22, 0.2)',
    neon: '#ff8c00',
  },
  
  // Blue system (cool actions)
  blue: {
    DEFAULT: '#3b82f6',
    light: '#60a5fa',
    dark: '#2563eb',
    muted: 'rgba(59, 130, 246, 0.2)',
  },
  
  // Accent colors
  amber: {
    DEFAULT: '#f59e0b',
    light: '#fbbf24',
  },
  
  green: {
    DEFAULT: '#22c55e',
    light: '#4ade80',
  },
  
  red: {
    DEFAULT: '#ef4444',
    light: '#f87171',
  },

  // Error alias for red
  error: {
    DEFAULT: '#ef4444',
    light: '#f87171',
  },
  
  purple: {
    DEFAULT: '#a855f7',
    light: '#c084fc',
  },
};

export const gradients = {
  // Primary gradients
  fire: ['#f97316', '#ea580c'] as const,
  ocean: ['#3b82f6', '#2563eb'] as const,
  sunset: ['#f97316', '#f59e0b'] as const,
  
  // Streak gradients (dynamic based on progress)
  streakCold: ['#3b82f6', '#2563eb'] as const,
  streakWarm: ['#f59e0b', '#f97316'] as const,
  streakHot: ['#f97316', '#ea580c'] as const,
  streakFire: ['#ef4444', '#f97316'] as const,
  
  // Background orbs
  orangeOrb: ['rgba(249, 115, 22, 0.25)', 'transparent'] as const,
  blueOrb: ['rgba(59, 130, 246, 0.15)', 'transparent'] as const,
};

export const shadows = {
  glow: {
    shadowColor: '#f97316',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.3,
    shadowRadius: 12,
    elevation: 8,
  },
  card: {
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.25,
    shadowRadius: 8,
    elevation: 4,
  },
  soft: {
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.15,
    shadowRadius: 4,
    elevation: 2,
  },
  float: {
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 8 },
    shadowOpacity: 0.3,
    shadowRadius: 16,
    elevation: 12,
  },
};

export const spacing = {
  xs: 4,
  sm: 8,
  md: 12,
  lg: 16,
  xl: 24,
  '2xl': 32,
  '3xl': 48,
  '4xl': 64,
};

export const radius = {
  sm: 6,
  md: 8,
  lg: 12,
  xl: 16,
  '2xl': 24,
  full: 9999,
};

export const typography = {
  sizes: {
    xs: 11,
    sm: 13,
    base: 15,
    lg: 17,
    xl: 20,
    '2xl': 24,
    '3xl': 30,
    '4xl': 36,
    '5xl': 48,
  },
  weights: {
    normal: '400' as const,
    medium: '500' as const,
    semibold: '600' as const,
    bold: '700' as const,
    extrabold: '800' as const,
    black: '900' as const,
  },
  tracking: {
    tighter: -1,
    tight: -0.5,
    normal: 0,
    wide: 0.5,
    wider: 1,
    widest: 2,
  },
};
