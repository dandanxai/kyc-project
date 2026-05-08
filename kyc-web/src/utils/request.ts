// src/utils/request.ts

import axios from 'axios'
import { toast } from '@/utils/toast' // 🎯 引入全局通用提示工具

const request = axios.create({
baseURL: 'http://localhost:8080/api', 
timeout: 10000,
headers: {
    'Content-Type': 'application/json'
}
})

// 请求拦截器
request.interceptors.request.use(
(config) => {
    const token = localStorage.getItem('auth_token')
    if (token && config.headers) {
    config.headers['Authorization'] = token 
    }
    return config
},
(error) => {
    return Promise.reject(error)
}
)

// 响应拦截器
request.interceptors.response.use(
(response) => {
    const res = response.data
    
    if (res.code === 200) {
    return res
    }
    
    if (res.code === 401) {
    localStorage.removeItem('auth_token')
    localStorage.removeItem('user_info')
    
    // 🎯 直接调用通用错误提示
    toast.error(res.message || '您的登录已过期，请重新登录')
    
    setTimeout(() => {
        window.location.href = '/login'
    }, 1500)
    
    return Promise.reject(new Error(res.message || 'Unauthorized'))
    }

    // 参数错误等业务异常
    toast.warning(res.message || '请求数据异常，请重试')
    return Promise.reject(new Error(res.message || 'Validate Failed'))
},
(error) => {
    const status = error.response?.status
    if (status === 401) {
    localStorage.removeItem('auth_token')
    localStorage.removeItem('user_info')
    toast.error('登录状态失效，请重新登录')
    setTimeout(() => {
        window.location.href = '/login'
    }, 1500)
    } else {
    toast.error(error.message || '网络连接失败，请检查服务是否开启')
    }
    return Promise.reject(error)
}
)

export default request