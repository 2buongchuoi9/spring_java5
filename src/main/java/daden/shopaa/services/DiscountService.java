package daden.shopaa.services;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import daden.shopaa.dto.model.CartModel;
import daden.shopaa.dto.model.DiscountAmountModel;
import daden.shopaa.dto.req.CartProductReq;
import daden.shopaa.entity.Discount;
import daden.shopaa.exceptions.BabRequestError;
import daden.shopaa.exceptions.NotFoundError;
import daden.shopaa.repository.DiscountRepo;
import daden.shopaa.utils._enum.TypeDiscount;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class DiscountService {
  private final DiscountRepo discountRepo;
  private final CartService cartService;

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
    if (foundDiscount.getStatus() || foundDiscount.getTotalCount() <= 0)
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

  public Discount createDiscount() {
    // TODO
    return null;
  }

}
