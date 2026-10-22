from gigachat_service import ask_gigachat


def main():
    answer = ask_gigachat(
        "Привет! Ответь одним коротким предложением."
    )

    print("Ответ GigaChat:")
    print(answer)


if __name__ == "__main__":
    main()