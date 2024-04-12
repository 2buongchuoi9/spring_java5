package daden.shopaa.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.AssertTrue;
import lombok.Builder;
import lombok.Data;

@Document
@Data
@Builder
public class Student {
  private String id;
  private String name;
  private Long maSV;
  private Double mark;
  private String major;

  @AssertTrue(message = "mark must be between 0 and 10")
  private boolean isValidMark() {
    if (mark == null)
      return true;

    return mark >= 0.0 && mark <= 10;
  }

}
