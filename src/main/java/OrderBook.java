import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
