package com.example.alfacasebot_0

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.alfacasebot_0.model.Case
import com.example.alfacasebot_0.model.CaseResult
import com.example.alfacasebot_0.model.RatingUser
import com.example.alfacasebot_0.network.SubmitCaseRequest
import com.example.alfacasebot_0.network.Tokens
import com.example.alfacasebot_0.network.LogoutRequest
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.alfacasebot_0.network.RetrofitClient
import com.example.alfacasebot_0.network.ApiService
import com.example.alfacasebot_0.network.RateCaseRequest
class  AppViewModel(application: Application) : AndroidViewModel(application) {
    private val api: ApiService =
        RetrofitClient.getApiService(application)
    var userId by mutableStateOf(0)
        private set
    var username by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set

    val isLogged: Boolean get() = userId > 0

    fun onAuthSuccess(
        id: Int,
        name: String,
        mail: String,
        accessToken: String,
        refreshToken: String
    ) {
        Tokens.save(getApplication(), accessToken, refreshToken)

        userId = id
        username = name
        email = mail
        currentScreen = Screen.HOME
    }

    fun logout() {
        viewModelScope.launch {
            try {
                val refreshToken = Tokens.getRefresh(getApplication())
                if (!refreshToken.isNullOrBlank()) {
                    api.logout(LogoutRequest(refreshToken))
                }
            } catch (e: Exception) {

            } finally {
                Tokens.clear(getApplication())
                userId = 0
                username = ""
                email = ""
                selectedCase = null
                caseResult = null
                cases = emptyList()
                topUsers = emptyList()
                casesSolved = "0"
                averageScore = "0"
                resetReference()
                currentScreen = Screen.HOME
                showRatingDialog = false
                ratingLoading = false
                ratingMessage = ""
            }
        }
    }
    var currentScreen by mutableStateOf(Screen.HOME)
        private set
    var selectedCase by mutableStateOf<Case?>(null)
        private set
    var caseResult by mutableStateOf<CaseResult?>(null)
        private set

    fun navigate(screen: Screen) {
        currentScreen = screen
    }
    fun back() {
        currentScreen = when (currentScreen) {
            Screen.CASE_DETAILS -> Screen.CASES
            Screen.SOLUTION -> Screen.CASE_DETAILS
            Screen.RESULT -> Screen.CASES
            else -> Screen.HOME
        }
    }
    fun selectCase(case: Case) {
        selectedCase = case
        submitError = ""
        resetReference()
    }
    var cases by mutableStateOf<List<Case>>(emptyList())
        private set
    var casesLoading by mutableStateOf(false)
        private set
    var casesError by mutableStateOf<String?>(null)
        private set

    fun loadCases(force: Boolean = false) {
        if (cases.isNotEmpty() && !force) return
        viewModelScope.launch {
            casesLoading = true
            casesError = null

            try {
                val response = api.getCases()
                val responseText = if (response.isSuccessful) {
                    response.body()?.string() ?: "{}"
                } else {
                    response.errorBody()?.string() ?: "{}"
                }
                val json = JSONObject(responseText)
                if (response.isSuccessful && json.optBoolean("success")) {
                    val arr = json.getJSONArray("cases")
                    cases = List(arr.length()) { i ->
                        val item = arr.getJSONObject(i)
                        Case(
                            id = item.getInt("id"),
                            title = item.getString("title"),
                            description = item.getString("description")
                        )
                    }
                } else {
                    casesError = json.optString(
                        "message",
                        json.optString("detail", "Не удалось загрузить кейсы")
                    )
                }
            } catch (e: IOException) {
                casesError = "Нет соединения с сервером"
            } catch (e: Exception) {
                casesError = "Не удалось обработать ответ сервера"
            } finally {
                casesLoading = false
            }
        }
    }
    var topUsers by mutableStateOf<List<RatingUser>>(emptyList())
        private set
    var ratingLoading by mutableStateOf(false)
        private set
    var ratingError by mutableStateOf("")
        private set

    fun loadRating() {
        viewModelScope.launch {
            topRatingLoading = true
            ratingError = ""
            try {
                val response = api.getTopRating()
                val responseText = if (response.isSuccessful) {
                    response.body()?.string() ?: "{}"
                } else {
                    response.errorBody()?.string() ?: "{}"
                }
                val json = JSONObject(responseText)
                if (response.isSuccessful && json.optBoolean("success")) {
                    val arr = json.optJSONArray("users")
                    topUsers = if (arr == null) {
                        emptyList()
                    } else {
                        List(arr.length()) { i ->
                            val u = arr.getJSONObject(i)
                            RatingUser(
                                username = u.optString("username"),
                                score = u.optDouble("score", 0.0)
                            )
                        }
                    }
                } else {
                    ratingError = json.optString(
                        "message",
                        json.optString("detail", "Не удалось загрузить рейтинг")
                    )
                }
            } catch (e: IOException) {
                ratingError = "Нет соединения с сервером"
            } catch (e: Exception) {
                ratingError = "Не удалось обработать ответ сервера"
            } finally {
                topRatingLoading = false
            }
        }
    }
    var casesSolved by mutableStateOf("0")
        private set
    var averageScore by mutableStateOf("0")
        private set
    var profileLoading by mutableStateOf(false)
        private set
    var profileError by mutableStateOf("")
        private set

    fun loadProfileStats() {
        if (userId <= 0) return
        viewModelScope.launch {
            profileLoading = true
            profileError = ""
            try {
                val response = api.getProfileStats()

                val responseText = if (response.isSuccessful) {
                    response.body()?.string() ?: "{}"
                } else {
                    response.errorBody()?.string() ?: "{}"
                }
                val json = JSONObject(responseText)
                if (response.isSuccessful && json.optBoolean("success")) {
                    casesSolved = json.optString("cases_solved", "0")
                    averageScore = json.optString("average_score", "0")
                } else {
                    profileError = json.optString(
                        "message",
                        json.optString("detail", "Не удалось загрузить статистику")
                    )
                }
            } catch (e: IOException) {
                profileError = "Нет соединения с сервером"
            } catch (e: Exception) {
                profileError = "Не удалось обработать ответ сервера"
            } finally {
                profileLoading = false
            }
        }
    }
    var submitLoading by mutableStateOf(false)
        private set
    var submitError by mutableStateOf("")
        private set

    fun submitSolution(solution: String) {
        val case = selectedCase ?: return
        viewModelScope.launch {
            submitLoading = true
            submitError = ""
            try {
                val response = api.submitCase(
                    SubmitCaseRequest(
                        caseId = case.id,
                        solution = solution
                    )
                )
                val responseText = if (response.isSuccessful) {
                    response.body()?.string() ?: "{}"
                } else {
                    response.errorBody()?.string() ?: "{}"
                }
                val json = JSONObject(responseText)
                if (response.isSuccessful && json.optBoolean("success")) {
                    val scores = json.getJSONObject("scores")

                    caseResult = CaseResult(
                        score = json.getDouble("score"),
                        points = json.getDouble("points"),
                        attemptNum = json.getInt("attempt_number"),
                        attemptCoef = json.getDouble("attempt_coefficient"),
                        audience = scores.getDouble("audience"),
                        finance = scores.getDouble("finance"),
                        proves = scores.getDouble("proves"),
                        risks = scores.getDouble("risks"),
                        sol = scores.getDouble("sol"),
                        feedback = json.optString("feedback")
                    )
                    resetReference()
                    ratingMessage = ""
                    showRatingDialog = json.getInt("attempt_number") == 1
                    currentScreen = Screen.RESULT
                } else {
                    submitError = json.optString(
                        "message",
                        json.optString("detail", "Не удалось отправить решение")
                    )
                }
            } catch (e: IOException) {
                submitError = "Нет соединения с сервером"
            } catch (e: Exception) {
                submitError = "Не удалось обработать ответ сервера"
            } finally {
                submitLoading = false
            }
        }
    }
    var referenceText by mutableStateOf("")
        private set
    var showReference by mutableStateOf(false)
        private set
    var referenceLoading by mutableStateOf(false)
        private set
    var referenceError by mutableStateOf("")
        private set

    fun loadReference() {
        val case = selectedCase ?: return
        viewModelScope.launch {
            referenceLoading = true
            referenceError = ""
            try {
                val response = api.getReferenceSolution(case.id)
                val responseText = if (response.isSuccessful) {
                    response.body()?.string() ?: "{}"
                } else {
                    response.errorBody()?.string() ?: "{}"
                }
                val json = JSONObject(responseText)
                if (response.isSuccessful && json.optBoolean("success")) {
                    referenceText = json.optString("reference_solution")
                    showReference = true
                } else {
                    referenceError = json.optString(
                        "message",
                        json.optString("detail", "Не удалось загрузить эталонное решение")
                    )
                }
            } catch (e: IOException) {
                referenceError = "Нет соединения с сервером"
            } catch (e: Exception) {
                referenceError = "Ошибка загрузки эталонного решения"
            } finally {
                referenceLoading = false
            }
        }
    }

    var showRatingDialog by mutableStateOf(false)
        private set

    var topRatingLoading by mutableStateOf(false)
        private set
    var ratingMessage by mutableStateOf("")
        private set

    fun dismissRatingDialog() {
        showRatingDialog = false
    }

    fun rateCase(rating: Int) {
        val case = selectedCase ?: return
        if (userId <= 0 || rating !in 1..5 || ratingLoading) return
        viewModelScope.launch {
            ratingLoading = true
            ratingMessage = ""
            try {
                val response = api.rateCase(
                    RateCaseRequest(
                        caseId = case.id,
                        rating = rating
                    )
                )
                val responseText = if (response.isSuccessful) {
                    response.body()?.string() ?: "{}"
                } else {
                    response.errorBody()?.string() ?: "{}"
                }
                val json = JSONObject(responseText)
                if (response.isSuccessful && json.optBoolean("success")) {
                    ratingMessage = "Спасибо за оценку!"
                    showRatingDialog = false
                } else {
                    val message = json.optString(
                        "message",
                        json.optString("detail", "Не удалось сохранить оценку")
                    )
                    ratingMessage = message
                    if (message.contains("уже оценили", ignoreCase = true)) {
                        showRatingDialog = false
                    }
                }
            } catch (e: IOException) {
                ratingMessage = "Нет соединения с сервером"
            } catch (e: Exception) {
                ratingMessage = "Не удалось обработать ответ сервера"
            } finally {
                ratingLoading = false
            }
        }
    }
    fun dismissReference() {
        showReference = false
    }
    private fun resetReference() {
        referenceText = ""
        showReference = false
        referenceError = ""
    }
}