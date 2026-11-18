package ru.MyCollection.App.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.MyCollection.App.model.ProfileEntity;

public interface ProfileRepository  extends JpaRepository<ProfileEntity, Long> {
}
