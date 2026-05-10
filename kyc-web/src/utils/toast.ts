// src/utils/toast.ts

/**
 * 🍎 Apple 风格全局极简消息提示（Toast）与自定义优雅确认框（Confirm）
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
                gap: '10px',
                pointerEvents: 'none'
            })
            document.body.appendChild(container)
        }

        // 2. 创建单条 Toast 卡片
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

        // 左侧提示彩色圆点
        const dot = document.createElement('span')
        Object.assign(dot.style, {
            width: '8px',
            height: '8px',
            borderRadius: '50%',
            backgroundColor: colors[type],
            boxShadow: `0 0 8px ${colors[type]}`
        })

        // 文本
        const text = document.createElement('span')
        text.innerText = message

        toastCard.appendChild(dot)
        toastCard.appendChild(text)
        container.appendChild(toastCard)

        // 3. 显式动画触发
        requestAnimationFrame(() => {
            toastCard.style.opacity = '1'
            toastCard.style.transform = 'translateY(0)'
        })

        // 4. 定时销毁
        setTimeout(() => {
            toastCard.style.opacity = '0'
            toastCard.style.transform = 'translateY(-20px)'
            setTimeout(() => {
                toastCard.remove()
            }, 400)
        }, 2800)
    },

    /**
     * 🍎 拟物扁平化、毛玻璃自定义 Apple 风格确认弹窗
     */
    confirm(title: string, message: string): Promise<boolean> {
        return new Promise((resolve) => {
            // 1. 创建背景遮罩（毛玻璃）
            const mask = document.createElement('div')
            Object.assign(mask.style, {
                position: 'fixed',
                inset: '0',
                backgroundColor: 'rgba(0, 0, 0, 0.15)',
                backdropFilter: 'blur(8px)',
                webkitBackdropFilter: 'blur(8px)',
                zIndex: '99999',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                opacity: '0',
                transition: 'opacity 0.3s cubic-bezier(0.16, 1, 0.3, 1)'
            })

            // 2. 创建确认框容器
            const dialog = document.createElement('div')
            Object.assign(dialog.style, {
                width: '90%',
                maxWidth: '400px',
                background: 'rgba(255, 255, 255, 0.9)',
                backdropFilter: 'blur(30px)',
                webkitBackdropFilter: 'blur(30px)',
                border: '1px solid rgba(255, 255, 255, 0.5)',
                borderRadius: '24px',
                boxShadow: '0 20px 60px rgba(0, 0, 0, 0.1)',
                padding: '24px',
                display: 'flex',
                flexDirection: 'column',
                gap: '16px',
                transform: 'scale(0.9) translateY(10px)',
                transition: 'all 0.3s cubic-bezier(0.16, 1, 0.3, 1)'
            })

            // 3. 内容：标题与正文
            const titleEl = document.createElement('h3')
            titleEl.innerText = title
            Object.assign(titleEl.style, {
                fontSize: '17px',
                fontWeight: '700',
                color: '#1D1D1F',
                textAlign: 'center',
                margin: '0'
            })

            const messageEl = document.createElement('p')
            messageEl.innerText = message
            Object.assign(messageEl.style, {
                fontSize: '13px',
                fontWeight: '400',
                color: '#86868B',
                textAlign: 'center',
                lineHeight: '1.5',
                margin: '0'
            })

            // 4. 操作按钮容器
            const btnContainer = document.createElement('div')
            Object.assign(btnContainer.style, {
                display: 'flex',
                gap: '12px',
                marginTop: '8px'
            })

            // 取消按钮 (经典 Apple 灰)
            const cancelBtn = document.createElement('button')
            cancelBtn.innerText = '取消'
            Object.assign(cancelBtn.style, {
                flex: '1',
                padding: '12px',
                border: 'none',
                borderRadius: '12px',
                backgroundColor: 'rgba(0, 0, 0, 0.05)',
                color: '#007AFF',
                fontSize: '14px',
                fontWeight: '600',
                cursor: 'pointer',
                transition: 'background-color 0.2s'
            })
            cancelBtn.onmouseenter = () => cancelBtn.style.backgroundColor = 'rgba(0, 0, 0, 0.08)'
            cancelBtn.onmouseleave = () => cancelBtn.style.backgroundColor = 'rgba(0, 0, 0, 0.05)'

            // 确认按钮 (高亮危险红)
            const confirmBtn = document.createElement('button')
            confirmBtn.innerText = '确认删除'
            Object.assign(confirmBtn.style, {
                flex: '1',
                padding: '12px',
                border: 'none',
                borderRadius: '12px',
                backgroundColor: '#FF3B30',
                color: '#FFFFFF',
                fontSize: '14px',
                fontWeight: '600',
                cursor: 'pointer',
                transition: 'background-color 0.2s',
                boxShadow: '0 4px 12px rgba(255, 59, 48, 0.2)'
            })
            confirmBtn.onmouseenter = () => confirmBtn.style.backgroundColor = '#E03126'
            confirmBtn.onmouseleave = () => confirmBtn.style.backgroundColor = '#FF3B30'

            // 5. 组合 DOM 结构
            btnContainer.appendChild(cancelBtn)
            btnContainer.appendChild(confirmBtn)
            dialog.appendChild(titleEl)
            dialog.appendChild(messageEl)
            dialog.appendChild(btnContainer)
            mask.appendChild(dialog)
            document.body.appendChild(mask)

            // 6. 出场动效
            requestAnimationFrame(() => {
                mask.style.opacity = '1'
                dialog.style.transform = 'scale(1) translateY(0)'
            })

            // 7. 关闭逻辑封装
            const close = (result: boolean) => {
                mask.style.opacity = '0'
                dialog.style.transform = 'scale(0.9) translateY(10px)'
                setTimeout(() => {
                    mask.remove()
                    resolve(result)
                }, 300)
            }

            // 8. 绑定事件
            confirmBtn.onclick = () => close(true)
            cancelBtn.onclick = () => close(false)
            mask.onclick = (e) => {
                if (e.target === mask) close(false)
            }
        })
    }
}