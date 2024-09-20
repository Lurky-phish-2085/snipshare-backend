package xyz.lurkyphish2085.snipshare;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestAuthController {

    @GetMapping("/testAuth")
    public ResponseEntity<String> greet(Authentication authentication) {
        return ResponseEntity.ok("Hello, " + authentication.getName() + "!");
    }
}
