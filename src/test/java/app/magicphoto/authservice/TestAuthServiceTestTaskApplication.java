package app.magicphoto.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestAuthServiceTestTaskApplication {

    public static void main(String[] args) {
        SpringApplication.from(AuthServiceTestTaskApplication::main).with(TestAuthServiceTestTaskApplication.class).run(args);
    }

}
