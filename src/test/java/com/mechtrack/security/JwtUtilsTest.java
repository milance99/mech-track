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
    "mechtrack.security.access-token-expiration-ms=900000",
    "mechtrack.security.refresh-token-expiration-ms=604800000"
})
class JwtUtilsTest extends AbstractMechtrackTest {

    @Autowired
    private JwtUtils jwtUtils;

    private static final String TEST_USERNAME = "test_user";
    private static final String INVALID_TOKEN = "invalid.token.here";

    @Test
    @DisplayName("Should generate valid access token")
    void shouldGenerateValidAccessToken() {
        String token = jwtUtils.generateAccessToken(TEST_USERNAME);
        
        assertThat(token).isNotBlank();
        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    @DisplayName("Should generate valid refresh token")
    void shouldGenerateValidRefreshToken() {
        String token = jwtUtils.generateRefreshToken(TEST_USERNAME);
        
        assertThat(token).isNotBlank();
        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    @DisplayName("Should extract username from valid access token")
    void shouldExtractUsernameFromValidAccessToken() {
        String token = jwtUtils.generateAccessToken(TEST_USERNAME);
        String extractedUsername = jwtUtils.getUsernameFromJwtToken(token);
        
        assertThat(extractedUsername).isEqualTo(TEST_USERNAME);
    }

    @Test
    @DisplayName("Should extract username from valid refresh token")
    void shouldExtractUsernameFromValidRefreshToken() {
        String token = jwtUtils.generateRefreshToken(TEST_USERNAME);
        String extractedUsername = jwtUtils.getUsernameFromJwtToken(token);
        
        assertThat(extractedUsername).isEqualTo(TEST_USERNAME);
    }

    @Test
    @DisplayName("Should extract expiration date from valid access token")
    void shouldExtractExpirationDateFromValidAccessToken() {
        String token = jwtUtils.generateAccessToken(TEST_USERNAME);
        Date expirationDate = jwtUtils.getExpirationFromJwtToken(token);
        
        assertThat(expirationDate).isNotNull();
        assertThat(expirationDate).isAfter(new Date());
    }

    @Test
    @DisplayName("Should extract expiration date from valid refresh token")
    void shouldExtractExpirationDateFromValidRefreshToken() {
        String token = jwtUtils.generateRefreshToken(TEST_USERNAME);
        Date expirationDate = jwtUtils.getExpirationFromJwtToken(token);
        
        assertThat(expirationDate).isNotNull();
        assertThat(expirationDate).isAfter(new Date());
    }

    @Test
    @DisplayName("Should validate valid access token")
    void shouldValidateValidAccessToken() {
        String token = jwtUtils.generateAccessToken(TEST_USERNAME);
        boolean isValid = jwtUtils.validateJwtToken(token);
        
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Should validate valid refresh token")
    void shouldValidateValidRefreshToken() {
        String token = jwtUtils.generateRefreshToken(TEST_USERNAME);
        boolean isValid = jwtUtils.validateRefreshToken(token);
        
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Should reject access token as refresh token")
    void shouldRejectAccessTokenAsRefreshToken() {
        String accessToken = jwtUtils.generateAccessToken(TEST_USERNAME);
        boolean isValid = jwtUtils.validateRefreshToken(accessToken);
        
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should identify token types correctly")
    void shouldIdentifyTokenTypesCorrectly() {
        String accessToken = jwtUtils.generateAccessToken(TEST_USERNAME);
        String refreshToken = jwtUtils.generateRefreshToken(TEST_USERNAME);
        
        assertThat(jwtUtils.getTokenType(accessToken)).isEqualTo("access");
        assertThat(jwtUtils.getTokenType(refreshToken)).isEqualTo("refresh");
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
    @DisplayName("Should generate different access tokens for different usernames")
    void shouldGenerateDifferentAccessTokensForDifferentUsernames() {
        String token1 = jwtUtils.generateAccessToken("user1");
        String token2 = jwtUtils.generateAccessToken("user2");
        
        assertThat(token1).isNotEqualTo(token2);
        assertThat(jwtUtils.getUsernameFromJwtToken(token1)).isEqualTo("user1");
        assertThat(jwtUtils.getUsernameFromJwtToken(token2)).isEqualTo("user2");
    }

    @Test
    @DisplayName("Should generate different refresh tokens for different usernames")
    void shouldGenerateDifferentRefreshTokensForDifferentUsernames() {
        String token1 = jwtUtils.generateRefreshToken("user1");
        String token2 = jwtUtils.generateRefreshToken("user2");
        
        assertThat(token1).isNotEqualTo(token2);
        assertThat(jwtUtils.getUsernameFromJwtToken(token1)).isEqualTo("user1");
        assertThat(jwtUtils.getUsernameFromJwtToken(token2)).isEqualTo("user2");
    }
}
