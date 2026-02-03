import React, { useState, useCallback, useEffect, useRef } from 'react';
import { View, Text, ScrollView, TouchableOpacity, RefreshControl, Animated, Easing } from 'react-native';
import { useFocusEffect } from '@react-navigation/native';
import { LinearGradient } from 'expo-linear-gradient';
import { ChevronLeft, ChevronRight, Trophy, Flame, Snowflake, Target, Award, TrendingUp, Zap, Sunrise, Lock } from 'lucide-react-native';
import { colors, gradients, shadows, radius, spacing, typography } from '../constants/theme';
import { brickApi } from '../services/api';
import { BrickResponse, BrickStatsResponse } from '../types/api';
import B3Logo from '../components/B3Logo';
import BrickBackground from '../components/BrickBackground';
import * as Haptics from '../utils/haptics';

// Hardcoded achievements to showcase
const SHOWCASE_ACHIEVEMENTS = [
  { id: 1, name: 'First Brick', description: 'Complete your first workout', icon: Target, color: colors.orange.DEFAULT, unlocked: true },
  { id: 2, name: 'On Fire', description: '3-day workout streak', icon: Flame, color: colors.amber.DEFAULT, unlocked: true },
  { id: 3, name: 'Week Warrior', description: '7-day workout streak', icon: Zap, color: colors.blue.DEFAULT, unlocked: true },
  { id: 4, name: 'Foundation Builder', description: 'Complete 10 total workouts', icon: Award, color: colors.green.DEFAULT, unlocked: true },
  { id: 5, name: 'Early Bird', description: 'Complete a workout before 7 AM', icon: Sunrise, color: colors.purple.DEFAULT, unlocked: false },
  { id: 6, name: 'Unstoppable', description: '30-day workout streak', icon: TrendingUp, color: colors.orange.light, unlocked: false },
];

// Generate mock bricks for a 7-day streak ending today
const generateMockBricks = (): BrickResponse[] => {
  const bricks: BrickResponse[] = [];
  const today = new Date();

  for (let i = 6; i >= 0; i--) {
    const date = new Date(today);
    date.setDate(today.getDate() - i);
    const dateStr = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;

    bricks.push({
      brickId: 100 + i,
      brickDate: dateStr,
      brickType: 'WORKOUT',
      workoutName: ['Morning Flow', 'Power Hour', 'Core Blast', 'Full Body', 'HIIT Session', 'Strength Training', 'Active Recovery'][6 - i],
      workoutType: 'STRENGTH',
      duration: 20 + (i * 5),
      createdAt: dateStr,
    });
  }

  return bricks;
};

const MOCK_BRICKS = generateMockBricks();

// Animated 3D Brick Component
const AnimatedBrick = ({
  day,
  hasBrick,
  isToday,
  isFuture,
  heatColor,
  index
}: {
  day: number | null;
  hasBrick: boolean;
  isToday: boolean;
  isFuture: boolean;
  heatColor: string;
  index: number;
}) => {
  const scaleAnim = useRef(new Animated.Value(0)).current;
  const opacityAnim = useRef(new Animated.Value(0)).current;
  const pulseAnim = useRef(new Animated.Value(1)).current;

  useEffect(() => {
    if (day !== null) {
      // Staggered entrance animation
      const delay = index * 15;

      Animated.parallel([
        Animated.timing(opacityAnim, {
          toValue: 1,
          duration: 300,
          delay,
          useNativeDriver: true,
        }),
        Animated.spring(scaleAnim, {
          toValue: 1,
          tension: 100,
          friction: 8,
          delay,
          useNativeDriver: true,
        }),
      ]).start();

      // Pulse animation for today's brick
      if (isToday && hasBrick) {
        Animated.loop(
          Animated.sequence([
            Animated.timing(pulseAnim, {
              toValue: 1.08,
              duration: 1000,
              easing: Easing.inOut(Easing.ease),
              useNativeDriver: true,
            }),
            Animated.timing(pulseAnim, {
              toValue: 1,
              duration: 1000,
              easing: Easing.inOut(Easing.ease),
              useNativeDriver: true,
            }),
          ])
        ).start();
      }
    }
  }, [day, hasBrick, isToday]);

  if (day === null) {
    return <View style={{ width: '14.28%', aspectRatio: 1, padding: 2 }} />;
  }

  // 3D brick effect colors
  const highlightColor = hasBrick ? adjustBrightness(heatColor, 30) : 'transparent';
  const shadowColor = hasBrick ? adjustBrightness(heatColor, -40) : 'transparent';

  return (
    <Animated.View
      style={{
        width: '14.28%',
        aspectRatio: 1,
        padding: 2,
        opacity: opacityAnim,
        transform: [
          { scale: Animated.multiply(scaleAnim, isToday && hasBrick ? pulseAnim : new Animated.Value(1)) }
        ],
      }}
    >
      {hasBrick ? (
        // 3D Brick with gradients and depth
        <View style={{
          flex: 1,
          borderRadius: radius.sm + 2,
          overflow: 'hidden',
          shadowColor: heatColor,
          shadowOffset: { width: 0, height: 2 },
          shadowOpacity: 0.5,
          shadowRadius: 4,
          elevation: 4,
        }}>
          {/* Main brick body */}
          <LinearGradient
            colors={[highlightColor, heatColor, shadowColor]}
            locations={[0, 0.4, 1]}
            style={{
              flex: 1,
              justifyContent: 'center',
              alignItems: 'center',
              borderWidth: isToday ? 2 : 0,
              borderColor: isToday ? '#fff' : 'transparent',
              borderRadius: radius.sm + 2,
            }}
          >
            {/* Top shine effect */}
            <View style={{
              position: 'absolute',
              top: 0,
              left: 0,
              right: 0,
              height: '30%',
              backgroundColor: 'rgba(255,255,255,0.2)',
              borderTopLeftRadius: radius.sm,
              borderTopRightRadius: radius.sm,
            }} />

            {/* Bottom shadow line */}
            <View style={{
              position: 'absolute',
              bottom: 0,
              left: 0,
              right: 0,
              height: 3,
              backgroundColor: 'rgba(0,0,0,0.3)',
              borderBottomLeftRadius: radius.sm,
              borderBottomRightRadius: radius.sm,
            }} />

            <Text style={{
              color: '#fff',
              fontSize: typography.sizes.xs,
              fontWeight: typography.weights.bold,
              textShadowColor: 'rgba(0,0,0,0.5)',
              textShadowOffset: { width: 0, height: 1 },
              textShadowRadius: 2,
            }}>
              {day}
            </Text>
          </LinearGradient>
        </View>
      ) : (
        // Empty day cell
        <View style={{
          flex: 1,
          backgroundColor: isFuture ? 'transparent' : colors.background.glass,
          borderRadius: radius.sm,
          justifyContent: 'center',
          alignItems: 'center',
          borderWidth: isToday ? 2 : 1,
          borderColor: isToday ? '#fff' : isFuture ? 'transparent' : colors.background.glassBorder,
        }}>
          <Text style={{
            color: isFuture ? colors.text.muted : colors.text.secondary,
            fontSize: typography.sizes.xs,
            fontWeight: isToday ? typography.weights.bold : typography.weights.normal,
          }}>
            {day}
          </Text>
        </View>
      )}
    </Animated.View>
  );
};

// Helper function to adjust color brightness
const adjustBrightness = (hex: string, percent: number): string => {
  const num = parseInt(hex.replace('#', ''), 16);
  const amt = Math.round(2.55 * percent);
  const R = Math.min(255, Math.max(0, (num >> 16) + amt));
  const G = Math.min(255, Math.max(0, ((num >> 8) & 0x00FF) + amt));
  const B = Math.min(255, Math.max(0, (num & 0x0000FF) + amt));
  return '#' + (0x1000000 + R * 0x10000 + G * 0x100 + B).toString(16).slice(1);
};

export default function ProgressScreen() {
  const [brickStats, setBrickStats] = useState<BrickStatsResponse | null>(null);
  const [brickCalendar, setBrickCalendar] = useState<BrickResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [currentMonth, setCurrentMonth] = useState(new Date());

  const loadData = async () => {
    try {
      const [statsData, calendarData] = await Promise.all([
        brickApi.getBrickStats(),
        brickApi.getBrickCalendar(1, currentMonth.getFullYear(), currentMonth.getMonth() + 1),
      ]);

      // Merge API data with mock bricks, prioritizing mock data for demo
      const existingDates = new Set(calendarData.map(b => b.brickDate));
      const mergedBricks = [
        ...calendarData,
        ...MOCK_BRICKS.filter(b => !existingDates.has(b.brickDate))
      ];

      // Override stats with mock data to show the streak
      const mockStats: BrickStatsResponse = {
        ...statsData,
        totalBricks: Math.max(statsData?.totalBricks || 0, 12),
        currentStreak: 7,
        longestStreak: Math.max(statsData?.longestStreak || 0, 7),
      };

      setBrickStats(mockStats);
      setBrickCalendar(mergedBricks);
    } catch (error) {
      console.error('Error loading progress data:', error);
      // Even on error, show mock data for demo
      setBrickStats({ totalBricks: 12, currentStreak: 7, longestStreak: 7, thisWeekBricks: 7, thisMonthBricks: 7 });
      setBrickCalendar(MOCK_BRICKS);
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

  // Heat map color calculation - returns color based on streak intensity
  const getHeatMapColor = (day: number): string => {
    const brick = getBrickForDate(day);
    if (!brick) return colors.background.glass;

    // Calculate consecutive days leading up to this day
    let consecutiveDays = 1;
    let checkDay = day - 1;

    while (checkDay >= 1) {
      const prevBrick = getBrickForDate(checkDay);
      if (prevBrick) {
        consecutiveDays++;
        checkDay--;
      } else {
        break;
      }
    }

    // Color scale from ice cold (blue) to fire hot (orange/red)
    if (consecutiveDays >= 7) return '#ef4444'; // Fire red - 7+ days
    if (consecutiveDays >= 5) return '#f97316'; // Orange - 5-6 days
    if (consecutiveDays >= 3) return '#f59e0b'; // Amber - 3-4 days
    if (consecutiveDays >= 2) return '#60a5fa'; // Light blue - 2 days
    return '#3b82f6'; // Blue - 1 day (cold start)
  };

  const getHeatMapGlow = (day: number) => {
    const brick = getBrickForDate(day);
    if (!brick) return {};

    let consecutiveDays = 1;
    let checkDay = day - 1;
    while (checkDay >= 1 && getBrickForDate(checkDay)) {
      consecutiveDays++;
      checkDay--;
    }

    if (consecutiveDays >= 5) {
      return {
        shadowColor: '#f97316',
        shadowOffset: { width: 0, height: 0 },
        shadowOpacity: 0.6,
        shadowRadius: 6,
      };
    }
    return {};
  };

  const changeMonth = (delta: number) => {
    Haptics.lightTap();
    const newDate = new Date(currentMonth);
    newDate.setMonth(newDate.getMonth() + delta);
    setCurrentMonth(newDate);
  };

  const monthNames = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];

  if (loading) {
    return (
      <BrickBackground>
        <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
          <B3Logo size={80} />
        </View>
      </BrickBackground>
    );
  }

  return (
    <BrickBackground>
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
              { icon: (brickStats?.currentStreak || 0) >= 3 ? Flame : Snowflake, value: brickStats?.currentStreak || 0, label: 'Streak', color: (brickStats?.currentStreak || 0) >= 3 ? colors.orange.DEFAULT : colors.blue.DEFAULT },
              { icon: Trophy, value: brickStats?.longestStreak || 0, label: 'Best Streak', color: colors.amber.DEFAULT },
            ].map((stat, i) => (
              <View key={i} style={{ flex: 1, backgroundColor: colors.background.glass, borderRadius: radius.xl, padding: spacing.lg, borderWidth: 1, borderColor: colors.background.glassBorder, ...shadows.card }}>
                <View style={{ backgroundColor: stat.color + '20', width: 40, height: 40, borderRadius: radius.md, justifyContent: 'center', alignItems: 'center', marginBottom: spacing.sm }}>
                  <stat.icon size={20} color={stat.color} />
                </View>
                <Text style={{ color: colors.text.primary, fontSize: typography.sizes['2xl'], fontWeight: typography.weights.black }}>{stat.value}</Text>
                <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs, marginTop: 2 }}>{stat.label}</Text>
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

            {/* Animated 3D Calendar Grid */}
            <View style={{ flexDirection: 'row', flexWrap: 'wrap' }}>
              {generateCalendarDays().map((day, index) => {
                const brick = day ? getBrickForDate(day) : null;
                const today = day ? isToday(day) : false;
                const future = day ? isFutureDate(day) : false;
                const heatColor = day ? getHeatMapColor(day) : colors.background.glass;

                return (
                  <AnimatedBrick
                    key={index}
                    day={day}
                    hasBrick={!!brick}
                    isToday={today}
                    isFuture={future}
                    heatColor={heatColor}
                    index={index}
                  />
                );
              })}
            </View>

            {/* Heat Map Legend */}
            <View style={{ marginTop: spacing.xl, paddingTop: spacing.lg, borderTopWidth: 1, borderTopColor: colors.background.glassBorder }}>
              <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs, textAlign: 'center', marginBottom: spacing.sm, letterSpacing: 1 }}>STREAK INTENSITY</Text>
              <View style={{ flexDirection: 'row', justifyContent: 'center', alignItems: 'center', gap: spacing.xs }}>
                <Snowflake size={14} color={colors.blue.DEFAULT} />
                <View style={{ flexDirection: 'row', alignItems: 'center', gap: 3 }}>
                  <View style={{ width: 20, height: 12, backgroundColor: '#3b82f6', borderRadius: 3 }} />
                  <View style={{ width: 20, height: 12, backgroundColor: '#60a5fa', borderRadius: 3 }} />
                  <View style={{ width: 20, height: 12, backgroundColor: '#f59e0b', borderRadius: 3 }} />
                  <View style={{ width: 20, height: 12, backgroundColor: '#f97316', borderRadius: 3 }} />
                  <View style={{ width: 20, height: 12, backgroundColor: '#ef4444', borderRadius: 3 }} />
                </View>
                <Flame size={14} color={colors.orange.DEFAULT} />
              </View>
              <View style={{ flexDirection: 'row', justifyContent: 'center', marginTop: spacing.sm }}>
                <Text style={{ color: colors.text.muted, fontSize: 9 }}>1 day</Text>
                <Text style={{ color: colors.text.muted, fontSize: 9, marginLeft: spacing['3xl'] }}>7+ days</Text>
              </View>
            </View>
          </View>
        </View>

        {/* Achievements */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing['2xl'] }}>
          <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: spacing.md }}>
            <Text style={{ color: colors.text.primary, fontSize: typography.sizes.xl, fontWeight: typography.weights.bold }}>
              Achievements
            </Text>
            <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm }}>
              {SHOWCASE_ACHIEVEMENTS.filter(a => a.unlocked).length}/{SHOWCASE_ACHIEVEMENTS.length} Unlocked
            </Text>
          </View>

          {/* Unlocked Achievements */}
          <View style={{ gap: spacing.md }}>
            {SHOWCASE_ACHIEVEMENTS.filter(a => a.unlocked).map((achievement) => (
              <View key={achievement.id} style={{
                backgroundColor: colors.background.card,
                borderRadius: radius.xl,
                padding: spacing.lg,
                flexDirection: 'row',
                alignItems: 'center',
                borderWidth: 1,
                borderColor: achievement.color + '40'
              }}>
                <View style={{
                  backgroundColor: achievement.color + '20',
                  width: 48,
                  height: 48,
                  borderRadius: 24,
                  justifyContent: 'center',
                  alignItems: 'center',
                  marginRight: spacing.lg,
                  borderWidth: 2,
                  borderColor: achievement.color + '40'
                }}>
                  <achievement.icon size={24} color={achievement.color} />
                </View>
                <View style={{ flex: 1 }}>
                  <Text style={{ color: colors.text.primary, fontSize: typography.sizes.base, fontWeight: typography.weights.semibold }}>
                    {achievement.name}
                  </Text>
                  <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm }}>
                    {achievement.description}
                  </Text>
                </View>
                <Trophy size={18} color={colors.amber.DEFAULT} />
              </View>
            ))}
          </View>

          {/* Locked Achievements */}
          <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.base, fontWeight: typography.weights.semibold, marginTop: spacing.xl, marginBottom: spacing.md }}>
            Locked
          </Text>
          <View style={{ gap: spacing.md }}>
            {SHOWCASE_ACHIEVEMENTS.filter(a => !a.unlocked).map((achievement) => (
              <View key={achievement.id} style={{
                backgroundColor: colors.background.card,
                borderRadius: radius.xl,
                padding: spacing.lg,
                flexDirection: 'row',
                alignItems: 'center',
                borderWidth: 1,
                borderColor: colors.background.glassBorder,
                opacity: 0.6
              }}>
                <View style={{
                  backgroundColor: colors.background.glass,
                  width: 48,
                  height: 48,
                  borderRadius: 24,
                  justifyContent: 'center',
                  alignItems: 'center',
                  marginRight: spacing.lg
                }}>
                  <achievement.icon size={24} color={colors.text.muted} />
                </View>
                <View style={{ flex: 1 }}>
                  <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.base, fontWeight: typography.weights.semibold }}>
                    {achievement.name}
                  </Text>
                  <Text style={{ color: colors.text.muted, fontSize: typography.sizes.sm }}>
                    {achievement.description}
                  </Text>
                </View>
                <Lock size={16} color={colors.text.muted} />
              </View>
            ))}
          </View>
        </View>
      </ScrollView>
    </BrickBackground>
  );
}
