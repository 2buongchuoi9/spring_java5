package daden.shopaa.dto.parampetterRequest;

import daden.shopaa.utils._enum.StatusProductEnum;
import lombok.Builder;
import lombok.Data;
import lombok.Builder.Default;

@Data
@Builder
public class ProductParamRequets {
  private String keySearch;
  private Double minPrice;
  private Double maxPrice;
  private String categoryId;
  private String color;
  private String sizee;
  @Default
  private String status = StatusProductEnum.PUBLISHED.name();
}