package ru.MyCollection.App.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.MyCollection.App.model.FileTypeEntity;

public interface FileTypeRepository extends JpaRepository<FileTypeEntity, Long> {
}