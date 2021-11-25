import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderBookTest {


    @Test
    void successfullyAddOrders() {
        OrderBook orderBook = new OrderBook();
        Order order1 = new Order("testOrder1", Side.SELL, Spot.BTC, 1100, BigDecimal.valueOf(58269.5));
        Order order2 = new Order("testOrder2", Side.SELL, Spot.BTC, 1100, BigDecimal.valueOf(58269.5));
        Order order3 = new Order("testOrder3", Side.SELL, Spot.BTC, 1100, BigDecimal.valueOf(58269.5));
        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(order3);

        List<Order> expectedSellOrders = new ArrayList<>();
        Collections.addAll(expectedSellOrders, order1, order2, order3);

        assertEquals(expectedSellOrders, orderBook.getSellOrders());

        Order order4 = new Order("testOrder4", Side.BUY, Spot.BTC, 1100, BigDecimal.valueOf(58269.5));
        Order order5 = new Order("testOrder5", Side.BUY, Spot.BTC, 1100, BigDecimal.valueOf(58269.5));
        Order order6 = new Order("testOrder6", Side.BUY, Spot.BTC, 1100, BigDecimal.valueOf(58269.5));
        orderBook.addOrder(order4);
        orderBook.addOrder(order5);
        orderBook.addOrder(order6);

        List<Order> expectedBuyOrders = new ArrayList<>();
        Collections.addAll(expectedBuyOrders, order4, order5, order6);

        assertEquals(expectedBuyOrders, orderBook.getBuyOrders());
    }

    @Test
    void successfullyCancelOrder() {
        OrderBook orderBook = new OrderBook();
        Order order1 = new Order("testOrder1", Side.SELL, Spot.BTC, 500, BigDecimal.valueOf(58269.5));
        Order order2 = new Order("testOrder2", Side.BUY, Spot.BTC, 652, BigDecimal.valueOf(58270));
        Order order3 = new Order("testOrder3", Side.SELL, Spot.BTC, 5451, BigDecimal.valueOf(58280));
        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(order3);

        //ensure order we are going to remove is there before we remove it
        assertEquals(order2, orderBook.getBuyOrders().get(0));

        orderBook.cancelOrder(order2.getOrderId());

        assertEquals(0, orderBook.getBuyOrders().size());
    }

    @Test
    void errorWhenRemovingNonExistingOrder() {
        OrderBook orderBook = new OrderBook();
        Order order1 = new Order("testOrder1", Side.SELL, Spot.BTC, 500, BigDecimal.valueOf(58269.5));
        orderBook.addOrder(order1);
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    orderBook.cancelOrder("fakeOrder1");
                }
        );
        assertEquals(thrown.getMessage(), "OrderID fakeOrder1 is not found within OrderBook");
    }
}