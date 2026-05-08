<template>
    <div class="h-full w-full bg-[#F5F5F7] p-8 overflow-y-auto no-scrollbar font-sans select-none animate-fade-in">
    
    <header class="mb-8 flex flex-col md:flex-row md:items-end md:justify-between gap-6">
        <div>
        <h1 class="text-3xl font-black text-black flex items-center gap-2">
            企业中心与安全设置 <span class="text-xl">⚙️</span>
        </h1>
        <p class="text-xs font-bold text-gray-400 uppercase tracking-widest mt-1">
            Kirin Enterprise Hub · Credentials, Security & Preferences
        </p>
        </div>

        <div class="bg-gray-200/60 p-1 rounded-2xl flex gap-1 self-start">
        <button 
            v-for="tab in tabs" 
            :key="tab.id"
            @click="activeTab = tab.id"
            :class="[
            activeTab === tab.id 
                ? 'bg-white text-black shadow-sm' 
                : 'text-gray-500 hover:text-black'
            ]"
            class="px-4 py-2 text-xs font-black rounded-xl transition-all duration-300"
        >
            {{ tab.name }}
        </button>
        </div>
    </header>

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        
        <div class="space-y-8 lg:col-span-1">
        <div class="bg-white rounded-[32px] border border-gray-100 p-6 shadow-sm space-y-6">
            <div class="flex flex-col items-center text-center pb-6 border-b border-neutral-100">
            <div class="w-20 h-20 rounded-[28px] bg-black flex items-center justify-center text-white text-3xl font-black mb-4 shadow-xl shadow-black/10 relative overflow-hidden group">
                <span>麒</span>
                <div class="absolute inset-0 bg-gradient-to-tr from-indigo-500/10 to-transparent"></div>
            </div>
            <h3 class="text-base font-black text-black">麒麟软件研发管理中心</h3>
            <span class="mt-2 px-2.5 py-1 bg-indigo-50 text-indigo-600 text-[9px] font-black rounded-lg uppercase tracking-wider">
                信创白名单企业
            </span>
            <p class="text-[10px] text-gray-400 font-bold mt-2">南京研发中心 · 核心系统适配部</p>
            </div>

            <div class="space-y-4 text-xs font-bold">
            <div class="flex justify-between">
                <span class="text-gray-400">企业规模</span>
                <span class="text-black">1000 - 9999 人</span>
            </div>
            <div class="flex justify-between">
                <span class="text-gray-400">入驻系统时间</span>
                <span class="text-black">2025 年 10 月</span>
            </div>
            <div class="flex justify-between">
                <span class="text-gray-400">安全合规状态</span>
                <span class="text-emerald-600 flex items-center gap-1">
                <span class="w-1.5 h-1.5 rounded-full bg-emerald-500"></span> 绿区准入通过
                </span>
            </div>
            </div>
        </div>

        <div class="p-5 bg-neutral-900 text-neutral-400 rounded-3xl space-y-2">
            <p class="text-[9px] font-black uppercase text-white tracking-widest">系统运行时保障</p>
            <p class="text-[11px] leading-relaxed">
            当前客户端已与<b>国资云信创安全沙箱</b>建立强加密链路，本地所有账号修改、凭证更新操作将秒级同步至加密中心。
            </p>
        </div>
        </div>

        <div class="lg:col-span-2 space-y-8">
        
        <div v-if="activeTab === 'assets'" class="space-y-8 animate-fade-in">
            
            <div class="bg-black text-white rounded-[32px] p-8 shadow-xl space-y-6 relative overflow-hidden">
            <div class="absolute -right-16 -bottom-16 w-48 h-48 bg-indigo-500/25 rounded-full blur-3xl pointer-events-none"></div>

            <div class="flex justify-between items-center">
                <div>
                <h3 class="text-sm font-black uppercase tracking-wider text-neutral-300 flex items-center gap-2">
                    <span class="text-indigo-400">⚡</span> AI 智能资源与模型算力消耗大盘
                </h3>
                <p class="text-[9px] text-neutral-500 font-bold mt-0.5">Kirin AI Token & Resume Screening Quota</p>
                </div>
                <button class="px-3.5 py-1.5 bg-white/10 hover:bg-white/20 text-white text-[10px] font-black rounded-xl transition-all">
                算力续期 ⚡
                </button>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-3 gap-6 pt-2">
                <div class="space-y-2">
                <div class="flex justify-between text-[10px] font-bold">
                    <span class="text-neutral-400">AI 简历自动初筛</span>
                    <span class="text-cyan-300">4,120 / 5,000 份</span>
                </div>
                <div class="h-1.5 bg-white/10 rounded-full overflow-hidden">
                    <div class="h-full bg-cyan-400 rounded-full" :style="{ width: '82.4%' }"></div>
                </div>
                </div>
                <div class="space-y-2">
                <div class="flex justify-between text-[10px] font-bold">
                    <span class="text-neutral-400">岗位解析 Token 额度</span>
                    <span class="text-indigo-300">76.8% 剩余</span>
                </div>
                <div class="h-1.5 bg-white/10 rounded-full overflow-hidden">
                    <div class="h-full bg-indigo-500 rounded-full" :style="{ width: '76.8%' }"></div>
                </div>
                </div>
                <div class="space-y-2">
                <div class="flex justify-between text-[10px] font-bold">
                    <span class="text-neutral-400">AI 面试高活并发通道</span>
                    <span class="text-emerald-300">4 / 10 线程</span>
                </div>
                <div class="h-1.5 bg-white/10 rounded-full overflow-hidden">
                    <div class="h-full bg-emerald-400 rounded-full" :style="{ width: '40%' }"></div>
                </div>
                </div>
            </div>
            </div>

            <div class="bg-white rounded-[32px] border border-gray-100 p-8 shadow-sm space-y-6">
            <h4 class="text-xs font-black uppercase text-gray-400 tracking-wider flex items-center gap-1.5">
                <span>📈</span> 2026 年度企业招聘效能与留存监控
            </h4>
            <div class="grid grid-cols-2 md:grid-cols-4 gap-6">
                <div class="p-4 bg-[#F5F5F7] rounded-2xl text-center space-y-1">
                <span class="text-[9px] text-gray-400 font-bold uppercase">平均到岗周期</span>
                <p class="text-xl font-black text-black">18.2 天</p>
                <span class="text-[8px] text-emerald-600 font-bold">比行业快 4.5天</span>
                </div>
                <div class="p-4 bg-[#F5F5F7] rounded-2xl text-center space-y-1">
                <span class="text-[9px] text-gray-400 font-bold uppercase">初筛流转效率</span>
                <p class="text-xl font-black text-black">1.2 小时/份</p>
                <span class="text-[8px] text-indigo-600 font-bold">AI 提效 400%</span>
                </div>
                <div class="p-4 bg-[#F5F5F7] rounded-2xl text-center space-y-1">
                <span class="text-[9px] text-gray-400 font-bold uppercase">新进留存率</span>
                <p class="text-xl font-black text-black">94.2%</p>
                <span class="text-[8px] text-emerald-600 font-bold">高度契合</span>
                </div>
                <div class="p-4 bg-[#F5F5F7] rounded-2xl text-center space-y-1">
                <span class="text-[9px] text-gray-400 font-bold uppercase">面试赴约率</span>
                <p class="text-xl font-black text-black">88.5%</p>
                <span class="text-[8px] text-gray-400 font-bold">业界领先</span>
                </div>
            </div>
            </div>

            <div class="bg-white rounded-[32px] border border-gray-100 p-8 shadow-sm space-y-4">
            <h4 class="text-xs font-black uppercase text-gray-400 tracking-wider flex items-center gap-1.5">
                <span>🔌</span> 旗下产品信创兼容认证证书
            </h4>
            <div class="space-y-3">
                <div 
                v-for="cert in certs" 
                :key="cert.name"
                class="p-4 bg-[#F5F5F7] rounded-2xl flex justify-between items-center"
                >
                <div class="flex items-center gap-3">
                    <span class="text-xl">{{ cert.icon }}</span>
                    <div>
                    <p class="text-xs font-black text-black">{{ cert.name }}</p>
                    <p class="text-[9px] text-gray-400 font-bold">证书编号: {{ cert.code }}</p>
                    </div>
                </div>
                <span :class="[cert.active ? 'bg-emerald-50 text-emerald-600' : 'bg-rose-50 text-rose-500']" class="text-[9px] font-black px-2.5 py-1 rounded-lg">
                    {{ cert.active ? '已互认' : '审核中' }}
                </span>
                </div>
            </div>
            </div>

        </div>

        <div v-if="activeTab === 'security'" class="bg-white rounded-[32px] border border-gray-100 p-8 shadow-sm space-y-8 animate-fade-in">
            
            <div class="space-y-4">
            <h4 class="text-xs font-black uppercase text-gray-400 tracking-wider flex items-center gap-1.5">
                <span>🔐</span> 重置登录安全密码
            </h4>
            
            <form @submit.prevent="updatePassword" class="space-y-4">
                <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div class="space-y-1">
                    <label class="text-[10px] font-black text-gray-400 uppercase tracking-widest">当前旧密码</label>
                    <input 
                    v-model="passwordForm.old" 
                    type="password" 
                    placeholder="输入当前密码"
                    class="w-full px-4 py-3 bg-[#F5F5F7] border border-transparent rounded-2xl text-xs font-bold focus:outline-none focus:border-black focus:bg-white transition-all"
                    />
                </div>
                <div class="space-y-1">
                    <label class="text-[10px] font-black text-gray-400 uppercase tracking-widest">新设定密码</label>
                    <input 
                    v-model="passwordForm.new" 
                    type="password" 
                    placeholder="至少 8 位、带符号"
                    class="w-full px-4 py-3 bg-[#F5F5F7] border border-transparent rounded-2xl text-xs font-bold focus:outline-none focus:border-black focus:bg-white transition-all"
                    />
                </div>
                <div class="space-y-1">
                    <label class="text-[10px] font-black text-gray-400 uppercase tracking-widest">确认新密码</label>
                    <input 
                    v-model="passwordForm.confirm" 
                    type="password" 
                    placeholder="再次输入"
                    class="w-full px-4 py-3 bg-[#F5F5F7] border border-transparent rounded-2xl text-xs font-bold focus:outline-none focus:border-black focus:bg-white transition-all"
                    />
                </div>
                </div>
                <button 
                type="submit"
                class="px-5 py-2.5 bg-black hover:bg-neutral-800 text-white text-xs font-black rounded-xl transition-all active:scale-95"
                >
                更新安全密码
                </button>
            </form>
            </div>

            <div class="border-t border-neutral-100 pt-6 space-y-4">
            <h4 class="text-xs font-black uppercase text-gray-400 tracking-wider flex items-center gap-1.5">
                <span>✉️</span> 账号归属与验证邮箱修改
            </h4>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div class="space-y-1">
                <label class="text-[10px] font-black text-gray-400 uppercase tracking-widest">企业官方邮箱 (接收简历投递通知)</label>
                <input 
                    v-model="accountForm.email" 
                    type="email" 
                    class="w-full px-4 py-3 bg-[#F5F5F7] border border-transparent rounded-2xl text-xs font-bold focus:outline-none focus:border-black focus:bg-white transition-all"
                />
                </div>
                <div class="space-y-1">
                <label class="text-[10px] font-black text-gray-400 uppercase tracking-widest">国资盾盾号/秘钥保护区</label>
                <input 
                    v-model="accountForm.shieldKey" 
                    type="password" 
                    class="w-full px-4 py-3 bg-[#F5F5F7] border border-transparent rounded-2xl text-xs font-bold focus:outline-none focus:border-black focus:bg-white transition-all"
                />
                </div>
            </div>
            <button 
                @click="saveAccountConfig"
                class="px-5 py-2.5 bg-black hover:bg-neutral-800 text-white text-xs font-black rounded-xl transition-all active:scale-95"
            >
                保存联系人账号信息
            </button>
            </div>

        </div>

        <div v-if="activeTab === 'privacy'" class="bg-white rounded-[32px] border border-gray-100 p-8 shadow-sm space-y-6 animate-fade-in">
            <h4 class="text-xs font-black uppercase text-gray-400 tracking-wider flex items-center gap-1.5">
            <span>👁️</span> 招聘隐私安全控制与沙箱沙盘
            </h4>
            <p class="text-[11px] text-gray-400 leading-relaxed">
            配置企业在“人才广场”及“人才公海”内的隐私策略。支持在确保技术不外泄的情况下，最精准地对接优秀国产软件人才。
            </p>

            <div class="space-y-4 pt-4">
            <div class="flex items-center justify-between p-4 bg-[#F5F5F7] rounded-2xl">
                <div>
                <p class="text-xs font-black text-black">全网保密极速发布 (不公开企业名称)</p>
                <p class="text-[10px] text-gray-400 font-bold mt-1">开启后，对外隐藏“麒麟软件”公司实体，仅以“某大型基础软硬件适配国企”展示岗位。</p>
                </div>
                <label class="relative inline-flex items-center cursor-pointer">
                <input type="checkbox" v-model="privacySettings.anonymousMode" class="sr-only peer">
                <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-indigo-600"></div>
                </label>
            </div>

            <div class="flex items-center justify-between p-4 bg-[#F5F5F7] rounded-2xl">
                <div>
                <p class="text-xs font-black text-black">屏蔽猎头骚扰与交叉查看</p>
                <p class="text-[10px] text-gray-400 font-bold mt-1">自动拒绝任何非高校应届生/独立全栈开发者的第三方猎头中介访问我方职位能力图谱。</p>
                </div>
                <label class="relative inline-flex items-center cursor-pointer">
                <input type="checkbox" v-model="privacySettings.blockHeadhunters" class="sr-only peer">
                <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-indigo-600"></div>
                </label>
            </div>

            <div class="flex items-center justify-between p-4 bg-[#F5F5F7] rounded-2xl">
                <div>
                <p class="text-xs font-black text-black">自动将简历转化为国密安全隔离件</p>
                <p class="text-[10px] text-gray-400 font-bold mt-1">下载及预览候选人简历时，系统自动脱敏手机号及真实姓名，通过KYC加密协议端到端安全传输。</p>
                </div>
                <label class="relative inline-flex items-center cursor-pointer">
                <input type="checkbox" v-model="privacySettings.autoEncryptResume" class="sr-only peer">
                <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-indigo-600"></div>
                </label>
            </div>
            </div>

            <div class="pt-4 flex justify-end">
            <button 
                @click="savePrivacySettings"
                class="px-6 py-3 bg-black hover:bg-neutral-800 text-white text-xs font-black rounded-2xl transition-all active:scale-95 shadow-md"
            >
                更新全局企业隐私规则
            </button>
            </div>
        </div>

        </div>

    </div>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

// 菜单选项定义
const tabs = [
    { id: 'assets', name: '📋 资质与资产大盘' },
    { id: 'security', name: '🔐 账号密码安全' },
    { id: 'privacy', name: '👁️ 隐私权限系统' }
]
const activeTab = ref('assets')

// Tab 1: 模拟已配置的企业信创产品兼容证书
const certs = ref([
    { name: '银河麒麟高级服务器操作系统V10兼容认证', code: 'KY-2025-CS-8891', icon: '💻', active: true },
    { name: '达梦大型通用数据库DM8兼容认证', code: 'DM-2025-DB-0422', icon: '💾', active: true },
    { name: '统信服务器操作系统V20兼容认证', code: 'TS-2026-OS-7712', icon: '🖥️', active: false }
])

// Tab 2: 密码修改表单
const passwordForm = ref({
    old: '',
    new: '',
    confirm: ''
})

const updatePassword = () => {
    if (!passwordForm.value.old || !passwordForm.value.new || !passwordForm.value.confirm) {
    alert('请将密码表单补充完整！')
    return
    }
    if (passwordForm.value.new !== passwordForm.value.confirm) {
    alert('新设密码与确认新密码输入不一致，请核对！')
    return
    }
    alert('【安全中心通知】：您的企业登录安全密码已更新！下次登录请使用新密码。')
    passwordForm.value = { old: '', new: '', confirm: '' }
}

// Tab 2: 绑定设置表单
const accountForm = ref({
    email: 'hr_partner@kylinos.cn',
    shieldKey: '••••••••••••••••••••••••'
})

const saveAccountConfig = () => {
    alert('企业官方绑定邮箱与国资密盾通道参数更新成功！')
}

// Tab 3: 隐私策略
const privacySettings = ref({
    anonymousMode: false,
    blockHeadhunters: true,
    autoEncryptResume: true
})

const savePrivacySettings = () => {
    alert('企业全局隐私管理设定已在 KYC 运行沙箱中生效！')
}
</script>

<style scoped>
.no-scrollbar::-webkit-scrollbar { display: none; }
.no-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }

.animate-fade-in {
    animation: fadeIn 0.45s cubic-bezier(0.16, 1, 0.3, 1) forwards;
}

@keyframes fadeIn {
    from { opacity: 0; transform: translateY(6px); }
    to { opacity: 1; transform: translateY(0); }
}
</style>