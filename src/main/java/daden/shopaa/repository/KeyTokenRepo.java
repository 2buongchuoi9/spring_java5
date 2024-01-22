package daden.shopaa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import daden.shopaa.entity.KeyToken;

import java.util.Optional;

@Repository
public interface KeyTokenRepo extends MongoRepository<KeyToken, String> {

  Optional<KeyToken> findByUserId(String userId);

}
