package app.magicphoto.authservice.error.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Сущность ошибок аутентификации")
public class AuthenticationErrorResponse {

    private final String httpCode;
    private final String message;
}
