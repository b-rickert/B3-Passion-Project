import React, { useState, useEffect } from 'react';
import { View, Text, ScrollView, TouchableOpacity, TextInput, Alert } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useNavigation } from '@react-navigation/native';
import { LinearGradient } from 'expo-linear-gradient';
import { colors } from '../constants/theme';
import { dailyLogApi } from '../services/api';
import { Mood, DailyLogDTO } from '../types/api';

const orangeOutline = {
  borderWidth: 2,
  borderColor: colors.orange.DEFAULT,
};

const MOOD_OPTIONS: { value: Mood; label: string; emoji: string }[] = [
  { value: 'GREAT', label: 'Great', emoji: '😄' },
  { value: 'GOOD', label: 'Good', emoji: '🙂' },
  { value: 'OKAY', label: 'Okay', emoji: '😐' },
  { value: 'LOW', label: 'Low', emoji: '😔' },
  { value: 'STRESSED', label: 'Stressed', emoji: '😰' },
];

export default function DailyLogScreen() {
  const navigation = useNavigation();
  const [energyLevel, setEnergyLevel] = useState(3);
  const [stressLevel, setStressLevel] = useState(3);
  const [sleepQuality, setSleepQuality] = useState(3);
  const [mood, setMood] = useState<Mood>('OKAY');
  const [notes, setNotes] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [existingLogId, setExistingLogId] = useState<number | null>(null);
  const [isUpdate, setIsUpdate] = useState(false);

  useEffect(() => {
    checkExistingLog();
  }, []);

  const checkExistingLog = async () => {
    try {
      const { hasLoggedToday } = await dailyLogApi.hasLoggedToday(1);
      if (hasLoggedToday) {
        const todaysLog = await dailyLogApi.getTodaysLog(1);
        setExistingLogId(todaysLog.logId);
        setEnergyLevel(todaysLog.energyLevel);
        setStressLevel(todaysLog.stressLevel);
        setSleepQuality(todaysLog.sleepQuality);
        setMood(todaysLog.mood);
        setNotes(todaysLog.notes || '');
        setIsUpdate(true);
      }
    } catch (error) {
      console.log('No existing log found');
    }
  };

  const handleSubmit = async () => {
    setSubmitting(true);
    try {
      const logData = {
        profileId: 1,
        energyLevel,
        stressLevel,
        sleepQuality,
        mood,
        notes: notes.trim() || undefined,
      };

      if (isUpdate && existingLogId) {
        await dailyLogApi.updateDailyLog(existingLogId, logData);
      } else {
        await dailyLogApi.submitDailyLog(logData);
      }

      Alert.alert(
        '✅ Check-in Complete!',
        isUpdate 
          ? 'Your daily log has been updated.'
          : 'Your daily log has been saved. BRIX will use this to personalize your recommendations.',
        [{ text: 'Awesome!', onPress: () => navigation.goBack() }]
      );
    } catch (error) {
      console.error('Error submitting daily log:', error);
      Alert.alert('Error', 'Failed to save your check-in. Please try again.');
    } finally {
      setSubmitting(false);
    }
  };

  const SliderRow = ({
    label,
    value,
    onChange,
    lowLabel,
    highLabel,
    icon,
  }: {
    label: string;
    value: number;
    onChange: (v: number) => void;
    lowLabel: string;
    highLabel: string;
    icon: string;
  }) => (
    <View style={{ marginBottom: 24 }}>
      <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 12 }}>
        <Text style={{ fontSize: 20, marginRight: 8 }}>{icon}</Text>
        <Text style={{ color: colors.text.primary, fontSize: 16, fontWeight: '600' }}>{label}</Text>
      </View>
      <View style={{ flexDirection: 'row', justifyContent: 'space-between', marginBottom: 8 }}>
        <Text style={{ color: colors.text.muted, fontSize: 12 }}>{lowLabel}</Text>
        <Text style={{ color: colors.text.muted, fontSize: 12 }}>{highLabel}</Text>
      </View>
      <View style={{ flexDirection: 'row', gap: 8 }}>
        {[1, 2, 3, 4, 5].map((level) => (
          <TouchableOpacity
            key={level}
            onPress={() => onChange(level)}
            style={{ flex: 1 }}
          >
            <View
              style={{
                backgroundColor: value >= level ? colors.orange.DEFAULT : colors.background.elevated,
                height: 48,
                borderRadius: 12,
                justifyContent: 'center',
                alignItems: 'center',
                borderWidth: value === level ? 2 : 0,
                borderColor: colors.orange.light,
              }}
            >
              <Text
                style={{
                  color: value >= level ? '#fff' : colors.text.muted,
                  fontSize: 16,
                  fontWeight: '700',
                }}
              >
                {level}
              </Text>
            </View>
          </TouchableOpacity>
        ))}
      </View>
    </View>
  );

  return (
    <View style={{ flex: 1, backgroundColor: colors.background.secondary }}>
      <ScrollView style={{ flex: 1 }} contentContainerStyle={{ paddingBottom: 120 }}>
        {/* Header */}
        <LinearGradient
          colors={['#2563eb', '#3b82f6', '#60a5fa']}
          start={{ x: 0, y: 0 }}
          end={{ x: 1, y: 1 }}
          style={{
            paddingTop: 50,
            paddingBottom: 30,
            paddingHorizontal: 20,
            borderBottomLeftRadius: 20,
            borderBottomRightRadius: 20,
            borderWidth: 2,
            borderColor: colors.orange.DEFAULT,
            borderTopWidth: 0,
          }}
        >
          {/* Back Button */}
          <TouchableOpacity onPress={() => navigation.goBack()} style={{ marginBottom: 16 }}>
            <View style={{ flexDirection: 'row', alignItems: 'center' }}>
              <Ionicons name="arrow-back" size={24} color="#fff" />
              <Text style={{ color: '#fff', fontSize: 16, marginLeft: 8 }}>Back</Text>
            </View>
          </TouchableOpacity>

          <View style={{ flexDirection: 'row', alignItems: 'center' }}>
            <View
              style={{
                backgroundColor: 'rgba(255,255,255,0.2)',
                width: 56,
                height: 56,
                borderRadius: 16,
                justifyContent: 'center',
                alignItems: 'center',
                marginRight: 16,
              }}
            >
              <Text style={{ fontSize: 28 }}>📋</Text>
            </View>
            <View>
              <Text style={{ color: '#fff', fontSize: 24, fontWeight: '800' }}>
                {isUpdate ? 'Update Check-in' : 'Daily Check-in'}
              </Text>
              <Text style={{ color: 'rgba(255,255,255,0.8)', fontSize: 14, marginTop: 4 }}>
                {isUpdate ? 'Update how you\'re feeling today' : 'How are you feeling today?'}
              </Text>
            </View>
          </View>
        </LinearGradient>

        {/* Update Badge */}
        {isUpdate && (
          <View style={{ paddingHorizontal: 20, marginTop: 16 }}>
            <View style={{ backgroundColor: colors.blue.DEFAULT + '20', padding: 12, borderRadius: 12, flexDirection: 'row', alignItems: 'center' }}>
              <Ionicons name="checkmark-circle" size={20} color={colors.blue.DEFAULT} />
              <Text style={{ color: colors.blue.DEFAULT, marginLeft: 8, fontSize: 14 }}>
                You've already checked in today. Update your log below.
              </Text>
            </View>
          </View>
        )}

        {/* Form */}
        <View style={{ paddingHorizontal: 20, marginTop: 24 }}>
          {/* Energy Level */}
          <SliderRow
            label="Energy Level"
            value={energyLevel}
            onChange={setEnergyLevel}
            lowLabel="Exhausted"
            highLabel="Energized"
            icon="⚡"
          />

          {/* Stress Level */}
          <SliderRow
            label="Stress Level"
            value={stressLevel}
            onChange={setStressLevel}
            lowLabel="Relaxed"
            highLabel="Very Stressed"
            icon="😤"
          />

          {/* Sleep Quality */}
          <SliderRow
            label="Sleep Quality"
            value={sleepQuality}
            onChange={setSleepQuality}
            lowLabel="Poor"
            highLabel="Excellent"
            icon="😴"
          />

          {/* Mood Selector */}
          <View style={{ marginBottom: 24 }}>
            <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 12 }}>
              <Text style={{ fontSize: 20, marginRight: 8 }}>🎭</Text>
              <Text style={{ color: colors.text.primary, fontSize: 16, fontWeight: '600' }}>Mood</Text>
            </View>
            <View style={{ flexDirection: 'row', gap: 8 }}>
              {MOOD_OPTIONS.map((option) => (
                <TouchableOpacity
                  key={option.value}
                  onPress={() => setMood(option.value)}
                  style={{ flex: 1 }}
                >
                  <View
                    style={{
                      backgroundColor: mood === option.value ? colors.orange.DEFAULT : colors.background.tertiary,
                      paddingVertical: 16,
                      borderRadius: 12,
                      alignItems: 'center',
                      ...orangeOutline,
                      borderColor: mood === option.value ? colors.orange.light : colors.orange.DEFAULT,
                    }}
                  >
                    <Text style={{ fontSize: 28 }}>{option.emoji}</Text>
                    <Text
                      style={{
                        color: mood === option.value ? '#fff' : colors.text.secondary,
                        fontSize: 11,
                        marginTop: 4,
                        fontWeight: mood === option.value ? '600' : '400',
                      }}
                    >
                      {option.label}
                    </Text>
                  </View>
                </TouchableOpacity>
              ))}
            </View>
          </View>

          {/* Notes */}
          <View style={{ marginBottom: 24 }}>
            <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 12 }}>
              <Text style={{ fontSize: 20, marginRight: 8 }}>📝</Text>
              <Text style={{ color: colors.text.primary, fontSize: 16, fontWeight: '600' }}>
                Notes <Text style={{ color: colors.text.muted, fontWeight: '400' }}>(optional)</Text>
              </Text>
            </View>
            <View
              style={{
                backgroundColor: colors.background.tertiary,
                borderRadius: 12,
                ...orangeOutline,
              }}
            >
              <TextInput
                value={notes}
                onChangeText={setNotes}
                placeholder="Anything else you want to note about today?"
                placeholderTextColor={colors.text.muted}
                multiline
                numberOfLines={4}
                style={{
                  color: colors.text.primary,
                  fontSize: 15,
                  padding: 16,
                  minHeight: 100,
                  textAlignVertical: 'top',
                }}
              />
            </View>
          </View>

          {/* Summary Card */}
          <View
            style={{
              backgroundColor: colors.background.tertiary,
              borderRadius: 16,
              padding: 16,
              ...orangeOutline,
            }}
          >
            <Text style={{ color: colors.text.primary, fontSize: 14, fontWeight: '600', marginBottom: 12 }}>
              Today's Summary
            </Text>
            <View style={{ flexDirection: 'row', justifyContent: 'space-around' }}>
              <View style={{ alignItems: 'center' }}>
                <Text style={{ fontSize: 24 }}>⚡</Text>
                <Text style={{ color: colors.orange.DEFAULT, fontSize: 20, fontWeight: '700' }}>{energyLevel}</Text>
                <Text style={{ color: colors.text.muted, fontSize: 11 }}>Energy</Text>
              </View>
              <View style={{ alignItems: 'center' }}>
                <Text style={{ fontSize: 24 }}>😤</Text>
                <Text style={{ color: colors.text.primary, fontSize: 20, fontWeight: '700' }}>{stressLevel}</Text>
                <Text style={{ color: colors.text.muted, fontSize: 11 }}>Stress</Text>
              </View>
              <View style={{ alignItems: 'center' }}>
                <Text style={{ fontSize: 24 }}>😴</Text>
                <Text style={{ color: colors.text.primary, fontSize: 20, fontWeight: '700' }}>{sleepQuality}</Text>
                <Text style={{ color: colors.text.muted, fontSize: 11 }}>Sleep</Text>
              </View>
              <View style={{ alignItems: 'center' }}>
                <Text style={{ fontSize: 24 }}>{MOOD_OPTIONS.find((m) => m.value === mood)?.emoji}</Text>
                <Text style={{ color: colors.text.primary, fontSize: 14, fontWeight: '700' }}>
                  {MOOD_OPTIONS.find((m) => m.value === mood)?.label}
                </Text>
                <Text style={{ color: colors.text.muted, fontSize: 11 }}>Mood</Text>
              </View>
            </View>
          </View>
        </View>
      </ScrollView>

      {/* Fixed Bottom Button */}
      <View
        style={{
          position: 'absolute',
          bottom: 0,
          left: 0,
          right: 0,
          padding: 20,
          paddingBottom: 32,
          backgroundColor: colors.background.secondary,
          borderTopWidth: 1,
          borderTopColor: colors.background.elevated,
        }}
      >
        <TouchableOpacity onPress={handleSubmit} disabled={submitting} activeOpacity={0.9}>
          <LinearGradient
            colors={submitting ? [colors.text.muted, colors.text.muted] : [colors.orange.DEFAULT, colors.orange.dark]}
            start={{ x: 0, y: 0 }}
            end={{ x: 1, y: 0 }}
            style={{ borderRadius: 16, paddingVertical: 18, alignItems: 'center' }}
          >
            <Text style={{ color: '#fff', fontSize: 18, fontWeight: '700' }}>
              {submitting ? 'Saving...' : isUpdate ? 'Update Check-in ✓' : 'Complete Check-in ✓'}
            </Text>
          </LinearGradient>
        </TouchableOpacity>
      </View>
    </View>
  );
}
