package by.vk.catalog.configuration.client.properties;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("service.webclient")
public record WebClientProperties(int timeout, String baseUrl, List<String> datasets, int maxAttempts, long duration) {
}
