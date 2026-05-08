<template>
    <div class="h-full w-full bg-[#F5F5F7] p-8 overflow-y-auto no-scrollbar font-sans select-none animate-fade-in">
    <div class="max-w-7xl mx-auto space-y-8">
        
        <div class="flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
        <div>
            <h1 class="text-3xl font-black tracking-tight text-black flex items-center gap-2">
            欢迎回来，HR Partner <span class="text-xl">✨</span>
            </h1>
            <p class="text-xs font-bold text-gray-400 uppercase tracking-widest mt-1.5">
            Kirin Talent Intelligence Console System v2.6
            </p>
        </div>
        <div class="flex items-center gap-2 bg-white px-4 py-2 rounded-full border border-gray-100 shadow-sm">
            <span class="w-2 h-2 rounded-full bg-emerald-500 animate-pulse"></span>
            <span class="text-[10px] font-black text-gray-500 uppercase tracking-wider">实时同步中 (2026-05-08)</span>
        </div>
        </div>

        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        <div 
            v-for="stat in stats" 
            :key="stat.title"
            class="bg-white rounded-[28px] border border-gray-100 p-6 flex flex-col justify-between shadow-sm hover:shadow-[0_12px_24px_rgba(0,0,0,0.02)] transition-all duration-300 group hover:-translate-y-1"
        >
            <div class="flex justify-between items-start">
            <span class="text-3xl">{{ stat.icon }}</span>
            <span :class="stat.trendClass" class="text-[10px] font-black px-2 py-1 rounded-full uppercase tracking-tighter">
                {{ stat.trend }}
            </span>
            </div>
            <div class="mt-8">
            <p class="text-[10px] font-black text-gray-400 uppercase tracking-widest">{{ stat.title }}</p>
            <div class="flex items-baseline gap-1.5 mt-1">
                <span class="text-3xl font-black text-black tracking-tight">{{ stat.value }}</span>
                <span class="text-xs font-bold text-gray-400">{{ stat.unit }}</span>
            </div>
            </div>
        </div>
        </div>

        <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        
        <div class="lg:col-span-2 bg-white rounded-[32px] border border-gray-100 p-8 shadow-sm flex flex-col justify-between">
            <div>
            <div class="flex justify-between items-center mb-6">
                <div>
                <h3 class="text-base font-black text-black">AI 初筛与面试流转效能</h3>
                <p class="text-[9px] font-bold text-gray-400 uppercase tracking-widest mt-0.5">AI Filter Efficiency & Flow Analytics</p>
                </div>
                <div class="flex gap-2">
                <button class="px-3 py-1.5 bg-neutral-100 text-neutral-800 text-[10px] font-black rounded-lg transition-all active:scale-95">本周</button>
                <button class="px-3 py-1.5 bg-black text-white text-[10px] font-black rounded-lg shadow-md transition-all active:scale-95">本月</button>
                </div>
            </div>

            <div class="h-64 w-full flex items-end justify-between px-4 pt-8 relative">
                <div class="absolute inset-x-0 top-0 h-full flex flex-col justify-between pointer-events-none opacity-40">
                <div class="border-t border-dashed border-neutral-100 w-full h-0"></div>
                <div class="border-t border-dashed border-neutral-100 w-full h-0"></div>
                <div class="border-t border-dashed border-neutral-100 w-full h-0"></div>
                <div class="border-t border-dashed border-neutral-100 w-full h-0"></div>
                </div>

                <svg class="absolute inset-0 w-full h-full p-4 pointer-events-none" viewBox="0 0 100 100" preserveAspectRatio="none">
                <defs>
                    <linearGradient id="chartGrad" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="0%" stop-color="#10B981" stop-opacity="0.25" />
                    <stop offset="100%" stop-color="#10B981" stop-opacity="0" />
                    </linearGradient>
                </defs>
                <path d="M 0,100 L 10,75 L 30,50 L 50,65 L 70,30 L 90,15 L 100,15 L 100,100 Z" fill="url(#chartGrad)"></path>
                <path d="M 0,100 L 10,75 L 30,50 L 50,65 L 70,30 L 90,15 L 100,15" fill="none" stroke="#10B981" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"></path>
                </svg>

                <div 
                v-for="(item, idx) in chartData" 
                :key="idx" 
                class="flex flex-col items-center gap-3 z-10 w-12 group/bar"
                >
                <div class="relative w-4 h-48 bg-neutral-50 rounded-full flex items-end overflow-hidden">
                    <div 
                    :style="{ height: item.percent + '%' }" 
                    class="w-full bg-gradient-to-t from-black to-neutral-800 rounded-full transition-all duration-1000 ease-out group-hover/bar:brightness-125"
                    ></div>
                </div>
                <span class="text-[10px] font-black text-gray-400 group-hover/bar:text-black transition-colors">{{ item.label }}</span>
                </div>
            </div>
            </div>

            <div class="flex items-center justify-between pt-6 mt-6 border-t border-gray-50 text-[10px] text-gray-400 font-bold uppercase">
            <span>数据由 麒麟AI大脑 提供实时分析反馈</span>
            <span class="text-black hover:underline cursor-pointer">导出深度报告 ➔</span>
            </div>
        </div>

        <div class="bg-white rounded-[32px] border border-gray-100 p-8 shadow-sm flex flex-col justify-between">
            <div class="space-y-6">
            <div class="flex justify-between items-center">
                <div>
                <h3 class="text-base font-black text-black">最新投递动态</h3>
                <p class="text-[9px] font-bold text-gray-400 uppercase tracking-widest mt-0.5">Real-time Job Applications</p>
                </div>
                <span class="text-[10px] bg-neutral-100 text-neutral-800 font-black px-2.5 py-1 rounded-full">3 未处理</span>
            </div>

            <div class="space-y-4 max-h-[300px] overflow-y-auto no-scrollbar">
                <div 
                v-for="app in applications" 
                :key="app.name"
                class="group p-4 bg-[#F5F5F7]/60 hover:bg-[#F5F5F7] rounded-2xl border border-transparent hover:border-black/5 transition-all duration-300 flex items-center justify-between"
                >
                <div class="flex items-center gap-3">
                    <div class="w-10 h-10 rounded-xl bg-white border border-gray-100 overflow-hidden shadow-inner flex-shrink-0">
                    <img :src="app.avatar" class="w-full h-full object-cover" />
                    </div>
                    <div>
                    <h4 class="text-xs font-black text-black flex items-center gap-1.5">
                        {{ app.name }}
                        <span class="text-[9px] bg-indigo-50 text-indigo-600 px-1.5 py-0.5 rounded-md font-bold">Match {{ app.match }}%</span>
                    </h4>
                    <p class="text-[10px] text-gray-400 font-semibold mt-0.5">{{ app.targetJob }}</p>
                    </div>
                </div>

                <div class="flex items-center gap-1.5 opacity-0 group-hover:opacity-100 transition-opacity duration-300">
                    <button @click="handleAction(app.name, 'approve')" class="w-7 h-7 bg-black hover:bg-neutral-800 text-white rounded-lg text-xs flex items-center justify-center shadow-md active:scale-90 transition-transform" title="通过筛选">✓</button>
                    <button @click="handleAction(app.name, 'reject')" class="w-7 h-7 bg-white hover:bg-rose-50 text-rose-500 rounded-lg text-xs flex items-center justify-center border border-gray-100 shadow-sm active:scale-90 transition-transform" title="不合适">✗</button>
                </div>
                </div>
            </div>
            </div>

            <button class="w-full py-3.5 bg-[#F5F5F7] hover:bg-neutral-100 text-black text-xs font-black rounded-2xl transition-all active:scale-[0.98] text-center mt-6">
            进入人才公海（处理全部 {{ totalPending }} 件投递）
            </button>
        </div>

        </div>

        <div class="bg-white rounded-[32px] border border-gray-100 p-8 shadow-sm">
        <div class="flex justify-between items-center mb-6">
            <div>
            <h3 class="text-base font-black text-black">核心在招信创岗位监测</h3>
            <p class="text-[9px] font-bold text-gray-400 uppercase tracking-widest mt-0.5">Active Job Openings & Funnel Monitoring</p>
            </div>
            <button class="text-xs font-black text-black hover:underline">发布新信创职位 ➔</button>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div 
            v-for="job in activeJobs" 
            :key="job.title"
            class="p-6 bg-[#F5F5F7]/40 rounded-2xl border border-neutral-100 hover:border-black/10 transition-all duration-300"
            >
            <div class="flex justify-between items-start">
                <span class="text-xs font-black text-black bg-white border border-gray-100 px-3 py-1 rounded-xl shadow-sm">{{ job.department }}</span>
                <span class="w-2 h-2 rounded-full bg-emerald-500"></span>
            </div>
            <h4 class="text-sm font-bold text-black mt-4">{{ job.title }}</h4>
            <p class="text-[10px] text-gray-400 font-medium mt-1">薪酬范围：{{ job.salary }} <span class="mx-1">|</span> 信创架构环境</p>

            <div class="mt-6 space-y-2">
                <div class="flex justify-between text-[10px] font-black text-gray-400 uppercase tracking-wide">
                <span>投递流转进度</span>
                <span class="text-black">{{ job.ratio }}%</span>
                </div>
                <div class="w-full h-1.5 bg-neutral-100 rounded-full overflow-hidden">
                <div :style="{ width: job.ratio + '%' }" class="h-full bg-black rounded-full"></div>
                </div>
            </div>
            </div>
        </div>
        </div>

    </div>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

// 总待处理投递数
const totalPending = ref(18)

// 四大核心指标卡数据
const stats = [
    { title: '发布中信创岗位', value: '8', unit: '个', icon: '🎯', trend: '+2 较上周', trendClass: 'bg-emerald-50 text-emerald-600' },
    { title: '简历收件箱', value: '142', unit: '份', icon: '📥', trend: '+34% 本周', trendClass: 'bg-emerald-50 text-emerald-600' },
    { title: 'AI 自动初筛率', value: '92', unit: '%', icon: '🤖', trend: '稳定运行', trendClass: 'bg-indigo-50 text-indigo-600' },
    { title: '待面试候选人', value: '24', unit: '人', icon: '💬', trend: '明日约面 5人', trendClass: 'bg-amber-50 text-amber-600' }
]

// AI 初筛走势图虚构数据 (代表一至六月 AI 处理率和简历流量)
const chartData = [
    { label: 'Jan', percent: 45 },
    { label: 'Feb', percent: 65 },
    { label: 'Mar', percent: 50 },
    { label: 'Apr', percent: 75 },
    { label: 'May', percent: 90 },
    { label: 'Jun', percent: 95 }
]

// 投递动态速递模拟数据
const applications = ref([
    { name: '叶文洁', targetJob: 'Linux 内核驱动开发', match: 96, avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Enya' },
    { name: '罗辑', targetJob: 'Rust 基础框架工程师', match: 91, avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Jude' },
    { name: '章北海', targetJob: 'ARM架构适配优化岗位', match: 88, avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=George' }
])

// 核心在招信创岗位状态监控
const activeJobs = [
    { title: 'Linux 内核适配与基础软件开发', department: '麒麟软件 (南京研发中心)', salary: '25K-40K', ratio: 78 },
    { title: '华为昇腾大模型工程优化岗', department: '华为技术有限公司 (计算产品线)', salary: '30K-55K', ratio: 95 },
    { title: 'Spring Boot 架构重构岗', department: '达梦数据库有限公司 (武汉总部)', salary: '18K-30K', ratio: 42 }
]

// 交互处理器
const handleAction = (name: string, type: 'approve' | 'reject') => {
    if (type === 'approve') {
    alert(`已通过候选人 [${name}] 的 AI 初筛，即将为其下发 AI 模拟面试直面邀请！`)
    } else {
    alert(`已将候选人 [${name}] 转入淘汰库。`)
    }
}
</script>

<style scoped>
.no-scrollbar::-webkit-scrollbar { display: none; }
.no-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }

.animate-fade-in {
    animation: fadeIn 0.4s cubic-bezier(0.16, 1, 0.3, 1) forwards;
}

@keyframes fadeIn {
    from { opacity: 0; transform: translateY(8px); }
    to { opacity: 1; transform: translateY(0); }
}
</style>