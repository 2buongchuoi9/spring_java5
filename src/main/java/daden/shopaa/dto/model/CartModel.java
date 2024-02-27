package daden.shopaa.dto.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartModel {
  private String productVariationId;
  private String productId;
  private Integer quantity;
  private Double price;
}
