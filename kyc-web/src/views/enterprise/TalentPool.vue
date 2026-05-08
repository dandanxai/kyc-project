<template>
    <div class="h-full w-full bg-[#F5F5F7] p-8 overflow-hidden flex flex-col font-sans select-none animate-fade-in">
    
    <header class="flex-shrink-0 flex flex-col md:flex-row justify-between items-start md:items-center gap-4 mb-6">
        <div>
        <h1 class="text-3xl font-black tracking-tight text-black flex items-center gap-2">
            麒麟人才生态公海 <span class="text-xl">🧭</span>
        </h1>
        <p class="text-xs font-bold text-gray-400 uppercase tracking-widest mt-1">
            Kirin Talent Ecosystem & Multidimensional Filter Console
        </p>
        </div>
        
        <div class="relative w-full md:w-80">
        <span class="absolute left-4 top-1/2 -translate-y-1/2 text-sm">🔍</span>
        <input 
            v-model="searchQuery"
            type="text" 
            placeholder="输入姓名或学校快速定位..." 
            class="w-full pl-10 pr-4 py-2.5 bg-white border border-gray-100 rounded-2xl text-xs font-bold focus:outline-none focus:border-black shadow-sm transition-all"
        />
        </div>
    </header>

    <div class="flex-1 flex gap-8 overflow-hidden min-h-0">
        
        <aside class="w-80 bg-white/90 backdrop-blur-md rounded-[32px] border border-gray-100 p-6 flex-shrink-0 flex flex-col justify-between shadow-sm overflow-y-auto no-scrollbar">
        <div class="space-y-6">
            <div class="flex items-center justify-between pb-4 border-b border-neutral-100">
            <span class="text-xs font-black text-black tracking-wider uppercase">多维智能筛选</span>
            <button @click="resetFilters" class="text-[10px] font-black text-indigo-600 hover:underline">重置 🔄</button>
            </div>

            <div class="space-y-2">
            <label class="text-[9px] font-black text-gray-400 uppercase tracking-widest">信创生态阵营</label>
            <div class="flex flex-col gap-1.5">
                <button 
                v-for="eco in ecosystems" 
                :key="eco.value"
                @click="filterEcosystem = eco.value"
                :class="[filterEcosystem === eco.value ? 'bg-black text-white' : 'bg-[#F5F5F7] text-gray-700 hover:bg-neutral-200']"
                class="w-full text-left px-4 py-2.5 rounded-xl text-xs font-bold transition-all text-ellipsis overflow-hidden whitespace-nowrap"
                >
                {{ eco.label }}
                </button>
            </div>
            </div>

            <div class="space-y-2">
            <label class="text-[9px] font-black text-gray-400 uppercase tracking-widest">经验年限要求</label>
            <select v-model="filterExp" class="w-full px-3 py-2.5 bg-[#F5F5F7] rounded-xl text-xs font-bold focus:outline-none cursor-pointer">
                <option value="all">不限年限</option>
                <option value="junior">1-3年 潜力开发者</option>
                <option value="middle">3-5年 核心骨干</option>
                <option value="senior">5年以上 资深架构师</option>
            </select>
            </div>

            <div class="space-y-2">
            <label class="text-[9px] font-black text-gray-400 uppercase tracking-widest">学历背景</label>
            <select v-model="filterEdu" class="w-full px-3 py-2.5 bg-[#F5F5F7] rounded-xl text-xs font-bold focus:outline-none cursor-pointer">
                <option value="all">不限学历</option>
                <option value="bachelor">统招本科及以上</option>
                <option value="special">高水平高职/专科</option>
                <option value="project">科班软件studio经历</option>
            </select>
            </div>

            <div class="space-y-2">
            <label class="text-[9px] font-black text-gray-400 uppercase tracking-widest">期望薪资</label>
            <select v-model="filterSalary" class="w-full px-3 py-2.5 bg-[#F5F5F7] rounded-xl text-xs font-bold focus:outline-none cursor-pointer">
                <option value="all">不限薪资</option>
                <option value="low">15K - 25K</option>
                <option value="mid">25K - 35K</option>
                <option value="high">35K以上</option>
            </select>
            </div>

            <div class="space-y-2">
            <label class="text-[9px] font-black text-gray-400 uppercase tracking-widest">数据排序</label>
            <select v-model="filterSort" class="w-full px-3 py-2.5 bg-[#F5F5F7] rounded-xl text-xs font-bold focus:outline-none cursor-pointer">
                <option value="match">AI 智能匹配度优先</option>
                <option value="experience">从业经验年限优先</option>
                <option value="salary">期望薪资高低优先</option>
            </select>
            </div>
        </div>

        <div class="pt-6 border-t border-neutral-100 text-center">
            <p class="text-[8px] font-black text-gray-400 uppercase tracking-widest">KIRIN ANALYTICS CO.</p>
        </div>
        </aside>

        <main class="flex-1 overflow-y-auto no-scrollbar pr-1">
        <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
            <div 
            v-for="candidate in filteredCandidates" 
            :key="candidate.id"
            class="bg-white rounded-[28px] border border-gray-100 p-6 flex flex-col justify-between shadow-sm hover:shadow-[0_20px_40px_rgba(0,0,0,0.04)] transition-all duration-300 hover:-translate-y-1.5 relative group overflow-hidden"
            >
            <div class="space-y-4">
                <div class="flex items-start justify-between">
                <div class="flex items-center gap-3">
                    <div class="w-12 h-12 rounded-2xl border border-gray-50 overflow-hidden shadow-inner flex-shrink-0 transition-transform duration-300 group-hover:scale-105">
                    <img :src="candidate.avatar" class="w-full h-full object-cover" />
                    </div>
                    <div>
                    <h3 class="text-sm font-black text-black tracking-tight">{{ candidate.name }}</h3>
                    <p class="text-[10px] text-gray-400 font-bold mt-0.5">{{ candidate.school }} · {{ candidate.education }}</p>
                    </div>
                </div>

                <span 
                    :class="[candidate.match >= 90 ? 'bg-emerald-50 text-emerald-600 border-emerald-100' : 'bg-indigo-50 text-indigo-600 border-indigo-100']"
                    class="text-[9px] px-2 py-1 rounded-xl font-black border uppercase tracking-wider"
                >
                    AI {{ candidate.match }}%
                </span>
                </div>

                <div>
                <h4 class="text-xs font-black text-black tracking-tight line-clamp-1">{{ candidate.role }}</h4>
                <div class="flex items-center gap-1.5 mt-1 text-[10px] text-gray-400 font-bold uppercase tracking-wider">
                    <span>{{ candidate.exp }}年开发实战</span>
                    <span>•</span>
                    <span>{{ candidate.status }}</span>
                </div>
                </div>

                <div class="p-3 bg-[#F5F5F7]/80 rounded-xl border border-neutral-50">
                <p class="text-[11px] text-gray-500 font-medium leading-relaxed line-clamp-2">
                    {{ candidate.summary }}
                </p>
                </div>

                <div class="flex flex-wrap gap-1.5">
                <span 
                    v-for="tag in candidate.tags" 
                    :key="tag"
                    class="text-[8px] bg-neutral-50 text-gray-500 border border-neutral-100 px-2.5 py-0.5 rounded-lg font-bold uppercase tracking-wider"
                >
                    {{ tag }}
                </span>
                </div>
            </div>

            <div class="flex items-center justify-between pt-5 mt-5 border-t border-neutral-50">
                <div>
                <p class="text-[8px] font-bold text-gray-400 uppercase tracking-widest">期望薪资</p>
                <p class="text-sm font-black text-black tracking-tight mt-0.5">{{ candidate.salary }}</p>
                </div>
                
                <div class="flex items-center gap-2">
                <button 
                    @click="showDetails(candidate)" 
                    class="px-4 py-2 bg-neutral-100 hover:bg-neutral-200 text-neutral-800 text-xs font-black rounded-xl transition-all active:scale-95"
                >
                    详情
                </button>
                <button 
                    @click="inviteInterview(candidate.name)" 
                    class="px-4 py-2 bg-black hover:bg-neutral-800 text-white text-xs font-black rounded-xl shadow-md transition-all active:scale-95"
                >
                    直面邀请
                </button>
                </div>
            </div>

            </div>
        </div>

        <div v-if="filteredCandidates.length === 0" class="h-[500px] flex flex-col items-center justify-center text-gray-400 gap-2">
            <span class="text-5xl">🧬</span>
            <p class="text-xs font-bold uppercase tracking-widest mt-2">未找到符合该多维过滤条件的信创人才</p>
        </div>
        </main>

    </div>

    <transition name="drawer">
        <div 
        v-if="activeCandidate" 
        class="fixed inset-0 z-50 flex justify-end bg-black/20 backdrop-blur-sm"
        @click.self="activeCandidate = null"
        >
        <div class="w-full max-w-xl h-full bg-white shadow-2xl p-8 flex flex-col justify-between overflow-y-auto no-scrollbar">
            
            <div class="space-y-8">
            <div class="flex justify-between items-start">
                <div class="flex items-center gap-4">
                <div class="w-16 h-16 rounded-2xl overflow-hidden border border-gray-100 shadow-sm">
                    <img :src="activeCandidate.avatar" class="w-full h-full object-cover" />
                </div>
                <div>
                    <h2 class="text-lg font-black text-black flex items-center gap-2">
                    {{ activeCandidate.name }}
                    <span class="text-xs bg-indigo-50 text-indigo-600 px-2 py-0.5 rounded-full font-black">MATCH {{ activeCandidate.match }}%</span>
                    </h2>
                    <p class="text-xs text-gray-400 font-bold uppercase tracking-wide mt-1">
                    {{ activeCandidate.school }} · {{ activeCandidate.education }}
                    </p>
                </div>
                </div>
                <button @click="activeCandidate = null" class="w-8 h-8 bg-neutral-100 hover:bg-neutral-200 text-black rounded-full flex items-center justify-center text-xs transition-colors">✕</button>
            </div>

            <div class="p-5 bg-indigo-50/40 rounded-2xl border border-indigo-50 space-y-2">
                <h4 class="text-xs font-black text-indigo-900 flex items-center gap-1.5">
                <span>🤖</span> AI 麒麟评估报告
                </h4>
                <p class="text-xs text-indigo-950 font-bold leading-relaxed">
                {{ activeCandidate.aiEvaluation }}
                </p>
            </div>

            <div class="space-y-4">
                <h4 class="text-[10px] font-black text-gray-400 uppercase tracking-widest">专业核心技能评估</h4>
                <div class="grid grid-cols-2 gap-4">
                <div v-for="(val, skill) in activeCandidate.skills" :key="skill" class="space-y-1">
                    <div class="flex justify-between text-[10px] font-black text-black">
                    <span>{{ skill }}</span>
                    <span>{{ val }}%</span>
                    </div>
                    <div class="w-full h-1.5 bg-neutral-100 rounded-full overflow-hidden">
                    <div :style="{ width: val + '%' }" class="h-full bg-black rounded-full"></div>
                    </div>
                </div>
                </div>
            </div>

            <div class="space-y-4">
                <h4 class="text-[10px] font-black text-gray-400 uppercase tracking-widest">核心信创实战经历</h4>
                <div class="space-y-6">
                <div v-for="proj in activeCandidate.projects" :key="proj.title" class="relative pl-4 border-l-2 border-neutral-100 space-y-1">
                    <div class="w-2 h-2 rounded-full bg-black absolute -left-[5px] top-1.5"></div>
                    <h5 class="text-xs font-black text-black">{{ proj.title }}</h5>
                    <p class="text-[9px] text-gray-400 font-bold uppercase mt-0.5">{{ proj.period }}</p>
                    <p class="text-xs text-gray-500 font-semibold leading-relaxed mt-1.5">{{ proj.desc }}</p>
                </div>
                </div>
            </div>
            </div>

            <div class="pt-6 border-t border-neutral-100 flex gap-4 mt-8">
            <button @click="inviteInterview(activeCandidate.name)" class="flex-1 py-3.5 bg-black hover:bg-neutral-800 text-white text-xs font-black rounded-2xl shadow-lg transition-all active:scale-[0.98]">
                📧 发起信创面试邀请
            </button>
            <button @click="contactCandidate" class="px-6 py-3.5 bg-neutral-100 hover:bg-neutral-200 text-black text-xs font-black rounded-2xl transition-all active:scale-[0.98]">
                💬 在线沟通
            </button>
            </div>

        </div>
        </div>
    </transition>

    </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 选中的候选人弹窗详情
const activeCandidate = ref<any>(null)

// 筛选项响应式变量
const searchQuery = ref('')
const filterEcosystem = ref('all')
const filterExp = ref('all')
const filterEdu = ref('all')
const filterSalary = ref('all')
const filterStatus = ref('all')
const filterSort = ref('match')

// 信创阵营常数
const ecosystems = [
    { label: '🔥 全部生态阵营', value: 'all' },
    { label: '🛡️ 银河麒麟 / 飞腾', value: 'kirin' },
    { label: '💻 统信UOS / 龙芯', value: 'uos' },
    { label: '⚡ 华为鲲鹏 / 昇腾', value: 'kunpeng' },
    { label: '💾 国产分布式数据库', value: 'db' }
]

// 模拟信创多维高精度人才数据库
const candidates = ref([
    {
    id: 1,
    name: '叶文洁',
    role: 'Linux 内核驱动适配开发岗',
    school: '清华大学',
    education: '硕士研究生',
    exp: 5,
    match: 96,
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Enya',
    tags: ['Kylin OS', 'C / C++', '架构移植'],
    summary: '资深内核级专家。曾主持过多核Cortex-A系列处理器在Linux环境下的深度移植，对系统底层和驱动加载机制有绝对控制力。',
    salary: '32K-45K',
    status: '积极寻觅中',
    ecosystem: 'kirin',
    aiEvaluation: '深度熟悉国产银河麒麟及统信UOS架构体系。对多线程内核锁和中断调度优化非常有造诣，AI双向评估为S+。',
    skills: { '国产内核移植': 98, '内核级驱动研发': 95, 'C/C++底层架构': 92, 'Linux优化调试': 88 },
    projects: [
        { title: '国产银河麒麟V10嵌入式底层内核适配升级', period: '2024 - 2025', desc: '负责完成嵌入式内核对于新型国产处理器的基础驱动接口重构，实现启动自愈机制。' }
    ]
    },
    {
    id: 2,
    name: '罗辑',
    role: 'Rust 信创微服务及分布式高并发工程师',
    school: '滁州职业技术学院',
    education: '大专',
    exp: 3,
    match: 91,
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Jude',
    tags: ['Rust', 'Java', '达梦DB'],
    summary: '高技能软件工作室带头人。连续荣获中国软件杯二等奖，擅长高并发Spring Boot框架的迁移与Rust内存安全重构。',
    salary: '22K-35K',
    status: '积极寻觅中',
    ecosystem: 'db',
    aiEvaluation: '拥有强悍的安全底层思维，熟练运用 Rust 对原有高耗能、易溢出的 C/Java 业务进行内存安全性重构，适配达梦数据库性能拉满。',
    skills: { 'Rust安全重构': 94, 'Java高并发系统': 88, '国产DB性能调优': 90, '信创全栈迁移': 85 },
    projects: [
        { title: '全国产化社保多中心异地双活高并发平台', period: '2025 - 2026', desc: '使用Spring Boot配合达梦分布式事务数据库构建极低卡顿的公共平台，支撑千万级高频次查询。' }
    ]
    },
    {
    id: 3,
    name: '章北海',
    role: 'ARM 及申威架构优化适配专家',
    school: '哈尔滨工业大学',
    education: '本科',
    exp: 8,
    match: 88,
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=George',
    tags: ['ARM 汇编', '申威适配', 'UOS工业版'],
    summary: '硬核跨指令集大牛。对申威架构和华为鲲鹏指令集进行过指令级别的精细性能测试与高并发缺陷隔离。',
    salary: '38K-50K',
    status: '在职可看机会',
    ecosystem: 'uos',
    aiEvaluation: '资深指令集适配专家，擅长特种及工业嵌入式设备的软件环境移植。对Linux内核调度器有极深的性能压榨调优能力。',
    skills: { '跨指令集适配': 96, '底层汇编指令调优': 92, '信创兼容认证': 85, '系统崩溃灾备': 90 },
    projects: [
        { title: '基于申威处理器的国网控制站系统安全硬隔离项目', period: '2023 - 2025', desc: '深度移植UOS工业版系统，并用汇编改写了部分热路径，安全性与系统响应均通过国家一级特种评测。' }
    ]
    },
    {
    id: 4,
    name: '程心',
    role: '华为昇腾大模型算子开发工程师',
    school: '北京航空航天大学',
    education: '硕士研究生',
    exp: 2,
    match: 85,
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Clara',
    tags: ['昇腾 Canna', 'Python', '算子调优'],
    summary: '专注于大模型国产计算卡适配。基于华为昇腾 Ascend 芯片完成过多维自定义算子的融合与重载。',
    salary: '28K-38K',
    status: '在职可看机会',
    ecosystem: 'kunpeng',
    aiEvaluation: '专注于国产人工智能底层算子优化。基于华为昇腾 Ascend 系列芯片，完成过大模型长文本处理底层的自定义算子融合重构，运行速度提升 22%。',
    skills: { '昇腾算子重构': 90, '主流AI算子适配': 88, 'CANN框架集成': 85, '大模型微调部署': 82 },
    projects: [
        { title: '昇腾昇思平台高能物理微调算法库重构项目', period: '2024', desc: '成功使用昇腾算子库深度改写传统Transformer的多头注意力热路径，显存开销降低40%。' }
    ]
    }
])

// 高精度筛过滤算法
const filteredCandidates = computed(() => {
    let list = [...candidates.value]

    // 1. 文本检索 (搜索名字与学校)
    if (searchQuery.value) {
    const q = searchQuery.value.toLowerCase()
    list = list.filter(c => 
        c.name.toLowerCase().includes(q) || 
        c.school.toLowerCase().includes(q)
    )
    }

    // 2. 信创生态阵营筛选
    if (filterEcosystem.value !== 'all') {
    list = list.filter(c => c.ecosystem === filterEcosystem.value)
    }

    // 3. 工作年限筛选
    if (filterExp.value !== 'all') {
    if (filterExp.value === 'junior') {
        list = list.filter(c => c.exp >= 1 && c.exp <= 3)
    } else if (filterExp.value === 'middle') {
        list = list.filter(c => c.exp > 3 && c.exp <= 5)
    } else if (filterExp.value === 'senior') {
        list = list.filter(c => c.exp > 5)
    }
    }

    // 4. 学历筛选
    if (filterEdu.value !== 'all') {
    if (filterEdu.value === 'bachelor') {
        list = list.filter(c => c.education.includes('本科') || c.education.includes('硕士') || c.education.includes('研究生'))
    } else if (filterEdu.value === 'special') {
        list = list.filter(c => c.education.includes('大专'))
    } else if (filterEdu.value === 'project') {
        list = list.filter(c => c.aiEvaluation.includes('Studio') || c.aiEvaluation.includes('工作坊'))
    }
    }

    // 5. 期望薪资筛选
    if (filterSalary.value !== 'all') {
    if (filterSalary.value === 'low') {
        list = list.filter(c => parseInt(c.salary) >= 15 && parseInt(c.salary) <= 25)
    } else if (filterSalary.value === 'mid') {
        list = list.filter(c => parseInt(c.salary) > 25 && parseInt(c.salary) <= 35)
    } else if (filterSalary.value === 'high') {
        list = list.filter(c => parseInt(c.salary) > 35)
    }
    }

    // 6. 最终排序策略
    if (filterSort.value === 'match') {
    list.sort((a, b) => b.match - a.match)
    } else if (filterSort.value === 'experience') {
    list.sort((a, b) => b.exp - a.exp)
    } else if (filterSort.value === 'salary') {
    list.sort((a, b) => {
        const salA = parseInt(a.salary.split('-')[0])
        const salB = parseInt(b.salary.split('-')[0])
        return salB - salA
    })
    }

    return list
})

// 重置所有筛选
const resetFilters = () => {
    searchQuery.value = ''
    filterEcosystem.value = 'all'
    filterExp.value = 'all'
    filterEdu.value = 'all'
    filterSalary.value = 'all'
    filterSort.value = 'match'
}

// 展开右侧免跳转抽屉
const showDetails = (candidate: any) => {
    activeCandidate.value = candidate
}

// 在线沟通
const contactCandidate = () => {
    activeCandidate.value = null
    router.push('/enterprise/messages')
}

// 发起面试通知
const inviteInterview = (name: string) => {
    alert(`【直面邀请下发成功】！已通过系统专属信道向 [${name}] 下发专属 AI 模拟对练面试入场指令与重点考察大纲。`)
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

/* 右侧抽屉切移动效 */
.drawer-enter-active, .drawer-leave-active {
    transition: all 0.4s cubic-bezier(0.16, 1, 0.3, 1);
}
.drawer-enter-from, .drawer-leave-to {
    opacity: 0;
    transform: translateX(100%);
}
</style>