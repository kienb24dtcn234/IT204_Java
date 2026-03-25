package util;

public class DiscountUtil {

    public static double applyDiscount(double total, double percent) {
        return total - total * percent / 100;
    }
}