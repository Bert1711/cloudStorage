package ru.zaroyan.draftcloudstorage.controllers;

import lombok.RequiredArgsConstructor;
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
import ru.zaroyan.draftcloudstorage.dto.UserDto;
import ru.zaroyan.draftcloudstorage.models.UserEntity;
import ru.zaroyan.draftcloudstorage.services.AuthService;
import ru.zaroyan.draftcloudstorage.services.RegistrationService;
import ru.zaroyan.draftcloudstorage.services.UserDetailsServiceImpl;
import ru.zaroyan.draftcloudstorage.utils.JwtTokenUtils;
import ru.zaroyan.draftcloudstorage.utils.UserValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author Zaroyan
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")

public class AuthController {
    private final AuthService authService;
    private final UserDetailsServiceImpl userEntityDetailsService;
    private final RegistrationService registrationService;
    private final JwtTokenUtils jwtTokenUtils;

    private final UserValidator userValidator;
    private final ModelMapper modelMapper;

    @PostMapping("/registration")
    public ResponseEntity<Object> performRegistration(@RequestBody @Valid UserDto userDto, BindingResult bindingResult) {
        UserEntity user = convertToUser(userDto);
        userValidator.validate(user, bindingResult);
        if(bindingResult.hasErrors())
            return new ResponseEntity<>("Что-то пошло не так",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        registrationService.register(user);
        String token = jwtTokenUtils.generateToken(user.getUsername());
        return ResponseEntity.ok(new JWTResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> performLogin(@RequestBody AuthenticationDTO authenticationDTO) {
        authService.performLogin(authenticationDTO);
        return ResponseEntity.ok(authService.performLogin(authenticationDTO));
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


    public UserEntity convertToUser(UserDto userDto) {
        return this.modelMapper.map(userDto, UserEntity.class);
    }
}
