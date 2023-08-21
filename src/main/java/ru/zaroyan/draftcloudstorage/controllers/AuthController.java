package ru.zaroyan.draftcloudstorage.controllers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.zaroyan.draftcloudstorage.dto.JwtRequest;
import ru.zaroyan.draftcloudstorage.dto.JwtResponse;
import ru.zaroyan.draftcloudstorage.dto.UserDto;
import ru.zaroyan.draftcloudstorage.exceptions.AppError;
import ru.zaroyan.draftcloudstorage.models.UserEntity;
import ru.zaroyan.draftcloudstorage.security.UserEntityDetails;
import ru.zaroyan.draftcloudstorage.services.RegistrationService;
import ru.zaroyan.draftcloudstorage.services.UserEntityDetailsService;
import ru.zaroyan.draftcloudstorage.utils.JwtTokenUtils;
import ru.zaroyan.draftcloudstorage.utils.UserValidator;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author Zaroyan
 */
@RestController
@RequiredArgsConstructor

public class AuthController {
    private final UserEntityDetailsService userEntityDetailsService;
    private final RegistrationService registrationService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final UserValidator userValidator;
    private final ModelMapper modelMapper;

    @PostMapping("/registration")
    public Map<String, String> performRegistration(@RequestBody @Valid UserDto userDto, BindingResult bindingResult) {
        UserEntity user = convertToUser(userDto);
        userValidator.validate(user, bindingResult);
        if(bindingResult.hasErrors())
            return Map.of("message", "Ошибка!");
        registrationService.register(user);
        String token = jwtTokenUtils.generateToken(user.getUsername());
        return Map.of("jwt-token", token);
    }

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(
                    new AppError(
                            HttpStatus.UNAUTHORIZED.value(),
                            "Неправильный логин или пароль"),
                    HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userEntityDetailsService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public UserEntity convertToUser(UserDto userDto) {
        return this.modelMapper.map(userDto, UserEntity.class);
    }
}
