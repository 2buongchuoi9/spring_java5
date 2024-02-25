package daden.shopaa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import daden.shopaa.entity.Order;
import java.util.List;

@Repository
public interface OrderRepo extends MongoRepository<Order, String> {
  List<Order> findByUserId(String userId);
}
