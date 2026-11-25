package ru.MyCollection.App.repository;

import io.jsonwebtoken.security.Jwks;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.MyCollection.App.model.RoleEntity;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByName(String name);
}
