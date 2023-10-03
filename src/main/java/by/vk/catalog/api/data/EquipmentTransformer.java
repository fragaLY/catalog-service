package by.vk.catalog.api.data;

import by.vk.catalog.api.repository.Equipment;
import by.vk.catalog.api.repository.EquipmentRawData;
import by.vk.catalog.configuration.client.WebClientConfiguration;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;


@Component
public class EquipmentTransformer implements Function<Flux<EquipmentRawData>, Flux<Equipment>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentTransformer.class);

  @Override
  public Flux<Equipment> apply(Flux<EquipmentRawData> data) {
    return data.map(it -> {
      List<String> sizes = new ArrayList<>();

      if (it.Sizes() != null) {
        sizes = Arrays.stream(it.Sizes().replaceAll("Size:", "").trim().split(","))
            .toList();
      }

      BigDecimal price = null;
      if (it.Sizes() != null) {
        try {
          price = BigDecimal.valueOf(Double.parseDouble(it.SellPrice()));
        } catch (Exception exception) {
          LOGGER.warn("The price of equipment is not compatible with numeric value");
        }
      }

      BigDecimal discount = null;

      if (it.Discount() != null) {
        var decimalNumPattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        var matcher = decimalNumPattern.matcher(it.Discount());
        var discountRaws = new ArrayList<String>();
        while (matcher.find()) {
          discountRaws.add(matcher.group());
        }
        var discountMin = discountRaws.stream()
            .mapToDouble(Double::valueOf)
            .min();
        discount =
            discountMin.isPresent() ? BigDecimal.valueOf(discountMin.getAsDouble())
                : BigDecimal.ZERO;
      }
      return new Equipment(it.BrandName(), it.Details(), sizes, it.MRP(), price, discount,
          it.Category());
    });
  }

}
