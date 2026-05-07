<template>
    <div class="max-w-[1200px] mx-auto p-8 animate-in fade-in duration-500">
    <div class="flex justify-between items-end mb-10">
        <div>
        <h1 class="text-3xl font-bold text-black tracking-tight">职位广场</h1>
        <p class="text-gray-400 text-sm mt-2 font-medium">基于您的技术能力图谱精准匹配</p>
        </div>
        <div class="relative">
        <input type="text" placeholder="搜索麒麟生态岗位..." class="pl-12 pr-6 py-3 bg-white border border-gray-100 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-black/5 transition-all w-[300px]" />
        <span class="absolute left-4 top-3.5 opacity-30">🔍</span>
        </div>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div v-for="i in 4" :key="i" class="bg-white p-8 rounded-[32px] border border-gray-100 hover:shadow-2xl hover:shadow-black/5 transition-all cursor-pointer group">
        <div class="flex justify-between items-start mb-6">
            <div class="w-14 h-14 bg-gray-50 rounded-2xl flex items-center justify-center font-bold text-xl group-hover:scale-110 transition-transform">麒</div>
            <div class="text-right">
            <span class="text-green-500 font-bold text-lg">25k - 40k</span>
            <p class="text-[10px] text-gray-400 font-bold uppercase tracking-widest mt-1">Beijing · Fulltime</p>
            </div>
        </div>
        <h2 class="text-xl font-bold text-black group-hover:text-blue-600 transition-colors">Linux 系统研发工程师</h2>
        <p class="text-sm text-gray-500 mt-2 font-medium mb-6">麒麟软件有限公司 · 生态适配部</p>
        <div class="flex gap-2">
            <span class="px-3 py-1 bg-gray-50 rounded-lg text-[10px] font-bold text-gray-400 border border-gray-100">AArch64</span>
            <span class="px-3 py-1 bg-gray-50 rounded-lg text-[10px] font-bold text-gray-400 border border-gray-100">Kernel</span>
        </div>
        </div>
    </div>
    </div>
</template>