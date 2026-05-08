<template>
    <div class="dashboard-wrapper">
    <section class="visual-charts-grid">
        <div class="chart-card">
        <div class="chart-header">
            <div class="header-left">
            <span class="icon-pulse">⚡</span>
            <div>
                <h4>本地 AI 算力集群监测</h4>
                <p>Ollama / DeepSeek / 2x RTX 4090</p>
            </div>
            </div>
            <span class="badge-status green">运行中</span>
        </div>
        <div class="chart-body">
            <div class="vram-info">
            <span class="big-num">19.2</span>
            <span class="unit">GB / 48.0 GB 占用 (40%)</span>
            </div>
            <div class="svg-container">
            <svg viewBox="0 0 300 60" class="sparkline">
                <defs>
                <linearGradient id="wave-grad" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="0%" stop-color="#3b82f6" stop-opacity="0.4" />
                    <stop offset="100%" stop-color="#3b82f6" stop-opacity="0.0" />
                </linearGradient>
                </defs>
                <path
                d="M0 50 Q25 15, 50 35 T100 20 T150 45 T200 15 T250 30 T300 25 L300 60 L0 60 Z"
                fill="url(#wave-grad)"
                />
                <path
                d="M0 50 Q25 15, 50 35 T100 20 T150 45 T200 15 T250 30 T300 25"
                fill="none"
                stroke="#3b82f6"
                stroke-width="2"
                stroke-linecap="round"
                />
                <circle cx="250" cy="30" r="4" fill="#3b82f6" class="pulse-point" />
            </svg>
            </div>
            <div class="chart-footer">
            <span>吞吐率: <strong class="text-emerald-500">42 tokens/s</strong></span>
            <span>算力负载: <strong>稳定 (42°C)</strong></span>
            </div>
        </div>
        </div>

        <div class="chart-card flex-row">
        <div class="gauge-left">
            <span class="icon-pulse">🏅</span>
            <h4>信创适配成功率</h4>
            <p class="desc">纯血国产软硬件环境一站式适配达标率</p>
            <div class="segmented-info">
            <div class="seg"><strong>412 家</strong>全网企业</div>
            <div class="seg"><strong>8,192 人</strong>高活人才</div>
            </div>
        </div>
        <div class="gauge-right">
            <div class="circle-gauge">
            <svg viewBox="0 0 100 100">
                <circle cx="50" cy="50" r="40" class="circle-bg" />
                <circle
                cx="50"
                cy="50"
                r="40"
                class="circle-val"
                stroke-dasharray="251.2"
                stroke-dashoffset="29.6"
                />
                <text x="50" y="55" class="gauge-text">88.2%</text>
            </svg>
            </div>
        </div>
        </div>

        <div class="chart-card">
        <div class="chart-header">
            <div class="header-left">
            <span class="icon-pulse">🛡️</span>
            <div>
                <h4>国密级安全审计通道</h4>
                <p>系统操作行为防篡改日志实时链上同步</p>
            </div>
            </div>
            <span class="badge-status red blink">监听中</span>
        </div>
        <div class="terminal-body no-scrollbar">
            <div class="log-item green">
            <span class="time">[08:42:11]</span> SM4 硬件密盾通道握手成功 (Zone B IP: 10.21.3.2)
            </div>
            <div class="log-item blue">
            <span class="time">[08:45:30]</span> AI_MATCH: 简历与信创职位余弦向量对齐完成 (相似度 95%)
            </div>
            <div class="log-item yellow">
            <span class="time">[08:52:14]</span> SECURITY: 个人隐私数据完成单向不可逆国密脱敏
            </div>
        </div>
        </div>
    </section>

    <section class="quick-nav-section">
        <h3 class="section-title">🧭 快速操作导航</h3>
        <div class="quick-nav-grid">
        <div 
            v-for="menu in shortcutMenus" 
            :key="menu.path" 
            @click="navigateTo(menu.path)" 
            class="nav-card"
        >
            <div class="nav-card-icon" :style="{ background: menu.color }">{{ menu.icon }}</div>
            <div class="nav-card-content">
            <h4>{{ menu.title }}</h4>
            <p>{{ menu.desc }}</p>
            </div>
            <div class="nav-card-arrow">→</div>
        </div>
        </div>
    </section>

    <section class="workspace-section">
        <div class="table-title-row">
        <div>
            <h3 class="section-title">👥 平台双角色准入管理</h3>
            <p class="section-desc">快速重置实体盾号或对异常/违规实体执行阻断冻结操作</p>
        </div>
        <div class="filter-actions">
            <input 
            v-model="searchQuery"
            type="text" 
            placeholder="搜索 UID、主体名称..." 
            class="search-input"
            />
        </div>
        </div>

        <div class="table-wrapper">
        <table class="data-table">
            <thead>
            <tr>
                <th>UID</th>
                <th>主体名称</th>
                <th>实体角色</th>
                <th>安全信誉凭证</th>
                <th class="text-right">系统应急管控</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="user in filteredUsers" :key="user.uid">
                <td class="font-mono font-bold col-uid">{{ user.uid }}</td>
                <td class="font-bold col-name">{{ user.name }}</td>
                <td>
                <span :class="user.role" class="role-badge">
                    {{ user.role === 'enterprise' ? '企业机构' : '个人求职' }}
                </span>
                </td>
                <td class="font-mono text-emerald-500 font-bold">{{ user.shield }}</td>
                <td class="text-right action-btns">
                <button @click="resetShield(user)" class="btn-action">重置盾号</button>
                <button @click="toggleStatus(user)" class="btn-action danger">冻结</button>
                </td>
            </tr>
            </tbody>
        </table>
        </div>
    </section>
    </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const searchQuery = ref('')

// 快捷操作导航菜单配置
const shortcutMenus = [
    { title: '企业资质审核', desc: '入驻白名单及信创硬件盾分发', icon: '🏢', path: '/admin/identity/enterprise', color: 'rgba(59, 130, 246, 0.1)' },
    { title: '个人求职管控', desc: '简历违规审查及恶意求职管控', icon: '👨‍💻', path: '/admin/identity/candidate', color: 'rgba(16, 185, 129, 0.1)' },
    { title: '兼容证书核验', desc: '智能 OCR 核验麒麟与统信适配证书', icon: '📝', path: '/admin/compliance/certs', color: 'rgba(245, 158, 11, 0.1)' },
    { title: '职位标签发放', desc: '飞腾/鲲鹏等纯国产化信创徽章分发', icon: '🏅', path: '/admin/compliance/tags', color: 'rgba(139, 92, 246, 0.1)' },
    { title: '向量匹配调试', desc: '微调 Milvus 与数据库余弦相似度阈值', icon: '🔬', path: '/admin/ai-tuning/vector', color: 'rgba(236, 72, 153, 0.1)' },
    { title: '算力模型看板', desc: 'Ollama 本地 DeepSeek GPU 显存监控', icon: '🤖', path: '/admin/ai-tuning/model', color: 'rgba(20, 184, 166, 0.1)' },
    { title: '副管理员分权', desc: '三员隔离体系，手动分发操作权限', icon: '🔑', path: '/admin/settings/subadmin', color: 'rgba(107, 114, 128, 0.1)' },
    { title: '操作日志审计', desc: '国密信道防篡改敏感操作追踪审计', icon: '📋', path: '/admin/settings/audit', color: 'rgba(239, 68, 68, 0.1)' }
]

const usersList = ref([
    { uid: 'UID_10214', name: '麒麟软件南京研发中心', role: 'enterprise', shield: 'KY-SHIELD-V10' },
    { uid: 'UID_88421', name: 'Captain_X (全栈安全开发者)', role: 'candidate', shield: 'KY-SECURE-PERSONAL' },
    { uid: 'UID_10399', name: '统信软件适配中心', role: 'enterprise', shield: 'TS-SHIELD-V20' },
    { uid: 'UID_90122', name: 'Ge Wenjing (数据库内核学子)', role: 'candidate', shield: 'KY-SECURE-PERSONAL' }
])

const filteredUsers = computed(() => {
    if (!searchQuery.value) return usersList.value
    return usersList.value.filter(u => 
    u.uid.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
    u.name.toLowerCase().includes(searchQuery.value.toLowerCase())
    )
})

const navigateTo = (path: string) => {
    router.push(path)
}

const resetShield = (user: any) => {
    alert(`已成功通过国密通道为【${user.name}】重新安全分发密匙盾号！`)
}

const toggleStatus = (user: any) => {
    alert(`防线拉起！已阻断该实体【${user.name}】在当前会话下的数据访问控制权限。`)
}
</script>

<style scoped>
.dashboard-wrapper {
    display: flex;
    flex-direction: column;
    gap: 28px;
}

/* 1. 极简可视化仪表盘网格 */
.visual-charts-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
    gap: 20px;
}

.chart-card {
    background-color: var(--bg-card);
    border: 1px solid var(--border-color);
    border-radius: 20px;
    padding: 24px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    min-height: 190px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.01);
    transition: transform 0.3s cubic-bezier(0.16, 1, 0.3, 1), box-shadow 0.3s;
}
.chart-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 12px 30px rgba(0, 0, 0, 0.03);
}

.chart-card.flex-row {
    flex-direction: row;
    align-items: center;
}

.chart-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    width: 100%;
}
.header-left {
    display: flex;
    gap: 12px;
    align-items: center;
}
.header-left h4 {
    font-size: 14px;
    font-weight: 700;
    color: var(--text-primary);
}
.header-left p {
    font-size: 11px;
    color: var(--text-secondary);
    margin-top: 2px;
}
.icon-pulse {
    font-size: 18px;
}

.badge-status {
    font-size: 10px;
    font-weight: 700;
    padding: 3px 8px;
    border-radius: 8px;
}
.badge-status.green {
    background-color: rgba(16, 185, 129, 0.1);
    color: #10b981;
}
.badge-status.red {
    background-color: rgba(239, 68, 68, 0.1);
    color: #ef4444;
}

/* SVG 算力波形图细节 */
.chart-body {
    margin-top: 14px;
    width: 100%;
}
.vram-info {
    display: flex;
    align-items: baseline;
    gap: 6px;
    margin-bottom: 8px;
}
.big-num {
    font-size: 28px;
    font-weight: 800;
    color: var(--text-primary);
    font-family: monospace;
}
.unit {
    font-size: 11px;
    color: var(--text-secondary);
}
.svg-container {
    width: 100%;
    height: 50px;
    overflow: hidden;
}
.sparkline {
    width: 100%;
    height: 100%;
}
@keyframes pulse {
    0% { r: 3px; opacity: 1; }
    100% { r: 8px; opacity: 0; }
}
.pulse-point {
    animation: pulse 1.8s infinite;
}

.chart-footer {
    display: flex;
    justify-content: space-between;
    font-size: 10px;
    color: var(--text-secondary);
    border-top: 1px solid var(--border-color);
    padding-top: 10px;
    margin-top: 10px;
}

/* 环形进度仪表盘 */
.gauge-left {
    flex: 1;
    display: flex;
    flex-direction: column;
}
.gauge-left h4 {
    font-size: 14px;
    font-weight: 700;
    color: var(--text-primary);
    margin-top: 4px;
}
.gauge-left .desc {
    font-size: 11px;
    color: var(--text-secondary);
    margin-top: 4px;
    line-height: 1.4;
}
.segmented-info {
    display: flex;
    gap: 16px;
    margin-top: 16px;
}
.seg {
    font-size: 11px;
    color: var(--text-secondary);
}
.seg strong {
    display: block;
    font-size: 16px;
    color: var(--text-primary);
    font-weight: 800;
}

.gauge-right {
    width: 100px;
    height: 100px;
}
.circle-gauge {
    width: 100%;
    height: 100%;
}
.circle-bg {
    fill: none;
    stroke: rgba(0, 0, 0, 0.04);
    stroke-width: 8;
}
:root.dark .circle-bg {
    stroke: rgba(255, 255, 255, 0.04);
}
.circle-val {
    fill: none;
    stroke: #10b981;
    stroke-width: 10;
    stroke-linecap: round;
    transform: rotate(-90deg);
    transform-origin: 50px 50px;
}
.gauge-text {
    text-anchor: middle;
    font-size: 18px;
    font-weight: 800;
    fill: var(--text-primary);
    font-family: monospace;
}

/* 终端风格防篡改审计日志 */
.terminal-body {
    background-color: rgba(0, 0, 0, 0.04);
    border: 1px solid var(--border-color);
    border-radius: 14px;
    padding: 14px;
    font-family: 'Courier New', Courier, monospace;
    font-size: 11px;
    display: flex;
    flex-direction: column;
    gap: 8px;
    max-height: 110px;
    overflow-y: auto;
    margin-top: 14px;
}
:root.dark .terminal-body {
    background-color: rgba(0, 0, 0, 0.4);
}
.log-item {
    line-height: 1.4;
    white-space: pre-wrap;
    word-break: break-all;
}
.log-item .time {
    color: var(--text-secondary);
}
.log-item.green { color: #10b981; }
.log-item.blue { color: #3b82f6; }
.log-item.yellow { color: #f59e0b; }

.blink {
    animation: blink 1.2s infinite;
}
@keyframes blink {
    0%, 100% { opacity: 1; }
    50% { opacity: 0.4; }
}


/* 2. 快捷菜单导航区域 */
.quick-nav-section {
    width: 100%;
}
.section-title {
    font-size: 15px;
    font-weight: 800;
    color: var(--text-primary);
    margin-bottom: 16px;
    letter-spacing: -0.01em;
}

.quick-nav-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
    gap: 16px;
}

.nav-card {
    background-color: var(--bg-card);
    border: 1px solid var(--border-color);
    border-radius: 16px;
    padding: 16px;
    display: flex;
    align-items: center;
    gap: 14px;
    cursor: pointer;
    transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.005);
    position: relative;
    overflow: hidden;
}
.nav-card::after {
    content: '';
    position: absolute;
    top: 0; left: 0; width: 100%; height: 100%;
    background: linear-gradient(to right, rgba(59, 130, 246, 0.02), transparent);
    opacity: 0;
    transition: opacity 0.3s;
}
.nav-card:hover {
    transform: translateY(-2px);
    border-color: #3b82f6;
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.02);
}
.nav-card:hover::after {
    opacity: 1;
}

.nav-card-icon {
    width: 44px;
    height: 44px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 18px;
    flex-shrink: 0;
    transition: transform 0.3s;
}
.nav-card:hover .nav-card-icon {
    transform: scale(1.08);
}

.nav-card-content {
    flex: 1;
    min-width: 0;
}
.nav-card-content h4 {
    font-size: 13px;
    font-weight: 700;
    color: var(--text-primary);
}
.nav-card-content p {
    font-size: 10px;
    color: var(--text-secondary);
    margin-top: 3px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.nav-card-arrow {
    font-size: 14px;
    color: var(--text-secondary);
    opacity: 0;
    transform: translateX(-5px);
    transition: all 0.25s;
    font-weight: bold;
}
.nav-card:hover .nav-card-arrow {
    opacity: 1;
    transform: translateX(0);
    color: #3b82f6;
}


/* 3. 数据表格区 */
.workspace-section {
    background-color: var(--bg-card);
    border: 1px solid var(--border-color);
    border-radius: 20px;
    padding: 24px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.01);
}
.table-title-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    flex-wrap: wrap;
    gap: 14px;
}
.section-desc {
    font-size: 11px;
    color: var(--text-secondary);
    margin-top: 4px;
}
.search-input {
    padding: 8px 14px;
    background-color: var(--bg-main);
    border: 1px solid var(--border-color);
    color: var(--text-primary);
    border-radius: 10px;
    font-size: 12px;
    width: 220px;
    transition: border-color 0.2s;
}
.search-input:focus {
    border-color: #3b82f6;
    outline: none;
}

.table-wrapper {
    width: 100%;
    overflow-x: auto;
}
.data-table {
    width: 100%;
    border-collapse: collapse;
    text-align: left;
    font-size: 12px;
}
.data-table th {
    padding: 12px 16px;
    color: var(--text-secondary);
    font-weight: 700;
    border-bottom: 1px solid var(--border-color);
    font-size: 11px;
}
.data-table td {
    padding: 16px;
    border-bottom: 1px solid var(--border-color);
}
.data-table tr:last-child td {
    border-bottom: none;
}

.col-uid {
    color: var(--text-primary);
}
.col-name {
    color: var(--text-primary);
}

.role-badge {
    font-size: 10px;
    font-weight: 700;
    padding: 4px 8px;
    border-radius: 6px;
}
.role-badge.enterprise {
    background-color: rgba(59, 130, 246, 0.1);
    color: #3b82f6;
}
.role-badge.candidate {
    background-color: rgba(16, 185, 129, 0.1);
    color: #10b981;
}

.action-btns {
    display: flex;
    gap: 8px;
    justify-content: flex-end;
}
.btn-action {
    padding: 6px 12px;
    font-size: 11px;
    font-weight: 700;
    border: 1px solid var(--border-color);
    background-color: var(--bg-card);
    color: var(--text-primary);
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.2s;
}
.btn-action:hover {
    background-color: rgba(0, 0, 0, 0.03);
}
:root.dark .btn-action:hover {
    background-color: rgba(255, 255, 255, 0.03);
}
.btn-action.danger {
    color: #ef4444;
    border-color: rgba(239, 68, 68, 0.15);
    background-color: rgba(239, 68, 68, 0.04);
}
.btn-action.danger:hover {
    background-color: rgba(239, 68, 68, 0.08);
}
</style>