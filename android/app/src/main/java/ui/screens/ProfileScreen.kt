package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alfacasebot_0.Screen

private val Background = Color(0xFF0B0B0C)
private val Red = Color(0xFFE22B1F)
private val Green = Color(0xFFB9F42B)

private fun getUserLevel(casesSolved: String): String {
    val count = casesSolved.toIntOrNull() ?: 0
    return when {
        count >= 20 -> "Эксперт"
        count >= 10 -> "Специалист"
        count >= 3 -> "Практик"
        else -> "Новичок"
    }
}

@Composable
fun ProfileScreen(
    username: String,
    email: String,
    casesSolved: String,
    averageScore: String,
    isLoading: Boolean,
    errorMessage: String,
    onLoad: () -> Unit,
    onNavigate: (Screen) -> Unit,
    onLogout: () -> Unit
) {
    LaunchedEffect(Unit) {
        onLoad()
    }
    Scaffold(
        containerColor = Background,
        bottomBar = {
            BottomBar(
                selected = 3,
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
                text = "Профиль",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .background(Green, RoundedCornerShape(50)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Уровень пользователя",
                            tint = Color.Black,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Ваш уровень",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isLoading) {
                                "Загрузка..."
                            } else {
                                getUserLevel(casesSolved)
                            },
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Решено кейсов: ${
                                if (isLoading) "..." else casesSolved
                            }",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Green, RoundedCornerShape(50)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Профиль",
                            tint = Color.Black,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(18.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = username,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = email,
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Статистика",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatisticCard(
                    modifier = Modifier.weight(1f),
                    value = if (isLoading) "..." else casesSolved,
                    title = "Кейсов решено"
                )
                StatisticCard(
                    modifier = Modifier.weight(1f),
                    value = if (isLoading) "..." else averageScore,
                    title = "Средняя оценка из 5"
                )
            }
            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = errorMessage,
                    color = Color.White,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onLoad,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Green
                    )
                ) {
                    Text("Повторить загрузку")
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Green,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Выйти из аккаунта",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun StatisticCard(
    modifier: Modifier,
    value: String,
    title: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Green
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                color = Color.White,
                lineHeight = 18.sp
            )
        }
    }
}
