package ru.zaroyan.draftcloudstorage.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.zaroyan.draftcloudstorage.dto.AuthenticationDTO;
import ru.zaroyan.draftcloudstorage.dto.JWTResponse;
import ru.zaroyan.draftcloudstorage.models.UserEntity;
import ru.zaroyan.draftcloudstorage.services.AuthService;
import ru.zaroyan.draftcloudstorage.services.RegistrationService;
import ru.zaroyan.draftcloudstorage.utils.JwtTokenUtils;
import ru.zaroyan.draftcloudstorage.utils.UserValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

/**
 * @author Zaroyan
 */
@RestController
@AllArgsConstructor
@Data
@Slf4j
public class AuthController {
    private final AuthService authService;


//    @PostMapping("/registration")
//    public ResponseEntity<Object> performRegistration(@RequestBody @Valid AuthenticationDTO userDto, BindingResult bindingResult) {
//        UserEntity user = convertToUser(userDto);
//        userValidator.validate(user, bindingResult);
//        if(bindingResult.hasErrors())
//            return new ResponseEntity<>("Что-то пошло не так",
//                    HttpStatus.INTERNAL_SERVER_ERROR);
//        registrationService.register(user);
//        String token = jwtTokenUtils.generateToken(user.getUsername());
//        return ResponseEntity.ok(new JWTResponse(token));
//    }

//    @PostMapping("/login")
//    public ResponseEntity<?> performLogin(@RequestBody UserDto userDto) {
//        log.info("Вызван метод performLogin");
//        JWTResponse jwtResponse = authService.performLogin(userDto);
//        return ResponseEntity.ok(jwtResponse);
//    }


//    @PostMapping("/login")
//    public Map<String, String> performLogin(@RequestBody AuthenticationDTO authenticationDTO) {
//        UsernamePasswordAuthenticationToken authInputToken =
//                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
//                        authenticationDTO.getPassword());
//
//        try {
//            authenticationManager.authenticate(authInputToken);
//        } catch (BadCredentialsException e) {
//            return Map.of("message", "Incorrect credentials!");
//        }
//
//        String token = jwtTokenUtils.generateToken(authenticationDTO.getUsername());
//        return Map.of("jwt-token", token);
//    }
    @PostMapping("/login")
    public ResponseEntity<?> authenticationLogin(@RequestBody AuthenticationDTO userDto) {
        log.info("Пользователь пытается войти в систему: {}", userDto);
        JWTResponse token = authService.performLogin(userDto);
        log.info("Пользователь: {} успешно вошел в систему. Auth-token: {}", userDto, token.getAuthToken());
        return ResponseEntity.ok(token);
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


//    public UserEntity convertToUser(AuthenticationDTO authenticationDTO) {
//        return this.modelMapper.map(authenticationDTO, UserEntity.class);
//    }
}




