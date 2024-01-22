package daden.shopaa.entity.listent;

import com.github.slugify.Slugify;
import daden.shopaa.entity.Product;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.stereotype.Component;

@Component
public class ProductListener extends AbstractMongoEventListener<Product> {

  @Override
  public void onBeforeSave(BeforeSaveEvent<Product> event) {
    super.onBeforeSave(event);

    Product product = event.getSource();
    addSlugIfNotPresent(product);
  }

  private void addSlugIfNotPresent(Product product) {
    if (product != null && product.getSlug() == null) {
      String generatedSlug = Slugify.builder().build().slugify(product.getName());
      product.setSlug(generatedSlug);
    }
  }
}
