package orders;

import java.util.List;

public record OrderTrades(Order order, List<Trade> trades) {}
