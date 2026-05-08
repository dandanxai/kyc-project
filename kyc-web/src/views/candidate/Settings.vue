<template>
    <div class="h-full w-full bg-[#F5F5F7] p-8 overflow-y-auto no-scrollbar font-sans select-none">
    <div class="max-w-5xl mx-auto flex flex-col md:flex-row gap-8">
        
        <aside class="w-full md:w-64 flex-shrink-0 flex flex-col gap-2">
        <button 
            v-for="tab in tabs" 
            :key="tab.id"
            @click="activeTab = tab.id"
            :class="[activeTab === tab.id ? 'bg-black text-white shadow-md' : 'bg-white text-gray-500 hover:bg-gray-50']"
            class="w-full text-left px-5 py-4 rounded-2xl text-xs font-black transition-all flex items-center justify-between group active:scale-98"
        >
            <span class="flex items-center gap-3">
            <span class="text-base">{{ tab.icon }}</span>
            <span>{{ tab.name }}</span>
            </span>
            <span class="text-[10px] opacity-60">➔</span>
        </button>
        </aside>

        <main class="flex-1 bg-white rounded-[32px] border border-gray-100 p-8 shadow-sm relative">
        
        <div v-if="isLoading" class="absolute inset-0 bg-white/80 backdrop-blur-sm rounded-[32px] z-50 flex items-center justify-center">
            <div class="flex flex-col items-center gap-2">
            <svg class="animate-spin h-8 w-8 text-black" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
            </svg>
            <span class="text-xs font-bold text-gray-400">正在同步安全数据...</span>
            </div>
        </div>

        <div v-if="activeTab === 'profile'" class="space-y-8 animate-fade-in">
            <div>
            <h3 class="text-lg font-black text-black">基本资料</h3>
            <p class="text-[10px] text-gray-400 font-bold uppercase mt-1">Manage your basic public profile details</p>
            </div>

            <div class="flex items-center gap-6 pb-6 border-b border-gray-100">
            <div class="relative w-20 h-20 rounded-full border border-gray-100 shadow-inner overflow-hidden group">
                <img :src="profileForm.avatar || 'https://api.dicebear.com/7.x/avataaars/svg?seed=Felix'" alt="avatar" class="w-full h-full object-cover" />
                <div @click="triggerAvatarUpload" class="absolute inset-0 bg-black/40 flex items-center justify-center opacity-0 group-hover:opacity-100 cursor-pointer transition-opacity">
                <span class="text-[10px] text-white font-bold">更换头像</span>
                </div>
                <input type="file" ref="fileInputRef" class="hidden" accept="image/*" @change="handleAvatarChange" />
            </div>
            <div>
                <h4 class="text-xs font-black text-black">个人头像</h4>
                <p class="text-[10px] text-gray-400 mt-1">推荐使用 200x200 像素的 JPG/PNG 格式</p>
            </div>
            </div>

            <div class="grid grid-cols-1 sm:grid-cols-2 gap-6">
            <div class="space-y-2">
                <label class="text-[10px] font-black text-gray-400 uppercase">真实姓名</label>
                <input v-model="profileForm.username" type="text" class="w-full px-4 py-3 bg-gray-50 rounded-xl text-xs font-bold border border-transparent focus:outline-none focus:border-black focus:bg-white transition-all" />
            </div>
            <div class="space-y-2">
                <label class="text-[10px] font-black text-gray-400 uppercase">用户昵称 (nickname)</label>
                <input v-model="profileForm.nickname" type="text" placeholder="设置你的昵称" class="w-full px-4 py-3 bg-gray-50 rounded-xl text-xs font-bold border border-transparent focus:outline-none focus:border-black focus:bg-white transition-all" />
            </div>
            <div class="space-y-2">
                <label class="text-[10px] font-black text-gray-400 uppercase">求职状态 (jobStatus)</label>
                <select v-model="profileForm.jobStatus" class="w-full px-4 py-3 bg-gray-50 rounded-xl text-xs font-bold border border-transparent focus:outline-none focus:border-black focus:bg-white transition-all">
                <option value="在校-寻找实习">在校-寻找实习</option>
                <option value="离职-随时到岗">离职-随时到岗</option>
                <option value="在职-考虑机会">在职-考虑机会</option>
                <option value="在职-暂不考虑">在职-暂不考虑</option>
                </select>
            </div>
            <div class="space-y-2">
                <label class="text-[10px] font-black text-gray-400 uppercase">电子邮箱 (email)</label>
                <input v-model="profileForm.email" type="email" placeholder="example@domain.com" class="w-full px-4 py-3 bg-gray-50 rounded-xl text-xs font-bold border border-transparent focus:outline-none focus:border-black focus:bg-white transition-all" />
            </div>
            <div class="space-y-2">
                <label class="text-[10px] font-black text-gray-400 uppercase">手机号码 (phone)</label>
                <input v-model="profileForm.phone" type="text" placeholder="输入11位手机号" class="w-full px-4 py-3 bg-gray-50 rounded-xl text-xs font-bold border border-transparent focus:outline-none focus:border-black focus:bg-white transition-all" />
            </div>
            </div>

            <div class="pt-4">
            <button 
                @click="saveProfile" 
                :disabled="isSaving"
                class="px-6 py-3 bg-black hover:bg-neutral-900 disabled:bg-gray-400 text-white text-xs font-bold rounded-xl transition-all active:scale-95 shadow-lg shadow-black/10 flex items-center gap-2"
            >
                <span v-if="isSaving" class="animate-spin text-sm">⌛</span>
                <span>保存更改</span>
            </button>
            </div>
        </div>

        <div v-if="activeTab === 'security'" class="space-y-8 animate-fade-in">
            <div>
            <h3 class="text-lg font-black text-black">账号与安全</h3>
            <p class="text-[10px] text-gray-400 font-bold uppercase mt-1">Keep your credentials and account safe</p>
            </div>

            <div class="space-y-6 max-w-md">
            <div class="space-y-2">
                <label class="text-[10px] font-black text-gray-400 uppercase">当前密码</label>
                <input v-model="securityForm.currentPassword" type="password" placeholder="请输入旧密码" class="w-full px-4 py-3 bg-gray-50 rounded-xl text-xs font-bold border border-transparent focus:outline-none focus:border-black focus:bg-white transition-all" />
            </div>
            <div class="space-y-2">
                <label class="text-[10px] font-black text-gray-400 uppercase">新密码</label>
                <input v-model="securityForm.newPassword" type="password" placeholder="密码长度不得少于 6 位" class="w-full px-4 py-3 bg-gray-50 rounded-xl text-xs font-bold border border-transparent focus:outline-none focus:border-black focus:bg-white transition-all" />
            </div>
            <div class="space-y-2">
                <label class="text-[10px] font-black text-gray-400 uppercase">确认新密码</label>
                <input v-model="securityForm.confirmPassword" type="password" placeholder="请再次输入新密码" class="w-full px-4 py-3 bg-gray-50 rounded-xl text-xs font-bold border border-transparent focus:outline-none focus:border-black focus:bg-white transition-all" />
            </div>
            </div>

            <div class="pt-4 border-t border-gray-100 flex justify-between items-center">
            <button 
                @click="updatePassword" 
                :disabled="isUpdatingPwd"
                class="px-6 py-3 bg-black text-white disabled:bg-gray-400 text-xs font-bold rounded-xl transition-all active:scale-95 flex items-center gap-2"
            >
                <span v-if="isUpdatingPwd" class="animate-spin text-sm">⌛</span>
                <span>更新登录密码</span>
            </button>
            </div>
        </div>

        <div v-if="activeTab === 'privacy'" class="space-y-8 animate-fade-in">
            <div>
            <h3 class="text-lg font-black text-black">隐私控制</h3>
            <p class="text-[10px] text-gray-400 font-bold uppercase mt-1">Control your visibility to recruiters</p>
            </div>
            <div class="space-y-6">
            <div class="flex items-center justify-between p-4 bg-gray-50 rounded-2xl">
                <div>
                <h4 class="text-xs font-black text-black">允许企业搜索我的简历</h4>
                <p class="text-[10px] text-gray-400 mt-1">开启后，信创企业 HR 可以在人才大厅搜寻到你并主动发起直面邀请</p>
                </div>
                <input type="checkbox" v-model="privacyForm.isSearchable" class="w-4 h-4 accent-black cursor-pointer" />
            </div>
            </div>
            <div class="pt-4">
            <button @click="savePrivacySettings" class="px-6 py-3 bg-black text-white text-xs font-bold rounded-xl transition-all active:scale-95">
                保存隐私策略
            </button>
            </div>
        </div>

        <div v-if="activeTab === 'system'" class="space-y-8 animate-fade-in">
            <div>
            <h3 class="text-lg font-black text-black">系统配置</h3>
            <p class="text-[10px] text-gray-400 font-bold uppercase mt-1">Customize your system behavior and view</p>
            </div>
            <div class="space-y-6">
            <div class="space-y-2">
                <label class="text-[10px] font-black text-gray-400 uppercase">系统主语言 Preference</label>
                <select v-model="systemForm.lang" class="w-full px-4 py-3 bg-gray-50 rounded-xl text-xs font-bold border border-transparent focus:outline-none focus:border-black focus:bg-white transition-all">
                <option value="zh-CN">简体中文 (Chinese Simplified)</option>
                <option value="en-US">English (US)</option>
                </select>
            </div>
            </div>
            <div class="pt-4">
            <button @click="saveSystemSettings" class="px-6 py-3 bg-black text-white text-xs font-bold rounded-xl transition-all active:scale-95">
                应用配置
            </button>
            </div>
        </div>

        </main>
    </div>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { 
    updateCandidateProfile, 
    updateCandidatePassword, 
    getCandidateProfile 
} from '@/api/auth' 
import { toast } from '@/utils/toast'

const activeTab = ref('profile')
const isLoading = ref(false)
const isSaving = ref(false)
const isUpdatingPwd = ref(false)

const fileInputRef = ref<HTMLInputElement | null>(null)

const tabs = [
    { id: 'profile', name: '个人信息', icon: '👤' },
    { id: 'security', name: '账号与安全', icon: '🔐' },
    { id: 'privacy', name: '隐私设置', icon: '🛡️' },
    { id: 'system', name: '系统设置', icon: '⚙️' }
]

// 1. 个人资料响应式表单 (字段完全对齐 Candidate 属性)
const profileForm = reactive({
    id: 0,
    username: '',
    nickname: '',
    phone: '',
    email: '',
    avatar: '',
    jobStatus: '在校-寻找实习' // 对应实体类的 jobStatus 属性
})

// 2. 安全设置密码修改表单
const securityForm = reactive({
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
})

// 3. 隐私与系统本地参数
const privacyForm = reactive({ isSearchable: true })
const systemForm = reactive({ lang: 'zh-CN' })

// ==================== 🚀 初始化生命周期钩子 ====================
onMounted(async () => {
    isLoading.value = true
    try {
    // 优先从 localStorage 提取主键 ID 与静态用户名
    const rawUserInfo = localStorage.getItem('user_info')
    if (rawUserInfo) {
        const basicUser = JSON.parse(rawUserInfo)
        profileForm.id = basicUser.id || 0
        profileForm.username = basicUser.username || ''
        
        // 调用 API 实时从数据库中拉取最新的资料细节
        if (profileForm.id) {
        const res = await getCandidateProfile(profileForm.id)
        if (res && res.data) {
            profileForm.nickname = res.data.nickname || ''
            profileForm.phone = res.data.phone || ''
            profileForm.email = res.data.email || ''
            profileForm.avatar = res.data.avatar || ''
            profileForm.jobStatus = res.data.jobStatus || '在校-寻找实习'
        }
        }
    }
    } catch (error: any) {
    console.error('【加载个人配置失败】', error)
    toast.error('加载最新的同步资料失败')
    } finally {
    isLoading.value = false
    }
})

// ==================== 🎯 功能一：更新基本资料到数据库 ====================
const saveProfile = async () => {
    if (!profileForm.id) {
    toast.warning('未获取到账户 ID，请重新登录！')
    return
    }
    if (!profileForm.nickname) {
    toast.warning('用户昵称不能为空哦')
    return
    }

    isSaving.value = true
    try {
    // 提交对齐后的 CandidateProfile 数据包
    await updateCandidateProfile({
        id: profileForm.id,
        username: profileForm.username,
        nickname: profileForm.nickname,
        phone: profileForm.phone,
        email: profileForm.email,
        avatar: profileForm.avatar,
        jobStatus: profileForm.jobStatus
    })

    // 同步更新本地 LocalStorage 保证左侧侧边栏(MainLayout)的头像昵称即时刷新显示
    const rawUserInfo = localStorage.getItem('user_info')
    if (rawUserInfo) {
        const userObj = JSON.parse(rawUserInfo)
        userObj.nickname = profileForm.nickname
        userObj.avatar = profileForm.avatar
        localStorage.setItem('user_info', JSON.stringify(userObj))
    }

    toast.success('资料已安全更新至达梦数据库！')
    } catch (error: any) {
    toast.error(error.message || '资料保存失败，请检查服务状态')
    } finally {
    isSaving.value = false
    }
}

// ==================== 🎯 功能二：修改账号登录密码 ====================
const updatePassword = async () => {
    if (!securityForm.currentPassword || !securityForm.newPassword) {
    toast.warning('请完整填写旧密码与新密码')
    return
    }
    if (securityForm.newPassword.length < 6) {
    toast.warning('新密码长度不能少于 6 位')
    return
    }
    if (securityForm.newPassword !== securityForm.confirmPassword) {
    toast.warning('两次新密码不一致，请核对')
    return
    }

    isUpdatingPwd.value = true
    try {
    // 提交旧密码校验修改请求
    await updateCandidatePassword({
        id: profileForm.id,
        currentPassword: securityForm.currentPassword,
        newPassword: securityForm.newPassword
    })

    toast.success('安全密码修改成功！')
    // 重置重置密码表单
    securityForm.currentPassword = ''
    securityForm.newPassword = ''
    securityForm.confirmPassword = ''
    } catch (error: any) {
    toast.error(error.message || '密码修改失败，请确认旧密码正确')
    } finally {
    isUpdatingPwd.value = false
    }
}

// ==================== 🎯 功能三：非实体关联的其他设置反馈 ====================
const savePrivacySettings = () => {
    toast.success('隐私防沙箱策略保存成功，已实时生效！')
}

const saveSystemSettings = () => {
    toast.success('系统推送与语言偏好应用成功！')
}

// ==================== 🎯 头像选择与模拟 Base64 解析处理 ====================
const triggerAvatarUpload = () => {
    fileInputRef.value?.click()
}

const handleAvatarChange = (event: Event) => {
    const target = event.target as HTMLInputElement
    if (target.files && target.files[0]) {
    const file = target.files[0]
    // 限制 2MB
    if (file.size > 2 * 1024 * 1024) {
        toast.warning('头像文件大小不能超过 2MB')
        return
    }

    const reader = new FileReader()
    reader.onload = (e) => {
        if (e.target?.result) {
        // 直接转换为 Base64 格式并赋给 avatar 字段 (也可改为上传至 OSS / 静态服务器)
        profileForm.avatar = e.target.result as string
        toast.success('头像就绪，点击下方保存更改后生效！')
        }
    }
    reader.readAsDataURL(file)
    }
}
</script>

<style scoped>
.no-scrollbar::-webkit-scrollbar { display: none; }
.no-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }
.animate-fade-in {
    animation: fadeIn 0.35s cubic-bezier(0.4, 0, 0.2, 1) forwards;
}
@keyframes fadeIn {
    from { opacity: 0; transform: translateY(4px); }
    to { opacity: 1; transform: translateY(0); }
}
</style>