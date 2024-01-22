package daden.shopaa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import daden.shopaa.entity.Product;

@Repository
public interface ProductRepo extends MongoRepository<Product, String> {

}
