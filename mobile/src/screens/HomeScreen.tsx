import React, { useEffect, useState } from 'react';
import { View, Text, ScrollView, TouchableOpacity, RefreshControl, Dimensions } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { LinearGradient } from 'expo-linear-gradient';
import { Flame, Zap, Trophy, ChevronRight, Play, Target, MessageCircle, Sparkles } from 'lucide-react-native';
import { colors, gradients, shadows, radius, spacing, typography } from '../constants/theme';
import { profileApi, brickApi } from '../services/api';
import { UserProfileResponse, BrickStatsResponse } from '../types/api';
import B3Logo from '../components/B3Logo';

const { width: SCREEN_WIDTH, height: SCREEN_HEIGHT } = Dimensions.get('window');

export default function HomeScreen() {
  const navigation = useNavigation();
  const [profile, setProfile] = useState<UserProfileResponse | null>(null);
  const [brickStats, setBrickStats] = useState<BrickStatsResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);

  const loadData = async () => {
    try {
      const [profileData, statsData] = await Promise.all([profileApi.getProfile(), brickApi.getBrickStats()]);
      setProfile(profileData);
      setBrickStats(statsData);
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
    if (streak >= 3) return 'HEATING UP';
    return 'CURRENT STREAK';
  };

  if (loading) {
    return (
      <View style={{ flex: 1, backgroundColor: colors.background.end, justifyContent: 'center', alignItems: 'center' }}>
        <B3Logo size={80} />
      </View>
    );
  }

  return (
    <View style={{ flex: 1, backgroundColor: colors.background.end }}>
      {/* BACKGROUND */}
      <View style={{ position: 'absolute', top: 0, left: 0, right: 0, bottom: 0 }}>
        <LinearGradient colors={[colors.background.start, colors.background.mid, colors.background.end]} style={{ position: 'absolute', top: 0, left: 0, right: 0, bottom: 0 }} />
        <LinearGradient colors={['rgba(249, 115, 22, 0.25)', 'transparent']} style={{ position: 'absolute', top: -100, right: -100, width: 400, height: 400, borderRadius: 200 }} />
        <LinearGradient colors={['rgba(59, 130, 246, 0.15)', 'transparent']} style={{ position: 'absolute', bottom: 100, left: -150, width: 350, height: 350, borderRadius: 175 }} />
      </View>

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
              <LinearGradient colors={getStreakGradient()} start={{ x: 0, y: 0 }} end={{ x: 1, y: 1 }} style={{ padding: spacing['2xl'] }}>
                <View style={{ position: 'absolute', top: 0, right: 0, bottom: 0, left: '50%', backgroundColor: 'rgba(255,255,255,0.1)', borderTopLeftRadius: 100, borderBottomLeftRadius: 200 }} />
                
                <View style={{ flexDirection: 'row', alignItems: 'flex-end', justifyContent: 'space-between' }}>
                  <View>
                    <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                      <Flame size={20} color="rgba(255,255,255,0.9)" />
                      <Text style={{ color: 'rgba(255,255,255,0.9)', fontSize: typography.sizes.xs, fontWeight: typography.weights.bold, letterSpacing: 3, marginLeft: spacing.sm, textTransform: 'uppercase' }}>{getStreakLabel()}</Text>
                    </View>
                    <Text style={{ color: '#fff', fontSize: typography.sizes['5xl'], fontWeight: typography.weights.black, letterSpacing: -4, lineHeight: typography.sizes['5xl'], marginTop: spacing.sm }}>{streak}</Text>
                    <Text style={{ color: 'rgba(255,255,255,0.85)', fontSize: typography.sizes.xl, fontWeight: typography.weights.semibold, marginTop: -spacing.xs }}>days</Text>
                  </View>
                  
                  <View style={{ alignItems: 'flex-end' }}>
                    <View style={{ backgroundColor: 'rgba(0,0,0,0.25)', paddingHorizontal: spacing.lg, paddingVertical: spacing.sm, borderRadius: radius.full }}>
                      <Text style={{ color: '#fff', fontSize: typography.sizes.sm, fontWeight: typography.weights.bold }}>{brickStats?.totalBricks || 0} bricks</Text>
                    </View>
                    <ChevronRight size={28} color="rgba(255,255,255,0.5)" style={{ marginTop: spacing.lg }} />
                  </View>
                </View>

                <View style={{ flexDirection: 'row', marginTop: spacing.xl, gap: 8 }}>
                  {['M', 'T', 'W', 'T', 'F', 'S', 'S'].map((day, i) => (
                    <View key={i} style={{ flex: 1, alignItems: 'center' }}>
                      <View style={{ width: '100%', height: 6, borderRadius: 3, backgroundColor: i < streak ? 'rgba(255,255,255,0.95)' : 'rgba(0,0,0,0.25)', marginBottom: spacing.xs }} />
                      <Text style={{ color: 'rgba(255,255,255,0.6)', fontSize: typography.sizes.xs, fontWeight: typography.weights.medium }}>{day}</Text>
                    </View>
                  ))}
                </View>
              </LinearGradient>
            </View>
          </TouchableOpacity>
        </View>

        {/* STATS */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing['2xl'] }}>
          <View style={{ flexDirection: 'row', gap: spacing.md }}>
            {[
              { icon: Zap, value: brickStats?.bricksThisWeek || 0, label: 'This Week' },
              { icon: Trophy, value: brickStats?.longestStreak || 0, label: 'Best Streak' },
              { icon: Target, value: profile?.weeklyGoalDays || 0, label: 'Goal' },
            ].map((stat, i) => (
              <View key={i} style={{ flex: 1, backgroundColor: colors.background.glass, borderRadius: radius.xl, padding: spacing.lg, borderWidth: 1, borderColor: colors.background.glassBorder, ...shadows.card }}>
                <View style={{ backgroundColor: colors.orange.DEFAULT + '20', width: 36, height: 36, borderRadius: radius.md, justifyContent: 'center', alignItems: 'center', marginBottom: spacing.md }}>
                  <stat.icon size={18} color={colors.orange.DEFAULT} />
                </View>
                <Text style={{ color: colors.text.primary, fontSize: typography.sizes['2xl'], fontWeight: typography.weights.black }}>{stat.value}</Text>
                <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs, marginTop: 2 }}>{stat.label}</Text>
              </View>
            ))}
          </View>
        </View>

        {/* TODAY'S WORKOUT */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing['3xl'] }}>
          <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: spacing.lg }}>
            <Text style={{ color: colors.text.primary, fontSize: typography.sizes.xl, fontWeight: typography.weights.bold }}>Ready to build</Text>
            <TouchableOpacity onPress={() => navigation.navigate('Workouts' as never)}>
              <Text style={{ color: colors.orange.DEFAULT, fontSize: typography.sizes.sm, fontWeight: typography.weights.semibold }}>View all</Text>
            </TouchableOpacity>
          </View>
          
          <TouchableOpacity activeOpacity={0.95} onPress={() => navigation.navigate('Workouts' as never)}>
            <View style={{ backgroundColor: colors.background.card, borderRadius: radius['2xl'], overflow: 'hidden', borderWidth: 1, borderColor: colors.background.glassBorder, ...shadows.float }}>
              <LinearGradient colors={gradients.fire} start={{ x: 0, y: 0 }} end={{ x: 1, y: 0 }} style={{ height: 3 }} />
              
              <View style={{ padding: spacing.xl }}>
                <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                  <LinearGradient colors={gradients.fire} style={{ width: 56, height: 56, borderRadius: radius.lg, justifyContent: 'center', alignItems: 'center', marginRight: spacing.lg }}>
                    <Sparkles size={28} color="#fff" />
                  </LinearGradient>
                  <View style={{ flex: 1 }}>
                    <Text style={{ color: colors.orange.DEFAULT, fontSize: typography.sizes.xs, fontWeight: typography.weights.bold, letterSpacing: 2 }}>RECOMMENDED</Text>
                    <Text style={{ color: colors.text.primary, fontSize: typography.sizes.xl, fontWeight: typography.weights.bold, marginTop: 4 }}>Upper Body Blast</Text>
                    <Text style={{ color: colors.text.muted, fontSize: typography.sizes.sm, marginTop: 4 }}>45 min - Intermediate - Dumbbells</Text>
                  </View>
                </View>
                
                <TouchableOpacity activeOpacity={0.9} style={{ marginTop: spacing.xl }}>
                  <LinearGradient colors={gradients.fire} start={{ x: 0, y: 0 }} end={{ x: 1, y: 0 }} style={{ borderRadius: radius.lg, paddingVertical: spacing.lg, flexDirection: 'row', alignItems: 'center', justifyContent: 'center', ...shadows.glow }}>
                    <Play size={20} color="#fff" fill="#fff" />
                    <Text style={{ color: '#fff', fontSize: typography.sizes.base, fontWeight: typography.weights.bold, marginLeft: spacing.sm }}>Start Workout</Text>
                  </LinearGradient>
                </TouchableOpacity>
              </View>
            </View>
          </TouchableOpacity>
        </View>

        {/* BRIX AI */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing['2xl'] }}>
          <TouchableOpacity activeOpacity={0.95} onPress={() => navigation.navigate('Brix' as never)}>
            <LinearGradient colors={['rgba(249, 115, 22, 0.2)', 'rgba(249, 115, 22, 0.1)']} style={{ borderRadius: radius['2xl'], padding: spacing.xl, borderWidth: 1, borderColor: 'rgba(249, 115, 22, 0.3)' }}>
              <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                <LinearGradient colors={gradients.fire} style={{ width: 52, height: 52, borderRadius: radius.lg, justifyContent: 'center', alignItems: 'center', marginRight: spacing.lg, ...shadows.glow }}>
                  <MessageCircle size={26} color="#fff" />
                </LinearGradient>
                <View style={{ flex: 1 }}>
                  <Text style={{ color: colors.orange.neon, fontSize: typography.sizes.xs, fontWeight: typography.weights.bold, letterSpacing: 2 }}>BRIX AI COACH</Text>
                  <Text style={{ color: colors.text.primary, fontSize: typography.sizes.base, marginTop: 4 }}>
                    {streak > 0 ? "You're making progress! Let's keep the momentum." : "Ready to lay your foundation?"}
                  </Text>
                </View>
                <ChevronRight size={22} color={colors.orange.DEFAULT} />
              </View>
            </LinearGradient>
          </TouchableOpacity>
        </View>
      </ScrollView>
    </View>
  );
}
