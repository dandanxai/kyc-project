from fastapi import FastAPI, UploadFile, File, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from app.services.parser_service import resume_parser

app = FastAPI(
    title="KYC AI Core Engine",
    description="智能画像与双生建模微服务（FastAPI + DeepSeek）",
    version="2.5.0"
)

# 允许跨域（解决 web 端、admin 端本地调试端口阻碍）
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.get("/")
async def health_check():
    """
    健康检查端点
    """
    return {"status": "GREEN", "service": "kyc-ai-service"}


@app.post("/api/ai/resume/parse")
async def parse_resume(file: UploadFile = File(...)):
    """
    接收上传的简历（PDF/DOCX），在内存中解析纯文本后请求大模型，
    返回标准的结构化 JSON 画像。
    """
    # 校验文件后缀合法性
    filename = file.filename.lower()
    allowed_extensions = [".pdf", ".docx", ".doc"]

    file_ext = ""
    for ext in allowed_extensions:
        if filename.endswith(ext):
            file_ext = ext
            break

    if not file_ext:
        raise HTTPException(
            status_code=400,
            detail="上传失败：不支持的简历文件类型。仅支持 PDF / DOCX 格式。"
        )

    try:
        # 在内存中直接读取文件，不产生物理磁盘残留碎片，速度飞快
        file_bytes = await file.read()

        # 解析文本，由大模型重塑画像
        structured_data = await resume_parser.parse_and_analyze(file_bytes, file_ext)

        return {
            "code": 200,
            "success": True,
            "message": "AI 深度画像提取完毕",
            "data": structured_data
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))