package daden.shopaa.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import daden.shopaa.dto.req.ProductVariationReq;
import daden.shopaa.entity.Product;
import daden.shopaa.entity.ProductVariation;
import daden.shopaa.repository.ProductVariationRepo;

@Service
public class ProductvariationService {
  @Autowired
  private ProductVariationRepo variationRepo;

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
}
