package app.magicphoto.authservice.error.validation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Ошибка валидации, содержащая имя поля/сообщение о нарушении.")
public class Violation {
    private final String fieldName;
    private final String message;
}
