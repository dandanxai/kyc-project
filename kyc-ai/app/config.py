import os
from pathlib import Path
from dotenv import load_dotenv

env_path = Path(__file__).resolve().parent.parent / '.env'
load_dotenv(dotenv_path=env_path)


class Config:
    ENV: str = os.getenv("ENV", "development")
    PORT: int = int(os.getenv("PORT", 8002))

    # 🎯 阿里云百炼的 OpenAI 兼容 Base URL
    DASHSCOPE_BASE_URL: str = "https://dashscope.aliyuncs.com/compatible-mode/v1"
    DASHSCOPE_API_KEY: str = os.getenv("DASHSCOPE_API_KEY", "")

    # 大模型简历结构化提取的 Prompt 提示词模板
    PROMPT_TEMPLATE: str = """
你是一位专业的信创全栈开发工程师技术面试官。请严格从下方提供的简历文本内容中，提取并分析出对应的技术画像。
如果简历中未提及某项，请输出“未在简历中提及”。
必须严格按照以下 JSON 格式响应，不要包含任何 Markdown 格式包裹（如 ```json），直接返回一个合法的 JSON 字符串本身：

{{
  "name": "求职者真实姓名",
  "education": "最高学历与毕业院校",
  "expertise": "核心技能栈（例如 Java, Spring Boot, Vue 3, UniApp 等）",
  "artifacts": "具有代表性的软件工程成果/项目经历",
  "dmSql": "达梦数据库或信创适配调优经验（如有）",
  "llmAi": "本地大模型、RAG 向量库或 Agent 构建经验（如有）",
  "language": "外语证书（如 CET-4, JLPT N2 等）"
}}

[以下为简历文本内容]
{resume_text}
"""


settings = Config()