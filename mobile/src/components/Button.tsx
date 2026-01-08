import React from 'react';
import { TouchableOpacity, Text, ActivityIndicator } from 'react-native';
import { colors } from '../constants/theme';

type ButtonVariant = 'primary' | 'secondary' | 'outline';

interface ButtonProps {
  title: string;
  onPress: () => void;
  variant?: ButtonVariant;
  disabled?: boolean;
  loading?: boolean;
  fullWidth?: boolean;
}

export default function Button({ 
  title, 
  onPress, 
  variant = 'primary',
  disabled = false,
  loading = false,
  fullWidth = true,
}: ButtonProps) {
  const getBackgroundColor = () => {
    if (disabled) return colors.background.tertiary;
    switch (variant) {
      case 'primary': return colors.blue.DEFAULT;
      case 'secondary': return colors.orange.DEFAULT;
      case 'outline': return 'transparent';
    }
  };

  const getBorderColor = () => {
    if (variant === 'outline') return colors.text.muted;
    return 'transparent';
  };

  return (
    <TouchableOpacity
      onPress={onPress}
      disabled={disabled || loading}
      style={{
        backgroundColor: getBackgroundColor(),
        borderWidth: variant === 'outline' ? 1 : 0,
        borderColor: getBorderColor(),
        paddingVertical: 16,
        paddingHorizontal: 24,
        borderRadius: 12,
        width: fullWidth ? '100%' : 'auto',
        opacity: disabled ? 0.5 : 1,
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
      }}
      activeOpacity={0.8}
    >
      {loading ? (
        <ActivityIndicator color={colors.text.primary} />
      ) : (
        <Text style={{ 
          color: variant === 'outline' ? colors.text.primary : colors.text.primary,
          fontSize: 16,
          fontWeight: '600',
        }}>
          {title}
        </Text>
      )}
    </TouchableOpacity>
  );
}