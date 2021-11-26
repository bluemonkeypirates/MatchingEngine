package user;

import orders.Order;
import orders.Side;
import orders.Trade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UserAccount {
    private String userID;
    private List<Order> userOrders;
    private List<Trade> userTrades;
    private long userBalance;

    public UserAccount(String userID) {
        this.userID = userID;
        this.userTrades = new ArrayList<>();
    }

    public List<Order> getUserOrders() {
        return userOrders;
    }

    public long getUserBalance() {
        return userBalance;
    }

    public BigDecimal getBalance() {
        BigDecimal sumBuys = BigDecimal.valueOf(0);
        BigDecimal sumSells = BigDecimal.valueOf(0);
        userTrades.stream().filter(x -> x.side().equals(Side.BUY)).forEach(trade -> sumBuys.add(trade.price()));
        userTrades.stream().filter(x -> x.side().equals(Side.SELL)).forEach(trade -> sumSells.add(trade.price()));
        return sumSells.subtract(sumSells);
    }

    public void addUserTrade(Trade trade) {
        this.userTrades.add(trade);
    }
}
