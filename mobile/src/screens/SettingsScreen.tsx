import React, { useState, useCallback } from 'react';
import { View, Text, ScrollView, TouchableOpacity, Switch, Alert } from 'react-native';
import { useNavigation, useFocusEffect } from '@react-navigation/native';
import { LinearGradient } from 'expo-linear-gradient';
import {
  ArrowLeft,
  Bell,
  Moon,
  Volume2,
  Vibrate,
  Clock,
  Target,
  Dumbbell,
  ChevronRight,
  RotateCcw,
  Trash2
} from 'lucide-react-native';
import { colors, gradients, shadows, radius, spacing, typography } from '../constants/theme';
import { profileApi } from '../services/api';
import { UserProfileResponse } from '../types/api';
import B3Logo from '../components/B3Logo';

export default function SettingsScreen() {
  const navigation = useNavigation();
  const [profile, setProfile] = useState<UserProfileResponse | null>(null);
  const [loading, setLoading] = useState(true);

  // Settings state
  const [notifications, setNotifications] = useState(true);
  const [darkMode, setDarkMode] = useState(true);
  const [soundEffects, setSoundEffects] = useState(true);
  const [hapticFeedback, setHapticFeedback] = useState(true);

  const loadProfile = async () => {
    try {
      const profileData = await profileApi.getProfile();
      setProfile(profileData);
    } catch (error) {
      console.error('Error loading profile:', error);
    } finally {
      setLoading(false);
    }
  };

  useFocusEffect(useCallback(() => { loadProfile(); }, []));

  const formatLevel = (level: string) => level.charAt(0) + level.slice(1).toLowerCase();
  const formatGoal = (goal: string) => {
    switch (goal) {
      case 'STRENGTH': return 'Build Strength';
      case 'CARDIO': return 'Improve Cardio';
      case 'FLEXIBILITY': return 'Stay Flexible';
      case 'WEIGHT_LOSS': return 'Lose Weight';
      default: return goal;
    }
  };

  const handleResetProgress = () => {
    Alert.alert(
      'Reset Progress',
      'Are you sure you want to reset all your progress? This action cannot be undone.',
      [
        { text: 'Cancel', style: 'cancel' },
        { text: 'Reset', style: 'destructive', onPress: () => console.log('Progress reset') }
      ]
    );
  };

  const handleDeleteAccount = () => {
    Alert.alert(
      'Delete Account',
      'Are you sure you want to delete your account? All data will be permanently lost.',
      [
        { text: 'Cancel', style: 'cancel' },
        { text: 'Delete', style: 'destructive', onPress: () => console.log('Account deleted') }
      ]
    );
  };

  const SettingToggle = ({ icon: Icon, label, value, onValueChange, color = colors.orange.DEFAULT }: any) => (
    <View style={{
      flexDirection: 'row',
      alignItems: 'center',
      paddingVertical: spacing.md,
      borderBottomWidth: 1,
      borderBottomColor: colors.background.glassBorder
    }}>
      <View style={{
        backgroundColor: color + '20',
        width: 40,
        height: 40,
        borderRadius: radius.md,
        justifyContent: 'center',
        alignItems: 'center',
        marginRight: spacing.lg
      }}>
        <Icon size={20} color={color} />
      </View>
      <Text style={{ flex: 1, color: colors.text.primary, fontSize: typography.sizes.base, fontWeight: typography.weights.semibold }}>
        {label}
      </Text>
      <Switch
        value={value}
        onValueChange={onValueChange}
        trackColor={{ false: colors.background.elevated, true: colors.orange.DEFAULT + '60' }}
        thumbColor={value ? colors.orange.DEFAULT : colors.text.muted}
      />
    </View>
  );

  const SettingItem = ({ icon: Icon, label, value, color = colors.blue.DEFAULT, onPress }: any) => (
    <TouchableOpacity
      onPress={onPress}
      style={{
        flexDirection: 'row',
        alignItems: 'center',
        paddingVertical: spacing.md,
        borderBottomWidth: 1,
        borderBottomColor: colors.background.glassBorder
      }}
    >
      <View style={{
        backgroundColor: color + '20',
        width: 40,
        height: 40,
        borderRadius: radius.md,
        justifyContent: 'center',
        alignItems: 'center',
        marginRight: spacing.lg
      }}>
        <Icon size={20} color={color} />
      </View>
      <View style={{ flex: 1 }}>
        <Text style={{ color: colors.text.primary, fontSize: typography.sizes.base, fontWeight: typography.weights.semibold }}>
          {label}
        </Text>
        {value && (
          <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm, marginTop: 2 }}>
            {value}
          </Text>
        )}
      </View>
      <ChevronRight size={20} color={colors.text.muted} />
    </TouchableOpacity>
  );

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
        <LinearGradient
          colors={[colors.background.start, colors.background.mid, colors.background.end]}
          style={{ position: 'absolute', top: 0, left: 0, right: 0, bottom: 0 }}
        />
        <LinearGradient
          colors={['rgba(249, 115, 22, 0.15)', 'transparent']}
          style={{ position: 'absolute', top: -100, right: -100, width: 300, height: 300, borderRadius: 150 }}
        />
      </View>

      <ScrollView style={{ flex: 1 }} contentContainerStyle={{ paddingBottom: 120 }} showsVerticalScrollIndicator={false}>
        {/* Header */}
        <View style={{ paddingTop: 60, paddingHorizontal: spacing.xl, paddingBottom: spacing.xl }}>
          <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: spacing.lg }}>
            <TouchableOpacity
              onPress={() => navigation.goBack()}
              style={{
                width: 40,
                height: 40,
                borderRadius: radius.md,
                backgroundColor: colors.background.glass,
                justifyContent: 'center',
                alignItems: 'center',
                marginRight: spacing.md
              }}
            >
              <ArrowLeft size={24} color={colors.text.primary} />
            </TouchableOpacity>
            <Text style={{ color: colors.text.primary, fontSize: typography.sizes['2xl'], fontWeight: typography.weights.black }}>
              Settings
            </Text>
          </View>
        </View>

        {/* Profile Section */}
        <View style={{ paddingHorizontal: spacing.xl, marginBottom: spacing.xl }}>
          <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm, fontWeight: typography.weights.semibold, marginBottom: spacing.md, textTransform: 'uppercase', letterSpacing: 1 }}>
            Profile
          </Text>
          <View style={{
            backgroundColor: colors.background.card,
            borderRadius: radius['2xl'],
            padding: spacing.xl,
            borderWidth: 1,
            borderColor: colors.background.glassBorder
          }}>
            <SettingItem
              icon={Dumbbell}
              label="Fitness Level"
              value={formatLevel(profile?.fitnessLevel || 'BEGINNER')}
              color={colors.blue.DEFAULT}
              onPress={() => Alert.alert('Fitness Level', 'Edit your fitness level in your profile.')}
            />
            <SettingItem
              icon={Target}
              label="Primary Goal"
              value={formatGoal(profile?.primaryGoal || 'STRENGTH')}
              color={colors.orange.DEFAULT}
              onPress={() => Alert.alert('Primary Goal', 'Edit your primary goal in your profile.')}
            />
            <View style={{ borderBottomWidth: 0 }}>
              <SettingItem
                icon={Clock}
                label="Weekly Goal"
                value={`${profile?.weeklyGoalDays || 3} days per week`}
                color={colors.green.DEFAULT}
                onPress={() => Alert.alert('Weekly Goal', 'Edit your weekly goal in your profile.')}
              />
            </View>
          </View>
        </View>

        {/* Preferences Section */}
        <View style={{ paddingHorizontal: spacing.xl, marginBottom: spacing.xl }}>
          <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm, fontWeight: typography.weights.semibold, marginBottom: spacing.md, textTransform: 'uppercase', letterSpacing: 1 }}>
            Preferences
          </Text>
          <View style={{
            backgroundColor: colors.background.card,
            borderRadius: radius['2xl'],
            padding: spacing.xl,
            borderWidth: 1,
            borderColor: colors.background.glassBorder
          }}>
            <SettingToggle
              icon={Bell}
              label="Push Notifications"
              value={notifications}
              onValueChange={setNotifications}
              color={colors.blue.DEFAULT}
            />
            <SettingToggle
              icon={Moon}
              label="Dark Mode"
              value={darkMode}
              onValueChange={setDarkMode}
              color={colors.purple.DEFAULT}
            />
            <SettingToggle
              icon={Volume2}
              label="Sound Effects"
              value={soundEffects}
              onValueChange={setSoundEffects}
              color={colors.green.DEFAULT}
            />
            <View style={{ borderBottomWidth: 0 }}>
              <SettingToggle
                icon={Vibrate}
                label="Haptic Feedback"
                value={hapticFeedback}
                onValueChange={setHapticFeedback}
                color={colors.amber.DEFAULT}
              />
            </View>
          </View>
        </View>

        {/* Danger Zone */}
        <View style={{ paddingHorizontal: spacing.xl, marginBottom: spacing.xl }}>
          <Text style={{ color: colors.error.DEFAULT, fontSize: typography.sizes.sm, fontWeight: typography.weights.semibold, marginBottom: spacing.md, textTransform: 'uppercase', letterSpacing: 1 }}>
            Danger Zone
          </Text>
          <View style={{
            backgroundColor: colors.background.card,
            borderRadius: radius['2xl'],
            padding: spacing.xl,
            borderWidth: 1,
            borderColor: colors.error.DEFAULT + '30'
          }}>
            <TouchableOpacity
              onPress={handleResetProgress}
              style={{
                flexDirection: 'row',
                alignItems: 'center',
                paddingVertical: spacing.md,
                borderBottomWidth: 1,
                borderBottomColor: colors.background.glassBorder
              }}
            >
              <View style={{
                backgroundColor: colors.amber.DEFAULT + '20',
                width: 40,
                height: 40,
                borderRadius: radius.md,
                justifyContent: 'center',
                alignItems: 'center',
                marginRight: spacing.lg
              }}>
                <RotateCcw size={20} color={colors.amber.DEFAULT} />
              </View>
              <View style={{ flex: 1 }}>
                <Text style={{ color: colors.text.primary, fontSize: typography.sizes.base, fontWeight: typography.weights.semibold }}>
                  Reset Progress
                </Text>
                <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm, marginTop: 2 }}>
                  Clear all workout history and streaks
                </Text>
              </View>
            </TouchableOpacity>
            <TouchableOpacity
              onPress={handleDeleteAccount}
              style={{
                flexDirection: 'row',
                alignItems: 'center',
                paddingVertical: spacing.md
              }}
            >
              <View style={{
                backgroundColor: colors.error.DEFAULT + '20',
                width: 40,
                height: 40,
                borderRadius: radius.md,
                justifyContent: 'center',
                alignItems: 'center',
                marginRight: spacing.lg
              }}>
                <Trash2 size={20} color={colors.error.DEFAULT} />
              </View>
              <View style={{ flex: 1 }}>
                <Text style={{ color: colors.error.DEFAULT, fontSize: typography.sizes.base, fontWeight: typography.weights.semibold }}>
                  Delete Account
                </Text>
                <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm, marginTop: 2 }}>
                  Permanently delete your account and data
                </Text>
              </View>
            </TouchableOpacity>
          </View>
        </View>

        {/* App Info */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing.xl, alignItems: 'center' }}>
          <B3Logo size={48} />
          <Text style={{ color: colors.text.muted, fontSize: typography.sizes.sm, marginTop: spacing.md }}>B3 - Brick by Brick</Text>
          <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs, marginTop: spacing.xs }}>Version 1.0.0</Text>
        </View>
      </ScrollView>
    </View>
  );
}
