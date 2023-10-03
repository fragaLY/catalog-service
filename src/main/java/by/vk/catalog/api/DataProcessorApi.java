package by.vk.catalog.api;

import by.vk.catalog.api.data.DataProcessingPayload;
import by.vk.catalog.api.repository.Equipment;
import by.vk.catalog.api.service.DataSinkProcessor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/data/process")
public record DataProcessorApi(DataSinkProcessor processor) {

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public Flux<Equipment> process(
      @Valid @RequestBody(required = false) DataProcessingPayload payload) {
    return processor.process(payload);
  }

}
