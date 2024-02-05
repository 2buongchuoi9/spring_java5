package daden.shopaa.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import daden.shopaa.entity.Cart;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepo extends MongoRepository<Cart, String> {
  Optional<Cart> findByUserId(String userId);

  Optional<Cart> findByUserIdAndState(String userId, String state);
}
