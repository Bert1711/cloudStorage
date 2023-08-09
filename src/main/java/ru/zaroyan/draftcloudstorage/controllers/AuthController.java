package ru.zaroyan.draftcloudstorage.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.zaroyan.draftcloudstorage.dto.JwtRequest;
import ru.zaroyan.draftcloudstorage.dto.JwtResponse;
import ru.zaroyan.draftcloudstorage.exceptions.AppError;
import ru.zaroyan.draftcloudstorage.security.UserEntityDetails;
import ru.zaroyan.draftcloudstorage.services.UserEntityDetailsService;
import ru.zaroyan.draftcloudstorage.utils.JwtTokenUtils;

/**
 * @author Zaroyan
 */
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserEntityDetailsService userEntityDetailsService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

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
}
