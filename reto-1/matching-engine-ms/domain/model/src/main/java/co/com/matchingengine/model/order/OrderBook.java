package co.com.matchingengine.model.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
//@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class OrderBook {
    private List<OrderItem> buyOrders;
    private List<OrderItem> sellOrders;
}
