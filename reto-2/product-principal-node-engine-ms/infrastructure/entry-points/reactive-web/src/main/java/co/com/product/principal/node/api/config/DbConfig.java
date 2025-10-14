package co.com.product.principal.node.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@RefreshScope
@Component
public class DbConfig {

    @Value("${infrastructure.adapters.db.status}")
    private String dbStatus;

    public String getDbStatus() {
        return dbStatus;
    }
}
