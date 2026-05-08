/** @type {import('tailwindcss').Config} */
export default {
    content: [
        "./index.html",
        "./src/**/*.{vue,js,ts,jsx,tsx}",
    ],
    theme: {
        extend: {
            colors: {
            'apple-blue': '#0071E3',
            'apple-gray': '#F5F5F7',
            }
        },
    },
    plugins: [],
}