package co.com.matchingengine.model.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
//@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Order {
    private final int id;
    private final OrderType orderType;
    private final LocalDateTime entryTime;
    private double price;
    private int quantity;

    public Order(int id, OrderType orderType, double price, int quantity) {
        this.id = id;
        this.orderType = orderType;
        this.entryTime = LocalDateTime.now();
        this.price = price;
        this.quantity = quantity;
    }

    public Order(Order order) {
        this(order.id, order.orderType, order.entryTime, order.price, order.quantity);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final Order order = (Order) obj;
        return id == order.id && Double.compare(price, order.price) == 0 && quantity == order.quantity && orderType == order.orderType && Objects.equals(entryTime, order.entryTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderType, entryTime, price, quantity);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderType=" + orderType +
                ", entryTime=" + entryTime +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
