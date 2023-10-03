package by.vk.catalog.configuration.client;

import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

@Component
public class ClientErrorHandler implements Function<ClientResponse, Mono<? extends Throwable>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClientErrorHandler.class);

  @Override
  public Mono<? extends Throwable> apply(ClientResponse response) {
    LOGGER.error("[SERVICE] Unexpected exception with status code [{}]. Please, try again later.",
        response.statusCode());
    return Mono.error(
        new RuntimeException(
            "Unexpected exception with status code [%s]. Please, try again later."
                .formatted(response.statusCode())
        ));
  }
}