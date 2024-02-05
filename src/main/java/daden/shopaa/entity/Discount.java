package daden.shopaa.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;

import daden.shopaa.utils._enum.TypeDiscount;
import lombok.Builder;
import lombok.Data;
import lombok.Builder.Default;

@Document
@Data
@Builder
public class Discount {
  @Id
  private String id;
  private String name;
  private String code;
  @Default
  private String type = TypeDiscount.PERCENTAGE_AMOUNT.name();
  @Default
  private Double value = 0.0; // neu type=PERCENTAGE_AMOUNT thi tinh theo %(vidu: 34.5 => 34.5%)
  @Default
  @Field(name = "total_count")
  private Integer totalCount = 50;
  @Default
  private Double minOrderValue = null; // gia tri toi thieu de ap dung discount
  private List<String> userUsedIds; // danh sach user da dung discount
  @Default
  private Integer countUserUseDiscount = 1; // so lan su dung cua moi user
  @Default
  private Boolean status = true;
  @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
  private LocalDateTime dateStart;
  @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
  private LocalDateTime dateEnd;
}
