<template>
    <div class="h-full w-full flex items-center justify-center relative overflow-hidden bg-gray-50/50 py-10">
    <div class="absolute top-[-10%] left-[-5%] w-[600px] h-[600px] bg-purple-100/40 blur-[120px] rounded-full pointer-events-none"></div>
    <div class="absolute bottom-[-10%] right-[-5%] w-[500px] h-[500px] bg-blue-100/30 blur-[120px] rounded-full pointer-events-none"></div>

    <div class="relative z-10 w-[95%] max-w-[450px] bg-white/80 backdrop-blur-3xl p-10 rounded-[40px] shadow-2xl shadow-black/5 border border-white overflow-y-auto max-h-[90vh] no-scrollbar">
        
        <div class="flex flex-col items-center mb-8">
        <h1 class="text-3xl font-bold text-black tracking-tight text-center">加入 KYC 智库</h1>
        <p class="text-gray-400 mt-2 text-[14px] text-center">构建您的数字人才能力图谱</p>
        </div>

        <div class="space-y-1 mb-6">
        <div class="flex p-1.5 bg-gray-200/50 rounded-2xl">
            <button 
            @click="role = 'candidate'"
            :class="role === 'candidate' ? 'bg-white text-black shadow-sm' : 'text-gray-500 hover:text-black'"
            class="flex-1 py-3 text-[14px] font-semibold rounded-xl transition-all duration-300"
            >
            新秀求职者
            </button>
            <button 
            @click="role = 'enterprise'"
            :class="role === 'enterprise' ? 'bg-white text-black shadow-sm' : 'text-gray-500 hover:text-black'"
            class="flex-1 py-3 text-[14px] font-semibold rounded-xl transition-all duration-300"
            >
            合作企业
            </button>
        </div>
        </div>

        <div v-if="role === 'candidate'" class="flex justify-center space-x-6 mb-6 text-[13px]">
        <button 
            @click="registerMethod = 'phone'"
            :class="registerMethod === 'phone' ? 'text-black font-bold border-b-2 border-black pb-1' : 'text-gray-400'"
            class="transition-all"
        >
            手机号注册
        </button>
        <button 
            @click="registerMethod = 'email'"
            :class="registerMethod === 'email' ? 'text-black font-bold border-b-2 border-black pb-1' : 'text-gray-400'"
            class="transition-all"
        >
            邮箱注册
        </button>
        </div>

        <form @submit.prevent="handleRegister">
        
        <div v-if="role === 'candidate'" class="space-y-4 mb-4">
            <div v-if="registerMethod === 'phone'" class="relative">
            <input 
                v-model="form.phone" 
                type="tel" 
                placeholder="请输入中国大陆手机号" 
                class="w-full px-5 py-4 bg-gray-100/80 focus:bg-white border border-transparent focus:border-black rounded-2xl outline-none transition-all text-[15px]"
            />
            </div>

            <div v-else class="relative">
            <input 
                v-model="form.email" 
                type="email" 
                placeholder="请输入企业/个人常用邮箱" 
                class="w-full px-5 py-4 bg-gray-100/80 focus:bg-white border border-transparent focus:border-black rounded-2xl outline-none transition-all text-[15px]"
            />
            </div>
        </div>

        <div v-else class="space-y-4 mb-4 animate-fade-in">
            <div class="relative">
            <input 
                v-model="form.username" 
                type="text" 
                placeholder="企业登录账号 (必填)" 
                class="w-full px-5 py-4 bg-gray-100/80 focus:bg-white border border-transparent focus:border-black rounded-2xl outline-none transition-all text-[15px]"
            />
            </div>

            <div class="relative">
            <input 
                v-model="form.enterpriseName" 
                type="text" 
                placeholder="企业工商全称 (必填)" 
                class="w-full px-5 py-4 bg-gray-100/80 focus:bg-white border border-transparent focus:border-black rounded-2xl outline-none transition-all text-[15px]"
            />
            </div>

            <div class="relative">
            <input 
                v-model="form.legalPerson" 
                type="text" 
                placeholder="企业法人代表姓名 (必填)" 
                class="w-full px-5 py-4 bg-gray-100/80 focus:bg-white border border-transparent focus:border-black rounded-2xl outline-none transition-all text-[15px]"
            />
            </div>

            <div class="relative">
            <input 
                v-model="form.enterpriseCode" 
                type="text" 
                placeholder="统一社会信用代码 (必填)" 
                class="w-full px-5 py-4 bg-gray-100/80 focus:bg-white border border-transparent focus:border-black rounded-2xl outline-none transition-all text-[15px]"
            />
            </div>

            <div class="relative">
            <input 
                v-model="form.contactName" 
                type="text" 
                placeholder="联系人姓名 (必填)" 
                class="w-full px-5 py-4 bg-gray-100/80 focus:bg-white border border-transparent focus:border-black rounded-2xl outline-none transition-all text-[15px]"
            />
            </div>

            <div class="relative">
            <input 
                v-model="form.phone" 
                type="tel" 
                placeholder="联系人手机号码 (必填)" 
                class="w-full px-5 py-4 bg-gray-100/80 focus:bg-white border border-transparent focus:border-black rounded-2xl outline-none transition-all text-[15px]"
            />
            </div>
        </div>

        <div class="space-y-4 mb-6">
            <div class="relative">
            <input 
                v-model="form.password" 
                type="password" 
                placeholder="安全密码 (建议不低于 6 位)" 
                class="w-full px-5 py-4 bg-gray-100/80 focus:bg-white border border-transparent focus:border-black rounded-2xl outline-none transition-all text-[15px]"
                required
            />
            </div>
            <div class="relative">
            <input 
                v-model="form.confirmPassword" 
                type="password" 
                placeholder="重复输入安全密码以确认" 
                class="w-full px-5 py-4 bg-gray-100/80 focus:bg-white border border-transparent focus:border-black rounded-2xl outline-none transition-all text-[15px]"
                required
            />
            </div>
        </div>

        <div class="flex items-start space-x-2.5 px-1 text-[13px] text-gray-400 mb-6 cursor-pointer">
            <input type="checkbox" id="agree" class="mt-0.5 rounded border-gray-300 text-black focus:ring-black" required />
            <label for="agree" class="leading-relaxed">
            我已阅读并同意 <a href="#" class="text-black hover:underline font-medium">《用户使用服务协议》</a> 与 <a href="#" class="text-black hover:underline font-medium">《隐私保护政策》</a>
            </label>
        </div>

        <button 
            type="submit" 
            :disabled="isSubmitting"
            class="w-full py-4 bg-black hover:bg-gray-900 active:scale-[0.98] text-white font-semibold rounded-2xl transition-all shadow-lg shadow-black/10 flex items-center justify-center space-x-2 disabled:opacity-70 disabled:pointer-events-none text-[15px]"
        >
            <span v-if="isSubmitting" class="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin"></span>
            <span>{{ isSubmitting ? '正在构建安全凭证...' : '立即注册并加入' }}</span>
        </button>
        </form>

        <div class="mt-8 text-center text-[14px] text-gray-400">
        已有账号？
        <router-link to="/login" class="text-black font-semibold hover:underline">返回登录</router-link>
        </div>
    </div>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { registerCandidate, registerEnterprise } from '@/api/auth'
import { toast } from '@/utils/toast'

const router = useRouter()
const role = ref<'candidate' | 'enterprise'>('candidate')
const registerMethod = ref<'phone' | 'email'>('phone')
const isSubmitting = ref(false)

const form = reactive({
    phone: '',
    email: '',
    password: '',
    confirmPassword: '',
    // 🏢 企业专属注册数据
    username: '',
    enterpriseName: '',
    enterpriseCode: '',
    contactName: '',
    legalPerson: '' // 🎯 已添加法人字段映射
})

const handleRegister = async () => {
    if (form.password !== form.confirmPassword) {
    toast.error('两次密码输入不一致')
    return
    }

    isSubmitting.value = true
    
    try {
    if (role.value === 'candidate') {
        // 个人端注册
        if (registerMethod.value === 'phone') {
        form.email = ''
        if (!form.phone || !form.password) {
            toast.warning('请完整填写手机号和密码')
            isSubmitting.value = false
            return
        }
        } else {
        form.phone = ''
        if (!form.email || !form.password) {
            toast.warning('请完整填写邮箱和密码')
            isSubmitting.value = false
            return
        }
        }

        await registerCandidate({
        phone: form.phone || undefined,
        email: form.email || undefined,
        password: form.password
        })

        toast.success('个人求职账号注册成功！')
        router.push('/login')
    } else {
        // 企业端注册 - 对应达梦数据库的 NOT NULL 强校验进行前端预校验
        if (!form.username || !form.password) {
        toast.warning('请设定企业登录账号和登录密码')
        isSubmitting.value = false
        return
        }
        if (!form.enterpriseName) {
        toast.warning('请填写企业工商全称')
        isSubmitting.value = false
        return
        }
        if (!form.legalPerson) {
        toast.warning('请填写企业法人代表姓名')
        isSubmitting.value = false
        return
        }
        if (!form.enterpriseCode) {
        toast.warning('请填写统一社会信用代码')
        isSubmitting.value = false
        return
        }
        if (!form.contactName) {
        toast.warning('请填写联系人姓名')
        isSubmitting.value = false
        return
        }
        if (!form.phone) {
        toast.warning('请填写联系人手机号码')
        isSubmitting.value = false
        return
        }

        // 发起请求
        await registerEnterprise({
        username: form.username,
        password: form.password,
        enterpriseName: form.enterpriseName,
        legalPerson: form.legalPerson, // 🎯 成功映射并传输
        enterpriseCode: form.enterpriseCode,
        contactName: form.contactName,
        contactPhone: form.phone,
        email: form.email || undefined
        })

        toast.success('企业注册申请提交成功，请联系管理员审核！')
        router.push('/login')
    }
    } catch (error: any) {
    toast.error(error.message || '注册失败，请稍后重试')
    } finally {
    isSubmitting.value = false
    }
}
</script>

<style scoped>
.no-scrollbar::-webkit-scrollbar {
    display: none;
}
.no-scrollbar {
    -ms-overflow-style: none;
    scrollbar-width: none;
}

.animate-fade-in {
    animation: fadeIn 0.4s cubic-bezier(0.16, 1, 0.3, 1) forwards;
}

@keyframes fadeIn {
    from {
    opacity: 0;
    transform: translateY(8px);
    }
    to {
    opacity: 1;
    transform: translateY(0);
    }
}

input:focus {
    box-shadow: 0 0 0 4px rgba(0, 0, 0, 0.03);
}
</style>