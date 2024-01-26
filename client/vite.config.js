import { defineConfig } from "vite";

export default defineConfig({
    server: {
        proxy: {
            '/register': 'http://localhost:8080',
            '/landrecords': 'http://localhost:8080'
        }
    }
})