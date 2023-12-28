package app.magicphoto.authservice.controller;

import app.magicphoto.authservice.config.token.AccessCodeAuthenticationToken;
import app.magicphoto.authservice.controller.auth.LoginController;
import app.magicphoto.authservice.dto.SignInWithCodeDTO;
import app.magicphoto.authservice.dto.SignInWithPasswordDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AuthenticationManager authManager;

    @Captor
    private ArgumentCaptor<Authentication> authenticationArgumentCaptor;

    @Autowired
    private LoginController controller;

    @Test
    void loginControllerContextLoads() throws Exception {
        Assertions.assertThat(controller).isNotNull();
    }

    @Test
    void returnBadRequestWhenUserNotProvideAnyDataOnPasswordPath() throws Exception {
        this.mockMvc.perform(post("/api/login/password"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnBadRequestWhenUserNotProvideAnyDataOnAccessCodePath() throws Exception {
        this.mockMvc.perform(post("/api/login/access_code"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnUnauthorizedWithNonexistentLogin() throws Exception {
        SignInWithPasswordDTO loginUser = new SignInWithPasswordDTO();
        loginUser.setLogin("unreal_user");
        loginUser.setPassword("password123");

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginUser.getLogin(),
                loginUser.getPassword());

        when(authManager.authenticate(token)).thenReturn(token);

        mockMvc.perform(post("/api/login/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString(loginUser))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void returnUnauthorizedWithNonexistentAccessCode() throws Exception {
        SignInWithCodeDTO accessCode = new SignInWithCodeDTO();
        accessCode.setAccessCode("12313123123");

        AccessCodeAuthenticationToken token = new AccessCodeAuthenticationToken(null, accessCode);

        when(authManager.authenticate(token)).thenReturn(token);

        mockMvc.perform(post("/api/login/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString(accessCode))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    private String jsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

}
