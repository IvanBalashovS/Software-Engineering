package main.kotlin

enum class Operator(val precedence: Int) {
    PLUS(1),
    MINUS(1),
    MULTIPLY(2),
    DIVIDE(2),
    POWER(3), // 3-й уровень приоритета
    ;

    companion object {
        fun fromChar(char: Char): Operator? =
            when (char) {
                '+' -> PLUS
                '-' -> MINUS
                '*' -> MULTIPLY
                '/' -> DIVIDE
                '^' -> POWER
                else -> null
            }
    }
}
