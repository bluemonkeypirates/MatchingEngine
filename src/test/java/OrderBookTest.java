import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OrderBookTest {


    @Test
    void getBestBuyOrder(){
        OrderBook orderBook = new OrderBook();
        Order order1 = new Order("testOrder1", Side.BUY, Spot.BTC, 20, BigDecimal.valueOf(60000), OrderType.MARKET_ORDER);
        Order order2 = new Order("testOrder2", Side.BUY, Spot.BTC, 50, BigDecimal.valueOf(65000), OrderType.MARKET_ORDER);
        Order order3 = new Order("testOrder3", Side.BUY, Spot.BTC, 100, BigDecimal.valueOf(55000), OrderType.MARKET_ORDER);
        Order order4 = new Order("testOrder4", Side.BUY, Spot.ETH, 100, BigDecimal.valueOf(4000), OrderType.MARKET_ORDER);

        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(order3);
        orderBook.addOrder(order4);

        Order sellOrder = new Order("testOrder4", Side.SELL, Spot.BTC, 60, BigDecimal.valueOf(50000), OrderType.MARKET_ORDER);
        Optional<Order> matchingOrder = orderBook.matchSell(sellOrder);
        assertTrue(matchingOrder.isPresent());
        assertEquals(Side.BUY,matchingOrder.get().getSide());
        assertEquals(BigDecimal.valueOf(65000),matchingOrder.get().getPrice());
    }

    @Test
    void matchSellWhenUsingMarketOrderNoneWithLimitOrder(){
        OrderBook orderBook = new OrderBook();
        Order order1 = new Order("testOrder1", Side.BUY, Spot.BTC, 20, BigDecimal.valueOf(60000), OrderType.MARKET_ORDER);
        Order order2 = new Order("testOrder2", Side.BUY, Spot.BTC, 50, BigDecimal.valueOf(65000), OrderType.MARKET_ORDER);
        Order order3 = new Order("testOrder3", Side.BUY, Spot.BTC, 100, BigDecimal.valueOf(55000), OrderType.MARKET_ORDER);
        Order order4 = new Order("testOrder4", Side.BUY, Spot.ETH, 100, BigDecimal.valueOf(4000), OrderType.MARKET_ORDER);

        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(order3);
        orderBook.addOrder(order4);

        Order sellOrder = new Order("testOrder4", Side.SELL, Spot.BTC, 60, BigDecimal.valueOf(60000), OrderType.MARKET_ORDER);
        Optional<Order> matchingOrderMarket = orderBook.matchSell(sellOrder);
        assertTrue(matchingOrderMarket.isPresent());
        assertEquals(Side.BUY,matchingOrderMarket.get().getSide());
        assertEquals(BigDecimal.valueOf(65000),matchingOrderMarket.get().getPrice());

        //as the sell order price is 70000, and the highest BTC buy is 65000, there wont be any matching orders as it is a LIMIT_ORDER
        Order sellOrderLimit = new Order("testOrder4", Side.SELL, Spot.BTC, 60, BigDecimal.valueOf(70000), OrderType.LIMIT_ORDER);
        Optional<Order> matchingOrderLimit = orderBook.matchSell(sellOrderLimit);
        assertFalse(matchingOrderLimit.isPresent());
    }

    @Test
    void matchBuyWhenUsingMarketOrderNoneWithLimitOrder(){
        OrderBook orderBook = new OrderBook();
        Order order1 = new Order("testOrder1", Side.SELL, Spot.BTC, 20, BigDecimal.valueOf(60000), OrderType.MARKET_ORDER);
        Order order2 = new Order("testOrder2", Side.SELL, Spot.BTC, 50, BigDecimal.valueOf(65000), OrderType.MARKET_ORDER);
        Order order3 = new Order("testOrder3", Side.SELL, Spot.BTC, 100, BigDecimal.valueOf(55000), OrderType.MARKET_ORDER);
        Order order4 = new Order("testOrder4", Side.SELL, Spot.ETH, 100, BigDecimal.valueOf(4000), OrderType.MARKET_ORDER);

        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(order3);
        orderBook.addOrder(order4);

        Order sellOrderMarket = new Order("testOrder4", Side.BUY, Spot.BTC, 60, BigDecimal.valueOf(50000), OrderType.MARKET_ORDER);
        Optional<Order> matchingOrderMarket = orderBook.matchBuy(sellOrderMarket);
        assertTrue(matchingOrderMarket.isPresent());
        assertEquals(Side.SELL,matchingOrderMarket.get().getSide());
        assertEquals(BigDecimal.valueOf(55000),matchingOrderMarket.get().getPrice());

        //as the buy order price is 50000, and the lowest BTC sell is 55000, there wont be any matching orders as it is a LIMIT_ORDER
        Order buyOrderLimit = new Order("testOrder4", Side.BUY, Spot.BTC, 60, BigDecimal.valueOf(50000), OrderType.LIMIT_ORDER);
        Optional<Order> matchingOrderLimit = orderBook.matchBuy(buyOrderLimit);
        assertFalse(matchingOrderLimit.isPresent());


        //as the buy order price is 50000, and the lowest BTC sell is 55000, there wont be any matching orders as it is a LIMIT_ORDER
        Order buyOrderLimit2 = new Order("testOrder4", Side.BUY, Spot.BTC, 60, BigDecimal.valueOf(70000), OrderType.LIMIT_ORDER);
        Optional<Order> matchingOrderLimit2 = orderBook.matchBuy(buyOrderLimit2);
        assertTrue(matchingOrderLimit2.isPresent());
        assertEquals(Side.SELL,matchingOrderLimit2.get().getSide());
        assertEquals(BigDecimal.valueOf(55000),matchingOrderLimit2.get().getPrice());
    }

    @Test
    void successfullyAddOrders() {
        OrderBook orderBook = new OrderBook();
        Order order1 = new Order("testOrder1", Side.SELL, Spot.BTC, 1100, BigDecimal.valueOf(58269.5), OrderType.MARKET_ORDER);
        Order order2 = new Order("testOrder2", Side.SELL, Spot.BTC, 1100, BigDecimal.valueOf(58269.5), OrderType.MARKET_ORDER);
        Order order3 = new Order("testOrder3", Side.SELL, Spot.BTC, 1100, BigDecimal.valueOf(58269.5), OrderType.MARKET_ORDER);
        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(order3);

        List<Order> expectedSellOrders = new ArrayList<>();
        Collections.addAll(expectedSellOrders, order1, order2, order3);

        assertEquals(expectedSellOrders, orderBook.getSellOrders());

        Order order4 = new Order("testOrder4", Side.BUY, Spot.BTC, 1100, BigDecimal.valueOf(58269.5), OrderType.MARKET_ORDER);
        Order order5 = new Order("testOrder5", Side.BUY, Spot.BTC, 1100, BigDecimal.valueOf(58269.5), OrderType.MARKET_ORDER);
        Order order6 = new Order("testOrder6", Side.BUY, Spot.BTC, 1100, BigDecimal.valueOf(58269.5), OrderType.MARKET_ORDER);
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
        Order order1 = new Order("testOrder1", Side.SELL, Spot.BTC, 500, BigDecimal.valueOf(58269.5), OrderType.MARKET_ORDER);
        Order order2 = new Order("testOrder2", Side.BUY, Spot.BTC, 652, BigDecimal.valueOf(58270), OrderType.MARKET_ORDER);
        Order order3 = new Order("testOrder3", Side.SELL, Spot.BTC, 5451, BigDecimal.valueOf(58280), OrderType.MARKET_ORDER);
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
        Order order1 = new Order("testOrder1", Side.SELL, Spot.BTC, 500, BigDecimal.valueOf(58269.5), OrderType.MARKET_ORDER);
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