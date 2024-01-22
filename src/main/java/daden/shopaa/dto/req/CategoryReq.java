package daden.shopaa.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Builder.Default;

@Data
@Builder
public class CategoryReq {

  @Default
  private String parentId = null;

  @NotNull(message = "title is must require")
  private String title;

  private String description;

  private String thumb;
}
