import React, { useState, useCallback } from 'react';
import { View, Text, ScrollView, TouchableOpacity, RefreshControl } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { Ionicons } from '@expo/vector-icons';
import { useNavigation, useFocusEffect } from '@react-navigation/native';
import { LinearGradient } from 'expo-linear-gradient';
import { colors } from '../constants/theme';
import { profileApi, brickApi, milestoneApi } from '../services/api';
import { UserProfileResponse, BrickStatsResponse } from '../types/api';

const orangeOutline = {
  borderWidth: 2,
  borderColor: colors.orange.DEFAULT,
};

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

  useFocusEffect(
    useCallback(() => {
      loadData();
    }, [])
  );

  const onRefresh = () => {
    setRefreshing(true);
    loadData();
  };

  const formatGoal = (goal: string) => {
    switch (goal) {
      case 'STRENGTH': return 'Build Strength';
      case 'CARDIO': return 'Improve Cardio';
      case 'FLEXIBILITY': return 'Stay Flexible';
      case 'WEIGHT_LOSS': return 'Lose Weight';
      default: return goal;
    }
  };

  const formatLevel = (level: string) => {
    return level.charAt(0) + level.slice(1).toLowerCase();
  };

  if (loading) {
    return (
      <SafeAreaView style={{ flex: 1, backgroundColor: colors.background.secondary }}>
        <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
          <Text style={{ color: colors.text.secondary }}>Loading profile...</Text>
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
        {/* Blue Gradient Header with Avatar */}
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
            alignItems: 'center',
          }}
        >
          {/* Settings Button */}
          <TouchableOpacity
            onPress={() => console.log('Settings')}
            style={{ position: 'absolute', top: 50, right: 20 }}
          >
            <Ionicons name="settings-outline" size={24} color="#fff" />
          </TouchableOpacity>

          {/* Avatar */}
          <View
            style={{
              backgroundColor: colors.orange.DEFAULT,
              width: 80,
              height: 80,
              borderRadius: 40,
              justifyContent: 'center',
              alignItems: 'center',
              marginBottom: 12,
              borderWidth: 3,
              borderColor: '#fff',
            }}
          >
            <Text style={{ color: '#fff', fontSize: 36, fontWeight: '800' }}>
              {profile?.displayName?.charAt(0) || 'B'}
            </Text>
          </View>

          <Text style={{ color: '#fff', fontSize: 24, fontWeight: '800' }}>
            {profile?.displayName || 'Builder'}
          </Text>
          <Text style={{ color: 'rgba(255,255,255,0.8)', fontSize: 14, marginTop: 2 }}>
            {formatLevel(profile?.fitnessLevel || 'BEGINNER')} • {formatGoal(profile?.primaryGoal || 'STRENGTH')}
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
              <Text style={{ color: colors.orange.DEFAULT, fontSize: 24, fontWeight: '800' }}>
                {brickStats?.totalBricks || 0}
              </Text>
              <Text style={{ color: colors.text.secondary, fontSize: 12 }}>Bricks</Text>
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
              <Text style={{ color: colors.text.primary, fontSize: 24, fontWeight: '800' }}>
                {brickStats?.longestStreak || 0}
              </Text>
              <Text style={{ color: colors.text.secondary, fontSize: 12 }}>Best Streak</Text>
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
              <Text style={{ color: colors.amber.DEFAULT, fontSize: 24, fontWeight: '800' }}>
                {milestoneCount}
              </Text>
              <Text style={{ color: colors.text.secondary, fontSize: 12 }}>Badges</Text>
            </View>
          </View>
        </View>

        {/* Profile Info Card */}
        <View style={{ paddingHorizontal: 20, marginTop: 24 }}>
          <Text style={{ color: colors.text.primary, fontSize: 20, fontWeight: '700', marginBottom: 12 }}>
            Profile Info
          </Text>
          <View
            style={{
              backgroundColor: colors.background.tertiary,
              borderRadius: 20,
              padding: 20,
              ...orangeOutline,
            }}
          >
            <View style={{ gap: 16 }}>
              <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                <View
                  style={{
                    backgroundColor: colors.blue.DEFAULT + '20',
                    width: 40,
                    height: 40,
                    borderRadius: 10,
                    justifyContent: 'center',
                    alignItems: 'center',
                    marginRight: 14,
                  }}
                >
                  <Ionicons name="fitness-outline" size={22} color={colors.blue.DEFAULT} />
                </View>
                <View style={{ flex: 1 }}>
                  <Text style={{ color: colors.text.secondary, fontSize: 13 }}>Fitness Level</Text>
                  <Text style={{ color: colors.text.primary, fontSize: 16, fontWeight: '600' }}>
                    {formatLevel(profile?.fitnessLevel || 'BEGINNER')}
                  </Text>
                </View>
              </View>

              <View style={{ height: 1, backgroundColor: colors.background.elevated }} />

              <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                <View
                  style={{
                    backgroundColor: colors.orange.DEFAULT + '20',
                    width: 40,
                    height: 40,
                    borderRadius: 10,
                    justifyContent: 'center',
                    alignItems: 'center',
                    marginRight: 14,
                  }}
                >
                  <Ionicons name="flag-outline" size={22} color={colors.orange.DEFAULT} />
                </View>
                <View style={{ flex: 1 }}>
                  <Text style={{ color: colors.text.secondary, fontSize: 13 }}>Primary Goal</Text>
                  <Text style={{ color: colors.text.primary, fontSize: 16, fontWeight: '600' }}>
                    {formatGoal(profile?.primaryGoal || 'STRENGTH')}
                  </Text>
                </View>
              </View>

              <View style={{ height: 1, backgroundColor: colors.background.elevated }} />

              <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                <View
                  style={{
                    backgroundColor: colors.green.DEFAULT + '20',
                    width: 40,
                    height: 40,
                    borderRadius: 10,
                    justifyContent: 'center',
                    alignItems: 'center',
                    marginRight: 14,
                  }}
                >
                  <Ionicons name="calendar-outline" size={22} color={colors.green.DEFAULT} />
                </View>
                <View style={{ flex: 1 }}>
                  <Text style={{ color: colors.text.secondary, fontSize: 13 }}>Weekly Goal</Text>
                  <Text style={{ color: colors.text.primary, fontSize: 16, fontWeight: '600' }}>
                    {profile?.weeklyGoalDays || 0} days per week
                  </Text>
                </View>
              </View>

              <View style={{ height: 1, backgroundColor: colors.background.elevated }} />

              <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                <View
                  style={{
                    backgroundColor: colors.amber.DEFAULT + '20',
                    width: 40,
                    height: 40,
                    borderRadius: 10,
                    justifyContent: 'center',
                    alignItems: 'center',
                    marginRight: 14,
                  }}
                >
                  <Ionicons name="barbell-outline" size={22} color={colors.amber.DEFAULT} />
                </View>
                <View style={{ flex: 1 }}>
                  <Text style={{ color: colors.text.secondary, fontSize: 13 }}>Equipment</Text>
                  <Text style={{ color: colors.text.primary, fontSize: 16, fontWeight: '600' }}>
                    {profile?.equipment || 'None specified'}
                  </Text>
                </View>
              </View>
            </View>
          </View>
        </View>

        {/* Achievement Preview */}
        <View style={{ paddingHorizontal: 20, marginTop: 24 }}>
          <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 12 }}>
            <Text style={{ color: colors.text.primary, fontSize: 20, fontWeight: '700' }}>
              Achievements
            </Text>
            <TouchableOpacity onPress={() => navigation.navigate('Progress' as never)}>
              <Text style={{ color: colors.blue.DEFAULT, fontSize: 14, fontWeight: '600' }}>See All</Text>
            </TouchableOpacity>
          </View>
          <View
            style={{
              backgroundColor: colors.background.tertiary,
              borderRadius: 20,
              padding: 20,
              ...orangeOutline,
            }}
          >
            <View style={{ flexDirection: 'row', justifyContent: 'space-around' }}>
              {['🧱', '🔥', '💪', '🏆'].map((emoji, index) => (
                <View
                  key={index}
                  style={{
                    alignItems: 'center',
                    opacity: index < milestoneCount ? 1 : 0.3,
                  }}
                >
                  <View
                    style={{
                      backgroundColor: index < milestoneCount ? colors.amber.DEFAULT + '30' : colors.background.elevated,
                      width: 56,
                      height: 56,
                      borderRadius: 28,
                      justifyContent: 'center',
                      alignItems: 'center',
                    }}
                  >
                    <Text style={{ fontSize: 28 }}>{emoji}</Text>
                  </View>
                </View>
              ))}
            </View>
            {milestoneCount === 0 && (
              <Text style={{ color: colors.text.secondary, textAlign: 'center', marginTop: 12, fontSize: 13 }}>
                Complete workouts to unlock achievements!
              </Text>
            )}
          </View>
        </View>

        {/* Action Buttons */}
        <View style={{ paddingHorizontal: 20, marginTop: 24, gap: 12 }}>
          <TouchableOpacity onPress={() => console.log('Settings')} activeOpacity={0.9}>
            <View
              style={{
                backgroundColor: colors.background.tertiary,
                borderRadius: 16,
                padding: 16,
                flexDirection: 'row',
                alignItems: 'center',
                ...orangeOutline,
              }}
            >
              <Ionicons name="settings-outline" size={24} color={colors.text.primary} />
              <Text style={{ color: colors.text.primary, fontSize: 16, fontWeight: '600', marginLeft: 14, flex: 1 }}>
                Settings
              </Text>
              <Ionicons name="chevron-forward" size={20} color={colors.text.muted} />
            </View>
          </TouchableOpacity>

          <TouchableOpacity onPress={() => console.log('Help')} activeOpacity={0.9}>
            <View
              style={{
                backgroundColor: colors.background.tertiary,
                borderRadius: 16,
                padding: 16,
                flexDirection: 'row',
                alignItems: 'center',
                ...orangeOutline,
              }}
            >
              <Ionicons name="help-circle-outline" size={24} color={colors.text.primary} />
              <Text style={{ color: colors.text.primary, fontSize: 16, fontWeight: '600', marginLeft: 14, flex: 1 }}>
                Help & Support
              </Text>
              <Ionicons name="chevron-forward" size={20} color={colors.text.muted} />
            </View>
          </TouchableOpacity>
        </View>

        {/* App Info */}
        <View style={{ paddingHorizontal: 20, marginTop: 32, alignItems: 'center' }}>
          <Text style={{ color: colors.orange.DEFAULT, fontSize: 24, fontWeight: '800' }}>🧱 B3</Text>
          <Text style={{ color: colors.text.muted, fontSize: 13, marginTop: 4 }}>
            Built by Ben Rickert 
          </Text>
          <Text style={{ color: colors.text.muted, fontSize: 12, marginTop: 2 }}>
            Version 1.0.0
          </Text>
        </View>
      </ScrollView>
    </View>
  );
}