package app.magicphoto.authservice.model.dto.mapper;

import app.magicphoto.authservice.model.dao.CustomUser;
import app.magicphoto.authservice.model.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserAndUserDtoMapper {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomUserAndUserDtoMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO toDto(CustomUser user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setLogin(user.getLogin());
        userDTO.setPassword(user.getPassword());
        userDTO.setAccessCode(user.getAccessCode());

        return userDTO;
    }

    public CustomUser fromDto(UserDTO userDTO) {
        CustomUser user = new CustomUser();

        user.setLogin(userDTO.getLogin());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setAccessCode(passwordEncoder.encode(userDTO.getAccessCode()));

        return user;
    }

}
