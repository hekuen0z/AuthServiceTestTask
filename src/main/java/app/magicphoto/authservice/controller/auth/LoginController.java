package app.magicphoto.authservice.controller.auth;

import app.magicphoto.authservice.config.token.AccessCodeAuthenticationToken;
import app.magicphoto.authservice.dto.JwtResponse;
import app.magicphoto.authservice.dto.SignInWithCodeDTO;
import app.magicphoto.authservice.dto.SignInWithPasswordDTO;
import app.magicphoto.authservice.model.CustomUser;
import app.magicphoto.authservice.service.JwtService;
import app.magicphoto.authservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/login", headers = "accept=application/json")
@Tag(name = "Контроллер для аутентификации/авторизации пользователя")
public class LoginController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    public LoginController(AuthenticationManager authManager, JwtService jwtService, UserService userService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Operation(summary = "Метод аутентификации/авторизации пользователя по логину/паролю.",
    description = "Принимает логин/пароль в формате JSON и возвращает статус OK в случае успешной авторизации." +
            " При возникновении ошибок отправляет AuthenticationErrorResponse.")
    @PostMapping(value = "/password")
    public ResponseEntity<JwtResponse> authenticateUserWithPassword(
            @RequestBody @Parameter(description = "Логин/пароль в формате JSON") SignInWithPasswordDTO userDTO) {
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken
                .unauthenticated(userDTO.getLogin(), userDTO.getPassword());
        performManualAuthentication(token);


        log.info("Logged user: " + userDTO);
        return ResponseEntity.ok(createJwtResponse());
    }

    @Operation(summary = "Метод аутентификации/авторизации пользователя по коду доступа.",
            description = "Принимает код доступа в формате JSON и возвращает статус OK в случае успешной авторизации." +
                    " При возникновении ошибок отправляет AuthenticationErrorResponse.")
    @PostMapping(value = "/access_code")
    public ResponseEntity<JwtResponse> authenticateUserWithAccessCode(
            @RequestBody @Parameter(description = "Пользовательский код доступа в формате JSON") SignInWithCodeDTO userDTO) {
        AccessCodeAuthenticationToken token = AccessCodeAuthenticationToken
                .unauthenticated(null, userDTO.getAccessCode());
        performManualAuthentication(token);

        log.info("Logged user: " + userDTO);
        return ResponseEntity.ok(createJwtResponse());
    }

    private void performManualAuthentication(Authentication token) {
        Authentication authUser = authManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authUser);

        log.info(String.format("Set SecurityContextHolder to %s", authUser));
    }

    private JwtResponse createJwtResponse() {
        CustomUser user = userService.findAuthenticatedUser(SecurityContextHolder.getContext().getAuthentication());
        String jwtToken = jwtService.generateToken(user);

        return new JwtResponse(jwtToken, jwtService.getJwtExpirationTime());
    }
}
