package ru.MyCollection.App.dto;

import java.time.LocalDate;

public record Collection(
        Long id,
        Long ownerUserId,
        String name,
        String description,
        Long coverFileId,
        String visibility,
        String status,
        LocalDate createdAt,
        LocalDate updatedAt,
        Boolean isDeleted
) {
}
