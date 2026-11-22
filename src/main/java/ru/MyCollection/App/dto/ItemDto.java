package ru.MyCollection.App.dto;

import ru.MyCollection.App.model.CollectionEntity;
import ru.MyCollection.App.model.UserEntity;

import java.time.OffsetDateTime;
import ru.MyCollection.App.model.entityEnums.ItemStatus;
import ru.MyCollection.App.model.entityEnums.VisibilityStatus;

public record ItemDto(
        Long id,
        CollectionEntity collectionId,
        UserEntity ownerUserId,
        String name,
        String description,
        VisibilityStatus visibility,
        ItemStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        Boolean isDeleted
) {
}
