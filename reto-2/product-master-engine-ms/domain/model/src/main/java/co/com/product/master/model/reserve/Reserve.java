package co.com.product.master.model.reserve;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Reserve {
    Long id;
    String name;
    Long count;
    Long clientId;
    String messageId;
}
