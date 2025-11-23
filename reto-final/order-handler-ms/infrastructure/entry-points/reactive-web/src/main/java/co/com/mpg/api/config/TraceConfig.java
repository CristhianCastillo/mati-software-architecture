package co.com.mpg.api.config;

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
    public Timer processOrderTimer() {
        return Timer.builder("order.processing.time")
                .description("Time taken to process orders")
                .publishPercentileHistogram(true)
                .minimumExpectedValue(java.time.Duration.ofMillis(1))
                .maximumExpectedValue(java.time.Duration.ofSeconds(10))
                .register(meterRegistry);
    }
}