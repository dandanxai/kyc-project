<template>
    <div class="p-8 max-w-[1200px] mx-auto animate-in fade-in duration-700">
    <section class="relative h-[300px] w-full rounded-[40px] overflow-hidden mb-10 bg-black text-white p-12 flex flex-col justify-center">
        <div class="relative z-10">
        <h1 class="text-5xl font-bold tracking-tighter mb-4">探索麒麟生态机会</h1>
        <p class="text-gray-400 text-lg max-w-lg">基于 AI 驱动的人才图谱，连接 10,000+ 信创产业岗位。</p>
        </div>
        <div class="absolute right-[-10%] top-[-20%] w-[500px] h-[500px] bg-gradient-to-br from-blue-600/30 to-purple-600/30 blur-[100px]"></div>
    </section>

    <div class="grid grid-cols-12 gap-8">
        <div class="col-span-12 lg:col-span-8 space-y-6">
        <div class="flex justify-between items-center">
            <h2 class="text-2xl font-bold">精选职位</h2>
            <button class="text-sm font-bold text-blue-600">查看全部</button>
        </div>
        
        <div v-for="i in 3" :key="i" class="bg-white p-6 rounded-[32px] border border-gray-100 flex items-center gap-6 hover:shadow-xl hover:shadow-black/5 transition-all cursor-pointer group">
            <div class="w-16 h-16 bg-gray-50 rounded-2xl flex items-center justify-center font-bold text-2xl group-hover:bg-black group-hover:text-white transition-all">麒</div>
            <div class="flex-1">
            <h3 class="font-bold text-lg">C++ 驱动开发工程师</h3>
            <p class="text-gray-400 text-sm">麒麟软件 · 北京 · 20k-40k</p>
            </div>
            <button class="px-6 py-2 bg-gray-100 rounded-full text-xs font-bold group-hover:bg-black group-hover:text-white transition-all">申请</button>
        </div>
        </div>

        <div class="col-span-12 lg:col-span-4 space-y-6">
        <div class="bg-blue-600 rounded-[32px] p-8 text-white shadow-xl shadow-blue-200">
            <h3 class="text-xl font-bold mb-4">能力图谱尚未激活</h3>
            <p class="text-sm opacity-80 mb-6 leading-relaxed">上传简历通过 AI 深度解析，获取专属你的国产化技术画像。</p>
            <router-link to="/candidate/dashboard" class="block w-full py-4 bg-white text-blue-600 text-center rounded-2xl font-bold text-sm shadow-lg">立即解析</router-link>
        </div>
        
        <div class="bg-white rounded-[32px] p-8 border border-gray-100">
            <h3 class="font-bold mb-4">热门技术栈</h3>
            <div class="flex flex-wrap gap-2">
            <span v-for="tag in ['OpenHarmony', 'Linux Kernel', 'Qt', 'Rust']" :key="tag" class="px-3 py-1.5 bg-gray-50 rounded-lg text-[10px] font-bold text-gray-500 border border-gray-100">{{tag}}</span>
            </div>
        </div>
        </div>
    </div>
    </div>
</template>

<script setup lang="ts">
// 首页逻辑
</script>