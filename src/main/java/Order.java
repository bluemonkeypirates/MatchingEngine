import java.math.BigDecimal;

public class Order {
    private final String orderId;
    private final Side side;
    private final Spot spot;
    private final long volume;
    private final BigDecimal price;

    public Order(String orderId, Side side, Spot spot, long volume, BigDecimal price) {
        this.orderId = orderId;
        this.side = side;
        this.spot = spot;
        this.volume = volume;
        this.price = price;
    }


    public Side getSide() {
        return side;
    }

    public String getOrderId() {
        return orderId;
    }
}
