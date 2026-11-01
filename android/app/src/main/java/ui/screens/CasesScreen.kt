package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
private val Green = Color(0xFFB9F42B)
private val Text2 = Color(0xFF9A9A9F)

@Composable
fun CasesScreen(
    cases: List<Case>,
    isLoading: Boolean,
    errorMessage: String?,
    onLoad: () -> Unit,
    onNavigate: (Screen) -> Unit,
    onCaseSelected: (Case) -> Unit
) {
    LaunchedEffect(Unit) {
        onLoad()
    }
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
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Кейсы",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Выберите кейс и проверьте свои навыки",
                fontSize = 16.sp,
                color = Text2
            )
            Spacer(modifier = Modifier.height(24.dp))
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Green
                        )
                    }
                }
                errorMessage != null -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Red
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp)
                        ) {
                            Text(
                                text = "Не удалось загрузить кейсы",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = errorMessage,
                                color = Color.White,
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
                    }
                }
                cases.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Пока нет доступных кейсов",
                            fontSize = 16.sp,
                            color = Text2
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 20.dp)
                    ) {
                        items(
                            items = cases,
                            key = { case -> case.id }
                        ) { case ->
                            CaseCard(
                                title = case.title,
                                description = case.description,
                                onClick = {
                                    onCaseSelected(case)
                                    onNavigate(Screen.CASE_DETAILS)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CaseCard(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Red
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                fontSize = 15.sp,
                lineHeight = 22.sp,
                color = Color.White.copy(alpha = 0.85f),
                maxLines = 3
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Открыть кейс →",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Green
            )
        }
    }
}
