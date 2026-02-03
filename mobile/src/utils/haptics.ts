import * as Haptics from 'expo-haptics';

/**
 * Haptic feedback utility for B3
 * Provides consistent haptic patterns throughout the app
 */

// Light tap - for selections, toggles, small actions
export const lightTap = () => {
  Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light);
};

// Medium tap - for completing sets, navigation
export const mediumTap = () => {
  Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium);
};

// Heavy tap - for important completions
export const heavyTap = () => {
  Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Heavy);
};

// Selection feedback - for picker/slider selections
export const selection = () => {
  Haptics.selectionAsync();
};

// Success pattern - for workout completion, achievements, check-in submission
export const success = async () => {
  await Haptics.notificationAsync(Haptics.NotificationFeedbackType.Success);
};

// Warning pattern - for alerts, confirmations
export const warning = () => {
  Haptics.notificationAsync(Haptics.NotificationFeedbackType.Warning);
};

// Error pattern - for failures
export const error = () => {
  Haptics.notificationAsync(Haptics.NotificationFeedbackType.Error);
};

// Brick placed pattern - custom sequence for placing a brick
export const brickPlaced = async () => {
  await Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Heavy);
  setTimeout(() => {
    Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light);
  }, 100);
};

// Streak milestone pattern - celebratory sequence
export const streakMilestone = async () => {
  await Haptics.notificationAsync(Haptics.NotificationFeedbackType.Success);
  setTimeout(() => {
    Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium);
  }, 150);
  setTimeout(() => {
    Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light);
  }, 300);
};

// Set complete pattern - satisfying tap for completing a set
export const setComplete = () => {
  Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium);
};

// Rest timer end - alert that rest is over
export const restTimerEnd = () => {
  Haptics.notificationAsync(Haptics.NotificationFeedbackType.Warning);
};

export default {
  lightTap,
  mediumTap,
  heavyTap,
  selection,
  success,
  warning,
  error,
  brickPlaced,
  streakMilestone,
  setComplete,
  restTimerEnd,
};
