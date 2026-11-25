package ru.MyCollection.App.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.MyCollection.App.exception.ValidationErrorMessage;

@Data
public class RegistrationUserDto {
    @NotBlank(message = ValidationErrorMessage.EMPTY)
    @Size(min = 3, max = 50)
    private String username;
    @NotBlank(message = ValidationErrorMessage.EMPTY)
    @Size(min = 8, max = 72)
    private String password;
    @NotBlank(message = ValidationErrorMessage.EMPTY)
    @Size(min = 8, max = 72)
    private String confirmPassword;
    @Email(message = ValidationErrorMessage.BAD_EMAIL)
    @NotBlank(message = ValidationErrorMessage.EMPTY)
    @Size(max = 255)
    private String email;
}
