package xyz.lurkyphish2085.snipshare.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsServiceImpl userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Get token from the Authorization header
        Optional<String> jwsOptional = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION));
        if (jwsOptional.isPresent()) {
            // Verify token and get user
            String username = jwtService.getAuthenticatedUser(request);

            // load user details from database
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Authenticate
            Authentication authentication =
                new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());

            SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
