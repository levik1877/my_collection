package ru.MyCollection.App.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.MyCollection.App.model.UserEntity;

import java.time.LocalDate;
import ru.MyCollection.App.model.entityEnums.VisibilityStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionDto {
    Long id;
    UserEntity ownerUserId;
    String name;
    String description;
    Long coverFileId;
    VisibilityStatus visibility;
    LocalDate createdAt;
    LocalDate updatedAt;
    Boolean isDeleted;
}
