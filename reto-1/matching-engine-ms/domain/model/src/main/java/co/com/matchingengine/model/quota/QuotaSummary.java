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
public class QuotaSummary {
    private Quota bestBuyQuota;
    private Quota bestSellQuota;
}
