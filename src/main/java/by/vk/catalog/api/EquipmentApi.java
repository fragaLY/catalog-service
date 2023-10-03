package by.vk.catalog.api;

import by.vk.catalog.api.repository.Equipment;
import by.vk.catalog.api.service.EquipmentService;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/equipments")
public record EquipmentApi(EquipmentService service) {

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Flux<Equipment> get(@RequestParam(required = false) Map<String, String> parameters,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "20") int size) {
    return service.search(parameters, PageRequest.of(page, size));
  }

}
