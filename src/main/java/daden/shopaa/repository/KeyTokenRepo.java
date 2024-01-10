package daden.shopaa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import daden.shopaa.models.KeyTokenModel;
import java.util.Optional;

@Repository
public interface KeyTokenRepo extends MongoRepository<KeyTokenModel, String> {

  Optional<KeyTokenModel> findByUserId(String userId);

}
