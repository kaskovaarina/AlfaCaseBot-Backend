
from pathlib import Path

import joblib
import numpy as np

BASE_DIR = Path(__file__).resolve().parent

MODEL_NAMES = ("audience", "finance", "proves", "risks", "sol")

MODELS = {
    name: joblib.load(BASE_DIR / f"lr_{name}1.joblib")
    for name in MODEL_NAMES
}

DECISION_MODEL = joblib.load(
    BASE_DIR / "model_decision_tfidf_svm.joblib"
)


def model_predict(text: str, name: str) -> float:
    model = MODELS[name]
    prediction = model.predict([str(text)])[0]

    return round(float(np.clip(prediction, 1, 5)), 2)


def predict_scores(solution: str) -> dict:
    scores = {
        name: model_predict(solution, name)
        for name in MODEL_NAMES
    }

    final_score = sum(scores.values()) / len(scores)

    return {
        "audience": scores["audience"],
        "finance": scores["finance"],
        "proves": scores["proves"],
        "risks": scores["risks"],
        "sol": scores["sol"],
        "final_score": final_score,
    }


def predict_decision(text: str) -> int:
    score = DECISION_MODEL.decision_function([str(text)])[0]
    return int(score >= -0.1)
