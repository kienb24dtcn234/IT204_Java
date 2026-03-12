package model;

public class Food extends MenuItem {

    public Food(String id, String name, double price) {
        super(id, name, price);
    }

    @Override
    public double calculatePrice() {
        return getBasePrice();
    }
}