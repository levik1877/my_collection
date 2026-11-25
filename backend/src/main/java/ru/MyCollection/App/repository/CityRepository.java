package ru.MyCollection.App.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.MyCollection.App.model.CityEntity;

public interface CityRepository extends JpaRepository<CityEntity, Long> {
}