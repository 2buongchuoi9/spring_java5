package daden.shopaa.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Document(collection = "Ratings")
@Data
@Builder
public class Rating {
  @Id
  private String id;

  private String productId;

  private String userId;

  private Double value;
}
