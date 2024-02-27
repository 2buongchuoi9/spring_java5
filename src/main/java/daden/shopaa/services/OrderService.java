package daden.shopaa.services;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import daden.shopaa.dto.model.CartModel;
import daden.shopaa.dto.model.CheckoutModel;
import daden.shopaa.dto.parampetterRequest.OrderParamRequest;
import daden.shopaa.dto.req.CartProductReq;
import daden.shopaa.dto.req.CheckoutReq;
import daden.shopaa.entity.Cart;
import daden.shopaa.entity.Discount;
import daden.shopaa.entity.Order;
import daden.shopaa.entity.Product;
import daden.shopaa.entity.ProductVariation;
import daden.shopaa.exceptions.BabRequestError;
import daden.shopaa.exceptions.NotFoundError;
import daden.shopaa.repository.CartRepo;
import daden.shopaa.repository.OrderRepo;
import daden.shopaa.repository.ProductRepo;
import daden.shopaa.repository.ProductVariationRepo;
import daden.shopaa.repository.UserRepo;
import daden.shopaa.repository.repositoryUtils.PageCustom;
import daden.shopaa.utils._enum.StateOrderEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class OrderService {
  private final MongoTemplate mongoTemplate;
  private final OrderRepo orderRepo;
  private final ProductvariationService variationService;
  private final ProductVariationRepo variationRepo;
  private final CartService cartService;
  private final DiscountService discountService;
  private final UserRepo userRepo;
  private final CartRepo cartRepo;
  private final ProductService productService;
  private final ProductRepo productRepo;

  // cartId:""
  // userId:""
  // items:[{productVariationId,quantity,oldQuantity}],
  public CheckoutModel checkoutReview(CheckoutReq checkoutReq) {
    String cartId = checkoutReq.getCartId();
    String discountId = checkoutReq.getDiscountId();
    String userId = checkoutReq.getUserId();
    List<CartProductReq> items = checkoutReq.getItems();

    cartRepo.findById(cartId)
        .orElseThrow(() -> new BabRequestError("not found cart id: " + cartId));

    userRepo.findById(userId)
        .orElseThrow(() -> new BabRequestError("not found user id: " + cartId));

    List<CartModel> listItems = cartService.checkoutProductServer(items);

    // tinh tong tien don hang chua xu li
    Double totalOrder = listItems.stream()
        .mapToDouble(e -> e.getPrice() * e.getQuantity())
        .sum();

    // check discount and calculate totalPrice after aplyy discount
    Double[] resultDiscount = { 0.0, totalOrder };
    if (discountId != null && !discountId.isEmpty())
      resultDiscount = discountService.getDiscountAmount(items, discountId, userId);

    double totalShip = Math.ceil(Math.random() * 16 + 5) * 1000;

    return CheckoutModel.builder()
        .userId(userId)
        .cartId(cartId)
        .address(checkoutReq.getAddress())
        .discountId(discountId)
        .items(listItems)
        .totalOrder(totalOrder)
        .totalDiscount(resultDiscount[0])
        .totaShip(checkoutReq.getAddress() == null ? 0 : totalShip) // randome 5000 -> 20000
        .totalCheckout(resultDiscount[1] + totalShip)
        .payment(checkoutReq.getPayment())
        .build();
  }

  private Integer getQuantity(String variationId, List<CartModel> items) {
    return items.stream().filter(v -> v.getProductVariationId().equals(variationId)).findFirst().get().getQuantity();
  }

  @Transactional(rollbackFor = Exception.class)
  public Order orderByUser(CheckoutReq checkoutReq) {
    CheckoutModel checkout = checkoutReview(checkoutReq);

    String discountId = checkout.getDiscountId();

    Map<String, Product> map = new HashMap<>();

    // List<String> variationIds = checkout.getItems().stream().map(v ->
    // v.getProductVariationId())
    // .collect(Collectors.toList());

    for (CartModel item : checkout.getItems()) {
      Product product = productService.findProductByVariation(item.getProductVariationId());
      map.put(item.getProductVariationId(), product);
    }

    // tong von
    Double capital = map.keySet().stream()
        .mapToDouble(v -> map.get(v).getPriceImport() + getQuantity(v, checkout.getItems())).sum();
    // tong doanh thu
    Double revenue = checkout.getTotalCheckout();
    // tong loi nhuan
    Double profit = revenue - capital;
    try {
      // save order
      Order order = orderRepo.save(
          Order.builder()
              .userId(checkout.getUserId())
              .shippingAddress(checkout.getAddress())
              .totalOrder(checkout.getTotalOrder())
              .totalShipping(checkout.getTotaShip())
              .totalDiscount(checkout.getTotalDiscount())
              .totalCheckout(checkout.getTotalCheckout())
              .capital(capital)
              .revenue(revenue)
              .profit(profit)
              .items(checkout.getItems())
              .payment(checkout.getPayment())
              .createDate(LocalDateTime.now())
              .state(StateOrderEnum.PENDING.name())
              .build());

      // remove product items in cart and set quantity in productVariation
      order.getItems().stream()
          .forEach(v -> {
            cartService.removeItemToCart(order.getUserId(), v.getProductVariationId());
            variationService.updateQuantityProductVariation(v.getQuantity(), v.getProductVariationId());
          });

      // update discount if exits
      if (discountId != null && !discountId.isEmpty() && checkout.getTotalDiscount() != 0.0) {
        discountService.changeTotalCountDiscount(discountId, checkout.getUserId());
      }
      return order;
    } catch (Exception e) {
      throw new BabRequestError("fali to save order. Error at: " + e.getMessage());
    }

  }

  public Order setStateOrderByAdmin(String orderId, String state, String note) {
    Order foundOrder = orderRepo.findById(orderId).orElseThrow(() -> new NotFoundError("orderId", orderId));
    foundOrder.setState(state);
    foundOrder.setNote(note);
    return orderRepo.save(foundOrder);
  }

  public Order cancelOrderByUser(String orderId, String note) {
    return setStateOrderByAdmin(orderId, StateOrderEnum.CANCELLED.name(), note);
  }

  public List<Order> findAllOrder(OrderParamRequest paramRequest) {

    LocalDateTime startDate = paramRequest.getStartDate();
    LocalDateTime endDate = paramRequest.getEndDate();

    String keySearch = paramRequest.getKeySearch();
    String state = paramRequest.getState();
    String payment = paramRequest.getPayment();

    Double minOrder = paramRequest.getMinTotalOrder();
    Double maxOrder = paramRequest.getMaxTotalOrder();

    Double minCheckout = paramRequest.getMinTotalCheckout();
    Double maxCheckout = paramRequest.getMaxTotalCheckout();

    Double minShopping = paramRequest.getMinTotalShopping();
    Double maxShopping = paramRequest.getMaxTotalShopping();

    Double minDiscount = paramRequest.getMinTotalDiscount();
    Double maxDiscount = paramRequest.getMaxTotalDiscount();

    Query query = new Query();

    if (keySearch != null && !keySearch.isEmpty()) {
      String regexPattern = "(?i)" + keySearch.trim(); // Thêm ?i để không phân biệt chữ hoa chữ thường
      query.addCriteria(new Criteria().orOperator(
          Criteria.where("shippingAddress").regex(regexPattern),
          Criteria.where("payment").regex(regexPattern),
          Criteria.where("state").regex(regexPattern),
          Criteria.where("note").regex(regexPattern)));
    }

    if (state != null && !state.isEmpty())
      query.addCriteria(Criteria.where("state").is(state));

    if (payment != null && !payment.isEmpty())
      query.addCriteria(Criteria.where("payment").is(payment));

    // date
    if (startDate != null && endDate != null)
      query.addCriteria(Criteria.where("createDate").gte(startDate).lte(endDate));
    else if (startDate != null)
      query.addCriteria(Criteria.where("createDate").gte(startDate));
    else if (endDate != null)
      query.addCriteria(Criteria.where("createDate").lte(endDate));

    // order
    getBewtten(query, "price", minOrder, maxOrder);

    // checkout
    getBewtten(query, "price", minCheckout, maxCheckout);

    // shopping
    getBewtten(query, "price", minShopping, maxShopping);

    // discount
    getBewtten(query, "price", minDiscount, maxDiscount);

    List<Order> list = mongoTemplate.find(query, Order.class);

    return list;
  }

  public PageCustom<Order> findAllOrder(Pageable pageable, OrderParamRequest paramRequest) {
    // System.out.println("pageable" + pageable);
    System.out.println("dateee" + paramRequest.getStartDate());

    LocalDateTime startDate = paramRequest.getStartDate();
    LocalDateTime endDate = paramRequest.getEndDate();

    String keySearch = paramRequest.getKeySearch();
    String state = paramRequest.getState();
    String payment = paramRequest.getPayment();

    Double minOrder = paramRequest.getMinTotalOrder();
    Double maxOrder = paramRequest.getMaxTotalOrder();

    Double minCheckout = paramRequest.getMinTotalCheckout();
    Double maxCheckout = paramRequest.getMaxTotalCheckout();

    Double minShopping = paramRequest.getMinTotalShopping();
    Double maxShopping = paramRequest.getMaxTotalShopping();

    Double minDiscount = paramRequest.getMinTotalDiscount();
    Double maxDiscount = paramRequest.getMaxTotalDiscount();

    Query query = new Query();

    if (keySearch != null && !keySearch.isEmpty()) {
      String regexPattern = "(?i)" + keySearch.trim(); // Thêm ?i để không phân biệt chữ hoa chữ thường
      query.addCriteria(new Criteria().orOperator(
          Criteria.where("shippingAddress").regex(regexPattern),
          Criteria.where("payment").regex(regexPattern),
          Criteria.where("state").regex(regexPattern),
          Criteria.where("note").regex(regexPattern)));
    }

    if (state != null && !state.isEmpty())
      query.addCriteria(Criteria.where("state").is(state));

    if (payment != null && !payment.isEmpty())
      query.addCriteria(Criteria.where("payment").is(payment));

    // date
    if (startDate != null && endDate != null)
      query.addCriteria(Criteria.where("createDate").gte(startDate).lte(endDate));
    else if (startDate != null)
      query.addCriteria(Criteria.where("createDate").gte(startDate));
    else if (endDate != null)
      query.addCriteria(Criteria.where("createDate").lte(endDate));

    // order
    getBewtten(query, "price", minOrder, maxOrder);

    // checkout
    getBewtten(query, "price", minCheckout, maxCheckout);

    // shopping
    getBewtten(query, "price", minShopping, maxShopping);

    // discount
    getBewtten(query, "price", minDiscount, maxDiscount);

    long total = mongoTemplate.count(query, Order.class);
    query.with(pageable);
    List<Order> list = mongoTemplate.find(query, Order.class);
    return new PageCustom<>(PageableExecutionUtils.getPage(list, pageable, () -> total));
  }

  public List<Order> findAllOrderByReport(Pageable pageable, OrderParamRequest paramRequest) {

    LocalDateTime startDate = paramRequest.getStartDate();
    LocalDateTime endDate = paramRequest.getEndDate();

    String keySearch = paramRequest.getKeySearch();
    String state = paramRequest.getState();
    String payment = paramRequest.getPayment();

    Double minOrder = paramRequest.getMinTotalOrder();
    Double maxOrder = paramRequest.getMaxTotalOrder();

    Double minCheckout = paramRequest.getMinTotalCheckout();
    Double maxCheckout = paramRequest.getMaxTotalCheckout();

    Double minShopping = paramRequest.getMinTotalShopping();
    Double maxShopping = paramRequest.getMaxTotalShopping();

    Double minDiscount = paramRequest.getMinTotalDiscount();
    Double maxDiscount = paramRequest.getMaxTotalDiscount();

    Query query = new Query();

    if (keySearch != null && !keySearch.isEmpty()) {
      String regexPattern = "(?i)" + keySearch.trim(); // Thêm ?i để không phân biệt chữ hoa chữ thường
      query.addCriteria(new Criteria().orOperator(
          Criteria.where("shippingAddress").regex(regexPattern),
          Criteria.where("payment").regex(regexPattern),
          Criteria.where("state").regex(regexPattern),
          Criteria.where("note").regex(regexPattern)));
    }

    if (state != null && !state.isEmpty())
      query.addCriteria(Criteria.where("state").is(state));

    if (payment != null && !payment.isEmpty())
      query.addCriteria(Criteria.where("payment").is(payment));

    // date
    if (startDate != null && endDate != null)
      query.addCriteria(Criteria.where("createDate").gte(startDate).lte(endDate));
    else if (startDate != null)
      query.addCriteria(Criteria.where("createDate").gte(startDate));
    else if (endDate != null)
      query.addCriteria(Criteria.where("createDate").lte(endDate));

    // order
    getBewtten(query, "price", minOrder, maxOrder);

    // checkout
    getBewtten(query, "price", minCheckout, maxCheckout);

    // shopping
    getBewtten(query, "price", minShopping, maxShopping);

    // discount
    getBewtten(query, "price", minDiscount, maxDiscount);

    query.with(pageable.getSort());
    List<Order> list = mongoTemplate.find(query, Order.class);
    return list;
  }

  public Order findOrderById(String orderId) {
    Order order = orderRepo.findById(orderId).orElseThrow(() -> new NotFoundError("orderId", orderId));
    return order;
  }

  public void removeOrderAndReturnProductQuantityBecausePaymentFail(String orderId) {
    Order order = orderRepo.findById(orderId).orElse(null);
    if (order != null) {
      // return quantity product
      order.getItems().stream().map(v -> {
        ProductVariation variation = variationRepo.findById(v.getProductVariationId()).get();
        variation.setQuantity(variation.getQuantity() + v.getQuantity());
        variationRepo.save(variation);
        return v;
      });
      // remove order
      orderRepo.delete(order);
    }
  }

  public List<Order> findOrdersByUserId(String id, String state) {
    Query query = new Query();
    query.addCriteria(Criteria.where("userId").is(id));
    if (state != null)
      query.addCriteria(Criteria.where("state").is(state));

    return mongoTemplate.find(query, Order.class);
  }

  // support get bewtten value double price
  private void getBewtten(Query query, String name, Double minValue, Double maxValue) {
    if (minValue != null && maxValue != null)
      query.addCriteria(Criteria.where(name).gte(minValue * 1000).lte(maxValue * 1000));
    else if (minValue != null)
      query.addCriteria(Criteria.where(name).gte(minValue * 1000));
    else if (maxValue != null)
      query.addCriteria(Criteria.where(name).lte(maxValue * 1000));
  }

}
