package daden.shopaa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import daden.shopaa.entity.Discount;

@Repository
public interface DiscountRepo extends MongoRepository<Discount, String> {

}
