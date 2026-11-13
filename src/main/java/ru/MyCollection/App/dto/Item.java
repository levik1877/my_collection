package ru.MyCollection.App.dto;

import java.time.OffsetDateTime;
import ru.MyCollection.App.model.entityEnums.ItemStatus;
import ru.MyCollection.App.model.entityEnums.VisibilityStatus;

public record Item(
        Long id,
        Long collectionId,
        Long ownerUserId,
        String name,
        String description,
        VisibilityStatus visibility,
        ItemStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        Boolean isDeleted
) {
}
