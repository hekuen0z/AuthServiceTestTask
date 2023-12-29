package app.magicphoto.authservice.controller.error;

import app.magicphoto.authservice.error.security.AuthenticationErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
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
    @Operation(summary = "Метод для обработки AuthenticationErrorResponse.")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public AuthenticationErrorResponse onAuthenticationException(AuthenticationException e) {
        return new AuthenticationErrorResponse(
                HttpStatus.UNAUTHORIZED.toString(), e.getMessage()
        );
    }

    @ResponseBody
    @ExceptionHandler(JwtException.class)
    @Operation(summary = "Метод для обработки JwtException.")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public AuthenticationErrorResponse onJwtValidationException(JwtException e) {
        AuthenticationErrorResponse response = null;

        if(e instanceof SignatureException) {
            response = new AuthenticationErrorResponse(
                    HttpStatus.UNAUTHORIZED.toString(), "The JWT Signature is invalid");
        }
        if(e instanceof ExpiredJwtException) {
            response = new AuthenticationErrorResponse(
                    HttpStatus.UNAUTHORIZED.toString(), "The JWT token has expired");
        }

        if(response == null) {
            response = new AuthenticationErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                    "Unknown internal JWT service error.");
        }

        return response;
    }
}
