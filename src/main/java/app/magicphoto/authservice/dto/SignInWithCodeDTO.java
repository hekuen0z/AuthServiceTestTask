package app.magicphoto.authservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Пользовательский код доступа")
public class SignInWithCodeDTO {

    private String accessCode;

}
