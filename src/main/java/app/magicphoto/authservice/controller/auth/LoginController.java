package app.magicphoto.authservice.controller.auth;

import app.magicphoto.authservice.model.dto.JwtResponse;
import app.magicphoto.authservice.model.dto.SignInWithCodeDTO;
import app.magicphoto.authservice.model.dto.SignInWithPasswordDTO;
import app.magicphoto.authservice.security.token.AccessCodeAuthenticationToken;
import app.magicphoto.authservice.service.JwtService;
import app.magicphoto.authservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/login", headers = "accept=application/json")
@Tag(name = "Контроллер для аутентификации/авторизации пользователя")
public class LoginController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserService userService;

    @Operation(summary = "Метод аутентификации/авторизации пользователя по логину/паролю.",
            description = "Принимает логин/пароль в формате JSON и возвращает статус OK в случае успешной авторизации." +
                    " При возникновении ошибок отправляет AuthenticationErrorResponse.")
    @PostMapping(value = "/password")
    public ResponseEntity<JwtResponse> authenticateUserWithPassword(@Validated @RequestBody SignInWithPasswordDTO userDTO) {
        var token = UsernamePasswordAuthenticationToken.unauthenticated(userDTO.getLogin(), userDTO.getPassword());
        performManualAuthentication(token);

        log.info("User authenticated successfully with login/password. Timestamp: " + LocalDateTime.now());
        return ResponseEntity.ok(createJwtResponse());
    }

    @Operation(summary = "Метод аутентификации/авторизации пользователя по коду доступа.",
            description = "Принимает код доступа в формате JSON и возвращает статус OK в случае успешной авторизации." +
                    " При возникновении ошибок отправляет AuthenticationErrorResponse.")
    @PostMapping(value = "/access_code")
    public ResponseEntity<JwtResponse> authenticateUserWithAccessCode(@Validated @RequestBody SignInWithCodeDTO userDTO) {

        AccessCodeAuthenticationToken token=AccessCodeAuthenticationToken.unauthenticated(Optional.empty(),
                                                                                          userDTO.getAccessCode());
        performManualAuthentication(token);

        log.info("User authenticated successfully with access code.");
        return ResponseEntity.ok(createJwtResponse());
    }


    private void performManualAuthentication(Authentication token) {
        Authentication authUser = authManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authUser);

        log.info("Set SecurityContextHolder to {}", authUser);
    }

    /**
     * Creates a JWT response.
     *
     * @return Returns a JwtResponse object containing the generated JWT token and the JWT expiration time.
     */
    private JwtResponse createJwtResponse() {
        var user = userService.findAuthenticatedUser(SecurityContextHolder.getContext().getAuthentication());
        String jwtToken = jwtService.generateToken(user);

        log.info("Generated JWT token: {}", jwtToken);
        return new JwtResponse(jwtToken, jwtService.getJwtExpirationTime());
    }
}
