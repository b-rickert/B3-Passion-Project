import React, { useEffect, useState } from 'react';
import { View, Text, ScrollView, TouchableOpacity, RefreshControl } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { Ionicons } from '@expo/vector-icons';
import { useNavigation } from '@react-navigation/native';
import { Card, Button } from '../components';
import { colors } from '../constants/theme';
import { profileApi, brickApi } from '../services/api';
import { UserProfileResponse, BrickStatsResponse } from '../types/api';

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
        contentContainerStyle={{ padding: 16 }}
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} tintColor={colors.orange.DEFAULT} />
        }
      >
        {/* Header */}
        <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 24 }}>
          <View>
            <Text style={{ color: colors.text.secondary, fontSize: 14 }}>{getGreeting()},</Text>
            <Text style={{ color: colors.text.primary, fontSize: 24, fontWeight: '700' }}>
              {profile?.displayName || 'Builder'}
            </Text>
          </View>
          <TouchableOpacity
            style={{
              backgroundColor: colors.background.secondary,
              width: 44,
              height: 44,
              borderRadius: 22,
              justifyContent: 'center',
              alignItems: 'center',
            }}
          >
            <Ionicons name="notifications-outline" size={24} color={colors.text.secondary} />
          </TouchableOpacity>
        </View>

        {/* BRIX Card */}
        <Card borderColor={colors.orange.DEFAULT} marginBottom={16}>
          <View style={{ flexDirection: 'row', alignItems: 'flex-start' }}>
            <View
              style={{
                backgroundColor: colors.orange.DEFAULT,
                width: 40,
                height: 40,
                borderRadius: 20,
                justifyContent: 'center',
                alignItems: 'center',
                marginRight: 12,
              }}
            >
              <Text style={{ fontSize: 20 }}>🤖</Text>
            </View>
            <View style={{ flex: 1 }}>
              <Text style={{ color: colors.orange.DEFAULT, fontSize: 14, fontWeight: '600', marginBottom: 4 }}>
                BRIX
              </Text>
              <Text style={{ color: colors.text.primary, fontSize: 15, lineHeight: 22 }}>
                {brickStats?.currentStreak && brickStats.currentStreak > 0
                  ? `Amazing! You're on a ${brickStats.currentStreak} day streak! Keep building! 🔥`
                  : "Ready to lay your first brick today? Let's get started! 💪"}
              </Text>
            </View>
          </View>
        </Card>

        {/* Stats Row */}
        <View style={{ flexDirection: 'row', gap: 12, marginBottom: 24 }}>
          <Card padding={16}>
            <View style={{ alignItems: 'center', flex: 1 }}>
              <Text style={{ fontSize: 28, marginBottom: 4 }}>🔥</Text>
              <Text style={{ color: colors.orange.DEFAULT, fontSize: 28, fontWeight: '800' }}>
                {brickStats?.currentStreak || 0}
              </Text>
              <Text style={{ color: colors.text.secondary, fontSize: 12 }}>Day Streak</Text>
            </View>
          </Card>
          <Card padding={16}>
            <View style={{ alignItems: 'center', flex: 1 }}>
              <Text style={{ fontSize: 28, marginBottom: 4 }}>🧱</Text>
              <Text style={{ color: colors.text.primary, fontSize: 28, fontWeight: '800' }}>
                {brickStats?.totalBricks || 0}
              </Text>
              <Text style={{ color: colors.text.secondary, fontSize: 12 }}>Total Bricks</Text>
            </View>
          </Card>
          <Card padding={16}>
            <View style={{ alignItems: 'center', flex: 1 }}>
              <Text style={{ fontSize: 28, marginBottom: 4 }}>📅</Text>
              <Text style={{ color: colors.text.primary, fontSize: 28, fontWeight: '800' }}>
                {brickStats?.bricksThisWeek || 0}
              </Text>
              <Text style={{ color: colors.text.secondary, fontSize: 12 }}>This Week</Text>
            </View>
          </Card>
        </View>

        {/* Action Buttons */}
        <Text style={{ color: colors.text.primary, fontSize: 18, fontWeight: '600', marginBottom: 12 }}>
          Quick Actions
        </Text>

        <Button
          title="🏋️  Start Workout"
          onPress={() => navigation.navigate('Workouts' as never)}
          variant="primary"
        />

        <View style={{ marginTop: 12 }}>
          <Button
            title="📝  Daily Check-In"
            onPress={() => console.log('Daily Log - coming soon')}
            variant="secondary"
          />
        </View>

        <View style={{ marginTop: 12 }}>
          <Button
            title="📊  View Progress"
            onPress={() => navigation.navigate('Progress' as never)}
            variant="outline"
          />
        </View>

        {/* This Week Summary */}
        <Text style={{ color: colors.text.primary, fontSize: 18, fontWeight: '600', marginTop: 24, marginBottom: 12 }}>
          Your Stats
        </Text>
        <Card>
          <View style={{ flexDirection: 'row', justifyContent: 'space-between', marginBottom: 12 }}>
            <Text style={{ color: colors.text.secondary }}>Fitness Level</Text>
            <Text style={{ color: colors.text.primary, fontWeight: '600' }}>{profile?.fitnessLevel}</Text>
          </View>
          <View style={{ flexDirection: 'row', justifyContent: 'space-between', marginBottom: 12 }}>
            <Text style={{ color: colors.text.secondary }}>Primary Goal</Text>
            <Text style={{ color: colors.text.primary, fontWeight: '600' }}>{profile?.primaryGoal}</Text>
          </View>
          <View style={{ flexDirection: 'row', justifyContent: 'space-between', marginBottom: 12 }}>
            <Text style={{ color: colors.text.secondary }}>Weekly Goal</Text>
            <Text style={{ color: colors.text.primary, fontWeight: '600' }}>{profile?.weeklyGoalDays} days</Text>
          </View>
          <View style={{ flexDirection: 'row', justifyContent: 'space-between' }}>
            <Text style={{ color: colors.text.secondary }}>Longest Streak</Text>
            <Text style={{ color: colors.orange.DEFAULT, fontWeight: '600' }}>{brickStats?.longestStreak || 0} days</Text>
          </View>
        </Card>
      </ScrollView>
    </SafeAreaView>
  );
}