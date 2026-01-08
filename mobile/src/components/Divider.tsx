import React from 'react';
import { View } from 'react-native';
import { colors } from '../constants/theme';

interface DividerProps {
  marginVertical?: number;
}

export default function Divider({ marginVertical = 16 }: DividerProps) {
  return (
    <View style={{
      height: 1,
      backgroundColor: colors.background.elevated,
      marginVertical,
    }} />
  );
}