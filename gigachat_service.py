import os
from dotenv import load_dotenv
from gigachat import GigaChat


load_dotenv()

GIGACHAT_CREDENTIALS = os.getenv("GIGACHAT_CREDENTIALS")
GIGACHAT_SCOPE = os.getenv("GIGACHAT_SCOPE", "GIGACHAT_API_PERS")
GIGACHAT_MODEL = os.getenv("GIGACHAT_MODEL", "GigaChat-3-Pro")
GIGACHAT_BASE_URL = os.getenv(
    "GIGACHAT_BASE_URL",
    "https://api.giga.chat/v1"
)

GIGACHAT_VERIFY_SSL_CERTS = (
    os.getenv("GIGACHAT_VERIFY_SSL_CERTS", "False").lower() == "true"
)


def ask_gigachat(prompt: str) -> str:
    if not GIGACHAT_CREDENTIALS:
        raise RuntimeError(
            "GIGACHAT_CREDENTIALS не найден. Проверь файл .env"
        )
    with GigaChat(
        credentials=GIGACHAT_CREDENTIALS,
        scope=GIGACHAT_SCOPE,
        base_url=GIGACHAT_BASE_URL,
        model=GIGACHAT_MODEL,
        verify_ssl_certs=GIGACHAT_VERIFY_SSL_CERTS
    ) as giga:
        response = giga.chat(prompt)
        return response.choices[0].message.content