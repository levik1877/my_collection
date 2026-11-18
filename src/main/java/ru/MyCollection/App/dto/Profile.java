package ru.MyCollection.App.dto;

import ru.MyCollection.App.model.UserEntity;

import java.time.LocalDate;

public record Profile(
        Long id,
        UserEntity userId,
        String publicName,
        Long avatarFileId,
        String country,
        Integer reputation,
        String description,
        LocalDate updateAt,
        Boolean isDeleted
) {
}
