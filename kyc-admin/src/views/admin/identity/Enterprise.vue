<template>
  <div class="enterprise-page-wrapper">
    <header class="stats-flat-row">
      <div class="stat-flat-card">
        <div class="card-left">
          <span class="stat-icon-bg blue">📥</span>
          <div>
            <p class="stat-title">待处理入驻申请</p>
            <h3 class="stat-count">14 <small>家</small></h3>
          </div>
        </div>
        <div class="card-indicator pending">待响应</div>
      </div>

      <div class="stat-flat-card">
        <div class="card-left">
          <span class="stat-icon-bg green">🛡️</span>
          <div>
            <p class="stat-title">已授信入驻企业</p>
            <h3 class="stat-count">398 <small>家</small></h3>
          </div>
        </div>
        <div class="card-indicator approved">正常运行</div>
      </div>

      <div class="stat-flat-card">
        <div class="card-left">
          <span class="stat-icon-bg red">❄️</span>
          <div>
            <p class="stat-title">被隔离冻结企业</p>
            <h3 class="stat-count">3 <small>家</small></h3>
          </div>
        </div>
        <div class="card-indicator frozen">安全受限</div>
      </div>
    </header>

    <div class="main-workspace">
      <section class="workspace-card">
        <div class="workspace-header">
          <div class="tab-selectors">
            <button 
              @click="currentTab = 'pending'" 
              :class="{ 'active': currentTab === 'pending' }"
              class="tab-btn"
            >
              📥 待审核 
              <span class="tab-badge yellow" v-if="pendingCount > 0">{{ pendingCount }}</span>
            </button>
            <button 
              @click="currentTab = 'approved'" 
              :class="{ 'active': currentTab === 'approved' }"
              class="tab-btn"
            >
              🟢 已审核 (企业列表)
            </button>
            <button 
              @click="currentTab = 'rejected'" 
              :class="{ 'active': currentTab === 'rejected' }"
              class="tab-btn"
            >
              🔴 已驳回
            </button>
            <button 
              @click="currentTab = 'frozen'" 
              :class="{ 'active': currentTab === 'frozen' }"
              class="tab-btn"
            >
              ❄️ 已冻结
            </button>
          </div>

          <div class="header-actions">
            <input 
              v-model="searchQuery" 
              type="text" 
              placeholder="搜索企业名称、信用代码..." 
              class="search-input"
            />
          </div>
        </div>

        <div class="table-wrapper">
          <table class="data-table">
            <thead>
              <tr>
                <th>企业基本信息</th>
                <th>适配信创环境</th>
                <th>AI 置信度评分</th>
                <th>安全密盾状态</th>
                <th>当前状态</th>
                <th class="text-right">快捷操作</th>
              </tr>
            </thead>
            <tbody>
              <tr 
                v-for="corp in filteredCorps" 
                :key="corp.id" 
                @click="openDetails(corp)"
                class="clickable-row"
              >
                <td>
                  <div class="corp-profile">
                    <div class="corp-avatar" :style="{ background: getRandomGradient(corp.id) }">
                      {{ corp.name.slice(0, 2) }}
                    </div>
                    <div>
                      <p class="corp-name">{{ corp.name }}</p>
                      <p class="corp-code">{{ corp.code }}</p>
                    </div>
                  </div>
                </td>
                <td>
                  <div class="env-badges">
                    <span v-for="env in corp.environments" :key="env" class="env-badge">
                      {{ env }}
                    </span>
                  </div>
                </td>
                <td>
                  <div class="score-container">
                    <span class="score-text" :style="{ color: getScoreColor(corp.aiScore) }">
                      {{ corp.aiScore }}%
                    </span>
                    <div class="score-bar-bg">
                      <div 
                        class="score-bar-fill" 
                        :style="{ width: corp.aiScore + '%', background: getScoreColor(corp.aiScore) }"
                      ></div>
                    </div>
                  </div>
                </td>
                <td>
                  <span :class="corp.shieldStatus" class="shield-badge">
                    {{ corp.shieldStatus === 'bound' ? '🔐 已绑定硬件盾' : '🔓 未授权' }}
                  </span>
                </td>
                <td>
                  <span :class="corp.status" class="status-dot-label">
                    <span class="dot"></span>
                    {{ getStatusText(corp.status) }}
                  </span>
                </td>
                
                <td class="text-right action-btns" @click.stop>
                  <template v-if="currentTab === 'pending'">
                    <button @click="approveCorp(corp)" class="btn-action success">准入通过</button>
                    <button @click="rejectCorp(corp)" class="btn-action danger">安全驳回</button>
                  </template>

                  <template v-else-if="currentTab === 'approved'">
                    <button @click="freezeCorp(corp)" class="btn-action warning">冻结企业</button>
                  </template>

                  <template v-else-if="currentTab === 'rejected'">
                    <button @click="reconsiderCorp(corp)" class="btn-action info">重回审核</button>
                  </template>

                  <template v-else-if="currentTab === 'frozen'">
                    <button @click="unfreezeCorp(corp)" class="btn-action success-outline">解冻</button>
                  </template>
                </td>
              </tr>
              <tr v-if="filteredCorps.length === 0">
                <td colspan="6" class="empty-placeholder">该状态分类下暂无匹配的企业资质数据</td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>

      <transition name="fade">
        <div v-if="selectedCorp" class="drawer-overlay" @click="selectedCorp = null">
          <transition name="slide">
            <aside class="drawer-panel" @click.stop>
              <div class="drawer-header">
                <div class="header-title-area">
                  <span class="status-indicator-badge" :class="selectedCorp.status">
                    {{ getStatusText(selectedCorp.status) }}
                  </span>
                  <h4>📁 企业信创准入资质详情</h4>
                  <p class="font-mono">系统备案流水号: UID-202605{{ selectedCorp.id }}0942</p>
                </div>
                <button @click="selectedCorp = null" class="close-btn">×</button>
              </div>

              <div class="drawer-content no-scrollbar">
                
                <div class="detail-section">
                  <h5 class="section-title">🏢 工商准入主体档案</h5>
                  <div class="details-grid-2col">
                    <div class="grid-item"><span class="label">企业全称</span><span class="val">{{ selectedCorp.name }}</span></div>
                    <div class="grid-item"><span class="label">统一社会信用代码</span><span class="val font-mono">{{ selectedCorp.code }}</span></div>
                    <div class="grid-item"><span class="label">法定代表人</span><span class="val">{{ selectedCorp.legalPerson }}</span></div>
                    <div class="grid-item"><span class="label">注册资本</span><span class="val">{{ selectedCorp.registeredCapital }}</span></div>
                    <div class="grid-item"><span class="label">企业类型</span><span class="val">{{ selectedCorp.corpType }}</span></div>
                    <div class="grid-item"><span class="label">成立日期</span><span class="val font-mono">{{ selectedCorp.establishDate }}</span></div>
                    <div class="grid-item"><span class="label">经营状态</span><span class="val text-emerald-500 font-bold">{{ selectedCorp.businessStatus }}</span></div>
                    <div class="grid-item"><span class="label">注册地址</span><span class="val">{{ selectedCorp.address }}</span></div>
                  </div>
                </div>

                <div class="detail-section">
                  <h5 class="section-title">👤 资质申报经办人</h5>
                  <div class="details-grid-2col">
                    <div class="grid-item"><span class="label">经办人姓名</span><span class="val">{{ selectedCorp.contactName }}</span></div>
                    <div class="grid-item"><span class="label">联系电话</span><span class="val font-mono">{{ selectedCorp.contactPhone }}</span></div>
                    <div class="grid-item"><span class="label">企业电子邮箱</span><span class="val font-mono">{{ selectedCorp.contactEmail }}</span></div>
                    <div class="grid-item"><span class="label">申报时间</span><span class="val font-mono">{{ selectedCorp.applyTime }}</span></div>
                  </div>
                </div>

                <div class="detail-section">
                  <h5 class="section-title">📄 信创兼容性适配材料</h5>
                  <div class="materials-box">
                    <div class="material-preview-card">
                      <span class="pdf-icon">📄</span>
                      <div class="pdf-info">
                        <p class="file-name">{{ selectedCorp.certName }}</p>
                        <p class="file-meta">材料属性：信创互认兼容性证明 | 格式：PDF</p>
                      </div>
                      <a href="#" @click.prevent class="btn-preview-link">在线预览</a>
                    </div>
                    <div class="ocr-report-box">
                      <p class="report-title">🤖 国产适配认证 OCR 交叉验签报告：</p>
                      <div class="report-metrics">
                        <span>比对一致率: <strong class="text-emerald-500">98.7%</strong></span>
                        <span>官方防伪电子印章: <strong class="text-emerald-500">验证通过</strong></span>
                        <span>证书有效期: <strong class="text-slate-700">2030年12月前有效</strong></span>
                      </div>
                    </div>
                  </div>
                </div>

                <div class="detail-section">
                  <h5 class="section-title">🤖 AI 行业图谱与资质安全研判</h5>
                  <div class="ai-deep-card">
                    <div class="ai-score-ring">
                      <span class="score">{{ selectedCorp.aiScore }}</span>
                      <span class="unit">置信分</span>
                    </div>
                    <div class="ai-verdict">
                      <p class="verdict-title">LLM 多维度风控合规意见</p>
                      <p class="verdict-desc">{{ selectedCorp.aiOpinion }}</p>
                    </div>
                  </div>
                </div>

                <div class="detail-section last">
                  <h5 class="section-title">🔑 安全硬件密盾配置</h5>
                  <div class="shield-settings-box">
                    <div class="setting-row">
                      <span class="setting-label">支持国密算法：</span>
                      <span class="algo-badge">SM2 (非对称)</span>
                      <span class="algo-badge">SM3 (哈希)</span>
                      <span class="algo-badge">SM4 (对称)</span>
                    </div>
                    <div class="setting-row">
                      <span class="setting-label">专属硬件公钥 (PublicKey)：</span>
                      <div class="key-display font-mono" v-if="selectedCorp.shieldKey">
                        {{ selectedCorp.shieldKey }}
                      </div>
                      <div class="key-display font-mono empty" v-else>
                        ⚠️ 资质未批准，暂未分配国密芯片密钥安全盾
                      </div>
                    </div>
                  </div>
                </div>

              </div>

              <div class="drawer-footer">
                <template v-if="selectedCorp.status === 'pending'">
                  <div class="footer-btn-group">
                    <button @click="approveCorp(selectedCorp)" class="btn-primary-large">批准资质并绑定密盾</button>
                    <button @click="rejectCorp(selectedCorp)" class="btn-secondary-large">安全驳回申请</button>
                  </div>
                </template>
                <template v-else-if="selectedCorp.status === 'approved'">
                  <button @click="freezeCorp(selectedCorp)" class="btn-danger-large">⚠️ 紧急冻结企业访问权</button>
                </template>
                <template v-else-if="selectedCorp.status === 'frozen'">
                  <button @click="unfreezeCorp(selectedCorp)" class="btn-primary-large">🔓 解除冻结恢复正常访问</button>
                </template>
                <template v-else-if="selectedCorp.status === 'rejected'">
                  <button @click="reconsiderCorp(selectedCorp)" class="btn-secondary-large">将该驳回重新拉入审核流程</button>
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

const currentTab = ref<'pending' | 'approved' | 'rejected' | 'frozen'>('pending')
const searchQuery = ref('')
const selectedCorp = ref<any>(null)

// 深度丰富后的真实企业资质数据库
const corpsList = ref([
  {
    id: 1,
    name: '南京麒麟适配安全系统有限公司',
    code: '91320100MA1Y6TR8XX',
    environments: ['麒麟OS V10', '飞腾FT-2000', '达梦DM8'],
    aiScore: 96,
    shieldStatus: 'bound',
    status: 'pending',
    
    // 补齐企业工商信息与联系人信息
    legalPerson: '张建国',
    registeredCapital: '5000 万人民币',
    corpType: '有限责任公司(自然人投资或控股)',
    establishDate: '2018-06-12',
    businessStatus: '在营（开业）',
    address: '南京市江宁区信创产业园B座12层',
    contactName: '王经理',
    contactPhone: '13812345678',
    contactEmail: 'contact@kylin-nanjing.cn',
    applyTime: '2026-05-07 10:15',
    certName: 'Kirin_OS_Compatibility_V10.pdf',
    aiOpinion: '【推荐准入】该主体工商在营状态良好。经多模态大模型研判，提交的信创兼容性证书防伪水印完整，印章与麒麟官方库交叉验证100%重合，无经营异常记录。安全度极高。',
    shieldKey: 'SM4-KEY-KY-02941-8A9F-2026'
  },
  {
    id: 2,
    name: '统信软件（北京）技术服务部',
    code: '91110108MA01QX8R11',
    environments: ['统信UOS', '鲲鹏920', '人大金仓'],
    aiScore: 94,
    shieldStatus: 'unbound',
    status: 'pending',
    
    legalPerson: '李向东',
    registeredCapital: '12000 万人民币',
    corpType: '股份有限公司(非上市)',
    establishDate: '2019-11-14',
    businessStatus: '在营（开业）',
    address: '北京市海淀区中关村软件园三期5号楼',
    contactName: '陈秘书',
    contactPhone: '18699887766',
    contactEmail: 'services@uniontech.com',
    applyTime: '2026-05-08 09:30',
    certName: 'UnionTech_UOS_Adaptation_Certificate.pdf',
    aiOpinion: '【推荐准入】统一社会信用代码与全国企业公示系统数据匹配。该企业与统信生态圈、华为鲲鹏芯片架构具有良好的适配性，材料符合标准，置信水平优异。',
    shieldKey: 'SM4-KEY-TS-88214-3B2D-2026'
  },
  {
    id: 3,
    name: '云腾国产虚拟化科技工坊',
    code: '91330106MA2Y5TR39Y',
    environments: ['海光3号', '中科曙光'],
    aiScore: 78,
    shieldStatus: 'unbound',
    status: 'pending',
    
    legalPerson: '赵云飞',
    registeredCapital: '200 万人民币',
    corpType: '有限合伙企业',
    establishDate: '2025-02-18',
    businessStatus: '在营（开业）',
    address: '杭州市西湖区云谷科技园1号楼',
    contactName: '赵云飞',
    contactPhone: '15911223344',
    contactEmail: 'yunteng_tech@163.com',
    applyTime: '2026-05-08 11:02',
    certName: 'CloudTeng_Hypervisor_Cert.png',
    aiOpinion: '【建议人工复审】初创型合伙企业，成立时间较短（仅3个月）。OCR核验识别到上传的证明图片存在边缘轻微噪点，疑似存在拼贴修图痕迹，建议人工核验原件。',
    shieldKey: ''
  },
  {
    id: 4,
    name: '北京宏达海外空壳信息科技',
    code: '91440300MA5EXXX999',
    environments: ['未知环境'],
    aiScore: 32,
    shieldStatus: 'unbound',
    status: 'rejected',
    
    legalPerson: '隐名股东',
    registeredCapital: '10 万人民币',
    corpType: '有限责任公司',
    establishDate: '2024-08-01',
    businessStatus: '在营（开业）',
    address: '北京市朝阳区高碑店虚拟办公室B-02',
    contactName: '张三（代办）',
    contactPhone: '17000000000',
    contactEmail: 'temp_user@domain.com',
    applyTime: '2026-05-05 16:40',
    certName: 'null',
    aiOpinion: '【高危警告】该主体经营异常，注册地址为虚拟挂靠地址。无法提取有效的信创证明或合作伙伴书面授权，且申请IP触发恶意高频段警报，系统已自动阻断并驳回。',
    shieldKey: ''
  },
  {
    id: 5,
    name: '中科曙光（南京）计算系统有限公司',
    code: '91320115MA1YAA8811',
    environments: ['曙光服务器', '海光CPU'],
    aiScore: 98,
    shieldStatus: 'bound',
    status: 'approved',
    
    legalPerson: '周华强',
    registeredCapital: '15000 万人民币',
    corpType: '有限责任公司(外商投资企业合资)',
    establishDate: '2016-04-05',
    businessStatus: '在营（开业）',
    address: '南京市雨花台区软件谷核心区3号楼',
    contactName: '李总监',
    contactPhone: '13988889999',
    contactEmail: 'li.jq@sugon.com.cn',
    applyTime: '2026-05-04 14:20',
    certName: 'Sugon_Server_Validate_2026.pdf',
    aiOpinion: '【已核验入驻】国家专精特新企业，信息完备，证书验签章合法一致，安全级别极高。',
    shieldKey: 'SM4-KEY-SG-99124-7F8C-2026'
  },
  {
    id: 6,
    name: '博通国产数据库集成服务商',
    code: '91310115MA1YZZ5544',
    environments: ['万里数据库', '金仓Kingbase'],
    aiScore: 89,
    shieldStatus: 'bound',
    status: 'frozen',
    
    legalPerson: '刘光明',
    registeredCapital: '1000 万人民币',
    corpType: '有限责任公司',
    establishDate: '2020-03-20',
    businessStatus: '在营（开业）',
    address: '上海市浦东新区张江高科核心区B区',
    contactName: '刘经理',
    contactPhone: '13566778899',
    contactEmail: 'management@broad-db.com',
    applyTime: '2026-04-20 11:15',
    certName: 'GreatDB_Compatibility.pdf',
    aiOpinion: '【安全隔离中】由于该主体的物理硬件安全秘盾组件在客户端检测到解密溢出攻击尝试，触发平台防爆预警日志。系统现已启动应急控制链，将其完全隔离冻结。',
    shieldKey: 'SM4-KEY-BT-44122-3C9A-2026'
  }
])

const pendingCount = computed(() => corpsList.value.filter(c => c.status === 'pending').length)

const filteredCorps = computed(() => {
  return corpsList.value.filter(corp => {
    if (corp.status !== currentTab.value) return false
    if (!searchQuery.value) return true
    return corp.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
           corp.code.toLowerCase().includes(searchQuery.value.toLowerCase())
  })
})

const openDetails = (corp: any) => {
  selectedCorp.value = corp
}

const getScoreColor = (score: number) => {
  if (score >= 90) return '#10b981'
  if (score >= 70) return '#f59e0b'
  return '#ef4444'
}

const getStatusText = (status: string) => {
  switch (status) {
    case 'pending': return '待审核'
    case 'approved': return '已审核'
    case 'rejected': return '已驳回'
    case 'frozen': return '已冻结'
    default: return status
  }
}

const getRandomGradient = (id: number) => {
  const gradients = [
    'linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%)',
    'linear-gradient(135deg, #10b981 0%, #047857 100%)',
    'linear-gradient(135deg, #f59e0b 0%, #b45309 100%)',
    'linear-gradient(135deg, #ec4899 0%, #be185d 100%)',
    'linear-gradient(135deg, #8b5cf6 0%, #6d28d9 100%)'
  ]
  return gradients[id % gradients.length]
}

/* --- 工作流事件控制 --- */
const approveCorp = (corp: any) => {
  corp.status = 'approved'
  corp.shieldStatus = 'bound'
  corp.shieldKey = corp.shieldKey || 'SM4-KEY-GEN-' + Math.random().toString(36).substr(2, 9).toUpperCase() + '-2026'
  if (selectedCorp.value && selectedCorp.value.id === corp.id) {
    selectedCorp.value = { ...corp }
  }
}

const rejectCorp = (corp: any) => {
  corp.status = 'rejected'
  corp.shieldStatus = 'unbound'
  corp.shieldKey = ''
  if (selectedCorp.value && selectedCorp.value.id === corp.id) {
    selectedCorp.value = { ...corp }
  }
}

const freezeCorp = (corp: any) => {
  corp.status = 'frozen'
  if (selectedCorp.value && selectedCorp.value.id === corp.id) {
    selectedCorp.value = { ...corp }
  }
}

const unfreezeCorp = (corp: any) => {
  corp.status = 'approved'
  if (selectedCorp.value && selectedCorp.value.id === corp.id) {
    selectedCorp.value = { ...corp }
  }
}

const reconsiderCorp = (corp: any) => {
  corp.status = 'pending'
  if (selectedCorp.value && selectedCorp.value.id === corp.id) {
    selectedCorp.value = { ...corp }
  }
}
</script>

<style scoped>
.enterprise-page-wrapper {
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
.card-indicator.pending { background-color: rgba(245, 158, 11, 0.1); color: #f59e0b; }
.card-indicator.approved { background-color: rgba(16, 185, 129, 0.1); color: #10b981; }
.card-indicator.frozen { background-color: rgba(107, 114, 128, 0.1); color: var(--text-secondary); }


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
.tab-badge.yellow {
  background-color: rgba(245, 158, 11, 0.15);
  color: #f59e0b;
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

.corp-profile {
  display: flex;
  gap: 10px;
  align-items: center;
}
.corp-avatar {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  font-weight: bold;
}
.corp-name {
  font-weight: 700;
  color: var(--text-primary);
}
.corp-code {
  font-size: 10px;
  color: var(--text-secondary);
  font-family: monospace;
  margin-top: 2px;
}

.env-badges {
  display: flex;
  gap: 4px;
  flex-wrap: wrap;
}
.env-badge {
  background-color: rgba(107, 114, 128, 0.05);
  border: 1px solid rgba(107, 114, 128, 0.1);
  color: var(--text-primary);
  font-size: 10px;
  padding: 1px 5px;
  border-radius: 5px;
}

/* AI 置信度 */
.score-container {
  display: flex;
  flex-direction: column;
  gap: 4px;
}
.score-text {
  font-size: 11px;
  font-weight: 800;
  font-family: monospace;
}
.score-bar-bg {
  width: 50px;
  height: 4px;
  background-color: rgba(0, 0, 0, 0.04);
  border-radius: 2px;
}
:root.dark .score-bar-bg { background-color: rgba(255, 255, 255, 0.04); }
.score-bar-fill { height: 100%; border-radius: 2px; }

/* 密盾状态 */
.shield-badge {
  font-size: 10px;
  font-weight: 700;
  padding: 2px 6px;
  border-radius: 5px;
}
.shield-badge.bound {
  background-color: rgba(16, 185, 129, 0.08);
  color: #10b981;
}
.shield-badge.unbound {
  background-color: rgba(107, 114, 128, 0.08);
  color: var(--text-secondary);
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
.status-dot-label.pending { color: #f59e0b; }
.status-dot-label.pending .dot { background-color: #f59e0b; }
.status-dot-label.approved { color: #10b981; }
.status-dot-label.approved .dot { background-color: #10b981; }
.status-dot-label.rejected { color: #ef4444; }
.status-dot-label.rejected .dot { background-color: #ef4444; }
.status-dot-label.frozen { color: #6b7280; }
.status-dot-label.frozen .dot { background-color: #6b7280; }

/* 快捷操作 */
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

.btn-action.warning {
  color: #f59e0b;
  border-color: rgba(245, 158, 11, 0.15);
  background-color: rgba(245, 158, 11, 0.04);
}
.btn-action.warning:hover { background-color: rgba(245, 158, 11, 0.08); }

.btn-action.success-outline {
  color: #10b981;
  border-color: #10b981;
  background-color: transparent;
}
.btn-action.success-outline:hover { background-color: rgba(16, 185, 129, 0.05); }

.btn-action.info {
  color: #3b82f6;
  border-color: rgba(59, 130, 246, 0.15);
  background-color: rgba(59, 130, 246, 0.04);
}
.btn-action.info:hover { background-color: rgba(59, 130, 246, 0.08); }

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
  backdrop-filter: blur(10px); /* 磨砂玻璃特效 */
  -webkit-backdrop-filter: blur(10px);
  z-index: 999;
  display: flex;
  justify-content: flex-end;
}

.drawer-panel {
  width: 620px; /* 大幅拓宽，容纳更专业深度的内容 */
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
.status-indicator-badge.pending { background-color: rgba(245, 158, 11, 0.12); color: #f59e0b; }
.status-indicator-badge.approved { background-color: rgba(16, 185, 129, 0.12); color: #10b981; }
.status-indicator-badge.rejected { background-color: rgba(239, 68, 68, 0.12); color: #ef4444; }
.status-indicator-badge.frozen { background-color: rgba(107, 114, 128, 0.12); color: #6b7280; }

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
  transition: color 0.2s;
}
.close-btn:hover {
  color: var(--text-primary);
}

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

/* 2列网格，更合理的信息密度 */
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

/* 信创材料与 OCR */
.materials-box {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.material-preview-card {
  display: flex;
  align-items: center;
  gap: 12px;
  background-color: var(--bg-main);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  padding: 12px 16px;
}
.pdf-icon {
  font-size: 24px;
}
.pdf-info {
  flex: 1;
}
.pdf-info .file-name {
  font-size: 12px;
  font-weight: bold;
  color: var(--text-primary);
}
.pdf-info .file-meta {
  font-size: 10px;
  color: var(--text-secondary);
  margin-top: 2px;
}
.btn-preview-link {
  font-size: 11px;
  color: #3b82f6;
  text-decoration: none;
  font-weight: bold;
}

.ocr-report-box {
  background-color: rgba(16, 185, 129, 0.03);
  border: 1px dashed rgba(16, 185, 129, 0.2);
  border-radius: 10px;
  padding: 12px;
}
.ocr-report-box .report-title {
  font-size: 11px;
  font-weight: bold;
  color: #10b981;
}
.ocr-report-box .report-metrics {
  display: flex;
  gap: 16px;
  font-size: 11px;
  color: var(--text-secondary);
  margin-top: 6px;
  flex-wrap: wrap;
}

/* 模块4：大模型研判 */
.ai-deep-card {
  display: flex;
  gap: 16px;
  background-color: rgba(59, 130, 246, 0.04);
  border: 1px solid rgba(59, 130, 246, 0.08);
  border-radius: 14px;
  padding: 16px;
}
.ai-score-ring {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  background: conic-gradient(#10b981 0% 90%, rgba(0,0,0,0.05) 90% 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #10b981;
  flex-shrink: 0;
  box-shadow: inset 0 0 0 4px var(--bg-card);
}
.ai-score-ring .score {
  font-size: 16px;
  font-weight: 900;
}
.ai-score-ring .unit {
  font-size: 8px;
  font-weight: bold;
}
.ai-verdict .verdict-title {
  font-size: 12px;
  font-weight: bold;
  color: var(--text-primary);
}
.ai-verdict .verdict-desc {
  font-size: 11px;
  color: var(--text-secondary);
  line-height: 1.5;
  margin-top: 4px;
}

/* 模块5：密盾配置 */
.shield-settings-box {
  background-color: var(--bg-main);
  border: 1px solid var(--border-color);
  padding: 16px;
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.setting-row {
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.setting-label {
  font-size: 11px;
  color: var(--text-secondary);
}
.algo-badge {
  display: inline-block;
  font-size: 10px;
  font-weight: 700;
  background-color: var(--bg-card);
  border: 1px solid var(--border-color);
  padding: 2px 8px;
  border-radius: 6px;
  margin-right: 6px;
  width: fit-content;
}
.key-display {
  background-color: var(--bg-card);
  border: 1px solid var(--border-color);
  padding: 10px;
  border-radius: 8px;
  font-size: 11px;
  color: var(--text-primary);
  text-align: center;
  word-break: break-all;
}
.key-display.empty {
  color: var(--text-secondary);
  font-style: italic;
  border-style: dashed;
}

/* 抽屉底部工作栏 */
.drawer-footer {
  padding: 20px 24px;
  border-top: 1px solid var(--border-color);
  background-color: var(--bg-card);
}
.footer-btn-group {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 12px;
}
.btn-primary-large {
  width: 100%;
  padding: 12px;
  background-color: #3b82f6;
  color: white;
  border: none;
  border-radius: 10px;
  font-weight: bold;
  cursor: pointer;
  font-size: 13px;
  transition: opacity 0.2s;
}
.btn-primary-large:hover { opacity: 0.9; }

.btn-secondary-large {
  width: 100%;
  padding: 12px;
  background: none;
  border: 1px solid var(--border-color);
  color: var(--text-primary);
  border-radius: 10px;
  font-weight: bold;
  cursor: pointer;
  font-size: 13px;
  transition: background-color 0.2s;
}
.btn-secondary-large:hover { background-color: var(--bg-main); }

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


/* 全屏遮罩与抽屉过渡动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.25s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.slide-enter-active,
.slide-leave-active {
  transition: transform 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}
.slide-enter-from,
.slide-leave-to {
  transform: translateX(100%);
}
</style>