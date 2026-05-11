<template>
    <div class="w-full min-w-0 p-4 sm:p-6 h-[87vh] max-h-[87vh] flex flex-col overflow-hidden relative bg-gray-50/50 select-none">
    
    <header class="mb-5 px-2 flex justify-between items-center shrink-0">
        <div class="flex items-center gap-3">
        <button 
            v-if="viewMode === 'detail'" 
            @click="backToBoard"
            class="flex items-center justify-center w-10 h-10 rounded-full bg-white border border-gray-200 text-gray-700 hover:bg-gray-50 active:scale-95 transition-all shadow-sm cursor-pointer"
        >
            ⬅️
        </button>
        <div>
            <h2 class="text-3xl font-black text-black tracking-tighter flex items-center gap-3">
            {{ viewMode === 'board' ? '简历智库中心' : '简历双生映射' }}
            <span class="text-xs bg-blue-600 text-white px-2.5 py-0.5 rounded-full font-mono font-bold">v2.5 Enterprise</span>
            </h2>
            <p class="text-[10px] text-gray-400 font-bold uppercase tracking-wider mt-0.5">
            {{ viewMode === 'board' ? 'Resume Management Dashboard' : 'Resume Content & AI Digital Twin' }}
            </p>
        </div>
        </div>
    </header>

    <div class="flex-1 min-h-0">
        
        <div v-if="viewMode === 'board'" class="h-full flex flex-col justify-between overflow-hidden">
        
        <div class="flex-1 min-h-0 flex flex-col mb-4">
            <h4 class="text-xs font-black text-black uppercase tracking-wider mb-3 flex items-center gap-1.5 px-1 shrink-0">
            📂 已存档简历库 
            <span class="text-[9px] bg-gray-200 text-gray-600 px-1.5 py-0.5 rounded-md font-mono font-bold">{{ resumeList.length }}</span>
            </h4>
            
            <div class="flex-1 overflow-y-auto pr-1.5 custom-card-scrollbar">
            <div v-if="resumeList.length > 0" class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4 pb-4">
                <div 
                v-for="resume in resumeList" 
                :key="resume.id"
                class="group relative bg-white border border-gray-100 rounded-[24px] p-5 shadow-sm hover:shadow-md hover:border-gray-200 transition-all duration-300 flex flex-col justify-between min-h-[160px] overflow-hidden"
                >
                <div class="absolute top-0 right-0 w-20 h-20 bg-gradient-to-br from-gray-50 to-transparent rounded-full -mr-5 -mt-5 pointer-events-none"></div>

                <div v-if="resume.isDefault" class="absolute top-4 right-4 bg-amber-500 text-white text-[9px] font-black px-2 py-0.5 rounded-full uppercase tracking-wider shadow-sm z-10">
                    ★ DEFAULT
                </div>

                <div class="relative z-10 pr-12">
                    <div class="w-10 h-10 rounded-xl bg-gray-50 flex items-center justify-center text-xl mb-3 border border-gray-100">
                    📄
                    </div>
                    <h5 class="text-sm font-bold text-gray-800 line-clamp-1 group-hover:text-blue-600 transition-colors">
                    {{ resume.fileName || resume.name || '未命名简历' }}
                    </h5>
                    <p class="text-[9px] text-gray-400 font-bold uppercase tracking-wider mt-1">
                    解析完成 • {{ formatTime(resume.createTime) }}
                    </p>
                </div>

                <div class="absolute inset-0 bg-black/80 backdrop-blur-md opacity-0 group-hover:opacity-100 transition-all duration-300 flex flex-col justify-center items-center gap-2 p-4 z-20">
                    <button 
                    @click.stop="viewResumeDetail(resume)"
                    class="w-3/4 py-2 bg-blue-600 hover:bg-blue-500 text-white font-black text-[10px] rounded-xl uppercase tracking-wider transition-all active:scale-95 cursor-pointer"
                    >
                    🔍 查看并分析简历
                    </button>
                    <div class="w-3/4 flex gap-2">
                    <button 
                        @click.stop="handleSetDefault(resume.id)"
                        :disabled="resume.isDefault"
                        class="flex-1 py-2 bg-white/10 hover:bg-white/20 disabled:opacity-40 text-white font-bold text-[9px] rounded-xl transition-all active:scale-95 cursor-pointer border border-white/5"
                    >
                        ★ {{ resume.isDefault ? '已默认' : '设为默认' }}
                    </button>
                    <button 
                        @click.stop="handleSingleDelete(resume.id)"
                        class="flex-1 py-2 bg-red-600/25 hover:bg-red-600/80 text-red-200 font-bold text-[9px] rounded-xl transition-all active:scale-95 cursor-pointer border border-red-500/10"
                    >
                        🗑️ 删除
                    </button>
                    </div>
                </div>
                </div>
            </div>

            <div v-else class="h-full min-h-[220px] flex flex-col items-center justify-center text-center bg-gray-50 rounded-[28px] border border-gray-100 p-6">
                <span class="text-3xl mb-2">📥</span>
                <p class="text-xs font-bold text-gray-400 uppercase tracking-widest">No resumes uploaded yet</p>
            </div>
            </div>
        </div>

        <div 
            @click="triggerFileInput"
            @dragover.prevent="isDragging = true"
            @dragleave.prevent="isDragging = false"
            @drop.prevent="handleDrop"
            :class="[
            'border-2 border-dashed rounded-[32px] p-6 flex flex-col items-center justify-center gap-2 transition-all cursor-pointer shrink-0',
            isDragging ? 'border-blue-500 bg-blue-500/5 scale-[0.99]' : 'border-gray-200 bg-white hover:border-blue-400 hover:shadow-md'
            ]"
        >
            <input 
            type="file" 
            ref="fileInput" 
            class="hidden" 
            multiple 
            accept=".pdf,.doc,.docx,.txt" 
            @change="handleFileChange"
            />
            <div class="w-10 h-10 rounded-xl bg-blue-50 flex items-center justify-center text-xl shadow-inner text-blue-600">
            📤
            </div>
            <div class="text-center">
            <p class="text-xs font-black text-gray-800">拖拽简历文件到此处，或<span class="text-blue-600">点击上传</span></p>
            <p class="text-[9px] text-gray-400 mt-0.5 uppercase font-bold tracking-wider">Supports PDF, Word, TXT (Max 10MB)</p>
            </div>
        </div>

        </div>

        <div v-else class="h-full grid grid-cols-12 gap-5 items-stretch">
        
        <div class="col-span-12 xl:col-span-5 h-full min-h-0">
            <ResumeViewer 
            :resumeData="selectedResume" 
            :loading="isAnalyzing"
            />
        </div>

        <div class="col-span-12 xl:col-span-7 h-full min-h-0">
            <ResumeAnalysis 
            :analysisData="selectedResume" 
            :loading="isAnalyzing" 
            />
        </div>

        </div>

    </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import * as resumeApi from '@/api/resume'
import { toast } from '@/utils/toast'
import ResumeViewer from './resume/ResumeViewer.vue'
import ResumeAnalysis from './resume/ResumeAnalysis.vue'

// 视图与上传
const viewMode = ref<'board' | 'detail'>('board')
const isDragging = ref(false)
const fileInput = ref<HTMLInputElement | null>(null)

// 数据集
const resumeList = ref<any[]>([])
const selectedResume = ref<any>(null)
const isAnalyzing = ref(false)

// 载入列表数据
const loadResumes = async () => {
    try {
    const res = await resumeApi.getResumeList()
    resumeList.value = res.data || []
    } catch (e) {
    toast.error('获取列表失败')
    }
}

// 触发文件窗
const triggerFileInput = () => {
    fileInput.value?.click()
}

const handleFileChange = (e: Event) => {
    const target = e.target as HTMLInputElement
    if (target.files) {
    handleUpload(Array.from(target.files))
    }
}

const handleDrop = (e: DragEvent) => {
    isDragging.value = false
    if (e.dataTransfer?.files) {
    handleUpload(Array.from(e.dataTransfer.files))
    }
}

const handleUpload = async (files: File[]) => {
    try {
    toast.warning('正在分析同步至达梦数据库...')
    await Promise.all(files.map(file => resumeApi.uploadResume(file)))
    toast.success('🎉 简历已安全存入数据库中')
    await loadResumes()
    } catch (e) {
    toast.error('导入后端时遇到网络传输障碍')
    }
}

// 🎯 点击卡片，传入整个 resume 对象，不仅能保留原本的基本信息，还能发起最新的 AI 解析请求
const viewResumeDetail = async (resume: any) => {
    viewMode.value = 'detail'
    isAnalyzing.value = true
    
    // 1. 初始化 selectedResume，先装载列表里已有的基本属性（如文件名），防止渲染空白
    selectedResume.value = {
    fileName: resume.fileName || resume.name,
    resumeText: resume.resumeText || '' // 如果列表里本来就有解析好的，先展示
    }
    
    try {
    // 2. 发起 API 请求，获取 Python AI 引擎的最新分析与提纯结果
    const res = await resumeApi.parseResumeAi(resume.id)
    
    
    if (res && res.data) {
        const apiData = res.data.data

        // 3. 🎯 核心兼容逻辑：抹平 Python 接口返回的数据结构差异
        // 提取解析出的纯文本内容（优先从 apiData.resumeText 或 apiData.content、rawText 中获取）
        const extractedText = apiData.resumeText || apiData.content || apiData.rawText || resume.resumeText || ''
        
        selectedResume.value = {
        // 保留文件基本信息
        id: resume.id,
        fileName: resume.fileName || resume.name || apiData.fileName || apiData.name,
        
        // 左边 Viewer 消费的纯净源文件文本
        resumeText: extractedText,
        
        // 右边 Analysis 消费的 AI 画像字段（根据你的 Python 返回字段名进行精确对齐）
        name: apiData.name || '未提供',
        education: apiData.education || '未提供',
        expertise: Array.isArray(apiData.expertise) ? apiData.expertise : (apiData.skills || []),
        artifacts: Array.isArray(apiData.artifacts) ? apiData.artifacts : (apiData.projects || apiData.experience || [])
        }
    } else {
        toast.warning('接口未返回画像数据，将展示本地快照')
    }
    } catch (e) {
    console.error('AI 解析失败:', e)
    toast.error('AI 引擎无响应，已启用本地预存文本渲染')
    } finally {
    isAnalyzing.value = false
    }
}

// 退出详情视图
const backToBoard = () => {
    viewMode.value = 'board'
    selectedResume.value = null
}

// 设为置顶
const handleSetDefault = async (id: number) => {
    try {
    await resumeApi.setDefaultResume(id)
    toast.success('已成功将此简历设为默认/置顶')
    await loadResumes()
    } catch (e) {
    toast.error('设置失败')
    }
}

// 删除简历
const handleSingleDelete = async (id: number) => {
    const isConfirmed = await toast.confirm(
    '🗑️ 安全警示',
    '确定要永久删除这份附件简历吗？删除后对应的服务器物理文件和达梦数据将会被立刻清理。'
    )
    if (isConfirmed) {
    try {
        await resumeApi.deleteResume(id)
        toast.success('简历删除成功')
        await loadResumes()
    } catch (e) {
        toast.error('删除操作失败')
    }
    }
}

const formatTime = (timeStr: string) => {
    if (!timeStr) return '2026/05/11'
    const date = new Date(timeStr)
    return `${date.getFullYear()}/${String(date.getMonth() + 1).padStart(2, '0')}/${String(date.getDate()).padStart(2, '0')}`
}

onMounted(() => {
    loadResumes()
})
</script>

<style scoped>
.custom-card-scrollbar::-webkit-scrollbar {
    width: 6px;
}
.custom-card-scrollbar::-webkit-scrollbar-track {
    background: transparent;
}
.custom-card-scrollbar::-webkit-scrollbar-thumb {
    background: rgba(0, 0, 0, 0.08);
    border-radius: 99px;
}
.custom-card-scrollbar::-webkit-scrollbar-thumb:hover {
    background: rgba(0, 0, 0, 0.16);
}
</style>