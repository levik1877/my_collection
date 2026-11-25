package ru.MyCollection.App.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
    @NotNull
    UserEntity ownerUserId;
    @NotBlank
    @Size(max = 255)
    String name;
    String description;
    @Positive
    Long coverFileId;
    @NotNull
    VisibilityStatus visibility;
    LocalDate createdAt;
    LocalDate updatedAt;
    Boolean isDeleted;
}
