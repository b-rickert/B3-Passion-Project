import React, { useEffect, useState } from 'react';
import { View, Text, ScrollView, TouchableOpacity, ActivityIndicator, Image } from 'react-native';
import { useNavigation, useRoute, RouteProp } from '@react-navigation/native';
import { LinearGradient } from 'expo-linear-gradient';
import { ChevronLeft, Clock, Dumbbell, Heart, Sparkles, Zap, Play, ListChecks, Timer } from 'lucide-react-native';
import { colors, gradients, shadows, spacing, typography, radius } from '../constants/theme';
import { workoutApi } from '../services/api';
import { WorkoutResponse, WorkoutExerciseDTO } from '../types/api';
import B3Logo from '../components/B3Logo';

type WorkoutDetailRouteParams = {
  WorkoutDetail: { workoutId: number };
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
        <LinearGradient colors={['rgba(249, 115, 22, 0.2)', 'transparent']} style={{ position: 'absolute', top: -100, right: -100, width: 350, height: 350, borderRadius: 175 }} />
        <LinearGradient colors={['rgba(59, 130, 246, 0.1)', 'transparent']} style={{ position: 'absolute', bottom: 100, left: -100, width: 300, height: 300, borderRadius: 150 }} />
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
            Exercises ({exercises.length})
          </Text>

          <View style={{ gap: spacing.md }}>
            {exercises.map((exercise, index) => (
              <View
                key={exercise.exerciseId}
                style={{
                  backgroundColor: colors.background.card,
                  borderRadius: radius.xl,
                  padding: spacing.lg,
                  borderWidth: 1,
                  borderColor: colors.background.glassBorder,
                  ...shadows.card,
                }}
              >
                <View style={{ flexDirection: 'row', alignItems: 'flex-start' }}>
                  {/* Order Number */}
                  <LinearGradient
                    colors={gradients.fire}
                    style={{
                      width: 36,
                      height: 36,
                      borderRadius: 18,
                      justifyContent: 'center',
                      alignItems: 'center',
                      marginRight: spacing.md,
                    }}
                  >
                    <Text style={{ color: '#fff', fontSize: typography.sizes.sm, fontWeight: typography.weights.bold }}>{index + 1}</Text>
                  </LinearGradient>

                  {/* Exercise Info */}
                  <View style={{ flex: 1 }}>
                    <Text style={{ color: colors.text.primary, fontSize: typography.sizes.lg, fontWeight: typography.weights.bold }}>{exercise.name}</Text>

                    {/* Muscle & Equipment Tags */}
                    <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: spacing.xs, marginTop: spacing.sm }}>
                      <View style={{ backgroundColor: colors.orange.DEFAULT + '20', paddingHorizontal: spacing.sm, paddingVertical: spacing.xs, borderRadius: radius.sm }}>
                        <Text style={{ color: colors.orange.DEFAULT, fontSize: typography.sizes.xs, fontWeight: typography.weights.semibold }}>{exercise.muscleGroup.replace('_', ' ')}</Text>
                      </View>
                      <View style={{ backgroundColor: colors.blue.DEFAULT + '20', paddingHorizontal: spacing.sm, paddingVertical: spacing.xs, borderRadius: radius.sm }}>
                        <Text style={{ color: colors.blue.DEFAULT, fontSize: typography.sizes.xs, fontWeight: typography.weights.semibold }}>{exercise.equipmentType}</Text>
                      </View>
                    </View>

                    {/* Sets, Reps, Rest */}
                    <View style={{ flexDirection: 'row', marginTop: spacing.md, gap: spacing.xl }}>
                      <View>
                        <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs, letterSpacing: 1 }}>SETS</Text>
                        <Text style={{ color: colors.orange.DEFAULT, fontSize: typography.sizes.xl, fontWeight: typography.weights.black }}>{exercise.sets}</Text>
                      </View>
                      {exercise.reps && (
                        <View>
                          <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs, letterSpacing: 1 }}>REPS</Text>
                          <Text style={{ color: colors.text.primary, fontSize: typography.sizes.xl, fontWeight: typography.weights.black }}>{exercise.reps}</Text>
                        </View>
                      )}
                      {exercise.durationSeconds && (
                        <View>
                          <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs, letterSpacing: 1 }}>TIME</Text>
                          <Text style={{ color: colors.text.primary, fontSize: typography.sizes.xl, fontWeight: typography.weights.black }}>{exercise.durationSeconds}s</Text>
                        </View>
                      )}
                      <View>
                        <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs, letterSpacing: 1 }}>REST</Text>
                        <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.xl, fontWeight: typography.weights.black }}>{exercise.restSeconds}s</Text>
                      </View>
                    </View>

                    {/* Description */}
                    {exercise.description && (
                      <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm, marginTop: spacing.md, lineHeight: 20 }}>
                        {exercise.description}
                      </Text>
                    )}

                    {/* Exercise GIF */}
                    {exercise.videoUrl && (
                      <View style={{ marginTop: spacing.md, borderRadius: radius.lg, overflow: 'hidden', backgroundColor: colors.background.elevated }}>
                        <Image
                          source={{ uri: exercise.videoUrl }}
                          style={{ width: '100%', height: 200, borderRadius: radius.lg }}
                          resizeMode="contain"
                        />
                      </View>
                    )}

                    {/* Notes */}
                    {exercise.notes && (
                      <View style={{ backgroundColor: colors.amber.DEFAULT + '20', padding: spacing.sm, borderRadius: radius.md, marginTop: spacing.md }}>
                        <Text style={{ color: colors.amber.DEFAULT, fontSize: typography.sizes.sm }}>💡 {exercise.notes}</Text>
                      </View>
                    )}
                  </View>
                </View>
              </View>
            ))}
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
          backgroundColor: colors.background.card,
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
