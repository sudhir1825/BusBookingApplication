package com.example.BusBookingApplication.Controller;

import com.example.BusBookingApplication.DTO.UserDTO;
import com.example.BusBookingApplication.Service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void showRegisterForm_ShouldReturnRegisterView() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void processRegistration_WhenSuccess_ShouldRedirectToLoginWithSuccessMessage() throws Exception {
        doNothing().when(userService).registerUser(any(UserDTO.class));

        mockMvc.perform(post("/register")
                        .param("email", "test@example.com")
                        .param("password", "password123")
                        .param("name", "Test User"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("successMessage"));

        verify(userService, times(1)).registerUser(any(UserDTO.class));
    }

    @Test
    void processRegistration_WhenFailure_ShouldReturnRegisterViewWithError() throws Exception {
        doThrow(new RuntimeException("Email already exists")).when(userService).registerUser(any(UserDTO.class));

        mockMvc.perform(post("/register")
                        .param("email", "test@example.com")
                        .param("password", "password123")
                        .param("name", "Test User"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attributeExists("user"));

        verify(userService, times(1)).registerUser(any(UserDTO.class));
    }

    @Test
    void showLoginForm_ShouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void showHomePage_ShouldReturnHomeView() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }
}

