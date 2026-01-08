import React from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { colors } from '../constants/theme';

interface HeaderProps {
  title: string;
  showBack?: boolean;
  onBack?: () => void;
  rightIcon?: keyof typeof Ionicons.glyphMap;
  onRightPress?: () => void;
}

export default function Header({ 
  title, 
  showBack = false, 
  onBack,
  rightIcon,
  onRightPress,
}: HeaderProps) {
  return (
    <View style={{
      flexDirection: 'row',
      alignItems: 'center',
      justifyContent: 'space-between',
      paddingHorizontal: 16,
      paddingVertical: 12,
      backgroundColor: colors.background.primary,
    }}>
      {showBack ? (
        <TouchableOpacity onPress={onBack} style={{ padding: 8 }}>
          <Ionicons name="arrow-back" size={24} color={colors.text.primary} />
        </TouchableOpacity>
      ) : (
        <View style={{ width: 40 }} />
      )}
      
      <Text style={{ 
        color: colors.text.primary, 
        fontSize: 18, 
        fontWeight: '600',
      }}>
        {title}
      </Text>
      
      {rightIcon ? (
        <TouchableOpacity onPress={onRightPress} style={{ padding: 8 }}>
          <Ionicons name={rightIcon} size={24} color={colors.text.primary} />
        </TouchableOpacity>
      ) : (
        <View style={{ width: 40 }} />
      )}
    </View>
  );
}