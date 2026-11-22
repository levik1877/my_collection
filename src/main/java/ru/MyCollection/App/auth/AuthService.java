package ru.MyCollection.App.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.MyCollection.App.dto.UserDto;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final CustomUserDetailsService userService;
    private final JwtTokenUtils jwtTokenUtils;
    @Lazy
    private final AuthenticationManager authenticationManager;

    public TokenResponse authenticate(
            LoginRequest authRequest
    ) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);
        return new TokenResponse(token);
    }

    public UserDto createNewUser(
            RegistrationUserDto registrationUserDto
    ) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords are not equal");
        }
        if (userService.findByUsername(registrationUserDto.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        }
        return userService.createNewUser(registrationUserDto);
    }
}
