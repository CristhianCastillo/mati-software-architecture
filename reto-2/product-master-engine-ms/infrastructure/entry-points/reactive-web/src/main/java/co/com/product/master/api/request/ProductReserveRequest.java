package co.com.product.master.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductReserveRequest {

    @NotNull(message = "id can not be empty")
    private Long id;

    @NotEmpty(message = "name can not be empty")
    private String name;

    @NotNull(message = "count can not be empty")
    private Integer count;

    @NotNull(message = "clientId can not be empty")
    private Long clientId;
}
