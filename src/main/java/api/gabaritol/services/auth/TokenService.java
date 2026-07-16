package api.gabaritol.services.auth;

import api.gabaritol.entities.user.User;
import api.gabaritol.exceptions.raises.TokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@Slf4j
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    private static final String ISSUER = "gabaritol-api";

    public String generateToken(User user) {
        log.info("Generating security token for user: {}", user.getId());
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getId().toString())
                    .withExpiresAt(this.generateExpirationDate())
                    .sign(algorithm);

            log.info("Token successfully generated for user: {}", user.getId());
            return token;

        } catch (JWTCreationException e) {
            log.error("Critical error during token creation for user: {}", user.getId(), e);
            throw new TokenException("Error while authenticating");
        }
    }

    public String validateToken(String token) {
        log.debug("Attempting to validate incoming token");
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String subject = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();

            log.debug("Token validated successfully for subject: {}", subject);
            return subject;

        } catch (JWTVerificationException e) {
            log.warn("Token verification failed: {}", e.getMessage());
            return null;
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(23).toInstant(ZoneOffset.of("-03:00"));
    }
}