import React, { useEffect, useState } from 'react';
import { View, Text, ScrollView, TouchableOpacity, Image } from 'react-native';
import { useNavigation, useRoute, RouteProp } from '@react-navigation/native';
import { LinearGradient } from 'expo-linear-gradient';
import {
  ChevronLeft, Clock, Dumbbell, Heart, Sparkles, Zap, Play, ListChecks, Timer,
  Target, Flame, CircleDot, Repeat, TrendingUp, Activity
} from 'lucide-react-native';
import { colors, gradients, shadows, spacing, typography, radius } from '../constants/theme';
import { workoutApi } from '../services/api';
import { WorkoutResponse, WorkoutExerciseDTO } from '../types/api';
import B3Logo from '../components/B3Logo';

type WorkoutDetailRouteParams = {
  WorkoutDetail: { workoutId: number };
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

export default function WorkoutDetailScreen() {
  const navigation = useNavigation();
  const route = useRoute<RouteProp<WorkoutDetailRouteParams, 'WorkoutDetail'>>();
  const { workoutId } = route.params;

  const [workout, setWorkout] = useState<WorkoutResponse | null>(null);
  const [exercises, setExercises] = useState<WorkoutExerciseDTO[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadData = async () => {
      try {
        const [workoutData, exercisesData] = await Promise.all([
          workoutApi.getWorkoutById(workoutId),
          workoutApi.getWorkoutExercises(workoutId),
        ]);
        setWorkout(workoutData);
        setExercises(exercisesData);
      } catch (error) {
        console.error('Error loading workout:', error);
      } finally {
        setLoading(false);
      }
    };
    loadData();
  }, [workoutId]);

  const getDifficultyColor = (level: string) => {
    switch (level) {
      case 'BEGINNER': return colors.green.DEFAULT;
      case 'INTERMEDIATE': return colors.amber.DEFAULT;
      case 'ADVANCED': return colors.error.DEFAULT;
      default: return colors.text.muted;
    }
  };

  const getTypeIcon = (type: string) => {
    switch (type) {
      case 'STRENGTH': return Dumbbell;
      case 'CARDIO': return Heart;
      case 'FLEXIBILITY': return Sparkles;
      default: return Zap;
    }
  };

  if (loading) {
    return (
      <View style={{ flex: 1, backgroundColor: colors.background.end, justifyContent: 'center', alignItems: 'center' }}>
        <B3Logo size={80} />
      </View>
    );
  }

  if (!workout) {
    return (
      <View style={{ flex: 1, backgroundColor: colors.background.end, justifyContent: 'center', alignItems: 'center' }}>
        <Text style={{ color: colors.text.secondary }}>Workout not found</Text>
      </View>
    );
  }

  const TypeIcon = getTypeIcon(workout.workoutType);

  return (
    <View style={{ flex: 1, backgroundColor: colors.background.end }}>
      {/* Background */}
      <View style={{ position: 'absolute', top: 0, left: 0, right: 0, bottom: 0 }}>
        <LinearGradient colors={[colors.background.start, colors.background.mid, colors.background.end]} style={{ position: 'absolute', top: 0, left: 0, right: 0, bottom: 0 }} />
        <LinearGradient colors={['rgba(249, 115, 22, 0.15)', 'transparent']} style={{ position: 'absolute', top: -100, right: -100, width: 350, height: 350, borderRadius: 175 }} />
        <LinearGradient colors={['rgba(59, 130, 246, 0.08)', 'transparent']} style={{ position: 'absolute', bottom: 100, left: -100, width: 300, height: 300, borderRadius: 150 }} />
      </View>

      <ScrollView style={{ flex: 1 }} contentContainerStyle={{ paddingBottom: 120 }} showsVerticalScrollIndicator={false}>
        {/* Header */}
        <View style={{ paddingHorizontal: spacing.xl, paddingTop: 60 }}>
          {/* Back Button */}
          <TouchableOpacity onPress={() => navigation.goBack()} style={{ marginBottom: spacing.xl }}>
            <View style={{ flexDirection: 'row', alignItems: 'center' }}>
              <ChevronLeft size={24} color={colors.text.secondary} />
              <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.base, marginLeft: spacing.xs }}>Back</Text>
            </View>
          </TouchableOpacity>

          <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'flex-start' }}>
            <View style={{ flexDirection: 'row', alignItems: 'center', flex: 1 }}>
              <LinearGradient colors={gradients.fire} style={{ width: 64, height: 64, borderRadius: radius.xl, justifyContent: 'center', alignItems: 'center', marginRight: spacing.lg, ...shadows.glow }}>
                <TypeIcon size={32} color="#fff" />
              </LinearGradient>
              <View style={{ flex: 1 }}>
                <Text style={{ color: colors.text.primary, fontSize: typography.sizes['2xl'], fontWeight: typography.weights.black }}>{workout.name}</Text>
                <View style={{ flexDirection: 'row', alignItems: 'center', marginTop: spacing.xs }}>
                  <Clock size={14} color={colors.text.muted} />
                  <Text style={{ color: colors.text.muted, fontSize: typography.sizes.sm, marginLeft: spacing.xs }}>{workout.estimatedDuration} min</Text>
                  <View style={{ width: 4, height: 4, borderRadius: 2, backgroundColor: colors.text.muted, marginHorizontal: spacing.sm }} />
                  <View style={{ width: 8, height: 8, borderRadius: 4, backgroundColor: getDifficultyColor(workout.difficultyLevel), marginRight: spacing.xs }} />
                  <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm }}>
                    {workout.difficultyLevel.charAt(0) + workout.difficultyLevel.slice(1).toLowerCase()}
                  </Text>
                </View>
              </View>
            </View>
            <B3Logo size={48} />
          </View>
        </View>

        {/* Description */}
        {workout.description && (
          <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing.xl }}>
            <View style={{ backgroundColor: colors.background.card, borderRadius: radius.xl, padding: spacing.lg, borderWidth: 1, borderColor: colors.background.glassBorder }}>
              <Text style={{ color: colors.text.primary, fontSize: typography.sizes.base, lineHeight: 22 }}>{workout.description}</Text>
            </View>
          </View>
        )}

        {/* Quick Stats */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing.xl }}>
          <View style={{ flexDirection: 'row', gap: spacing.md }}>
            {[
              { icon: ListChecks, value: exercises.length, label: 'Exercises', color: colors.blue.DEFAULT },
              { icon: Timer, value: workout.estimatedDuration, label: 'Minutes', color: colors.orange.DEFAULT },
              { icon: Dumbbell, value: workout.requiredEquipment || 'None', label: 'Equipment', color: colors.amber.DEFAULT, isText: true },
            ].map((stat, i) => (
              <View key={i} style={{ flex: 1, backgroundColor: colors.background.card, borderRadius: radius.xl, padding: spacing.lg, alignItems: 'center', borderWidth: 1, borderColor: colors.background.glassBorder, ...shadows.card }}>
                <View style={{ backgroundColor: stat.color + '20', width: 40, height: 40, borderRadius: radius.md, justifyContent: 'center', alignItems: 'center', marginBottom: spacing.sm }}>
                  <stat.icon size={20} color={stat.color} />
                </View>
                <Text style={{ color: colors.text.primary, fontSize: stat.isText ? typography.sizes.sm : typography.sizes['2xl'], fontWeight: typography.weights.black, textAlign: 'center' }}>{stat.value}</Text>
                <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs, marginTop: 2 }}>{stat.label}</Text>
              </View>
            ))}
          </View>
        </View>

        {/* Exercises List */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing['2xl'] }}>
          <Text style={{ color: colors.text.primary, fontSize: typography.sizes.xl, fontWeight: typography.weights.bold, marginBottom: spacing.lg }}>
            Exercises
          </Text>

          <View style={{ gap: spacing.sm }}>
            {exercises.map((exercise, index) => {
              const muscleStyle = getMuscleGroupStyle(exercise.muscleGroup);
              const MuscleIcon = muscleStyle.icon;

              return (
                <View
                  key={exercise.exerciseId}
                  style={{
                    backgroundColor: colors.background.card,
                    borderRadius: radius.lg,
                    overflow: 'hidden',
                    borderWidth: 1,
                    borderColor: colors.background.glassBorder,
                  }}
                >
                  {/* Gradient accent bar */}
                  <LinearGradient
                    colors={muscleStyle.gradient}
                    start={{ x: 0, y: 0 }}
                    end={{ x: 1, y: 0 }}
                    style={{ height: 3 }}
                  />

                  <View style={{ padding: spacing.md }}>
                    <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                      {/* Exercise Image or Icon */}
                      {exercise.imageUrl ? (
                        <View style={{
                          width: 56,
                          height: 56,
                          borderRadius: radius.lg,
                          overflow: 'hidden',
                          marginRight: spacing.md,
                          borderWidth: 1,
                          borderColor: muscleStyle.gradient[0] + '30',
                        }}>
                          <Image
                            source={{ uri: exercise.imageUrl }}
                            style={{ width: '100%', height: '100%' }}
                            resizeMode="cover"
                          />
                        </View>
                      ) : (
                        <View style={{
                          width: 48,
                          height: 48,
                          borderRadius: radius.md,
                          backgroundColor: muscleStyle.gradient[0] + '15',
                          justifyContent: 'center',
                          alignItems: 'center',
                          marginRight: spacing.md,
                        }}>
                          <MuscleIcon size={24} color={muscleStyle.gradient[0]} />
                        </View>
                      )}

                      {/* Exercise Info */}
                      <View style={{ flex: 1 }}>
                        <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' }}>
                          <Text style={{
                            color: colors.text.primary,
                            fontSize: typography.sizes.base,
                            fontWeight: typography.weights.semibold,
                            flex: 1,
                          }}>
                            {exercise.name}
                          </Text>
                          <View style={{
                            backgroundColor: colors.background.elevated,
                            paddingHorizontal: spacing.sm,
                            paddingVertical: 2,
                            borderRadius: radius.full,
                          }}>
                            <Text style={{
                              color: colors.text.muted,
                              fontSize: typography.sizes.xs,
                              fontWeight: typography.weights.medium,
                            }}>
                              #{index + 1}
                            </Text>
                          </View>
                        </View>

                        {/* Tags */}
                        <View style={{ flexDirection: 'row', alignItems: 'center', marginTop: spacing.xs, gap: spacing.xs }}>
                          <Text style={{
                            color: muscleStyle.gradient[0],
                            fontSize: typography.sizes.xs,
                            fontWeight: typography.weights.medium,
                          }}>
                            {muscleStyle.label}
                          </Text>
                          <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs }}>•</Text>
                          <Text style={{
                            color: colors.text.muted,
                            fontSize: typography.sizes.xs,
                          }}>
                            {exercise.equipmentType.replace('_', ' ')}
                          </Text>
                        </View>
                      </View>
                    </View>

                    {/* Stats Row */}
                    <View style={{
                      flexDirection: 'row',
                      marginTop: spacing.md,
                      paddingTop: spacing.md,
                      borderTopWidth: 1,
                      borderTopColor: colors.background.glassBorder,
                      gap: spacing.lg,
                    }}>
                      <View style={{ alignItems: 'center' }}>
                        <Text style={{ color: colors.text.muted, fontSize: 10, letterSpacing: 1, fontWeight: typography.weights.medium }}>SETS</Text>
                        <Text style={{ color: colors.orange.DEFAULT, fontSize: typography.sizes.lg, fontWeight: typography.weights.black, marginTop: 2 }}>{exercise.sets}</Text>
                      </View>

                      {exercise.reps && (
                        <View style={{ alignItems: 'center' }}>
                          <Text style={{ color: colors.text.muted, fontSize: 10, letterSpacing: 1, fontWeight: typography.weights.medium }}>REPS</Text>
                          <Text style={{ color: colors.text.primary, fontSize: typography.sizes.lg, fontWeight: typography.weights.black, marginTop: 2 }}>{exercise.reps}</Text>
                        </View>
                      )}

                      {exercise.durationSeconds && (
                        <View style={{ alignItems: 'center' }}>
                          <Text style={{ color: colors.text.muted, fontSize: 10, letterSpacing: 1, fontWeight: typography.weights.medium }}>TIME</Text>
                          <Text style={{ color: colors.text.primary, fontSize: typography.sizes.lg, fontWeight: typography.weights.black, marginTop: 2 }}>{exercise.durationSeconds}s</Text>
                        </View>
                      )}

                      <View style={{ alignItems: 'center' }}>
                        <Text style={{ color: colors.text.muted, fontSize: 10, letterSpacing: 1, fontWeight: typography.weights.medium }}>REST</Text>
                        <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.lg, fontWeight: typography.weights.black, marginTop: 2 }}>{exercise.restSeconds}s</Text>
                      </View>

                      {/* Spacer to push notes to right */}
                      <View style={{ flex: 1 }} />

                      {exercise.notes && (
                        <View style={{
                          backgroundColor: colors.amber.DEFAULT + '15',
                          paddingHorizontal: spacing.sm,
                          paddingVertical: spacing.xs,
                          borderRadius: radius.sm,
                          maxWidth: 120,
                        }}>
                          <Text style={{ color: colors.amber.DEFAULT, fontSize: 10, fontWeight: typography.weights.medium }} numberOfLines={1}>
                            {exercise.notes}
                          </Text>
                        </View>
                      )}
                    </View>
                  </View>
                </View>
              );
            })}
          </View>
        </View>
      </ScrollView>

      {/* Fixed Bottom Button */}
      <View
        style={{
          position: 'absolute',
          bottom: 0,
          left: 0,
          right: 0,
          padding: spacing.xl,
          paddingBottom: 34,
          backgroundColor: colors.background.card + 'F5',
          borderTopWidth: 1,
          borderTopColor: colors.background.glassBorder,
        }}
      >
        <TouchableOpacity onPress={() => navigation.navigate('WorkoutSession' as never, { workoutId } as never)} activeOpacity={0.9}>
          <LinearGradient
            colors={gradients.fire}
            start={{ x: 0, y: 0 }}
            end={{ x: 1, y: 0 }}
            style={{ borderRadius: radius.xl, paddingVertical: spacing.lg, flexDirection: 'row', alignItems: 'center', justifyContent: 'center', ...shadows.glow }}
          >
            <Play size={22} color="#fff" fill="#fff" />
            <Text style={{ color: '#fff', fontSize: typography.sizes.lg, fontWeight: typography.weights.bold, marginLeft: spacing.sm }}>Start Workout</Text>
          </LinearGradient>
        </TouchableOpacity>
      </View>
    </View>
  );
}
