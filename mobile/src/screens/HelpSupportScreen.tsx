import React, { useState } from 'react';
import { View, Text, ScrollView, TouchableOpacity, Linking } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { LinearGradient } from 'expo-linear-gradient';
import {
  ArrowLeft,
  HelpCircle,
  MessageSquare,
  Mail,
  ExternalLink,
  ChevronDown,
  ChevronUp,
  BookOpen,
  Zap,
  Target,
  Flame,
  Trophy,
  Heart
} from 'lucide-react-native';
import { colors, radius, spacing, typography } from '../constants/theme';
import B3Logo from '../components/B3Logo';

interface FAQItem {
  question: string;
  answer: string;
  icon: any;
}

const faqs: FAQItem[] = [
  {
    question: 'What is a "brick" in B3?',
    answer: 'Each completed workout is a brick in your fitness foundation. Just like building a wall brick by brick, you\'re building your health one workout at a time. The brick metaphor helps you focus on consistency over perfection.',
    icon: Target
  },
  {
    question: 'How does BRIX, the AI coach, work?',
    answer: 'BRIX is your adaptive AI behavior coach. It learns your patterns, tracks your motivation, and adjusts its coaching tone to match your needs. When you\'re struggling, BRIX is empathetic. When you\'re on fire, BRIX challenges you to push harder.',
    icon: MessageSquare
  },
  {
    question: 'What happens if I miss a workout?',
    answer: 'Missing a day isn\'t failure - it\'s just a gap in your wall that BRIX helps you repair. B3 focuses on momentum, not perfection. Come back when you\'re ready, and BRIX will meet you where you are with the right encouragement.',
    icon: Heart
  },
  {
    question: 'How are streaks calculated?',
    answer: 'Your streak counts consecutive days with at least one completed workout. Streaks reset when you miss a day, but your total bricks and achievements are never lost. Focus on building your foundation, not just the streak.',
    icon: Flame
  },
  {
    question: 'How do I earn achievements?',
    answer: 'Achievements are unlocked by completing milestones like your first workout, hitting streak goals (3, 7, 14, 30 days), reaching total brick counts, and more. Check the Progress tab to see all available achievements and your progress toward each.',
    icon: Trophy
  },
  {
    question: 'Can I customize my workouts?',
    answer: 'Currently, B3 offers a curated library of workouts tailored to different fitness levels and goals. BRIX recommends workouts based on your energy level, stress, and preferences. Custom workout creation is planned for a future update.',
    icon: Zap
  }
];

export default function HelpSupportScreen() {
  const navigation = useNavigation();
  const [expandedFAQ, setExpandedFAQ] = useState<number | null>(null);

  const toggleFAQ = (index: number) => {
    setExpandedFAQ(expandedFAQ === index ? null : index);
  };

  const handleEmailSupport = () => {
    Linking.openURL('mailto:support@b3app.com?subject=B3 Support Request');
  };

  const handleVisitWebsite = () => {
    Linking.openURL('https://github.com/b-rickert/B3-Passion-Project');
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
          colors={['rgba(59, 130, 246, 0.15)', 'transparent']}
          style={{ position: 'absolute', top: -100, left: -100, width: 300, height: 300, borderRadius: 150 }}
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
              Help & Support
            </Text>
          </View>
          <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.base, lineHeight: 22 }}>
            Get answers to common questions or reach out for personalized support.
          </Text>
        </View>

        {/* Quick Actions */}
        <View style={{ paddingHorizontal: spacing.xl, marginBottom: spacing['2xl'] }}>
          <View style={{ flexDirection: 'row', gap: spacing.md }}>
            <TouchableOpacity
              onPress={handleEmailSupport}
              style={{
                flex: 1,
                backgroundColor: colors.background.card,
                borderRadius: radius.xl,
                padding: spacing.lg,
                alignItems: 'center',
                borderWidth: 1,
                borderColor: colors.background.glassBorder
              }}
            >
              <View style={{
                backgroundColor: colors.blue.DEFAULT + '20',
                width: 48,
                height: 48,
                borderRadius: radius.lg,
                justifyContent: 'center',
                alignItems: 'center',
                marginBottom: spacing.sm
              }}>
                <Mail size={24} color={colors.blue.DEFAULT} />
              </View>
              <Text style={{ color: colors.text.primary, fontSize: typography.sizes.sm, fontWeight: typography.weights.semibold }}>
                Email Support
              </Text>
            </TouchableOpacity>

            <TouchableOpacity
              onPress={handleVisitWebsite}
              style={{
                flex: 1,
                backgroundColor: colors.background.card,
                borderRadius: radius.xl,
                padding: spacing.lg,
                alignItems: 'center',
                borderWidth: 1,
                borderColor: colors.background.glassBorder
              }}
            >
              <View style={{
                backgroundColor: colors.orange.DEFAULT + '20',
                width: 48,
                height: 48,
                borderRadius: radius.lg,
                justifyContent: 'center',
                alignItems: 'center',
                marginBottom: spacing.sm
              }}>
                <ExternalLink size={24} color={colors.orange.DEFAULT} />
              </View>
              <Text style={{ color: colors.text.primary, fontSize: typography.sizes.sm, fontWeight: typography.weights.semibold }}>
                Visit GitHub
              </Text>
            </TouchableOpacity>
          </View>
        </View>

        {/* FAQ Section */}
        <View style={{ paddingHorizontal: spacing.xl }}>
          <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: spacing.lg }}>
            <BookOpen size={20} color={colors.orange.DEFAULT} />
            <Text style={{ color: colors.text.primary, fontSize: typography.sizes.xl, fontWeight: typography.weights.bold, marginLeft: spacing.sm }}>
              Frequently Asked Questions
            </Text>
          </View>

          <View style={{
            backgroundColor: colors.background.card,
            borderRadius: radius['2xl'],
            borderWidth: 1,
            borderColor: colors.background.glassBorder,
            overflow: 'hidden'
          }}>
            {faqs.map((faq, index) => (
              <View key={index}>
                <TouchableOpacity
                  onPress={() => toggleFAQ(index)}
                  style={{
                    flexDirection: 'row',
                    alignItems: 'center',
                    padding: spacing.lg
                  }}
                >
                  <View style={{
                    backgroundColor: colors.orange.DEFAULT + '20',
                    width: 36,
                    height: 36,
                    borderRadius: radius.md,
                    justifyContent: 'center',
                    alignItems: 'center',
                    marginRight: spacing.md
                  }}>
                    <faq.icon size={18} color={colors.orange.DEFAULT} />
                  </View>
                  <Text style={{
                    flex: 1,
                    color: colors.text.primary,
                    fontSize: typography.sizes.base,
                    fontWeight: typography.weights.semibold
                  }}>
                    {faq.question}
                  </Text>
                  {expandedFAQ === index ? (
                    <ChevronUp size={20} color={colors.text.muted} />
                  ) : (
                    <ChevronDown size={20} color={colors.text.muted} />
                  )}
                </TouchableOpacity>

                {expandedFAQ === index && (
                  <View style={{
                    paddingHorizontal: spacing.lg,
                    paddingBottom: spacing.lg,
                    paddingTop: 0
                  }}>
                    <View style={{
                      backgroundColor: colors.background.elevated,
                      borderRadius: radius.lg,
                      padding: spacing.md
                    }}>
                      <Text style={{
                        color: colors.text.secondary,
                        fontSize: typography.sizes.sm,
                        lineHeight: 20
                      }}>
                        {faq.answer}
                      </Text>
                    </View>
                  </View>
                )}

                {index < faqs.length - 1 && (
                  <View style={{ height: 1, backgroundColor: colors.background.glassBorder, marginHorizontal: spacing.lg }} />
                )}
              </View>
            ))}
          </View>
        </View>

        {/* About Section */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing['2xl'] }}>
          <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm, fontWeight: typography.weights.semibold, marginBottom: spacing.md, textTransform: 'uppercase', letterSpacing: 1 }}>
            About B3
          </Text>
          <View style={{
            backgroundColor: colors.background.card,
            borderRadius: radius['2xl'],
            padding: spacing.xl,
            borderWidth: 1,
            borderColor: colors.background.glassBorder
          }}>
            <View style={{ alignItems: 'center' }}>
              <B3Logo size={64} />
              <Text style={{ color: colors.text.primary, fontSize: typography.sizes.lg, fontWeight: typography.weights.bold, marginTop: spacing.md }}>
                B3 - Brick by Brick
              </Text>
              <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm, marginTop: spacing.xs, textAlign: 'center' }}>
                Build yourself, one brick at a time.
              </Text>
              <View style={{ marginTop: spacing.lg, paddingTop: spacing.lg, borderTopWidth: 1, borderTopColor: colors.background.glassBorder, width: '100%' }}>
                <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm, textAlign: 'center', lineHeight: 20 }}>
                  B3 is a mobile fitness application that helps you rebuild your health through consistent, achievable actions.
                  Our AI coach BRIX adapts to your motivation, energy, and patterns to keep you building your foundation.
                </Text>
              </View>
            </View>
          </View>
        </View>

        {/* Footer */}
        <View style={{ paddingHorizontal: spacing.xl, marginTop: spacing['3xl'], alignItems: 'center' }}>
          <Text style={{ color: colors.text.muted, fontSize: typography.sizes.sm }}>Built by Ben Rickert</Text>
          <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs, marginTop: spacing.xs }}>Version 1.0.0</Text>
        </View>
      </ScrollView>
    </View>
  );
}
