package daden.shopaa.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Document()
@Builder
@Data
public class TestModel {
  @Id
  private String id;
  private String name;

}
