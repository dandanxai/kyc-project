<template>
<div class="w-full min-w-0 p-4 sm:p-5 h-[87vh] max-h-[87vh] flex flex-col overflow-hidden relative bg-gray-50/50">
    <header class="mb-4 px-2 flex justify-between items-center shrink-0">
    <div>
        <h2 class="text-3xl font-black text-black tracking-tighter flex items-center gap-3">
        智能画像与双生建模
        <span class="text-xs bg-blue-600 text-white px-2.5 py-0.5 rounded-full font-mono">v2.5 Enterprise</span>
        </h2>
    </div>
    </header>

    <div class="grid grid-cols-12 gap-5 flex-1 min-h-0 items-stretch mb-2">
    <div class="col-span-12 xl:col-span-5 h-full min-h-0">
        <ResumeList 
        v-model="selectedDocId"
        :list="resumeList"
        @upload="handleUpload"
        @single-delete="handleSingleDelete" 
        @batch-delete="handleBatchDelete"
        @set-default="handleSetDefault"
        />
    </div>

    <div class="col-span-12 xl:col-span-7 h-full min-h-0">
        <ResumeAnalysis :analysisData="currentAnalysis" :loading="selectedDocId ? isAnalyzing : false" />
    </div>
    </div>
</div>
</template>

<script setup lang="ts">
import { ref, watch, onMounted } from 'vue'
import ResumeList from './resume/ResumeList.vue'
import ResumeAnalysis from './resume/ResumeAnalysis.vue'
import * as resumeApi from '@/api/resume'
import { toast } from '@/utils/toast'

// --- 响应式状态 ---
const selectedDocId = ref<number | null>(null) // 🎯 默认设为 null，不进行默认选中
const resumeList = ref<any[]>([])
const currentAnalysis = ref<any>(null)
const isAnalyzing = ref<boolean>(false) // 🎯 核心控制：右侧解析 Loading 动画开关

// --- 🎯 核心监听：左边切换，右边立刻触发 AI 解析与动画 ---
watch(selectedDocId, async (newVal) => {
// 如果没有 newVal（比如刚进页面或者全部删除了），清空画像，不发请求
if (!newVal || typeof newVal !== 'number') {
    currentAnalysis.value = null
    isAnalyzing.value = false
    return
}

// 开启极致科技感加载动画
isAnalyzing.value = true
currentAnalysis.value = null

// 为了让动画有一个丝滑的过渡时间（至少展示 800ms），避免闪烁
const minDelay = new Promise(resolve => setTimeout(resolve, 800))

try {
    // 真实数据：通过 Java 接口去代理请求 Python FastAPI 大模型
    const [res] = await Promise.all([
    resumeApi.parseResumeAi(newVal),
    minDelay
    ])

    if (res.code === 200) {
    // 🎯 规范对接：res.data.data 获取真实数据
    currentAnalysis.value = res.data.data
    toast.success('AI 画像构建成功')
    } else {
    toast.error(res.message || 'AI 深度画像提取遇到故障')
    }
} catch (e: any) {
    console.error(e)
    toast.error('AI 解析失败，请确认后端或 python 服务已正常启动')
} finally {
    isAnalyzing.value = false // 关闭加载动画
}
}) // 🎯 移除 immediate: true，首次加载页面不触发，只有在点击列表项时才触发

// --- 基础生命周期与业务方法 ---
const loadResumes = async () => {
try {
    const res = await resumeApi.getResumeList()
    resumeList.value = res.data || []
    
    // 🎯 刚进页面或重载列表时，不做任何默认聚焦。
    // 仅当之前选中的简历在重新加载后被删除了，才重置选中状态。
    if (selectedDocId.value) {
    const isExist = resumeList.value.some(item => item.id === selectedDocId.value)
    if (!isExist) {
        selectedDocId.value = null
        currentAnalysis.value = null
    }
    }
} catch (e) {
    toast.error('获取列表失败')
}
}

const handleUpload = async (files: File | File[]) => {
try {
    toast.warning('正在分析同步到服务器和达梦数据库中...')
    
    if (Array.isArray(files)) {
    await Promise.all(files.map(file => resumeApi.uploadResume(file)))
    } else {
    await resumeApi.uploadResume(files)
    }

    toast.success('🎉 所有选定简历已被安全存入数据库中')
    await loadResumes()
} catch (e) {
    toast.error('导入后端时遇到网络传输障碍')
}
}

const handleSingleDelete = async (id: number) => {
try {
    await resumeApi.deleteResume(id)
    toast.success('简历删除成功')
    
    if (selectedDocId.value === id) {
    selectedDocId.value = null
    currentAnalysis.value = null
    }
    await loadResumes()
} catch (e) {
    toast.error('删除操作失败')
}
}

const handleBatchDelete = async (ids: number[]) => {
try {
    // 🎯 修复函数名：调对 resume.ts 中的 deleteResumes 方法
    await resumeApi.deleteResumes(ids)
    toast.success('所选简历全部成功擦除')
    
    if (ids.includes(selectedDocId.value as number)) {
    selectedDocId.value = null
    currentAnalysis.value = null
    }
    await loadResumes()
} catch (e) {
    toast.error('批量删除时发生中断')
}
}

const handleSetDefault = async (id: number) => {
try {
    await resumeApi.setDefaultResume(id)
    toast.success('默认投递简历切换成功')
    await loadResumes()
} catch (e) {
    toast.error('激活状态设置失败')
}
}

onMounted(() => {
loadResumes()
})
</script>