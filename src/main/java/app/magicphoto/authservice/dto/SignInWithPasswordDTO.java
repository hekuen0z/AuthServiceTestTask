package app.magicphoto.authservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Учетные данные пользователя: логин/пароль")
public class SignInWithPasswordDTO {

    private String login;

    private String password;
}
