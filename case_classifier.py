import joblib
import re
from pathlib import Path

BASE_D = Path(__file__).resolve().parent
model_correct_case = joblib.load(
    BASE_D / "case_classifier.joblib"
)

CASE_IDS = {
    "ОТРАСЛЕВОЕ РЕШЕНИЕ АЛЬФА-БАНКА ДЛЯ МАЛОГО И СРЕДНЕГО БИЗНЕСА": 1,
    "ОТРАСЛЕВОЕ РЕШЕНИЕ АЛЬФА-БАНКА ДЛЯ ОРГАНИЗАЦИЙ КУЛЬТУРНО-ДОСУГОВОГО НАЗНАЧЕНИЯ": 2,
    "ОТРАСЛЕВОЕ РЕШЕНИЕ АЛЬФА-БАНКА ДЛЯ РАЗВИТИЯ МАССОВОГО СПОРТА И ФИЗИЧЕСКОЙ КУЛЬТУРЫ": 3,
    "КОМПЛЕКСНОЕ ОТРАСЛЕВОЕ РЕШЕНИЕ АЛЬФА-БАНКА ДЛЯ СИСТЕМЫ ОБРАЗОВАНИЯ НА БАЗЕ ИСКУССТВЕННОГО ИНТЕЛЛЕКТА": 4,
    "ОТРАСЛЕВОЕ РЕШЕНИЕ АЛЬФА-БАНКА ДЛЯ НЕФТЕГАЗОВОГО СЕКТОРА": 5,
    "ФИДЖИТАЛ-ОФИС 2.0 — НОВОЕ ПОКОЛЕНИЕ БАНКОВСКИХ ОТДЕЛЕНИЙ ДЛЯ ГОРОДОВ С НАСЕЛЕНИЕМ ОТ 30 ТЫСЯЧ ЧЕЛОВЕК": 6,
    "ЦЕНТР ПОДГОТОВКИ ПРОФЕССИОНАЛОВ БУДУЩЕГО — ОБРАЗОВАТЕЛЬНОЕ УЧРЕЖДЕНИЕ 2050": 7,
    "ОТРАСЛЕВОЕ РЕШЕНИЕ АЛЬФА-БАНКА ДЛЯ РАЗВИТИЯ ПРОФЕССИОНАЛЬНОГО СПОРТА В РЕГИОНЕ": 8,
    "ЭЛЕКТРОМОБИЛЬНЫЙ КЛАСТЕР ДЛЯ РЕГИОНА": 9,
    "АНТИКРИЗИСНАЯ СТРАТЕГИЯ ДЛЯ СЕТИ РЕСТОРАНОВ": 10,
}

THRESHOLD = 0.10
THRESHOLD_BY_CASE = {
    2: 0.05,
    3: 0.05,
    6: 0.05,
    8: 0.05
}
def check_case1(text: str, case_name: str) -> int:
    case_id = CASE_IDS[case_name]
    text = re.sub(r"<br\s*/?>", " ", str(text))
    text = re.sub(r"\s+", " ", text).strip()
    proba = model_correct_case.predict_proba([text])[0]
    classes = list(model_correct_case.classes_)
    if case_id not in classes:
        return 0
    p_chosen = proba[classes.index(case_id)]
    threshold = THRESHOLD_BY_CASE.get(
        case_id,
        THRESHOLD
    )
    return int(p_chosen >= threshold)