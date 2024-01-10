package app.magicphoto.authservice.error.validation;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ошибка валидации, содержащая имя поля/сообщение о нарушении.")
public record Violation(String fieldName, String message) {
}
