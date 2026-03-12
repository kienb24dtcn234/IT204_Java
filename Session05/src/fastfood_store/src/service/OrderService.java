package service;

import model.*;
import repository.OrderRepository;
import exception.InvalidOrderIdException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class OrderService {

    private OrderRepository repo;

    public OrderService(OrderRepository repo) {
        this.repo = repo;
    }

    public Order createOrder(String id) {
        Order order = new Order(id);
        repo.save(order);
        return order;
    }

    public void addItem(String orderId, MenuItem item, int quantity)
            throws InvalidOrderIdException {

        Order order = repo.findById(orderId)
                .orElseThrow(() -> new InvalidOrderIdException("Order not found"));

        order.addItem(item, quantity);
    }

    public double getTotal(String orderId) throws InvalidOrderIdException {

        Order order = repo.findById(orderId)
                .orElseThrow(() -> new InvalidOrderIdException("Order not found"));

        return order.calculateTotal();
    }

    public void updateStatus(String orderId, OrderStatus status)
            throws InvalidOrderIdException {

        Order order = repo.findById(orderId)
                .orElseThrow(() -> new InvalidOrderIdException("Order not found"));

        order.setStatus(status);
    }

    public Collection<Order> getAllOrders() {
        return repo.findAll();
    }

    public double getRevenue() {

        return repo.findAll()
                .stream()
                .filter(o -> o.getStatus() == OrderStatus.PAID)
                .mapToDouble(Order::calculateTotal)
                .sum();
    }
    public Map<String, Integer> bestSeller() {

        Map<String, Integer> map = new HashMap<>();

        for (Order order : repo.findAll()) {

            if (order.getStatus() != OrderStatus.PAID) continue;

            for (var entry : order.getItems().entrySet()) {

                MenuItem item = entry.getKey();
                int quantity = entry.getValue();

                System.out.println(
                        item.getName() +
                                " x " + quantity +
                                " = " + item.calculatePrice() * quantity
                );
            }
        }

        return map;
    }
    public double revenueByDay(LocalDate day) {

        return repo.findAll()
                .stream()
                .filter(o -> o.getStatus() == OrderStatus.PAID)
                .filter(o -> o.getDate().equals(day))
                .mapToDouble(Order::calculateTotal)
                .sum();
    }
    public double revenueByMonth(int month) {

        return repo.findAll()
                .stream()
                .filter(o -> o.getStatus() == OrderStatus.PAID)
                .filter(o -> o.getDate().getMonthValue() == month)
                .mapToDouble(Order::calculateTotal)
                .sum();
    }
    public Order getOrder(String id) throws InvalidOrderIdException {

        return repo.findById(id)
                .orElseThrow(() -> new InvalidOrderIdException("Order not found"));
    }
}