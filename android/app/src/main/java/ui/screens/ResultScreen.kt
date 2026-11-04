package ui.screens

import androidx.compose.foundation.background
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
import com.example.alfacasebot_0.model.CaseResult
import java.util.Locale
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownColor
private val Background = Color(0xFF0B0B0C)
private val Red = Color(0xFFE22B1F)
private val Gray = Color(0xFF1C1C1E)
private val Gray2 = Color(0xFF9A9A9F)
private val Green = Color(0xFFB9F42B)

@Composable
fun ResultScreen(
    result: CaseResult,
    referenceSolution: String,
    showReference: Boolean,
    isReferenceLoading: Boolean,
    errorMessage: String,
    onLoadReference: () -> Unit,
    onDismissReference: () -> Unit,
    onNavigate: (Screen) -> Unit,
    showRatingDialog: Boolean,
    isRatingLoading: Boolean,
    ratingMessage: String,
    onRateCase: (Int) -> Unit,
    onDismissRating: () -> Unit
) {
    var showConfirm by rememberSaveable { mutableStateOf(false) }
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
                text = "Результат",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Результат вашего решения",
                fontSize = 16.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Gray
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Итоговая оценка",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Gray2
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = String.format(Locale.US, "%.1f / 5.0", result.score),
                        fontSize = 52.sp,
                        fontWeight = FontWeight.Bold,
                        color = Green
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = getScoreDescription(result.score),
                        fontSize = 14.sp,
                        color = Gray2
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Gray
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Заработанные очки",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Gray2
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = formatPoints(result.points),
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold,
                        color = Green
                    )
                    Text(
                        text = "очков",
                        fontSize = 15.sp,
                        color = Gray2
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider(
                        color = Green
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ResultInfoRow(
                        title = "Номер попытки",
                        value = "${result.attemptNum}"
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    ResultInfoRow(
                        title = "Коэффициент",
                        value = formatCoef(
                            result.attemptCoef
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = when (result.attemptNum) {
                            1 -> "Первая попытка — полный зачёт очков."
                            2 -> "Вторая попытка — начисляется 80% очков."
                            3 -> "Третья попытка — начисляется 60% очков."
                            else -> "Лимит начисления очков исчерпан. Можно продолжать практиковаться."
                        },
                        fontSize = 14.sp,
                        color = Gray2,
                        lineHeight = 20.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Gray
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Оценка по критериям",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ScoreRow(
                        title = "Аудитория",
                        score = result.audience
                    )
                    ScoreRow(
                        title = "Финансовая модель",
                        score = result.finance
                    )
                    ScoreRow(
                        title = "Доказательность",
                        score = result.proves
                    )
                    ScoreRow(
                        title = "Риски",
                        score = result.risks
                    )
                    ScoreRow(
                        title = "Решение",
                        score = result.sol
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Gray
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Обратная связь",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Markdown(
                        content = result.feedback,
                        colors = markdownColor(
                            text = Gray2
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { showConfirm = true },
                enabled = !isReferenceLoading,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Green
                )
            ) {
                Text(
                    text = if (isReferenceLoading) {
                        "Загрузка..."
                    } else {
                        "Посмотреть эталонное решение"
                    },
                    color = Background
                )
            }
            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage,
                    color = Red,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    onNavigate(Screen.CASES)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Green
                )
            ) {
                Text(
                    text = "Вернуться к кейсам",
                    fontSize = 16.sp,
                    color = Background
                )
            }
            if (ratingMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = ratingMessage,
                    color = if (ratingMessage == "Спасибо за оценку!") Green else Red,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
    if (showConfirm) {
        AlertDialog(
            onDismissRequest = {
                showConfirm = false
            },
            containerColor = Gray,
            title = {
                Text(
                    text = "Посмотреть эталон?",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "После просмотра эталонного решения повторно решить этот кейс будет нельзя.",
                    color = Gray2,
                    fontSize = 15.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirm = false
                        onLoadReference()
                    }
                ) {
                    Text(
                        text = "Посмотреть",
                        color = Green
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showConfirm = false
                    }
                ) {
                    Text(
                        text = "Отмена",
                        color = Gray2
                    )
                }
            }
        )
    }
    if (showReference) {
        AlertDialog(
            onDismissRequest = onDismissReference,
            containerColor = Gray,
            title = {
                Text(
                    text = "Эталонное решение",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Markdown(
                        content = referenceSolution,
                        colors = markdownColor(
                            text = Gray2
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = onDismissReference
                ) {
                    Text(
                        text = "Вернуться к результату",
                        color = Green
                    )
                }
            }
        )
    }
    if (showRatingDialog) {
        AlertDialog(
            onDismissRequest = {
                if (!isRatingLoading) {
                    onDismissRating()
                }
            },
            containerColor = Gray,
            title = {
                Text(
                    text = "Оцените кейс",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Насколько вам понравился этот кейс?",
                        color = Gray2,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        (1..5).forEach { star ->
                            TextButton(
                                enabled = !isRatingLoading,
                                onClick = {
                                    onRateCase(star)
                                }
                            ) {
                                Text(
                                    text = "★",
                                    color = Green,
                                    fontSize = 30.sp
                                )
                            }
                        }
                    }
                    if (isRatingLoading) {
                        CircularProgressIndicator(
                            color = Green,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = onDismissRating,
                    enabled = !isRatingLoading
                ) {
                    Text(
                        text = "Позже",
                        color = Gray2
                    )
                }
            }
        )
    }
}

@Composable
fun ResultInfoRow(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 15.sp,
            color = Gray2
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun ScoreRow(
    title: String,
    score: Double
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                fontSize = 15.sp,
                color = Gray2
            )
            Text(
                text = String.format(Locale.US, "%.1f / 5.0", score),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

fun getScoreDescription(score: Double): String {
    return when {
        score >= 4.5 -> "Очень сильное решение"
        score >= 3.5 -> "Хорошее решение"
        score >= 2.5 -> "Есть потенциал для улучшения"
        else -> "Решение требует доработки"
    }
}


fun formatPoints(points: Double): String {
    return if (points % 1.0 == 0.0) {
        points.toLong().toString()
    } else {
        String.format(Locale.US, "%.1f", points)
    }
}

fun formatCoef(coef: Double): String {
    return String.format(Locale.US, "%.1f", coef)
}