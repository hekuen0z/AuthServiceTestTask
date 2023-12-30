package app.magicphoto.authservice;

import app.magicphoto.authservice.model.dao.Role;
import app.magicphoto.authservice.repository.RoleRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Authentication Service Test Tack",
                description = "Сервис реализует регистрацию, аутентификацию и авторизацию пользователя",
                version = "1.0.0",
                contact = @Contact(
                        name = "Alexey Kaptur",
                        email = "kaptur.swdev@gmail.com"
                )
        )
)
public class AuthServiceTestTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceTestTaskApplication.class, args);
    }

    @Bean
    DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .build();
    }

    @Bean
    CommandLineRunner demoData(RoleRepository roleRepo) {
        return (args) -> {
            Role roleUser = new Role("ROLE_USER");
            roleRepo.save(roleUser);
            Role roleAdmin = new Role("ROLE_ADMIN");
            roleRepo.save(roleAdmin);
        };

    }

}
