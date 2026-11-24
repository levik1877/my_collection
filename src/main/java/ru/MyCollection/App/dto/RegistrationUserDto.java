package ru.MyCollection.App.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.MyCollection.App.exception.ValidationErrorMessage;

@Data
public class RegistrationUserDto {
    @NotBlank(message = ValidationErrorMessage.NOT_EMPTY)
    private String username;
    @NotBlank(message = ValidationErrorMessage.NOT_EMPTY)
    private String password;
    @NotBlank(message = ValidationErrorMessage.NOT_EMPTY)
    private String confirmPassword;
    @Email(message = ValidationErrorMessage.BAD_EMAIL)
    private String email;
}
