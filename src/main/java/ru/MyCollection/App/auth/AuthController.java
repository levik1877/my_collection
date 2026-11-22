package ru.MyCollection.App.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.MyCollection.App.dto.UserDto;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth")
    public ResponseEntity<TokenResponse> authenticate(
            @RequestBody LoginRequest authRequest
    ) {
        return ResponseEntity.ok(authService.authenticate(authRequest));
    }

    @PostMapping("/registration")
    public ResponseEntity<UserDto> createNewUser(
            @RequestBody RegistrationUserDto registrationUserDto
    ) {
        return ResponseEntity.ok(authService.createNewUser(registrationUserDto));
    }
}
