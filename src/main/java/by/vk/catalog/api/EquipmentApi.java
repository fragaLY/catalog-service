package by.vk.catalog.api;

import by.vk.catalog.api.repository.Equipment;
import by.vk.catalog.api.service.EquipmentService;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/equipments")
public record EquipmentApi(EquipmentService service) {

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public Flux<Equipment> get(@RequestParam Map<String, String> parameters,
      @PageableDefault(size = 20) Pageable pageable) {
    return service.search(parameters, pageable);
  }

}
