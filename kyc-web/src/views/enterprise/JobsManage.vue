<template>
    <div class="h-full w-full bg-[#F5F5F7] p-8 overflow-hidden flex flex-col font-sans select-none animate-fade-in relative">
    
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
        @click="handleOpenPublish"
        class="px-5 py-3.5 bg-black hover:bg-neutral-800 text-white text-xs font-black rounded-2xl shadow-lg shadow-black/10 transition-all active:scale-95 flex items-center gap-2"
        >
        <span>+</span> 发布新信创岗位
        </button>
    </header>

    <div class="flex-1 overflow-y-auto no-scrollbar pr-1 pb-6">
        <div v-if="jobs.length === 0" class="h-64 flex flex-col items-center justify-center bg-white rounded-3xl border border-gray-100">
        <span class="text-3xl mb-2">📁</span>
        <p class="text-gray-400 text-xs font-bold">暂无发布的岗位需求，点击右上角发布</p>
        </div>

        <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div 
            v-for="(job, idx) in jobs" 
            :key="job.id || idx"
            class="bg-white rounded-3xl p-6 border border-gray-100/80 shadow-sm hover:shadow-xl hover:scale-[1.01] transition-all duration-300 flex flex-col justify-between"
        >
            <div>
            <div class="flex justify-between items-start">
                <div>
                <span class="px-2.5 py-1 bg-blue-50 text-blue-600 rounded-lg text-[10px] font-black uppercase tracking-wider">
                    {{ job.requirementSpec?.workLocation || '全国' }}
                </span>
                <h2 class="text-lg font-black text-black mt-2 leading-tight">{{ job.title }}</h2>
                </div>
                <div class="flex gap-2">
                <button @click="handleOpenEdit(job)" class="text-gray-400 hover:text-black transition-all text-xs font-bold">
                    编辑
                </button>
                <button @click="handleDelete(job, idx)" class="text-gray-400 hover:text-red-600 transition-all text-xs font-bold">
                    删除
                </button>
                </div>
            </div>

            <div class="flex gap-3 my-4">
                <span class="text-xs font-black text-red-500">
                {{ job.requirementSpec?.salary?.text || '薪资面议' }}
                </span>
                <span class="text-xs font-bold text-gray-400">|</span>
                <span class="text-xs font-bold text-gray-500">
                {{ job.requirementSpec?.experienceRequirement || '经验不限' }}
                </span>
                <span class="text-xs font-bold text-gray-400">|</span>
                <span class="text-xs font-bold text-gray-500">
                {{ job.requirementSpec?.educationRequirement || '学历不限' }}
                </span>
            </div>

            <div class="flex flex-wrap gap-1.5 mb-5">
                <span 
                v-for="(skill, sIdx) in job.requirementSpec?.skills" 
                :key="sIdx"
                class="px-2.5 py-1 bg-gray-50 text-gray-600 text-[10px] font-bold rounded-lg border border-gray-100"
                >
                {{ skill }}
                </span>
            </div>
            </div>

            <div class="pt-4 border-t border-gray-50 flex justify-between items-center text-[10px] text-gray-400 font-bold">
            <span>发布于 {{ formatDate(job.createTime) }}</span>
            <span class="px-2 py-0.5 bg-green-50 text-green-600 rounded">招聘中</span>
            </div>
        </div>
        </div>
    </div>

    <JobEditDrawer 
        v-model:visible="drawerVisible" 
        :job-data="selectedJob" 
        @submit="handleDrawerSubmit"
    />
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import JobEditDrawer from './job/JobEditDrawer.vue' // 🎯 引入组件

// 假设我们当前系统绑定的静态职位列表数据（后续可对接你的 /api/enterprise/job/page 接口）
const jobs = ref<any[]>([])

// 控制抽屉状态
const drawerVisible = ref(false)
const selectedJob = ref<any>(null)

// 挂载时，从本地缓存或接口模拟一些初始岗位数据
onMounted(() => {
    const mockJobs = [
    {
        id: 1,
        title: 'Kirin OS 应用层适配工程师',
        createTime: '2026-05-11T14:30:00',
        requirementSpec: {
        workLocation: '合肥',
        salary: { text: '12K-20K' },
        experienceRequirement: '1-3年',
        educationRequirement: '本科',
        skills: ['Java', 'C++', 'Qt', 'Kirin OS'],
        jobDescription: ['负责核心业务向国产操作系统Kirin系统的平滑迁移与适配设计。'],
        jobRequirement: ['具有国产操作系统原生套件或Linux底层软件构建研发背景。']
        }
    }
    ]
    jobs.value = mockJobs
})

// 打开发布新岗位抽屉
const handleOpenPublish = () => {
    selectedJob.value = null // 传 null 代表新增
    drawerVisible.value = true
}

// 打开修改岗位抽屉
const handleOpenEdit = (job: any) => {
    selectedJob.value = job // 传入当前行数据
    drawerVisible.value = true
}

// 删除职位（删除就是物理抹除）
const handleDelete = (job: any, index: number) => {
    if (confirm(`确认要永久删除岗位「${job.title}」吗？该删除操作物理生效无法撤销。`)) {
    // 💡 提示：后端写好后，这里可以用 deleteJob(job.id) 发送 RESTful 请求
    jobs.value.splice(index, 1)
    alert('该职位已成功彻底删除！')
    }
}

// 统一处理抽屉提交的数据（无论是新增还是修改）
const handleDrawerSubmit = (formPayload: any) => {
    if (selectedJob.value) {
    // 1. 修改保存逻辑
    const index = jobs.value.findIndex(j => j.id === selectedJob.value.id)
    if (index !== -1) {
        // 💡 提示：此处对接后端 updateJob(jobId, payload)
        jobs.value[index] = {
        ...formPayload,
        updateTime: new Date().toISOString()
        }
        alert('岗位信息修改成功！')
    }
    } else {
    // 2. 新增发布逻辑
    const newJob = {
        ...formPayload,
        id: Date.now(), // 模拟主键
        createTime: new Date().toISOString()
    }
    // 💡 提示：此处对接后端 saveJob(payload)
    jobs.value.unshift(newJob)
    alert('信创岗位发布成功！')
    }

    // 关闭抽屉
    drawerVisible.value = false
}

// 辅助方法：格式化时间
const formatDate = (dateStr: string) => {
    if (!dateStr) return ''
    const date = new Date(dateStr)
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}
</script>

<style scoped>
.no-scrollbar::-webkit-scrollbar { display: none; }
.no-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }

.animate-fade-in {
    animation: fadeIn 0.4s ease-out forwards;
}

@keyframes fadeIn {
    from { opacity: 0; transform: translateY(10px); }
    to { opacity: 1; transform: translateY(0); }
}
</style>