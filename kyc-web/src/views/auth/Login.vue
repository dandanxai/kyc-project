<template>
    <div class="h-full w-full flex items-center justify-center relative overflow-hidden bg-gray-50/50">
    <div class="absolute top-[-10%] right-[-5%] w-[500px] h-[500px] bg-blue-100/40 blur-[120px] rounded-full"></div>
    <div class="absolute bottom-[-10%] left-[-5%] w-[500px] h-[500px] bg-indigo-100/30 blur-[120px] rounded-full"></div>

    <div class="relative z-10 w-[90%] max-w-[460px] bg-white/80 backdrop-blur-3xl p-12 rounded-[40px] shadow-2xl shadow-black/5 border border-white">
        <div class="flex flex-col items-center mb-10">
        <div class="w-14 h-14 bg-black rounded-[18px] flex items-center justify-center mb-6 shadow-xl">
            <span class="text-white text-2xl font-bold">K</span>
        </div>
        <h1 class="text-3xl font-bold text-black tracking-tight text-center">KYC System</h1>
        <p class="text-gray-400 mt-3 text-[15px] text-center">AI 驱动的麒麟操作系统人才图谱</p>
        </div>

        <div class="flex p-1.5 bg-gray-200/50 rounded-2xl mb-8">
        <button 
            @click="role = 'candidate'" 
            :class="role === 'candidate' ? 'bg-white shadow-md text-black' : 'text-gray-500'" 
            class="flex-1 py-2.5 text-[13px] font-semibold rounded-xl transition-all duration-300"
        >
            个人登录
        </button>
        <button 
            @click="role = 'enterprise'" 
            :class="role === 'enterprise' ? 'bg-white shadow-md text-black' : 'text-gray-500'" 
            class="flex-1 py-2.5 text-[13px] font-semibold rounded-xl transition-all duration-300"
        >
            企业管理
        </button>
        </div>

        <div class="space-y-4">
        <input 
            v-model="account" 
            type="text" 
            placeholder="手机号码 / 电子邮箱" 
            class="w-full px-6 py-4 bg-gray-100/80 rounded-2xl focus:bg-white transition-all outline-none text-[15px] border-2 border-transparent focus:border-black/5" 
        />
        <input 
            v-model="password" 
            type="password" 
            placeholder="输入密码" 
            class="w-full px-6 py-4 bg-gray-100/80 rounded-2xl focus:bg-white transition-all outline-none text-[15px] border-2 border-transparent focus:border-black/5" 
        />
        </div>

        <button 
        @click="handleLogin" 
        :disabled="isLoggingIn"
        class="w-full mt-10 py-4 bg-black text-white rounded-[20px] text-[15px] font-bold hover:bg-gray-800 active:scale-[0.98] transition-all disabled:bg-gray-400 flex items-center justify-center"
        >
        <template v-if="!isLoggingIn">即刻进入</template>
        <template v-else>
            <svg class="animate-spin h-5 w-5 text-white mr-2" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
            </svg>
            验证中...
        </template>
        </button>
        
        <div class="mt-8 text-center text-sm text-gray-400">
        还没有账户？ 
        <router-link to="/register" class="text-black font-bold hover:underline">申请加入</router-link>
        </div>
    </div>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { loginCandidate } from '@/api/auth'
import { toast } from '@/utils/toast' // 🎯 引入通用 Apple-Style 提示工具

const router = useRouter()
const role = ref<'candidate' | 'enterprise'>('candidate')
const isLoggingIn = ref(false)

// 响应式表单字段
const account = ref('')
const password = ref('')

const handleLogin = async () => {
    if (!account.value || !password.value) {
    toast.warning('请输入账号和密码！') // 🎯 替换
    return
    }

    if (role.value === 'enterprise') {
    toast.warning('目前仅支持个人用户通过达梦数据库登录测试！') // 🎯 替换
    return
    }

    isLoggingIn.value = true
    
    try {
    const res = await loginCandidate({
        account: account.value,
        password: password.value
    })

    // 请求成功：将后端签发的 JWT 真实钥匙存入 localStorage
    localStorage.setItem('auth_token', res.data.token) 
    localStorage.setItem('user_info', JSON.stringify(res.data))

    toast.success(`欢迎回来，${res.data.nickname || res.data.username}`) // 🎯 替换
    
    // 跳转到对应的个人端工作台
    router.push('/candidate/dashboard')

    } catch (error: any) {
    console.error('【登录异常拦截】：', error)
    toast.error(error.message || '登录异常，请检查后端运行状态！') // 🎯 替换
    } finally {
    isLoggingIn.value = false
    }
}
</script>