package co.com.product.secondary.node.api.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "cors")
@Getter
public class CorsConfigParameters {
    private final List<Allowed> allowed = new ArrayList<>();

    @Data
    public static class Allowed {
        private String path;
        private List<String> headers;
        private List<String> origins;
        private List<String> methods;
    }
}
