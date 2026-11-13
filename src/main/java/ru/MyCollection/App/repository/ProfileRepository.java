package ru.MyCollection.App.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.MyCollection.App.model.UserEntity;

public interface ProfileRepository  extends JpaRepository<UserEntity, Long> {
}
