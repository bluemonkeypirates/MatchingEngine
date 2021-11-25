import engine.MatchingEngine;
import orders.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchingEngineTest {

    @Test
    public void successfullyFulfilLimitBuyOrder() {
        MatchingEngine matchingEngine = new MatchingEngine();
        List<Trade> firstOrder = matchingEngine.addOrder(Instrument.BTCUSD, Side.SELL, 1000, BigDecimal.valueOf(60000), OrderType.LIMIT_ORDER);
        assertTrue(firstOrder.isEmpty());
        List<Trade> secondOrder = matchingEngine.addOrder(Instrument.BTCUSD, Side.BUY, 500, BigDecimal.valueOf(65000), OrderType.LIMIT_ORDER);

        assertEquals(2, secondOrder.size());
        assertEquals(Side.BUY, secondOrder.get(0).side());
        assertEquals(500, secondOrder.get(0).volume());
        assertEquals(BigDecimal.valueOf(60000), secondOrder.get(0).price());
        assertEquals(Side.SELL, secondOrder.get(1).side());
        assertEquals(500, secondOrder.get(1).volume());
        assertEquals(BigDecimal.valueOf(60000), secondOrder.get(1).price());

        OrderBook orderBook = matchingEngine.getOrderBook(Instrument.BTCUSD);
        assertEquals(1, orderBook.getOrders().size());
        Order order = orderBook.getOrders().get(secondOrder.get(1).orderID());
        assertEquals(500, order.getQuantityRemaining());
    }

    @Test
    public void successfullyFulfilLimitSellOrder() {
        MatchingEngine matchingEngine = new MatchingEngine();
        List<Trade> firstOrder = matchingEngine.addOrder(Instrument.BTCUSD, Side.BUY, 1000, BigDecimal.valueOf(55000), OrderType.LIMIT_ORDER);
        assertTrue(firstOrder.isEmpty());
        List<Trade> secondOrder = matchingEngine.addOrder(Instrument.BTCUSD, Side.SELL, 500, BigDecimal.valueOf(50000), OrderType.LIMIT_ORDER);

        assertEquals(2, secondOrder.size());
        assertEquals(Side.SELL, secondOrder.get(0).side());
        assertEquals(500, secondOrder.get(0).volume());
        assertEquals(BigDecimal.valueOf(55000), secondOrder.get(0).price());
        assertEquals(Side.BUY, secondOrder.get(1).side());
        assertEquals(500, secondOrder.get(1).volume());
        assertEquals(BigDecimal.valueOf(55000), secondOrder.get(1).price());

        OrderBook orderBook = matchingEngine.getOrderBook(Instrument.BTCUSD);
        assertEquals(1, orderBook.getOrders().size());
        Order order = orderBook.getOrders().get(secondOrder.get(1).orderID());
        assertEquals(500, order.getQuantityRemaining());
    }

    @Test
    public void limitOrderNotFulfilledInitially(){
        MatchingEngine matchingEngine = new MatchingEngine();
        List<Trade> order1 = matchingEngine.addOrder(Instrument.BTCUSD, Side.SELL, 1000, BigDecimal.valueOf(60000), OrderType.MARKET_ORDER);
        List<Trade> order2 = matchingEngine.addOrder(Instrument.BTCUSD, Side.SELL, 1000, BigDecimal.valueOf(65000), OrderType.MARKET_ORDER);
        List<Trade> order3 = matchingEngine.addOrder(Instrument.BTCUSD, Side.SELL, 1000, BigDecimal.valueOf(59000), OrderType.MARKET_ORDER);

        assertTrue(order3.isEmpty());

        List<Trade> order4 = matchingEngine.addOrder(Instrument.BTCUSD, Side.BUY, 1000, BigDecimal.valueOf(55000), OrderType.LIMIT_ORDER);
        //no orders as the limit is 55000, but only buys of
        assertTrue(order4.isEmpty());
        //new market order will be fulfilled
        List<Trade> order5 = matchingEngine.addOrder(Instrument.BTCUSD, Side.BUY, 1000, BigDecimal.valueOf(0), OrderType.MARKET_ORDER);
        assertEquals(2, order5.size());

        //new limit order with higher price will trade
        List<Trade> order6 = matchingEngine.addOrder(Instrument.BTCUSD, Side.BUY, 1000, BigDecimal.valueOf(61000), OrderType.LIMIT_ORDER);
        assertEquals(2, order5.size());
        assertEquals(Side.BUY, order6.get(0).side());
        assertEquals(1000, order6.get(0).volume());
        assertEquals(BigDecimal.valueOf(60000), order6.get(0).price());
        assertEquals(Side.SELL, order6.get(1).side());
        assertEquals(1000, order6.get(1).volume());
        assertEquals(BigDecimal.valueOf(60000), order6.get(1).price());

        //new SELL with lower price should satisfy limit order buy (order4)
        List<Trade> order7 = matchingEngine.addOrder(Instrument.BTCUSD, Side.SELL, 1000, BigDecimal.valueOf(54000), OrderType.MARKET_ORDER);
        assertEquals(2, order7.size());
    }


}