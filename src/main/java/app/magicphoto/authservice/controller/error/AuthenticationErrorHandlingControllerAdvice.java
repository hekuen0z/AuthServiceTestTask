package app.magicphoto.authservice.controller.error;

import app.magicphoto.authservice.error.security.AuthenticationErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Tag(name = "Контроллер для обработки ошибок авторизации")
public class AuthenticationErrorHandlingControllerAdvice {

    @ResponseBody
    @ExceptionHandler(AuthenticationException.class)
    @Operation(summary = "Метод для отправки AuthenticationErrorResponse")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public AuthenticationErrorResponse onAuthenticationException(AuthenticationException e) {
        return new AuthenticationErrorResponse(
                HttpStatus.UNAUTHORIZED.toString(), e.getMessage()
        );
    }
}
