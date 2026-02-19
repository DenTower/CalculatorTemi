package com.example.calculatortemi

// класс состояния калькулятора хранит число1, число2, операцию
data class CalculatorState(
    val number1: String = "",
    val number2: String = "",
    val operation: CalculatorOperation? = null
)