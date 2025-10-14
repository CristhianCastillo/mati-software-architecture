package co.com.product.master.consumer.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class TraceConfig {

    private final MeterRegistry meterRegistry;

    @Bean("principalNodeReserveProductsCount")
    public Counter principalNodeReserveProductsCount() {
        return Counter.builder("principal.node.reserveProduct.count")
                .description("Total number of requests to reserve products from principal node")
                .register(meterRegistry);
    }

    @Bean("secondaryNodeReserveProductsCount")
    public Counter secondaryNodeReserveProductsCount() {
        return Counter.builder("secondary.node.reserveProduct.count")
                .description("Total number of requests to reserve products from secondary node")
                .register(meterRegistry);
    }
}
