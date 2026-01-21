import React, { useState } from 'react';
import { View, Text, ScrollView, TouchableOpacity, TextInput, Modal } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { LinearGradient } from 'expo-linear-gradient';
import {
  ArrowLeft,
  Trophy,
  TrendingUp,
  Dumbbell,
  Timer,
  Flame,
  Plus,
  Award,
  Zap,
  Target,
  ChevronRight,
  X,
  Check
} from 'lucide-react-native';
import { colors, gradients, shadows, radius, spacing, typography } from '../constants/theme';
import B3Logo from '../components/B3Logo';

interface PersonalRecord {
  id: number;
  exercise: string;
  category: 'strength' | 'cardio' | 'endurance';
  value: number;
  unit: string;
  date: string;
  icon: any;
  color: string;
}

// Hardcoded personal records for demo
const MOCK_RECORDS: PersonalRecord[] = [
  { id: 1, exercise: 'Bench Press', category: 'strength', value: 185, unit: 'lbs', date: '2025-01-15', icon: Dumbbell, color: colors.orange.DEFAULT },
  { id: 2, exercise: 'Deadlift', category: 'strength', value: 275, unit: 'lbs', date: '2025-01-12', icon: Dumbbell, color: colors.orange.DEFAULT },
  { id: 3, exercise: 'Squat', category: 'strength', value: 225, unit: 'lbs', date: '2025-01-10', icon: Dumbbell, color: colors.orange.DEFAULT },
  { id: 4, exercise: '5K Run', category: 'cardio', value: 24.5, unit: 'min', date: '2025-01-18', icon: Timer, color: colors.blue.DEFAULT },
  { id: 5, exercise: 'Mile Run', category: 'cardio', value: 7.2, unit: 'min', date: '2025-01-14', icon: Timer, color: colors.blue.DEFAULT },
  { id: 6, exercise: 'Plank Hold', category: 'endurance', value: 180, unit: 'sec', date: '2025-01-16', icon: Flame, color: colors.amber.DEFAULT },
  { id: 7, exercise: 'Push-ups', category: 'endurance', value: 45, unit: 'reps', date: '2025-01-17', icon: Zap, color: colors.green.DEFAULT },
  { id: 8, exercise: 'Pull-ups', category: 'endurance', value: 12, unit: 'reps', date: '2025-01-13', icon: Zap, color: colors.green.DEFAULT },
];

const CATEGORIES = [
  { key: 'all', label: 'All', icon: Trophy, color: colors.orange.DEFAULT },
  { key: 'strength', label: 'Strength', icon: Dumbbell, color: colors.orange.DEFAULT },
  { key: 'cardio', label: 'Cardio', icon: Timer, color: colors.blue.DEFAULT },
  { key: 'endurance', label: 'Endurance', icon: Flame, color: colors.amber.DEFAULT },
];

export default function PersonalRecordsScreen() {
  const navigation = useNavigation();
  const [records, setRecords] = useState<PersonalRecord[]>(MOCK_RECORDS);
  const [selectedCategory, setSelectedCategory] = useState('all');
  const [showAddModal, setShowAddModal] = useState(false);
  const [newRecord, setNewRecord] = useState({
    exercise: '',
    value: '',
    unit: 'lbs',
    category: 'strength' as 'strength' | 'cardio' | 'endurance',
  });

  const filteredRecords = selectedCategory === 'all'
    ? records
    : records.filter(r => r.category === selectedCategory);

  const getRecordStats = () => {
    const totalRecords = records.length;
    const recentRecords = records.filter(r => {
      const recordDate = new Date(r.date);
      const weekAgo = new Date();
      weekAgo.setDate(weekAgo.getDate() - 7);
      return recordDate >= weekAgo;
    }).length;
    return { totalRecords, recentRecords };
  };

  const stats = getRecordStats();

  const formatDate = (dateStr: string) => {
    const date = new Date(dateStr);
    return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric' });
  };

  const handleAddRecord = () => {
    if (newRecord.exercise && newRecord.value) {
      const categoryConfig = {
        strength: { icon: Dumbbell, color: colors.orange.DEFAULT },
        cardio: { icon: Timer, color: colors.blue.DEFAULT },
        endurance: { icon: Flame, color: colors.amber.DEFAULT },
      };

      const config = categoryConfig[newRecord.category];

      setRecords([
        {
          id: records.length + 1,
          exercise: newRecord.exercise,
          category: newRecord.category,
          value: parseFloat(newRecord.value),
          unit: newRecord.unit,
          date: new Date().toISOString().split('T')[0],
          icon: config.icon,
          color: config.color,
        },
        ...records,
      ]);

      setNewRecord({ exercise: '', value: '', unit: 'lbs', category: 'strength' });
      setShowAddModal(false);
    }
  };

  return (
    <View style={{ flex: 1, backgroundColor: colors.background.end }}>
      {/* Background */}
      <View style={{ position: 'absolute', top: 0, left: 0, right: 0, bottom: 0 }}>
        <LinearGradient
          colors={[colors.background.start, colors.background.mid, colors.background.end]}
          style={{ position: 'absolute', top: 0, left: 0, right: 0, bottom: 0 }}
        />
        <LinearGradient
          colors={['rgba(249, 115, 22, 0.2)', 'transparent']}
          style={{ position: 'absolute', top: -100, right: -100, width: 350, height: 350, borderRadius: 175 }}
        />
      </View>

      <ScrollView style={{ flex: 1 }} contentContainerStyle={{ paddingBottom: 120 }} showsVerticalScrollIndicator={false}>
        {/* Header */}
        <View style={{ paddingTop: 60, paddingHorizontal: spacing.xl, paddingBottom: spacing.lg }}>
          <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' }}>
            <View style={{ flexDirection: 'row', alignItems: 'center' }}>
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
              <View>
                <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm, letterSpacing: 1, textTransform: 'uppercase' }}>
                  Personal
                </Text>
                <Text style={{ color: colors.text.primary, fontSize: typography.sizes['2xl'], fontWeight: typography.weights.black }}>
                  Records
                </Text>
              </View>
            </View>
            <TouchableOpacity
              onPress={() => setShowAddModal(true)}
              style={{
                backgroundColor: colors.orange.DEFAULT,
                width: 44,
                height: 44,
                borderRadius: 22,
                justifyContent: 'center',
                alignItems: 'center',
                ...shadows.glow,
              }}
            >
              <Plus size={24} color="#fff" />
            </TouchableOpacity>
          </View>
        </View>

        {/* Stats Cards */}
        <View style={{ paddingHorizontal: spacing.xl, marginBottom: spacing.xl }}>
          <View style={{ flexDirection: 'row', gap: spacing.md }}>
            <View style={{
              flex: 1,
              backgroundColor: colors.background.card,
              borderRadius: radius.xl,
              padding: spacing.lg,
              borderWidth: 1,
              borderColor: colors.orange.DEFAULT + '40',
            }}>
              <View style={{
                backgroundColor: colors.orange.DEFAULT + '20',
                width: 40,
                height: 40,
                borderRadius: radius.md,
                justifyContent: 'center',
                alignItems: 'center',
                marginBottom: spacing.sm
              }}>
                <Trophy size={20} color={colors.orange.DEFAULT} />
              </View>
              <Text style={{ color: colors.text.primary, fontSize: typography.sizes['2xl'], fontWeight: typography.weights.black }}>
                {stats.totalRecords}
              </Text>
              <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs }}>Total PRs</Text>
            </View>

            <View style={{
              flex: 1,
              backgroundColor: colors.background.card,
              borderRadius: radius.xl,
              padding: spacing.lg,
              borderWidth: 1,
              borderColor: colors.green.DEFAULT + '40',
            }}>
              <View style={{
                backgroundColor: colors.green.DEFAULT + '20',
                width: 40,
                height: 40,
                borderRadius: radius.md,
                justifyContent: 'center',
                alignItems: 'center',
                marginBottom: spacing.sm
              }}>
                <TrendingUp size={20} color={colors.green.DEFAULT} />
              </View>
              <Text style={{ color: colors.text.primary, fontSize: typography.sizes['2xl'], fontWeight: typography.weights.black }}>
                {stats.recentRecords}
              </Text>
              <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs }}>This Week</Text>
            </View>
          </View>
        </View>

        {/* Category Filters */}
        <View style={{ paddingHorizontal: spacing.xl, marginBottom: spacing.lg }}>
          <ScrollView horizontal showsHorizontalScrollIndicator={false}>
            <View style={{ flexDirection: 'row', gap: spacing.sm }}>
              {CATEGORIES.map((cat) => (
                <TouchableOpacity
                  key={cat.key}
                  onPress={() => setSelectedCategory(cat.key)}
                  style={{
                    flexDirection: 'row',
                    alignItems: 'center',
                    backgroundColor: selectedCategory === cat.key ? cat.color + '20' : colors.background.glass,
                    paddingHorizontal: spacing.md,
                    paddingVertical: spacing.sm,
                    borderRadius: radius.full,
                    borderWidth: 1,
                    borderColor: selectedCategory === cat.key ? cat.color + '40' : colors.background.glassBorder,
                  }}
                >
                  <cat.icon size={16} color={selectedCategory === cat.key ? cat.color : colors.text.muted} />
                  <Text style={{
                    color: selectedCategory === cat.key ? cat.color : colors.text.secondary,
                    fontSize: typography.sizes.sm,
                    fontWeight: typography.weights.semibold,
                    marginLeft: spacing.xs,
                  }}>
                    {cat.label}
                  </Text>
                </TouchableOpacity>
              ))}
            </View>
          </ScrollView>
        </View>

        {/* Records List */}
        <View style={{ paddingHorizontal: spacing.xl }}>
          <Text style={{
            color: colors.text.primary,
            fontSize: typography.sizes.lg,
            fontWeight: typography.weights.bold,
            marginBottom: spacing.md
          }}>
            Your Personal Bests
          </Text>

          <View style={{ gap: spacing.md }}>
            {filteredRecords.map((record, index) => (
              <TouchableOpacity
                key={record.id}
                activeOpacity={0.9}
                style={{
                  backgroundColor: colors.background.card,
                  borderRadius: radius.xl,
                  padding: spacing.lg,
                  flexDirection: 'row',
                  alignItems: 'center',
                  borderWidth: 1,
                  borderColor: index === 0 ? record.color + '40' : colors.background.glassBorder,
                }}
              >
                <View style={{
                  backgroundColor: record.color + '20',
                  width: 52,
                  height: 52,
                  borderRadius: radius.lg,
                  justifyContent: 'center',
                  alignItems: 'center',
                  marginRight: spacing.lg,
                }}>
                  <record.icon size={26} color={record.color} />
                </View>
                <View style={{ flex: 1 }}>
                  <Text style={{ color: colors.text.primary, fontSize: typography.sizes.base, fontWeight: typography.weights.semibold }}>
                    {record.exercise}
                  </Text>
                  <Text style={{ color: colors.text.muted, fontSize: typography.sizes.sm, marginTop: 2 }}>
                    {formatDate(record.date)}
                  </Text>
                </View>
                <View style={{ alignItems: 'flex-end' }}>
                  <Text style={{ color: record.color, fontSize: typography.sizes.xl, fontWeight: typography.weights.black }}>
                    {record.value}
                  </Text>
                  <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs }}>
                    {record.unit}
                  </Text>
                </View>
                {index === 0 && (
                  <View style={{
                    position: 'absolute',
                    top: -8,
                    right: spacing.lg,
                    backgroundColor: colors.amber.DEFAULT,
                    paddingHorizontal: spacing.sm,
                    paddingVertical: 2,
                    borderRadius: radius.sm,
                  }}>
                    <Text style={{ color: '#fff', fontSize: 9, fontWeight: typography.weights.bold }}>LATEST</Text>
                  </View>
                )}
              </TouchableOpacity>
            ))}
          </View>
        </View>

        {/* Motivational Footer */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing['3xl'], alignItems: 'center' }}>
          <Award size={32} color={colors.text.muted} />
          <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm, textAlign: 'center', marginTop: spacing.sm }}>
            Keep pushing your limits!
          </Text>
          <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs, textAlign: 'center', marginTop: spacing.xs }}>
            Every PR is a brick in your foundation
          </Text>
        </View>
      </ScrollView>

      {/* Add Record Modal */}
      <Modal
        visible={showAddModal}
        transparent
        animationType="slide"
        onRequestClose={() => setShowAddModal(false)}
      >
        <View style={{
          flex: 1,
          backgroundColor: 'rgba(0,0,0,0.7)',
          justifyContent: 'flex-end',
        }}>
          <View style={{
            backgroundColor: colors.background.card,
            borderTopLeftRadius: radius['2xl'],
            borderTopRightRadius: radius['2xl'],
            padding: spacing.xl,
            paddingBottom: spacing['4xl'],
          }}>
            {/* Modal Header */}
            <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: spacing.xl }}>
              <Text style={{ color: colors.text.primary, fontSize: typography.sizes.xl, fontWeight: typography.weights.bold }}>
                Add New PR
              </Text>
              <TouchableOpacity onPress={() => setShowAddModal(false)}>
                <X size={24} color={colors.text.muted} />
              </TouchableOpacity>
            </View>

            {/* Exercise Name */}
            <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm, marginBottom: spacing.sm }}>
              Exercise Name
            </Text>
            <TextInput
              value={newRecord.exercise}
              onChangeText={(text) => setNewRecord({ ...newRecord, exercise: text })}
              placeholder="e.g., Bench Press"
              placeholderTextColor={colors.text.muted}
              style={{
                backgroundColor: colors.background.elevated,
                borderRadius: radius.lg,
                padding: spacing.lg,
                color: colors.text.primary,
                fontSize: typography.sizes.base,
                marginBottom: spacing.lg,
              }}
            />

            {/* Category Selection */}
            <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm, marginBottom: spacing.sm }}>
              Category
            </Text>
            <View style={{ flexDirection: 'row', gap: spacing.sm, marginBottom: spacing.lg }}>
              {['strength', 'cardio', 'endurance'].map((cat) => (
                <TouchableOpacity
                  key={cat}
                  onPress={() => setNewRecord({ ...newRecord, category: cat as any })}
                  style={{
                    flex: 1,
                    backgroundColor: newRecord.category === cat ? colors.orange.DEFAULT + '20' : colors.background.elevated,
                    padding: spacing.md,
                    borderRadius: radius.lg,
                    alignItems: 'center',
                    borderWidth: 1,
                    borderColor: newRecord.category === cat ? colors.orange.DEFAULT + '40' : 'transparent',
                  }}
                >
                  <Text style={{
                    color: newRecord.category === cat ? colors.orange.DEFAULT : colors.text.secondary,
                    fontSize: typography.sizes.sm,
                    fontWeight: typography.weights.semibold,
                    textTransform: 'capitalize',
                  }}>
                    {cat}
                  </Text>
                </TouchableOpacity>
              ))}
            </View>

            {/* Value and Unit */}
            <View style={{ flexDirection: 'row', gap: spacing.md, marginBottom: spacing.xl }}>
              <View style={{ flex: 2 }}>
                <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm, marginBottom: spacing.sm }}>
                  Value
                </Text>
                <TextInput
                  value={newRecord.value}
                  onChangeText={(text) => setNewRecord({ ...newRecord, value: text })}
                  placeholder="0"
                  placeholderTextColor={colors.text.muted}
                  keyboardType="numeric"
                  style={{
                    backgroundColor: colors.background.elevated,
                    borderRadius: radius.lg,
                    padding: spacing.lg,
                    color: colors.text.primary,
                    fontSize: typography.sizes.base,
                  }}
                />
              </View>
              <View style={{ flex: 1 }}>
                <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm, marginBottom: spacing.sm }}>
                  Unit
                </Text>
                <TouchableOpacity
                  onPress={() => {
                    const units = ['lbs', 'kg', 'min', 'sec', 'reps'];
                    const currentIndex = units.indexOf(newRecord.unit);
                    setNewRecord({ ...newRecord, unit: units[(currentIndex + 1) % units.length] });
                  }}
                  style={{
                    backgroundColor: colors.background.elevated,
                    borderRadius: radius.lg,
                    padding: spacing.lg,
                    alignItems: 'center',
                  }}
                >
                  <Text style={{ color: colors.text.primary, fontSize: typography.sizes.base }}>
                    {newRecord.unit}
                  </Text>
                </TouchableOpacity>
              </View>
            </View>

            {/* Save Button */}
            <TouchableOpacity activeOpacity={0.9} onPress={handleAddRecord}>
              <LinearGradient
                colors={gradients.fire}
                start={{ x: 0, y: 0 }}
                end={{ x: 1, y: 0 }}
                style={{
                  borderRadius: radius.lg,
                  paddingVertical: spacing.lg,
                  flexDirection: 'row',
                  alignItems: 'center',
                  justifyContent: 'center',
                }}
              >
                <Check size={20} color="#fff" />
                <Text style={{ color: '#fff', fontSize: typography.sizes.base, fontWeight: typography.weights.bold, marginLeft: spacing.sm }}>
                  Save Record
                </Text>
              </LinearGradient>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>
    </View>
  );
}
