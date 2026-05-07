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
    meta: { title: '登录 - KYC系统' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/auth/Register.vue'),
    meta: { title: '申请加入 - KYC系统' }
  },
  
  // 个人端 (Candidate)
  {
    path: '/candidate',
    component: () => import('../layout/MainLayout.vue'),
    redirect: '/candidate/home', 
    children: [
      {
        path: 'home',
        name: 'CandidateHome',
        component: () => import('../views/candidate/Home.vue'), 
        meta: { title: '首页 - 麒麟生态人才广场', role: 'candidate' }
      },
      {
        path: 'dashboard',
        name: 'CandidateDashboard',
        component: () => import('../views/candidate/Dashboard.vue'),
        meta: { title: '个人工作台 - 能力图谱', role: 'candidate' }
      }
    ]
  },

  // 企业端 (Enterprise)
  {
    path: '/enterprise',
    component: () => import('../layout/MainLayout.vue'),
    redirect: '/enterprise/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'EnterpriseDashboard',
        component: () => import('../views/enterprise/Dashboard.vue'),
        meta: { title: '企业管理后台', role: 'enterprise' }
      }
      // TalentPool 暂时注释，等创建文件后再开启
      /* {
        path: 'talent-pool',
        name: 'TalentPool',
        component: () => import('../views/enterprise/TalentPool.vue'),
        meta: { title: '人才公海', role: 'enterprise' }
      } */
    ]
  },

  // 管理端 (Admin)
  {
    path: '/admin',
    component: () => import('../layout/MainLayout.vue'),
    redirect: '/admin/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: () => import('../views/admin/Dashboard.vue'),
        meta: { title: '系统监控中心', role: 'admin' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router