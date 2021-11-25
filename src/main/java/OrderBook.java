import java.util.*;
import java.util.stream.Collectors;

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


    /**
     * Matches the SELL order with the best BUY order (max price)
     */
    public Optional<Order> matchSell(Order sellOrder) {
        List<Order> ordersForSpot = buyOrders.stream().filter(b -> b.getSpot().equals(sellOrder.getSpot())).collect(Collectors.toList());
        if(sellOrder.getOrderType().equals(OrderType.MARKET_ORDER)) {
            return ordersForSpot.stream().max(Comparator.comparing(Order::getPrice));
        } else{
            //filter out orders where the buy price is lower than the sell order limit
            return ordersForSpot.stream().filter(x -> x.getPrice().compareTo(sellOrder.getPrice())>0).max(Comparator.comparing(Order::getPrice));
        }
    }

    /**
     * Matches the BUY order with the best SELL order (min price)
     */
    public Optional<Order> matchBuy(Order buyOrder) {
        List<Order> ordersForSpot = sellOrders.stream().filter(b -> b.getSpot().equals(buyOrder.getSpot())).collect(Collectors.toList());

        if(buyOrder.getOrderType().equals(OrderType.MARKET_ORDER)) {
            return ordersForSpot.stream().min(Comparator.comparing(Order::getPrice));
        } else{
            //filter out orders where the sell price is lower than the buy order limit
            return ordersForSpot.stream().filter(x -> x.getPrice().compareTo(buyOrder.getPrice())<0).min(Comparator.comparing(Order::getPrice));
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

    public void cancelOrder(String orderId) {
        if (ordersById.containsKey(orderId)) {
            Order order = ordersById.get(orderId);
            if (order.getSide().equals(Side.BUY)) {
                buyOrders.remove(order);
            } else {
                sellOrders.remove(order);
            }
        } else {
            throw new IllegalArgumentException("OrderID " + orderId + " is not found within OrderBook");
        }
    }

    public List<Order> getBuyOrders() {
        return buyOrders;
    }

    public List<Order> getSellOrders() {
        return sellOrders;
    }
}
