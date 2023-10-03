package by.vk.catalog.api.service;

import by.vk.catalog.api.repository.Equipment;
import java.math.BigDecimal;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
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

    var criteria = new Criteria();

    if (!ObjectUtils.isEmpty(parameters.get("BrandName"))) {
      criteria.and(new Criteria("BrandName").contains(parameters.get("BrandName")));
    }

    if (!ObjectUtils.isEmpty(parameters.get("Details"))) {
      criteria.and(new Criteria("Details").contains(parameters.get("Details")));
    }

    if (!ObjectUtils.isEmpty(parameters.get("Sizes"))) {
      criteria.and(new Criteria("Sizes").contains(parameters.get("Sizes")));
    }

    if (!ObjectUtils.isEmpty(parameters.get("Category"))) {
      criteria.and(new Criteria("Category").contains(parameters.get("Category")));
    }

    if (!ObjectUtils.isEmpty(parameters.get("SellPrice"))) {
      var sellPrice = parameters.get("SellPrice");
      criteria.and(new Criteria("SellPrice").lessThanEqual(
          BigDecimal.valueOf(Double.parseDouble(sellPrice))));
    }

    if (!ObjectUtils.isEmpty(parameters.get("Discount"))) {
      criteria.and(
          new Criteria("Discount").greaterThanEqual(
              BigDecimal.valueOf(Double.parseDouble(parameters.get("Discount")))));
    }

    var query = new CriteriaQuery(criteria);
    query.setPageable(pageable);

    return template.search(query, Equipment.class).map(SearchHit::getContent);
  }

}
