import React, { useEffect, useState, useCallback } from 'react';
import { View, Text, ScrollView, TouchableOpacity, RefreshControl, Image, ActivityIndicator } from 'react-native';
import { useNavigation, useFocusEffect } from '@react-navigation/native';
import { LinearGradient } from 'expo-linear-gradient';
import { Dumbbell, Clock, ChevronRight, ChevronDown, Zap, Heart, Sparkles, Play, Search } from 'lucide-react-native';
import { colors, gradients, shadows, radius, spacing, typography } from '../constants/theme';
import { workoutApi } from '../services/api';
import { WorkoutResponse } from '../types/api';

interface ExerciseDetail {
  exerciseId: number;
  name: string;
  description: string;
  muscleGroup: string;
  equipmentType: string;
  videoUrl: string;
  sets: number;
  reps: number;
  durationSeconds: number;
  restSeconds: number;
  orderIndex: number;
}

interface WorkoutWithExercises extends WorkoutResponse {
  exercises?: ExerciseDetail[];
}

export default function WorkoutsScreen() {
  const navigation = useNavigation();
  const [workouts, setWorkouts] = useState<WorkoutWithExercises[]>([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [selectedType, setSelectedType] = useState<string | null>(null);
  const [expandedWorkout, setExpandedWorkout] = useState<number | null>(null);
  const [loadingExercises, setLoadingExercises] = useState<number | null>(null);

  const loadData = async () => {
    try {
      const data = await workoutApi.getAllWorkouts();
      setWorkouts(data);
    } catch (error) {
      console.error('Error loading workouts:', error);
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };

  useFocusEffect(useCallback(() => { loadData(); }, []));
  const onRefresh = () => { setRefreshing(true); loadData(); };

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

  const toggleExpand = async (workoutId: number) => {
    if (expandedWorkout === workoutId) {
      setExpandedWorkout(null);
      return;
    }

    // Check if we already have exercises loaded for this workout
    const workout = workouts.find(w => w.workoutId === workoutId);
    if (workout && workout.exercises && workout.exercises.length > 0) {
      setExpandedWorkout(workoutId);
      return;
    }

    // Fetch workout details with exercises
    setLoadingExercises(workoutId);
    try {
      const workoutDetail = await workoutApi.getWorkoutById(workoutId);
      
      // Update the workout in state with exercises
      setWorkouts(prev => prev.map(w => 
        w.workoutId === workoutId 
          ? { ...w, exercises: (workoutDetail as any).exercises || [] }
          : w
      ));
      setExpandedWorkout(workoutId);
    } catch (error) {
      console.error('Error loading workout details:', error);
    } finally {
      setLoadingExercises(null);
    }
  };

  const filterTypes = ['All', 'STRENGTH', 'CARDIO', 'FLEXIBILITY', 'MIXED'];
  const filteredWorkouts = selectedType && selectedType !== 'All'
    ? workouts.filter(w => w.workoutType === selectedType)
    : workouts;

  if (loading) {
    return (
      <View style={{ flex: 1, backgroundColor: colors.background.end, justifyContent: 'center', alignItems: 'center' }}>
        <Text style={{ color: colors.orange.DEFAULT, fontSize: 48, fontWeight: '900' }}>B3</Text>
      </View>
    );
  }

  return (
    <View style={{ flex: 1, backgroundColor: colors.background.end }}>
      {/* Background */}
      <View style={{ position: 'absolute', top: 0, left: 0, right: 0, bottom: 0 }}>
        <LinearGradient colors={[colors.background.start, colors.background.mid, colors.background.end]} style={{ position: 'absolute', top: 0, left: 0, right: 0, bottom: 0 }} />
        <LinearGradient colors={['rgba(249, 115, 22, 0.2)', 'transparent']} style={{ position: 'absolute', top: -50, right: -50, width: 250, height: 250, borderRadius: 125 }} />
      </View>

      <ScrollView
        style={{ flex: 1 }}
        contentContainerStyle={{ paddingBottom: 120 }}
        showsVerticalScrollIndicator={false}
        refreshControl={<RefreshControl refreshing={refreshing} onRefresh={onRefresh} tintColor={colors.orange.DEFAULT} />}
      >
        {/* Header */}
        <View style={{ paddingHorizontal: spacing.xl, paddingTop: 70 }}>
          <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm, letterSpacing: 2, textTransform: 'uppercase' }}>
            Build Your
          </Text>
          <Text style={{ color: colors.text.primary, fontSize: typography.sizes['4xl'], fontWeight: typography.weights.black, letterSpacing: -1 }}>
            Workouts
          </Text>
          <Text style={{ color: colors.text.muted, fontSize: typography.sizes.base, marginTop: spacing.xs }}>
            {workouts.length} workouts available
          </Text>
        </View>

        {/* Filter Chips */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing.xl }}>
          <ScrollView horizontal showsHorizontalScrollIndicator={false}>
            <View style={{ flexDirection: 'row', gap: spacing.sm }}>
              {filterTypes.map((type) => (
                <TouchableOpacity
                  key={type}
                  onPress={() => setSelectedType(type === 'All' ? null : type)}
                  style={{
                    backgroundColor: (selectedType === type || (type === 'All' && !selectedType)) ? colors.orange.DEFAULT : colors.background.glass,
                    paddingHorizontal: spacing.lg,
                    paddingVertical: spacing.sm,
                    borderRadius: radius.full,
                    borderWidth: 1,
                    borderColor: (selectedType === type || (type === 'All' && !selectedType)) ? colors.orange.DEFAULT : colors.background.glassBorder,
                  }}
                >
                  <Text style={{ color: colors.text.primary, fontSize: typography.sizes.sm, fontWeight: typography.weights.semibold }}>
                    {type === 'All' ? 'All' : type.charAt(0) + type.slice(1).toLowerCase()}
                  </Text>
                </TouchableOpacity>
              ))}
            </View>
          </ScrollView>
        </View>

        {/* Workout Cards */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing.xl, gap: spacing.md }}>
          {filteredWorkouts.map((workout) => {
            const IconComponent = getTypeIcon(workout.workoutType);
            const isExpanded = expandedWorkout === workout.workoutId;
            const isLoadingThis = loadingExercises === workout.workoutId;
            const exercises = workout.exercises || [];
            
            return (
              <View key={workout.workoutId}>
                <TouchableOpacity activeOpacity={0.95} onPress={() => toggleExpand(workout.workoutId)}>
                  <View style={{
                    backgroundColor: colors.background.card,
                    borderRadius: isExpanded ? radius.xl : radius.xl,
                    borderBottomLeftRadius: isExpanded ? 0 : radius.xl,
                    borderBottomRightRadius: isExpanded ? 0 : radius.xl,
                    padding: spacing.lg,
                    borderWidth: 1,
                    borderColor: isExpanded ? colors.orange.DEFAULT : colors.background.glassBorder,
                    borderBottomWidth: isExpanded ? 0 : 1,
                    ...shadows.card,
                  }}>
                    <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                      <LinearGradient
                        colors={gradients.fire}
                        style={{ width: 50, height: 50, borderRadius: radius.lg, justifyContent: 'center', alignItems: 'center', marginRight: spacing.lg }}
                      >
                        <IconComponent size={24} color="#fff" />
                      </LinearGradient>

                      <View style={{ flex: 1 }}>
                        <Text style={{ color: colors.text.primary, fontSize: typography.sizes.lg, fontWeight: typography.weights.bold }}>
                          {workout.name}
                        </Text>
                        <View style={{ flexDirection: 'row', alignItems: 'center', marginTop: spacing.xs }}>
                          <Clock size={14} color={colors.text.muted} />
                          <Text style={{ color: colors.text.muted, fontSize: typography.sizes.sm, marginLeft: spacing.xs }}>
                            {workout.estimatedDuration} min
                          </Text>
                          <Text style={{ color: colors.text.muted, marginHorizontal: spacing.sm }}>-</Text>
                          <View style={{ width: 8, height: 8, borderRadius: 4, backgroundColor: getDifficultyColor(workout.difficultyLevel), marginRight: spacing.xs }} />
                          <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm }}>
                            {workout.difficultyLevel.charAt(0) + workout.difficultyLevel.slice(1).toLowerCase()}
                          </Text>
                        </View>
                      </View>

                      {isLoadingThis ? (
                        <ActivityIndicator size="small" color={colors.orange.DEFAULT} />
                      ) : isExpanded ? (
                        <ChevronDown size={24} color={colors.orange.DEFAULT} />
                      ) : (
                        <ChevronRight size={24} color={colors.text.muted} />
                      )}
                    </View>
                  </View>
                </TouchableOpacity>

                {/* Expanded Exercise List */}
                {isExpanded && (
                  <View style={{
                    backgroundColor: colors.background.card,
                    borderBottomLeftRadius: radius.xl,
                    borderBottomRightRadius: radius.xl,
                    borderWidth: 1,
                    borderTopWidth: 0,
                    borderColor: colors.orange.DEFAULT,
                    overflow: 'hidden',
                  }}>
                    <View style={{ padding: spacing.lg, gap: spacing.sm }}>
                      <Text style={{ color: colors.orange.DEFAULT, fontSize: typography.sizes.xs, fontWeight: typography.weights.bold, letterSpacing: 2, marginBottom: spacing.xs }}>
                        EXERCISES ({exercises.length})
                      </Text>
                      
                      {exercises.length === 0 ? (
                        <View style={{ padding: spacing.xl, alignItems: 'center' }}>
                          <Text style={{ color: colors.text.muted, textAlign: 'center' }}>
                            No exercises found for this workout
                          </Text>
                        </View>
                      ) : (
                        exercises.map((exercise, index) => (
                          <View 
                            key={exercise.exerciseId || index}
                            style={{ 
                              flexDirection: 'row', 
                              alignItems: 'center', 
                              backgroundColor: colors.background.glass,
                              padding: spacing.md,
                              borderRadius: radius.md,
                              borderWidth: 1,
                              borderColor: colors.background.glassBorder,
                            }}
                          >
                            {/* Exercise GIF/Video thumbnail */}
                            {exercise.videoUrl ? (
                              <Image
                                source={{ uri: exercise.videoUrl }}
                                style={{
                                  width: 50,
                                  height: 50,
                                  borderRadius: radius.sm,
                                  marginRight: spacing.md,
                                  backgroundColor: colors.background.glass,
                                }}
                                resizeMode="cover"
                              />
                            ) : (
                              <View style={{ 
                                width: 50, 
                                height: 50, 
                                borderRadius: radius.sm, 
                                backgroundColor: colors.orange.DEFAULT + '20',
                                justifyContent: 'center',
                                alignItems: 'center',
                                marginRight: spacing.md,
                              }}>
                                <Text style={{ color: colors.orange.DEFAULT, fontSize: typography.sizes.lg, fontWeight: typography.weights.bold }}>
                                  {index + 1}
                                </Text>
                              </View>
                            )}
                            
                            <View style={{ flex: 1 }}>
                              <Text style={{ color: colors.text.primary, fontSize: typography.sizes.base, fontWeight: typography.weights.semibold }}>
                                {exercise.name}
                              </Text>
                              <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs, marginTop: 2 }}>
                                {exercise.muscleGroup.replace('_', ' ')}
                              </Text>
                            </View>
                            
                            <View style={{ alignItems: 'flex-end' }}>
                              <Text style={{ color: colors.text.primary, fontSize: typography.sizes.sm, fontWeight: typography.weights.semibold }}>
                                {exercise.sets} x {exercise.reps || `${exercise.durationSeconds}s`}
                              </Text>
                              {exercise.restSeconds > 0 && (
                                <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs }}>
                                  {exercise.restSeconds}s rest
                                </Text>
                              )}
                            </View>
                          </View>
                        ))
                      )}
                    </View>

                    {/* Start Workout Button */}
                    <View style={{ padding: spacing.lg, paddingTop: 0 }}>
                      <TouchableOpacity activeOpacity={0.9}>
                        <LinearGradient
                          colors={gradients.fire}
                          start={{ x: 0, y: 0 }}
                          end={{ x: 1, y: 0 }}
                          style={{ 
                            borderRadius: radius.lg, 
                            paddingVertical: spacing.lg, 
                            flexDirection: 'row', 
                            alignItems: 'center', 
                            justifyContent: 'center',
                          }}
                        >
                          <Play size={20} color="#fff" fill="#fff" />
                          <Text style={{ color: '#fff', fontSize: typography.sizes.base, fontWeight: typography.weights.bold, marginLeft: spacing.sm }}>
                            Start Workout
                          </Text>
                        </LinearGradient>
                      </TouchableOpacity>
                    </View>
                  </View>
                )}
              </View>
            );
          })}

          {filteredWorkouts.length === 0 && (
            <View style={{ backgroundColor: colors.background.card, borderRadius: radius.xl, padding: spacing['2xl'], alignItems: 'center', borderWidth: 1, borderColor: colors.background.glassBorder }}>
              <Search size={40} color={colors.text.muted} />
              <Text style={{ color: colors.text.secondary, textAlign: 'center', marginTop: spacing.md }}>No workouts found for this filter</Text>
            </View>
          )}
        </View>
      </ScrollView>
    </View>
  );
}
