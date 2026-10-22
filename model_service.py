from pathlib import Path

import joblib
import numpy as np

BASE_DIR = Path(__file__).parent
MODEL_NAMES = ("audience", "finance", "proves", "risks", "sol")
MODELS = {
    name: joblib.load(BASE_DIR / f"model_{name}.joblib")
    for name in MODEL_NAMES
}


def model_predict(text: str, name: str) -> float:
    data = MODELS[name]

    X = data["vectorizer"].transform([text])
    prediction = data["model"].predict(X)[0]
    low, high = data["target_range"]
    return float(np.clip(prediction, low, high))


def predict_scores(solution: str):
    scores = {name: model_predict(solution, name) for name in MODEL_NAMES}
    final_score = sum(scores.values()) / len(scores)
    return {
        "audience": scores["audience"],
        "finance": scores["finance"],
        "proves": scores["proves"],
        "risks": scores["risks"],
        "sol": scores["sol"],
        "final_score": final_score,
    }