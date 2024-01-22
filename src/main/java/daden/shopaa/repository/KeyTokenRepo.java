package daden.shopaa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import daden.shopaa.entity.KeyToken;

import java.util.Optional;
import java.util.List;

@Repository
public interface KeyTokenRepo extends MongoRepository<KeyToken, String> {

  Optional<KeyToken> findByUserId(String userId);

  Optional<KeyToken> findByRefreshToken(String refreshToken);

  void deleteByUserId(String userId);

}
