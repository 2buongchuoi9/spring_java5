package daden.shopaa.dto.parampetterRequest;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Unwrapped.Empty;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import daden.shopaa.config.WebMvcConfig;
import lombok.Data;

@Data
public class OrderParamRequest {
  private String keySearch;

  private Double minTotalOrder;
  private Double maxTotalOrder;

  private Double minTotalCheckout;
  private Double maxTotalCheckout;

  private Double minTotalShopping;
  private Double maxTotalShopping;

  private Double minTotalDiscount;
  private Double maxTotalDiscount;

  @DateTimeFormat()
  @JsonFormat(pattern = WebMvcConfig.dateTimeFormat)
  private LocalDateTime startDate;

  @DateTimeFormat
  @JsonFormat(pattern = WebMvcConfig.dateTimeFormat)
  private LocalDateTime endDate;

  private String state;
  private String payment;

}
