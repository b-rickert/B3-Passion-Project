import React, { useEffect, useState, useRef, useCallback } from 'react';
import { View, Text, ScrollView, TouchableOpacity, Alert, Vibration } from 'react-native';
import { useNavigation, useRoute, RouteProp } from '@react-navigation/native';
import { LinearGradient } from 'expo-linear-gradient';
import {
  X, Play, Pause, SkipForward, Check, Dumbbell,
  ChevronRight, RotateCcw, Flag,
  Target, Flame, CircleDot, Zap, TrendingUp, Activity, Sparkles, Heart
} from 'lucide-react-native';
import { colors, gradients, shadows, spacing, typography, radius } from '../constants/theme';
import { sessionApi, workoutApi } from '../services/api';
import { WorkoutSessionResponse, WorkoutExerciseDTO, WorkoutResponse } from '../types/api';
import B3Logo from '../components/B3Logo';

type WorkoutSessionRouteParams = {
  WorkoutSession: { workoutId: number };
};

type ExerciseProgress = {
  exerciseId: number;
  completedSets: number;
  totalSets: number;
  isComplete: boolean;
};

// Muscle group icons and colors for premium look
const getMuscleGroupStyle = (muscleGroup: string) => {
  const styles: Record<string, { icon: typeof Dumbbell; gradient: string[]; label: string }> = {
    'CHEST': { icon: Target, gradient: ['#F97316', '#EA580C'], label: 'Chest' },
    'BACK': { icon: TrendingUp, gradient: ['#3B82F6', '#2563EB'], label: 'Back' },
    'SHOULDERS': { icon: CircleDot, gradient: ['#8B5CF6', '#7C3AED'], label: 'Shoulders' },
    'BICEPS': { icon: Flame, gradient: ['#EF4444', '#DC2626'], label: 'Biceps' },
    'TRICEPS': { icon: Zap, gradient: ['#F59E0B', '#D97706'], label: 'Triceps' },
    'LEGS': { icon: Activity, gradient: ['#10B981', '#059669'], label: 'Legs' },
    'QUADS': { icon: Activity, gradient: ['#10B981', '#059669'], label: 'Quads' },
    'HAMSTRINGS': { icon: Activity, gradient: ['#14B8A6', '#0D9488'], label: 'Hamstrings' },
    'GLUTES': { icon: Activity, gradient: ['#EC4899', '#DB2777'], label: 'Glutes' },
    'CORE': { icon: Target, gradient: ['#F97316', '#EA580C'], label: 'Core' },
    'FULL_BODY': { icon: Sparkles, gradient: ['#6366F1', '#4F46E5'], label: 'Full Body' },
  };
  return styles[muscleGroup] || { icon: Dumbbell, gradient: ['#6B7280', '#4B5563'], label: muscleGroup };
};

export default function WorkoutSessionScreen() {
  const navigation = useNavigation();
  const route = useRoute<RouteProp<WorkoutSessionRouteParams, 'WorkoutSession'>>();
  const { workoutId } = route.params;

  // Session state
  const [session, setSession] = useState<WorkoutSessionResponse | null>(null);
  const [workout, setWorkout] = useState<WorkoutResponse | null>(null);
  const [exercises, setExercises] = useState<WorkoutExerciseDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Workout progress state
  const [currentExerciseIndex, setCurrentExerciseIndex] = useState(0);
  const [exerciseProgress, setExerciseProgress] = useState<ExerciseProgress[]>([]);
  const [isResting, setIsResting] = useState(false);
  const [restTimeRemaining, setRestTimeRemaining] = useState(0);
  const [workoutStartTime] = useState(Date.now());
  const [elapsedTime, setElapsedTime] = useState(0);
  const [isPaused, setIsPaused] = useState(false);

  // Timers
  const restTimerRef = useRef<NodeJS.Timeout | null>(null);
  const elapsedTimerRef = useRef<NodeJS.Timeout | null>(null);

  const currentExercise = exercises[currentExerciseIndex];
  const currentProgress = exerciseProgress[currentExerciseIndex];
  const totalExercises = exercises.length;
  const completedExercises = exerciseProgress.filter(p => p.isComplete).length;

  // Start session and load workout data
  useEffect(() => {
    const initSession = async () => {
      try {
        setLoading(true);

        // Load workout details and exercises
        const [workoutData, exercisesData] = await Promise.all([
          workoutApi.getWorkoutById(workoutId),
          workoutApi.getWorkoutExercises(workoutId),
        ]);

        setWorkout(workoutData);
        setExercises(exercisesData);

        // Initialize exercise progress
        const progress = exercisesData.map(ex => ({
          exerciseId: ex.exerciseId,
          completedSets: 0,
          totalSets: ex.sets,
          isComplete: false,
        }));
        setExerciseProgress(progress);

        // Start the session via API
        const sessionData = await sessionApi.startSession({
          profileId: 1,
          workoutId: workoutId,
        });
        setSession(sessionData);

      } catch (err) {
        console.error('Error starting session:', err);
        setError('Failed to start workout session');
      } finally {
        setLoading(false);
      }
    };

    initSession();

    return () => {
      if (restTimerRef.current) clearInterval(restTimerRef.current);
      if (elapsedTimerRef.current) clearInterval(elapsedTimerRef.current);
    };
  }, [workoutId]);

  // Elapsed time tracker
  useEffect(() => {
    if (!loading && !isPaused) {
      elapsedTimerRef.current = setInterval(() => {
        setElapsedTime(Math.floor((Date.now() - workoutStartTime) / 1000));
      }, 1000);
    }

    return () => {
      if (elapsedTimerRef.current) clearInterval(elapsedTimerRef.current);
    };
  }, [loading, isPaused, workoutStartTime]);

  // Rest timer
  useEffect(() => {
    if (isResting && restTimeRemaining > 0) {
      restTimerRef.current = setInterval(() => {
        setRestTimeRemaining(prev => {
          if (prev <= 1) {
            setIsResting(false);
            Vibration.vibrate(500);
            return 0;
          }
          return prev - 1;
        });
      }, 1000);
    }

    return () => {
      if (restTimerRef.current) clearInterval(restTimerRef.current);
    };
  }, [isResting, restTimeRemaining]);

  const formatTime = (seconds: number): string => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins}:${secs.toString().padStart(2, '0')}`;
  };

  const completeSet = useCallback(() => {
    if (!currentProgress || !currentExercise) return;

    const newCompletedSets = currentProgress.completedSets + 1;
    const isExerciseComplete = newCompletedSets >= currentProgress.totalSets;

    setExerciseProgress(prev =>
      prev.map((p, i) =>
        i === currentExerciseIndex
          ? { ...p, completedSets: newCompletedSets, isComplete: isExerciseComplete }
          : p
      )
    );

    // Start rest timer if not the last set
    if (!isExerciseComplete) {
      setRestTimeRemaining(currentExercise.restSeconds);
      setIsResting(true);
    } else if (currentExerciseIndex < totalExercises - 1) {
      // Move to next exercise after a brief delay
      setRestTimeRemaining(currentExercise.restSeconds);
      setIsResting(true);
    }
  }, [currentProgress, currentExercise, currentExerciseIndex, totalExercises]);

  const skipRest = () => {
    setIsResting(false);
    setRestTimeRemaining(0);
    if (restTimerRef.current) clearInterval(restTimerRef.current);
  };

  const nextExercise = () => {
    if (currentExerciseIndex < totalExercises - 1) {
      setCurrentExerciseIndex(prev => prev + 1);
      setIsResting(false);
      setRestTimeRemaining(0);
    }
  };

  const previousExercise = () => {
    if (currentExerciseIndex > 0) {
      setCurrentExerciseIndex(prev => prev - 1);
      setIsResting(false);
      setRestTimeRemaining(0);
    }
  };

  const togglePause = () => {
    setIsPaused(prev => !prev);
  };

  const finishWorkout = async () => {
    if (!session) return;

    Alert.alert(
      'Finish Workout?',
      `You've completed ${completedExercises}/${totalExercises} exercises. Ready to finish?`,
      [
        { text: 'Keep Going', style: 'cancel' },
        {
          text: 'Finish',
          onPress: async () => {
            try {
              const actualDuration = Math.floor(elapsedTime / 60);
              await sessionApi.completeSession(session.sessionId, {
                actualDuration: actualDuration > 0 ? actualDuration : 1,
                notes: `Completed ${completedExercises}/${totalExercises} exercises`,
              });

              Alert.alert(
                'Workout Complete!',
                'Great job! You just laid another brick.',
                [{ text: 'Nice!', onPress: () => navigation.goBack() }]
              );
            } catch (err) {
              console.error('Error completing session:', err);
              Alert.alert('Error', 'Failed to save workout. Please try again.');
            }
          },
        },
      ]
    );
  };

  if (loading) {
    return (
      <View style={{ flex: 1, backgroundColor: colors.background.end, justifyContent: 'center', alignItems: 'center' }}>
        <B3Logo size={80} />
        <Text style={{ color: colors.text.secondary, marginTop: spacing.lg }}>Starting workout...</Text>
      </View>
    );
  }

  if (error || !workout || !currentExercise) {
    return (
      <View style={{ flex: 1, backgroundColor: colors.background.end, justifyContent: 'center', alignItems: 'center', padding: spacing.xl }}>
        <Text style={{ color: colors.error.DEFAULT, fontSize: typography.sizes.lg, textAlign: 'center' }}>
          {error || 'Something went wrong'}
        </Text>
        <TouchableOpacity onPress={() => navigation.goBack()} style={{ marginTop: spacing.xl }}>
          <Text style={{ color: colors.orange.DEFAULT, fontSize: typography.sizes.base }}>Go Back</Text>
        </TouchableOpacity>
      </View>
    );
  }

  const muscleStyle = getMuscleGroupStyle(currentExercise.muscleGroup);
  const MuscleIcon = muscleStyle.icon;

  return (
    <View style={{ flex: 1, backgroundColor: colors.background.end }}>
      {/* Background */}
      <LinearGradient
        colors={[colors.background.start, colors.background.mid, colors.background.end]}
        style={{ position: 'absolute', top: 0, left: 0, right: 0, bottom: 0 }}
        pointerEvents="none"
      />
      <LinearGradient
        colors={[muscleStyle.gradient[0] + '20', 'transparent']}
        style={{ position: 'absolute', top: -50, right: -50, width: 300, height: 300, borderRadius: 150 }}
        pointerEvents="none"
      />

      {/* Header */}
      <View style={{
        paddingHorizontal: spacing.xl,
        paddingTop: 60,
        paddingBottom: spacing.md,
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
      }}>
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <View style={{
            backgroundColor: colors.background.card,
            padding: spacing.sm,
            borderRadius: radius.full,
            borderWidth: 1,
            borderColor: colors.background.glassBorder,
          }}>
            <X size={24} color={colors.text.secondary} />
          </View>
        </TouchableOpacity>

        {/* Timer Display */}
        <View style={{ alignItems: 'center' }}>
          <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs, letterSpacing: 1 }}>ELAPSED</Text>
          <Text style={{ color: colors.text.primary, fontSize: typography.sizes['2xl'], fontWeight: typography.weights.black }}>
            {formatTime(elapsedTime)}
          </Text>
        </View>

        <TouchableOpacity onPress={togglePause}>
          <View style={{
            backgroundColor: colors.background.card,
            padding: spacing.sm,
            borderRadius: radius.full,
            borderWidth: 1,
            borderColor: colors.background.glassBorder,
          }}>
            {isPaused ? (
              <Play size={24} color={colors.orange.DEFAULT} fill={colors.orange.DEFAULT} />
            ) : (
              <Pause size={24} color={colors.text.secondary} />
            )}
          </View>
        </TouchableOpacity>
      </View>

      {/* Progress Bar */}
      <View style={{ paddingHorizontal: spacing.xl, marginBottom: spacing.lg }}>
        <View style={{
          flexDirection: 'row',
          justifyContent: 'space-between',
          alignItems: 'center',
          marginBottom: spacing.sm
        }}>
          <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm }}>
            {workout.name}
          </Text>
          <Text style={{ color: colors.orange.DEFAULT, fontSize: typography.sizes.sm, fontWeight: typography.weights.bold }}>
            {currentExerciseIndex + 1}/{totalExercises}
          </Text>
        </View>
        <View style={{ height: 4, backgroundColor: colors.background.elevated, borderRadius: 2, overflow: 'hidden' }}>
          <LinearGradient
            colors={gradients.fire}
            start={{ x: 0, y: 0 }}
            end={{ x: 1, y: 0 }}
            style={{
              height: '100%',
              width: `${((currentExerciseIndex + (currentProgress?.isComplete ? 1 : 0)) / totalExercises) * 100}%`,
              borderRadius: 2,
            }}
          />
        </View>
      </View>

      <ScrollView
        style={{ flex: 1 }}
        contentContainerStyle={{ paddingHorizontal: spacing.xl, paddingBottom: 200 }}
        showsVerticalScrollIndicator={false}
      >
        {/* Rest Timer Overlay */}
        {isResting && (
          <View style={{
            backgroundColor: colors.background.card,
            borderRadius: radius['2xl'],
            padding: spacing.xl,
            marginBottom: spacing.xl,
            borderWidth: 2,
            borderColor: colors.blue.DEFAULT,
            alignItems: 'center',
          }}>
            <Text style={{ color: colors.blue.DEFAULT, fontSize: typography.sizes.sm, letterSpacing: 2, marginBottom: spacing.sm }}>
              REST TIME
            </Text>
            <Text style={{
              color: colors.text.primary,
              fontSize: 64,
              fontWeight: typography.weights.black,
              marginBottom: spacing.lg,
            }}>
              {formatTime(restTimeRemaining)}
            </Text>
            <TouchableOpacity onPress={skipRest}>
              <View style={{
                flexDirection: 'row',
                alignItems: 'center',
                backgroundColor: colors.background.elevated,
                paddingHorizontal: spacing.lg,
                paddingVertical: spacing.sm,
                borderRadius: radius.full,
              }}>
                <SkipForward size={18} color={colors.text.secondary} />
                <Text style={{ color: colors.text.secondary, marginLeft: spacing.sm }}>Skip Rest</Text>
              </View>
            </TouchableOpacity>
          </View>
        )}

        {/* Current Exercise Card - Premium Design */}
        <View style={{
          backgroundColor: colors.background.card,
          borderRadius: radius['2xl'],
          overflow: 'hidden',
          borderWidth: 1,
          borderColor: colors.background.glassBorder,
          ...shadows.card,
        }}>
          {/* Gradient accent bar at top */}
          <LinearGradient
            colors={muscleStyle.gradient}
            start={{ x: 0, y: 0 }}
            end={{ x: 1, y: 0 }}
            style={{ height: 4 }}
          />

          <View style={{ padding: spacing.xl }}>
            {/* Exercise Header with Icon */}
            <View style={{ alignItems: 'center', marginBottom: spacing.xl }}>
              <View style={{
                width: 80,
                height: 80,
                borderRadius: 40,
                backgroundColor: muscleStyle.gradient[0] + '15',
                justifyContent: 'center',
                alignItems: 'center',
                marginBottom: spacing.md,
              }}>
                <MuscleIcon size={40} color={muscleStyle.gradient[0]} />
              </View>

              <Text style={{
                color: colors.text.primary,
                fontSize: typography.sizes['2xl'],
                fontWeight: typography.weights.black,
                textAlign: 'center',
              }}>
                {currentExercise.name}
              </Text>

              <View style={{ flexDirection: 'row', alignItems: 'center', marginTop: spacing.sm, gap: spacing.sm }}>
                <Text style={{
                  color: muscleStyle.gradient[0],
                  fontSize: typography.sizes.sm,
                  fontWeight: typography.weights.semibold,
                }}>
                  {muscleStyle.label}
                </Text>
                <Text style={{ color: colors.text.muted }}>•</Text>
                <Text style={{
                  color: colors.text.muted,
                  fontSize: typography.sizes.sm,
                }}>
                  {currentExercise.equipmentType.replace('_', ' ')}
                </Text>
              </View>
            </View>

            {/* Stats Grid */}
            <View style={{
              flexDirection: 'row',
              backgroundColor: colors.background.elevated,
              borderRadius: radius.xl,
              padding: spacing.lg,
              marginBottom: spacing.lg,
            }}>
              <View style={{ flex: 1, alignItems: 'center' }}>
                <Text style={{ color: colors.text.muted, fontSize: 10, letterSpacing: 1, fontWeight: typography.weights.medium }}>SETS</Text>
                <Text style={{ marginTop: spacing.xs }}>
                  <Text style={{ color: colors.orange.DEFAULT, fontSize: typography.sizes['3xl'], fontWeight: typography.weights.black }}>
                    {currentProgress?.completedSets || 0}
                  </Text>
                  <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.xl, fontWeight: typography.weights.bold }}>
                    /{currentExercise.sets}
                  </Text>
                </Text>
              </View>

              {currentExercise.reps && (
                <View style={{ flex: 1, alignItems: 'center', borderLeftWidth: 1, borderLeftColor: colors.background.glassBorder }}>
                  <Text style={{ color: colors.text.muted, fontSize: 10, letterSpacing: 1, fontWeight: typography.weights.medium }}>REPS</Text>
                  <Text style={{ color: colors.text.primary, fontSize: typography.sizes['3xl'], fontWeight: typography.weights.black, marginTop: spacing.xs }}>
                    {currentExercise.reps}
                  </Text>
                </View>
              )}

              {currentExercise.durationSeconds && (
                <View style={{ flex: 1, alignItems: 'center', borderLeftWidth: 1, borderLeftColor: colors.background.glassBorder }}>
                  <Text style={{ color: colors.text.muted, fontSize: 10, letterSpacing: 1, fontWeight: typography.weights.medium }}>TIME</Text>
                  <Text style={{ color: colors.text.primary, fontSize: typography.sizes['3xl'], fontWeight: typography.weights.black, marginTop: spacing.xs }}>
                    {currentExercise.durationSeconds}s
                  </Text>
                </View>
              )}

              <View style={{ flex: 1, alignItems: 'center', borderLeftWidth: 1, borderLeftColor: colors.background.glassBorder }}>
                <Text style={{ color: colors.text.muted, fontSize: 10, letterSpacing: 1, fontWeight: typography.weights.medium }}>REST</Text>
                <Text style={{ color: colors.text.secondary, fontSize: typography.sizes['3xl'], fontWeight: typography.weights.black, marginTop: spacing.xs }}>
                  {currentExercise.restSeconds}s
                </Text>
              </View>
            </View>

            {/* Description */}
            {currentExercise.description && (
              <View style={{
                backgroundColor: colors.background.elevated,
                padding: spacing.md,
                borderRadius: radius.lg,
                marginBottom: spacing.md,
              }}>
                <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm, lineHeight: 20, textAlign: 'center' }}>
                  {currentExercise.description}
                </Text>
              </View>
            )}

            {/* Notes */}
            {currentExercise.notes && (
              <View style={{
                backgroundColor: colors.amber.DEFAULT + '15',
                padding: spacing.md,
                borderRadius: radius.lg,
                borderLeftWidth: 3,
                borderLeftColor: colors.amber.DEFAULT,
              }}>
                <Text style={{ color: colors.amber.DEFAULT, fontSize: typography.sizes.sm, fontWeight: typography.weights.medium }}>
                  {currentExercise.notes}
                </Text>
              </View>
            )}
          </View>
        </View>

        {/* Exercise Navigation */}
        <View style={{
          flexDirection: 'row',
          justifyContent: 'space-between',
          marginTop: spacing.xl,
          gap: spacing.md,
        }}>
          <TouchableOpacity
            onPress={previousExercise}
            disabled={currentExerciseIndex === 0}
            style={{ flex: 1 }}
          >
            <View style={{
              backgroundColor: colors.background.card,
              paddingVertical: spacing.md,
              borderRadius: radius.xl,
              alignItems: 'center',
              opacity: currentExerciseIndex === 0 ? 0.5 : 1,
              borderWidth: 1,
              borderColor: colors.background.glassBorder,
            }}>
              <RotateCcw size={20} color={colors.text.secondary} />
              <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm, marginTop: spacing.xs }}>
                Previous
              </Text>
            </View>
          </TouchableOpacity>

          <TouchableOpacity
            onPress={nextExercise}
            disabled={currentExerciseIndex === totalExercises - 1}
            style={{ flex: 1 }}
          >
            <View style={{
              backgroundColor: colors.background.card,
              paddingVertical: spacing.md,
              borderRadius: radius.xl,
              alignItems: 'center',
              opacity: currentExerciseIndex === totalExercises - 1 ? 0.5 : 1,
              borderWidth: 1,
              borderColor: colors.background.glassBorder,
            }}>
              <ChevronRight size={20} color={colors.text.secondary} />
              <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm, marginTop: spacing.xs }}>
                Next
              </Text>
            </View>
          </TouchableOpacity>
        </View>
      </ScrollView>

      {/* Bottom Action Buttons */}
      <View style={{
        position: 'absolute',
        bottom: 0,
        left: 0,
        right: 0,
        padding: spacing.xl,
        paddingBottom: 34,
        backgroundColor: colors.background.card + 'F5',
        borderTopWidth: 1,
        borderTopColor: colors.background.glassBorder,
      }}>
        {currentProgress?.isComplete ? (
          // Exercise complete - show next or finish
          currentExerciseIndex < totalExercises - 1 ? (
            <TouchableOpacity onPress={nextExercise} activeOpacity={0.9}>
              <LinearGradient
                colors={gradients.fire}
                start={{ x: 0, y: 0 }}
                end={{ x: 1, y: 0 }}
                style={{
                  borderRadius: radius.xl,
                  paddingVertical: spacing.lg,
                  flexDirection: 'row',
                  alignItems: 'center',
                  justifyContent: 'center',
                  ...shadows.glow,
                }}
              >
                <ChevronRight size={22} color="#fff" />
                <Text style={{ color: '#fff', fontSize: typography.sizes.lg, fontWeight: typography.weights.bold, marginLeft: spacing.sm }}>
                  Next Exercise
                </Text>
              </LinearGradient>
            </TouchableOpacity>
          ) : (
            <TouchableOpacity onPress={finishWorkout} activeOpacity={0.9}>
              <LinearGradient
                colors={[colors.green.DEFAULT, colors.green.light]}
                start={{ x: 0, y: 0 }}
                end={{ x: 1, y: 0 }}
                style={{
                  borderRadius: radius.xl,
                  paddingVertical: spacing.lg,
                  flexDirection: 'row',
                  alignItems: 'center',
                  justifyContent: 'center',
                  ...shadows.glow,
                }}
              >
                <Flag size={22} color="#fff" />
                <Text style={{ color: '#fff', fontSize: typography.sizes.lg, fontWeight: typography.weights.bold, marginLeft: spacing.sm }}>
                  Finish Workout
                </Text>
              </LinearGradient>
            </TouchableOpacity>
          )
        ) : (
          // Show complete set button
          <TouchableOpacity onPress={completeSet} activeOpacity={0.9} disabled={isResting}>
            <LinearGradient
              colors={isResting ? [colors.background.elevated, colors.background.card] : gradients.fire}
              start={{ x: 0, y: 0 }}
              end={{ x: 1, y: 0 }}
              style={{
                borderRadius: radius.xl,
                paddingVertical: spacing.lg,
                flexDirection: 'row',
                alignItems: 'center',
                justifyContent: 'center',
                ...shadows.glow,
                opacity: isResting ? 0.6 : 1,
              }}
            >
              <Check size={22} color="#fff" />
              <Text style={{ color: '#fff', fontSize: typography.sizes.lg, fontWeight: typography.weights.bold, marginLeft: spacing.sm }}>
                Complete Set {(currentProgress?.completedSets || 0) + 1}/{currentExercise.sets}
              </Text>
            </LinearGradient>
          </TouchableOpacity>
        )}
      </View>
    </View>
  );
}
