import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class MatchingEngine {

    private final Map<Instrument, OrderBook> orderBooks = new ConcurrentHashMap<>();
    private final Logger logger = Logger.getLogger(MatchingEngine.class.getName());

    MatchingEngine() {
    }


    public OrderBook getOrderBook(Instrument instrument) {
        return orderBooks.get(instrument);
    }

    public List<Trade> addOrder(Instrument instrument, Side side, long volume, BigDecimal price, OrderType orderType) {
        Order order = new Order(UUID.randomUUID().toString(), side, instrument, volume, price, orderType);
        OrderBook orderBook = orderBooks.computeIfAbsent(instrument, i -> new OrderBook());
        return handleOrder(orderBook, order);
    }

    public List<Trade> handleOrder(OrderBook orderBook, Order order) {
        List<Trade> trades = new ArrayList<>();
        fulfilOrder(orderBook, order, trades);
        return trades;
    }

    private void fulfilOrder(OrderBook orderBook, Order order, List<Trade> trades) {
        Optional<Order> bestOrder;
        bestOrder = orderBook.getBestOrder(order);
        while (bestOrder.isPresent() && order.getQuantityRemaining() > 0) {
            Order matchingOrder = bestOrder.get();
            long tradeVolume = Math.min(order.getInitialVolume(), matchingOrder.getInitialVolume());
            order.updateQuantityRemaining(tradeVolume);
            trades.add(new Trade(order.getOrderId(), order.getSide(), order.getInstrument(), tradeVolume, matchingOrder.getPrice()));

            matchingOrder.updateQuantityRemaining(tradeVolume);
            trades.add(new Trade(matchingOrder.getOrderId(), matchingOrder.getSide(), order.getInstrument(), tradeVolume, matchingOrder.getPrice()));
            //if the matching order is fulfilled, remove it
            if (matchingOrder.getQuantityRemaining() == 0) {
                orderBook.removeOrder(matchingOrder.getOrderId());
            }
            bestOrder = orderBook.getBestOrder(order);
        }

        if (order.getQuantityRemaining() > 0) {
            //no more matching orders, so add to book
            orderBook.addOrder(order);
        }
    }
}
