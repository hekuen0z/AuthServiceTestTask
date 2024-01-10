package app.magicphoto.authservice.controller.error;

import app.magicphoto.authservice.error.security.AuthenticationErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
@Tag(name = "Контроллер для обработки ошибок авторизации")
public class AuthenticationErrorHandlingControllerAdvice {

    @ExceptionHandler(AuthenticationException.class)
    @Operation(summary = "Метод для обработки AuthenticationErrorResponse.")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public AuthenticationErrorResponse onAuthenticationException(AuthenticationException e) {
        AuthenticationErrorResponse response;

        if (e instanceof BadCredentialsException) {
            response = new AuthenticationErrorResponse(
                    HttpStatus.BAD_REQUEST.value(), "Bad credentials: " + e.getMessage());
        } else if (e instanceof AccountStatusException) {
            response = new AuthenticationErrorResponse(
                    HttpStatus.FORBIDDEN.value(), e.getMessage());
        } else if (e instanceof UsernameNotFoundException) {
            response = new AuthenticationErrorResponse(
                    HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } else {
            response = new AuthenticationErrorResponse(
                    HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        }

        return response;
    }

    @ExceptionHandler(AccessDeniedException.class)
    @Operation(summary = "Метод для обработки исключения AccessDeniedException")
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public AuthenticationErrorResponse onAccessDeniedException(AccessDeniedException e) {
        return new AuthenticationErrorResponse(
                HttpStatus.FORBIDDEN.value(), e.getMessage() + " " + e.getCause());
    }

    @ExceptionHandler(ResponseStatusException.class)
    @Operation(summary = "Метод для обработки исключения ResponseStatusException")
    public AuthenticationErrorResponse onResponseStatusException(ResponseStatusException e) {
        return new AuthenticationErrorResponse(
                e.getStatusCode().value(), e.getReason());
    }

    @ExceptionHandler(JwtException.class)
    @Operation(summary = "Метод для обработки JwtException.")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public AuthenticationErrorResponse onJwtValidationException(JwtException e) {
        AuthenticationErrorResponse response;

        if(e instanceof SignatureException) {
            response = new AuthenticationErrorResponse(
                    HttpStatus.UNAUTHORIZED.value(), "The JWT Signature is invalid");
        } else if (e instanceof ExpiredJwtException) {
            response = new AuthenticationErrorResponse(
                    HttpStatus.UNAUTHORIZED.value(), "The JWT token has expired");
        } else {
            response=new AuthenticationErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Unknown internal JWT service error.");
        }

        return response;
    }
}
