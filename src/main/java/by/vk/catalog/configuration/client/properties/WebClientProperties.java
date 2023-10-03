package by.vk.catalog.configuration.client.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("service.webclient")
public record WebClientProperties(int timeout, int maxAttempts, long duration) {
}
