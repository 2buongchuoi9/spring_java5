package daden.shopaa.dto.res;

import java.util.List;

import daden.shopaa.dto.req.CartProductReq;
import daden.shopaa.entity.Order;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportFromOrderRes {
  private Order order;
  // von ban dau
  private Double capital;
  // doanh thu
  private Double revenue;
  // loi nhuan
  private Double profit;
}
