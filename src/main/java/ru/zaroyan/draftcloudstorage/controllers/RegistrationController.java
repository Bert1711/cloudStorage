package ru.zaroyan.draftcloudstorage.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.zaroyan.draftcloudstorage.dto.AuthenticationDTO;
import ru.zaroyan.draftcloudstorage.models.UserEntity;
import ru.zaroyan.draftcloudstorage.services.RegistrationService;

/**
 * @author Zaroyan
 */
@RestController
@Data
@Slf4j
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
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
