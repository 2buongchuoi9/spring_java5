package daden.shopaa.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Document
@Builder
@Data
public class OtpToken {
  @Id
  private String id;
  private Integer token;
  private String userId;

  @Indexed(expireAfterSeconds = 180) // tự động xóa bảng ghi sau 3 phút
  private LocalDateTime createAt = LocalDateTime.now();

}
