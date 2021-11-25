import java.math.BigDecimal;

public class Order {
    private final String orderId;
    private final Side side;
    private final Instrument instrument;
    private final long initialVolume;
    private final BigDecimal price;
    private final OrderType orderType;
    private long quantityRemaining;

    public Order(String orderId, Side side, Instrument instrument, long volume, BigDecimal price, OrderType orderType) {
        this.orderId = orderId;
        this.side = side;
        this.instrument = instrument;
        this.initialVolume = volume;
        this.price = price;
        this.orderType = orderType;
        this.quantityRemaining = volume;
    }

    public void updateQuantityRemaining(long tradeVolume){
        quantityRemaining -= tradeVolume;
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

    public long getInitialVolume() {
        return initialVolume;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public long getQuantityRemaining() {
        return quantityRemaining;
    }
}
