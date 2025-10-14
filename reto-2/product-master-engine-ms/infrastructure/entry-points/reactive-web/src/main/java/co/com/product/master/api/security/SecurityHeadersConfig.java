package co.com.product.master.api.security;

import co.com.product.master.api.constants.TracingConstants;
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
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        headers.set("Referrer-Policy", "strict-origin-when-cross-origin");

        ContextRegistry.getInstance()
                .registerThreadLocalAccessor(TracingConstants.PRODUCT_ID, () -> MDC.get(TracingConstants.PRODUCT_ID),
                        clientId -> MDC.put(TracingConstants.PRODUCT_ID, clientId),
                        () -> MDC.remove(TracingConstants.PRODUCT_ID));
        return chain.filter(exchange);
    }
}
