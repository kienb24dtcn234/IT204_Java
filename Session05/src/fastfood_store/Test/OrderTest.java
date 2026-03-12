import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderTest {

    @Test
    void testTotalPrice() {

        Order order = new Order("1");

        MenuItem food = new Food("f1", "Burger", 50000);

        order.addItem(food, 2);

        Assertions.assertEquals(100000, order.calculateTotal());
    }
}