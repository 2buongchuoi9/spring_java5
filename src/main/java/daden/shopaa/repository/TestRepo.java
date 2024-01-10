package daden.shopaa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import daden.shopaa.models.TestModel;

@Repository
public interface TestRepo extends MongoRepository<TestModel, String> {

}
