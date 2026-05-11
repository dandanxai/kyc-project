<template>
    <div class="h-full min-h-0 bg-white rounded-[28px] p-6 border border-gray-100 shadow-sm flex flex-col overflow-hidden relative">
        <div class="flex justify-between items-center border-b border-gray-100 pb-4 mb-4 shrink-0">
            <div>
                <span class="text-[9px] text-blue-500 font-black uppercase tracking-[0.2em] block">Original Document Twin</span>
                <h4 class="text-sm font-black text-gray-800 tracking-tight line-clamp-1">
                    📄 {{ loading ? 'AI 正在同步原件视轨...' : (resumeData?.fileName || '物理原件预览') }}
                </h4>
            </div>

            <div v-if="resumeData?.id && !isPdf && !iframeLoading" class="flex items-center gap-2 bg-gray-50 border border-gray-100 px-3 py-1.5 rounded-xl text-gray-500 text-[11px] font-bold">
                <button @click="zoomOut" class="hover:text-blue-500 active:scale-90 transition-all cursor-pointer px-1">➖</button>
                <span class="font-mono min-w-[35px] text-center">{{ Math.round(zoomScale * 100) }}%</span>
                <button @click="zoomIn" class="hover:text-blue-500 active:scale-90 transition-all cursor-pointer px-1">➕</button>
                <div class="w-[1px] h-3 bg-gray-200 mx-1"></div>
                <button @click="resetZoom" class="hover:text-blue-500 active:scale-90 transition-all cursor-pointer text-[10px]">重置</button>
            </div>
        </div>

        <div class="flex-1 min-h-0 relative rounded-2xl overflow-hidden bg-gray-50 border border-gray-100">
            
            <div v-if="iframeLoading" class="absolute inset-0 z-10 flex flex-col items-center justify-center bg-white/80 backdrop-blur-sm">
                <div class="w-8 h-8 rounded-full border-2 border-blue-500/20 border-t-blue-500 animate-spin"></div>
                <p class="text-[10px] text-gray-400 font-black uppercase tracking-widest mt-4">Loading Source Document...</p>
            </div>

            <div v-if="resumeData?.id && isPdf && localBlobUrl" class="w-full h-full p-2 bg-white flex items-center justify-center">
                <iframe
                    :src="`${localBlobUrl}#toolbar=0&navpanes=0`"
                    class="w-full h-full border-0 rounded-xl shadow-inner bg-white"
                    @load="onIframeLoad"
                ></iframe>
            </div>

            <div 
                v-else-if="resumeData?.id && !isPdf" 
                class="w-full h-full overflow-auto bg-gray-100/50 p-6 flex justify-center items-start custom-viewer-scrollbar"
            >
                <div 
                    ref="wordContainer" 
                    class="bg-white shadow-md p-8 min-h-full rounded-xl transition-all duration-200 origin-top"
                    :style="{ transform: `scale(${zoomScale})`, width: '100%', maxWidth: '800px' }"
                ></div>
            </div>

            <div v-else class="h-full flex flex-col items-center justify-center text-center py-12">
                <span class="text-2xl mb-2">📁</span>
                <p class="text-xs font-black text-gray-400 uppercase tracking-widest">No document loaded</p>
                <p class="text-[9px] text-gray-400 mt-1">请在左侧列表点击简历，激活原件沙箱视轨</p>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, watch, onUnmounted, computed } from 'vue'
import { getResumePreviewBlob } from '@/api/resume'
import { renderAsync } from 'docx-preview'

const props = defineProps<{
    resumeData: any
    loading?: boolean
}>()

const iframeLoading = ref(false)
const localBlobUrl = ref<string>('')
const wordContainer = ref<HTMLElement | null>(null)

// 🎯 缩放比例状态
const zoomScale = ref(1.0)

// 判定当前选中的简历是否是 PDF 格式
const isPdf = computed(() => {
    const fileName = props.resumeData?.fileName || ''
    return fileName.toLowerCase().endsWith('.pdf')
})

// 🎯 Word 缩放方法
const zoomIn = () => {
    if (zoomScale.value < 2.0) {
        zoomScale.value = parseFloat((zoomScale.value + 0.1).toFixed(1))
    }
}

const zoomOut = () => {
    if (zoomScale.value > 0.5) {
        zoomScale.value = parseFloat((zoomScale.value - 0.1).toFixed(1))
    }
}

const resetZoom = () => {
    zoomScale.value = 1.0
}

watch(() => props.resumeData?.id, async (newId) => {
    if (!newId) {
        localBlobUrl.value = ''
        return
    }

    iframeLoading.value = true
    resetZoom() // 切换简历时，自动重置 Word 的缩放比例

    if (localBlobUrl.value) {
        URL.revokeObjectURL(localBlobUrl.value)
        localBlobUrl.value = ''
    }

    try {
        const response = await getResumePreviewBlob(newId)
        let blob: Blob = response instanceof Blob ? response : response.data
        
        if (isPdf.value) {
            if (blob.type !== 'application/pdf') {
                blob = new Blob([blob], { type: 'application/pdf' })
            }
            localBlobUrl.value = URL.createObjectURL(blob)
        } else {
            // Word 渲染逻辑
            setTimeout(async () => {
                if (wordContainer.value) {
                    wordContainer.value.innerHTML = ''
                    await renderAsync(blob, wordContainer.value, undefined, {
                        className: 'docx',
                        inWrapper: false
                    })
                }
                iframeLoading.value = false
            }, 100)
        }
        
    } catch (error) {
        console.error('获取简历预览失败：', error)
        iframeLoading.value = false
    }
}, { immediate: true })

const onIframeLoad = () => {
    iframeLoading.value = false
}

onUnmounted(() => {
    if (localBlobUrl.value) {
        URL.revokeObjectURL(localBlobUrl.value)
    }
})
</script>

<style scoped>
/* 🎯 自定义滚动条，契合高奢简约风格 */
.custom-viewer-scrollbar::-webkit-scrollbar {
    width: 6px;
    height: 6px;
}
.custom-viewer-scrollbar::-webkit-scrollbar-track {
    background: transparent;
}
.custom-viewer-scrollbar::-webkit-scrollbar-thumb {
    background: #e4e4e7;
    border-radius: 10px;
}
.custom-viewer-scrollbar::-webkit-scrollbar-thumb:hover {
    background: #d4d4d8;
}

/* 🎯 深度控制 Word 渲染组件内的样式，使其更加清爽好看 */
:deep(.docx) {
    background: white !important;
    padding: 0 !important;
    min-height: auto !important;
    box-shadow: none !important;
}

:deep(.docx_wrapper) {
    background: transparent !important;
    padding: 0 !important;
}

/* 强制让 iframe 内部的 HTML 元素背景为纯白，防止穿透出丑陋颜色 */
iframe {
    background-color: #ffffff;
}
</style>