import React, { useState, useCallback } from 'react';
import { View, Text, ScrollView, TouchableOpacity, RefreshControl } from 'react-native';
import { useNavigation, useFocusEffect } from '@react-navigation/native';
import { LinearGradient } from 'expo-linear-gradient';
import { Settings, HelpCircle, ChevronRight, Target, Flame, Trophy, Dumbbell, Calendar, Flag, Zap, Star, Award, TrendingUp, Sunrise, Medal } from 'lucide-react-native';
import { colors, gradients, shadows, radius, spacing, typography } from '../constants/theme';
import { profileApi, brickApi, milestoneApi } from '../services/api';
import { UserProfileResponse, BrickStatsResponse } from '../types/api';
import B3Logo from '../components/B3Logo';
import BrickBackground from '../components/BrickBackground';

// Hardcoded achievements to showcase
const SHOWCASE_ACHIEVEMENTS = [
  { id: 1, name: 'First Brick', description: 'Complete your first workout', icon: Target, color: colors.orange.DEFAULT, unlocked: true },
  { id: 2, name: 'On Fire', description: '3-day workout streak', icon: Flame, color: colors.amber.DEFAULT, unlocked: true },
  { id: 3, name: 'Week Warrior', description: '7-day workout streak', icon: Zap, color: colors.blue.DEFAULT, unlocked: true },
  { id: 4, name: 'Foundation Builder', description: 'Complete 10 total workouts', icon: Award, color: colors.green.DEFAULT, unlocked: true },
  { id: 5, name: 'Early Bird', description: 'Complete a workout before 7 AM', icon: Sunrise, color: colors.purple.DEFAULT, unlocked: false },
  { id: 6, name: 'Unstoppable', description: '30-day workout streak', icon: TrendingUp, color: colors.orange.light, unlocked: false },
];

export default function ProfileScreen() {
  const navigation = useNavigation();
  const [profile, setProfile] = useState<UserProfileResponse | null>(null);
  const [brickStats, setBrickStats] = useState<BrickStatsResponse | null>(null);
  const [milestoneCount, setMilestoneCount] = useState(0);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);

  const loadData = async () => {
    try {
      const [profileData, statsData, milestonesData] = await Promise.all([
        profileApi.getProfile(),
        brickApi.getBrickStats(),
        milestoneApi.getAchievedMilestones(),
      ]);
      setProfile(profileData);
      setBrickStats(statsData);
      setMilestoneCount(milestonesData.length);
    } catch (error) {
      console.error('Error loading profile data:', error);
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };

  useFocusEffect(useCallback(() => { loadData(); }, []));
  const onRefresh = () => { setRefreshing(true); loadData(); };

  const formatGoal = (goal: string) => {
    switch (goal) {
      case 'STRENGTH': return 'Build Strength';
      case 'CARDIO': return 'Improve Cardio';
      case 'FLEXIBILITY': return 'Stay Flexible';
      case 'WEIGHT_LOSS': return 'Lose Weight';
      default: return goal;
    }
  };

  const formatLevel = (level: string) => level.charAt(0) + level.slice(1).toLowerCase();

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
        {/* Header with Avatar */}
        <View style={{ paddingTop: 70, paddingHorizontal: spacing.xl }}>
          {/* Top row with settings and logo */}
          <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: spacing.xl }}>
            <TouchableOpacity onPress={() => navigation.navigate('Settings' as never)}>
              <Settings size={24} color={colors.text.secondary} />
            </TouchableOpacity>
            <B3Logo size={48} />
          </View>

          {/* Avatar and name centered */}
          <View style={{ alignItems: 'center' }}>
            <LinearGradient colors={gradients.fire} style={{ width: 100, height: 100, borderRadius: 50, justifyContent: 'center', alignItems: 'center', marginBottom: spacing.lg, ...shadows.glow }}>
              <Text style={{ color: '#fff', fontSize: 44, fontWeight: typography.weights.black }}>{profile?.displayName?.charAt(0) || 'B'}</Text>
            </LinearGradient>

            <Text style={{ color: colors.text.primary, fontSize: typography.sizes['3xl'], fontWeight: typography.weights.black }}>{profile?.displayName || 'Builder'}</Text>
            <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.base, marginTop: spacing.xs }}>
              {formatLevel(profile?.fitnessLevel || 'BEGINNER')} - {formatGoal(profile?.primaryGoal || 'STRENGTH')}
            </Text>
          </View>
        </View>

        {/* Stats Row */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing['2xl'] }}>
          <View style={{ flexDirection: 'row', gap: spacing.md }}>
            {[
              { icon: Target, value: brickStats?.totalBricks || 0, label: 'Bricks' },
              { icon: Flame, value: brickStats?.longestStreak || 0, label: 'Best Streak' },
              { icon: Trophy, value: SHOWCASE_ACHIEVEMENTS.filter(a => a.unlocked).length, label: 'Badges' },
            ].map((stat, i) => (
              <View key={i} style={{ flex: 1, backgroundColor: colors.background.glass, borderRadius: radius.xl, padding: spacing.lg, alignItems: 'center', borderWidth: 1, borderColor: colors.background.glassBorder }}>
                <stat.icon size={20} color={colors.orange.DEFAULT} />
                <Text style={{ color: colors.text.primary, fontSize: typography.sizes['2xl'], fontWeight: typography.weights.black, marginTop: spacing.sm }}>{stat.value}</Text>
                <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs }}>{stat.label}</Text>
              </View>
            ))}
          </View>
        </View>

        {/* Profile Info Card */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing['2xl'] }}>
          <Text style={{ color: colors.text.primary, fontSize: typography.sizes.xl, fontWeight: typography.weights.bold, marginBottom: spacing.md }}>Profile Info</Text>
          <View style={{ backgroundColor: colors.background.card, borderRadius: radius['2xl'], padding: spacing.xl, borderWidth: 1, borderColor: colors.background.glassBorder }}>
            {[
              { icon: Dumbbell, label: 'Fitness Level', value: formatLevel(profile?.fitnessLevel || 'BEGINNER'), color: colors.blue.DEFAULT },
              { icon: Flag, label: 'Primary Goal', value: formatGoal(profile?.primaryGoal || 'STRENGTH'), color: colors.orange.DEFAULT },
              { icon: Calendar, label: 'Weekly Goal', value: `${profile?.weeklyGoalDays || 0} days per week`, color: colors.green.DEFAULT },
              { icon: Target, label: 'Equipment', value: profile?.equipment || 'None specified', color: colors.amber.DEFAULT },
            ].map((item, index) => (
              <View key={index}>
                <View style={{ flexDirection: 'row', alignItems: 'center', paddingVertical: spacing.md }}>
                  <View style={{ backgroundColor: item.color + '20', width: 40, height: 40, borderRadius: radius.md, justifyContent: 'center', alignItems: 'center', marginRight: spacing.lg }}>
                    <item.icon size={20} color={item.color} />
                  </View>
                  <View style={{ flex: 1 }}>
                    <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm }}>{item.label}</Text>
                    <Text style={{ color: colors.text.primary, fontSize: typography.sizes.base, fontWeight: typography.weights.semibold }}>{item.value}</Text>
                  </View>
                </View>
                {index < 3 && <View style={{ height: 1, backgroundColor: colors.background.glassBorder }} />}
              </View>
            ))}
          </View>
        </View>

        {/* Achievements Showcase */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing['2xl'] }}>
          <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: spacing.md }}>
            <Text style={{ color: colors.text.primary, fontSize: typography.sizes.xl, fontWeight: typography.weights.bold }}>Achievements</Text>
            <TouchableOpacity onPress={() => navigation.navigate('Progress' as never)}>
              <Text style={{ color: colors.orange.DEFAULT, fontSize: typography.sizes.sm, fontWeight: typography.weights.semibold }}>See All</Text>
            </TouchableOpacity>
          </View>
          <View style={{ backgroundColor: colors.background.card, borderRadius: radius['2xl'], padding: spacing.lg, borderWidth: 1, borderColor: colors.background.glassBorder }}>
            {/* Achievement Icons Row */}
            <View style={{ flexDirection: 'row', justifyContent: 'space-around', marginBottom: spacing.lg }}>
              {SHOWCASE_ACHIEVEMENTS.slice(0, 4).map((achievement) => (
                <View key={achievement.id} style={{ alignItems: 'center', opacity: achievement.unlocked ? 1 : 0.4 }}>
                  <View style={{
                    backgroundColor: achievement.unlocked ? achievement.color + '20' : colors.background.glass,
                    width: 56,
                    height: 56,
                    borderRadius: 28,
                    justifyContent: 'center',
                    alignItems: 'center',
                    borderWidth: achievement.unlocked ? 2 : 0,
                    borderColor: achievement.unlocked ? achievement.color + '40' : 'transparent'
                  }}>
                    <achievement.icon size={26} color={achievement.unlocked ? achievement.color : colors.text.muted} />
                  </View>
                </View>
              ))}
            </View>

            {/* Achievement Details */}
            <View style={{ gap: spacing.sm }}>
              {SHOWCASE_ACHIEVEMENTS.filter(a => a.unlocked).slice(0, 3).map((achievement, index) => (
                <View key={achievement.id} style={{
                  flexDirection: 'row',
                  alignItems: 'center',
                  backgroundColor: colors.background.elevated,
                  borderRadius: radius.lg,
                  padding: spacing.md
                }}>
                  <View style={{
                    backgroundColor: achievement.color + '20',
                    width: 40,
                    height: 40,
                    borderRadius: radius.md,
                    justifyContent: 'center',
                    alignItems: 'center',
                    marginRight: spacing.md
                  }}>
                    <achievement.icon size={20} color={achievement.color} />
                  </View>
                  <View style={{ flex: 1 }}>
                    <Text style={{ color: colors.text.primary, fontSize: typography.sizes.sm, fontWeight: typography.weights.semibold }}>
                      {achievement.name}
                    </Text>
                    <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs }}>
                      {achievement.description}
                    </Text>
                  </View>
                  <Trophy size={16} color={colors.amber.DEFAULT} />
                </View>
              ))}
            </View>

            {/* Locked Achievement Teaser */}
            <View style={{ marginTop: spacing.md, paddingTop: spacing.md, borderTopWidth: 1, borderTopColor: colors.background.glassBorder }}>
              <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.xs, textAlign: 'center' }}>
                {SHOWCASE_ACHIEVEMENTS.filter(a => !a.unlocked).length} more achievements to unlock!
              </Text>
            </View>
          </View>
        </View>

        {/* Action Buttons */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing['2xl'], gap: spacing.md }}>
          {[
            { icon: Medal, label: 'Personal Records', route: 'PersonalRecords' },
            { icon: Settings, label: 'Settings', route: 'Settings' },
            { icon: HelpCircle, label: 'Help & Support', route: 'HelpSupport' },
          ].map((item, index) => (
            <TouchableOpacity key={index} activeOpacity={0.95} onPress={() => navigation.navigate(item.route as never)}>
              <View style={{ backgroundColor: colors.background.card, borderRadius: radius.xl, padding: spacing.lg, flexDirection: 'row', alignItems: 'center', borderWidth: 1, borderColor: colors.background.glassBorder }}>
                <item.icon size={24} color={colors.text.primary} />
                <Text style={{ color: colors.text.primary, fontSize: typography.sizes.base, fontWeight: typography.weights.semibold, marginLeft: spacing.lg, flex: 1 }}>{item.label}</Text>
                <ChevronRight size={20} color={colors.text.muted} />
              </View>
            </TouchableOpacity>
          ))}
        </View>

        {/* App Info */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing['3xl'], alignItems: 'center' }}>
          <B3Logo size={48} />
          <Text style={{ color: colors.text.muted, fontSize: typography.sizes.sm, marginTop: spacing.md }}>Built by Ben Rickert</Text>
          <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs, marginTop: spacing.xs }}>Version 1.0.0</Text>
        </View>
      </ScrollView>
    </BrickBackground>
  );
}
