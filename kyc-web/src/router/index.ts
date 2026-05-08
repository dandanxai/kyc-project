import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import { toast } from '@/utils/toast' // 🎯 引入你封装的高级 Apple 风格提示工具

const routes: Array<RouteRecordRaw> = [
  // 1. 根路径重定向
  {
    path: '/',
    redirect: '/login'
  },

  // 2. 鉴权相关页面（登录、注册）
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
  
  // 3. 个人端 (Candidate)
  {
    path: '/candidate',
    component: () => import('../layout/MainLayout.vue'),
    redirect: '/candidate/home', 
    children: [
      {
        path: 'home',
        name: 'CandidateHome',
        component: () => import('../views/candidate/Home.vue'), 
        // 🎯 isPublic: true 代表公开大厅，未登录也能直接看，不被拦截
        meta: { title: '人才广场 - 麒麟生态', role: 'candidate', isPublic: true }
      },
      {
        path: 'resume',
        name: 'CandidateResume',
        component: () => import('../views/candidate/Resume.vue'),
        meta: { title: '智能画像 & 能力建模', role: 'candidate' }
      },
      {
        path: 'jobs',
        name: 'CandidateJobs',
        component: () => import('../views/candidate/Jobs.vue'),
        meta: { title: '精准匹配 - 职位推荐', role: 'candidate' }
      },
      {
        path: 'messages',
        name: 'CandidateMessages',
        component: () => import('../views/candidate/Messages.vue'),
        meta: { title: '消息中心', role: 'candidate' }
      },
      {
        path: 'interview',
        name: 'CandidateInterview',
        component: () => import('../views/candidate/Interview.vue'),
        meta: { title: 'AI 模拟面试训练', role: 'candidate' }
      },
      {
        path: 'insights',
        name: 'CandidateInsights',
        component: () => import('../views/candidate/Insights.vue'),
        meta: { title: '产业洞察 & 趋势分析', role: 'candidate' }
      },
      {
        path: 'settings',
        name: 'CandidateSettings',
        component: () => import('../views/candidate/Settings.vue'),
        meta: { title: '个人设置 & 系统安全', role: 'candidate' }
      },
      {
        path: 'dashboard',
        redirect: '/candidate/resume'
      }
    ]
  },

  // 4. 企业端 (Enterprise)
  {
    path: '/enterprise',
    component: () => import('../layout/MainLayout.vue'),
    redirect: '/enterprise/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'EnterpriseDashboard',
        component: () => import('../views/enterprise/Dashboard.vue'),
        meta: { title: '企业管理大盘', role: 'enterprise' }
      },
      {
        path: 'match',
        name: 'EnterpriseMatch',
        component: () => import('../views/enterprise/Match.vue'),
        meta: { title: 'AI 智能双向匹配推荐', role: 'enterprise' }
      },
      {
        path: 'talent-pool',
        name: 'TalentPool',
        component: () => import('../views/enterprise/TalentPool.vue'),
        meta: { title: '人才公海中心', role: 'enterprise' }
      },
      {
        path: 'jobs-manage',
        name: 'JobsManage',
        component: () => import('../views/enterprise/JobsManage.vue'),
        meta: { title: '岗位发布控制台', role: 'enterprise' }
      },
      {
        path: 'messages',
        name: 'EnterpriseMessages',
        component: () => import('../views/candidate/Messages.vue'), // 复用即时通讯组件
        meta: { title: '即时沟通反馈', role: 'enterprise' }
      },
      {
        path: 'settings',
        name: 'EnterpriseSettings',
        component: () => import('../views/enterprise/Settings.vue'), // 企业专属设置
        meta: { title: '企业信息 & 安全设置', role: 'enterprise' }
      }
    ]
  },

  // 5. 管理端 (Admin)
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
  },

  // 6. 404 错误展示页（必须注册在通配符路由之前！）
  {
    path: '/404',
    name: 'NotFound',
    component: () => import('../views/error/NotFound.vue'),
    meta: { title: '页面未找到 - KYC系统', isPublic: true } // 公开页面，未登录也允许查看
  },

  // 7. 终极兜底安全网：通配符匹配（必须严格放在数组的最底端！）
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

/**
 * 🛡️ 全局路由前置守卫：统一的权限验证与拦截中心
 */
router.beforeEach((to, from, next) => {
  // 1. 动态切换浏览器标签页标题
  if (to.meta.title) {
    document.title = to.meta.title as string
  }

  // 2. 从本地存储读取用户凭证 Token
  const token = localStorage.getItem('auth_token')

  // 3. 拦截状态判定
  const isAuthPage = to.path === '/login' || to.path === '/register'

  if (token) {
    // 🎯 【已登录状态】：禁止重复访问登录或注册页，直接强制推进到系统首页
    if (isAuthPage) {
      next('/candidate/home')
    } else {
      next() // 允许访问其他受保护路由
    }
  } else {
    // 🎯 【未登录状态】
    // 如果访问的是登录/注册页，或者声明了 isPublic 允许公开浏览的页面（如：人才广场大厅）
    if (isAuthPage || to.meta.isPublic) {
      next() // 放行
    } else {
      // 试图去往其他受限的核心功能页（如简历画像、AI面试、企业端等）
      
      // 1. 弹出你专属的 Apple 风高级悬浮 Toast 提示，取代丑陋的 alert 弹窗
      toast.warning('请先登录以解锁更多核心功能')

      // 2. 重定向回登录页面，并携带 redirect 路径，以便登录成功后无缝带用户原路返回
      next({
        path: '/login',
        query: { redirect: to.fullPath }
      })
    }
  }
})

export default router