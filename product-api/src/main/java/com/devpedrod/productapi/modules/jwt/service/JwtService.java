package com.devpedrod.productapi.modules.jwt.service;

import com.devpedrod.productapi.modules.jwt.DTO.JwtResponse;
import com.devpedrod.productapi.modules.shared.exceptions.AuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import static org.springframework.util.ObjectUtils.*;

@Service
public class JwtService {

    private static final String EMPTY_SPACE = " ";
    private static final Integer TOKEN_INDEX = 1;

    @Value("${app-config.secrets.api-secret}")
    private String apiSecret;

    public void validateAuthorization(String token) {
        var accessToken = extractToken(token);
        try {
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(apiSecret.getBytes()))
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
            var user = JwtResponse.getUser(claims);
            if (isEmpty(user) || isEmpty(user.getId())) {
                throw new AuthenticationException("The user is not valid.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new AuthenticationException("Error while trying to proccess the Access Token.");
        }
    }

    private String extractToken(String token) {
        if (isEmpty(token)) {
            throw new AuthenticationException("The access token was not informed.");
        }
        if (token.contains(EMPTY_SPACE)) {
            return token.split(EMPTY_SPACE)[TOKEN_INDEX];
        }
        return token;
    }
}
