package xyz.lurkyphish2085.snipshare.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import xyz.lurkyphish2085.snipshare.common.RestEndpoints;
import xyz.lurkyphish2085.snipshare.user.AppUser;
import xyz.lurkyphish2085.snipshare.user.AppUserDTO;
import xyz.lurkyphish2085.snipshare.user.AppUserRepository;

import java.util.Optional;

@RestController
@RequestMapping(path = RestEndpoints.AUTHENTICATION)
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationController(JwtService jwtService, AuthenticationManager authenticationManager, AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AccountCredentials credentials) {
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

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AccountCredentials credentials) {
        userRepository.findByUsername(credentials.username())
            .ifPresent(user -> {
                throw new IllegalArgumentException("User already exists.");
            });

        AppUser registeredUser =
                new AppUser(
                        credentials.username(),
                        passwordEncoder.encode(credentials.password()),
                        Roles.USER.name()
                );
        userRepository.save(registeredUser);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/validate")
    public ResponseEntity<AppUserDTO> validate(HttpServletRequest request) {
        Optional<String> username = Optional.ofNullable(jwtService.getAuthenticatedUser(request));
        if (username.isEmpty()) {
            throw new IllegalArgumentException("Jwt is invalid or Subject doesn't exist as User.");
        }

        Optional<AppUser> userOptional = userRepository.findByUsername(username.get());
        userOptional.orElseThrow(() -> new IllegalArgumentException("User doesn't exists."));
        AppUser user = userOptional.get();
        String jwts = jwtService.generateToken(user.getUsername());

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwts)
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
                .body(new AppUserDTO(user.getUsername(), user.getRole()));
    }
}