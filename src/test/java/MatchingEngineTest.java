import engine.MatchingEngine;
import orders.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class MatchingEngineTest {

    @Test
    public void successfullyFulfilLimitBuyOrder() {
        MatchingEngine matchingEngine = new MatchingEngine();
        OrderTrades firstOrder = matchingEngine.addOrder(Instrument.BTCUSD, Side.SELL, 1000, BigDecimal.valueOf(60000), OrderType.LIMIT_ORDER);
        assertTrue(firstOrder.trades().isEmpty());
        OrderTrades secondOrder = matchingEngine.addOrder(Instrument.BTCUSD, Side.BUY, 500, BigDecimal.valueOf(65000), OrderType.LIMIT_ORDER);

        assertEquals(2, secondOrder.trades().size());
        assertEquals(Side.BUY, secondOrder.trades().get(0).side());
        assertEquals(500, secondOrder.trades().get(0).volume());
        assertEquals(BigDecimal.valueOf(60000), secondOrder.trades().get(0).price());
        assertEquals(Side.SELL, secondOrder.trades().get(1).side());
        assertEquals(500, secondOrder.trades().get(1).volume());
        assertEquals(BigDecimal.valueOf(60000), secondOrder.trades().get(1).price());

        OrderBook orderBook = matchingEngine.getOrderBook(Instrument.BTCUSD);
        assertEquals(1, orderBook.getOrders().size());
        Order order = orderBook.getOrders().get(secondOrder.trades().get(1).orderID());
        assertEquals(500, order.getQuantityRemaining());
    }

    @Test
    public void successfullyFulfilLimitSellOrder() {
        MatchingEngine matchingEngine = new MatchingEngine();
        OrderTrades firstOrder = matchingEngine.addOrder(Instrument.BTCUSD, Side.BUY, 1000, BigDecimal.valueOf(55000), OrderType.LIMIT_ORDER);
        assertTrue(firstOrder.trades().isEmpty());
        OrderTrades secondOrder = matchingEngine.addOrder(Instrument.BTCUSD, Side.SELL, 500, BigDecimal.valueOf(50000), OrderType.LIMIT_ORDER);

        assertEquals(2, secondOrder.trades().size());
        assertEquals(Side.SELL, secondOrder.trades().get(0).side());
        assertEquals(500, secondOrder.trades().get(0).volume());
        assertEquals(BigDecimal.valueOf(55000), secondOrder.trades().get(0).price());
        assertEquals(Side.BUY, secondOrder.trades().get(1).side());
        assertEquals(500, secondOrder.trades().get(1).volume());
        assertEquals(BigDecimal.valueOf(55000), secondOrder.trades().get(1).price());

        OrderBook orderBook = matchingEngine.getOrderBook(Instrument.BTCUSD);
        assertEquals(1, orderBook.getOrders().size());
        Order order = orderBook.getOrders().get(secondOrder.trades().get(1).orderID());
        assertEquals(500, order.getQuantityRemaining());
    }

    @Test
    public void limitOrderNotFulfilledInitially(){
        MatchingEngine matchingEngine = new MatchingEngine();
        OrderTrades order1 = matchingEngine.addOrder(Instrument.BTCUSD, Side.SELL, 1000, BigDecimal.valueOf(60000), OrderType.MARKET_ORDER);
        OrderTrades order2 = matchingEngine.addOrder(Instrument.BTCUSD, Side.SELL, 1000, BigDecimal.valueOf(65000), OrderType.MARKET_ORDER);
        OrderTrades order3 = matchingEngine.addOrder(Instrument.BTCUSD, Side.SELL, 1000, BigDecimal.valueOf(59000), OrderType.MARKET_ORDER);

        assertTrue(order3.trades().isEmpty());

        OrderTrades order4 = matchingEngine.addOrder(Instrument.BTCUSD, Side.BUY, 1000, BigDecimal.valueOf(55000), OrderType.LIMIT_ORDER);
        //no orders as the limit is 55000, but only buys of
        assertTrue(order4.trades().isEmpty());
        //new market order will be fulfilled
        OrderTrades order5 = matchingEngine.addOrder(Instrument.BTCUSD, Side.BUY, 1000, BigDecimal.valueOf(0), OrderType.MARKET_ORDER);
        assertEquals(2, order5.trades().size());

        //new limit order with higher price will trade
        OrderTrades order6 = matchingEngine.addOrder(Instrument.BTCUSD, Side.BUY, 1000, BigDecimal.valueOf(61000), OrderType.LIMIT_ORDER);
        assertEquals(2, order5.trades().size());
        assertEquals(Side.BUY, order6.trades().get(0).side());
        assertEquals(1000, order6.trades().get(0).volume());
        assertEquals(BigDecimal.valueOf(60000), order6.trades().get(0).price());
        assertEquals(Side.SELL, order6.trades().get(1).side());
        assertEquals(1000, order6.trades().get(1).volume());
        assertEquals(BigDecimal.valueOf(60000), order6.trades().get(1).price());

        //new SELL with lower price should satisfy limit order buy (order4)
        OrderTrades order7 = matchingEngine.addOrder(Instrument.BTCUSD, Side.SELL, 1000, BigDecimal.valueOf(54000), OrderType.MARKET_ORDER);
        assertEquals(2, order7.trades().size());
    }

    @Test
    public void cancelOrderSuccessfully(){
        MatchingEngine matchingEngine = new MatchingEngine();
        OrderTrades orderTrades = matchingEngine.addOrder(Instrument.BTCUSD, Side.SELL, 1000, BigDecimal.valueOf(10000L), OrderType.MARKET_ORDER);
        Order cancelledOrder = matchingEngine.cancelOrder(orderTrades.order().getOrderId(), orderTrades.order().getInstrument());
        assertEquals(orderTrades.order(), cancelledOrder);
    }

    @Test
    public void cancelAllOrders(){
        MatchingEngine matchingEngine = new MatchingEngine();
        List<Order> expectedOrders = new ArrayList<>();
        IntStream.range(0, 10).forEach( i -> {
            expectedOrders.add(matchingEngine.addOrder(Instrument.BTCUSD, Side.SELL, 1000, BigDecimal.valueOf(i* 10000L), OrderType.MARKET_ORDER).order());
            expectedOrders.add(matchingEngine.addOrder(Instrument.ETHUSD, Side.SELL, 1000, BigDecimal.valueOf(i* 1000L), OrderType.MARKET_ORDER).order());
        });
        List<Order> cancelledOrders = matchingEngine.cancelAllOrders();
        assertTrue(cancelledOrders.containsAll(expectedOrders));
    }
}