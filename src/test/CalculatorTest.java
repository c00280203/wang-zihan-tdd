package test;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;
import main.Calculator;

public class CalculatorTest {
    
    @ParameterizedTest
    @CsvSource({
        "2, 3, 5",
        "0, 0, 0",
        "-1, 1, 0",
        "-5, -3, -8",
        "100, 200, 300"
    })
    public void testAdd(int a, int b, int expected) {
        Calculator calculator = new Calculator();
        int result = calculator.add(a, b);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @CsvSource({
        "5, 3, 2",
        "0, 0, 0",
        "1, 1, 0",
        "-1, -1, 0",
        "10, 15, -5"
    })
    public void testSubtract(int a, int b, int expected) {
        Calculator calculator = new Calculator();
        int result = calculator.subtract(a, b);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @CsvSource({
        "4, 3, 12",
        "0, 5, 0",
        "7, 0, 0",
        "-2, 3, -6",
        "-4, -5, 20"
    })
    public void testMultiply(int a, int b, int expected) {
        Calculator calculator = new Calculator();
        int result = calculator.multiply(a, b);
        assertEquals(expected, result);
    }

    @Test
    public void testDivide() {
        Calculator calculator = new Calculator();
        int result = calculator.divide(10, 2);
        assertEquals(5, result);
    }

    @ParameterizedTest
    @CsvSource({
        "10, 2, 5",
        "0, 5, 0",
        "15, 3, 5",
        "-10, 2, -5",
        "-20, -4, 5"
    })
    public void testDivide(int a, int b, int expected) {
        Calculator calculator = new Calculator();
        int result = calculator.divide(a, b);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @CsvSource({
        "10, 0",
        "0, 0",
        "-5, 0"
    })
    public void testDivideByZeroThrowsException(int a, int b) {
        Calculator calculator = new Calculator();
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.divide(a, b);
        });
    }
}