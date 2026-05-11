<template>
    <transition name="drawer-fade">
    <div v-if="visible" class="fixed inset-0 z-50 flex justify-end">
        <div class="absolute inset-0 bg-black/20 backdrop-blur-sm transition-opacity" @click="close"></div>

        <div class="relative w-full md:max-w-[88%] bg-[#F5F5F7] h-full shadow-2xl flex flex-col justify-between overflow-hidden animate-slide-in">
        
        <header class="px-8 py-5 border-b border-gray-100 flex justify-between items-center bg-white shrink-0">
            <div>
            <h2 class="text-xl font-black text-black flex items-center gap-2">
                {{ isEdit ? '编辑信创岗位' : '发布新信创岗位' }}
                <span class="px-2.5 py-0.5 bg-gray-100 text-[10px] font-black text-gray-500 rounded-full">88% HUD PANEL</span>
            </h2>
            <p class="text-xs text-gray-400 mt-1">左侧支持高保真附件视轨预览，右侧为岗位技术匹配规格表单</p>
            </div>
            <button @click="close" class="w-9 h-9 rounded-full bg-gray-50 hover:bg-gray-100 flex items-center justify-center transition-all cursor-pointer">
            <span class="text-gray-400 text-xl font-light">×</span>
            </button>
        </header>

        <div class="flex-1 flex overflow-hidden min-h-0 bg-[#F5F5F7]">
            
            <div class="w-1/2 p-6 flex flex-col border-r border-gray-100 bg-white min-w-0">
            <div class="flex justify-between items-center border-b border-gray-100 pb-4 mb-4 shrink-0">
                <div>
                <span class="text-[9px] text-blue-500 font-black uppercase tracking-[0.2em] block">Original Document Twin</span>
                <h4 class="text-sm font-black text-gray-800 tracking-tight line-clamp-1">
                    📄 {{ iframeLoading ? 'AI 正在同步原件视轨...' : (tempFileName || '未加载物理原件') }}
                </h4>
                </div>

                <div v-if="tempFileName && !isPdf && !iframeLoading" class="flex items-center gap-2 bg-gray-50 border border-gray-100 px-3 py-1.5 rounded-xl text-gray-500 text-[11px] font-bold">
                <button @click="zoomOut" class="hover:text-blue-500 active:scale-90 transition-all cursor-pointer px-1">➖</button>
                <span class="font-mono min-w-[35px] text-center">{{ Math.round(zoomScale * 100) }}%</span>
                <button @click="zoomIn" class="hover:text-blue-500 active:scale-90 transition-all cursor-pointer px-1">➕</button>
                </div>
            </div>

            <div class="flex-1 min-h-0 relative rounded-2xl overflow-hidden bg-gray-50 border border-gray-100 flex flex-col items-center justify-center">
                
                <iframe 
                v-if="isPdf && localBlobUrl" 
                :src="localBlobUrl" 
                class="w-full h-full border-0"
                @load="iframeLoading = false"
                ></iframe>

                <div 
                v-else-if="!isPdf && localBlobUrl"
                ref="wordContainer"
                class="w-full h-full overflow-y-auto p-8 custom-viewer-scrollbar bg-white"
                :style="{ transform: `scale(${zoomScale})`, transformOrigin: 'top center' }"
                ></div>

                <div 
                v-else 
                class="w-full h-full flex flex-col items-center justify-center p-8 text-center cursor-pointer hover:bg-gray-100/50 transition-all"
                @dragover.prevent
                @drop.prevent="handleFileDrop"
                @click="triggerFileSelect"
                >
                <div class="w-16 h-16 rounded-full bg-blue-50 flex items-center justify-center mb-4">
                    <span class="text-2xl">📁</span>
                </div>
                <h5 class="text-sm font-bold text-gray-800">将岗位说明书/附件拖拽至此</h5>
                <p class="text-xs text-gray-400 mt-1 max-w-[280px]">支持 PDF、DOCX 格式文件，上传后右侧表单将完美绑定，并在左侧无缝高保真展示</p>
                <input 
                    type="file" 
                    ref="fileInput" 
                    class="hidden" 
                    accept=".pdf,.docx" 
                    @change="handleFileSelect"
                />
                </div>

                <div v-if="iframeLoading" class="absolute inset-0 flex flex-col items-center justify-center bg-white/95 backdrop-blur-md z-10 transition-all">
                <div class="animate-spin rounded-full h-10 w-10 border-t-2 border-b-2 border-blue-500 mb-4"></div>
                <span class="text-xs font-black text-gray-500 tracking-wider">AI 正在同步原件视轨...</span>
                </div>
            </div>
            </div>

            <div class="w-1/2 p-8 overflow-y-auto bg-[#F5F5F7] custom-viewer-scrollbar space-y-6">
            
            <div class="bg-white p-6 rounded-[24px] border border-gray-100 shadow-sm space-y-4">
                <h3 class="text-sm font-bold text-gray-800 border-l-4 border-blue-500 pl-3">基础规格</h3>
                
                <div class="grid grid-cols-2 gap-4">
                <div>
                    <label class="block text-xs font-bold text-gray-400 uppercase tracking-wider mb-2">职位名称 *</label>
                    <input v-model="form.title" type="text" placeholder="如：信创Java开发工程师" class="w-full px-4 py-3 bg-gray-50 border border-gray-100 rounded-xl text-xs text-black focus:outline-none focus:border-blue-500" />
                </div>
                <div>
                    <label class="block text-xs font-bold text-gray-400 uppercase tracking-wider mb-2">薪资待遇</label>
                    <input v-model="form.salary" type="text" placeholder="如：12K-18K" class="w-full px-4 py-3 bg-gray-50 border border-gray-100 rounded-xl text-xs text-black focus:outline-none focus:border-blue-500" />
                </div>
                </div>

                <div class="grid grid-cols-3 gap-4">
                <div>
                    <label class="block text-xs font-bold text-gray-400 uppercase tracking-wider mb-2">工作城市</label>
                    <input v-model="form.city" type="text" placeholder="如：哈尔滨" class="w-full px-4 py-3 bg-gray-50 border border-gray-100 rounded-xl text-xs text-black focus:outline-none focus:border-blue-500" />
                </div>
                <div>
                    <label class="block text-xs font-bold text-gray-400 uppercase tracking-wider mb-2">学历要求</label>
                    <select v-model="form.education" class="w-full px-4 py-3 bg-gray-50 border border-gray-100 rounded-xl text-xs text-black focus:outline-none focus:border-blue-500">
                    <option value="大专">大专</option>
                    <option value="本科">本科</option>
                    <option value="硕士">硕士</option>
                    <option value="博士">博士</option>
                    <option value="不限">不限</option>
                    </select>
                </div>
                <div>
                    <label class="block text-xs font-bold text-gray-400 uppercase tracking-wider mb-2">经验要求</label>
                    <input v-model="form.experience" type="text" placeholder="如：3-5年" class="w-full px-4 py-3 bg-gray-50 border border-gray-100 rounded-xl text-xs text-black focus:outline-none focus:border-blue-500" />
                </div>
                </div>
            </div>

            <div class="bg-white p-6 rounded-[24px] border border-gray-100 shadow-sm space-y-4">
                <h3 class="text-sm font-bold text-gray-800 border-l-4 border-blue-500 pl-3">AI 核心技能画像</h3>
                
                <div>
                <label class="block text-xs font-bold text-gray-400 uppercase tracking-wider mb-2">技能标签（按回车或逗号键追加）</label>
                <div class="flex flex-wrap gap-2 p-3 bg-gray-50 border border-gray-100 rounded-2xl min-h-[50px] items-center">
                    <span 
                    v-for="(skill, index) in form.requirementSpec.skills" 
                    :key="index" 
                    class="px-3 py-1 bg-black text-white rounded-lg text-[10px] font-bold flex items-center gap-1.5"
                    >
                    {{ skill }}
                    <button @click="removeSkill(index)" class="hover:text-red-400 cursor-pointer font-light">×</button>
                    </span>
                    <input 
                    v-model="skillInput" 
                    type="text" 
                    placeholder="输入技能按回车..." 
                    class="bg-transparent text-xs text-black focus:outline-none border-0 py-1 px-2 flex-1 min-w-[100px]" 
                    @keydown.enter.prevent="addSkill"
                    @keydown.delete="handleSkillDelete"
                    />
                </div>
                </div>
            </div>

            <div class="bg-white p-6 rounded-[24px] border border-gray-100 shadow-sm space-y-4">
                <h3 class="text-sm font-bold text-gray-800 border-l-4 border-blue-500 pl-3">职责描述与岗位要求</h3>
                
                <div>
                <label class="block text-xs font-bold text-gray-400 uppercase tracking-wider mb-2">职责描述（按换行符换行，存为数组形式）</label>
                <textarea 
                    v-model="jobDescText" 
                    rows="4" 
                    placeholder="请输入岗位职责，每行一条..." 
                    class="w-full p-4 bg-gray-50 border border-gray-100 rounded-2xl text-xs text-black focus:outline-none focus:border-blue-500 font-mono"
                ></textarea>
                </div>

                <div>
                <label class="block text-xs font-bold text-gray-400 uppercase tracking-wider mb-2">岗位要求（按换行符换行，存为数组形式）</label>
                <textarea 
                    v-model="jobReqText" 
                    rows="4" 
                    placeholder="请输入岗位要求，每行一条..." 
                    class="w-full p-4 bg-gray-50 border border-gray-100 rounded-2xl text-xs text-black focus:outline-none focus:border-blue-500 font-mono"
                ></textarea>
                </div>
            </div>

            <div v-if="tempFileName" class="bg-blue-50/50 p-5 rounded-[24px] border border-blue-100/50 flex justify-between items-center">
                <div class="flex items-center gap-3">
                <span class="text-xl">📎</span>
                <div>
                    <h4 class="text-xs font-black text-gray-800">已成功锚定原件附件</h4>
                    <p class="text-[10px] text-gray-400 mt-0.5 font-mono">{{ tempFileName }}</p>
                </div>
                </div>
                <button @click="removeAttachedFile" class="text-xs font-bold text-red-500 hover:text-red-700 cursor-pointer">
                清除附件
                </button>
            </div>

            </div>
        </div>

        <footer class="px-8 py-5 border-t border-gray-100 bg-white flex justify-between items-center shrink-0">
            <button @click="close" class="px-5 py-3 bg-gray-50 hover:bg-gray-100 text-gray-500 text-xs font-black rounded-xl cursor-pointer">
            取消
            </button>
            
            <div class="flex items-center gap-3">
            <button 
                v-if="!tempFileName"
                @click="triggerFileSelect"
                class="px-5 py-3 bg-gray-100 hover:bg-gray-200 text-gray-700 text-xs font-black rounded-xl cursor-pointer flex items-center gap-2"
            >
                <span>📎</span> 附带原件
            </button>
            
            <button @click="handleSubmit" class="px-7 py-3 bg-black hover:bg-neutral-800 text-white text-xs font-black rounded-xl shadow-lg shadow-black/10 transition-all cursor-pointer">
                {{ isEdit ? '保存修改' : '确认发布' }}
            </button>
            </div>
        </footer>

        </div>
    </div>
    </transition>
</template>

<script setup lang="ts">
import { ref, watch, onUnmounted } from 'vue'
import { renderAsync } from 'docx-preview'

// 🎨 优雅解耦：纯粹使用 @/api/job 内定义的安全方法进行 API 交互，剔除原生 axios
import { 
    uploadJobTempFile, 
    createJob, 
    updateJob, 
    getJobDetail,
    getJobTempFileBlob // 👈 刚才在 api/job.ts 里新增的配置方法
} from '@/api/job'

const props = defineProps({
    visible: { type: Boolean, default: false },
    isEdit: { type: Boolean, default: false },
    jobId: { type: [Number, String], default: '' }
})

const emit = defineEmits(['update:visible', 'submit'])

// 响应式数据绑定（达梦数据库表字段）
const form = ref({
    title: '',
    salary: '',
    city: '哈尔滨',
    education: '本科',
    experience: '不限',
    tempFilePath: '', // 用于跟后端绑定的物理相对路径
    requirementSpec: {
    skills: [] as string[],
    jobDescription: [] as string[],
    jobRequirement: [] as string[]
    }
})

// 视图级状态
const skillInput = ref('')
const jobDescText = ref('')
const jobReqText = ref('')

const tempFileName = ref('') // 原件物理名字
const isPdf = ref(false)
const iframeLoading = ref(false)
const localBlobUrl = ref('') // 内存映射伪 URL
const zoomScale = ref(1.0)

// 挂载点
const fileInput = ref<HTMLInputElement | null>(null)
const wordContainer = ref<HTMLElement | null>(null)

// 统一监听显示
watch(() => props.visible, async (newVal) => {
    if (newVal) {
    resetForm()
    if (props.isEdit && props.jobId) {
        await loadJobData(props.jobId)
    }
    }
})

// ==================== 🛠️ API 统一交互 ====================

// 1. 获取职位详情
const loadJobData = async (id: number | string) => {
    try {
    iframeLoading.value = true
    const res = await getJobDetail(id)
    if (res.code === 200 && res.data) {
        const data = res.data
        form.value.title = data.title || ''
        form.value.salary = data.salary || ''
        form.value.city = data.city || '哈尔滨'
        form.value.education = data.education || '本科'
        form.value.experience = data.experience || '不限'
        form.value.tempFilePath = data.tempFilePath || ''
        
        if (data.requirementSpec) {
        form.value.requirementSpec.skills = data.requirementSpec.skills || []
        form.value.requirementSpec.jobDescription = data.requirementSpec.jobDescription || []
        form.value.requirementSpec.jobRequirement = data.requirementSpec.jobRequirement || []
        
        jobDescText.value = (data.requirementSpec.jobDescription || []).join('\n')
        jobReqText.value = (data.requirementSpec.jobRequirement || []).join('\n')
        }

        // 如果之前已经绑定了暂存附件，自动拉取预览流
        if (form.value.tempFilePath) {
        const lastSlash = form.value.tempFilePath.lastIndexOf('/')
        tempFileName.value = lastSlash !== -1 ? form.value.tempFilePath.substring(lastSlash + 1) : form.value.tempFilePath
        await fetchFileBlob(form.value.tempFilePath)
        } else {
        iframeLoading.value = false
        }
    }
    } catch (error) {
    console.error('获取信创岗位详情失败:', error)
    iframeLoading.value = false
    }
}

// 2. 触发暂存上传
const handleUpload = async (file: File) => {
    try {
    iframeLoading.value = true
    const res = await uploadJobTempFile(file)
    if (res.code === 200 && res.data) {
        form.value.tempFilePath = res.data.tempPath
        tempFileName.value = file.name
        
        // 上传后同步拉取预览
        await fetchFileBlob(res.data.tempPath)
    } else {
        alert(res.message || '上传岗位指南失败')
        iframeLoading.value = false
    }
    } catch (error) {
    console.error('上传附件异常:', error)
    iframeLoading.value = false
    }
}

// 3. 🛡️ 安全拉取后端二进制物理文件流
const fetchFileBlob = async (filePath: string) => {
    try {
    isPdf.value = filePath.toLowerCase().endsWith('.pdf')
    
    // 🎯 核心调用：使用我们刚才封装在 api/job 中的 blob 下载流服务
    // 因为走的是 request 拦截，所以会自动带上 Authorization Token，从而绝不报错！
    const blobResponse = await getJobTempFileBlob(filePath)
    
    // 接收流
    const blob = new Blob([blobResponse as any])
    
    if (localBlobUrl.value) {
        URL.revokeObjectURL(localBlobUrl.value)
    }
    localBlobUrl.value = URL.createObjectURL(blob)

    // 如果是非 PDF 格式 (即 DOCX)，利用挂载点和 docx-preview 无损呈现
    if (!isPdf.value) {
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
    } else {
        // PDF iframe 渲染
    }
    } catch (error) {
    console.error('安全加载附件预览流异常 (可能是文件权限或拦截器错误):', error)
    iframeLoading.value = false
    }
}

// ==================== 📁 拖拽或手动选择捕获 ====================

const triggerFileSelect = () => {
    fileInput.value?.click()
}

const handleFileSelect = (e: Event) => {
    const files = (e.target as HTMLInputElement).files
    if (files && files.length > 0) {
    handleUpload(files[0])
    }
}

const handleFileDrop = (e: DragEvent) => {
    const files = e.dataTransfer?.files
    if (files && files.length > 0) {
    const file = files[0]
    const ext = file.name.substring(file.name.lastIndexOf('.')).toLowerCase()
    if (ext === '.pdf' || ext === '.docx') {
        handleUpload(file)
    } else {
        alert('系统目前仅支持预渲染高保真 PDF、DOCX 的信创物理原件。')
    }
    }
}

const removeAttachedFile = () => {
    form.value.tempFilePath = ''
    tempFileName.value = ''
    if (localBlobUrl.value) {
    URL.revokeObjectURL(localBlobUrl.value)
    localBlobUrl.value = ''
    }
}

// ==================== 🛠️ 缩放控制（和 ResumeViewer.vue 一致） ====================
const zoomIn = () => {
    if (zoomScale.value < 1.5) zoomScale.value += 0.1
}
const zoomOut = () => {
    if (zoomScale.value > 0.6) zoomScale.value -= 0.1
}

// ==================== 📑 表单操作辅组 ====================
const addSkill = () => {
    const cleanVal = skillInput.value.replace(/,/g, '').trim()
    if (cleanVal && !form.value.requirementSpec.skills.includes(cleanVal)) {
    form.value.requirementSpec.skills.push(cleanVal)
    }
    skillInput.value = ''
}

const removeSkill = (idx: number) => {
    form.value.requirementSpec.skills.splice(idx, 1)
}

const handleSkillDelete = () => {
    if (!skillInput.value && form.value.requirementSpec.skills.length > 0) {
    form.value.requirementSpec.skills.pop()
    }
}

const resetForm = () => {
    form.value = {
    title: '',
    salary: '',
    city: '哈尔滨',
    education: '本科',
    experience: '不限',
    tempFilePath: '',
    requirementSpec: {
        skills: [],
        jobDescription: [],
        jobRequirement: []
    }
    }
    jobDescText.value = ''
    jobReqText.value = ''
    tempFileName.value = ''
    isPdf.value = false
    iframeLoading.value = false
    if (localBlobUrl.value) {
    URL.revokeObjectURL(localBlobUrl.value)
    localBlobUrl.value = ''
    }
    zoomScale.value = 1.0
}

const close = () => {
    emit('update:visible', false)
}

// ==================== 🚀 统一发布/保存提交 ====================
const handleSubmit = async () => {
    if (!form.value.title.trim()) {
    alert('请输入职位名称')
    return
    }

    form.value.requirementSpec.jobDescription = jobDescText.value
    .split('\n')
    .map(i => i.trim())
    .filter(Boolean)

    form.value.requirementSpec.jobRequirement = jobReqText.value
    .split('\n')
    .map(i => i.trim())
    .filter(Boolean)

    try {
    const payload = JSON.parse(JSON.stringify(form.value))
    let res
    if (props.isEdit && props.jobId) {
        res = await updateJob(props.jobId, payload)
    } else {
        res = await createJob(payload)
    }

    if (res.code === 200) {
        alert(props.isEdit ? '信创岗位变更已存储！' : '新信创职位发布成功，AI 匹配链已部署！')
        emit('submit')
        close()
    } else {
        alert(res.message || '操作异常')
    }
    } catch (error) {
    console.error('提交岗位规格失败:', error)
    }
}

onUnmounted(() => {
    if (localBlobUrl.value) {
    URL.revokeObjectURL(localBlobUrl.value)
    }
})
</script>

<style scoped>
/* 🎯 自定义高奢滚动条 */
.custom-viewer-scrollbar::-webkit-scrollbar {
    width: 5px;
    height: 5px;
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

/* 🎯 控制 docx-preview 渲染容器样式 */
:deep(.docx) {
    background: #ffffff !important;
    padding: 0 !important;
    box-shadow: none !important;
    min-height: auto !important;
    width: 100% !important;
    font-family: inherit !important;
}

:deep(.docx_wrapper) {
    background: transparent !important;
    padding: 0 !important;
}

/* 动效过渡 */
.drawer-fade-enter-active, .drawer-fade-leave-active {
    transition: opacity 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}
.drawer-fade-enter-from, .drawer-fade-leave-to {
    opacity: 0;
}

.animate-slide-in {
    animation: slideIn 0.4s cubic-bezier(0.16, 1, 0.3, 1);
}

@keyframes slideIn {
    from { transform: translateX(100%); }
    to { transform: translateX(0); }
}
</style>