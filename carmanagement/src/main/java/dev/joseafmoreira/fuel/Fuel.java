package dev.joseafmoreira.fuel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Fuel {
    private final static int PRIME_NUMBER = 31;
    private final FuelType type;
    private BigDecimal price;

    public Fuel(FuelType type, double price) {
        this.type = type;
        setPrice(price);
    }

    public String getName() {
        return type.toString();
    }

    public FuelType getType() {
        return type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = BigDecimal.valueOf(price).setScale(3, RoundingMode.HALF_UP);
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        hashCode = PRIME_NUMBER * hashCode + type.hashCode();
        hashCode = PRIME_NUMBER * hashCode + price.hashCode();
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || !(obj instanceof Fuel))
            return false;
        Fuel otherFuel = (Fuel) obj;
        return type == otherFuel.type && price.compareTo(otherFuel.price) == 0;
    }

    @Override
    public String toString() {
        return getName() + "(" + (new DecimalFormat("0.000").format(price)) + "€)";
    }
}
