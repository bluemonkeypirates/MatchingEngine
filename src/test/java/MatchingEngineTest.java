import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchingEngineTest {

    @Test
    public void successfullyFulfilMarketBuyOrder() {
        MatchingEngine matchingEngine = new MatchingEngine();
        List<Trade> firstOrder = matchingEngine.addOrder(Instrument.BTC, Side.SELL, 1000, BigDecimal.valueOf(60000), OrderType.MARKET_ORDER);
        assertTrue(firstOrder.isEmpty());
        List<Trade> secondOrder = matchingEngine.addOrder(Instrument.BTC, Side.BUY, 500, BigDecimal.valueOf(55000), OrderType.MARKET_ORDER);

        assertEquals(2, secondOrder.size());
        assertEquals(Side.BUY, secondOrder.get(0).side());
        assertEquals(500, secondOrder.get(0).volume());
        assertEquals(BigDecimal.valueOf(60000), secondOrder.get(0).price());
        assertEquals(Side.SELL, secondOrder.get(1).side());
        assertEquals(500, secondOrder.get(1).volume());
        assertEquals(BigDecimal.valueOf(60000), secondOrder.get(1).price());

        OrderBook orderBook = matchingEngine.getOrderBook(Instrument.BTC);
        assertEquals(1, orderBook.getOrders().size());
        Order order = orderBook.getOrders().get(secondOrder.get(1).orderID());
        assertEquals(500, order.getQuantityRemaining());
    }


    @Test
    public void successfullyFulfilMarketSellOrder() {
        MatchingEngine matchingEngine = new MatchingEngine();
        List<Trade> firstOrder = matchingEngine.addOrder(Instrument.BTC, Side.BUY, 1000, BigDecimal.valueOf(55000), OrderType.MARKET_ORDER);
        assertTrue(firstOrder.isEmpty());
        List<Trade> secondOrder = matchingEngine.addOrder(Instrument.BTC, Side.SELL, 500, BigDecimal.valueOf(60000), OrderType.MARKET_ORDER);

        assertEquals(2, secondOrder.size());
        assertEquals(Side.SELL, secondOrder.get(0).side());
        assertEquals(500, secondOrder.get(0).volume());
        assertEquals(BigDecimal.valueOf(55000), secondOrder.get(0).price());
        assertEquals(Side.BUY, secondOrder.get(1).side());
        assertEquals(500, secondOrder.get(1).volume());
        assertEquals(BigDecimal.valueOf(55000), secondOrder.get(1).price());

        OrderBook orderBook = matchingEngine.getOrderBook(Instrument.BTC);
        assertEquals(1, orderBook.getOrders().size());
        Order order = orderBook.getOrders().get(secondOrder.get(1).orderID());
        assertEquals(500, order.getQuantityRemaining());
    }


}