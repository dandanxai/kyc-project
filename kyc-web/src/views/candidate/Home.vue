<template>
<div class="w-full min-w-0 p-4 sm:p-8 animate-in fade-in slide-in-from-bottom-8 duration-1000 pb-20">
    
    <section class="relative min-h-[360px] lg:h-[420px] w-full rounded-[48px] overflow-hidden mb-12 shadow-2xl shadow-black/10 group bg-black">
    <div class="absolute inset-0">
        <div class="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[120%] h-[120%] bg-gradient-to-br from-blue-600/30 via-indigo-500/20 to-transparent blur-[120px] animate-pulse"></div>
    </div>
    
    <div class="relative z-10 h-full p-8 lg:p-16 flex flex-col justify-between">
        <div class="flex justify-between items-start">
        <div class="inline-flex items-center gap-2 px-4 py-2 bg-white/10 backdrop-blur-md rounded-full border border-white/10">
            <span class="w-2 h-2 bg-green-400 rounded-full animate-pulse"></span>
            <span class="text-[10px] font-black text-white uppercase tracking-widest">Kirin Intelligence Active</span>
        </div>
        <div class="text-right text-white/40 font-mono text-xs tracking-tighter">
            <p>LAST UPDATE: {{ currentDate }}</p>
            <p>NETWORK: STABLE</p>
        </div>
        </div>
        
        <div class="mt-8">
        <h1 class="text-5xl lg:text-8xl font-black text-white tracking-tighter mb-8 leading-[0.85]">
            进化你的<br/><span class="text-transparent bg-clip-text bg-gradient-to-r from-blue-200 via-gray-100 to-gray-500">技术图谱</span>
        </h1>
        <div class="flex flex-wrap gap-5">
            <button class="px-10 py-4 bg-white text-black rounded-2xl font-bold hover:scale-105 transition-all shadow-xl shadow-white/5">解析简历</button>
            <button class="px-10 py-4 bg-white/10 text-white backdrop-blur-md rounded-2xl font-bold border border-white/10 hover:bg-white/20 transition-all">探索岗位</button>
        </div>
        </div>
    </div>
    </section>

    <section class="mb-16">
    <div class="flex items-end gap-4 mb-8 px-2">
        <h2 class="text-3xl font-black text-black">ECO-PULSE</h2>
        <p class="text-xs text-gray-400 font-bold uppercase tracking-widest pb-1.5">/ 生态脉动</p>
    </div>
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div v-for="(news, i) in hotNews" :key="i" class="group bg-white/60 hover:bg-white border border-white p-8 rounded-[40px] shadow-sm hover:shadow-xl transition-all duration-500">
        <div class="text-[10px] font-black text-blue-600 uppercase mb-4">{{ news.tag }}</div>
        <h3 class="text-xl font-bold text-black mb-4 leading-snug group-hover:text-blue-600 transition-colors">{{ news.title }}</h3>
        <div class="flex justify-between items-center text-gray-400 text-[10px] font-bold">
            <span>{{ news.date }}</span>
            <span class="opacity-0 group-hover:opacity-100 transition-opacity">READ MORE →</span>
        </div>
        </div>
    </div>
    </section>

    <div class="grid grid-cols-12 gap-10">
    
    <div class="col-span-12 xl:col-span-8">
        <div class="flex justify-between items-center mb-8 px-2">
        <h2 class="text-2xl font-black text-black">TOP OPPORTUNITIES</h2>
        <button class="text-xs font-bold text-gray-400 hover:text-black">查看全部清单</button>
        </div>
        
        <div class="space-y-4">
        <div v-for="(job, i) in jobList" :key="i" class="bg-white/40 hover:bg-white p-8 rounded-[40px] border border-white flex flex-col sm:flex-row items-start sm:items-center justify-between group transition-all duration-700 gap-6">
            <div class="flex items-center gap-8">
            <div class="w-20 h-20 bg-black rounded-3xl flex-shrink-0 flex items-center justify-center font-bold text-3xl text-white shadow-xl shadow-black/10">
                {{ job.logo }}
            </div>
            <div>
                <h3 class="font-bold text-xl text-black mb-1 flex items-center gap-2">
                {{ job.title }}
                <span class="text-[10px] bg-green-100 text-green-600 px-2 py-0.5 rounded-md">AI 匹配 98%</span>
                </h3>
                <p class="text-sm font-medium text-gray-400">{{ job.company }} · {{ job.location }} · <span class="text-black">{{ job.salary }}</span></p>
            </div>
            </div>
            <div class="flex flex-col items-end gap-4">
            <div class="flex gap-2">
                <span v-for="tag in job.tags" :key="tag" class="px-3 py-1 bg-gray-100 text-[10px] font-bold text-gray-500 rounded-lg">{{ tag }}</span>
            </div>
            <button class="px-8 py-3 bg-black text-white rounded-2xl text-xs font-bold hover:scale-105 transition-all">立即投递</button>
            </div>
        </div>
        </div>
    </div>

    <div class="col-span-12 xl:col-span-4 space-y-8">
        <div class="bg-black rounded-[48px] p-8 text-white shadow-2xl relative overflow-hidden">
        <h3 class="text-lg font-black mb-8 uppercase tracking-widest">Growth Curve</h3>
        <div class="flex items-end justify-between h-32 gap-1 mb-8">
            <div v-for="h in [40, 70, 45, 90, 65, 80, 100]" :key="h" :style="{ height: h + '%' }" class="flex-1 bg-gradient-to-t from-blue-600 to-indigo-400 rounded-t-lg"></div>
        </div>
        <div class="flex justify-between items-center">
            <div>
            <p class="text-[10px] text-gray-500 font-bold uppercase mb-1">当前评分</p>
            <p class="text-3xl font-black italic">89.4</p>
            </div>
            <router-link to="/candidate/dashboard" class="px-6 py-3 bg-white/10 hover:bg-white/20 rounded-2xl text-[10px] font-bold backdrop-blur-md transition-all">查看详情报告</router-link>
        </div>
        </div>

        <div class="bg-white rounded-[48px] p-8 border border-white shadow-sm">
        <h3 class="text-sm font-black mb-6 uppercase tracking-widest text-gray-400">生态周报</h3>
        <div class="space-y-6">
            <div v-for="news in techNews" :key="news.title" class="flex gap-4 cursor-pointer group">
            <div class="w-12 h-12 bg-gray-50 rounded-2xl flex-shrink-0 flex items-center justify-center group-hover:bg-black group-hover:text-white transition-all text-lg">{{ news.icon }}</div>
            <div>
                <p class="text-xs font-bold text-black line-clamp-2 leading-snug">{{ news.title }}</p>
                <p class="text-[10px] text-gray-400 font-medium mt-1">{{ news.views }} 阅览</p>
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

const currentDate = new Date().toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' })

const hotNews = ref([
{ tag: 'Industry', title: '2026 信创产业人才供需报告发布：基础软件人才缺口达 30 万', date: 'MAY 08' },
{ tag: 'Tech', title: '银河麒麟桌面操作系统 V10 SP3 正式发布，全面增强 AI 加速能力', date: 'MAY 05' },
{ tag: 'Ecosystem', title: 'OpenHarmony 全球开发者大会：探讨国产操作系统的下一个十年', date: 'APR 28' },
])

const jobList = ref([
{ title: '高级 Linux 内核研发', company: '麒麟软件', location: '北京', salary: '35k-55k', logo: '麒', tags: ['C/C++', 'Kernel', 'Security'] },
{ title: '分布式系统架构师', company: '华为', location: '深圳', salary: '45k-80k', logo: '华', tags: ['Rust', 'K8s', 'Cloud'] },
{ title: '信创解决方案专家', company: '中电科', location: '南京', salary: '30k-50k', logo: '中', tags: ['Qt', 'Linux', 'Adaptation'] },
])

const techNews = ref([
{ icon: '📦', title: '如何在银河麒麟下高效配置 Rust 开发环境？', views: '2.4k' },
{ icon: '🛠️', title: '手把手教你为 OpenHarmony 贡献第一个 PR', views: '1.8k' },
{ icon: '🔒', title: '国产化适配中常见的安全合规性检查点解析', views: '3.1k' },
])
</script>