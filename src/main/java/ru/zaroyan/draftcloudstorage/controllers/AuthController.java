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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.zaroyan.draftcloudstorage.dto.AuthenticationDTO;
import ru.zaroyan.draftcloudstorage.dto.JWTResponse;
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

    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(
            AuthenticationManager authenticationManager,
            JwtTokenUtils jwtTokenUtils,
            UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtils = jwtTokenUtils;
        this.userDetailsService = userDetailsService;
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
}




