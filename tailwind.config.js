/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    'apps/shop/src/**/*.{html,ts}',
    'apps/api/src/**/*.{html,ts}',
    'libs/**/*.{html,ts}',
  ],
  theme: {
    extend: {
      fontFamily: {
        sans: ['Inter', 'ui-sans-serif', 'system-ui', '-apple-system', 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial'],
      },
    },
  },
  plugins: [require('daisyui')],
  daisyui: {
    themes: [
      {
        light: {
          primary: '#3b82f6',
          'primary-content': '#ffffff',
          secondary: '#f3f4f6',
          'secondary-content': '#1f2937',
          accent: '#10b981',
          neutral: '#1f2937',
          'neutral-content': '#f9fafb',
          'base-100': '#ffffff',
          'base-200': '#f9fafb',
          'base-300': '#f3f4f6',
          info: '#3b82f6',
          success: '#10b981',
          warning: '#f59e0b',
          error: '#ef4444',
        },
      },
    ],
  },
};
