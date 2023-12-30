package app.magicphoto.authservice.controller;

import static org.mockito.Mockito.when;

import app.magicphoto.authservice.model.dao.CustomUser;
import app.magicphoto.authservice.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {CustomUserController.class})
@ExtendWith(SpringExtension.class)
class CustomUserControllerDiffblueTest {
    @Autowired
    private CustomUserController customUserController;

    @MockBean
    private UserService userService;

    /**
     * Method under test: {@link CustomUserController#deleteUser()}
     */
    @Test
    void testDeleteUser() throws Exception {
        // Arrange
        CustomUser customUser = new CustomUser();
        customUser.setAccessCode("Access Code");
        customUser.setId(1L);
        customUser.setLogin("Login");
        customUser.setPassword("iloveyou");
        customUser.setRoleSet(new HashSet<>());
        customUser.setRoles(new ArrayList<>());
        when(userService.findAuthenticatedUser(Mockito.any())).thenReturn(customUser);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/info");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(customUserController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":1}"));
    }

    /**
     * Method under test: {@link CustomUserController#deleteUser()}
     */
    @Test
    void testDeleteUser2() throws Exception {
        // Arrange
        CustomUser customUser = new CustomUser();
        customUser.setAccessCode("Access Code");
        customUser.setId(1L);
        customUser.setLogin("Login");
        customUser.setPassword("iloveyou");
        customUser.setRoleSet(new HashSet<>());
        customUser.setRoles(new ArrayList<>());
        when(userService.findAuthenticatedUser(Mockito.any())).thenReturn(customUser);
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders.formLogin();

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(customUserController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
