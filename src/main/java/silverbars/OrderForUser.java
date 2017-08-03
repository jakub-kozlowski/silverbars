package silverbars;

import java.math.BigDecimal;

public class OrderForUser extends Order {

    private final String userId;

    private OrderForUser(String userId, Type type, BigDecimal price, BigDecimal quantity ) {
        super(type, price, quantity);

        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public static class Builder {
        private String userId;
        private Type type;
        private BigDecimal price;
        private BigDecimal quantity;

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder type(Order.Type type) {
            this.type = type;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder quantity(BigDecimal quantity) {
            this.quantity = quantity;
            return this;
        }

        private void validate(OrderForUser orderForUser) {
            if( orderForUser.userId == null
                    || orderForUser.type == null
                    || orderForUser.price == null
                    || orderForUser.quantity == null)
                throw new IllegalStateException();
        }

        public OrderForUser build() {
            final OrderForUser orderForUser = new OrderForUser(userId, type, price, quantity);
            validate(orderForUser);
            return orderForUser;
        }
    }
}
