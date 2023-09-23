package ru.zaroyan.draftcloudstorage.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.zaroyan.draftcloudstorage.dto.AuthenticationDTO;
import ru.zaroyan.draftcloudstorage.dto.JWTResponse;
import ru.zaroyan.draftcloudstorage.security.UserEntityDetails;
import ru.zaroyan.draftcloudstorage.utils.JwtTokenUtils;

/**
 * @author Zaroyan
 */
@Service
@Slf4j
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JwtTokenUtils jwtTokenUtils, UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtils = jwtTokenUtils;
        this.userDetailsService = userDetailsService;
    }

    public JWTResponse performLogin(AuthenticationDTO userDto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDto.getUsername(), userDto.getPassword()));
        UserEntityDetails userDetails = (UserEntityDetails) userDetailsService.loadUserByUsername(userDto.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);
        return new JWTResponse(token);
    }
}
