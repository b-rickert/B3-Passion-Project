import React, { useEffect, useState, useCallback } from 'react';
import { View, Text, ScrollView, TouchableOpacity, RefreshControl, Image, ActivityIndicator, Modal } from 'react-native';
import { useNavigation, useFocusEffect } from '@react-navigation/native';
import { LinearGradient } from 'expo-linear-gradient';
import { Dumbbell, Clock, ChevronRight, ChevronDown, Zap, Heart, Sparkles, Play, Search, Wand2, X, Battery, Brain, Moon, Smile } from 'lucide-react-native';
import { colors, gradients, shadows, radius, spacing, typography } from '../constants/theme';
import { workoutApi, dailyLogApi } from '../services/api';
import { WorkoutResponse } from '../types/api';
import B3Logo from '../components/B3Logo';

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

interface DailyFeeling {
  energyLevel: number;
  stressLevel: number;
  sleepQuality: number;
  mood: string;
}

export default function WorkoutsScreen() {
  const navigation = useNavigation();
  const [workouts, setWorkouts] = useState<WorkoutWithExercises[]>([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [selectedType, setSelectedType] = useState<string | null>(null);
  const [expandedWorkout, setExpandedWorkout] = useState<number | null>(null);
  const [loadingExercises, setLoadingExercises] = useState<number | null>(null);
  
  // Generate workout modal state
  const [showGenerateModal, setShowGenerateModal] = useState(false);
  const [generating, setGenerating] = useState(false);
  const [feelings, setFeelings] = useState<DailyFeeling>({
    energyLevel: 3,
    stressLevel: 3,
    sleepQuality: 3,
    mood: 'OKAY'
  });
  const [generatedWorkout, setGeneratedWorkout] = useState<WorkoutWithExercises | null>(null);

  const loadData = async () => {
    try {
      const data = await workoutApi.getAllWorkouts();
      setWorkouts(data);
      
      // Try to load today's daily log to pre-fill feelings
      try {
        const todayLog = await dailyLogApi.getTodaysLog();
        if (todayLog) {
          setFeelings({
            energyLevel: todayLog.energyLevel,
            stressLevel: todayLog.stressLevel,
            sleepQuality: todayLog.sleepQuality,
            mood: todayLog.mood
          });
        }
      } catch (e) {
        // No daily log for today, use defaults
      }
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

    const workout = workouts.find(w => w.workoutId === workoutId);
    if (workout && workout.exercises && workout.exercises.length > 0) {
      setExpandedWorkout(workoutId);
      return;
    }

    setLoadingExercises(workoutId);
    try {
      const workoutDetail = await workoutApi.getWorkoutById(workoutId);
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

  // Generate workout based on feelings
  const generateWorkout = async () => {
    setGenerating(true);
    
    // Calculate recommended intensity based on feelings
    const wellnessScore = (feelings.energyLevel + (6 - feelings.stressLevel) + feelings.sleepQuality) / 15;
    
    let recommendedDifficulty: string;
    let recommendedType: string;
    let recommendation: string;
    
    if (wellnessScore >= 0.7 && feelings.mood !== 'STRESSED' && feelings.mood !== 'LOW') {
      // High energy, low stress, good sleep - go hard!
      recommendedDifficulty = 'ADVANCED';
      recommendedType = 'STRENGTH';
      recommendation = "You're feeling great! Time to push your limits.";
    } else if (wellnessScore >= 0.5) {
      // Moderate - standard workout
      recommendedDifficulty = 'INTERMEDIATE';
      recommendedType = feelings.stressLevel >= 4 ? 'FLEXIBILITY' : 'STRENGTH';
      recommendation = feelings.stressLevel >= 4 
        ? "Moderate stress detected. A flexibility session will help you unwind."
        : "You're in a good place for a solid workout.";
    } else {
      // Low energy or high stress - take it easy
      recommendedDifficulty = 'BEGINNER';
      recommendedType = feelings.stressLevel >= 4 ? 'FLEXIBILITY' : 'CARDIO';
      recommendation = "Your body needs a lighter session today. Listen to it!";
    }
    
    // Find best matching workout
    const matchingWorkouts = workouts.filter(w => {
      const difficultyMatch = w.difficultyLevel === recommendedDifficulty;
      const typeMatch = w.workoutType === recommendedType;
      return difficultyMatch || typeMatch;
    });
    
    // Sort by best match (both criteria > one criteria)
    matchingWorkouts.sort((a, b) => {
      const aScore = (a.difficultyLevel === recommendedDifficulty ? 1 : 0) + (a.workoutType === recommendedType ? 1 : 0);
      const bScore = (b.difficultyLevel === recommendedDifficulty ? 1 : 0) + (b.workoutType === recommendedType ? 1 : 0);
      return bScore - aScore;
    });
    
    const selected = matchingWorkouts[0] || workouts[0];
    
    // Fetch exercises for the selected workout
    if (selected) {
      try {
        const workoutDetail = await workoutApi.getWorkoutById(selected.workoutId);
        setGeneratedWorkout({
          ...selected,
          exercises: (workoutDetail as any).exercises || [],
          description: recommendation
        });
      } catch (e) {
        setGeneratedWorkout({ ...selected, description: recommendation });
      }
    }
    
    setGenerating(false);
  };

  const filterTypes = ['All', 'STRENGTH', 'CARDIO', 'FLEXIBILITY', 'MIXED'];
  const filteredWorkouts = selectedType && selectedType !== 'All'
    ? workouts.filter(w => w.workoutType === selectedType)
    : workouts;

  const moodOptions = ['GREAT', 'GOOD', 'OKAY', 'LOW', 'STRESSED'];

  if (loading) {
    return (
      <View style={{ flex: 1, backgroundColor: colors.background.end, justifyContent: 'center', alignItems: 'center' }}>
        <B3Logo size={80} />
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
          <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'flex-start' }}>
            <View>
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
            <B3Logo size={48} />
          </View>
        </View>

        {/* Generate Workout Button */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing.xl }}>
          <TouchableOpacity activeOpacity={0.9} onPress={() => setShowGenerateModal(true)}>
            <LinearGradient
              colors={gradients.fire}
              start={{ x: 0, y: 0 }}
              end={{ x: 1, y: 0 }}
              style={{
                borderRadius: radius.xl,
                padding: spacing.lg,
                flexDirection: 'row',
                alignItems: 'center',
                ...shadows.glow,
              }}
            >
              <View style={{ backgroundColor: 'rgba(255,255,255,0.2)', width: 44, height: 44, borderRadius: radius.lg, justifyContent: 'center', alignItems: 'center', marginRight: spacing.lg }}>
                <Wand2 size={24} color="#fff" />
              </View>
              <View style={{ flex: 1 }}>
                <Text style={{ color: '#fff', fontSize: typography.sizes.lg, fontWeight: typography.weights.bold }}>
                  Generate Workout
                </Text>
                <Text style={{ color: 'rgba(255,255,255,0.8)', fontSize: typography.sizes.sm }}>
                  Based on how you're feeling today
                </Text>
              </View>
              <ChevronRight size={24} color="rgba(255,255,255,0.8)" />
            </LinearGradient>
          </TouchableOpacity>
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
                    borderRadius: radius.xl,
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

      {/* Generate Workout Modal */}
      <Modal
        visible={showGenerateModal}
        animationType="slide"
        transparent={true}
        onRequestClose={() => setShowGenerateModal(false)}
      >
        <View style={{ flex: 1, backgroundColor: 'rgba(0,0,0,0.8)', justifyContent: 'flex-end' }}>
          <View style={{ backgroundColor: colors.background.card, borderTopLeftRadius: radius['2xl'], borderTopRightRadius: radius['2xl'], maxHeight: '90%' }}>
            {/* Modal Header */}
            <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', padding: spacing.xl, borderBottomWidth: 1, borderBottomColor: colors.background.glassBorder }}>
              <Text style={{ color: colors.text.primary, fontSize: typography.sizes.xl, fontWeight: typography.weights.bold }}>
                {generatedWorkout ? 'Your Workout' : 'How are you feeling?'}
              </Text>
              <TouchableOpacity onPress={() => { setShowGenerateModal(false); setGeneratedWorkout(null); }}>
                <X size={24} color={colors.text.muted} />
              </TouchableOpacity>
            </View>

            <ScrollView style={{ padding: spacing.xl }}>
              {!generatedWorkout ? (
                <>
                  {/* Energy Level */}
                  <View style={{ marginBottom: spacing.xl }}>
                    <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: spacing.md }}>
                      <Battery size={20} color={colors.orange.DEFAULT} />
                      <Text style={{ color: colors.text.primary, fontSize: typography.sizes.base, fontWeight: typography.weights.semibold, marginLeft: spacing.sm }}>
                        Energy Level
                      </Text>
                    </View>
                    <View style={{ flexDirection: 'row', gap: spacing.sm }}>
                      {[1, 2, 3, 4, 5].map((level) => (
                        <TouchableOpacity
                          key={level}
                          onPress={() => setFeelings(prev => ({ ...prev, energyLevel: level }))}
                          style={{
                            flex: 1,
                            paddingVertical: spacing.md,
                            borderRadius: radius.md,
                            backgroundColor: feelings.energyLevel === level ? colors.orange.DEFAULT : colors.background.glass,
                            borderWidth: 1,
                            borderColor: feelings.energyLevel === level ? colors.orange.DEFAULT : colors.background.glassBorder,
                            alignItems: 'center',
                          }}
                        >
                          <Text style={{ color: colors.text.primary, fontWeight: typography.weights.bold }}>{level}</Text>
                        </TouchableOpacity>
                      ))}
                    </View>
                    <View style={{ flexDirection: 'row', justifyContent: 'space-between', marginTop: spacing.xs }}>
                      <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs }}>Exhausted</Text>
                      <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs }}>Energized</Text>
                    </View>
                  </View>

                  {/* Stress Level */}
                  <View style={{ marginBottom: spacing.xl }}>
                    <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: spacing.md }}>
                      <Brain size={20} color={colors.blue.DEFAULT} />
                      <Text style={{ color: colors.text.primary, fontSize: typography.sizes.base, fontWeight: typography.weights.semibold, marginLeft: spacing.sm }}>
                        Stress Level
                      </Text>
                    </View>
                    <View style={{ flexDirection: 'row', gap: spacing.sm }}>
                      {[1, 2, 3, 4, 5].map((level) => (
                        <TouchableOpacity
                          key={level}
                          onPress={() => setFeelings(prev => ({ ...prev, stressLevel: level }))}
                          style={{
                            flex: 1,
                            paddingVertical: spacing.md,
                            borderRadius: radius.md,
                            backgroundColor: feelings.stressLevel === level ? colors.blue.DEFAULT : colors.background.glass,
                            borderWidth: 1,
                            borderColor: feelings.stressLevel === level ? colors.blue.DEFAULT : colors.background.glassBorder,
                            alignItems: 'center',
                          }}
                        >
                          <Text style={{ color: colors.text.primary, fontWeight: typography.weights.bold }}>{level}</Text>
                        </TouchableOpacity>
                      ))}
                    </View>
                    <View style={{ flexDirection: 'row', justifyContent: 'space-between', marginTop: spacing.xs }}>
                      <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs }}>Relaxed</Text>
                      <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs }}>Very Stressed</Text>
                    </View>
                  </View>

                  {/* Sleep Quality */}
                  <View style={{ marginBottom: spacing.xl }}>
                    <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: spacing.md }}>
                      <Moon size={20} color={colors.amber.DEFAULT} />
                      <Text style={{ color: colors.text.primary, fontSize: typography.sizes.base, fontWeight: typography.weights.semibold, marginLeft: spacing.sm }}>
                        Sleep Quality
                      </Text>
                    </View>
                    <View style={{ flexDirection: 'row', gap: spacing.sm }}>
                      {[1, 2, 3, 4, 5].map((level) => (
                        <TouchableOpacity
                          key={level}
                          onPress={() => setFeelings(prev => ({ ...prev, sleepQuality: level }))}
                          style={{
                            flex: 1,
                            paddingVertical: spacing.md,
                            borderRadius: radius.md,
                            backgroundColor: feelings.sleepQuality === level ? colors.amber.DEFAULT : colors.background.glass,
                            borderWidth: 1,
                            borderColor: feelings.sleepQuality === level ? colors.amber.DEFAULT : colors.background.glassBorder,
                            alignItems: 'center',
                          }}
                        >
                          <Text style={{ color: colors.text.primary, fontWeight: typography.weights.bold }}>{level}</Text>
                        </TouchableOpacity>
                      ))}
                    </View>
                    <View style={{ flexDirection: 'row', justifyContent: 'space-between', marginTop: spacing.xs }}>
                      <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs }}>Terrible</Text>
                      <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs }}>Great</Text>
                    </View>
                  </View>

                  {/* Mood */}
                  <View style={{ marginBottom: spacing.xl }}>
                    <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: spacing.md }}>
                      <Smile size={20} color={colors.green.DEFAULT} />
                      <Text style={{ color: colors.text.primary, fontSize: typography.sizes.base, fontWeight: typography.weights.semibold, marginLeft: spacing.sm }}>
                        Current Mood
                      </Text>
                    </View>
                    <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: spacing.sm }}>
                      {moodOptions.map((mood) => (
                        <TouchableOpacity
                          key={mood}
                          onPress={() => setFeelings(prev => ({ ...prev, mood }))}
                          style={{
                            paddingHorizontal: spacing.lg,
                            paddingVertical: spacing.sm,
                            borderRadius: radius.full,
                            backgroundColor: feelings.mood === mood ? colors.green.DEFAULT : colors.background.glass,
                            borderWidth: 1,
                            borderColor: feelings.mood === mood ? colors.green.DEFAULT : colors.background.glassBorder,
                          }}
                        >
                          <Text style={{ color: colors.text.primary, fontWeight: typography.weights.semibold }}>
                            {mood.charAt(0) + mood.slice(1).toLowerCase()}
                          </Text>
                        </TouchableOpacity>
                      ))}
                    </View>
                  </View>

                  {/* Generate Button */}
                  <TouchableOpacity activeOpacity={0.9} onPress={generateWorkout} disabled={generating}>
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
                        marginBottom: spacing['2xl'],
                      }}
                    >
                      {generating ? (
                        <ActivityIndicator color="#fff" />
                      ) : (
                        <>
                          <Wand2 size={20} color="#fff" />
                          <Text style={{ color: '#fff', fontSize: typography.sizes.base, fontWeight: typography.weights.bold, marginLeft: spacing.sm }}>
                            Generate My Workout
                          </Text>
                        </>
                      )}
                    </LinearGradient>
                  </TouchableOpacity>
                </>
              ) : (
                /* Generated Workout Result */
                <View style={{ marginBottom: spacing['2xl'] }}>
                  {/* Recommendation */}
                  <View style={{ backgroundColor: colors.orange.DEFAULT + '20', padding: spacing.lg, borderRadius: radius.lg, marginBottom: spacing.xl, borderWidth: 1, borderColor: colors.orange.DEFAULT + '40' }}>
                    <Text style={{ color: colors.orange.DEFAULT, fontSize: typography.sizes.sm, fontWeight: typography.weights.semibold }}>
                      BRIX RECOMMENDATION
                    </Text>
                    <Text style={{ color: colors.text.primary, fontSize: typography.sizes.base, marginTop: spacing.xs }}>
                      {generatedWorkout.description}
                    </Text>
                  </View>

                  {/* Workout Card */}
                  <View style={{ backgroundColor: colors.background.glass, borderRadius: radius.xl, padding: spacing.lg, borderWidth: 1, borderColor: colors.background.glassBorder }}>
                    <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: spacing.lg }}>
                      <LinearGradient
                        colors={gradients.fire}
                        style={{ width: 56, height: 56, borderRadius: radius.lg, justifyContent: 'center', alignItems: 'center', marginRight: spacing.lg }}
                      >
                        {React.createElement(getTypeIcon(generatedWorkout.workoutType), { size: 28, color: '#fff' })}
                      </LinearGradient>
                      <View>
                        <Text style={{ color: colors.text.primary, fontSize: typography.sizes.xl, fontWeight: typography.weights.bold }}>
                          {generatedWorkout.name}
                        </Text>
                        <View style={{ flexDirection: 'row', alignItems: 'center', marginTop: spacing.xs }}>
                          <Clock size={14} color={colors.text.muted} />
                          <Text style={{ color: colors.text.muted, fontSize: typography.sizes.sm, marginLeft: spacing.xs }}>
                            {generatedWorkout.estimatedDuration} min
                          </Text>
                          <Text style={{ color: colors.text.muted, marginHorizontal: spacing.sm }}>-</Text>
                          <View style={{ width: 8, height: 8, borderRadius: 4, backgroundColor: getDifficultyColor(generatedWorkout.difficultyLevel), marginRight: spacing.xs }} />
                          <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm }}>
                            {generatedWorkout.difficultyLevel.charAt(0) + generatedWorkout.difficultyLevel.slice(1).toLowerCase()}
                          </Text>
                        </View>
                      </View>
                    </View>

                    {/* Exercise Preview */}
                    {generatedWorkout.exercises && generatedWorkout.exercises.length > 0 && (
                      <View style={{ gap: spacing.sm }}>
                        <Text style={{ color: colors.orange.DEFAULT, fontSize: typography.sizes.xs, fontWeight: typography.weights.bold, letterSpacing: 2 }}>
                          {generatedWorkout.exercises.length} EXERCISES
                        </Text>
                        {generatedWorkout.exercises.slice(0, 3).map((exercise, index) => (
                          <View key={index} style={{ flexDirection: 'row', alignItems: 'center', backgroundColor: colors.background.card, padding: spacing.md, borderRadius: radius.md }}>
                            <View style={{ width: 28, height: 28, borderRadius: radius.sm, backgroundColor: colors.orange.DEFAULT + '20', justifyContent: 'center', alignItems: 'center', marginRight: spacing.md }}>
                              <Text style={{ color: colors.orange.DEFAULT, fontSize: typography.sizes.sm, fontWeight: typography.weights.bold }}>{index + 1}</Text>
                            </View>
                            <Text style={{ color: colors.text.primary, flex: 1 }}>{exercise.name}</Text>
                            <Text style={{ color: colors.text.muted, fontSize: typography.sizes.sm }}>{exercise.sets} x {exercise.reps || `${exercise.durationSeconds}s`}</Text>
                          </View>
                        ))}
                        {generatedWorkout.exercises.length > 3 && (
                          <Text style={{ color: colors.text.muted, fontSize: typography.sizes.sm, textAlign: 'center' }}>
                            +{generatedWorkout.exercises.length - 3} more exercises
                          </Text>
                        )}
                      </View>
                    )}
                  </View>

                  {/* Action Buttons */}
                  <View style={{ marginTop: spacing.xl, gap: spacing.md }}>
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

                    <TouchableOpacity 
                      onPress={() => setGeneratedWorkout(null)}
                      style={{ backgroundColor: colors.background.glass, borderRadius: radius.lg, paddingVertical: spacing.lg, alignItems: 'center', borderWidth: 1, borderColor: colors.background.glassBorder }}
                    >
                      <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.base, fontWeight: typography.weights.semibold }}>
                        Try Different Settings
                      </Text>
                    </TouchableOpacity>
                  </View>
                </View>
              )}
            </ScrollView>
          </View>
        </View>
      </Modal>
    </View>
  );
}
