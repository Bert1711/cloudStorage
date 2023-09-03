package ru.zaroyan.draftcloudstorage.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.zaroyan.draftcloudstorage.dto.JWTResponse;
import ru.zaroyan.draftcloudstorage.dto.UserDto;
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

    public JWTResponse performLogin(UserDto userDto) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(userDto.getUsername(),
                        userDto.getPassword());
        authenticationManager.authenticate(authInputToken);
        String token = jwtTokenUtils.generateToken(userDto.getUsername());
        return new JWTResponse(token);
    }
}
