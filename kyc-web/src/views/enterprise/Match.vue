<template>
    <div class="h-full w-full bg-[#F5F5F7] p-8 overflow-hidden flex flex-col font-sans select-none animate-fade-in">
    
    <header class="flex-shrink-0 flex justify-between items-center mb-6">
        <div>
        <h1 class="text-2xl font-black text-black flex items-center gap-2">
            AI 智能双向匹配中心 <span class="text-indigo-600">⚡</span>
        </h1>
        <p class="text-[10px] font-bold text-gray-400 uppercase tracking-widest mt-1">
            Kirin Vector Matching & AI Double-Sided Recommendation Engine
        </p>
        </div>
        <div class="flex items-center gap-4 bg-white/60 backdrop-blur-md px-4 py-2 rounded-2xl border border-white/80 shadow-sm text-xs font-black">
        <span class="flex items-center gap-1.5 text-indigo-600">
            <span class="w-2 h-2 rounded-full bg-indigo-600 animate-ping"></span>
            引擎状态: 向量高活匹配中
        </span>
        </div>
    </header>

    <div class="flex-1 flex gap-8 overflow-hidden min-h-0">
        
        <section class="w-[380px] flex flex-col gap-6 overflow-y-auto no-scrollbar flex-shrink-0">
        <div class="text-xs font-black text-gray-400 uppercase tracking-wider">选择当前审阅的岗位</div>
        
        <div class="space-y-4">
            <div 
            v-for="job in jobsList" 
            :key="job.id"
            @click="selectJob(job)"
            :class="[
                selectedJob.id === job.id 
                ? 'border-black ring-2 ring-black/5 bg-white shadow-lg' 
                : 'border-transparent bg-white hover:border-black/5 hover:shadow-md'
            ]"
            class="p-5 rounded-[24px] border cursor-pointer transition-all duration-300 relative overflow-hidden"
            >
            <div v-if="selectedJob.id === job.id" class="absolute -right-6 -top-6 w-20 h-20 bg-indigo-50/50 rounded-full blur-xl"></div>

            <div class="space-y-3 relative z-10">
                <div class="flex justify-between items-center">
                <span class="text-[9px] font-black text-indigo-600 bg-indigo-50 px-2 py-0.5 rounded-lg">{{ job.department }}</span>
                <span class="text-[9px] font-bold text-gray-400">{{ job.salary }}</span>
                </div>
                <h3 class="text-sm font-black text-black">{{ job.title }}</h3>
                
                <div class="pt-2 border-t border-neutral-50">
                <p class="text-[8px] font-black text-gray-400 uppercase tracking-wider mb-2">匹配特征向量 (部分图谱)</p>
                <div class="space-y-1.5">
                    <div v-for="(val, skill) in job.skills" :key="skill" class="flex justify-between items-center text-[9px]">
                    <span class="text-gray-500 font-bold">{{ skill }}</span>
                    <span class="font-black text-black">{{ val }}%</span>
                    </div>
                </div>
                </div>
            </div>
            </div>
        </div>
        </section>

        <section class="flex-1 bg-white rounded-[32px] border border-gray-100 p-8 flex flex-col justify-between overflow-hidden">
        
        <div class="flex-shrink-0 flex justify-between items-center pb-4 border-b border-neutral-100">
            <div>
            <h2 class="text-base font-black text-black flex items-center gap-1.5">
                🎯 为该岗位 AI 推荐的符合条件候选人
            </h2>
            <p class="text-[9px] font-black text-indigo-600 uppercase tracking-widest mt-1">
                基于该岗位「能力图谱」与人才库求职画像的交叉相似度检索
            </p>
            </div>
            <span class="text-[10px] font-black bg-neutral-100 text-neutral-600 px-3 py-1.5 rounded-xl">
            已检索公海人才: 142 人
            </span>
        </div>

        <div class="flex-1 overflow-y-auto no-scrollbar space-y-6 py-6 min-h-0">
            
            <div 
            v-for="candidate in matchedCandidates" 
            :key="candidate.id"
            class="p-6 bg-[#F5F5F7] hover:bg-neutral-50 border border-transparent hover:border-black/5 rounded-2xl transition-all flex flex-col lg:flex-row gap-6 relative overflow-hidden group"
            >
            <div class="absolute right-0 top-0 h-full w-24 bg-gradient-to-l from-indigo-50/20 to-transparent pointer-events-none"></div>

            <div class="flex flex-col items-center justify-center text-center lg:w-36 flex-shrink-0 border-b lg:border-b-0 lg:border-r border-neutral-200/50 pb-4 lg:pb-0 lg:pr-6">
                <div class="w-14 h-14 rounded-full border-2 border-white shadow-md overflow-hidden relative mb-2">
                <img :src="candidate.avatar" alt="avatar" />
                </div>
                <h4 class="text-xs font-black text-black">{{ candidate.name }}</h4>
                <p class="text-[9px] text-gray-400 font-bold mt-0.5">{{ candidate.title }}</p>
                
                <div class="mt-4 px-3 py-1 bg-black text-white rounded-xl text-[10px] font-black tracking-tighter">
                匹配度 <span class="text-cyan-300 text-xs">{{ candidate.matchScore }}%</span>
                </div>
            </div>

            <div class="flex-1 space-y-4">
                <div class="bg-white p-4 rounded-xl border border-neutral-100 space-y-1.5 relative">
                <div class="text-[9px] font-black text-indigo-600 flex items-center gap-1">
                    <span>🧠</span> Kirin AI 深度匹配依据与推荐理由：
                </div>
                <p class="text-[11px] text-neutral-700 leading-relaxed font-bold">
                    {{ candidate.matchReason }}
                </p>
                </div>

                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div v-for="skill in candidate.skillsCompare" :key="skill.name" class="space-y-1">
                    <div class="flex justify-between items-center text-[10px] font-bold">
                    <span class="text-neutral-500">{{ skill.name }}</span>
                    <span class="text-black font-black">
                        岗位需 {{ skill.target }}% <span class="text-neutral-300">/</span> 
                        <span :class="skill.current >= skill.target ? 'text-emerald-600' : 'text-amber-500'">
                        候选人 {{ skill.current }}%
                        </span>
                    </span>
                    </div>
                    <div class="h-1.5 w-full bg-neutral-200 rounded-full overflow-hidden relative">
                    <div class="absolute h-full bg-neutral-300" :style="{ width: skill.target + '%' }"></div>
                    <div 
                        class="absolute h-full rounded-full transition-all duration-1000" 
                        :class="skill.current >= skill.target ? 'bg-indigo-600' : 'bg-amber-500'"
                        :style="{ width: skill.current + '%' }"
                    ></div>
                    </div>
                </div>
                </div>

                <div class="flex justify-between items-center pt-2">
                <div class="flex gap-2">
                    <span 
                    v-for="tag in candidate.highlights" 
                    :key="tag"
                    class="text-[8px] bg-indigo-50 text-indigo-600 font-bold px-2 py-0.5 rounded"
                    >
                    {{ tag }}
                    </span>
                </div>
                <button 
                    @click="inviteCandidate(candidate)"
                    class="px-4 py-2 bg-neutral-900 hover:bg-black text-white text-[10px] font-black rounded-xl transition-all active:scale-95 flex items-center gap-1.5"
                >
                    💬 一键发起意向沟通
                </button>
                </div>

            </div>
            </div>

        </div>

        </section>

    </div>
    </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

// 模拟企业当前发布的所有岗位
const jobsList = ref([
    {
    id: 1,
    title: 'Linux 内核适配与基础软件开发',
    department: '麒麟软件 (南京研发中心)',
    salary: '25K-40K',
    skills: {
        '内核及基础适配': 95,
        '飞腾底层芯片级调试': 90,
        'C/C++底层架构开发': 85
    }
    },
    {
    id: 2,
    title: '达梦数据库存储引擎重构专家',
    department: '达梦数据库 (武汉总部)',
    salary: '35K-55K',
    skills: {
        '数据库内核开发': 98,
        '事务控制与锁机制': 90,
        '高并发高可用架构': 80
    }
    }
])

const selectedJob = ref(jobsList.value[0])

// 模拟人才公海中经过 AI 向量化匹配出来的推荐候选人
const mockCandidates = {
    1: [ // 针对岗位 1（Linux 内核适配）的推荐人才
    {
        id: 101,
        name: 'Captain_X (林睿)',
        title: '全栈信创底层研发专家 / 开源大牛',
        avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Felix',
        matchScore: 95,
        matchReason: '候选人深度拥有在国产飞腾芯片架构下的 Linux 内核适配移植经验。在之前的软件设计大赛中带队获全国二等奖，对多核中断隔离、SMP 架构下的自旋锁 (spin_lock_irqsave) 拥有极深的性能调优实战，且能自主编写高效、不依赖欧美商业中间件的轻量级安全认证和数据传输闭环。',
        highlights: ['全国软件设计二等奖', '精通 C/C++ 与 Rust', '信创适配专家'],
        skillsCompare: [
        { name: '内核及基础适配', target: 95, current: 98 },
        { name: '飞腾底层芯片级调试', target: 90, current: 92 },
        { name: 'C/C++底层架构开发', target: 85, current: 95 }
        ]
    },
    {
        id: 102,
        name: '张昭明',
        title: 'C/C++ 系统级工程师',
        avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Zack',
        matchScore: 82,
        matchReason: '熟悉多线程并发控制与内核编译，拥有 3 年银河麒麟系统兼容性适配经验，能够快速排查驱动级内存泄露，性能调优功底扎实。但在飞腾原生芯片底座调试上尚缺大型量产项目历练。',
        highlights: ['3年银河麒麟开发', '精通底线并发调优'],
        skillsCompare: [
        { name: '内核及基础适配', target: 95, current: 85 },
        { name: '飞腾底层芯片级调试', target: 90, current: 75 },
        { name: 'C/C++底层架构开发', target: 85, current: 88 }
        ]
    }
    ],
    2: [ // 针对岗位 2（达梦存储引擎专家）的推荐人才
    {
        id: 201,
        name: '陆伟博',
        title: '分布式数据库内核研发负责人',
        avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Oliver',
        matchScore: 92,
        matchReason: '5年大厂存储内核研发背景。精通 MVCC 机制、分布式强一致性事务控制以及两阶段提交协议。在共享存储架构（Shared-Everything）下对高并发行锁冲突调优具备多套千亿级生产环境实战，与贵司该岗位存储重构需求 99% 契合。',
        highlights: ['前阿里PolarDB内核专家', '分布式强一致性事务'],
        skillsCompare: [
        { name: '数据库内核开发', target: 98, current: 95 },
        { name: '事务控制与锁机制', target: 90, current: 96 },
        { name: '高并发高可用架构', target: 80, current: 90 }
        ]
    }
    ]
} as any

const matchedCandidates = computed(() => {
    return mockCandidates[selectedJob.value.id] || []
})

const selectJob = (job: any) => {
    selectedJob.value = job
}

const inviteCandidate = (candidate: any) => {
    alert(`已向候选人 [${candidate.name}] 发送定向岗位意向书！此举将自动通过 KYC 系统与候选人的“即时互联/消息中心”拉起安全沟通线程。`)
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