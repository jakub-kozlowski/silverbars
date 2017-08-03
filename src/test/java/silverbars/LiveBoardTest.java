package silverbars;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class LiveBoardTest {

    public static final String USER_ID = "userId";
    public static final BigDecimal ANY_QUANTITY = new BigDecimal("3.5");
    public static final BigDecimal ANY_PRICE = new BigDecimal("303");

    private LiveBoard board;

    @Before
    public void setup() {
        board = new LiveBoard();
    }

    @Test
    public void whenOrderRegistered_summaryInformation_hasOneOrder() {
        board.registerOrder(createAnyOrderForUser(USER_ID));

        assertThat(board.getSummaryInformation().size(), is(1));
    }

    @Test
    public void whenOnlyOrderCancelled_summaryInformationIsEmpty() {
        final OrderForUser order = createAnyOrderForUser(USER_ID);

        board.registerOrder(order);
        board.cancelOrder(new OrderForUser.Builder()
                .userId(order.getUserId())
                .type(order.getType())
                .price(order.getPrice())
                .quantity(order.getQuantity())
                .build());

        assertThat(board.getSummaryInformation().size(), is(0));
    }

    @Test
    public void ordersOfSameType_andSamePrice_areCollated_inSummaryInformation() {
        board.registerOrder( createOrder(USER_ID, Order.Type.SELL, BigDecimal.ONE, BigDecimal.ONE));
        board.registerOrder( createOrder(USER_ID, Order.Type.SELL, BigDecimal.ONE, BigDecimal.ONE));
        board.registerOrder( createOrder(USER_ID, Order.Type.BUY, BigDecimal.ONE, BigDecimal.ONE));
        board.registerOrder( createOrder(USER_ID, Order.Type.SELL, BigDecimal.TEN, BigDecimal.ONE));

        final List<Order> summaryInfo = board.getSummaryInformation();

        assertThat(summaryInfo.size(), is(3));
        assertTrue(summaryInfoContainsOrder(summaryInfo, Order.Type.SELL, BigDecimal.ONE, new BigDecimal("2")));
        assertTrue(summaryInfoContainsOrder(summaryInfo, Order.Type.SELL, BigDecimal.TEN, BigDecimal.ONE));
        assertTrue(summaryInfoContainsOrder(summaryInfo, Order.Type.BUY, BigDecimal.ONE, BigDecimal.ONE));
    }

    @Test
    public void ordersAreSorted_withSellOrdersWithLowestPricesFirst_finishingWithBuyOrdersWithLowestPrices() {
        board.registerOrder(createOrder("user1", Order.Type.BUY, new BigDecimal("80"), new BigDecimal("3.5")));
        board.registerOrder(createOrder("user1", Order.Type.BUY, new BigDecimal("50"), new BigDecimal("3.5")));
        board.registerOrder(createOrder("user1", Order.Type.BUY, new BigDecimal("100"), new BigDecimal("3.5")));
        board.registerOrder(createOrder("user1", Order.Type.SELL, new BigDecimal("100"), new BigDecimal("0.7")));
        board.registerOrder(createOrder("user1", Order.Type.SELL, new BigDecimal("20"), new BigDecimal("0.7")));
        board.registerOrder(createOrder("user1", Order.Type.SELL, new BigDecimal("50"), new BigDecimal("0.7")));

        final List<Order> summaryInfo = board.getSummaryInformation();

        assertThat(summaryInfo.size(), is(6));
        assertTrue(orderMatches(summaryInfo.get(0), Order.Type.SELL, 20));
        assertTrue(orderMatches(summaryInfo.get(1), Order.Type.SELL, 50));
        assertTrue(orderMatches(summaryInfo.get(2), Order.Type.SELL, 100));
        assertTrue(orderMatches(summaryInfo.get(3), Order.Type.BUY, 100));
        assertTrue(orderMatches(summaryInfo.get(4), Order.Type.BUY, 80));
        assertTrue(orderMatches(summaryInfo.get(5), Order.Type.BUY, 50));
    }

    private boolean orderMatches(Order order, Order.Type type, int quantity) {
        return order.getType() == type && order.getPrice().compareTo(BigDecimal.valueOf(quantity)) == 0;
    }

    private boolean summaryInfoContainsOrder(List<Order> summaryInfo, Order.Type type, BigDecimal price, BigDecimal quantity) {
        return summaryInfo.stream().anyMatch(order -> order.getType() == type
                && order.getPrice().compareTo(price) == 0
                && order.getQuantity().compareTo(quantity) == 0);
    }

    private OrderForUser createAnyOrderForUser(String userId) {
        return new OrderForUser.Builder()
                .userId(userId)
                .type(Order.Type.SELL)
                .price(ANY_PRICE)
                .quantity(ANY_QUANTITY).build();
    }

    private OrderForUser createOrder(String userId, Order.Type type, BigDecimal price, BigDecimal quantity) {
        return new OrderForUser.Builder()
                .userId(userId)
                .type(type)
                .price(price)
                .quantity(quantity).build();
    }
}