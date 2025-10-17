package test;
// import org.junit.jupiter.api.Test;
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

    @Test
    public void testSubtract() {
        Calculator calculator = new Calculator();
        int result = calculator.subtract(5, 3);
        assertEquals(2, result);
    }
}