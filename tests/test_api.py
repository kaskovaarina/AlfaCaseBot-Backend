from fastapi.testclient import TestClient

from main import app


client = TestClient(app)


def test_cases():
    response = client.get("/cases")

    assert response.status_code == 200

    data = response.json()

    assert isinstance(data, dict)
    assert data["success"] is True
    assert isinstance(data["cases"], list)
    assert len(data["cases"]) > 0


def test_rating(): 
    response = client.get("/rating/top")

    assert response.status_code == 200

    data = response.json()

    assert isinstance(data, dict)
    assert data["success"] is True
    assert isinstance(data["users"], list)


def test_profile_not_found():
    response = client.get(
        "/profile?email=notfound@example.com"
    )

    assert response.status_code == 200

    data = response.json()

    assert data["success"] is False
    assert data["message"] == "Пользователь не найден"

def test_points_calculation():
    score = 4.5

    assert round(score * 1.0 * 100, 2) == 450
    assert round(score * 0.8 * 100, 2) == 360
    assert round(score * 0.6 * 100, 2) == 270
    assert round(score * 0.0 * 100, 2) == 0