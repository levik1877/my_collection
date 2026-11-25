package ru.MyCollection.App.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
    @Positive
    Long id;
    @NotNull
    CollectionEntity collectionId;
    @NotNull
    UserEntity ownerUserId;
    @NotBlank
    @Size(max = 255)
    String name;
    String description;
    @NotNull
    VisibilityStatus visibility;
    @NotNull
    ItemStatus status;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
    Boolean isDeleted;
}
