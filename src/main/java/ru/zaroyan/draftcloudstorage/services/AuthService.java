package ru.zaroyan.draftcloudstorage.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.zaroyan.draftcloudstorage.dto.AuthenticationDTO;
import ru.zaroyan.draftcloudstorage.dto.JWTResponse;
import ru.zaroyan.draftcloudstorage.exceptions.AuthenticationException;
import ru.zaroyan.draftcloudstorage.utils.JwtTokenUtils;

/**
 * @author Zaroyan
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;

    public JWTResponse performLogin(AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                        authenticationDTO.getPassword());

        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            log.error("Wrong password");
            throw new AuthenticationException("Bad credentials");
        }
        String token = jwtTokenUtils.generateToken(authenticationDTO.getUsername());
        return new JWTResponse(token);
    }
}
