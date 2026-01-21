import React, { useState, useEffect } from 'react';
import { View, Text, ScrollView, TouchableOpacity, TextInput, Alert } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { LinearGradient } from 'expo-linear-gradient';
import { ChevronLeft, Zap, Brain, Moon, Smile, FileText, CheckCircle } from 'lucide-react-native';
import { colors, gradients, shadows, spacing, typography, radius } from '../constants/theme';
import { dailyLogApi } from '../services/api';
import { Mood, DailyLogDTO } from '../types/api';
import B3Logo from '../components/B3Logo';

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
        'Check-in Complete!',
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
    icon: Icon,
    color,
  }: {
    label: string;
    value: number;
    onChange: (v: number) => void;
    lowLabel: string;
    highLabel: string;
    icon: any;
    color: string;
  }) => (
    <View style={{ marginBottom: spacing['2xl'] }}>
      <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: spacing.md }}>
        <View style={{ backgroundColor: color + '20', width: 36, height: 36, borderRadius: radius.md, justifyContent: 'center', alignItems: 'center', marginRight: spacing.md }}>
          <Icon size={18} color={color} />
        </View>
        <Text style={{ color: colors.text.primary, fontSize: typography.sizes.lg, fontWeight: typography.weights.semibold }}>{label}</Text>
      </View>
      <View style={{ flexDirection: 'row', justifyContent: 'space-between', marginBottom: spacing.sm }}>
        <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs }}>{lowLabel}</Text>
        <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs }}>{highLabel}</Text>
      </View>
      <View style={{ flexDirection: 'row', gap: spacing.sm }}>
        {[1, 2, 3, 4, 5].map((level) => (
          <TouchableOpacity
            key={level}
            onPress={() => onChange(level)}
            style={{ flex: 1 }}
          >
            <View
              style={{
                backgroundColor: value >= level ? color : colors.background.glass,
                height: 52,
                borderRadius: radius.lg,
                justifyContent: 'center',
                alignItems: 'center',
                borderWidth: 1,
                borderColor: value >= level ? color : colors.background.glassBorder,
              }}
            >
              <Text
                style={{
                  color: value >= level ? '#fff' : colors.text.muted,
                  fontSize: typography.sizes.lg,
                  fontWeight: typography.weights.bold,
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
    <View style={{ flex: 1, backgroundColor: colors.background.end }}>
      {/* Background */}
      <View style={{ position: 'absolute', top: 0, left: 0, right: 0, bottom: 0 }}>
        <LinearGradient colors={[colors.background.start, colors.background.mid, colors.background.end]} style={{ position: 'absolute', top: 0, left: 0, right: 0, bottom: 0 }} />
        <LinearGradient colors={['rgba(249, 115, 22, 0.2)', 'transparent']} style={{ position: 'absolute', top: -100, right: -100, width: 350, height: 350, borderRadius: 175 }} />
        <LinearGradient colors={['rgba(59, 130, 246, 0.1)', 'transparent']} style={{ position: 'absolute', bottom: 100, left: -100, width: 300, height: 300, borderRadius: 150 }} />
      </View>

      <ScrollView style={{ flex: 1 }} contentContainerStyle={{ paddingBottom: 120 }} showsVerticalScrollIndicator={false}>
        {/* Header */}
        <View style={{ paddingHorizontal: spacing.xl, paddingTop: 60 }}>
          {/* Back Button */}
          <TouchableOpacity onPress={() => navigation.goBack()} style={{ marginBottom: spacing.xl }}>
            <View style={{ flexDirection: 'row', alignItems: 'center' }}>
              <ChevronLeft size={24} color={colors.text.secondary} />
              <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.base, marginLeft: spacing.xs }}>Back</Text>
            </View>
          </TouchableOpacity>

          <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'flex-start' }}>
            <View>
              <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm, letterSpacing: 2, textTransform: 'uppercase' }}>
                {isUpdate ? 'Update your' : 'Daily'}
              </Text>
              <Text style={{ color: colors.text.primary, fontSize: typography.sizes['4xl'], fontWeight: typography.weights.black, letterSpacing: -1 }}>
                Check-in
              </Text>
              <Text style={{ color: colors.text.muted, fontSize: typography.sizes.base, marginTop: spacing.xs }}>
                How are you feeling today?
              </Text>
            </View>
            <B3Logo size={48} />
          </View>
        </View>

        {/* Update Badge */}
        {isUpdate && (
          <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing.xl }}>
            <View style={{ backgroundColor: colors.success.DEFAULT + '20', padding: spacing.lg, borderRadius: radius.xl, flexDirection: 'row', alignItems: 'center', borderWidth: 1, borderColor: colors.success.DEFAULT + '40' }}>
              <CheckCircle size={20} color={colors.success.DEFAULT} />
              <Text style={{ color: colors.success.DEFAULT, marginLeft: spacing.md, fontSize: typography.sizes.sm, flex: 1 }}>
                You've already checked in today. Update your log below.
              </Text>
            </View>
          </View>
        )}

        {/* Form */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing['2xl'] }}>
          {/* Energy Level */}
          <SliderRow
            label="Energy Level"
            value={energyLevel}
            onChange={setEnergyLevel}
            lowLabel="Exhausted"
            highLabel="Energized"
            icon={Zap}
            color={colors.orange.DEFAULT}
          />

          {/* Stress Level */}
          <SliderRow
            label="Stress Level"
            value={stressLevel}
            onChange={setStressLevel}
            lowLabel="Relaxed"
            highLabel="Very Stressed"
            icon={Brain}
            color={colors.blue.DEFAULT}
          />

          {/* Sleep Quality */}
          <SliderRow
            label="Sleep Quality"
            value={sleepQuality}
            onChange={setSleepQuality}
            lowLabel="Poor"
            highLabel="Excellent"
            icon={Moon}
            color={colors.amber.DEFAULT}
          />

          {/* Mood Selector */}
          <View style={{ marginBottom: spacing['2xl'] }}>
            <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: spacing.md }}>
              <View style={{ backgroundColor: colors.green.DEFAULT + '20', width: 36, height: 36, borderRadius: radius.md, justifyContent: 'center', alignItems: 'center', marginRight: spacing.md }}>
                <Smile size={18} color={colors.green.DEFAULT} />
              </View>
              <Text style={{ color: colors.text.primary, fontSize: typography.sizes.lg, fontWeight: typography.weights.semibold }}>Mood</Text>
            </View>
            <View style={{ flexDirection: 'row', gap: spacing.sm }}>
              {MOOD_OPTIONS.map((option) => (
                <TouchableOpacity
                  key={option.value}
                  onPress={() => setMood(option.value)}
                  style={{ flex: 1 }}
                >
                  <View
                    style={{
                      backgroundColor: mood === option.value ? colors.green.DEFAULT : colors.background.card,
                      paddingVertical: spacing.lg,
                      borderRadius: radius.xl,
                      alignItems: 'center',
                      borderWidth: 1,
                      borderColor: mood === option.value ? colors.green.DEFAULT : colors.background.glassBorder,
                    }}
                  >
                    <Text style={{ fontSize: 28 }}>{option.emoji}</Text>
                    <Text
                      style={{
                        color: mood === option.value ? '#fff' : colors.text.secondary,
                        fontSize: typography.sizes.xs,
                        marginTop: spacing.xs,
                        fontWeight: mood === option.value ? typography.weights.semibold : typography.weights.normal,
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
          <View style={{ marginBottom: spacing['2xl'] }}>
            <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: spacing.md }}>
              <View style={{ backgroundColor: colors.text.muted + '30', width: 36, height: 36, borderRadius: radius.md, justifyContent: 'center', alignItems: 'center', marginRight: spacing.md }}>
                <FileText size={18} color={colors.text.secondary} />
              </View>
              <Text style={{ color: colors.text.primary, fontSize: typography.sizes.lg, fontWeight: typography.weights.semibold }}>
                Notes <Text style={{ color: colors.text.muted, fontWeight: typography.weights.normal, fontSize: typography.sizes.sm }}>(optional)</Text>
              </Text>
            </View>
            <View
              style={{
                backgroundColor: colors.background.card,
                borderRadius: radius.xl,
                borderWidth: 1,
                borderColor: colors.background.glassBorder,
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
                  fontSize: typography.sizes.base,
                  padding: spacing.lg,
                  minHeight: 100,
                  textAlignVertical: 'top',
                }}
              />
            </View>
          </View>

          {/* Summary Card */}
          <View
            style={{
              backgroundColor: colors.background.card,
              borderRadius: radius['2xl'],
              padding: spacing.xl,
              borderWidth: 1,
              borderColor: colors.background.glassBorder,
              ...shadows.card,
            }}
          >
            <Text style={{ color: colors.text.primary, fontSize: typography.sizes.base, fontWeight: typography.weights.bold, marginBottom: spacing.lg }}>
              Today's Summary
            </Text>
            <View style={{ flexDirection: 'row', justifyContent: 'space-around' }}>
              <View style={{ alignItems: 'center' }}>
                <View style={{ backgroundColor: colors.orange.DEFAULT + '20', width: 48, height: 48, borderRadius: radius.lg, justifyContent: 'center', alignItems: 'center', marginBottom: spacing.sm }}>
                  <Zap size={24} color={colors.orange.DEFAULT} />
                </View>
                <Text style={{ color: colors.orange.DEFAULT, fontSize: typography.sizes.xl, fontWeight: typography.weights.black }}>{energyLevel}</Text>
                <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs }}>Energy</Text>
              </View>
              <View style={{ alignItems: 'center' }}>
                <View style={{ backgroundColor: colors.blue.DEFAULT + '20', width: 48, height: 48, borderRadius: radius.lg, justifyContent: 'center', alignItems: 'center', marginBottom: spacing.sm }}>
                  <Brain size={24} color={colors.blue.DEFAULT} />
                </View>
                <Text style={{ color: colors.blue.DEFAULT, fontSize: typography.sizes.xl, fontWeight: typography.weights.black }}>{stressLevel}</Text>
                <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs }}>Stress</Text>
              </View>
              <View style={{ alignItems: 'center' }}>
                <View style={{ backgroundColor: colors.amber.DEFAULT + '20', width: 48, height: 48, borderRadius: radius.lg, justifyContent: 'center', alignItems: 'center', marginBottom: spacing.sm }}>
                  <Moon size={24} color={colors.amber.DEFAULT} />
                </View>
                <Text style={{ color: colors.amber.DEFAULT, fontSize: typography.sizes.xl, fontWeight: typography.weights.black }}>{sleepQuality}</Text>
                <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs }}>Sleep</Text>
              </View>
              <View style={{ alignItems: 'center' }}>
                <View style={{ backgroundColor: colors.green.DEFAULT + '20', width: 48, height: 48, borderRadius: radius.lg, justifyContent: 'center', alignItems: 'center', marginBottom: spacing.sm }}>
                  <Text style={{ fontSize: 24 }}>{MOOD_OPTIONS.find((m) => m.value === mood)?.emoji}</Text>
                </View>
                <Text style={{ color: colors.green.DEFAULT, fontSize: typography.sizes.sm, fontWeight: typography.weights.bold }}>
                  {MOOD_OPTIONS.find((m) => m.value === mood)?.label}
                </Text>
                <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs }}>Mood</Text>
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
          padding: spacing.xl,
          paddingBottom: 34,
          backgroundColor: colors.background.card,
          borderTopWidth: 1,
          borderTopColor: colors.background.glassBorder,
        }}
      >
        <TouchableOpacity onPress={handleSubmit} disabled={submitting} activeOpacity={0.9}>
          <LinearGradient
            colors={submitting ? [colors.text.muted, colors.text.muted] : gradients.fire}
            start={{ x: 0, y: 0 }}
            end={{ x: 1, y: 0 }}
            style={{ borderRadius: radius.xl, paddingVertical: spacing.lg, flexDirection: 'row', alignItems: 'center', justifyContent: 'center', ...(submitting ? {} : shadows.glow) }}
          >
            <CheckCircle size={22} color="#fff" />
            <Text style={{ color: '#fff', fontSize: typography.sizes.lg, fontWeight: typography.weights.bold, marginLeft: spacing.sm }}>
              {submitting ? 'Saving...' : isUpdate ? 'Update Check-in' : 'Complete Check-in'}
            </Text>
          </LinearGradient>
        </TouchableOpacity>
      </View>
    </View>
  );
}
