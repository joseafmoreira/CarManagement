package dev.joseafmoreira.fuel;

public class Fuel {
    private final FuelType type;
    private double price;

    public Fuel(FuelType type, double price) {
        this.type = type;
        setPrice(price);
    }

    public FuelType getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
