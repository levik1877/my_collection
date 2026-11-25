package ru.MyCollection.App.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.MyCollection.App.model.ItemEntity;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
}
