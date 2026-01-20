import React, { useState, useRef, useEffect } from 'react';
import { View, Text, ScrollView, TouchableOpacity, TextInput, KeyboardAvoidingView, Platform, ActivityIndicator } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { LinearGradient } from 'expo-linear-gradient';
import { colors } from '../constants/theme';

const orangeOutline = {
  borderWidth: 2,
  borderColor: colors.orange.DEFAULT,
};

interface Message {
  id: string;
  text: string;
  isUser: boolean;
  timestamp: Date;
}

interface WorkoutRecommendation {
  workoutId: number;
  name: string;
  type: string;
  difficulty: string;
  duration: number;
  reason: string;
}

export default function BrixScreen() {
  const navigation = useNavigation();
  const scrollViewRef = useRef<ScrollView>(null);
  const [inputText, setInputText] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [recommendation, setRecommendation] = useState<WorkoutRecommendation | null>(null);
  const [messages, setMessages] = useState<Message[]>([
    { id: '1', text: "Hey there! I'm BRIX, your AI fitness coach. I'm here to help you build your foundation, one brick at a time. How are you feeling today?", isUser: false, timestamp: new Date() },
  ]);

  const quickResponses = ["Ready to crush it!", "Feeling tired", "Need motivation", "What workout today?"];

  const getBrixResponse = (userMessage: string): string => {
    const lowerMessage = userMessage.toLowerCase();
    
    if (lowerMessage.includes('tired') || lowerMessage.includes('exhausted')) {
      return "I hear you! Rest is just as important as the workout itself. 🧱 Remember, even the strongest walls need a solid foundation. Maybe try some light stretching or a short walk today? Tomorrow you'll come back stronger!";
    }
    if (lowerMessage.includes('ready') || lowerMessage.includes('crush')) {
      return "That's the spirit! 🔥 Your energy is contagious! Let's channel that motivation into building something great. Check out the Workouts tab - I've got some perfect options waiting for you!";
    }
    if (lowerMessage.includes('motivation') || lowerMessage.includes('motivate')) {
      return "Here's some truth: Every single brick you lay matters. 🧱 You're not just working out - you're building the person you want to become. The wall doesn't care if you're slow, it only cares that you show up. And YOU showed up today!";
    }
    if (lowerMessage.includes('workout') || lowerMessage.includes('what should')) {
      return "Based on your profile, I'd recommend checking out the 'Full Body Strength' workout today! It's 30 minutes of compound movements that'll help you build a solid foundation. 💪 Head to the Workouts tab when you're ready!";
    }
    if (lowerMessage.includes('hello') || lowerMessage.includes('hi') || lowerMessage.includes('hey')) {
      return "Hey! Great to see you! 👋 What's on your mind today? Need a workout recommendation, some motivation, or just want to chat about your fitness journey?";
    }
    
    return "I love your commitment to showing up! 🧱 Remember, every interaction here is another brick in your foundation. What else can I help you with? Feel free to ask about workouts, motivation, or your progress!";
  };

  const sendMessage = (text: string) => {
    if (!text.trim()) return;

    const userMessage: Message = {
      id: Date.now().toString(),
      text: text.trim(),
      isUser: true,
      timestamp: new Date(),
    };

    setMessages(prev => [...prev, userMessage]);
    setInputText('');

    setTimeout(() => {
      const brixResponse: Message = {
        id: (Date.now() + 1).toString(),
        text: getBrixResponse(text),
        isUser: false,
        timestamp: new Date(),
      };
      setMessages(prev => [...prev, brixResponse]);

      // Update recommendation if provided
      if (response.data.recommendation) {
        setRecommendation(response.data.recommendation);
      }
    } catch (error) {
      console.error('BRIX chat error:', error);
      // Fallback response
      const errorResponse: Message = {
        id: (Date.now() + 1).toString(),
        text: "Hmm, I'm having trouble connecting right now. But hey, you showing up matters! 🧱 Try again in a moment.",
        isUser: false,
        timestamp: new Date(),
      };
      setMessages(prev => [...prev, errorResponse]);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => { scrollViewRef.current?.scrollToEnd({ animated: true }); }, [messages]);

  const formatTime = (date: Date) => {
    return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
  };

  return (
    <View style={{ flex: 1, backgroundColor: colors.background.end }}>
      {/* Background */}
      <View style={{ position: 'absolute', top: 0, left: 0, right: 0, bottom: 0 }}>
        <LinearGradient colors={[colors.background.start, colors.background.mid, colors.background.end]} style={{ position: 'absolute', top: 0, left: 0, right: 0, bottom: 0 }} />
        <LinearGradient colors={['rgba(249, 115, 22, 0.15)', 'transparent']} style={{ position: 'absolute', top: -50, right: -50, width: 200, height: 200, borderRadius: 100 }} />
      </View>

      <KeyboardAvoidingView behavior={Platform.OS === 'ios' ? 'padding' : 'height'} style={{ flex: 1 }} keyboardVerticalOffset={0}>
        {/* Header */}
        <LinearGradient
          colors={['#2563eb', '#3b82f6', '#60a5fa']}
          start={{ x: 0, y: 0 }}
          end={{ x: 1, y: 1 }}
          style={{
            paddingTop: 50,
            paddingBottom: 20,
            paddingHorizontal: 20,
            borderBottomLeftRadius: 20,
            borderBottomRightRadius: 20,
            borderWidth: 2,
            borderColor: colors.orange.DEFAULT,
            borderTopWidth: 0,
          }}
        >
          <View style={{ flexDirection: 'row', alignItems: 'center' }}>
            <View
              style={{
                backgroundColor: colors.orange.DEFAULT,
                width: 48,
                height: 48,
                borderRadius: 24,
                justifyContent: 'center',
                alignItems: 'center',
                marginRight: 14,
              }}
            >
              <Text style={{ fontSize: 26 }}>🤖</Text>
            </View>
            <View style={{ flex: 1 }}>
              <Text style={{ color: '#fff', fontSize: 22, fontWeight: '800' }}>BRIX</Text>
              <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                <View
                  style={{
                    backgroundColor: colors.green.DEFAULT,
                    width: 8,
                    height: 8,
                    borderRadius: 4,
                    marginRight: 6,
                  }}
                />
                <Text style={{ color: 'rgba(255,255,255,0.8)', fontSize: 13 }}>Online • Adaptive Coach</Text>
              </View>
            </View>
            <B3Logo size={44} />
          </View>
        </LinearGradient>

        {/* Messages */}
        <ScrollView
          ref={scrollViewRef}
          style={{ flex: 1, paddingHorizontal: 16 }}
          contentContainerStyle={{ paddingVertical: 16, paddingBottom: 100 }}
          showsVerticalScrollIndicator={false}
        >
          {messages.map((message) => (
            <View
              key={message.id}
              style={{
                alignSelf: message.isUser ? 'flex-end' : 'flex-start',
                maxWidth: '85%',
                marginBottom: 12,
              }}
            >
              {!message.isUser && (
                <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 4 }}>
                  <View
                    style={{
                      backgroundColor: colors.orange.DEFAULT,
                      width: 24,
                      height: 24,
                      borderRadius: 12,
                      justifyContent: 'center',
                      alignItems: 'center',
                      marginRight: 6,
                    }}
                  >
                    <Text style={{ fontSize: 12 }}>🤖</Text>
                  </View>
                  <Text style={{ color: colors.orange.DEFAULT, fontSize: 13, fontWeight: '600' }}>BRIX</Text>
                </View>
              )}
              <View
                style={{
                  backgroundColor: message.isUser ? colors.blue.DEFAULT : colors.orange.DEFAULT,
                  borderRadius: 16,
                  borderTopRightRadius: message.isUser ? 4 : 16,
                  borderTopLeftRadius: message.isUser ? 16 : 4,
                  padding: 14,
                }}
              >
                <Text style={{ color: colors.text.primary, fontSize: typography.sizes.base, lineHeight: 22 }}>{message.text}</Text>
              </LinearGradient>
              <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs, marginTop: spacing.xs, alignSelf: message.isUser ? 'flex-end' : 'flex-start' }}>{formatTime(message.timestamp)}</Text>
            </View>
          ))}
          
          {/* Loading indicator */}
          {isLoading && (
            <View style={{ alignSelf: 'flex-start', marginBottom: 12 }}>
              <View style={{ 
                backgroundColor: colors.background.tertiary, 
                paddingHorizontal: 20, 
                paddingVertical: 14, 
                borderRadius: 16,
                borderBottomLeftRadius: 4,
                ...orangeOutline,
                flexDirection: 'row',
                alignItems: 'center',
              }}>
                <ActivityIndicator size="small" color={colors.orange.DEFAULT} />
                <Text style={{ color: colors.text.muted, marginLeft: 10, fontSize: 14 }}>BRIX is thinking...</Text>
              </View>
            </View>
          )}
        </ScrollView>

        {/* Quick Responses */}
        <View style={{ paddingHorizontal: spacing.lg }}>
          <ScrollView horizontal showsHorizontalScrollIndicator={false}>
            <View style={{ flexDirection: 'row', gap: spacing.sm, paddingVertical: spacing.sm }}>
              {quickResponses.map((response, index) => (
                <TouchableOpacity
                  key={index}
                  onPress={() => sendMessage(response)}
                  style={{
                    backgroundColor: colors.background.tertiary,
                    paddingHorizontal: 14,
                    paddingVertical: 8,
                    borderRadius: 20,
                    ...orangeOutline,
                  }}
                >
                  <Text style={{ color: colors.text.primary, fontSize: 13 }}>{response}</Text>
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
              editable={!isLoading}
            />
          </View>
          <TouchableOpacity
            onPress={() => sendMessage(inputText)}
            disabled={!inputText.trim()}
            style={{
              backgroundColor: inputText.trim() ? colors.orange.DEFAULT : colors.background.elevated,
              width: 44,
              height: 44,
              borderRadius: 22,
              justifyContent: 'center',
              alignItems: 'center',
              marginLeft: 10,
            }}
          >
            <Ionicons name="send" size={20} color={colors.text.primary} />
          </TouchableOpacity>
        </View>
      </KeyboardAvoidingView>
    </View>
  );
}
