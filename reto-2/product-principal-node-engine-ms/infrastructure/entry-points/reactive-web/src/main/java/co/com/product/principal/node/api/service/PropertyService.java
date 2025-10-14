package co.com.product.principal.node.api.service;

import lombok.AllArgsConstructor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class PropertyService {

    private static final String DYNAMIC_PROPERTIES_SOURCE_NAME = "dynamicProperties";

    private final ConfigurableEnvironment configurableEnvironment;

    public void updateProperty(String key, String value) {
        MutablePropertySources propertySources = this.configurableEnvironment.getPropertySources();
        if (!propertySources.contains(DYNAMIC_PROPERTIES_SOURCE_NAME)) {
            Map<String, Object> dynamicProperties = new HashMap<>();
            dynamicProperties.put(key, value);
            propertySources.addFirst(new MapPropertySource(DYNAMIC_PROPERTIES_SOURCE_NAME, dynamicProperties));
        } else {
            MapPropertySource propertySource = (MapPropertySource) propertySources.get(DYNAMIC_PROPERTIES_SOURCE_NAME);
            propertySource.getSource().put(key, value);
        }
    }
}
