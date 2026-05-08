<template>
    <div class="login-container">
    
    <div class="light-glow glow-1"></div>
    <div class="light-glow glow-2"></div>

    <div class="login-card">
        
        <div class="header-section">
        <div class="logo-box">
            K
            <span class="logo-glow"></span>
        </div>
        
        <div class="title-group">
            <h2 class="main-title">Kirin 平台运营超级管理控制中心</h2>
            <p class="sub-title">
            Security Sandbox & Operations Management Console
            </p>
        </div>
        </div>

        <form @submit.prevent="handleLogin" class="form-container">
        <div class="input-group">
            <label class="input-label">Security Account / 管理员账号</label>
            <div class="input-wrapper">
            <span class="input-icon">🛡️</span>
            <input 
                v-model="loginForm.username"
                type="text" 
                required
                placeholder="请输入管理员账户" 
                class="styled-input"
            />
            </div>
        </div>

        <div class="input-group">
            <label class="input-label">Access Cipher / 动态安全口令</label>
            <div class="input-wrapper">
            <span class="input-icon">🔑</span>
            <input 
                v-model="loginForm.password"
                type="password" 
                required
                placeholder="请输入安全认证密匙" 
                class="styled-input"
            />
            </div>
        </div>

        <div class="options-row">
            <label class="remember-me">
            <input type="checkbox" v-model="loginForm.remember" class="styled-checkbox">
            <span>记住此终端设备</span>
            </label>
            <span class="shield-link">插入国密 SM4 硬件密盾</span>
        </div>

        <button 
            type="submit"
            :disabled="isSubmitting"
            class="submit-button"
        >
            <span v-if="isSubmitting" class="spinner"></span>
            <span>{{ isSubmitting ? 'SECURE AUTHORIZING...' : '安全授权登录' }}</span>
        </button>
        </form>

        <footer class="footer-section">
        <p class="footer-text">
            Authorized personnel only. All access is logged and audited.
        </p>
        <p class="footer-sub-text">
            SEC-ID: {{ currentTerminalIp }} (INTERNAL ZONE B)
        </p>
        </footer>

    </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const isSubmitting = ref(false)
const currentTerminalIp = ref('10.211.55.2')

const loginForm = ref({
    username: '',
    password: '',
    remember: false
})

const handleLogin = () => {
    isSubmitting.value = true
    
    setTimeout(() => {
    isSubmitting.value = false
    localStorage.setItem('admin_token', 'KIRIN_SUPER_SECRET_TOKEN_2026')
    localStorage.setItem('admin_role', 'admin')
    
    // 跳转到你 index.ts 中配置的后台路径
    router.push('/admin/dashboard')
    }, 1200)
}

onMounted(() => {
    const randomSub = Math.floor(Math.random() * 254) + 1
    currentTerminalIp.value = `10.211.55.${randomSub}`
})
</script>

<style scoped>
/* 使用极高兼容性的 CSS 变量和 Flex/Grid 彻底重构布局，保证绝不垮掉 */
.login-container {
    height: 100vh;
    width: 100vw;
    background-color: #0D0E12;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 16px;
    position: relative;
    overflow: hidden;
    user-select: none;
}

/* 背景光晕 */
.light-glow {
    position: absolute;
    border-radius: 50%;
    filter: blur(120px);
    pointer-events: none;
    opacity: 0.15;
}
.glow-1 {
    top: -150px;
    left: -150px;
    width: 400px;
    height: 400px;
    background-color: #6366f1;
}
.glow-2 {
    bottom: -150px;
    right: -150px;
    width: 400px;
    height: 400px;
    background-color: #06b6d4;
}

/* 登录卡片 */
.login-card {
    width: 100%;
    max-width: 440px;
    background-color: rgba(255, 255, 255, 0.02);
    backdrop-filter: blur(40px);
    -webkit-backdrop-filter: blur(40px);
    border: 1px solid rgba(255, 255, 255, 0.05);
    padding: 40px;
    border-radius: 24px;
    box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5);
    display: flex;
    flex-direction: column;
    z-index: 10;
}

/* 头部样式 */
.header-section {
    text-align: center;
}
.logo-box {
    width: 56px;
    height: 56px;
    margin: 0 auto 16px auto;
    background: linear-gradient(135deg, #6366f1 0%, #22d3ee 100%);
    border-radius: 16px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: 900;
    font-size: 24px;
    color: #000000;
    position: relative;
    box-shadow: 0 10px 15px -3px rgba(99, 102, 241, 0.3);
}
.logo-glow {
    position: absolute;
    inset: -2px;
    border-radius: 18px;
    background: linear-gradient(135deg, #6366f1 0%, #22d3ee 100%);
    opacity: 0.3;
    filter: blur(4px);
    z-index: -1;
}
.main-title {
    font-size: 18px;
    font-weight: 900;
    letter-spacing: -0.025em;
    color: #ffffff;
}
.sub-title {
    font-size: 9px;
    font-family: monospace;
    color: #4b5563;
    text-transform: uppercase;
    letter-spacing: 0.1em;
    margin-top: 4px;
}

/* 表单与输入框 */
.form-container {
    margin-top: 32px;
    margin-bottom: 32px;
}
.input-group {
    margin-bottom: 20px;
}
.input-label {
    font-size: 9px;
    font-family: monospace;
    color: #6b7280;
    text-transform: uppercase;
    letter-spacing: 0.05em;
    display: block;
    margin-bottom: 6px;
}
.input-wrapper {
    position: relative;
}
.input-icon {
    position: absolute;
    left: 14px;
    top: 50%;
    transform: translateY(-50%);
    font-size: 14px;
    opacity: 0.8;
}
.styled-input {
    width: 100%;
    padding: 12px 16px 12px 42px;
    background-color: rgba(255, 255, 255, 0.03);
    border: 1px solid rgba(255, 255, 255, 0.06);
    border-radius: 12px;
    font-size: 13px;
    color: #ffffff;
    transition: all 0.25s ease;
}
.styled-input::placeholder {
    color: #374151;
}
.styled-input:focus {
    border-color: rgba(99, 102, 241, 0.5);
    background-color: rgba(255, 255, 255, 0.06);
    outline: none;
}

/* 选项行 */
.options-row {
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 11px;
    color: #6b7280;
    margin-top: 16px;
    padding: 0 4px;
}
.remember-me {
    display: flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;
}
.styled-checkbox {
    accent-color: #6366f1;
}
.shield-link {
    color: #818cf8;
    cursor: pointer;
}
.shield-link:hover {
    text-decoration: underline;
}

/* 登录按钮 */
.submit-button {
    width: 100%;
    padding: 14px;
    background: linear-gradient(90deg, #4f46e5 0%, #06b6d4 100%);
    border: none;
    border-radius: 12px;
    font-size: 13px;
    font-weight: 900;
    letter-spacing: 0.05em;
    color: #ffffff;
    cursor: pointer;
    transition: all 0.2s ease;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    margin-top: 24px;
}
.submit-button:hover {
    opacity: 0.95;
}
.submit-button:active {
    transform: scale(0.98);
}
.submit-button:disabled {
    opacity: 0.5;
    cursor: not-allowed;
}

/* 加载动画 */
.spinner {
    width: 16px;
    height: 16px;
    border: 2px solid rgba(255, 255, 255, 0.2);
    border-top-color: #ffffff;
    border-radius: 50%;
    animation: spin 0.8s linear infinite;
}
@keyframes spin {
    to { transform: rotate(360deg); }
}

/* 底部声明 */
.footer-section {
    text-align: center;
    border-top: 1px solid rgba(255, 255, 255, 0.05);
    padding-top: 16px;
}
.footer-text {
    font-size: 8px;
    font-family: monospace;
    color: #374151;
    text-transform: uppercase;
    letter-spacing: 0.05em;
}
.footer-sub-text {
    font-size: 8px;
    font-family: monospace;
    color: #4b5563;
    margin-top: 4px;
}
</style>