package co.com.matchingengine.model.quota;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Quota {
    private double bestPrice;
    private int totalVolumeActions;
    private int totalOrders;
}
