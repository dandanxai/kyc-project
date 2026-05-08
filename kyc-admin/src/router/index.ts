import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: Array<RouteRecordRaw> = [
{
    path: '/',
    redirect: '/login'
},
{
    path: '/login',
    name: 'Login',
    component: () => import('../views/auth/Login.vue'),
    meta: { title: '安全授权登录 - Kirin Admin' }
},

// ================= 管理端 (Admin) 5大核心模块 =================
{
    path: '/admin',
    component: () => import('../layout/AdminLayout.vue'),
    redirect: '/admin/dashboard',
    children: [
    // 1. 控制大盘
    {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: () => import('../views/admin/Dashboard.vue'),
        meta: { title: '控制大盘 - Kirin Admin', role: 'admin' }
    },
    
    // 2. 准入与用户管理 (子页面)
    {
        path: 'identity/enterprise',
        name: 'AdminEnterprise',
        component: () => import('../views/admin/identity/Enterprise.vue'),
        meta: { title: '企业资质审核 - Kirin Admin', role: 'admin' }
    },
    {
        path: 'identity/candidate',
        name: 'AdminCandidate',
        component: () => import('../views/admin/identity/Candidate.vue'),
        meta: { title: '个人求职管控 - Kirin Admin', role: 'admin' }
    },

    // 3. 信创适配合规 (子页面)
    {
        path: 'compliance/certs',
        name: 'AdminCerts',
        component: () => import('../views/admin/compliance/Certs.vue'),
        meta: { title: '兼容性证书核验 - Kirin Admin', role: 'admin' }
    },
    {
        path: 'compliance/tags',
        name: 'AdminTags',
        component: () => import('../views/admin/compliance/Tags.vue'),
        meta: { title: '信创职位标签发放 - Kirin Admin', role: 'admin' }
    },

    // 4. AI 智能匹配引擎 (子页面)
    {
        path: 'ai-tuning/vector',
        name: 'AdminVector',
        component: () => import('../views/admin/ai-tuning/Vector.vue'),
        meta: { title: '向量匹配调试 - Kirin Admin', role: 'admin' }
    },
    {
        path: 'ai-tuning/model',
        name: 'AdminModel',
        component: () => import('../views/admin/ai-tuning/Model.vue'),
        meta: { title: '本地模型状态 - Kirin Admin', role: 'admin' }
    },

    // 5. 系统设置与审计 (子页面)
    {
        path: 'settings/sub-admin',
        name: 'AdminSubAccount',
        component: () => import('../views/admin/settings/SubAdmin.vue'),
        meta: { title: '管理员分权 - Kirin Admin', role: 'admin' }
    },
    {
        path: 'settings/audit',
        name: 'AdminAudit',
        component: () => import('../views/admin/settings/Audit.vue'),
        meta: { title: '操作日志审计 - Kirin Admin', role: 'admin' }
    }
    ]
}
]

const router = createRouter({
history: createWebHistory(),
routes
})

// 路由拦截守卫
router.beforeEach((to, from, next) => {
document.title = (to.meta.title as string) || 'Kirin Admin'
const token = localStorage.getItem('admin_token')

// 安全防爆：访问管理端但没有 token 凭证，打回登录页
if (to.path.startsWith('/admin') && !token) {
    next('/login')
} else {
    next()
}
})

export default router