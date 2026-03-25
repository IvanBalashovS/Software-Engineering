import main.kotlin.Calculator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CalculatorTest {
    private val calculator = Calculator()

    @Test
    fun `test simple addition`() {
        val rpn = calculator.toRPN("2 + 2")
        assertEquals(listOf("2", "2", "+"), rpn)
        assertEquals(4.0, calculator.evaluate(rpn))
    }

    @Test
    fun `test precedence multiplication before addition`() {
        val rpn = calculator.toRPN("2 + 3 * 4")
        assertEquals(listOf("2", "3", "4", "*", "+"), rpn)
        assertEquals(14.0, calculator.evaluate(rpn))
    }

    @Test
    fun `test parentheses override precedence`() {
        val rpn = calculator.toRPN("(2 + 3) * 4")
        assertEquals(listOf("2", "3", "+", "4", "*"), rpn)
        assertEquals(20.0, calculator.evaluate(rpn))
    }

    @Test
    fun `test power has highest precedence`() {
        val rpn = calculator.toRPN("2 * 3 ^ 2")
        assertEquals(listOf("2", "3", "2", "^", "*"), rpn)
        assertEquals(18.0, calculator.evaluate(rpn))
    }

    @Test
    fun `test subtraction is left associative`() {
        // 10 - 5 - 2 = (10-5) - 2 = 3
        val rpn = calculator.toRPN("10 - 5 - 2")
        assertEquals(listOf("10", "5", "-", "2", "-"), rpn)
        assertEquals(3.0, calculator.evaluate(rpn))
    }

    @Test
    fun `test division by zero throws exception`() {
        assertThrows<ArithmeticException> {
            calculator.calculate("5 / 0")
        }
    }

    @Test
    fun `test complex expression`() {
        // (2 + 3) * 4 - 10 / 2 ^ 1 = 20 - 5 = 15
        val result = calculator.calculate("(2 + 3) * 4 - 10 / 2 ^ 1")
        assertEquals(15.0, result)
    }

    @Test
    fun `test floating point numbers`() {
        val result = calculator.calculate("2.5 + 3.5 * 2")
        assertEquals(9.5, result)
    }
}
