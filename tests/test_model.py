from model_service import predict_scores, predict_decision

def test_predict_scores_keys():
    text = (
        "Предлагаю создать банковское решение для малого бизнеса "
        "с кредитованием, аналитикой и сервисами."
    )
    scores = predict_scores(text)

    assert "audience" in scores
    assert "finance" in scores
    assert "proves" in scores
    assert "risks" in scores
    assert "sol" in scores
    assert "final_score" in scores

def test_predict_scores_range():
    text = "Тестовое решение для проверки диапазона оценок."
    scores = predict_scores(text)
    for value in scores.values():
        assert 1.0 <= float(value) <= 5.0

def test_predict_decision():
    text = "Короткое тестовое решение."
    result = predict_decision(text)
    assert result in (0, 1)