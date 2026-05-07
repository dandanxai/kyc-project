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
        meta: { title: '人才广场 - 麒麟生态', role: 'candidate' }
      },
      {
        path: 'resume', // 对应侧边栏：智能画像
        name: 'CandidateResume',
        component: () => import('../views/candidate/Resume.vue'),
        meta: { title: '智能画像 & 能力建模', role: 'candidate' }
      },
      // {
      //   path: 'jobs', // 对应侧边栏：精准匹配
      //   name: 'CandidateJobs',
      //   component: () => import('../views/candidate/Jobs.vue'),
      //   meta: { title: '精准匹配 - 职位推荐', role: 'candidate' }
      // },
      // {
      //   path: 'messages', // 对应侧边栏：即时互联
      //   name: 'CandidateMessages',
      //   component: () => import('../views/candidate/Messages.vue'),
      //   meta: { title: '消息中心', role: 'candidate' }
      // },
      // {
      //   path: 'interview', // 对应侧边栏：AI 模拟面试
      //   name: 'CandidateInterview',
      //   component: () => import('../views/candidate/Interview.vue'),
      //   meta: { title: 'AI 模拟面试训练', role: 'candidate' }
      // },
      // {
      //   path: 'insights', // 对应侧边栏：产业洞察
      //   name: 'CandidateInsights',
      //   component: () => import('../views/candidate/Insights.vue'),
      //   meta: { title: '产业洞察 & 趋势分析', role: 'candidate' }
      // },
      // 保留一个通用的 dashboard 路由作为备用或重定向目标
      {
        path: 'dashboard',
        redirect: '/candidate/resume'
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