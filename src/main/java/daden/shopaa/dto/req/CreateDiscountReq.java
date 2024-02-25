package daden.shopaa.dto.req;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;

import daden.shopaa.config.WebMvcConfig;
import daden.shopaa.utils._enum.TypeDiscount;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateDiscountReq {
  private String name;
  private String code;
  @Pattern(regexp = "FIXED_AMOUNT|PERCENTAGE_AMOUNT", message = "Invalid type value must in FIXED_AMOUNT, PERCENTAGE_AMOUNT")
  private String type;
  private Double value;
  private Integer totalCount;
  private Double minOrderValue;
  private Integer countUserUseDiscount;

  @JsonFormat(pattern = WebMvcConfig.dateTimeFormat)
  @DateTimeFormat
  private LocalDateTime dateStart;
  @JsonFormat(pattern = WebMvcConfig.dateTimeFormat)
  @DateTimeFormat
  private LocalDateTime dateEnd;

  @AssertTrue(message = "start date must befor end date")
  private boolean isValidateDate() {
    if (dateEnd.isBefore(dateStart))
      return false;
    return true;
  }
}
