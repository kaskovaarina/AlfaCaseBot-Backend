from model_service import predict_scores


def main():
    solution = """
    Предлагаю создать специальное банковское решение
    для малого и среднего бизнеса, которое объединит
    кредитование, аналитику и дополнительные сервисы.
    """

    scores = predict_scores(solution)

    print("Оценки по критериям:")

    for criterion, score in scores.items():
        print(f"{criterion}: {score:.1f}")


if __name__ == "__main__":
    main()