package ru.MyCollection.App.dto;

import java.time.LocalDate;
import ru.MyCollection.App.model.entityEnums.VisibilityStatus;

public record Collection(
        Long id,
        Long ownerUserId,
        String name,
        String description,
        Long coverFileId,
        VisibilityStatus visibility,
        LocalDate createdAt,
        LocalDate updatedAt,
        Boolean isDeleted
) {
}
