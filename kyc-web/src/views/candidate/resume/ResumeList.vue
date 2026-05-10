<template>
<div class="h-full flex flex-col min-h-0 gap-4 select-none">
    
    <div 
    class="h-[65%] flex flex-col bg-white rounded-[28px] p-5 border border-gray-100 shadow-sm transition-all relative overflow-hidden shrink-0"
    >
    <div class="flex justify-between items-center mb-3 shrink-0">
        <div>
        <h4 class="text-xs font-black text-black uppercase tracking-wider flex items-center gap-1.5">
            📂 历史解析库 
            <span class="text-[9px] bg-gray-100 text-gray-500 px-1.5 py-0.5 rounded-md font-mono">
            {{ (list || []).length }}
            </span>
        </h4>
        <p class="text-[9px] text-gray-400 font-bold uppercase tracking-widest mt-0.5">Repository Version Control</p>
        </div>
        
        <div class="flex items-center gap-1.5">
        <button 
            v-if="!isBatchMode"
            @click="enterBatchMode"
            :disabled="!list || list.length === 0"
            class="text-[9px] font-black px-2.5 py-1.5 rounded-lg border border-gray-200 hover:bg-gray-50 active:scale-95 disabled:opacity-30 transition-all cursor-pointer"
        >
            批量管理
        </button>
        <template v-else>
            <button 
            @click="handleBatchDelete"
            :disabled="selectedIds.length === 0"
            class="text-[9px] font-black px-2.5 py-1.5 rounded-lg bg-red-50 text-red-600 border border-red-100 active:scale-95 disabled:opacity-30 transition-all cursor-pointer"
            >
            物理擦除 ({{ selectedIds.length }})
            </button>
            <button 
            @click="exitBatchMode"
            class="text-[9px] font-black px-2.5 py-1.5 rounded-lg bg-black text-white active:scale-95 transition-all cursor-pointer"
            >
            退出
            </button>
        </template>
        </div>
    </div>

    <div 
        v-if="isBatchMode" 
        class="flex items-center justify-between px-3 py-2 bg-gray-50/70 border border-gray-100/40 rounded-xl shrink-0 mb-2"
    >
        <div class="flex items-center gap-2 cursor-pointer" @click="toggleSelectAll">
        <div 
            class="w-3.5 h-3.5 rounded-md border flex items-center justify-center transition-all"
            :class="[
            isAllSelected 
                ? 'bg-black border-black text-white' 
                : isPartiallySelected 
                ? 'bg-white border-black text-black' 
                : 'bg-white border-gray-300 hover:border-gray-400'
            ]"
        >
            <span v-if="isAllSelected" class="text-[8px] font-bold">✓</span>
            <span v-else-if="isPartiallySelected" class="w-1.5 h-[1.5px] bg-black rounded-full"></span>
        </div>
        <span class="text-[9px] font-black text-gray-700">
            {{ isAllSelected ? '取消全选' : '选择所有达梦备份' }}
        </span>
        </div>
        <span class="text-[8px] text-gray-400 font-mono">SELECTED: {{ selectedIds.length }}/{{ (list || []).length }}</span>
    </div>

    <div class="flex-1 overflow-y-auto space-y-2 pr-1.5 custom-scrollbar min-h-0">
        <template v-if="list && list.length > 0">
          <div 
            v-for="doc in list" 
            :key="doc.id"
            @click="handleItemClick(doc)"
            class="group relative flex items-center p-3 rounded-2xl border transition-all cursor-pointer"
            :class="[
                modelValue === doc.id && !isBatchMode ? 'bg-gray-50 border-black/5 shadow-inner' : 'bg-white border-gray-100 hover:border-gray-200 shadow-sm'
            ]"
          >
            <div v-if="isBatchMode" class="mr-2.5 shrink-0 flex items-center" @click.stop>
                <input 
                type="checkbox" 
                :value="doc.id" 
                v-model="selectedIds"
                class="w-3 h-3 accent-black rounded cursor-pointer"
                >
            </div>

            <div class="w-7 h-7 rounded-lg bg-gray-100 flex items-center justify-center text-xs mr-2.5 shrink-0 group-hover:scale-105 transition-transform">
                {{ doc.fileType?.toLowerCase().includes('pdf') || doc.fileName?.endsWith('pdf') ? '📄' : '📝' }}
            </div>
            
            <div class="flex-1 min-w-0 mr-2">
                <div class="flex items-center gap-1.5">
                <h5 class="text-[11px] font-bold text-black truncate">{{ doc.fileName }}</h5>
                <span v-if="doc.isActive === 1 || doc.isActive === true" class="text-[7px] bg-green-500 text-white px-1 py-0.2 rounded font-mono scale-90 origin-left">DEFAULT</span>
                </div>
                <p class="text-[8px] text-gray-400 font-bold mt-0.5">
                {{ doc.createTime ? doc.createTime.split('T')[0] : '刚刚' }} · {{ doc.fileSize ? (doc.fileSize / 1024).toFixed(0) : 0 }} KB
                </p>
            </div>

            <div v-show="!isBatchMode" class="flex items-center gap-1 shrink-0 opacity-0 group-hover:opacity-100 transition-opacity">
                <button 
                v-if="!doc.isActive && !isBatchMode"
                @click.stop="emit('set-default', doc.id)"
                class="px-1.5 py-1 text-[8px] font-black bg-white border border-gray-200 rounded-md hover:bg-black hover:text-white transition-all cursor-pointer"
                >
                设为默认
                </button>
                <button 
                v-if="!isBatchMode"
                @click.stop="handleSingleDelete(doc.id)"
                class="w-5 h-5 rounded-md border border-red-100 bg-red-50 hover:bg-red-600 text-red-500 hover:text-white flex items-center justify-center text-[9px] transition-all active:scale-90 cursor-pointer"
                title="物理抹除"
                >
                🗑️
                </button>
            </div>
          </div>
        </template>

        <div v-else class="h-full flex flex-col items-center justify-center border-2 border-dashed border-gray-100 rounded-[20px] opacity-40 text-center py-8">
          <span class="text-xl mb-1">📥</span>
          <p class="text-[8px] font-black uppercase tracking-widest text-gray-400">本地数据库暂无简历记录</p>
        </div>
    </div>
    </div>

    <div class="flex-1 min-h-0 relative">
    <input 
        type="file" 
        ref="fileInput" 
        class="hidden" 
        accept=".pdf,.doc,.docx"
        multiple
        @change="handleFileChange"
    >

    <div 
        v-if="pendingFiles.length === 0"
        class="h-full border-2 border-dashed rounded-[28px] transition-all duration-300 flex flex-col items-center justify-center p-4 cursor-pointer overflow-hidden bg-white border-gray-200 hover:border-gray-400 hover:bg-gray-50/50"
        :class="{ 'border-blue-500 bg-blue-50/40 scale-[0.99] ring-4 ring-blue-500/10': isDragging }"
        @dragover.prevent="onDragOver"
        @dragleave.prevent="onDragLeave"
        @drop.prevent="onDrop"
        @click="triggerFileInput"
    >
        <div class="flex flex-col items-center text-center pointer-events-none">
        <div 
            class="w-9 h-9 rounded-2xl bg-gray-50 flex items-center justify-center text-base shadow-sm border border-gray-100/60 mb-2 transition-all"
            :class="{ 'animate-bounce text-blue-600 bg-blue-50': isDragging }"
        >
            {{ isDragging ? '📥' : '☁️' }}
        </div>
        <p class="text-[10px] font-black text-black uppercase tracking-widest">
            {{ isDragging ? '释放以解析导入' : '双生通道·支持多选拖放' }}
        </p>
        <p class="text-[8px] text-gray-400 font-bold mt-1 max-w-[200px] leading-normal">
            拖拽到此或点击浏览，可在下一步中自由核对、删除和补加。
        </p>
        </div>
    </div>

    <div 
        v-else
        class="h-full bg-black text-white rounded-[28px] p-4 flex flex-col min-h-0 border border-black shadow-lg animate-in fade-in zoom-in-95 duration-200"
    >
        <div class="flex justify-between items-center mb-2 shrink-0">
        <div>
            <h5 class="text-[10px] font-black tracking-widest text-gray-300 uppercase">⚡ 待同步导入队列 ({{ pendingFiles.length }})</h5>
            <p class="text-[7px] text-gray-500 font-bold uppercase mt-0.5">Pre-Upload Sandbox Verification</p>
        </div>
        <button 
            @click="triggerFileInput"
            class="text-[8px] font-black px-2 py-1 rounded-md bg-white/10 hover:bg-white/20 active:scale-95 transition-all text-white cursor-pointer"
        >
            ➕ 继续添加
        </button>
        </div>

        <div class="flex-1 overflow-y-auto space-y-1.5 pr-1 custom-scrollbar-white min-h-0 mb-3">
        <div 
            v-for="(pf, index) in pendingFiles" 
            :key="index"
            class="flex items-center justify-between p-2 rounded-xl bg-white/5 border border-white/10 hover:bg-white/10 transition-all"
        >
            <div class="flex items-center min-w-0 mr-2">
            <span class="text-[10px] mr-1.5 shrink-0">
                {{ pf.name.toLowerCase().endsWith('.pdf') ? '📄' : '📝' }}
            </span>
            <span class="text-[9px] font-bold truncate text-white max-w-[150px]" :title="pf.name">
                {{ pf.name }}
            </span>
            <span class="text-[7px] text-gray-400 font-mono ml-2 shrink-0">
                ({{(pf.size / 1024).toFixed(0)}} KB)
            </span>
            </div>
            <button 
            @click.stop="removePendingFile(index)" 
            class="w-4 h-4 rounded-full flex items-center justify-center text-[9px] text-gray-400 hover:text-red-400 hover:bg-white/5 transition-colors cursor-pointer"
            title="剔除该文件"
            >
            ✕
            </button>
        </div>
        </div>

        <div class="flex items-center gap-2 shrink-0">
        <button 
            @click="clearPendingFiles"
            class="flex-1 py-1.5 text-[9px] font-black uppercase text-gray-400 hover:text-white transition-colors cursor-pointer text-center"
        >
            全部放弃
        </button>
        <button 
            @click="submitPendingFiles"
            class="flex-1 py-1.5 text-[9px] font-black uppercase bg-blue-600 text-white rounded-xl hover:bg-blue-500 active:scale-[0.98] transition-all cursor-pointer text-center shadow-md shadow-blue-900/30"
        >
            🚀 确认同步达梦
        </button>
        </div>
    </div>
    </div>

</div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { toast } from '@/utils/toast'

const props = withDefaults(
defineProps<{
    modelValue: number | null
    list: any[]
}>(),
{
    modelValue: null,
    list: () => [] // 🎯 初始空数组兜底，确保安全不报错
}
)

const emit = defineEmits(['update:modelValue', 'upload', 'batch-delete', 'single-delete', 'set-default'])

const fileInput = ref<HTMLInputElement | null>(null)
const isDragging = ref(false)
const isBatchMode = ref(false)
const selectedIds = ref<number[]>([])

// 前端缓冲区
const pendingFiles = ref<File[]>([])

const isAllSelected = computed(() => {
if (!props.list || props.list.length === 0) return false
return props.list.every(doc => selectedIds.value.includes(doc.id))
})

const isPartiallySelected = computed(() => {
if (!props.list || props.list.length === 0 || isAllSelected.value) return false
return props.list.some(doc => selectedIds.value.includes(doc.id))
})

const toggleSelectAll = () => {
if (isAllSelected.value) {
    selectedIds.value = []
} else {
    selectedIds.value = props.list.map(doc => doc.id)
}
}

const triggerFileInput = () => {
fileInput.value?.click()
}

const onDragOver = () => { isDragging.value = true }
const onDragLeave = () => { isDragging.value = false }
const onDrop = (e: DragEvent) => {
isDragging.value = false
const files = e.dataTransfer?.files
if (files && files.length > 0) {
    processFiles(files)
}
}

const handleFileChange = (e: Event) => {
const target = e.target as HTMLInputElement
if (target.files && target.files.length > 0) {
    processFiles(target.files)
}
}

const processFiles = (fileList: FileList) => {
const allowedExtensions = ['.pdf', '.doc', '.docx']
let addedCount = 0

for (let i = 0; i < fileList.length; i++) {
    const file = fileList[i]
    const fileName = file.name.toLowerCase()
    const isValid = allowedExtensions.some(ext => fileName.endsWith(ext))
    const isExist = pendingFiles.value.some(f => f.name === file.name && f.size === file.size)

    if (isValid) {
    if (!isExist) {
        pendingFiles.value.push(file)
        addedCount++
    }
    } else {
    toast.warning(`文件 "${file.name}" 格式不支持`)
    }
}

if (addedCount > 0) {
    toast.success(`已添加 ${addedCount} 份待确认文件`)
}

if (fileInput.value) {
    fileInput.value.value = ''
}
}

const removePendingFile = (index: number) => {
pendingFiles.value.splice(index, 1)
}

const clearPendingFiles = () => {
pendingFiles.value = []
toast.warning('队列已清空')
}

const submitPendingFiles = () => {
if (pendingFiles.value.length === 0) return
const filesToUpload = [...pendingFiles.value]
pendingFiles.value = []
emit('upload', filesToUpload)
}

// 单条删除的核心拦截处理
const handleSingleDelete = async (id: number) => {
const isConfirmed = await toast.confirm(
    '🗑️ 安全警示',
    '确定要永久删除这份附件简历吗？删除后对应的服务器物理文件和解析记录也会被立刻清理。'
)
if (isConfirmed) {
    emit('single-delete', id)
}
}

const handleItemClick = (doc: any) => {
if (isBatchMode.value) {
    const index = selectedIds.value.indexOf(doc.id)
    if (index > -1) {
    selectedIds.value.splice(index, 1)
    } else {
    selectedIds.value.push(doc.id)
    }
} else {
    emit('update:modelValue', doc.id)
}
}

const enterBatchMode = () => {
isBatchMode.value = true
selectedIds.value = []
}

const exitBatchMode = () => {
isBatchMode.value = false
selectedIds.value = []
}

const handleBatchDelete = async () => {
if (selectedIds.value.length === 0) {
    toast.warning('未选择任何可进行擦除的备份记录')
    return
}

const isConfirmed = await toast.confirm(
    '🚨 批量物理抹除警告', 
    `确定要将选中的 ${selectedIds.value.length} 份简历永久清理吗？`
)

if (isConfirmed) {
    emit('batch-delete', [...selectedIds.value])
    exitBatchMode()
}
}
</script>

<style scoped>
.custom-scrollbar::-webkit-scrollbar {
width: 4px;
}
.custom-scrollbar::-webkit-scrollbar-track {
background: transparent;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
background: rgba(0, 0, 0, 0.08);
border-radius: 99px;
}
.custom-scrollbar-white::-webkit-scrollbar {
width: 4px;
}
.custom-scrollbar-white::-webkit-scrollbar-track {
background: transparent;
}
.custom-scrollbar-white::-webkit-scrollbar-thumb {
background: rgba(255, 255, 255, 0.15);
border-radius: 99px;
}
</style>