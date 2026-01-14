import React, { useState, useRef, useEffect } from 'react';
import { View, Text, ScrollView, TouchableOpacity, TextInput, KeyboardAvoidingView, Platform, ActivityIndicator } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { LinearGradient } from 'expo-linear-gradient';
import { useNavigation } from '@react-navigation/native';
import { colors } from '../constants/theme';
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/v1';

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
    {
      id: '1',
      text: "Hey there! 👋 I'm BRIX, your AI fitness coach. I'm here to help you build your foundation, one brick at a time. How are you feeling today?",
      isUser: false,
      timestamp: new Date(),
    },
  ]);

  const quickResponses = [
    "💪 Ready to crush it!",
    "😴 Feeling tired",
    "🎯 Need motivation",
    "❓ What workout today?",
  ];

  const sendMessage = async (text: string) => {
    if (!text.trim() || isLoading) return;

    const userMessage: Message = {
      id: Date.now().toString(),
      text: text.trim(),
      isUser: true,
      timestamp: new Date(),
    };

    setMessages(prev => [...prev, userMessage]);
    setInputText('');
    setIsLoading(true);

    try {
      const response = await axios.post(`${API_BASE_URL}/brix/chat`, {
        profileId: 1,
        message: text.trim(),
      });

      const brixResponse: Message = {
        id: (Date.now() + 1).toString(),
        text: response.data.message,
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

  useEffect(() => {
    scrollViewRef.current?.scrollToEnd({ animated: true });
  }, [messages]);

  const formatTime = (date: Date) => {
    return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
  };

  const handleRecommendationPress = () => {
    if (recommendation) {
      (navigation as any).navigate('WorkoutDetail', { workoutId: recommendation.workoutId });
    }
  };

  return (
    <View style={{ flex: 1, backgroundColor: colors.background.secondary }}>
      <KeyboardAvoidingView
        behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
        style={{ flex: 1 }}
        keyboardVerticalOffset={0}
      >
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
                borderRadius: 12,
                justifyContent: 'center',
                alignItems: 'center',
                marginRight: 12,
              }}
            >
              <Text style={{ fontSize: 24 }}>🧱</Text>
            </View>
            <View style={{ flex: 1 }}>
              <Text style={{ color: '#fff', fontSize: 20, fontWeight: '800' }}>BRIX</Text>
              <Text style={{ color: 'rgba(255,255,255,0.8)', fontSize: 13 }}>Your AI Fitness Coach</Text>
            </View>
            <View style={{ backgroundColor: '#22c55e', paddingHorizontal: 10, paddingVertical: 4, borderRadius: 12 }}>
              <Text style={{ color: '#fff', fontSize: 11, fontWeight: '600' }}>● Online</Text>
            </View>
          </View>
        </LinearGradient>

        {/* Recommendation Card */}
        {recommendation && (
          <TouchableOpacity onPress={handleRecommendationPress} style={{ paddingHorizontal: 16, paddingTop: 12 }}>
            <View style={{ 
              backgroundColor: colors.orange.DEFAULT + '20', 
              borderRadius: 12, 
              padding: 12,
              borderWidth: 1,
              borderColor: colors.orange.DEFAULT,
              flexDirection: 'row',
              alignItems: 'center',
            }}>
              <View style={{ flex: 1 }}>
                <Text style={{ color: colors.orange.DEFAULT, fontSize: 11, fontWeight: '600' }}>BRIX RECOMMENDS</Text>
                <Text style={{ color: colors.text.primary, fontSize: 15, fontWeight: '700', marginTop: 2 }}>{recommendation.name}</Text>
                <Text style={{ color: colors.text.secondary, fontSize: 12, marginTop: 2 }}>{recommendation.reason}</Text>
              </View>
              <View style={{ backgroundColor: colors.orange.DEFAULT, paddingHorizontal: 12, paddingVertical: 8, borderRadius: 8 }}>
                <Text style={{ color: '#fff', fontSize: 12, fontWeight: '600' }}>View →</Text>
              </View>
            </View>
          </TouchableOpacity>
        )}

        {/* Messages */}
        <ScrollView
          ref={scrollViewRef}
          style={{ flex: 1, paddingHorizontal: 16 }}
          contentContainerStyle={{ paddingVertical: 16 }}
          onContentSizeChange={() => scrollViewRef.current?.scrollToEnd({ animated: true })}
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
              <View
                style={{
                  backgroundColor: message.isUser ? colors.orange.DEFAULT : colors.background.tertiary,
                  paddingHorizontal: 16,
                  paddingVertical: 12,
                  borderRadius: 16,
                  borderBottomRightRadius: message.isUser ? 4 : 16,
                  borderBottomLeftRadius: message.isUser ? 16 : 4,
                  ...(!message.isUser && orangeOutline),
                }}
              >
                <Text style={{ color: colors.text.primary, fontSize: 15, lineHeight: 22 }}>
                  {message.text}
                </Text>
              </View>
              <Text
                style={{
                  color: colors.text.muted,
                  fontSize: 11,
                  marginTop: 4,
                  alignSelf: message.isUser ? 'flex-end' : 'flex-start',
                }}
              >
                {formatTime(message.timestamp)}
              </Text>
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
        <View style={{ paddingHorizontal: 16 }}>
          <ScrollView horizontal showsHorizontalScrollIndicator={false}>
            <View style={{ flexDirection: 'row', gap: 8, paddingVertical: 8 }}>
              {quickResponses.map((response, index) => (
                <TouchableOpacity
                  key={index}
                  onPress={() => sendMessage(response)}
                  disabled={isLoading}
                  style={{
                    backgroundColor: isLoading ? colors.background.elevated : colors.background.tertiary,
                    paddingHorizontal: 14,
                    paddingVertical: 8,
                    borderRadius: 20,
                    ...orangeOutline,
                    opacity: isLoading ? 0.5 : 1,
                  }}
                >
                  <Text style={{ color: colors.text.primary, fontSize: 13 }}>{response}</Text>
                </TouchableOpacity>
              ))}
            </View>
          </ScrollView>
        </View>

        {/* Input Bar */}
        <View
          style={{
            flexDirection: 'row',
            alignItems: 'center',
            paddingHorizontal: 16,
            paddingVertical: 12,
            paddingBottom: 32,
            backgroundColor: colors.background.primary,
            borderTopWidth: 1,
            borderTopColor: colors.background.elevated,
          }}
        >
          <View
            style={{
              flex: 1,
              backgroundColor: colors.background.tertiary,
              borderRadius: 24,
              paddingHorizontal: 16,
              paddingVertical: 10,
              flexDirection: 'row',
              alignItems: 'center',
              ...orangeOutline,
            }}
          >
            <TextInput
              value={inputText}
              onChangeText={setInputText}
              placeholder="Message BRIX..."
              placeholderTextColor={colors.text.muted}
              style={{
                flex: 1,
                color: colors.text.primary,
                fontSize: 15,
              }}
              onSubmitEditing={() => sendMessage(inputText)}
              returnKeyType="send"
              editable={!isLoading}
            />
          </View>
          <TouchableOpacity
            onPress={() => sendMessage(inputText)}
            disabled={!inputText.trim() || isLoading}
            style={{
              backgroundColor: inputText.trim() && !isLoading ? colors.orange.DEFAULT : colors.background.elevated,
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
