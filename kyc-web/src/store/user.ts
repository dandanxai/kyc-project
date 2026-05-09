// store/user.ts
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { getCandidateProfile } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
    const id = ref<number | null>(null)
    const username = ref('')
    const nickname = ref('')
    const avatar = ref('')
    const phone = ref('')
    const email = ref('')
    const jobStatus = ref('在校-寻找实习')

    // 🎯 核心：是否已加载完信息的标记
    const hasLoaded = ref(false)

    const userNickname = computed(() => nickname.value || username.value || '求职者')
    const userAvatar = computed(() => avatar.value || 'https://api.dicebear.com/7.x/avataaars/svg?seed=Felix')

    // 🎯 核心 Action：免 ID 拉取个人信息并写入 Pinia
    const fetchUserProfile = async () => {
        if (hasLoaded.value) return

        try {
            const res = await getCandidateProfile()
            if (res && res.data) {
                id.value = res.data.id
                username.value = res.data.username || ''
                nickname.value = res.data.nickname || ''
                avatar.value = res.data.avatar || ''
                phone.value = res.data.phone || ''
                email.value = res.data.email || ''
                jobStatus.value = res.data.jobStatus || '在校-寻找实习'
                
                hasLoaded.value = true // ⭐ 标记已成功加载
                
            }
        } catch (error) {
            console.error('Pinia 状态机拉取用户信息失败:', error)
            throw error 
        }
    }

    // 安全退出，清除状态
    const clearUserInfo = () => {
        id.value = null
        username.value = ''
        nickname.value = ''
        avatar.value = ''
        phone.value = ''
        email.value = ''
        jobStatus.value = '在校-寻找实习'
        hasLoaded.value = false
    }

    return {
        id,
        username,
        nickname,
        avatar,
        phone,
        email,
        jobStatus,
        hasLoaded,
        userNickname,
        userAvatar,
        fetchUserProfile,
        clearUserInfo
    }
})