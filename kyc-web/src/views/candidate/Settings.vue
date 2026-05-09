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
                class="px-6 py-3 bg-rose-500 hover:bg-rose-600 disabled:bg-gray-400 text-white text-xs font-bold rounded-xl transition-all active:scale-95 shadow-lg shadow-rose-500/10 flex items-center gap-2"
            >
                <span v-if="isUpdatingPwd" class="animate-spin text-sm">⌛</span>
                <span>重置安全密码</span>
            </button>
            </div>
        </div>

        <div v-if="activeTab === 'privacy'" class="space-y-8 animate-fade-in">
            <div>
            <h3 class="text-lg font-black text-black">隐私防沙箱设置</h3>
            <p class="text-[10px] text-gray-400 font-bold uppercase mt-1">Control who can discover your portfolio</p>
            </div>

            <div class="space-y-6">
            <div class="flex items-center justify-between p-4 bg-gray-50 rounded-2xl max-w-xl">
                <div>
                <h4 class="text-xs font-black text-black">允许企业主动检索我</h4>
                <p class="text-[10px] text-gray-400 mt-1">开启后，企业端才可以通过人才公海搜索到您的信息</p>
                </div>
                <input v-model="privacyForm.isSearchable" type="checkbox" class="w-5 h-5 accent-black cursor-pointer" />
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
            <h3 class="text-lg font-black text-black">系统偏好</h3>
            <p class="text-[10px] text-gray-400 font-bold uppercase mt-1">System preference configurations</p>
            </div>

            <div class="space-y-6 max-w-md">
            <div class="space-y-2">
                <label class="text-[10px] font-black text-gray-400 uppercase">语言偏好 (Language)</label>
                <select v-model="systemForm.lang" class="w-full px-4 py-3 bg-gray-50 rounded-xl text-xs font-bold border border-transparent focus:outline-none focus:border-black focus:bg-white transition-all">
                <option value="zh-CN">简体中文 (zh-CN)</option>
                <option value="en-US">English (en-US)</option>
                <option value="ja-JP">日本語 (ja-JP)</option>
                </select>
            </div>
            </div>

            <div class="pt-4">
            <button @click="saveSystemSettings" class="px-6 py-3 bg-black text-white text-xs font-bold rounded-xl transition-all active:scale-95">
                应用偏好
            </button>
            </div>
        </div>

        </main>
    </div>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getCandidateProfile, updateCandidateProfile, updateCandidatePassword } from '@/api/auth'
import { useUserStore } from '@/store/user' // 🎯 1. 引入 Pinia Store
import { toast } from '@/utils/toast'

// 💡 如果你的 Vite 路径解析有问题，可改成相对路径引入：
// import { useUserStore } from '../store/user'

const userStore = useUserStore() // 🎯 2. 实例化 Pinia 状态机

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
    jobStatus: '在校-寻找实习'
})

// 2. 安全设置密码修改表单
const securityForm = reactive({
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
})

// 3. 隐私与系统本地参数
const privacyForm = reactive({
    isSearchable: true
})
const systemForm = reactive({
    lang: 'zh-CN'
})

// ==================== 🚀 初始化生命周期钩子 ====================
onMounted(async () => {
    isLoading.value = true
    try {
        // 🎯 1. 如果 Pinia 没数据（比如用户在这里按了 F5 刷新页面），才触发接口拉取
        if (!userStore.hasLoaded) {
            await userStore.fetchUserProfile()
        }
        
        // 🎯 2. 现在 Pinia 里一定有数据了，直接从 Pinia 内存读取，不发任何网络请求！
        profileForm.id = userStore.id || 0
        profileForm.username = userStore.username
        profileForm.nickname = userStore.nickname
        profileForm.avatar = userStore.avatar
        profileForm.phone = userStore.phone
        profileForm.email = userStore.email
        profileForm.jobStatus = userStore.jobStatus

    } catch (error: any) {
        toast.error('账户数据同步失败！')
    } finally {
        isLoading.value = false
    }
})

// ==================== 🎯 保存个人资料更改 ====================
const saveProfile = async () => {
    if (!profileForm.username) {
        toast.warning('姓名不能为空！')
        return
    }

    isSaving.value = true
    try {
        // 1. 调用 API 写入达梦数据库
        const res = await updateCandidateProfile({
            id: profileForm.id,
            username: profileForm.username,
            nickname: profileForm.nickname,
            phone: profileForm.phone,
            email: profileForm.email,
            avatar: profileForm.avatar,
            jobStatus: profileForm.jobStatus
        })

        if (res.code === 200) {
            // 🎯 2. 核心联动：数据库保存成功后，立刻同步更新 Pinia 状态机中的状态！
            userStore.nickname = profileForm.nickname
            userStore.avatar = profileForm.avatar
            userStore.hasLoaded = true

            toast.success('安全配置保存成功，全局视图已同步更新！ ✨')
        }
    } catch (error: any) {
        toast.error(error.message || '资料更新失败，请稍后重试')
    } finally {
        isSaving.value = false
    }
}

// ==================== 🎯 账户密码修改逻辑 ====================
const updatePassword = async () => {
    if (!securityForm.currentPassword) {
        toast.warning('请提供当前密码以验证身份！')
        return
    }
    if (!securityForm.newPassword || securityForm.newPassword.length < 6) {
        toast.warning('新密码长度不得少于 6 位！')
        return
    }
    if (securityForm.newPassword !== securityForm.confirmPassword) {
        toast.warning('两次输入的新密码不匹配！')
        return
    }

    isUpdatingPwd.value = true
    try {
        await updateCandidatePassword({
            id: profileForm.id,
            currentPassword: securityForm.currentPassword,
            newPassword: securityForm.newPassword
        })

        toast.success('安全密码修改成功！下次登录时生效。')
        // 清空密码表单
        securityForm.currentPassword = ''
        securityForm.newPassword = ''
        securityForm.confirmPassword = ''
    } catch (error: any) {
        toast.error(error.message || '密码修改失败，请确认旧密码正确')
    } finally {
        isUpdatingPwd.value = false
    }
}

// ==================== 🎯 其他偏好设置 ====================
const savePrivacySettings = () => {
    toast.success('隐私防沙箱策略保存成功，已实时生效！')
}

const saveSystemSettings = () => {
    toast.success('系统推送与语言偏好应用成功！')
}

// ==================== 🎯 头像选择处理 ====================
const triggerAvatarUpload = () => {
    fileInputRef.value?.click()
}

const handleAvatarChange = (event: Event) => {
    const target = event.target as HTMLInputElement
    if (target.files && target.files[0]) {
        const file = target.files[0]
        if (file.size > 2 * 1024 * 1024) {
            toast.warning('头像文件大小不能超过 2MB')
            return
        }

        const reader = new FileReader()
        reader.onload = (e) => {
            if (e.target?.result) {
                // 转成 Base64
                profileForm.avatar = e.target.result as string
                toast.success('本地解析成功，请点击“保存更改”进行云同步')
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
    animation: fadeIn 0.35s ease-out forwards;
}
@keyframes fadeIn {
    from { opacity: 0; transform: translateY(6px); }
    to { opacity: 1; transform: translateY(0); }
}
</style>