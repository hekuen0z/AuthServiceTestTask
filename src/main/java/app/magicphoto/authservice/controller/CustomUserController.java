package app.magicphoto.authservice.controller;

import app.magicphoto.authservice.dto.IdDTO;
import app.magicphoto.authservice.dto.UserDTO;
import app.magicphoto.authservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/info", headers = "accept=application/json")
@Tag(name = "Контроллер предоставления сведений о текущем пользователе")
public class CustomUserController {

    private final UserService userService;

    @Autowired
    public CustomUserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Возвращает уникальный идентификатор авторизированного пользователя")
    @GetMapping
    public IdDTO viewInfoAboutUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        Long id = userService.findAuthenticatedUser(auth).getId();

        return new IdDTO(id);
    }

    @Operation(summary = "Изменяет данные авторизованного пользователя",
    description = "Заменяет каждое поле у авторизированного пользователя и сохраняет изменения в БД")
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeUserData(@Valid @RequestBody UserDTO userDTO) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = userService.findAuthenticatedUser(auth);

        currentUser.setLogin(userDTO.getLogin());
        currentUser.setPassword(userDTO.getPassword());
        currentUser.setAccessCode(userDTO.getAccessCode());

        userService.save(currentUser);
    }

    @Operation(summary = "Удаление авторизированного пользователя",
    description = "Метод выполняет удаление авторизованного пользователя из БД, а также удаляет данные о нем из контекста авторизации.")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = userService.findAuthenticatedUser(auth).getId();

        userService.deleteUserById(currentUserId);

        SecurityContextHolder.getContext().setAuthentication(null);
    }

}
