package daden.shopaa.dto.req;

import daden.shopaa.entity.ProductVariation;
import lombok.Data;

@Data
public class ProductVariationReq {
  private String color;
  private String size;
  private Integer quantity;

  public ProductVariation maptoEntity(String productId) {
    return ProductVariation.builder()
        .productId(productId)
        .color(color)
        .size(size)
        .build();

  }
}
