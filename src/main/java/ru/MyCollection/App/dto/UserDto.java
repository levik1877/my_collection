package ru.MyCollection.App.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    Long id;
    String username;
    String passwordHash;
    String email;
    LocalDate emailVerifiedAt;
    LocalDate createdAt;
    LocalDate updateAt;
    LocalDate lastLoginAt;
    Boolean isDeleted;
}
