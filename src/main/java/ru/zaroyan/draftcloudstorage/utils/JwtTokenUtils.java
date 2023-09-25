package ru.zaroyan.draftcloudstorage.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author Zaroyan
 */
@Component
@Slf4j
public class JwtTokenUtils {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.lifetime}")
    private int jwtLifetime;

    public String generateToken(UserDetails userDetails) {
        String username = userDetails.getUsername();
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(jwtLifetime).toInstant());

        return JWT.create()
                .withSubject("User details")
                .withClaim("login", username)
                .withIssuedAt(new Date())
                .withIssuer("zaroyan")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        log.info(token);
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("zaroyan")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        String login = jwt.getClaim("login").asString();
        log.info("OK " + login);
        return jwt.getClaim("login").asString();
    }
}



//    public String generateToken(UserDetails userDetails) {
//        Map<String, Object> claims = new HashMap<>();
//        List<String> rolesList = userDetails.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());
//        claims.put("roles", rolesList);
//        Date issuedDate = new Date();
//        Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime.toMillis());
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(userDetails.getUsername())
//                .setIssuedAt(issuedDate)
//                .setExpiration(expiredDate)
//                .signWith(SignatureAlgorithm.ES256, secret)
//                .compact();
//    }
//
//    public String getUsername(String token) {
//        return getAllClaimsFromToken(token).getSubject();
//    }
//
//    public List<String> getRoles(String token) {
//        return getAllClaimsFromToken(token).get("roles", List.class);
//    }
//
//    private Claims getAllClaimsFromToken(String token) {
//        return Jwts.parser()
//                .setSigningKey(secret)
//                .parseClaimsJws(token)
//                .getBody();
//    }

