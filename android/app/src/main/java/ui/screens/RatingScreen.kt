
package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alfacasebot_0.Screen
import com.example.alfacasebot_0.model.RatingUser

private val Background = Color(0xFF0B0B0C)
private val Red = Color(0xFFE22B1F)
private val Green = Color(0xFFB9F42B)
private val Text2 = Color(0xFF9A9A9F)

@Composable
fun RatingScreen(
    topUsers: List<RatingUser>,
    isLoading: Boolean,
    errorMessage: String,
    onLoad: () -> Unit,
    onNavigate: (Screen) -> Unit
) {
    LaunchedEffect(Unit) {
        onLoad()
    }
    Scaffold(
        containerColor = Background,
        bottomBar = {
            BottomBar(
                selected = 2,
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
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Рейтинг",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Топ 5 пользователей",
                fontSize = 16.sp,
                color = Text2
            )
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Red
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    when {
                        isLoading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = Green
                                )
                            }
                            Text(
                                text = "Загрузка рейтинга...",
                                modifier = Modifier.fillMaxWidth(),
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                        errorMessage.isNotEmpty() -> {
                            Text(
                                text = "Не удалось загрузить рейтинг",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = errorMessage,
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = onLoad,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Green,
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Повторить",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        topUsers.isEmpty() -> {
                            Text(
                                text = "Рейтинг пока пуст",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Рейтинг появится после того, как пользователи получат оценки за решения.",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 14.sp,
                                lineHeight = 21.sp
                            )
                        }
                        else -> {
                            topUsers.take(5).forEachIndexed { index, user ->
                                RatingRow(
                                    position = index + 1,
                                    username = user.username,
                                    score = user.score
                                )
                                if (index < minOf(topUsers.size, 5) - 1) {
                                    Spacer(
                                        modifier = Modifier.height(18.dp)
                                    )
                                    HorizontalDivider(
                                        color = Color.White.copy(alpha = 0.2f)
                                    )
                                    Spacer(
                                        modifier = Modifier.height(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun RatingRow(
    position: Int,
    username: String,
    score: Double
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = Green,
                    shape = RoundedCornerShape(50)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = position.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = username,
            modifier = Modifier.weight(1f),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = score.toInt().toString(),
            color = Green,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
