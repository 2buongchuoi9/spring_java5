package daden.shopaa.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import daden.shopaa.utils._enum.StatusProductEnum;
import lombok.Builder;
import lombok.Data;
import lombok.Builder.Default;

@Document(collection = "Producsts")
@CompoundIndex(name = "index_status", def = "{'status': 1}")
@Data
@Builder
public class Product {
  @Id
  private String id;
  private String name;
  private String slug;
  private String image;
  private List<String> images;
  private Double price;
  @Default
  private Double discount = 0.0;
  @Default
  private Integer quantity = 0;
  private String description;
  private List<Category> categories;
  @Default
  private Double ratingAvg = 4.5;
  @Default
  private String status = StatusProductEnum.DRAFT.name();
  private List<ProductVariation> variations;

}
