import React from 'react';
import { View, Text, TextInput, TextInputProps } from 'react-native';
import { colors } from '../constants/theme';

interface InputProps extends TextInputProps {
  label?: string;
  error?: string;
}

export default function Input({ label, error, ...props }: InputProps) {
  return (
    <View style={{ marginBottom: 16 }}>
      {label && (
        <Text style={{ 
          color: colors.text.secondary, 
          fontSize: 14, 
          marginBottom: 8,
          fontWeight: '500',
        }}>
          {label}
        </Text>
      )}
      <TextInput
        style={{
          backgroundColor: colors.background.tertiary,
          borderRadius: 8,
          padding: 12,
          color: colors.text.primary,
          fontSize: 16,
          borderWidth: error ? 1 : 0,
          borderColor: error ? colors.red.DEFAULT : 'transparent',
        }}
        placeholderTextColor={colors.text.muted}
        {...props}
      />
      {error && (
        <Text style={{ color: colors.red.DEFAULT, fontSize: 12, marginTop: 4 }}>
          {error}
        </Text>
      )}
    </View>
  );
}