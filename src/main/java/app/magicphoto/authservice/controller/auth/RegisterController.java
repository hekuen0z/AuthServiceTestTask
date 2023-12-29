package app.magicphoto.authservice.controller.auth;

import app.magicphoto.authservice.dto.UserDTO;
import app.magicphoto.authservice.dto.mapper.CustomUserAndUserDtoMapper;
import app.magicphoto.authservice.model.CustomUser;
import app.magicphoto.authservice.service.RoleService;
import app.magicphoto.authservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@Slf4j
@RestController
@RequestMapping(value = "/api/register", headers = "accept=application/json")
@Tag(name = "Контроллер для регистрации пользователя")
public class RegisterController {


    private final RoleService roleService;
    private final UserService userService;
    private final CustomUserAndUserDtoMapper mapper;

    @Autowired
    public RegisterController(RoleService roleService, UserService userService,
                              CustomUserAndUserDtoMapper mapper) {
        this.roleService = roleService;
        this.userService = userService;
        this.mapper = mapper;
    }

    @Operation(summary = "Метод регистрации пользователя.",
            description = "Принимает данные пользователя в формате JSON и" +
                    "возвращает статус OK при предоставлении корректных данных и успешного добавления в БД." +
                    " При возникновении ошибок отправляет AuthenticationErrorResponse/ValidationErrorResponse.")
    @PostMapping
    public ResponseEntity<String> registerUser(
            @Validated@RequestBody @Parameter(description = "SignUpDTO для создания пользователя в системе") UserDTO userDTO) {

        if(userService.existsByLogin(userDTO.getLogin())) {
            log.error("Specified login is already exist: " + userDTO.getLogin());
            throw new BadCredentialsException("Specified login is already exist!");
        }
        if(userService.existsByAccessCode(userDTO.getAccessCode())) {
            log.error("Access code is already exist: " + userDTO.getAccessCode());
            throw new BadCredentialsException("Access code is already exist!");
        }

        CustomUser user = mapper.fromDto(userDTO);

        app.magicphoto.authservice.model.Role role =
                roleService.findByName("ROLE_USER");
        user.setRoles(Collections.singleton(role));

        userService.save(user);
        log.info("Registered user: " + user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
