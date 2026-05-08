<template>
    <div class="h-full w-full flex flex-col p-6 sm:p-10 bg-[#F5F5F7] relative overflow-hidden font-sans">
    
    <div class="absolute -top-40 -right-40 w-[500px] h-[500px] bg-gradient-to-tr from-blue-500/10 to-indigo-500/5 rounded-full blur-[100px] pointer-events-none"></div>
    <div class="absolute -bottom-40 -left-40 w-[400px] h-[400px] bg-gradient-to-tr from-emerald-500/5 to-teal-500/5 rounded-full blur-[80px] pointer-events-none"></div>

    <header class="flex-shrink-0 mb-8 flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 relative z-10">
        <div>
        <div class="inline-flex items-center gap-1.5 px-3 py-1 bg-black/[0.04] rounded-full mb-2">
            <span class="w-1.5 h-1.5 bg-blue-600 rounded-full animate-pulse"></span>
            <span class="text-[10px] font-bold tracking-widest uppercase text-gray-500">Kirin Match-Engine v3.0</span>
        </div>
        <h2 class="text-3xl font-black text-black tracking-tight">智能双向匹配中心</h2>
        </div>

        <div class="flex items-center gap-3 bg-white px-5 py-3 rounded-2xl shadow-sm hover:shadow-md transition-all duration-300">
        <span class="text-[10px] font-black text-gray-400 uppercase tracking-wider">Active Profile</span>
        <div class="h-4 w-[1px] bg-gray-200"></div>
        <select 
            v-model="activeResumeId" 
            class="bg-transparent text-xs font-bold text-gray-800 focus:outline-none cursor-pointer pr-2"
        >
            <option value="resume-1">📄 个人简历 - 底层研发内核适配版 (Linux Kernel / C++)</option>
            <option value="resume-2">📄 个人简历 - AI 算法与全栈版 (Qwen / DeepSeek / Vue3)</option>
            <option value="resume-3">📄 个人简历 - 标准信创应用开发版 (Java / SpringBoot)</option>
            <option value="resume-4">📄 个人简历 - 涉外项目安全集成版 (JLPT N2 / Security)</option>
            <option value="resume-5">📄 个人简历 - 云原生与高性能计算版 (Go / Rust / K8s)</option>
        </select>
        </div>
    </header>

    <div class="flex-1 min-h-0 grid grid-cols-12 gap-8 relative z-10">
        
        <div class="col-span-12 lg:col-span-4 h-full flex flex-col overflow-y-auto no-scrollbar pr-1">
        <div class="bg-white rounded-[32px] p-8 shadow-[0_4px_24px_rgba(0,0,0,0.02)] space-y-8 flex-shrink-0">
            
            <div class="flex justify-between items-center">
            <div>
                <h4 class="text-xs font-black text-black tracking-wider uppercase">🎛️ 主动动态加分器</h4>
                <p class="text-[10px] text-gray-400 mt-1">微调权重，右侧匹配分实时重算</p>
            </div>
            <button @click="resetFilters" class="text-xs font-bold text-blue-600 hover:text-blue-800 transition-colors">重置</button>
            </div>

            <div class="space-y-4">
            <div class="flex justify-between items-center text-xs">
                <span class="font-bold text-gray-800">📍 城市契合度</span>
                <span class="font-mono text-blue-600 font-extrabold bg-blue-50 px-2 py-0.5 rounded-md">+{{ cityWeight }}% 加分</span>
            </div>
            
            <div class="space-y-2">
                <span class="text-[10px] font-black text-gray-400 uppercase tracking-widest pl-1">📍 现居城市 (Current Location)</span>
                <div class="grid grid-cols-2 gap-3">
                <div class="bg-gray-50/70 rounded-2xl p-3 hover:bg-gray-100/50 transition-colors">
                    <label class="text-[9px] text-gray-400 block font-bold mb-1">选择省份</label>
                    <select v-model="currentProvince" @change="onCurrentProvinceChange" class="bg-transparent text-xs font-bold text-black focus:outline-none w-full cursor-pointer">
                    <option v-for="prov in provinces" :key="prov.name" :value="prov.name">{{ prov.name }}</option>
                    </select>
                </div>
                <div class="bg-gray-50/70 rounded-2xl p-3 hover:bg-gray-100/50 transition-colors">
                    <label class="text-[9px] text-gray-400 block font-bold mb-1">选择城市</label>
                    <select v-model="userGeo.current" class="bg-transparent text-xs font-bold text-black focus:outline-none w-full cursor-pointer">
                    <option v-for="city in currentCities" :key="city" :value="city">{{ city }}</option>
                    </select>
                </div>
                </div>
            </div>

            <div class="space-y-2 pt-2">
                <span class="text-[10px] font-black text-gray-400 uppercase tracking-widest pl-1">🎯 意向城市 (Target Location)</span>
                <div class="grid grid-cols-2 gap-3">
                <div class="bg-gray-50/70 rounded-2xl p-3 hover:bg-gray-100/50 transition-colors">
                    <label class="text-[9px] text-gray-400 block font-bold mb-1">选择省份</label>
                    <select v-model="targetProvince" @change="onTargetProvinceChange" class="bg-transparent text-xs font-bold text-black focus:outline-none w-full cursor-pointer">
                    <option v-for="prov in provinces" :key="prov.name" :value="prov.name">{{ prov.name }}</option>
                    </select>
                </div>
                <div class="bg-gray-50/70 rounded-2xl p-3 hover:bg-gray-100/50 transition-colors">
                    <label class="text-[9px] text-gray-400 block font-bold mb-1">选择城市</label>
                    <select v-model="userGeo.target" class="bg-transparent text-xs font-bold text-black focus:outline-none w-full cursor-pointer">
                    <option v-for="city in targetCities" :key="city" :value="city">{{ city }}</option>
                    </select>
                </div>
                </div>
            </div>

            <div class="pt-4">
                <input 
                type="range" 
                v-model="cityWeight" 
                min="0" 
                max="50" 
                class="apple-slider w-full"
                :style="getSliderStyle(cityWeight, 0, 50, '#0071e3')"
                />
            </div>
            </div>

            <div class="space-y-4">
            <div class="flex justify-between items-center text-xs">
                <span class="font-bold text-gray-800">🛡️ 国产软硬件生态</span>
                <span class="font-mono text-emerald-600 font-extrabold bg-emerald-50 px-2 py-0.5 rounded-md">+{{ xinchuangWeight }}% 加分</span>
            </div>
            <div>
                <input 
                type="range" 
                v-model="xinchuangWeight" 
                min="0" 
                max="50" 
                class="apple-slider w-full"
                :style="getSliderStyle(xinchuangWeight, 0, 50, '#10b981')"
                />
            </div>
            <p class="text-[10px] text-gray-400 leading-relaxed">智能倾斜支持银河麒麟OS、飞腾、中电科等信创链产品调试经历的岗位。</p>
            </div>

            <div class="space-y-4">
            <div class="flex justify-between items-center text-xs">
                <span class="font-bold text-gray-800">💵 薪资期望吻合</span>
                <span class="font-mono text-amber-600 font-extrabold bg-amber-50 px-2 py-0.5 rounded-md">+{{ salaryWeight }}% 加分</span>
            </div>
            <div>
                <input 
                type="range" 
                v-model="salaryWeight" 
                min="0" 
                max="50" 
                class="apple-slider w-full"
                :style="getSliderStyle(salaryWeight, 0, 50, '#f59e0b')"
                />
            </div>
            </div>

            <div class="pt-6 border-t border-gray-100 space-y-3">
            <h5 class="text-[9px] font-black text-gray-400 uppercase tracking-widest">🌟 额外资质加分 (Booster)</h5>
            
            <div class="grid grid-cols-1 gap-2.5">
                <button 
                @click="extraBoosters.cet4 = !extraBoosters.cet4"
                :class="extraBoosters.cet4 ? 'bg-blue-50/50 shadow-sm' : 'bg-gray-50/50 hover:bg-gray-100/30'"
                class="flex items-center justify-between p-4 rounded-2xl transition-all duration-300 text-left"
                >
                <div class="flex items-center gap-3">
                    <span class="text-lg">🇬🇧</span>
                    <div>
                    <p class="text-xs font-bold text-gray-900">CET-4 英语过级</p>
                    <p class="text-[9px] text-gray-400">大中型信创国企加分项</p>
                    </div>
                </div>
                <div class="w-4 h-4 rounded-full flex items-center justify-center border transition-all"
                    :class="extraBoosters.cet4 ? 'bg-blue-600 border-transparent text-white text-[9px]' : 'border-gray-300 bg-white'">
                    <span v-if="extraBoosters.cet4">✓</span>
                </div>
                </button>

                <button 
                @click="extraBoosters.jlpt = !extraBoosters.jlpt"
                :class="extraBoosters.jlpt ? 'bg-indigo-50/50 shadow-sm' : 'bg-gray-50/50 hover:bg-gray-100/30'"
                class="flex items-center justify-between p-4 rounded-2xl transition-all duration-300 text-left"
                >
                <div class="flex items-center gap-3">
                    <span class="text-lg">🇯🇵</span>
                    <div>
                    <p class="text-xs font-bold text-gray-900">JLPT N2 日语过级</p>
                    <p class="text-[9px] text-gray-400">涉外安全集成加分项</p>
                    </div>
                </div>
                <div class="w-4 h-4 rounded-full flex items-center justify-center border transition-all"
                    :class="extraBoosters.jlpt ? 'bg-indigo-600 border-transparent text-white text-[9px]' : 'border-gray-300 bg-white'">
                    <span v-if="extraBoosters.jlpt">✓</span>
                </div>
                </button>
            </div>
            </div>

        </div>
        </div>

        <div class="col-span-12 lg:col-span-8 h-full flex flex-col overflow-y-auto no-scrollbar pb-10">
        <div class="space-y-6">
            
            <div class="flex justify-between items-center px-2">
            <div>
                <h3 class="text-lg font-black text-black">📌 AI 匹配度最佳机遇 (Matches)</h3>
                <p class="text-xs text-gray-400">基于信创安全标准与多维算法实时运算</p>
            </div>
            
            <div class="flex items-center gap-3">
                <div class="flex bg-white/80 p-1 rounded-xl shadow-sm border border-gray-100">
                <button 
                    @click="showAllJobs = false" 
                    :class="[!showAllJobs ? 'bg-black text-white' : 'text-gray-500 hover:text-black']"
                    class="px-3 py-1.5 rounded-lg text-xs font-bold transition-all"
                >
                    精选匹配
                </button>
                <button 
                    @click="showAllJobs = true" 
                    :class="[showAllJobs ? 'bg-black text-white' : 'text-gray-500 hover:text-black']"
                    class="px-3 py-1.5 rounded-lg text-xs font-bold transition-all"
                >
                    查看全部 ({{ rawJobs.length }})
                </button>
                </div>
                
                <span class="text-[10px] font-mono bg-black text-white px-3 py-2 rounded-full font-bold shadow-sm">
                FOUND: {{ filteredJobs.length }}
                </span>
            </div>
            </div>

            <div class="space-y-6">
            <div 
                v-for="item in filteredJobs" 
                :key="item.id" 
                class="bg-white rounded-[32px] p-8 shadow-[0_4px_30px_rgba(0,0,0,0.01)] hover:shadow-[0_12px_40px_rgba(0,0,0,0.04)] transition-all duration-500 relative overflow-hidden group"
            >
                <div class="absolute inset-0 bg-gradient-to-r from-blue-500/[0.01] to-indigo-500/[0.01] opacity-0 group-hover:opacity-100 transition-opacity duration-500 pointer-events-none"></div>

                <div class="flex flex-col sm:flex-row justify-between items-start gap-6 border-b border-gray-50 pb-6 relative z-10">
                <div class="flex gap-4">
                    <div class="w-14 h-14 rounded-2xl bg-[#F5F5F7] text-gray-800 text-lg font-black flex items-center justify-center group-hover:bg-black group-hover:text-white transition-all duration-500 shadow-sm flex-shrink-0">
                    {{ item.avatar }}
                    </div>
                    <div class="space-y-2">
                    <h4 class="text-lg font-black text-black group-hover:text-blue-600 transition-colors duration-300">
                        {{ item.title }}
                        <span class="text-base text-rose-500 font-mono font-black ml-2">{{ item.salary }}</span>
                    </h4>
                    <p class="text-sm text-gray-500 font-bold">
                        {{ item.company }} <span class="mx-1.5 text-gray-200">|</span> <span>{{ item.city }}</span>
                    </p>
                    
                    <div class="flex flex-wrap gap-1.5 pt-1">
                        <span v-for="tag in item.tags" :key="tag" class="px-3 py-1.5 rounded-lg text-xs font-bold bg-gray-50 text-gray-400">
                        # {{ tag }}
                        </span>
                    </div>
                    </div>
                </div>

                <div class="flex items-center gap-3 self-end sm:self-auto bg-gray-50/50 p-2.5 rounded-2xl">
                    <div class="w-11 h-11 relative flex items-center justify-center">
                    <svg class="absolute w-full h-full transform -rotate-90">
                        <circle cx="22" cy="22" r="18" stroke="#eaeded" stroke-width="3" fill="transparent"/>
                        <circle cx="22" cy="22" r="18" :stroke="item.score >= 80 ? '#10b981' : '#3b82f6'" stroke-width="3" fill="transparent"
                        :stroke-dasharray="2 * Math.PI * 18"
                        :stroke-dashoffset="2 * Math.PI * 18 * (1 - item.score / 100)"
                        class="transition-all duration-1000 ease-out"
                        />
                    </svg>
                    <span class="text-xs font-black font-mono text-black">{{ item.score }}</span>
                    </div>
                    <div class="pr-2">
                    <span class="text-[9px] font-black text-gray-400 block uppercase tracking-widest">Match Rate</span>
                    </div>
                </div>
                </div>

                <div class="flex flex-wrap gap-2 my-4">
                <span v-if="item.city === userGeo.current" class="text-xs bg-blue-50/60 text-blue-600 px-3 py-1 rounded-lg font-bold">
                    📍 现居{{ userGeo.current }}本地 (+{{ cityWeight }}%)
                </span>
                <span v-else-if="item.city === userGeo.target" class="text-xs bg-indigo-50/60 text-indigo-600 px-3 py-1 rounded-lg font-bold">
                    🎯 意向目标城市 (+{{ Math.round(cityWeight * 0.8) }}%)
                </span>
                <span v-if="item.hasXinChuang" class="text-xs bg-emerald-50/60 text-emerald-600 px-3 py-1 rounded-lg font-bold">
                    🛡️ 核心信创生态 (+{{ xinchuangWeight }}%)
                </span>
                <span v-if="extraBoosters.cet4" class="text-xs bg-purple-50/60 text-purple-600 px-3 py-1 rounded-lg font-bold">
                    🇬🇧 英语 CET-4 (+3%)
                </span>
                </div>

                <div class="grid grid-cols-1 md:grid-cols-2 gap-6 bg-gray-50/40 p-6 rounded-2xl mb-5">
                <div class="space-y-2">
                    <span class="text-emerald-600 font-extrabold text-xs flex items-center gap-1.5">
                    <span class="w-2 h-2 bg-emerald-500 rounded-full animate-pulse"></span> 
                    能力突围 (ADVANTAGE)
                    </span>
                    <p class="text-sm text-gray-600 leading-relaxed font-medium">{{ item.reasonPro }}</p>
                </div>
                <div class="space-y-2">
                    <span class="text-rose-500 font-extrabold text-xs flex items-center gap-1.5">
                    <span class="w-2 h-2 bg-rose-400 rounded-full"></span>
                    生态短板 (COMPETENCY GAP)
                    </span>
                    <p class="text-sm text-gray-600 leading-relaxed font-medium">{{ item.reasonContra }}</p>
                </div>
                </div>

                <div class="space-y-4 relative z-10">
                
                <div class="p-4 bg-blue-50/20 rounded-2xl flex justify-between items-center gap-4 hover:bg-blue-50/40 transition-colors duration-300">
                    <p class="text-xs sm:text-sm text-gray-500 italic truncate flex-1 font-medium">"{{ generateIntro(item) }}"</p>
                    <button @click="copyIntro(item)" class="text-xs font-black text-blue-600 hover:text-blue-800 transition-colors flex-shrink-0">
                    一键复制自荐语
                    </button>
                </div>

                <div class="bg-gray-50/20 rounded-2xl p-5 space-y-3">
                    <div class="flex justify-between items-center">
                    <span class="text-[10px] font-black text-amber-600 tracking-wider uppercase">🎯 信创模拟面试备考库</span>
                    <button 
                        @click="toggleInterview(item.id)" 
                        class="text-xs font-bold text-gray-500 bg-white hover:bg-gray-100 px-3.5 py-1.5 rounded-xl shadow-sm transition-all"
                    >
                        {{ activeInterviewId === item.id ? '收起真题' : 'AI 释出真题' }}
                    </button>
                    </div>
                    
                    <div v-if="activeInterviewId === item.id" class="space-y-5 pt-4 border-t border-dashed border-gray-200 animate-in fade-in duration-300">
                    <div v-for="(qa, idx) in item.interviewQA" :key="idx" class="space-y-2">
                        <p class="font-bold text-sm text-black flex gap-1"><span class="text-amber-500">Q{{ idx + 1 }}:</span> {{ qa.q }}</p>
                        <div class="text-sm text-gray-600 leading-relaxed bg-white p-4 rounded-xl shadow-[0_2px_12px_rgba(0,0,0,0.01)] border border-gray-100/50">
                        <span class="text-[10px] font-black text-emerald-600 uppercase block mb-1">💡 推荐应答逻辑</span>
                        {{ qa.a }}
                        </div>
                    </div>
                    </div>
                </div>

                </div>

            </div>
            </div>

        </div>
        </div>

    </div>
    </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'

// ==========================================
// 🌌 极简内置全国省市行政区划数据包（免体积引入）
// ==========================================
const provinces = [
    { name: '安徽', cities: ['滁州', '合肥', '芜湖', '马鞍山', '蚌埠', '安庆', '阜阳'] },
    { name: '江苏', cities: ['南京', '苏州', '无锡', '常州', '南通', '扬州', '徐州', '盐城'] },
    { name: '北京', cities: ['北京'] },
    { name: '上海', cities: ['上海'] },
    { name: '广东', cities: ['深圳', '广州', '东莞', '佛山', '珠海', '中山'] },
    { name: '浙江', cities: ['杭州', '宁波', '温州', '绍兴', '嘉兴', '金华'] },
    { name: '四川', cities: ['成都', '绵阳', '德阳', '宜宾'] },
    { name: '湖北', cities: ['武汉', '宜昌', '襄阳'] }
]

// 1. 联动状态控制
const currentProvince = ref('安徽')
const targetProvince = ref('江苏')

// 现居城市与意向城市的当前子级列表
const currentCities = computed(() => {
    const p = provinces.find(prov => prov.name === currentProvince.value)
    return p ? p.cities : []
})

const targetCities = computed(() => {
    const p = provinces.find(prov => prov.name === targetProvince.value)
    return p ? p.cities : []
})

// 城市实际绑定值（在城市改变时自适应）
const userGeo = ref({
    current: '滁州', 
    target: '南京'   
})

// 防止切换省份后，子城市选中残留越界错误
const onCurrentProvinceChange = () => {
    if (currentCities.value.length > 0) {
    userGeo.value.current = currentCities.value[0]
    }
}

const onTargetProvinceChange = () => {
    if (targetCities.value.length > 0) {
    userGeo.value.target = targetCities.value[0]
    }
}

// 2. 活跃简历分身
const activeResumeId = ref('resume-1')

// 控制是否展示全部（精选过滤）
const showAllJobs = ref(false)

// 3. 额外自主加分项
const extraBoosters = ref({
    cet4: true,
    jlpt: false
})

// 4. 权重动态滑块
const cityWeight = ref(15)
const xinchuangWeight = ref(30)
const salaryWeight = ref(10)

// 5. 模拟面试真题
const activeInterviewId = ref<string | null>(null)

// 滑块左高亮右灰色渐变函数
const getSliderStyle = (val: number, min: number, max: number, activeColor: string) => {
    const percentage = ((val - min) / (max - min)) * 100
    return {
    background: `linear-gradient(to right, ${activeColor} 0%, ${activeColor} ${percentage}%, #e5e5ea ${percentage}%, #e5e5ea 100%)`
    }
}

const resetFilters = () => {
    cityWeight.value = 15
    xinchuangWeight.value = 30
    salaryWeight.value = 10
    currentProvince.value = '安徽'
    targetProvince.value = '江苏'
    userGeo.value.current = '滁州'
    userGeo.value.target = '南京'
    extraBoosters.value.cet4 = true
    extraBoosters.value.jlpt = false
}

const toggleInterview = (jobId: string) => {
    activeInterviewId.value = activeInterviewId.value === jobId ? null : jobId
}

// 6. 岗位数据库基底
const rawJobs = ref([
    {
    id: 'job-1',
    avatar: '麒',
    title: 'Linux 内核适配工程专家',
    company: '麒麟软件有限公司 · 研发部',
    city: '南京',
    salary: '30k-50k',
    salaryLevel: 30,
    hasXinChuang: true,
    tags: ['C/C++', 'Kernel', '银河麒麟V10', '飞腾派驱动'],
    baseScoreMap: {
        'resume-1': 55, 
        'resume-2': 30, 
        'resume-3': 15,
        'resume-4': 25,
        'resume-5': 50
    },
    reasonProMap: {
        'resume-1': '您的“底层内核适配版”简历深度提及了“第十四届中国软件杯全国二等奖”，并在麒麟操作系统的内核及总线协议适配方向有高达95%的基因吻合率。',
        'resume-2': '您的开发经历中包含部分全栈C++编译及JNI底层对接经验，可勉强上手一部分中间件适配工作。',
        'resume-3': '没有检测到任何关于国产微处理器或 Linux 驱动开发的相关底座基础。',
        'resume-4': '具备外资与信创系统基础交互认知，能够支撑部分国际合规文档核验。',
        'resume-5': '具有极强的 Go/Rust 高性能适配功底，易于迁移到内核层面的虚拟化模块开发中。'
    },
    reasonContraMap: {
        'resume-1': '缺少国密安全合规模块二次开发经历，建议修读《安全操作系统规范标准》。',
        'resume-2': '您的核心技术专长在于 AI 算法，而此岗位极其需要高度聚焦的 C 底层汇编与硬件调试功底。',
        'resume-3': '完全缺乏国产软硬件底座常识，与麒麟软件信创技术规范契合度过低。',
        'resume-4': '系统底层代码库架构经验缺乏。',
        'resume-5': '对 ARM64 内核的驱动级开发实践案例较少，侧重于网络应用层。'
    },
    interviewQA: [
        { q: "在银河麒麟V10下，如何排查一个内核模块加载时报出的'Invalid module format'错误？", a: "应着重阐述 vermagic 的检查。通常是因为交叉编译链的 GCC 版本、或麒麟系统的内核头文件版本与当前编译环境不一致。可通过 modinfo 检查模块签名，并在麒麟系统本地重新拉取内核包进行对齐编译。" },
        { q: "在飞腾微处理器架构下进行内核态调试有哪些特殊考量？", a: "飞腾芯片基于ARM64架构，要强调在ARM64下的系统寄存器读取、页表映射区别、以及通过JTAG接口或国产适配版GDB进行远程调试，并阐明多核调度下的时序竞争排查。" }
    ]
    },
    {
    id: 'job-2',
    avatar: '华',
    title: '信创 AI 算法落地架构师',
    company: '华为技术有限公司 · 昇腾计算事业部',
    city: '北京',
    salary: '45k-70k',
    salaryLevel: 45,
    hasXinChuang: true,
    tags: ['Python', '昇腾NPU', 'DeepSeek-V3', 'RAG架构'],
    baseScoreMap: {
        'resume-1': 35,
        'resume-2': 58, 
        'resume-3': 20,
        'resume-4': 15,
        'resume-5': 42
    },
    reasonProMap: {
        'resume-1': '具有基础的微内核开发理论，能够进行华为边缘端微处理器指令级优化。',
        'resume-2': '您的“AI 算法版”简历完美呼应了在人工智能算法大赛项中的突破，以及“筑理·考工台”中基于 Qwen/DeepSeek 本地部署与 Web3D 结合的落地案例。',
        'resume-3': '没有检测到关于 LLM、NPU 或者大模型 API 的调用及微调经验。',
        'resume-4': '缺乏落地AI架构的系统知识。',
        'resume-5': '能够熟练编写高并发的大语言模型微服务推理网关，具备微服务容灾和高可用设计底蕴。'
    },
    reasonContraMap: {
        'resume-1': '严重缺乏神经网络训练、Prompt 链路及知识库向量数据库架构经验。',
        'resume-2': '该工作地点为北京，与您的期望办公城市较偏远，匹配得分已被动态平衡。',
        'resume-3': '技术栈与现代大语言模型落地应用场景完全错开。',
        'resume-4': '在技术栈与算法模型的衔接上出现显著真空断层。',
        'resume-5': '侧重于平台基建，在大模型具体微调、训练参数优化和指令对齐（SFT）方面深度略显不足。'
    },
    interviewQA: [
        { q: "如何利用昇腾MindSpore框架或Ollama对开源DeepSeek-R1进行蒸馏和轻量化本地部署？", a: "要结合NPU的TensorCore加速原理。详细阐述利用昇腾全栈软件开发工具链进行模型的INT8或FP16量化，并说明如何配置向量内存池提高RAG多段文本召回的推理效率。" },
        { q: "“筑理·考工台”中，Web3D模型渲染与大模型的交互是如何解决高延迟问题的？", a: "应从流式传输（SSE）、前端轻量化几何解算、以及在本地部署的大模型中利用KV Cache机制进行推理加速等技术层面，条理清晰地进行阐述。" }
    ]
    },
    {
    id: 'job-3',
    avatar: '达',
    title: '国产微服务系统全栈开发',
    company: '达梦数据库股份有限公司',
    city: '南京',
    salary: '25k-40k',
    salaryLevel: 25,
    hasXinChuang: true,
    tags: ['Java', '达梦数据库', 'Vue3', '微服务适配'],
    baseScoreMap: {
        'resume-1': 45,
        'resume-2': 52, 
        'resume-3': 25,
        'resume-4': 30,
        'resume-5': 38
    },
    reasonProMap: {
        'resume-1': '全栈技术中包含了 C++ 本地库调用，可直接参与达梦数据库底层连接池调优工作。',
        'resume-2': '项目集中包含 Java (Spring Boot) 与 Vue3 前后端协同开发经历，能快速接手国产系统数据迁移任务。',
        'resume-3': '普通的 Java 技术栈无信创产品（达梦等）的整合调试经验。',
        'resume-4': '可支撑信创数据库产品的涉外合规及系统权限整合审核工作。',
        'resume-5': '其高并发调度、分库分表及高可用双机热备架构理念可以完美融入数据库中间件调优。'
    },
    reasonContraMap: {
        'resume-1': '偏向底层，在 SpringBoot 微服务全栈流程中的前端 Vue.js 交互相对薄弱。',
        'resume-2': '该岗位需要高并发高安全的分布式事务调优经验。',
        'resume-3': '无国产关系型数据库的迁移迁移经验。',
        'resume-4': '无法独立支撑微服务框架的二次封装和前后端全套开发。',
        'resume-5': '侧重分布式集群，对具体的 Spring MVC 业务逻辑层和前端 Vue 页面的定制化效率稍低。'
    },
    interviewQA: [
        { q: "从MySQL向达梦数据库做微服务适配迁移时，通常有哪些语法兼容性巨坑？", a: "需要提到空字符串与NULL处理机制、特有方言解析器差异、ROWID和自增序列机制、以及达梦独有的安全审计机制。建议在SpringBoot配置多数据源并配置DM方言解析器进行过度。" },
        { q: "在全信创环境下，如何定位微服务响应超时的性能瓶颈？", a: "首先确认是否是CPU架构体系指令集导致的JVM编译劣化，再通过麒麟系统原生性能分析工具排查I/O锁，最后对达梦数据库慢SQL进行执行计划核查。" }
    ]
    }
])

// 7. 核心匹配分算法：实时联动
const processedJobs = computed(() => {
    return rawJobs.value.map(job => {
    let score = job.baseScoreMap[activeResumeId.value as 'resume-1' | 'resume-2' | 'resume-3' | 'resume-4' | 'resume-5'] || 30
    
    // 省市匹配度核心权重算法
    if (job.city === userGeo.value.current) {
        score += Number(cityWeight.value) 
    } else if (job.city === userGeo.value.target) {
        score += Number(cityWeight.value * 0.8) 
    }
    
    if (job.hasXinChuang) {
        score += Number(xinchuangWeight.value * 0.5)
    }

    if (job.salaryLevel >= 30) {
        score += Number(salaryWeight.value * 0.4)
    }

    if (extraBoosters.value.cet4) score += 3
    if (extraBoosters.value.jlpt) score += 5

    return {
        ...job,
        score: Math.min(Math.round(score), 99),
        reasonPro: job.reasonProMap[activeResumeId.value as 'resume-1' | 'resume-2' | 'resume-3' | 'resume-4' | 'resume-5'],
        reasonContra: job.reasonContraMap[activeResumeId.value as 'resume-1' | 'resume-2' | 'resume-3' | 'resume-4' | 'resume-5']
    }
    }).sort((a, b) => b.score - a.score) 
})

// 根据「查看全部」进行过滤
const filteredJobs = computed(() => {
    if (showAllJobs.value) {
    return processedJobs.value
    }
    return processedJobs.value.filter(job => job.score >= 60)
})

// 生成自荐信
const generateIntro = (item: any) => {
    let certDesc = extraBoosters.value.cet4 ? '，且已通过英语四级(CET-4)' : ''
    if (activeResumeId.value === 'resume-1') {
    return `您好！我是第14届中国软件杯全国二等奖得主。我对贵司发布的“${item.title}”岗非常感兴趣。我在银河麒麟V10及飞腾架构下有丰富的内核及驱动移植、C++适配经验${certDesc}，期待能为贵司信创研发贡献力量。`
    } else if (activeResumeId.value === 'resume-2') {
    return `您好！我对贵司的“${item.title}”岗极其感兴趣。我在 Vue3/SpringBoot 体系有全栈交付能力，主导过融合 AI 的传统建筑分析项目（筑理·考工台）${certDesc}，极为契合贵司国产 AI 生态的落地需求。`
    } else {
    return `您好！我非常关注国产信息安全和信创体系的生态构建，看到贵司的“${item.title}”有招聘计划。我拥有主流语言的技术开发经验，希望能有机会进一步沟通。`
    }
}

const copyIntro = (item: any) => {
    const text = generateIntro(item)
    navigator.clipboard.writeText(text).then(() => {
    alert('AI自荐信已成功复制到剪贴板，可直接用于投递！')
    })
}
</script>

<style scoped>
/* 纯净视感滚动条去除 */
.no-scrollbar::-webkit-scrollbar { display: none; }
.no-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }

/* 苹果风高精度微感滑块 */
.apple-slider {
    -webkit-appearance: none;
    appearance: none;
    width: 100%;
    height: 6px;
    border-radius: 9999px;
    background: #e5e5ea;
    outline: none;
    transition: background 0.1s ease;
}

.apple-slider::-webkit-slider-thumb {
    -webkit-appearance: none;
    appearance: none;
    width: 18px;
    height: 18px;
    border-radius: 50%;
    background: #ffffff;
    cursor: pointer;
    border: 1px solid rgba(0, 0, 0, 0.1);
    box-shadow: 0 3px 8px rgba(0,0,0,0.15);
    transition: transform 0.1s ease, background-color 0.1s ease;
}

.apple-slider::-webkit-slider-thumb:hover {
    transform: scale(1.15);
}

.apple-slider::-webkit-slider-thumb:active {
    transform: scale(0.95);
}
</style>