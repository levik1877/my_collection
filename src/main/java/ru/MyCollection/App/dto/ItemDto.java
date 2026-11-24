package ru.MyCollection.App.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.MyCollection.App.model.CollectionEntity;
import ru.MyCollection.App.model.UserEntity;

import java.time.OffsetDateTime;
import ru.MyCollection.App.model.entityEnums.ItemStatus;
import ru.MyCollection.App.model.entityEnums.VisibilityStatus;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    Long id;
    CollectionEntity collectionId;
    UserEntity ownerUserId;
    String name;
    String description;
    VisibilityStatus visibility;
    ItemStatus status;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
    Boolean isDeleted;
}
