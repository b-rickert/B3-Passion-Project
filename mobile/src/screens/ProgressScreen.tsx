import React, { useState, useCallback } from 'react';
import { View, Text, ScrollView, TouchableOpacity, RefreshControl } from 'react-native';
import { useFocusEffect } from '@react-navigation/native';
import { LinearGradient } from 'expo-linear-gradient';
import { ChevronLeft, ChevronRight, Trophy, Flame, Target, Award } from 'lucide-react-native';
import { colors, gradients, shadows, radius, spacing, typography } from '../constants/theme';
import { brickApi, milestoneApi } from '../services/api';
import { BrickResponse, BrickStatsResponse, MilestoneDTO } from '../types/api';
import B3Logo from '../components/B3Logo';

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

  useFocusEffect(useCallback(() => { loadData(); }, [currentMonth]));
  const onRefresh = () => { setRefreshing(true); loadData(); };

  const generateCalendarDays = () => {
    const year = currentMonth.getFullYear();
    const month = currentMonth.getMonth();
    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const daysInMonth = lastDay.getDate();
    const startingDay = firstDay.getDay();
    const days: (number | null)[] = [];
    for (let i = 0; i < startingDay; i++) days.push(null);
    for (let i = 1; i <= daysInMonth; i++) days.push(i);
    return days;
  };

  const getBrickForDate = (day: number) => {
    const dateStr = `${currentMonth.getFullYear()}-${String(currentMonth.getMonth() + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
    return brickCalendar.find(b => b.brickDate === dateStr);
  };

  const isToday = (day: number) => {
    const today = new Date();
    return day === today.getDate() && currentMonth.getMonth() === today.getMonth() && currentMonth.getFullYear() === today.getFullYear();
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

  const monthNames = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];

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
        <LinearGradient colors={['rgba(59, 130, 246, 0.15)', 'transparent']} style={{ position: 'absolute', top: 100, left: -100, width: 300, height: 300, borderRadius: 150 }} />
      </View>

      <ScrollView style={{ flex: 1 }} contentContainerStyle={{ paddingBottom: 120 }} showsVerticalScrollIndicator={false} refreshControl={<RefreshControl refreshing={refreshing} onRefresh={onRefresh} tintColor={colors.orange.DEFAULT} />}>
        {/* Header */}
        <View style={{ paddingHorizontal: spacing.xl, paddingTop: 70 }}>
          <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'flex-start' }}>
            <View>
              <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm, letterSpacing: 2, textTransform: 'uppercase' }}>Your</Text>
              <Text style={{ color: colors.text.primary, fontSize: typography.sizes['4xl'], fontWeight: typography.weights.black, letterSpacing: -1 }}>Foundation</Text>
            </View>
            <B3Logo size={48} />
          </View>
        </View>

        {/* Stats Row */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing.xl }}>
          <View style={{ flexDirection: 'row', gap: spacing.md }}>
            {[
              { icon: Target, value: brickStats?.totalBricks || 0, label: 'Total Bricks', color: colors.orange.DEFAULT },
              { icon: Flame, value: brickStats?.currentStreak || 0, label: 'Streak', color: colors.orange.DEFAULT },
              { icon: Trophy, value: brickStats?.bricksThisMonth || 0, label: 'This Month', color: colors.orange.DEFAULT },
            ].map((stat, i) => (
              <View key={i} style={{ flex: 1, backgroundColor: colors.background.glass, borderRadius: radius.xl, padding: spacing.lg, borderWidth: 1, borderColor: colors.background.glassBorder }}>
                <View style={{ backgroundColor: stat.color + '20', width: 36, height: 36, borderRadius: radius.md, justifyContent: 'center', alignItems: 'center', marginBottom: spacing.sm }}>
                  <stat.icon size={18} color={stat.color} />
                </View>
                <Text style={{ color: colors.text.primary, fontSize: typography.sizes['2xl'], fontWeight: typography.weights.black }}>{stat.value}</Text>
                <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs }}>{stat.label}</Text>
              </View>
            ))}
          </View>
        </View>

        {/* Brick Wall Calendar */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing['2xl'] }}>
          <View style={{ backgroundColor: colors.background.card, borderRadius: radius['2xl'], padding: spacing.xl, borderWidth: 1, borderColor: colors.background.glassBorder, ...shadows.card }}>
            {/* Month Navigation */}
            <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: spacing.lg }}>
              <TouchableOpacity onPress={() => changeMonth(-1)}>
                <ChevronLeft size={24} color={colors.blue.DEFAULT} />
              </TouchableOpacity>
              <Text style={{ color: colors.text.primary, fontSize: typography.sizes.xl, fontWeight: typography.weights.bold }}>
                {monthNames[currentMonth.getMonth()]} {currentMonth.getFullYear()}
              </Text>
              <TouchableOpacity onPress={() => changeMonth(1)}>
                <ChevronRight size={24} color={colors.blue.DEFAULT} />
              </TouchableOpacity>
            </View>

            {/* Day Headers */}
            <View style={{ flexDirection: 'row', marginBottom: spacing.sm }}>
              {['S', 'M', 'T', 'W', 'T', 'F', 'S'].map((day, index) => (
                <View key={index} style={{ flex: 1, alignItems: 'center' }}>
                  <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs, fontWeight: typography.weights.semibold }}>{day}</Text>
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
                  <View key={index} style={{ width: '14.28%', aspectRatio: 1, padding: 2 }}>
                    {day !== null && (
                      <View style={{
                        flex: 1,
                        backgroundColor: brick ? colors.orange.DEFAULT : future ? 'transparent' : colors.background.glass,
                        borderRadius: radius.sm,
                        justifyContent: 'center',
                        alignItems: 'center',
                        borderWidth: today ? 2 : 0,
                        borderColor: today ? colors.blue.DEFAULT : 'transparent',
                      }}>
                        <Text style={{
                          color: brick ? colors.text.primary : future ? colors.text.muted : colors.text.secondary,
                          fontSize: typography.sizes.xs,
                          fontWeight: brick || today ? typography.weights.bold : typography.weights.normal,
                        }}>
                          {day}
                        </Text>
                      </View>
                    )}
                  </View>
                );
              })}
            </View>

            {/* Legend */}
            <View style={{ flexDirection: 'row', justifyContent: 'center', gap: spacing.xl, marginTop: spacing.lg }}>
              <View style={{ flexDirection: 'row', alignItems: 'center', gap: spacing.xs }}>
                <View style={{ width: 12, height: 12, backgroundColor: colors.orange.DEFAULT, borderRadius: 3 }} />
                <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.xs }}>Workout</Text>
              </View>
              <View style={{ flexDirection: 'row', alignItems: 'center', gap: spacing.xs }}>
                <View style={{ width: 12, height: 12, backgroundColor: colors.blue.DEFAULT, borderRadius: 3 }} />
                <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.xs }}>Today</Text>
              </View>
            </View>
          </View>
        </View>

        {/* Recent Achievements */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing['2xl'] }}>
          <Text style={{ color: colors.text.primary, fontSize: typography.sizes.xl, fontWeight: typography.weights.bold, marginBottom: spacing.md }}>
            Recent Achievements
          </Text>

          {milestones.length === 0 ? (
            <View style={{ backgroundColor: colors.background.card, borderRadius: radius.xl, padding: spacing['2xl'], alignItems: 'center', borderWidth: 1, borderColor: colors.background.glassBorder }}>
              <Trophy size={40} color={colors.text.muted} />
              <Text style={{ color: colors.text.primary, fontSize: typography.sizes.lg, fontWeight: typography.weights.semibold, marginTop: spacing.md }}>No achievements yet</Text>
              <Text style={{ color: colors.text.secondary, textAlign: 'center', marginTop: spacing.xs }}>Complete workouts to earn achievements!</Text>
            </View>
          ) : (
            <View style={{ gap: spacing.md }}>
              {milestones.map((milestone) => (
                <View key={milestone.milestoneId} style={{ backgroundColor: colors.background.card, borderRadius: radius.xl, padding: spacing.lg, flexDirection: 'row', alignItems: 'center', borderWidth: 1, borderColor: colors.background.glassBorder }}>
                  <LinearGradient colors={gradients.fire} style={{ width: 44, height: 44, borderRadius: 22, justifyContent: 'center', alignItems: 'center', marginRight: spacing.lg }}>
                    <Award size={22} color="#fff" />
                  </LinearGradient>
                  <View style={{ flex: 1 }}>
                    <Text style={{ color: colors.text.primary, fontSize: typography.sizes.base, fontWeight: typography.weights.semibold }}>{milestone.milestoneName}</Text>
                    <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm }}>{milestone.description}</Text>
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
