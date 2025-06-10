package com.gustavo.mobiauto_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gustavo.mobiauto_backend.model.user.User;
import com.gustavo.mobiauto_backend.model.user.UserEmail;
import com.gustavo.mobiauto_backend.model.user.UserName;
import com.gustavo.mobiauto_backend.model.user.UserPassword;
import com.gustavo.mobiauto_backend.service.exceptions.TokenGenerationException;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("TokenService Tests")
class TokenServiceTest {

    @Autowired
    private TokenService tokenService;

    private User testUser;
    private String testSecret;
    private static final long TWELVE_HOURS_IN_MILLIS = 43200000L;
    private static final long FIXED_TIME_MILLIS = System.currentTimeMillis();

    @BeforeEach
    void setUp() {
        testUser = new User(
                new UserName("John", "Doe"),
                new UserEmail("john.doe@example.com"),
                new UserPassword("password123"));
        testUser.setId(1L);

        testSecret = "test-secret-key-for-jwt-token-generation";
        ReflectionTestUtils.setField(tokenService, "secret", testSecret);
    }

    @Test
    @DisplayName("Should generate valid JWT token")
    void shouldGenerateValidJwtToken() {
        String token = tokenService.generateToken(testUser);

        assertNotNull(token);
        assertTrue(!token.isEmpty());

        assertEquals(testUser.getEmail().getValue(), tokenService.validateToken(token));
    }

    @Test
    @DisplayName("Should generate token with correct claims")
    void shouldGenerateTokenWithCorrectClaims() {
        String token = tokenService.generateToken(testUser);

        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(testSecret))
                .withIssuer("mobiauto-auth-api")
                .build()
                .verify(token);

        assertEquals("mobiauto-auth-api", decodedJWT.getIssuer());
        assertEquals(testUser.getEmail().getValue(), decodedJWT.getSubject());

        long issueTime = decodedJWT.getIssuedAt().getTime();
        long currentTime = System.currentTimeMillis();
        long timeDifference = Math.abs(currentTime - issueTime);
        assertTrue(timeDifference <= 5000);

        long expirationTime = decodedJWT.getExpiresAt().getTime();
        assertNotNull(decodedJWT.getExpiresAt());
        assertEquals(issueTime + TWELVE_HOURS_IN_MILLIS, expirationTime);
    }

    @Test
    @DisplayName("Should generate tokens with same subject for same user")
    void shouldGenerateTokensWithSameSubjectForSameUser() {
        String token1 = tokenService.generateToken(testUser);
        String token2 = tokenService.generateToken(testUser);

        DecodedJWT decodedJWT1 = JWT.decode(token1);
        DecodedJWT decodedJWT2 = JWT.decode(token2);

        assertEquals(decodedJWT1.getSubject(), decodedJWT2.getSubject());
        assertEquals(testUser.getEmail().getValue(), decodedJWT1.getSubject());
        assertEquals(testUser.getEmail().getValue(), decodedJWT2.getSubject());
    }

    @Test
    @DisplayName("Should validate valid token and return subject")
    void shouldValidateValidTokenAndReturnSubject() {
        String token = tokenService.generateToken(testUser);

        String subject = tokenService.validateToken(token);

        assertNotNull(subject);
        assertEquals(testUser.getEmail().getValue(), subject);
    }

    @Test
    @DisplayName("Should return null for invalid token")
    void shouldReturnNullForInvalidToken() {
        String invalidToken = "invalid.jwt.token";

        String subject = tokenService.validateToken(invalidToken);

        assertNull(subject);
    }

    @Test
    @DisplayName("Should return null for malformed token")
    void shouldReturnNullForMalformedToken() {
        String malformedToken = "not-a-jwt-token";

        String subject = tokenService.validateToken(malformedToken);

        assertNull(subject);
    }

    @Test
    @DisplayName("Should return null for expired token")
    void shouldReturnNullForExpiredToken() {
        long twoHoursInMillis = 7200000L;
        long pastTime = FIXED_TIME_MILLIS - twoHoursInMillis;
        Date pastDate = new Date(pastTime);
        Date veryPastDate = new Date(pastTime - TWELVE_HOURS_IN_MILLIS);

        String expiredToken = JWT.create()
                .withIssuer("mobiauto-auth-api")
                .withSubject(testUser.getEmail().getValue())
                .withIssuedAt(veryPastDate)
                .withExpiresAt(pastDate)
                .sign(Algorithm.HMAC256(testSecret));

        String subject = tokenService.validateToken(expiredToken);

        assertNull(subject);
    }

    @Test
    @DisplayName("Should return null for token with wrong issuer")
    void shouldReturnNullForTokenWithWrongIssuer() {
        Date fixedTime = new Date(FIXED_TIME_MILLIS);
        Date fixedExpiration = new Date(FIXED_TIME_MILLIS + TWELVE_HOURS_IN_MILLIS);

        String tokenWithWrongIssuer = JWT.create()
                .withIssuer("wrong-issuer")
                .withSubject(testUser.getEmail().getValue())
                .withIssuedAt(fixedTime)
                .withExpiresAt(fixedExpiration)
                .sign(Algorithm.HMAC256(testSecret));

        String subject = tokenService.validateToken(tokenWithWrongIssuer);

        assertNull(subject);
    }

    @Test
    @DisplayName("Should return null for token signed with wrong secret")
    void shouldReturnNullForTokenSignedWithWrongSecret() {
        Date fixedTime = new Date(FIXED_TIME_MILLIS);
        Date fixedExpiration = new Date(FIXED_TIME_MILLIS + TWELVE_HOURS_IN_MILLIS);
        String wrongSecret = "wrong-secret";

        String tokenWithWrongSecret = JWT.create()
                .withIssuer("mobiauto-auth-api")
                .withSubject(testUser.getEmail().getValue())
                .withIssuedAt(fixedTime)
                .withExpiresAt(fixedExpiration)
                .sign(Algorithm.HMAC256(wrongSecret));

        String subject = tokenService.validateToken(tokenWithWrongSecret);

        assertNull(subject);
    }

    @Test
    @DisplayName("Should return null for null token")
    void shouldReturnNullForNullToken() {
        String subject = tokenService.validateToken(null);

        assertNull(subject);
    }

    @Test
    @DisplayName("Should return null for empty token")
    void shouldReturnNullForEmptyToken() {
        String subject = tokenService.validateToken("");

        assertNull(subject);
    }

    @Test
    @DisplayName("Should handle different user emails correctly")
    void shouldHandleDifferentUserEmailsCorrectly() {
        User user1 = new User(
                new UserName("Alice", "Johnson"),
                new UserEmail("alice.johnson@example.com"),
                new UserPassword("password123"));
        user1.setId(2L);

        User user2 = new User(
                new UserName("Bob", "Wilson"),
                new UserEmail("bob.wilson@example.com"),
                new UserPassword("password456"));
        user2.setId(3L);

        String token1 = tokenService.generateToken(user1);
        String token2 = tokenService.generateToken(user2);

        String subject1 = tokenService.validateToken(token1);
        String subject2 = tokenService.validateToken(token2);

        assertEquals("alice.johnson@example.com", subject1);
        assertEquals("bob.wilson@example.com", subject2);
        assertNotEquals(subject1, subject2);
    }

    @Test
    @DisplayName("Should handle special characters in email")
    void shouldHandleSpecialCharactersInEmail() {
        User userWithSpecialEmail = new User(
                new UserName("Test", "User"),
                new UserEmail("test+tag@sub-domain.example-site.co.uk"),
                new UserPassword("password123"));
        userWithSpecialEmail.setId(4L);

        String token = tokenService.generateToken(userWithSpecialEmail);
        String subject = tokenService.validateToken(token);

        assertEquals("test+tag@sub-domain.example-site.co.uk", subject);
    }

    @Test
    @DisplayName("Should validate token generated with current secret only")
    void shouldValidateTokenGeneratedWithCurrentSecretOnly() {
        String token1 = tokenService.generateToken(testUser);

        String newSecret = "new-secret-key";
        ReflectionTestUtils.setField(tokenService, "secret", newSecret);

        String token2 = tokenService.generateToken(testUser);

        String subject1 = tokenService.validateToken(token1);
        String subject2 = tokenService.validateToken(token2);

        assertNull(subject1);
        assertEquals(testUser.getEmail().getValue(), subject2);
    }

    @Test
    @DisplayName("Should handle token validation edge cases")
    void shouldHandleTokenValidationEdgeCases() {
        assertNull(tokenService.validateToken(""));
        assertNull(tokenService.validateToken(" "));
        assertNull(tokenService.validateToken("   "));
        assertNull(tokenService.validateToken("not.a.token"));
        assertNull(tokenService.validateToken("header.payload"));
        assertNull(tokenService.validateToken("a.b.c.d"));
        assertNull(tokenService.validateToken("header.payload.signature.extra"));
    }

    @Test
    @DisplayName("Should handle token with future issue date")
    void shouldHandleTokenWithFutureIssueDate() {
        long oneHourInMillis = 3600000L;
        long futureTime = FIXED_TIME_MILLIS + oneHourInMillis;
        String futureToken = JWT.create()
                .withIssuer("mobiauto-auth-api")
                .withSubject(testUser.getEmail().getValue())
                .withIssuedAt(new Date(futureTime))
                .withExpiresAt(new Date(futureTime + TWELVE_HOURS_IN_MILLIS))
                .sign(Algorithm.HMAC256(testSecret));

        String subject = tokenService.validateToken(futureToken);

        assertNull(subject);
    }

    @Test
    @DisplayName("Should validate token structure and algorithm")
    void shouldValidateTokenStructureAndAlgorithm() {
        String token = tokenService.generateToken(testUser);

        DecodedJWT decodedJWT = JWT.decode(token);

        assertEquals("HS256", decodedJWT.getAlgorithm());
        assertEquals("JWT", decodedJWT.getType());

        assertNotNull(decodedJWT.getIssuer());
        assertNotNull(decodedJWT.getSubject());
        assertNotNull(decodedJWT.getIssuedAt());
        assertNotNull(decodedJWT.getExpiresAt());
    }

    @Test
    @DisplayName("Should handle concurrent token generation safely")
    void shouldHandleConcurrentTokenGenerationSafely() throws InterruptedException {
        String[] tokens = new String[3];
        Thread[] threads = new Thread[3];

        for (int i = 0; i < 3; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                tokens[index] = tokenService.generateToken(testUser);
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        for (String token : tokens) {
            assertNotNull(token);
            String subject = tokenService.validateToken(token);
            assertEquals(testUser.getEmail().getValue(), subject);
        }

        for (String token : tokens) {
            String[] parts = token.split("\\.");
            assertEquals(3, parts.length);
        }
    }

    @Test
    @DisplayName("Should throw TokenGenerationException when JWT creation fails")
    void shouldThrowTokenGenerationExceptionWhenJwtCreationFails() {
        ReflectionTestUtils.setField(tokenService, "secret", "");

        TokenGenerationException exception = assertThrows(
                TokenGenerationException.class,
                () -> tokenService.generateToken(testUser));

        assertTrue(exception.getMessage()
                .contains("Failed to generate token for user with email: " + testUser.getEmail().getValue()));
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }
}