public class MathUtils {
    public static int max(int a, int b) {
        if (a >= b) return a;
        return b;
    }

    public static int divide(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("Divider must not be zero");
        }
        return a / b;
    }
}
