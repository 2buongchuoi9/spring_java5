package daden.shopaa.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Document(collection = "Variations")
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
