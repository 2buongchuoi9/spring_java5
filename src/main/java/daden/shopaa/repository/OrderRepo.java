package daden.shopaa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import daden.shopaa.entity.Order;

@Repository
public interface OrderRepo extends MongoRepository<Order, String> {

}
