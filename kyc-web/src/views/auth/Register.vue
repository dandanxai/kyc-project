<template>
    <div class="h-full w-full flex items-center justify-center relative overflow-hidden bg-gray-50/50 py-10">
    <div class="absolute top-[-10%] left-[-5%] w-[600px] h-[600px] bg-purple-100/40 blur-[120px] rounded-full"></div>
    <div class="absolute bottom-[-10%] right-[-5%] w-[500px] h-[500px] bg-blue-100/30 blur-[120px] rounded-full"></div>

    <div class="relative z-10 w-[95%] max-w-[450px] bg-white/80 backdrop-blur-3xl p-10 rounded-[40px] shadow-2xl shadow-black/5 border border-white overflow-y-auto max-h-[90vh] no-scrollbar">
        
        <div class="flex flex-col items-center mb-8">
        <h1 class="text-3xl font-bold text-black tracking-tight text-center">加入 KYC 智库</h1>
        <p class="text-gray-400 mt-2 text-[14px] text-center">构建您的数字人才能力图谱</p>
        </div>

        <div class="space-y-1 mb-6">
        <div class="flex p-1.5 bg-gray-200/50 rounded-2xl">
            <button 
            @click="role = 'candidate'" 
            :class="role === 'candidate' ? 'bg-white shadow-md text-black' : 'text-gray-500'" 
            class="flex-1 py-2.5 text-[13px] font-semibold rounded-xl transition-all duration-300"
            >
            个人注册
            </button>
            <button 
            @click="role = 'enterprise'" 
            :class="role === 'enterprise' ? 'bg-white shadow-md text-black' : 'text-gray-500'" 
            class="flex-1 py-2.5 text-[13px] font-semibold rounded-xl transition-all duration-300"
            >
            企业注册
            </button>
        </div>
        </div>

        <div class="space-y-4">
        
        <div v-if="registerMethod === 'phone'" class="space-y-1">
            <label class="text-[11px] font-bold text-gray-400 ml-1">手机号码</label>
            <input 
            v-model="form.phone" 
            type="tel" 
            placeholder="输入11位手机号" 
            class="w-full px-5 py-3.5 bg-gray-100/80 rounded-2xl focus:bg-white transition-all outline-none text-[14px] border-2 border-transparent focus:border-black/5" 
            />
        </div>

        <div v-if="registerMethod === 'email'" class="space-y-1">
            <label class="text-[11px] font-bold text-gray-400 ml-1">电子邮箱</label>
            <input 
            v-model="form.email" 
            type="email" 
            placeholder="example@domain.com" 
            class="w-full px-5 py-3.5 bg-gray-100/80 rounded-2xl focus:bg-white transition-all outline-none text-[14px] border-2 border-transparent focus:border-black/5" 
            />
        </div>

        <div class="space-y-1">
            <label class="text-[11px] font-bold text-gray-400 ml-1">设置登录密码</label>
            <input 
            v-model="form.password" 
            type="password" 
            placeholder="设置密码" 
            class="w-full px-5 py-3.5 bg-gray-100/80 rounded-2xl focus:bg-white transition-all outline-none text-[14px] border-2 border-transparent focus:border-black/5" 
            />
        </div>

        <div class="space-y-1">
            <label class="text-[11px] font-bold text-gray-400 ml-1">再次确认密码</label>
            <input 
            v-model="form.confirmPassword" 
            type="password" 
            placeholder="再次确认密码" 
            class="w-full px-5 py-3.5 bg-gray-100/80 rounded-2xl focus:bg-white transition-all outline-none text-[14px] border-2 border-transparent focus:border-black/5" 
            />
            <p v-if="form.confirmPassword && form.password !== form.confirmPassword" class="text-red-400 text-[12px] ml-2">
            两次输入的密码不一致
            </p>
        </div>

        <div v-if="role === 'enterprise'" class="pt-4 border-t border-gray-100 space-y-4">
            <p class="text-[12px] font-bold text-black ml-1">企业主体认证信息</p>
            
            <div class="space-y-1">
            <label class="text-[11px] font-bold text-gray-400 ml-1">企业名称</label>
            <input 
                v-model="form.enterpriseName" 
                type="text" 
                placeholder="请输入工商注册全称" 
                class="w-full px-5 py-3.5 bg-gray-100/80 rounded-2xl focus:bg-white transition-all outline-none text-[14px] border-2 border-transparent focus:border-black/5" 
            />
            </div>

            <div class="space-y-1">
            <label class="text-[11px] font-bold text-gray-400 ml-1">行业类型</label>
            <input 
                v-model="form.industry" 
                type="text" 
                placeholder="如：信创软件 / 操作系统" 
                class="w-full px-5 py-3.5 bg-gray-100/80 rounded-2xl focus:bg-white transition-all outline-none text-[14px] border-2 border-transparent focus:border-black/5" 
            />
            </div>
        </div>

        </div>

        <div class="mt-8 flex flex-col items-center justify-center space-y-3">
        <span class="text-[11px] font-bold text-gray-400 tracking-wider">其他注册方式</span>
        
        <div class="flex items-center gap-6">
            <button 
            @click="registerMethod = 'phone'"
            :class="registerMethod === 'phone' ? 'bg-black text-white scale-110 shadow-lg' : 'bg-gray-100 text-gray-400 hover:bg-gray-200'"
            class="w-11 h-11 rounded-full flex items-center justify-center transition-all duration-300 relative group"
            title="使用手机号注册"
            >
            <svg class="w-5 h-5" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <rect x="5" y="2" width="14" height="20" rx="2" ry="2"></rect>
                <line x1="12" y1="18" x2="12.01" y2="18"></line>
            </svg>
            </button>

            <button 
            @click="registerMethod = 'email'"
            :class="registerMethod === 'email' ? 'bg-black text-white scale-110 shadow-lg' : 'bg-gray-100 text-gray-400 hover:bg-gray-200'"
            class="w-11 h-11 rounded-full flex items-center justify-center transition-all duration-300 relative group"
            title="使用电子邮箱注册"
            >
            <svg class="w-5 h-5" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"></path>
                <polyline points="22,6 12,13 2,6"></polyline>
            </svg>
            </button>
        </div>
        </div>

        <div class="mt-6 flex items-start gap-2 ml-1">
        <input type="checkbox" v-model="form.agree" class="mt-1 w-4 h-4 accent-black" />
        <label class="text-[12px] text-gray-400 leading-relaxed">
            我已阅读并同意 <span class="text-black font-medium underline">用户服务协议</span> 和 <span class="text-black font-medium underline">隐私政策</span>
        </label>
        </div>

        <button 
        @click="submitRegister"
        :disabled="!form.agree || isSubmitting"
        class="w-full mt-8 py-4 bg-black text-white rounded-[20px] text-[15px] font-bold hover:bg-gray-800 active:scale-[0.98] transition-all shadow-xl shadow-black/10 disabled:bg-gray-300 flex items-center justify-center"
        >
        <template v-if="!isSubmitting">完成注册</template>
        <template v-else>
            <svg class="animate-spin h-5 w-5 text-white mr-2" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
            </svg>
            正在注册中...
        </template>
        </button>

        <div class="mt-8 text-center text-sm text-gray-400">
        已有账户？ <router-link to="/login" class="text-black font-bold hover:underline">立即登录</router-link>
        </div>
    </div>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { registerCandidate } from '@/api/auth'
import { toast } from '@/utils/toast' // 🎯 引入通用 Apple-Style 提示工具

const router = useRouter()

// 核心状态控制
const role = ref<'candidate' | 'enterprise'>('candidate') // 身份：个人 / 企业
const registerMethod = ref<'phone' | 'email'>('phone')     // 注册方式：手机（默认） / 邮箱
const isSubmitting = ref(false)

// 响应式表单
const form = reactive({
    phone: '',
    email: '',
    password: '',
    confirmPassword: '',
    enterpriseName: '',
    industry: '',
    agree: false
})

const submitRegister = async () => {
    if (form.password !== form.confirmPassword) {
    toast.error('两次密码输入不一致') // 🎯 替换
    return
    }

    // 整理数据，非当前模式的字段强制清空，避免垃圾数据污染接口
    if (registerMethod.value === 'phone') {
    form.email = ''
    if (!form.phone || !form.password) {
        toast.warning('请完整填写手机号和密码') // 🎯 替换
        return
    }
    } else {
    form.phone = ''
    if (!form.email || !form.password) {
        toast.warning('请完整填写邮箱和密码') // 🎯 替换
        return
    }
    }

    if (role.value === 'enterprise') {
    if (!form.enterpriseName) {
        toast.warning('请输入企业工商名称') // 🎯 替换
        return
    }
    }

    isSubmitting.value = true
    
    try {
    if (role.value === 'candidate') {
        // 个人端注册
        await registerCandidate({
        phone: form.phone || undefined,
        email: form.email || undefined,
        password: form.password
        })
        toast.success('个人账户注册成功！') // 🎯 替换
    } else {
        // 企业端注册模拟
        toast.warning(`企业注册暂未开放，稍后为您对接：${form.enterpriseName}`) // 🎯 替换
    }

    router.push('/login')

    } catch (error: any) {
    console.error('【注册异常】：', error)
    // 细节：若 Axios 拦截器已经弹过 toast，此处便只做 fallback 兜底
    toast.error(error.message || '注册失败，请重试！') 
    } finally {
    isSubmitting.value = false
    }
}
</script>

<style scoped>
.no-scrollbar::-webkit-scrollbar {
    display: none;
}
.no-scrollbar {
    -ms-overflow-style: none;
    scrollbar-width: none;
}
</style>