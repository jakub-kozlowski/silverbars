package silverbars;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class LiveBoard {

    final List<OrderForUser> orders;

    private final Comparator<Order> orderComparator = (orderA, orderB) ->
            Order.Type.SELL.equals(orderA.getType()) && Order.Type.BUY.equals(orderB.getType()) ? -1
                    : Order.Type.BUY.equals(orderA.getType()) && Order.Type.SELL.equals(orderB.getType()) ? 1
                    : Order.Type.SELL.equals(orderA.getType()) ? orderA.getPrice().compareTo(orderB.getPrice())
                    : orderB.getPrice().compareTo(orderA.getPrice());

    public LiveBoard() {
        orders = new LinkedList<OrderForUser>();
    }

    public void registerOrder(OrderForUser order) {
        orders.add(order);
    }

    public void cancelOrder(OrderForUser order) {
        orders.removeIf(orderInList -> orderInList.getUserId().equals(order.getUserId())
                            && orderInList.getType() == order.getType()
                            && orderInList.getPrice().compareTo(order.getPrice()) == 0
                            && orderInList.getQuantity().compareTo(order.getQuantity()) == 0);
    }

    public List<Order> getSummaryInformation() {
        return orders.stream()
                .map(Order.class::cast)
                .collect( Collectors.groupingBy(order -> new Order(order.getType(), order.getPrice(), BigDecimal.ZERO),
                        Collectors.reducing(BigDecimal.ZERO, Order::getQuantity, BigDecimal::add)) )
                .entrySet().stream()
                .map(entry -> new Order(entry.getKey().getType(), entry.getKey().getPrice(), entry.getValue()))
                .sorted(orderComparator)
                .collect(Collectors.toList());
    }
}
