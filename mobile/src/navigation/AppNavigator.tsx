import React from 'react';
import { View } from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { LinearGradient } from 'expo-linear-gradient';
import { Home, Dumbbell, Calendar, MessageCircle, User } from 'lucide-react-native';
import { colors, gradients, radius } from '../constants/theme';
import HomeScreen from '../screens/HomeScreen';
import WorkoutsScreen from '../screens/WorkoutsScreen';
import ProgressScreen from '../screens/ProgressScreen';
import BrixScreen from '../screens/BrixScreen';
import ProfileScreen from '../screens/ProfileScreen';

const Stack = createNativeStackNavigator();
const Tab = createBottomTabNavigator();

function TabNavigator() {
import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { Ionicons } from '@expo/vector-icons';

import HomeScreen from '../screens/HomeScreen';
import WorkoutsScreen from '../screens/WorkoutsScreen';
import ProgressScreen from '../screens/ProgressScreen';
import BrixScreen from '../screens/BrixScreen';
import ProfileScreen from '../screens/ProfileScreen';
import WorkoutDetailScreen from '../screens/WorkoutDetailScreen';
import DailyLogScreen from '../screens/DailyLogScreen';

const Stack = createNativeStackNavigator();
const Tab = createBottomTabNavigator();

function TabNavigator() {
  return (
    <Tab.Navigator
      screenOptions={({ route }) => ({
        headerShown: false,
        tabBarStyle: {
          backgroundColor: '#27272a',
          borderTopColor: '#f97316',
          borderTopWidth: 2,
          height: 80,
          paddingBottom: 10,
          paddingTop: 10,
        },
        tabBarActiveTintColor: '#f97316',
        tabBarInactiveTintColor: '#71717a',
        tabBarIcon: ({ focused, color }) => {
          let iconName: keyof typeof Ionicons.glyphMap = 'home';
          if (route.name === 'Home') iconName = focused ? 'home' : 'home-outline';
          else if (route.name === 'Workouts') iconName = focused ? 'barbell' : 'barbell-outline';
          else if (route.name === 'Progress') iconName = focused ? 'calendar' : 'calendar-outline';
          else if (route.name === 'Brix') iconName = focused ? 'chatbubble' : 'chatbubble-outline';
          else if (route.name === 'Profile') iconName = focused ? 'person' : 'person-outline';
          return <Ionicons name={iconName} size={24} color={color} />;
        },
      })}
    >
      <Tab.Screen name="Home" component={HomeScreen} />
      <Tab.Screen name="Workouts" component={WorkoutsScreen} />
      <Tab.Screen name="Progress" component={ProgressScreen} />
      <Tab.Screen name="Brix" component={BrixScreen} options={{ tabBarLabel: 'BRIX' }} />
      <Tab.Screen name="Profile" component={ProfileScreen} />
    </Tab.Navigator>
  );
}

export default function AppNavigator() {
  return (
    <Tab.Navigator
      screenOptions={({ route }) => ({
        headerShown: false,
        tabBarBackground: () => (
          <View style={{ 
            position: 'absolute', 
            top: 0, 
            left: 0, 
            right: 0, 
            bottom: 0,
            borderRadius: radius['2xl'],
            overflow: 'hidden',
          }}>
            {/* Orange gradient border */}
            <LinearGradient
              colors={gradients.fire}
              start={{ x: 0, y: 0 }}
              end={{ x: 1, y: 1 }}
              style={{ 
                flex: 1, 
                padding: 2,
                borderRadius: radius['2xl'],
              }}
            >
              {/* Dark inner fill */}
              <View style={{ 
                flex: 1, 
                backgroundColor: '#141416',
                borderRadius: radius['2xl'] - 2,
              }} />
            </LinearGradient>
          </View>
        ),
        tabBarStyle: {
          position: 'absolute',
          bottom: 24,
          left: 20,
          right: 20,
          height: 70,
          backgroundColor: 'transparent',
          borderRadius: radius['2xl'],
          borderTopWidth: 0,
          borderWidth: 0,
          paddingBottom: 0,
          paddingTop: 0,
          elevation: 0,
          shadowColor: colors.orange.DEFAULT,
          shadowOffset: { width: 0, height: 0 },
          shadowOpacity: 0.5,
          shadowRadius: 16,
        },
        tabBarActiveTintColor: colors.orange.DEFAULT,
        tabBarInactiveTintColor: '#6b6b6b',
        tabBarShowLabel: true,
        tabBarLabelStyle: { 
          fontSize: 10, 
          fontWeight: '600', 
          marginTop: -4, 
          marginBottom: 10 
        },
        tabBarIconStyle: { marginTop: 10 },
        tabBarIcon: ({ focused, color }) => {
          const size = focused ? 24 : 22;
          const strokeWidth = focused ? 2.5 : 2;
          
          switch (route.name) {
            case 'Home':
              return <Home size={size} color={color} strokeWidth={strokeWidth} />;
            case 'Workouts':
              return <Dumbbell size={size} color={color} strokeWidth={strokeWidth} />;
            case 'Progress':
              return <Calendar size={size} color={color} strokeWidth={strokeWidth} />;
            case 'Brix':
              return <MessageCircle size={size} color={color} strokeWidth={strokeWidth} />;
            case 'Profile':
              return <User size={size} color={color} strokeWidth={strokeWidth} />;
            default:
              return <Home size={size} color={color} />;
          }
        },
      })}
    >
      <Tab.Screen name="Home" component={HomeScreen} />
      <Tab.Screen name="Workouts" component={WorkoutsScreen} />
      <Tab.Screen name="Progress" component={ProgressScreen} />
      <Tab.Screen name="Brix" component={BrixScreen} options={{ tabBarLabel: 'BRIX' }} />
      <Tab.Screen name="Profile" component={ProfileScreen} />
    </Tab.Navigator>
  );
}

export default function AppNavigator() {
  return (
    <NavigationContainer>
      <Stack.Navigator screenOptions={{ headerShown: false }}>
        <Stack.Screen name="MainTabs" component={TabNavigator} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
