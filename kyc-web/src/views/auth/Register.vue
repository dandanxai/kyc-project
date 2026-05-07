<template>
    <div class="h-full w-full flex items-center justify-center relative overflow-hidden">
    <div class="absolute top-[-10%] left-[-5%] w-[600px] h-[600px] bg-purple-100/40 blur-[120px] rounded-full"></div>
    <div class="absolute bottom-[-10%] right-[-5%] w-[500px] h-[500px] bg-blue-100/30 blur-[120px] rounded-full"></div>

    <div class="relative z-10 w-[95%] max-w-[520px] bg-white/80 backdrop-blur-3xl p-10 rounded-[40px] shadow-2xl shadow-black/5 border border-white overflow-y-auto max-h-[90vh] no-scrollbar">
        
        <div class="flex flex-col items-center mb-8">
        <h1 class="text-3xl font-bold text-black tracking-tight text-center">加入 KYC 智库</h1>
        <p class="text-gray-400 mt-2 text-[14px] text-center">构建您的数字人才能力图谱</p>
        </div>

        <div class="flex p-1.5 bg-gray-200/50 rounded-2xl mb-8">
        <button @click="role = 'candidate'" :class="role === 'candidate' ? 'bg-white shadow-md text-black' : 'text-gray-500'" class="flex-1 py-2.5 text-[13px] font-semibold rounded-xl transition-all duration-300">人才个人注册</button>
        <button @click="role = 'enterprise'" :class="role === 'enterprise' ? 'bg-white shadow-md text-black' : 'text-gray-500'" class="flex-1 py-2.5 text-[13px] font-semibold rounded-xl transition-all duration-300">企业机构入驻</button>
        </div>

        <div class="space-y-4">
        <div class="grid grid-cols-2 gap-4">
            <input type="text" v-model="form.username" placeholder="用户名" class="px-5 py-3.5 bg-gray-100/80 rounded-2xl focus:bg-white transition-all outline-none text-[14px] border-2 border-transparent focus:border-black/5" />
            <input type="text" v-model="form.realName" :placeholder="role === 'candidate' ? '真实姓名' : '经办人姓名'" class="px-5 py-3.5 bg-gray-100/80 rounded-2xl focus:bg-white transition-all outline-none text-[14px] border-2 border-transparent focus:border-black/5" />
        </div>

        <div class="relative flex items-center">
            <input type="tel" v-model="form.phone" placeholder="手机号码" class="w-full px-5 py-3.5 bg-gray-100/80 rounded-2xl focus:bg-white transition-all outline-none text-[14px] border-2 border-transparent focus:border-black/5" />
            <button class="absolute right-2 px-4 py-1.5 bg-black text-white text-[12px] rounded-lg hover:bg-gray-800 transition-colors">获取验证码</button>
        </div>
        <input type="text" v-model="form.code" placeholder="短信验证码" class="w-full px-5 py-3.5 bg-gray-100/80 rounded-2xl focus:bg-white transition-all outline-none text-[14px] border-2 border-transparent focus:border-black/5" />

        <div v-if="role === 'enterprise'" class="space-y-4 animate-in fade-in slide-in-from-top-2 duration-300">
            <input type="text" v-model="form.enterpriseName" placeholder="企业全称 (需与营业执照一致)" class="w-full px-5 py-3.5 bg-gray-100/80 rounded-2xl focus:bg-white transition-all outline-none text-[14px] border-2 border-transparent focus:border-black/5" />
            <div class="grid grid-cols-2 gap-4">
            <select v-model="form.enterpriseType" class="px-5 py-3.5 bg-gray-100/80 rounded-2xl focus:bg-white transition-all outline-none text-[14px] text-gray-500 border-2 border-transparent">
                <option value="">企业规模</option>
                <option value="small">20人以下</option>
                <option value="medium">20-99人</option>
                <option value="large">100人以上</option>
            </select>
            <input type="text" v-model="form.industry" placeholder="所属行业" class="px-5 py-3.5 bg-gray-100/80 rounded-2xl focus:bg-white transition-all outline-none text-[14px] border-2 border-transparent focus:border-black/5" />
            </div>
        </div>

        <div class="space-y-4">
            <input type="password" v-model="form.password" placeholder="设置登录密码" class="w-full px-5 py-3.5 bg-gray-100/80 rounded-2xl focus:bg-white transition-all outline-none text-[14px] border-2 border-transparent focus:border-black/5" />
            <input type="password" v-model="form.confirmPassword" placeholder="再次确认密码" class="w-full px-5 py-3.5 bg-gray-100/80 rounded-2xl focus:bg-white transition-all outline-none text-[14px] border-2 border-transparent focus:border-black/5" />
            <p v-if="form.confirmPassword && form.password !== form.confirmPassword" class="text-red-400 text-[12px] ml-2">两次输入的密码不一致</p>
        </div>
        </div>

        <div class="mt-6 flex items-start gap-2 ml-1">
        <input type="checkbox" v-model="form.agree" class="mt-1 w-4 h-4 accent-black" />
        <label class="text-[12px] text-gray-400 leading-relaxed">
            我已阅读并同意 <span class="text-black font-medium underline">用户服务协议</span> 和 <span class="text-black font-medium underline">隐私政策</span>
        </label>
        </div>

        <button 
        @click="submitRegister"
        :disabled="!form.agree"
        class="w-full mt-8 py-4 bg-black text-white rounded-[20px] text-[15px] font-bold hover:bg-gray-800 active:scale-[0.98] transition-all shadow-xl shadow-black/10 disabled:bg-gray-300"
        >
        完成注册
        </button>

        <div class="mt-8 text-center text-sm text-gray-400">
        已有账户？ <router-link to="/login" class="text-black font-bold hover:underline">立即登录</router-link>
        </div>
    </div>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'

const role = ref<'candidate' | 'enterprise'>('candidate')

const form = reactive({
    username: '',
    realName: '',
    phone: '',
    code: '',
    password: '',
    confirmPassword: '',
    enterpriseName: '',
    enterpriseType: '',
    industry: '',
    agree: false
})

const submitRegister = () => {
    if (form.password !== form.confirmPassword) {
    alert('密码不一致')
    return
    }
    console.log('提交注册数据:', form)
    alert('注册请求已发送，请等待后台审核')
}
</script>

<style scoped>
/* 隐藏滚动条但保留滚动功能 */
.no-scrollbar::-webkit-scrollbar {
    display: none;
}
.no-scrollbar {
    -ms-overflow-style: none;
    scrollbar-width: none;
}
</style>