package by.vk.catalog.api;

import by.vk.catalog.api.repository.Equipment;
import by.vk.catalog.api.service.DataSinkProcessor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(value = "/api/v1/data/process", produces = MediaType.APPLICATION_JSON_VALUE)
public record DataProcessorApi(DataSinkProcessor processor) {

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public Flux<Equipment> process(
      @Valid @RequestBody(required = false) String uri) {
    return processor.process(uri);
  }

}
