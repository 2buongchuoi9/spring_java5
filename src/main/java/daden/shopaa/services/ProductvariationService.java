package daden.shopaa.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import daden.shopaa.dto.req.ProductVariationReq;
import daden.shopaa.entity.Product;
import daden.shopaa.entity.ProductVariation;
import daden.shopaa.exceptions.BabRequestError;
import daden.shopaa.exceptions.NotFoundError;
import daden.shopaa.repository.ProductVariationRepo;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@SuppressWarnings("null")
public class ProductvariationService {
  private final ProductVariationRepo variationRepo;

  public List<ProductVariation> addOrUpdateProductVariation(List<ProductVariationReq> variations, Product product) {
    List<ProductVariation> existingVariations = product.getVariations();
    if (existingVariations == null) {
      existingVariations = new ArrayList<>();
      product.setVariations(existingVariations);
    }

    for (ProductVariationReq newVariation : variations) {
      boolean variationExists = existingVariations.stream()
          .anyMatch(v -> v.getColor().equals(newVariation.getColor()) && v.getSize().equals(newVariation.getSize()));

      if (variationExists) {
        // Variation already exists, update quantity
        existingVariations.stream()
            .filter(v -> v.getColor().equals(newVariation.getColor()) && v.getSize().equals(newVariation.getSize()))
            .findFirst()
            .ifPresent(v -> v.setQuantity(v.getQuantity() + newVariation.getQuantity()));
      } else {
        // Variation does not exist, add it
        existingVariations.add(ProductVariation.builder()
            .productId(product.getId())
            .size(newVariation.getSize())
            .color(newVariation.getColor())
            .quantity(newVariation.getQuantity())
            .build());
      }
    }
    return variationRepo.saveAll(existingVariations);
  }

  public void updateQuantityProductVariation(Integer minusQuantity, String productVatiationId) {
    ProductVariation foundVariation = variationRepo.findById(productVatiationId)
        .orElseThrow(() -> new NotFoundError("productVatiationId", productVatiationId));

    if (foundVariation.getQuantity() < minusQuantity)
      throw new BabRequestError("quantity in inventory is " + foundVariation.getQuantity() + " but you set "
          + minusQuantity + " at productVariationId: " + productVatiationId);

    foundVariation.setQuantity(foundVariation.getQuantity() - minusQuantity);
    variationRepo.save(foundVariation);
  }

}
