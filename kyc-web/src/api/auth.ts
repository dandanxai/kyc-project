import request from '@/utils/request'

// ==================== 💎 TypeScript 类型声明 ====================

// 1. 注册参数模型 (对应后端 CandidateRegisterDTO)
export interface RegisterParams {
phone?: string;    // 手机号注册时传递
email?: string;    // 邮箱注册时传递
password: string;  // 必填的纯净密码
}

// 2. 登录参数模型 (对应后端 LoginDTO)
export interface LoginParams {
account: string;   // 账户（可以填手机号，也可以填邮箱）
password: string;  // 明文密码
}

// 3. 通用 API 返回体模型 (对应后端的 CommonResult<T>)
export interface ApiResponse<T = any> {
code: number;
message: string;
data: T;
}

// 4. 登录成功返回的用户数据载荷 (对应后端的 LoginResultVO)
export interface LoginResultData {
token: string;
username: string;
nickname?: string;
avatar?: string;
email?: string;
phone?: string;
role: string;
}

// 5. 个人详细信息返回体 (完全对应后端实体类 Candidate)
export interface CandidateProfile {
    username: string;
    nickname?: string;
    phone?: string;
    email?: string;
    avatar?: string;
    status: number;
    role: string;
    jobStatus?: string; // 对应后端的 jobStatus 驼峰命名
}

// 6. 更新基本资料参数载荷
export interface UpdateProfileParams {
id: number;
nickname?: string;
phone?: string;
email?: string;
avatar?: string;
jobStatus?: string; // 对应求职状态选择
}

// 7. 更新密码参数载荷 (🎯 删除了 id)
export interface UpdatePasswordParams {
    currentPassword?: string; // 旧密码，用于后端安全校验
    newPassword?: string;     // 新密码
}

// ==================== 🚀 规范的接口方法暴露 ====================

/**
 * 个人用户注册（手机号或邮箱二选一）
 */
export function registerCandidate(data: RegisterParams): Promise<ApiResponse<null>> {
return request({
    url: '/portal/auth/register',
    method: 'post',
    data
})
}

/**
 * 个人用户登录
 */
export function loginCandidate(data: LoginParams): Promise<ApiResponse<LoginResultData>> {
return request({
    url: '/portal/auth/login',
    method: 'post',
    data
})
} // 🎯 成功闭合 loginCandidate 函数

/**
 * 8. 获取当前登录用户的最新个人详细配置 (🎯 无需传 id，后端自动从 Token 解析)
 * 路由：GET /api/portal/candidate/profile
 */
export function getCandidateProfile(): Promise<ApiResponse<CandidateProfile>> {
    return request({
    url: '/portal/candidate/profile', // 🎯 丢掉 URL 末尾的 ID 占位
    method: 'get'
    })
}

/**
 * 9. 更新个人基本资料 (🎯 无需传 id)
 * 路由：PUT /api/portal/candidate/profile/update
 */
export function updateCandidateProfile(data: Partial<CandidateProfile>): Promise<ApiResponse<CandidateProfile>> {
    return request({
    url: '/portal/candidate/profile/update',
    method: 'put',
    data // 🎯 data 里不再携带 id，后端直接用 Token 的 ID 覆盖
    })
}

/**
 * 10. 修改个人账户密码 (🎯 无需传 id)
 * 路由：PUT /api/portal/candidate/password/update
 */
export function updateCandidatePassword(data: UpdatePasswordParams): Promise<ApiResponse<null>> {
    return request({
    url: '/portal/candidate/password/update',
    method: 'put',
    data // 🎯 data 里只有旧密码和新密码，不带 id
    })
}