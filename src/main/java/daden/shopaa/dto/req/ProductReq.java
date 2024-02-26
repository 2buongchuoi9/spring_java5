package daden.shopaa.dto.req;

import java.util.List;

import daden.shopaa.utils._enum.StatusProductEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
  @NotNull
  @NotEmpty
  private Double priceImport;
  private List<String> categoryIds;
  private List<ProductVariationReq> variations;
  @Default
  private String status = StatusProductEnum.DRAFT.name();
}
