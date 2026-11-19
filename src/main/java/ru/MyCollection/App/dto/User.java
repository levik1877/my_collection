package ru.MyCollection.App.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.MyCollection.App.model.RoleEntity;

import java.time.LocalDate;

public record User(
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
