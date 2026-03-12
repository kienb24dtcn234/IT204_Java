package repository;

import model.Order;
import java.util.*;

public class OrderRepository {

    private Map<String, Order> orders = new HashMap<>();

    public void save(Order order) {
        orders.put(order.getId(), order);
    }

    public Optional<Order> findById(String id) {
        return Optional.ofNullable(orders.get(id));
    }

    public Collection<Order> findAll() {
        return orders.values();
    }
}