package main.kotlin

class Calculator {
    private fun isOperator(char: Char): Boolean = Operator.fromChar(char) != null

    fun toRPN(expression: String): List<String> {
        val output = mutableListOf<String>()
        val opsStack = ArrayDeque<String>()
        val tokens = expression.replace(" ", "")
        var i = 0

        while (i < tokens.length) {
            val char = tokens[i]

            when {
                char.isDigit() || char == '.' -> {
                    val numBuilder = StringBuilder()
                    while (i < tokens.length && (tokens[i].isDigit() || tokens[i] == '.')) {
                        numBuilder.append(tokens[i++])
                    }
                    output.add(numBuilder.toString())
                    continue
                }
                char == '(' -> opsStack.addLast("(")
                char == ')' -> {
                    while (opsStack.isNotEmpty() && opsStack.last() != "(") {
                        output.add(opsStack.removeLast())
                    }
                    if (opsStack.isNotEmpty()) opsStack.removeLast()
                }
                isOperator(char) -> {
                    val currentOp = Operator.fromChar(char)!!
                    while (opsStack.isNotEmpty() && opsStack.last() != "(") {
                        val topOpChar = opsStack.last()
                        if (isOperator(topOpChar[0])) {
                            val topOp = Operator.fromChar(topOpChar[0])!!
                            //тут делаем баг
                            if (topOp.precedence >= currentOp.precedence) {
                                output.add(opsStack.removeLast())
                            } else {
                                break
                            }
                        } else {
                            break
                        }
                    }
                    opsStack.addLast(char.toString())
                }
                else -> throw IllegalArgumentException("Unexpected character: $char at position $i")
            }
            i++
        }

        while (opsStack.isNotEmpty()) {
            val op = opsStack.removeLast()
            if (op == "(") throw IllegalArgumentException("Mismatched parentheses")
            output.add(op)
        }

        return output
    }

    fun evaluate(rpn: List<String>): Double {
        val stack = ArrayDeque<Double>()

        for (token in rpn) {
            when {
                token.toDoubleOrNull() != null -> {
                    stack.addLast(token.toDouble())
                }
                token == "+" -> {
                    require(stack.size >= 2) { "Invalid RPN" }
                    stack.addLast(stack.removeLast() + stack.removeLast())
                }
                token == "-" -> {
                    require(stack.size >= 2) { "Invalid RPN" }
                    val b = stack.removeLast()
                    val a = stack.removeLast()
                    stack.addLast(a - b)
                }
                token == "*" -> {
                    require(stack.size >= 2) { "Invalid RPN" }
                    stack.addLast(stack.removeLast() * stack.removeLast())
                }
                token == "/" -> {
                    require(stack.size >= 2) { "Invalid RPN" }
                    val b = stack.removeLast()
                    val a = stack.removeLast()
                    if (b == 0.0) throw ArithmeticException("Division by zero")
                    stack.addLast(a / b)
                }
                token == "^" -> {
                    require(stack.size >= 2) { "Invalid RPN" }
                    val b = stack.removeLast()
                    val a = stack.removeLast()
                    val result = Math.pow(a, b)
                    if (result.isInfinite() || result.isNaN()) {
                        throw ArithmeticException("Invalid power operation")
                    }
                    stack.addLast(result)
                }
                else -> throw IllegalArgumentException("Unknown token: $token")
            }
        }

        require(stack.size == 1) { "Invalid RPN: expected 1 result, got ${stack.size}" }
        return stack.first()
    }

    fun calculate(expression: String): Double {
        return evaluate(toRPN(expression))
    }
}
