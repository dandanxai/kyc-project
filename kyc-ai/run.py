import uvicorn
from app.config import settings

if __name__ == "__main__":
    # 动态调配端口，热重载模式（reload=True）适合开发调试
    port = settings.PORT
    print(f"🚀 KYC AI 引擎正在启动，监听地址: http://127.0.0.1:{port}")
    uvicorn.run("app.main:app", host="127.0.0.1", port=port, reload=True)