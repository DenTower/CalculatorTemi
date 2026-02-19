package com.example.calculatortemi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculatortemi.ui.theme.CalculatorTemiTheme


// Главный экран приложения
class MainActivity: ComponentActivity() {

    // onCreate — стандартная точка запуска Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculatorTemiTheme {

                // Получаем ViewModel, класс через который идет обработка действий пользователя
                // и изменение значений состояния калькулятора
                val viewModel = viewModel<CalculatorViewModel>()

                // Состояние калькулятора (числа, операция и т.п.)
                val state = viewModel.state

                // Расстояние между кнопками
                val buttonSpacing = 8.dp

                // Цвета кнопок
                val mediumGray = Color(0xFF9E9E9E)
                val orange = Color(0xFFFFA500)

                // Box — контейнер для UI элементов
                Box(
                    modifier = Modifier
                        .fillMaxSize() // занять весь экран
                        .background(Color.DarkGray) // цвет фона
                        .padding(16.dp) // отступы от границ
                ) {

                    // Column — вертикальная разметка
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter), // прижать вниз
                        verticalArrangement = Arrangement
                            .spacedBy(buttonSpacing), // Расстояние между кнопками
                    ) {

                        // состояние прокрутки для строки результата
                        val scrollState = rememberScrollState()

                        // LaunchedEffect — срабатывает при изменении state и при первом запуске экрана
                        LaunchedEffect(state) {
                            // автоскролл вправо при добавлении цифр
                            scrollState.animateScrollTo(scrollState.maxValue)
                        }

                        // длина всей строки выражения
                        val length = (state.number1 + (state.operation?.symbol ?: "") + state.number2).length

                        // динамический размер шрифта
                        // чем длиннее выражение — тем меньше шрифт
                        val fontSize = when {
                            length < 8 -> 80.sp
                            length < 12 -> 60.sp
                            length < 16 -> 45.sp
                            else -> 35.sp
                        }

                        // Text — отображение текущего выражения
                        Text(
                            text = state.number1 + (state.operation?.symbol ?: "") + state.number2,
                            textAlign = TextAlign.End, // выравнивание вправо
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp)
                                .horizontalScroll(scrollState), // включение горизонтального скролла выражения
                            fontWeight = FontWeight.Light,
                            fontSize = fontSize,
                            color = Color.White,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Clip
                        )

                        // --- ДАЛЕЕ ИДУТ РЯДЫ КНОПОК ---
                        // Row — горизонтальный layout
                        // AC Del /
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                        ) {
                            // Кнопка очистки
                            CalculatorButton(
                                symbol = "AC",
                                color = Color.LightGray,
                                modifier = Modifier
                                    .aspectRatio(2f) // шире обычной
                                    .weight(2f)      // занимает больше места
                            ) {
                                viewModel.onAction(CalculatorAction.Clear)
                            }

                            // Кнопка удаления символа
                            CalculatorButton(
                                symbol = "Del",
                                color = Color.LightGray,
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .weight(1f)
                            ) {
                                viewModel.onAction(CalculatorAction.Delete)
                            }

                            // Кнопка деления
                            CalculatorButton(
                                symbol = "/",
                                color = orange,
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .weight(1f)
                            ) {
                                viewModel.onAction(
                                    CalculatorAction.Operation(
                                        CalculatorOperation.Divide
                                    )
                                )
                            }
                        }

                        // 7 8 9 x
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                        ) {
                            CalculatorButton("7", color = mediumGray, modifier = Modifier.aspectRatio(1f).weight(1f)) {
                                viewModel.onAction(CalculatorAction.Number(7))
                            }
                            CalculatorButton("8", color = mediumGray, modifier = Modifier.aspectRatio(1f).weight(1f)) {
                                viewModel.onAction(CalculatorAction.Number(8))
                            }
                            CalculatorButton("9", color = mediumGray, modifier = Modifier.aspectRatio(1f).weight(1f)) {
                                viewModel.onAction(CalculatorAction.Number(9))
                            }
                            CalculatorButton("x", color = orange, modifier = Modifier.aspectRatio(1f).weight(1f)) {
                                viewModel.onAction(
                                    CalculatorAction.Operation(
                                        CalculatorOperation.Multiply
                                    )
                                )
                            }
                        }

                        // 4 5 6 -
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                        ) {
                            CalculatorButton("4", color = mediumGray, modifier = Modifier.aspectRatio(1f).weight(1f)) {
                                viewModel.onAction(CalculatorAction.Number(4))
                            }
                            CalculatorButton("5", color = mediumGray, modifier = Modifier.aspectRatio(1f).weight(1f)) {
                                viewModel.onAction(CalculatorAction.Number(5))
                            }
                            CalculatorButton("6", color = mediumGray, modifier = Modifier.aspectRatio(1f).weight(1f)) {
                                viewModel.onAction(CalculatorAction.Number(6))
                            }
                            CalculatorButton("-", color = orange, modifier = Modifier.aspectRatio(1f).weight(1f)) {
                                viewModel.onAction(
                                    CalculatorAction.Operation(
                                        CalculatorOperation.Subtract
                                    )
                                )
                            }
                        }

                        // 1 2 3 +
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                        ) {
                            CalculatorButton("1", color = mediumGray, modifier = Modifier.aspectRatio(1f).weight(1f)) {
                                viewModel.onAction(CalculatorAction.Number(1))
                            }
                            CalculatorButton("2", color = mediumGray, modifier = Modifier.aspectRatio(1f).weight(1f)) {
                                viewModel.onAction(CalculatorAction.Number(2))
                            }
                            CalculatorButton("3", color = mediumGray, modifier = Modifier.aspectRatio(1f).weight(1f)) {
                                viewModel.onAction(CalculatorAction.Number(3))
                            }
                            CalculatorButton("+", color = orange, modifier = Modifier.aspectRatio(1f).weight(1f)) {
                                viewModel.onAction(
                                    CalculatorAction.Operation(
                                        CalculatorOperation.Add
                                    )
                                )
                            }
                        }

                        // ( 0 ) . =
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                        ) {
                            CalculatorButton("0", color = mediumGray, modifier = Modifier.aspectRatio(2f).weight(2f)) {
                                viewModel.onAction(CalculatorAction.Number(0))
                            }
                            CalculatorButton(".", color = mediumGray, modifier = Modifier.aspectRatio(1f).weight(1f)) {
                                viewModel.onAction(CalculatorAction.Decimal)
                            }
                            CalculatorButton("=", color = orange, modifier = Modifier.aspectRatio(1f).weight(1f)) {
                                viewModel.onAction(CalculatorAction.Calculate)
                            }
                        }
                    }
                }
            }
        }
    }
}
