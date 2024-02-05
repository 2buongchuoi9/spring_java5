package daden.shopaa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import daden.shopaa.entity.User;
import daden.shopaa.utils._enum.RoleShopEnum;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepo extends MongoRepository<User, String> {
  Optional<User> findByEmail(String email);

  Set<User> findByRoles(Set<RoleShopEnum> roles);

  Optional<User> findByIdAndRolesIn(String id, Collection<RoleShopEnum> roles);

  boolean existsByEmail(String email);
}
