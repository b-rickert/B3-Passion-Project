import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { SafeAreaProvider, SafeAreaView } from 'react-native-safe-area-context';
import { NavigationContainer } from '@react-navigation/native';
import AppNavigator from './src/navigation/AppNavigator';

// Test component to verify rendering works
function TestScreen() {
  return (
    <View style={styles.container}>
      <Text style={styles.text}>If you see this, React Native is working!</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#09090b',
    justifyContent: 'center',
    alignItems: 'center',
  },
  text: {
    color: '#f97316',
    fontSize: 20,
    fontWeight: 'bold',
    textAlign: 'center',
    padding: 20,
  },
});

export default function App() {
  // Uncomment TestScreen and comment out the real app to debug:
  // return (
  //   <SafeAreaProvider>
  //     <SafeAreaView style={{ flex: 1, backgroundColor: '#09090b' }}>
  //       <TestScreen />
  //     </SafeAreaView>
  //   </SafeAreaProvider>
  // );

  return (
    <SafeAreaProvider>
      <NavigationContainer>
        <AppNavigator />
      </NavigationContainer>
    </SafeAreaProvider>
  );
}
