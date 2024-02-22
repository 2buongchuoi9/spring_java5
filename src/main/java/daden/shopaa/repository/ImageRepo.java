package daden.shopaa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import daden.shopaa.entity.Image;

@Repository
public interface ImageRepo extends MongoRepository<Image, String> {

}
