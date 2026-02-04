import React, { useMemo } from 'react';
import { View, Dimensions, StyleSheet } from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { colors } from '../constants/theme';

/**
 * KEY DESIGN: Procedurally generated 3D brick wall background.
 *
 * Key techniques:
 * 1. useMemo() - Brick positions calculated once and cached (no re-render flicker)
 * 2. Deterministic pseudo-random - Formula (row*17 + col*31) % 100 creates
 *    organic variation but renders identically every time
 * 3. 3D effect via edge overlays - Each brick has highlight (top/left) and
 *    shadow (bottom/right) edges creating beveled appearance
 * 4. Three shade levels - Adds depth without actual 3D rendering
 * 5. Gradient accent glows - Subtle orange/blue gradients add warmth
 *
 * This creates the signature B3 visual without images or complex assets.
 */

const { width: SCREEN_WIDTH, height: SCREEN_HEIGHT } = Dimensions.get('window');

// Brick dimensions
const BRICK_WIDTH = 80;
const BRICK_HEIGHT = 32;
const MORTAR_GAP = 3;
const BRICK_RADIUS = 4;

interface BrickProps {
  x: number;
  y: number;
  opacity: number;
  shade: number; // 0-2 for different depth levels
}

const Brick: React.FC<BrickProps> = ({ x, y, opacity, shade }) => {
  // Different shades for 3D depth effect - high contrast for visibility
  const shadeColors = [
    { base: '#2a2a30', highlight: '#4a4a55', shadow: '#0a0a0c' },
    { base: '#323238', highlight: '#55555f', shadow: '#0c0c0e' },
    { base: '#252528', highlight: '#45454f', shadow: '#08080a' },
  ];

  const brickColor = shadeColors[shade] || shadeColors[0];

  return (
    <View
      style={[
        styles.brick,
        {
          left: x,
          top: y,
          width: BRICK_WIDTH,
          height: BRICK_HEIGHT,
          opacity,
        },
      ]}
    >
      {/* Main brick body */}
      <View style={[styles.brickBody, { backgroundColor: brickColor.base }]}>
        {/* Top highlight edge for 3D effect */}
        <View style={[styles.brickHighlight, { backgroundColor: brickColor.highlight }]} />
        {/* Left highlight edge */}
        <View style={[styles.brickHighlightLeft, { backgroundColor: brickColor.highlight }]} />
        {/* Bottom shadow edge */}
        <View style={[styles.brickShadow, { backgroundColor: brickColor.shadow }]} />
        {/* Right shadow edge */}
        <View style={[styles.brickShadowRight, { backgroundColor: brickColor.shadow }]} />
      </View>
    </View>
  );
};

interface BrickBackgroundProps {
  children?: React.ReactNode;
  showGradientOverlay?: boolean;
  intensity?: 'subtle' | 'medium' | 'strong';
}

export default function BrickBackground({
  children,
  showGradientOverlay = true,
  intensity = 'subtle'
}: BrickBackgroundProps) {

  const opacityMultiplier = {
    subtle: 1.0,
    medium: 1.0,
    strong: 1.0,
  }[intensity];

  // Generate brick positions
  const bricks = useMemo(() => {
    const result: BrickProps[] = [];
    const rows = Math.ceil(SCREEN_HEIGHT / (BRICK_HEIGHT + MORTAR_GAP)) + 2;
    const cols = Math.ceil(SCREEN_WIDTH / (BRICK_WIDTH + MORTAR_GAP)) + 2;

    for (let row = 0; row < rows; row++) {
      const isOffset = row % 2 === 1;
      const offsetX = isOffset ? -(BRICK_WIDTH / 2) : 0;

      for (let col = 0; col < cols + 1; col++) {
        const x = col * (BRICK_WIDTH + MORTAR_GAP) + offsetX;
        const y = row * (BRICK_HEIGHT + MORTAR_GAP);

        // Deterministic "random" for organic look - same seed = same visual every render
        // Using primes (17, 31) creates good distribution without actual randomness
        const randomSeed = (row * 17 + col * 31) % 100;
        const opacity = (0.75 + (randomSeed / 400)) * opacityMultiplier;
        const shade = randomSeed % 3;  // 0, 1, or 2 for three depth levels

        result.push({ x, y, opacity, shade });
      }
    }

    return result;
  }, [opacityMultiplier]);

  return (
    <View style={styles.container}>
      {/* Base dark background */}
      <View style={styles.baseBackground} />

      {/* Brick pattern layer */}
      <View style={styles.brickLayer}>
        {bricks.map((brick, index) => (
          <Brick key={index} {...brick} />
        ))}
      </View>

      {/* Gradient overlays for depth and color */}
      {showGradientOverlay && (
        <>
          {/* Orange accent glow (top right) */}
          <LinearGradient
            colors={['rgba(249, 115, 22, 0.15)', 'transparent']}
            style={styles.orangeGlow}
          />

          {/* Blue accent glow (bottom left) */}
          <LinearGradient
            colors={['rgba(59, 130, 246, 0.08)', 'transparent']}
            style={styles.blueGlow}
          />
        </>
      )}

      {/* Content */}
      <View style={styles.content}>{children}</View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    position: 'relative',
    backgroundColor: colors.background.start,
  },
  baseBackground: {
    ...StyleSheet.absoluteFillObject,
    backgroundColor: colors.background.start,
  },
  brickLayer: {
    ...StyleSheet.absoluteFillObject,
    overflow: 'hidden',
  },
  brick: {
    position: 'absolute',
    borderRadius: BRICK_RADIUS,
    overflow: 'hidden',
  },
  brickBody: {
    flex: 1,
    borderRadius: BRICK_RADIUS,
    position: 'relative',
  },
  brickHighlight: {
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    height: 2,
    borderTopLeftRadius: BRICK_RADIUS,
    borderTopRightRadius: BRICK_RADIUS,
  },
  brickHighlightLeft: {
    position: 'absolute',
    top: 0,
    left: 0,
    bottom: 0,
    width: 2,
    borderTopLeftRadius: BRICK_RADIUS,
    borderBottomLeftRadius: BRICK_RADIUS,
  },
  brickShadow: {
    position: 'absolute',
    bottom: 0,
    left: 0,
    right: 0,
    height: 3,
    borderBottomLeftRadius: BRICK_RADIUS,
    borderBottomRightRadius: BRICK_RADIUS,
  },
  brickShadowRight: {
    position: 'absolute',
    top: 0,
    right: 0,
    bottom: 0,
    width: 3,
    borderTopRightRadius: BRICK_RADIUS,
    borderBottomRightRadius: BRICK_RADIUS,
  },
  gradientOverlay: {
    ...StyleSheet.absoluteFillObject,
  },
  orangeGlow: {
    position: 'absolute',
    top: -150,
    right: -150,
    width: 400,
    height: 400,
    borderRadius: 200,
  },
  blueGlow: {
    position: 'absolute',
    bottom: 50,
    left: -150,
    width: 350,
    height: 350,
    borderRadius: 175,
  },
  content: {
    flex: 1,
    backgroundColor: 'transparent',
  },
});
