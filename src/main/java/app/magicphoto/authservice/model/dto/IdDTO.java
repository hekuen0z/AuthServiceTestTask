package app.magicphoto.authservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Идентификатор авторизированного пользователя")
public class IdDTO {

    private final Long Id;
}
