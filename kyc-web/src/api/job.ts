// api/job.ts
import request from '@/utils/request' // 🎯 引入你项目里封装好的 axios 实例

// ==================== 🚀 规范的信创岗位接口方法暴露 ====================

/**
 * 1. 岗位文档/附件临时上传暂存 (MultipartForm 格式)
 * @param file 选中的二进制文件对象
 * @returns 包含 tempPath (相对路径) 和 previewUrl (预览链接) 的对象
 */
export function uploadJobTempFile(file: File) {
const formData = new FormData()
formData.append('file', file)
return request({
    url: '/enterprise/job/temp-upload',
    method: 'post',
    data: formData,
    headers: {
    'Content-Type': 'multipart/form-data' // 🎯 必须指定为表单类型，以便后端 MultipartFile 接收
    }
})
}

/**
 * 2. 发布新职位 (携带暂存文件路径 tempFilePath)
 * @param data 职位基础数据 + 规格 + tempFilePath
 */
export function createJob(data: any) {
return request({
    url: '/enterprise/job/create',
    method: 'post',
    data: data
})
}

/**
 * 3. 更新职位信息
 * @param jobId 职位主键 ID
 * @param data 职位修改数据
 */
export function updateJob(jobId: number | string, data: any) {
return request({
    url: `/enterprise/job/update/${jobId}`,
    method: 'put',
    data: data
})
}

/**
 * 4. 物理删除指定职位
 * @param jobId 职位主键 ID
 */
export function deleteJob(jobId: number | string) {
return request({
    url: `/enterprise/job/delete/${jobId}`,
    method: 'delete'
})
}

/**
 * 5. 获取单个职位详情 (包含 requirementSpec JSON 数据)
 * @param jobId 职位主键 ID
 */
export function getJobDetail(jobId: number | string) {
return request({
    url: `/enterprise/job/detail/${jobId}`,
    method: 'get'
})
}

/**
 * 6. 分页获取企业自身发布的职位列表
 * @param params { current: 页码, size: 每页条数, status: 状态 }
 */
export function getJobPage(params: { current: number; size: number; status?: number }) {
return request({
    url: '/enterprise/job/page',
    method: 'get',
    params: params
})
}

/**
 * 7. 获取暂存岗位附件的二进制流 (用于 docx-preview 与 PDF 渲染)
 * @param filePath 物理文件相对路径 (e.g. "uploads/temp/xxx.docx")
 */
export function getJobTempFileBlob(filePath: string) {
    return request({
    url: '/enterprise/job/temp-preview',
    method: 'get',
    params: { path: filePath },
    responseType: 'blob' // 🎯 核心设置：极其重要，必须指定为 blob，否则解析出来的就是乱码文本
    })
}