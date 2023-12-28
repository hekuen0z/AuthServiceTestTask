package app.magicphoto.authservice.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomUserController controller;

    @Test
    void loginControllerContextLoads() throws Exception {
        Assertions.assertThat(controller).isNotNull();
    }

    @Test
    void returnUnauthorizedWhenUserNotDontHaveJwtToken() throws Exception {
        this.mockMvc.perform(post("/api/info"))
                .andExpect(status().isUnauthorized());

        this.mockMvc.perform(get("/api/info"))
                .andExpect(status().isUnauthorized());

        this.mockMvc.perform(put("/api/info"))
                .andExpect(status().isUnauthorized());

        this.mockMvc.perform(delete("/api/info"))
                .andExpect(status().isUnauthorized());

        this.mockMvc.perform(patch("/api/info"))
                .andExpect(status().isUnauthorized());
    }

}
