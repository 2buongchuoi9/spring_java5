package daden.shopaa.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.management.Query;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import daden.shopaa.dto.parampetterRequest.OrderParamRequest;
import daden.shopaa.dto.res.ReportFromOrderRes;
import daden.shopaa.entity.Order;
import daden.shopaa.entity.Product;
import daden.shopaa.entity.ProductVariation;
import daden.shopaa.exceptions.NotFoundError;
import daden.shopaa.repository.OrderRepo;
import daden.shopaa.repository.ProductRepo;
import daden.shopaa.repository.ProductVariationRepo;
// import daden.shopaa.repository.ProductRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {
  private final ProductRepo productRepo;
  private final ProductVariationRepo variationRepo;
  private final OrderRepo orderRepo;
  private final OrderService orderService;
  private final MongoTemplate mongoTemplate;

  private final ProductService productService;

  // default
  public List<ReportFromOrderRes> reportFromOrdersByDay(OrderParamRequest paramRequest) {

    // List<Order> orders = orderRepo.findByCreateDateBetween(startOfDay, endOfDay);
    List<Order> orders = orderService.findAllOrder(paramRequest);

    List<ReportFromOrderRes> result = orders.stream().map(v -> root(v))
        .collect(Collectors.toList());

    // doanh thu, loi nhuan trong 1 ngay
    return result;
  }

  // report theo ngay
  public List<ReportFromOrderRes> reportFromOrdersByDay(LocalDate date) {
    LocalDateTime startOfDay = date.atStartOfDay();
    LocalDateTime endOfDay = date.atStartOfDay().plusDays(1).minusSeconds(1);

    List<Order> orders = orderRepo.findByCreateDateBetween(startOfDay, endOfDay);

    List<ReportFromOrderRes> result = orders.stream().map(v -> reportFromOrdersByOneOrder(v.getId()))
        .collect(Collectors.toList());

    // doanh thu, loi nhuan trong 1 ngay
    return result;
  }

  // report theo thang
  public List<ReportFromOrderRes> reportFromOrdersByMonth(LocalDate date) {

    LocalDate startOfMonth = date.withDayOfMonth(1);
    LocalDate endOfMonth = date.withDayOfMonth(date.lengthOfMonth());
    LocalDateTime startOfDay = startOfMonth.atStartOfDay();
    LocalDateTime endOfDay = endOfMonth.atTime(LocalTime.MAX);

    List<Order> orders = orderRepo.findByCreateDateBetween(startOfDay, endOfDay);

    List<ReportFromOrderRes> result = orders.stream().map(v -> reportFromOrdersByOneOrder(v.getId()))
        .collect(Collectors.toList());

    // doanh thu, loi nhuan trong 1 ngay
    return result;
  }

  // report theo 1 hoa don
  public ReportFromOrderRes reportFromOrdersByOneOrder(String orderId) {
    Order order = orderRepo.findById(orderId).orElseThrow(() -> new NotFoundError("orderId", orderId));

    // tong von ban dau
    Double capital = order.getItems().stream()
        .mapToDouble(v -> productService.findProductByVariation(v.getProductVariationId()).getPriceImport()
            * v.getQuantity())
        .sum();
    // tong doanh thu
    Double revenue = order.getTotalCheckout();
    // tong loi nhuan
    Double profit = revenue - capital;

    return ReportFromOrderRes.builder()
        .order(order)
        .capital(capital)
        .revenue(revenue)
        .profit(profit)
        .build();
  }

  // report theo 1 hoa don
  public ReportFromOrderRes root(Order order) {

    // tong von ban dau
    Double capital = order.getItems().stream()
        .mapToDouble(v -> productService.findProductByVariation(v.getProductVariationId()).getPriceImport()
            * v.getQuantity())
        .sum();
    // tong doanh thu
    Double revenue = order.getTotalCheckout();
    // tong loi nhuan
    Double profit = revenue - capital;

    order.setCapital(capital);
    order.setRevenue(revenue);
    order.setProfit(profit);

    orderRepo.save(order);

    return ReportFromOrderRes.builder()
        .order(order)
        .capital(capital)
        .revenue(revenue)
        .profit(profit)
        .build();
  }

}
