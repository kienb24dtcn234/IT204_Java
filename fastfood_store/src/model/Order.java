package model;

import java.util.*;
import java.time.LocalDate;
public class Order {

    private String id;
    private LocalDate date;
    private Map<MenuItem, Integer> items = new HashMap<>();
    private OrderStatus status = OrderStatus.PENDING;

    public Order(String id) {
        this.id = id;
        this.date = LocalDate.now();
    }

    public void addItem(MenuItem item, int quantity) {

        items.put(
                item,
                items.getOrDefault(item, 0) + quantity
        );
    }

    public double calculateTotal() {

        return items.entrySet()
                .stream()
                .mapToDouble(e ->
                        e.getKey().calculatePrice() * e.getValue()
                )
                .sum();
    }
    public String getId() {
        return id;
    }
    public LocalDate getDate() {
        return date;
    }

    public Map<MenuItem, Integer> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}