package daden.shopaa.entity.listent;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;

import daden.shopaa.entity.Product;
import daden.shopaa.entity.ProductVariation;
import daden.shopaa.repository.ProductRepo;

public class ProductVariationListener extends AbstractMongoEventListener<ProductVariation> {

  @Autowired
  private ProductRepo productRepo;

  @Override
  public void onAfterSave(AfterSaveEvent<ProductVariation> event) {
    super.onAfterSave(event);

    ProductVariation productVariation = event.getSource();

    updateProductQuantity(productVariation);

  }

  private void updateProductQuantity(ProductVariation productVariation) {
    String productId = productVariation.getProductId();

    // Truy vấn Product từ cơ sở dữ liệu
    Product product = productRepo.findById(productId).orElse(null);

    if (product != null) {
      int totalQuantity = product.getVariations().stream().mapToInt(ProductVariation::getQuantity).sum();
      product.setQuantity(totalQuantity);
      productRepo.save(product);
    }
  }

}
