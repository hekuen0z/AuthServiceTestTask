package app.magicphoto.authservice.service;

import app.magicphoto.authservice.model.Role;
import app.magicphoto.authservice.repository.RoleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
public class RoleServiceTest {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepo;

    @BeforeEach
    void setUp() {
        roleRepo.deleteAll();
        Role role1 = new Role("ROLE_USER");
        roleRepo.save(role1);
    }

    @Test
    void RoleServiceContextLoads() {
        Assertions.assertThat(roleService).isNotNull();
    }

    @Test
    void shouldReturnRoleByExistingRoleName() {
        Role role = roleService.findByName("ROLE_USER");

        assertNotNull(role);
    }

    @Test
    void shouldNotReturnRoleByNonexistentRoleName() {
        Role role = roleService.findByName("ROLE_POLZOVATEL");

        assertNull(role);
    }
}
