<template>
  <div class="candidate-page-wrapper">
    <header class="stats-flat-row">
      <div class="stat-flat-card">
        <div class="card-left">
          <span class="stat-icon-bg blue">👥</span>
          <div>
            <p class="stat-title">全网信创人才</p>
            <h3 class="stat-count">1,284 <small>人</small></h3>
          </div>
        </div>
        <div class="card-indicator total">总用户量</div>
      </div>

      <div class="stat-flat-card">
        <div class="card-left">
          <span class="stat-icon-bg green">🟢</span>
          <div>
            <p class="stat-title">活跃求职人才</p>
            <h3 class="stat-count">842 <small>人</small></h3>
          </div>
        </div>
        <div class="card-indicator active">高活跃度</div>
      </div>

      <div class="stat-flat-card">
        <div class="card-left">
          <span class="stat-icon-bg red">🚫</span>
          <div>
            <p class="stat-title">违规封禁账号</p>
            <h3 class="stat-count">5 <small>人</small></h3>
          </div>
        </div>
        <div class="card-indicator banned">风控隔离</div>
      </div>
    </header>

    <div class="main-workspace">
      <section class="workspace-card">
        <div class="workspace-header">
          <div class="tab-selectors">
            <button 
              @click="currentTab = 'all'" 
              :class="{ 'active': currentTab === 'all' }"
              class="tab-btn"
            >
              👥 全部用户
            </button>
            <button 
              @click="currentTab = 'active'" 
              :class="{ 'active': currentTab === 'active' }"
              class="tab-btn"
            >
              🟢 正常活跃
            </button>
            <button 
              @click="currentTab = 'banned'" 
              :class="{ 'active': currentTab === 'banned' }"
              class="tab-btn"
            >
              🚫 已封号
              <span class="tab-badge red" v-if="bannedCount > 0">{{ bannedCount }}</span>
            </button>
          </div>

          <div class="header-actions">
            <input 
              v-model="searchQuery" 
              type="text" 
              placeholder="搜索姓名、邮箱、技能..." 
              class="search-input"
            />
          </div>
        </div>

        <div class="table-wrapper">
          <table class="data-table">
            <thead>
              <tr>
                <th>个人基本信息</th>
                <th>技术栈/领域</th>
                <th>当前状态</th>
                <th>电子邮箱</th>
                <th>注册时间</th>
                <th class="text-right">账号管控</th>
              </tr>
            </thead>
            <tbody>
              <tr 
                v-for="user in filteredUsers" 
                :key="user.id" 
                @click="openDetails(user)"
                class="clickable-row"
              >
                <td>
                  <div class="user-profile">
                    <div class="user-avatar" :style="{ background: getRandomGradient(user.id) }">
                      {{ user.name.slice(0, 1) }}
                    </div>
                    <div>
                      <p class="user-name">{{ user.name }}</p>
                      <p class="user-phone">{{ user.phone }}</p>
                    </div>
                  </div>
                </td>
                <td>
                  <div class="skill-badges">
                    <span v-for="skill in user.skills" :key="skill" class="skill-badge">
                      {{ skill }}
                    </span>
                  </div>
                </td>
                <td>
                  <span :class="user.status" class="status-dot-label">
                    <span class="dot"></span>
                    {{ user.status === 'active' ? '正常活跃' : '已被封禁' }}
                  </span>
                </td>
                <td class="font-mono text-secondary">{{ user.email }}</td>
                <td class="font-mono text-secondary">{{ user.joinTime }}</td>
                
                <td class="text-right action-btns" @click.stop>
                  <button 
                    v-if="user.status === 'active'" 
                    @click="banUser(user)" 
                    class="btn-action danger"
                  >
                    封号
                  </button>
                  <button 
                    v-else 
                    @click="unbanUser(user)" 
                    class="btn-action success"
                  >
                    解封账号
                  </button>
                </td>
              </tr>
              <tr v-if="filteredUsers.length === 0">
                <td colspan="6" class="empty-placeholder">暂无匹配的个人用户数据</td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>

      <transition name="fade">
        <div v-if="selectedUser" class="drawer-overlay" @click="selectedUser = null">
          <transition name="slide">
            <aside class="drawer-panel" @click.stop>
              <div class="drawer-header">
                <div class="header-title-area">
                  <span class="status-indicator-badge" :class="selectedUser.status">
                    {{ selectedUser.status === 'active' ? '正常活跃' : '已被封禁' }}
                  </span>
                  <h4>📁 信创人才电子档案</h4>
                  <p class="font-mono font-bold">UID: KC-20260{{ selectedUser.id }}881</p>
                </div>
                <button @click="selectedUser = null" class="close-btn">×</button>
              </div>

              <div class="drawer-content no-scrollbar">
                
                <div class="detail-section">
                  <h5 class="section-title">👤 个人基本档案</h5>
                  <div class="details-grid-2col">
                    <div class="grid-item"><span class="label">真实姓名</span><span class="val">{{ selectedUser.name }}</span></div>
                    <div class="grid-item"><span class="label">联系电话</span><span class="val font-mono">{{ selectedUser.phone }}</span></div>
                    <div class="grid-item"><span class="label">电子邮箱</span><span class="val font-mono">{{ selectedUser.email }}</span></div>
                    <div class="grid-item"><span class="label">毕业院校 / 机构</span><span class="val">{{ selectedUser.education }}</span></div>
                    <div class="grid-item"><span class="label">最高学历</span><span class="val">{{ selectedUser.degree }}</span></div>
                    <div class="grid-item"><span class="label">注册日期</span><span class="val font-mono">{{ selectedUser.joinTime }}</span></div>
                    <div class="grid-item"><span class="label">当前求职意向</span><span class="val text-emerald-500 font-bold">{{ selectedUser.jobStatus }}</span></div>
                    <div class="grid-item"><span class="label">期望工作城市</span><span class="val">{{ selectedUser.expectCity }}</span></div>
                  </div>
                </div>

                <div class="detail-section">
                  <h5 class="section-title">⚡ 掌握的信创技能生态</h5>
                  <div class="skills-box">
                    <span v-for="skill in selectedUser.skills" :key="skill" class="skill-big-badge">
                      🎯 {{ skill }}
                    </span>
                  </div>
                </div>

                <div class="detail-section">
                  <h5 class="section-title">📄 个人主页自荐信</h5>
                  <div class="bio-box">
                    <p class="bio-text">“ {{ selectedUser.bio }} ”</p>
                  </div>
                </div>

                <div class="detail-section" v-if="selectedUser.status === 'banned'">
                  <h5 class="section-title">🚨 风控封禁细节说明</h5>
                  <div class="ban-report-box">
                    <p class="ban-reason-title">🛡️ 平台违规封号报告：</p>
                    <div class="ban-reason-content">
                      <p><span class="ban-label">封禁时间：</span><span class="ban-val font-mono">{{ selectedUser.banTime }}</span></p>
                      <p><span class="ban-label">触发规则：</span><span class="ban-val text-rose-500 font-bold">{{ selectedUser.banReason }}</span></p>
                      <p><span class="ban-label">操作管理员：</span><span class="ban-val">Kirin_Admin</span></p>
                    </div>
                  </div>
                </div>

              </div>

              <div class="drawer-footer">
                <template v-if="selectedUser.status === 'active'">
                  <button @click="banUser(selectedUser)" class="btn-danger-large">
                    🚫 立即冻结并封禁该账号
                  </button>
                </template>
                <template v-else>
                  <button @click="unbanUser(selectedUser)" class="btn-primary-large">
                    🟢 解除账号封禁（恢复访问权限）
                  </button>
                </template>
              </div>
            </aside>
          </transition>
        </div>
      </transition>

    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

const currentTab = ref<'all' | 'active' | 'banned'>('all')
const searchQuery = ref('')
const selectedUser = ref<any>(null)

// 个人用户模拟数据库
const usersList = ref([
  {
    id: 1,
    name: '李明轩',
    phone: '13955112233',
    email: 'limingxuan@kylin.mail.cn',
    skills: ['Spring Boot', 'Vue 3', 'UniApp', '麒麟系统部署'],
    status: 'active',
    education: '南京邮电大学',
    degree: '本科',
    joinTime: '2026-01-10',
    jobStatus: '积极寻找信创开发岗',
    expectCity: '南京、上海',
    bio: '全栈开发工程师，拥有3年企业级Spring Boot与Vue3开发经验。曾在14届“中国软件杯”大赛中获得过荣誉，对信创操作系统下服务的适配与打包发布有深厚的实战理解。',
    banTime: '',
    banReason: ''
  },
  {
    id: 2,
    name: '周静雅',
    phone: '15866223344',
    email: 'zhoujy_sh@163.com',
    skills: ['达梦数据库', 'SQL', 'Python', 'Linux运维'],
    status: 'active',
    education: '同济大学',
    degree: '硕士',
    joinTime: '2026-02-15',
    jobStatus: '准备JLPT N2 考试中 / 寻国产DBA岗位',
    expectCity: '上海、杭州',
    bio: '主攻国产数据库（达梦、人大金仓）的迁移与性能优化。具备深厚的SQL功底，能独立在海光、飞腾等信创裸金属服务器上配置高可用数据库集群。',
    banTime: '',
    banReason: ''
  },
  {
    id: 3,
    name: '张宇昂',
    phone: '18833445566',
    email: 'zhangya_dev@outlook.com',
    skills: ['GoLang', 'Docker', 'K8s', '信创安全加密'],
    status: 'active',
    education: '合肥工业大学',
    degree: '本科',
    joinTime: '2026-03-01',
    jobStatus: '在职（考虑国产微服务架构岗）',
    expectCity: '合肥、南京',
    bio: '极简主义开发者，擅长云原生及信创安全。主要负责将传统软件容器化并平滑迁移至基于海光CPU的信创云环境。',
    banTime: '',
    banReason: ''
  },
  {
    id: 4,
    name: '赵黑产（虚假账号）',
    phone: '17011223344',
    email: 'fake_crawler@spam.com',
    skills: ['Python自动化爬虫', '批量注册'],
    status: 'banned',
    education: '社会大学',
    degree: '无',
    joinTime: '2026-04-12',
    jobStatus: '未知',
    expectCity: '远程',
    bio: '承接高频简历爬取、批量信息抓取业务，支持API代理。',
    banTime: '2026-05-01 14:32',
    banReason: '触发平台风控雷达，利用爬虫高频无授权抓取“人才公海”简历数据，判定为商业黑产间谍账号。'
  },
  {
    id: 5,
    name: '虚假恶意刷单号',
    phone: '17188990022',
    email: 'ad_spammer@163.com',
    skills: ['SEO推广', '群发广告'],
    status: 'banned',
    education: '虚假学历',
    degree: '大专',
    joinTime: '2026-04-20',
    jobStatus: '接单中',
    expectCity: '全国',
    bio: '接信创岗位代刷、简历置顶点击、简历代润色刷赞等业务，量大优惠。',
    banTime: '2026-05-03 09:12',
    banReason: '在平台信创人才社区高频群发“接单刷量”垃圾广告，被多名企业hr举报，触发平台一级禁言封号守则。'
  }
])

// 封号计数
const bannedCount = computed(() => usersList.value.filter(u => u.status === 'banned').length)

// 按分类 Tab 与搜索词过滤个人列表
const filteredUsers = computed(() => {
  return usersList.value.filter(user => {
    // 1. Tab 过滤
    if (currentTab.value === 'active' && user.status !== 'active') return false
    if (currentTab.value === 'banned' && user.status !== 'banned') return false
    
    // 2. 搜索过滤
    if (!searchQuery.value) return true
    const q = searchQuery.value.toLowerCase()
    return user.name.toLowerCase().includes(q) ||
           user.email.toLowerCase().includes(q) ||
           user.skills.some(s => s.toLowerCase().includes(q))
  })
})

const openDetails = (user: any) => {
  selectedUser.value = user
}

// 优雅的 Apple 渐变背景
const getRandomGradient = (id: number) => {
  const gradients = [
    'linear-gradient(135deg, #60a5fa 0%, #2563eb 100%)',
    'linear-gradient(135deg, #34d399 0%, #059669 100%)',
    'linear-gradient(135deg, #fbbf24 0%, #d97706 100%)',
    'linear-gradient(135deg, #f472b6 0%, #db2777 100%)',
    'linear-gradient(135deg, #a78bfa 0%, #7c3aed 100%)'
  ]
  return gradients[id % gradients.length]
}

/* --- 账号管控事件 --- */

// 1. 执行封号
const banUser = (user: any) => {
  const reason = prompt('请输入封禁原因：', '发布涉嫌黑产、垃圾广告信息或触发安全高频检索警报。')
  if (reason === null) return // 取消操作
  
  user.status = 'banned'
  user.banTime = new Date().toLocaleString()
  user.banReason = reason || '未提供具体原因'
  
  // 同步更新抽屉内状态
  if (selectedUser.value && selectedUser.value.id === user.id) {
    selectedUser.value = { ...user }
  }
}

// 2. 解除封号
const unbanUser = (user: any) => {
  if (confirm(`确定要解除对用户 [${user.name}] 的账号封禁吗？`)) {
    user.status = 'active'
    user.banTime = ''
    user.banReason = ''
    
    if (selectedUser.value && selectedUser.value.id === user.id) {
      selectedUser.value = { ...user }
    }
  }
}
</script>

<style scoped>
.candidate-page-wrapper {
  display: flex;
  flex-direction: column;
  gap: 20px;
  width: 100%;
}

/* 1. TOP FLAT STATS: 中等尺寸扁平看板 */
.stats-flat-row {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  width: 100%;
}
@media (max-width: 900px) {
  .stats-flat-row { grid-template-columns: 1fr; }
}

.stat-flat-card {
  background-color: var(--bg-card);
  border: 1px solid var(--border-color);
  padding: 16px 20px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.01);
}

.card-left {
  display: flex;
  align-items: center;
  gap: 12px;
}
.stat-icon-bg {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
}
.stat-icon-bg.blue { background-color: rgba(59, 130, 246, 0.08); }
.stat-icon-bg.green { background-color: rgba(16, 185, 129, 0.08); }
.stat-icon-bg.red { background-color: rgba(239, 68, 68, 0.08); }

.stat-title {
  font-size: 11px;
  color: var(--text-secondary);
  font-weight: 600;
}
.stat-count {
  font-size: 20px;
  font-weight: 800;
  color: var(--text-primary);
  font-family: monospace;
  margin-top: 2px;
}
.stat-count small {
  font-size: 11px;
  font-weight: normal;
  color: var(--text-secondary);
}

.card-indicator {
  font-size: 9px;
  font-weight: 700;
  padding: 3px 8px;
  border-radius: 8px;
}
.card-indicator.total { background-color: rgba(59, 130, 246, 0.1); color: #3b82f6; }
.card-indicator.active { background-color: rgba(16, 185, 129, 0.1); color: #10b981; }
.card-indicator.banned { background-color: rgba(239, 68, 68, 0.1); color: #ef4444; }


/* 2. MAIN WORKSPACE */
.main-workspace {
  width: 100%;
}
.workspace-card {
  background-color: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.015);
}

.workspace-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
  gap: 16px;
}

/* 极简 Tab */
.tab-selectors {
  display: flex;
  background-color: var(--bg-main);
  padding: 4px;
  border-radius: 10px;
  border: 1px solid var(--border-color);
}
.tab-btn {
  padding: 8px 16px;
  font-size: 12px;
  font-weight: 700;
  border: none;
  background: none;
  color: var(--text-secondary);
  cursor: pointer;
  border-radius: 7px;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  gap: 6px;
}
.tab-btn.active {
  background-color: var(--bg-card);
  color: var(--text-primary);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}
.tab-badge {
  font-size: 9px;
  padding: 1px 5px;
  border-radius: 10px;
  font-weight: 800;
}
.tab-badge.red {
  background-color: rgba(239, 68, 68, 0.15);
  color: #ef4444;
}

.search-input {
  padding: 8px 14px;
  background-color: var(--bg-main);
  border: 1px solid var(--border-color);
  color: var(--text-primary);
  border-radius: 8px;
  font-size: 12px;
  width: 220px;
}
.search-input:focus {
  border-color: #3b82f6;
  outline: none;
}

/* 数据表格 */
.table-wrapper {
  overflow-x: auto;
}
.data-table {
  width: 100%;
  border-collapse: collapse;
  text-align: left;
  font-size: 12px;
}
.data-table th {
  padding: 12px 14px;
  color: var(--text-secondary);
  font-weight: 700;
  border-bottom: 1px solid var(--border-color);
  font-size: 11px;
}
.data-table td {
  padding: 14px;
  border-bottom: 1px solid var(--border-color);
}
.clickable-row {
  cursor: pointer;
  transition: background-color 0.15s;
}
.clickable-row:hover {
  background-color: rgba(0, 0, 0, 0.008);
}
:root.dark .clickable-row:hover {
  background-color: rgba(255, 255, 255, 0.008);
}

.user-profile {
  display: flex;
  gap: 10px;
  align-items: center;
}
.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%; /* 个人用圆形头像，跟企业的方形进行区分 */
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: bold;
}
.user-name {
  font-weight: 700;
  color: var(--text-primary);
}
.user-phone {
  font-size: 10px;
  color: var(--text-secondary);
  font-family: monospace;
  margin-top: 2px;
}

.skill-badges {
  display: flex;
  gap: 4px;
  flex-wrap: wrap;
}
.skill-badge {
  background-color: rgba(59, 130, 246, 0.05);
  border: 1px solid rgba(59, 130, 246, 0.1);
  color: #3b82f6;
  font-size: 10px;
  padding: 1px 5px;
  border-radius: 5px;
}

.status-dot-label {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  font-weight: 700;
}
.status-dot-label .dot {
  width: 5px;
  height: 5px;
  border-radius: 50%;
}
.status-dot-label.active { color: #10b981; }
.status-dot-label.active .dot { background-color: #10b981; }
.status-dot-label.banned { color: #ef4444; }
.status-dot-label.banned .dot { background-color: #ef4444; }

/* 按钮 */
.action-btns {
  display: flex;
  gap: 6px;
  justify-content: flex-end;
}
.btn-action {
  padding: 5px 10px;
  font-size: 11px;
  font-weight: 700;
  border: 1px solid var(--border-color);
  background-color: var(--bg-card);
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.15s;
}
.btn-action.success {
  color: #10b981;
  border-color: rgba(16, 185, 129, 0.15);
  background-color: rgba(16, 185, 129, 0.04);
}
.btn-action.success:hover { background-color: rgba(16, 185, 129, 0.08); }

.btn-action.danger {
  color: #ef4444;
  border-color: rgba(239, 68, 68, 0.15);
  background-color: rgba(239, 68, 68, 0.04);
}
.btn-action.danger:hover { background-color: rgba(239, 68, 68, 0.08); }

.empty-placeholder {
  text-align: center;
  color: var(--text-secondary);
  padding: 40px 0;
  font-size: 11px;
}


/* 3. FULL-SCREEN OVERLAY DRAWER: 悬浮模态抽屉 */
.drawer-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  z-index: 999;
  display: flex;
  justify-content: flex-end;
}

.drawer-panel {
  width: 580px;
  max-width: 90%;
  height: 100%;
  background-color: var(--bg-card);
  border-left: 1px solid var(--border-color);
  box-shadow: -10px 0 30px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
}

/* 抽屉头部 */
.drawer-header {
  padding: 24px;
  border-bottom: 1px solid var(--border-color);
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}
.header-title-area {
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.status-indicator-badge {
  align-self: flex-start;
  font-size: 10px;
  font-weight: 800;
  padding: 2px 8px;
  border-radius: 6px;
}
.status-indicator-badge.active { background-color: rgba(16, 185, 129, 0.12); color: #10b981; }
.status-indicator-badge.banned { background-color: rgba(239, 68, 68, 0.12); color: #ef4444; }

.drawer-header h4 {
  font-size: 16px;
  font-weight: 800;
  color: var(--text-primary);
}
.drawer-header p {
  font-size: 11px;
  color: var(--text-secondary);
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  color: var(--text-secondary);
  cursor: pointer;
  padding: 0 6px;
}
.close-btn:hover { color: var(--text-primary); }

/* 抽屉内容 */
.drawer-content {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 28px;
}

.detail-section {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.detail-section .section-title {
  font-size: 12px;
  color: var(--text-secondary);
  font-weight: 800;
  letter-spacing: 0.05em;
  border-left: 4px solid #3b82f6;
  padding-left: 8px;
}

.details-grid-2col {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px 20px;
  background-color: var(--bg-main);
  border: 1px solid var(--border-color);
  padding: 16px;
  border-radius: 12px;
}
.grid-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}
.grid-item .label {
  font-size: 11px;
  color: var(--text-secondary);
}
.grid-item .val {
  font-size: 12px;
  font-weight: 600;
  color: var(--text-primary);
}

/* 技能谱系 */
.skills-box {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  background-color: var(--bg-main);
  border: 1px solid var(--border-color);
  padding: 14px;
  border-radius: 12px;
}
.skill-big-badge {
  font-size: 11px;
  font-weight: bold;
  background-color: var(--bg-card);
  border: 1px solid var(--border-color);
  padding: 4px 10px;
  border-radius: 8px;
  color: var(--text-primary);
}

/* 个人陈述 */
.bio-box {
  background-color: var(--bg-main);
  border: 1px solid var(--border-color);
  padding: 16px;
  border-radius: 12px;
}
.bio-text {
  font-size: 12px;
  color: var(--text-primary);
  line-height: 1.6;
  font-style: italic;
}

/* 封号风控说明 */
.ban-report-box {
  background-color: rgba(239, 68, 68, 0.03);
  border: 1px dashed rgba(239, 68, 68, 0.2);
  border-radius: 12px;
  padding: 16px;
}
.ban-reason-title {
  font-size: 12px;
  font-weight: bold;
  color: #ef4444;
  margin-bottom: 8px;
}
.ban-reason-content {
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.ban-reason-content p {
  font-size: 11px;
}
.ban-label {
  color: var(--text-secondary);
}
.ban-val {
  color: var(--text-primary);
}

/* 抽屉底部工作栏 */
.drawer-footer {
  padding: 20px 24px;
  border-top: 1px solid var(--border-color);
  background-color: var(--bg-card);
}
.btn-primary-large {
  width: 100%;
  padding: 12px;
  background-color: #10b981;
  color: white;
  border: none;
  border-radius: 10px;
  font-weight: bold;
  cursor: pointer;
  font-size: 13px;
  transition: opacity 0.2s;
}
.btn-primary-large:hover { opacity: 0.9; }

.btn-danger-large {
  width: 100%;
  padding: 12px;
  background-color: #ef4444;
  color: white;
  border: none;
  border-radius: 10px;
  font-weight: bold;
  cursor: pointer;
  font-size: 13px;
  transition: opacity 0.2s;
}
.btn-danger-large:hover { opacity: 0.9; }

.no-scrollbar::-webkit-scrollbar { display: none; }
.no-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }

/* 侧滑动画 */
.fade-enter-active, .fade-leave-active { transition: opacity 0.25s ease; }
.fade-enter-from, .fade-leave-to { opacity: 0; }

.slide-enter-active, .slide-leave-active { transition: transform 0.3s cubic-bezier(0.16, 1, 0.3, 1); }
.slide-enter-from, .slide-leave-to { transform: translateX(100%); }
</style>