package ru.MyCollection.App.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.MyCollection.App.model.UploadedFileEntity;

public interface UploadedFileRepository extends JpaRepository<UploadedFileEntity, Long> {
}