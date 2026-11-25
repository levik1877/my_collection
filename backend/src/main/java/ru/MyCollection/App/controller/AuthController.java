package ru.MyCollection.App.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.MyCollection.App.security.service.AuthService;
import ru.MyCollection.App.dto.LoginRequestDto;
import ru.MyCollection.App.dto.RegistrationUserDto;
import ru.MyCollection.App.dto.TokenResponseDto;
import ru.MyCollection.App.dto.UserDto;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth")
    public ResponseEntity<TokenResponseDto> authenticate(
            @Valid @RequestBody LoginRequestDto authRequest
    ) {
        return ResponseEntity.ok(authService.authenticate(authRequest));
    }

    @PostMapping("/registration")
    public ResponseEntity<UserDto> createNewUser(
            @Valid @RequestBody RegistrationUserDto registrationUserDto
    ) {
        return ResponseEntity.ok(authService.createNewUser(registrationUserDto));
    }
}
