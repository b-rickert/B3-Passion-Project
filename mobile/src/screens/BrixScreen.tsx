import React, { useState, useRef, useEffect } from 'react';
import { View, Text, ScrollView, TouchableOpacity, TextInput, KeyboardAvoidingView, Platform, ActivityIndicator } from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { Send, MessageCircle, Zap, Smile, Battery, Dumbbell } from 'lucide-react-native';
import { colors, gradients, shadows, spacing, typography, radius } from '../constants/theme';
import { B3Logo } from '../components';
import { brixApi } from '../services/api';
import BrickBackground from '../components/BrickBackground';

interface Message {
  id: string;
  text: string;
  isUser: boolean;
  timestamp: Date;
}

export default function BrixScreen() {
  const scrollViewRef = useRef<ScrollView>(null);
  const [inputText, setInputText] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [messages, setMessages] = useState<Message[]>([
    { id: '1', text: "Hey there! I'm BRIX, your AI fitness coach. I'm here to help you build your foundation, one brick at a time. How are you feeling today?", isUser: false, timestamp: new Date() },
  ]);

  const quickResponses = [
    { text: "Ready to crush it!", icon: Zap },
    { text: "Feeling tired", icon: Battery },
    { text: "Need motivation", icon: Smile },
    { text: "What workout today?", icon: Dumbbell },
  ];

  const sendMessage = async (text: string) => {
    if (!text.trim()) return;

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
      // Call the actual backend API
      const response = await brixApi.chat(1, text.trim());

      const brixResponse: Message = {
        id: (Date.now() + 1).toString(),
        text: response.message,
        isUser: false,
        timestamp: new Date(response.timestamp),
      };
      setMessages(prev => [...prev, brixResponse]);
    } catch (error) {
      console.error('Error calling BRIX API:', error);
      // Fallback message if API fails
      const errorResponse: Message = {
        id: (Date.now() + 1).toString(),
        text: "I'm having trouble connecting right now. Make sure the backend server is running and Ollama is set up. Let's try again in a moment!",
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
    <BrickBackground>
      <KeyboardAvoidingView behavior={Platform.OS === 'ios' ? 'padding' : 'height'} style={{ flex: 1 }} keyboardVerticalOffset={0}>
        {/* Header */}
        <View style={{ paddingHorizontal: spacing.xl, paddingTop: 70 }}>
          <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'flex-start' }}>
            <View style={{ flexDirection: 'row', alignItems: 'center' }}>
              <LinearGradient colors={gradients.fire} style={{ width: 56, height: 56, borderRadius: radius.xl, justifyContent: 'center', alignItems: 'center', marginRight: spacing.lg, ...shadows.glow }}>
                <MessageCircle size={28} color="#fff" />
              </LinearGradient>
              <View>
                <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                  <Text style={{ color: colors.text.primary, fontSize: typography.sizes['3xl'], fontWeight: typography.weights.black }}>BRIX</Text>
                  <View style={{ backgroundColor: colors.success.DEFAULT + '30', paddingHorizontal: spacing.sm, paddingVertical: 2, borderRadius: radius.sm, marginLeft: spacing.sm }}>
                    <Text style={{ color: colors.success.DEFAULT, fontSize: 9, fontWeight: typography.weights.bold }}>AI</Text>
                  </View>
                </View>
                <Text style={{ color: colors.text.secondary, fontSize: typography.sizes.sm }}>Powered by Llama</Text>
              </View>
            </View>
            <B3Logo size={48} />
          </View>
        </View>

        {/* Messages */}
        <ScrollView
          ref={scrollViewRef}
          style={{ flex: 1, marginTop: spacing.xl }}
          contentContainerStyle={{ paddingHorizontal: spacing.xl, paddingBottom: 20 }}
          showsVerticalScrollIndicator={false}
        >
          {messages.map((message) => (
            <View
              key={message.id}
              style={{
                alignSelf: message.isUser ? 'flex-end' : 'flex-start',
                maxWidth: '85%',
                marginBottom: spacing.md,
              }}
            >
              {!message.isUser && (
                <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: spacing.xs }}>
                  <LinearGradient colors={gradients.fire} style={{ width: 24, height: 24, borderRadius: 12, justifyContent: 'center', alignItems: 'center', marginRight: spacing.xs }}>
                    <MessageCircle size={12} color="#fff" />
                  </LinearGradient>
                  <Text style={{ color: colors.orange.DEFAULT, fontSize: typography.sizes.sm, fontWeight: typography.weights.semibold }}>BRIX</Text>
                </View>
              )}
              <View
                style={{
                  backgroundColor: message.isUser ? colors.blue.DEFAULT : colors.background.card,
                  borderRadius: radius.xl,
                  borderTopRightRadius: message.isUser ? radius.sm : radius.xl,
                  borderTopLeftRadius: message.isUser ? radius.xl : radius.sm,
                  padding: spacing.lg,
                  borderWidth: message.isUser ? 0 : 1,
                  borderColor: colors.background.glassBorder,
                  ...shadows.card,
                }}
              >
                <Text style={{ color: colors.text.primary, fontSize: typography.sizes.base, lineHeight: 22 }}>{message.text}</Text>
              </View>
              <Text style={{ color: colors.text.muted, fontSize: typography.sizes.xs, marginTop: spacing.xs, alignSelf: message.isUser ? 'flex-end' : 'flex-start' }}>{formatTime(message.timestamp)}</Text>
            </View>
          ))}

          {/* Loading indicator */}
          {isLoading && (
            <View style={{ alignSelf: 'flex-start', marginBottom: spacing.md }}>
              <View style={{
                backgroundColor: colors.background.card,
                paddingHorizontal: spacing.xl,
                paddingVertical: spacing.lg,
                borderRadius: radius.xl,
                borderTopLeftRadius: radius.sm,
                borderWidth: 1,
                borderColor: colors.background.glassBorder,
                flexDirection: 'row',
                alignItems: 'center',
              }}>
                <ActivityIndicator size="small" color={colors.orange.DEFAULT} />
                <Text style={{ color: colors.text.muted, marginLeft: spacing.md, fontSize: typography.sizes.sm }}>BRIX is thinking...</Text>
              </View>
            </View>
          )}
        </ScrollView>

        {/* Quick Responses */}
        <View style={{ paddingHorizontal: spacing.xl, paddingBottom: spacing.md }}>
          <ScrollView horizontal showsHorizontalScrollIndicator={false}>
            <View style={{ flexDirection: 'row', gap: spacing.sm }}>
              {quickResponses.map((response, index) => (
                <TouchableOpacity
                  key={index}
                  onPress={() => sendMessage(response.text)}
                  disabled={isLoading}
                  style={{
                    backgroundColor: colors.background.card,
                    paddingHorizontal: spacing.lg,
                    paddingVertical: spacing.sm,
                    borderRadius: radius.full,
                    borderWidth: 1,
                    borderColor: colors.background.glassBorder,
                    flexDirection: 'row',
                    alignItems: 'center',
                    opacity: isLoading ? 0.5 : 1,
                  }}
                >
                  <response.icon size={14} color={colors.orange.DEFAULT} />
                  <Text style={{ color: colors.text.primary, fontSize: typography.sizes.sm, marginLeft: spacing.xs }}>{response.text}</Text>
                </TouchableOpacity>
              ))}
            </View>
          </ScrollView>
        </View>

        {/* Input Bar */}
        <View style={{
          flexDirection: 'row',
          alignItems: 'center',
          paddingHorizontal: spacing.xl,
          paddingTop: spacing.md,
          paddingBottom: 110,
          backgroundColor: colors.background.card,
          borderTopWidth: 1,
          borderTopColor: colors.background.glassBorder
        }}>
          <View style={{
            flex: 1,
            backgroundColor: colors.background.glass,
            borderRadius: radius.full,
            paddingHorizontal: spacing.xl,
            paddingVertical: spacing.md,
            flexDirection: 'row',
            alignItems: 'center',
            borderWidth: 1,
            borderColor: colors.background.glassBorder
          }}>
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
            disabled={!inputText.trim() || isLoading}
            style={{ marginLeft: spacing.md }}
          >
            <LinearGradient
              colors={inputText.trim() && !isLoading ? gradients.fire : [colors.background.elevated, colors.background.elevated]}
              style={{
                width: 48,
                height: 48,
                borderRadius: 24,
                justifyContent: 'center',
                alignItems: 'center',
                ...((inputText.trim() && !isLoading) ? shadows.glow : {}),
              }}
            >
              <Send size={20} color={inputText.trim() && !isLoading ? '#fff' : colors.text.muted} />
            </LinearGradient>
          </TouchableOpacity>
        </View>
      </KeyboardAvoidingView>
    </BrickBackground>
  );
}
