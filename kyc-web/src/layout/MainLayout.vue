<template>
<div class="h-screen w-full flex bg-[#F5F5F7] overflow-hidden font-sans p-4">
    
    <aside 
    :class="[isCollapsed ? 'w-24' : 'w-64']"
    class="flex flex-col items-center py-8 bg-white/80 backdrop-blur-2xl rounded-[32px] border border-white shadow-sm z-30 transition-all duration-500 ease-[cubic-bezier(0.4,0,0.2,1)]"
    >
    
    <div 
        @click="isCollapsed = !isCollapsed"
        class="w-12 h-12 bg-black rounded-2xl flex items-center justify-center mb-12 shadow-lg cursor-pointer hover:scale-105 active:scale-95 transition-all group"
    >
        <div 
        :class="[isCollapsed ? 'rotate-45' : 'rotate-0']"
        class="w-5 h-5 border-2 border-white rounded-sm transition-transform duration-500 ease-out"
        ></div>
    </div>
    
    <nav class="flex-1 w-full px-4 flex flex-col gap-4">
        <router-link 
        v-for="item in navItems" 
        :key="item.path"
        :to="item.path"
        class="relative flex items-center p-4 rounded-2xl transition-all duration-300 group overflow-hidden"
        :class="[isCollapsed ? 'justify-center' : 'justify-start px-6']"
        >
        <div v-if="$route.path.includes(item.path)" class="absolute inset-0 bg-black rounded-2xl shadow-xl shadow-black/10"></div>
        
        <span 
            class="relative z-10 text-xl transition-transform group-hover:scale-110" 
            :class="[$route.path.includes(item.path) ? 'text-white' : 'text-gray-400 group-hover:text-black']"
        >
            {{ item.icon }}
        </span>

        <transition name="fade">
            <span 
            v-if="!isCollapsed" 
            class="relative z-10 ml-4 text-sm font-bold whitespace-nowrap"
            :class="[$route.path.includes(item.path) ? 'text-white' : 'text-gray-400 group-hover:text-black']"
            >
            {{ item.name }}
            </span>
        </transition>
        </router-link>
    </nav>

    <div 
        class="w-full px-4 flex items-center transition-all duration-300"
        :class="[isCollapsed ? 'justify-center' : 'justify-start px-6']"
    >
        <div class="w-10 h-10 rounded-full border-2 border-white shadow-sm overflow-hidden flex-shrink-0">
        <img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Felix" alt="avatar" />
        </div>
        <div v-if="!isCollapsed" class="ml-3 overflow-hidden animate-in fade-in slide-in-from-left-2">
        <p class="text-xs font-black text-black truncate w-24">Captain_X</p>
        <p class="text-[9px] font-bold text-gray-400 uppercase tracking-tighter">Pro Developer</p>
        </div>
    </div>
    </aside>

    <div class="flex-1 flex flex-col h-full ml-4 transition-all duration-500">
    <header class="h-20 w-full flex items-center justify-between px-6">
        <div>
        <h2 class="text-2xl font-black tracking-tight text-black">{{ currentPageTitle }}</h2>
        <p class="text-[10px] font-bold text-gray-400 uppercase tracking-widest mt-1">
            Kirin Talent Ecosystem <span class="mx-2 text-gray-200">|</span> 2026 EDITION
        </p>
        </div>
        
        <div class="flex items-center gap-4">
        <button class="w-10 h-10 bg-white rounded-full shadow-sm border border-white flex items-center justify-center hover:bg-gray-50">🔔</button>
        <button class="px-6 py-3 bg-black text-white rounded-2xl text-xs font-bold shadow-lg hover:shadow-black/20 hover:-translate-y-0.5 transition-all active:scale-95">
            快速上传
        </button>
        </div>
    </header>

    <main class="flex-1 overflow-y-auto no-scrollbar bg-white/40 rounded-[40px] border border-white/50 m-2 shadow-sm relative">
        <router-view />
    </main>
    </div>
</div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const isCollapsed = ref(true) // 默认收起模式，更显高级

const navItems = [
{ 
    name: '人才广场', 
    path: '/candidate/home', 
    icon: '🧭' // 发现与导向
},
{ 
    name: '智能画像', 
    path: '/candidate/resume', 
    icon: '🧬' // 简历解析与结构化建模
},
{ 
    name: '精准匹配', 
    path: '/candidate/jobs', 
    icon: '🎯' // AI 双向匹配与推荐
},
{ 
    name: '即时互联', 
    path: '/candidate/messages', 
    icon: '💬' // 聊天与投递记录
},
{ 
    name: 'AI 模拟面试', 
    path: '/candidate/interview', 
    icon: '🤖' // 针对信创岗位的 AI 机器人对练
},
{ 
    name: '产业洞察', 
    path: '/candidate/insights', 
    icon: '📊' // 职业、薪水及大环境趋势分析
}
]
const currentPageTitle = computed(() => {
const titles: Record<string, string> = {
    'home': '发现灵感',
    'dashboard': '核心看板',
    'jobs': '机遇广场',
    'messages': '即时沟通'
}
const key = Object.keys(titles).find(k => route.path.includes(k))
return key ? titles[key] : '系统控制台'
})
</script>

<style scoped>
.no-scrollbar::-webkit-scrollbar { display: none; }
.no-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }

/* 文字淡入淡出动画 */
.fade-enter-active, .fade-leave-active {
transition: opacity 0.2s ease, transform 0.2s ease;
}
.fade-enter-from, .fade-leave-to {
opacity: 0;
transform: translateX(-10px);
}
</style>