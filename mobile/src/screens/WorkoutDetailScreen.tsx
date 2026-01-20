import React, { useEffect, useState } from 'react';
import { View, Text, ScrollView, TouchableOpacity, ActivityIndicator } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useNavigation, useRoute, RouteProp } from '@react-navigation/native';
import { LinearGradient } from 'expo-linear-gradient';
import { colors } from '../constants/theme';
import { workoutApi } from '../services/api';
import { WorkoutResponse, WorkoutExerciseDTO } from '../types/api';

const orangeOutline = {
  borderWidth: 2,
  borderColor: colors.orange.DEFAULT,
};

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
      case 'ADVANCED': return colors.red.DEFAULT;
      default: return colors.text.muted;
    }
  };

  const getTypeIcon = (type: string): keyof typeof Ionicons.glyphMap => {
    switch (type) {
      case 'STRENGTH': return 'barbell';
      case 'CARDIO': return 'heart';
      case 'FLEXIBILITY': return 'body';
      case 'MIXED': return 'fitness';
      default: return 'barbell';
    }
  };

  const getMuscleIcon = (muscle: string): string => {
    switch (muscle) {
      case 'CHEST': return '🫁';
      case 'BACK': return '🔙';
      case 'SHOULDERS': return '💪';
      case 'BICEPS': return '💪';
      case 'TRICEPS': return '💪';
      case 'LEGS': return '🦵';
      case 'CORE': return '🎯';
      case 'FULL_BODY': return '🏋️';
      default: return '💪';
    }
  };

  if (loading) {
    return (
      <View style={{ flex: 1, backgroundColor: colors.background.secondary, justifyContent: 'center', alignItems: 'center' }}>
        <ActivityIndicator size="large" color={colors.orange.DEFAULT} />
        <Text style={{ color: colors.text.secondary, marginTop: 12 }}>Loading workout...</Text>
      </View>
    );
  }

  if (!workout) {
    return (
      <View style={{ flex: 1, backgroundColor: colors.background.secondary, justifyContent: 'center', alignItems: 'center' }}>
        <Text style={{ color: colors.text.secondary }}>Workout not found</Text>
      </View>
    );
  }

  return (
    <View style={{ flex: 1, backgroundColor: colors.background.secondary }}>
      <ScrollView style={{ flex: 1 }} contentContainerStyle={{ paddingBottom: 120 }}>
        {/* Header */}
        <LinearGradient
          colors={['#2563eb', '#3b82f6', '#60a5fa']}
          start={{ x: 0, y: 0 }}
          end={{ x: 1, y: 1 }}
          style={{
            paddingTop: 50,
            paddingBottom: 30,
            paddingHorizontal: 20,
            borderBottomLeftRadius: 20,
            borderBottomRightRadius: 20,
            borderWidth: 2,
            borderColor: colors.orange.DEFAULT,
            borderTopWidth: 0,
          }}
        >
          {/* Back Button */}
          <TouchableOpacity onPress={() => navigation.goBack()} style={{ marginBottom: 16 }}>
            <View style={{ flexDirection: 'row', alignItems: 'center' }}>
              <Ionicons name="arrow-back" size={24} color="#fff" />
              <Text style={{ color: '#fff', fontSize: 16, marginLeft: 8 }}>Back</Text>
            </View>
          </TouchableOpacity>

          {/* Workout Title */}
          <View style={{ flexDirection: 'row', alignItems: 'center' }}>
            <View
              style={{
                backgroundColor: 'rgba(255,255,255,0.2)',
                width: 56,
                height: 56,
                borderRadius: 16,
                justifyContent: 'center',
                alignItems: 'center',
                marginRight: 16,
              }}
            >
              <Ionicons name={getTypeIcon(workout.workoutType)} size={28} color="#fff" />
            </View>
            <View style={{ flex: 1 }}>
              <Text style={{ color: '#fff', fontSize: 24, fontWeight: '800' }}>{workout.name}</Text>
              <View style={{ flexDirection: 'row', alignItems: 'center', marginTop: 4 }}>
                <Text style={{ color: 'rgba(255,255,255,0.8)', fontSize: 14 }}>
                  {workout.estimatedDuration} min
                </Text>
                <View style={{ width: 4, height: 4, borderRadius: 2, backgroundColor: 'rgba(255,255,255,0.5)', marginHorizontal: 8 }} />
                <View style={{ width: 8, height: 8, borderRadius: 4, backgroundColor: getDifficultyColor(workout.difficultyLevel), marginRight: 6 }} />
                <Text style={{ color: 'rgba(255,255,255,0.8)', fontSize: 14 }}>
                  {workout.difficultyLevel.charAt(0) + workout.difficultyLevel.slice(1).toLowerCase()}
                </Text>
              </View>
            </View>
          </View>
        </LinearGradient>

        {/* Description */}
        {workout.description && (
          <View style={{ paddingHorizontal: 20, marginTop: 20 }}>
            <View style={{ backgroundColor: colors.background.tertiary, borderRadius: 16, padding: 16, ...orangeOutline }}>
              <Text style={{ color: colors.text.primary, fontSize: 15, lineHeight: 22 }}>{workout.description}</Text>
            </View>
          </View>
        )}

        {/* Quick Stats */}
        <View style={{ paddingHorizontal: 20, marginTop: 16 }}>
          <View style={{ flexDirection: 'row', gap: 12 }}>
            <View style={{ flex: 1, backgroundColor: colors.background.tertiary, borderRadius: 12, padding: 16, alignItems: 'center', ...orangeOutline }}>
              <Ionicons name="list" size={24} color={colors.blue.DEFAULT} />
              <Text style={{ color: colors.text.primary, fontSize: 20, fontWeight: '700', marginTop: 8 }}>{exercises.length}</Text>
              <Text style={{ color: colors.text.muted, fontSize: 12 }}>Exercises</Text>
            </View>
            <View style={{ flex: 1, backgroundColor: colors.background.tertiary, borderRadius: 12, padding: 16, alignItems: 'center', ...orangeOutline }}>
              <Ionicons name="time-outline" size={24} color={colors.orange.DEFAULT} />
              <Text style={{ color: colors.text.primary, fontSize: 20, fontWeight: '700', marginTop: 8 }}>{workout.estimatedDuration}</Text>
              <Text style={{ color: colors.text.muted, fontSize: 12 }}>Minutes</Text>
            </View>
            <View style={{ flex: 1, backgroundColor: colors.background.tertiary, borderRadius: 12, padding: 16, alignItems: 'center', ...orangeOutline }}>
              <Ionicons name="barbell" size={24} color={colors.amber.DEFAULT} />
              <Text style={{ color: colors.text.primary, fontSize: 14, fontWeight: '700', marginTop: 8, textAlign: 'center' }}>
                {workout.requiredEquipment || 'None'}
              </Text>
              <Text style={{ color: colors.text.muted, fontSize: 12 }}>Equipment</Text>
            </View>
          </View>
        </View>

        {/* Exercises List */}
        <View style={{ paddingHorizontal: 20, marginTop: 24 }}>
          <Text style={{ color: colors.text.primary, fontSize: 20, fontWeight: '700', marginBottom: 16 }}>
            Exercises ({exercises.length})
          </Text>
          
          {exercises.map((exercise, index) => (
            <View
              key={exercise.exerciseId}
              style={{
                backgroundColor: colors.background.tertiary,
                borderRadius: 16,
                padding: 16,
                marginBottom: 12,
                ...orangeOutline,
              }}
            >
              <View style={{ flexDirection: 'row', alignItems: 'flex-start' }}>
                {/* Order Number */}
                <View
                  style={{
                    backgroundColor: colors.blue.DEFAULT,
                    width: 32,
                    height: 32,
                    borderRadius: 16,
                    justifyContent: 'center',
                    alignItems: 'center',
                    marginRight: 12,
                  }}
                >
                  <Text style={{ color: '#fff', fontSize: 14, fontWeight: '700' }}>#{index + 1}</Text>
                </View>

                {/* Exercise Info */}
                <View style={{ flex: 1 }}>
                  <Text style={{ color: colors.text.primary, fontSize: 16, fontWeight: '700' }}>{exercise.name}</Text>
                  
                  {/* Muscle & Equipment Tags */}
                  <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: 6, marginTop: 8 }}>
                    <View style={{ backgroundColor: colors.background.elevated, paddingHorizontal: 8, paddingVertical: 4, borderRadius: 6 }}>
                      <Text style={{ color: colors.text.secondary, fontSize: 12 }}>
                        {getMuscleIcon(exercise.muscleGroup)} {exercise.muscleGroup}
                      </Text>
                    </View>
                    <View style={{ backgroundColor: colors.background.elevated, paddingHorizontal: 8, paddingVertical: 4, borderRadius: 6 }}>
                      <Text style={{ color: colors.text.secondary, fontSize: 12 }}>🏋️ {exercise.equipmentType}</Text>
                    </View>
                  </View>

                  {/* Sets, Reps, Rest */}
                  <View style={{ flexDirection: 'row', marginTop: 12, gap: 16 }}>
                    <View>
                      <Text style={{ color: colors.text.muted, fontSize: 11 }}>SETS</Text>
                      <Text style={{ color: colors.orange.DEFAULT, fontSize: 18, fontWeight: '700' }}>{exercise.sets}</Text>
                    </View>
                    {exercise.reps && (
                      <View>
                        <Text style={{ color: colors.text.muted, fontSize: 11 }}>REPS</Text>
                        <Text style={{ color: colors.text.primary, fontSize: 18, fontWeight: '700' }}>{exercise.reps}</Text>
                      </View>
                    )}
                    {exercise.durationSeconds && (
                      <View>
                        <Text style={{ color: colors.text.muted, fontSize: 11 }}>DURATION</Text>
                        <Text style={{ color: colors.text.primary, fontSize: 18, fontWeight: '700' }}>{exercise.durationSeconds}s</Text>
                      </View>
                    )}
                    <View>
                      <Text style={{ color: colors.text.muted, fontSize: 11 }}>REST</Text>
                      <Text style={{ color: colors.text.secondary, fontSize: 18, fontWeight: '700' }}>{exercise.restSeconds}s</Text>
                    </View>
                  </View>

                  {/* Description */}
                  {exercise.description && (
                    <Text style={{ color: colors.text.secondary, fontSize: 13, marginTop: 10, lineHeight: 18 }}>
                      {exercise.description}
                    </Text>
                  )}

                  {/* Notes */}
                  {exercise.notes && (
                    <View style={{ backgroundColor: colors.amber.DEFAULT + '20', padding: 8, borderRadius: 8, marginTop: 10 }}>
                      <Text style={{ color: colors.amber.DEFAULT, fontSize: 12 }}>💡 {exercise.notes}</Text>
                    </View>
                  )}
                </View>
              </View>
            </View>
          ))}
        </View>
      </ScrollView>

      {/* Fixed Bottom Button */}
      <View
        style={{
          position: 'absolute',
          bottom: 0,
          left: 0,
          right: 0,
          padding: 20,
          paddingBottom: 32,
          backgroundColor: colors.background.secondary,
          borderTopWidth: 1,
          borderTopColor: colors.background.elevated,
        }}
      >
        <TouchableOpacity onPress={() => console.log('Start workout:', workoutId)} activeOpacity={0.9}>
          <LinearGradient
            colors={[colors.orange.DEFAULT, colors.orange.dark]}
            start={{ x: 0, y: 0 }}
            end={{ x: 1, y: 0 }}
            style={{ borderRadius: 16, paddingVertical: 18, alignItems: 'center' }}
          >
            <Text style={{ color: '#fff', fontSize: 18, fontWeight: '700' }}>Start Workout 🔥</Text>
          </LinearGradient>
        </TouchableOpacity>
      </View>
    </View>
  );
}
