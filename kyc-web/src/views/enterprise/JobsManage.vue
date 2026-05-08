<template>
    <div class="h-full w-full bg-[#F5F5F7] p-8 overflow-hidden flex flex-col font-sans select-none animate-fade-in">
    
    <header class="flex-shrink-0 flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 mb-8">
        <div>
        <h1 class="text-3xl font-black tracking-tight text-black flex items-center gap-2">
            信创岗位管理大盘 <span class="text-xl">🎯</span>
        </h1>
        <p class="text-xs font-bold text-gray-400 uppercase tracking-widest mt-1">
            Kirin Enterprise Job Publishing & AI Profiling Console
        </p>
        </div>
        
        <button 
        @click="openPublishPanel"
        class="px-5 py-3.5 bg-black hover:bg-neutral-800 text-white text-xs font-black rounded-2xl shadow-lg shadow-black/10 transition-all active:scale-95 flex items-center gap-2"
        >
        <span>+</span> 发布新信创岗位
        </button>
    </header>

    <div class="flex-1 flex gap-8 overflow-hidden min-h-0">
        
        <section class="flex-1 overflow-y-auto no-scrollbar space-y-6 pr-1">
        <div class="grid grid-cols-1 xl:grid-cols-2 gap-6">
            <div 
            v-for="job in jobs" 
            :key="job.id"
            :class="[
                selectedJobId === job.id 
                ? 'border-black ring-2 ring-black/5 bg-white shadow-lg' 
                : 'border-transparent bg-white hover:border-black/5 hover:shadow-md'
            ]"
            class="p-6 rounded-[28px] border transition-all duration-300 flex flex-col justify-between group relative overflow-hidden"
            >
            <div v-if="selectedJobId === job.id" class="absolute top-0 right-0 w-24 h-24 bg-indigo-50/40 rounded-full blur-2xl pointer-events-none"></div>

            <div class="space-y-4">
                <div class="flex justify-between items-start">
                <span class="text-[9px] font-black text-indigo-600 bg-indigo-50 px-2.5 py-1 rounded-xl uppercase tracking-wider">
                    {{ job.department }}
                </span>
                <span class="flex items-center gap-1.5">
                    <span class="w-2 h-2 rounded-full" :class="job.active ? 'bg-emerald-500 animate-pulse' : 'bg-gray-300'"></span>
                    <span class="text-[10px] font-black text-gray-400 uppercase">{{ job.active ? '招募中' : '已暂停' }}</span>
                </span>
                </div>

                <div>
                <h3 class="text-base font-black text-black group-hover:text-neutral-800 transition-colors">{{ job.title }}</h3>
                <p class="text-[10px] text-gray-400 font-bold uppercase mt-1">
                    薪酬范围: {{ job.salary }} <span class="mx-1">|</span> {{ job.experience }} <span class="mx-1">|</span> {{ job.education }}
                </p>
                </div>

                <div class="p-4 bg-[#F5F5F7] rounded-2xl space-y-3">
                <div class="flex justify-between items-center text-[10px] font-black text-gray-400 uppercase tracking-wider">
                    <span>AI 简历初筛漏斗</span>
                    <span class="text-black">初筛通过率 {{ job.passRate }}%</span>
                </div>
                <div class="grid grid-cols-3 gap-1 h-2 rounded-full overflow-hidden">
                    <div class="bg-black" :style="{ width: '100%' }" title="总投递"></div>
                    <div class="bg-indigo-600" :style="{ width: job.passRate + '%' }" title="AI通过"></div>
                    <div class="bg-emerald-500" :style="{ width: (job.passRate * 0.6) + '%' }" title="约面率"></div>
                </div>
                <div class="flex justify-between text-[9px] font-bold text-gray-400">
                    <span>投递: <b>{{ job.appCount }}</b> 份</span>
                    <span>AI通过: <b>{{ Math.round(job.appCount * (job.passRate/100)) }}</b> 份</span>
                    <span>待面试: <b>{{ job.interviewCount }}</b> 人</span>
                </div>
                </div>

                <div class="flex flex-wrap gap-1.5 pt-2">
                <span 
                    v-for="(val, skill) in job.skillsMap" 
                    :key="skill"
                    class="text-[9px] font-bold bg-neutral-100 text-neutral-600 px-2 py-0.5 rounded-md"
                >
                    {{ skill }} {{ val }}%
                </span>
                </div>
            </div>

            <div class="flex items-center justify-between pt-5 mt-5 border-t border-neutral-50 flex-shrink-0">
                <span class="text-xs font-black text-black tracking-tight">{{ job.date }} 发布</span>
                
                <div class="flex items-center gap-2">
                <button 
                    @click="editJob(job)"
                    class="px-3.5 py-2 bg-neutral-100 hover:bg-neutral-200 text-neutral-800 text-xs font-black rounded-xl transition-all active:scale-95"
                >
                    修改配置 ⚙️
                </button>
                <button 
                    @click="toggleJobStatus(job)"
                    :class="[job.active ? 'bg-rose-50 text-rose-600 hover:bg-rose-100' : 'bg-emerald-50 text-emerald-600 hover:bg-emerald-100']"
                    class="px-3.5 py-2 text-xs font-black rounded-xl transition-all active:scale-95"
                >
                    {{ job.active ? '下架' : '上架' }}
                </button>
                </div>
            </div>

            </div>
        </div>
        </section>

        <aside 
        v-if="isPanelOpen"
        class="w-[500px] bg-white rounded-[32px] border border-gray-100 p-8 shadow-xl flex flex-col justify-between h-full animate-slide-left flex-shrink-0"
        >
        <div class="space-y-6 overflow-y-auto no-scrollbar flex-1 pb-4 pr-1">
            <div class="flex justify-between items-center pb-4 border-b border-neutral-100">
            <div>
                <h2 class="text-lg font-black text-black">
                {{ isEditMode ? '修改岗位配置' : '发布新信创岗位' }}
                </h2>
                <p class="text-[9px] font-black text-gray-400 uppercase tracking-widest mt-0.5">Define Job & Set AI Target</p>
            </div>
            <button @click="closePublishPanel" class="w-8 h-8 bg-[#F5F5F7] hover:bg-neutral-200 text-black rounded-full flex items-center justify-center text-xs transition-colors">✕</button>
            </div>

            <div v-if="!isEditMode" class="space-y-2">
            <label class="text-[10px] font-black text-gray-400 uppercase tracking-widest flex items-center gap-1.5">
                <span>📂</span> 智能文档解析 (支持草稿、PDF、DOC 拖入)
            </label>
            
            <div 
                @dragover.prevent="isDragging = true"
                @dragleave.prevent="isDragging = false"
                @drop.prevent="handleFileDrop"
                @click="triggerFileInput"
                :class="[
                isDragging ? 'border-indigo-500 bg-indigo-50/20' : 'border-neutral-200 bg-[#F5F5F7] hover:border-black/15',
                parseStatus !== 'idle' ? 'pointer-events-none' : 'cursor-pointer'
                ]"
                class="border-2 border-dashed rounded-2xl p-6 transition-all text-center relative group overflow-hidden"
            >
                <input 
                ref="fileInput" 
                type="file" 
                class="hidden" 
                accept=".txt,.doc,.docx,.pdf"
                @change="handleFileSelect"
                />
                
                <div v-if="parseStatus === 'idle'" class="space-y-2">
                <span class="text-2xl group-hover:scale-110 transition-transform inline-block">🔮</span>
                <p class="text-xs font-black text-black">将岗位要求草稿/老版说明书拖到这里</p>
                <p class="text-[10px] text-gray-400 font-bold">支持 PDF, DOCX, TXT · AI 一秒解构并提取图谱</p>
                </div>

                <div v-else-if="parseStatus === 'parsing'" class="space-y-3 py-2">
                <div class="relative w-10 h-10 mx-auto">
                    <div class="absolute inset-0 rounded-full border-4 border-indigo-100"></div>
                    <div class="absolute inset-0 rounded-full border-4 border-indigo-600 border-t-transparent animate-spin"></div>
                </div>
                <div class="space-y-1">
                    <p class="text-xs font-black text-indigo-600 animate-pulse">Kirin AI 正在深度建模并提取能力图谱...</p>
                    <p class="text-[9px] text-gray-400 font-bold uppercase tracking-wider">正在解构核心技术栈与面试大纲</p>
                </div>
                </div>

                <div v-else-if="parseStatus === 'success'" class="space-y-1 py-1">
                <span class="text-2xl text-emerald-500">✨</span>
                <p class="text-xs font-black text-emerald-600">文档解构完成！核心能力图谱已成功建立</p>
                <p class="text-[9px] text-gray-400 font-bold">已提取: {{ parsedFileName }}</p>
                </div>
            </div>
            </div>

            <div class="p-5 bg-black text-white rounded-3xl space-y-4 shadow-xl">
            <div class="flex justify-between items-center">
                <h4 class="text-xs font-black tracking-wider text-neutral-300 uppercase flex items-center gap-2">
                <span class="text-indigo-400">⚡</span> AI 职位能力图谱权重 (Kirin Profiling)
                </h4>
                <span class="text-[8px] bg-white/10 text-white font-bold px-2 py-0.5 rounded">精准画像基准</span>
            </div>
            
            <div class="space-y-3.5">
                <div v-for="(val, skill) in form.skillsMap" :key="skill" class="space-y-1.5">
                <div class="flex justify-between items-center text-[10px] font-black">
                    <span class="text-neutral-200">{{ skill }}</span>
                    <span class="text-indigo-300">{{ val }}%</span>
                </div>
                <div class="h-1.5 w-full bg-white/10 rounded-full overflow-hidden">
                    <div class="h-full bg-gradient-to-r from-indigo-500 to-cyan-400 rounded-full transition-all duration-1000" :style="{ width: val + '%' }"></div>
                </div>
                </div>
                <p class="text-[9px] text-neutral-400 leading-relaxed font-medium">
                * 本职位能力图谱作为**AI简历筛选**和**求职者精准推荐**的底层相似度向量检索依据。
                </p>
            </div>
            </div>

            <div class="space-y-4">
            <div class="space-y-1">
                <label class="text-[10px] font-black text-gray-400 uppercase tracking-widest">岗位名称</label>
                <input 
                v-model="form.title" 
                type="text" 
                placeholder="例如：银河麒麟内核级研发工程师"
                @input="handleTitleInput"
                class="w-full px-4 py-3 bg-[#F5F5F7] border border-transparent rounded-2xl text-xs font-bold focus:outline-none focus:border-black focus:bg-white transition-all"
                />
            </div>

            <div class="grid grid-cols-2 gap-4">
                <div class="space-y-1">
                <label class="text-[10px] font-black text-gray-400 uppercase tracking-widest">目标部门</label>
                <input 
                    v-model="form.department" 
                    type="text" 
                    placeholder="如：计算产品研发部"
                    class="w-full px-4 py-3 bg-[#F5F5F7] border border-transparent rounded-2xl text-xs font-bold focus:outline-none focus:border-black focus:bg-white transition-all"
                />
                </div>
                <div class="space-y-1">
                <label class="text-[10px] font-black text-gray-400 uppercase tracking-widest">薪资范畴</label>
                <input 
                    v-model="form.salary" 
                    type="text" 
                    placeholder="如：25K-40K"
                    class="w-full px-4 py-3 bg-[#F5F5F7] border border-transparent rounded-2xl text-xs font-bold focus:outline-none focus:border-black focus:bg-white transition-all"
                />
                </div>
            </div>

            <div class="grid grid-cols-2 gap-4">
                <div class="space-y-1">
                <label class="text-[10px] font-black text-gray-400 uppercase tracking-widest">经验年限</label>
                <select v-model="form.experience" class="w-full px-3 py-3 bg-[#F5F5F7] rounded-2xl text-xs font-bold focus:outline-none cursor-pointer">
                    <option value="1-3年">1-3年 潜力股</option>
                    <option value="3-5年">3-5年 骨干开发者</option>
                    <option value="5年以上">5年以上 资深专家</option>
                </select>
                </div>
                <div class="space-y-1">
                <label class="text-[10px] font-black text-gray-400 uppercase tracking-widest">学历要求</label>
                <select v-model="form.education" class="w-full px-3 py-3 bg-[#F5F5F7] rounded-2xl text-xs font-bold focus:outline-none cursor-pointer">
                    <option value="本科及以上">本科及以上</option>
                    <option value="统招专科/高职">统招专科/高职</option>
                    <option value="不限学历">不限学历</option>
                </select>
                </div>
            </div>
            </div>

            <div class="space-y-2">
            <div class="flex justify-between items-center">
                <label class="text-[10px] font-black text-gray-400 uppercase tracking-widest">岗位要求与职责 (Markdown)</label>
                <button 
                v-if="!form.jd"
                @click="generateJD" 
                type="button"
                class="text-[10px] text-indigo-600 hover:underline font-black transition-all"
                >
                手写草稿？让 AI 帮我规范排版 ✨
                </button>
            </div>
            <textarea 
                v-model="form.jd" 
                rows="6" 
                placeholder="上传上方文件或在此处编辑具体的岗位职责、专业技能、加分项..."
                class="w-full px-4 py-3 bg-[#F5F5F7] border border-transparent rounded-2xl text-[11px] font-medium leading-relaxed focus:outline-none focus:border-black focus:bg-white transition-all no-scrollbar"
            ></textarea>
            </div>

            <div class="p-5 bg-emerald-50/40 rounded-2xl border border-emerald-100/60 space-y-3">
            <div class="flex justify-between items-center">
                <label class="text-xs font-black text-emerald-900 flex items-center gap-1.5">
                <span>🤖</span> AI 模拟面试专项考题配置
                </label>
                <span class="text-[8px] bg-emerald-100 text-emerald-800 font-bold px-2 py-0.5 rounded uppercase">双端热激活</span>
            </div>
            <p class="text-[10px] text-emerald-800 leading-normal">
                此处填写的考核重点将**即时同步并作为个人端 AI 面试对练系统的核心知识考查题库**。
            </p>
            <textarea 
                v-model="form.interviewFocus" 
                rows="3" 
                placeholder="请输入考核重点（解析上传的文件时，AI 会自动从职责中提炼出最适配信创生态的考查题目）"
                class="w-full px-4 py-3 bg-white border border-transparent rounded-xl text-[11px] font-medium leading-relaxed focus:outline-none focus:border-emerald-500 transition-all no-scrollbar"
            ></textarea>
            </div>
        </div>

        <div class="pt-6 border-t border-neutral-100 flex gap-4 bg-white">
            <button 
            @click="submitJob"
            class="flex-1 py-3.5 bg-black hover:bg-neutral-800 text-white text-xs font-black rounded-2xl shadow-lg transition-all active:scale-[0.98]"
            >
            {{ isEditMode ? '保存修改配置' : '正式全网发布招募' }}
            </button>
            <button 
            @click="closePublishPanel"
            class="px-6 py-3.5 bg-neutral-100 hover:bg-neutral-200 text-neutral-800 text-xs font-black rounded-2xl transition-all active:scale-[0.98]"
            >
            取消
            </button>
        </div>
        </aside>

        <aside 
        v-else 
        class="hidden lg:flex w-[500px] bg-white rounded-[32px] border border-gray-100 border-dashed items-center justify-center text-gray-300 flex-col gap-2 flex-shrink-0"
        >
        <span class="text-5xl">🎯</span>
        <p class="text-[10px] font-black uppercase tracking-widest mt-2">点击“修改配置”或“发布新岗位”激活看板</p>
        </aside>

    </div>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

// 模拟已发布的信创岗位数据（自带AI能力图谱）
const jobs = ref([
    {
    id: 1,
    title: 'Linux 内核适配与基础软件开发',
    department: '麒麟软件 (南京研发中心)',
    salary: '25K-40K',
    experience: '3-5年',
    education: '本科及以上',
    active: true,
    passRate: 78,
    appCount: 84,
    interviewCount: 12,
    date: '2026-04-12',
    jd: '## 🎯 岗位职责：\n1. 负责主流Linux内核在国产飞腾芯片架构下的适配移植；\n2. 调优内核调度系统，实现极高吞吐性能。\n\n## 🛠️ 核心要求：\n- 精通C语言，熟悉多核中断隔离。',
    interviewFocus: '重点考查国产芯片特有寄存器控制、Linux内核中断管理与编译链重组。',
    skillsMap: {
        '内核及基础软件适配': 95,
        '飞腾/申威芯片级调试': 90,
        'C/C++底层架构开发': 85,
        '指令集兼容性转换': 70
    }
    },
    {
    id: 2,
    title: '达梦数据库存储引擎重构专家',
    department: '达梦数据库 (武汉总部)',
    salary: '35K-55K',
    experience: '5年以上',
    education: '本科及以上',
    active: true,
    passRate: 52,
    appCount: 41,
    interviewCount: 4,
    date: '2026-04-28',
    jd: '## 🎯 岗位职责：\n1. 负责达梦内核级事务锁与行锁性能极致重构；\n2. 构建多节点、高可用的国产灾备闭环。\n\n## 🛠️ 核心要求：\n- 5年以上数据库内核存储引擎实战。',
    interviewFocus: '重点考查ACID锁机制、共享存储技术和大并发分布式一致性算法。',
    skillsMap: {
        '数据库内核开发': 98,
        '事务控制与锁机制': 90,
        '高并发高可用架构': 80,
        '国密标准数据安全': 75
    }
    }
])

// 拖拽与解析状态控制
const isDragging = ref(false)
const parseStatus = ref<'idle' | 'parsing' | 'success'>('idle')
const parsedFileName = ref('')
const fileInput = ref<HTMLInputElement | null>(null)

// 面板管理状态
const isPanelOpen = ref(false)
const isEditMode = ref(false)
const selectedJobId = ref<number | null>(null)

// 默认空图谱
const defaultSkillsMap = {
    'Linux/国产系统底座': 60,
    '全栈跨平台迁移能力': 50,
    '高并发核心性能调优': 40,
    '国密/自主可信安全': 30
}

// 岗位表单初始模板
const defaultForm = {
    id: 0,
    title: '',
    department: '',
    salary: '',
    experience: '3-5年',
    education: '本科及以上',
    jd: '',
    interviewFocus: '',
    skillsMap: { ...defaultSkillsMap }
}
const form = ref({ ...defaultForm })

// 输入框实时关联基础图谱权重（无文档上传时，手打标题时触发图谱智能衍生）
const handleTitleInput = () => {
    if (isEditMode.value) return
    const title = form.value.title
    if (title.includes('Linux') || title.includes('内核') || title.includes('适配')) {
    form.value.skillsMap = {
        'Linux/国产系统底座': 90,
        '核心底层语言调试': 85,
        '高并发核心性能调优': 75,
        '国密/自主可信安全': 60
    }
    } else if (title.includes('前端') || title.includes('Vue') || title.includes('UOS')) {
    form.value.skillsMap = {
        'WebKit系统渲染优化': 95,
        'Vue3/前端生态构建': 90,
        '全栈跨平台迁移能力': 80,
        '国密/自主可信安全': 40
    }
    }
}

// 触发隐藏的文件上传
const triggerFileInput = () => {
    if (parseStatus.value === 'idle') {
    fileInput.value?.click()
    }
}

// 处理点击选择文件
const handleFileSelect = (event: Event) => {
    const files = (event.target as HTMLInputElement).files
    if (files && files.length > 0) {
    parseUploadedFile(files[0])
    }
}

// 处理拖拽拖入
const handleFileDrop = (event: DragEvent) => {
    isDragging.value = false
    const files = event.dataTransfer?.files
    if (files && files.length > 0) {
    parseUploadedFile(files[0])
    }
}

// 模拟 AI 文档深度解析与自动字段提取（同时自动构建职位能力图谱）
const parseUploadedFile = (file: File) => {
    parsedFileName.value = file.name
    parseStatus.value = 'parsing'

    // 模拟 2 秒钟的 AI 深度结构化阅读分析与图谱向量抽取
    setTimeout(() => {
    parseStatus.value = 'success'
    
    // 模拟针对不同关键词文档提取出高精度的信创岗位和能力图谱
    if (file.name.toLowerCase().includes('uos') || file.name.includes('统信') || file.name.includes('前端')) {
        form.value.title = 'UOS 桌面系统适配研发（Vue3/C++）'
        form.value.department = '统信软件适配中心'
        form.value.salary = '20K-32K'
        form.value.experience = '3-5年'
        form.value.education = '本科及以上'
        form.value.jd = `## 🎯 岗位职责：\n1. 负责前端及客户端程序在统信 UOS 操作系统中的深度兼容与性能调优；\n2. 构建基于 Vue3 / UniApp 架构的全国产政务系统迁移闭环；\n3. 调优 WebKit 渲染内核，解决信创多终端显示兼容痛点。\n\n## 🛠️ 岗位要求：\n- 精通 Vue3 全家桶，具备良好的底层渲染生命周期把控能力。`
        form.value.interviewFocus = '考查 WebKit 底层渲染优化机制、Vue3 在国产安全浏览器下的高可用适配方案。'
        form.value.skillsMap = {
        'WebKit系统渲染优化': 95,
        'Vue3/前端生态构建': 92,
        '全栈跨平台迁移能力': 85,
        '国密/自主可信安全': 55
        }
    } else {
        // 默认解析为高精尖后端迁移架构岗位
        form.value.title = 'Spring Cloud 信创平台重构专家'
        form.value.department = '国资云研发中心'
        form.value.salary = '28K-45K'
        form.value.experience = '5年以上'
        form.value.education = '本科及以上'
        form.value.jd = `## 🎯 岗位职责：\n1. 领导微服务向“鲲鹏芯片 + 银河麒麟V10 + 金蝶中间件 + 达梦数据库”全栈信创底座的安全迁移；\n2. 设计高并发数据同步管道，移除对 Oracle 等欧美软硬件的隐式依赖。\n\n## 🛠️ 岗位要求：\n- 精通 Spring Boot / Spring Cloud 分布式架构。`
        form.value.interviewFocus = '核心考查：在不使用复杂安全中间件的前提下，设计更轻量的、防止数据溢出的自定义拦截认证逻辑。'
        form.value.skillsMap = {
        '微服务信创底座重构': 96,
        '国资云平台无缝安全迁移': 90,
        '高并发核心性能调优': 85,
        '国密/自主可信安全': 80
        }
    }
    }, 2000)
}

// 开启发布面板
const openPublishPanel = () => {
    isEditMode.value = false
    selectedJobId.value = null
    parseStatus.value = 'idle'
    parsedFileName.value = ''
    form.value = { 
    ...defaultForm,
    skillsMap: { ...defaultSkillsMap }
    }
    isPanelOpen.value = true
}

// 开启编辑配置面板
const editJob = (job: any) => {
    isEditMode.value = true
    selectedJobId.value = job.id
    form.value = {
    id: job.id,
    title: job.title,
    department: job.department,
    salary: job.salary,
    experience: job.experience,
    education: job.education,
    jd: job.jd,
    interviewFocus: job.interviewFocus,
    skillsMap: { ...job.skillsMap }
    }
    isPanelOpen.value = true
}

// 关闭面板
const closePublishPanel = () => {
    isPanelOpen.value = false
    selectedJobId.value = null
}

// 模拟 AI 一键规范化
const generateJD = () => {
    if (!form.value.title) {
    alert('请先输入岗位名称，以便 AI 精准感知生成！')
    return
    }
    form.value.jd = `## 🎯 岗位职责：\n1. 负责 [${form.value.title}] 底座系统的适配与优化工作；\n2. 协同技术团队，快速在国产化 CPU 与 OS 环境中进行业务全栈迁移与重构。\n\n## 🛠️ 核心要求：\n- 熟悉国产处理器架构与主流信创操作系统底层工作机制。`
}

// 岗位上/下架开关
const toggleJobStatus = (job: any) => {
    job.active = !job.active
}

// 保存岗位并执行【双端同步】
const submitJob = () => {
    if (!form.value.title || !form.value.department || !form.value.salary) {
    alert('请补充完整岗位基本信息！')
    return
    }

    const currentFocus = form.value.interviewFocus || '未设置特定技能重点，偏重通用信创迁移考查。'

    if (isEditMode.value) {
    // 编辑现有岗位
    const target = jobs.value.find(j => j.id === form.value.id)
    if (target) {
        target.title = form.value.title
        target.department = form.value.department
        target.salary = form.value.salary
        target.experience = form.value.experience
        target.education = form.value.education
        target.jd = form.value.jd
        target.interviewFocus = currentFocus
        target.skillsMap = { ...form.value.skillsMap }
    }
    
    // 同步到本地运行时
    updateLocalInterviewCache(form.value.title, currentFocus, form.value.skillsMap)
    alert('修改成功！岗位配置与能力图谱已热更，已同步至匹配底层系统！')
    } else {
    // 新增岗位
    const newJob = {
        id: Date.now(),
        title: form.value.title,
        department: form.value.department,
        salary: form.value.salary,
        experience: form.value.experience,
        education: form.value.education,
        active: true,
        passRate: 100,
        appCount: 0,
        interviewCount: 0,
        date: new Date().toISOString().split('T')[0],
        jd: form.value.jd,
        interviewFocus: currentFocus,
        skillsMap: { ...form.value.skillsMap }
    }
    jobs.value.unshift(newJob)
    
    // 同步到本地运行时
    updateLocalInterviewCache(form.value.title, currentFocus, form.value.skillsMap)
    alert(`【信创岗位发布成功】！已成功推送 [${newJob.title}] 并建立能力图谱模型。`)
    }
    closePublishPanel()
}

// 轻量级本地同步机制
const updateLocalInterviewCache = (jobTitle: string, focusText: string, skillsMap: any) => {
    try {
    const cacheKey = 'KYC_ACTIVE_INTERVIEW_MAP'
    const currentMap = JSON.parse(localStorage.getItem(cacheKey) || '{}')
    currentMap[jobTitle] = {
        title: jobTitle,
        focus: focusText,
        skillsMap: skillsMap,
        updatedAt: new Date().getTime()
    }
    localStorage.setItem(cacheKey, JSON.stringify(currentMap))
    } catch (e) {
    console.error('双端极简认证同步逻辑异常:', e)
    }
}
</script>

<style scoped>
.no-scrollbar::-webkit-scrollbar { display: none; }
.no-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }

.animate-fade-in {
    animation: fadeIn 0.4s cubic-bezier(0.16, 1, 0.3, 1) forwards;
}

.animate-slide-left {
    animation: slideLeft 0.35s cubic-bezier(0.34, 1.56, 0.64, 1) forwards;
}

@keyframes fadeIn {
    from { opacity: 0; transform: translateY(8px); }
    to { opacity: 1; transform: translateY(0); }
}

@keyframes slideLeft {
    from { opacity: 0; transform: translateX(20px) scale(0.98); }
    to { opacity: 1; transform: translateX(0) scale(1); }
}
</style>