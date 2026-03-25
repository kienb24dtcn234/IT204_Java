package ui;

import model.*;
import repository.*;
import service.*;
import util.DiscountUtil;
import exception.InvalidOrderIdException;

import java.time.LocalDate;
import java.util.Scanner;

public class ConsoleMenu {

    private MenuService menuService;
    private OrderService orderService;
    private Scanner sc = new Scanner(System.in);

    public ConsoleMenu(MenuService menuService, OrderService orderService) {
        this.menuService = menuService;
        this.orderService = orderService;
    }

    public void start() {

        while (true) {

            System.out.println("\n===== FAST FOOD STORE =====");
            System.out.println("1. Add Menu Item");
            System.out.println("2. Show Menu");
            System.out.println("3. Search Menu By Name");
            System.out.println("4. Search Menu Price");
            System.out.println("3. Search Menu By Name");
            System.out.println("4. Search Menu Price");
            System.out.println("5. Delete Menu Item");

            System.out.println("6. Create Order");
            System.out.println("7. Add Item To Order");
            System.out.println("8. Apply Discount");
            System.out.println("9. Update Order Status");

            System.out.println("10. Revenue Statistic");
            System.out.println("11. Best Seller");
            System.out.println("12. View Orders");
            System.out.println("0. Exit");

            System.out.print("Choose: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    addMenuItem();
                    break;

                case 2:
                    showMenu();
                    break;

                case 3:
                    searchMenu();
                    break;

                case 4:
                    searchByPrice();
                    break;

                case 5:
                    deleteMenuItem();
                    break;

                case 6:
                    createOrder();
                    break;

                case 7:
                    addItemToOrder();
                    break;

                case 8:
                    applyDiscount();
                    break;

                case 9:
                    updateStatus();
                    break;

                case 10:
                    revenueStatistic();
                    break;

                case 11:
                    bestSeller();
                    break;

                case 12:
                    viewOrder();
                    break;

                case 0:
                    return;
            }
        }
    }

    private void addMenuItem() {

        System.out.print("ID: ");
        String id = sc.nextLine();

        System.out.print("Name: ");
        String name = sc.nextLine();

        double price;
        while (true) {
            System.out.print("Price: ");
            if (sc.hasNextDouble()) {
                price = sc.nextDouble();
                sc.nextLine();
                if (price > 0) break;
            } else {
                sc.nextLine();
            }
            System.out.println("Price must be > 0");
        }

        String type;
        while (true) {
            System.out.print("Type (food/drink/dessert): ");
            type = sc.nextLine();

            if (type.equalsIgnoreCase("food") ||
                    type.equalsIgnoreCase("drink") ||
                    type.equalsIgnoreCase("dessert")) {
                break;
            }

            System.out.println("Invalid type!");
        }

        MenuItem item;

        if (type.equalsIgnoreCase("drink")) {

            String size;
            while (true) {

                System.out.print("Size (S/M/L): ");
                size = sc.nextLine().toUpperCase();

                if (size.equals("S") || size.equals("M") || size.equals("L")) {
                    break;
                }

                System.out.println("Size must be S, M or L");
            }

            item = new Drink(id, name, price, size);

        } else if (type.equalsIgnoreCase("dessert")) {

            item = new Dessert(id, name, price);

        } else {

            item = new Food(id, name, price);
        }

        menuService.addItem(item);

        System.out.println("Added successfully");
    }

    private void showMenu() {

        menuService.getAll()
                .forEach(i ->
                        System.out.println(
                                i.getId() + " - " +
                                        i.getName() + " - " +
                                        i.calculatePrice()
                        ));
    }

    private void searchMenu() {

        String name;

        while (true) {

            System.out.print("Enter name: ");
            name = sc.nextLine();

            if (!name.isBlank()) break;

            System.out.println("Name cannot be empty");
        }

        menuService.searchByName(name)
                .forEach(i ->
                        System.out.println(
                                i.getName() + " - " +
                                        i.calculatePrice()
                        ));
    }

    private void deleteMenuItem() {

        System.out.print("Enter ID: ");
        String id = sc.nextLine();

        menuService.delete(id);

        System.out.println("Deleted");
    }

    private void createOrder() {

        System.out.print("Order ID: ");
        String id = sc.nextLine();

        orderService.createOrder(id);

        System.out.println("Order created");
    }

    private void addItemToOrder() {

        try {

            System.out.print("Order ID: ");
            String orderId = sc.nextLine();

            System.out.print("Menu ID: ");
            String menuId = sc.nextLine();

            int q;

            while (true) {

                System.out.print("Quantity: ");

                if (sc.hasNextInt()) {

                    q = sc.nextInt();
                    sc.nextLine();

                    if (q > 0) break;
                } else {
                    sc.nextLine();
                }

                System.out.println("Quantity must be > 0");
            }

            MenuItem item = menuService.getAll()
                    .stream()
                    .filter(i -> i.getId().equals(menuId))
                    .findFirst()
                    .orElse(null);

            if (item == null) {
                System.out.println("Menu not found");
                return;
            }

            orderService.addItem(orderId, item, q);

            System.out.println("Item added");

        } catch (InvalidOrderIdException e) {

            System.out.println(e.getMessage());
        }
    }

    private void applyDiscount() {

        try {

            System.out.print("Order ID: ");
            String id = sc.nextLine();

            double percent;

            while (true) {

                System.out.print("Discount %: ");

                if (sc.hasNextDouble()) {

                    percent = sc.nextDouble();
                    sc.nextLine();

                    if (percent >= 0 && percent <= 100) break;
                } else {
                    sc.nextLine();
                }

                System.out.println("Discount must be 0-100");
            }

            double total = orderService.getTotal(id);

            double after = DiscountUtil.applyDiscount(total, percent);

            System.out.println("Total after discount: " + after);

        } catch (InvalidOrderIdException e) {

            System.out.println(e.getMessage());
        }
    }

    private void updateStatus() {

        try {

            System.out.print("Order ID: ");
            String id = sc.nextLine();

            System.out.print("Status (PENDING/PAID/CANCELLED): ");
            String status = sc.nextLine();

            orderService.updateStatus(id, OrderStatus.valueOf(status));

            System.out.println("Status updated");

        } catch (Exception e) {

            System.out.println("Error");
        }
    }




    private void viewOrder() {

        System.out.print("Enter order id: ");
        String id = sc.nextLine();

        try {

            Order order = orderService.getOrder(id);

            System.out.println("Order ID: " + order.getId());
            System.out.println("Status: " + order.getStatus());

            for (var entry : order.getItems().entrySet()) {

                MenuItem item = entry.getKey();
                int quantity = entry.getValue();

                double total = item.calculatePrice() * quantity;

                System.out.println(
                        item.getName() +
                                " x " + quantity +
                                " = " + total
                );
            }

            System.out.println("Total: " + order.calculateTotal());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private void searchByPrice() {

        double min;
        double max;

        while (true) {

            System.out.print("Min price: ");

            if (sc.hasNextDouble()) {
                min = sc.nextDouble();
                sc.nextLine();
                if (min >= 0) break;
            } else {
                sc.nextLine();
            }

            System.out.println("Invalid price");
        }

        while (true) {

            System.out.print("Max price: ");

            if (sc.hasNextDouble()) {
                max = sc.nextDouble();
                sc.nextLine();
                if (max >= min) break;
            } else {
                sc.nextLine();
            }

            System.out.println("Max must >= Min");
        }

        menuService.searchByPrice(min, max)
                .forEach(i ->
                        System.out.println(
                                i.getId() + " - " +
                                        i.getName() + " - " +
                                        i.calculatePrice()
                        ));
    }
    private void bestSeller() {

        orderService.bestSeller()
                .entrySet()
                .stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(5)
                .forEach(e ->
                        System.out.println(
                                e.getKey() + " sold: " + e.getValue()
                        ));
    }
    private void revenueStatistic() {

        int choice;

        while (true) {

            System.out.println("1 Revenue by day");
            System.out.println("2 Revenue by month");

            System.out.print("Choose: ");

            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                sc.nextLine();

                if (choice == 1 || choice == 2) break;
            } else {
                sc.nextLine();
            }

            System.out.println("Invalid choice");
        }

        if (choice == 1) {

            System.out.print("Day (yyyy-mm-dd): ");
            String input = sc.nextLine();

            LocalDate date = LocalDate.parse(input);

            double revenue = orderService.revenueByDay(date);

            System.out.println("Revenue: " + revenue);

        } else {

            int month;

            while (true) {

                System.out.print("Month (1-12): ");

                if (sc.hasNextInt()) {

                    month = sc.nextInt();
                    sc.nextLine();

                    if (month >= 1 && month <= 12) break;
                } else {
                    sc.nextLine();
                }

                System.out.println("Month must be 1-12");
            }

            double revenue = orderService.revenueByMonth(month);

            System.out.println("Revenue: " + revenue);
        }
    }
}