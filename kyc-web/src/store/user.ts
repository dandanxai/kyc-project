// store/user.ts
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { getCandidateProfile , getEnterpriseProfile} from '@/api/auth'

export const useUserStore = defineStore('user', () => {
    const id = ref<number | null>(null)
    const username = ref('')
    const nickname = ref('')
    const avatar = ref('')
    const phone = ref('')
    const email = ref('')
    const jobStatus = ref('在校-寻找实习')
    
    // 🎯 新增核心状态：当前用户的角色类型
    const role = ref<'candidate' | 'enterprise' | ''>('')
    // 企业特有字段
    const enterpriseName = ref('')
    const contactName = ref('')
    const enterpriseStatus = ref<number | null>(null) // 企业审核状态

    // 🎯 是否已加载完信息的标记
    const hasLoaded = ref(false)

    // 计算属性兼容：如果是企业就显示企业名，否则显示昵称
    const userNickname = computed(() => {
        if (role.value === 'enterprise') {
            return enterpriseName.value || username.value || '企业用户'
        }
        return nickname.value || username.value || '求职者'
    })

    const userAvatar = computed(() => {
        if (avatar.value) return avatar.value
        if (role.value === 'enterprise') {
            return 'https://api.dicebear.com/7.x/initials/svg?seed=' + (enterpriseName.value || 'EP')
        }
        return 'https://api.dicebear.com/7.x/avataaars/svg?seed=Felix'
    })

    // 🎯 核心 Action：根据登录时存入的角色，免 ID 拉取对应信息并写入 Pinia
    const fetchUserProfile = async () => {
        if (hasLoaded.value) return

        // 🎯 关键：从 LocalStorage 获取登录时存下的角色标识
        const currentRole = localStorage.getItem('user_role') || 'candidate'

        console.log(currentRole);
        

        try {
            if (currentRole === 'enterprise') {
                // 🏢 走企业端拉取逻辑
                const res = await getEnterpriseProfile()
                if (res && res.data) {
                    // 🎯 增加防空保护
                    id.value = res.data.id || null
                    username.value = res.data.username || ''
                    enterpriseName.value = res.data.enterpriseName || ''
                    contactName.value = res.data.contactName || ''
                    phone.value = res.data.contactPhone || ''
                    email.value = res.data.email || ''
                    role.value = 'enterprise'
                    enterpriseStatus.value = res.data.status !== undefined ? res.data.status : null 
                    
                    hasLoaded.value = true
                }
            } else {
                // 🎓 走求职者（个人）端拉取逻辑
                const res = await getCandidateProfile()
                if (res && res.data) {
                    id.value = res.data.id
                    username.value = res.data.username || ''
                    nickname.value = res.data.nickname || ''
                    avatar.value = res.data.avatar || ''
                    phone.value = res.data.phone || ''
                    email.value = res.data.email || ''
                    jobStatus.value = res.data.jobStatus || '在校-寻找实习'
                    role.value = 'candidate'
                    
                    hasLoaded.value = true
                }
            }
        } catch (error) {
            console.error('Pinia 状态机拉取用户信息失败:', error)
            throw error 
        }
    }

    // 安全退出，清除所有状态
    const clearUserInfo = () => {
        id.value = null
        username.value = ''
        nickname.value = ''
        avatar.value = ''
        phone.value = ''
        email.value = ''
        jobStatus.value = '在校-寻找实习'
        role.value = ''
        enterpriseName.value = ''
        contactName.value = ''
        enterpriseStatus.value = null
        hasLoaded.value = false
        
        // 清理本地存储
        localStorage.removeItem('auth_token')
        localStorage.removeItem('user_role')
    }

    return {
        id,
        username,
        nickname,
        avatar,
        phone,
        email,
        jobStatus,
        role,
        enterpriseName,
        contactName,
        enterpriseStatus,
        hasLoaded,
        userNickname,
        userAvatar,
        fetchUserProfile,
        clearUserInfo
    }
})