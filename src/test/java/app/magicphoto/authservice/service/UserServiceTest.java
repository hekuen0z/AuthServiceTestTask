package app.magicphoto.authservice.service;

import app.magicphoto.authservice.model.CustomUser;
import app.magicphoto.authservice.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@AutoConfigureTestDatabase
public class UserServiceTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepo;

    @BeforeEach
    void setUp() {
        CustomUser user1 = new CustomUser();
        user1.setId(1L);
        user1.setLogin("testuser");
        user1.setPassword(passwordEncoder.encode("password123"));
        user1.setAccessCode(passwordEncoder.encode("accessCode123"));
        userRepo.save(user1);

        CustomUser user2 = new CustomUser();
        user2.setId(2L);
        user2.setLogin("testuser2");
        user2.setPassword(passwordEncoder.encode("password123"));
        user2.setAccessCode(passwordEncoder.encode("accessCode456"));
        userRepo.save(user2);
    }

    @Test
    void UserServiceContextLoads() {
        Assertions.assertThat(userService).isNotNull();
    }

    @Test
    void shouldReturnUserByExistingLogin() {
        Optional<CustomUser> user = userService.findUserByLogin("testuser");

        assertTrue(user.isPresent());
    }

    @Test
    void shouldNotReturnUserByNonexistentLogin() {
        Optional<CustomUser> user = userService.findUserByLogin("no_user");

        assertTrue(user.isEmpty());
    }

    @Test
    void shouldReturnUserByExistingId() {
        Optional<CustomUser> user = userService.findUserById(2L);

        assertTrue(user.isPresent());
    }

    @Test
    void shouldNotReturnUserByWrongId() {
        Optional<CustomUser> user = userService.findUserById(3L);

        assertTrue(user.isEmpty());
    }

    @Test
    void shouldReturnUserByExistingAccessCode() {
        Optional<CustomUser> user = userService.findUserByAccessToken("accessCode123");

        assertTrue(user.isPresent());
    }

    @Test
    void shouldNotReturnUserByNonexistentAccessCode() {
        Optional<CustomUser> user = userService.findUserByAccessToken("noValidToken");

        assertTrue(user.isEmpty());
    }
}
