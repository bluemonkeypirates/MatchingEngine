import java.math.BigDecimal;

public class Order {
    private final String orderId;
    private final Side side;
    private final Spot spot;
    private final long volume;
    private final BigDecimal price;
    private final OrderType orderType;

    public Order(String orderId, Side side, Spot spot, long volume, BigDecimal price, OrderType orderType) {
        this.orderId = orderId;
        this.side = side;
        this.spot = spot;
        this.volume = volume;
        this.price = price;
        this.orderType = orderType;
    }


    public Side getSide() {
        return side;
    }

    public String getOrderId() {
        return orderId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getVolume() {
        return volume;
    }

    public Spot getSpot() {
        return spot;
    }

    public OrderType getOrderType() {
        return orderType;
    }
}
