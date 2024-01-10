package daden.shopaa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import daden.shopaa.models.ShopModel;

import java.util.Optional;

@Repository
public interface ShopRepo extends MongoRepository<ShopModel, String> {
  Optional<ShopModel> findByEmail(String email);

  boolean existsByEmail(String email);
}
