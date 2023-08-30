package ru.zaroyan.draftcloudstorage.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.zaroyan.draftcloudstorage.dto.AuthenticationDTO;
import ru.zaroyan.draftcloudstorage.dto.JWTResponse;
import ru.zaroyan.draftcloudstorage.services.AuthService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Zaroyan
 */


@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @Mock
    private AuthService authService;
    private MockMvc mockMvc;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testCreateAuthToken() throws Exception {
        AuthenticationDTO authRequest = new AuthenticationDTO("username", "password");
        JWTResponse jwtResponse = new JWTResponse("token");

        when(authService.performLogin(authRequest)).thenReturn(jwtResponse);

        mockMvc.perform(post("/auth/login")
                        .content(objectMapper.writeValueAsString(authRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(authService, times(1)).performLogin(authRequest);
    }
}