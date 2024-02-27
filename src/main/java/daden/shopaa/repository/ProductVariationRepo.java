package daden.shopaa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import daden.shopaa.entity.ProductVariation;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariationRepo extends MongoRepository<ProductVariation, String> {
  List<ProductVariation> findByProductId(String productId);

  Optional<ProductVariation> findByProductIdAndColorAndSize(String productId, String color, String size);

  // findByIdIn
}
