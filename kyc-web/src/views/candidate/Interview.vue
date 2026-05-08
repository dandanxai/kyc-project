<template>
    <div class="h-full w-full flex flex-col p-8 bg-[#F5F5F7] overflow-hidden font-sans relative">
    
    <header class="flex-shrink-0 mb-6 z-10 flex justify-between items-end">
        <div>
        <div class="inline-flex items-center gap-1.5 px-3 py-1 bg-black/[0.04] rounded-full mb-1.5">
            <span class="w-1.5 h-1.5 bg-indigo-600 rounded-full animate-pulse"></span>
            <span class="text-[10px] font-bold tracking-widest uppercase text-neutral-500">Kirin Interview-Tactics v2.0</span>
        </div>
        <h2 class="text-3xl font-black text-neutral-900 tracking-tight">AI 模拟面试工作台</h2>
        </div>
        <div class="flex gap-4 text-[11px] font-bold text-neutral-400 bg-white px-5 py-2.5 rounded-2xl border border-neutral-100 shadow-sm">
        <span>待演练队列: <span class="text-neutral-900 font-mono font-black text-xs">{{ pendingCount }}</span></span>
        <span class="w-[1px] bg-neutral-200"></span>
        <span>AI 已就绪: <span class="text-emerald-600 font-mono font-black text-xs">{{ readyCount }}</span></span>
        </div>
    </header>

    <div class="flex-1 min-h-0 w-full flex flex-row gap-6 overflow-hidden">
        
        <div class="flex-1 min-w-[380px] bg-white rounded-[28px] shadow-[0_8px_30px_rgb(0,0,0,0.02)] border border-neutral-100 flex flex-col h-full overflow-hidden">
        
        <div class="p-6 border-b border-neutral-100 flex-shrink-0 space-y-4">
            <div class="flex bg-[#F5F5F7] p-1 rounded-xl">
            <button 
                @click="activeCategory = 'pending'"
                :class="[activeCategory === 'pending' ? 'bg-white text-neutral-900 shadow-sm font-black' : 'text-neutral-400 font-bold']"
                class="flex-1 py-2.5 text-xs rounded-lg transition-all flex items-center justify-center gap-2"
            >
                ⏳ 待面试队列
            </button>
            <button 
                @click="activeCategory = 'all'"
                :class="[activeCategory === 'all' ? 'bg-white text-neutral-900 shadow-sm font-black' : 'text-neutral-400 font-bold']"
                class="flex-1 py-2.5 text-xs rounded-lg transition-all flex items-center justify-center gap-2"
            >
                🌐 职位大厅
            </button>
            </div>

            <div class="relative flex items-center">
            <span class="absolute left-4 text-neutral-400 text-sm">🔍</span>
            <input 
                v-model="searchQuery"
                type="text" 
                placeholder="检索职位或企业名称..." 
                class="w-full pl-10 pr-4 py-3 bg-[#F5F5F7] rounded-xl text-xs font-semibold text-neutral-900 focus:outline-none placeholder-neutral-400 border border-transparent focus:border-neutral-200 transition-all"
            />
            </div>
        </div>

        <div class="flex-1 overflow-y-auto no-scrollbar p-6 space-y-3.5">
            <div 
            v-for="job in filteredJobs" 
            :key="job.id"
            @click="selectJob(job)"
            :class="[activeJob && activeJob.id === job.id ? 'bg-[#F5F5F7]/80 ring-1 ring-neutral-200/50' : 'hover:bg-[#FAFAFC]']"
            class="p-5 rounded-2xl cursor-pointer transition-all border border-neutral-100/50 flex flex-col gap-4 relative overflow-hidden"
            >
            <div class="flex justify-between items-start">
                <div class="min-w-0 flex-1">
                <span class="text-[10px] font-mono font-bold text-neutral-400 tracking-wider block uppercase">{{ job.company }}</span>
                <h4 class="text-sm font-black text-neutral-900 truncate mt-1.5 leading-tight">{{ job.title }}</h4>
                </div>
                <span class="text-xs font-mono font-black text-indigo-600 pl-3">Match {{ job.matchRate }}%</span>
            </div>

            <div class="flex items-center justify-between border-t border-neutral-100/80 pt-3.5">
                
                <template v-if="activeCategory === 'pending'">
                <div class="flex items-center gap-2">
                    <span class="w-2 h-2 rounded-full" :class="[job.aiReady ? 'bg-emerald-500 animate-pulse' : 'bg-rose-500']"></span>
                    <span class="text-xs font-bold" :class="[job.aiReady ? 'text-emerald-600' : 'text-rose-500']">
                    {{ job.aiReady ? 'AI 面试已就绪' : 'AI 面试未准备' }}
                    </span>
                </div>
                
                <button 
                    v-if="!job.aiReady" 
                    @click.stop="prepareJob(job)"
                    class="px-3.5 py-1.5 bg-black text-white text-[10px] font-black rounded-lg hover:bg-neutral-800 transition-all active:scale-95 shadow-sm"
                >
                    去准备 ⚡
                </button>
                </template>

                <template v-else>
                <span class="text-[10px] font-medium text-neutral-400">更新于: {{ job.date }}</span>
                
                <button 
                    v-if="!job.isAdded" 
                    @click.stop="addToPending(job)"
                    class="px-4 py-1.5 bg-indigo-600 text-white text-[10px] font-black rounded-lg hover:bg-indigo-700 transition-all active:scale-95 shadow-sm"
                >
                    ＋ 加入待面试
                </button>
                <span v-else class="text-[10px] text-emerald-600 font-bold flex items-center gap-0.5">
                    ✓ 已在队列中
                </span>
                </template>

            </div>
            </div>

            <div v-if="filteredJobs.length === 0" class="h-48 flex flex-col items-center justify-center text-neutral-400 gap-2">
            <span class="text-2xl">📭</span>
            <p class="text-xs font-bold">暂无对应分类下的职位信息</p>
            </div>
        </div>
        </div>

        <div class="flex-1 min-w-[380px] bg-white rounded-[28px] shadow-[0_8px_30px_rgb(0,0,0,0.02)] border border-neutral-100 flex flex-col h-full overflow-hidden relative">
        
        <div v-if="!activeJob" class="flex-1 flex flex-col items-center justify-center text-neutral-400 gap-2 bg-[#FAFAFC]/50">
            <span class="text-3xl">📋</span>
            <p class="text-xs font-bold">选择左侧职位，开启 AI 破局指南</p>
        </div>

        <div v-else class="flex-1 flex flex-col h-full relative overflow-hidden bg-white">
            
            <div class="p-6 border-b border-neutral-100 flex-shrink-0 flex justify-between items-center bg-[#FAFAFC]/50">
            <div class="min-w-0 flex-1 pr-4">
                <span class="text-[10px] bg-neutral-900 text-white px-2 py-0.5 rounded-md font-black tracking-widest uppercase">
                {{ activeCategory === 'pending' ? 'PREPARATION' : 'DATABASE PROFILE' }}
                </span>
                <h3 class="text-xl font-black text-neutral-900 mt-2.5 tracking-tight truncate leading-tight">{{ activeJob.title }}</h3>
                <p class="text-xs font-bold text-neutral-400 mt-1">{{ activeJob.company }}</p>
            </div>

            <div class="flex-shrink-0">
                <button 
                v-if="activeCategory === 'pending' && activeJob.aiReady"
                @click="startMockInterview" 
                class="px-5 py-3 bg-black hover:bg-neutral-900 text-white rounded-xl shadow-md hover:scale-[1.02] active:scale-[0.98] transition-all flex items-center gap-2"
                >
                <span class="text-xs font-black tracking-wide">开始仿真演练</span>
                <span class="text-xs">➔</span>
                </button>

                <button 
                v-else-if="activeCategory === 'pending' && !activeJob.aiReady"
                @click="prepareJob(activeJob)"
                class="px-5 py-3 bg-neutral-100 text-neutral-400 rounded-xl font-black text-xs cursor-not-allowed flex items-center gap-2"
                >
                <span>未就绪</span>
                <span>🔒</span>
                </button>

                <button 
                v-else
                @click="addToPending(activeJob)"
                :disabled="activeJob.isAdded"
                :class="[activeJob.isAdded ? 'bg-neutral-100 text-neutral-400 cursor-not-allowed' : 'bg-indigo-600 hover:bg-indigo-700 text-white shadow-md hover:scale-[1.02] active:scale-[0.98]']"
                class="px-5 py-3 rounded-xl transition-all flex items-center gap-2 text-xs font-black"
                >
                <span>{{ activeJob.isAdded ? '已加入队列' : '加入队列' }}</span>
                <span>＋</span>
                </button>
            </div>
            </div>

            <div class="flex-1 overflow-y-auto no-scrollbar p-6 space-y-6">
            
            <div v-if="activeCategory === 'pending' && !activeJob.aiReady" class="bg-rose-50/30 border border-rose-100/40 p-6 rounded-2xl flex flex-col items-center text-center gap-4">
                <span class="text-2xl">⚠️</span>
                <div>
                <h4 class="text-sm font-black text-rose-600">AI 考战数据尚未就绪</h4>
                <p class="text-xs text-neutral-500 leading-relaxed font-semibold mt-1.5 max-w-[320px]">
                    该职位属于新导入岗位。我们需要针对该企业的技术栈进行模型深度剖析，定制考纲与通关真题。
                </p>
                </div>
                <button 
                @click="prepareJob(activeJob)"
                class="px-5 py-2.5 bg-rose-600 hover:bg-rose-700 text-white text-xs font-black rounded-xl transition-all active:scale-95 shadow-sm"
                >
                立刻一键诊断并建库 ➔
                </button>
            </div>

            <template v-else>
                <div>
                <h4 class="text-xs font-black text-neutral-900 tracking-wider uppercase mb-3 flex items-center gap-2">
                    <span class="w-1.5 h-1.5 bg-indigo-600 rounded-full"></span> 核心攻防方向
                </h4>
                <div class="grid grid-cols-3 gap-3">
                    <div v-for="point in activeJob.coreFocus" :key="point.title" class="bg-[#F5F5F7]/80 p-4 rounded-xl border border-neutral-100 flex flex-col gap-1 hover:bg-neutral-50 transition-colors">
                    <span class="text-[11px] font-black text-neutral-900 truncate">{{ point.title }}</span>
                    <p class="text-[10px] text-neutral-400 font-medium leading-relaxed mt-0.5">{{ point.desc }}</p>
                    </div>
                </div>
                </div>

                <div class="bg-indigo-50/25 border border-indigo-100/20 p-5 rounded-2xl">
                <h4 class="text-xs font-black text-indigo-700 tracking-wider uppercase mb-2 flex items-center gap-1.5">
                    <span>🕵️‍♂️</span> 考官核心考察意图
                </h4>
                <p class="text-xs text-neutral-600 leading-relaxed font-semibold">
                    {{ activeJob.aiIntent }}
                </p>
                </div>

                <div class="bg-purple-50/25 border border-purple-100/20 p-5 rounded-2xl">
                <h4 class="text-xs font-black text-purple-700 tracking-wider uppercase mb-2 flex items-center gap-1.5">
                    <span>💡</span> 通关金句与避坑指南
                </h4>
                <p class="text-xs text-neutral-600 leading-relaxed font-semibold">
                    {{ activeJob.cheatSheet }}
                </p>
                </div>

                <div>
                <h4 class="text-xs font-black text-neutral-900 tracking-wider uppercase mb-3 flex items-center gap-2">
                    <span class="w-1.5 h-1.5 bg-black rounded-full"></span> 仿真压轴真题
                </h4>
                <div class="space-y-3">
                    <div v-for="(q, idx) in activeJob.mockQuestions" :key="idx" class="bg-[#FAFAFC] border border-neutral-100/80 p-4.5 rounded-xl hover:shadow-sm transition-shadow">
                    <p class="text-xs font-bold text-neutral-900 flex gap-2">
                        <span class="font-mono text-indigo-600">Q{{ idx + 1 }}:</span>
                        <span>{{ q.title }}</span>
                    </p>
                    <p class="text-[10px] text-neutral-400 font-medium mt-2 leading-relaxed pl-5 border-l border-neutral-200">
                        {{ q.hint }}
                    </p>
                    </div>
                </div>
                </div>
            </template>

            </div>

        </div>

        </div>

    </div>
    </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

const searchQuery = ref('')
const activeCategory = ref<'pending' | 'all'>('pending')
const selectedJobId = ref('job-1')

const interviewJobs = ref([
    {
    id: 'job-1',
    title: 'Linux 内核适配与基础软件开发',
    company: '麒麟软件 (南京研发中心)',
    status: 'pending',
    date: '05-18 10:00',
    matchRate: 92,
    aiReady: true,
    isAdded: true,
    coreFocus: [
        { title: '多核竞争死锁', desc: '基于飞腾/ARM64微处理器的硬件周期对齐、Lockdep依赖树排障。' },
        { title: '中断上下文安全', desc: '深入研究自旋锁(spin_lock_irqsave)下绝对不能引入睡眠的原生原理。' },
        { title: '信创全栈环境适配', desc: '银河麒麟V10及国产操作系统的驱动级兼容性BUG排查调试。' }
    ],
    aiIntent: '大厂的基础软件开发组最看重开发者的“底层防御式编程意识”。面试官意图在于挖掘你是否有在多线程并发竞争、内核临界区安全等极限边界环境下的真实实操经验，而不仅仅是会调库。',
    cheatSheet: '回答时多使用专业内核态行话。比如：在分析中断上下文不退出的问题时，一定要指出“中断没有对应的task_struct，所以不允许触发进程调度器运行，凡是涉及休眠的操作均会引发内核 Panic”。这比单纯回答“不能睡”高级十倍。',
    mockQuestions: [
        { title: '在多核(SMP)架构下，自旋锁与互斥锁的最佳性能分界点在哪里？', hint: '提示：从锁争用时的上下文切换开销（Context Switch）以及自旋等待占用的 CPU 周期两个维度进行权衡。' },
        { title: '你曾获得中国软件杯二等奖，请结合你的适配项目，聊聊驱动级大内存拷贝时的零拷贝（Zero-Copy）实践。', hint: '提示：重点切入 mmap 用户态与内核态共享页，或者使用 DMA 内存通道等方式削减 CPU 的数据搬运压力。' }
    ]
    },
    {
    id: 'job-2',
    title: '华为昇腾大模型工程优化岗',
    company: '华为技术有限公司 (计算产品线)',
    status: 'pending',
    date: '05-20 14:00',
    matchRate: 95,
    aiReady: false,
    isAdded: true,
    coreFocus: [
        { title: '模型轻量量化', desc: '熟悉大模型（如DeepSeek）在昇腾 NPU 下的 INT4/INT8 量化损耗。' },
        { title: 'KVCache 交互加速', desc: '大流量交互中流式传输（SSE）优化，减少首 token 的极致时延。' },
        { title: 'MindSpore 算子融合', desc: '适配昇腾 CANN 架构，整合端侧解算方案，解决硬件兼容底盘。' }
    ],
    aiIntent: '华为对国产算力生态（CANN和昇腾）的开发敏感度非常看重。考官意在了解你是否懂得如何在硬件矩阵计算单元中，对神经网络计算层进行精简裁剪，以应对大推理任务的极限吞吐瓶颈。',
    cheatSheet: '建议你强调之前在大模型结合3D分析（如“筑理·考工台”）交互实践。着重说明你如何通过引入 SSE（Server-Sent Events）接口并定制 KVCache 加载算法来解决加载高延迟。',
    mockQuestions: [
        { title: '如何定位大模型推理卡首字延迟（TTFT）偏高的硬件根源？', hint: '提示：分析输入 Token 编码阶段（Prefill Stage）对矩阵乘法单元的算力压榨，并结合昇腾 profiling 工具抓取瓶颈。' }
    ]
    },
    {
    id: 'job-3',
    title: 'Spring Boot 架构重构岗',
    company: '达梦数据库有限公司 (武汉总部)',
    status: 'none',
    date: '05-02 11:00',
    matchRate: 87,
    aiReady: false,
    isAdded: false,
    coreFocus: [
        { title: '国产数据库兼容性', desc: '达梦 DM8 数据库对 Spring Boot/MyBatis-Plus 生态的 Schema 大小写敏感映射。' },
        { title: '多数据源热切换', desc: '微服务架构中，复杂租户和数据库平滑降级、动态连接池切换方案。' },
        { title: '微服务高可用重构', desc: '基于 Spring Cloud 体系的安全降级、性能死锁优化与自定义认证实现。' }
    ],
    aiIntent: '达梦目前的核心工作是将原本依赖于国外数据库的旧系统重构为达梦生态。他们非常需要熟悉 Java 体系，同时掌握关系型数据库底层差异的实战全栈工程师。',
    cheatSheet: '可重点介绍多数据源切换、去除了传统沉重安全组件后的“定制化轻量身份认证”以及在国产关系型数据库下，如何解决大数据吞吐连接池抖动和连接复用。',
    mockQuestions: [
        { title: '如何在不改变既有 MyBatis 业务 SQL 的情况下，平滑解决达梦大写表名导致的映射崩溃？', hint: '提示：可通过在 JDBC 连接配置中添加兼容性模式（如 Oracle Mode），或者定制 MyBatis 拦截器修改 AST。' }
    ]
    }
])

const pendingCount = computed(() => interviewJobs.value.filter(j => j.isAdded).length)
const readyCount = computed(() => interviewJobs.value.filter(j => j.isAdded && j.aiReady).length)

const filteredJobs = computed(() => {
    return interviewJobs.value.filter(job => {
    const matchesSearch = job.title.toLowerCase().includes(searchQuery.value.toLowerCase()) || 
                            job.company.toLowerCase().includes(searchQuery.value.toLowerCase())
    if (activeCategory.value === 'pending') {
        return matchesSearch && job.isAdded
    } else {
        return matchesSearch && !job.isAdded
    }
    })
})

const activeJob = computed(() => {
    const current = interviewJobs.value.find(j => j.id === selectedJobId.value)
    if (current) {
    if (activeCategory.value === 'pending' && !current.isAdded) {
        return filteredJobs.value[0] || null
    }
    if (activeCategory.value === 'all' && current.isAdded) {
        return filteredJobs.value[0] || null
    }
    return current
    }
    return filteredJobs.value[0] || null
})

const selectJob = (job: any) => {
    selectedJobId.value = job.id
}

const addToPending = (job: any) => {
    job.isAdded = true
    alert(`✅ 已成功将【${job.title}】加入待面试队列。`)
}

const prepareJob = (job: any) => {
    if (job.aiReady) return
    alert(`⚡ AI 智脑引擎正在深度剖析【${job.company}】的底层技术栈需求...`)
    setTimeout(() => {
    job.aiReady = true
    alert(`🎉 【${job.title}】面试考纲、通关真题以及避坑金句生成完毕！状态已更新。`)
    }, 1200)
}

const startMockInterview = () => {
    alert(`🚀 AI 仿真面试舱开启！\n\n当前正在连线：${activeJob.value?.company} 的面试总监\n岗位：${activeJob.value?.title}\n\n请整理仪容并确认麦克风正常。`)
}
</script>

<style scoped>
.no-scrollbar::-webkit-scrollbar { display: none; }
.no-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }
</style>