package daden.shopaa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import daden.shopaa.entity.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepo extends MongoRepository<Order, String> {
  List<Order> findByUserId(String userId);

  @Query("{ 'createDate' : { $gte: ?0, $lt: ?1 } }")
  List<Order> findByCreateDateBetween(LocalDateTime startDate, LocalDateTime endDate);

  default List<Order> findByCreateDateBetween(LocalDate startDate, LocalDate endDate) {
    LocalDateTime startOfDay = startDate.atStartOfDay();
    LocalDateTime endOfDay = endDate.atStartOfDay().plusDays(1).minusSeconds(1);
    return findByCreateDateBetween(startOfDay, endOfDay);
  }

}
