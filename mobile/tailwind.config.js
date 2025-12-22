/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./App.{js,jsx,ts,tsx}",
    "./src/**/*.{js,jsx,ts,tsx}"
  ],
  presets: [require("nativewind/preset")],
  theme: {
    extend: {
      colors: {
        'zinc-950': '#09090b',
        'zinc-900': '#18181b',
        'zinc-800': '#27272a',
        'zinc-700': '#3f3f46',
        'zinc-500': '#71717a',
        'zinc-400': '#a1a1aa',
        'zinc-100': '#f4f4f5',
        'orange-600': '#ea580c',
        'orange-500': '#f97316',
        'orange-400': '#fb923c',
        'amber-500': '#f59e0b',
        'amber-400': '#fbbf24',
        'green-500': '#22c55e',
        'blue-500': '#3b82f6',
      },
    },
  },
  plugins: [],
}