<template>
    <div class="h-screen w-screen flex flex-col items-center justify-center relative overflow-hidden bg-gray-50/50 p-6">
    <div class="absolute top-[-10%] left-[-10%] w-[600px] h-[600px] bg-red-100/30 blur-[120px] rounded-full"></div>
    <div class="absolute bottom-[-10%] right-[-10%] w-[500px] h-[500px] bg-blue-100/20 blur-[120px] rounded-full"></div>

    <div class="relative z-10 flex flex-col items-center text-center max-w-[450px]">
        <div class="w-24 h-24 bg-white/80 backdrop-blur-2xl border border-white/50 rounded-[32px] shadow-2xl shadow-black/5 flex items-center justify-center mb-8">
        <span class="text-4xl font-extrabold text-black tracking-tight select-none">404</span>
        </div>

        <h1 class="text-2xl font-bold text-black tracking-tight">迷失在数字星空</h1>
        <p class="text-gray-400 mt-3 text-[14px] leading-relaxed">
        您访问的页面路径不存在，或者该功能已被安全迁移。
        </p>

        <div class="mt-10 flex gap-4 w-full">
        <button 
            @click="goBack" 
            class="flex-1 py-3.5 bg-gray-100 text-gray-700 rounded-[18px] text-[14px] font-semibold hover:bg-gray-200 active:scale-[0.98] transition-all"
        >
            返回上一页
        </button>
        <button 
            @click="goHome" 
            class="flex-1 py-3.5 bg-black text-white rounded-[18px] text-[14px] font-semibold hover:bg-gray-850 active:scale-[0.98] transition-all shadow-lg shadow-black/5"
        >
            返回首页
        </button>
        </div>
    </div>
    </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const goBack = () => {
    router.back()
}

const goHome = () => {
    // 如果有 token 就去人才大厅，没有就去登录页
    const token = localStorage.getItem('auth_token')
    if (token) {
    router.push('/candidate/home')
    } else {
    router.push('/login')
    }
}
</script>

<style scoped>
/* 极致灵动回弹 */
button {
    transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}
</style>