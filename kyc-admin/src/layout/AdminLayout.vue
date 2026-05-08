<template>
    <div class="layout-container">
    
    <aside :class="[isCollapsed ? 'w-20' : 'w-64']" class="sidebar">
        
        <div @click="toggleSidebar" class="logo-toggle-btn">
        <div :class="[isCollapsed ? 'rotate-45' : 'rotate-0']" class="toggle-icon"></div>
        </div>
        
        <nav class="nav-menu no-scrollbar">
        <div v-for="menu in menuStructure" :key="menu.name" class="menu-group">
            
            <router-link 
            v-if="!menu.children" 
            :to="menu.path" 
            class="nav-item"
            :class="{ 'active': $route.path === menu.path, 'justify-center': isCollapsed }"
            >
            <span class="nav-icon" :title="menu.name">{{ menu.icon }}</span>
            <span v-if="!isCollapsed" class="nav-name">{{ menu.name }}</span>
            </router-link>

            <div v-else class="parent-menu-wrapper">
            <div 
                @click="toggleSubMenu(menu.name)"
                class="nav-item parent-item"
                :class="{ 
                'active': isParentActive(menu), 
                'justify-center': isCollapsed 
                }"
                :title="menu.name"
            >
                <div class="flex items-center">
                <span class="nav-icon">{{ menu.icon }}</span>
                <span v-if="!isCollapsed" class="nav-name">{{ menu.name }}</span>
                </div>
                <span 
                v-if="!isCollapsed" 
                class="arrow-icon" 
                :class="{ 'rotated': expandedMenus.includes(menu.name) }"
                >
                ▼
                </span>
            </div>

            <transition name="expand">
                <div 
                v-if="expandedMenus.includes(menu.name) && !isCollapsed" 
                class="sub-menu-list"
                >
                <router-link 
                    v-for="sub in menu.children" 
                    :key="sub.path"
                    :to="sub.path"
                    class="sub-nav-item"
                    active-class="sub-active"
                >
                    <span class="sub-dot"></span>
                    <span class="sub-name">{{ sub.name }}</span>
                </router-link>
                </div>
            </transition>
            </div>

        </div>
        </nav>

        <div class="user-profile" :class="{ 'justify-center': isCollapsed }">
        <div class="avatar">👑</div>
        <div v-if="!isCollapsed" class="user-info animate-fade-in">
            <p class="username">Kirin_Admin</p>
            <p class="role">超级管理员</p>
        </div>
        </div>
    </aside>

    <div class="main-content">
        
        <header class="header">
        <div>
            <h2 class="header-title">{{ currentPageTitle }}</h2>
            <p class="header-subtitle">Platform Operations & Security Console · v2.0 Core</p>
        </div>
        
        <div class="header-actions">
            <button @click="toggleTheme" class="theme-toggle-btn">
            <span v-if="isDark">☀️ 浅色模式</span>
            <span v-else>🌙 深色模式</span>
            </button>
            
            <button @click="handleLogout" class="logout-btn">
            安全退出
            </button>
        </div>
        </header>

        <main class="page-viewport no-scrollbar">
        <router-view />
        </main>

    </div>
    </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const isCollapsed = ref(false) // 默认展开，提升体验
const isDark = ref(false)

// 当前展开的二级菜单
const expandedMenus = ref<string[]>([])

// 5 大模块菜单架构定义 (完美的父子嵌套)
const menuStructure = [
    {
    name: '控制大盘',
    path: '/admin/dashboard',
    icon: '📊'
    },
    {
    name: '准入与用户',
    icon: '👥',
    children: [
        { name: '企业资质审核', path: '/admin/identity/enterprise' },
        { name: '个人求职管控', path: '/admin/identity/candidate' }
    ]
    },
    {
    name: '信创适配合规',
    icon: '🔌',
    children: [
        { name: '兼容性证书核验', path: '/admin/compliance/certs' },
        { name: '信创职位标签', path: '/admin/compliance/tags' }
    ]
    },
    {
    name: 'AI 智能匹配',
    icon: '🧠',
    children: [
        { name: '向量匹配调试', path: '/admin/ai-tuning/vector' },
        { name: '本地模型状态', path: '/admin/ai-tuning/model' }
    ]
    },
    {
    name: '系统设置与审计',
    icon: '⚙️',
    children: [
        { name: '管理员分权', path: '/admin/settings/sub-admin' },
        { name: '操作日志审计', path: '/admin/settings/audit' }
    ]
    }
]

// 切换侧边栏
const toggleSidebar = () => {
    isCollapsed.value = !isCollapsed.value
}

// 展开/折叠子菜单
const toggleSubMenu = (menuName: string) => {
    if (isCollapsed.value) {
    isCollapsed.value = false // 展开侧边栏才能看子菜单
    }
    const index = expandedMenus.value.indexOf(menuName)
    if (index > -1) {
    expandedMenus.value.splice(index, 1)
    } else {
    expandedMenus.value.push(menuName)
    }
}

// 检查当前二级子菜单是否有正在激活的子项
const isParentActive = (menu: any) => {
    if (!menu.children) return false
    return menu.children.some((sub: any) => route.path === sub.path)
}

// 动态 Header 标题
const currentPageTitle = computed(() => {
    const currentPath = route.path
    if (currentPath.includes('dashboard')) return 'Kirin 麒麟生态平台超级管理中心'
    if (currentPath.includes('enterprise')) return '企业资质合规准入'
    if (currentPath.includes('candidate')) return '个人求职信用与风控'
    if (currentPath.includes('certs')) return '信创兼容性证书审核'
    if (currentPath.includes('tags')) return '国产化职位标签策略'
    if (currentPath.includes('vector')) return 'AI 向量多维匹配调试'
    if (currentPath.includes('model')) return 'Ollama 模型与算力调度'
    if (currentPath.includes('sub-admin')) return '管理集群分权控制'
    if (currentPath.includes('audit')) return '国密安全通道日志审计'
    return '系统控制台'
})

// 主题切换逻辑
const toggleTheme = () => {
    isDark.value = !isDark.value
    const root = document.documentElement
    if (isDark.value) {
    root.classList.add('dark')
    localStorage.setItem('theme', 'dark')
    } else {
    root.classList.remove('dark')
    localStorage.setItem('theme', 'light')
    }
}

onMounted(() => {
    const savedTheme = localStorage.getItem('theme')
    if (savedTheme === 'dark') {
    isDark.value = true
    document.documentElement.classList.add('dark')
    } else {
    isDark.value = false
    document.documentElement.classList.remove('dark')
    }

    // 页面加载时自动展开活跃状态下的二级菜单
    menuStructure.forEach(menu => {
    if (menu.children && isParentActive(menu)) {
        expandedMenus.value.push(menu.name)
    }
    })
})

const handleLogout = () => {
    localStorage.clear()
    router.push('/login')
}
</script>

<style scoped>
.layout-container {
    height: 100vh;
    width: 100vw;
    display: flex;
    background-color: var(--bg-main);
    padding: 16px;
    overflow: hidden;
}

/* 侧边栏主框架 */
.sidebar {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 24px 0;
    background-color: var(--bg-card);
    border: 1px solid var(--border-color);
    border-radius: 24px;
    box-shadow: var(--shadow-card);
    transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1);
    z-index: 30;
}

.logo-toggle-btn {
    width: 44px;
    height: 44px;
    background: linear-gradient(135deg, #3b82f6 0%, #8b5cf6 100%);
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 24px;
    cursor: pointer;
    box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
    transition: transform 0.3s;
    flex-shrink: 0;
}
.logo-toggle-btn:hover {
    transform: scale(1.05);
}
.toggle-icon {
    width: 14px;
    height: 14px;
    border: 2px solid white;
    border-radius: 3px;
    transition: transform 0.4s;
}

.nav-menu {
    flex: 1;
    width: 100%;
    padding: 0 12px;
    display: flex;
    flex-direction: column;
    gap: 6px;
    overflow-y: auto;
}

.menu-group {
    width: 100%;
}

.nav-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 12px 16px;
    border-radius: 12px;
    color: var(--text-secondary);
    text-decoration: none;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.25s ease;
    user-select: none;
}
.nav-item:hover {
    color: var(--text-primary);
    background-color: rgba(59, 130, 246, 0.06);
}
.nav-item.active {
    color: #3b82f6;
    background-color: rgba(59, 130, 246, 0.1);
}

.nav-icon {
    font-size: 18px;
    display: inline-block;
    min-width: 20px;
}
.nav-name {
    font-size: 13px;
    margin-left: 12px;
    white-space: nowrap;
}

.parent-item {
    display: flex;
    justify-content: space-between;
}
.arrow-icon {
    font-size: 9px;
    transition: transform 0.3s ease;
    opacity: 0.6;
}
.arrow-icon.rotated {
    transform: rotate(180deg);
}

/* 子路由布局 */
.sub-menu-list {
    display: flex;
    flex-direction: column;
    gap: 4px;
    padding-left: 28px;
    margin-top: 4px;
    margin-bottom: 8px;
}
.sub-nav-item {
    display: flex;
    align-items: center;
    padding: 8px 12px;
    border-radius: 8px;
    color: var(--text-secondary);
    text-decoration: none;
    font-size: 12px;
    font-weight: 500;
    transition: all 0.2s;
}
.sub-nav-item:hover {
    color: var(--text-primary);
    background-color: rgba(0, 0, 0, 0.02);
}
:root.dark .sub-nav-item:hover {
    background-color: rgba(255, 255, 255, 0.03);
}
.sub-active {
    color: #3b82f6 !important;
    font-weight: 700;
}
.sub-dot {
    width: 4px;
    height: 4px;
    background-color: currentColor;
    border-radius: 50%;
    margin-right: 8px;
}
.sub-name {
    white-space: nowrap;
}

/* 底部管理员资料 */
.user-profile {
    width: 85%;
    padding: 10px;
    border-radius: 16px;
    display: flex;
    align-items: center;
    background-color: rgba(0, 0, 0, 0.02);
    border: 1px solid var(--border-color);
    margin-top: 16px;
    flex-shrink: 0;
}
:root.dark .user-profile {
    background-color: rgba(255, 255, 255, 0.02);
}
.avatar {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    background: rgba(59, 130, 246, 0.1);
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 16px;
    flex-shrink: 0;
}
.user-info {
    margin-left: 10px;
    overflow: hidden;
}
.username {
    font-size: 12px;
    font-weight: 700;
    color: var(--text-primary);
    white-space: nowrap;
}
.role {
    font-size: 9px;
    color: var(--text-secondary);
}

/* 主显示内容区 */
.main-content {
    flex: 1;
    display: flex;
    flex-direction: column;
    margin-left: 16px;
    overflow: hidden;
}
.header {
    height: 70px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 12px;
}
.header-title {
    font-size: 20px;
    font-weight: 800;
    color: var(--text-primary);
}
.header-subtitle {
    font-size: 11px;
    color: var(--text-secondary);
}
.header-actions {
    display: flex;
    align-items: center;
    gap: 12px;
}

.theme-toggle-btn {
    padding: 8px 14px;
    font-size: 12px;
    font-weight: 600;
    background-color: var(--bg-card);
    border: 1px solid var(--border-color);
    color: var(--text-primary);
    border-radius: 10px;
    cursor: pointer;
    transition: all 0.25s;
}
.theme-toggle-btn:hover {
    background-color: rgba(59, 130, 246, 0.05);
}
.logout-btn {
    padding: 8px 14px;
    font-size: 12px;
    font-weight: 600;
    background-color: rgba(239, 68, 68, 0.08);
    border: 1px solid rgba(239, 68, 68, 0.15);
    color: #ef4444;
    border-radius: 10px;
    cursor: pointer;
    transition: all 0.25s;
}
.logout-btn:hover {
    background-color: rgba(239, 68, 68, 0.15);
}

.page-viewport {
    flex: 1;
    background-color: var(--bg-card);
    border: 1px solid var(--border-color);
    border-radius: 24px;
    box-shadow: var(--shadow-card);
    overflow-y: auto;
    padding: 24px;
}

/* 高雅的折叠动画 */
.expand-enter-active, .expand-leave-active {
    transition: all 0.3s ease;
    max-height: 120px;
    overflow: hidden;
}
.expand-enter-from, .expand-leave-to {
    max-height: 0;
    opacity: 0;
}

.animate-fade-in {
    animation: fadeIn 0.2s ease-out forwards;
}
@keyframes fadeIn {
    from { opacity: 0; transform: scale(0.95); }
    to { opacity: 1; transform: scale(1); }
}
</style>