package daden.shopaa.dto.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class UpdateStateOrder {
  private String state;
  @NotEmpty(message = "note is must require")
  private String note;
}
