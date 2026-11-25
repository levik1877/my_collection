package ru.MyCollection.App.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.MyCollection.App.exception.ValidationErrorMessage;
import ru.MyCollection.App.model.RoleEntity;

import java.time.LocalDate;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @Positive
    Long id;

    @NotNull(message = ValidationErrorMessage.NULL)
    Collection<@NotNull RoleEntity> roles;

    @NotBlank(message = ValidationErrorMessage.EMPTY)
    @Size(min = 3, max = 50, message = ValidationErrorMessage.OUT_OF_MIN_OR_MAX_SIZE)
    String username;

    @NotBlank(message = ValidationErrorMessage.EMPTY)
    @Email(message = ValidationErrorMessage.BAD_EMAIL)
    @Size(max = 255, message = ValidationErrorMessage.OUT_OF_MAX_SIZE)
    String email;

    LocalDate emailVerifiedAt;

    LocalDate createdAt;

    LocalDate updateAt;

    LocalDate lastLoginAt;

    Boolean isDeleted;
}
