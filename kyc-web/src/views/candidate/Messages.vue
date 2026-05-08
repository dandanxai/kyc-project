<template>
    <div class="h-full w-full flex flex-col p-6 bg-[#F5F5F7] overflow-hidden font-sans relative">
    
    <header class="flex-shrink-0 mb-4 z-10">
        <div class="inline-flex items-center gap-1.5 px-3 py-1 bg-black/[0.04] rounded-full mb-1">
        <span class="w-1.5 h-1.5 bg-indigo-600 rounded-full animate-pulse"></span>
        <span class="text-[10px] font-bold tracking-widest uppercase text-gray-500">Kirin Message-Link v5.1</span>
        </div>
        <h2 class="text-2xl font-black text-black tracking-tight">即时互联中心</h2>
    </header>

    <div class="flex-1 min-h-0 w-full bg-white rounded-[24px] shadow-sm border border-gray-100 flex flex-row overflow-hidden relative">
        
        <div class="flex-[1_1_25%] min-w-[240px] max-w-[300px] border-r border-gray-100 flex flex-col h-full bg-white relative z-20">
        <div class="p-4 flex-shrink-0">
            <div class="relative flex items-center">
            <span class="absolute left-3.5 text-gray-400 text-xs">🔍</span>
            <input 
                v-model="searchQuery"
                type="text" 
                placeholder="搜索联系人..." 
                class="w-full pl-9 pr-3 py-2 bg-[#F5F5F7] rounded-xl text-xs font-semibold text-black focus:outline-none"
            />
            </div>
        </div>

        <div class="px-4 pb-2 flex-shrink-0">
            <div class="flex bg-[#F5F5F7] p-0.5 rounded-lg">
            <button 
                @click="activeCategory = 'applied'"
                :class="[activeCategory === 'applied' ? 'bg-white text-black shadow-sm' : 'text-gray-400']"
                class="flex-1 py-1 text-[10px] font-black rounded-md transition-all"
            >
                📥 投递反馈
            </button>
            <button 
                @click="activeCategory = 'inbound'"
                :class="[activeCategory === 'inbound' ? 'bg-white text-black shadow-sm' : 'text-gray-400']"
                class="flex-1 py-1 text-[10px] font-black rounded-md transition-all"
            >
                💼 企业邀约
            </button>
            </div>
        </div>

        <div class="flex-1 overflow-y-auto no-scrollbar px-2 space-y-0.5 pb-4">
            <div 
            v-for="contact in filteredContacts" 
            :key="contact.id"
            @click="selectContact(contact)"
            :class="[activeContact && activeContact.id === contact.id ? 'bg-[#F5F5F7]' : 'hover:bg-gray-50']"
            class="flex items-center justify-between p-3 rounded-xl cursor-pointer transition-all"
            >
            <div class="flex items-center gap-2.5 min-w-0">
                <div class="w-8 h-8 bg-black text-white rounded-lg text-xs font-black flex items-center justify-center flex-shrink-0">
                {{ contact.avatar }}
                </div>
                <div class="min-w-0">
                <p class="text-xs font-black text-black truncate">{{ contact.name.split(' · ')[0] }}</p>
                <p class="text-[10px] text-gray-400 truncate">{{ contact.lastMessage }}</p>
                </div>
            </div>
            <span class="text-[8px] font-mono text-gray-400 flex-shrink-0 pl-1">{{ contact.time }}</span>
            </div>
        </div>
        </div>

        <div v-if="!activeContact || !activeContact.id" class="flex-1 flex flex-col items-center justify-center bg-[#FAFAFC]/50 text-gray-400 gap-2">
        <span class="text-2xl">💬</span>
        <p class="text-xs font-bold">请在左侧选择一个对话开启交流</p>
        </div>

        <template v-else>
        <div class="flex-[2_2_50%] min-w-[380px] flex flex-col h-full bg-[#FAFAFC]/50 border-r border-gray-100 overflow-hidden">
            <div class="h-14 px-6 border-b border-gray-100 flex items-center bg-white/80 backdrop-blur-md flex-shrink-0">
            <div class="flex items-center gap-2">
                <span class="w-5 h-5 bg-black rounded-md text-[10px] flex items-center justify-center font-bold text-white">{{ activeContact.avatar }}</span>
                <span class="text-xs font-black text-black">{{ activeContact.name }}</span>
            </div>
            </div>

            <div ref="messageContainer" class="flex-1 overflow-y-auto no-scrollbar p-5 space-y-4 bg-white/30">
            <div 
                v-for="msg in activeContact.chatHistory" 
                :key="msg.id"
                :class="[msg.sender === 'me' ? 'justify-end' : 'justify-start']"
                class="flex items-end gap-2"
            >
                <div class="max-w-[75%] flex flex-col" :class="[msg.sender === 'me' ? 'items-end' : 'items-start']">
                <div 
                    :class="[
                    msg.sender === 'me' 
                        ? 'bg-black text-white rounded-[14px_14px_4px_14px]' 
                        : 'bg-white text-gray-800 rounded-[14px_14px_14px_4px] border border-gray-100'
                    ]"
                    class="px-3.5 py-2.5 text-xs font-semibold leading-relaxed break-all"
                >
                    {{ msg.content }}
                </div>
                <span class="text-[8px] font-mono text-gray-400 mt-1 px-1">{{ msg.time }}</span>
                </div>
            </div>
            </div>

            <div class="p-4 bg-white border-t border-gray-100 flex-shrink-0">
            <div class="flex items-center gap-2 bg-[#F5F5F7] rounded-xl p-1.5">
                <input 
                v-model="newMessage" 
                @keyup.enter="sendMessage"
                type="text" 
                placeholder="键入您的回复..." 
                class="flex-1 bg-transparent px-2.5 text-xs text-black font-semibold focus:outline-none"
                />
                <button 
                @click="sendMessage"
                class="w-7 h-7 bg-black text-white rounded-lg flex items-center justify-center hover:scale-105 active:scale-95 transition-all shadow-sm"
                >
                ➔
                </button>
            </div>
            </div>
        </div>

        <div class="flex-[1_1_25%] min-w-[240px] max-w-[320px] bg-white flex flex-col h-full overflow-hidden">
            <div class="h-14 px-5 border-b border-gray-100 flex items-center justify-between flex-shrink-0">
            <div class="flex items-center gap-1.5">
                <span class="text-[8px] bg-indigo-600 text-white px-1.5 py-0.5 rounded font-black">COPILOT v5.1</span>
                <p class="text-xs font-black text-black">AI 沟通智囊</p>
            </div>
            </div>

            <div class="flex-1 overflow-y-auto no-scrollbar p-4 space-y-4">
            <div class="bg-indigo-50/20 p-3.5 rounded-xl border border-indigo-100/10">
                <div class="flex items-center gap-1.5 mb-2">
                <span class="text-xs">🗣️</span>
                <span class="text-[9px] font-black text-indigo-600 uppercase tracking-wider">对话研判</span>
                </div>
                <p class="text-[10px] text-gray-600 leading-relaxed font-semibold">
                {{ activeContact.aiIntent }}
                </p>
            </div>

            <div class="bg-purple-50/20 p-3.5 rounded-xl border border-purple-100/10 flex flex-col justify-between min-h-[140px]">
                <div>
                <div class="flex items-center gap-1.5 mb-2">
                    <span class="text-xs">🚀</span>
                    <span class="text-[9px] font-black text-purple-600 uppercase tracking-wider">推荐回复草稿</span>
                </div>
                <p class="text-[10px] text-gray-500 leading-relaxed italic font-medium">
                    "{{ activeContact.aiReplyDraft }}"
                </p>
                </div>
                <button 
                @click="applyAiDraft" 
                class="w-full py-2 bg-black text-white text-[9px] font-black rounded-lg transition-all text-center mt-3 active:scale-95"
                >
                一键填充草稿 ➔
                </button>
            </div>

            <div class="bg-gray-50 p-3.5 rounded-xl border border-gray-100">
                <div class="flex items-center gap-1.5 mb-2">
                <span class="text-xs">💡</span>
                <span class="text-[9px] font-black text-gray-600 uppercase tracking-wider">高频考点速递</span>
                </div>
                <ul class="space-y-2.5">
                <li 
                    v-for="(tip, index) in activeContact.mockQuestions" 
                    :key="index"
                    class="text-[9px] text-gray-500 font-semibold leading-relaxed flex items-start gap-1"
                >
                    <span class="text-indigo-600 font-mono font-bold">{{ index + 1 }}.</span>
                    <span>{{ tip }}</span>
                </li>
                </ul>
            </div>
            </div>
        </div>
        </template>

    </div>
    </div>
</template>

<script setup lang="ts">
import { ref, computed, nextTick, onMounted } from 'vue'

const messageContainer = ref<HTMLElement | null>(null)
const newMessage = ref('')
const searchQuery = ref('')
const activeCategory = ref<'applied' | 'inbound'>('applied')

// 保持不变的数据集
const contacts = ref([
    {
    id: 'contact-1',
    category: 'applied',
    name: '林建国 · 麒麟软件研发总监',
    avatar: '麒',
    role: 'Kirin Operating System (南京)',
    lastMessage: '期待你在飞腾/麒麟生态下的内核适配发挥作用。',
    time: '10:24',
    unread: true,
    aiIntent: '对方正处于大型信创国企的架构升级窗口期，迫切需要攻关飞腾系列硬件的内核态高可用调试。你的中国软件杯国家级二等奖直接打中了他们的痛点。',
    aiReplyDraft: '林总您好，感谢回复！我对银河麒麟V10及飞腾架构的驱动与内核态开发非常感兴趣。针对飞腾微处理器架构，我曾研究过ARM64系统寄存器及多核竞争的时序排查。期待能用我底层适配的功底参与到咱们内核工程组中！',
    mockQuestions: [
        '飞腾 ARM64 处理器在多核竞争下的内核时序排查怎么做？',
        '银河麒麟 V10 系统下底层驱动适配与常见兼容性 Bug 调试手段。',
        'Linux 内核态（Kernel Space）与用户态之间高效通信的方法。'
    ],
    chatHistory: [
        { id: 'm1', sender: 'other', content: '你好，我是麒麟软件的林建国。在系统后台看到了你做的内核适配版结构化分析画像，非常惊艳。', time: '10:20' },
        { id: 'm2', sender: 'me', content: '林总您好！非常荣幸能得到您的关注。我个人对底层 Linux Kernel 适配、国产微处理器开发，尤其是银河麒麟生态架构一直保持着深厚的研发兴趣。', time: '10:22' },
        { id: 'm3', sender: 'other', content: '非常不错，我们目前的重点正是针对飞腾微处理器进行内核态兼容调优，这需要极高的系统底层稳定性调试功底。期待你在飞腾/麒麟生态下的内核适配发挥作用。', time: '10:24' }
    ]
    },
    {
    id: 'contact-2',
    category: 'applied',
    name: '周雨航 · 华为昇腾高级专家',
    avatar: '华',
    role: 'Ascend Computing Division (北京)',
    lastMessage: '下周三能约个线上技术交流吗？聊聊DeepSeek在国产算力下的量化。',
    time: '昨天',
    unread: false,
    aiIntent: '华为昇腾生态正在密集推进大语言模型（如 DeepSeek-R1）在本土 NPU 硬件上的零损耗蒸馏与轻量化落地。对方对你“筑理·考工台”项目中的大模型本地化实践与前后端协同优化极感兴趣。',
    aiReplyDraft: '周老师您好！下周三时间我可以安排。对于 DeepSeek 在国产算力下的量化与部署，我曾基于 Ollama 及昇腾工具链尝试过大模型的本地加速，并对 KVCache 交互调优做过实践。非常期待与您交流！',
    mockQuestions: [
        '基于 NPU（如昇腾）的模型轻量化量化（INT8/INT4）实战踩坑点。',
        '在 Web3D 项目中如何通过 SSE 流式接口及缓存机制平抑模型延迟？',
        '如何评估与排查国产算力硬件上的 KVCache 交互瓶颈？'
    ],
    chatHistory: [
        { id: 'm4', sender: 'other', content: '同学你好，关注到你参与 of AI 算法大赛以及你主导的 Web3D 与大模型交互项目（筑理·考工台）。在国产昇腾硬件环境下，你们做模型本地轻量部署时遇到过延迟瓶颈吗？', time: '昨天 15:30' },
        { id: 'm5', sender: 'me', content: '周老师您好！确实遇到过。当时我们主要通过 SSE 流式传输、在本地大模型中引入特定 KV Cache 策略，并配合前端轻量解算来尽可能对齐推理高延迟，整体体验有了明显提升。', time: '昨天 15:45' },
        { id: 'm6', sender: 'other', content: '非常有实操价值的思路。下周三能约个线上技术交流吗？聊聊DeepSeek在国产算力下的量化。', time: '昨天 16:00' }
    ]
    },
    {
    id: 'contact-3',
    category: 'inbound',
    name: '刘经理 · 达梦数据库招聘官',
    avatar: '达',
    role: 'DM Database (武汉)',
    lastMessage: '同学你好，对我们到达梦进行适配重构的全栈岗感兴趣吗？',
    time: '2天前',
    unread: false,
    aiIntent: '达梦数据库正在大力推广与 Spring Boot 生态的深度集成及在信创全栈环境下的微服务兼容性升级。他们对你具备 Java (Spring Boot) 以及 Vue 全栈技术栈非常看重。',
    aiReplyDraft: '刘经理您好！我做过不少 Spring Boot 架构的项目，并对多数据源切换、国产数据库（如达梦 DM8）的语法兼容及大写敏感配置有深入研究，非常期待能到达梦参与基础重构！',
    mockQuestions: [
        'SpringBoot 接入达梦数据库时的 Schema 大小写敏感核心排坑。',
        '大并发场景下，如何优化国产关系型数据库连接池的抖动与重连机制？',
        '微服务环境下如何实现 MySQL 与 达梦数据库 的平滑无损迁移？'
    ],
    chatHistory: [
        { id: 'm7', sender: 'other', content: '同学你好，关注到你的全栈开发背景，对我们到达梦进行适配重构的全栈岗位感兴趣吗？我们这需要对 Java 生态 and 前端都非常熟悉的同学。', time: '2天前' }
    ]
    }
])

const activeContact = ref<any>(null)

const filteredContacts = computed(() => {
    return contacts.value.filter(c => {
    const matchesSearch = c.name.toLowerCase().includes(searchQuery.value.toLowerCase()) || 
                            c.lastMessage.toLowerCase().includes(searchQuery.value.toLowerCase())
    const matchesCategory = c.category === activeCategory.value
    return matchesSearch && matchesCategory
    })
})

const selectContact = (contact: any) => {
    activeContact.value = contact
    contact.unread = false
    scrollToBottom()
}

const applyAiDraft = () => {
    if (activeContact.value) {
    newMessage.value = activeContact.value.aiReplyDraft
    }
}

const sendMessage = () => {
    if (!newMessage.value.trim() || !activeContact.value) return

    activeContact.value.chatHistory.push({
    id: `m-new-${Date.now()}`,
    sender: 'me',
    content: newMessage.value,
    time: getFormattedTime()
    })

    activeContact.value.lastMessage = newMessage.value
    activeContact.value.time = getFormattedTime()

    newMessage.value = ''
    scrollToBottom()

    setTimeout(() => {
    if (!activeContact.value) return
    let replyText = '收到，我们相关技术团队会进一步评估。'
    if (activeContact.value.id === 'contact-1') {
        replyText = '好气魄！针对你提到的多核竞争时序排查，我们组目前刚好有类似的痛点。那我下周一让 HR 给你排一个正式的一面，我们深入聊一聊内核源码！'
    } else if (activeContact.value.id === 'contact-2') {
        replyText = '非常好。那我下周二下午让助教给你发会议链接。我们主要探讨国产计算底座对大模型算子级别的裁剪方案。'
    } else if (activeContact.value.id === 'contact-3') {
        replyText = '非常棒的思路！你的工程基础扎实，那我们尽快推进一个技术初面。'
    }

    activeContact.value.chatHistory.push({
        id: `m-reply-${Date.now()}`,
        sender: 'other',
        content: replyText,
        time: getFormattedTime()
    })
    activeContact.value.lastMessage = replyText.slice(0, 30) + '...'
    activeContact.value.time = getFormattedTime()
    scrollToBottom()
    }, 1500)
}

const getFormattedTime = () => {
    const now = new Date()
    const hours = now.getHours().toString().padStart(2, '0')
    const minutes = now.getMinutes().toString().padStart(2, '0')
    return `${hours}:${minutes}`
}

const scrollToBottom = () => {
    nextTick(() => {
    if (messageContainer.value) {
        messageContainer.value.scrollTo({
        top: messageContainer.value.scrollHeight,
        behavior: 'smooth'
        })
    }
    })
}

onMounted(() => {
    if (contacts.value.length > 0) {
    activeContact.value = contacts.value[0]
    }
    scrollToBottom()
})
</script>

<style scoped>
.no-scrollbar::-webkit-scrollbar { display: none; }
.no-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }
</style>