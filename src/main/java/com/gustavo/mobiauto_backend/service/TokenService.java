package com.gustavo.mobiauto_backend.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.gustavo.mobiauto_backend.model.user.User;
import com.gustavo.mobiauto_backend.service.exceptions.TokenGenerationException;

@Service
public class TokenService {
    @Value("${mobiauto.jwt.secret}")
    private String secret;

    private static final long TWELVE_HOURS_IN_MILLISECONDS = 43200000;

    public String generateToken(User user) {
        try {
            var now = new Date();
            var twelveHoursFromNow = new Date(now.getTime() + TWELVE_HOURS_IN_MILLISECONDS);

            return JWT.create()
                    .withIssuer("mobiauto-auth-api")
                    .withSubject(user.getEmail().getValue())
                    .withIssuedAt(now)
                    .withExpiresAt(twelveHoursFromNow)
                    .sign(Algorithm.HMAC256(secret));
        } catch (JWTCreationException exception) {
            throw new TokenGenerationException(user, exception);
        }
    }

    public String validateToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer("mobiauto-auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }
}
