<template>
<div class="w-full min-w-0 p-4 sm:p-8 animate-in fade-in duration-1000 pb-20">
    <header class="mb-10 px-2 flex justify-between items-end">
    <div>
        <h2 class="text-4xl font-black text-black tracking-tighter">人才数字化双生 <span class="text-blue-600">Digital Twin</span></h2>
        <p class="text-xs text-gray-400 font-bold uppercase mt-2 tracking-[0.2em]">基于 NLP 深度学习的文档解析与多维能力建模系统</p>
    </div>
    <div class="text-right hidden md:block">
        <span class="text-[10px] font-mono text-gray-400">ENGINE: DeepSeek-R1 / Kirin-NLP-v2</span>
    </div>
    </header>

    <div class="grid grid-cols-12 gap-8">
    
    <div class="col-span-12 xl:col-span-5 space-y-6">
        
        <div class="bg-white rounded-[40px] p-8 border border-white shadow-sm hover:shadow-xl transition-all group overflow-hidden">
        <div class="flex items-center justify-between mb-6">
            <h3 class="text-lg font-black flex items-center gap-2">
            <span class="w-8 h-8 bg-black text-white rounded-full flex items-center justify-center text-xs">01</span>
            多格式文档智能采集
            </h3>
            <span class="text-[10px] bg-blue-50 text-blue-600 px-2 py-1 rounded font-bold">OCR + NLP</span>
        </div>
        
        <div @click="simulateUpload" class="relative border-2 border-dashed border-gray-100 rounded-[32px] p-10 flex flex-col items-center justify-center group-hover:border-blue-300 transition-all cursor-pointer bg-gray-50/30 overflow-hidden">
            <div v-if="isScanning" class="absolute inset-0 bg-blue-600/5 animate-pulse"></div>
            <div v-if="isScanning" class="absolute top-0 left-0 w-full h-1 bg-blue-500 shadow-[0_0_20px_blue] animate-scan"></div>
            
            <div class="text-4xl mb-3 group-hover:-translate-y-2 transition-transform duration-500">📄</div>
            <p class="text-sm font-black text-gray-600">{{ fileName || '点击或拖拽上传简历/JD文档' }}</p>
            <div class="flex gap-4 mt-4 text-[9px] font-bold text-gray-400">
            <span>PDF</span><span>DOCX</span><span>JPG</span><span>WPS</span>
            </div>
        </div>
        </div>

        <div class="bg-gray-900 rounded-[40px] p-8 text-green-400 font-mono text-[11px] h-[400px] shadow-2xl relative">
        <div class="flex justify-between items-center mb-4 text-gray-500 border-b border-white/10 pb-4">
            <span class="font-bold tracking-widest text-[9px]">PARSING LOGS</span>
            <span class="flex gap-1"><i class="w-1.5 h-1.5 bg-red-500 rounded-full"></i><i class="w-1.5 h-1.5 bg-yellow-500 rounded-full"></i><i class="w-1.5 h-1.5 bg-green-500 rounded-full"></i></span>
        </div>
        <div class="space-y-2 overflow-y-auto h-[300px] no-scrollbar" ref="logContainer">
            <div v-for="(log, i) in parsingLogs" :key="i" class="animate-in fade-in slide-in-from-left-2">
            <span class="opacity-40">[{{ log.time }}]</span> 
            <span :class="log.type === 'info' ? 'text-blue-400' : 'text-green-400'">{{ log.msg }}</span>
            </div>
            <div v-if="isParsed" class="mt-4 p-4 bg-green-500/10 border border-green-500/20 rounded-xl text-green-400 italic">
            >> 解析完成: 提取出 42 个结构化标签, 映射至 5 个能力维度
            </div>
        </div>
        </div>
    </div>

    <div class="col-span-12 xl:col-span-7 space-y-6">
        
        <div class="bg-white rounded-[48px] p-10 border border-white shadow-sm h-full flex flex-col relative overflow-hidden">
        <div class="flex justify-between items-start mb-8">
            <div>
            <h3 class="text-xl font-black flex items-center gap-2">
                <span class="w-8 h-8 bg-blue-600 text-white rounded-full flex items-center justify-center text-xs shadow-lg shadow-blue-200">02</span>
                多维能力谱系图
            </h3>
            <p class="text-xs text-gray-400 mt-1 font-bold">Vector-based Competency Mapping</p>
            </div>
            <div class="flex bg-gray-50 p-1 rounded-xl">
            <button class="px-4 py-1.5 text-[10px] font-black rounded-lg bg-white shadow-sm">个人图谱</button>
            <button class="px-4 py-1.5 text-[10px] font-black text-gray-400">对比职位</button>
            </div>
        </div>

        <div class="flex-1 flex flex-col lg:flex-row items-center gap-10">
            <div class="w-full lg:w-1/2 aspect-square relative flex items-center justify-center">
            <div class="absolute inset-0 bg-gradient-to-br from-blue-50 to-transparent rounded-full animate-pulse"></div>
            <div class="absolute w-full h-full border border-gray-100 rounded-full scale-100"></div>
            <div class="absolute w-full h-full border border-gray-100 rounded-full scale-75"></div>
            <div class="absolute w-full h-full border border-gray-100 rounded-full scale-50"></div>
            <div v-for="i in 6" :key="i" class="absolute w-full h-[1px] bg-gray-100" :style="{ transform: `rotate(${i*30}deg)` }"></div>
            
            <div v-for="point in abilityPoints" :key="point.name" 
                class="absolute w-3 h-3 bg-blue-600 rounded-full shadow-[0_0_15px_rgba(37,99,235,0.6)] cursor-help transition-all hover:scale-150"
                :style="point.style">
                <div class="absolute -top-8 left-1/2 -translate-x-1/2 bg-black text-white text-[9px] px-2 py-1 rounded whitespace-nowrap opacity-0 group-hover:opacity-100">{{ point.name }}</div>
            </div>
            </div>

            <div class="w-full lg:w-1/2 space-y-6">
            <h4 class="text-xs font-black text-gray-400 uppercase tracking-widest border-b pb-2">结构化特征向量 (Feature Vectors)</h4>
            <div class="flex flex-wrap gap-2">
                <span v-for="tag in parsedTags" :key="tag.text" 
                :class="tag.active ? 'bg-blue-600 text-white' : 'bg-gray-50 text-gray-400'"
                class="px-3 py-1.5 rounded-xl text-[10px] font-bold transition-all duration-500">
                {{ tag.text }}
                </span>
            </div>
            <div class="mt-8 p-6 bg-blue-50 rounded-[32px]">
                <p class="text-[10px] font-black text-blue-600 mb-2 tracking-tighter">AI 职业匹配建议</p>
                <p class="text-xs font-bold text-blue-900 leading-relaxed">您的核心优势在于“底层适配”与“大语言模型集成”，建议关注【信创 AI 架构师】方向，该维度匹配度高达 94.2%。</p>
            </div>
            </div>
        </div>
        </div>

        <div class="grid grid-cols-3 gap-6">
        <div v-for="feat in keyFeatures" :key="feat.title" class="bg-white border border-gray-100 p-6 rounded-[32px] hover:border-blue-200 transition-all">
            <div class="text-2xl mb-3">{{ feat.icon }}</div>
            <h5 class="text-[11px] font-black mb-1 uppercase tracking-tight">{{ feat.title }}</h5>
            <p class="text-[10px] text-gray-400 leading-tight">{{ feat.desc }}</p>
        </div>
        </div>
    </div>

    </div>
</div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

const isScanning = ref(false)
const isParsed = ref(false)
const fileName = ref('')
const parsingLogs = ref<{time: string, msg: string, type: string}[]>([])

const addLog = (msg: string, type = 'info') => {
const now = new Date().toLocaleTimeString().split(' ')[0]
parsingLogs.value.unshift({ time: now, msg, type })
}

const simulateUpload = () => {
fileName.value = 'Candidate_Profile_Kirin.pdf'
isScanning.value = true
isParsed.value = false
parsingLogs.value = []

// 模拟深度的 NLP 解析过程
setTimeout(() => addLog("正在加载多维解析内核 v2.1..."), 200)
setTimeout(() => addLog("识别文档格式: Adobe PDF (Text-Based)"), 600)
setTimeout(() => addLog("执行 OCR 光学字符识别... [完毕]", 'success'), 1000)
setTimeout(() => addLog("正在提取命名实体 (NER)..."), 1400)
setTimeout(() => addLog("检测到核心技能: Linux Kernel, C++, Vue3", 'success'), 1800)
setTimeout(() => addLog("正在进行信创兼容性评估 (Kirin-Ecosystem)..."), 2200)
setTimeout(() => {
    isScanning.value = false
    isParsed.value = true
    addLog("结构化建模成功。已存入向量数据库。", 'success')
}, 3000)
}

const abilityPoints = [
{ name: '技术深度', style: { top: '20%', left: '50%' } },
{ name: '信创贡献', style: { top: '40%', left: '80%' } },
{ name: '工程实践', style: { top: '80%', left: '70%' } },
{ name: '安全合规', style: { top: '70%', left: '20%' } },
{ name: '协作能力', style: { top: '30%', left: '15%' } },
]

const parsedTags = ref([
{ text: '银河麒麟', active: true }, { text: 'DeepSeek-V3', active: true },
{ text: 'SpringBoot', active: false }, { text: '内核适配', active: true },
{ text: '国密算法', active: true }, { text: 'RAG 架构', active: true },
{ text: 'Docker', active: false }, { text: '分布式缓存', active: false }
])

const keyFeatures = [
{ icon: '🧬', title: '双生建模', desc: '基于 128 位向量维度构建数字孪生画像' },
{ icon: '📡', title: '实时透视', desc: '图形化展示能力与职位要求的极径距离' },
{ icon: '🛡️', title: '信创合规', desc: '自动校验技能是否符合国产化安全体系标准' }
]
</script>

<style scoped>
@keyframes scan { 0% { top: 0; opacity: 0; } 50% { opacity: 1; } 100% { top: 100%; opacity: 0; } }
.animate-scan { animation: scan 2s cubic-bezier(0.4, 0, 0.2, 1) infinite; }
.no-scrollbar::-webkit-scrollbar { display: none; }
</style>