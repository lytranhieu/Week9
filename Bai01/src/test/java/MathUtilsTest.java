import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MathUtilsTest {

    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(MathUtilsTest.class);

    @BeforeAll
    static void beforeAll() {
        logger.info("Bat dau chay MathUtilsTest");
    }

    @Test
    void max_whenAGreaterThanB() {
        assertEquals(8, MathUtils.max(8, 3));
    }

    @Test
    void max_whenAEqualsB() {
        assertEquals(5, MathUtils.max(5, 5));
    }

    @Test
    void max_whenALessThanB() {
        assertEquals(9, MathUtils.max(2, 9));
    }

    @Test
    void max_bothAtMinValue() {
        assertEquals(Integer.MIN_VALUE,
                MathUtils.max(Integer.MIN_VALUE, Integer.MIN_VALUE));
    }

    @Test
    void max_minAndMinPlusOne() {
        assertEquals(Integer.MIN_VALUE + 1,
                MathUtils.max(Integer.MIN_VALUE, Integer.MIN_VALUE + 1));
    }

    @Test
    void max_minPlusOneAndMin() {
        assertEquals(Integer.MIN_VALUE + 1,
                MathUtils.max(Integer.MIN_VALUE + 1, Integer.MIN_VALUE));
    }

    @Test
    void max_bothAtMaxValue() {
        assertEquals(Integer.MAX_VALUE,
                MathUtils.max(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    @Test
    void max_maxAndMaxMinusOne() {
        assertEquals(Integer.MAX_VALUE,
                MathUtils.max(Integer.MAX_VALUE, Integer.MAX_VALUE - 1));
    }

    @Test
    void max_maxMinusOneAndMax() {
        assertEquals(Integer.MAX_VALUE,
                MathUtils.max(Integer.MAX_VALUE - 1, Integer.MAX_VALUE));
    }

    @Test
    void max_minAndMax() {
        assertEquals(Integer.MAX_VALUE,
                MathUtils.max(Integer.MIN_VALUE, Integer.MAX_VALUE));
    }

    @Test
    void max_maxAndMin() {
        assertEquals(Integer.MAX_VALUE,
                MathUtils.max(Integer.MAX_VALUE, Integer.MIN_VALUE));
    }

    @Test
    void divide_whenBPositive() {
        assertEquals(5, MathUtils.divide(10, 2));
    }

    @Test
    void divide_whenBNegative() {
        assertEquals(-5, MathUtils.divide(10, -2));
    }

    @Test
    void divide_whenBZero_throwsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> MathUtils.divide(10, 0));
        assertEquals("Divider must not be zero", exception.getMessage());
    }

    @Test
    void divide_truncatesFraction() {
        assertEquals(3, MathUtils.divide(7, 2));
    }

    @Test
    void divide_whenANegative() {
        assertEquals(-3, MathUtils.divide(-7, 2));
    }

    @Test
    void divide_whenAZero() {
        assertEquals(0, MathUtils.divide(0, -3));
    }

    @AfterAll
    static void afterAll() {
        logger.info("Ket thuc MathUtilsTest");
    }
}
