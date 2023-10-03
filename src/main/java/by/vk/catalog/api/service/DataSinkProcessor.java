package by.vk.catalog.api.service;

import by.vk.catalog.api.data.DataProcessingPayload;
import by.vk.catalog.api.data.EquipmentTransformer;
import by.vk.catalog.api.repository.Equipment;
import by.vk.catalog.api.repository.EquipmentRawData;
import by.vk.catalog.api.repository.ReactiveEquipmentRepository;
import by.vk.catalog.configuration.client.ClientErrorHandler;
import by.vk.catalog.configuration.exception.UnexpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.util.retry.RetryBackoffSpec;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public record DataSinkProcessor(WebClient client, RetryBackoffSpec retrySpec,
                                ClientErrorHandler errorHandler, EquipmentTransformer transformer,
                                ReactiveEquipmentRepository repository) {

  private static final Logger LOGGER = LoggerFactory.getLogger(DataSinkProcessor.class);


  //todo vk: add caches
  public Flux<Equipment> process(DataProcessingPayload processor) {
    LOGGER.info("[CATALOG-SERVICE] ETL process for datasets [{}] started.", processor.uris());
    return processor.uris().parallelStream()
        .map(url -> client.get().uri(url))
        .map(spec -> spec.retrieve().onStatus(HttpStatusCode::isError, errorHandler))
        .map(body -> body.bodyToFlux(EquipmentRawData.class).retryWhen(retrySpec))
        .reduce(Flux::mergeWith)
        .map(it -> it.map(transformer))
        .map(repository::saveAll)
        .orElseThrow(() -> new UnexpectedException("Datasets were not processed"));
  }

}
