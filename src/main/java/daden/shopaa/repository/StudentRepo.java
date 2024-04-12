package daden.shopaa.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import daden.shopaa.entity.Student;

@Repository
public interface StudentRepo extends MongoRepository<Student, String> {

  Optional<Student> findTopByOrderByMaSVDesc();

}
