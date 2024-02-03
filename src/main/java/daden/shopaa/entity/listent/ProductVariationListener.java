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
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductVariationListener extends AbstractMongoEventListener<ProductVariation> {
  private final ProductRepo productRepo;

  // @Override
  // public void onBeforeSave(BeforeSaveEvent<ProductVariation> event) {
  // super.onBeforeSave(event);
  // ProductVariation productVariation = event.getSource();
  // updateProductQuantity(productVariation);
  // }

  @Override
  public void onAfterSave(AfterSaveEvent<ProductVariation> event) {
    super.onAfterSave(event);
    ProductVariation productVariation = event.getSource();
    updateProductQuantity(productVariation);
  }

  private void updateProductQuantity(ProductVariation productVariation) {
    String productId = productVariation.getProductId();

    Product product = productRepo.findById(productId).orElse(null);
    if (product != null && product.getVariations() != null) {
      int totalQuantity = product.getVariations().stream().mapToInt(ProductVariation::getQuantity).sum();
      product.setQuantity(totalQuantity);
      productRepo.save(product);
    }
  }

}
