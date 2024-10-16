package xyz.lurkyphish2085.snipshare.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.lurkyphish2085.snipshare.common.RestEndpoints;

@RestController
@RequestMapping(path = RestEndpoints.AUTHENTICATION)
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationController(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> getToken(@RequestBody AccountCredentials credentials) {
        UsernamePasswordAuthenticationToken creds = new
                UsernamePasswordAuthenticationToken(
                    credentials.username(),
                    credentials.password()
                );

        Authentication auth = authenticationManager.authenticate(creds);

        // Generate token
        String jwts = jwtService.generateToken(auth.getName());

        // Build response with the generated token
        return ResponseEntity
                   .ok()
                   .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwts)
                   .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
                   .build();
    }
}