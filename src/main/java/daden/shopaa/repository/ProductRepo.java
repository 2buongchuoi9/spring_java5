package daden.shopaa.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import daden.shopaa.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
public interface ProductRepo extends MongoRepository<Product, String> {
  Optional<Product> findByName(String name);

  Boolean existsByName(String name);

  Optional<Product> findByNameAndIdNot(String name, String id);

  Boolean existsByNameAndIdNot(String name, String id);

  // Optional<Product> findByNameAndIdNot(String name, String id);

}
