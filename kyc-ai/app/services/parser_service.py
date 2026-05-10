import io
import json
import requests
import pdfplumber
import docx2txt
from app.config import settings


class ResumeParserService:

    def extract_text_from_bytes(self, file_bytes: bytes, extension: str) -> str:
        """
        本地基础解析：从 PDF/DOCX 二进制流中快速提取文本（若为纯图片扫描件，此处为空）
        """
        text = ""
        file_like = io.BytesIO(file_bytes)

        try:
            if extension == ".pdf":
                with pdfplumber.open(file_like) as pdf:
                    for page in pdf.pages:
                        page_text = page.extract_text()
                        if page_text:
                            text += page_text + "\n"
            elif extension in [".docx", ".doc"]:
                text = docx2txt.process(file_like)
            else:
                raise ValueError(f"暂不支持 {extension} 格式的文件解析")
        except Exception as e:
            raise Exception(f"读取简历物理文本失败: {str(e)}")

        return text

    async def parse_and_analyze(self, file_bytes: bytes, extension: str) -> dict:
        """
        AI 智能画像建模流（阿里云百炼版）
        """
        # 1. 抽取简历纯文本（若后期需要高级 OCR，可在此处替换为阿里云百炼文档解析 API）
        raw_text = self.extract_text_from_bytes(file_bytes, extension)
        if not raw_text.strip():
            raise Exception("未从上传的文件中解析到任何文字。若是扫描件，请先使用 OCR 转化为文字版。")

        # 2. 注入 Prompt 模板
        prompt = settings.PROMPT_TEMPLATE.format(resume_text=raw_text)

        # 3. 优先使用阿里云百炼通道
        if settings.DASHSCOPE_API_KEY:
            # 兼容 OpenAI 协议的百炼接口
            api_url = f"{settings.DASHSCOPE_BASE_URL}/chat/completions"
            headers = {
                "Authorization": f"Bearer {settings.DASHSCOPE_API_KEY}",
                "Content-Type": "application/json"
            }
            payload = {
                # 🎯 采用百炼平台性价比极高且推理能力极强的 qwen-plus 或商用旗舰级 qwen-max
                "model": "qwen-plus",
                "messages": [
                    {
                        "role": "system",
                        "content": "你是一个严格按照 JSON 格式输出的简历分析助手，禁止输出除了 JSON 之外的任何解释性文字或 Markdown 标记。"
                    },
                    {"role": "user", "content": prompt}
                ],
                "temperature": 0.1,  # 极低随机性确保精确提取
                "stream": False
            }
        else:
            raise Exception("未在环境变量中检测到有效的 DASHSCOPE_API_KEY 密钥，请检查根目录下的 .env 文件！")

        try:
            response = requests.post(api_url, headers=headers, json=payload, timeout=60)
            if response.status_code != 200:
                raise Exception(f"阿里云百炼 API 响应异常，HTTP {response.status_code}: {response.text}")

            res_json = response.json()
            ai_content = res_json["choices"][0]["message"]["content"].strip()

            # 清理可能带有的 ```json 代码块包裹
            if ai_content.startswith("```json"):
                ai_content = ai_content.replace("```json", "", 1)
            if ai_content.endswith("```"):
                ai_content = ai_content[:-3]
            ai_content = ai_content.strip()

            # 4. 反序列化
            return json.loads(ai_content)

        except json.JSONDecodeError:
            return {
                "raw_response": ai_content if 'ai_content' in locals() else "通义千问输出无法被正确解析为 JSON"
            }
        except Exception as e:
            raise Exception(f"阿里云大模型请求链路失败: {str(e)}")


# 单例暴露
resume_parser = ResumeParserService()