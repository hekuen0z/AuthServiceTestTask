package app.magicphoto.authservice.controller;

import app.magicphoto.authservice.controller.auth.RegisterController;
import app.magicphoto.authservice.model.dto.UserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RegisterController controller;

    @Test
    void registerControllerContextLoads() throws Exception {
        Assertions.assertThat(controller).isNotNull();
    }

    @Test
    void returnBadRequestWhenProvideNotValidLogin() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("uu");
        userDTO.setPassword("password123");
        userDTO.setAccessCode("1i2312893u12913");

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString(userDTO))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
    }

    @Test
    void returnBadRequestWhenProvideNotValidPassword() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("testuser");
        userDTO.setPassword("pass");
        userDTO.setAccessCode("1i2312893u12913");

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString(userDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnBadRequestWhenProvideNotValidAccessCode() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("testuser");
        userDTO.setPassword("password123");
        userDTO.setAccessCode("123");

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString(userDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnCreatedWhenProvideValidUserData() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("testuser");
        userDTO.setPassword("password123");
        userDTO.setAccessCode("1312o312-u12903");

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString(userDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    private String jsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
