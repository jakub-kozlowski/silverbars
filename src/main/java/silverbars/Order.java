package silverbars;

import java.math.BigDecimal;

public class Order {

    public enum Type {
        BUY, SELL
    }

    protected final Type type;
    protected final BigDecimal price;
    protected final BigDecimal quantity;

    public Order(Type type, BigDecimal price, BigDecimal quantity ) {
        this.type = type;
        this.price = price;
        this.quantity = quantity;
    }

    public Type getType() {
        return type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (type != order.type) return false;
        if (!price.equals(order.price)) return false;
        return quantity.equals(order.quantity);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + price.hashCode();
        result = 31 * result + quantity.hashCode();
        return result;
    }
}
