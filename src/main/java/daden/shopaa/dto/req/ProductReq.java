package daden.shopaa.dto.req;

import java.util.List;

import daden.shopaa.utils._enum.StatusProductEnum;
import lombok.Builder.Default;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductReq {
  private String name;
  private String image;
  private List<String> images;
  private String description;
  private Double price;
  @Default
  private Double discount = 0.0;
  private List<String> categoryIds;
  private List<ProductVariationReq> variations;
  @Default
  private String status = StatusProductEnum.DRAFT.name();
}
