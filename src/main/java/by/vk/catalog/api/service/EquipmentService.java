package by.vk.catalog.api.service;

import by.vk.catalog.api.repository.Equipment;
import co.elastic.clients.json.JsonData;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Flux;

@Service
public record EquipmentService(ReactiveElasticsearchTemplate template) {

  private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentService.class);

  public Flux<Equipment> search(Map<String, String> parameters, Pageable pageable) {
    LOGGER.info(
        "[CATALOG-SERVICE] Searching an equipment with parameters[{}] and pagination [{}] process started.",
        parameters, pageable);

    var queryBuilder = NativeQuery.builder()
        .withPageable(pageable);

    if (!ObjectUtils.isEmpty(parameters.get("BrandName"))) {
      queryBuilder.withQuery(q -> q
          .match(m -> m
              .field("BrandName")
              .fuzziness("AUTO")
              .query(parameters.get("BrandName")))
      );
    }

    if (!ObjectUtils.isEmpty(parameters.get("Details"))) {
      queryBuilder.withQuery(q -> q
          .match(m -> m
              .field("Details")
              .fuzziness("AUTO")
              .query(parameters.get("Details"))
          )
      );
    }

    if (!ObjectUtils.isEmpty(parameters.get("Sizes"))) {
      queryBuilder.withQuery(q -> q
          .match(m -> m
              .field("Sizes")
              .fuzziness("AUTO")
              .query(parameters.get("Sizes"))
          )
      );
    }

    if (!ObjectUtils.isEmpty(parameters.get("Category"))) {
      queryBuilder.withQuery(q -> q
          .match(m -> m
              .field("Category")
              .fuzziness("AUTO")
              .query(parameters.get("Category"))
          )
      );
    }

    if (!ObjectUtils.isEmpty(parameters.get("SellPrice"))) {
      queryBuilder.withQuery(q -> q
          .range(m -> m
              .field("SellPrice")
              .lte(JsonData.of(parameters.get("SellPrice")))
          )
      ).withSort(Sort.by(Direction.DESC, "SellPrice"));
    }

    if (!ObjectUtils.isEmpty(parameters.get("Discount"))) {
      queryBuilder.withQuery(q -> q
          .range(r -> r
              .field("Discount")
              .gte(JsonData.of(parameters.get("Discount")))
          ));
    }

    return template.search(queryBuilder.build(), Equipment.class).map(SearchHit::getContent);
  }

}
