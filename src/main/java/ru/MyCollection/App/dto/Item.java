package ru.MyCollection.App.dto;

import java.time.OffsetDateTime;

public record Item(
        Long id,
        Long collectionId,
        Long ownerUserId,
        String name,
        String description,
        String visibility,
        String status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        Boolean isDeleted
) {
}
