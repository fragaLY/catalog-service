package by.vk.catalog.api.data;

import by.vk.catalog.api.repository.Equipment;
import by.vk.catalog.api.repository.EquipmentRawData;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class EquipmentTransformer implements Function<EquipmentRawData, Equipment> {

  @Override
  public Equipment apply(EquipmentRawData data) {
    var sizes = Arrays.stream(data.sizes().replaceAll("Size:", "").trim().split(",")).toList();
    var price = BigDecimal.valueOf(data.sellPrice());
    var decimalNumPattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    var matcher = decimalNumPattern.matcher(data.discount());
    var discountRaws = new ArrayList<String>();
    while (matcher.find()) {
      discountRaws.add(matcher.group());
    }
    var discountMin = discountRaws.stream()
        .mapToDouble(Double::valueOf)
        .min();
    var discount =
        discountMin.isPresent() ? BigDecimal.valueOf(discountMin.getAsDouble()) : BigDecimal.ZERO;
    return new Equipment(data.brandName(), data.details(), sizes, data.mrp(), price, discount,
        data.category());
  }

}
