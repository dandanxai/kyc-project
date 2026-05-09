import { createApp } from 'vue'
import { createPinia } from 'pinia' // 1. 引入 pinia
import App from './App.vue'
import router from './router' // 引入路由
import './style.css'

const app = createApp(App)
app.use(createPinia()) // 2. 挂载 pinia 插件
app.use(router) // 使用路由
app.mount('#app')