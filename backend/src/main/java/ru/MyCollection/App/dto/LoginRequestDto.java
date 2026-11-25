package ru.MyCollection.App.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.MyCollection.App.exception.ValidationErrorMessage;

@Data
public class LoginRequestDto {
    @NotBlank(message = ValidationErrorMessage.EMPTY)
    @Size(max = 50)
    private String username;
    @NotBlank(message = ValidationErrorMessage.EMPTY)
    private String password;
}
