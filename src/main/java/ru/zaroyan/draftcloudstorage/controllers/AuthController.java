package ru.zaroyan.draftcloudstorage.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import ru.zaroyan.draftcloudstorage.dto.AuthenticationDTO;
import ru.zaroyan.draftcloudstorage.dto.JWTResponse;
import ru.zaroyan.draftcloudstorage.models.UserEntity;
import ru.zaroyan.draftcloudstorage.services.RegistrationService;
import ru.zaroyan.draftcloudstorage.services.UserDetailsServiceImpl;
import ru.zaroyan.draftcloudstorage.utils.JwtTokenUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Zaroyan
 */
@RestController
@AllArgsConstructor
@Data
@Slf4j
public class AuthController {
    private final UserDetailsServiceImpl userDetailsService;
    private final RegistrationService registrationService;

    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(
            AuthenticationManager authenticationManager,
            JwtTokenUtils jwtTokenUtils,
            UserDetailsServiceImpl userDetailsService,
            RegistrationService registrationService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtils = jwtTokenUtils;
        this.userDetailsService = userDetailsService;
        this.registrationService = registrationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> performLogin(@RequestBody AuthenticationDTO userDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
            );

            // В этом месте аутентификация прошла успешно

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtTokenUtils.generateToken(userDetails);

            return ResponseEntity.ok(new JWTResponse(token));
        } catch (AuthenticationException e) {
            log.info("Ошибка аутентификации: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response,
                                         @RequestHeader("auth-token") String authToken) {
        String jwt = authToken.substring(7);

        if (jwt == null) {
            return new ResponseEntity<>("Что-то пошло не так",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return ResponseEntity.ok("Success logout");
    }


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AuthenticationDTO userRequest) {
        try {
            UserEntity newUser = UserEntity.builder()
                    .login(userRequest.getLogin())
                    .password(userRequest.getPassword())
                    .build();

            registrationService.register(newUser);
            return ResponseEntity.ok("Пользователь успешно зарегистрирован.");
        } catch (Exception e) {
            log.error("Ошибка при регистрации пользователя: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Произошла ошибка при регистрации пользователя.");
        }
    }
}




