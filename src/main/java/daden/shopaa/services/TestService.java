package daden.shopaa.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import daden.shopaa.dto.model.CartModel;
import daden.shopaa.dto.res.ReportFromOrderRes;
import daden.shopaa.entity.Order;
import daden.shopaa.entity.Product;
import daden.shopaa.entity.ProductVariation;
import daden.shopaa.exceptions.NotFoundError;
import daden.shopaa.repository.OrderRepo;
import daden.shopaa.repository.ProductRepo;
import daden.shopaa.repository.ProductVariationRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestService {

  private final OrderRepo orderRepo;
  private final ProductVariationRepo variationRepo;
  private final ProductService productService;
  private final ProductRepo productRepo;

  public List<ReportFromOrderRes> reportFromOrdersByDay(LocalDate date) {
    LocalDateTime startOfDay = date.atStartOfDay();
    LocalDateTime endOfDay = date.atStartOfDay().plusDays(1).minusSeconds(1);

    // Thực hiện một truy vấn lớn hơn để lấy thông tin về đơn hàng và sản phẩm tương
    // ứng
    List<Order> orders = orderRepo.findByCreateDateBetween(startOfDay, endOfDay);

    // Tạo một Map để lưu trữ thông tin sản phẩm của mỗi đơn hàng
    Map<String, List<Product>> productsByOrderId = new HashMap<>();

    // Lặp qua danh sách đơn hàng để lấy thông tin sản phẩm của mỗi đơn hàng
    for (Order order : orders) {
      List<String> variationIds = order.getItems().stream()
          .map(CartModel::getProductVariationId)
          .collect(Collectors.toList());
      // List<ProductVariation> variations = variationRepo.findByIdIn(variationIds);
      List<Product> products = productService.findProductByVariationIds(variationIds);
      productsByOrderId.put(order.getId(), products);
    }

    // Tính toán doanh thu, lợi nhuận và tạo danh sách kết quả
    List<ReportFromOrderRes> result = new ArrayList<>();
    for (Order order : orders) {
      List<Product> products = productsByOrderId.get(order.getId());
      Double capital = calculateCapital(order, products);
      Double revenue = order.getTotalCheckout();
      Double profit = revenue - capital;
      result.add(ReportFromOrderRes.builder()
          .order(order)
          .capital(capital)
          .revenue(revenue)
          .profit(profit)
          .build());
    }

    return result;
  }

  // Hàm tiện ích để lấy thông tin sản phẩm dựa trên danh sách các
  // ProductVariation
  private List<Product> getProductsWithVariations(List<ProductVariation> variations) {
    List<String> productIds = variations.stream()
        .map(ProductVariation::getProductId)
        .collect(Collectors.toList());
    return productRepo.findByIdIn(productIds);
  }

  // Hàm tiện ích để tính toán vốn của mỗi đơn hàng
  private Double calculateCapital(Order order, List<Product> products) {
    Double capital = 0.0;
    for (Product product : products) {
      List<CartModel> items = order.getItems().stream()
          .filter(item -> item.getProductVariationId().equals(product.getId()))
          .collect(Collectors.toList());
      for (CartModel item : items) {
        ProductVariation variation = getProductVariation(product, item.getProductVariationId());
        capital += variation.getPriceImport() * item.getQuantity();
      }
    }
    return capital;
  }

  // Hàm tiện ích để lấy thông tin ProductVariation dựa trên productId và
  // productVariationId
  private ProductVariation getProductVariation(Product product, String productVariationId) {
    return product.getVariations().stream()
        .filter(variation -> variation.getId().equals(productVariationId))
        .findFirst()
        .orElseThrow(() -> new NotFoundError("productVariationId", productVariationId));
  }

}
