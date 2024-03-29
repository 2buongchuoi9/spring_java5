package daden.shopaa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import daden.shopaa.entity.OtpToken;

@Repository
public interface OtpRepo extends MongoRepository<OtpToken, String> {

}
