package ru.MyCollection.App.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.MyCollection.App.model.CollectionEntity;

public interface CollectionRepository extends JpaRepository<CollectionEntity, Long> {
}
