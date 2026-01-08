import React, { useEffect, useState, useCallback } from 'react';
import { View, Text, ScrollView, RefreshControl } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useFocusEffect } from '@react-navigation/native';
import { Card, Header } from '../components';
import { colors } from '../constants/theme';
import { brickApi, milestoneApi } from '../services/api';
import { BrickResponse, BrickStatsResponse, MilestoneDTO } from '../types/api';

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

  // Generate calendar grid
  const generateCalendarDays = () => {
    const year = currentMonth.getFullYear();
    const month = currentMonth.getMonth();
    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const daysInMonth = lastDay.getDate();
    const startingDay = firstDay.getDay();

    const days: (number | null)[] = [];

    // Add empty slots for days before the 1st
    for (let i = 0; i < startingDay; i++) {
      days.push(null);
    }

    // Add days of the month
    for (let i = 1; i <= daysInMonth; i++) {
      days.push(i);
    }

    return days;
  };

  const getBrickForDate = (day: number) => {
    const dateStr = `${currentMonth.getFullYear()}-${String(currentMonth.getMonth() + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
    return brickCalendar.find((b) => b.brickDate === dateStr);
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

  const monthNames = [
    'January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December',
  ];

  const consistencyPercent = brickStats
    ? Math.round((brickStats.bricksThisMonth / new Date().getDate()) * 100)
    : 0;

  if (loading) {
    return (
      <SafeAreaView style={{ flex: 1, backgroundColor: colors.background.primary }}>
        <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
          <Text style={{ color: colors.text.secondary }}>Loading...</Text>
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
        {/* Header Stats */}
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
            Your Foundation
          </Text>

          <View style={{ flexDirection: 'row', justifyContent: 'space-between' }}>
            <View>
              <Text style={{ color: colors.orange.DEFAULT, fontSize: 32, fontWeight: '800' }}>
                {brickStats?.totalBricks || 0}
              </Text>
              <Text style={{ color: 'rgba(255,255,255,0.7)', fontSize: 14 }}>Bricks Laid</Text>
            </View>
            <View>
              <Text style={{ color: colors.text.primary, fontSize: 32, fontWeight: '800' }}>
                {brickStats?.currentStreak || 0}
              </Text>
              <Text style={{ color: 'rgba(255,255,255,0.7)', fontSize: 14 }}>Day Streak</Text>
            </View>
            <View>
              <Text style={{ color: colors.text.primary, fontSize: 32, fontWeight: '800' }}>
                {consistencyPercent}%
              </Text>
              <Text style={{ color: 'rgba(255,255,255,0.7)', fontSize: 14 }}>Consistency</Text>
            </View>
          </View>
        </View>

        {/* Brick Wall Calendar */}
        <View style={{ paddingHorizontal: 16, marginTop: 24 }}>
          <Text style={{ color: colors.text.primary, fontSize: 18, fontWeight: '600', marginBottom: 16 }}>
            {monthNames[currentMonth.getMonth()]} {currentMonth.getFullYear()}
          </Text>

          {/* Day Headers */}
          <View style={{ flexDirection: 'row', marginBottom: 8 }}>
            {['S', 'M', 'T', 'W', 'T', 'F', 'S'].map((day, index) => (
              <View key={index} style={{ flex: 1, alignItems: 'center' }}>
                <Text style={{ color: colors.text.muted, fontSize: 12 }}>{day}</Text>
              </View>
            ))}
          </View>

          {/* Brick Wall Grid */}
          <View
            style={{
              flexDirection: 'row',
              flexWrap: 'wrap',
              backgroundColor: colors.background.secondary,
              borderRadius: 12,
              padding: 8,
            }}
          >
            {generateCalendarDays().map((day, index) => {
              const brick = day ? getBrickForDate(day) : null;
              const today = day ? isToday(day) : false;
              const future = day ? isFutureDate(day) : false;

              // Offset every other row for brick wall effect
              const row = Math.floor(index / 7);
              const isOffsetRow = row % 2 === 1;

              return (
                <View
                  key={index}
                  style={{
                    width: '14.28%',
                    aspectRatio: 1,
                    padding: 2,
                    marginLeft: isOffsetRow && index % 7 === 0 ? '3%' : 0,
                  }}
                >
                  {day !== null && (
                    <View
                      style={{
                        flex: 1,
                        backgroundColor: brick
                          ? colors.brick.workout
                          : today
                          ? colors.blue.DEFAULT
                          : future
                          ? 'transparent'
                          : colors.background.tertiary,
                        borderRadius: 4,
                        justifyContent: 'center',
                        alignItems: 'center',
                        borderWidth: today && !brick ? 2 : 0,
                        borderColor: colors.blue.light,
                      }}
                    >
                      <Text
                        style={{
                          color: brick || today ? colors.text.primary : future ? colors.text.muted : colors.text.secondary,
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
          <View style={{ flexDirection: 'row', justifyContent: 'center', gap: 16, marginTop: 12 }}>
            <View style={{ flexDirection: 'row', alignItems: 'center', gap: 4 }}>
              <View style={{ width: 12, height: 12, backgroundColor: colors.brick.workout, borderRadius: 2 }} />
              <Text style={{ color: colors.text.secondary, fontSize: 12 }}>Completed</Text>
            </View>
            <View style={{ flexDirection: 'row', alignItems: 'center', gap: 4 }}>
              <View style={{ width: 12, height: 12, backgroundColor: colors.blue.DEFAULT, borderRadius: 2 }} />
              <Text style={{ color: colors.text.secondary, fontSize: 12 }}>Today</Text>
            </View>
            <View style={{ flexDirection: 'row', alignItems: 'center', gap: 4 }}>
              <View style={{ width: 12, height: 12, backgroundColor: colors.background.tertiary, borderRadius: 2 }} />
              <Text style={{ color: colors.text.secondary, fontSize: 12 }}>Missed</Text>
            </View>
          </View>
        </View>

        {/* Recent Milestones */}
        <View style={{ paddingHorizontal: 16, marginTop: 32 }}>
          <Text style={{ color: colors.text.primary, fontSize: 18, fontWeight: '600', marginBottom: 12 }}>
            Recent Achievements
          </Text>

          {milestones.length === 0 ? (
            <Card>
              <View style={{ alignItems: 'center', padding: 16 }}>
                <Text style={{ fontSize: 32, marginBottom: 8 }}>🏆</Text>
                <Text style={{ color: colors.text.secondary, textAlign: 'center' }}>
                  Complete workouts to earn achievements!
                </Text>
              </View>
            </Card>
          ) : (
            <View style={{ gap: 12 }}>
              {milestones.map((milestone) => (
                <Card key={milestone.milestoneId}>
                  <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                    <View
                      style={{
                        backgroundColor: colors.amber.DEFAULT,
                        width: 40,
                        height: 40,
                        borderRadius: 20,
                        justifyContent: 'center',
                        alignItems: 'center',
                        marginRight: 12,
                      }}
                    >
                      <Text style={{ fontSize: 20 }}>🏆</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                      <Text style={{ color: colors.text.primary, fontSize: 16, fontWeight: '600' }}>
                        {milestone.milestoneName}
                      </Text>
                      <Text style={{ color: colors.text.secondary, fontSize: 14 }}>{milestone.description}</Text>
                    </View>
                  </View>
                </Card>
              ))}
            </View>
          )}
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}