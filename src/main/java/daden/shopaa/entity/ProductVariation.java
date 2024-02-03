package daden.shopaa.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Document(collection = "Variations")
@CompoundIndex(name = "index_productId_color_zise", def = "{productId: 1, color: 1, size: 1}")
@Data
@Builder
public class ProductVariation {
  @Id
  private String id;
  private String productId;
  private String color;
  private String size;
  private Integer quantity;
}
