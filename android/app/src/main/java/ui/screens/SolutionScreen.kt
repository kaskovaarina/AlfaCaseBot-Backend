package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alfacasebot_0.Screen
import com.example.alfacasebot_0.model.Case

private val Background = Color(0xFF0B0B0C)
private val Red = Color(0xFFE22B1F)
private val Gray3 = Color(0xFF1C1C1E)
private val Gray = Color(0xFF7A7A80)
private val Gray2 = Color(0xFF9A9A9F)
private val Green = Color(0xFFB9F42B)

private const val MIN_WORDS = 50
private const val MAX_WORDS = 2000
private const val MAX_REPEAT_PERCENT = 30

private fun getWords(text: String): List<String> {
    return Regex("[A-Za-zА-Яа-яЁё]+")
        .findAll(text)
        .map { it.value.lowercase() }
        .toList()
}
private fun checkSolution(text: String): String? {
    for (ch in text) {
        if (ch.isLetter()) {
            val isEn = ch in 'a'..'z' || ch in 'A'..'Z'
            val isRu =
                ch in 'а'..'я' ||
                        ch in 'А'..'Я' ||
                        ch == 'ё' ||
                        ch == 'Ё'

            if (!isEn && !isRu) {
                return "Можно использовать только русские и английские буквы"
            }
        }
    }

    if (Regex("(.)\\1{4,}").containsMatchIn(text)) {
        return "В тексте много опечаток: одна буква повторяется слишком много раз подряд"
    }
    val words = getWords(text)
    if (words.size < MIN_WORDS) {
        return "Слишком короткое решение: ${words.size} из $MIN_WORDS слов"
    }
    if (words.size > MAX_WORDS) {
        return "Слишком длинное решение: ${words.size} слов, максимум $MAX_WORDS"
    }
    val longWords = words.filter { it.length >= 4 }

    if (longWords.isNotEmpty()) {
        val uniqueCount = longWords.toSet().size
        val repeatedCount = longWords.size - uniqueCount
        val repeatPercent = repeatedCount * 100 / longWords.size

        if (repeatPercent > MAX_REPEAT_PERCENT) {
            return "Слишком много повторяющихся слов ($repeatPercent%), максимум $MAX_REPEAT_PERCENT%"
        }
    }
    return null
}

@Composable
fun SolutionScreen(
    case: Case,
    isLoading: Boolean,
    errorMessage: String,
    onSubmit: (String) -> Unit,
    onNavigate: (Screen) -> Unit
) {
    var solutionText by rememberSaveable { mutableStateOf("") }
    var showCondition by rememberSaveable { mutableStateOf(false) }
    var localError by remember { mutableStateOf("") }
    val shownError = localError.ifEmpty { errorMessage }
    Scaffold(
        containerColor = Background,
        bottomBar = {
            BottomBar(
                selected = 1,
                onItemSelected = { index ->
                    when (index) {
                        0 -> onNavigate(Screen.HOME)
                        1 -> onNavigate(Screen.CASES)
                        2 -> onNavigate(Screen.RATING)
                        3 -> onNavigate(Screen.PROFILE)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Решение кейса",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Опишите своё решение: от $MIN_WORDS до $MAX_WORDS слов.",
                fontSize = 16.sp,
                color = Gray2
            )
            Spacer(modifier = Modifier.height(20.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showCondition = !showCondition
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Red
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = case.title,
                            modifier = Modifier.weight(1f),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = if (showCondition) Int.MAX_VALUE else 2
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = if (showCondition) "▲" else "▼",
                            fontSize = 18.sp,
                            color = Green
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (showCondition) {
                        Text(
                            text = case.description,
                            fontSize = 15.sp,
                            color = Color.White,
                            lineHeight = 22.sp
                        )
                    } else {
                        Text(
                            text = "Нажмите, чтобы посмотреть условие",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = solutionText,
                onValueChange = {
                    solutionText = it
                    localError = ""
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                placeholder = {
                    Text(
                        text = "Опишите ваше предложение",
                        color = Gray3
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Gray2,
                    focusedBorderColor = Red,
                    unfocusedBorderColor = Red,
                    disabledBorderColor = Red.copy(alpha = 0.5f),
                    cursorColor = Green,
                    focusedContainerColor = Gray3,
                    unfocusedContainerColor = Gray3,
                    disabledContainerColor = Gray3,
                    focusedPlaceholderColor = Gray,
                    unfocusedPlaceholderColor = Gray,
                    disabledPlaceholderColor = Gray
                ),
                shape = RoundedCornerShape(16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            val wordCount = getWords(solutionText).size
            Text(
                text = "Слов: $wordCount / $MAX_WORDS",
                fontSize = 14.sp,
                color = Gray2
            )
            Spacer(modifier = Modifier.height(20.dp))
            if (shownError.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Gray3
                    )
                ) {
                    Text(
                        text = shownError,
                        modifier = Modifier.padding(12.dp),
                        color = Red,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            if (isLoading) {
                Text(
                    text = "Проверка может занять до минуты",
                    color = Gray2,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            Button(
                onClick = {
                    val problem = checkSolution(solutionText)
                    if (problem != null) {
                        localError = problem
                    } else {
                        localError = ""
                        onSubmit(solutionText)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Green,
                    contentColor = Background,
                    disabledContainerColor = Green.copy(alpha = 0.5f),
                    disabledContentColor = Background.copy(alpha = 0.6f)
                ),
                enabled = !isLoading
            ) {
                Text(
                    text = if (isLoading) {
                        "Проверяем..."
                    } else {
                        "Отправить решение"
                    },
                    color = Background,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
