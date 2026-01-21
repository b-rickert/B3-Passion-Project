import React, { useEffect, useRef } from 'react';
import { View, Text, TouchableOpacity, Animated, Dimensions } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { LinearGradient } from 'expo-linear-gradient';
import { ChevronRight, Flame, Target, Zap } from 'lucide-react-native';
import { colors, gradients, shadows, radius, spacing, typography } from '../constants/theme';
import B3Logo from '../components/B3Logo';

const { width: SCREEN_WIDTH, height: SCREEN_HEIGHT } = Dimensions.get('window');

export default function LandingScreen() {
  const navigation = useNavigation();

  // Animations
  const logoScale = useRef(new Animated.Value(0)).current;
  const logoOpacity = useRef(new Animated.Value(0)).current;
  const taglineOpacity = useRef(new Animated.Value(0)).current;
  const taglineTranslate = useRef(new Animated.Value(30)).current;
  const featuresOpacity = useRef(new Animated.Value(0)).current;
  const featuresTranslate = useRef(new Animated.Value(40)).current;
  const buttonOpacity = useRef(new Animated.Value(0)).current;
  const buttonTranslate = useRef(new Animated.Value(50)).current;
  const floatingBrick1 = useRef(new Animated.Value(0)).current;
  const floatingBrick2 = useRef(new Animated.Value(0)).current;
  const floatingBrick3 = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    // Staggered entrance animations
    Animated.sequence([
      // Logo entrance
      Animated.parallel([
        Animated.spring(logoScale, {
          toValue: 1,
          tension: 50,
          friction: 7,
          useNativeDriver: true,
        }),
        Animated.timing(logoOpacity, {
          toValue: 1,
          duration: 600,
          useNativeDriver: true,
        }),
      ]),
      // Tagline entrance
      Animated.parallel([
        Animated.timing(taglineOpacity, {
          toValue: 1,
          duration: 500,
          useNativeDriver: true,
        }),
        Animated.spring(taglineTranslate, {
          toValue: 0,
          tension: 50,
          friction: 8,
          useNativeDriver: true,
        }),
      ]),
      // Features entrance
      Animated.parallel([
        Animated.timing(featuresOpacity, {
          toValue: 1,
          duration: 500,
          useNativeDriver: true,
        }),
        Animated.spring(featuresTranslate, {
          toValue: 0,
          tension: 50,
          friction: 8,
          useNativeDriver: true,
        }),
      ]),
      // Button entrance
      Animated.parallel([
        Animated.timing(buttonOpacity, {
          toValue: 1,
          duration: 500,
          useNativeDriver: true,
        }),
        Animated.spring(buttonTranslate, {
          toValue: 0,
          tension: 50,
          friction: 8,
          useNativeDriver: true,
        }),
      ]),
    ]).start();

    // Floating brick animations (continuous)
    const createFloatingAnimation = (animValue: Animated.Value, duration: number) => {
      return Animated.loop(
        Animated.sequence([
          Animated.timing(animValue, {
            toValue: 1,
            duration: duration,
            useNativeDriver: true,
          }),
          Animated.timing(animValue, {
            toValue: 0,
            duration: duration,
            useNativeDriver: true,
          }),
        ])
      );
    };

    createFloatingAnimation(floatingBrick1, 3000).start();
    createFloatingAnimation(floatingBrick2, 2500).start();
    createFloatingAnimation(floatingBrick3, 3500).start();
  }, []);

  const handleGetStarted = () => {
    navigation.reset({
      index: 0,
      routes: [{ name: 'MainTabs' as never }],
    });
  };

  const features = [
    { icon: Target, text: 'Build your foundation', color: colors.orange.DEFAULT },
    { icon: Flame, text: 'Track your streaks', color: colors.amber.DEFAULT },
    { icon: Zap, text: 'AI-powered coaching', color: colors.blue.DEFAULT },
  ];

  return (
    <View style={{ flex: 1, backgroundColor: colors.background.end }}>
      {/* Background gradients */}
      <View style={{ position: 'absolute', top: 0, left: 0, right: 0, bottom: 0 }}>
        <LinearGradient
          colors={[colors.background.start, colors.background.mid, colors.background.end]}
          style={{ position: 'absolute', top: 0, left: 0, right: 0, bottom: 0 }}
        />
        {/* Decorative gradient orbs */}
        <LinearGradient
          colors={['rgba(249, 115, 22, 0.3)', 'transparent']}
          style={{
            position: 'absolute',
            top: -150,
            right: -100,
            width: 400,
            height: 400,
            borderRadius: 200,
          }}
        />
        <LinearGradient
          colors={['rgba(59, 130, 246, 0.2)', 'transparent']}
          style={{
            position: 'absolute',
            bottom: -100,
            left: -150,
            width: 400,
            height: 400,
            borderRadius: 200,
          }}
        />
      </View>

      {/* Floating decorative bricks */}
      <Animated.View
        style={{
          position: 'absolute',
          top: SCREEN_HEIGHT * 0.15,
          left: 30,
          opacity: 0.3,
          transform: [{
            translateY: floatingBrick1.interpolate({
              inputRange: [0, 1],
              outputRange: [0, -20],
            }),
          }],
        }}
      >
        <View style={{
          width: 40,
          height: 24,
          backgroundColor: colors.orange.DEFAULT,
          borderRadius: 6,
          ...shadows.glow,
        }} />
      </Animated.View>

      <Animated.View
        style={{
          position: 'absolute',
          top: SCREEN_HEIGHT * 0.25,
          right: 40,
          opacity: 0.25,
          transform: [{
            translateY: floatingBrick2.interpolate({
              inputRange: [0, 1],
              outputRange: [0, -15],
            }),
          }],
        }}
      >
        <View style={{
          width: 50,
          height: 30,
          backgroundColor: colors.amber.DEFAULT,
          borderRadius: 8,
        }} />
      </Animated.View>

      <Animated.View
        style={{
          position: 'absolute',
          top: SCREEN_HEIGHT * 0.55,
          right: 60,
          opacity: 0.2,
          transform: [{
            translateY: floatingBrick3.interpolate({
              inputRange: [0, 1],
              outputRange: [0, -25],
            }),
          }],
        }}
      >
        <View style={{
          width: 35,
          height: 20,
          backgroundColor: colors.blue.DEFAULT,
          borderRadius: 5,
        }} />
      </Animated.View>

      {/* Main content */}
      <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center', paddingHorizontal: spacing['2xl'] }}>
        {/* Logo */}
        <Animated.View
          style={{
            opacity: logoOpacity,
            transform: [{ scale: logoScale }],
            marginBottom: spacing['2xl'],
          }}
        >
          <View style={{ ...shadows.glow }}>
            <B3Logo size={120} />
          </View>
        </Animated.View>

        {/* Tagline */}
        <Animated.View
          style={{
            opacity: taglineOpacity,
            transform: [{ translateY: taglineTranslate }],
            alignItems: 'center',
          }}
        >
          <Text style={{
            color: colors.text.primary,
            fontSize: typography.sizes['3xl'],
            fontWeight: typography.weights.black,
            textAlign: 'center',
            letterSpacing: -1,
          }}>
            Build Yourself.
          </Text>
          <Text style={{
            color: colors.orange.DEFAULT,
            fontSize: typography.sizes['3xl'],
            fontWeight: typography.weights.black,
            textAlign: 'center',
            letterSpacing: -1,
            marginTop: -4,
          }}>
            Brick by Brick.
          </Text>
          <Text style={{
            color: colors.text.secondary,
            fontSize: typography.sizes.base,
            textAlign: 'center',
            marginTop: spacing.lg,
            lineHeight: 22,
            maxWidth: 280,
          }}>
            Transform your fitness journey with adaptive AI coaching and visual progress tracking.
          </Text>
        </Animated.View>

        {/* Features */}
        <Animated.View
          style={{
            opacity: featuresOpacity,
            transform: [{ translateY: featuresTranslate }],
            marginTop: spacing['3xl'],
            width: '100%',
          }}
        >
          {features.map((feature, index) => (
            <View
              key={index}
              style={{
                flexDirection: 'row',
                alignItems: 'center',
                backgroundColor: colors.background.glass,
                borderRadius: radius.lg,
                padding: spacing.md,
                marginBottom: spacing.sm,
                borderWidth: 1,
                borderColor: colors.background.glassBorder,
              }}
            >
              <View style={{
                backgroundColor: feature.color + '20',
                width: 40,
                height: 40,
                borderRadius: radius.md,
                justifyContent: 'center',
                alignItems: 'center',
                marginRight: spacing.md,
              }}>
                <feature.icon size={20} color={feature.color} />
              </View>
              <Text style={{
                color: colors.text.primary,
                fontSize: typography.sizes.base,
                fontWeight: typography.weights.semibold,
              }}>
                {feature.text}
              </Text>
            </View>
          ))}
        </Animated.View>
      </View>

      {/* Get Started Button */}
      <Animated.View
        style={{
          opacity: buttonOpacity,
          transform: [{ translateY: buttonTranslate }],
          paddingHorizontal: spacing['2xl'],
          paddingBottom: spacing['4xl'],
        }}
      >
        <TouchableOpacity activeOpacity={0.9} onPress={handleGetStarted}>
          <LinearGradient
            colors={gradients.fire}
            start={{ x: 0, y: 0 }}
            end={{ x: 1, y: 0 }}
            style={{
              borderRadius: radius.xl,
              paddingVertical: spacing.lg + 4,
              flexDirection: 'row',
              alignItems: 'center',
              justifyContent: 'center',
              ...shadows.glow,
            }}
          >
            <Text style={{
              color: '#fff',
              fontSize: typography.sizes.lg,
              fontWeight: typography.weights.bold,
            }}>
              Get Started
            </Text>
            <ChevronRight size={24} color="#fff" style={{ marginLeft: spacing.sm }} />
          </LinearGradient>
        </TouchableOpacity>

        <Text style={{
          color: colors.text.muted,
          fontSize: typography.sizes.sm,
          textAlign: 'center',
          marginTop: spacing.lg,
        }}>
          Your fitness foundation awaits
        </Text>
      </Animated.View>
    </View>
  );
}
