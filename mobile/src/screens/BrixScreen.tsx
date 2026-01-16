import React, { useState, useRef, useEffect } from 'react';
import { View, Text, ScrollView, TouchableOpacity, TextInput, KeyboardAvoidingView, Platform } from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { Send, MessageCircle } from 'lucide-react-native';
import { colors, gradients, shadows, radius, spacing, typography } from '../constants/theme';

interface Message {
  id: string;
  text: string;
  isUser: boolean;
  timestamp: Date;
}

export default function BrixScreen() {
  const scrollViewRef = useRef<ScrollView>(null);
  const [inputText, setInputText] = useState('');
  const [messages, setMessages] = useState<Message[]>([
    { id: '1', text: "Hey there! I'm BRIX, your AI fitness coach. I'm here to help you build your foundation, one brick at a time. How are you feeling today?", isUser: false, timestamp: new Date() },
  ]);

  const quickResponses = ["Ready to crush it!", "Feeling tired", "Need motivation", "What workout today?"];

  const getBrixResponse = (userMessage: string): string => {
    const lowerMessage = userMessage.toLowerCase();
    if (lowerMessage.includes('tired') || lowerMessage.includes('exhausted')) {
      return "I hear you! Rest is just as important as the workout itself. Remember, even the strongest walls need a solid foundation. Maybe try some light stretching or a short walk today? Tomorrow you'll come back stronger!";
    }
    if (lowerMessage.includes('ready') || lowerMessage.includes('crush')) {
      return "That's the spirit! Your energy is contagious! Let's channel that motivation into building something great. Check out the Workouts tab - I've got some perfect options waiting for you!";
    }
    if (lowerMessage.includes('motivation') || lowerMessage.includes('motivate')) {
      return "Here's some truth: Every single brick you lay matters. You're not just working out - you're building the person you want to become. The wall doesn't care if you're slow, it only cares that you show up. And YOU showed up today!";
    }
    if (lowerMessage.includes('workout') || lowerMessage.includes('what should')) {
      return "Based on your profile, I'd recommend checking out the 'Full Body Strength' workout today! It's 30 minutes of compound movements that'll help you build a solid foundation. Head to the Workouts tab when you're ready!";
    }
    if (lowerMessage.includes('hello') || lowerMessage.includes('hi') || lowerMessage.includes('hey')) {
      return "Hey! Great to see you! What's on your mind today? Need a workout recommendation, some motivation, or just want to chat about your fitness journey?";
    }
    return "I love your commitment to showing up! Remember, every interaction here is another brick in your foundation. What else can I help you with? Feel free to ask about workouts, motivation, or your progress!";
  };

  const sendMessage = (text: string) => {
    if (!text.trim()) return;
    const userMessage: Message = { id: Date.now().toString(), text: text.trim(), isUser: true, timestamp: new Date() };
    setMessages(prev => [...prev, userMessage]);
    setInputText('');
    setTimeout(() => {
      const brixResponse: Message = { id: (Date.now() + 1).toString(), text: getBrixResponse(text), isUser: false, timestamp: new Date() };
      setMessages(prev => [...prev, brixResponse]);
    }, 1000);
  };

  useEffect(() => { scrollViewRef.current?.scrollToEnd({ animated: true }); }, [messages]);

  const formatTime = (date: Date) => date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

  return (
    <View style={{ flex: 1, backgroundColor: colors.background.end }}>
      {/* Background */}
      <View style={{ position: 'absolute', top: 0, left: 0, right: 0, bottom: 0 }}>
        <LinearGradient colors={[colors.background.start, colors.background.mid, colors.background.end]} style={{ position: 'absolute', top: 0, left: 0, right: 0, bottom: 0 }} />
        <LinearGradient colors={['rgba(249, 115, 22, 0.15)', 'transparent']} style={{ position: 'absolute', top: -50, right: -50, width: 200, height: 200, borderRadius: 100 }} />
      </View>

      <KeyboardAvoidingView behavior={Platform.OS === 'ios' ? 'padding' : 'height'} style={{ flex: 1 }} keyboardVerticalOffset={0}>
        {/* Header */}
        <View style={{ paddingTop: 60, paddingBottom: spacing.lg, paddingHorizontal: spacing.xl, borderBottomWidth: 1, borderBottomColor: colors.background.glassBorder }}>
          <View style={{ flexDirection: 'row', alignItems: 'center' }}>
            <LinearGradient colors={gradients.fire} style={{ width: 48, height: 48, borderRadius: radius.lg, justifyContent: 'center', alignItems: 'center', marginRight: spacing.lg, ...shadows.glow }}>
              <MessageCircle size={24} color="#fff" />
            </LinearGradient>
            <View style={{ flex: 1 }}>
              <Text style={{ color: colors.text.primary, fontSize: typography.sizes.xl, fontWeight: typography.weights.bold }}>BRIX</Text>
              <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                <View style={{ backgroundColor: colors.green.DEFAULT, width: 8, height: 8, borderRadius: 4, marginRight: spacing.xs }} />
                <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm }}>Online - Adaptive Coach</Text>
              </View>
            </View>
          </View>
        </View>

        {/* Messages */}
        <ScrollView ref={scrollViewRef} style={{ flex: 1, paddingHorizontal: spacing.lg }} contentContainerStyle={{ paddingVertical: spacing.lg, paddingBottom: 100 }} showsVerticalScrollIndicator={false}>
          {messages.map((message) => (
            <View key={message.id} style={{ alignSelf: message.isUser ? 'flex-end' : 'flex-start', maxWidth: '85%', marginBottom: spacing.md }}>
              {!message.isUser && (
                <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: spacing.xs }}>
                  <LinearGradient colors={gradients.fire} style={{ width: 24, height: 24, borderRadius: 12, justifyContent: 'center', alignItems: 'center', marginRight: spacing.xs }}>
                    <MessageCircle size={12} color="#fff" />
                  </LinearGradient>
                  <Text style={{ color: colors.orange.DEFAULT, fontSize: typography.sizes.sm, fontWeight: typography.weights.semibold }}>BRIX</Text>
                </View>
              )}
              <LinearGradient
                colors={message.isUser ? [colors.blue.DEFAULT, colors.blue.dark] : [colors.orange.DEFAULT, colors.orange.dark]}
                style={{ borderRadius: radius.lg, borderTopRightRadius: message.isUser ? spacing.xs : radius.lg, borderTopLeftRadius: message.isUser ? radius.lg : spacing.xs, padding: spacing.lg }}
              >
                <Text style={{ color: colors.text.primary, fontSize: typography.sizes.base, lineHeight: 22 }}>{message.text}</Text>
              </LinearGradient>
              <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs, marginTop: spacing.xs, alignSelf: message.isUser ? 'flex-end' : 'flex-start' }}>{formatTime(message.timestamp)}</Text>
            </View>
          ))}
        </ScrollView>

        {/* Quick Responses */}
        <View style={{ paddingHorizontal: spacing.lg }}>
          <ScrollView horizontal showsHorizontalScrollIndicator={false}>
            <View style={{ flexDirection: 'row', gap: spacing.sm, paddingVertical: spacing.sm }}>
              {quickResponses.map((response, index) => (
                <TouchableOpacity key={index} onPress={() => sendMessage(response)} style={{ backgroundColor: colors.background.card, paddingHorizontal: spacing.lg, paddingVertical: spacing.sm, borderRadius: radius.full, borderWidth: 1, borderColor: colors.orange.DEFAULT + '40' }}>
                  <Text style={{ color: colors.text.primary, fontSize: typography.sizes.sm }}>{response}</Text>
                </TouchableOpacity>
              ))}
            </View>
          </ScrollView>
        </View>

        {/* Input Bar */}
        <View style={{ flexDirection: 'row', alignItems: 'center', paddingHorizontal: spacing.lg, paddingVertical: spacing.md, paddingBottom: 32, backgroundColor: colors.background.card, borderTopWidth: 1, borderTopColor: colors.background.glassBorder }}>
          <View style={{ flex: 1, backgroundColor: colors.background.glass, borderRadius: radius.full, paddingHorizontal: spacing.lg, paddingVertical: spacing.md, flexDirection: 'row', alignItems: 'center', borderWidth: 1, borderColor: colors.background.glassBorder }}>
            <TextInput
              value={inputText}
              onChangeText={setInputText}
              placeholder="Message BRIX..."
              placeholderTextColor={colors.text.muted}
              style={{ flex: 1, color: colors.text.primary, fontSize: typography.sizes.base }}
              onSubmitEditing={() => sendMessage(inputText)}
              returnKeyType="send"
            />
          </View>
          <TouchableOpacity onPress={() => sendMessage(inputText)} disabled={!inputText.trim()}>
            <LinearGradient colors={inputText.trim() ? gradients.fire : [colors.background.glass, colors.background.glass]} style={{ width: 44, height: 44, borderRadius: 22, justifyContent: 'center', alignItems: 'center', marginLeft: spacing.md }}>
              <Send size={20} color={colors.text.primary} />
            </LinearGradient>
          </TouchableOpacity>
        </View>
      </KeyboardAvoidingView>
    </View>
  );
}
