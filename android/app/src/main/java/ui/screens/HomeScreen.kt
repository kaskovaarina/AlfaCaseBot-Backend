package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alfacasebot_0.Screen
import java.util.Calendar

private val Background = Color(0xFF0B0B0C)
private val Red = Color(0xFFE22B1F)
private val Green = Color(0xFFB9F42B)
private val Text1 = Color.White
private val Text2 = Color(0xFF9A9A9F)

private data class DailyTip(
    val title: String,
    val text: String
)
private val dailyTips = listOf(
    DailyTip("Начинай с проблемы", "Сначала определи, что именно мешает бизнесу расти. Не предлагай решение, пока не понял проблему."),
    DailyTip("Разделяй проблему на части", "Разбей сложную задачу на несколько небольших направлений и анализируй каждое отдельно."),
    DailyTip("Опирайся на цифры", "Подкрепляй свои выводы данными, а не только предположениями."),
    DailyTip("Изучи клиента", "Подумай, кто целевая аудитория, чего она хочет и почему выбирает конкурентов."),
    DailyTip("Проверь выручку", "Если бизнес теряет деньги, выясни, падают ли продажи, средний чек или количество клиентов."),
    DailyTip("Изучи расходы", "Раздели расходы на постоянные и переменные, чтобы понять, где можно сэкономить."),
    DailyTip("Формулируй гипотезы", "Записывай, почему, по твоему мнению, возникла проблема, и думай, как это проверить."),
    DailyTip("Оцени рынок", "Учитывай размер рынка, его динамику и изменения в поведении покупателей."),
    DailyTip("Изучи конкурентов", "Сравнивай цены, продукты, клиентский опыт и преимущества конкурентов."),
    DailyTip("Не забывай о прибыли", "Рост выручки не всегда означает рост прибыли. Анализируй оба показателя."),
    DailyTip("Выбирай метрики", "Определи показатели, по которым можно измерить успех предложенного решения."),
    DailyTip("Сравни альтернативы", "Рассмотри несколько вариантов действий, прежде чем выбирать один."),
    DailyTip("Оцени риски", "Подумай, что может пойти не так при реализации твоего решения."),
    DailyTip("Считай эффект", "По возможности оцени ожидаемый прирост дохода, экономию и затраты на внедрение."),
    DailyTip("Расставляй приоритеты", "Сначала решай задачи с наибольшим ожидаемым эффектом и приемлемыми затратами."),
    DailyTip("Учитывай сроки", "Даже хорошее решение может не подойти, если его невозможно реализовать вовремя."),
    DailyTip("Проверяй допущения", "Отмечай, какие факты известны точно, а какие пока являются предположениями."),
    DailyTip("Смотри на воронку продаж", "Проверяй, на каком этапе потенциальные клиенты чаще всего уходят."),
    DailyTip("Изучи удержание", "Понять, почему клиенты возвращаются или уходят, часто не менее важно, чем привлечь новых."),
    DailyTip("Не усложняй решение", "Если простое решение устраняет основную причину проблемы, начни с него."),
    DailyTip("Думай о внедрении", "Учитывай людей, ресурсы, технологии и процессы, необходимые для реализации идеи."),
    DailyTip("Учитывай ограничения", "Проверь бюджет, доступные данные, сроки и возможности команды."),
    DailyTip("Сформулируй цель", "Цель должна быть конкретной и позволять проверить результат."),
    DailyTip("Проверь экономику", "Оцени, сколько стоит привлечение клиента и какую ценность он приносит бизнесу."),
    DailyTip("Ищи первопричину", "Не путай симптом проблемы с её настоящей причиной."),
    DailyTip("Подготовь план", "Разбей реализацию решения на последовательные шаги с ответственными и сроками."),
    DailyTip("Проверяй результат", "После внедрения сравни фактические показатели с теми, которых ожидал."),
    DailyTip("Объясняй свою логику", "Покажи, как данные и анализ привели тебя к выбранному решению."),
    DailyTip("Давай рекомендацию", "В конце кейса чётко скажи, что нужно сделать и почему."),
    DailyTip("Подумай о следующем шаге", "Укажи, что нужно проверить или измерить в первую очередь после принятия решения.")
)

@Composable
fun HomeScreen(
    username: String,
    onNavigate: (Screen) -> Unit
) {
    val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
    val tipIndex = (dayOfYear - 1) % dailyTips.size
    val todayTip = dailyTips[tipIndex]
    Scaffold(
        containerColor = Color.White,
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
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Добро пожаловать!",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Text1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = username,
                fontSize = 18.sp,
                color = Text2
            )
            Spacer(modifier = Modifier.height(30.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Red
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "AI Case Trainer",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Решайте кейсы и получайте оценку от ИИ",
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            onNavigate(Screen.CASES)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Green
                        )
                    ) {
                        Text(
                            text = "Начать",
                            color = Color.Black
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Red
                )
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = "💡 Совет дня",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = todayTip.title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFB9F42B)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = todayTip.text,
                        fontSize = 14.sp,
                        color = Color.White,
                        lineHeight = 21.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Red
                ),
                onClick = {
                    onNavigate(Screen.GUIDE)
                }
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Как пользоваться?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Краткий гайд: как решать кейсы и получать оценку",
                        color = Color.White,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Открыть гайд →",
                        color = Color(0xFFB9F42B),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
