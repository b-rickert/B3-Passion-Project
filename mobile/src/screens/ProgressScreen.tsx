import React, { useState, useCallback } from 'react';
import { View, Text, ScrollView, TouchableOpacity, RefreshControl } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { Ionicons } from '@expo/vector-icons';
import { useFocusEffect } from '@react-navigation/native';
import { LinearGradient } from 'expo-linear-gradient';
import { colors } from '../constants/theme';
import { brickApi, milestoneApi } from '../services/api';
import { BrickResponse, BrickStatsResponse, MilestoneDTO } from '../types/api';

const orangeOutline = {
  borderWidth: 2,
  borderColor: colors.orange.DEFAULT,
};

const orangeGlow = {
  shadowColor: colors.orange.DEFAULT,
  shadowOffset: { width: 0, height: 4 },
  shadowOpacity: 0.4,
  shadowRadius: 12,
  elevation: 8,
};

export default function ProgressScreen() {
  const [brickStats, setBrickStats] = useState<BrickStatsResponse | null>(null);
  const [brickCalendar, setBrickCalendar] = useState<BrickResponse[]>([]);
  const [milestones, setMilestones] = useState<MilestoneDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [currentMonth, setCurrentMonth] = useState(new Date());

  const loadData = async () => {
    try {
      const [statsData, calendarData, milestonesData] = await Promise.all([
        brickApi.getBrickStats(),
        brickApi.getBrickCalendar(1, currentMonth.getFullYear(), currentMonth.getMonth() + 1),
        milestoneApi.getAchievedMilestones(),
      ]);

      setBrickStats(statsData);
      setBrickCalendar(calendarData);
      setMilestones(milestonesData.slice(0, 3));
    } catch (error) {
      console.error('Error loading progress data:', error);
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };

  useFocusEffect(
    useCallback(() => {
      loadData();
    }, [currentMonth])
  );

  const onRefresh = () => {
    setRefreshing(true);
    loadData();
  };

  const generateCalendarDays = () => {
    const year = currentMonth.getFullYear();
    const month = currentMonth.getMonth();
    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const daysInMonth = lastDay.getDate();
    const startingDay = firstDay.getDay();

    const days: (number | null)[] = [];

    for (let i = 0; i < startingDay; i++) {
      days.push(null);
    }

    for (let i = 1; i <= daysInMonth; i++) {
      days.push(i);
    }

    return days;
  };

  const getBrickForDate = (day: number) => {
    const dateStr = `${currentMonth.getFullYear()}-${String(currentMonth.getMonth() + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
    return brickCalendar.find(b => b.brickDate === dateStr);
  };

  const isToday = (day: number) => {
    const today = new Date();
    return (
      day === today.getDate() &&
      currentMonth.getMonth() === today.getMonth() &&
      currentMonth.getFullYear() === today.getFullYear()
    );
  };

  const isFutureDate = (day: number) => {
    const date = new Date(currentMonth.getFullYear(), currentMonth.getMonth(), day);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    return date > today;
  };

  const changeMonth = (delta: number) => {
    const newDate = new Date(currentMonth);
    newDate.setMonth(newDate.getMonth() + delta);
    setCurrentMonth(newDate);
  };

  const monthNames = ['January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December'];

  if (loading) {
    return (
      <SafeAreaView style={{ flex: 1, backgroundColor: colors.background.secondary }}>
        <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
          <Text style={{ color: colors.text.secondary }}>Loading progress...</Text>
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
            paddingBottom: 70,
            paddingHorizontal: 20,
            borderBottomLeftRadius: 20,
            borderBottomRightRadius: 20,
            borderWidth: 2,
            borderColor: colors.orange.DEFAULT,
            borderTopWidth: 0,
          }}
        >
          <Text style={{ color: '#fff', fontSize: 28, fontWeight: '800' }}>
            Your Foundation 🧱
          </Text>
          <Text style={{ color: 'rgba(255,255,255,0.8)', fontSize: 14, marginTop: 4 }}>
            Every brick builds your future
          </Text>
        </LinearGradient>

        {/* Stats Row */}
        <View style={{ paddingHorizontal: 20, marginTop: -50 }}>
          <View style={{ flexDirection: 'row', gap: 10 }}>
            <View
              style={{
                flex: 1,
                backgroundColor: colors.background.tertiary,
                borderRadius: 12,
                padding: 12,
                alignItems: 'center',
                ...orangeOutline,
              }}
            >
              <Text style={{ fontSize: 20, marginBottom: 2 }}>🧱</Text>
              <Text style={{ color: colors.orange.DEFAULT, fontSize: 24, fontWeight: '800' }}>
                {brickStats?.totalBricks || 0}
              </Text>
              <Text style={{ color: colors.text.secondary, fontSize: 11 }}>Total Bricks</Text>
            </View>
            <View
              style={{
                flex: 1,
                backgroundColor: colors.background.tertiary,
                borderRadius: 12,
                padding: 12,
                alignItems: 'center',
                ...orangeOutline,
              }}
            >
              <Text style={{ fontSize: 20, marginBottom: 2 }}>🔥</Text>
              <Text style={{ color: colors.text.primary, fontSize: 24, fontWeight: '800' }}>
                {brickStats?.currentStreak || 0}
              </Text>
              <Text style={{ color: colors.text.secondary, fontSize: 11 }}>Streak</Text>
            </View>
            <View
              style={{
                flex: 1,
                backgroundColor: colors.background.tertiary,
                borderRadius: 12,
                padding: 12,
                alignItems: 'center',
                ...orangeOutline,
              }}
            >
              <Text style={{ fontSize: 20, marginBottom: 2 }}>📅</Text>
              <Text style={{ color: colors.text.primary, fontSize: 24, fontWeight: '800' }}>
                {brickStats?.bricksThisMonth || 0}
              </Text>
              <Text style={{ color: colors.text.secondary, fontSize: 11 }}>This Month</Text>
            </View>
          </View>
        </View>

        {/* Brick Wall Calendar */}
        <View style={{ paddingHorizontal: 20, marginTop: 24 }}>
          <View
            style={{
              backgroundColor: colors.background.tertiary,
              borderRadius: 20,
              padding: 20,
              ...orangeOutline,
              ...orangeGlow,
            }}
          >
            {/* Month Navigation */}
            <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 }}>
              <TouchableOpacity onPress={() => changeMonth(-1)}>
                <Ionicons name="chevron-back" size={24} color={colors.blue.DEFAULT} />
              </TouchableOpacity>
              <Text style={{ color: colors.text.primary, fontSize: 18, fontWeight: '700' }}>
                {monthNames[currentMonth.getMonth()]} {currentMonth.getFullYear()}
              </Text>
              <TouchableOpacity onPress={() => changeMonth(1)}>
                <Ionicons name="chevron-forward" size={24} color={colors.blue.DEFAULT} />
              </TouchableOpacity>
            </View>

            {/* Day Headers */}
            <View style={{ flexDirection: 'row', marginBottom: 8 }}>
              {['S', 'M', 'T', 'W', 'T', 'F', 'S'].map((day, index) => (
                <View key={index} style={{ flex: 1, alignItems: 'center' }}>
                  <Text style={{ color: colors.text.muted, fontSize: 12, fontWeight: '600' }}>{day}</Text>
                </View>
              ))}
            </View>

            {/* Calendar Grid */}
            <View style={{ flexDirection: 'row', flexWrap: 'wrap' }}>
              {generateCalendarDays().map((day, index) => {
                const brick = day ? getBrickForDate(day) : null;
                const today = day ? isToday(day) : false;
                const future = day ? isFutureDate(day) : false;

                return (
                  <View
                    key={index}
                    style={{
                      width: '14.28%',
                      aspectRatio: 1,
                      padding: 2,
                    }}
                  >
                    {day !== null && (
                      <View
                        style={{
                          flex: 1,
                          backgroundColor: brick
                            ? colors.brick.workout
                            : future
                              ? 'transparent'
                              : colors.background.elevated,
                          borderRadius: 6,
                          justifyContent: 'center',
                          alignItems: 'center',
                          borderWidth: today ? 2 : 0,
                          borderColor: today ? colors.orange.DEFAULT : 'transparent',
                        }}
                      >
                        <Text
                          style={{
                            color: brick
                              ? colors.text.primary
                              : future
                                ? colors.text.muted
                                : colors.text.secondary,
                            fontSize: 12,
                            fontWeight: brick || today ? '700' : '400',
                          }}
                        >
                          {day}
                        </Text>
                      </View>
                    )}
                  </View>
                );
              })}
            </View>

            {/* Legend */}
            <View style={{ flexDirection: 'row', justifyContent: 'center', gap: 16, marginTop: 16 }}>
              <View style={{ flexDirection: 'row', alignItems: 'center', gap: 4 }}>
                <View style={{ width: 12, height: 12, backgroundColor: colors.brick.workout, borderRadius: 3 }} />
                <Text style={{ color: colors.text.secondary, fontSize: 11 }}>Workout</Text>
              </View>
              <View style={{ flexDirection: 'row', alignItems: 'center', gap: 4 }}>
                <View style={{ width: 12, height: 12, backgroundColor: colors.brick.streakBonus, borderRadius: 3 }} />
                <Text style={{ color: colors.text.secondary, fontSize: 11 }}>Streak</Text>
              </View>
              <View style={{ flexDirection: 'row', alignItems: 'center', gap: 4 }}>
                <View style={{ width: 12, height: 12, backgroundColor: colors.brick.milestone, borderRadius: 3 }} />
                <Text style={{ color: colors.text.secondary, fontSize: 11 }}>Milestone</Text>
              </View>
            </View>
          </View>
        </View>

        {/* Recent Achievements */}
        <View style={{ paddingHorizontal: 20, marginTop: 24 }}>
          <Text style={{ color: colors.text.primary, fontSize: 20, fontWeight: '700', marginBottom: 12 }}>
            Recent Achievements
          </Text>

          {milestones.length === 0 ? (
            <View
              style={{
                backgroundColor: colors.background.tertiary,
                borderRadius: 16,
                padding: 32,
                alignItems: 'center',
                ...orangeOutline,
              }}
            >
              <Text style={{ fontSize: 40, marginBottom: 12 }}>🏆</Text>
              <Text style={{ color: colors.text.primary, fontSize: 16, fontWeight: '600', marginBottom: 4 }}>
                No achievements yet
              </Text>
              <Text style={{ color: colors.text.secondary, textAlign: 'center' }}>
                Complete workouts to earn achievements!
              </Text>
            </View>
          ) : (
            <View style={{ gap: 12 }}>
              {milestones.map((milestone) => (
                <View
                  key={milestone.milestoneId}
                  style={{
                    backgroundColor: colors.background.tertiary,
                    borderRadius: 16,
                    padding: 16,
                    flexDirection: 'row',
                    alignItems: 'center',
                    ...orangeOutline,
                  }}
                >
                  <View
                    style={{
                      backgroundColor: colors.amber.DEFAULT,
                      width: 44,
                      height: 44,
                      borderRadius: 22,
                      justifyContent: 'center',
                      alignItems: 'center',
                      marginRight: 14,
                    }}
                  >
                    <Text style={{ fontSize: 22 }}>🏆</Text>
                  </View>
                  <View style={{ flex: 1 }}>
                    <Text style={{ color: colors.text.primary, fontSize: 16, fontWeight: '600' }}>
                      {milestone.milestoneName}
                    </Text>
                    <Text style={{ color: colors.text.secondary, fontSize: 14 }}>
                      {milestone.description}
                    </Text>
                  </View>
                </View>
              ))}
            </View>
          )}
        </View>
      </ScrollView>
    </View>
  );
}