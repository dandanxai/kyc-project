<template>
    <div class="h-full w-full bg-[#EFEFEF] overflow-y-auto no-scrollbar font-sans pb-24 relative select-none">
    
    <div class="absolute top-[-10%] left-[-10%] w-[50%] h-[600px] rounded-full bg-indigo-300/20 blur-[130px] pointer-events-none"></div>
    <div class="absolute top-[40%] right-[-10%] w-[50%] h-[700px] rounded-full bg-purple-300/20 blur-[150px] pointer-events-none"></div>
    <div class="absolute bottom-[5%] left-[10%] w-[50%] h-[600px] rounded-full bg-emerald-300/20 blur-[130px] pointer-events-none"></div>
    
    <section class="max-w-6xl mx-auto px-8 pt-16 pb-12 z-10 relative">
        <div class="inline-flex items-center gap-1.5 px-3 py-1 bg-white/80 backdrop-blur-md rounded-full mb-4 ring-1 ring-black/[0.04] shadow-sm">
        <span class="w-1.5 h-1.5 bg-indigo-600 rounded-full animate-pulse"></span>
        <span class="text-[10px] font-black tracking-widest uppercase text-neutral-500">Kirin Industry-Insights v4.5</span>
        </div>
        
        <div class="flex flex-col md:flex-row md:items-end justify-between gap-6">
        <div>
            <h1 class="text-4xl md:text-5xl font-black text-neutral-950 tracking-tight leading-tight">
            信创产业全景<br>
            <span class="bg-clip-text text-transparent bg-gradient-to-r from-indigo-600 to-purple-600">与底层技术数据洞察</span>
            </h1>
            <p class="text-xs font-semibold text-neutral-400 mt-4 max-w-lg leading-relaxed">
            实时捕捉国产基础软件生态、大模型昇腾算力及达梦关系型数据库的底层效能波动。通过直观的物理量化图表，穿透信创技术迷雾。
            </p>
        </div>
        
        <div class="flex gap-4">
            <div class="bg-white/80 backdrop-blur-xl px-5 py-3 rounded-2xl ring-1 ring-black/[0.03] shadow-[0_8px_24px_rgba(0,0,0,0.02)]">
            <span class="block text-[9px] font-bold text-neutral-400 uppercase tracking-wider">监测核心维度</span>
            <span class="text-xl font-mono font-black text-indigo-600">3 Sectors</span>
            </div>
            <div class="bg-white/80 backdrop-blur-xl px-5 py-3 rounded-2xl ring-1 ring-black/[0.03] shadow-[0_8px_24px_rgba(0,0,0,0.02)]">
            <span class="block text-[9px] font-bold text-neutral-400 uppercase tracking-wider">数据刷新延迟</span>
            <span class="text-xl font-mono font-black text-neutral-950">Realtime</span>
            </div>
        </div>
        </div>
    </section>

    <section class="max-w-6xl mx-auto px-8 py-8 z-10 relative">
        <div class="bg-white/80 backdrop-blur-2xl rounded-[32px] ring-1 ring-black/[0.04] shadow-[0_24px_60px_-15px_rgba(0,0,0,0.04)] p-8 md:p-12">
        <div class="grid grid-cols-1 lg:grid-cols-12 gap-12 items-center">
            
            <div class="lg:col-span-5 space-y-6">
            <span class="inline-block text-[9px] bg-indigo-600 text-white px-2.5 py-1 rounded font-black tracking-widest uppercase">
                01 / OS & KERNEL PROFILE
            </span>
            <h3 class="text-2xl font-black text-neutral-950 tracking-tight leading-snug">
                信创内核适配加速：<br>零拷贝技术带来的吞吐跃迁
            </h3>
            <p class="text-xs text-neutral-500 font-semibold leading-relaxed">
                传统的物理网络和磁盘 I/O 需要在用户空间与内核空间之间进行多次繁重的上下文切换和数据内存拷贝。通过引入 **mmap 共享内存映射** 与 **DMA（直接内存访问）** 级零拷贝技术，系统在多核 CPU 下的拷贝时延呈现断崖式下跌。
            </p>
            
            <div class="bg-indigo-500/[0.03] ring-1 ring-indigo-500/10 p-5 rounded-2xl">
                <span class="text-[10px] font-black text-indigo-600 tracking-wider block uppercase mb-1">💡 研判结论与破局点</span>
                <p class="text-[11px] text-neutral-600 font-semibold leading-relaxed">
                在面试中聊到多核（SMP）架构时，若能拿出该组吞吐对比数据，并指出“零拷贝消除了 CPU 的数据搬运压力，使其能更专注于执行内核临界区代码”，将是绝对的加分项。
                </p>
            </div>
            </div>

            <div class="lg:col-span-7 bg-neutral-950 rounded-2xl p-6 ring-1 ring-white/[0.05] shadow-[0_12px_36px_rgba(0,0,0,0.3)] relative overflow-hidden group">
            <div class="flex justify-between items-center mb-6">
                <div>
                <span class="text-[9px] text-indigo-400 font-mono font-black uppercase tracking-widest">PERFORMANCE MONITOR</span>
                <h4 class="text-xs font-black text-white mt-1">内核态-用户态数据拷贝开销对比 (ms)</h4>
                </div>
                <div class="flex gap-3 text-[9px] font-bold">
                <span class="flex items-center gap-1.5 text-rose-500"><span class="w-1.5 h-1.5 bg-rose-500 rounded-full"></span>传统方式</span>
                <span class="flex items-center gap-1.5 text-indigo-400"><span class="w-1.5 h-1.5 bg-indigo-400 rounded-full"></span>零拷贝技术</span>
                </div>
            </div>

            <div class="w-full h-48 relative">
                <svg viewBox="0 0 500 200" class="w-full h-full overflow-visible">
                <line x1="0" y1="40" x2="500" y2="40" stroke="rgba(255,255,255,0.05)" stroke-dasharray="4"/>
                <line x1="0" y1="100" x2="500" y2="100" stroke="rgba(255,255,255,0.05)" stroke-dasharray="4"/>
                <line x1="0" y1="160" x2="500" y2="160" stroke="rgba(255,255,255,0.05)" stroke-dasharray="4"/>
                
                <defs>
                    <linearGradient id="grad-red" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="0%" stop-color="rgb(244, 63, 94)" stop-opacity="0.2"/>
                    <stop offset="100%" stop-color="rgb(244, 63, 94)" stop-opacity="0.0"/>
                    </linearGradient>
                    <linearGradient id="grad-blue" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="0%" stop-color="rgb(129, 140, 248)" stop-opacity="0.25"/>
                    <stop offset="100%" stop-color="rgb(129, 140, 248)" stop-opacity="0.0"/>
                    </linearGradient>
                </defs>

                <path d="M 0 50 L 100 45 L 200 120 L 300 130 L 400 160 L 500 175 L 500 200 L 0 200 Z" fill="url(#grad-red)"/>
                <path d="M 0 50 L 100 45 L 200 120 L 300 130 L 400 160 L 500 175" fill="none" stroke="rgb(244, 63, 94)" stroke-width="2.5" stroke-linecap="round"/>

                <path d="M 0 170 L 100 165 L 200 180 L 300 182 L 400 185 L 500 188 L 500 200 L 0 200 Z" fill="url(#grad-blue)"/>
                <path d="M 0 170 L 100 165 L 200 180 L 300 182 L 400 185 L 500 188" fill="none" stroke="rgb(129, 140, 248)" stroke-width="3" stroke-linecap="round"/>

                <circle cx="200" cy="120" r="4" fill="rgb(244, 63, 94)" class="animate-pulse"/>
                <circle cx="200" cy="180" r="4" fill="rgb(129, 140, 248)"/>
                </svg>
            </div>
            
            <div class="flex justify-between text-[10px] font-mono text-neutral-500 mt-4">
                <span>数据负载: 64KB</span>
                <span>128KB</span>
                <span>256KB</span>
                <span>512KB</span>
                <span>1024KB</span>
                <span>2048KB</span>
            </div>
            </div>

        </div>
        </div>
    </section>

    <section class="max-w-6xl mx-auto px-8 py-8 z-10 relative">
        <div class="bg-white/80 backdrop-blur-2xl rounded-[32px] ring-1 ring-black/[0.04] shadow-[0_24px_60px_-15px_rgba(0,0,0,0.04)] p-8 md:p-12">
        <div class="grid grid-cols-1 lg:grid-cols-12 gap-12 items-center">
            
            <div class="lg:col-span-7 order-last lg:order-first bg-neutral-950 rounded-2xl p-6 ring-1 ring-white/[0.05] shadow-[0_12px_36px_rgba(0,0,0,0.3)] flex flex-col md:flex-row items-center gap-8 group">
            
            <div class="relative w-44 h-44 flex-shrink-0">
                <svg viewBox="0 0 100 100" class="w-full h-full transform -rotate-90">
                <circle cx="50" cy="50" r="40" stroke="rgba(255, 255, 255, 0.05)" stroke-width="8" fill="none" />
                
                <circle cx="50" cy="50" r="40" stroke="rgb(168, 85, 247)" stroke-width="8" stroke-dasharray="251.2" stroke-dashoffset="75.3" fill="none" class="transition-all duration-1000" />
                <circle cx="50" cy="50" r="40" stroke="rgb(59, 130, 246)" stroke-width="8" stroke-dasharray="251.2" stroke-dashoffset="175.8" fill="none" class="transition-all duration-1000" />
                <circle cx="50" cy="50" r="40" stroke="rgb(16, 185, 129)" stroke-width="8" stroke-dasharray="251.2" stroke-dashoffset="213.5" fill="none" class="transition-all duration-1000" />
                </svg>
                <div class="absolute inset-0 flex flex-col items-center justify-center">
                <span class="text-[9px] text-neutral-400 font-bold uppercase tracking-wider">CANN 效率</span>
                <span class="text-xl font-mono font-black text-white">94%</span>
                </div>
            </div>

            <div class="flex-1 space-y-4 w-full">
                <span class="text-[9px] text-purple-400 font-mono font-black uppercase tracking-widest block">NPU PROFILE</span>
                <h4 class="text-xs font-black text-white">昇腾 NPU 大模型推理阶段算力分配</h4>
                
                <div class="space-y-2.5">
                <div class="flex justify-between items-center text-[11px] border-b border-white/5 pb-1.5">
                    <span class="flex items-center gap-2 text-neutral-300 font-semibold">
                    <span class="w-2 h-2 rounded-full bg-purple-500"></span>Prefill (首字预填充)
                    </span>
                    <span class="font-mono font-bold text-white">70%</span>
                </div>
                <div class="flex justify-between items-center text-[11px] border-b border-white/5 pb-1.5">
                    <span class="flex items-center gap-2 text-neutral-300 font-semibold">
                    <span class="w-2 h-2 rounded-full bg-blue-500"></span>Attention (算子注意力计算)
                    </span>
                    <span class="font-mono font-bold text-white">15%</span>
                </div>
                <div class="flex justify-between items-center text-[11px]">
                    <span class="flex items-center gap-2 text-neutral-300 font-semibold">
                    <span class="w-2 h-2 rounded-full bg-emerald-500"></span>KV Cache (动态缓存载入)
                    </span>
                    <span class="font-mono font-bold text-white">15%</span>
                </div>
                </div>
            </div>

            </div>

            <div class="lg:col-span-5 space-y-6">
            <span class="inline-block text-[9px] bg-rose-600 text-white px-2.5 py-1 rounded font-black tracking-widest uppercase">
                02 / AI COMPUTE POWER
            </span>
            <h3 class="text-2xl font-black text-neutral-950 tracking-tight leading-snug">
                国产 AI 算力崛起：<br>精细化 NPU 算子能效优化
            </h3>
            <p class="text-xs text-neutral-500 font-semibold leading-relaxed">
                大模型（如 DeepSeek）部署在国产昇腾硬件上时，算力瓶颈通常不是原始的 FP16 浮点吞吐，而是在首字生成（Prefill 阶段）对矩阵乘法单元（Cube Core）的极限压榨。对 Attention 算子和 KV 缓存进行深度合并和量化，是释放算力余量的核心。
            </p>
            
            <div class="bg-rose-500/[0.03] ring-1 ring-rose-500/10 p-5 rounded-2xl">
                <span class="text-[10px] font-black text-rose-600 tracking-wider block uppercase mb-1">💡 研判结论与破局点</span>
                <p class="text-[11px] text-neutral-600 font-semibold leading-relaxed">
                面试时重点阐述你在项目开发中引入 SSE（流式推送）和精细化缓存映射对齐，说明你不仅仅懂算法，更懂得如何榨干信创服务器每一帧算力。
                </p>
            </div>
            </div>

        </div>
        </div>
    </section>

    <section class="max-w-6xl mx-auto px-8 py-8 z-10 relative">
        <div class="bg-white/80 backdrop-blur-2xl rounded-[32px] ring-1 ring-black/[0.04] shadow-[0_24px_60px_-15px_rgba(0,0,0,0.04)] p-8 md:p-12">
        <div class="grid grid-cols-1 lg:grid-cols-12 gap-12 items-center">
            
            <div class="lg:col-span-5 space-y-6">
            <span class="inline-block text-[9px] bg-emerald-600 text-white px-2.5 py-1 rounded font-black tracking-widest uppercase">
                03 / RESTRUCTURING DATABASE
            </span>
            <h3 class="text-2xl font-black text-neutral-950 tracking-tight leading-snug">
                达梦数据库重构：<br>高并发高容灾环境吞吐测试
            </h3>
            <p class="text-xs text-neutral-500 font-semibold leading-relaxed">
                企业应用由 MySQL/Oracle 重构向达梦 DM8 国产数据库迁移时，连接池容易在极度并发写操作时产生瞬间死锁和连接泄露。通过加入**高可用多数据源降级策略**和 HikariCP 参数优化，吞吐稳定性将得到最大化保驾。
            </p>
            
            <div class="bg-emerald-500/[0.03] ring-1 ring-emerald-500/10 p-5 rounded-2xl">
                <span class="text-[10px] font-black text-emerald-600 tracking-wider block uppercase mb-1">💡 研判结论与破局点</span>
                <p class="text-[11px] text-neutral-600 font-semibold leading-relaxed">
                若面试官追问底层兼容性，大方介绍你通过重写持久层动态 AST（抽象语法树）拦截，无感知翻译大小写映射冲突的实战策略。
                </p>
            </div>
            </div>

            <div class="lg:col-span-7 bg-neutral-950 rounded-2xl p-6 ring-1 ring-white/[0.05] shadow-[0_12px_36px_rgba(0,0,0,0.3)] relative overflow-hidden group">
            <div class="flex justify-between items-center mb-6">
                <div>
                <span class="text-[9px] text-emerald-400 font-mono font-black uppercase tracking-widest">STRESS TESTING MATRIX</span>
                <h4 class="text-xs font-black text-white mt-1">达梦 DM8 数据库高负载运行吞吐稳定性波动 (tps)</h4>
                </div>
                <span class="text-[9px] font-mono font-black text-emerald-500 bg-emerald-500/10 px-2.5 py-1 rounded">
                均值波动 1.2% (极优)
                </span>
            </div>

            <div class="w-full h-48 relative">
                <svg viewBox="0 0 500 200" class="w-full h-full overflow-visible">
                <defs>
                    <linearGradient id="grad-green" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="0%" stop-color="rgb(16, 185, 129)" stop-opacity="0.2"/>
                    <stop offset="100%" stop-color="rgb(16, 185, 129)" stop-opacity="0.0"/>
                    </linearGradient>
                </defs>

                <line x1="0" y1="50" x2="500" y2="50" stroke="rgba(255,255,255,0.05)"/>
                <line x1="0" y1="100" x2="500" y2="100" stroke="rgba(255,255,255,0.05)"/>
                <line x1="0" y1="150" x2="500" y2="150" stroke="rgba(255,255,255,0.05)"/>

                <path d="M 0 100 Q 50 80, 100 105 T 200 95 T 300 110 T 400 90 T 500 100 L 500 200 L 0 200 Z" fill="url(#grad-green)"/>
                
                <path d="M 0 100 Q 50 80, 100 105 T 200 95 T 300 110 T 400 90 T 500 100" fill="none" stroke="rgb(16, 185, 129)" stroke-width="3" stroke-linecap="round"/>
                
                <circle cx="200" cy="95" r="4" fill="rgb(16, 185, 129)" class="animate-ping"/>
                <circle cx="200" cy="95" r="4" fill="rgb(16, 185, 129)"/>
                </svg>
            </div>

            <div class="flex justify-between text-[10px] font-mono text-neutral-500 mt-4">
                <span>压测进程: 10m</span>
                <span>20m</span>
                <span>30m</span>
                <span>40m</span>
                <span>50m</span>
                <span>60m (运行完毕)</span>
            </div>
            </div>

        </div>
        </div>
    </section>

    </div>
</template>

<script setup lang="ts">
// 干净纯粹、高度聚焦图表呈现的 Apple 官网风格长页面逻辑
</script>

<style scoped>
.no-scrollbar::-webkit-scrollbar { display: none; }
.no-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }
</style>