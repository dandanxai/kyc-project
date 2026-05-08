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
id: number;
username: string;
nickname?: string;
phone?: string;
email?: string;
avatar?: string;
status: number;
role: string;
jobStatus?: string; // 对应后端的 jobStatus 驼峰命名
createTime?: string;
updateTime?: string;
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

// 7. 更新密码参数载荷
export interface UpdatePasswordParams {
id: number;
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
 * 获取当前登录用户的最新个人详细配置 (通过用户主键 ID)
 */
export function getCandidateProfile(id: number | string): Promise<ApiResponse<CandidateProfile>> {
return request({
    url: `/portal/candidate/profile/${id}`,
    method: 'get'
})
}

/**
 * 更新个人基本资料 (数据安全写入达梦数据库)
 */
export function updateCandidateProfile(data: UpdateProfileParams): Promise<ApiResponse<CandidateProfile>> {
return request({
    url: '/portal/candidate/profile/update',
    method: 'put',
    data
})
}

/**
 * 修改账户登录密码 (支持旧密码校验升级)
 */
export function updateCandidatePassword(data: UpdatePasswordParams): Promise<ApiResponse<null>> {
return request({
    url: '/portal/candidate/password/update',
    method: 'put',
    data
})
}