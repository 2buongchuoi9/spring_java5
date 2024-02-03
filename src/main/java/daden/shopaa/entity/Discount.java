package daden.shopaa.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
  private TypeDiscount type = TypeDiscount.FIXED_AMOUNT;
  @Default
  private Double value = 0.0;

  @Default
  @Field(name = "total_count")
  private Integer totalCount = 50;

  @Default
  private Boolean status = true;

  private Date dateStart;
  private Date dateEnd;
}
