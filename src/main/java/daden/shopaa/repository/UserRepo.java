package daden.shopaa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import daden.shopaa.entity.User;

import java.util.Optional;

@Repository
public interface UserRepo extends MongoRepository<User, String> {
  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);
}
