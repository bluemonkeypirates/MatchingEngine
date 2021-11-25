package orders;

import orders.OrderType;

import java.util.*;

/**
 * Shows all orders, both with a buy and sell interest
 */
public class OrderBook {

    private final List<Order> buyOrders;
    private final List<Order> sellOrders;

    private final Map<String, Order> ordersById;


    public OrderBook() {
        this.buyOrders = new ArrayList<>();
        this.sellOrders = new ArrayList<>();
        this.ordersById = new HashMap<>();
    }


    public Map<String, Order> getOrders() {
        return ordersById;
    }

    public Optional<Order> getBestOrder(Order order) {
        switch (order.getSide()) {
            case BUY -> {
                return getBestSell(order);
            }
            case SELL -> {
                return getBestBuy(order);
            }
        }
        return Optional.empty();
    }

    /**
     * Gets list of the best the SELL orders (max price)
     */
    public Optional<Order> getBestBuy(Order sellOrder) {
        if (sellOrder.getOrderType().equals(OrderType.MARKET_ORDER)) {
            return buyOrders.stream().max(Comparator.comparing(Order::getPrice));
        } else {
            //filter out orders where the buy price is lower than the sell order limit
            return buyOrders.stream().filter(x -> x.getPrice().compareTo(sellOrder.getPrice()) > 0).max(Comparator.comparing(Order::getPrice));
        }
    }

    /**
     * Gets a list of the best BUY orders (min price)
     */
    public Optional<Order> getBestSell(Order buyOrder) {
        if (buyOrder.getOrderType().equals(OrderType.MARKET_ORDER)) {
            return sellOrders.stream().min(Comparator.comparing(Order::getPrice));
        } else {
            //filter out orders where the sell price is lower than the buy order limit
            return sellOrders.stream().filter(x -> x.getPrice().compareTo(buyOrder.getPrice()) < 0).max(Comparator.comparing(Order::getPrice));
        }
    }

    public void addOrder(Order order) {
        if (order.getSide().equals(Side.BUY)) {
            buyOrders.add(order);
        } else {
            sellOrders.add(order);
        }
        ordersById.put(order.getOrderId(), order);
    }

    public Order removeOrder(String orderId) {
        if (ordersById.containsKey(orderId)) {
            Order order = ordersById.get(orderId);
            if (order.getSide().equals(Side.BUY)) {
                buyOrders.remove(order);
            } else {
                sellOrders.remove(order);
            }
            return order;
        } else {
            throw new IllegalArgumentException("OrderID " + orderId + " is not found within orders.OrderBook");
        }
    }

    public List<Order> getBuyOrders() {
        return buyOrders;
    }

    public List<Order> getSellOrders() {
        return sellOrders;
    }
}
