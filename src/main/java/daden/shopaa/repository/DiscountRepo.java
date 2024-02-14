package daden.shopaa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import daden.shopaa.entity.Discount;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountRepo extends MongoRepository<Discount, String> {
  Optional<Discount> findByCode(String code);

  boolean existsByCode(String code);
}
