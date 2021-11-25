import java.math.BigDecimal;

public record Trade(String orderID,
                    Side side,
                    Instrument spot,
                    long volume,
                    BigDecimal price) {

}

