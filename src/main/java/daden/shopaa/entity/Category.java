package daden.shopaa.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;
import lombok.Builder.Default;

@Document(collection = "Categories")
@Builder
@Data
public class Category {
  @Id
  private String id;

  @Default
  private String parentId = null;

  private String title;

  private String description;

  private String thumb;
}
