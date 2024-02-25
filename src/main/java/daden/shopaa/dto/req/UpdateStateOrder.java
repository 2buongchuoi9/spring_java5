package daden.shopaa.dto.req;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UpdateStateOrder {

  @Pattern(regexp = "PENDING|CONFIRMED|SHIPPING|CANCELLED|DELIVERED", message = "Invalid type value must in PENDING, CONFIRMED, SHIPPING, CANCELLED, DELIVERED")
  private String state;
  @Nullable
  private String note;
}
