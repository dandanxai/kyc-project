import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'

// 引入全局 CSS（包含 Tailwind）
import './style.css'

// 引入 Element Plus（后台最实用的 UI 库）
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

const app = createApp(App)

app.use(createPinia()) // 启用状态管理
app.use(router)        // 启用路由
app.use(ElementPlus)   // 启用 Element UI

app.mount('#app')