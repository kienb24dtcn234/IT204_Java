import repository.*;
import service.*;
import ui.ConsoleMenu;

public class Main {

    public static void main(String[] args) {

        MenuRepository menuRepo = new MenuRepository();
        OrderRepository orderRepo = new OrderRepository();

        MenuService menuService = new MenuService(menuRepo);
        OrderService orderService = new OrderService(orderRepo);

        ConsoleMenu menu = new ConsoleMenu(menuService, orderService);

        menu.start();
    }
}