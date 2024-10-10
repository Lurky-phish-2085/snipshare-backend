package xyz.lurkyphish2085.snipshare;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtService {


    private static final Long EXPIRATION_TIME = 86400000L; // 1 day in ms.
    private static final String PREFIX = "Bearer";

    // These are test Keys. In production keys should be read from configuration.
    private static final MacAlgorithm ALGORITHM = Jwts.SIG.HS256;
    private static final SecretKey SECRET_KEY = ALGORITHM.key().build();

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    public String getAuthenticatedUser(HttpServletRequest request) {
        Optional<String> tokenOptional = Optional.ofNullable(
                request.getHeader(HttpHeaders.AUTHORIZATION)
            );

       if (tokenOptional.isEmpty()) {
           return null;
       }

       String token = tokenOptional.get();
       Optional<String> user = Optional.ofNullable(
               Jwts.parser()
                   .verifyWith(SECRET_KEY)
                   .build()
                   .parseSignedClaims(token.replace(PREFIX, ""))
                   .getPayload()
                   .getSubject()
           );

        return user.orElse(null);
    }
}
