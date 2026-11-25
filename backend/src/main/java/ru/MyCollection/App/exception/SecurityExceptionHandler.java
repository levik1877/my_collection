package ru.MyCollection.App.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.PrematureJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.MyCollection.App.dto.ErrorResponseDto;

import java.time.LocalDateTime;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(SecurityExceptionHandler.class);

    @ExceptionHandler(exception = {
            BadCredentialsException.class,
            UsernameNotFoundException.class,
            InsufficientAuthenticationException.class,
            AuthenticationCredentialsNotFoundException.class
    })
    public ResponseEntity<ErrorResponseDto> handleUnauthorizedException(Exception e) {
        log.error("Handle 401 unauthorized", e);
        var errorDto = new ErrorResponseDto(
                "Unauthorized",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
    }

    @ExceptionHandler(exception = {
            AccessDeniedException.class
    })
    public ResponseEntity<ErrorResponseDto> handleForbiddenException(Exception e) {
        log.error("Handle 403 Forbidden", e);
        var errorDto = new ErrorResponseDto(
                "Forbidden",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDto);
    }

    @ExceptionHandler(exception = {
            ExpiredJwtException.class,
            SignatureException.class,
            MalformedJwtException.class,
            UnsupportedJwtException.class,
            PrematureJwtException.class
    })
    public ResponseEntity<ErrorResponseDto> handleJwtUnauthorizedException(Exception e) {
        log.error("Jwt handle 401 Unauthorized", e);
        var errorDto = new ErrorResponseDto(
                "Jwt unauthorized",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
    }
}
