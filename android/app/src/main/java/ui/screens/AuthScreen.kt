package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import com.example.alfacasebot_0.network.RetrofitClient
import com.example.alfacasebot_0.network.LoginRequest
import com.example.alfacasebot_0.network.RegisterRequest
import org.json.JSONObject
@Composable
private fun AuColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedBorderColor = Color(0xFFEF3124),
    unfocusedBorderColor = Color(0xFF65140F),
    focusedLabelColor = Color(0xFFEF3124),
    unfocusedLabelColor = Color.White,
    cursorColor = Color(0xFFEF3124)
)
@Composable
fun AuthScreen(
    onAuthSuccess: (Int, String, String, String, String) -> Unit
) {
    val context = LocalContext.current
    val api = remember { RetrofitClient.getApiService(context) }
    var isLogin by remember { mutableStateOf(true) }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    fun validate(): String? {
        if (!isLogin && username.isBlank()) {
            return "Введите имя пользователя"
        }
        if (email.isBlank() || !email.contains("@") || !email.contains(".")) {
            return "Введите корректный email"
        }
        if (password.length < 6) {
            return "Пароль должен быть не короче 6 символов"
        }
        return null
    }

    fun submit() {
        val error = validate()
        if (error != null) {
            resultText = error
            return
        }
        isLoading = true
        resultText = ""
        scope.launch {
            try {
                val response = if (isLogin) {
                    api.login(
                        LoginRequest(
                            email = email.trim(),
                            password = password
                        )
                    )
                } else {
                    api.register(
                        RegisterRequest(
                            username = username.trim(),
                            email = email.trim(),
                            password = password
                        )
                    )
                }
                if (response.isSuccessful) {
                    val auth = response.body()
                    if (auth != null && auth.success) {
                        if (auth.accessToken.isNotBlank() &&
                            auth.refreshToken.isNotBlank() &&
                            auth.id != -1
                        ) {
                            onAuthSuccess(
                                auth.id,
                                auth.username,
                                auth.email,
                                auth.accessToken,
                                auth.refreshToken
                            )
                        } else {
                            resultText = "Сервер не вернул необходимые данные авторизации"
                        }
                    } else {
                        resultText = auth?.message ?: "Не удалось выполнить авторизацию"
                    }
                } else {
                    val errorText = response.errorBody()?.string() ?: "{}"
                    val errorJson = JSONObject(errorText)
                    resultText = errorJson.optString(
                        "message",
                        errorJson.optString(
                            "detail",
                            "Ошибка авторизации: ${response.code()}"
                        )
                    )
                }
            } catch (e: java.io.IOException) {
                resultText = "Нет соединения с сервером"
            } catch (e: Exception) {
                resultText = "Не удалось обработать ответ сервера"
            } finally {
                isLoading = false
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isLogin) "Вход" else "Регистрация",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(24.dp))
        if (!isLogin) {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Имя пользователя", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isLoading,
                colors = AuColors(),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = Color.White) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isLoading,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = AuColors(),
            shape = RoundedCornerShape(14.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль", color = Color.White) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isLoading,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = AuColors(),
            shape = RoundedCornerShape(14.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { submit() },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEF3124)
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = if (isLogin) "Войти" else "Создать аккаунт",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(
            enabled = !isLoading,
            onClick = {
                isLogin = !isLogin
                resultText = ""
            }
        ) {
            Text(
                text = if (isLogin)
                    "Нет аккаунта? Зарегистрироваться"
                else
                    "Уже есть аккаунт? Войти",
                color = Color(0xFFEF3124),
                fontSize = 14.sp
            )
        }

        if (resultText.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = resultText,
                color = Color.White,
                fontSize = 15.sp
            )
        }
    }
}