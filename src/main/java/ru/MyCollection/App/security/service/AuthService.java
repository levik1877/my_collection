package ru.MyCollection.App.security.service;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.MyCollection.App.dto.LoginRequestDto;
import ru.MyCollection.App.dto.RegistrationUserDto;
import ru.MyCollection.App.dto.TokenResponseDto;
import ru.MyCollection.App.dto.UserDto;
import ru.MyCollection.App.security.JwtTokenUtils;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final CustomUserDetailsService userService;
    private final JwtTokenUtils jwtTokenUtils;
    @Lazy
    private final AuthenticationManager authenticationManager;

    public TokenResponseDto authenticate(
            LoginRequestDto authRequest
    ) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);
        userService.setLastLoginDateUserByUsername(authRequest.getUsername());
        return new TokenResponseDto(token);
    }

    public UserDto createNewUser(
            RegistrationUserDto registrationUserDto
    ) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords are not equal");
        }
        return userService.createNewUser(registrationUserDto);
    }
}
