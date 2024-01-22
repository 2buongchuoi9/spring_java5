package daden.shopaa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import daden.shopaa.entity.Category;

@Repository
public interface CategoryRepo extends MongoRepository<Category, String> {
  boolean existsByTitle(String title);

  boolean existsByTitleAndIdNot(String title, String id);
}
