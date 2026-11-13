package ru.MyCollection.App.dto;

import java.time.LocalDate;

public record Profile(
        Long id,
        Long userId,
        String publicName,
        Long avatarFileId,
        String country,
        Integer reputation,
        String description,
        LocalDate updateAt,
        Boolean isDeleted
) {

}