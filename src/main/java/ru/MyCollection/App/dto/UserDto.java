package ru.MyCollection.App.dto;

import java.time.LocalDate;

public record UserDto(
        Long id,
        String username,
        String passwordHash,
        String email,
        LocalDate emailVerifiedAt,
        LocalDate createdAt,
        LocalDate updateAt,
        LocalDate lastLoginAt,
        Boolean isDeleted
) {
}
