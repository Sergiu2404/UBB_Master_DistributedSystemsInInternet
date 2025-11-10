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
    '/api': { //any requests starting with /api are forwarded to backnd target
      target: 'http://localhost:8083/weather_preferences_app',
      changeOrigin: true,
      secure: false,
      rewrite: (path) => path.replace(/^\/api/, ''), //remove api prefix before forwrd
    },
  },
    historyApiFallback: true,
  }
})
