import React, { useEffect, useState } from 'react';
import { View, Text, ScrollView, TouchableOpacity, RefreshControl, Dimensions, Animated } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { LinearGradient } from 'expo-linear-gradient';
import { Flame, Snowflake, Zap, Trophy, ChevronRight, Play, Target, MessageCircle, Sparkles, TrendingUp, Clock, Heart, Sword, PartyPopper, HandHeart } from 'lucide-react-native';
import { colors, gradients, shadows, radius, spacing, typography } from '../constants/theme';
import { profileApi, brickApi, workoutApi, dailyLogApi, behaviorApi } from '../services/api';
import { UserProfileResponse, BrickStatsResponse, WorkoutResponse, BehaviorProfileResponse } from '../types/api';
import B3Logo from '../components/B3Logo';
import BrickBackground from '../components/BrickBackground';

// BRIX mood configurations
const BRIX_MOODS = {
  ENCOURAGING: { icon: Heart, color: colors.green.DEFAULT, label: 'Supportive', message: "I'm here to lift you up!" },
  CHALLENGING: { icon: Sword, color: colors.orange.DEFAULT, label: 'Fired Up', message: "Let's push your limits!" },
  EMPATHETIC: { icon: HandHeart, color: colors.blue.DEFAULT, label: 'Understanding', message: "I understand. Take your time." },
  CELEBRATORY: { icon: PartyPopper, color: colors.amber.DEFAULT, label: 'Celebrating', message: "You're crushing it!" },
};

const { width: SCREEN_WIDTH, height: SCREEN_HEIGHT } = Dimensions.get('window');

export default function HomeScreen() {
  const navigation = useNavigation();
  const [profile, setProfile] = useState<UserProfileResponse | null>(null);
  const [brickStats, setBrickStats] = useState<BrickStatsResponse | null>(null);
  const [behaviorProfile, setBehaviorProfile] = useState<BehaviorProfileResponse | null>(null);
  const [recommendedWorkout, setRecommendedWorkout] = useState<WorkoutResponse | null>(null);
  const [hasLoggedToday, setHasLoggedToday] = useState(false);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);

  const loadData = async () => {
    try {
      const [profileData, statsData, workouts, logCheck, behaviorData] = await Promise.all([
        profileApi.getProfile(),
        brickApi.getBrickStats(),
        workoutApi.getAllWorkouts(),
        dailyLogApi.hasLoggedToday().catch(() => ({ hasLoggedToday: false })),
        behaviorApi.getBehaviorProfile().catch(() => null),
      ]);
      setProfile(profileData);

      // Use mock data for demo if no real workouts exist (matches calendar view)
      const mockStats = {
        totalBricks: Math.max(statsData?.totalBricks || 0, 12),
        currentStreak: Math.max(statsData?.currentStreak || 0, 7),
        longestStreak: Math.max(statsData?.longestStreak || 0, 7),
        bricksThisMonth: Math.max(statsData?.bricksThisMonth || 0, 7),
        bricksThisWeek: Math.max(statsData?.bricksThisWeek || 0, 7),
      };
      setBrickStats(mockStats);

      setBehaviorProfile(behaviorData);
      setHasLoggedToday(logCheck.hasLoggedToday);
      // Pick a recommended workout (first one for now, could be smarter based on profile)
      if (workouts.length > 0) {
        setRecommendedWorkout(workouts[0]);
      }
    } catch (error) {
      console.error('Error:', error);
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };

  useEffect(() => { loadData(); }, []);
  const onRefresh = () => { setRefreshing(true); loadData(); };

  const firstName = profile?.displayName?.split(' ')[0] || 'Builder';
  const streak = brickStats?.currentStreak || 0;

  const getStreakGradient = (): readonly [string, string, ...string[]] => {
    if (streak >= 7) return gradients.streakFire;
    if (streak >= 5) return gradients.streakHot;
    if (streak >= 3) return gradients.streakWarm;
    return gradients.streakCold;
  };

  const getStreakLabel = () => {
    if (streak >= 7) return 'ON FIRE';
    if (streak >= 5) return 'BLAZING';
    if (streak >= 3) return 'HEATING UP';
    if (streak >= 1) return 'WARMING UP';
    return 'GET STARTED';
  };

  const getStreakIcon = () => {
    if (streak >= 3) return Flame;
    return Snowflake;
  };

  const StreakIcon = getStreakIcon();

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
        {/* HEADER */}
        <View style={{ paddingHorizontal: spacing.xl, paddingTop: 70 }}>
          <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'flex-start' }}>
            <View>
              <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm, letterSpacing: 2, textTransform: 'uppercase' }}>Welcome back</Text>
              <Text style={{ color: colors.text.primary, fontSize: typography.sizes['4xl'], fontWeight: typography.weights.black, letterSpacing: typography.tracking.tighter, marginTop: spacing.xs }}>{firstName}</Text>
            </View>
            <B3Logo size={48} />
          </View>
        </View>

        {/* HERO STREAK CARD */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing['2xl'] }}>
          <TouchableOpacity activeOpacity={0.95} onPress={() => navigation.navigate('Progress' as never)}>
            <View style={{ borderRadius: radius['2xl'], overflow: 'hidden', ...shadows.glow }}>
              <LinearGradient colors={getStreakGradient()} start={{ x: 0, y: 0 }} end={{ x: 1, y: 1 }} style={{ padding: spacing['2xl'], position: 'relative' }}>
                {/* Decorative background shape */}
                <View style={{ position: 'absolute', top: 0, right: 0, bottom: 0, left: '50%', backgroundColor: 'rgba(255,255,255,0.08)', borderTopLeftRadius: 100, borderBottomLeftRadius: 200 }} />

                {/* Large background icon */}
                <View style={{ position: 'absolute', right: -20, top: -20, opacity: 0.15 }}>
                  <StreakIcon size={180} color="#fff" />
                </View>

                <View style={{ flexDirection: 'row', alignItems: 'flex-end', justifyContent: 'space-between' }}>
                  <View style={{ zIndex: 1 }}>
                    <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                      <View style={{ backgroundColor: 'rgba(255,255,255,0.2)', padding: 6, borderRadius: radius.md }}>
                        <StreakIcon size={18} color="#fff" />
                      </View>
                      <Text style={{ color: 'rgba(255,255,255,0.95)', fontSize: typography.sizes.xs, fontWeight: typography.weights.bold, letterSpacing: 3, marginLeft: spacing.sm, textTransform: 'uppercase' }}>{getStreakLabel()}</Text>
                    </View>
                    <Text style={{ color: '#fff', fontSize: typography.sizes['5xl'], fontWeight: typography.weights.black, letterSpacing: -4, lineHeight: typography.sizes['5xl'], marginTop: spacing.md }}>{streak}</Text>
                    <Text style={{ color: 'rgba(255,255,255,0.9)', fontSize: typography.sizes.lg, fontWeight: typography.weights.semibold, marginTop: -spacing.xs }}>day streak</Text>
                  </View>

                  <View style={{ alignItems: 'flex-end', zIndex: 1 }}>
                    <View style={{ backgroundColor: 'rgba(0,0,0,0.3)', paddingHorizontal: spacing.lg, paddingVertical: spacing.sm, borderRadius: radius.full, flexDirection: 'row', alignItems: 'center' }}>
                      <View style={{ width: 8, height: 8, borderRadius: 4, backgroundColor: colors.orange.light, marginRight: spacing.sm }} />
                      <Text style={{ color: '#fff', fontSize: typography.sizes.sm, fontWeight: typography.weights.bold }}>{brickStats?.totalBricks || 0} bricks</Text>
                    </View>
                    <View style={{ flexDirection: 'row', alignItems: 'center', marginTop: spacing.md }}>
                      <Text style={{ color: 'rgba(255,255,255,0.7)', fontSize: typography.sizes.xs, marginRight: spacing.xs }}>View Progress</Text>
                      <ChevronRight size={16} color="rgba(255,255,255,0.7)" />
                    </View>
                  </View>
                </View>

                {/* Weekly progress bar */}
                <View style={{ flexDirection: 'row', marginTop: spacing.xl, gap: 6, backgroundColor: 'rgba(0,0,0,0.2)', padding: spacing.md, borderRadius: radius.lg }}>
                  {['M', 'T', 'W', 'T', 'F', 'S', 'S'].map((day, i) => (
                    <View key={i} style={{ flex: 1, alignItems: 'center' }}>
                      <View style={{
                        width: '100%',
                        height: 8,
                        borderRadius: 4,
                        backgroundColor: i < (streak % 7 || (streak > 0 ? 7 : 0)) ? 'rgba(255,255,255,0.95)' : 'rgba(255,255,255,0.15)',
                        marginBottom: spacing.xs,
                        ...(i < (streak % 7 || (streak > 0 ? 7 : 0)) ? { shadowColor: '#fff', shadowOffset: { width: 0, height: 0 }, shadowOpacity: 0.5, shadowRadius: 4 } : {})
                      }} />
                      <Text style={{ color: i < (streak % 7 || (streak > 0 ? 7 : 0)) ? 'rgba(255,255,255,0.9)' : 'rgba(255,255,255,0.4)', fontSize: typography.sizes.xs, fontWeight: typography.weights.semibold }}>{day}</Text>
                    </View>
                  ))}
                </View>
              </LinearGradient>
            </View>
          </TouchableOpacity>
        </View>

        {/* Daily Check-In */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing.xl }}>
          <TouchableOpacity
            onPress={() => navigation.navigate('DailyLog' as never)}
            activeOpacity={0.9}
          >
            <View
              style={{
                backgroundColor: hasLoggedToday ? colors.success.muted : colors.background.card,
                borderRadius: radius.xl,
                padding: spacing.lg,
                flexDirection: 'row',
                alignItems: 'center',
                borderWidth: 1,
                borderColor: hasLoggedToday ? colors.success.DEFAULT + '40' : colors.background.glassBorder,
                ...shadows.card,
              }}
            >
              <View
                style={{
                  backgroundColor: hasLoggedToday ? colors.success.DEFAULT + '30' : colors.orange.DEFAULT + '20',
                  width: 52,
                  height: 52,
                  borderRadius: radius.lg,
                  justifyContent: 'center',
                  alignItems: 'center',
                  marginRight: spacing.lg,
                }}
              >
                {hasLoggedToday ? (
                  <Trophy size={26} color={colors.success.DEFAULT} />
                ) : (
                  <Target size={26} color={colors.orange.DEFAULT} />
                )}
              </View>
              <View style={{ flex: 1 }}>
                <Text style={{ color: colors.text.primary, fontSize: typography.sizes.base, fontWeight: typography.weights.bold }}>
                  {hasLoggedToday ? 'Check-In Complete' : 'Daily Check-In'}
                </Text>
                <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm, marginTop: 2 }}>
                  {hasLoggedToday ? 'Great job tracking your progress!' : 'Log how you\'re feeling today'}
                </Text>
              </View>
              <View style={{ backgroundColor: hasLoggedToday ? colors.success.DEFAULT + '20' : colors.orange.DEFAULT + '15', paddingHorizontal: spacing.md, paddingVertical: spacing.sm, borderRadius: radius.full }}>
                <ChevronRight size={18} color={hasLoggedToday ? colors.success.DEFAULT : colors.orange.DEFAULT} />
              </View>
            </View>
          </TouchableOpacity>
        </View>

        {/* TODAY'S WORKOUT */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing['2xl'] }}>
          <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: spacing.lg }}>
            <Text style={{ color: colors.text.primary, fontSize: typography.sizes.xl, fontWeight: typography.weights.bold }}>Ready to build</Text>
            <TouchableOpacity onPress={() => navigation.navigate('Workouts' as never)} style={{ flexDirection: 'row', alignItems: 'center' }}>
              <Text style={{ color: colors.orange.DEFAULT, fontSize: typography.sizes.sm, fontWeight: typography.weights.semibold }}>View all</Text>
              <ChevronRight size={16} color={colors.orange.DEFAULT} />
            </TouchableOpacity>
          </View>

          {recommendedWorkout ? (
            <TouchableOpacity
              activeOpacity={0.95}
              onPress={() => navigation.navigate('WorkoutDetail' as never, { workoutId: recommendedWorkout.workoutId } as never)}
            >
              <View style={{ backgroundColor: colors.background.card, borderRadius: radius['2xl'], overflow: 'hidden', borderWidth: 1, borderColor: colors.background.glassBorder, ...shadows.float }}>
                <LinearGradient colors={gradients.fire} start={{ x: 0, y: 0 }} end={{ x: 1, y: 0 }} style={{ height: 4 }} />

                <View style={{ padding: spacing.xl }}>
                  <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                    <LinearGradient colors={gradients.fire} style={{ width: 60, height: 60, borderRadius: radius.xl, justifyContent: 'center', alignItems: 'center', marginRight: spacing.lg, ...shadows.glow }}>
                      <Sparkles size={30} color="#fff" />
                    </LinearGradient>
                    <View style={{ flex: 1 }}>
                      <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 4 }}>
                        <View style={{ backgroundColor: colors.orange.DEFAULT + '20', paddingHorizontal: spacing.sm, paddingVertical: 2, borderRadius: radius.sm }}>
                          <Text style={{ color: colors.orange.DEFAULT, fontSize: typography.sizes.xs, fontWeight: typography.weights.bold, letterSpacing: 1 }}>RECOMMENDED</Text>
                        </View>
                      </View>
                      <Text style={{ color: colors.text.primary, fontSize: typography.sizes.xl, fontWeight: typography.weights.bold }}>{recommendedWorkout.name}</Text>
                      <View style={{ flexDirection: 'row', alignItems: 'center', marginTop: spacing.xs, gap: spacing.md }}>
                        <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                          <Clock size={14} color={colors.text.muted} />
                          <Text style={{ color: colors.text.muted, fontSize: typography.sizes.sm, marginLeft: 4 }}>{recommendedWorkout.estimatedDuration} min</Text>
                        </View>
                        <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                          <TrendingUp size={14} color={colors.text.muted} />
                          <Text style={{ color: colors.text.muted, fontSize: typography.sizes.sm, marginLeft: 4 }}>
                            {recommendedWorkout.difficultyLevel.charAt(0) + recommendedWorkout.difficultyLevel.slice(1).toLowerCase()}
                          </Text>
                        </View>
                      </View>
                    </View>
                  </View>

                  <TouchableOpacity
                    activeOpacity={0.9}
                    style={{ marginTop: spacing.xl }}
                    onPress={() => navigation.navigate('WorkoutDetail' as never, { workoutId: recommendedWorkout.workoutId } as never)}
                  >
                    <LinearGradient colors={gradients.fire} start={{ x: 0, y: 0 }} end={{ x: 1, y: 0 }} style={{ borderRadius: radius.lg, paddingVertical: spacing.lg, flexDirection: 'row', alignItems: 'center', justifyContent: 'center', ...shadows.glow }}>
                      <Play size={22} color="#fff" fill="#fff" />
                      <Text style={{ color: '#fff', fontSize: typography.sizes.base, fontWeight: typography.weights.bold, marginLeft: spacing.sm }}>Start Workout</Text>
                    </LinearGradient>
                  </TouchableOpacity>
                </View>
              </View>
            </TouchableOpacity>
          ) : (
            <TouchableOpacity activeOpacity={0.95} onPress={() => navigation.navigate('Workouts' as never)}>
              <View style={{ backgroundColor: colors.background.card, borderRadius: radius['2xl'], padding: spacing['2xl'], alignItems: 'center', borderWidth: 1, borderColor: colors.background.glassBorder }}>
                <Sparkles size={40} color={colors.text.muted} />
                <Text style={{ color: colors.text.primary, fontSize: typography.sizes.lg, fontWeight: typography.weights.semibold, marginTop: spacing.md }}>Find a Workout</Text>
                <Text style={{ color: colors.text.secondary, textAlign: 'center', marginTop: spacing.xs }}>Browse workouts to get started</Text>
              </View>
            </TouchableOpacity>
          )}
        </View>

        {/* Quick Stats */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing['2xl'] }}>
          <View style={{ flexDirection: 'row', gap: spacing.md }}>
            {[
              { icon: Zap, value: brickStats?.bricksThisWeek || 0, label: 'This Week', color: colors.orange.DEFAULT },
              { icon: Trophy, value: brickStats?.longestStreak || 0, label: 'Best Streak', color: colors.amber.DEFAULT },
              { icon: Target, value: profile?.weeklyGoalDays || 0, label: 'Weekly Goal', color: colors.blue.DEFAULT },
            ].map((stat, i) => (
              <View key={i} style={{ flex: 1, backgroundColor: colors.background.card, borderRadius: radius.xl, padding: spacing.lg, borderWidth: 1, borderColor: colors.background.glassBorder, ...shadows.card }}>
                <View style={{ backgroundColor: stat.color + '15', width: 38, height: 38, borderRadius: radius.md, justifyContent: 'center', alignItems: 'center', marginBottom: spacing.sm }}>
                  <stat.icon size={18} color={stat.color} />
                </View>
                <Text style={{ color: colors.text.primary, fontSize: typography.sizes['2xl'], fontWeight: typography.weights.black }}>{stat.value}</Text>
                <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs, marginTop: 2 }}>{stat.label}</Text>
              </View>
            ))}
          </View>
        </View>

        {/* BRIX AI Coach with Mood Indicator */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing['2xl'] }}>
          <TouchableOpacity activeOpacity={0.95} onPress={() => navigation.navigate('Brix' as never)}>
            <View style={{ backgroundColor: colors.background.card, borderRadius: radius['2xl'], overflow: 'hidden', borderWidth: 1, borderColor: colors.background.glassBorder, ...shadows.card }}>
              <LinearGradient colors={gradients.fire} start={{ x: 0, y: 0 }} end={{ x: 1, y: 0 }} style={{ height: 3 }} />
              <View style={{ padding: spacing.xl }}>
                {/* Mood Indicator Bar */}
                {(() => {
                  const coachingTone = (behaviorProfile?.coachingTone || 'ENCOURAGING') as keyof typeof BRIX_MOODS;
                  const mood = BRIX_MOODS[coachingTone] || BRIX_MOODS.ENCOURAGING;
                  const MoodIcon = mood.icon;

                  return (
                    <View style={{
                      flexDirection: 'row',
                      alignItems: 'center',
                      backgroundColor: mood.color + '15',
                      paddingHorizontal: spacing.md,
                      paddingVertical: spacing.sm,
                      borderRadius: radius.full,
                      alignSelf: 'flex-start',
                      marginBottom: spacing.md,
                    }}>
                      <MoodIcon size={14} color={mood.color} />
                      <Text style={{
                        color: mood.color,
                        fontSize: typography.sizes.xs,
                        fontWeight: typography.weights.bold,
                        marginLeft: spacing.xs,
                      }}>
                        {mood.label} Mode
                      </Text>
                    </View>
                  );
                })()}

                <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                  <View style={{ position: 'relative' }}>
                    {(() => {
                      const coachingTone = (behaviorProfile?.coachingTone || 'ENCOURAGING') as keyof typeof BRIX_MOODS;
                      const mood = BRIX_MOODS[coachingTone] || BRIX_MOODS.ENCOURAGING;
                      const MoodIcon = mood.icon;

                      return (
                        <>
                          <LinearGradient colors={gradients.fire} style={{ width: 56, height: 56, borderRadius: radius.xl, justifyContent: 'center', alignItems: 'center', ...shadows.glow }}>
                            <MessageCircle size={28} color="#fff" />
                          </LinearGradient>
                          {/* Mood face indicator */}
                          <View style={{
                            position: 'absolute',
                            bottom: -4,
                            right: -4,
                            width: 24,
                            height: 24,
                            backgroundColor: mood.color,
                            borderRadius: 12,
                            borderWidth: 2,
                            borderColor: colors.background.card,
                            justifyContent: 'center',
                            alignItems: 'center',
                          }}>
                            <MoodIcon size={12} color="#fff" />
                          </View>
                        </>
                      );
                    })()}
                  </View>
                  <View style={{ flex: 1, marginLeft: spacing.lg }}>
                    <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 4 }}>
                      <Text style={{ color: colors.orange.DEFAULT, fontSize: typography.sizes.xs, fontWeight: typography.weights.bold, letterSpacing: 1.5 }}>BRIX AI COACH</Text>
                      <View style={{ backgroundColor: colors.success.DEFAULT + '30', paddingHorizontal: spacing.sm, paddingVertical: 2, borderRadius: radius.sm, marginLeft: spacing.sm }}>
                        <Text style={{ color: colors.success.DEFAULT, fontSize: 9, fontWeight: typography.weights.bold }}>ONLINE</Text>
                      </View>
                    </View>
                    <Text style={{ color: colors.text.primary, fontSize: typography.sizes.base, lineHeight: 20 }}>
                      {(() => {
                        const coachingTone = (behaviorProfile?.coachingTone || 'ENCOURAGING') as keyof typeof BRIX_MOODS;
                        const mood = BRIX_MOODS[coachingTone] || BRIX_MOODS.ENCOURAGING;

                        if (coachingTone === 'CELEBRATORY') return "You're crushing it! Let's celebrate with another win!";
                        if (coachingTone === 'CHALLENGING') return "You're ready for more. Let's push those limits!";
                        if (coachingTone === 'EMPATHETIC') return "Take it easy today. Even small steps count.";
                        if (streak >= 7) return "Amazing streak! Your foundation is rock solid.";
                        if (streak >= 3) return "Great momentum! Ready to keep building?";
                        if (streak > 0) return "You're making progress! Let's keep it going.";
                        return "Ready to lay your first brick today?";
                      })()}
                    </Text>
                  </View>
                  <View style={{ backgroundColor: colors.orange.DEFAULT + '15', paddingHorizontal: spacing.md, paddingVertical: spacing.sm, borderRadius: radius.full }}>
                    <ChevronRight size={18} color={colors.orange.DEFAULT} />
                  </View>
                </View>
              </View>
            </View>
          </TouchableOpacity>
        </View>
      </ScrollView>
    </BrickBackground>
  );
}
