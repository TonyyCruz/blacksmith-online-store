package com.anthony.blacksmithOnlineStore.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.exceptions.InvalidTokenException;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockUser;
import com.anthony.blacksmithOnlineStore.security.TokenService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@Tag("unit")
@DisplayName("Unit test for TokenService")
@ExtendWith(MockitoExtension.class)
class TokenServiceTest {
  private TokenService tokenService;
  private final User user = MockUser.user();

  @BeforeEach
  void setup() {
    tokenService = new TokenService("test-secret");
    ReflectionTestUtils.setField(tokenService, "issuer", "test-issuer");
    ReflectionTestUtils.setField(tokenService, "expirationTime", Duration.ofHours(2));
  }

  @Nested
  @DisplayName("Happy Path")
  class HappyPath {

    @Test
    void generateToken_ShouldCreateValidToken() {
      String token = tokenService.generateToken(user);

      assertNotNull(token);

      DecodedJWT decoded = JWT.decode(token);

      assertEquals(user.getUsername(), decoded.getSubject());
      assertEquals("test-issuer", decoded.getIssuer());
      assertEquals(user.getId().toString(), decoded.getClaim("id").asString());
      assertEquals("CUSTOMER", decoded.getClaim("role").asString());
    }

    @Test
    void decodeToken_ShouldReturnDecodedJwt_WhenValid() {
      String token = tokenService.generateToken(user);

      DecodedJWT decoded = tokenService.decodeToken(token);

      assertEquals(user.getUsername(), decoded.getSubject());
      assertEquals(user.getId().toString(), decoded.getClaim("id").asString());
      assertEquals("CUSTOMER", decoded.getClaim("role").asString());
    }

    @Test
    void generateToken_ShouldSetCorrectExpirationTime() {
      Instant before = Instant.now();
      String token = tokenService.generateToken(user);
      DecodedJWT decoded = JWT.decode(token);
      Instant expiresAt = decoded.getExpiresAt().toInstant();
      Duration diff = Duration.between(before, expiresAt);
      assertTrue(diff.toHours() <= 2 && diff.toHours() >= 1); // tolerância
    }

  }

  @Nested
  @DisplayName("Exception Path")
  class ExceptionPath {

    @Test
    @DisplayName("DecodeToken should throw InvalidTokenException when signature is invalid")
    void decodeToken_ShouldThrowInvalidTokenException_WhenSignatureInvalid() {
      Algorithm wrongAlgorithm = com.auth0.jwt.algorithms.Algorithm.HMAC256("wrong-secret");
      String fakeToken = JWT.create()
          .withIssuer("test-issuer")
          .withSubject(user.getUsername())
          .sign(wrongAlgorithm);

      assertThrows(InvalidTokenException.class, () -> tokenService.decodeToken(fakeToken));
    }

    @Test
    @DisplayName("DecodeToken should throw InvalidTokenException when issuer mismatches")
    void decodeToken_ShouldThrowInvalidTokenException_WhenIssuerMismatch() {
      String token = JWT.create()
          .withIssuer("issuer-errado")
          .withSubject(user.getUsername())
          .sign(Algorithm.HMAC256("test-secret"));

      assertThrows(InvalidTokenException.class, () -> tokenService.decodeToken(token));
    }

    @Test
    @DisplayName("DecodeToken should throw InvalidTokenException when token is expired")
    void decodeToken_ShouldThrowInvalidTokenException_WhenExpired() throws InterruptedException {
      ReflectionTestUtils.setField(tokenService, "expirationTime", Duration.ofSeconds(1));

      String token = tokenService.generateToken(user);

      Thread.sleep(1100);

      assertThrows(InvalidTokenException.class, () -> tokenService.decodeToken(token));
    }

    @Test
    @DisplayName("DecodeToken should throw InvalidTokenException when subject is missing")
    void decodeToken_ShouldThrowInvalidTokenException_WhenMissingSubject() {
      String token = JWT.create()
          .withIssuer("test-issuer")
          .withClaim("id", user.getId().toString())
          .withClaim("role", user.getRole().name())
          .sign(Algorithm.HMAC256("test-secret"));

      assertThrows(InvalidTokenException.class, () -> tokenService.decodeToken(token));
    }

    @Test
    @DisplayName("DecodeToken should throw when subject is blank")
    void decodeToken_ShouldThrowInvalidTokenException_WhenSubjectBlank() {
      String token = JWT.create()
          .withIssuer("test-issuer")
          .withSubject("   ")
          .withClaim("id", user.getId().toString())
          .withClaim("role", user.getRole().name())
          .sign(Algorithm.HMAC256("test-secret"));

      assertThrows(InvalidTokenException.class, () -> tokenService.decodeToken(token));
    }

    @Test
    @DisplayName("DecodeToken should throw InvalidTokenException when claim 'orderId' is missing")
    void decodeToken_ShouldThrowInvalidTokenException_WhenMissingClaimId() {
      String token = JWT.create()
          .withIssuer("test-issuer")
          .withSubject(user.getUsername())
          .withClaim("role", user.getRole().name())
          .sign(Algorithm.HMAC256("test-secret"));

      assertThrows(InvalidTokenException.class, () -> tokenService.decodeToken(token));
    }

    @Test
    @DisplayName("DecodeToken should throw InvalidTokenException when claim 'role' is missing")
    void decodeToken_ShouldThrowInvalidTokenException_WhenMissingClaimRole() {
      String token = JWT.create()
          .withIssuer("test-issuer")
          .withSubject("test-user")
          .withClaim("id", user.getId().toString())
          .sign(Algorithm.HMAC256("test-secret"));

      assertThrows(InvalidTokenException.class, () -> tokenService.decodeToken(token));
    }

    @Test
    @DisplayName("DecodeToken should throw InvalidTokenException when token is malformed")
    void decodeToken_ShouldThrowInvalidTokenException_WhenMalformedToken() {
      String malformed = "this.is.not.a.valid.token";
      assertThrows(InvalidTokenException.class, () -> tokenService.decodeToken(malformed));
    }
  }
}
