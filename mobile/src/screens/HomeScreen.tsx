import React, { useEffect, useState } from 'react';
import { View, Text, ScrollView, TouchableOpacity, RefreshControl } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { Ionicons } from '@expo/vector-icons';
import { useNavigation } from '@react-navigation/native';
import { LinearGradient } from 'expo-linear-gradient';
import { colors } from '../constants/theme';
import { profileApi, brickApi } from '../services/api';
import { UserProfileResponse, BrickStatsResponse } from '../types/api';

// Reusable styles
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

export default function HomeScreen() {
  const navigation = useNavigation();
  const [profile, setProfile] = useState<UserProfileResponse | null>(null);
  const [brickStats, setBrickStats] = useState<BrickStatsResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);

  const loadData = async () => {
    try {
      const [profileData, statsData] = await Promise.all([
        profileApi.getProfile(),
        brickApi.getBrickStats(),
      ]);
      setProfile(profileData);
      setBrickStats(statsData);
    } catch (error) {
      console.error('Error loading home data:', error);
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const onRefresh = () => {
    setRefreshing(true);
    loadData();
  };

  const getGreeting = () => {
    const hour = new Date().getHours();
    if (hour < 12) return 'Good morning';
    if (hour < 18) return 'Good afternoon';
    return 'Good evening';
  };

  if (loading) {
    return (
      <SafeAreaView style={{ flex: 1, backgroundColor: colors.background.secondary }}>
        <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
          <Text style={{ color: colors.text.secondary }}>Loading...</Text>
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
        {/* Blue Gradient Header - Short */}
        <View style={{ paddingHorizontal: 0 }}>
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
            {/* Top Row */}
            <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' }}>
              <View>
                <Text style={{ color: 'rgba(255,255,255,0.8)', fontSize: 14 }}>{getGreeting()}</Text>
                <Text style={{ color: '#fff', fontSize: 24, fontWeight: '800' }}>
                  {profile?.displayName || 'Builder'} 👋
                </Text>
              </View>
              <TouchableOpacity
                style={{
                  backgroundColor: 'rgba(255,255,255,0.2)',
                  width: 44,
                  height: 44,
                  borderRadius: 22,
                  justifyContent: 'center',
                  alignItems: 'center',
                }}
              >
                <Ionicons name="notifications-outline" size={22} color="#fff" />
              </TouchableOpacity>
            </View>
          </LinearGradient>
        </View>

        {/* Stats Row - Pulled up to overlap header */}
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
              <Text style={{ fontSize: 20, marginBottom: 2 }}>🔥</Text>
              <Text style={{ color: colors.orange.DEFAULT, fontSize: 24, fontWeight: '800' }}>
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
              <Text style={{ fontSize: 20, marginBottom: 2 }}>🧱</Text>
              <Text style={{ color: colors.text.primary, fontSize: 24, fontWeight: '800' }}>
                {brickStats?.totalBricks || 0}
              </Text>
              <Text style={{ color: colors.text.secondary, fontSize: 11 }}>Bricks</Text>
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
                {brickStats?.bricksThisWeek || 0}
              </Text>
              <Text style={{ color: colors.text.secondary, fontSize: 11 }}>Week</Text>
            </View>
          </View>
        </View>

        {/* BRIX Coach Card - With Glow */}
        <View style={{ paddingHorizontal: 20, marginTop: 16 }}>
          <View
            style={{
              backgroundColor: colors.background.tertiary,
              borderRadius: 20,
              padding: 20,
              ...orangeOutline,
              ...orangeGlow,
            }}
          >
            <View style={{ flexDirection: 'row', alignItems: 'flex-start' }}>
              <View
                style={{
                  backgroundColor: colors.orange.DEFAULT,
                  width: 48,
                  height: 48,
                  borderRadius: 24,
                  justifyContent: 'center',
                  alignItems: 'center',
                  marginRight: 16,
                }}
              >
                <Text style={{ fontSize: 24 }}>🤖</Text>
              </View>
              <View style={{ flex: 1 }}>
                <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 6 }}>
                  <Text style={{ color: colors.orange.DEFAULT, fontSize: 16, fontWeight: '700' }}>BRIX</Text>
                  <View
                    style={{
                      backgroundColor: colors.green.DEFAULT,
                      width: 8,
                      height: 8,
                      borderRadius: 4,
                      marginLeft: 8,
                    }}
                  />
                  <Text style={{ color: colors.text.muted, fontSize: 12, marginLeft: 4 }}>Online</Text>
                </View>
                <Text style={{ color: colors.text.primary, fontSize: 15, lineHeight: 22 }}>
                  {brickStats?.currentStreak && brickStats.currentStreak > 0
                    ? `You're on fire! ${brickStats.currentStreak} days strong. Let's keep this momentum going! 💪`
                    : "Ready to start building? Every brick counts. Let's lay your foundation today! 🧱"}
                </Text>
              </View>
            </View>
          </View>
        </View>

        {/* Today's Workout */}
        <View style={{ paddingHorizontal: 20, marginTop: 24 }}>
          <Text style={{ color: colors.text.primary, fontSize: 20, fontWeight: '700', marginBottom: 12 }}>
            Today's Workout
          </Text>
          <TouchableOpacity
            onPress={() => navigation.navigate('Workouts' as never)}
            activeOpacity={0.9}
          >
            <View
              style={{
                backgroundColor: colors.background.tertiary,
                borderRadius: 20,
                padding: 20,
                ...orangeOutline,
              }}
            >
              <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 16 }}>
                <LinearGradient
                  colors={['#3b82f6', '#2563eb']}
                  style={{
                    width: 56,
                    height: 56,
                    borderRadius: 16,
                    justifyContent: 'center',
                    alignItems: 'center',
                    marginRight: 16,
                  }}
                >
                  <Ionicons name="barbell" size={28} color="#fff" />
                </LinearGradient>
                <View style={{ flex: 1 }}>
                  <Text style={{ color: colors.text.secondary, fontSize: 12, marginBottom: 4 }}>RECOMMENDED</Text>
                  <Text style={{ color: colors.text.primary, fontSize: 18, fontWeight: '700' }}>Full Body Strength</Text>
                  <View style={{ flexDirection: 'row', alignItems: 'center', marginTop: 4 }}>
                    <Ionicons name="time-outline" size={14} color={colors.text.muted} />
                    <Text style={{ color: colors.text.muted, fontSize: 12, marginLeft: 4 }}>30 min</Text>
                    <View style={{ width: 4, height: 4, borderRadius: 2, backgroundColor: colors.text.muted, marginHorizontal: 8 }} />
                    <View style={{ width: 8, height: 8, borderRadius: 4, backgroundColor: colors.amber.DEFAULT, marginRight: 4 }} />
                    <Text style={{ color: colors.text.muted, fontSize: 12 }}>Intermediate</Text>
                  </View>
                </View>
                <Ionicons name="chevron-forward" size={24} color={colors.text.muted} />
              </View>
              <TouchableOpacity
                onPress={() => navigation.navigate('Workouts' as never)}
                activeOpacity={0.8}
              >
                <LinearGradient
                  colors={['#3b82f6', '#2563eb']}
                  start={{ x: 0, y: 0 }}
                  end={{ x: 1, y: 0 }}
                  style={{
                    borderRadius: 12,
                    paddingVertical: 14,
                    alignItems: 'center',
                  }}
                >
                  <Text style={{ color: '#fff', fontSize: 16, fontWeight: '700' }}>Start Workout</Text>
                </LinearGradient>
              </TouchableOpacity>
            </View>
          </TouchableOpacity>
        </View>

        {/* Daily Check-In */}
        <View style={{ paddingHorizontal: 20, marginTop: 20 }}>
          <TouchableOpacity
            onPress={() => navigation.navigate('DailyLog' as never)}
            activeOpacity={0.9}
          >
            <View
              style={{
                backgroundColor: colors.background.tertiary,
                borderRadius: 20,
                padding: 20,
                flexDirection: 'row',
                alignItems: 'center',
                ...orangeOutline,
              }}
            >
              <View
                style={{
                  backgroundColor: colors.orange.DEFAULT + '20',
                  width: 48,
                  height: 48,
                  borderRadius: 12,
                  justifyContent: 'center',
                  alignItems: 'center',
                  marginRight: 16,
                }}
              >
                <Ionicons name="clipboard-outline" size={24} color={colors.orange.DEFAULT} />
              </View>
              <View style={{ flex: 1 }}>
                <Text style={{ color: colors.text.primary, fontSize: 16, fontWeight: '600' }}>Daily Check-In</Text>
                <Text style={{ color: colors.text.secondary, fontSize: 14 }}>How are you feeling today?</Text>
              </View>
              <View
                style={{
                  backgroundColor: colors.orange.DEFAULT,
                  paddingHorizontal: 12,
                  paddingVertical: 6,
                  borderRadius: 8,
                }}
              >
                <Text style={{ color: '#fff', fontSize: 12, fontWeight: '600' }}>Log</Text>
              </View>
            </View>
          </TouchableOpacity>
        </View>

        {/* Your Profile Card */}
        <View style={{ paddingHorizontal: 20, marginTop: 24 }}>
          <Text style={{ color: colors.text.primary, fontSize: 20, fontWeight: '700', marginBottom: 12 }}>
            Your Profile
          </Text>
          <View
            style={{
              backgroundColor: colors.background.tertiary,
              borderRadius: 20,
              padding: 20,
              ...orangeOutline,
            }}
          >
            <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 }}>
              <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                <View
                  style={{
                    backgroundColor: colors.blue.DEFAULT,
                    width: 48,
                    height: 48,
                    borderRadius: 24,
                    justifyContent: 'center',
                    alignItems: 'center',
                    marginRight: 12,
                  }}
                >
                  <Text style={{ color: '#fff', fontSize: 20, fontWeight: '700' }}>
                    {profile?.displayName?.charAt(0) || 'B'}
                  </Text>
                </View>
                <View>
                  <Text style={{ color: colors.text.primary, fontSize: 16, fontWeight: '600' }}>
                    {profile?.displayName}
                  </Text>
                  <Text style={{ color: colors.text.secondary, fontSize: 14 }}>
                    {profile?.fitnessLevel?.charAt(0)}{profile?.fitnessLevel?.slice(1).toLowerCase()} • {profile?.primaryGoal?.charAt(0)}{profile?.primaryGoal?.slice(1).toLowerCase()}
                  </Text>
                </View>
              </View>
              <Ionicons name="chevron-forward" size={20} color={colors.text.muted} />
            </View>

            <View style={{ height: 1, backgroundColor: colors.background.elevated, marginBottom: 16 }} />

            <View style={{ flexDirection: 'row', justifyContent: 'space-around' }}>
              <View style={{ alignItems: 'center' }}>
                <Text style={{ color: colors.orange.DEFAULT, fontSize: 20, fontWeight: '700' }}>
                  {profile?.weeklyGoalDays || 0}
                </Text>
                <Text style={{ color: colors.text.muted, fontSize: 12 }}>Weekly Goal</Text>
              </View>
              <View style={{ alignItems: 'center' }}>
                <Text style={{ color: colors.text.primary, fontSize: 20, fontWeight: '700' }}>
                  {profile?.totalWorkouts || 0}
                </Text>
                <Text style={{ color: colors.text.muted, fontSize: 12 }}>Workouts</Text>
              </View>
              <View style={{ alignItems: 'center' }}>
                <Text style={{ color: colors.amber.DEFAULT, fontSize: 20, fontWeight: '700' }}>
                  {brickStats?.longestStreak || 0}
                </Text>
                <Text style={{ color: colors.text.muted, fontSize: 12 }}>Best Streak</Text>
              </View>
            </View>
          </View>
        </View>
      </ScrollView>
    </View>
  );
}