package daden.shopaa.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryReq {

  private String parentId;

  @NotNull(message = "title is must require")
  private String title;

  private String description;

  private String thumb;
}
