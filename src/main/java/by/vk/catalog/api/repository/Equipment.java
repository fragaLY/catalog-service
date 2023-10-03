package by.vk.catalog.api.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "equipments")
public class Equipment {

  public Equipment() {
  }

  public Equipment(String brandName, String details, List<String> sizes, String mrp,
      BigDecimal sellPrice, BigDecimal discount, String category) {
    this.brandName = brandName;
    this.details = details;
    this.sizes = sizes;
    this.mrp = mrp;
    this.sellPrice = sellPrice;
    this.discount = discount;
    this.category = category;
  }

  public Equipment(String id, String brandName, String details, List<String> sizes, String mrp,
      BigDecimal sellPrice, BigDecimal discount, String category) {
    this.id = id;
    this.brandName = brandName;
    this.details = details;
    this.sizes = sizes;
    this.mrp = mrp;
    this.sellPrice = sellPrice;
    this.discount = discount;
    this.category = category;
  }

  @Id
  private String id;

  @Field(type = FieldType.Text, name = "BrandName")
  private String brandName;

  @Field(type = FieldType.Text, name = "Details")
  private String details;

  @Field(type = FieldType.Text, name = "Sizes")
  private List<String> sizes;

  @Field(type = FieldType.Text, name = "MRP")
  private String mrp;

  @Field(type = FieldType.Double, name = "SellPrice")
  private BigDecimal sellPrice;

  @Field(type = FieldType.Double, name = "Discount")
  private BigDecimal discount;

  @Field(type = FieldType.Text, name = "Category")
  private String category;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getBrandName() {
    return brandName;
  }

  public void setBrandName(String brandName) {
    this.brandName = brandName;
  }

  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }

  public List<String> getSizes() {
    return sizes;
  }

  public void setSizes(List<String> sizes) {
    this.sizes = sizes;
  }

  public String getMrp() {
    return mrp;
  }

  public void setMrp(String mrp) {
    this.mrp = mrp;
  }

  public BigDecimal getSellPrice() {
    return sellPrice;
  }

  public void setSellPrice(BigDecimal sellPrice) {
    this.sellPrice = sellPrice;
  }

  public BigDecimal getDiscount() {
    return discount;
  }

  public void setDiscount(BigDecimal discount) {
    this.discount = discount;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Equipment equipment)) {
      return false;
    }

    if (!Objects.equals(brandName, equipment.brandName)) {
      return false;
    }
    if (!Objects.equals(details, equipment.details)) {
      return false;
    }
    if (!Objects.equals(sizes, equipment.sizes)) {
      return false;
    }
    if (!Objects.equals(mrp, equipment.mrp)) {
      return false;
    }
    if (!Objects.equals(sellPrice, equipment.sellPrice)) {
      return false;
    }
    if (!Objects.equals(discount, equipment.discount)) {
      return false;
    }
    return Objects.equals(category, equipment.category);
  }

  @Override
  public int hashCode() {
    int result = brandName != null ? brandName.hashCode() : 0;
    result = 31 * result + (details != null ? details.hashCode() : 0);
    result = 31 * result + (sizes != null ? sizes.hashCode() : 0);
    result = 31 * result + (mrp != null ? mrp.hashCode() : 0);
    result = 31 * result + (sellPrice != null ? sellPrice.hashCode() : 0);
    result = 31 * result + (discount != null ? discount.hashCode() : 0);
    result = 31 * result + (category != null ? category.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Equipment{" + "id='" + id + '\'' + ", brandName='" + brandName + '\'' + ", details='"
        + details + '\'' + ", sizes='" + sizes + '\'' + ", mrp='" + mrp + '\'' + ", sellPrice="
        + sellPrice + ", discount='" + discount + '\'' + ", category='" + category + '\'' + '}';
  }
}
