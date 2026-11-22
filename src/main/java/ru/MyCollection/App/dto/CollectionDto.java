package ru.MyCollection.App.dto;

import ru.MyCollection.App.model.UserEntity;

import java.time.LocalDate;
import ru.MyCollection.App.model.entityEnums.VisibilityStatus;

public record CollectionDto(
        Long id,
        UserEntity ownerUserId,
        String name,
        String description,
        Long coverFileId,
        VisibilityStatus visibility,
        LocalDate createdAt,
        LocalDate updatedAt,
        Boolean isDeleted
) {
}
