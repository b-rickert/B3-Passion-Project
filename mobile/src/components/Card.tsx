import React, { ReactNode } from 'react';
import { View } from 'react-native';
import { colors } from '../constants/theme';

interface CardProps {
  children: ReactNode;
  padding?: number;
  marginBottom?: number;
  borderColor?: string;
}

export default function Card({ 
  children, 
  padding = 16,
  marginBottom = 0,
  borderColor,
}: CardProps) {
  return (
    <View style={{
      backgroundColor: colors.background.secondary,
      borderRadius: 12,
      padding,
      marginBottom,
      borderWidth: borderColor ? 1 : 0,
      borderColor: borderColor || 'transparent',
    }}>
      {children}
    </View>
  );
}