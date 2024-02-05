package daden.shopaa.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import daden.shopaa.dto.model.CartModel;
import daden.shopaa.utils._enum.StateOrderEnum;
import daden.shopaa.utils._enum.TypePayment;
import lombok.Builder;
import lombok.Data;
import lombok.Builder.Default;

@Document(collection = "Orders")
@Data
@Builder
public class Order {
  @Id
  private String id;
  private String userId;
  private String shippingAddress;
  private Double totalOrder;
  private Double totalShipping;
  private Double totalDiscount;
  private Double totalCheckout;
  private List<CartModel> items;
  @Default
  private String payment = TypePayment.CASH.name();
  @Default
  private String state = StateOrderEnum.PENDING.name();

  @CreatedDate
  @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
  private LocalDateTime createDate;
  @LastModifiedDate
  @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
  private LocalDateTime updateDate;

}
