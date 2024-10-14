package xyz.lurkyphish2085.snipshare.snip.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import xyz.lurkyphish2085.snipshare.AuthenticationEntryPointImpl;
import xyz.lurkyphish2085.snipshare.AuthenticationFilter;
import xyz.lurkyphish2085.snipshare.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationFilter authenticationFilter;
    private final AuthenticationEntryPointImpl exceptionHandler;

    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService, AuthenticationFilter authenticationFilter, AuthenticationEntryPointImpl exceptionHandler) {
        this.userDetailsService = userDetailsService;
        this.authenticationFilter = authenticationFilter;
        this.exceptionHandler = exceptionHandler;
    }

    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf((csrf) -> csrf.disable())
            .sessionManagement((session) -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests((authorize) -> {
                authorize
                    .requestMatchers("/login", "/api/v1/snip/**")
                        .permitAll()
                    .anyRequest()
                        .authenticated();
            })
            .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling((handler) -> handler
                    .authenticationEntryPoint(exceptionHandler)
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
        return authConfig.getAuthenticationManager();
    }
}
