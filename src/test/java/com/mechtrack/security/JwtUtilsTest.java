package com.mechtrack.security;

import com.mechtrack.AbstractMechtrackTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestPropertySource(properties = {
    "mechtrack.security.jwt-secret=testSecretKeyThatIsLongEnoughForHS256AlgorithmToWorkProperly123456789",
    "mechtrack.security.jwt-expiration-ms=3600000"
})
class JwtUtilsTest extends AbstractMechtrackTest {

    @Autowired
    private JwtUtils jwtUtils;

    private static final String TEST_USERNAME = "test_user";
    private static final String INVALID_TOKEN = "invalid.token.here";

    @Test
    @DisplayName("Should generate valid JWT token")
    void shouldGenerateValidJwtToken() {
        String token = jwtUtils.generateJwtToken(TEST_USERNAME);
        
        assertThat(token).isNotBlank();
        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    @DisplayName("Should extract username from valid token")
    void shouldExtractUsernameFromValidToken() {
        String token = jwtUtils.generateJwtToken(TEST_USERNAME);
        String extractedUsername = jwtUtils.getUsernameFromJwtToken(token);
        
        assertThat(extractedUsername).isEqualTo(TEST_USERNAME);
    }

    @Test
    @DisplayName("Should extract expiration date from valid token")
    void shouldExtractExpirationDateFromValidToken() {
        String token = jwtUtils.generateJwtToken(TEST_USERNAME);
        Date expirationDate = jwtUtils.getExpirationFromJwtToken(token);
        
        assertThat(expirationDate).isNotNull();
        assertThat(expirationDate).isAfter(new Date());
    }

    @Test
    @DisplayName("Should validate valid token")
    void shouldValidateValidToken() {
        String token = jwtUtils.generateJwtToken(TEST_USERNAME);
        boolean isValid = jwtUtils.validateJwtToken(token);
        
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Should reject invalid token")
    void shouldRejectInvalidToken() {
        boolean isValid = jwtUtils.validateJwtToken(INVALID_TOKEN);
        
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should reject null token")
    void shouldRejectNullToken() {
        boolean isValid = jwtUtils.validateJwtToken(null);
        
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should reject empty token")
    void shouldRejectEmptyToken() {
        boolean isValid = jwtUtils.validateJwtToken("");
        
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should throw exception when extracting username from invalid token")
    void shouldThrowExceptionWhenExtractingUsernameFromInvalidToken() {
        assertThatThrownBy(() -> jwtUtils.getUsernameFromJwtToken(INVALID_TOKEN))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Should throw exception when extracting expiration from invalid token")
    void shouldThrowExceptionWhenExtractingExpirationFromInvalidToken() {
        assertThatThrownBy(() -> jwtUtils.getExpirationFromJwtToken(INVALID_TOKEN))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Should generate tokens with different usernames")
    void shouldGenerateTokensWithDifferentUsernames() {
        String token1 = jwtUtils.generateJwtToken("user1");
        String token2 = jwtUtils.generateJwtToken("user2");
        
        assertThat(token1).isNotEqualTo(token2);
        assertThat(jwtUtils.getUsernameFromJwtToken(token1)).isEqualTo("user1");
        assertThat(jwtUtils.getUsernameFromJwtToken(token2)).isEqualTo("user2");
    }
}
