package com.pmb.PayMyBuddy.security.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.SignatureException;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${paymybuddy.app.jwtSecret}")
    private String jwtSecret;
    @Value("${paymybuddy.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * generate JWT
     * @param authentication
     * @return JWT
     */
    public String generateJwtToken(Authentication authentication) {
        logger.debug("generation of JWT");
        User userPrincipal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * get username from JWT
     * @param token JWT
     * @return username
     */
    public String getUserNameFromJwtToken(String token) {
        logger.debug("getting Username from JWT");
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * validate JWT
     * @param authToken
     * @return true is token is valid
     */
    public boolean validateJwtToken(String authToken) {
        logger.debug("validation of JWT");
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
