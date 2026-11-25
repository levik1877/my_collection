package ru.MyCollection.App.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.MyCollection.App.exception.ValidationErrorMessage;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotBlank(message = ValidationErrorMessage.EMPTY)
    Long id;

    @NotBlank(message = ValidationErrorMessage.EMPTY)
    @Size(min = 3, max = 50, message = ValidationErrorMessage.OUT_OF_MIN_OR_MAX_SIZE)
    String username;

    @NotBlank(message = ValidationErrorMessage.EMPTY)
    @Size(max = 255, message = ValidationErrorMessage.OUT_OF_MAX_SIZE)
    String passwordHash;

    @NotBlank(message = ValidationErrorMessage.EMPTY)
    @Email(message = ValidationErrorMessage.BAD_EMAIL)
    String email;

    LocalDate emailVerifiedAt;

    @NotBlank(message = ValidationErrorMessage.EMPTY)
    LocalDate createdAt;

    LocalDate updateAt;

    LocalDate lastLoginAt;

    @NotNull(message = ValidationErrorMessage.NULL)
    Boolean isDeleted;
}
