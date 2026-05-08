// src/utils/toast.ts

/**
 * 🍎 Apple 风格全局极简消息提示（Toast）
 * 支持在任何 Vue 页面或 TS/JS 脚本中直接调用
 */
export const toast = {
    /**
     * 成功提示（绿色圆点）
     */
    success(message: string) {
    this.show(message, 'success')
    },

    /**
     * 错误提示（红色圆点）
     */
    error(message: string) {
    this.show(message, 'error')
    },

    /**
     * 警告提示（黄色圆点）
     */
    warning(message: string) {
    this.show(message, 'warning')
    },

    /**
     * 核心渲染逻辑
     */
    show(message: string, type: 'success' | 'error' | 'warning' = 'error') {
    // 1. 获取或创建全局 Toast 容器
    let container = document.getElementById('apple-toast-container')
    if (!container) {
        container = document.createElement('div')
        container.id = 'apple-toast-container'
        Object.assign(container.style, {
        position: 'fixed',
        top: '24px',
        left: '50%',
        transform: 'translateX(-50%)',
        zIndex: '99999',
        display: 'flex',
        flexDirection: 'column',
        gap: '12px',
        pointerEvents: 'none'
        })
        document.body.appendChild(container)
    }

    // 2. 创建单条卡片
    const toastCard = document.createElement('div')
    
    const colors = {
        success: '#34C759', // 经典苹果绿
        error: '#FF3B30',   // 经典苹果红
        warning: '#FF9500'  // 经典苹果橙
    }

    // 拟物扁平化、毛玻璃特效（完美匹配 Apple 设计规范）
    Object.assign(toastCard.style, {
        padding: '12px 20px',
        background: 'rgba(255, 255, 255, 0.85)',
        backdropFilter: 'blur(20px)',
        webkitBackdropFilter: 'blur(20px)',
        border: '1px solid rgba(255, 255, 255, 0.4)',
        borderRadius: '16px',
        boxShadow: '0 8px 32px 0 rgba(0, 0, 0, 0.08)',
        display: 'flex',
        alignItems: 'center',
        gap: '10px',
        fontSize: '14px',
        color: '#1D1D1F', // 苹果标志性深色文字
        fontWeight: '500',
        minWidth: '240px',
        justifyContent: 'center',
        transition: 'all 0.4s cubic-bezier(0.16, 1, 0.3, 1)', // 高级阻尼回弹过渡
        opacity: '0',
        transform: 'translateY(-20px)'
    })

    toastCard.innerHTML = `
        <span style="width: 8px; height: 8px; border-radius: 50%; background: ${colors[type]}; display: inline-block;"></span>
        <span>${message}</span>
    `

    container.appendChild(toastCard)

    // 3. 触发淡入及回弹
    requestAnimationFrame(() => {
        toastCard.style.opacity = '1'
        toastCard.style.transform = 'translateY(0)'
    })

    // 4. 定时淡出并销毁
    setTimeout(() => {
        toastCard.style.opacity = '0'
        toastCard.style.transform = 'translateY(-10px)'
        setTimeout(() => {
        toastCard.remove()
        }, 400)
    }, 2500)
    }
}