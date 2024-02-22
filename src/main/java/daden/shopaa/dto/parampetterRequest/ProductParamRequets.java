package daden.shopaa.dto.parampetterRequest;

import java.util.ArrayList;
import java.util.List;

import daden.shopaa.utils._enum.StatusProductEnum;
import jakarta.validation.constraints.AssertTrue;
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

  @AssertTrue(message = "minPrice must be less than or equal to maxPrice")
  boolean isMinMaxValid() {
    if (minPrice != null && maxPrice != null)
      return minPrice <= maxPrice;
    return true;
  }

  public String getMessage() {
    List<String> params = new ArrayList<>();

    if (keySearch != null && !keySearch.isEmpty())
      params.add("keySearch: " + keySearch);

    if (minPrice != null)
      params.add("minPrice: " + minPrice);

    if (maxPrice != null)
      params.add("maxPrice: " + maxPrice);

    if (categoryId != null && !categoryId.isEmpty())
      params.add("categoryId: " + categoryId);

    if (color != null)
      params.add("color: " + color);

    if (sizee != null)
      params.add("sizee: " + sizee);

    return params.isEmpty() ? "success" : "success with param: " + String.join(", ", params);
  }
}