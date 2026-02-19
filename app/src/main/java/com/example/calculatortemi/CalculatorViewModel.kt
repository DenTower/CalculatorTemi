package com.example.calculatortemi

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

// ViewModel — это слой логики между UI и данными.
// Он хранит состояние экрана и переживает повороты экрана,
// пересоздание Activity и т.п.
class CalculatorViewModel: ViewModel() {

    // mutableStateOf — реактивное состояние Compose.
    // Когда переменная меняется → UI автоматически перерисуется.
    var state by mutableStateOf(CalculatorState())


    // Главная точка входа всех действий пользователя.
    // UI отправляет сюда события (нажатия кнопок).
    fun onAction(action: CalculatorAction) {
        when(action) {

            // нажата цифра
            is CalculatorAction.Number -> enterNumber(action.number)

            // удалить последний символ
            is CalculatorAction.Delete -> delete()

            // полностью очистить калькулятор
            is CalculatorAction.Clear -> state = CalculatorState()

            // нажата операция (+ - * /)
            is CalculatorAction.Operation -> enterOperation(action.operation)

            // нажата точка
            is CalculatorAction.Decimal -> enterDecimal()

            // нажато =
            is CalculatorAction.Calculate -> calculate()
        }
    }


    // обработка ввода операции (+ - * /)
    private fun enterOperation(operation: CalculatorOperation) {

        // если уже введено выражение вида "2 + 3"
        // и пользователь нажал новую операцию
        // → сначала считаем результат
        if (state.number1.isNotBlank() && state.number2.isNotBlank()) {
            calculate()
        }

        // если есть только первое число — сохраняем операцию
        if (state.number1.isNotBlank()) {
            state = state.copy(operation = operation)
        }
    }


    // выполнение вычисления
    private fun calculate() {

        // безопасный парсинг строк в числа
        val number1 = state.number1.toDoubleOrNull()
        val number2 = state.number2.toDoubleOrNull()

        // если хотя бы одно число невалидное — ничего не делаем
        if(number1 != null && number2 != null) {

            // вычисляем результат в зависимости от операции
            val result = when(state.operation) {
                is CalculatorOperation.Add -> number1 + number2
                is CalculatorOperation.Subtract -> number1 - number2
                is CalculatorOperation.Multiply -> number1 * number2
                is CalculatorOperation.Divide -> number1 / number2
                null -> return // если операции нет — выходим
            }

            // обновляем состояние калькулятора
            state = state.copy(

                // результат становится первым числом
                number1 = result.toString().take(15),

                // второе число очищаем
                number2 = "",

                // операцию сбрасываем
                operation = null
            )
        }
    }


    // удаление символа (кнопка Del)
    private fun delete() {

        when {

            // если вводится второе число — удаляем из него
            state.number2.isNotBlank() -> state = state.copy(
                number2 = state.number2.dropLast(1)
            )

            // если есть операция — удаляем её
            state.operation != null -> state = state.copy(
                operation = null
            )

            // иначе удаляем из первого числа
            state.number1.isNotBlank() -> state = state.copy(
                number1 = state.number1.dropLast(1)
            )
        }
    }


    // ввод десятичной точки
    private fun enterDecimal() {

        // если операция ещё не выбрана — редактируем первое число
        if(
            state.operation == null &&
            !state.number1.contains(".") && // точка ещё не была введена
            state.number1.isNotBlank()
        ) {
            state = state.copy(
                number1 = state.number1 + "."
            )
            return
        }

        // иначе редактируем второе число
        else if(!state.number2.contains(".") && state.number2.isNotBlank()) {
            state = state.copy(
                number2 = state.number2 + "."
            )
        }
    }


    // ввод цифры
    private fun enterNumber(number: Int) {

        // если операция ещё не выбрана → вводим первое число
        if(state.operation == null) {

            // ограничение длины числа
            if(state.number1.length >= MAX_NUM_LENGTH) {
                return
            }

            state = state.copy(
                number1 = state.number1 + number
            )
            return
        }

        // иначе вводим второе число
        if(state.number2.length >= MAX_NUM_LENGTH) {
            return
        }

        state = state.copy(
            number2 = state.number2 + number
        )
    }

    companion object {

        // максимальная длина числа на экране чтобы умещался тип данных Double
        private const val MAX_NUM_LENGTH = 15
    }
}
