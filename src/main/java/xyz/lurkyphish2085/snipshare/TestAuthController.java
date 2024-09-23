package xyz.lurkyphish2085.snipshare;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.lurkyphish2085.snipshare.snip.GreetDTO;

@RestController
public class TestAuthController {

    @GetMapping("/testAuth")
    public ResponseEntity<GreetDTO> greet(Authentication authentication) {
        return ResponseEntity.ok(new GreetDTO("Hello, " + authentication.getName() + "!"));
    }
}
