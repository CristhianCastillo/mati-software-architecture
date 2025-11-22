package co.com.mpg.api.config;

import co.com.mpg.api.constants.TracingConstants;
import io.micrometer.context.ContextRegistry;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class SecurityHeadersConfig implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        HttpHeaders headers = exchange.getResponse().getHeaders();
        headers.set("Content-Security-Policy", "default-src 'self'; frame-ancestors 'self'; form-action 'self'");
        headers.set("Strict-Transport-Security", "max-age=31536000;");
        headers.set("X-Content-Type-Options", "nosniff");
        headers.set("Server", "");
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        headers.set("Referrer-Policy", "strict-origin-when-cross-origin");

        ContextRegistry.getInstance()
                .registerThreadLocalAccessor(TracingConstants.ORDER_ORIGIN_PAYMENT_ID, () -> MDC.get(TracingConstants.ORDER_ORIGIN_PAYMENT_ID),
                        orderId -> MDC.put(TracingConstants.ORDER_ORIGIN_PAYMENT_ID, orderId),
                        () -> MDC.remove(TracingConstants.ORDER_ORIGIN_PAYMENT_ID));
        return chain.filter(exchange);
    }
}
