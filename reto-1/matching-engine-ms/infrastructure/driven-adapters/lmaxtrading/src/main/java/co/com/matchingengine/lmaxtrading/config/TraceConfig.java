package co.com.matchingengine.lmaxtrading.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class TraceConfig {

    private final MeterRegistry meterRegistry;

    @Bean
    public Timer matchOrderTimer() {
        return Timer.builder("order.matching.time")
                .description("Time taken to match orders")
                .minimumExpectedValue(java.time.Duration.ofMillis(1))
                .maximumExpectedValue(java.time.Duration.ofSeconds(10))
                .register(meterRegistry);
    }

    @Bean("matchOrderErrorCount")
    public Counter matchOrderErrorCount() {
        return Counter.builder("order.matching.count.error")
                .description("Total number of match orders with error")
                .register(meterRegistry);
    }
}
