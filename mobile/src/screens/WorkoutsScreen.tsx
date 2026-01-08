import React, { useEffect, useState, useCallback } from 'react';
import { View, Text, ScrollView, RefreshControl, TouchableOpacity } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useFocusEffect, useNavigation } from '@react-navigation/native';
import { Ionicons } from '@expo/vector-icons';
import { Card } from '../components';
import { colors } from '../constants/theme';
import { workoutApi } from '../services/api';
import { WorkoutResponse } from '../types/api';

export default function WorkoutsScreen() {
  const navigation = useNavigation();
  const [workouts, setWorkouts] = useState<WorkoutResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [selectedFilter, setSelectedFilter] = useState<string | null>(null);

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

  useFocusEffect(
    useCallback(() => {
      loadData();
    }, [])
  );

  const onRefresh = () => {
    setRefreshing(true);
    loadData();
  };

  const getDifficultyColor = (difficulty: string) => {
    switch (difficulty) {
      case 'BEGINNER':
        return colors.green.DEFAULT;
      case 'INTERMEDIATE':
        return colors.amber.DEFAULT;
      case 'ADVANCED':
        return colors.red.DEFAULT;
      default:
        return colors.text.secondary;
    }
  };

  const getWorkoutTypeIcon = (type: string) => {
    switch (type) {
      case 'STRENGTH':
        return 'barbell-outline';
      case 'CARDIO':
        return 'heart-outline';
      case 'FLEXIBILITY':
        return 'body-outline';
      default:
        return 'fitness-outline';
    }
  };

  const filters = ['All', 'STRENGTH', 'CARDIO', 'FLEXIBILITY'];

  const filteredWorkouts = selectedFilter && selectedFilter !== 'All'
    ? workouts.filter((w) => w.workoutType === selectedFilter)
    : workouts;

  const recommendedWorkout = workouts[0]; // First workout as recommendation

  if (loading) {
    return (
      <SafeAreaView style={{ flex: 1, backgroundColor: colors.background.primary }}>
        <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
          <Text style={{ color: colors.text.secondary }}>Loading workouts...</Text>
        </View>
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={{ flex: 1, backgroundColor: colors.background.primary }}>
      <ScrollView
        style={{ flex: 1 }}
        contentContainerStyle={{ paddingBottom: 24 }}
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} tintColor={colors.orange.DEFAULT} />
        }
      >
        {/* Header */}
        <View
          style={{
            backgroundColor: colors.blue.DEFAULT,
            paddingHorizontal: 16,
            paddingTop: 16,
            paddingBottom: 24,
            borderBottomLeftRadius: 24,
            borderBottomRightRadius: 24,
          }}
        >
          <Text style={{ color: colors.text.primary, fontSize: 28, fontWeight: '800', marginBottom: 16 }}>
            Workouts
          </Text>

          {/* BRIX Recommendation */}
          {recommendedWorkout && (
            <TouchableOpacity
              onPress={() => console.log('Navigate to workout detail:', recommendedWorkout.workoutId)}
              style={{
                backgroundColor: 'rgba(255,255,255,0.15)',
                borderRadius: 12,
                padding: 16,
              }}
              activeOpacity={0.7}
            >
              <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 8 }}>
                <Ionicons name="flash" size={16} color={colors.orange.DEFAULT} />
                <Text style={{ color: colors.orange.DEFAULT, fontSize: 12, fontWeight: '600', marginLeft: 4 }}>
                  BRIX RECOMMENDS
                </Text>
              </View>
              <Text style={{ color: colors.text.primary, fontSize: 18, fontWeight: '600' }}>
                {recommendedWorkout.name}
              </Text>
              <Text style={{ color: 'rgba(255,255,255,0.7)', fontSize: 14 }}>
                {recommendedWorkout.estimatedDuration} min • {recommendedWorkout.difficultyLevel}
              </Text>
            </TouchableOpacity>
          )}
        </View>

        {/* Filters */}
        <ScrollView
          horizontal
          showsHorizontalScrollIndicator={false}
          style={{ marginTop: 16 }}
          contentContainerStyle={{ paddingHorizontal: 16, gap: 8 }}
        >
          {filters.map((filter) => (
            <TouchableOpacity
              key={filter}
              onPress={() => setSelectedFilter(filter === 'All' ? null : filter)}
              style={{
                backgroundColor:
                  (filter === 'All' && !selectedFilter) || filter === selectedFilter
                    ? colors.orange.DEFAULT
                    : colors.background.secondary,
                paddingVertical: 8,
                paddingHorizontal: 16,
                borderRadius: 20,
              }}
            >
              <Text
                style={{
                  color:
                    (filter === 'All' && !selectedFilter) || filter === selectedFilter
                      ? colors.text.primary
                      : colors.text.secondary,
                  fontWeight: '500',
                }}
              >
                {filter === 'All' ? 'All' : filter.charAt(0) + filter.slice(1).toLowerCase()}
              </Text>
            </TouchableOpacity>
          ))}
        </ScrollView>

        {/* Workout List */}
        <View style={{ paddingHorizontal: 16, marginTop: 16, gap: 12 }}>
          {filteredWorkouts.map((workout) => (
            <TouchableOpacity
              key={workout.workoutId}
              onPress={() => console.log('Navigate to workout detail:', workout.workoutId)}
              activeOpacity={0.7}
            >
              <Card>
                <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                  {/* Icon */}
                  <View
                    style={{
                      backgroundColor: colors.background.tertiary,
                      width: 48,
                      height: 48,
                      borderRadius: 12,
                      justifyContent: 'center',
                      alignItems: 'center',
                      marginRight: 12,
                    }}
                  >
                    <Ionicons
                      name={getWorkoutTypeIcon(workout.workoutType) as any}
                      size={24}
                      color={colors.blue.DEFAULT}
                    />
                  </View>

                  {/* Info */}
                  <View style={{ flex: 1 }}>
                    <Text style={{ color: colors.text.primary, fontSize: 16, fontWeight: '600', marginBottom: 4 }}>
                      {workout.name}
                    </Text>
                    <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8 }}>
                      <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                        <Ionicons name="time-outline" size={14} color={colors.text.secondary} />
                        <Text style={{ color: colors.text.secondary, fontSize: 12, marginLeft: 4 }}>
                          {workout.estimatedDuration} min
                        </Text>
                      </View>
                      <View
                        style={{
                          width: 4,
                          height: 4,
                          borderRadius: 2,
                          backgroundColor: colors.text.muted,
                        }}
                      />
                      <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                        <View
                          style={{
                            width: 8,
                            height: 8,
                            borderRadius: 4,
                            backgroundColor: getDifficultyColor(workout.difficultyLevel),
                            marginRight: 4,
                          }}
                        />
                        <Text style={{ color: colors.text.secondary, fontSize: 12 }}>
                          {workout.difficultyLevel.charAt(0) + workout.difficultyLevel.slice(1).toLowerCase()}
                        </Text>
                      </View>
                    </View>
                  </View>

                  {/* Arrow */}
                  <Ionicons name="chevron-forward" size={20} color={colors.text.muted} />
                </View>

                {/* Equipment Tag */}
                {workout.requiredEquipment && (
                  <View style={{ marginTop: 12 }}>
                    <View
                      style={{
                        backgroundColor: colors.background.tertiary,
                        paddingVertical: 4,
                        paddingHorizontal: 8,
                        borderRadius: 4,
                        alignSelf: 'flex-start',
                      }}
                    >
                      <Text style={{ color: colors.text.secondary, fontSize: 12 }}>
                        🏋️ {workout.requiredEquipment}
                      </Text>
                    </View>
                  </View>
                )}
              </Card>
            </TouchableOpacity>
          ))}
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}