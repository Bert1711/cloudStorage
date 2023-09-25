package ru.zaroyan.draftcloudstorage.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.zaroyan.draftcloudstorage.services.UserDetailsServiceImpl;
import ru.zaroyan.draftcloudstorage.utils.JwtTokenUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Zaroyan
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsServiceImpl userEntityDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        log.info("вошли в фильтр JwtRequestFilter");
        String authHeader = httpServletRequest.getHeader("auth-token");

        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            if (jwt.isBlank()) {
                log.info("Неверный токен JWT в заголовке носителя");
                httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid JWT Token in Bearer Header");
            } else {
                try {
                    log.info("Validate jwt");
                    String username = jwtTokenUtils.validateTokenAndRetrieveClaim(jwt);
                    UserDetails userDetails = userEntityDetailsService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails,
                                    userDetails.getPassword(),
                                    userDetails.getAuthorities());

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        log.info("setAuthentication authToken");
                    }
                } catch (JWTVerificationException exc) {
                    log.info("Invalid JWT Token");
                    httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid JWT Token");
                }
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
        log.info("вышли из фильтра JwtRequestFilter");
    }
}

