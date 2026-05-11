<template>
    <div class="h-full w-full flex items-center justify-center relative overflow-hidden bg-gray-50/50">
    <div class="absolute top-[-10%] right-[-5%] w-[500px] h-[500px] bg-blue-100/40 blur-[120px] rounded-full pointer-events-none"></div>
    <div class="absolute bottom-[-10%] left-[-5%] w-[500px] h-[500px] bg-indigo-100/30 blur-[120px] rounded-full pointer-events-none"></div>

    <div class="relative z-10 w-[90%] max-w-[460px] bg-white/80 backdrop-blur-3xl p-12 rounded-[40px] shadow-2xl shadow-black/5 border border-white">
        <div class="flex flex-col items-center mb-10">
        <div class="w-14 h-14 bg-black rounded-[18px] flex items-center justify-center mb-6 shadow-xl">
            <span class="text-white text-2xl font-bold">K</span>
        </div>
        <h1 class="text-3xl font-bold text-black tracking-tight text-center">KYC System</h1>
        <p class="text-gray-400 mt-3 text-[15px] text-center">AI 驱动的麒麟操作系统人才图谱</p>
        </div>

        <div class="flex p-1 bg-gray-100 rounded-2xl mb-8 relative">
        <button 
            @click="role = 'candidate'"
            :class="[
            'flex-1 py-3 text-sm font-semibold rounded-xl transition-all duration-300 relative z-10',
            role === 'candidate' ? 'bg-white text-black shadow-sm' : 'text-gray-400 hover:text-gray-600'
            ]"
        >
            个人求职端
        </button>
        <button 
            @click="role = 'enterprise'"
            :class="[
            'flex-1 py-3 text-sm font-semibold rounded-xl transition-all duration-300 relative z-10',
            role === 'enterprise' ? 'bg-white text-black shadow-sm' : 'text-gray-400 hover:text-gray-600'
            ]"
        >
            企业管理端
        </button>
        </div>

        <form @submit.prevent="handleLogin" class="space-y-5">
        <div>
            <label class="block text-xs font-semibold text-gray-400 uppercase tracking-wider mb-2 ml-1">账号</label>
            <input 
            v-model="account"
            type="text" 
            placeholder="请输入手机号 / 邮箱" 
            class="w-full px-5 py-4 bg-gray-50 border border-gray-100 rounded-2xl focus:outline-none focus:ring-2 focus:ring-black/5 focus:border-black transition-all text-sm"
            required
            />
        </div>

        <div>
            <div class="flex justify-between items-center mb-2 ml-1">
            <label class="block text-xs font-semibold text-gray-400 uppercase tracking-wider">密码</label>
            <a href="#" class="text-xs text-gray-400 hover:text-black transition-colors font-medium">忘记密码？</a>
            </div>
            <input 
            v-model="password"
            type="password" 
            placeholder="请输入您的登录密码" 
            class="w-full px-5 py-4 bg-gray-50 border border-gray-100 rounded-2xl focus:outline-none focus:ring-2 focus:ring-black/5 focus:border-black transition-all text-sm"
            required
            />
        </div>

        <button 
            type="submit"
            :disabled="isLoggingIn"
            class="w-full py-4 bg-black text-white font-semibold rounded-2xl hover:bg-neutral-800 active:scale-[0.98] transition-all disabled:opacity-50 disabled:pointer-events-none shadow-lg shadow-black/10 mt-2 flex items-center justify-center gap-2"
        >
            <span v-if="isLoggingIn" class="w-4 h-4 border-2 border-white/30 border-t-white rounded-full animate-spin"></span>
            <span>{{ isLoggingIn ? '安全登录中...' : '即刻登录' }}</span>
        </button>
        </form>

        <div class="mt-8 text-center">
        <p class="text-sm text-gray-400">
            还没有 KYC 账户？
            <router-link to="/register" class="text-black font-semibold hover:underline">立即申请加入</router-link>
        </p>
        </div>
    </div>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { loginCandidate } from '@/api/auth'
import { loginEnterprise } from '@/api/auth' // 🎯 引入企业端专用的登录接口
import { useUserStore } from '@/store/user'
import { toast } from '@/utils/toast'

const router = useRouter()
const userStore = useUserStore()

const role = ref<'candidate' | 'enterprise'>('candidate')
const isLoggingIn = ref(false)

const account = ref('')
const password = ref('')

const handleLogin = async () => {
    if (!account.value || !password.value) {
        toast.warning('请输入账号和密码！')
        return
    }

    isLoggingIn.value = true
    
    try {
        if (role.value === 'candidate') {
            // ==================== 🎓 个人用户登录 ====================
            const res = await loginCandidate({
                account: account.value,
                password: password.value
            })

            // 1. 存储 JWT 令牌与身份标记（注意此处的值要和 Pinia Store 中的判断逻辑一致）
            localStorage.setItem('auth_token', res.data.token)
            localStorage.setItem('user_role', 'candidate') // 存入小写的 candidate

            // 2. 借助新写入的 Token 和角色，通知 Pinia 拉取个人信息
            await userStore.fetchUserProfile()
            
            toast.success('登录成功，欢迎回来！')
            router.push('/candidate/home') // 跳转至个人端主页
        } else {
            // ==================== 🏢 企业用户登录 ====================
            // 🎯 调用后端企业登录接口，传入账号密码
            const res = await loginEnterprise({
                username: account.value, // 企业端 DTO 接收字段通常为 username
                password: password.value
            })

            // 1. 存储 JWT 令牌与身份标记
            localStorage.setItem('auth_token', res.data.token)
            localStorage.setItem('user_role', 'enterprise') // 存入小写的 enterprise

            // 2. 借助新写入的 Token 和角色，通知 Pinia 拉取企业档案信息
            await userStore.fetchUserProfile()

            toast.success('企业后台登录成功！')
            router.push('/enterprise/dashboard') // 🚀 跳转到企业后台工作台仪表盘
        }
    } catch (error: any) {
        console.error('登录异常:', error)
        // 异常提示已被拦截器优雅接管，此处做防死锁重置即可
    } finally {
        isLoggingIn.value = false
    }
}
</script>

<style scoped>
/* 隐藏滚动条 */
.no-scrollbar::-webkit-scrollbar {
    display: none;
}
.no-scrollbar {
    -ms-overflow-style: none;
    scrollbar-width: none;
}
</style>