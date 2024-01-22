package daden.shopaa.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Inventories")
public class Inventory {
  @Id
  private String id;

  private ProductVariation productVariationId;

  private Integer stock;
}
