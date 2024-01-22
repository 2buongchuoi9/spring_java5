package daden.shopaa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import daden.shopaa.entity.Rating;
import java.util.List;

@Repository
public interface RatingRepo extends MongoRepository<Rating, String> {

  List<Rating> findAllByProductId(String productId);

}
