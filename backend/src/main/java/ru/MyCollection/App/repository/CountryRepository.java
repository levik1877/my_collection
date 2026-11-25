package ru.MyCollection.App.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.MyCollection.App.model.CountryEntity;

public interface CountryRepository extends JpaRepository<CountryEntity, Long> {
}