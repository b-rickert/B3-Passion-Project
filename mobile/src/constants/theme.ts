// B3 Theme - Industrial Warmth (Orange/Blue/Dark Gray)
export const colors = {
  orange: {
    DEFAULT: '#f97316',
    light: '#fb923c',
    dark: '#ea580c',
    vibrant: '#ff6b00',
    neon: '#f97316',
    muted: 'rgba(249, 115, 22, 0.15)',
  },
  blue: {
    DEFAULT: '#3b82f6',
    light: '#60a5fa',
    dark: '#2563eb',
    cyan: '#3b82f6',
    electric: '#60a5fa',
    muted: 'rgba(59, 130, 246, 0.15)',
  },
  purple: {
    DEFAULT: '#3b82f6',
    light: '#60a5fa',
    dark: '#2563eb',
  },
  pink: {
    DEFAULT: '#f97316',
    light: '#fb923c',
  },
  background: {
    start: '#09090b',
    mid: '#0c0c0f',
    end: '#09090b',
    card: '#18181b',
    cardHover: '#27272a',
    glass: '#18181b',
    glassBorder: '#27272a',
    glassLight: '#3f3f46',
    primary: '#09090b',
    secondary: '#18181b',
    tertiary: '#27272a',
    elevated: '#27272a',
  },
  text: {
    primary: '#ffffff',
    secondary: '#a1a1aa',
    muted: '#71717a',
    inverse: '#09090b',
  },
  success: { DEFAULT: '#22c55e', muted: 'rgba(34, 197, 94, 0.15)' },
  warning: { DEFAULT: '#f59e0b', muted: 'rgba(245, 158, 11, 0.15)' },
  error: { DEFAULT: '#ef4444', muted: 'rgba(239, 68, 68, 0.15)' },
  red: { DEFAULT: '#ef4444', light: '#f87171' },
  amber: { DEFAULT: '#f59e0b', light: '#fbbf24' },
  green: { DEFAULT: '#22c55e', light: '#4ade80' },
  cyan: { DEFAULT: '#3b82f6' },
};

export const gradients = {
  fire: ['#fb923c', '#f97316', '#ea580c'] as const,
  ice: ['#60a5fa', '#3b82f6', '#2563eb'] as const,
  aurora: ['#f97316', '#ea580c'] as const,
  sunset: ['#f97316', '#ea580c'] as const,
  ocean: ['#f97316', '#ea580c'] as const,
  
  meshPurple: '#3b82f6',
  meshOrange: '#f97316',
  meshBlue: '#3b82f6',
  meshCyan: '#3b82f6',
  
  streakCold: ['#3b82f6', '#60a5fa'] as const,
  streakWarm: ['#60a5fa', '#f59e0b'] as const,
  streakHot: ['#f59e0b', '#f97316'] as const,
  streakFire: ['#f97316', '#ea580c', '#dc2626'] as const,
  
  cardGlass: ['#18181b', '#18181b'] as const,
};

export const spacing = { xs: 4, sm: 8, md: 12, lg: 16, xl: 20, '2xl': 24, '3xl': 32, '4xl': 40, '5xl': 48, '6xl': 64 };
export const radius = { sm: 10, md: 14, lg: 18, xl: 24, '2xl': 32, '3xl': 40, full: 9999 };

export const typography = {
  sizes: { xs: 10, sm: 12, base: 14, lg: 16, xl: 20, '2xl': 28, '3xl': 38, '4xl': 52, '5xl': 68 },
  weights: { normal: '400' as const, medium: '500' as const, semibold: '600' as const, bold: '700' as const, extrabold: '800' as const, black: '900' as const },
  tracking: { tighter: -1.5, tight: -0.5, normal: 0, wide: 1 },
};

export const shadows = {
  glow: { shadowColor: '#f97316', shadowOffset: { width: 0, height: 0 }, shadowOpacity: 0.4, shadowRadius: 20, elevation: 12 },
  glowBlue: { shadowColor: '#3b82f6', shadowOffset: { width: 0, height: 0 }, shadowOpacity: 0.3, shadowRadius: 16, elevation: 10 },
  card: { shadowColor: '#000', shadowOffset: { width: 0, height: 8 }, shadowOpacity: 0.4, shadowRadius: 16, elevation: 15 },
  float: { shadowColor: '#000', shadowOffset: { width: 0, height: 16 }, shadowOpacity: 0.5, shadowRadius: 24, elevation: 20 },
};

export const TAB_BAR = { height: 70, iconSize: 22, labelSize: 9 };

export default { colors, gradients, spacing, radius, typography, shadows, TAB_BAR };
