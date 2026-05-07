<template>
    <div class="w-full bg-[#F5F5F7] selection:bg-black selection:text-white">
    <nav class="fixed top-0 w-full h-20 bg-white/80 backdrop-blur-2xl z-50 border-b border-gray-100/50 flex items-center justify-between px-12">
        <div class="flex items-center gap-10">
        <div class="text-2xl font-black tracking-tighter">KYC.</div>
        <div class="hidden md:flex gap-8 text-[13px] font-semibold text-gray-500">
            <a href="#" class="hover:text-black transition-colors">生态招聘</a>
            <a href="#" class="hover:text-black transition-colors">人才图谱</a>
            <a href="#" class="hover:text-black transition-colors">关于信创</a>
        </div>
        </div>
        <div class="flex items-center gap-4">
        <router-link to="/login" class="px-6 py-2.5 text-[13px] font-bold hover:opacity-70 transition-all">登录</router-link>
        <router-link to="/register" class="px-6 py-2.5 bg-black text-white rounded-full text-[13px] font-bold hover:shadow-xl hover:shadow-black/10 transition-all active:scale-95">开始入驻</router-link>
        </div>
    </nav>

    <section class="pt-40 pb-20 px-6">
        <div class="max-w-[1200px] mx-auto text-center">
        <div class="inline-block px-4 py-1.5 bg-blue-50 text-blue-600 rounded-full text-[11px] font-bold tracking-widest mb-6 animate-fade-in">
            2026 KIRIN ECOSYSTEM TALENT MAP
        </div>
        <h1 class="text-6xl md:text-8xl font-bold tracking-tighter text-black mb-8 leading-[1.1]">
            定义国产化时代的<br/>
            <span class="text-transparent bg-clip-text bg-gradient-to-r from-gray-400 to-black">人才数字化资产</span>
        </h1>
        <p class="max-w-2xl mx-auto text-gray-400 text-lg md:text-xl font-medium leading-relaxed mb-12">
            基于 DeepSeek-R1 架构，深度链接麒麟软件生态企业，为每一位开发者构建独一无二的能力图谱。
        </p>
        <div class="flex flex-col md:flex-row justify-center gap-4">
            <button class="px-10 py-5 bg-black text-white rounded-3xl font-bold text-lg hover:shadow-2xl hover:shadow-black/20 transition-all">寻找心仪职位</button>
            <button class="px-10 py-5 bg-white border border-gray-200 rounded-3xl font-bold text-lg hover:bg-gray-50 transition-all">了解 AI 引擎</button>
        </div>
        </div>
    </section>

    <section class="py-24 bg-white">
        <div class="max-w-[1200px] mx-auto px-6">
        <div class="flex justify-between items-end mb-16">
            <div>
            <h2 class="text-4xl font-bold tracking-tight mb-4">热门招聘</h2>
            <p class="text-gray-400 font-medium">实时同步麒麟生态伙伴最新人才需求</p>
            </div>
            <button class="text-blue-600 font-bold hover:underline">浏览全部 12,040+ 岗位</button>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div v-for="i in 6" :key="i" class="p-8 rounded-[40px] border border-gray-100 bg-[#F9F9FB] hover:bg-white hover:shadow-2xl hover:shadow-black/5 transition-all cursor-pointer group">
            <div class="flex justify-between mb-8">
                <div class="w-14 h-14 bg-white rounded-2xl flex items-center justify-center shadow-sm group-hover:scale-110 transition-transform">
                <span class="text-xs font-bold">LOGO</span>
                </div>
                <div class="text-right">
                <p class="text-sm font-bold text-black">25k - 45k</p>
                <p class="text-[10px] text-gray-400 font-bold">14薪 · 北京</p>
                </div>
            </div>
            <h3 class="text-xl font-bold mb-2 group-hover:text-blue-600 transition-colors">高级内核调优专家</h3>
            <p class="text-sm text-gray-500 mb-6 font-medium">麒麟软件有限公司</p>
            <div class="flex flex-wrap gap-2">
                <span class="px-3 py-1 bg-white rounded-full text-[10px] font-bold text-gray-400 border border-gray-100">RUST</span>
                <span class="px-3 py-1 bg-white rounded-full text-[10px] font-bold text-gray-400 border border-gray-100">LINUX</span>
            </div>
            </div>
        </div>
        </div>
    </section>

    <footer class="py-20 bg-black text-white text-center">
        <div class="opacity-30 text-[10px] font-mono tracking-[0.5em] mb-4">POWERED BY KYLIN AI LAB</div>
        <p class="text-xl font-light">每一个开发者，都是国产软件生态的基石。</p>
    </footer>
    </div>
</template>

<script setup lang="ts">
// 这里可以加入一些 Aos.js 或者 ScrollReveal 的滚动效果逻辑
</script>