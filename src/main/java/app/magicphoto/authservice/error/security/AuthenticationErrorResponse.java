package app.magicphoto.authservice.error.security;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Сущность ошибок аутентификации")
public record AuthenticationErrorResponse(Integer httpCode, String message) {}
