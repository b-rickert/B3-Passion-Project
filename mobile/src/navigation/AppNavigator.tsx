import React from 'react';
import { View } from 'react-native';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { LinearGradient } from 'expo-linear-gradient';
import { Home, Dumbbell, Calendar, MessageCircle, User } from 'lucide-react-native';
import { colors, gradients, radius } from '../constants/theme';
import HomeScreen from '../screens/HomeScreen';
import WorkoutsScreen from '../screens/WorkoutsScreen';
import WorkoutDetailScreen from '../screens/WorkoutDetailScreen';
import WorkoutSessionScreen from '../screens/WorkoutSessionScreen';
import ProgressScreen from '../screens/ProgressScreen';
import BrixScreen from '../screens/BrixScreen';
import ProfileScreen from '../screens/ProfileScreen';
import DailyLogScreen from '../screens/DailyLogScreen';
import SettingsScreen from '../screens/SettingsScreen';
import HelpSupportScreen from '../screens/HelpSupportScreen';
import LandingScreen from '../screens/LandingScreen';
import PersonalRecordsScreen from '../screens/PersonalRecordsScreen';

const Tab = createBottomTabNavigator();
const Stack = createNativeStackNavigator();

function TabNavigator() {
  return (
    <Tab.Navigator
      id="MainTabs"
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
    <Stack.Navigator screenOptions={{ headerShown: false }}>
      <Stack.Screen name="Landing" component={LandingScreen} />
      <Stack.Screen name="MainTabs" component={TabNavigator} />
      <Stack.Screen name="WorkoutDetail" component={WorkoutDetailScreen} />
      <Stack.Screen name="WorkoutSession" component={WorkoutSessionScreen} />
      <Stack.Screen name="DailyLog" component={DailyLogScreen} />
      <Stack.Screen name="Settings" component={SettingsScreen} />
      <Stack.Screen name="HelpSupport" component={HelpSupportScreen} />
      <Stack.Screen name="PersonalRecords" component={PersonalRecordsScreen} />
    </Stack.Navigator>
  );
}
