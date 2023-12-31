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
import ru.zaroyan.draftcloudstorage.dto.UserDto;
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
        UserDto userDto = new UserDto();
        userDto.setUsername("user1");
        userDto.setPassword("password");
        JWTResponse jwtResponse = new JWTResponse("token");

        when(authService.performLogin(userDto)).thenReturn(jwtResponse);

        mockMvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(authService, times(1)).performLogin(userDto);
    }
}