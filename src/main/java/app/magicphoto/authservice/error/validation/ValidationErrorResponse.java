package app.magicphoto.authservice.error.validation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "Объект для предоставления списка ошибок валидации")
public class ValidationErrorResponse {

    private List<Violation> violations;
}
