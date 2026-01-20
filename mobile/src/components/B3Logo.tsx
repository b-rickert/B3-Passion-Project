import React from 'react';
import { View, Text } from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';

interface B3LogoProps {
  size?: number;
}

export default function B3Logo({ size = 64 }: B3LogoProps) {
  const fontSize = size * 0.45;
  const borderRadius = size * 0.22;

  return (
    <LinearGradient
      colors={['#f97316', '#ea580c']}
      start={{ x: 0, y: 0 }}
      end={{ x: 1, y: 1 }}
      style={{
        width: size,
        height: size,
        borderRadius: borderRadius,
        justifyContent: 'center',
        alignItems: 'center',
      }}
    >
      <Text
        style={{
          color: '#ffffff',
          fontSize: fontSize,
          fontWeight: '900',
          letterSpacing: -1,
        }}
      >
        B3
      </Text>
    </LinearGradient>
  );
}
