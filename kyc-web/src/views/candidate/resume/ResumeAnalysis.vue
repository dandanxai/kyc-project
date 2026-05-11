<template>
<div class="h-full bg-black rounded-[40px] p-8 flex flex-col shadow-2xl relative overflow-hidden text-white select-none">
    <div class="absolute top-0 right-0 w-80 h-80 bg-blue-600/10 blur-[120px] rounded-full pointer-events-none"></div>
    <div class="absolute bottom-0 left-0 w-80 h-80 bg-purple-600/10 blur-[120px] rounded-full pointer-events-none"></div>

    <div v-if="loading" class="flex-1 flex flex-col items-center justify-center relative z-10 animate-fade-in">
        <div class="relative w-24 h-24 mb-6">
            <div class="absolute inset-0 rounded-full border border-dashed border-blue-500/30 animate-spin" style="animation-duration: 8s"></div>
            <div class="absolute inset-2 rounded-full border-2 border-transparent border-t-blue-500 border-r-purple-500 animate-spin" style="animation-duration: 1.5s"></div>
            <div class="absolute inset-6 rounded-full bg-blue-600/20 flex items-center justify-center backdrop-blur-md">
                <div class="w-3 h-3 rounded-full bg-blue-400 animate-ping"></div>
            </div>
        </div>
        <h4 class="text-lg font-black tracking-widest text-transparent bg-clip-text bg-gradient-to-r from-blue-400 to-purple-400 animate-pulse uppercase">
            AI 正在解析技术画像...
        </h4>
        <p class="text-[10px] text-gray-500 font-bold uppercase tracking-[0.2em] mt-2">
            Connecting to Qwen Big Model & DM8 DB
        </p>
    </div>

    <div v-else-if="analysisData" class="relative z-10 flex flex-col h-full animate-in fade-in slide-in-from-bottom-4 duration-500">
        
        <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 mb-6 shrink-0 bg-white/[0.02] border border-white/5 rounded-[24px] p-5 backdrop-blur-xl">
            <div class="flex items-center gap-4">
                <div class="w-12 h-12 rounded-2xl bg-gradient-to-br from-blue-500 to-purple-600 flex items-center justify-center text-xl shadow-lg shadow-blue-500/20">
                    👤
                </div>
                <div>
                    <span class="text-[9px] font-black text-blue-400 uppercase tracking-[0.2em] block">Candidate Profile</span>
                    <h3 class="text-2xl font-black tracking-tight text-white flex items-center gap-2">
                        {{ analysisData.name }}
                        <span v-if="analysisData.education" class="text-[9px] font-bold bg-white/10 px-2.5 py-0.5 rounded-full text-gray-300 border border-white/5">
                            {{ analysisData.education.split('，')[0] }}
                        </span>
                    </h3>
                </div>
            </div>

            <div class="flex bg-white/[0.04] border border-white/5 p-1 rounded-xl shrink-0 self-stretch sm:self-auto z-20">
                <button 
                    @click="activeTab = 'metrics'"
                    :class="[
                        'flex-1 sm:flex-none px-4 py-1.5 rounded-lg text-[10px] font-black uppercase tracking-wider transition-all duration-300 cursor-pointer',
                        activeTab === 'metrics' 
                            ? 'bg-white text-black shadow-lg font-black' 
                            : 'text-gray-400 hover:text-white'
                    ]"
                >
                    📝 文本指标
                </button>
                <button 
                    @click="activeTab = 'chart'"
                    :class="[
                        'flex-1 sm:flex-none px-4 py-1.5 rounded-lg text-[10px] font-black uppercase tracking-wider transition-all duration-300 cursor-pointer',
                        activeTab === 'chart' 
                            ? 'bg-white text-black shadow-lg font-black' 
                            : 'text-gray-400 hover:text-white'
                    ]"
                >
                    📊 动态拓扑图
                </button>
            </div>
        </div>

        <div class="flex-1 min-h-0 relative">
            
            <div 
                v-if="activeTab === 'metrics'"
                class="absolute inset-0 overflow-y-auto pr-1.5 custom-scrollbar animate-in fade-in slide-in-from-bottom-2 duration-300"
            >
                <div class="grid grid-cols-12 gap-4 pb-2">
                    
                    <div class="col-span-12 card-item border-t-2 border-t-blue-500/50 bg-gradient-to-b from-blue-950/10 to-transparent">
                        <span class="card-tag">
                            <span class="text-blue-400 mr-1">🛠️</span> EXPERTISE / 核心技术栈
                        </span>
                        <div v-if="Array.isArray(analysisData.expertise)" class="flex flex-wrap gap-1.5 mt-3">
                            <span 
                                v-for="(tech, idx) in analysisData.expertise" 
                                :key="idx"
                                class="text-[10px] font-mono font-bold px-2.5 py-1 rounded-lg bg-white/[0.04] border border-white/5 text-blue-300 hover:bg-blue-500/10 hover:border-blue-500/30 hover:text-white transition-all duration-200 cursor-default"
                            >
                                {{ tech }}
                            </span>
                        </div>
                        <p v-else class="card-content text-blue-100 font-mono mt-2">{{ analysisData.expertise || '暂无明确技术栈' }}</p>
                    </div>

                    <div class="col-span-12 card-item border-t-2 border-t-purple-500/50 bg-gradient-to-b from-purple-950/10 to-transparent">
                        <span class="card-tag">
                            <span class="text-purple-400 mr-1">🏆</span> ARTIFACTS / 代表性工程成果
                        </span>
                        <div v-if="Array.isArray(analysisData.artifacts)" class="grid grid-cols-1 sm:grid-cols-2 gap-2 mt-3">
                            <div 
                                v-for="(art, idx) in analysisData.artifacts" 
                                :key="idx"
                                class="flex items-start gap-2.5 p-3 rounded-xl bg-white/[0.02] border border-white/5 hover:bg-white/[0.04] hover:border-white/10 transition-all duration-300"
                            >
                                <span class="font-mono text-[9px] font-black text-purple-300 bg-purple-500/15 w-5 h-5 rounded-md flex items-center justify-center shrink-0 border border-purple-500/10">
                                    {{ String(idx + 1).padStart(2, '0') }}
                                </span>
                                <span class="text-[11px] font-medium text-gray-300 leading-relaxed">{{ art }}</span>
                            </div>
                        </div>
                        <p v-else class="card-content text-gray-200 mt-2">{{ analysisData.artifacts || '暂无明确成果' }}</p>
                    </div>

                    <div class="col-span-12 md:col-span-6 card-item border-l-2 border-l-blue-400/80 flex flex-col justify-between">
                        <div>
                            <span class="card-tag">
                                <span class="text-blue-400 mr-1">📊</span> DM SQL / 达梦适配
                            </span>
                            <p 
                                class="card-content mt-2.5"
                                :class="isNotMentioned(analysisData.dmSql) ? 'text-gray-500 italic font-normal text-[11px]' : 'text-gray-200'"
                            >
                                <span v-if="isNotMentioned(analysisData.dmSql)" class="mr-1">⚠️</span>
                                {{ analysisData.dmSql }}
                            </p>
                        </div>
                        <div v-if="!isNotMentioned(analysisData.dmSql)" class="mt-4 flex justify-end">
                            <span class="text-[8px] font-mono font-bold bg-blue-500/10 text-blue-400 px-1.5 py-0.5 rounded border border-blue-500/10 uppercase tracking-widest">DM8 Compatible</span>
                        </div>
                    </div>

                    <div class="col-span-12 md:col-span-6 card-item border-l-2 border-l-purple-400/80 flex flex-col justify-between">
                        <div>
                            <span class="card-tag">
                                <span class="text-purple-400 mr-1">🤖</span> LLM AI / 大模型与 RAG
                            </span>
                            <p 
                                class="card-content mt-2.5"
                                :class="isNotMentioned(analysisData.llmAi) ? 'text-gray-500 italic font-normal text-[11px]' : 'text-gray-200'"
                            >
                                <span v-if="isNotMentioned(analysisData.llmAi)" class="mr-1">⚠️</span>
                                {{ analysisData.llmAi }}
                            </p>
                        </div>
                        <div v-if="!isNotMentioned(analysisData.llmAi)" class="mt-4 flex justify-end">
                            <span class="text-[8px] font-mono font-bold bg-purple-500/10 text-purple-400 px-1.5 py-0.5 rounded border border-purple-500/10 uppercase tracking-widest">LLM Agent</span>
                        </div>
                    </div>

                    <div class="col-span-12 card-item flex items-center justify-between gap-4">
                        <div class="flex-1 min-w-0">
                            <span class="card-tag">
                                <span class="text-emerald-400 mr-1">🌐</span> LANGUAGE / 外语及国际化
                            </span>
                            <p 
                                class="card-content mt-2"
                                :class="isNotMentioned(analysisData.language) ? 'text-gray-500 italic font-normal text-[11px]' : 'text-gray-200'"
                            >
                                <span v-if="isNotMentioned(analysisData.language)" class="mr-1">⚠️</span>
                                {{ analysisData.language }}
                            </p>
                        </div>
                        <div v-if="!isNotMentioned(analysisData.language)" class="shrink-0 w-10 h-10 rounded-xl bg-white/5 border border-white/10 flex items-center justify-center text-lg">
                            🌍
                        </div>
                    </div>

                </div>
            </div>

            <div 
                v-else
                class="absolute inset-0 rounded-[32px] border border-white/5 bg-white/[0.01] backdrop-blur-md overflow-hidden flex flex-col"
            >
                <iframe
                    ref="chartIframe"
                    src="/competency_chart.html"
                    class="w-full h-full border-0 bg-transparent"
                    @load="sendDataToIframe"
                ></iframe>
            </div>

        </div>
    </div>

    <div v-else class="h-full flex flex-col items-center justify-center text-center">
        <div class="w-16 h-16 bg-white/5 rounded-full flex items-center justify-center mb-4 border border-white/5 shadow-inner">
            <span class="text-2xl text-gray-500">📄</span>
        </div>
        <p class="text-sm font-bold text-gray-400">未选择或未检测到可用画像记录</p>
        <p class="text-[10px] text-gray-600 tracking-widest uppercase mt-1">Please select a valid resume list</p>
    </div>
</div>
</template>

<script setup lang="ts">
import { ref, watch, nextTick } from 'vue'

const props = defineProps<{
    analysisData: any // 🎯 这里的 props 传入的就是 Java 返回的 CommonResult.success(response.getBody())
    loading?: boolean
}>()

const activeTab = ref<'metrics' | 'chart'>('metrics')
const chartIframe = ref<HTMLIFrameElement | null>(null)

// 辅助方法：判断内容是否未提及
const isNotMentioned = (val: string) => {
    return !val || val === '未在简历中提及' || val.includes('未提及')
}

/**
 * 🎯 信使逻辑：将 Java 返回的实时 AI 结构化 JSON 解析并投递给 iframe 渲染
 */
const sendDataToIframe = () => {
    if (!chartIframe.value || !props.analysisData) return
    
    const iframeWin = chartIframe.value.contentWindow
    if (!iframeWin) return

    const rawData = props.analysisData

    // 🎯 动态映射：将后端返回的真实数据映射成 Python 脚本所需的技能分类与图标结构
    const formattedProfile = {
        name: rawData.name || "候选人",
        avatar: "https://img.icons8.com/clouds/100/000000/user.png",
        categories: {
            "核心技术栈": (rawData.expertise || []).map((skill: string) => {
                // 根据常见技术动态配图标，没配上的用默认代码图标
                let icon = "https://img.icons8.com/color/48/code.png";
                const s = skill.toLowerCase();
                if (s.includes('java')) icon = "https://img.icons8.com/color/48/java-properties.png";
                else if (s.includes('spring')) icon = "https://img.icons8.com/color/48/spring-logo.png";
                else if (s.includes('vue')) icon = "https://img.icons8.com/color/48/vue-js.png";
                else if (s.includes('python')) icon = "https://img.icons8.com/color/48/python--v1.png";
                else if (s.includes('three')) icon = "https://img.icons8.com/color/48/3d-model.png";
                
                return { skill, level: "熟练", icon }
            }),
            "代表性工程成果": (rawData.artifacts || []).slice(0, 4).map((art: string) => ({
                skill: art.length > 11 ? art.substring(0, 11) + '...' : art,
                level: "项目成果",
                icon: "https://img.icons8.com/color/48/checked-laptop.png"
            })),
            "数据库/信创适配": isNotMentioned(rawData.dmSql) ? [] : [
                { skill: "达梦 DM8", level: "兼容适配", icon: "https://img.icons8.com/ios-filled/50/3498db/database.png" }
            ],
            "语言与国际化": isNotMentioned(rawData.language) ? [] : [
                { skill: rawData.language.length > 8 ? "国际化适配" : rawData.language, level: "熟练", icon: "https://img.icons8.com/color/48/globe--v1.png" }
            ]
        }
    }

    // 🚀 向 iframe 沙盒安全投递实时数据
    iframeWin.postMessage({
        type: 'RENDER_COMPETENCY_DATA',
        data: formattedProfile
    }, '*')
}

// 监听数据联动，如果简历发生切换，且正好在图表 Tab，需要重新投递渲染
watch(() => props.analysisData, () => {
    if (activeTab.value === 'chart') {
        nextTick(() => {
            sendDataToIframe()
        })
    }
}, { deep: true })

// 监听 Tab 切换
watch(activeTab, (newTab) => {
    if (newTab === 'chart') {
        nextTick(() => {
            sendDataToIframe()
        })
    }
})
</script>

<style scoped>
/* Bento 网格卡片高奢样式 */
.card-item {
    background-color: rgba(255, 255, 255, 0.02);
    border: 1px solid rgba(255, 255, 255, 0.04);
    padding: 1.25rem;
    border-radius: 24px;
    transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
    box-shadow: inset 0 1px 1px rgba(255, 255, 255, 0.02);
}

/* 3D Depth 悬停反馈 */
.card-item:hover {
    background-color: rgba(255, 255, 255, 0.04);
    border-color: rgba(255, 255, 255, 0.08);
    transform: translateY(-2px);
    box-shadow: 
        inset 0 1px 1px rgba(255, 255, 255, 0.05),
        0 10px 30px -10px rgba(0, 0, 0, 0.5);
}

.card-tag {
    display: flex;
    align-items: center;
    font-size: 9px;
    font-weight: 900;
    color: rgba(255, 255, 255, 0.4);
    letter-spacing: 0.12em;
    text-transform: uppercase;
}

.card-content {
    font-size: 12px;
    line-height: 1.6;
    font-weight: 500;
}

/* 优雅的小细滚动条 */
.custom-scrollbar::-webkit-scrollbar {
    width: 4px;
}
.custom-scrollbar::-webkit-scrollbar-track {
    background: transparent;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
    background: rgba(255, 255, 255, 0.08);
    border-radius: 99px;
}
.custom-scrollbar::-webkit-scrollbar-thumb:hover {
    background: rgba(255, 255, 255, 0.2);
}
</style>