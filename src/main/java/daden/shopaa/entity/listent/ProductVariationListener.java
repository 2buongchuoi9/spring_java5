package daden.shopaa.entity.listent;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

import daden.shopaa.entity.Product;
import daden.shopaa.entity.ProductVariation;
import daden.shopaa.repository.ProductRepo;
import daden.shopaa.repository.ProductVariationRepo;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@SuppressWarnings("null")
public class ProductVariationListener extends AbstractMongoEventListener<ProductVariation> {
  private final ProductRepo productRepo;
  private final ProductVariationRepo variationRepo;

  @Override
  public void onAfterSave(AfterSaveEvent<ProductVariation> event) {
    super.onAfterSave(event);
    ProductVariation productVariation = event.getSource();
    updateProductQuantity(productVariation);
  }

  private void updateProductQuantity(ProductVariation productVariation) {
    String productId = productVariation.getProductId();
    List<ProductVariation> variations = variationRepo.findByProductId(productId);

    Product product = productRepo.findById(productId).orElse(null);
    if (product != null && variations != null) {
      int quantity = variations.stream().mapToInt(v -> v.getQuantity()).sum();
      product.setVariations(variations);
      product.setQuantity(quantity);
      productRepo.save(product);
    }
  }

}
