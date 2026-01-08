import React, { useEffect, useState, useCallback } from 'react';
import { View, Text, ScrollView, TouchableOpacity, RefreshControl } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { Ionicons } from '@expo/vector-icons';
import { useNavigation, useFocusEffect } from '@react-navigation/native';
import { LinearGradient } from 'expo-linear-gradient';
import { colors } from '../constants/theme';
import { workoutApi } from '../services/api';
import { WorkoutResponse } from '../types/api';

const orangeOutline = {
  borderWidth: 2,
  borderColor: colors.orange.DEFAULT,
};

export default function WorkoutsScreen() {
  const navigation = useNavigation();
  const [workouts, setWorkouts] = useState<WorkoutResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [selectedType, setSelectedType] = useState<string | null>(null);

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

  const getDifficultyColor = (level: string) => {
    switch (level) {
      case 'BEGINNER': return colors.green.DEFAULT;
      case 'INTERMEDIATE': return colors.amber.DEFAULT;
      case 'ADVANCED': return colors.red.DEFAULT;
      default: return colors.text.muted;
    }
  };

  const getTypeIcon = (type: string): any => {
    switch (type) {
      case 'STRENGTH': return 'barbell';
      case 'CARDIO': return 'heart';
      case 'FLEXIBILITY': return 'body';
      case 'MIXED': return 'fitness';
      default: return 'barbell';
    }
  };

  const filterTypes = ['All', 'STRENGTH', 'CARDIO', 'FLEXIBILITY', 'MIXED'];

  const filteredWorkouts = selectedType && selectedType !== 'All'
    ? workouts.filter(w => w.workoutType === selectedType)
    : workouts;

  if (loading) {
    return (
      <SafeAreaView style={{ flex: 1, backgroundColor: colors.background.secondary }}>
        <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
          <Text style={{ color: colors.text.secondary }}>Loading workouts...</Text>
        </View>
      </SafeAreaView>
    );
  }

  return (
    <View style={{ flex: 1, backgroundColor: colors.background.secondary }}>
      <ScrollView
        style={{ flex: 1 }}
        contentContainerStyle={{ paddingBottom: 100 }}
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} tintColor={colors.orange.DEFAULT} />
        }
      >
        {/* Blue Gradient Header */}
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
          <Text style={{ color: '#fff', fontSize: 28, fontWeight: '800' }}>
            Workouts 💪
          </Text>
          <Text style={{ color: 'rgba(255,255,255,0.8)', fontSize: 14, marginTop: 4 }}>
            {workouts.length} workouts available
          </Text>
        </LinearGradient>

        {/* Filter Chips */}
        <View style={{ paddingHorizontal: 20, marginTop: 16 }}>
          <ScrollView horizontal showsHorizontalScrollIndicator={false}>
            <View style={{ flexDirection: 'row', gap: 8 }}>
              {filterTypes.map((type) => (
                <TouchableOpacity
                  key={type}
                  onPress={() => setSelectedType(type === 'All' ? null : type)}
                  style={{
                    backgroundColor: (selectedType === type || (type === 'All' && !selectedType))
                      ? colors.orange.DEFAULT
                      : colors.background.tertiary,
                    paddingHorizontal: 16,
                    paddingVertical: 8,
                    borderRadius: 20,
                    ...orangeOutline,
                  }}
                >
                  <Text style={{ color: colors.text.primary, fontSize: 14, fontWeight: '600' }}>
                    {type === 'All' ? 'All' : type.charAt(0) + type.slice(1).toLowerCase()}
                  </Text>
                </TouchableOpacity>
              ))}
            </View>
          </ScrollView>
        </View>

        {/* Workout Cards */}
        <View style={{ paddingHorizontal: 20, marginTop: 16, gap: 12 }}>
          {filteredWorkouts.map((workout) => (
            <TouchableOpacity
              key={workout.workoutId}
              onPress={() => console.log('Navigate to workout', workout.workoutId)}
              activeOpacity={0.9}
            >
              <View
                style={{
                  backgroundColor: colors.background.tertiary,
                  borderRadius: 16,
                  padding: 16,
                  ...orangeOutline,
                }}
              >
                <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                  <LinearGradient
                    colors={['#3b82f6', '#2563eb']}
                    style={{
                      width: 50,
                      height: 50,
                      borderRadius: 12,
                      justifyContent: 'center',
                      alignItems: 'center',
                      marginRight: 14,
                    }}
                  >
                    <Ionicons name={getTypeIcon(workout.workoutType)} size={24} color="#fff" />
                  </LinearGradient>

                  <View style={{ flex: 1 }}>
                    <Text style={{ color: colors.text.primary, fontSize: 16, fontWeight: '700' }}>
                      {workout.name}
                    </Text>
                    <View style={{ flexDirection: 'row', alignItems: 'center', marginTop: 4 }}>
                      <Ionicons name="time-outline" size={14} color={colors.text.muted} />
                      <Text style={{ color: colors.text.muted, fontSize: 13, marginLeft: 4 }}>
                        {workout.estimatedDuration} min
                      </Text>
                      <View
                        style={{
                          width: 4,
                          height: 4,
                          borderRadius: 2,
                          backgroundColor: colors.text.muted,
                          marginHorizontal: 8,
                        }}
                      />
                      <Text style={{ color: colors.text.secondary, fontSize: 13 }}>
                        {workout.workoutType.charAt(0) + workout.workoutType.slice(1).toLowerCase()}
                      </Text>
                    </View>
                  </View>

                  <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8 }}>
                    <View
                      style={{
                        width: 10,
                        height: 10,
                        borderRadius: 5,
                        backgroundColor: getDifficultyColor(workout.difficultyLevel),
                      }}
                    />
                    <Ionicons name="chevron-forward" size={20} color={colors.text.muted} />
                  </View>
                </View>

                {workout.requiredEquipment && (
                  <View style={{ marginTop: 12, flexDirection: 'row' }}>
                    <View
                      style={{
                        backgroundColor: colors.background.elevated,
                        paddingHorizontal: 10,
                        paddingVertical: 4,
                        borderRadius: 6,
                      }}
                    >
                      <Text style={{ color: colors.text.secondary, fontSize: 12 }}>
                        🏋️ {workout.requiredEquipment}
                      </Text>
                    </View>
                  </View>
                )}
              </View>
            </TouchableOpacity>
          ))}

          {filteredWorkouts.length === 0 && (
            <View
              style={{
                backgroundColor: colors.background.tertiary,
                borderRadius: 16,
                padding: 32,
                alignItems: 'center',
                ...orangeOutline,
              }}
            >
              <Text style={{ fontSize: 40, marginBottom: 12 }}>🔍</Text>
              <Text style={{ color: colors.text.secondary, textAlign: 'center' }}>
                No workouts found for this filter
              </Text>
            </View>
          )}
        </View>
      </ScrollView>
    </View>
  );
}