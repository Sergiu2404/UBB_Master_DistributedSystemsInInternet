import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    react(),
    tailwindcss()
  ],
  server: {
    port: 5173,
    proxy: {
      '/countries': {
        target: 'http://localhost:8083/weather_preferences_app',
        changeOrigin: true,
        secure: false,
      },
      '/locations': {
        target: 'http://localhost:8083/weather_preferences_app',
        changeOrigin: true,
        secure: false,
      },
      '/preferences': {
        target: 'http://localhost:8083/weather_preferences_app',
        changeOrigin: true,
        secure: false,
      }
    }
  }
})
