package xyz.lurkyphish2085.snipshare.token;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<JwtDTO> token(
            @RequestBody LoginSubmissionRequest body,
            HttpServletResponse response
    ) {
        if (!body.username().equals("user") || !body.password().equals("password")) {
            return ResponseEntity.notFound().build();
        }

        Instant now = Instant.now();
        long expiry = 36000L;

//        String scope = authentication.getAuthorities().stream()
//                        .map(GrantedAuthority::getAuthority)
//                        .collect(Collectors.joining(" "));
        String scope = "read";
        JwtClaimsSet claims = JwtClaimsSet.builder()
                                .issuer("self")
                                .issuedAt(now)
                                .expiresAt(now.plusSeconds(expiry))
//                                .subject(authentication.getName())
                                .subject(body.username())
                                .claim("scope", scope)
                                .build();

        String jwtString = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        response.addCookie(new Cookie("jwt", jwtString));

        return ResponseEntity.ok(new JwtDTO(jwtString));
    }
}
