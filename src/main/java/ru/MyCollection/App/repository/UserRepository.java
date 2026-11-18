package ru.MyCollection.App.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.MyCollection.App.model.UserEntity;



public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
}
