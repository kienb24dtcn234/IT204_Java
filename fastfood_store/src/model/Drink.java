package model;

public class Drink extends MenuItem {

    private String size;

    public Drink(String id, String name, double price, String size) {
        super(id, name, price);
        this.size = size;
    }

    @Override
    public double calculatePrice() {

        switch (size) {
            case "L":
                return getBasePrice() + 10000;
            case "M":
                return getBasePrice() + 5000;
            default:
                return getBasePrice();
        }
    }
}