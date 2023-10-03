package by.vk.catalog.configuration.client;

import by.vk.catalog.configuration.client.properties.WebClientProperties;
import by.vk.catalog.configuration.exception.NotFoundException;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.RetryBackoffSpec;
import reactor.util.retry.RetrySpec;

@Configuration
public class WebClientConfiguration {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebClientConfiguration.class);

  @Bean
  @ConditionalOnProperty(name = "service.client.enabled", matchIfMissing = true)
  public WebClient webClient(WebClientProperties properties) {

    final var httpClient = HttpClient.create()
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.timeout())
        .doOnConnected(connection -> {
          connection.addHandlerLast(
              new ReadTimeoutHandler(properties.timeout(), TimeUnit.MILLISECONDS));
          connection.addHandlerLast(
              new WriteTimeoutHandler(properties.timeout(), TimeUnit.MILLISECONDS));
        });

    return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
  }

  @Bean
  public RetryBackoffSpec retrySpec(WebClientProperties properties) {
    return RetrySpec.backoff(properties.maxAttempts(), Duration.ofMillis(properties.duration()))
        .doBeforeRetry(
            signal ->
                LOGGER.warn(
                    String.format(
                        "Retrying to retrieve dataset. Retry signal [%d]. Cause [%s]",
                        signal.totalRetries(), signal.failure().getMessage())))
        .filter(throwable -> throwable instanceof Exception)
        .onRetryExhaustedThrow(
            (retryBackoffSpec, retrySignal) -> {
              //todo: get info from caches
              throw new NotFoundException("No datasets found.");
            });
  }

}