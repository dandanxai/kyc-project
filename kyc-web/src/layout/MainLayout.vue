<template>
    <div class="h-screen w-full flex bg-[#F5F5F7] overflow-hidden font-sans p-4 relative select-none animate-fade-in">
    
    <aside 
        :class="[isCollapsed ? 'w-24' : 'w-64']"
        class="flex flex-col items-center py-8 bg-white/80 backdrop-blur-2xl rounded-[32px] border border-white shadow-sm z-30 transition-all duration-500 ease-[cubic-bezier(0.4,0,0.2,1)] flex-shrink-0 relative"
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
        
        <nav class="flex-1 w-full px-4 flex flex-col gap-4 overflow-y-auto no-scrollbar">
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
        v-if="isLoggedIn"
        ref="userCardRef"
        @mouseenter="showMenu"
        @mouseleave="hideMenu"
        class="w-[85%] mx-auto py-3 px-3 rounded-2xl flex items-center cursor-pointer transition-all duration-300 ease-out hover:bg-neutral-50 border border-transparent hover:border-black/[0.03] hover:shadow-[0_8px_24px_rgba(0,0,0,0.02)] active:scale-98"
        :class="[isCollapsed ? 'justify-center' : 'justify-start pl-4']"
        >
            <div class="w-10 h-10 rounded-full border-2 border-white shadow-sm overflow-hidden flex-shrink-0 transition-transform duration-300 hover:rotate-6">
                <img :src="userAvatar" alt="avatar" />
            </div>
            
            <div v-if="!isCollapsed" class="ml-3 overflow-hidden flex-1 animate-fade-in">
                <div class="flex items-center justify-between">
                <p class="text-xs font-black text-black truncate w-20">{{ nickname }}</p>
                <span 
                    :class="[isEnterprise ? 'bg-amber-50 text-amber-600' : 'bg-indigo-50 text-indigo-600']"
                    class="text-[8px] px-1.5 py-0.5 rounded-full font-bold"
                >
                    {{ isEnterprise ? 'CORP' : 'PRO' }}
                </span>
                </div>
                <p class="text-[9px] font-bold text-gray-400 uppercase tracking-tighter mt-0.5">
                {{ isEnterprise ? '企业控制' : '账号设置' }} ⚙️
                </p>
            </div>
        </div>

        <div 
        v-else
        class="w-[85%] mx-auto flex items-center justify-center transition-all duration-300"
        >
            <button 
            v-if="isCollapsed"
            @click="goToLogin"
            class="w-12 h-12 bg-black hover:bg-neutral-800 text-white rounded-2xl flex items-center justify-center shadow-lg hover:scale-105 active:scale-95 transition-all"
            title="请先登录"
            >
            <span class="text-lg">🔑</span>
            </button>
            
            <button 
            v-else
            @click="goToLogin"
            class="w-full py-3.5 px-4 bg-black hover:bg-neutral-800 text-white font-bold rounded-2xl text-xs flex items-center justify-center gap-2 shadow-lg shadow-black/5 hover:-translate-y-0.5 active:translate-y-0 active:scale-[0.98] transition-all"
            >
            <span>🔑</span>
            <span>即刻登录解锁更多</span>
            </button>
        </div>

    </aside>

    <div class="flex-1 flex flex-col h-full ml-4 transition-all duration-500 overflow-hidden">
        
        <header class="h-20 w-full flex items-center justify-between px-6 flex-shrink-0">
        <div>
            <h2 class="text-2xl font-black tracking-tight text-black transition-all duration-300">{{ currentPageTitle }}</h2>
            <p class="text-[10px] font-bold text-gray-400 uppercase tracking-widest mt-1">
            Kirin Talent Ecosystem <span class="mx-2 text-gray-200">|</span> 2026 EDITION
            </p>
        </div>
        
        <div class="flex items-center gap-4">
            <button class="w-10 h-10 bg-white rounded-full shadow-sm border border-gray-100 flex items-center justify-center hover:bg-gray-50 active:scale-95 transition-all">🔔</button>
            <button 
                @click="handleHeaderAction"
                class="px-6 py-3 bg-black text-white rounded-2xl text-xs font-bold shadow-lg hover:shadow-black/20 hover:-translate-y-0.5 transition-all active:scale-95"
            >
                {{ isEnterprise ? '发布新职位' : '快速上传' }}
            </button>
        </div>
        </header>

        <main class="flex-1 overflow-y-auto no-scrollbar bg-white/40 rounded-[40px] border border-white/50 m-2 shadow-sm relative">
        <router-view />
        </main>

    </div>

    <teleport to="body">
        <transition name="pop">
        <div 
            v-if="isMenuVisible && isLoggedIn"
            @mouseenter="keepMenu"
            @mouseleave="hideMenu"
            :style="menuStyle"
            class="fixed z-[9999] w-48 bg-white/95 backdrop-blur-xl rounded-2xl border border-neutral-100 shadow-[0_12px_30px_rgba(0,0,0,0.08)] p-2 flex flex-col gap-1 transition-all duration-200"
        >
            <button 
            @click="goToSettings"
            class="w-full text-left px-4 py-3 text-xs font-black text-gray-700 hover:text-black hover:bg-neutral-50 rounded-xl transition-colors flex items-center gap-3 active:scale-95"
            >
            <span>⚙️</span>
            <span>{{ isEnterprise ? '企业信息配置' : '个人中心设置' }}</span>
            </button>
            
            <div class="h-[1px] bg-neutral-100 my-0.5"></div>

            <button 
            @click="handleLogout"
            class="w-full text-left px-4 py-3 text-xs font-black text-rose-500 hover:text-rose-600 hover:bg-rose-50/50 rounded-xl transition-colors flex items-center gap-3 active:scale-95"
            >
            <span>🚪</span>
            <span>安全退出登录</span>
            </button>
        </div>
        </transition>
    </teleport>

    </div>
</template>

<script setup lang="ts">
import { ref, computed, nextTick, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { toast } from '@/utils/toast' // 🎯 引入高级 toast 提示

const route = useRoute()
const router = useRouter()
const isCollapsed = ref(true)

// ================== 🔑 登录状态与用户信息安全获取 ==================

// 动态检测本地是否有 token
const isLoggedIn = computed(() => {
    return !!localStorage.getItem('auth_token')
})

// 动态解析用户信息，加入安全熔断机制，防止无 token 时报错
const userInfo = computed(() => {
    const infoStr = localStorage.getItem('user_info')
    if (!infoStr) return null
    try {
    return JSON.parse(infoStr)
    } catch (e) {
    console.error('解析用户信息失败', e)
    return null
    }
})

// 安全地映射昵称/用户名
const nickname = computed(() => {
    if (isEnterprise.value) return 'HR_Partner'
    return userInfo.value?.nickname || userInfo.value?.username || 'Captain_X'
})

// 用户头像安全降级机制
const userAvatar = computed(() => {
    // 如果有自定义头像则使用，否则返回一个优雅的 Dicebear 默认头像
    return userInfo.value?.avatar || 'https://api.dicebear.com/7.x/avataaars/svg?seed=Felix'
})

const goToLogin = () => {
    router.push('/login')
}

// 统一的头部快捷操作（如果未登录，点击也需要提示并拦截）
const handleHeaderAction = () => {
    if (!isLoggedIn.value) {
    toast.warning('请先登录以使用此功能')
    router.push('/login')
    return
    }
    // 已登录正常运行业务
    if (isEnterprise.value) {
    toast.info('去往岗位发布控制台...')
    } else {
    toast.info('快速解析上传中...')
    }
}

// ================== 路由与菜单配置 ==================

// 判断当前是否是企业端视图
const isEnterprise = computed(() => route.path.startsWith('/enterprise'))

// 动态侧边栏导航：根据双端自适应渲染
const navItems = computed(() => {
    if (isEnterprise.value) {
        return [
        { name: '管理大盘', path: '/enterprise/dashboard', icon: '📊' },
        { name: 'AI 智配中心', path: '/enterprise/match', icon: '⚡' },
        { name: '人才公海', path: '/enterprise/talent-pool', icon: '🧭' },
        { name: '岗位管理', path: '/enterprise/jobs-manage', icon: '🎯' },
        { name: '即时沟通', path: '/enterprise/messages', icon: '💬' }
    ]
    } else {
    return [
        { name: '人才广场', path: '/candidate/home', icon: '🧭' },
        { name: '智能画像', path: '/candidate/resume', icon: '🧬' },
        { name: '精准匹配', path: '/candidate/jobs', icon: '🎯' },
        { name: '即时互联', path: '/candidate/messages', icon: '💬' },
        { name: 'AI 模拟面试', path: '/candidate/interview', icon: '🤖' },
        { name: '产业洞察', path: '/candidate/insights', icon: '📊' }
    ]
    }
})

// 动态路由大标题：适配候选人端与企业端
const currentPageTitle = computed(() => {
    const titles: Record<string, string> = {
    'home': '发现灵感',
    'resume': '智能画像与职业建模',
    'jobs': '精准匹配中心',
    'messages': '即时沟通反馈',
    'interview': 'AI 模拟面试工作台',
    'insights': '信创产业与底层数据洞察',
    
    // 企业端
    'dashboard': '企业数据看板 & 效能监控',
    'match': 'AI 智能双向精准匹配',
    'talent-pool': '麒麟人才生态公海',
    'jobs-manage': '信创发布与岗位流转',
    'enterprise/settings': '企业设置 & 基础资质配置'
    }
    const key = Object.keys(titles).find(k => route.path.includes(k))
    return key ? titles[key] : '系统控制台'
})

// ================== FLOATING MENU LOGIC (双端通用悬浮气泡) ==================
const userCardRef = ref<HTMLElement | null>(null)
const isMenuVisible = ref(false)
const menuStyle = ref({ top: '0px', left: '0px' })
let delayTimer: ReturnType<typeof setTimeout> | null = null

const showMenu = () => {
    if (!isLoggedIn.value) return // 未登录直接拦截悬浮卡片菜单显示
    if (delayTimer) clearTimeout(delayTimer)
    
    nextTick(() => {
    if (!userCardRef.value) return
    const rect = userCardRef.value.getBoundingClientRect()
    
    menuStyle.value = {
        top: `${rect.top + rect.height / 2 - 64}px`, // 居中校准
        left: `${rect.right + 12}px` // 弹出在右侧12px外
    }
    isMenuVisible.value = true
    })
}

const hideMenu = () => {
    delayTimer = setTimeout(() => {
    isMenuVisible.value = false
    }, 150)
}

const keepMenu = () => {
    if (delayTimer) clearTimeout(delayTimer)
}

// 动态路径跳转：跳转至个人配置或企业配置
const goToSettings = () => {
    isMenuVisible.value = false
    if (isEnterprise.value) {
    router.push('/enterprise/settings')
    } else {
    router.push('/candidate/settings')
    }
}

// 通用安全退出
const handleLogout = () => {
    isMenuVisible.value = false
    localStorage.clear()
    sessionStorage.clear()
    router.push('/login')
}
</script>

<style scoped>
.no-scrollbar::-webkit-scrollbar { display: none; }
.no-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }

/* 侧边栏菜单文字淡入动画 */
.fade-enter-active, .fade-leave-active {
    transition: opacity 0.25s ease, transform 0.25s ease;
}
.fade-enter-from, .fade-leave-to {
    opacity: 0;
    transform: translateX(-12px);
}

/* 浮窗 Apple 级动效 */
.pop-enter-active {
    transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
}
.pop-leave-active {
    transition: all 0.15s cubic-bezier(0.4, 0, 1, 1);
}
.pop-enter-from, .pop-leave-to {
    opacity: 0;
    transform: scale(0.9) translateX(-8px);
}

.animate-fade-in {
    animation: fadeIn 0.35s ease-out forwards;
}
@keyframes fadeIn {
    from { opacity: 0; transform: translateY(4px); }
    to { opacity: 1; transform: translateY(0); }
}
</style>