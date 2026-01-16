import React from 'react';
import { View, Text } from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { colors, gradients, shadows } from '../constants/theme';

interface B3LogoProps {
  size?: number;
}

export default function B3Logo({ size = 48 }: B3LogoProps) {
  return (
    <View style={{
      width: size,
      height: size,
      borderRadius: size * 0.24,
      overflow: 'hidden',
      ...shadows.glow,
    }}>
      <LinearGradient
        colors={gradients.fire}
        start={{ x: 0, y: 0 }}
        end={{ x: 1, y: 1 }}
        style={{
          flex: 1,
          justifyContent: 'center',
          alignItems: 'center',
          padding: 2,
        }}
      >
        {/* Inner dark container */}
        <View style={{
          flex: 1,
          width: '100%',
          backgroundColor: colors.background.card,
          borderRadius: size * 0.2,
          justifyContent: 'center',
          alignItems: 'center',
          flexDirection: 'row',
        }}>
          {/* B */}
          <Text style={{
            color: '#ffffff',
            fontSize: size * 0.38,
            fontWeight: '900',
            letterSpacing: -1,
          }}>
            B
          </Text>
          {/* 3 in gradient pill */}
          <LinearGradient
            colors={gradients.fire}
            start={{ x: 0, y: 0 }}
            end={{ x: 1, y: 0 }}
            style={{
              paddingHorizontal: size * 0.08,
              paddingVertical: size * 0.04,
              borderRadius: size * 0.1,
              marginLeft: 2,
            }}
          >
            <Text style={{
              color: '#ffffff',
              fontSize: size * 0.28,
              fontWeight: '900',
              letterSpacing: -0.5,
            }}>
              3
            </Text>
          </LinearGradient>
        </View>
      </LinearGradient>
    </View>
  );
}
