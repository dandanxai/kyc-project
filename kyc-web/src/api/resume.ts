import request from '@/utils/request' // 🎯 引入你项目里封装好的 axios 实例

// ==================== 🚀 规范的接口方法暴露 ====================

/**
 * 1. 上传简历文件 (MultipartForm 格式)
 * @param file 选中的二进制文件对象
 */
export function uploadResume(file: File) {
const formData = new FormData()
formData.append('file', file)
return request({
    url: '/portal/candidate/resume/upload',
    method: 'post',
    data: formData,
    headers: {
    'Content-Type': 'multipart-form-data' // 必须指定为表单类型，以便后端 MultipartFile 接收
    }
})
}

/**
 * 2. 获取当前用户的所有简历列表
 */
export function getResumeList() {
return request({
    url: '/portal/candidate/resume/list',
    method: 'get'
})
}

/**
 * 3. 将某份简历设为默认投递简历
 * @param resumeId 简历主键ID
 */
export function setDefaultResume(resumeId: number) {
return request({
    url: `/portal/candidate/resume/default/${resumeId}`,
    method: 'put'
})
}

/**
 * 4. 删除指定的单个简历记录 (🎯 确保此导出名称为 deleteResume)
 * @param resumeId 简历主键ID
 */
export function deleteResume(resumeId: number) {
return request({
    url: `/portal/candidate/resume/delete/${resumeId}`,
    method: 'delete'
})
}

/**
 * 5. 批量删除简历记录 (🎯 精准对接后端的 /delete-batch 路由)
 * @param resumeIds 简历主键ID数组
 */
export function deleteResumes(resumeIds: number[]) {
return request({
    url: '/portal/candidate/resume/delete-batch', // 👈 改为 delete-batch
    method: 'delete',
    data: resumeIds // 数组作为 request body 传输
})
}

/**
 * 7. 智能解析简历画像（通过 Java 后端代理请求 Python AI 引擎）
 * @param resumeId 简历主键ID
 */
export function parseResumeAi(resumeId: number) {
return request({
    url: `/portal/candidate/resume/parse/${resumeId}`,
    method: 'get'
})
}