package co.com.product.master.api.security;

import co.com.product.master.api.config.CorsConfigParameters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter(CorsConfigParameters corsConfigParameters) {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        corsConfigParameters.getAllowed().forEach(element -> {
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.setAllowedOrigins(element.getOrigins());
            corsConfiguration.setAllowedMethods(element.getMethods());
            corsConfiguration.setAllowedHeaders(element.getHeaders());
            source.registerCorsConfiguration(element.getPath(), corsConfiguration);
        });
        return new CorsWebFilter(source);
    }
}
