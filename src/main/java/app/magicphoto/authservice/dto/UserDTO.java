package app.magicphoto.authservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Сущность пользователя")
public class UserDTO {

    @NotBlank
    @Size(min = 3, max = 50, message = "login must be from 3 to 50 characters length inclusive!")
    private String login;

    @NotBlank
    @Size(min = 8, message = "Password must be 8 characters or more!")
    private String password;

    @NotBlank
    @Size(min = 8, message = "Access code must be 8 characters or more!")
    private String accessCode;
}
