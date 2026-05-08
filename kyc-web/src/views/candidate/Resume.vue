<template>
    <div class="w-full min-w-0 p-4 sm:p-8 animate-in fade-in duration-1000 pb-20">
    
    <header class="mb-8 px-2 flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
        <div>
        <h2 class="text-4xl font-black text-black tracking-tighter flex items-center gap-3">
            智能画像与双生建模
            <span class="text-xs bg-blue-600 text-white px-2.5 py-1 rounded-full font-mono">v2.5 Enterprise</span>
        </h2>
        <p class="text-xs text-gray-400 font-bold uppercase mt-2 tracking-[0.2em]">NLP Document Analysis & Multidimensional Twin Mapping</p>
        </div>
        
        <div class="flex bg-gray-100 p-1.5 rounded-2xl border border-gray-200">
        <button 
            @click="currentChannel = 'candidate'"
            :class="currentChannel === 'candidate' ? 'bg-white text-black shadow-md' : 'text-gray-400 hover:text-black'"
            class="px-5 py-2 text-xs font-black rounded-xl transition-all flex items-center gap-2"
        >
            👤 个人简历解析
        </button>
        <button 
            @click="currentChannel = 'job'"
            :class="currentChannel === 'job' ? 'bg-black text-white shadow-md' : 'text-gray-400 hover:text-black'"
            class="px-5 py-2 text-xs font-black rounded-xl transition-all flex items-center gap-2"
        >
            🏢 企业岗位解析
        </button>
        </div>
    </header>

    <div class="grid grid-cols-12 gap-8">
        
        <div class="col-span-12 xl:col-span-5 space-y-6">
        
        <div class="bg-white rounded-[40px] p-8 border border-white shadow-sm hover:shadow-xl transition-all">
            <div class="flex justify-between items-center mb-6">
            <h4 class="text-sm font-black text-black uppercase tracking-wider">📂 历史解析库 (Multi-Doc Repo)</h4>
            <span class="text-[10px] text-gray-400 font-mono">COUNT: {{ docRepo.length }}</span>
            </div>
            
            <div class="space-y-2 mb-6">
            <div 
                v-for="doc in docRepo.filter(d => d.type === currentChannel)" 
                :key="doc.id"
                @click="selectDoc(doc)"
                :class="selectedDocId === doc.id ? 'border-blue-500 bg-blue-50/20' : 'border-gray-100 hover:border-gray-300'"
                class="flex justify-between items-center p-3.5 border rounded-2xl cursor-pointer transition-all"
            >
                <div class="flex items-center gap-3">
                <span class="text-lg">📄</span>
                <div>
                    <p class="text-xs font-black text-black">{{ doc.name }}</p>
                    <p class="text-[9px] text-gray-400 mt-0.5">解析时间: {{ doc.date }}</p>
                </div>
                </div>
                <span 
                :class="doc.status === 'success' ? 'bg-green-50 text-green-600' : 'bg-amber-50 text-amber-600'" 
                class="text-[9px] font-bold px-2 py-0.5 rounded"
                >
                {{ doc.status === 'success' ? '结构化完成' : '解析中' }}
                </span>
            </div>
            </div>

            <div @click="simulateUpload" class="relative border-2 border-dashed border-gray-100 rounded-[28px] p-8 flex flex-col items-center justify-center hover:border-blue-300 transition-all cursor-pointer bg-gray-50/30 overflow-hidden">
            <div v-if="isScanning" class="absolute inset-0 bg-blue-600/5 animate-pulse"></div>
            <div v-if="isScanning" class="absolute top-0 left-0 w-full h-1 bg-blue-500 shadow-[0_0_20px_blue] animate-scan"></div>
            
            <div class="text-3xl mb-2">📥</div>
            <p class="text-xs font-black text-gray-600">上传新文档并开始分析</p>
            <p class="text-[9px] text-gray-400 mt-1">支持 PDF, DOCX, WPS (Max 10MB)</p>
            </div>
        </div>

        <div class="bg-white rounded-[40px] p-8 border border-white shadow-sm">
            <div class="flex justify-between items-center mb-6">
            <h4 class="text-sm font-black text-black uppercase tracking-wider">🧪 字段解析与人工校准</h4>
            <span class="text-[9px] bg-emerald-50 text-emerald-600 px-2 py-0.5 rounded font-mono">置信度: 98.2%</span>
            </div>
            
            <div class="space-y-3">
            <div v-for="(val, key) in activeSchema" :key="key" class="p-4 bg-gray-50 rounded-2xl border border-gray-100 group relative">
                <div class="flex justify-between items-center">
                <span class="text-[10px] font-black text-gray-400 uppercase tracking-tighter">{{ key }}</span>
                <input 
                    v-model="activeSchema[key]" 
                    class="bg-transparent text-right text-xs font-bold text-black border-none focus:outline-none focus:ring-1 focus:ring-blue-500 rounded px-2 py-0.5"
                />
                </div>
                <div class="absolute right-2 top-1/2 -translate-y-1/2 opacity-0 group-hover:opacity-100 transition-opacity pointer-events-none">
                <span class="text-[9px] bg-black text-white px-1.5 py-0.5 rounded">可修改</span>
                </div>
            </div>
            </div>
        </div>

        </div>

        <div class="col-span-12 xl:col-span-7 space-y-6">
        
        <div class="bg-black rounded-[48px] p-10 text-white shadow-2xl h-full flex flex-col min-h-[620px] relative overflow-hidden">
            
            <div class="absolute -right-20 -top-20 w-80 h-80 bg-blue-600/20 rounded-full blur-[100px]"></div>

            <div class="flex justify-between items-start mb-10 relative z-10">
            <div>
                <h3 class="text-xl font-black flex items-center gap-2">
                <span class="w-8 h-8 bg-white text-black rounded-full flex items-center justify-center text-xs">02</span>
                双向图谱透视引擎
                </h3>
                <p class="text-xs text-gray-500 mt-1">Personal DNA vs Job Blueprint Mapping</p>
            </div>
            
            <div class="flex bg-white/10 p-1 rounded-xl backdrop-blur-md">
                <button 
                @click="chartMode = 'individual'"
                :class="chartMode === 'individual' ? 'bg-white text-black' : 'text-gray-400'"
                class="px-4 py-1.5 text-[10px] font-black rounded-lg transition-all"
                >
                核心图谱
                </button>
                <button 
                @click="chartMode = 'compare'"
                :class="chartMode === 'compare' ? 'bg-blue-600 text-white' : 'text-gray-400'"
                class="px-4 py-1.5 text-[10px] font-black rounded-lg transition-all"
                >
                重叠对比
                </button>
            </div>
            </div>

            <div class="flex-1 flex flex-col lg:flex-row items-center gap-12 relative z-10">
            
            <div class="w-full lg:w-1/2 aspect-square relative flex items-center justify-center">
                
                <div class="absolute w-80 h-80 border border-white/10 rounded-full"></div>
                <div class="absolute w-60 h-60 border border-white/5 rounded-full"></div>
                <div class="absolute w-40 h-40 border border-white/5 rounded-full"></div>
                
                <div v-for="i in 5" :key="i" class="absolute w-full h-[1px] bg-white/5" :style="{ transform: `rotate(${i*36}deg)` }"></div>
                
                <svg v-if="chartMode === 'individual'" class="absolute w-80 h-80 overflow-visible" viewBox="0 0 100 100">
                <polygon points="50,20 80,45 70,80 30,80 20,45" fill="rgba(16, 185, 129, 0.2)" stroke="#10b981" stroke-width="1.5" class="animate-pulse" />
                </svg>

                <svg v-if="chartMode === 'compare'" class="absolute w-80 h-80 overflow-visible" viewBox="0 0 100 100">
                <polygon points="50,15 85,40 75,85 25,85 15,40" fill="rgba(59, 130, 246, 0.15)" stroke="#3b82f6" stroke-width="1.5" stroke-dasharray="3,3" />
                <polygon points="50,30 75,50 65,75 35,80 25,45" fill="rgba(16, 185, 129, 0.3)" stroke="#10b981" stroke-width="2" />
                </svg>

                <div v-for="point in abilityPoints" :key="point.name" 
                :class="point.active ? 'text-white' : 'text-gray-600'"
                class="absolute flex flex-col items-center gap-1 transition-all duration-500 hover:scale-110 cursor-pointer"
                :style="point.pos"
                >
                <span class="text-xs">{{ point.icon }}</span>
                <span class="text-[9px] font-bold tracking-tighter">{{ point.name }}</span>
                </div>
            </div>

            <div class="w-full lg:w-1/2 space-y-6">
                <div>
                <h4 class="text-xs font-black text-gray-500 uppercase tracking-widest mb-3">🔥 DNA 匹配差值诊断 (GAP Analysis)</h4>
                <div class="space-y-3.5">
                    <div v-for="stat in gapMetrics" :key="stat.name" class="space-y-1">
                    <div class="flex justify-between text-[10px] font-bold">
                        <span class="text-gray-300">{{ stat.name }}</span>
                        <span :class="stat.gap >= 0 ? 'text-green-400' : 'text-rose-400'">
                        {{ stat.gap >= 0 ? `+${stat.gap}%` : `${stat.gap}%` }}
                        </span>
                    </div>
                    <div class="w-full h-1 bg-white/10 rounded-full overflow-hidden flex">
                        <div class="h-full bg-blue-600" :style="{ width: stat.target + '%' }"></div>
                        <div :class="stat.gap >= 0 ? 'bg-green-500' : 'bg-rose-500'" :style="{ width: Math.abs(stat.gap) + '%' }"></div>
                    </div>
                    </div>
                </div>
                </div>

                <div class="p-6 bg-white/5 rounded-[32px] border border-white/10 relative overflow-hidden">
                <span class="absolute top-0 right-0 bg-blue-600 text-white text-[8px] font-black px-2.5 py-1 rounded-bl-xl">KIRIN CO-PILOT</span>
                <p class="text-[10px] font-black text-blue-400 mb-2">🎓 适配度跃升路径推荐</p>
                <p class="text-xs text-gray-300 leading-relaxed font-medium">
                    您的“国密合规”维度与【飞腾底层开发岗】存在 <strong class="text-rose-400">-15%</strong> 极径差值。系统推荐您修读：<span class="text-white font-bold underline">《银河麒麟高级国密安全应用指南》</span>，预计学习后适配度将跃升至 <strong class="text-green-400">95.8%</strong>。
                </p>
                </div>
            </div>

            </div>

            <div class="mt-auto pt-6 border-t border-white/10 grid grid-cols-3 gap-6">
            <div v-for="feat in techFeatures" :key="feat.title">
                <span class="text-[9px] font-mono text-gray-500 block mb-1">REFERENCE</span>
                <p class="text-xs font-black text-blue-500 mb-0.5">{{ feat.title }}</p>
                <p class="text-[10px] text-gray-400 leading-snug">{{ feat.desc }}</p>
            </div>
            </div>

        </div>
        </div>

    </div>
    </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

// 1. 状态定义
const currentChannel = ref<'candidate' | 'job'>('candidate')
const chartMode = ref<'compare' | 'individual'>('compare')
const isScanning = ref(false)
const selectedDocId = ref('doc-1')

// 2. 模拟多文档库数据 (一站式解决“多简历”和“JD解析”痛点)
const docRepo = ref([
    { id: 'doc-1', name: '个人简历-底层研发内核适配版.pdf', type: 'candidate', date: '2026-05-01', status: 'success' },
    { id: 'doc-2', name: '个人简历-AI算法工程适配版.docx', type: 'candidate', date: '2026-05-05', status: 'success' },
    { id: 'doc-3', name: '【华为】云计算平台架构师说明书.pdf', type: 'job', date: '2026-05-02', status: 'success' },
    { id: 'doc-4', name: '【飞腾】信创底层研发专家JD.docx', type: 'job', date: '2026-05-08', status: 'pending' },
])

// 3. 针对不同文档提取出的结构化特征元数据
const resumeSchemas: Record<string, Record<string, string>> = {
    'doc-1': {
    '核心技能 (Expertise)': 'C++ / Linux Kernel / Qt',
    '教育背景 (Education)': '软件技术专业 (2026届)',
    '核心履历 (Experience)': '第十四届中国软件杯全国二等奖',
    '信创标签 (Ecosystem)': '银河麒麟 V10 / 飞腾派适配',
    '技术成果 (Artifacts)': '编写 2.5w 行国产系统迁移驱动代码'
    },
    'doc-2': {
    '核心技能 (Expertise)': 'Python / PyTorch / DeepSeek',
    '教育背景 (Education)': '软件技术专业 (AI算法方向)',
    '核心履历 (Experience)': '金砖国家技能大赛算法赛一等奖',
    '信创标签 (Ecosystem)': '昇腾 NPU / Ollama 本地部署',
    '技术成果 (Artifacts)': '开发筑理·考工台 Web3D AI分析模块'
    }
}

const jobSchemas: Record<string, Record<string, string>> = {
    'doc-3': {
    '岗位要求 (Requirements)': 'Golang / K8s / Docker 深度底层',
    '适配平台 (Platform)': '鲲鹏芯片架构 / 欧拉 OS 适配',
    '职责描述 (Responsibilities)': '负责政企私有云信创环境的部署与调优',
    '核心福利 (Benefits)': '40k-70k / 北京 / 16薪'
    },
    'doc-4': {
    '岗位要求 (Requirements)': 'ARM 汇编 / Linux 内核源码级定制',
    '适配平台 (Platform)': '飞腾处理器系列 / 银河麒麟系统',
    '职责描述 (Responsibilities)': '主导信创安全底层硬件虚拟化和国密适配开发',
    '核心福利 (Benefits)': '35k-55k / 天津 / 六险一金'
    }
}

// 4. 计算当前选中的文档数据
const activeSchema = computed(() => {
    if (currentChannel.value === 'candidate') {
    return resumeSchemas[selectedDocId.value] || resumeSchemas['doc-1']
    } else {
    return jobSchemas[selectedDocId.value] || jobSchemas['doc-3']
    }
})

// 5. 能力图谱骨架和位置
const abilityPoints = [
    { name: '技术深度', icon: '🛡️', pos: { top: '8%', left: '42%' }, active: true },
    { name: '信创贡献', icon: '🌐', pos: { top: '33%', left: '84%' }, active: true },
    { name: '安全合规', icon: '⚙️', pos: { top: '75%', left: '68%' }, active: true },
    { name: '工程落地', icon: '💡', pos: { top: '75%', left: '16%' }, active: true },
    { name: '协作共创', icon: '🤝', pos: { top: '33%', left: '2%' }, active: false },
]

// 6. 极径差值诊断数据 (模拟实时计算，用于“对比视图”)
const gapMetrics = ref([
    { name: '底层技术 (Low-Level Tech)', target: 85, gap: 5 },
    { name: '信创适配 (Kirin Eco)', target: 70, gap: 15 },
    { name: '安全合规 (Cryptographic Compliance)', target: 80, gap: -15 },
    { name: 'AI集成 (RAG/NLP Engine)', target: 60, gap: 20 },
])

const techFeatures = [
    { title: '多文档管理', desc: '支持建立多份简历的分身，针对不同行业、信创赛道进行个性化配置管理。' },
    { title: '极径契合度', desc: '采用欧氏距离算法，自动算出个人图谱与职位要求的能力边界贴合度。' },
    { title: '国标级映射', desc: '字段完全对接工信部《信创产业技术人才标准（2025）》能力成熟度。' }
]

// 7. 行为方法
const selectDoc = (doc: any) => {
    selectedDocId.value = doc.id
}

const simulateUpload = () => {
    isScanning.value = true
    setTimeout(() => {
    isScanning.value = false
    const newDocId = `doc-${Date.now()}`
    docRepo.value.push({
        id: newDocId,
        name: currentChannel.value === 'candidate' ? '个人简历-新上传版本.pdf' : '企业最新职位说明.docx',
        type: currentChannel.value,
        date: new Date().toLocaleDateString(),
        status: 'success'
    })
    
    // 初始化新文档的模拟数据
    if (currentChannel.value === 'candidate') {
        resumeSchemas[newDocId] = {
        '核心技能 (Expertise)': 'Vue3 / SpringBoot / UniApp',
        '教育背景 (Education)': '软件技术 (全栈工程开发)',
        '核心履历 (Experience)': '软件工作室负责人 (Captain)',
        '信创标签 (Ecosystem)': '信创微服务适配架构',
        '技术成果 (Artifacts)': '设计并开发了一套高灵敏、Apple风格的KYC信创系统'
        }
    } else {
        jobSchemas[newDocId] = {
        '岗位要求 (Requirements)': 'Java SpringBoot / 数据库本地化适配经验',
        '适配平台 (Platform)': '达梦数据库 / 中标麒麟环境',
        '职责描述 (Responsibilities)': '进行高并发核心B端业务系统的信创数据库安全迁移开发',
        '核心福利 (Benefits)': '30k-45k / 南京 / 双休'
        }
    }
    
    selectedDocId.value = newDocId
    }, 2000)
}
</script>

<style scoped>
@keyframes scan {
    0% { top: 0; opacity: 0; }
    50% { opacity: 1; }
    100% { top: 100%; opacity: 0; }
}
.animate-scan {
    animation: scan 2.5s cubic-bezier(0.4, 0, 0.2, 1) infinite;
}
.no-scrollbar::-webkit-scrollbar { display: none; }
</style>