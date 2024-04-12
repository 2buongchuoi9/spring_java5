package daden.shopaa.dto.req;

import java.util.List;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.Builder.Default;

@Data
@Builder
public class StudentReq {
  private final List<String> validMajors = List.of("Ứng dụng phần mềm", "Phát triển phần mềm", "Công nghệ thông tin",
      "Lập trình web", "Lập trình mobile");

  @NotEmpty(message = "name is required")
  private String name;
  @Default
  private Double mark = -1.0;
  @NotEmpty(message = "major is required")
  private String major;

  @AssertTrue(message = "major must be one of the following: 'Ứng dụng phần mềm', 'Phát triển phần mềm', 'Công nghệ thông tin', 'Lập trình web', 'Lập trình mobile")
  private boolean isValidMajor() {
    return validMajors.contains(major);
  }
}
