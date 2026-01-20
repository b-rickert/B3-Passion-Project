import React, { useState, useCallback } from 'react';
import { View, Text, ScrollView, TouchableOpacity, RefreshControl } from 'react-native';
import { useNavigation, useFocusEffect } from '@react-navigation/native';
import { LinearGradient } from 'expo-linear-gradient';
import { Settings, HelpCircle, ChevronRight, Target, Flame, Trophy, Dumbbell, Calendar, Flag, Zap, Star } from 'lucide-react-native';
import { colors, gradients, shadows, radius, spacing, typography } from '../constants/theme';
import { profileApi, brickApi, milestoneApi } from '../services/api';
import { UserProfileResponse, BrickStatsResponse } from '../types/api';
import B3Logo from '../components/B3Logo';

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
        <LinearGradient colors={['rgba(249, 115, 22, 0.25)', 'transparent']} style={{ position: 'absolute', top: -100, right: -100, width: 350, height: 350, borderRadius: 175 }} />
      </View>

      <ScrollView style={{ flex: 1 }} contentContainerStyle={{ paddingBottom: 120 }} showsVerticalScrollIndicator={false} refreshControl={<RefreshControl refreshing={refreshing} onRefresh={onRefresh} tintColor={colors.orange.DEFAULT} />}>
        {/* Header with Avatar */}
        <View style={{ paddingTop: 70, paddingHorizontal: spacing.xl }}>
          {/* Top row with settings and logo */}
          <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: spacing.xl }}>
            <TouchableOpacity onPress={() => console.log('Settings')}>
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
              { icon: Trophy, value: milestoneCount, label: 'Badges' },
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

        {/* Achievements Preview */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing['2xl'] }}>
          <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: spacing.md }}>
            <Text style={{ color: colors.text.primary, fontSize: typography.sizes.xl, fontWeight: typography.weights.bold }}>Achievements</Text>
            <TouchableOpacity onPress={() => navigation.navigate('Progress' as never)}>
              <Text style={{ color: colors.orange.DEFAULT, fontSize: typography.sizes.sm, fontWeight: typography.weights.semibold }}>See All</Text>
            </TouchableOpacity>
          </View>
          <View style={{ backgroundColor: colors.background.card, borderRadius: radius['2xl'], padding: spacing.xl, borderWidth: 1, borderColor: colors.background.glassBorder }}>
            <View style={{ flexDirection: 'row', justifyContent: 'space-around' }}>
              {[Target, Flame, Zap, Star].map((IconComponent, index) => (
                <View key={index} style={{ alignItems: 'center', opacity: index < milestoneCount ? 1 : 0.3 }}>
                  <View style={{ backgroundColor: index < milestoneCount ? colors.orange.DEFAULT + '20' : colors.background.glass, width: 56, height: 56, borderRadius: 28, justifyContent: 'center', alignItems: 'center' }}>
                    <IconComponent size={28} color={index < milestoneCount ? colors.orange.DEFAULT : colors.text.muted} />
                  </View>
                </View>
              ))}
            </View>
            {milestoneCount === 0 && <Text style={{ color: colors.text.secondary, textAlign: 'center', marginTop: spacing.md, fontSize: typography.sizes.sm }}>Complete workouts to unlock achievements!</Text>}
          </View>
        </View>

        {/* Action Buttons */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing['2xl'], gap: spacing.md }}>
          {[
            { icon: Settings, label: 'Settings' },
            { icon: HelpCircle, label: 'Help & Support' },
          ].map((item, index) => (
            <TouchableOpacity key={index} activeOpacity={0.95} onPress={() => console.log(item.label)}>
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
    </View>
  );
}
