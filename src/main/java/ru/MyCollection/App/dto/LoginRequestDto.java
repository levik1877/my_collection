package ru.MyCollection.App.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.MyCollection.App.exception.ValidationErrorMessage;

@Data
public class LoginRequestDto {
    @NotBlank(message = ValidationErrorMessage.EMPTY)
    private String username;
    @NotBlank(message = ValidationErrorMessage.EMPTY)
    private String password;
}
