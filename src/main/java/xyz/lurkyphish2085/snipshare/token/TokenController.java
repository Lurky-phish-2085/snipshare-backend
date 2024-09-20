package xyz.lurkyphish2085.snipshare.token;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
public class TokenController {

    private JwtEncoder jwtEncoder;

    public TokenController(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    @PostMapping("/token")
    public ResponseEntity<String> token(Authentication authentication, HttpServletResponse response) {
        Instant now = Instant.now();
        long expiry = 36000L;

        String scope = authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                                .issuer("self")
                                .issuedAt(now)
                                .expiresAt(now.plusSeconds(expiry))
                                .subject(authentication.getName())
                                .claim("scope", scope)
                                .build();

        return ResponseEntity.ok(this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue());
    }
}
