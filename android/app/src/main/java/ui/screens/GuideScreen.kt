package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
private val Text2 = Color(0xFF9A9A9F)

@Composable
fun GuideScreen(
    onNavigate: (Screen) -> Unit
) {
    Scaffold(
        containerColor = Background,
        bottomBar = {
            BottomBar(
                selected = 0,
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
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text(
                text = "Гайд по приложению",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Всё, что нужно знать об AI Case Trainer",
                fontSize = 15.sp,
                color = Text2
            )
            Spacer(modifier = Modifier.height(24.dp))
            GuideCard(
                title = "О приложении",
                text = "AI Case Trainer — приложение для тренировки " +
                        "на бизнес-кейсах. Ты предлагаешь своё решение, " +
                        "а искусственный интеллект оценивает его и даёт обратную связь."
            )
            GuideCard(
                title = "Как пользоваться",
                text = "1. Открой раздел «Кейсы».\n\n" +
                        "2. Выбери интересующий кейс и прочитай условие.\n\n" +
                        "3. Подготовь решение и отправь его.\n\n" +
                        "4. Изучи оценку, баллы по критериям и обратную связь.\n\n" +
                        "5. Используй рекомендации, чтобы улучшать свои навыки."
            )
            GuideCard(
                title = "Попытки решения",
                text = "Для одного кейса предусмотрены три попытки " +
                        "с начислением очков.\n\n" +
                        "• Первая попытка — коэффициент 1,0 (100% очков).\n\n" +
                        "• Вторая попытка — коэффициент 0,8 (80% очков).\n\n" +
                        "• Третья попытка — коэффициент 0,6 (60% очков).\n\n" +
                        "После третьей попытки можно продолжать практиковаться, " +
                        "но новые очки за этот кейс не начисляются.\n\n" +
                        "Важно: после просмотра эталонного решения " +
                        "повторная отправка этого же кейса запрещена."
            )
            GuideCard(
                title = "Как формируется оценка",
                text = "Итоговая оценка решения выставляется по шкале " +
                        "от 1 до 5. Она сопровождается обратной связью ИИ.\n\n" +
                        "Решение оценивается по пяти критериям:\n\n" +
                        "• Аудитория — насколько хорошо определены целевые пользователи.\n\n" +
                        "• Финансовая модель — насколько обоснована экономика решения.\n\n" +
                        "• Доказательность — есть ли аргументы и подтверждения.\n\n" +
                        "• Риски — учтены ли возможные проблемы.\n\n" +
                        "• Решение — насколько логично и полно предложен итоговый подход."
            )
            GuideCard(
                title = "Рейтинг и очки",
                text = "В разделе «Рейтинг» можно посмотреть результаты " +
                        "участников и сравнить свои достижения с другими.\n\n" +
                        "Количество очков за кейс зависит от результата " +
                        "и коэффициента попытки.\n\n" +
                        "Старайся не просто получить высокий балл, " +
                        "но и обосновывать свои решения."

            )
            GuideCard(
                title = "Оценка кейса",
                text = "После получения результата ты можешь оценить сам кейс:\n\n" +
                        "• Появится диалог с предложением поставить оценку от 1 до 5 звёзд.\n" +
                        "• Твоя оценка поможет улучшить подборку кейсов.\n" +
                        "• Оценить кейс можно только один раз."
            )

            GuideCard(
                title = "Профиль и статистика",
                text = "В разделе «Профиль» ты можешь:\n\n" +
                        "• Посмотреть свои данные (имя, email).\n" +
                        "• Узнать, сколько кейсов ты решил.\n" +
                        "• Посмотреть свой средний балл.\n" +
                        "• Выйти из аккаунта."
            )
            GuideCard(
                title = "Эталонное решение",
                text = "После получения результата можно открыть " +
                        "эталонное решение и сравнить его со своим.\n\n" +
                        "Обращай внимание на структуру ответа, " +
                        "аргументацию, финансовую модель и анализ рисков.\n\n" +
                        "Помни: после просмотра эталона повторно отправить " +
                        "решение этого же кейса сейчас нельзя."
            )
            GuideCard(
                title = "Советы для хорошего решения",
                text = "• Чётко формулируй проблему.\n\n" +
                        "• Определяй целевую аудиторию.\n\n" +
                        "• Объясняй, почему предлагаешь именно это решение.\n\n" +
                        "• Приводи цифры и аргументы, если они доступны.\n\n" +
                        "• Учитывай риски и способы их уменьшения.\n\n" +
                        "• Внимательно читай обратную связь ИИ и используй её для обучения."
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    onNavigate(Screen.HOME)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Green,
                    contentColor = Background
                )
            ) {
                Text(
                    text = "Вернуться на главную",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun GuideCard(
    title: String,
    text: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Red
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = Green,
                            shape = RoundedCornerShape(50)
                        )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = text,
                fontSize = 14.sp,
                color = Color.White,
                lineHeight = 21.sp
            )
        }
    }
}
