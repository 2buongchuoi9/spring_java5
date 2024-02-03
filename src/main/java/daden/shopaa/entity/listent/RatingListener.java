package daden.shopaa.entity.listent;

import daden.shopaa.entity.Product;
import daden.shopaa.entity.Rating;
import daden.shopaa.repository.ProductRepo;
import daden.shopaa.repository.RatingRepo;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RatingListener extends AbstractMongoEventListener<Rating> {
    private final ProductRepo productRepo;
    private final RatingRepo ratingRepo;

    @Override
    public void onAfterSave(AfterSaveEvent<Rating> event) {
        super.onAfterSave(event);

        Rating ratting = event.getSource();
        updateProductRating(ratting.getProductId());
    }

    private void updateProductRating(String productId) {

        // Cập nhật rattingAvg vào Product
        Product product = productRepo.findById(productId).orElse(null);
        if (product != null) {
            List<Rating> allRatings = ratingRepo.findAllByProductId(productId);
            double sum = allRatings.stream().mapToDouble(Rating::getValue).sum();
            int count = allRatings.size();

            double newRatingAvg = (count > 0) ? sum / count : 0.0;
            product.setRatingAvg(newRatingAvg);
            productRepo.save(product);
        }
    }

}
