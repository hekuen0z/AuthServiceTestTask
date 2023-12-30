package app.magicphoto.authservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Пользовательский код доступа")
public class SignInWithCodeDTO {

    @NotBlank(message = "Access code cannot be blank!")
    @Size(min = 8, message = "Access code must be 8 characters or more!")
    private String accessCode;

}
