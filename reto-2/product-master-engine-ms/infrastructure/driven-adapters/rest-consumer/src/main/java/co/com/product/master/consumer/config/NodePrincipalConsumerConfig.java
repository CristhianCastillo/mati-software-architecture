package co.com.product.master.consumer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "infrastructure.adapters.rest-consumer.principal-node-service")
public class NodePrincipalConsumerConfig {
    private String url;
    private String defaultPath;
    private Long timeoutConnect;
    private Long timeoutRead;
}
