package daden.shopaa.services;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import daden.shopaa.dto.model.CartModel;
import daden.shopaa.dto.model.DiscountAmountModel;
import daden.shopaa.dto.req.CartProductReq;
import daden.shopaa.dto.req.CreateDiscountReq;
import daden.shopaa.entity.Discount;
import daden.shopaa.exceptions.BabRequestError;
import daden.shopaa.exceptions.DuplicateRecordError;
import daden.shopaa.exceptions.NotFoundError;
import daden.shopaa.repository.DiscountRepo;
import daden.shopaa.repository.repositoryUtils.PageCustom;
import daden.shopaa.utils._enum.TypeDiscount;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class DiscountService {
  private final DiscountRepo discountRepo;
  private final CartService cartService;
  private final MongoTemplate mongoTemplate;

  /**
   * tinh tien khi ap dung discount cho 1 order
   * 
   * @param items
   * @param discountId
   * @param userId
   * @return [totalDiscount,totalPrice]
   */
  public Double[] getDiscountAmount(List<CartProductReq> items, String discountId, String userId) {
    Discount foundDiscount = discountRepo.findById(discountId)
        .orElseThrow(() -> new NotFoundError("discountId", discountId));

    // check status and totalCount discount current
    if (!foundDiscount.getStatus() || foundDiscount.getTotalCount() <= 0)
      throw new NotFoundError("discount expried", discountId);

    // check date discount current
    if (LocalDateTime.now().isBefore(foundDiscount.getDateStart())
        || LocalDateTime.now().isAfter(foundDiscount.getDateEnd()))
      throw new NotFoundError("Discount code has expired");

    // check count use used
    if (foundDiscount.getUserUsedIds() != null && foundDiscount.getCountUserUseDiscount() > 0) {
      int lenght = foundDiscount.getUserUsedIds().stream()
          .filter(v -> v.equals(userId)).collect(Collectors.toList()).size();
      if (lenght >= foundDiscount.getCountUserUseDiscount())
        throw new BabRequestError(
            "discount requires a maximum of " + foundDiscount.getCountUserUseDiscount() + " uses per user");
    }

    List<CartModel> listItems = cartService.checkoutProductServer(items);
    Double totalOrder = listItems.stream().mapToDouble(e -> e.getPrice() * e.getQuantity()).sum();

    // check minOrder apply discount
    if (foundDiscount.getMinOrderValue() > totalOrder)
      throw new BabRequestError(
          "Order value must be at least " + foundDiscount.getMinOrderValue() + " to apply the discount");

    // check type discount (so tien co dinh hoac theo phan tram)
    Double amount = foundDiscount.getType().equals(TypeDiscount.FIXED_AMOUNT.name()) ? foundDiscount.getValue()
        : totalOrder * (foundDiscount.getValue() / 100);

    Double[] result = { amount, totalOrder - amount };
    return result;
  }

  public List<Discount> getDiscountsByUser() {
    Query query = new Query();

    query.addCriteria(
        Criteria.where("status").is(true)
            .and("dateEnd").gt(LocalDateTime.now())
            .and("totalCount").gt(0));

    return mongoTemplate.find(query, Discount.class);
  }

  public Discount createDiscount(CreateDiscountReq discountReq) {
    if (discountRepo.existsByCode(discountReq.getCode()))
      throw new DuplicateRecordError("code", discountReq.getCode());

    return discountRepo.save(Discount.builder()
        .name(discountReq.getName())
        .code(discountReq.getCode())
        .type(discountReq.getType())
        .value(discountReq.getValue())
        .totalCount(discountReq.getTotalCount())
        .minOrderValue(discountReq.getMinOrderValue())
        .countUserUseDiscount(discountReq.getCountUserUseDiscount())
        .dateStart(discountReq.getDateStart())
        .dateEnd(discountReq.getDateEnd())
        .build());
  }

  public PageCustom<Discount> findAllDiscount(Pageable pageable) {
    Query query = new Query();

    List<Discount> list = mongoTemplate.find(query, Discount.class);
    long total = mongoTemplate.count(query, Discount.class);
    return new PageCustom<>(PageableExecutionUtils.getPage(list, pageable, () -> total));
  }

  public void changeTotalCountDiscount(String discountId, String userId) {
    Discount foundDiscount = discountRepo.findById(discountId)
        .orElseThrow(() -> new NotFoundError("discountId", discountId));

    List<String> userUsedIds = foundDiscount.getUserUsedIds();

    foundDiscount.setTotalCount(foundDiscount.getTotalCount() - 1);
    if (userUsedIds.stream().noneMatch(v -> v.equals(userId))) {
      userUsedIds.add(userId);
      foundDiscount.setUserUsedIds(userUsedIds);
    }
    discountRepo.save(foundDiscount);
  }

  public Discount findDiscountById(String id) {
    return discountRepo.findById(id).orElseThrow(() -> new NotFoundError("id", id));
  }

}
